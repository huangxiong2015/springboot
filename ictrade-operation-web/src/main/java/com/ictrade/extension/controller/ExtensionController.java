package com.ictrade.extension.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/extension")
public class ExtensionController {
	/**
	 * 推广位列表
	 * @return
	 * @since 2017年4月14日
	 * @author guobin@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:253")
	@RequestMapping(method = RequestMethod.GET)
	public String list(){
		return "extension/extensionList";
	}
	
	/**
	 * 推广位编辑
	 * @return
	 * @since 2017年4月14日
	 * @author guobin@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:253")
	@RequestMapping(params="action=toedit",method = RequestMethod.GET)
	public String detail(){
		return "extension/extensionEdit";
	}
}
