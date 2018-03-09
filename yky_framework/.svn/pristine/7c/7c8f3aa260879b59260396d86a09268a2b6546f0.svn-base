/*
 * Created: 2015年12月21日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2015 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ykyframework.wink.handlers;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.SerializationUtils;
import org.apache.wink.server.handlers.AbstractHandler;
import org.apache.wink.server.handlers.MessageContext;
import org.apache.wink.server.internal.handlers.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ykyframework.wink.annotations.ResponseCache;

/**
 * 设置wink REST服务的浏览器缓存的 handler.
 * @author liaoke@yikuyi.com
 * @version 1.0.0
 * @see com.ykyframework.wink.annotations.ResponseCache
 */
public class SetHttpRespCacheHandler extends AbstractHandler {
	private static final Logger logger = LoggerFactory.getLogger(SetHttpRespCacheHandler.class);
	
	private final static CacheControl NO_CACHE = new CacheControl();
	
	@Override
	public void init(Properties props) {
		NO_CACHE.setNoCache(true);
		NO_CACHE.setNoStore(true);
		//NO_CACHE.setNoTransform(true);
	}

	@Override
	protected void handleResponse(MessageContext context) throws Throwable {
		final HttpServletResponse httpResponse = context.getAttribute(HttpServletResponse.class);
		//返回200才增加缓存
		if (context.getResponseStatusCode() == Status.OK.getStatusCode()) {
			ResponseCache rc = this.getResponseCache(context);
			if (rc == null) {
				this.setNoCache(httpResponse);
			} 
//			else if (rc.expireAt() != null ) {
//				//set expires的缓存
//				this.setExpireCache(httpResponse, rc);
//			} 
			else if (rc.eTagEnable()) {
				//set eTag缓存
				this.setETagCache(context, httpResponse);
			} else {
				//set 标准的cache control
				this.setFixedCache(httpResponse, rc);
			}
			
			
		}
	}
	
	private ResponseCache getResponseCache(MessageContext context) {
		Method javaMethod = null;
		SearchResult searchResult = context.getAttribute(SearchResult.class);
		javaMethod = searchResult.getMethod().getMetadata().getReflectionMethod();
		
		return javaMethod.getAnnotation(ResponseCache.class);
	}
	
	/**
	 * 不设置缓存
	 * @param httpResponse
	 * @author liaoke@yikuyi.com
	 */
	private void setNoCache(HttpServletResponse httpResponse) {
		httpResponse.setHeader(HttpHeaders.CACHE_CONTROL.intern(), NO_CACHE.toString());
		httpResponse.setDateHeader(HttpHeaders.EXPIRES.intern(), 1L);
	}
	
	/**
	 * 设置固定时间的缓存
	 * @param httpResponse
	 * @author liaoke@yikuyi.com
	 */
	private void setFixedCache(HttpServletResponse httpResponse, ResponseCache rc) {
		CacheControl cacheControl = new CacheControl();
		cacheControl.setMaxAge(((rc.day() * 24 + rc.hour()) * 60 + rc.minute()) * 60 + rc.second());
		httpResponse.setHeader(HttpHeaders.CACHE_CONTROL.intern(), cacheControl.toString());
		
		long now = System.currentTimeMillis();
		long expire = (((rc.day() * 24 + rc.hour()) * 60 + rc.minute()) * 60 + rc.second()) * 1000 + now;
		httpResponse.setDateHeader(HttpHeaders.EXPIRES.intern(), expire);
	}
	
	//TODO expire是不是要做呢？这个貌似只能从程序来设置，不是固定的，再看看
	private void setExpireCache(HttpServletResponse httpResponse, ResponseCache rc) {
		
	}
	
	/**
	 * 设置eTag的缓存
	 * @param context
	 * @param httpResponse
	 * @author liaoke@yikuyi.com
	 */
	private void setETagCache(MessageContext context, HttpServletResponse httpResponse) {
		// 1. 获取response的entity，并计算成MD5码生成etag value
		Object respBody = context.getResponseEntity();
		if (!(respBody instanceof Serializable)) { //必须是序列化的接口实现才处理
			return;
		}
		
		byte[] byteArray = SerializationUtils.serialize((Serializable)respBody);
		String newETagValue = DigestUtils.md5Hex(byteArray);
		
		// 2. 获取request的If-None-Match的header值
		List<String> etagHeaders = context.getHttpHeaders().getRequestHeader(HttpHeaders.IF_NONE_MATCH);
		if (etagHeaders == null) {
			httpResponse.setHeader(HttpHeaders.ETAG, newETagValue);
			return;
		}
		String eTagValue = etagHeaders.get(0);
		
		// 3. 判断request的If-None-Match的header值是否和计算出来的etag值相等，如果是的话返回304
		if (newETagValue.equals(eTagValue)) {
			context.setResponseStatusCode(Status.NOT_MODIFIED.getStatusCode());
			context.setResponseEntity(null);
		} else {
			httpResponse.setHeader(HttpHeaders.ETAG, newETagValue);
		}
	}

}
