package com.yikuyi.product.promotion.bll;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.framework.springboot.config.ObjectMapperHelper;
import com.yikuyi.activity.vo.ActivityProductVo;
import com.yikuyi.activity.vo.ActivityVo;
import com.yikuyi.product.activity.bll.ActivityManager;
import com.yikuyi.promotion.model.PromoModuleProduct;
import com.yikuyi.promotion.model.PromoModuleProductDraft;
import com.yikuyi.promotion.model.Promotion;
import com.yikuyi.promotion.model.PromotionModuleContent;
import com.yikuyi.promotion.model.PromotionModuleContentDraft;
import com.yikuyi.promotion.vo.ConditionVo;
import com.yikuyi.promotion.vo.DiscountVo;
import com.yikuyi.promotion.vo.PromoModuleProductVo;
import com.yikuyi.promotion.vo.PromotionFlagVo;
import com.yikuyi.promotion.vo.PromotionModuleEffectiveVo;
import com.ykyframework.exception.BusinessException;
import com.ykyframework.exception.SystemException;

@Service
public class PromotionCacheManager {
	
	/*private static final Logger logger = LoggerFactory.getLogger(PromotionCacheManager.class);*/
	
	// 活动预览的商品缓存
	public static final String ACTIVITY_PREVIEW_PRODUCT_CACHE = "activityPreviewProductCache";

	// 活动预览供应商规则缓存
	public static final String ACTIVITY_PREVIEW_SUPPLIER_CACHE = "activityPreviewSupplierCache";

	private static final ObjectMapper mapper = ObjectMapperHelper.configeObjectMapper(new ObjectMapper());

	private static final String JSON_KEY_DATASOURCE = "dataSource";
	private static final String JSON_KEY_VALUE_GET_BY_CONDITION = "GET_BY_CONDITION";
	private static final String JSON_KEY_CONDITION = "condition";
	private static final String JSON_KEY_CONTENTSET = "contentSet";
	private static final String JSON_KEY_PROMOTIONMODULEID = "promotionModuleId";

	// 注入HashOperations对象
	@Resource(name = "redisTemplateTransaction")
	private HashOperations<String, String, List<ActivityProductVo>> activitProductOps;

	// 注入HashOperations对象
	@Resource(name = "redisTemplateTransaction")
	private HashOperations<String, String, List<ActivityVo>> activitSupplierOps;

	/**
	 * 新增活动的正式预览缓存
	 * 
	 * @param products
	 * @since 2017年10月10日
	 * @author jik.shu@yikuyi.com
	 */
	public void addPreviewProductCache(List<PromoModuleProductVo> products) {
		addProductCache(preViewProductsGroupById(products), ACTIVITY_PREVIEW_PRODUCT_CACHE);
	}

	/**
	 * 同时删除和插入缓存
	 * 
	 * @param products
	 * @since 2017年10月10日
	 * @author jik.shu@yikuyi.com
	 */
	public void addActivityProductCacheAfterDel(String promotionId, List<PromoModuleProduct> delProducts, List<PromoModuleProductVo> addProducts) {
		try {
			if (CollectionUtils.isEmpty(delProducts)) {
				addProductCache(productsGroupById(addProducts), ACTIVITY_PREVIEW_PRODUCT_CACHE);
			} else {
				// 删除缓存,并且拿到删除后的缓存结构
				List<String> delProductIds = getProductIds(delProducts);
				List<List<ActivityProductVo>> allProductCache = deleteProductCache(promotionId, delProducts, ActivityManager.ACTIVITY_PRODUCT_CACHE);

				// 剥离完全新增的缓存，和修改的缓存
				Map<String, List<ActivityProductVo>> activityProductMap = productsGroupById(addProducts);
				Map<String, List<ActivityProductVo>> newActivityProductMap = new HashMap<>();
				activityProductMap.entrySet().stream().filter(v -> !delProductIds.contains(v.getKey())).forEach(v -> newActivityProductMap.put(v.getKey(), v.getValue()));
				Map<String, List<ActivityProductVo>> updateActivityProductMap = new HashMap<>();
				activityProductMap.entrySet().stream().filter(v -> delProductIds.contains(v.getKey())).forEach(v -> updateActivityProductMap.put(v.getKey(), v.getValue()));

				if (!newActivityProductMap.isEmpty()) {
					// 保存全部新增的缓存
					addProductCache(newActivityProductMap, ActivityManager.ACTIVITY_PRODUCT_CACHE);
				}
				if (!updateActivityProductMap.isEmpty()) {
					// 保存需要修改的缓存
					addProductCache(allProductCache, updateActivityProductMap, ActivityManager.ACTIVITY_PRODUCT_CACHE);
				}
			}
		} catch (Exception e) {
			throw new SystemException("删除商品缓存失败", e);
		}
	}

