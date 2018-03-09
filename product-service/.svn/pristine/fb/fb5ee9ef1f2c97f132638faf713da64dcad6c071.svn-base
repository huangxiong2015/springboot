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
package com.yikuyi.product.goods;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yikuyi.rule.mov.vo.MovInfo;
import com.yikuyi.rule.mov.vo.MovValidationParam;
import com.yikuyi.rule.mov.vo.MovValidationResult;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

public interface IMovQueryResource {
	@ApiOperation(value = "实时查询商品MOV信息", notes = "作者：王洪<br/>实时查询商品MOV信息" ,response = MovInfo.class,responseContainer = "List")
	@ApiResponses(value ={@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	@RequestMapping(method=RequestMethod.POST)
	public List<MovInfo> queryByIds(@ApiParam("商品的id数组") List<String> ids);
	
	@ApiOperation(value = "批量校验MOV信息", notes = "作者：王洪<br/>批量校验MOV信息" ,response = MovValidationResult.class)
	@ApiResponses(value ={@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	@RequestMapping(method=RequestMethod.POST)
	public MovValidationResult validMov(@ApiParam("MOV校验参数实体")List<MovValidationParam> movValidationParams);
}
