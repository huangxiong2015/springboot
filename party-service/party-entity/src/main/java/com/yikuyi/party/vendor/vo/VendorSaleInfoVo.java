/*
 * Created: 2017年3月23日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.vendor.vo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.yikuyi.party.facility.model.Facility;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 供应商销售VO
 * @author zr.chenxuemin@yikuyi.com
 * @version 1.0.0
 */
@ApiModel("VendorSaleInfoVo")
public class VendorSaleInfoVo extends PartySupplier{
	
	
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "仓库信息列表")
	private List<Facility> facilityList;
	
	@ApiModelProperty(value = "应付余额")
	private BigDecimal balanceDue;
	
	@ApiModelProperty(value = "剩余额度")
	private BigDecimal surplusQuota;
	
	
	@ApiModelProperty(value = "最后交易日期")
	private String lastTransactionDate;
	
	@ApiModelProperty(value = "最后交易金额")
	private BigDecimal lastTransactionPrice;
	
	@ApiModelProperty(value = "最后付款日期")
	private String lastPayDate;
	
	@ApiModelProperty(value = "最后付款金额")
	private BigDecimal lastPaymPrice;
	
	
	@ApiModelProperty(value = "关注领域")
	private String focusFields;
	
	@ApiModelProperty(value = "优势产品类别")
	private String productCategorys;
	
	@ApiModelProperty(value = "主要客户")
	private String majorClients;
	
	@ApiModelProperty(value = "描述说明")
	private String describe;
	
	@ApiModelProperty(value = "联系人信息")
	private List<ContactPersonInfo> contactPersonInfoList;
	
	@ApiModelProperty(value = "供应商产品线全量信息列表")
	private List<PartyProductLine> partyProductLineList;
	
	@ApiModelProperty(value = "关注领域key:VENDOR_SALE_INFOVO_FOCUSFIELDS 优势产品类别key:VENDOR_SALE_INFOVO_PRODUCTCATEGORYS,主要客户key:VENDOR_SALE_INFOVO_MAJORCLIENTS")
	private Map<String, String> saleAttributeMap;
	
	
	@ApiModelProperty(value = "申请人邮箱")
	private String  applyMail;
	
	@ApiModelProperty(value = "分管部门ID")
	private String deptId;
	
	@ApiModelProperty(value = "分管部门名称")
	private String deptName;
	
	@ApiModelProperty(value = "负责人ID")
	private String principalId;
	
	@ApiModelProperty(value = "负责人名称")
	private String principalName ;
	
	@ApiModelProperty(value = "询价员ID")
	private String enquiryId;
	
	@ApiModelProperty(value = "询价员名称")
	private String enquiryName;
	
	@ApiModelProperty(value = "报价员ID")
	private String offerId;
	
	@ApiModelProperty(value = "报价员名称")
	private String offerName;
	
	@ApiModelProperty(value = "询价员ID集合")
	private List<String> enquiryList;
	
	@ApiModelProperty(value = "报价员ID集合")
	private List<String> offerList;
	
	

	public List<String> getEnquiryList() {
		return enquiryList;
	}

	public void setEnquiryList(List<String> enquiryList) {
		this.enquiryList = enquiryList;
	}

	public List<String> getOfferList() {
		return offerList;
	}

	public void setOfferList(List<String> offerList) {
		this.offerList = offerList;
	}


	
	public String getApplyMail() {
		return applyMail;
	}

	public void setApplyMail(String applyMail) {
		this.applyMail = applyMail;
	}

	public List<PartyProductLine> getPartyProductLineList() {
		return partyProductLineList;
	}

	public void setPartyProductLineList(List<PartyProductLine> partyProductLineList) {
		this.partyProductLineList = partyProductLineList;
	}

	public Map<String, String> getSaleAttributeMap() {
		return saleAttributeMap;
	}

	public void setSaleAttributeMap(Map<String, String> saleAttributeMap) {
		this.saleAttributeMap = saleAttributeMap;
	}

	public List<Facility> getFacilityList() {
		return facilityList;
	}

	public void setFacilityList(List<Facility> facilityList) {
		this.facilityList = facilityList;
	}

	public BigDecimal getBalanceDue() {
		return balanceDue;
	}

	public void setBalanceDue(BigDecimal balanceDue) {
		this.balanceDue = balanceDue;
	}

	

	public BigDecimal getSurplusQuota() {
		return surplusQuota;
	}

	public void setSurplusQuota(BigDecimal surplusQuota) {
		this.surplusQuota = surplusQuota;
	}

	public String getLastTransactionDate() {
		return lastTransactionDate;
	}

	public void setLastTransactionDate(String lastTransactionDate) {
		this.lastTransactionDate = lastTransactionDate;
	}

	public BigDecimal getLastTransactionPrice() {
		return lastTransactionPrice;
	}

	public void setLastTransactionPrice(BigDecimal lastTransactionPrice) {
		this.lastTransactionPrice = lastTransactionPrice;
	}

	public String getLastPayDate() {
		return lastPayDate;
	}

	public void setLastPayDate(String lastPayDate) {
		this.lastPayDate = lastPayDate;
	}

	public BigDecimal getLastPaymPrice() {
		return lastPaymPrice;
	}

	public void setLastPaymPrice(BigDecimal lastPaymPrice) {
		this.lastPaymPrice = lastPaymPrice;
	}

	public String getFocusFields() {
		return focusFields;
	}

	public void setFocusFields(String focusFields) {
		this.focusFields = focusFields;
	}

	public String getProductCategorys() {
		return productCategorys;
	}

	public void setProductCategorys(String productCategorys) {
		this.productCategorys = productCategorys;
	}

	public String getMajorClients() {
		return majorClients;
	}

	public void setMajorClients(String majorClients) {
		this.majorClients = majorClients;
	}

	public List<ContactPersonInfo> getContactPersonInfoList() {
		return contactPersonInfoList;
	}

	public void setContactPersonInfoList(List<ContactPersonInfo> contactPersonInfoList) {
		this.contactPersonInfoList = contactPersonInfoList;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getPrincipalId() {
		return principalId;
	}

	public void setPrincipalId(String principalId) {
		this.principalId = principalId;
	}

	public String getPrincipalName() {
		return principalName;
	}

	public void setPrincipalName(String principalName) {
		this.principalName = principalName;
	}

	public String getEnquiryId() {
		return enquiryId;
	}

	public void setEnquiryId(String enquiryId) {
		this.enquiryId = enquiryId;
	}

	public String getEnquiryName() {
		return enquiryName;
	}

	public void setEnquiryName(String enquiryName) {
		this.enquiryName = enquiryName;
	}

	public String getOfferId() {
		return offerId;
	}

	public void setOfferId(String offerId) {
		this.offerId = offerId;
	}

	public String getOfferName() {
		return offerName;
	}

	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}
	
	
}
