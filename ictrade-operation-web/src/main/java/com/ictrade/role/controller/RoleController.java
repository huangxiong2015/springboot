package com.ictrade.role.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/role")
public class RoleController {
	private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

	@RequestMapping(method = RequestMethod.GET)
	@RequiresPermissions("MENU:VIEW:402")
	public String deptList(ModelMap model){
		return "role/roleList";
	}
	@RequiresPermissions("MENU:VIEW:402")
	@RequestMapping(params="action=addRole",method = RequestMethod.GET)
	public String add(ModelMap model){
		return "role/addRole";
	}
}
