package com.yikuyi.party.vo;

import java.util.List;

import com.yikuyi.party.facility.model.Facility;
import com.yikuyi.party.model.Party;

import io.swagger.annotations.ApiModelProperty;

public class PartyVo extends Party{

	private static final long serialVersionUID = -2496585627722976259L;
	
	private String displayName;
	
	@ApiModelProperty(value = "是否启用供应商MOV策略(Y/N)")
	private String vendorMovStatus;
	
	@ApiModelProperty(value = "是否启用供应商SKU的MOV策略(Y/N)")
	private String skuMovStatus;
	
	
	@ApiModelProperty(value = "订单是否审核(Y/N)")
	private String orderVerify;
	
	@ApiModelProperty(value = "是否显示名称(Y/N)")
	private String isShowName;

	@ApiModelProperty(value = "是否显示销售价格(Y/N)")
	private String isSupPrice; 

	@ApiModelProperty(value = "仓库信息列表")
	private List<Facility> facilityList;
	
	
	@ApiModelProperty(value = "询价人邮箱集合")
	private List<String> inquiryMailList;
	
	@ApiModelProperty(value = "商品失效时长天数")
	private String productInvalidays; 

	@ApiModelProperty(value = "负责人邮箱")
	private String principalMail; 
	
	@ApiModelProperty(value = "是否系统自动集成库存（Y/N）")
	private String isAutoIntegrateQty;


	@ApiModelProperty(value = "交期准确率")
	private String leadtimeAccuracyrate;

	public String getLeadtimeAccuracyrate() {
		return leadtimeAccuracyrate;
	}

	public void setLeadtimeAccuracyrate(String leadtimeAccuracyrate) {
		this.leadtimeAccuracyrate = leadtimeAccuracyrate;
	}

	public String getIsAutoIntegrateQty() {
		return isAutoIntegrateQty;
	}

	public void setIsAutoIntegrateQty(String isAutoIntegrateQty) {
		this.isAutoIntegrateQty = isAutoIntegrateQty;
	}

	public String getPrincipalMail() {
		return principalMail;
	}

	public void setPrincipalMail(String principalMail) {
		this.principalMail = principalMail;
	}

	public List<String> getInquiryMailList() {
		return inquiryMailList;
	}

	public void setInquiryMailList(List<String> inquiryMailList) {
		this.inquiryMailList = inquiryMailList;
	}

	public String getProductInvalidays() {
		return productInvalidays;
	}

	public void setProductInvalidays(String productInvalidays) {
		this.productInvalidays = productInvalidays;
	}

	public List<Facility> getFacilityList() {
		return facilityList;
	}

	public void setFacilityList(List<Facility> facilityList) {
		this.facilityList = facilityList;
	}

	public String getVendorMovStatus() {
		return vendorMovStatus;
	}

	public void setVendorMovStatus(String vendorMovStatus) {
		this.vendorMovStatus = vendorMovStatus;
	}

	public String getSkuMovStatus() {
		return skuMovStatus;
	}

	public void setSkuMovStatus(String skuMovStatus) {
		this.skuMovStatus = skuMovStatus;
	}

	public String getOrderVerify() {
		return orderVerify;
	}

	public void setOrderVerify(String orderVerify) {
		this.orderVerify = orderVerify;
	}

	public String getIsShowName() {
		return isShowName;
	}

	public void setIsShowName(String isShowName) {
		this.isShowName = isShowName;
	}

	public String getIsSupPrice() {
		return isSupPrice;
	}

	public void setIsSupPrice(String isSupPrice) {
		this.isSupPrice = isSupPrice;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String toString() {
		return "PartyVo [displayName=" + displayName + "]";
	}

}
