package com.ictrade.activity.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@Controller
@RequestMapping("/promotion")

public class PromotionController {
	private static final Logger logger = LoggerFactory.getLogger(PromotionController.class);
	
	@Value("#{appProps['api.product.serverUrlPrefix']}")
	private String dataPort;

	/**
	 * 促销管理页面
	 * @return
	 */
	@RequiresPermissions("MENU:VIEW:256")
	@RequestMapping(method = RequestMethod.GET)
	public String index(){
		return "promotion/index";
	}
	
	/**
	 * 促销装修页面
	 * @return
	 */
	@RequiresPermissions("MENU:VIEW:256")
	@RequestMapping(value="{promotionId}/decorate",method = RequestMethod.GET)
	public String decorate(){
		return "promotion/decorate";
	}
	
	/**
	 * 促销预览页面
	 * @return
	 */
	@RequiresPermissions("MENU:VIEW:256")
	@RequestMapping(value="{promotionId}/preview", method = RequestMethod.GET)
	public String preview(){
		return "promotion/preview";
	}


	/**
	 * 促销新增页面
	 * @return
	 */
	@RequiresPermissions("MENU:VIEW:256")
	@RequestMapping(value="/edit", method = RequestMethod.GET)
	public String edit(){
		return "promotion/promotionEdit";
	}
	
	/**
	 * 促销编辑页面
	 * @return
	 */
	@RequiresPermissions("MENU:VIEW:256")
	@RequestMapping(value="/edit/{activityId}", method = RequestMethod.GET)
	public String information(@PathVariable(value="activityId") String activityId,ModelMap model){
		model.addAttribute("activityId",activityId);
		return "promotion/promotionEdit";
	}
}
