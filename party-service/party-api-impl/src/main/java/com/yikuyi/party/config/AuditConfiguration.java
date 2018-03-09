/*
 * Created: 2017年3月16日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.jasig.inspektr.audit.AuditTrailManager;
import org.jasig.inspektr.audit.spi.AuditActionResolver;
import org.jasig.inspektr.audit.spi.AuditResourceResolver;
import org.jasig.inspektr.audit.spi.support.DefaultAuditActionResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.framework.springboot.audit.ParameterAsJsonStringResourceResolver;
import com.framework.springboot.audit.UserInfoPrincipalResolver;
import com.framework.springboot.audit.YkyAuditTrailManagementAspect;
import com.framework.springboot.audit.YkyAuditTrailManager;

@Configuration
@EnableAspectJAutoProxy
public class AuditConfiguration {

	private List<AuditTrailManager> auditTrailManagers;

	private Map<String, AuditActionResolver> auditActionResolverMap;

	private Map<String, AuditResourceResolver> auditResourceResolverMap;

	@Bean
	YkyAuditTrailManager mqAuditTrailManager() {
		return new YkyAuditTrailManager();
	}

	@PostConstruct
	public void setAuditTrailManagers() {
		auditTrailManagers = new ArrayList<>();
		auditTrailManagers.add(mqAuditTrailManager());
	}

	@PostConstruct
	public void setAuditActionResolverMap() {
		auditActionResolverMap = new HashMap<>();
		auditActionResolverMap.put("DEFAULT_ACTION_RESOLVER", new DefaultAuditActionResolver("_SUCCEEDED", "_FAILED"));
	}

	@PostConstruct
	public void setAuditResourceResolverMap() {
		auditResourceResolverMap = new HashMap<>();
		auditResourceResolverMap.put("DEFAULT_RESOURCE_RESOLVER", new ParameterAsJsonStringResourceResolver());
	}

	@Bean
	UserInfoPrincipalResolver auditablePrincipalResolver() {
		return new UserInfoPrincipalResolver();
	}

	@Bean
	YkyAuditTrailManagementAspect ykyAuditTrailManagementAspect() {
		return new YkyAuditTrailManagementAspect("PARTY", auditablePrincipalResolver(), this.auditTrailManagers,
				this.auditActionResolverMap, this.auditResourceResolverMap);
	}
}