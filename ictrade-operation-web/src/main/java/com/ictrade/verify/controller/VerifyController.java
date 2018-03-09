/*
 * Created: 2017年2月6日
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
@RequestMapping("/verify")
public class VerifyController {
	/**
	 * 跳转到企业审核页面
	 * @return
	 * @since 2016年2月6日
	 * @author zr.helinmei@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:252")
	@RequestMapping(method = RequestMethod.GET)
	public String list(ModelMap model){
		String userId = UserInfoUtils.getUserInfo().getId();
		model.put("userId", userId);
		model.put("userName", UserInfoUtils.getUserInfo().getName());
		return "verify/enterpriseList";
	}
	
	/**
	 * 到企业详情页面
	 * @return
	 * @since 2016年12月5日
	 * @author zr.helinmei@yikuyi.com
	 */
	@RequestMapping(params="action=detail",method = RequestMethod.GET)
	public String detail(ModelMap model){
		String userId = UserInfoUtils.getUserInfo().getId();
		model.put("userId", userId);
		model.put("userName", UserInfoUtils.getUserInfo().getName());
		return "verify/enterpriseDetails";
	}
	
	/**
	 * 到企业详情页面
	 * @return
	 * @since 2016年12月5日
	 * @author zr.helinmei@yikuyi.com
	 */
	@RequestMapping(params="action=examine",method = RequestMethod.GET)
	public String examine(ModelMap model){
		String userId = UserInfoUtils.getUserInfo().getId();
		model.put("userId", userId);
		model.put("userName", UserInfoUtils.getUserInfo().getName());
		return "verify/enterpriseExamine";
	}
	/**
	 * 到编辑资质页面
	 * @return
	 * @since 2016年12月5日
	 * @author zr.helinmei@yikuyi.com
	 */
	@RequestMapping(params="action=certified",method = RequestMethod.GET)
	public String toCertified(ModelMap model){
		String userId = UserInfoUtils.getUserInfo().getId();
		model.put("userId", userId);
		model.put("userName", UserInfoUtils.getUserInfo().getName());
		return "verify/enterpriseCertified";
	}
	/**
	 * 到审核资质页
	 * @return
	 * @since 2017年7月31日
	 * @author tb.zhoulixia@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:252")
	@RequestMapping(params="action=examineQualifi",method = RequestMethod.GET)
	public String examineQualifi(ModelMap model){
		String userId = UserInfoUtils.getUserInfo().getId();
		model.put("userId", userId);
		model.put("userName", UserInfoUtils.getUserInfo().getName());
		return "verify/examineQualification";
	}
	/**
	 * 到审核资质详情页
	 * @return
	 * @since 2017年7月31日
	 * @author tb.zhoulixia@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:252")
	@RequestMapping(params="action=examineQualifiDetail",method = RequestMethod.GET)
	public String examineQualifiDetail(ModelMap model){
		String userId = UserInfoUtils.getUserInfo().getId();
		model.put("userId", userId);
		model.put("userName", UserInfoUtils.getUserInfo().getName());
		return "verify/examineQualificationDetail";
	}
	/**
	 * 到审核子账号页
	 * @return
	 * @since 2017年7月31日
	 * @author tb.zhoulixia@yikuyi.com
	 */
	@RequestMapping(params="action=examineSubAccount",method = RequestMethod.GET)
	public String examineSubAccount(ModelMap model){
		String userId = UserInfoUtils.getUserInfo().getId();
		model.put("userId", userId);
		model.put("userName", UserInfoUtils.getUserInfo().getName());
		return "verify/examineSubAccount";
	}
	/**
	 * 到审核子账号详情页
	 * @return
	 * @since 2017年7月31日
	 * @author tb.zhoulixia@yikuyi.com
	 */
	@RequestMapping(params="action=examineSubAccountDetail",method = RequestMethod.GET)
	public String examineSubAccountDetail(ModelMap model){
		String userId = UserInfoUtils.getUserInfo().getId();
		model.put("userId", userId);
		model.put("userName", UserInfoUtils.getUserInfo().getName());
		return "verify/examineSubAccountDetail";
	}
	/**
	 * 到审核账期页
	 * @return
	 * @since 2017年7月31日
	 * @author tb.zhoulixia@yikuyi.com
	 */
	@RequestMapping(params="action=examineAccountPeriod",method = RequestMethod.GET)
	public String examineAccountPeriod(ModelMap model){
		String userId = UserInfoUtils.getUserInfo().getId();
		model.put("userId", userId);
		model.put("userName", UserInfoUtils.getUserInfo().getName());
		return "verify/examineAccountPeriod";
	}
	/**
	 * 到审核账期详情页
	 * @return
	 * @since 2017年7月31日
	 * @author tb.zhoulixia@yikuyi.com
	 */
	@RequestMapping(params="action=examineAccountPeriodDetail",method = RequestMethod.GET)
	public String examineAccountPeriodDetail(ModelMap model){
		String userId = UserInfoUtils.getUserInfo().getId();
		model.put("userId", userId);
		model.put("userName", UserInfoUtils.getUserInfo().getName());
		return "verify/examineAccountPeriodDetail";
	}
	

}