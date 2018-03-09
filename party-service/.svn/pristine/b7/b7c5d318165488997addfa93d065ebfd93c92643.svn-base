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

import com.yikuyi.party.credit.model.PartyCredit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 供应商信用VO
 * @author zr.chenxuemin@yikuyi.com
 * @version 1.0.0
 */
@ApiModel("VendorCreditVo")
public class VendorCreditVo  extends PartyCredit{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "银行资料列表")
	private  List<PartyBankAccount> partyBankAccount;
	
	@ApiModelProperty(value = "采购协议  已签：ALREADY_SIGN，未签：NOT_SIGN，已特批：ALREADY_SPE_SIGN")
	private String purchaseDeal;
	
	@ApiModelProperty(value = "采购协议有效期")
	private String purchaseDealDate;
	
	@ApiModelProperty(value = "保密协议  已签：ALREADY_SIGN，未签：NOT_SIGN，已特批：ALREADY_SPE_SIGN")
	private String secrecyProtocol;
	
	@ApiModelProperty(value = "保密协议有效期")
	private String secrecyProtocolDate;
	
	@ApiModelProperty(value = "描述说明")
	private String describe;
	
	@ApiModelProperty(value = "采购协议key:VENDOR_CREDIT_PURCHASEDEAL 采购协议有效期key:VENDOR_CREDIT_PURCHASEDEALDATE,保密协议key:VENDOR_CREDIT_SECRECYPROTOCOL,保密协议有效期key:VENDOR_CREDIT_SECRECYPROTOCOLDATE (已签：ALREADY_SIGN，未签：NOT_SIGN，已特批：ALREADY_SPE_SIGN)")
	private Map<String, String> creditAttributeMap;
	
	@ApiModelProperty(value = "申请人邮箱")
	private String applyMail;
	
	
	
	
	
	public String getApplyMail() {
		return applyMail;
	}
	public void setApplyMail(String applyMail) {
		this.applyMail = applyMail;
	}
	public Map<String, String> getCreditAttributeMap() {
		return creditAttributeMap;
	}
	public void setCreditAttributeMap(Map<String, String> creditAttributeMap) {
		this.creditAttributeMap = creditAttributeMap;
	}
	public List<PartyBankAccount> getPartyBankAccount() {
		return partyBankAccount;
	}
	public void setPartyBankAccount(List<PartyBankAccount> partyBankAccount) {
		this.partyBankAccount = partyBankAccount;
	}

	public String getPurchaseDealDate() {
		return purchaseDealDate;
	}
	public void setPurchaseDealDate(String purchaseDealDate) {
		this.purchaseDealDate = purchaseDealDate;
	}

	public String getSecrecyProtocolDate() {
		return secrecyProtocolDate;
	}
	public void setSecrecyProtocolDate(String secrecyProtocolDate) {
		this.secrecyProtocolDate = secrecyProtocolDate;
	}
	public String getPurchaseDeal() {
		return purchaseDeal;
	}
	public void setPurchaseDeal(String purchaseDeal) {
		this.purchaseDeal = purchaseDeal;
	}
	public String getSecrecyProtocol() {
		return secrecyProtocol;
	}
	public void setSecrecyProtocol(String secrecyProtocol) {
		this.secrecyProtocol = secrecyProtocol;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	
	
	
}