	/***************************
	 * 把传人多个商品按商品ID分组 转换为ActivityProductVo
	 *******************************/

	/**
	 * 根据商品ID分组
	 * 
	 * @param products
	 * @return
	 * @since 2017年10月12日
	 * @author jik.shu@yikuyi.com
	 */
	private Map<String, List<ActivityProductVo>> preViewProductsGroupById(List<PromoModuleProductVo> products) {
		try {
			if (CollectionUtils.isEmpty(products)) {
				return Collections.emptyMap();
			}
			JavaType valueType = TypeFactory.defaultInstance().constructParametricType(List.class, PromoModuleProductVo.class);
			List<PromoModuleProductVo> rstList = mapper.readValue(mapper.writeValueAsString(products), valueType);
			// 转List<PromoModuleProduct>为Map<String,ActivityProductVo>
			return conversionVo(rstList).stream().collect(Collectors.groupingBy(ActivityProductVo::getProductId));
		} catch (Exception e) {
			throw new SystemException(e.getMessage(), e);
		}
	}
	
	/**
	 * 根据商品ID分组
	 * 
	 * @param products
	 * @return
	 * @since 2017年10月12日
	 * @author jik.shu@yikuyi.com
	 */
	private Map<String, List<ActivityProductVo>> productsGroupById(List<PromoModuleProductVo> products) {
		if (CollectionUtils.isEmpty(products)) {
			return Collections.emptyMap();
		}
		// 转List<PromoModuleProduct>为Map<String,ActivityProductVo>
		return conversionVo(products).stream().collect(Collectors.groupingBy(ActivityProductVo::getProductId));
	}

	/**
	 * 把活动列表的商品PromoModuleProduct转换ActivityProductVo <br>
	 * 鉴于之前活动缓存结构采用的是ActivityProductVo, 为了减少修改量。这期还是服用ActivityProductVo的结构
	 * 
	 * @param list
	 * @return
	 * @since 2017年10月10日
	 * @author jik.shu@yikuyi.com
	 */
	private List<ActivityProductVo> conversionVo(List<PromoModuleProductVo> promoModuleProducts) {
		if (CollectionUtils.isEmpty(promoModuleProducts)) {
			return Collections.emptyList();
		}
		return promoModuleProducts.stream().map(PromotionCacheManager::conversionVo).collect(Collectors.toList());
	}

	// 属性替换
	private static ActivityProductVo conversionVo(PromoModuleProductVo promoModuleProduct) {
		try {
			//没使用Beanutil，beanutil有对属性类型转换有限制
			ActivityProductVo tempVo = mapper.readValue(mapper.writeValueAsString(promoModuleProduct), ActivityProductVo.class);
			
			tempVo.setProductId(promoModuleProduct.getProductId());
			tempVo.setActivityId(promoModuleProduct.getPromotionId());
			tempVo.setModuelId(promoModuleProduct.getPromoModuleId());
			tempVo.setActivityType(ActivityManager.ACTIVITY_PROMOTION_TYPE);// 促销规则
			tempVo.setIsSystemQty(promoModuleProduct.isUseStockQty() ? 'Y' : 'N');
			//有价格折扣，并且没有上传价格设置折扣
			if (null != promoModuleProduct.getDiscountVo() && (null == promoModuleProduct.getPriceBreak1() || null == promoModuleProduct.getPriceBreak1())) {
				tempVo.setPromoDiscountStatus(promoModuleProduct.getDiscountVo().isOpen() ? 'Y' : 'N');
				tempVo.setPromoDiscount(promoModuleProduct.getDiscountVo().getValue());
			}
	
			//update by zr.wanghong 设置模块生效开始时间和结束时间
			PromotionModuleEffectiveVo effectiveVo = promoModuleProduct.getPromotionModuleEffectiveVo();
			if(effectiveVo!=null){
				tempVo.setModuelStartTime(effectiveVo.getStartTime());
				tempVo.setModuelEndTime(effectiveVo.getEndTime());
			}
			return tempVo;
		} catch (Exception e) {
			throw new SystemException(e.getMessage(), e);
		}
	}

	/***************************
	 * 把传人多到商品按商品ID分组 转换为ActivityProductVo
	 *******************************/

	/************************** 把固定的java数据接口转换成缓存结构 *******************************/
	/**
	 * 获取商品ID集合
	 * 
	 * @param products
	 * @return
	 * @since 2017年10月12日
	 * @author jik.shu@yikuyi.com
	 */
	private List<String> getProductIds(List<PromoModuleProduct> products) {
		if (CollectionUtils.isEmpty(products)) {
			return Collections.emptyList();
		}
		return products.stream().map(PromoModuleProduct::getProductId).collect(Collectors.toList());
	}

