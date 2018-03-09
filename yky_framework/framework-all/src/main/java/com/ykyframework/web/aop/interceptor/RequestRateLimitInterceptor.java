/*
 * Created: 2017年12月13日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
/**
 * 
 */
package com.ykyframework.web.aop.interceptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.net.util.SubnetUtils.SubnetInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * @author liudian@yikuyi.com
 * @version 1.0.0
 */
public class RequestRateLimitInterceptor extends HandlerInterceptorAdapter {
	
	private static final Logger logger = LoggerFactory.getLogger(RequestRateLimitInterceptor.class);

	private Cache<String, AtomicInteger> hoursThroughputCache = CacheBuilder.newBuilder().initialCapacity(128).expireAfterWrite(60, TimeUnit.MINUTES).build();
	
	private Cache<String, AtomicInteger> minuteThroughputCache = CacheBuilder.newBuilder().initialCapacity(128).expireAfterWrite(60, TimeUnit.SECONDS).build();
	
	private int hoursThroughput = 40;
	
	private int throughput = 120;
	
	//ip白名单
	private Set<String> ipWhithList = new HashSet<>();
	
	//子网白名单(192.168.0.1/24的形式)
	private List<SubnetInfo> subnetWhithList = new ArrayList<>();


	public RequestRateLimitInterceptor(int throughput, long period) {
		this(throughput, period, TimeUnit.SECONDS, null);
	}
	
	public RequestRateLimitInterceptor(int throughput, long period, String[] whiteList) {
		this(throughput, period, TimeUnit.SECONDS, whiteList);
	}
	
	public RequestRateLimitInterceptor(int throughput, long period, TimeUnit timeunit, String[] whiteList) {
		if (ArrayUtils.isNotEmpty(whiteList)) {
			Arrays.asList(whiteList).stream().forEach(str -> {
				if (isIP(str)) {
					ipWhithList.add(str);
				}
				else {
					SubnetUtils subnetUtils = new SubnetUtils(str);
					SubnetInfo subnet = subnetUtils.getInfo();
					subnetWhithList.add(subnet);
				}
			});
		}
		this.throughput = throughput;
		minuteThroughputCache = CacheBuilder.newBuilder().initialCapacity(128)
				.expireAfterWrite(period, timeunit).build();
	}
	
	/**
	 * This implementation always returns {@code true}.
	 * <br>
	 * 2次修改.   一分钟内超过120次，关入小黑屋。     一小时超过40次访问，关入小黑屋(一分钟内最多算一次请求)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
		throws Exception {
		String ip = getIpAddr(request);
		if (StringUtils.isBlank(ip)) {
			return false;
		}
		//白名单中存在该ip，或者白名单子网中包含该ip，直接返回true
		if (this.ipWhithList.contains(ip) || this.subnetWhithList.stream().anyMatch(subnetInfo -> subnetInfo.isInRange(ip))) {
			return true;
		}
		//
		AtomicInteger counter = minuteThroughputCache.get(ip, () -> new AtomicInteger(0));
		AtomicInteger hoursCounter = hoursThroughputCache.get(ip, () -> new AtomicInteger(0));
		
		if (counter.incrementAndGet() == 1) {
			minuteThroughputCache.put(ip, counter);
			if(hoursCounter.incrementAndGet()==1){
				hoursThroughputCache.put(ip, hoursCounter);
			}
			if (logger.isDebugEnabled()) {
				logger.debug("流控计数器初始化成功，ip: {} 。", ip);
			}
		}
		
		//流控限制，1分钟大于 XX，本分钟内不允许继续访问
		//小时内访问超过限制，关入小黑屋
		if (counter.get() >= this.throughput || hoursCounter.get() > this.hoursThroughput) {
			logger.info("Ip({})达到流控上限，终止执行，url:{} 。", ip, request.getRequestURI());
			response.setStatus(302);
			response.setHeader("Location",request.getContextPath()+"/ratelimit.htm");
			return false;
		}
		return true;
	}

	/**
	 * 获取访问者IP
	 * 
	 * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
	 * 
	 * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
	 * 如果还不存在则调用Request .getRemoteAddr()。
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Real-IP");
		if (StringUtils.isNotBlank(ip) && isIP(ip)) {
			return ip;
		}
		ip = request.getHeader("X-Forwarded-For");
		if (StringUtils.isNotBlank(ip)) {
			// 多次反向代理后会有多个IP值，第一个为真实IP。
			String[] rIPs = StringUtils.split(ip, ',');

			for (String rIP : rIPs) {
				if (isIP(rIP)) {
					return rIP;
				}
			}
			ip = null;
		}
		if (!isIP(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	private static boolean isIP(String addr) {
		if (StringUtils.isBlank(addr)) {
			return false;
		}
		if (addr.length() < 7 || addr.length() > 15 || "".equals(addr)) {
			return false;
		}
		/**
		 * 判断IP格式和范围
		 */
		String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

		Pattern pat = Pattern.compile(rexp);

		Matcher mat = pat.matcher(addr);

		return mat.find();
	}
}
