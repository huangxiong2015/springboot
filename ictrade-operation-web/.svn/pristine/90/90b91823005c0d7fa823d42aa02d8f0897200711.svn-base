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
package com.ictrade.systemMaintain.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/shipmentlist")
public class SystemMaintainController {
	
	/**
	 * 快递管理
	 * @return
	 */
	@RequiresPermissions("MENU:VIEW:869877523763167232")
	@RequestMapping(method = RequestMethod.GET)
	public String list(){
		return "systemMaintain/shipmentList";
	}
	@RequiresPermissions("MENU:VIEW:869877523763167232")
	@RequestMapping(value="/create",method = RequestMethod.GET)
	public String toadd(){
		return "systemMaintain/addshipment";
	}
	
}
