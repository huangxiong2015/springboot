/*
 * Created: 2016年1月30日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ykyframework.web.sso;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SSO单点登出的servlet，不使用@Controller模式，怕反复进入cas sso的filter
 * @author liaoke@yikuyi.com
 * @version 1.0.0
 */
public class LogoutServlet extends HttpServlet {
	private static final Logger logger = LoggerFactory.getLogger(LogoutServlet.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 8019201819378216122L;
	
//	/**
//	 * SSO CAS服务器的上下文跟
//	 */
//	private static String casServerUrlPrefix;

//	@Override
//	public void init(ServletConfig config) throws ServletException {
//		//初始化的时候设置SSO CAS的上下文根
//		casServerUrlPrefix = config.getServletContext().getFilterRegistration("CAS Validation Filter").getInitParameter("casServerUrlPrefix");
//	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
		HttpSession session = req.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		
		// 跳转到上下文根的URL路径上
		// TODO 跳转到登录页面上
		// TODO 反向代理可能导致上下文跟被隐藏，要优化(使用数据字典)。例如 实际的URL是 http://localhost/mainweb，但是发布到服务器的可能是 http://localhost/，那么跳转出来的又变成了http://localhost
		String requestUrl = req.getRequestURL().toString();
		String contextRoot = req.getServletContext().getContextPath();
		String homeUrl = requestUrl.substring(0, requestUrl.indexOf(contextRoot)) + contextRoot + "/";
		
		;
		
		//跳转到SSO LOGOUT
		//String logoutUrl = casServerUrlPrefix + "/logout?service=" + URLEncoder.encode(homeUrl, "UTF-8");
		//String logoutUrl = casServerUrlPrefix + "/logout?service=" + URLEncoder.encode(casServerUrlPrefix + "/login?service=" + URLEncoder.encode(homeUrl, "UTF-8"), "UTF-8");
		try {
			resp.sendRedirect(homeUrl);
		} catch (IOException e) {
			logger.error("failed to redirect {}", homeUrl, e);
		}
	}
}
