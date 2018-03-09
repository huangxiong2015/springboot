package com.ictrade.department.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/department")
public class DepartmentController {
	
	/**
	 * 部门管理
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	@RequiresPermissions("MENU:VIEW:401")
	public String list(){
		return "department/department";
	}
	
}
