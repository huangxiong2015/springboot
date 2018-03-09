package com.yikuyi.rule.mov.vo;

import java.util.List;
import java.util.Map;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModelProperty;

public class MovValidResult extends AbstractEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8422001904150156189L;

	@ApiModelProperty(value="规则ID")
	private String vendorId;
	
	@ApiModelProperty(value="供应商MOV")
	private String vendorMov;
	
	@ApiModelProperty(value="供应商MOV校验不通过的商品ID集合")
	private List<Object> validVendorIds;
	
	@ApiModelProperty(value="仓库MOV校验不通过的商品ID集合")
	private List<Map<String, Object>> validWarehouseIds;
	
	@ApiModelProperty(value="品牌MOV校验不通过的商品ID集合")
	private List<Map<String, Object>> validBrandIds;
	
	@ApiModelProperty(value="商品MOV校验不通过的商品ID集合")
	private List<Map<String, Object>> validProductIds;

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorMov() {
		return vendorMov;
	}

	public void setVendorMov(String vendorMov) {
		this.vendorMov = vendorMov;
	}
	public List<Object> getValidVendorIds() {
		return validVendorIds;
	}

	public void setValidVendorIds(List<Object> validVendorIds) {
		this.validVendorIds = validVendorIds;
	}

	public List<Map<String, Object>> getValidWarehouseIds() {
		return validWarehouseIds;
	}

	public void setValidWarehouseIds(List<Map<String, Object>> validWarehouseIds) {
		this.validWarehouseIds = validWarehouseIds;
	}

	public List<Map<String, Object>> getValidBrandIds() {
		return validBrandIds;
	}

	public void setValidBrandIds(List<Map<String, Object>> validBrandIds) {
		this.validBrandIds = validBrandIds;
	}

	public List<Map<String, Object>> getValidProductIds() {
		return validProductIds;
	}

	public void setValidProductIds(List<Map<String, Object>> validProductIds) {
		this.validProductIds = validProductIds;
	}
	
	
}
