package com.yikuyi.product.rule.price.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.framewrok.springboot.web.RequestHelper;
import com.github.pagehelper.PageInfo;
import com.yikuyi.product.rule.price.IPriceRulesResource;
import com.yikuyi.product.rule.price.manager.ProductPriceRuleManager;
import com.yikuyi.rule.price.ProductPriceRule;
import com.yikuyi.rule.price.model.PriceRuleTemplate;

@RestController
@RequestMapping("v1/rules")
public class PriceRulesResource implements IPriceRulesResource {
	private static final Logger logger = LoggerFactory.getLogger(PriceRulesResource.class);
	
	/**
	 * 新增定价规则
	 */
	@Autowired
	private ProductPriceRuleManager productPriceRuleManager;
	@Override
	@RequestMapping(value="/price", method=RequestMethod.POST)
	public PriceRuleTemplate add(@RequestBody PriceRuleTemplate priceTemplate){
		String userId = RequestHelper.getLoginUserId();
		productPriceRuleManager.insert(priceTemplate,userId);
		return priceTemplate;
	}

	/**
	 * 修改定价规则
	 */
	@Override
	@RequestMapping(value="/price", method=RequestMethod.PUT)
	public PriceRuleTemplate update(@RequestBody PriceRuleTemplate priceTemplate) {
		String userId = RequestHelper.getLoginUserId();
		productPriceRuleManager.update(priceTemplate,userId);
		return priceTemplate;
	}

	/**
	 * 查询定价规则
	 */
	@Override
	@RequestMapping(value="/price/{id}", method=RequestMethod.GET)
	public PriceRuleTemplate query(@PathVariable String id) {
		return productPriceRuleManager.getById(id);
	}

	/**
	 * 删除定价规则
	 */
	@Override
	@RequestMapping(value="/price/{id}", method=RequestMethod.DELETE)
	public PriceRuleTemplate delete(@PathVariable("id") String ruleId) {
		String userId = RequestHelper.getLoginUserId();
		PriceRuleTemplate info = productPriceRuleManager.getById(ruleId);
		return productPriceRuleManager.updateStatus(ruleId,ProductPriceRule.RuleStatus.DELETED,userId,info);
	}

	/**
	 * 启用、禁用定价规则
	 */
	@Override
	@RequestMapping(value="/price/{id}/status", method=RequestMethod.PUT)
	public PriceRuleTemplate status(@PathVariable("id") String ruleId, @RequestParam(value="status") ProductPriceRule.RuleStatus status) {
		String userId = RequestHelper.getLoginUserId();
		PriceRuleTemplate info = productPriceRuleManager.getById(ruleId);
		return productPriceRuleManager.updateStatus(ruleId,status,userId,info);
	}

	/**
	 * 列表查询定价规则
	 */
	@Override
	@RequestMapping(value="/price", method=RequestMethod.GET)
	public PageInfo<PriceRuleTemplate> queryList(@RequestParam(value="page",defaultValue="1",required=false)int page, 
			@RequestParam(value="size",defaultValue="20",required=false)int size,
			@RequestParam(value="startDate",required=false)String startDate,
			@RequestParam(value="endDate",required=false)String endDate,
			@RequestParam(value="ruleStatus",required=false)String ruleStatus,
			@RequestParam(value="vendorId",required=false)String vendorId,
			@RequestParam(value="ruleName",required=false)String ruleName) {
		try {
			ruleName = StringUtils.isNotEmpty(ruleName) ? URLDecoder.decode(ruleName,"UTF-8").replaceAll("\\s", "") : ruleName;
		} catch (UnsupportedEncodingException e) {
			logger.error("PriceRulesResource.queryList decode ruleName UnsupportedEncodingException!");
		}
		return productPriceRuleManager.findList(startDate,endDate,ruleStatus,vendorId,ruleName,"PRODUCT_PRICE",page,size);
	}

}
