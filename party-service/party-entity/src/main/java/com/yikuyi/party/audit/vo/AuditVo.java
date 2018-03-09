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
package com.yikuyi.party.audit.vo;

import java.util.List;

import com.yikuyi.party.audit.model.Audit;

public class AuditVo extends Audit {

	private static final long serialVersionUID = -8071630422943674647L;
	
	private String searchValue;
	
	private String userName;

	private String createdateStard;
	
	private String createdateEnd;
	
	private String mail;
	
	private List<String> actions;
	
	private List<String> applicationCodes;
	
	private String currenUserId;
	
	public String getCurrenUserId() {
		return currenUserId;
	}

	public void setCurrenUserId(String currenUserId) {
		this.currenUserId = currenUserId;
	}

	public List<String> getActions() {
		return actions;
	}

	public void setActions(List<String> actions) {
		this.actions = actions;
	}

	public List<String> getApplicationCodes() {
		return applicationCodes;
	}

	public void setApplicationCodes(List<String> applicationCodes) {
		this.applicationCodes = applicationCodes;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCreatedateStard() {
		return createdateStard;
	}

	public void setCreatedateStard(String createdateStard) {
		this.createdateStard = createdateStard;
	}

	public String getCreatedateEnd() {
		return createdateEnd;
	}

	public void setCreatedateEnd(String createdateEnd) {
		this.createdateEnd = createdateEnd;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}
}