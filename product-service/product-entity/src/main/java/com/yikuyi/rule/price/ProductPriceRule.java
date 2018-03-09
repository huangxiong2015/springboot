package com.yikuyi.rule.price;

import java.util.List;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("ProductPriceRule")
public class ProductPriceRule extends AbstractEntity{
	private static final long serialVersionUID = -8823773164467638903L;

    @ApiModelProperty(value="商品价格规则ID")
	private String priceRuleId;

    /**
     * 策略目的
     * @author zr.wenjiao@yikuyi.com
     * @version 1.0.0
     */
    public enum PricePurposeType {
    	/**
    	 * 交期策略
    	 */
    	LEAD_TIME, 
    	/**
    	 * 商品价格
    	 */
    	PRODUCT_PRICE, 
    	/**
    	 * 运费价格
    	 */
    	SHIPMENT_PRICE,
    	/**
    	 * 最新订单金额策略
    	 */
    	MOV;
    }
    @ApiModelProperty(value="策略目的(LEAD_TIME：交期设置，PRODUCT_PRICE：定价设置，SHIPMENT_PRICE：运费设置)")
    private PricePurposeType pricePurposeType;

    @ApiModelProperty(value="规则名称")
	private String ruleName;

    @ApiModelProperty(value="描述")
	private String description;
    
    /**
     * 规则状态
     * @author zr.wenjiao@yikuyi.com
     * @version 1.0.0
     */
    public enum RuleStatus {
    	/**
    	 * 启用
    	 */
    	ENABLED, 
    	/**
    	 * 停用
    	 */
    	DISABLED, 
    	/**
    	 * 删除
    	 */
    	DELETED;
    }
    @ApiModelProperty(value="状态(ENABLED：启用，DISABLED：停用，DELETED：删除)")
    private RuleStatus ruleStatus;
    
    private List<ProductPriceAction> actions;
    
    private List<ProductPriceCond> conds;

	public RuleStatus getRuleStatus() {
		return ruleStatus;
	}

	public void setRuleStatus(RuleStatus ruleStatus) {
		this.ruleStatus = ruleStatus;
	}

	public List<ProductPriceAction> getActions() {
		return actions;
	}

	public void setActions(List<ProductPriceAction> actions) {
		this.actions = actions;
	}

	public List<ProductPriceCond> getConds() {
		return conds;
	}

	public void setConds(List<ProductPriceCond> conds) {
		this.conds = conds;
	}

	public PricePurposeType getPricePurposeType() {
		return pricePurposeType;
	}

	public void setPricePurposeType(PricePurposeType pricePurposeType) {
		this.pricePurposeType = pricePurposeType;
	}

	public String getPriceRuleId() {
		return priceRuleId;
	}

	public void setPriceRuleId(String priceRuleId) {
		this.priceRuleId = priceRuleId;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
