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

import com.yikuyi.party.vendor.vo.PartyProductLine.Status;
import com.yikuyi.party.vendor.vo.Vendor.Currency;
import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel("PartyBankAccount")
public class PartyBankAccount extends AbstractEntity {
	
	/**
	 * 供应商银行信息
	 */
	private static final long serialVersionUID = 1L;

	  
	@ApiModelProperty(value = "主键ID")
	private String partyBankAccountId;
	
	@ApiModelProperty(value = "账号名称")
	private String accountName;
	
	@ApiModelProperty(value = "party_id外键")
	private String partyId;
	
	@ApiModelProperty(value = "币种")
	private Currency currency;

	@ApiModelProperty(value = "银行账号")
	private String bankAccount;

	@ApiModelProperty(value = "银行名称")
	private String bankName;
	
	@ApiModelProperty(value = "税号")
	private String taxNumber;
	
	@ApiModelProperty(value = "地址")
	private String address;
	
	@ApiModelProperty(value = "电话")
	private String contactNumber;
	
	@ApiModelProperty(value = "是否默认银行,是:Y,否：N")
	private String isDefault;

	@ApiModelProperty(value = "状态(ENABLE/UNABLE/DELETE)")
	private Status status;
	
	@ApiModelProperty(value = "银行的国际代码")
	private String swiftCode;

	
	
	
	public String getSwiftCode() {
		return swiftCode;
	}

	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getPartyBankAccountId() {
		return partyBankAccountId;
	}

	public void setPartyBankAccountId(String partyBankAccountId) {
		this.partyBankAccountId = partyBankAccountId;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getTaxNumber() {
		return taxNumber;
	}

	public void setTaxNumber(String taxNumber) {
		this.taxNumber = taxNumber;
	}

	

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
}
