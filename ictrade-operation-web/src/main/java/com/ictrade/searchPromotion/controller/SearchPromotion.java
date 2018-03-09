/*
 * Created: 2017年5月2日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ictrade.searchPromotion.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/searchPromotion")
public class SearchPromotion {
	
	/**
	 * 搜索推广列表
	 * @return
	 * @since 2017年5月2日
	 * @author tb.dengcaiheng@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:862610523017969664")
	@RequestMapping(method=RequestMethod.GET)
	public String list(){
		return "searchPromotion/searchPromotionList";
	}
	
	
	/**
	 * 新增/编辑搜索推广入口
	 * @return
	 * @since 2017年5月2日
	 * @author tb.dengcaiheng@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:862610523017969664")
	@RequestMapping(value="/edit", method = RequestMethod.GET)
	public String edit(){
		return "searchPromotion/searchPromotionEdit";
	}
	
	/**
	 * 新增/编辑搜索推广入口
	 * @return
	 * @since 2017年5月2日
	 * @author tb.dengcaiheng@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:862610523017969664")
	@RequestMapping(value="/detail", method = RequestMethod.GET)
	public String detail(){
		return "searchPromotion/searchPromotionDetail";
	}
}