	/**
	 * 根据传人的cacheNamespace，插入指定的活动商品缓存
	 * 
	 * @param products
	 * @param cacheNamespace
	 * @since 2017年10月10日
	 * @author jik.shu@yikuyi.com
	 */
	private void addProductCache(Map<String, List<ActivityProductVo>> activityProductMap, String cacheNamespace) {
		// 查找缓存是否已经存在数据
		List<List<ActivityProductVo>> allProductCache = activitProductOps.multiGet(cacheNamespace, activityProductMap.keySet());
		buliderCacheFromListToMapAndSave(allProductCache, activityProductMap, cacheNamespace);

	}

	/**
	 * 根据传人的cacheNamespace，插入指定的活动商品缓存
	 * 
	 * @param products
	 * @param cacheNamespace
	 * @since 2017年10月10日
	 * @author jik.shu@yikuyi.com
	 */
	private void addProductCache(List<List<ActivityProductVo>> allProductCache, Map<String, List<ActivityProductVo>> activityProductMap, String cacheNamespace) {
		buliderCacheFromListToMapAndSave(allProductCache, activityProductMap, cacheNamespace);
	}

	/**
	 * 根据传入的List数据接口和要保存的Map数据，把List的结构修改后更新到Map
	 * 
	 * @param allProductCache
	 * @param activityProductMap
	 * @since 2017年10月12日
	 * @author jik.shu@yikuyi.com
	 */
	private void buliderCacheFromListToMapAndSave(List<List<ActivityProductVo>> allProductCache, Map<String, List<ActivityProductVo>> activityProductMap, String cacheNamespace) {
		// 查找缓存是否已经存在数据
		if (CollectionUtils.isNotEmpty(allProductCache)) {// 如果缓存存在,追加后替换到activityProductMap
			allProductCache.stream().filter(CollectionUtils::isNotEmpty).filter(Objects::nonNull).forEach(v -> {
				String tempId = v.get(0).getProductId();
				if (activityProductMap.containsKey(tempId)) {
					v.addAll(activityProductMap.get(tempId));// 末尾追加
					activityProductMap.put(tempId, v);
				}
			});
		}
		// 批量保存activityProductMap
		if (!activityProductMap.isEmpty()) {
			activitProductOps.putAll(cacheNamespace, activityProductMap);
		}
	}

	/************************** 把固定的java数据接口转换成缓存结构 *******************************/

	/**
	 * 新增预览的规则缓存
	 * 
	 * @param promotion
	 * @param contentVo
	 * @throws BusinessException
	 * @since 2017年10月10日
	 * @author jik.shu@yikuyi.com
	 */
	public void addPreviewRuleCache(Promotion promotion, List<PromotionModuleContentDraft> contentVos) {
		addSupplierRuleCache(promotion, getPreviewJsonRule(contentVos), ACTIVITY_PREVIEW_SUPPLIER_CACHE);
	}

	/**
	 * 批量插入缓存在删除之后
	 * 
	 * @param promotionId
	 * @param delContentVos
	 * @param promotion
	 * @param contentVo
	 * @since 2017年10月12日
	 * @author jik.shu@yikuyi.com
	 */
	public void addActivityRuleCacheAfterDel(List<PromotionModuleContent> delContentVos, Promotion promotion, List<PromotionModuleContent> addContentVos) {
		List<String> delKeys = batchGenerateRuleKey(getJsonRule(delContentVos), null);
		if (CollectionUtils.isEmpty(delKeys)) {
			addSupplierRuleCache(promotion, getJsonRule(delContentVos), ActivityManager.ACTIVITY_SUPPLIER_CACHE);
		} else {
			// 删除缓存,并且拿到删除后的缓存结构
			String promotionId = promotion.getPromotionId();
			List<List<ActivityVo>> delAfterCaches = deleteRuleCache(promotionId, getJsonRule(delContentVos), ActivityManager.ACTIVITY_SUPPLIER_CACHE);

			Map<String, ActivityVo> activityRulesMap = getFormalActivitys(promotion, addContentVos).stream().collect(Collectors.toMap(ActivityVo::getSupplierRuleKey, Function.identity(),(key1, key2) -> key2));
			// 剥离完全新增的缓存，和修改的缓存
			Map<String, ActivityVo> newRulesMap = new HashMap<>();
			activityRulesMap.entrySet().stream().filter(v -> !delKeys.contains(v.getKey())).forEach(v -> newRulesMap.put(v.getKey(), v.getValue()));
			Map<String, ActivityVo> updateRulesMap = new HashMap<>();
			activityRulesMap.entrySet().stream().filter(v -> delKeys.contains(v.getKey())).forEach(v -> updateRulesMap.put(v.getKey(), v.getValue()));

			if (!newRulesMap.isEmpty()) {
				// 保存全部新增的缓存
				addSupplierRuleCache(newRulesMap.values(), ActivityManager.ACTIVITY_SUPPLIER_CACHE);
			}
			if (!updateRulesMap.isEmpty()) {
				// 保存需要修改的缓存
				addSupplierRuleCache(delAfterCaches, updateRulesMap.values(), ActivityManager.ACTIVITY_SUPPLIER_CACHE);
			}
		}
	}

