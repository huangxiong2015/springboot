package com.ictrade.advertisement.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/advertisement")
public class AdvertisementController {
	
	/**
	 * 广告位列表入口
	 * @return
	 * @since 2017年5月26日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:255")
	@RequestMapping(method = RequestMethod.GET)
	public String list(){
		return "advertisement/advertisementList";
	}
	
	/**
	 * 广告位详情入口
	 * @return
	 * @since 2017年5月26日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:255")
	@RequestMapping(value="/detail", method = RequestMethod.GET)
	public String detail(){
		return "advertisement/advertisementDetail";
	}
	
	/**
	 * 
	 * @return
	 * @since 2017年5月26日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:255")
	@RequestMapping(value="/edit", method = RequestMethod.GET)
	public String add(){
		return "advertisement/advertisementEdit";
	}
	
	/**
	 * 
	 * @return
	 * @since 2017年10月26日
	 * @author zr.xieyuanpeng@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:255")
	@RequestMapping(value="classifiedAds/detail", method = RequestMethod.GET)
	public String classifiedAdsDetail(){
		return "advertisement/classifiedAdsDetail";
	}
	
	/**
	 * 
	 * @return
	 * @since 2017年10月26日
	 * @author zr.xieyuanpeng@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:255")
	@RequestMapping(value="/classifiedAdsList", method = RequestMethod.GET)
	public String classifiedAdsList(){
		return "advertisement/classifiedAdsList";
	}
	
	/**
	 * 
	 * @return
	 * @since 2017年10月26日
	 * @author zr.xieyuanpeng@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:255")
	@RequestMapping(value="classifiedAds/edit", method = RequestMethod.GET)
	public String classifiedAdsAdd(){
		return "advertisement/classifiedAdsEdit";
	}
}
