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

import java.util.List;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 供应商产品
 * @author zr.chenxuemin@yikuyi.com
 * @version 1.0.0
 */
@ApiModel("PartyProductLineVo")
public class PartyProductLineVo extends AbstractEntity {

	/**
	 * 供应商 产品线
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "供应商外键ID")
	private String partyId;
	
	@ApiModelProperty(value = "供应商全称")
	private String groupNameFull;
	
	@ApiModelProperty(value = "供应商简称")
	private String groupName;
	
	@ApiModelProperty(value = "产品线集合List")
	private List<PartyProductLine> partyProductLineList;
	
	@ApiModelProperty(value = "描述说明")
	private String describe;
		
	@ApiModelProperty(value = "申请人名字")
	private String applyName;
	
	@ApiModelProperty(value = "申请人邮箱")
	private String applyMail;
	
	@ApiModelProperty(value = "供应商编码")
	private String partyCode;
	
	@ApiModelProperty(value = "所属地区")
	private String region;
	
	@ApiModelProperty(value = "公司名称  （审核专用）")
	private String name;
	
	@ApiModelProperty(value = "申请人名称")
	private String contactUserName;
	
	@ApiModelProperty(value = "审核人名称")
	private String approvePartyName;
	
	
	@ApiModelProperty(value = "产品id")
	private String brandId;
	
	@ApiModelProperty(value = "产品名称")
	private String brandName;
	
	@ApiModelProperty(value = "代理线类型")
	private Type type;
	
	/**
	 * 代理线类型
	 *
	 */
	public enum Type {
		// 代理
		PROXY,
		// 不代理
		NOT_PROXY
	}
	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	
	
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

	public String getPartyCode() {
		return partyCode;
	}

	public void setPartyCode(String partyCode) {
		this.partyCode = partyCode;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	

	public List<PartyProductLine> getPartyProductLineList() {
		return partyProductLineList;
	}

	public void setPartyProductLineList(List<PartyProductLine> partyProductLineList) {
		this.partyProductLineList = partyProductLineList;
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
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}
