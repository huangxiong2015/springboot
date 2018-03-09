/*
 * Created: 2016年12月5日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ictrade.material.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/approveMaterial")
public class MaterialApproveController {
	
	@Value("#{appProps['api.product.serverUrlPrefix']}")
	private String productPort;
	
	/**
	 * 物料管理(审核人)列表
	 * @return
	 */
	@RequiresPermissions("MENU:VIEW:2991")
	@RequestMapping(method = RequestMethod.GET)
	public String materialist(){
		return "basicMaterial/list";
	}
	
	/**
	 * 物料管理(审核人)详情
	 * @return
	 */
	@RequiresPermissions("MENU:VIEW:2991")
	@RequestMapping(value="/detail", method = RequestMethod.GET)
	public String materialdetails(){
		return "basicMaterial/audetail";
	}
	
}
