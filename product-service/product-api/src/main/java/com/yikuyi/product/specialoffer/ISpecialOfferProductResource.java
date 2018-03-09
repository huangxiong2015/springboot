/*
 * Created: 2017年12月20日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.specialoffer;

import com.github.pagehelper.PageInfo;
import com.yikuyi.specialoffer.model.SpecialOfferProduct;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 专属特价商品
 * @author tb.lijing@yikuyi.com
 * @version 1.0.0
 */
public interface ISpecialOfferProductResource {
	/**
	 * 查询专属特价商品数据
	 * @param ruleId
	 * @param page
	 * @param pageSize
	 * @return
	 * @since 2017年12月20日
	 * @author tb.lijing@yikuyi.com
	 */
	@ApiOperation(value = "查询专属特价商品数据", notes = "作者：李京<br>查询专属特价商品数据", response = SpecialOfferProduct.class)
	public PageInfo<SpecialOfferProduct> findSpecialOfferProductByPage(@ApiParam(value = "规则id", required=true)String ruleId, 
			@ApiParam(value="页码", required=false) int page,
			@ApiParam(value="每页记录条数", required=false) int pageSize);
}
