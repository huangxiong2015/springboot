/*
 * Created: 2017年3月17日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ictrade.featuredProduct.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
/**
 * 精选商品
 * @author zr.wujiajun@yikuyi.com
 * @version 1.0.0
 */
@Controller
@RequestMapping("/featuredProduct")
public class FeaturedProductController {
	/**
	 * 精选商品列表
	 * @return
	 * @since 2017年3月17日
	 * @author zr.wujiajun@yikuyi.com
	 */	
	@RequestMapping(method=RequestMethod.GET)
	public String list(){
		return "featuredProduct/featuredProductList";
	}
	
	/**
	 * 精选商品编辑
	 * @return
	 * @since 2017年3月17日
	 * @author tb.dengcaiheng@yikuyi.com
	 */
	@RequestMapping(value="/edit", method = RequestMethod.GET)
	public String edit(){
		return "featuredProduct/featuredProductEdit";
	}
	
}
