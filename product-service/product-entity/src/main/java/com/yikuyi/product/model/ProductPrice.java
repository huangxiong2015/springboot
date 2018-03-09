package com.yikuyi.product.model;

import java.util.List;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author zr.wujiajun
 *
 */
public class ProductPrice extends AbstractEntity {

	private static final long serialVersionUID = -4001362999356565250L;
	@ApiModelProperty(value="币种")
	private String currencyCode;
	@ApiModelProperty(value="单价,和梯度价格中的第1个是冗余的")
	private String unitPrice;
	@ApiModelProperty(value="价格梯度")
	private List<ProductPriceLevel> priceLevels;

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}

	public List<ProductPriceLevel> getPriceLevels() {
		return priceLevels;
	}

	public void setPriceLevels(List<ProductPriceLevel> priceLevels) {
		this.priceLevels = priceLevels;
	}
}
