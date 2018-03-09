package com.yikuyi.rule.price;

import java.math.BigDecimal;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModelProperty;

public class ProductPriceAction extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5468030939486780220L;

	@ApiModelProperty(value="价格策略ID")
	private String productPriceRuleId;
	
	@ApiModelProperty(value="价格规则条件序号ID")
	private String productPriceCondSeqId;
	
	@ApiModelProperty(value="序号")
	private String productPriceActionSeqId;
	
	/** 
	 * 规则目标枚举
	 */
	public enum ProductPriceActionTypeId {
		/** 修改固定金额**/
		PRICE_POL, 
    	/** 百分数**/
		PRICE_FLAT, 
    }

	@ApiModelProperty(value="actin类型（按半分比PRICE_FLAT，按固定值PRICE_POL）")
	private String productPriceActionTypeId;
	
	
	@ApiModelProperty(value="单位ID，如人民币: CNY，美元: USD，天 : DAY，周: WEEK")
	private String uomId;

	@ApiModelProperty(value="金额")
	private BigDecimal amount;
	
	@ApiModelProperty(value="扩展值，用于对区间的计算规则。")
	private BigDecimal extendAmount;

	/** 
	 * 规则目标枚举
	 */
	public enum ProductPricePurposeId {
		/** 成本价格**/
		ORIGINAL_COST, 
    	/** 真实成本价**/
		REAL_COST, 
    	/** 销售价格**/
		RESALE_PRICE,
    	/** 特别销售价格**/
		SPECIAL_RESALE_PRICE,
		/**国内最小订单金额 **/
		MOV_CNY,
		/**国外最小订单金额 **/
		MOV_USD
    }
	@ApiModelProperty(value="规则目标，如real price，original resale，special resale price")
	private String productPricePurposeId;
	
	public String getUomId() {
		return uomId;
	}
	public void setUomId(String uomId) {
		this.uomId = uomId;
	}
	public BigDecimal getExtendAmount() {
		return extendAmount;
	}
	public void setExtendAmount(BigDecimal extendAmount) {
		this.extendAmount = extendAmount;
	}
	public String getProductPriceRuleId() {
		return productPriceRuleId;
	}
	public void setProductPriceRuleId(String productPriceRuleId) {
		this.productPriceRuleId = productPriceRuleId;
	}
	public String getProductPriceActionSeqId() {
		return productPriceActionSeqId;
	}
	public void setProductPriceActionSeqId(String productPriceActionSeqId) {
		this.productPriceActionSeqId = productPriceActionSeqId;
	}
	public String getProductPriceActionTypeId() {
		return productPriceActionTypeId;
	}
	public void setProductPriceActionTypeId(String productPriceActionTypeId) {
		this.productPriceActionTypeId = productPriceActionTypeId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getProductPricePurposeId() {
		return productPricePurposeId;
	}
	public void setProductPricePurposeId(String productPricePurposeId) {
		this.productPricePurposeId = productPricePurposeId;
	}
	public String getProductPriceCondSeqId() {
		return productPriceCondSeqId;
	}
	public void setProductPriceCondSeqId(String productPriceCondSeqId) {
		this.productPriceCondSeqId = productPriceCondSeqId;
	}
	
}
