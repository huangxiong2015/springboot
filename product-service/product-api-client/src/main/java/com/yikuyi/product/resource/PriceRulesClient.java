/*
 * Created: 2017年7月12日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.resource;

import org.springframework.web.bind.annotation.RequestBody;

import com.github.pagehelper.PageInfo;
import com.yikuyi.rule.price.ProductPriceRule;
import com.yikuyi.rule.price.model.PriceRuleTemplate;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

@Headers({"Content-Type: application/json;charset=UTF-8"})
public interface PriceRulesClient {
	@RequestLine("POST /v1/rules/price")
	@Headers({"Authorization: Basic {authToken}"})
	public PriceRuleTemplate add(@RequestBody PriceRuleTemplate priceTemplate,@Param("authToken") String authToken);
	
	@RequestLine("PUT /v1/rules/price")
	@Headers({"Authorization: Basic {authToken}"})
	public PriceRuleTemplate update(@RequestBody PriceRuleTemplate priceTemplate,@Param("authToken") String authToken);
	
	@RequestLine("GET /v1/rules/price/{id}?id={id}")
	@Headers({"Authorization: Basic {authToken}"})
	public PriceRuleTemplate query(@Param("id") String id,@Param("authToken") String authToken);
	
	@RequestLine("DELETE /v1/rules/price/{ruleId}?ruleId={ruleId}")
	@Headers({"Authorization: Basic {authToken}"})
	public PriceRuleTemplate delete(@Param("ruleId") String ruleId,@Param("authToken") String authToken);
	
	@RequestLine("PUT /v1/rules/price/{ruleId}/status?ruleId={ruleId}&status={status}")
	@Headers({"Authorization: Basic {authToken}"})
	public PriceRuleTemplate status(@Param("ruleId") String ruleId, @Param("status") ProductPriceRule.RuleStatus status,@Param("authToken") String authToken);
	
	@RequestLine("GET /v1/rules/price?page={page}&size={size}")
	@Headers({"Authorization: Basic {authToken}"})
	public PageInfo<PriceRuleTemplate> queryList(@Param("page")int page, 
			@Param("size")int size,@Param("authToken") String authToken);
	
}
