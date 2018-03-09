/*
 * Created: 2016年4月27日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.framework.springboot.audit;

import org.aspectj.lang.JoinPoint;
import org.jasig.inspektr.common.spi.PrincipalResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.framework.springboot.model.LoginUser;
import com.framewrok.springboot.web.RequestHelper;

public class UserInfoPrincipalResolver implements PrincipalResolver {

	private static final Logger logger = LoggerFactory.getLogger(YkyAuditTrailManagementAspect.class);
	
	static final String UNLOGIN_USER = "audit:unlogin";

	@Override
	public String resolveFrom(JoinPoint auditTarget, Object returnValue) {
		return getUserName();
	}

	@Override
	public String resolveFrom(JoinPoint auditTarget, Exception exception) {
		return getUserName();
	}

	@Override
	public String resolve() {
		return getUserName();
	}

	private String getUserName() {
		try {
			final LoginUser user = RequestHelper.getLoginUser();
			if (user == null) {
				return UNLOGIN_USER;
			}
			return user.getId();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return UNLOGIN_USER;
	}
}