package com.ictrade.maintenanceData.controller;

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
@RequestMapping("/maintenance")
public class MaintenanceDataController {
	
	/**
	 * 数据校准
	 * @return
	 */
	@RequiresPermissions("MENU:VIEW:3002")
	@RequestMapping(method = RequestMethod.GET)
	public String brand(){
		return "maintenanceData/brandData";
	}
	@RequiresPermissions("MENU:VIEW:3002")
	@RequestMapping(value="/category", method = RequestMethod.GET)
	public String category(){
		return "maintenanceData/categoryData";
	}

}