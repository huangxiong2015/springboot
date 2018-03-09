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

import com.yikuyi.rule.mov.vo.MovInfo;
import com.yikuyi.rule.mov.vo.MovValidResult;
import com.yikuyi.rule.mov.vo.MovValidationParam;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

@Headers({"Content-Type: application/json;charset=UTF-8"})
public interface MovQueryClientV2 {
	@RequestLine("POST /v2/products/batch/mov")
	public List<MovInfo> queryByIds(@RequestBody List<String> ids);
	
	@RequestLine("POST /v2/products/batch/mov/valid")
	@Headers({ "Authorization: Basic {authToken}" })
	public List<MovValidResult> validMov(@RequestBody List<MovValidationParam> movValidationParams,@Param("authToken") String authToken);
}
