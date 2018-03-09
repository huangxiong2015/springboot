/*
 * Created: 2017年3月22日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ictrade.common.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class MyObjectMapper extends ObjectMapper {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3119711730753237956L;

	public void init() {
		this.setSerializationInclusion(Include.NON_NULL);
		this.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

		this.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
		this.configure(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS, true);
		this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		this.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
		SimpleModule simpleModule = new SimpleModule().addDeserializer(Date.class, new CustomDateTimeDeserializer());
		this.registerModule(simpleModule);

	}

}
