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

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.framework.springboot.model.LoginUser;
import com.ykyframework.context.RuntimeContextHolder;
import com.ykyframework.mqservice.MQConstants;

/**
 * 用户请求的Helper类，用于一些基于线程上下文的工具方法。
 * @author liaoke@yikuyi.com
 * @version 1.0.0
 */
public class RequestHelper {
	
	private static final Logger logger = LoggerFactory.getLogger(RequestHelper.class);

	private RequestHelper() {
	}

	public static final LoginUser getLoginUser() {
		LoginUser loginUser = null;
		try {
			loginUser = (LoginUser) RequestContextHolder.currentRequestAttributes()
					.getAttribute(LoginUserInjectionInterceptor.LOGIN_USER_KEY, RequestAttributes.SCOPE_REQUEST);
		} catch (Exception exp) {
			//如果不是在Request的上下文中，可能是系统后台消息消费上下文，需要取MQ消费者上下文的用户信息
			logger.debug("Get loginuser in context error, caused by:", exp);
			if (RuntimeContextHolder.currentContextAttributes().isContextActive()) {
				String user = (String)RuntimeContextHolder.currentContextAttributes().get(MQConstants.MSG_HEADER_USER_NAME);
				String pass = (String)RuntimeContextHolder.currentContextAttributes().get(MQConstants.MSG_HEADER_PASS_WORD);
				if (StringUtils.isNotBlank(user) && StringUtils.isNotBlank(pass)) {
					loginUser = new LoginUser(pass, user, pass, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
				}
			}
			logger.debug("Get from runtime context {}", loginUser);
		}
		return loginUser;
	}
	
	/**
	 * 返回登录用户ID
	 * @return
	 * @since 2016年10月12日
	 * @author liaoke@yikuyi.com
	 */
	public static final String getLoginUserId() {
		LoginUser user = getLoginUser();
		return user != null ? user.getId() : null;
	}
	
}