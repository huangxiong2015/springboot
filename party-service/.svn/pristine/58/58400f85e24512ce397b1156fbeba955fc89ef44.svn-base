/*
 * Created: 2017年3月1日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.base;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.framework.springboot.config.CustomDateTimeDeserializer;

@TestConfiguration
public class ApplicationTestConfig {
	
	/**
	 * 
	 * @return
	 * @since 2017年2月22日
	 * @author liudian@yikuyi.com
	 */
	@Bean
	public RestTemplateBuilder restTemplateBuilder() {
		RestTemplateBuilder builder = new RestTemplateBuilder();
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		ObjectMapper om = new ObjectMapper();
		om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		SimpleModule simpleModule = new SimpleModule().addDeserializer(Date.class, new CustomDateTimeDeserializer());
		om.registerModule(simpleModule);
		converter.setObjectMapper(om);
		return builder.additionalMessageConverters(converter).basicAuthorization("admin", "9999999901");
	}
}
