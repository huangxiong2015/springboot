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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.wink.server.handlers.AbstractHandler;
import org.apache.wink.server.handlers.MessageContext;

/**
 * 增加跨域支持(CORS)的Handler, 保证部分的REST服务能够直接支持跨域的访问。避免一些跨域的问题
 * @author liaoke@yikuyi.com
 * @version 1.0.0
 * @see com.ykyframework.wink.annotations.ResponseCache
 */
public class CorsSupportHandler extends AbstractHandler {
//	private static final Logger logger = LoggerFactory.getLogger(CorsSupportHandler.class);
	
	@Override
	protected void handleResponse(MessageContext context) throws Throwable {
		super.handleResponse(context);
		this.addCorsHeader(context);
	}
	
	private void addCorsHeader(MessageContext context) {
		HttpServletRequest httpRequest = (HttpServletRequest) context.getAttribute(HttpServletRequest.class);
		String uri = httpRequest.getHeader("Origin".intern());
		if (StringUtils.isNotEmpty(uri)) {
			HttpServletResponse httpResponse = context.getAttribute(HttpServletResponse.class);
			httpResponse.setHeader("Access-Control-Allow-Origin".intern(), uri); 
			httpResponse.setHeader("Access-Control-Allow-Methods".intern(), "GET"); //暂时只开放GET
			httpResponse.setHeader("Access-Control-Allow-Credentials".intern(), "true");
		}
	}
	

}
