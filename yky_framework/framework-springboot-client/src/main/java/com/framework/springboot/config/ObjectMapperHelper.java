package com.framework.springboot.config;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class ObjectMapperHelper {
	
	private ObjectMapperHelper() {
		super();
	}
	
	public static final ObjectMapper configeObjectMapper(ObjectMapper objectMapper) {
		SimpleModule simpleModule = new SimpleModule().addDeserializer(Date.class, new CustomDateTimeDeserializer());
		return objectMapper.setSerializationInclusion(Include.NON_NULL)
				.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
				.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
				.configure(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS, true)
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
				.registerModule(simpleModule);
	}

}
