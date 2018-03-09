package com.yikuyi.product.strategy.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.yikuyi.product.strategy.IStrategyProductDraftResource;
import com.yikuyi.product.strategy.manager.StrategyProductDraftManager;
import com.yikuyi.strategy.model.StrategyProductDraft;
import com.yikuyi.strategy.model.Strategy.StrategyType;

/**
 * 包邮/限购草稿商品
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@RestController
@RequestMapping("v1/strategyProductDraft")
public class StrategyProductDraftResource implements IStrategyProductDraftResource {
	
	@Autowired
	private StrategyProductDraftManager strategyProductDraftManager;

	/**
	 * 草稿商品列表
	 */
	@Override
	@RequestMapping(method = RequestMethod.GET)
	public PageInfo<StrategyProductDraft> list(@RequestParam(value = "strategyId", required = true)String strategyId, 
			@RequestParam(value = "page", required = false, defaultValue = "1")int page, 
			@RequestParam(value = "pageSize", required = false, defaultValue = "20")int pageSize) {
		return strategyProductDraftManager.list(strategyId,page,pageSize);
	}
	
	/**
	 * 删除草稿信息
	 * @param ids
	 * @since 2018年1月8日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/delete",method = RequestMethod.DELETE)
	public void deleteStrategyProductDraft(@RequestBody(required = true)List<String> ids,
			@RequestParam(value = "strategyType", required = false) StrategyType strategyType){
		strategyProductDraftManager.deleteBatch(ids,strategyType);
	}
	
	
	/**
	 * 添加草稿商品信息
	 * @param productDraft
	 * @since 2018年1月8日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Override
	@RequestMapping(method = RequestMethod.POST)
	public void addStrategyProductDraft(@RequestBody StrategyProductDraft productDraft){
		strategyProductDraftManager.addStrategyProductDraft(productDraft);
	}

}