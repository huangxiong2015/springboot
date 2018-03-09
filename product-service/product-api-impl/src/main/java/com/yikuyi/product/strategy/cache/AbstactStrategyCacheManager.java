package com.yikuyi.product.strategy.cache;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.redis.core.HashOperations;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.yikuyi.strategy.model.Strategy;

public abstract class AbstactStrategyCacheManager<T> {
	
	@Resource(name = "redisTemplate")
	protected HashOperations<String,String,List<Strategy>> strategyCacheOps;
	
	/**
	 * 缓存房间名称
	 * @return 名称
	 */
	abstract String getCacheRoomName();
	
	/**
	 * 生成缓存key
	 * @param content 动态json条件
	 * @return 缓存key
	 */
	public static String generateCacheKey(JSONObject content){
		Collection<Object> conditions = content.values();
		StringBuilder sb = new StringBuilder();
		for (Object cond : conditions) {
			sb.append(cond.toString()).append("-");
		}
		return sb.substring(0, sb.lastIndexOf("-")).toString();
	}
	
	/**
	 * 新增策略缓存
	 * @author zr.wanghong
	 * @param key 缓存key
	 * @param strategy 策略实体
	 */
	public void addStrategyCache(List<String> keys,Strategy strategy){
		if(CollectionUtils.isNotEmpty(keys)){
			keys.stream().forEach(key -> {
				List<Strategy> strategies = strategyCacheOps.get(getCacheRoomName(), key);
				if(CollectionUtils.isNotEmpty(strategies)){
					this.deleteStrategyCache(key,strategy);
				}
				strategies = Lists.newArrayList();
				strategies.add(strategy);
				strategyCacheOps.put(getCacheRoomName(),key,strategies);
			});
		}
		
	}
	
	/**
	 * 删除策略缓存
	 * @param key 缓存key
	 * @param strategy 策略实体
	 */
	public void deleteStrategyCache(String key,Strategy strategy){
		List<Strategy> strategies = strategyCacheOps.get(getCacheRoomName(),key);
        if(CollectionUtils.isEmpty(strategies)){
        	return;
        }
        strategies.remove(strategy);
        if(CollectionUtils.isEmpty(strategies)){
        	strategyCacheOps.delete(getCacheRoomName(),key);
        }else{
        	strategyCacheOps.put(getCacheRoomName(),key,strategies);
        }
	}
}