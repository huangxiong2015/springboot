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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.ykyframework.exception.SystemException;

/**
 * @author liudian@yikuyi.com
 * @version 1.0.0
 */
@Configuration
@EnableCaching
@ConfigurationProperties(prefix = "cache.config")
public abstract class CacheConfig extends CachingConfigurerSupport {
	
	/**
	 * 是否跳过cache初始化后的测试,默认测试，如果需要跳过测试，在properties中设置如下值：
	 * cache.config.skipTestBefore=true
	 */
	private boolean skipTestBefore;
	
	/**
	 * @return the skipTestBefore
	 */
	public final boolean isSkipTestBefore() {
		return skipTestBefore;
	}

	/**
	 * @param skipTestBefore the skipTestBefore to set
	 */
	public final void setSkipTestBefore(boolean skipTestBefore) {
		this.skipTestBefore = skipTestBefore;
	}

	/**
	 * @author liudian@yikuyi.com
	 * @version 1.0.0
	 */
	static final class MyCompositeCacheManager extends CompositeCacheManager {
		
		private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>(16);
		
		@Override
		public Cache getCache(String name) {
			Cache cache = this.cacheMap.get(name);
			if (cache != null) {
				return cache;
			}
			final Cache orginalCache = super.getCache(name);
			if (orginalCache == null) {
				return orginalCache;
			}
			synchronized (this.cacheMap) {
				cache = this.cacheMap.get(name);
				if (cache != null) {
					return cache;
				}
				Class<?>[] interfaces = new Class[]{Cache.class};
				cache = (Cache)Proxy.newProxyInstance(orginalCache.getClass().getClassLoader(), interfaces, new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						Object result;
						try{
							result = method.invoke(orginalCache, args);
						}
						catch(Exception t) {							
							if ("get".equals(method.getName())){
								//如果get不到或出错，返回null
								result = null;
							}
							else {
								//其它抛出异常
								throw t;
							}
						}
						return result;
					}					
				});
				this.cacheMap.put(name, cache);
				
			}
			return cache;
		}
	}

	public abstract List<CacheManager> myCacheManager();
	
	@Override
	@Primary
	@Bean
	public CacheManager cacheManager(){		
		List<CacheManager> otherCacheMangers = this.myCacheManager();
		CompositeCacheManager cacheManager = new MyCompositeCacheManager();
		cacheManager.setFallbackToNoOpCache(false);
		cacheManager.setCacheManagers(otherCacheMangers);
		//测试cache是否正常使用
		if (!this.skipTestBefore) {
			this.doTest(Arrays.asList(cacheManager));
		}
		return cacheManager;
	}
	
	private void doTest(List<CacheManager> otherCacheMangers) {
		for (CacheManager cacheManager : otherCacheMangers) {
			Collection<String> caches = cacheManager.getCacheNames();
			if (CollectionUtils.isNotEmpty(caches)) {				
				String[] names = caches.toArray(new String[0]);
				Cache cache = cacheManager.getCache(names[0]);
				if (!this.testCache(cache)) {
					throw new SystemException("Cache " + cache + " error with test!");
				}
			}
		}
	}	
	
	private boolean testCache(Cache cache) {
		try{
			String key = "CACHE_INIT_TEST_KEY:" + RandomStringUtils.randomAlphabetic(10) + "." + RandomUtils.nextLong();
			cache.put(key, key);
			Object value = cache.get(key, String.class);
			if (!key.equals(value)){
				return false;
			}
			cache.evict(key);
			value = cache.get(key, String.class);
			//如果获取的值不同则返回true，否则false
			return !key.equals(value);
		}
		catch (Exception exp) {
			throw new SystemException(exp);
		}
	}
}