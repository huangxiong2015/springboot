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

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 供应商基本信息VO   变更存储的内容
 * @author zr.chenxuemin@yikuyi.com
 * @version 1.0.0
 */
@ApiModel("CheckVendorInfoVo")
public class CheckVendorInfoVo extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "供应商ID")
	private String partyId;
	
	@ApiModelProperty(value = "供应商全称")
	private String groupNameFull;
	
	@ApiModelProperty(value = "供应商简称")
	private String groupName;

	@ApiModelProperty(value = "供应商编码")
	private String newPartyCode;
	
	@ApiModelProperty(value = "所属分类")
	private String category;	
	
	@ApiModelProperty(value = "是否核心Y/N")
	private String isCore;
		
	@ApiModelProperty(value = "描述说明")
	private String describe;
		
	@ApiModelProperty(value = "申请人名字")
	private String applyName;
	
	@ApiModelProperty(value = "申请人邮箱")
	private String applyMail;
	
	@ApiModelProperty(value = "列表供应商编码")
	private String partyCode;
	
	@ApiModelProperty(value = "所属地区")
	private String region;
	
	
	@ApiModelProperty(value = "公司名称  （审核专用）")
	private String name;
	
	@ApiModelProperty(value = "申请人名称")
	private String contactUserName;
	
	@ApiModelProperty(value = "审核人名称")
	private String approvePartyName;
	
	

	public String getApprovePartyName() {
		return approvePartyName;
	}

	public void setApprovePartyName(String approvePartyName) {
		this.approvePartyName = approvePartyName;
	}

	public String getContactUserName() {
		return contactUserName;
	}

	public void setContactUserName(String contactUserName) {
		this.contactUserName = contactUserName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNewPartyCode() {
		return newPartyCode;
	}

	public void setNewPartyCode(String newPartyCode) {
		this.newPartyCode = newPartyCode;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getApplyName() {
		return applyName;
	}

	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}

	public String getApplyMail() {
		return applyMail;
	}

	public void setApplyMail(String applyMail) {
		this.applyMail = applyMail;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
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

	public String getIsCore() {
		return isCore;
	}

	public void setIsCore(String isCore) {
		this.isCore = isCore;
	}
	
	
	
	

	
}
