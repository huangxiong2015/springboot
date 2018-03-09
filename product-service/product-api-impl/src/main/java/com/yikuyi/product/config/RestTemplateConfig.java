package com.yikuyi.product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.framework.springboot.config.MyRestTemplateConfig;


@Configuration
public class RestTemplateConfig extends MyRestTemplateConfig{
	  @Bean
	  @Override
	    public RestTemplate restTemplate(){
	        return super.restTemplate();
	    }
}