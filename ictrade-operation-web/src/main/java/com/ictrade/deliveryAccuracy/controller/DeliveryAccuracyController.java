package com.ictrade.deliveryAccuracy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/deliveryAccuracy")
public class DeliveryAccuracyController {
	
	/**
	 * 分销商交期准确率列表
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list(){
		return "deliveryAccuracy/list";
	}
}