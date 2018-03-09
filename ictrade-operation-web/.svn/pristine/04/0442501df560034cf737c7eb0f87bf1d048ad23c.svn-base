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
package com.ictrade.supplier.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/supplier")
public class SupplierController {
	
	/**
	 * 供应商加盟列表
	 * @return
	 * @since 
	 * @author 
	 */
	@RequiresPermissions("MENU:VIEW:987777777777")
	@RequestMapping(method = RequestMethod.GET)
	public String list(){
		return "supplier/supplierList";
	}
	
	/**
	 * 供应商加盟详情
	 * @return
	 * @since 
	 * @author 
	 */
	@RequiresPermissions("MENU:VIEW:987777777777")
	@RequestMapping(value="/detail/{id}", method = RequestMethod.GET)
	public String getDetail(@PathVariable String id ,ModelMap model){
		model.put("id", id);
		return "supplier/supplierDetail";
	}
	
}