	private List<ActivityVo> getFormalActivitys(Promotion promotion, List<PromotionModuleContent> moduleContentVos) {
		return getActivityVoByCon(promotion, getJsonRule(moduleContentVos));
	}

	/**
	 * 增加活动的供应商规则缓存
	 * 
	 * @param promotion
	 * @param contentJson
	 * @param keyNamespace
	 * @throws BusinessException
	 * @since 2017年10月10日
	 * @author jik.shu@yikuyi.com
	 */
	private void addSupplierRuleCache(Promotion promotion, List<JSONObject> contentJsons, String cacheNamespace) {
		List<ActivityVo> activityRules = getActivityVoByCon(promotion, contentJsons);
		addSupplierRuleCache(activityRules, cacheNamespace);
	}

	private List<ActivityVo> getActivityVoByCon(Promotion promotion, List<JSONObject> listConditon) {
		if (Objects.isNull(promotion) || CollectionUtils.isEmpty(listConditon)) {
			return Collections.emptyList();
		}
		// 解析规则,并且存入指定的集合tempVos
		List<ActivityVo> tempVos = new ArrayList<>();
		try {
			for (int i = 0; i < listConditon.size(); i++) {
				JSONObject tempJsonObj = listConditon.get(i);
				JSONObject contentSetObj = tempJsonObj.getJSONObject(JSON_KEY_CONTENTSET);
				// 构建一个List<ActivityVo>活动集合
				ActivityVo activityVo = new ActivityVo();
				activityVo.setActivityId(promotion.getPromotionId());
				activityVo.setModuleId(tempJsonObj.getString(JSON_KEY_PROMOTIONMODULEID));
				activityVo.setPromotionFlag(mapper.readValue(contentSetObj.getString("promotionFlag"), PromotionFlagVo.class));
				// 折扣
				DiscountVo discountVo = mapper.readValue(contentSetObj.getString("discount"), DiscountVo.class);
				if (discountVo.isOpen()) {
					activityVo.setPromoDiscountStatus('Y');
					activityVo.setPromoDiscount(discountVo.getValue());
				}
				activityVo.setStartDate(promotion.getStartDate());
				activityVo.setEndDate(promotion.getEndDate());
				activityVo.setLastUpdateDate(promotion.getLastUpdateDate());
				
				// 模块生效时间
				if(StringUtils.isNotEmpty(contentSetObj.getString("effectSet"))){
					PromotionModuleEffectiveVo effectiveVo = mapper.readValue(contentSetObj.getString("effectSet"), PromotionModuleEffectiveVo.class);
					if(effectiveVo!=null){
						activityVo.setModuelStartTime(effectiveVo.getStartTime());
						activityVo.setModuelEndTime(effectiveVo.getEndTime());
					}
				}
				
				// 获取具体应用规则
				ConditionVo conditionVo = mapper.readValue(contentSetObj.getString(JSON_KEY_CONDITION), ConditionVo.class);

				List<String> conditionKeys = generateRuleKeyByCondition(conditionVo);
				for (int j = 0; j < conditionKeys.size(); j++) {// 为每个相同的ActivityVo设置不同的supplierRuleKey
					ActivityVo newVo = new ActivityVo();
					org.apache.commons.beanutils.BeanUtils.copyProperties(newVo, activityVo);
					//BeanUtils.copyProperties(activityVo,newVo);
					newVo.setSupplierRuleKey(conditionKeys.get(j));
					tempVos.add(newVo);
				}
			}
		} catch (Exception e) {
			throw new SystemException(e.getMessage(), e);
		}
		return tempVos;
	}

	/**
	 * 新增预览的缓存规则
	 * 
	 * @param activityRules
	 * @since 2017年10月10日
	 * @author jik.shu@yikuyi.com
	 */
	private void addSupplierRuleCache(List<List<ActivityVo>> allRuleCache, Collection<ActivityVo> activityRules, String cacheNamespace) {
		saveRuleCache(allRuleCache, activityRules, cacheNamespace);
	}

	/**
	 * 新增预览的缓存规则
	 * 
	 * @param activityRules
	 * @since 2017年10月10日
	 * @author jik.shu@yikuyi.com
	 */
	private void addSupplierRuleCache(Collection<ActivityVo> activityRules, String cacheNamespace) {
		List<List<ActivityVo>> allRuleCache = activitSupplierOps.multiGet(cacheNamespace, activityRules.stream().map(ActivityVo::getSupplierRuleKey).collect(Collectors.toList()));
		saveRuleCache(allRuleCache, activityRules, cacheNamespace);
	}

