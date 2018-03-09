package com.yikuyi.product.specialoffer.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yikuyi.product.specialoffer.ISpecialOfferResource;
import com.yikuyi.product.specialoffer.manager.SpecialOfferManager;
import com.yikuyi.specialoffer.model.SpecialOffer;
import com.yikuyi.specialoffer.model.SpecialOffer.RuleStatus;

/**
 * 专属特价
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@RestController
@RequestMapping("v1/specialOffer")
public class SpecialOfferResource implements ISpecialOfferResource {
	
	@Autowired
	private SpecialOfferManager specialOfferManager;

	/**
	 * 专属特价信息详情
	 */
	@Override
	@RequestMapping(method = RequestMethod.GET)
	public SpecialOffer getSpecialOffer(@RequestParam(value = "id", required = true)String id){
		return specialOfferManager.getSpecialOffer(id);
	}
	
	/**
	 * 编辑文案内容
	 */
	@Override
	@RequestMapping(value="/updateRuleText", method = RequestMethod.POST)
	public String editRuleText(@RequestParam(value = "id", required = true)String id,
			@RequestParam(value = "ruleText", required = true)String ruleText) {
		return specialOfferManager.editRuleText(id,ruleText);
	}
	
	

	/**
	 * 编辑状态
	 */
	@Override
	@RequestMapping(value="/updateRuleStatus", method = RequestMethod.POST)
	public String editRuleStatus(@RequestParam(value = "id", required = true)String id, 
			@RequestParam(value = "ruleStatus", required = true) RuleStatus ruleStatus,
			@RequestParam(value = "statusName", required = true) String statusName) {
		return specialOfferManager.editRuleStatus(id,ruleStatus,statusName);
	}
	
	

}