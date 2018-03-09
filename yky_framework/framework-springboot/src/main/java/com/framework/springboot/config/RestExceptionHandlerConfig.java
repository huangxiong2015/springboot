/*
 * Created: 2017年3月24日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
/**
 * 
 */
package com.framework.springboot.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ykyframework.web.aop.RestControllerAdvice;

/**
 * @author liudian@yikuyi.com
 * @version 1.0.0
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({RestControllerAdvice.class})
public class RestExceptionHandlerConfig {

	@Bean
	public RestControllerAdvice initRestControllerAdvice(){
		return new RestControllerAdvice();
	}
}
