/*
 * Created: 2016年1月15日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ictrade.common.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ykyframework.api.web.BaseController;

@Controller
@RequestMapping("/index")
public class IndexController extends BaseController {


	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap model) {
		
		return "home/index";
	}
		
	
}
