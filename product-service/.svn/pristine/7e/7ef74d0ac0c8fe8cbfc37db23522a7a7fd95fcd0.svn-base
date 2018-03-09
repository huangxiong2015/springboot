/*
 * Created: 2017年7月10日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.material;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

@FunctionalInterface
public interface IManufactureTransferResource {
	
	@ApiOperation(value = "物料品牌转移", notes = "物料品牌转移", response = String.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	@RequestMapping(method = RequestMethod.GET)
	public String transferManufacture();

}
