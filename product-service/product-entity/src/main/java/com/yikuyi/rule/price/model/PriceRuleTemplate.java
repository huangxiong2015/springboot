package com.yikuyi.rule.price.model;

import java.util.List;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel("PriceRuleTemplate")
public class PriceRuleTemplate extends AbstractEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5754405794213764239L;
	
	@ApiModelProperty(value="规则ID")
	private String ruleId;
	
	@ApiModelProperty(value="规则名称")
	private String ruleName;
	
	@ApiModelProperty(value="供应商")
	private String vendorName;
	
	@ApiModelProperty(value="商品仓库")
	private String warehouse;
	
	@ApiModelProperty(value="品牌")
	private String brand;
	
	@ApiModelProperty(value="商品分类ID拼接字符串")
	private String category;
	
	@ApiModelProperty(value="大类名称")
	private String categoryName1;
	
	@ApiModelProperty(value="大类名称")
	private String categoryName2;
	
	@ApiModelProperty(value="大类名称")
	private String categoryName3;
	
	@ApiModelProperty(value="价格来源")
	private String priceSource;
	
	@ApiModelProperty(value="币种类型")
	private String currencyType;
	
	@ApiModelProperty(value="具体规则集合")
	private List<PriceRuleDetail> priceRuleDetails;
	
	@ApiModelProperty(value="模板状态")
	private String status;
	
	@ApiModelProperty(value="模板状态")
	private boolean disableAll;
	
	public boolean isDisableAll() {
		return disableAll;
	}


	public void setDisableAll(boolean disableAll) {
		this.disableAll = disableAll;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getRuleId() {
		return ruleId;
	}


	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}


	public List<PriceRuleDetail> getPriceRuleDetails() {
		return priceRuleDetails;
	}


	public void setPriceRuleDetails(List<PriceRuleDetail> priceRuleDetails) {
		this.priceRuleDetails = priceRuleDetails;
	}


	public String getRuleName() {
		return ruleName;
	}


	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}


	public String getVendorName() {
		return vendorName;
	}


	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}


	public String getWarehouse() {
		return warehouse;
	}


	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	public String getBrand() {
		return brand;
	}


	public void setBrand(String brand) {
		this.brand = brand;
	}


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public String getCategoryName1() {
		return categoryName1;
	}


	public void setCategoryName1(String categoryName1) {
		this.categoryName1 = categoryName1;
	}


	public String getCategoryName2() {
		return categoryName2;
	}


	public void setCategoryName2(String categoryName2) {
		this.categoryName2 = categoryName2;
	}


	public String getCategoryName3() {
		return categoryName3;
	}


	public void setCategoryName3(String categoryName3) {
		this.categoryName3 = categoryName3;
	}


	public String getPriceSource() {
		return priceSource;
	}


	public void setPriceSource(String priceSource) {
		this.priceSource = priceSource;
	}


	public String getCurrencyType() {
		return currencyType;
	}


	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	private String description;
}
