/*
 * Created: 2016年10月12日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.framewrok.springboot.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.framework.springboot.model.LoginUser;

/**
 * 将当前登录用户的信息塞入请求上下文的Interceptor
 * @author liaoke@yikuyi.com
 * @version 1.0.0
 */
public class LoginUserInjectionInterceptor extends HandlerInterceptorAdapter {
	public static final String LOGIN_USER_KEY = "_LOGIN_USER_KEY_";
	private final Logger logger = LoggerFactory.getLogger(LoginUserInjectionInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal = auth.getPrincipal();
		logger.debug(" begin to get userInfo from request resttemplate header .");
		if (principal instanceof LoginUser) {
			LoginUser user = (LoginUser)auth.getPrincipal();
			logger.debug("user info [ userId:"+user.getId()+", userName:"+user.getUsername()+" ]");
			RequestContextHolder.currentRequestAttributes().setAttribute(LOGIN_USER_KEY, user, RequestAttributes.SCOPE_REQUEST);
		}else{
        	logger.debug("user is null  call will failed if local service request userinfo .");
        }
		return super.preHandle(request, response, handler);
	}

}
