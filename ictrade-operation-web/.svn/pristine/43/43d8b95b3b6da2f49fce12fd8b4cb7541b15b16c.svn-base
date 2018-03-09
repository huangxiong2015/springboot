package com.ictrade.vendor.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ictrade.common.utils.UserInfoUtils;

@Controller
public class VendorController {
	@RequiresPermissions("MENU:VIEW:501")
	@RequestMapping(value="/vendor",method = RequestMethod.GET)
	public String deptList(ModelMap model){
		String userId = UserInfoUtils.getUserInfo().getId();
		model.put("userId", userId);
		model.put("userName", UserInfoUtils.getUserInfo().getName());
		return "vendor/vendorList";
	}
	
	@RequiresPermissions("MENU:VIEW:501")
	@RequestMapping(value="/vendor/create",method = RequestMethod.GET)
	public String add(ModelMap model){
		return "vendor/vendorCreate";
	}
	
	@RequiresPermissions("MENU:VIEW:501")
	@RequestMapping(value="/vendor/detail",method = RequestMethod.GET)
	public String getDetail(ModelMap model){
		String userId = UserInfoUtils.getUserInfo().getId();
		model.put("userId", userId);
		model.put("userName", UserInfoUtils.getUserInfo().getName());
		return "vendor/vendorDetail";
	}
	
	@RequiresPermissions("MENU:VIEW:501")
	@RequestMapping(value="/vendor/edit",method = RequestMethod.GET)
	public String vendorEdit(ModelMap model){
		String userId = UserInfoUtils.getUserInfo().getId();
		model.put("userId", userId);
		model.put("userName", UserInfoUtils.getUserInfo().getName());
		return "vendor/vendorEdit";
	}
	
	/**
	 * 供应商审核入口
	 * @return
	 * @since 2017年8月18日
	 * @author zr.xieyuanpeng@yikuyi.com
	 */
	/*@RequestMapping(value="/auditVendor",method = RequestMethod.GET)
	public String vendorAuditList(ModelMap model){
		return "vendor/vendorAuditList";
	}*/
	@RequiresPermissions("MENU:VIEW:502")
	@RequestMapping(value="/auditVendor/baseinfo",method = RequestMethod.GET)
	public String baseInfoAudit(ModelMap model){
		String userId = UserInfoUtils.getUserInfo().getId();
		model.put("userId", userId);
		model.put("userName", UserInfoUtils.getUserInfo().getName());
		return "vendor/baseInfoAudit";
	}
	@RequiresPermissions("MENU:VIEW:502")
	@RequestMapping(value="/auditVendor/archiving",method = RequestMethod.GET)
	public String archivingAudit(ModelMap model){
		String userId = UserInfoUtils.getUserInfo().getId();
		model.put("userId", userId);
		model.put("userName", UserInfoUtils.getUserInfo().getName());
		return "vendor/archivingAudit";
	}
	@RequiresPermissions("MENU:VIEW:502")
	@RequestMapping(value="/auditVendor/productLine",method = RequestMethod.GET)
	public String productLineAudit(ModelMap model){
		String userId = UserInfoUtils.getUserInfo().getId();
		model.put("userId", userId);
		model.put("userName", UserInfoUtils.getUserInfo().getName());
		return "vendor/productLineAudit";
	}
	@RequiresPermissions("MENU:VIEW:502")
	@RequestMapping(value="/auditVendor/enable",method = RequestMethod.GET)
	public String enableOrDisableAudit(ModelMap model){
		String userId = UserInfoUtils.getUserInfo().getId();
		model.put("userId", userId);
		model.put("userName", UserInfoUtils.getUserInfo().getName());
		return "vendor/enableOrDisableAudit";
	}
	
	/**
	 * 供应商审核管理管理列表
	 * @param model
	 * @return
	 * @since 2017年8月24日
	 * @author zr.zhanghua@yikuyi.com
	 */
	@RequestMapping(value="/vendorManageList",method = RequestMethod.GET)
	public String queryVendorMaList(ModelMap model){
		return "vendor/vendorList";
	}
	
	/**
	 * 供应审核理列表
	 * @param model
	 * @return
	 * @since 2017年8月24日
	 * @author zr.zhanghua@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:502")
	@RequestMapping(value="/auditVendor",method = RequestMethod.GET)
	public String queryVendorCheckList(ModelMap model){
		return "vendor/vendorCheckList";
	}
	
	/**
	 * 我的供应审核理列表
	 * @param model
	 * @return
	 * @since 2017年8月24日
	 * @author zr.zhanghua@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:503")
	@RequestMapping(value="/myVendor",method = RequestMethod.GET)
	public String myVendorList(ModelMap model){
		String userId = UserInfoUtils.getUserInfo().getId();
		model.put("userId", userId);
		model.put("userName", UserInfoUtils.getUserInfo().getName());
		return "vendor/myVendorList2";
	}
	/**
	 * 我的基本信息申请详情
	 * @param model
	 * @return
	 * @since 2017年9月27日
	 * @author zr.xieyuanpeng@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:503")
	@RequestMapping(value="/myVendor/baseinfo",method = RequestMethod.GET)
	public String myBaseInfoApply(ModelMap model){
		String userId = UserInfoUtils.getUserInfo().getId();
		model.put("userId", userId);
		model.put("userName", UserInfoUtils.getUserInfo().getName());
		return "vendor/baseInfoAudit";
	}
	@RequiresPermissions("MENU:VIEW:503")
	@RequestMapping(value="/myVendor/archiving",method = RequestMethod.GET)
	public String myArchivingApply(ModelMap model){
		String userId = UserInfoUtils.getUserInfo().getId();
		model.put("userId", userId);
		model.put("userName", UserInfoUtils.getUserInfo().getName());
		return "vendor/archivingAudit";
	}
	@RequiresPermissions("MENU:VIEW:503")
	@RequestMapping(value="/myVendor/productLine",method = RequestMethod.GET)
	public String myProductLineApply(ModelMap model){
		String userId = UserInfoUtils.getUserInfo().getId();
		model.put("userId", userId);
		model.put("userName", UserInfoUtils.getUserInfo().getName());
		return "vendor/productLineAudit";
	}
	@RequiresPermissions("MENU:VIEW:503")
	@RequestMapping(value="/myVendor/enable",method = RequestMethod.GET)
	public String myEnableOrDisableApply(ModelMap model){
		String userId = UserInfoUtils.getUserInfo().getId();
		model.put("userId", userId);
		model.put("userName", UserInfoUtils.getUserInfo().getName());
		return "vendor/enableOrDisableAudit";
	}
}
