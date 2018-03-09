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

import com.yikuyi.activity.model.ActivityProduct;
import com.yikuyi.activity.vo.ActivityProducOrderVo;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

@Headers({"Content-Type: application/json;charset=UTF-8"})
public interface ActivityProductClient {
	
	@RequestLine("PUT /v1/activityproducts/order")
	@Headers({"Authorization: Basic {authToken}"})
	public List<ActivityProducOrderVo> updateActivityProductQty(@RequestBody List<ActivityProducOrderVo> productOrders,@Param("authToken") String authToken);
	
	@RequestLine("POST /v1/activityproducts/effectproduct")
	public List<ActivityProduct> getEffectProduct(@RequestBody List<String> ids);
	
	@RequestLine("GET v1/activities/refresh/task")
	public void activityTask();
}
