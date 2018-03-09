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
package com.ictrade.distributor.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/distributor")
public class DistributorController {
	/**
	 * 分销商列表
	 * @return
	 * @since 2017年2月15日
	 * @author aoxianbing@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:254")
	@RequestMapping(method = RequestMethod.GET)
	public String list(){
		return "distributor/distributorList";
	}
	
	/**
	 * 分销商新增
	 * @return
	 * @since 2017年2月15日
	 * @author aoxianbing@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:254")
	@RequestMapping(params="action=add",method = RequestMethod.GET)
	public String add(){
		return "distributor/distributorEdit";
	}	
	/**
	 * 分销商详情
	 * @return
	 * @since 2017年2月15日
	 * @author aoxianbing@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:254")
	@RequestMapping(params="action=detail",method = RequestMethod.GET)
	public String detail(@RequestParam(value ="id" , required = true)String id ,ModelMap model){
		model.put("id", id);
		return "distributor/distributorDetail";
	}
	/**
	 * 分销商编辑
	 * @return
	 * @since 2017年2月15日
	 * @author aoxianbing@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:254")
	@RequestMapping(params="action=edit",method = RequestMethod.GET)
	public String edit(@RequestParam(value ="id" , required = false)String id ,ModelMap model){
		model.put("id", id);
		return "distributor/distributorEdit";
	}
}