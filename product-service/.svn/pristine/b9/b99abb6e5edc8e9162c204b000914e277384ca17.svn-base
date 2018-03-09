package com.yikuyi.product.rule.price;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;
import com.yikuyi.rule.price.ProductPriceRule;
import com.yikuyi.rule.price.model.PriceRuleTemplate;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

public interface IPriceRulesResource {

	@ApiOperation(value = "新增定价规则", notes = "新增定价规则" ,response = PriceRuleTemplate.class)
	@ApiResponses(value ={@ApiResponse(code = 404, message = "定价规则不存在", response = Void.class) })
	@RequestMapping(method=RequestMethod.POST)
	public PriceRuleTemplate add(PriceRuleTemplate priceRuleTemplate);
	
	@ApiOperation(value = "修改定价规则", notes = "修改定价规则" ,response = PriceRuleTemplate.class)
	@ApiResponses(value ={@ApiResponse(code = 404, message = "定价规则不存在", response = Void.class) })
	@RequestMapping(method=RequestMethod.PUT)
	public PriceRuleTemplate update(PriceRuleTemplate priceRuleTemplate);
	
	@ApiOperation(value = "查询定价规则", notes = "查询定价规则" ,response = PriceRuleTemplate.class)
	@ApiResponses(value ={@ApiResponse(code = 404, message = "定价规则不存在", response = Void.class) })
	@RequestMapping(method=RequestMethod.GET)
	public PriceRuleTemplate query(String id);
	
	@ApiOperation(value = "列表查询定价规则", notes = "列表查询定价规则" ,response = PriceRuleTemplate.class,responseContainer = "List")
	@ApiResponses(value ={@ApiResponse(code = 404, message = "定价规则不存在", response = Void.class) })
	@RequestMapping(method=RequestMethod.GET)
	public PageInfo<PriceRuleTemplate> queryList( @ApiParam(value = "页码")int page, @ApiParam(value = "页条数")int size,
			@ApiParam(value = "创建时间-开始时间")String startDate,
			@ApiParam(value = "创建时间-结束时间")String endDate,
			@ApiParam(value = "规则状态，取值DISABLED、ENABLED或为空")String ruleStatus,
			@ApiParam(value = "供应商ID")String vendorId,
			@ApiParam(value = "规则名称")String ruleName);
	
	@ApiOperation(value = "删除定价规则", notes = "删除定价规则" ,response = PriceRuleTemplate.class)
	@ApiResponses(value ={@ApiResponse(code = 404, message = "定价规则不存在", response = Void.class) })
	@RequestMapping(method=RequestMethod.DELETE)
	public PriceRuleTemplate delete(@ApiParam("规则ID")String ruleId);
	
	@ApiOperation(value = "启用或禁用定价规则", notes = "启用或禁用定价规则" ,response = PriceRuleTemplate.class)
	@ApiResponses(value ={@ApiResponse(code = 404, message = "定价规则不存在", response = Void.class) })
	@RequestMapping(method=RequestMethod.PUT)
	public PriceRuleTemplate status(@ApiParam(value = "定价规则模板ID",required=true) @PathVariable("id") String ruleId,
			@ApiParam("模板状态") @RequestParam(value="status",required=true) ProductPriceRule.RuleStatus stauts);
	
}
