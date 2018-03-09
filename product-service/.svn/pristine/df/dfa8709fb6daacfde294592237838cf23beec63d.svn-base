package com.yikuyi.rule.price.model;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("PriceRuleDetil")
public class PriceRuleDetail extends AbstractEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2914195929627605715L;

	@ApiModelProperty(value="详细价格规则名称",example="ORIGINAL_COST,REAL_COST,RESALE_PRICE,SPECIAL_RESALE_PRICE")
	private String ruleActionName;
	
	@ApiModelProperty(value="交货地")
	private String deliveryPlace;
	
	@ApiModelProperty(value="规则类型,PRICE_FLAT按百分数,PRICE_POL按固定金额")
	private String ruleType;
	
	@ApiModelProperty(value="计算类型")
	private String calculateType;
	
	@ApiModelProperty(value="规则参数")
	private String calculateValue;
	
	public String getRuleActionName() {
		return ruleActionName;
	}

	public void setRuleActionName(String ruleActionName) {
		this.ruleActionName = ruleActionName;
	}

	public String getDeliveryPlace() {
		return deliveryPlace;
	}

	public void setDeliveryPlace(String deliveryPlace) {
		this.deliveryPlace = deliveryPlace;
	}

	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	public String getCalculateType() {
		return calculateType;
	}

	public void setCalculateType(String calculateType) {
		this.calculateType = calculateType;
	}

	public String getCalculateValue() {
		return calculateValue;
	}

	public void setCalculateValue(String calculateValue) {
		this.calculateValue = calculateValue;
	}
}