	private void saveRuleCache(List<List<ActivityVo>> allRuleCache, Collection<ActivityVo> activityRules, String cacheNamespace) {
		if (CollectionUtils.isEmpty(activityRules)) {
			return;
		}
		Map<String, List<ActivityVo>> saveCacheMap = activityRules.stream().collect(Collectors.groupingBy(ActivityVo::getSupplierRuleKey));
		// 查找缓存是否已经存在数据
		if (CollectionUtils.isNotEmpty(allRuleCache)) {// 如果缓存存在,追加后替换到saveCacheMap
			allRuleCache.stream().filter(Objects::nonNull).forEach(v -> {
				String cacheKey = v.get(0).getSupplierRuleKey();
				v.addAll(saveCacheMap.get(cacheKey));// 末尾追加
				saveCacheMap.put(cacheKey, v);
			});
		}
		if (!saveCacheMap.isEmpty()) {
			// 批量保存activityProductMap
			activitSupplierOps.putAll(cacheNamespace, saveCacheMap);
		}
	}

	/****************************************** 删除商品缓存 *************************************/

	/**
	 * 根据活动ID和商品ID删除预览缓存
	 * 
	 * @param promotionId
	 * @param productIds
	 * @since 2017年10月10日
	 * @author jik.shu@yikuyi.com
	 */
	public void deletePreviewProductCache(String promotionId, List<PromoModuleProductDraft> products) {
		try {
			JavaType valueType = TypeFactory.defaultInstance().constructParametricType(List.class, PromoModuleProductVo.class);
			deleteProductCache(promotionId, mapper.readValue(mapper.writeValueAsString(products), valueType), ACTIVITY_PREVIEW_PRODUCT_CACHE);
		} catch (Exception e) {
			throw new SystemException(e.getMessage(), e);
		}
	}

	/**
	 * 根据活动ID和商品ID删除正式缓存
	 * 
	 * @param promotionId
	 * @param productIds
	 * @since 2017年10月10日
	 * @author jik.shu@yikuyi.com
	 */
	public void deleteActivityProductCache(String promotionId, List<PromoModuleProduct> products) {
		deleteProductCache(promotionId, products, ActivityManager.ACTIVITY_PRODUCT_CACHE);
	}

	/**
	 * 根据活动ID和商品ID删除指定cacheNamespace缓存 <br/>
	 * 返回具体Key删除后的缓存结构 , 顺序和传入商品顺序一致
	 * 
	 * @param promotionId
	 * @param products
	 * @param cacheNamespace
	 * @return
	 * @since 2017年10月12日
	 * @author jik.shu@yikuyi.com
	 */
	private List<List<ActivityProductVo>> deleteProductCache(String promotionId, List<PromoModuleProduct> products, String cacheNamespace) {
		try {
			if (StringUtils.isEmpty(promotionId) || CollectionUtils.isEmpty(products)) {
				return Collections.emptyList();
			}
			List<String> productIds = products.stream().map(PromoModuleProduct::getProductId).collect(Collectors.toList());
			List<String> moduleIds = products.stream().map(PromoModuleProduct::getPromoModuleId).collect(Collectors.toList());
			return deleteProductCache(promotionId, moduleIds, productIds, cacheNamespace);
		} catch (Exception e) {
			throw new SystemException("删除商品缓存失败", e);
		}
	}

