package com.yikuyi.rule.price.model;

import com.yikuyi.rule.price.PriceInfo;
import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModelProperty;

public class ProductPriceCache extends AbstractEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5754405794213764239L;
	@ApiModelProperty(value="商品ID")
	private String productId;
	
	@ApiModelProperty(value="仓库名称")
	private PriceInfo priceInfo;
	
	@ApiModelProperty(value="模板缓存的key")
	private String templateKey;
	
	@ApiModelProperty(value="汇率缓存的key")
	private String rateKey;
	
	@ApiModelProperty(value="缓存的版本号")
	private String version;
	
	
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}


	public PriceInfo getPriceInfo() {
		return priceInfo;
	}

	public void setPriceInfo(PriceInfo priceInfo) {
		this.priceInfo = priceInfo;
	}

	public String getTemplateKey() {
		return templateKey;
	}

	public void setTemplateKey(String templateKey) {
		this.templateKey = templateKey;
	}

	public String getRateKey() {
		return rateKey;
	}

	public void setRateKey(String rateKey) {
		this.rateKey = rateKey;
	}
	
}
