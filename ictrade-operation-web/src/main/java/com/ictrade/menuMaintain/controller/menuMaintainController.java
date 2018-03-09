package com.ictrade.menuMaintain.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/*
 * Created: 2017年4月12日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017
 * License, Version 1.2.4 Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
@Controller
public class menuMaintainController {
	
	/**
	 * 菜单维护,menuTypeId分类页面
	 * @return
	 */
	@RequiresPermissions("MENU:VIEW:300")
	@RequestMapping(value="menuMaintain",method = RequestMethod.GET)
	public String list(){
		return "menuMaintain/menuMaintainList";
	}
	
	/**
	 * 菜单维护，同一menuTypeId菜单的维护
	 * @return
	 */
	@RequiresPermissions("MENU:VIEW:300")
	@RequestMapping(value="/menuMaintainList",method = RequestMethod.GET)
	public String subList(){
		return "menuMaintain/menuMaintainSubList";
	}
	/*@RequestMapping(value="/edit", method = RequestMethod.GET)
	public String edit(){
		return "menuMaintain/menuMaintainEdit";
	}*/

}