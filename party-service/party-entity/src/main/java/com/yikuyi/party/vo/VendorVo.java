package com.yikuyi.party.vo;

import java.util.List;

import com.yikuyi.party.model.PartyAttribute;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("VendorVo")
public class VendorVo {
	@ApiModelProperty(value = "供应商id")
	private String vendorId;
	@ApiModelProperty(value = "供应商简称")
	private String vendorName;
	@ApiModelProperty(value = "供应商全称")
	private String vendorNameFull;
	@ApiModelProperty(value = "供应商LOGO")
	private String logoUrl;
	@ApiModelProperty(value = "是否显示名称")
	private String isShowName;
	@ApiModelProperty(value = "是否支持价格策略,Y支持，N不支持")
	private String supPrice;

	@ApiModelProperty(value = "供应商多属性保存列表")
	// 供应商是否支持mov校验,vendorMovStatusY支持，N不支持
	// 供应商具体sku是否支持mov校验,skuMovStatusY支持，N不支持
	private List<PartyAttribute> partyAttributes;
	
	@ApiModelProperty(value = "供应商编码")
	private String code;
	@ApiModelProperty(value = "供应商仓库列表")
	private List<FacilityVo> item;

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

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getIsShowName() {
		return isShowName;
	}

	public void setIsShowName(String isShowName) {
		this.isShowName = isShowName;
	}

	public List<FacilityVo> getItem() {
		return item;
	}

	public void setItem(List<FacilityVo> item) {
		this.item = item;
	}

	public String getVendorNameFull() {
		return vendorNameFull;
	}

	public void setVendorNameFull(String vendorNameFull) {
		this.vendorNameFull = vendorNameFull;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSupPrice() {
		return supPrice;
	}

	public void setSupPrice(String supPrice) {
		this.supPrice = supPrice;
	}

	public List<PartyAttribute> getPartyAttributes() {
		return partyAttributes;
	}

	public void setPartyAttributes(List<PartyAttribute> partyAttributes) {
		this.partyAttributes = partyAttributes;
	}
}