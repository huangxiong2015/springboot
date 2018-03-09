package com.yikuyi.product.strategy.impl;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.yikuyi.product.strategy.IStrategyResource;
import com.yikuyi.product.strategy.cache.PackageMailCacheManager;
import com.yikuyi.product.strategy.manager.StrategyManager;
import com.yikuyi.strategy.model.Strategy;
import com.yikuyi.strategy.model.Strategy.StrategyStatus;
import com.yikuyi.strategy.model.Strategy.StrategyType;
import com.yikuyi.strategy.vo.StrategyVo;
import com.ykyframework.exception.BusinessException;

/**
 * 包邮/限购
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@RestController
@RequestMapping("v1/strategy")
public class StrategyResource implements IStrategyResource {
	
	
	@Autowired
	private StrategyManager strategyManager;
	
	@Autowired
	private PackageMailCacheManager packageMailCacheManager;

	/**
	 * 包邮/限购规则列表查询
	 */
	@Override
	@RequestMapping(method = RequestMethod.GET)
	public PageInfo<StrategyVo> list(@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "updateDateStart", required = false) String updateDateStart, 
			@RequestParam(value = "updateDateEnd", required = false) String updateDateEnd, 
			@RequestParam(value = "creatorName", required = false) String creatorName,
			@RequestParam(value = "strategyStatus", required = false) StrategyStatus strategyStatus, 
			@RequestParam(value = "strategyType", required = false) StrategyType strategyType, 
			@RequestParam(value = "page", required = true, defaultValue = "1") int page, 
			@RequestParam(value = "pageSize", required = true, defaultValue = "20") int pageSize) {
			return strategyManager.list(title,updateDateStart,updateDateEnd,creatorName,strategyStatus,strategyType,page,pageSize);
	}

	
	/**
	 * 查询包邮/限购规则详情
	 */
	@Override
	@RequestMapping(value="/{id}",method = RequestMethod.GET)
	public StrategyVo getStrategy(@PathVariable("id") String id) {
		return strategyManager.getStrategy(id);
	}


	/**
	 * 删除包邮/限购信息
	 */
	@Override
	@RequestMapping(value="/{id}",method = RequestMethod.DELETE)
	public void deleteStrategyById(@PathVariable(required=true) String id) throws BusinessException {
		strategyManager.deleteStrategyById(id);
	}

	
	/**
	 * 修改状态
	 */
	@Override
	@RequestMapping(value="/{id}/{strategyStatus}",method = RequestMethod.PUT)
	public void updateStrategyStatus(@PathVariable(required=true) String id,
			@PathVariable(required=true) StrategyStatus strategyStatus) throws BusinessException,ParseException {
		strategyManager.updateStrategyStatus(id,strategyStatus);
	}
	

	/**
	 * 添加包邮/限购模块相关信息
	 * @param strategyId
	 * @param strategy
	 * @since 2018年1月9日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Override
	@RequestMapping(method = RequestMethod.POST)
	public void addStrategy(@RequestParam(value = "strategyId", required = true) String strategyId,
			@RequestBody(required=true)Strategy strategy) throws BusinessException{
		strategyManager.addStrategy(strategyId,strategy);
	}


	/**
	 * 将正式商品表中的数据迁移到草稿表中
	 */
	@Override
	@RequestMapping(value="/cpoyStartegyProductToDraft",method = RequestMethod.POST)
	public void copyStrategyProductToDraft(@RequestParam(value = "strategyId", required = true)String strategyId) throws BusinessException {
		strategyManager.copyStrategyProductToDraft(strategyId);
		
	}


	/**
	 * 编辑包邮/限购模块信息
	 */
	@Override
	@RequestMapping(value="/{id}",method = RequestMethod.PUT)
	public void updateStrategy(@PathVariable(required=true) String id,@RequestBody(required=true)Strategy strategy) throws BusinessException {
		strategyManager.updateStrategy(id,strategy);
	}
	
	
	/**
	 * 定时任务清理策略缓存
	 */
	@Override
	@RequestMapping(value="/cache/refresh",method = RequestMethod.GET)
	public void refreshStrategyCacheTask() {
		packageMailCacheManager.refreshStrategyCacheTask();
	}

}