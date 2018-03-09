/*
 * Created: 2017年6月30日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.resource;

import com.yikuyi.activity.vo.ActivityVo;

import feign.Headers;
import feign.RequestLine;

@Headers({"Content-Type: application/json;charset=UTF-8"})
public interface ActivityClient {
	@RequestLine("GET /v1/activities/todayactivity")
	public ActivityVo getTodayActivityStandard();
	
	@RequestLine("GET /v1/activities/refresh/task")
	public void activityTask();
	
}
