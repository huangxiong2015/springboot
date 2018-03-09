package com.yikuyi.product.specialoffer.manager;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.yikuyi.specialoffer.model.SpecialOfferRule;

@Service
public class SpecialOfferCacheManager {
	
	private static final Logger logger = LoggerFactory.getLogger(SpecialOfferCacheManager.class);
    /**
     * 特殊供应商商品id缓存名
     */
    private static final String SPECIAL_OFFER_PRODUCT_ID_CACHE_NAME = "specialOfferProductIdCacheName";

    /**
     * 特殊供应商产品规则缓存名
     */
    private static final String SPECIAL_OFFER_PRODUCT_RULE_CACHE_NAME = "specialOfferProductRuleCacheName";

    /**
     * 特殊供应商文本名缓存
     */

    private static final String SPECIAL_OFFER_TEXT_CACHE_NAME = "specialOfferTextCacheName";

    @Resource(name = "redisTemplate")
    private HashOperations<String,String,List<String>> specialOfferProductIdCacheOps;

    @Resource(name = "redisTemplate")
    private HashOperations<String,String,List<String>> specialOfferProductRuleCacheOps;

    @Resource(name = "redisTemplate")
    private HashOperations<String,String,Map<String,String>> specialOfferTextCacheOps;

    /**
     * @author injor.huang
     * 初始化规则缓存
     * @param specialOfferRule
     * @param vendorId
     * @param ruleId
     */
    public void initSpecialOfferProductRuleCache(SpecialOfferRule specialOfferRule, String vendorId, String ruleId){
        List<String> keys = this.getProductRuleKeys(specialOfferRule,vendorId,ruleId);
        if(CollectionUtils.isEmpty(keys)){
        	return;
        }
        keys.forEach(k -> {
            //设置缓存
            this.setSpecialOfferProductRuleCache(k,ruleId);
        });
    }

    /**
     * @author injor.huang
     * 获取keys
     * @param specialOfferRule
     * @param vendorId
     * @param ruleId
     * @return
     */
    private List<String> getProductRuleKeys(SpecialOfferRule specialOfferRule, String vendorId, String ruleId){
        List<String> keys = Lists.newArrayList();
        List<Integer> mfrs =specialOfferRule.getMfrIds();
        List<String> mfrIds = Lists.newArrayList();
        if(CollectionUtils.isEmpty(mfrs)){
        	mfrIds.add("*");
        }else{
        	mfrs.stream().forEach(a -> {
              	 mfrIds.add(String.valueOf(a));
              });
        }
        
        List<String > sourceIds = specialOfferRule.getSourceIds();
        List<String > cateIds = specialOfferRule.getCatIds();
        if(CollectionUtils.isEmpty(sourceIds)){
        	sourceIds = Lists.newArrayList();
        	sourceIds.add("*");
        }
        if(CollectionUtils.isEmpty(cateIds)){
        	cateIds = Lists.newArrayList();
        	cateIds.add("*/*/*");
        }
        //key规则：供应商Id/品牌ID/仓库id/大类Id/小类Id/次小类Id
        for(String mfrId:mfrIds){
            StringBuilder sbMfr = new StringBuilder(vendorId);
            sbMfr.append("/").append(mfrId);
            for(String sourceId:sourceIds){
                StringBuilder sbSource = new StringBuilder(sbMfr);
                sbSource.append("/").append(sourceId);
                for(String cateId:cateIds){
                    StringBuilder sbCate = new StringBuilder(sbSource);
                    keys.add(sbCate.append("/").append(cateId).toString());
                }
            }
        }
        return keys;
    }

    /**
     * @author injor.huang
     * 删除规则缓存
     * @param specialOfferRule
     * @param vendorId
     * @param ruleId
     */
    public void deleteSpecialOfferProductRuleCache(SpecialOfferRule specialOfferRule, String vendorId, String ruleId){
    	logger.info("删除规则缓存数据:specialOfferRule={},vendorId={},ruleId={}",JSON.toJSONString(specialOfferRule),vendorId,ruleId);
        List<String> keys = this.getProductRuleKeys(specialOfferRule,vendorId,ruleId);
        if(CollectionUtils.isEmpty(keys)){
        	return;
        }
        keys.forEach(k -> {
            //设置缓存
            this.deleteProductRuleCache(k,ruleId);
        });
    }

    /**
     * @author injor.huang
     * 初始化商品Id缓存
     * @param productId
     * @param ruleId
     */
    public void initSpecialOfferProductIdCache(String productId,String ruleId){
        List<String> stringList = specialOfferProductIdCacheOps.get(SPECIAL_OFFER_PRODUCT_ID_CACHE_NAME,productId);
        if(CollectionUtils.isNotEmpty(stringList)){
        	this.deleteSpecialOfferProductIdCache(productId, ruleId);
        }
        stringList = Lists.newArrayList();
        stringList.add(ruleId);
        specialOfferProductIdCacheOps.put(SPECIAL_OFFER_PRODUCT_ID_CACHE_NAME,productId,stringList);
    }

    /**
     * @author injor.huang
     * 设置规则缓存
     * @param key //key规则：供应商Id/品牌ID/仓库id/大类Id/小类Id/次小类Id
     * @param ruleId
     */
    private void setSpecialOfferProductRuleCache(String key,String ruleId){
        List<String> stringList = specialOfferProductRuleCacheOps.get(SPECIAL_OFFER_PRODUCT_RULE_CACHE_NAME,key);
        if(CollectionUtils.isNotEmpty(stringList)){
        	this.deleteProductRuleCache(key, ruleId);
        }
        stringList = Lists.newArrayList();
        stringList.add(ruleId);
        specialOfferProductRuleCacheOps.put(SPECIAL_OFFER_PRODUCT_RULE_CACHE_NAME,key,stringList);
    }

    /**
     * @author injor.huang
     * 初始化特殊供应商文案文本名称
     * @param key
     * @param value
     */
    public void initSpecialOfferTextCache(String vendorId,Map<String,String> map){
        specialOfferTextCacheOps.put(SPECIAL_OFFER_TEXT_CACHE_NAME,vendorId,map);
    }


    /**
     * @author injor.huang
     * 删除商品Id缓存value=ruleId
     * @param productId
     * @param ruleId
     */
    public void deleteSpecialOfferProductIdCache(String productId,String ruleId){
    	logger.info("删除产品Id缓存数据:productId={},ruleId={}",productId,ruleId);
        List<String> stringList = specialOfferProductIdCacheOps.get(SPECIAL_OFFER_PRODUCT_ID_CACHE_NAME,productId);
        if(CollectionUtils.isEmpty(stringList)){
        	return;
        }
        stringList.remove(ruleId);
        specialOfferProductIdCacheOps.put(SPECIAL_OFFER_PRODUCT_ID_CACHE_NAME,productId,stringList);
    }

    /**
     * @author injor.huang
     * 删除规则缓存 value=ruleId
     * @param key //key规则：供应商Id/品牌ID/仓库id/大类Id/小类Id/次小类Id
     * @param ruleId
     */
    private void deleteProductRuleCache(String key,String ruleId){
        List<String> stringList = specialOfferProductRuleCacheOps.get(SPECIAL_OFFER_PRODUCT_RULE_CACHE_NAME,key);
        if(CollectionUtils.isEmpty(stringList)){
        	return;
        }
        stringList.remove(ruleId);
        specialOfferProductRuleCacheOps.put(SPECIAL_OFFER_PRODUCT_RULE_CACHE_NAME,key,stringList);
    }
}
