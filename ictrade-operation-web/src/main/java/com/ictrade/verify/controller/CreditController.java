/*
 * Created: 2017年10月30日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ictrade.verify.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ictrade.common.utils.UserInfoUtils;
@Controller
@RequestMapping("/credit")
public class CreditController {
	/**
	 * 到审核账期详情页
	 * @return
	 * @since 2017年10月26日
	 * @author zr.helinmei@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:9666665555")
	@RequestMapping(method = RequestMethod.GET)
	public String examineCredit(ModelMap model){
		String userId = UserInfoUtils.getUserInfo().getId();
		model.put("userId", userId);
		model.put("userName", UserInfoUtils.getUserInfo().getName());
		return "verify/enterpriseCreditList";
	}
	
	/**
	 * 到审核账期详情页
	 * @return
	 * @since 2017年11月3日
	 * @author chenhong@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:9666665555")
	@RequestMapping(params="action=examineAccountPeriodDetail",method = RequestMethod.GET)
	public String examineAccountPeriodDetail(ModelMap model){
		String userId = UserInfoUtils.getUserInfo().getId();
		model.put("userId", userId);
		model.put("userName", UserInfoUtils.getUserInfo().getName());
		return "verify/examineAccountPeriodDetail";
	}
	
	/**
	 * 到审核账期页
	 * @return
	 * @since 2017年11月3日
	 * @author chenhong@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:9666665555")
	@RequestMapping(params="action=examineAccountPeriod",method = RequestMethod.GET)
	public String examineAccountPeriod(ModelMap model){
		String userId = UserInfoUtils.getUserInfo().getId();
		model.put("userId", userId);
		model.put("userName", UserInfoUtils.getUserInfo().getName());
		return "verify/examineAccountPeriod";
	}
}
