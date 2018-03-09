package com.yikuyi.party.contact.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class CreditVo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "账期id")
	private String creditId;
	@ApiModelProperty(value = "授信期限")
	private String creditDeadline;
	
	@ApiModelProperty(value = "对账期限")
	private String checkCycle;
	
	@ApiModelProperty(value = "授信额度")
	private String creditQuota;
	
	@ApiModelProperty(value = "币种")
	private String currency;
	
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCreditId() {
		return creditId;
	}

	public void setCreditId(String creditId) {
		this.creditId = creditId;
	}
	
	public String getCreditDeadline() {
		return creditDeadline;
	}

	public void setCreditDeadline(String creditDeadline) {
		this.creditDeadline = creditDeadline;
	}

	public String getCheckCycle() {
		return checkCycle;
	}

	public void setCheckCycle(String checkCycle) {
		this.checkCycle = checkCycle;
	}

	public String getCreditQuota() {
		return creditQuota;
	}

	public void setCreditQuota(String creditQuota) {
		this.creditQuota = creditQuota;
	}
}
