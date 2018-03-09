package com.yikuyi.product.strategy.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.yikuyi.product.strategy.IStrategyProductResource;
import com.yikuyi.product.strategy.manager.StrategyProductManager;
import com.yikuyi.strategy.model.StrategyProduct;

/**
 * 包邮/限购正式商品
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@RestController
@RequestMapping("v1/strategyProduct")
public class StrategyProductResource implements IStrategyProductResource {
	
	@Autowired
	private StrategyProductManager strategyProductManager;

	/**
	 * 正式商品列表
	 */
	@Override
	@RequestMapping(method = RequestMethod.GET)
	public PageInfo<StrategyProduct> list(@RequestParam(value = "strategyId", required = true)String strategyId, 
			@RequestParam(value = "page", required = false, defaultValue = "1")int page, 
			@RequestParam(value = "pageSize", required = false, defaultValue = "20")int pageSize) {
		return strategyProductManager.list(strategyId,page,pageSize);
	}
	
	
	
	
	

}