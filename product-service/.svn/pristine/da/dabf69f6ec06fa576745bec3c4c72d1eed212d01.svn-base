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
package com.yikuyi.product.specialoffer.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.yikuyi.product.specialoffer.ISpecialOfferProductResource;
import com.yikuyi.product.specialoffer.manager.SpecialOfferProductManager;
import com.yikuyi.specialoffer.model.SpecialOfferProduct;

@RestController
@RequestMapping("v1/specialOfferProduct")
public class SpecialOfferProductResource implements ISpecialOfferProductResource{
	@Autowired
	private SpecialOfferProductManager specialOfferProductManager;
	
	/**
	 * 查询专属特价商品数据
	 */
	@Override
	@RequestMapping(value="/getStandard",method = RequestMethod.GET)
	public PageInfo<SpecialOfferProduct> findSpecialOfferProductByPage(@RequestParam(value = "ruleId", required = true)String ruleId, 
			@RequestParam(value = "page", required = false, defaultValue = "1")int page, 
			@RequestParam(value = "pageSize", required = false, defaultValue = "20")int pageSize) {
		return specialOfferProductManager.findSpecialOfferProductByPage(ruleId, page, pageSize);
	}

}
