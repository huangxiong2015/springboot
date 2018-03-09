package com.yikuyi.party.paymentInvoice;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("PartyInvoiceSettings")
public class PartyInvoiceSettings extends AbstractEntity {

	/**
	 * 序列Id
	 */
	private static final long serialVersionUID = -5027381822889021928L;

	/**
	 * 主键
	 */
	@ApiModelProperty(value="主键id")
	private String partyInvoiceSettingId;
	
	/**
	 * 接受发票方会议ID
	 */
	@ApiModelProperty(value="接受发票方会议ID")
	private String partyId;
	
	/**
	 * 发票类型
	 */
	@ApiModelProperty(value="发票类型")
	private InvoiceTypeId invoiceTypeId;
	
	/**
	 * 发票邮寄地址
	 */
	@ApiModelProperty(value="发票邮寄地址")
	private String contactMechId;
	
	/**
	 * 个人发票、还是公司发票
	 */
	@ApiModelProperty(value="个人发票、还是公司发票")
	private InvoiceOwnerType invoiceOwnerType;
	
	/**
	 * 发票抬头
	 */
	@ApiModelProperty(value="发票抬头")
	private String invoiceHeader;
	
	/**
	 * 纳税人识别码
	 */
	@ApiModelProperty(value="纳税人识别码")
	private String taxCode;
	
	/**
	 * 公司注册地址
	 */
	@ApiModelProperty(value="公司注册地址")
	private String regAddress;
	
	/**
	 * 注册电话
	 */
	@ApiModelProperty(value="注册电话")
	private String regPhone;
	
	/**
	 * 开户银行 
	 */
	@ApiModelProperty(value="开户银行")
	private String bankName;
	
	/**
	 * 银行账号 
	 */
	@ApiModelProperty(value="银行账号")
	private String accountCode;
	
	/**
	 * 币种 
	 */
	@ApiModelProperty(value="币种 ")
	private String currencyUomId;
	
	public String getPartyInvoiceSettingId() {
		return partyInvoiceSettingId;
	}

	public void setPartyInvoiceSettingId(String partyInvoiceSettingId) {
		this.partyInvoiceSettingId = partyInvoiceSettingId;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public String getContactMechId() {
		return contactMechId;
	}

	public void setContactMechId(String contactMechId) {
		this.contactMechId = contactMechId;
	}

	public InvoiceOwnerType getInvoiceOwnerType() {
		return invoiceOwnerType;
	}

	public void setInvoiceOwnerType(InvoiceOwnerType invoiceOwnerType) {
		this.invoiceOwnerType = invoiceOwnerType;
	}

	public String getInvoiceHeader() {
		return invoiceHeader;
	}

	public void setInvoiceHeader(String invoiceHeader) {
		this.invoiceHeader = invoiceHeader;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public String getRegAddress() {
		return regAddress;
	}

	public void setRegAddress(String regAddress) {
		this.regAddress = regAddress;
	}

	public String getRegPhone() {
		return regPhone;
	}

	public void setRegPhone(String regPhone) {
		this.regPhone = regPhone;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public String getCurrencyUomId() {
		return currencyUomId;
	}

	public void setCurrencyUomId(String currencyUomId) {
		this.currencyUomId = currencyUomId;
	}

	public InvoiceTypeId getInvoiceTypeId() {
		return invoiceTypeId;
	}

	public void setInvoiceTypeId(InvoiceTypeId invoiceTypeId) {
		this.invoiceTypeId = invoiceTypeId;
	}

	public enum InvoiceOwnerType{
		//个人
		PERSONAL,
		//企业
		ENTERPRISE
	}
	
	public enum InvoiceTypeId{
		//增值税普通发票
		COMMON,
		//增值税专用发票
		PROFESSIONAL,
		//不需要发票
		NOINVOICE
	}
}
