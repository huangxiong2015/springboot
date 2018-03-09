package com.ictrade.sales.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ictrade.common.utils.UserInfoUtils;

@Controller
public class SalesController {
	@RequiresPermissions("MENU:VIEW:601")
	@RequestMapping(value="/salesVendor",method = RequestMethod.GET)
	public String vendorSalesList(ModelMap model){
		String userId = UserInfoUtils.getUserInfo().getId();
		model.put("userId", userId);
		model.put("userName", UserInfoUtils.getUserInfo().getName());
		return "sales/vendorSalesList";
	}
	
	
	@RequiresPermissions("MENU:VIEW:601")
	@RequestMapping(value="/salesVendor/detail",method = RequestMethod.GET)
	public String vendorSalesDetail(ModelMap model){
		String userId = UserInfoUtils.getUserInfo().getId();
		model.put("userId", userId);
		model.put("userName", UserInfoUtils.getUserInfo().getName());
		return "vendor/vendorDetail";
	}
	
	@RequiresPermissions("MENU:VIEW:601")
	@RequestMapping(value="/salesVendor/edit",method = RequestMethod.GET)
	public String vendorSalesEdit(ModelMap model){
		String userId = UserInfoUtils.getUserInfo().getId();
		model.put("userId", userId);
		model.put("userName", UserInfoUtils.getUserInfo().getName());
		return "vendor/vendorEdit";
	}
}
