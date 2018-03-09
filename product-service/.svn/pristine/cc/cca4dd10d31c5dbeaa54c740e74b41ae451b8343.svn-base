//package com.yikuyi.product.strategy.cache;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.Future;
//
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.commons.lang.StringUtils;
//import org.apache.commons.lang.math.NumberUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.AsyncResult;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.yikuyi.product.vo.ProductVo;
//import com.yikuyi.strategy.model.Strategy;
//import com.yikuyi.strategy.vo.LimitedPurchaseVo;
//
//@Service
//@Transactional
//public class LimitedPurchaseCacheManager extends AbstactStrategyCacheManager<ProductVo>{
//	
//	private static final Logger logger = LoggerFactory.getLogger(LimitedPurchaseCacheManager.class);
//	
//	 /**
//	  * 特殊供应商商品id缓存名
//	  */
//	protected static final String PACKAGE_MAIL_PRODUCT_ID_CACHE_NAME = "packageMailProductIdCacheName";
//	
//	/**
//	 * 合并是否限购标识
//	 * @param productVos
//	 */
//	@Async
//	public Future<Void> mergeLimitedPurchaseInfo(List<ProductVo> productVos) {
//		logger.debug("合并限购标识");
//		Map<String, List<Strategy>> strategyMap = this.strategyCacheOps.entries(PACKAGE_MAIL_PRODUCT_ID_CACHE_NAME);
//		for (ProductVo productVo : productVos) {
//			//从缓存中取商品的包邮策略信息
//			List<Strategy> strategies = strategyMap.get(productVo.getId());
//			Strategy curentStrategy = null;
//			if(CollectionUtils.isNotEmpty(strategies)){
//				//迭代依次获取策略
//				for (int i = strategies.size()-1; i >= 0 ; i--) {
//					Strategy strategy = strategies.get(i);
//					if(!StringUtils.isNotEmpty(strategy.getStartDate())&& NumberUtils.isNumber(strategy.getStartDate())){
//						continue;
//					}
//					if(!StringUtils.isNotEmpty(strategy.getEndDate())&& NumberUtils.isNumber(strategy.getEndDate())){
//						continue;
//					}
//					long nowTime = System.currentTimeMillis();
//					
//					long startTime = Long.valueOf(strategy.getStartDate());
//					long endTime = Long.valueOf(strategy.getEndDate());
//					//是否在时间区段内的活动
//					if(nowTime>=startTime && nowTime<endTime){
//						curentStrategy = strategy;
//						break;
//					}
//				}
//			}
//			if(curentStrategy != null){
//				LimitedPurchaseVo limitedPurchaseVo = new LimitedPurchaseVo();
//				limitedPurchaseVo.setStrategyId(curentStrategy.getId());
//				limitedPurchaseVo.setIsLimitedPurchase("Y");
//				if(StringUtils.isNotEmpty(curentStrategy.getStartDate())){
//					limitedPurchaseVo.setStartTime(new Date(Long.valueOf(curentStrategy.getStartDate())));
//				}
//				if(StringUtils.isNotEmpty(curentStrategy.getEndDate())){
//					limitedPurchaseVo.setEndTime(new Date(Long.valueOf(curentStrategy.getEndDate())));
//				}
//				productVo.setLimitedPurchaseInfo(limitedPurchaseVo);
//			}
//		}
//		return new AsyncResult<Void>(null);
//	}
//
//	/**
//	 * 获取缓存房间名称
//	 */
//	@Override
//	String getCacheRoomName() {
//		return LimitedPurchaseCacheManager.PACKAGE_MAIL_PRODUCT_ID_CACHE_NAME;
//	}
//	
//	
//}
