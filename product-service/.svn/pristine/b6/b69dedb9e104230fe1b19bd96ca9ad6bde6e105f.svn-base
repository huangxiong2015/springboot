package com.yikuyi.brand.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModelProperty;

/**
 * 商品品牌
 * @author zr.wenjiao@yikuyi.com
 */
@Document(collection="product_brand")
public class ProductBrand extends AbstractEntity{
	
	private static final long serialVersionUID = 5334524279611049422L;

	@ApiModelProperty(value="id")
	@Id
	private Integer id;

	@ApiModelProperty(value="品牌名")
	private String brandName;
	
	@ApiModelProperty(value="LOGO")
	private String logo;
	
	@ApiModelProperty(value="描述信息")
	private String desc;

	@ApiModelProperty(value="品牌简称")
	private String brandShort;

	@ApiModelProperty(value="状态")
	private Integer status;

	@ApiModelProperty(value="别名")
	private List<String> brandAlias;

	@ApiModelProperty(value="包含供应商专用的别名")
	private List<ProductBrandAlias> vendorAlias;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<String> getBrandAlias() {
		return brandAlias;
	}

	public void setBrandAlias(List<String> brandAlias) {
		this.brandAlias = brandAlias;
	}

	public String getBrandShort() {
		return brandShort;
	}

	public void setBrandShort(String brandShort) {
		this.brandShort = brandShort;
	}

	public List<ProductBrandAlias> getVendorAlias() {
		return vendorAlias;
	}

	public void setVendorAlias(List<ProductBrandAlias> vendorAlias) {
		this.vendorAlias = vendorAlias;
	}
	
}
