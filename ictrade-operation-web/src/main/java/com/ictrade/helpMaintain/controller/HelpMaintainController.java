package com.ictrade.helpMaintain.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
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
@RequestMapping("/helpMaintain")
public class HelpMaintainController {
	
	/**
	 * 帮助中心入口
	 * @return
	 */
	@RequiresPermissions("MENU:VIEW:297")
	@RequestMapping(method = RequestMethod.GET)
	public String list(){
		return "helpMaintain/helpMaintainList";
	}
	@RequiresPermissions("MENU:VIEW:297")
	@RequestMapping(value="/edit", method = RequestMethod.GET)
	public String edit(){
		return "helpMaintain/helpMaintainEdit";
	}

}