/*
 * Created: 2016年4月26日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ykyframework.interceptor;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ykyframework.wink.annotations.SubmissionToken;
public class SubmissionTokenInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(SubmissionTokenInterceptor.class);

	public static final String _SUBMISSION_TOKEN_NAME = "SPRINGMVC_SUBMISSION_TOKEN";
	

	private String tokenExpirePage; // Token失效的跳转页面

	public void setTokenExpirePage(String tokenExpirePage) {
		this.tokenExpirePage = tokenExpirePage;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws IOException {
		// TODO Auto-generated method stub
		HandlerMethod method = (HandlerMethod) handler;
		SubmissionToken token = method.getMethodAnnotation(SubmissionToken.class);
		if(token==null) return true;
		HttpSession session = request.getSession(true);
		// 生成TOKEN
		if (token.generateToken()) {
			generateToken(method, request);
		}
		// 校验TOKEN
		if (token.checkToken()) {
			String paramToken = request.getParameter(_SUBMISSION_TOKEN_NAME);
			if (paramToken == null) {
				return dispatchToExpirePage(request, response);
			}

			if (!paramToken.equals(session.getAttribute(_SUBMISSION_TOKEN_NAME))) {
				return dispatchToExpirePage(request, response);
			}
			session.removeAttribute(_SUBMISSION_TOKEN_NAME);
		}
		return true;
	}

	private boolean dispatchToExpirePage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.sendRedirect(request.getContextPath() + tokenExpirePage);
		return false;
	}

	private void generateToken(HandlerMethod method, HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		String key = "";
		key = UUID.randomUUID().toString();
		session.setAttribute(_SUBMISSION_TOKEN_NAME, key);
		request.setAttribute(_SUBMISSION_TOKEN_NAME, key);
	}
}
