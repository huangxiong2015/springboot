/*
 * Created: 2017年8月14日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.externalclient;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.framework.springboot.utils.AuthorizationUtil;
import com.yikuyi.party.PartyClientBuilder;
import com.yikuyi.party.vendor.vo.PartyProductLine;
import com.yikuyi.party.vendor.vo.PartyProductLineVo;
import com.yikuyi.party.vendor.vo.VendorInfoVo;

@Service
public final class PartyClientUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(PartyClientUtils.class);
	
	private static final String SUPPLLIER_PRODUCTLINE_CACHENAME = "suppllierProductLineCachename";
	
	private static final String SUPPLLIER_IDS_CACHENAME = "suppllierIdsCachename";
	
	@Autowired
	private PartyClientBuilder partyClientBuilder;
	
	@Autowired
	private AuthorizationUtil authorizationUtil;
	
	@Autowired
	public CacheManager cacheManager;
	
	@SuppressWarnings({ "unchecked" })
	/**
	 * 通过供应商ID获取产品线配置(默认缓存5分钟时间)
	 * </br>可以通过运营后台redis监控管理手动删除
	 * @param supllierId
	 * @return
	 * @since 2017年9月7日
	 * @author jik.shu@yikuyi.com
	 */
	public Map<String,Boolean> getProductLIneBySupllierId(String supllierId){
		Cache cache = cacheManager.getCache(SUPPLLIER_PRODUCTLINE_CACHENAME);
		Map<String,Boolean> cacheMap = Optional.ofNullable(cache.get(supllierId)).map(v->(Map<String,Boolean>)v.get()).orElse(null);
		if(null == cacheMap){
			Optional<PartyProductLineVo> optional = Optional.ofNullable(partyClientBuilder.vendorsClient().findProductLineByPartyId(supllierId, authorizationUtil.getMockAuthorization()));
			List<PartyProductLine> productLines = optional.map(PartyProductLineVo::getPartyProductLineList).orElse(Collections.emptyList());
			cacheMap = new HashMap<>();
			for(PartyProductLine tempVo : productLines){
				String key = getSupplierProductLineCacheKey(tempVo.getBrandId(),tempVo.getCategory1Id(),tempVo.getCategory2Id(),tempVo.getCategory3Id());
				//包含并且之前的值 是 “代理”
				if(cacheMap.containsKey(key)){
					if(Optional.ofNullable(tempVo.getType()).isPresent() 
							&& StringUtils.equals(PartyProductLineVo.Type.NOT_PROXY.name(),tempVo.getType().name())){
						cacheMap.put(key, Boolean.FALSE);
					}else{
						cacheMap.put(PartyProductLineVo.Type.PROXY.name(), Boolean.TRUE);
					}
					continue;
				}
				//不代理
				if(Optional.ofNullable(tempVo.getType()).isPresent() 
						&& StringUtils.equals(PartyProductLineVo.Type.NOT_PROXY.name(),tempVo.getType().name())){
					cacheMap.put(key, Boolean.FALSE);
				}else{
					cacheMap.put(key, Boolean.TRUE);
					cacheMap.put(PartyProductLineVo.Type.PROXY.name(), Boolean.TRUE);
				}
				
			}
			cache.put(supllierId, cacheMap);
			return cacheMap;
		}else{
			return cacheMap;
		}
	}
	
	public static String getSupplierProductLineCacheKey(String manufacturerId, String cat1, String cat2, String cat3){
		return new StringBuffer().append("manufacturerId_").append(StringUtils.isEmpty(manufacturerId)?"*":manufacturerId)
				.append("_cat1_").append(StringUtils.isEmpty(cat1)?"*":cat1)
				.append("_cat2_").append(StringUtils.isEmpty(cat2)?"*":cat2)
				.append("_cat3_").append(StringUtils.isEmpty(cat3)?"*":cat3).toString();
	}
	
	
	/**
	 * 获取所有有效供应商ID
	 * @return
	 * @since 2018年2月7日
	 * @author jik.shu@yikuyi.com
	 */
	@SuppressWarnings({ "unchecked" })
	public Set<String> getAllSupplierIds() {
		Cache cache = cacheManager.getCache(SUPPLLIER_IDS_CACHENAME);
		if (null == cache.get(SUPPLLIER_IDS_CACHENAME)) {
			Set<String> ids = partyClientBuilder.supplierClient().getSupplierIds();
			cache.put(SUPPLLIER_IDS_CACHENAME, ids);
			return ids;
		} else {
			return Optional.ofNullable(cache.get(SUPPLLIER_IDS_CACHENAME)).map(v->(Set<String>)v.get()).orElse(Collections.emptySet());
		}
	}
	
	 /**
	  * 获取自动集成库的供应商Id
	  * @param result
	  * @return
	  */
	public Set<String> getAutoIntegrateQtyVendorIds(Boolean auto){
		return partyClientBuilder.supplierClient().getAutoIntegrateSupplier(auto);
	}
	
	 /**
	  * 查询所用供应商名称
	  * <br> key -- 供应商ID 
	  * <br> value -- 供应商显示名称 ? 供应商名称 : 供应商编码
	  * 
	  * @param showName null查询所有，true，查询只显示名称，false，查询不显示名称
	  * @return
	  * @since 2018年1月22日
	  * @author jik.shu@yikuyi.com
	  */
	public Map<String,String> getSupplierDisplayName(Boolean showName){
		return partyClientBuilder.supplierClient().getSupplierDisplayName(showName);
	}
	
	/**
	 * 获取供应商信息
	 * @param vendorIds
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String,VendorInfoVo> getVendorInfoMap(List<String> vendorIds){
		List<VendorInfoVo> vendorInfoVos = null;
		try{
			vendorInfoVos = partyClientBuilder.vendorsClient().vendorBatchList(vendorIds,authorizationUtil.getMockAuthorization());
		}catch (Exception e){
			logger.error("调用 party 服务异常：{}",e);
		}
		if(CollectionUtils.isEmpty(vendorInfoVos)){
			return MapUtils.EMPTY_MAP;
		}
		return vendorInfoVos.stream().collect(HashMap::new,(m,v)->m.put(v.getPartyId(),v),HashMap::putAll);
	}
}