package com.yikuyi.product.promotion.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yikuyi.product.promotion.IPromotionModuleResource;
import com.yikuyi.product.promotion.bll.PromotionModuleManager;
import com.yikuyi.promotion.model.PromotionModule;
import com.yikuyi.promotion.vo.PromotionPreviewVo;
@RestController
@RequestMapping("v1/promotionModule")
public class PromotionModuleResource implements IPromotionModuleResource{
	
	
	@Autowired
	private PromotionModuleManager promotionModuleManager;

	/**
	 * 查询促销模块详情
	 */
	@Override
	@RequestMapping(value = "/{promoModuleId}", method = RequestMethod.GET)
	public PromotionModule getPromotionModule(@PathVariable(required = true)String promoModuleId) {
		return promotionModuleManager.getPromotionModule(promoModuleId);
	}

	@Override
	@RequestMapping(value = "/getPromotionDetail/{promotionId}/{promoModuleId}/{promoModuleType}", method = RequestMethod.GET)
	public PromotionPreviewVo getPromotionDetail(@PathVariable(required = true)String promotionId, 
			@PathVariable(required = true)String promoModuleId, 
			@PathVariable(required = true)String promoModuleType,
			@RequestParam(value = "formal", required = false)String formal) {
		if("BANNER".equals(promoModuleType)){
			//如果为y则调用正式数据，否则调用草稿数据
			return promotionModuleManager.getPromotionDetail(promotionId, promoModuleId, promoModuleType, formal);
		}
		return null;
	}
	
	
}