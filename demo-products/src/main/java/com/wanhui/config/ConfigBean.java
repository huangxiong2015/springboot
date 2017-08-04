package com.wanhui.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

//通过配置文件给属性赋值
@Configuration
public class ConfigBean {
	
	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
	
}
