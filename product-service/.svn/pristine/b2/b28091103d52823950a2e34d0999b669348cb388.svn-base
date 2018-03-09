package com.yikuyi.product.rule.logistics.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.framewrok.springboot.web.RequestHelper;
import com.github.pagehelper.PageInfo;
import com.yikuyi.product.rule.logistics.ILogisticsResource;
import com.yikuyi.product.rule.logistics.manager.LogisticsManager;
import com.yikuyi.rule.logistics.vo.LogisticsInfo;
import com.yikuyi.rule.price.ProductPriceRule;

@RestController
@RequestMapping("v1/rules/logistics")
public class LogisticsResource implements ILogisticsResource{
	
	@Autowired
	private LogisticsManager logisticsManager;

	/**
	 * 新增运费模板
	 * @param info
	 * @return
	 * @since 2016年12月10日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Override
	@RequestMapping(method=RequestMethod.POST,produces = "application/json; charset=utf-8")
	public LogisticsInfo addLogistics(@RequestBody LogisticsInfo info) {
		String userId = RequestHelper.getLoginUserId();
		return logisticsManager.addLogistics(info, userId);
	}

	/**
	 * 查询运费模板列表
	 * @return
	 * @since 2016年12月10日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Override
	@RequestMapping(method=RequestMethod.GET)
	public PageInfo<LogisticsInfo> getLogisticsList() {
		return logisticsManager.getLogisticsList();
	}

	/**
	 * 根据运费模块Id查询详情
	 * @param id
	 * @return
	 * @since 2016年12月10日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public LogisticsInfo getLogisticsDetail(@PathVariable("id") String id) {
		return logisticsManager.getLogisticsDetail(id);
	}

	/**
	 * 修改运费模板
	 * @param id
	 * @param info
	 * @return
	 * @since 2016年12月10日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/{id}",method=RequestMethod.PUT)
	public LogisticsInfo updateLogistics(@PathVariable("id") String id, @RequestBody LogisticsInfo info) {
		String userId = RequestHelper.getLoginUserId();
		return logisticsManager.updateLogistics(id, info, userId);
	}

	/**
	 * 启用、停用运费模板
	 * @param id
	 * @param stauts
	 * @since 2016年12月10日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/{id}/status", method=RequestMethod.PUT)
	public LogisticsInfo updateLogisticsStatus(@PathVariable("id") String id, @RequestParam(value="status") ProductPriceRule.RuleStatus stauts) {
		String userId = RequestHelper.getLoginUserId();
		LogisticsInfo info = logisticsManager.getLogisticsDetail(id);
		return logisticsManager.updateLogisticsStatus(id, stauts, userId, info);
	}

	/**
	 * 删除运费模板
	 * @param id
	 * @since 2016年12月10日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public void deleteLogistics(@PathVariable("id") String id) {
		String userId = RequestHelper.getLoginUserId();
		LogisticsInfo info = logisticsManager.getLogisticsDetail(id);
		logisticsManager.deleteLogistics(id, userId, info.getLogisticsRuleName());
	}

}
