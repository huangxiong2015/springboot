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
/**
 * 
 */
package com.framework.springboot.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liudian@yikuyi.com
 * @version 1.0.0
 */
@ConfigurationProperties(prefix = "cache.config.redis")
public class RedisCacheProperties {

	private final Map<String, Long> cacheNames = new HashMap<>();
	
	private final List<String> txExcludes = new ArrayList<>();

	public Map<String, Long> getCacheNames() {
		return cacheNames;
	}

	public void setCacheNames(Map<String, Long> cacheNames) {
		this.cacheNames.putAll(cacheNames);
	}

	/**
	 * @return the txExcludes
	 */
	public List<String> getTxExcludes() {
		return txExcludes;
	}

	/**
	 * @param txExcludes the txExcludes to set
	 */
	public void setTxExcludes(List<String> txExcludes) {
		this.txExcludes.addAll(txExcludes);
	}	
}
