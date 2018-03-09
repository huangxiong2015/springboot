package com.yikuyi.party.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.springboot.config.ObjectMapperHelper;

import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

public abstract class BaseClient {

	/**
	 * 基础URL
	 */
	private String baseUrl;

	private <T> T builder(Class<T> apiType, String url) {
		ObjectMapper mapper = ObjectMapperHelper.configeObjectMapper(new ObjectMapper());
		return Feign.builder().decoder(new JacksonDecoder(mapper)).encoder(new JacksonEncoder(mapper)).retryer(Retryer.NEVER_RETRY).options(new Request.Options(45000, 5000)).target(apiType, url);
	}

	private <T> T basicBuilder(Class<T> apiType, String url) {
		ObjectMapper mapper = ObjectMapperHelper.configeObjectMapper(new ObjectMapper());
		return Feign.builder().encoder(new JacksonEncoder(mapper)).retryer(Retryer.NEVER_RETRY).options(new Request.Options(45000, 5000)).target(apiType, url);
	}

	public <T> T builder(Class<T> apiType) {
		return builder(apiType, this.getBaseUrl());
	}

	/**
	 * 后面参数只是一个简单标记，采用apacheHttpClinet
	 * 
	 * @param apiType
	 * @param rstStringType
	 * @return
	 */
	public <T> T builder(Class<T> apiType, Class<String> rstStringType) {
		return basicBuilder(apiType, this.getBaseUrl());
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
}