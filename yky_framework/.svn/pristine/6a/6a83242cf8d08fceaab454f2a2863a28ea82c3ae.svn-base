package com.framework.springboot.config;
/*
 * Created: 2016年10月11日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.framewrok.springboot.web.LoginUserInjectionInterceptor;
import com.ykyframework.web.aop.RestExceptionHandleAdvice;

/**
 * WEB MVC 的通用配置
 * @author liaoke@yikuyi.com
 * @version 1.0.0
 */

public class MyWebMvcConfig extends WebMvcConfigurerAdapter {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		
		registry.addMapping("/**")
        .allowedOrigins("*")
        .allowedMethods("*")
        .allowCredentials(true).maxAge(3600);
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//将LoginUser拦截器注入到所有的REST服务请求
		registry.addInterceptor(new LoginUserInjectionInterceptor()).excludePathPatterns("/v2/api-docs").addPathPatterns("/v*/**");
		super.addInterceptors(registry);
	}
	
	/**
	 * 注册rest 异常处理的 advice
	 * @return
	 * @since 2016年10月21日
	 * @author liaoke@yikuyi.com
	 */
	@Bean
	public RestExceptionHandleAdvice restExceptionHandleAdvice() {
		return new RestExceptionHandleAdvice();
	}

	
}
