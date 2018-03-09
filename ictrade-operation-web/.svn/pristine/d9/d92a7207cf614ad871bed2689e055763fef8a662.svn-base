package com.ictrade.goods.controller;

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
@RequestMapping("/goods")
public class GoodsController {
	
	/**
	 * 到商品列表页面
	 * @return
	 * @since 2016年9月26日
	 * @author guowenyao@yikuyi.com
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list(){
		return "goods/goodsList";
	}
	
	
	
	/**
	 * 到商品详情页面
	 * @return
	 * @since 2016年9月26日
	 * @author guowenyao@yikuyi.com
	 */
	@RequestMapping(params="action=detail",method = RequestMethod.GET)
	public String detail(){
		return "goods/goodsDetails";
	}

}
