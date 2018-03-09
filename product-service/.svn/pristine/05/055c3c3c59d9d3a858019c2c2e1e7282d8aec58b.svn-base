package com.yikuyi.product.rule.mov;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.pagehelper.PageInfo;
import com.yikuyi.rule.mov.vo.MovRuleTemplate;
import com.yikuyi.rule.price.ProductPriceRule;
import com.ykyframework.exception.BusinessException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

public interface IMovRulesResource {
	
	@ApiOperation(value = "新增mov规则 ", notes = "新增mov规则<br/>作者：王洪" ,response = MovRuleTemplate.class)
	@ApiResponses(value ={@ApiResponse(code = 404, message = "mov规则不存在", response = Void.class) })
	@RequestMapping(method=RequestMethod.POST)
	public MovRuleTemplate add(MovRuleTemplate movRuleTemplate)throws BusinessException;
	
	@ApiOperation(value = "删除mov规则", notes = "删除mov规则<br/>作者：王洪" ,response = MovRuleTemplate.class)
	@ApiResponses(value ={@ApiResponse(code = 404, message = "mov规则不存在", response = Void.class) })
	@RequestMapping(method=RequestMethod.DELETE)
	public MovRuleTemplate delete(@ApiParam(value = "规则ID") @PathVariable("ruleId") String ruleId);

	@ApiOperation(value = "修改mov规则", notes = "修改mov规则<br/>作者：王洪" ,response = MovRuleTemplate.class)
	@ApiResponses(value ={@ApiResponse(code = 404, message = "mov规则不存在", response = Void.class) })
	@RequestMapping(method=RequestMethod.PUT)
	public MovRuleTemplate update(MovRuleTemplate movRuleTemplate)throws BusinessException;
	
	@ApiOperation(value = "根据规则ID查询mov规则", notes = "查询mov规则<br/>作者：王洪" ,response = MovRuleTemplate.class)
	@ApiResponses(value ={@ApiResponse(code = 404, message = "mov规则不存在", response = Void.class) })
	@RequestMapping(method=RequestMethod.GET)
	public MovRuleTemplate findById(@ApiParam(value = "规则ID") @PathVariable("ruleId") String ruleId);
	
	@ApiOperation(value = "列表查询mov规则", notes = "列表查询mov规则<br/>作者：王洪" ,response = MovRuleTemplate.class,responseContainer = "List")
	@ApiResponses(value ={@ApiResponse(code = 404, message = "mov规则不存在", response = Void.class) })
	@RequestMapping(method=RequestMethod.GET)
	public PageInfo<MovRuleTemplate> findList(@ApiParam(value = "页码")int page, @ApiParam(value = "页条数")int size,
			@ApiParam(value = "供应商ID")String vendorId); 
	
	@ApiOperation(value = "启用或禁用mov规则", notes = "启用或禁用mov规则<br/>作者：王洪" ,response = MovRuleTemplate.class)
	@ApiResponses(value ={@ApiResponse(code = 404, message = "定价mov不存在", response = Void.class) })
	@RequestMapping(method=RequestMethod.PUT)
	public MovRuleTemplate updateStatus(
			@ApiParam(value = "mov规则模板ID",required=true) String ruleId,
			@ApiParam("模板状态") ProductPriceRule.RuleStatus stauts);
	
}
