package com.yikuyi.product.goods.manager;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import com.yikuyi.product.model.VendorWeight;

@Service
public class VendorWeightManager {
	
	private static final Logger logger = LoggerFactory.getLogger(VendorWeightManager.class);
	/**
	 * 缓存名
	 */
	private static final String CACHE_NAME = "vendorWeightCacheName";
	/**
	 * 缓存key		
	 */
	private static final String CACHE_KEY = "vendorWeightKey";	
	@Autowired
	private CacheManager cacheManager;
	
	@Autowired
	private MongoOperations mongoOperations;
	/**
	 * 是否能保存物料，根据供应商的权重来衡量
	 * @param vendorId
	 * @param originVendorId
	 * @return
	 */
	public boolean isCanSaveProductStand(Map<String,Integer> vendorWeightMap,String vendorId,String originVendorId){
		boolean result_b = false;
		//现供应商的权重大于或等于老供应商的权重，或者老供应商没有权重 
		if(!MapUtils.getBooleanValue(vendorWeightMap, originVendorId) || 
				MapUtils.getIntValue(vendorWeightMap, vendorId) >= MapUtils.getIntValue(vendorWeightMap, originVendorId)){
			result_b = true;
		}
		return result_b;
		
	}
	
	/**
	 * 获取所有权重的供应商
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Integer> getVendorWeightMap(){
		Cache cache = cacheManager.getCache(CACHE_NAME);
		ValueWrapper wrapper = cache.get(CACHE_KEY);
		if(wrapper != null){
			return (Map<String, Integer>) wrapper.get();
		}
		List<VendorWeight> weights = mongoOperations.findAll(VendorWeight.class);
		if(CollectionUtils.isEmpty(weights)){
			return MapUtils.EMPTY_MAP;
		}
		Map<String,Integer> map = weights.stream().collect(Collectors.toMap(VendorWeight :: getVendorId, VendorWeight :: getSpuWeight));
		cache.put(CACHE_KEY, map);
		return map;
	}
	
	/**
	 * 刷新供应商权重缓存
	 * @return
	 */
	public void refreshVendorWeightMapCache(){
		Cache cache = cacheManager.getCache(CACHE_NAME);
		List<VendorWeight> weights = mongoOperations.findAll(VendorWeight.class);
		if(CollectionUtils.isEmpty(weights)){
			logger.info("刷新供应商权重缓存异常，数据库没有查询导数据");
			return ;
		}
		Map<String,Integer> map = weights.stream().collect(Collectors.toMap(VendorWeight :: getVendorId, VendorWeight :: getSpuWeight));
		cache.put(CACHE_KEY, map);
	}
}