	/**
	 * 提供一套删除商品缓存的接口，不删除库存的缓存。 主要用来做老版本秒杀和促销的缓存删除
	 * 
	 * @param promotionId
	 * @param moduleIds
	 * @param productIds
	 * @param cacheNamespace
	 * @return
	 * @since 2017年10月14日
	 * @author jik.shu@yikuyi.com
	 */
	public List<List<ActivityProductVo>> deleteProductCache(String promotionId, List<String> moduleIds, List<String> productIds, String cacheNamespace) {
		// multiGet方法如果缓存不存在会返回一个null占位符在返回的list中
		List<List<ActivityProductVo>> allProductCache = activitProductOps.multiGet(cacheNamespace, productIds);
		int size = 0;
		while(null == allProductCache && size<3){
			allProductCache = activitProductOps.multiGet(cacheNamespace, productIds);
		}
		if(null == allProductCache){
			throw new SystemException("活动发布失败,请重新发布");
		}
		// 从缓存取出的结构中，按模块ID和活动ID，移除
		// 如果单个key的value全被移除，把单个key标记在另外一个delCahceProIds集合中，方便后面删除
		List<String> delCahceProIds = new ArrayList<>();
		int i = 0;
		Iterator<List<ActivityProductVo>> delIterator = allProductCache.iterator();
		while (delIterator.hasNext()) {
			List<ActivityProductVo> tempList = delIterator.next();
			if (CollectionUtils.isEmpty(tempList)) {
				delIterator.remove();
				continue;
			}
			Iterator<ActivityProductVo> activityProductVoIter = tempList.iterator();
			while (activityProductVoIter.hasNext()) {
				ActivityProductVo tempVo = activityProductVoIter.next();
				if (promotionId.equals(tempVo.getActivityId()) && (StringUtils.isEmpty(tempVo.getModuelId()) || moduleIds.get(i).equals(tempVo.getModuelId()))) {
					activityProductVoIter.remove();
				}
			}
			if (CollectionUtils.isEmpty(tempList)) {
				delCahceProIds.add(productIds.get(i));
				delIterator.remove();
			}
			i++;
		}
		// update一次移除后的结构
		allProductCache.stream().filter(CollectionUtils::isNotEmpty).filter(Objects::nonNull).forEach(v -> activitProductOps.put(cacheNamespace, v.get(0).getProductId(), v));
		// 删除完全被移除的Key
		if (CollectionUtils.isNotEmpty(delCahceProIds)) {
			activitProductOps.delete(cacheNamespace, delCahceProIds.toArray());
		}
		return allProductCache;
	}

	/****************************************** 删除规则缓存 *****************************************/

	/**
	 * 根据活动ID和供应商规则KEY删除预览缓存
	 * 
	 * @param promotionId
	 * @param productIds
	 * @since 2017年10月10日
	 * @author jik.shu@yikuyi.com
	 * @throws BusinessException
	 */
	public void deletePreviewRuleCache(String promotionId, List<PromotionModuleContentDraft> contentVos) {
		deleteRuleCache(promotionId, getPreviewJsonRule(contentVos), ACTIVITY_PREVIEW_SUPPLIER_CACHE);
	}

	/**
	 * 根据活动ID和供应商规则KEY删除正式缓存
	 * 
	 * @param promotionId
	 * @param productIds
	 * @since 2017年10月10日
	 * @author jik.shu@yikuyi.com
	 * @throws BusinessException
	 */
	public void deleteActivityRuleCache(String promotionId, List<PromotionModuleContent> contentVos) {
		deleteRuleCache(promotionId, getJsonRule(contentVos), ActivityManager.ACTIVITY_SUPPLIER_CACHE);
	}

	/**
	 * 根据活动ID和供应商规则KEY删除指定cacheNamespace缓存
	 * 
	 * @param promotionId
	 * @param productIds
	 * @since 2017年10月10日
	 * @author jik.shu@yikuyi.com
	 * @throws BusinessException
	 */
	private List<List<ActivityVo>> deleteRuleCache(String promotionId, List<JSONObject> contentJsons, String cacheNamespace) {
		try {
			if (StringUtils.isEmpty(promotionId) || CollectionUtils.isEmpty(contentJsons)) {
				return Collections.emptyList();
			}
			List<String> moduleIds = new ArrayList<>();
			// 解析规则,获取完成的Key
			List<String> conditionKeys = batchGenerateRuleKey(contentJsons,moduleIds);
			// 获取缓存结构
			return deleteRuleCache(promotionId, moduleIds, conditionKeys, cacheNamespace);
		} catch (Exception e) {
			throw new SystemException("删除缓存失败", e);
		}
	}

	/**
	 * 对外提供一套根据模块ID和对应KEYS删除缓存的接口，主要是兼容之前秒杀和促销的缓存结构
	 * 秒杀和促销的moduleIds没有，可以传人一个Empty List
	 * 
	 * @param promotionId
	 * @param moduleIds
	 * @param keys
	 * @param cacheNamespace
	 * @return
	 * @since 2017年10月14日
	 * @author jik.shu@yikuyi.com
	 */
	public List<List<ActivityVo>> deleteRuleCache(String promotionId, List<String> moduleIds, List<String> keys, String cacheNamespace) {
		List<List<ActivityVo>> allRuleCache = activitSupplierOps.multiGet(cacheNamespace, keys);
		List<String> delCacheKeys = new ArrayList<>();
		int i = 0;
		Iterator<List<ActivityVo>> deleteIterator = allRuleCache.iterator();
		while (deleteIterator.hasNext()) {
			List<ActivityVo> tempList = deleteIterator.next();
			if (CollectionUtils.isEmpty(tempList)) {
				deleteIterator.remove();
				continue;
			}
			Iterator<ActivityVo> activityVoIter = tempList.iterator();
			while (activityVoIter.hasNext()) {
				ActivityVo tempVo = activityVoIter.next();
				if (promotionId.equals(tempVo.getActivityId()) && (StringUtils.isEmpty(tempVo.getModuleId()) || moduleIds.get(i).equals(tempVo.getModuleId()))) {
					activityVoIter.remove();
				}
			}
			if (CollectionUtils.isEmpty(tempList)) {
				delCacheKeys.add(keys.get(i));
				deleteIterator.remove();
			}
			i++;
		}
		allRuleCache.stream().filter(CollectionUtils::isNotEmpty).forEach(v -> activitSupplierOps.put(cacheNamespace, v.get(0).getSupplierRuleKey(), v));

		if (CollectionUtils.isNotEmpty(delCacheKeys)) {
			activitProductOps.delete(cacheNamespace, delCacheKeys.toArray());
		}
		return allRuleCache;
	}

