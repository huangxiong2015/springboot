package com.framework.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * application.properties的配置文件不生效（），故在代码里面注入。
 * 
 * @author tangrong@yikuyi.com
 * @version 1.0.0
 */
public class MyJacksonConfig {
	
	@Autowired(required = true)
	public void configeJackson(ObjectMapper objectMapper) {
		ObjectMapperHelper.configeObjectMapper(objectMapper);
	}
}
