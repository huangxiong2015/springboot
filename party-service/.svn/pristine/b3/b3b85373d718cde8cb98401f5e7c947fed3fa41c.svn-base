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

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel("CheckStartOrLose")
public class CheckStartOrLose extends AbstractEntity {
	
	/**
	 * 审核  启动 或 失效
	 */
	private static final long serialVersionUID = 1L;
	


	@ApiModelProperty(value = "供应商ID")
	private String partyId;
		
	@ApiModelProperty(value = "描述说明")
	private String describe;
	
	@ApiModelProperty(value = "启动或失效")
	private StartOrLose startOrLose;
		
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
	
	@ApiModelProperty(value = "启动或失效   日志使用")
	private String startLose;
	
	
	
	public String getStartLose() {
		return startLose;
	}

	public void setStartLose(String startLose) {
		this.startLose = startLose;
	}

	public String getContactUserName() {
		return contactUserName;
	}

	public void setContactUserName(String contactUserName) {
		this.contactUserName = contactUserName;
	}

	public String getApprovePartyName() {
		return approvePartyName;
	}

	public void setApprovePartyName(String approvePartyName) {
		this.approvePartyName = approvePartyName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public enum StartOrLose {
		/**
		 * 启动
		 */
		START,
		/**
		 * 失效
		 */
		LOSE;
		public static StartOrLose getStartOrLose(String startOrLose){
			for(StartOrLose value : StartOrLose.values()){
				if(value.name().equals(startOrLose)){
					return value;
				}
			}
			return null;
		}
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public StartOrLose getStartOrLose() {
		return startOrLose;
	}

	public void setStartOrLose(StartOrLose startOrLose) {
		this.startOrLose = startOrLose;
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

	public String getPartyCode() {
		return partyCode;
	}

	public void setPartyCode(String partyCode) {
		this.partyCode = partyCode;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}
	
	
	
}