	/**
	 * 获取预览活动Json规则
	 * 
	 * @param moduleContentVos
	 * @return
	 * @since 2017年10月12日
	 * @author jik.shu@yikuyi.com
	 */
	private List<JSONObject> getPreviewJsonRule(List<PromotionModuleContentDraft> moduleContentVos) {
		List<PromotionModuleContentDraft> newList = moduleContentVos.stream().filter(Objects::nonNull)
				.filter(v -> (Objects.nonNull(v.getPromotionContent()) && Objects.nonNull(v.getPromotionContent().getJSONObject(JSON_KEY_CONTENTSET))
						&& JSON_KEY_VALUE_GET_BY_CONDITION.equals(v.getPromotionContent().getJSONObject(JSON_KEY_CONTENTSET).getString(JSON_KEY_DATASOURCE))
						&& v.getPromotionContent().getJSONObject(JSON_KEY_CONTENTSET).containsKey(JSON_KEY_CONDITION)))
				.map(Function.identity()).collect(Collectors.toList());
		newList.stream().forEach(v -> v.getPromotionContent().put(JSON_KEY_PROMOTIONMODULEID, v.getPromoModuleId()));
		return newList.stream().map(PromotionModuleContentDraft::getPromotionContent).collect(Collectors.toList());
	}

	/**
	 * 获取正式活动Json规则
	 * 
	 * @param moduleContentVos
	 * @return
	 * @since 2017年10月12日
	 * @author jik.shu@yikuyi.com
	 */
	private List<JSONObject> getJsonRule(List<PromotionModuleContent> moduleContentVos) {
		List<PromotionModuleContent> newList = moduleContentVos.stream().filter(Objects::nonNull)
				.filter(v -> (Objects.nonNull(v.getPromotionContent()) && Objects.nonNull(v.getPromotionContent().getJSONObject(JSON_KEY_CONTENTSET))
						&& JSON_KEY_VALUE_GET_BY_CONDITION.equals(v.getPromotionContent().getJSONObject(JSON_KEY_CONTENTSET).getString(JSON_KEY_DATASOURCE))
						&& v.getPromotionContent().getJSONObject(JSON_KEY_CONTENTSET).containsKey(JSON_KEY_CONDITION)))
				.map(Function.identity()).collect(Collectors.toList());
		newList.stream().forEach(v -> v.getPromotionContent().put(JSON_KEY_PROMOTIONMODULEID, v.getPromoModuleId()));
		return newList.stream().map(PromotionModuleContent::getPromotionContent).collect(Collectors.toList());
	}

	/**
	 * 批量解析规则的Key
	 * 
	 * @param listConditon
	 * @return
	 * @since 2017年10月12日
	 * @author jik.shu@yikuyi.com
	 */
	private List<String> batchGenerateRuleKey(List<JSONObject> listConditon, List<String> moduleIds) {
		try {
			if (CollectionUtils.isEmpty(listConditon)) {
				return Collections.emptyList();
			}
			List<String> rstKeys = new ArrayList<>();
			for (int i = 0; i < listConditon.size(); i++) {
				// 获取具体应用规则
				ConditionVo conditionVo = mapper.readValue(listConditon.get(i).getJSONObject(JSON_KEY_CONTENTSET).getString(JSON_KEY_CONDITION), ConditionVo.class);
				// 解析规则,并且存入指定的集合tempVos
				List<String> tempKeys = generateRuleKeyByCondition(conditionVo);
				rstKeys.addAll(tempKeys);
				if(null == moduleIds){
					continue;
				}
				String modulId = listConditon.get(i).getString(JSON_KEY_PROMOTIONMODULEID);
				List<String> tempModuls = tempKeys.stream().map(v -> modulId).collect(Collectors.toList());
				moduleIds.addAll(tempModuls);
			}
			return rstKeys;
		} catch (Exception e) {
			throw new SystemException(e.getMessage(), e);
		}
	}

