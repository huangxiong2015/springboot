package com.yikuyi.product;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

public abstract class BaseClientBuilder {
	
	private <T> T builder(Class<T> apiType, String url) {
		ObjectMapper mapper = new ObjectMapper()
		.setSerializationInclusion(Include.NON_NULL)
		.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
		.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
		.configure(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS, true)
		.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
		.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
		.registerModule(new SimpleModule().addDeserializer(Date.class, new CustomDateTimeDeserializer()));
		return Feign.builder().decoder(new JacksonDecoder(mapper)).encoder(new JacksonEncoder(mapper)).target(apiType, url);
	}

	
	public <T> T builder(Class<T> apiType) {
		return builder(apiType,this.getBaseUrl());
	} 
	
	/**
	 * 基础URL
	 */
	private String baseUrl;
	
	public String getBaseUrl(){
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
}