/*
 * Created: 2017年8月10日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.vendor.vo;

import java.util.List;
import java.util.Map;

import com.yikuyi.party.group.model.PartyGroup.AccountStatus;
import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 供应商基本信息VO
 * @author zr.chenxuemin@yikuyi.com
 * @version 1.0.0
 */
@ApiModel("VendorInfoVo")
public class VendorInfoVo extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "供应商ID")
	private String partyId;
	
	@ApiModelProperty(value = "供应商全称")
	private String groupNameFull;
	
	@ApiModelProperty(value = "供应商简称")
	private String groupName;

	@ApiModelProperty(value = "供应商编码")
	private String partyCode;
	
	@ApiModelProperty(value = "所属分类")
	private String category;
	
	@ApiModelProperty(value = "所属分类名称")
	private String categoryName;
	
	@ApiModelProperty(value = "是否核心Y/N")
	private String isCore;

	@ApiModelProperty(value = "供应商logo")
	private String logoImageUrl;
	
	@ApiModelProperty(value = "供应商官网")
	private String website;

	@ApiModelProperty(value = "所属地区")
	private String region;
	
	@ApiModelProperty(value = "所属地区名称")
	private String regionName;
	
	@ApiModelProperty(value = "供应商总公司")
	private String generalHeadquarters;
	
	@ApiModelProperty(value = "公司类型")
	private String genre;
	
	@ApiModelProperty(value = "公司类型名称")
	private String genreName;
	
	@ApiModelProperty(value = "是否上市,Y/N")
	private String listed;
	
	@ApiModelProperty(value = "上市的股票代码")
	private String stockCode;
	
	@ApiModelProperty(value = "公司法人")
	private String legalPerson;
	
	@ApiModelProperty(value = "注册资金")
	private String regPrice;
	
	@ApiModelProperty(value = "注册地址")
	private String regAddress;
	
	@ApiModelProperty(value = "员工人数")
	private String employeeNum;
	
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
	
	@ApiModelProperty(value = "询价员ID集合")
	private List<String> enquiryList;
	
	@ApiModelProperty(value = "询价员名称")
	private String enquiryName;
	
	@ApiModelProperty(value = "报价员ID")
	private String offerId;
	
	@ApiModelProperty(value = "报价员ID集合")
	private List<String> offerList;
	
	@ApiModelProperty(value = "报价员名称")
	private String offerName;
	
	@ApiModelProperty(value = "描述说明")
	private String describe;	
	
	@ApiModelProperty(value = "申请人邮箱")
	private String applyMail;
	
	@ApiModelProperty(value = "供应商状态：VALID 有效，NOT_VALID 无效 INEFFECTIVE未生效")
	private AccountStatus accountStatus;
	
	@ApiModelProperty(value = "公司法人key:VENDOR_INFO_LEGALPERSON 注册资金key:VENDOR_INFO_REGPRICE,注册地址key:VENDOR_INFO_REGRADDRESS,员工人数key:VENDOR_INFO_EMPLOYEENUM,供应商官网key:VENDOR_INFO_WEBSITE")
	private Map<String, String> vendorInfoAttributeMap;
	
	

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

	public AccountStatus getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(AccountStatus accountStatus) {
		this.accountStatus = accountStatus;
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

	public String getApplyMail() {
		return applyMail;
	}

	public void setApplyMail(String applyMail) {
		this.applyMail = applyMail;
	}

	public Map<String, String> getVendorInfoAttributeMap() {
		return vendorInfoAttributeMap;
	}

	public void setVendorInfoAttributeMap(Map<String, String> vendorInfoAttributeMap) {
		this.vendorInfoAttributeMap = vendorInfoAttributeMap;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public String getGroupNameFull() {
		return groupNameFull;
	}

	public void setGroupNameFull(String groupNameFull) {
		this.groupNameFull = groupNameFull;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getPartyCode() {
		return partyCode;
	}

	public void setPartyCode(String partyCode) {
		this.partyCode = partyCode;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getIsCore() {
		return isCore;
	}

	public void setIsCore(String isCore) {
		this.isCore = isCore;
	}

	public String getLogoImageUrl() {
		return logoImageUrl;
	}

	public void setLogoImageUrl(String logoImageUrl) {
		this.logoImageUrl = logoImageUrl;
	}

	

	public String getGeneralHeadquarters() {
		return generalHeadquarters;
	}

	public void setGeneralHeadquarters(String generalHeadquarters) {
		this.generalHeadquarters = generalHeadquarters;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getGenreName() {
		return genreName;
	}

	public void setGenreName(String genreName) {
		this.genreName = genreName;
	}

	public String getListed() {
		return listed;
	}

	public void setListed(String listed) {
		this.listed = listed;
	}

	public String getStockCode() {
		return stockCode;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getLegalPerson() {
		return legalPerson;
	}

	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}

	public String getRegPrice() {
		return regPrice;
	}

	public void setRegPrice(String regPrice) {
		this.regPrice = regPrice;
	}

	public String getRegAddress() {
		return regAddress;
	}

	public void setRegAddress(String regAddress) {
		this.regAddress = regAddress;
	}

	public String getEmployeeNum() {
		return employeeNum;
	}

	public void setEmployeeNum(String employeeNum) {
		this.employeeNum = employeeNum;
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

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}


	
}
