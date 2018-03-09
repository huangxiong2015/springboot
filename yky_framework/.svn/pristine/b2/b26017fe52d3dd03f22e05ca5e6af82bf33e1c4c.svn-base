/*
 * Created: 2016年3月29日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ykyframework.cache;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

@CacheConfig(cacheNames={"accountCache"})
public interface AccountDAO {
	

//	@Cacheable(value="accountCache", key="#acc.getId().toString().concat('.SUFFIX')+1", condition="#acc.getId() > 1"  )// 使用了一个缓存名叫 accountCache
//	@Cacheable(value = "accountCache", key="T(org.springframework.cache.interceptor.SimpleKeyGenerator).generateKey(#acc)")
	
//	@Cacheable(value="accountCache", cacheManager="cacheManager", key="#acc.getId().toString() + #acc.getName() + '.SUFFIX'", condition="#acc.getId() > 1 && #acc.getId() <= 3"  )// 使用了一个缓存名叫 accountCache
	@Cacheable(value="accountCache", key="#id")// 使用了一个缓存名叫 accountCache
	public Account getAccountById(Integer id);
	
//	@Caching( put = {@CachePut(value="accountCache", key="#account.id")},evict = {@CacheEvict(value="accountCache", key="#account.id")})
	@CachePut(value="accountCache", key="#account.id")
	public Account saveAccount(Account account);
	
	@CacheEvict(value="accountCache", key="#id")
	public void deleteAccountById(Integer id);

}
