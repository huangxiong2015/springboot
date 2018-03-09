package com.yikuyi.rule.price.model;

import com.yikuyi.rule.price.ProductPriceRule;
import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModelProperty;

public class PriceRuleTemplateCache extends AbstractEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5754405794213764239L;
	@ApiModelProperty(value="供应商ID")
	private Long vendorId;
	
	@ApiModelProperty(value="仓库名称")
	private String warehouse;
	
	@ApiModelProperty(value="商品类别")
	private String category;
	
	@ApiModelProperty(value="商品类别")
	private String cacheKey;
	
	@ApiModelProperty(value="定价规则")
	private ProductPriceRule productPriceRule;
	
	@ApiModelProperty(value="缓存的版本号")
	private String version;
	
	public String getCacheKey() {
		return cacheKey;
	}

	public void setCacheKey(String cacheKey) {
		this.cacheKey = cacheKey;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public ProductPriceRule getProductPriceRule() {
		return productPriceRule;
	}

	public void setProductPriceRule(ProductPriceRule productPriceRule) {
		this.productPriceRule = productPriceRule;
	}

	
}
