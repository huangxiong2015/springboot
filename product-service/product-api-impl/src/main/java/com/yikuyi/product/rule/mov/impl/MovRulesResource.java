package com.yikuyi.product.rule.mov.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.framewrok.springboot.web.RequestHelper;
import com.github.pagehelper.PageInfo;
import com.yikuyi.product.rule.mov.IMovRulesResource;
import com.yikuyi.product.rule.mov.manager.MovRulesManager;
import com.yikuyi.rule.mov.vo.MovRuleTemplate;
import com.yikuyi.rule.price.ProductPriceRule;

@RestController
@RequestMapping("v1/rules")
public class MovRulesResource implements IMovRulesResource {

	@Autowired
	private MovRulesManager movRulesManager;
	
	
	/**
	 * 新增mov策略
	 */
	@Override
	@RequestMapping(value="/mov", method=RequestMethod.POST)
	public MovRuleTemplate add(@RequestBody MovRuleTemplate movRuleTemplate) {
		String userId = RequestHelper.getLoginUserId();
		movRulesManager.insert(movRuleTemplate, userId);
		return movRuleTemplate;
	}

	/**
	 * 删除mov策略
	 */
	@Override
	@RequestMapping(value="/mov/{ruleId}", method=RequestMethod.DELETE)
	public MovRuleTemplate delete(@PathVariable("ruleId") String ruleId) {
		String userId = RequestHelper.getLoginUserId();
		MovRuleTemplate movRuleTemplate = movRulesManager.getById(ruleId);
		return movRulesManager.updateStatus(ruleId,ProductPriceRule.RuleStatus.DELETED,userId,movRuleTemplate);
	}

	/**
	 * 修改mov策略
	 */
	@Override
	@RequestMapping(value="/mov", method=RequestMethod.PUT)
	public MovRuleTemplate update(@RequestBody MovRuleTemplate movRuleTemplate) {
		String userId = RequestHelper.getLoginUserId();
		movRulesManager.update(movRuleTemplate, userId);
		return movRuleTemplate;
	}

	/**
	 * 根据策略ID查询策略模板
	 */
	@Override
	@RequestMapping(value="/mov/{ruleId}", method=RequestMethod.GET)
	public MovRuleTemplate findById(@PathVariable("ruleId")String ruleId) {
		return movRulesManager.getById(ruleId);
	}

	/**
	 * 列表查询mov策略
	 */
	@Override
	@RequestMapping(value="/mov", method=RequestMethod.GET)
	public PageInfo<MovRuleTemplate> findList(@RequestParam(value="page",defaultValue="1",required=false)int page, 
			@RequestParam(value="size",defaultValue="20",required=false)int size,
			@RequestParam(value="vendorId",required=false)String vendorId) {
		//return movRulesManager.findList(startDate, endDate, ruleStatus, vendorId, brandId, ruleName, PricePurposeType.MOV.toString(), page, size);
	  return null;
	}


	/**
	 * 启用、禁用或删除mov策略
	 */
	@Override
	@RequestMapping(value="/mov/{ruleId}/status", method=RequestMethod.PUT)
	public MovRuleTemplate updateStatus(@PathVariable("ruleId") String ruleId, 
			@RequestParam(value="status",required=true) ProductPriceRule.RuleStatus status) {
		String userId = RequestHelper.getLoginUserId();
		MovRuleTemplate info = movRulesManager.getById(ruleId);
		return movRulesManager.updateStatus(ruleId,status,userId,info);
	}

}
