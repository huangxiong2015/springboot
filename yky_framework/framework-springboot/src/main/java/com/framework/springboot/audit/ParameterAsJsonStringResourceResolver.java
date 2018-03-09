/*
 * Created: 2016年4月28日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.framework.springboot.audit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.jasig.inspektr.audit.spi.support.AbstractAuditResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ParameterAsJsonStringResourceResolver extends AbstractAuditResourceResolver {

	private static final Logger logger = LoggerFactory.getLogger(ParameterAsJsonStringResourceResolver.class);

	@Override
	protected String[] createResource(Object[] args) {
		List<String> stringArgs = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		for (final Object arg : args) {
			String json = "";
			try {
				json = mapper.writeValueAsString(arg);
			} catch (Exception e) {
				logger.error("ParameterAsJsonStringResourceResolver createResource object2Json is error:{}",
						e.getMessage(), e);
			}
			stringArgs.add(json);
		}
		return stringArgs.toArray(new String[stringArgs.size()]);
	}
}