/*
 * Created: 2017年3月30日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.model;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.yikuyi.product.vo.ProductVo;
import com.yikuyi.product.vo.RawData;

public class ProductUtils {
	/**
	 * 计算快速识别码
	 * @param data
	 * @param ps
	 * @param facilityId
	 * @return
	 * @since 2017年3月30日
	 * @author tongkun@yikuyi.com
	 */
	public static String getProductQuickFindKey(RawData data, ProductStand ps, String facilityId) {
		return getProductQuickFindKey(data,(ps.getManufacturerId()==null?ps.getManufacturer():ps.getManufacturerId().toString()),facilityId);
	}

	/**
	 * 计算快速识别码
	 * 
	 * @param data
	 * @param brand
	 * @param facilityId
	 * @return
	 * @since 2016年12月9日
	 * @author tongkun@yikuyi.com
	 */
	public static String getProductQuickFindKey(RawData data, String manufacturerId, String facilityId) {
		String skuId = data.getSkuId();
		return getProductQuickFindKey(skuId,manufacturerId,facilityId);
	}

	/**
	 * 计算快速识别码
	 * 
	 * @param skuId
	 * @param manufacturer
	 * @param facilityId
	 * @return
	 * @since 2016年12月13日
	 * @author zr.shuzuo@yikuyi.com
	 */
	public static String getProductQuickFindKey(String skuId, String manufacturerId, String facilityId) {
		return (facilityId+"-"+(manufacturerId==null?"":(manufacturerId+"-"))+skuId).toUpperCase();
	}
	
	/**
	 * rs 供应商型号不以p结尾处理为散装
	 * @param productVos
	 */
	public static void setVendorPackaging(List<ProductVo> productVos,String rsVendorId){
		productVos.stream().forEach(productVo -> {
			//处理rs 供应商型号不以p结尾处理为散装
			String skuId = productVo.getSkuId();
			if(StringUtils.isNotEmpty(rsVendorId) && rsVendorId.equals(productVo.getVendorId()) && StringUtils.isNotEmpty(skuId) && !skuId.toUpperCase().endsWith("P")){
				productVo.setVendorPackaging("散装");
			}
		});
	}
}
