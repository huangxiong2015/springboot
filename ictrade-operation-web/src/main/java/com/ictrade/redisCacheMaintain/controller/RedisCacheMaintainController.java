package com.ictrade.redisCacheMaintain.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/*
 * Created: 2017年04月25日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
@Controller
public class RedisCacheMaintainController {
	
	/**
	 * redis缓存管理列表
	 * @return
	 * @since 2017年4月25日
	 * @author zr.xieyuanpeng@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:869877681108287488")
	@RequestMapping(value="/redisList",method = RequestMethod.GET)
	public String list(){
		return "redisCacheMaintain/redisCacheList";
	}
	
	/*后台redis 搜索展示*/
	@RequiresPermissions("MENU:VIEW:869877681108287488")
	@RequestMapping(value="/redisCacheDetail",method = RequestMethod.GET)
	public String redisList(ModelMap model){
		return "redisCacheMaintain/redisCacheDetail";
	}

}