package com.yikuyi.rule.price;

import java.util.List;

import com.yikuyi.product.model.ProductPrice;
import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 单个商品的价格信息
 * @author zr.zhouyan
 *
 */
@ApiModel("priceInfo")
public class PriceInfo extends AbstractEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5754405794213764239L;
	@ApiModelProperty(value = "商品ID")
	private String productId;
	
	@ApiModelProperty(value = "梯度价格")
	private List<ProductPrice> prices; // orginal real Cost
	
	@ApiModelProperty(value = "成本价")
	private List<ProductPrice> costPrices; // real cost
	
	@ApiModelProperty(value = "当前销售价")
	private List<ProductPrice> resalePrices; // original resale  price
	
	@ApiModelProperty(value = "特价")
	private List<ProductPrice> specialResaleprices; // special resale price
	
	@ApiModelProperty(value = "原销售价")
	private List<ProductPrice> originalResalePrices; // special resale price

	public List<ProductPrice> getOriginalResalePrices() {
		return originalResalePrices;
	}

	public void setOriginalResalePrices(List<ProductPrice> originalResalePrices) {
		this.originalResalePrices = originalResalePrices;
	}

	public List<ProductPrice> getCostPrices() {
		return costPrices;
	}

	public void setCostPrices(List<ProductPrice> cosePrices) {
		this.costPrices = cosePrices;
	}

	public List<ProductPrice> getResalePrices() {
		return resalePrices;
	}

	public void setResalePrices(List<ProductPrice> resalePrices) {
		this.resalePrices = resalePrices;
	}

	public List<ProductPrice> getSpecialResaleprices() {
		return specialResaleprices;
	}

	public void setSpecialResaleprices(List<ProductPrice> specialResaleprices) {
		this.specialResaleprices = specialResaleprices;
	}

	public List<ProductPrice> getPrices() {
		return prices;
	}

	public void setPrices(List<ProductPrice> prices) {
		this.prices = prices;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
	
}
