package com.yikuyi.product.promotion.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yikuyi.product.promotion.IPromotionModuleDraftResource;
import com.yikuyi.product.promotion.bll.PromotionModuleDraftManager;
import com.yikuyi.promotion.model.PromotionModuleContentDraft;
import com.yikuyi.promotion.model.PromotionModuleDraft;
@RestController
@RequestMapping("v1/promotionModuleDraft")
public class PromotionModuleDraftResource implements IPromotionModuleDraftResource{
	@Autowired
	private PromotionModuleDraftManager promotionDraftManager;
	/**
	 * 创建模块保存草稿
	 * @param PromotionModuleDraft
	 * @return
	 * @since 2017年10月10日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(method = RequestMethod.POST)
	public void insert(@RequestBody PromotionModuleDraft moduleDraft) {
		promotionDraftManager.insert(moduleDraft);
	}
	
	/**
	 * 编辑模块草稿
	 * @param PromotionModuleDraft
	 * @return
	 * @since 2017年10月10日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(method = RequestMethod.PUT)
	public void save(@RequestBody PromotionModuleContentDraft moduleDraft) {
		promotionDraftManager.save(moduleDraft);
	}

	@Override
	@RequestMapping(value ="/orderSeq", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public void promotinModuleOrderSeq(@RequestBody List<String> ids) {
		promotionDraftManager.promotinModuleOrderSeq(ids);
	}
	
	/**
	 * 删除促销活动模块数据
	 */
	@Override
	@RequestMapping(value = "/{promoModuleId}", method = RequestMethod.DELETE)
	public void deletePromotionModuleAndDraft(@PathVariable(required = true)String promoModuleId) {
		promotionDraftManager.deletePromotionModuleAndDraft(promoModuleId);
	}

	/**
	 * 查询促销模块草稿详情
	 */
	@Override
	@RequestMapping(value = "/{promoModuleId}/draft", method = RequestMethod.GET)
	public PromotionModuleDraft getPromotionModuleDraft(@PathVariable(required = true)String promoModuleId) {
		return promotionDraftManager.getPromotionModuleDraft(promoModuleId);
	}

	
}