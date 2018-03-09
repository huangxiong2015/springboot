package com.ictrade.manufacturer.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/manufacturer")
public class ManufacturerController {
	
	/**
	 * 制造商列表入口
	 * @return
	 * @since 2017年3月15日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:299")
	@RequestMapping(method = RequestMethod.GET)
	public String list(){
		return "manufacturer/manufacturerList";
	}
	
	/**
	 * 制造商详情入口
	 * @return
	 * @since 2017年3月15日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:299")
	@RequestMapping(value="/detail", method = RequestMethod.GET)
	public String detail(){
		return "manufacturer/manufacturerDetail";
	}
	
	/**
	 * 新增/编辑制造商入口
	 * @return
	 * @since 2017年3月15日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:299")
	@RequestMapping(value="/edit", method = RequestMethod.GET)
	public String add(){
		return "manufacturer/addManufacturer";
	}

}
