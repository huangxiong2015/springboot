/*
 * Created: 2017年7月28日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ictrade.certification.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ictrade.common.utils.UserInfoUtils;
import com.ykyframework.exception.SystemException;

@Controller
@RequestMapping("/certificationEntRz")
public class CertificationRzController {
	private static final Logger logger = LoggerFactory.getLogger(CertificationRzController.class);
	@Value("#{appProps['api.party.serverUrlPrefix']}")
	private String dataPort;
	/**
	 * 认证企业管理列表-认证员
	 * @return
	 * @since 2017年7月28日
	 * @author tb.liushuisheng@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:9999966555")
	@RequestMapping(method = RequestMethod.GET)
	public String list(){
		return "enterprise/cerEnterpriseListRz";
	}
    
    /**
	 * 认证企业详情-认证员
	 * @return
	 * @since 2017年7月28日
	 * @author tb.liushuisheng@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:9999966555")
	@RequestMapping(params="action=detail",method = RequestMethod.GET)
	public String cerEntDetail(@RequestParam(value ="id" , required = true)String id ,ModelMap model){
		model.put("id", id);
		String userId = UserInfoUtils.getUserInfo().getId();
		model.put("userId", userId);
		return "enterprise/cerEnterpriseDetail";
	}
	
	/**
	 * 认证企业详情(子账号管理)-认证员
	 * @return
	 * @since 2017年7月28日
	 * @author tb.liushuisheng@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:9999966555")
	@RequestMapping(params="action=detailSonAccount",method = RequestMethod.GET)
	public String cerEntDetailSonAccount(@RequestParam(value="id" , required = true)String id ,ModelMap model){
		model.put("id", id);
		String userId = UserInfoUtils.getUserInfo().getId();
		model.put("userId", userId);
		return "enterprise/cerEnterpriseDetailSon";
	}
	
	/**
	 * 认证企业详情(账期管理)-认证员
	 * @return
	 * @since 2017年7月28日
	 * @author tb.liushuisheng@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:9999966555")
	@RequestMapping(params="action=detailAccountPeriod",method = RequestMethod.GET)
	public String detailAccountPeriod(@RequestParam(value="id" , required = true)String id ,ModelMap model){
		model.put("id", id);
		String userId = UserInfoUtils.getUserInfo().getId();
		model.put("userId", userId);
		model.put("userName", UserInfoUtils.getUserInfo().getName());
		return "enterprise/cerEnterpriseDetailAccountPeriod";
	}
	
	/**
	 * 认证企业编辑-认证员
	 * @return
	 * @since 2017年7月28日
	 * @author tb.liushuisheng@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:9999966555")
	@RequestMapping(params="action=edit",method = RequestMethod.GET)
	public String cerEntEdit(@RequestParam(value ="id" , required = true)String id ,ModelMap model){
		model.put("id", id);
		String userId = UserInfoUtils.getUserInfo().getId();
		model.put("userId", userId);
		return "enterprise/cerEnterpriseEditRz";
	}
}