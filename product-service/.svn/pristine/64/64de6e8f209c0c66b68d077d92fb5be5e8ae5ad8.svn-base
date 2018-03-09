package com.yikuyi.rule.price;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModelProperty;

public class ProductPriceCond extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3290831583053385086L;
	
	public enum InputParam {
		/** 商品来源**/
    	VENDOR_ID, 
    	/** 仓库**/
    	WAREHOUSE, 
    	/** 品牌**/
    	BRAND, 
    	/** 商品分类**/
    	CATEGORY,
    	/** 币种类型**/
    	CURRENCY_TYPE,
    	/** 商品类型**/
    	PRODUCT_TYPE,
    	/** 是否包邮  **/
    	SHIP_CHARGE_MODE,
    	/** 交货地  **/
    	SHIP_ADDRESS,
    	/** 包邮金额  **/
    	ORDER_AMOUNT,
    	/** 运送地点  **/
    	SHIP_PROVINCE,
    	/** 上传交期为空页面不显示**/
    	IS_SHOW_LEADTIME
    }

	@ApiModelProperty(value="价格规则ID")
	private String productPriceRuleId;
	
	@ApiModelProperty(value="价格规则条件序号ID")
	private String productPriceCondSeqId;

	@ApiModelProperty(value="条件输入参数ID，ENUMERATION .PROD_PRICE_IN_PARAM枚举")
	private String inputParamEnumId;

	@ApiModelProperty(value=" 操作符号枚举值,如PRC_EQ代表等于。ENUMERATION .PROD_PRICE_COND枚举")
	private String operatorEnumId;

	@ApiModelProperty(value="条件值")
	private String condValue;
	
	public String getProductPriceRuleId() {
		return productPriceRuleId;
	}
	public void setProductPriceRuleId(String productPriceRuleId) {
		this.productPriceRuleId = productPriceRuleId;
	}
	public String getProductPriceCondSeqId() {
		return productPriceCondSeqId;
	}
	public void setProductPriceCondSeqId(String productPriceCondSeqId) {
		this.productPriceCondSeqId = productPriceCondSeqId;
	}
	public String getInputParamEnumId() {
		return inputParamEnumId;
	}
	public void setInputParamEnumId(String inputParamEnumId) {
		this.inputParamEnumId = inputParamEnumId;
	}
	public String getOperatorEnumId() {
		return operatorEnumId;
	}
	public void setOperatorEnumId(String operatorEnumId) {
		this.operatorEnumId = operatorEnumId;
	}
	public String getCondValue() {
		return condValue;
	}
	public void setCondValue(String condValue) {
		this.condValue = condValue;
	}
	
	
	
	
}
