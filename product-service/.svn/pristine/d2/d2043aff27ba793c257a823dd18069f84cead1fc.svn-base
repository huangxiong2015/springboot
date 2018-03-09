/*
 * Created: 2017年6月29日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.resource;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import com.yikuyi.product.model.ProductStand;
import com.yikuyi.product.vo.RawData;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

@Headers({"Content-Type: application/json;charset=UTF-8"})
public interface ProductStandClient {
	@RequestLine("POST /v1/products/stand/batch")
	@Headers({"Authorization: Basic {authToken}"})
	public List<ProductStand> batchQuery(@RequestBody List<RawData> datas,@Param("authToken") String authToken) ;
	
	
	/**
	 * 批量验证型号是否存在
	 * @param manufacturerPartNumberList
	 * @param authToken
	 * @return
	 * @since 2017年11月28日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@RequestLine("POST /v1/products/stand/list/existManufacturerPartNumber")
	@Headers({"Authorization: Basic {authToken}"})
	public List<String> existManufacturerPartNumberList(@RequestBody List<String> manufacturerPartNumberList,@Param("authToken") String authToken);
	
}
