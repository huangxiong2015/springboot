/*
 * Created: 2017年7月17日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.contact.vo;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModelProperty;

public class UserParamVo extends AbstractEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7407884426019757947L;
	
	@ApiModelProperty(value = "用户id")
	private String partyId;
	
	@ApiModelProperty(value = "用户电话")
	private String tel;
	
	@ApiModelProperty(value = "用户公司名称")
	private String groupName;
	
	@ApiModelProperty(value = "用户类型")
	private String userType;
	
	@ApiModelProperty(value = "用户邮箱")
	private String mail;
	
	
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getPartyId() {
		return partyId;
	}
	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

}