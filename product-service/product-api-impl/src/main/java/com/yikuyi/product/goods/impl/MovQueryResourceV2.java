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
package com.yikuyi.product.goods.impl;

import java.util.List;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yikuyi.product.goods.IMovQueryResourceV2;
import com.yikuyi.product.goods.manager.MovQueryManagerV2;
import com.yikuyi.rule.mov.vo.MovInfo;
import com.yikuyi.rule.mov.vo.MovValidResult;
import com.yikuyi.rule.mov.vo.MovValidationParam;
import com.ykyframework.exception.BusinessException;

@RestController
@RequestMapping("v2/products/batch")
public class MovQueryResourceV2 implements IMovQueryResourceV2{

	@Autowired
	private MovQueryManagerV2 movQueryManager;
	
	/**
	 *批量根据ID集合查询mov信息
	 */
	@Override
	@RequestMapping(value="/mov", method=RequestMethod.POST)
	@Produces({MediaType.APPLICATION_JSON})
	public List<MovInfo> queryByIds(@RequestBody List<String> ids) {
		return movQueryManager.queryByIds(ids);
	}
	
	/**
	 * 批量校验mov信息
	 * @throws BusinessException 
	 */
	@Override
	@RequestMapping(value="/mov/valid", method=RequestMethod.POST)
	@Produces({MediaType.APPLICATION_JSON})
	public List<MovValidResult> validMov(@RequestBody List<MovValidationParam> movValidationParams) throws BusinessException {
		return movQueryManager.validMov(movValidationParams);
	}

}
