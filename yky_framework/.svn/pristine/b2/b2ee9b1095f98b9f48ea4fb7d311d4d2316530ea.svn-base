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

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.access.channel.ChannelDecisionManagerImpl;
import org.springframework.security.web.access.channel.ChannelProcessor;

/**
 * 在spring security模式下，基于nginx，负载均衡对https的支持。需要nginx将原始的scheme信息转发到tomcat后端。
 * 否则会陷入无休止的重定向。
 * 
 * @author liaoke@yikuyi.com
 * @version 1.0.0
 * @see http://stackoverflow.com/questions/8002272/offloading-https-to-load-
 *      balancers-with-spring-security
 */
@Configuration
public class SLBBeanPostProcessor implements BeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (/* behindLoadBalancer && */bean instanceof ChannelDecisionManagerImpl) {
			List<ChannelProcessor> list = new ArrayList<>();
			// list.add(mySecureChannelProcessor);
			// list.add(myInsecureChannelProcessor);
			list.add(new SLBSecureChannelProcessor());
			list.add(new SLBInsecureChannelProcessor());
			((ChannelDecisionManagerImpl) bean).setChannelProcessors(list);
		}
		return bean;
	}

}
