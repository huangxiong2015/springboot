package com.ictrade.crawler.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/crawler")
public class CrawlerController {
	
	/**
	 * 数据爬取菜单入口
	 * @return
	 * @since 2017年3月15日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:860437042385911808")
	@RequestMapping(method = RequestMethod.GET)
	public String list(){
		return "crawler/detail";
	}
	
}
