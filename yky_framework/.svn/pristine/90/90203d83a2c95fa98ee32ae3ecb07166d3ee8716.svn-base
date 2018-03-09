package com.framework.springboot.config;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MyRestTemplateConfig {

	public RestTemplate restTemplate() {
		MyClientHttpRequestFactory factory = new MyClientHttpRequestFactory();
		factory.setReadTimeout(5000);// ms
		factory.setConnectTimeout(45000);// ms
		
		final RestTemplate restTemplate = new RestTemplate(factory);
		

		// find and replace Jackson message converter with our own
		for (int i = 0; i < restTemplate.getMessageConverters().size(); i++) {
			final HttpMessageConverter<?> httpMessageConverter = restTemplate.getMessageConverters().get(i);
			if (httpMessageConverter instanceof MappingJackson2HttpMessageConverter) {
				ObjectMapper objectMapper = ((MappingJackson2HttpMessageConverter) httpMessageConverter).getObjectMapper();
				ObjectMapperHelper.configeObjectMapper(objectMapper);
			}
		}
		return restTemplate;
	}

}
