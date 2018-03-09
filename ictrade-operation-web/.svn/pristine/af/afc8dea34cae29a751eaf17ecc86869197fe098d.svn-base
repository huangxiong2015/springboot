package com.ictrade.manufacturerManage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/manageManufacturer")
public class ManufacturerManageController {
	
	/**
	 * 运维管理-制造商管理列表入口
	 * @return
	 * @since 2017年10月31日
	 * @author zhoulixia@yikuyi.com
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list(){
		return "manageManufacturer/manufacturerList";
	}
	
	/**
	 * 运维管理-制造商详情入口
	 * @return
	 * @since 2017年10月31日
	 * @author zhoulixia@yikuyi.com
	 */
	@RequestMapping(value="/detail", method = RequestMethod.GET)
	public String detail(){
		return "manageManufacturer/manufacturerDetail";
	}
	
	/**
	 * 运维管理-新增/编辑制造商入口
	 * @return
	 * @since 2017年10月31日
	 * @author zhoulixia@yikuyi.com
	 */
	@RequestMapping(value="/edit", method = RequestMethod.GET)
	public String add(){
		return "manageManufacturer/manufacturerEdit";
	}

}