	/**
	 * 解析前端分配的条件
	 * 
	 * @param conditionVo
	 * @return
	 * @since 2017年10月11日
	 * @author jik.shu@yikuyi.com
	 */
	private List<String> generateRuleKeyByCondition(ConditionVo conditionVo) {
		if (CollectionUtils.isEmpty(conditionVo.getVendor()) && CollectionUtils.isEmpty(conditionVo.getBrand()) && CollectionUtils.isEmpty(conditionVo.getCategory())) {
			return Collections.emptyList();
		}
		List<String> keys = new ArrayList<>();

		if (CollectionUtils.isNotEmpty(conditionVo.getVendor()) && CollectionUtils.isEmpty(conditionVo.getBrand()) && CollectionUtils.isEmpty(conditionVo.getCategory())) {
			// 分销商ID_*
			conditionVo.getVendor().stream().filter(StringUtils::isNotEmpty).forEach(v -> keys.add(new StringBuilder().append(v).append("_*").toString()));
		} else if (CollectionUtils.isNotEmpty(conditionVo.getVendor()) && CollectionUtils.isNotEmpty(conditionVo.getBrand()) && CollectionUtils.isEmpty(conditionVo.getCategory())) {
			// 供应商ID_品牌ID_*
			conditionVo.getVendor().stream().filter(StringUtils::isNotEmpty)
					.forEach(v -> conditionVo.getBrand().stream().filter(StringUtils::isNotEmpty).forEach(h -> keys.add(new StringBuilder().append(v).append("_").append(h).append("_*").toString())));
		} else if (CollectionUtils.isNotEmpty(conditionVo.getVendor()) && CollectionUtils.isNotEmpty(conditionVo.getBrand()) && CollectionUtils.isNotEmpty(conditionVo.getCategory())) {
			// 供应商ID_品牌ID_大类_*
			// 供应商ID_品牌ID_大类_小类_*
			conditionVo.getVendor().stream().filter(StringUtils::isNotEmpty)
					.forEach(v -> conditionVo.getBrand().stream().filter(StringUtils::isNotEmpty).forEach(h -> conditionVo.getCategory().stream().filter(Objects::nonNull).forEach(s -> {
						String[] cates = s.getId().split("/");
						keys.add(new StringBuilder().append(v).append("_")// 供应商ID
								.append(h).append("_")// 制造商ID
								.append(cates[0]).append("_")// 大类ID
								.append(cates[1]).append("_*").toString());// 小类ID
					})));
		} else if (CollectionUtils.isNotEmpty(conditionVo.getVendor()) && CollectionUtils.isEmpty(conditionVo.getBrand()) && CollectionUtils.isNotEmpty(conditionVo.getCategory())) {
			// 供应商ID_大类_*
			// 供应商ID_大类_小类_*
			conditionVo.getVendor().stream().filter(StringUtils::isNotEmpty).forEach(v -> conditionVo.getCategory().stream().filter(Objects::nonNull).forEach(s -> {
				String[] cates = s.getId().split("/");
				keys.add(new StringBuilder().append(v).append("_").append(cates[0]).append("_").append(cates[1]).append("_*").toString());
			}));
		} else if (CollectionUtils.isEmpty(conditionVo.getVendor()) && CollectionUtils.isEmpty(conditionVo.getBrand()) && CollectionUtils.isNotEmpty(conditionVo.getCategory())) {
			// 大类_*
			// 大类_小类_*
			conditionVo.getCategory().stream().filter(Objects::nonNull).forEach(s -> {
				String[] cates = s.getId().split("/");
				keys.add(new StringBuilder().append(cates[0]).append("_").append(cates[1]).append("_*").toString());// 小类ID
			});
		} else if (CollectionUtils.isEmpty(conditionVo.getVendor()) && CollectionUtils.isNotEmpty(conditionVo.getBrand()) && CollectionUtils.isNotEmpty(conditionVo.getCategory())) {
			// 品牌ID_大类_*
			// 品牌ID_大类_小类_*
			conditionVo.getBrand().stream().filter(StringUtils::isNotEmpty).forEach(h -> conditionVo.getCategory().stream().filter(Objects::nonNull).forEach(s -> {
				String[] cates = s.getId().split("/");
				keys.add(new StringBuilder().append(h).append("_").append(cates[0]).append("_").append(cates[1]).append("_*").toString());
			}));
		} else if (CollectionUtils.isEmpty(conditionVo.getVendor()) && CollectionUtils.isNotEmpty(conditionVo.getBrand()) && CollectionUtils.isEmpty(conditionVo.getCategory())) {
			// 品牌ID_*
			conditionVo.getBrand().stream().filter(StringUtils::isNotEmpty).forEach(h -> keys.add(new StringBuilder().append(h).append("_*").toString()));
		}
		return keys;
	}

}