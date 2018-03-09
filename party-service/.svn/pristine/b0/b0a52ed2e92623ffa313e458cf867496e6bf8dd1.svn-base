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

@ApiModel("ContactPersonInfo")
public class ContactPersonInfo extends AbstractEntity {

	/**
	 * 供应商联系人
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "供应商Id")
	private String vendorID;
	
	@ApiModelProperty(value = "联系人partyId")
	private String partyId;

	@ApiModelProperty(value = "联系人名字")
	private String lastNameLocal;
	
	@ApiModelProperty(value = "联系人职位")
	private String occupation;
	
	@ApiModelProperty(value = "联系人职能")
	private PersonalTitle personalTitle;

	@ApiModelProperty(value = "联系人绑定的产品线ID")
	private List<String> partyProductLineIdList;
	
	@ApiModelProperty(value = "产品线")
	private List<PartyProductLine> partyProductLineList;

	@ApiModelProperty(value = "邮箱")
	private String mail;

	@ApiModelProperty(value = "电话")
	private String fixedtel;

	@ApiModelProperty(value = "手机")
	private String tel;

	@ApiModelProperty(value = "地址")
	private String address;

	@ApiModelProperty(value = "是否默认联系人：是:Y,否：N")
	private String isDefault;


	public List<PartyProductLine> getPartyProductLineList() {
		return partyProductLineList;
	}

	public void setPartyProductLineList(List<PartyProductLine> partyProductLineList) {
		this.partyProductLineList = partyProductLineList;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getVendorID() {
		return vendorID;
	}




	public void setVendorID(String vendorID) {
		this.vendorID = vendorID;
	}




	public String getPartyId() {
		return partyId;
	}




	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}




	public String getLastNameLocal() {
		return lastNameLocal;
	}




	public void setLastNameLocal(String lastNameLocal) {
		this.lastNameLocal = lastNameLocal;
	}




	public PersonalTitle getPersonalTitle() {
		return personalTitle;
	}




	public void setPersonalTitle(PersonalTitle personalTitle) {
		this.personalTitle = personalTitle;
	}




	public List<String> getPartyProductLineIdList() {
		return partyProductLineIdList;
	}

	public void setPartyProductLineIdList(List<String> partyProductLineIdList) {
		this.partyProductLineIdList = partyProductLineIdList;
	}

	public String getMail() {
		return mail;
	}




	public void setMail(String mail) {
		this.mail = mail;
	}




	public String getFixedtel() {
		return fixedtel;
	}




	public void setFixedtel(String fixedtel) {
		this.fixedtel = fixedtel;
	}




	public String getTel() {
		return tel;
	}




	public void setTel(String tel) {
		this.tel = tel;
	}




	public String getAddress() {
		return address;
	}




	public void setAddress(String address) {
		this.address = address;
	}




	public String getIsDefault() {
		return isDefault;
	}




	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}




	public enum PersonalTitle {
		/**
		 * 询价
		 */
		ENQUIRY,
		/**
		 * 下单
		 */
		ORDER,
		/**
		 * 不限
		 */
		NOT_LIMIT;
		public static PersonalTitle getPersonalTitle(String personalTitle) {
			for (PersonalTitle value : PersonalTitle.values()) {
				if (value.name().equals(personalTitle)) {
					return value;
				}
			}
			return null;
		}
	}
	

}
