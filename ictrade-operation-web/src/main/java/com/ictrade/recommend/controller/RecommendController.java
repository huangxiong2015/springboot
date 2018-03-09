/*
 * Created: 2017年3月16日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ictrade.recommend.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
/**
 * 商品推荐
 * @author zr.wujiajun@yikuyi.com
 * @version 1.0.0
 */
@Controller
@RequestMapping("recommend")
public class RecommendController {
	/**
	 * 商品推荐列表入口
	 * @return
	 * @since 2017年3月16日
	 * @author zr.wujiajun@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:244")
	@RequestMapping(method=RequestMethod.GET)
	public String list(){
		return "recommend/recommendList";
	}
	@RequiresPermissions("MENU:VIEW:244")
	@RequestMapping(value="/edit")
	public String edit(){
		return "recommend/recommendEdit";
	}
}
