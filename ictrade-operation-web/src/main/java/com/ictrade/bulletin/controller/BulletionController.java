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
package com.ictrade.bulletin.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/bulletin")
public class BulletionController {
	
	/**
	 * 公告资讯入口
	 * @return
	 */
	@RequiresPermissions("MENU:VIEW:243")
	@RequestMapping(method = RequestMethod.GET)
	public String list(){
		return "bulletin/bulletinList";
	}

	/**
	 * 新增/编辑公告资讯入口
	 * @return
	 * @since 2017年3月16日
	 * @author zr.wujiajun@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:243")
	@RequestMapping(value={"edit","add"},method = RequestMethod.GET)
	public String edit(){
		return "bulletin/bulletinEdit";
	}
	
	/**
	 * 公告资讯预览
	 * @return
	 */
	@RequiresPermissions("MENU:VIEW:243")
	@RequestMapping(value="{id}", method = RequestMethod.GET)
	public String preView(){
		return "bulletin/bulletinList";
	}
	
}
