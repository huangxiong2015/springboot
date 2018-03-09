package com.yikuyi.product.strategycache.manager;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yikuyi.product.base.ProductApplicationTestBase;
import com.yikuyi.product.strategy.cache.PackageMailCacheManager;
import com.yikuyi.strategy.model.Strategy;

public class PackageMailCacheManagerTest extends ProductApplicationTestBase{

	@Autowired
	PackageMailCacheManager packageMailCacheManager;
	
	@Test
	public void testRefreshStrategyCacheTask(){
		packageMailCacheManager.refreshStrategyCacheTask();
	}
	
	@Test
	public void testAddStrategyCache(){
		String key = "testKey";
		Strategy strategy = new Strategy();
		strategy.setId("11111");
		List<String> keys = new ArrayList<>();
		keys.add(key);
		packageMailCacheManager.addStrategyCache(keys, strategy);
	}
	
	@Test
	public void testDeleteStrategyCache() {
		String key = "testKey";
		Strategy strategy = new Strategy();
		strategy.setId("11111");
		packageMailCacheManager.deleteStrategyCache(key, strategy);
	}
}
