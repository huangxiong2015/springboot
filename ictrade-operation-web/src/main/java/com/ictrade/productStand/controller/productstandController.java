package com.ictrade.productStand.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/*
 * Created: 2016年9月26日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
@Controller
@RequestMapping("/productstand")
public class productstandController {
	
	/**
	 * 到新增特殊SPU管理列表
	 * @return
	 * @since 2017年12月11日
	 * @author chenhong@yikuyi.com
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list(){
		return "productstand/productstandList";
	}
	
	
	
	/**
	 * 到新增特殊SPU管理新增
	 * @return
	 * @since 2017年12月11日
	 * @author chenhong@yikuyi.com
	 */
	@RequestMapping(value={"edit","add"},method = RequestMethod.GET)
	public String productstandedit(){
		return "productstand/productstandedit";
	}

}