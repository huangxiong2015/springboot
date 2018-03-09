/*
 * Created: 2017年8月17日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.vendor.vo;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 供应商列表查询条件vo
 * @author zr.zhanghua@yikuyi.com
 * @version 1.0.0
 */
@ApiModel("VendorQueryVo")
public class VendorResponVo {
	@ApiModelProperty(value = "供应商id")
	private String partyId;
	
	@ApiModelProperty(value = "供应商编码")
	private String partyCode;
	
	@ApiModelProperty(value = "供应商简称")
	private String groupName;
	
	@ApiModelProperty(value = "所属分类")
	private String category;
	
	@ApiModelProperty(value = "所属地区")
	private String region;
	
	@ApiModelProperty(value = "是否核心Y/N")
	private String isCore;
	
	@ApiModelProperty(value = "付款方式")
	private String payType;
	
	@ApiModelProperty(value = "询价员名称")
	private String enquiryName;
	
	@ApiModelProperty(value = "创建人Id")
	private String creater;
	
	@ApiModelProperty(value = "创建人名称")
	private String creatorName;
	
	
	@ApiModelProperty(value = "状态")
	private String status;
	
	@ApiModelProperty(value = "创建时间")
	private String createDate;
	
	@ApiModelProperty(value = "最后交易时间")
	private Date lastDealDate;
	
	@ApiModelProperty(value = "审核状态")
	private String accountStatus;
	
	@ApiModelProperty(value = "操作状态")
	private String verifyStatusId;
	
	@ApiModelProperty(value = "是否在审核：Y,N")
	private String applyStatus;
	
	@ApiModelProperty(value = "是否在审核订单：Y,N")
	private String orderVerify;
	
	@ApiModelProperty(value = "供应商logo")
	private String logoImageUrl;
	
	
	public String getLogoImageUrl() {
		return logoImageUrl;
	}

	public void setLogoImageUrl(String logoImageUrl) {
		this.logoImageUrl = logoImageUrl;
	}

	public String getOrderVerify() {
		return orderVerify;
	}

	public void setOrderVerify(String orderVerify) {
		this.orderVerify = orderVerify;
	}

	public String getApplyStatus() {
		return applyStatus == null ? "N": applyStatus;
	}

	public void setApplyStatus(String applyStatus) {
		this.applyStatus = applyStatus;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public String getVerifyStatusId() {
		return verifyStatusId;
	}

	public void setVerifyStatusId(String verifyStatusId) {
		this.verifyStatusId = verifyStatusId;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public String getPartyCode() {
		return partyCode;
	}

	public void setPartyCode(String partyCode) {
		this.partyCode = partyCode;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getIsCore() {
		return isCore;
	}

	public void setIsCore(String isCore) {
		this.isCore = isCore;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getEnquiryName() {
		return enquiryName;
	}

	public void setEnquiryName(String enquiryName) {
		this.enquiryName = enquiryName;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public Date getLastDealDate() {
		return lastDealDate;
	}

	public void setLastDealDate(Date lastDealDate) {
		this.lastDealDate = lastDealDate;
	}

}
