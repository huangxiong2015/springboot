package com.yikuyi.product.strategy.cache;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.springboot.config.ObjectMapperHelper;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.yikuyi.packagemail.vo.PackageMailVO;
import com.yikuyi.product.common.dao.BaseMongoClient;
import com.yikuyi.product.vo.ProductVo;
import com.yikuyi.strategy.model.Strategy;
import com.yikuyi.strategy.model.Strategy.StrategyStatus;

@Service
@Transactional
public class PackageMailCacheManager extends AbstactStrategyCacheManager<ProductVo>{
	
	private static final Logger logger = LoggerFactory.getLogger(PackageMailCacheManager.class);
	
	 /**
	  * 特殊供应商商品id缓存名
	  */
	protected static final String PACKAGE_MAIL_PRODUCT_ID_CACHE_NAME = "packageMailProductIdCacheName";

	@Autowired
	private BaseMongoClient mongoClient;
	
	public static final String COLLECTION_STRATEGY = "strategy";
	public static final String COLLECTION_STRATEGY_PRODUCT = "strategy_product";
	
	private static final ObjectMapper mapper = ObjectMapperHelper.configeObjectMapper(new ObjectMapper());
	
	/**
	 * 合并包邮信息
	 * @param productVos
	 */
	@Async
	public Future<Void> mergePackageMailInfo(List<ProductVo> productVos) {
		Map<String, List<Strategy>> strategyMap = this.strategyCacheOps.entries(PACKAGE_MAIL_PRODUCT_ID_CACHE_NAME);
		for (ProductVo productVo : productVos) {
			//从缓存中取商品的包邮策略信息
			List<Strategy> strategies = strategyMap.get(productVo.getId());
			Strategy curentStrategy = null;
			if(CollectionUtils.isNotEmpty(strategies)){
				//迭代依次获取策略
				for (int i = strategies.size()-1; i >= 0 ; i--) {
					Strategy strategy = strategies.get(i);
					if(!StringUtils.isNotEmpty(strategy.getStartDate())&& NumberUtils.isNumber(strategy.getStartDate())){
						continue;
					}
					if(!StringUtils.isNotEmpty(strategy.getEndDate())&& NumberUtils.isNumber(strategy.getEndDate())){
						continue;
					}
					long nowTime = System.currentTimeMillis();
					
					long startTime = Long.valueOf(strategy.getStartDate());
					long endTime = Long.valueOf(strategy.getEndDate());
					//是否在时间区段内的活动
					if(nowTime>=startTime && nowTime<endTime){
						curentStrategy = strategy;
						break;
					}
				}
			}
			if(curentStrategy != null){
				PackageMailVO packageMailInfo = new PackageMailVO();
				packageMailInfo.setStrategyId(curentStrategy.getId());
				packageMailInfo.setIsPackageMail("Y");
				if(StringUtils.isNotEmpty(curentStrategy.getStartDate())){
					packageMailInfo.setStartTime(new Date(Long.valueOf(curentStrategy.getStartDate())));
				}
				if(StringUtils.isNotEmpty(curentStrategy.getEndDate())){
					packageMailInfo.setEndTime(new Date(Long.valueOf(curentStrategy.getEndDate())));
				}
				productVo.setPackageMailInfo(packageMailInfo);
			}
		}
		return new AsyncResult<Void>(null);
	}

	/**
	 * 获取缓存房间名称
	 */
	@Override
	String getCacheRoomName() {
		return PackageMailCacheManager.PACKAGE_MAIL_PRODUCT_ID_CACHE_NAME;
	}
	
	/**
	 * 定时清理过期包邮缓存接口
	 * 1.执行时清理昨天已经过期的策略缓存
	 */
	public void refreshStrategyCacheTask(){
		try {
			refreshStrategyCacheTaskAsync().get();
		} catch (Exception e) {
			logger.error("异步调用定时清理过期包邮缓存错误!e:{}",e);
		}
	}

	@Async
	public Future<Void> refreshStrategyCacheTaskAsync(){
		logger.info("开始定时清理过期包邮缓存!");
		/** 获取三天前的时间**/
		LocalDateTime time = LocalDateTime.now().withNano(0).minusDays(Long.valueOf(3));
		String timeMillis = String.valueOf(Timestamp.valueOf(time).getTime());
		String lastId = StringUtils.EMPTY;
		while(true){
			BasicDBObject param = new BasicDBObject("strategyStatus",StrategyStatus.START.toString())
			.append("endDate", new Document("$gt",timeMillis))
			.append("_id", new Document("$gt",lastId)
					);
			MongoCursor<Document> strategyCursor =  mongoClient.getDatabase().getCollection(COLLECTION_STRATEGY).find(param).limit(1000).iterator();
			
			if(!strategyCursor.hasNext()){
				logger.info("已无待清理的数据!");
				break;
			}else{
				lastId = batchUpdateStrategyCache(strategyCursor);
			}
		}
		logger.info("清理过期包邮缓存完成!");
		return new AsyncResult<Void>(null);
	}

	private String batchUpdateStrategyCache(MongoCursor<Document> strategyCursor) {
		String lastId = StringUtils.EMPTY;
		//过滤过期策略的Map
		Map<String, List<Strategy>> strategiesMap = new HashMap<>();
		List<String> deleteKeys = new ArrayList<>(); 
		while (strategyCursor.hasNext()) {
			Document strategyDoc = strategyCursor.next();
			String startDate = strategyDoc.getString("startDate");
			String endDate = strategyDoc.getString("endDate");
			long nowTime = System.currentTimeMillis();
			if(StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate) ){
				continue;
			}
			//过滤出过期的策略
			if(nowTime < Long.valueOf(startDate) || nowTime > Long.valueOf(endDate)){
				try {
					strategyDoc.put("id",strategyDoc.get("_id"));
					Strategy strategy = mapper.readValue(mapper.writeValueAsString(strategyDoc), Strategy.class);
					//根据策略ID查询商品
					MongoCollection<Document> collection =  mongoClient.getDatabase().getCollection(COLLECTION_STRATEGY_PRODUCT);
					MongoCursor<Document> mongoCursor = collection.find(new Document("strategyId",strategy.getId())).iterator();
					while(mongoCursor.hasNext()){
						Document doc = mongoCursor.next();
						//商品id为策略缓存的key
						String key = doc.getString("productId");
						//根据商品Id先查询redis
						List<Strategy> strategiesCache = strategyCacheOps.get(getCacheRoomName(), key);
						if(CollectionUtils.isEmpty(strategiesCache)){
							deleteKeys.add(key);
						}else{
							strategiesCache.remove(strategy);
							if(CollectionUtils.isEmpty(strategiesCache)){
								deleteKeys.add(key);
							}else{
								strategiesMap.put(key, strategiesCache);
							}
						}
					}
				} catch (Exception e) {
					logger.error("Document conversion to Java Bean Strategy error:{}",e);
				}
			}
			lastId =  strategyDoc.getString("_id");
		}
		//更新redis缓存，如果商品无应用的策略则清除key
		strategiesMap.keySet().stream().forEach(key -> {
				strategyCacheOps.put(getCacheRoomName(), key, strategiesMap.get(key));
		});
		//根据key批量删除策略
		if(CollectionUtils.isNotEmpty(deleteKeys)){
			strategyCacheOps.delete(getCacheRoomName(),deleteKeys.toArray());
		}
		return lastId;
	}
	
}
