/*
 * Created: 2016年8月9日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ykyframework.security.web.access.channel;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.channel.InsecureChannelProcessor;
import org.springframework.util.Assert;

/**
 * 重写spring security默认的 InsecureChannelProcessor, 支持SLB模式并且兼容默认的模式
 * @author liaoke@yikuyi.com
 * @version 1.0.0
 */
public class SLBInsecureChannelProcessor extends InsecureChannelProcessor {

	@Override
	public void decide(FilterInvocation invocation, Collection<ConfigAttribute> config)
			throws IOException, ServletException {
		Assert.isTrue((invocation != null) && (config != null),
				"Nulls cannot be provided");
		
		for (ConfigAttribute attribute : config) {
			if (supports(attribute)) {
				String scheme = invocation.getHttpRequest().getHeader("X-Forwarded-Proto");
				if ((scheme!= null && scheme.equalsIgnoreCase("https")) ||
						(scheme== null && invocation.getHttpRequest().isSecure())) {
					getEntryPoint().commence(invocation.getRequest(), invocation.getResponse());
				}
			}
		}
	}
}
