package com.ictrade.user.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/user")
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@RequestMapping(method = RequestMethod.GET)
	@RequiresPermissions("MENU:VIEW:403")
	public String deptList(ModelMap model){
		return "user/userList";
	}
	@RequiresPermissions("MENU:VIEW:403")
	@RequestMapping(params="action=addUser",method = RequestMethod.GET)
	public String add(ModelMap model){
		return "user/addUser";
	}
	@RequiresPermissions("MENU:VIEW:403")
	@RequestMapping(params="action=editUser",method = RequestMethod.GET)
	public String edit(ModelMap model){
		return "user/addUser";
	}
}