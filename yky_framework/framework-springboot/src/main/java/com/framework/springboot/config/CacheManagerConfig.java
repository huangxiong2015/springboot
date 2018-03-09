/*
 * Created: 2017年3月21日
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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author liudian@yikuyi.com
 * @version 1.0.0
 */
@EnableConfigurationProperties(RedisCacheProperties.class)
@Configuration
public class CacheManagerConfig extends CacheConfig {

	@Autowired
	private RedisCacheProperties cacheProperties;
		
	@Autowired
	private RedisCacheManager redisCacheManager;
	
	@Autowired
	private RedisCacheManager redisCacheManagerNoTransaction;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public RedisTemplate redisTemplate(RedisConnectionFactory fa) {
		RedisTemplate template = new RedisTemplate();
		template.setConnectionFactory(fa);
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		return template;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public RedisTemplate redisTemplateTransaction(RedisConnectionFactory fa) {
		RedisTemplate template = new RedisTemplate();
		template.setConnectionFactory(fa);
		template.setKeySerializer(new StringRedisSerializer());
		template.setEnableTransactionSupport(true);
		template.setHashKeySerializer(new StringRedisSerializer());
		return template;
	}

	@SuppressWarnings({ "rawtypes" })
	protected RedisCacheManager getRedisCacheManager(RedisTemplate redisTemplate, List<String> cacheNames,
			boolean transactionAware) {
		RedisCacheManager rCacheManager = new RedisCacheManager(redisTemplate, cacheNames);
		rCacheManager.setDefaultExpiration(300);
		rCacheManager.setUsePrefix(true);
		rCacheManager.setTransactionAware(transactionAware);
		rCacheManager.setExpires(cacheProperties.getCacheNames());
		return rCacheManager;
	}

	@SuppressWarnings({ "rawtypes" })
	@Bean
	RedisCacheManager redisCacheManager(RedisTemplate redisTemplate) {
		// Transactional cache
		List<String> allTxCache = this.cacheProperties.getCacheNames().keySet().stream()
				.filter(key -> !this.cacheProperties.getTxExcludes().contains(key)).collect(Collectors.toList());
		return getRedisCacheManager(redisTemplate, allTxCache, true);
	}

	@SuppressWarnings({ "rawtypes" })
	@Bean
	RedisCacheManager redisCacheManagerNoTransaction(RedisTemplate redisTemplate) {
		// no transaction cache
		List<String> noTxCache = this.cacheProperties.getCacheNames().keySet().stream()
				.filter(key -> this.cacheProperties.getTxExcludes().contains(key)).collect(Collectors.toList());
		return getRedisCacheManager(redisTemplate, noTxCache, false);
	}
	
	/* (non-Javadoc)
	 * @see com.framework.springboot.config.CacheConfig#myCacheManager()
	 */
	@Override
	public List<CacheManager> myCacheManager() {
		return Arrays.asList(this.redisCacheManagerNoTransaction, this.redisCacheManager);
	}
}