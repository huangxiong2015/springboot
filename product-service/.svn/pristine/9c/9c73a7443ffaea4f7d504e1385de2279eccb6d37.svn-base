package com.yikuyi.product.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModelProperty;

/**
 * 供应商权重
 * @author injor.huang
 *
 */
@Document(collection = "vendor_weight")
public class VendorWeight extends AbstractEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@JsonProperty("vendorId")
	@ApiModelProperty(value="供应商Id")
	private String vendorId;
	
	@ApiModelProperty(value="供应商名称")
	private String vendorName;
	
	@ApiModelProperty(value="spu权重。数字越大，权重越高")
	private int spuWeight;

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public int getSpuWeight() {
		return spuWeight;
	}

	public void setSpuWeight(int spuWeight) {
		this.spuWeight = spuWeight;
	}
}
