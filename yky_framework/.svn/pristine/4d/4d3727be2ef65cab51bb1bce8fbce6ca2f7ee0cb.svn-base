/*
 * Created: 2016年5月17日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ykyframework.web.aop.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

import com.ykyframework.exception.InvalidDataException;

/**
 * 校验客户端的REST服务器请求是否合法的的拦截器。校验包括 Referer, User Agent
 * @author liaoke@yikuyi.com
 * @version 1.0.0
 */
public class RestRequestValidateInterceptor implements WebRequestInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(RestRequestValidateInterceptor.class);

	@Override
	public void preHandle(WebRequest request) throws Exception {
		//如果是空的referer，则出错
		//TODO 后续要判断是否是同一个domain的
		if (StringUtils.isEmpty(request.getHeader("Referer"))) {
			throw new InvalidDataException("invalid http header param.");
		}
		
		//非法的user agent
		//我们发现攻击者是使用 Mozilla/5.0 这个User agent，但是正常的一般形如 Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:2.0b13pre) Gecko/20110307 Firefox/4.0b13 或者 Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30
		String userAgent = request.getHeader("User-Agent");
		if (userAgent == null || userAgent.length() < 20) {
			throw new InvalidDataException("invalid http header param.");
		}

	}

	@Override
	public void postHandle(WebRequest request, ModelMap model) throws Exception {

	}

	@Override
	public void afterCompletion(WebRequest request, Exception ex) throws Exception {
//		logger.info("RestRequestValidateInterceptor afterCompletion");
	}

}
