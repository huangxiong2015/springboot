package com.yikuyi.party.contact.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class CustomerBaseVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "客户名称")
	private String customerName;

	@ApiModelProperty(value = "YKY编码")
	private String partyCode;

	@ApiModelProperty(value = "分类")
	private String companyType;

	@ApiModelProperty(value = "所属地区")
	private String area;

	@ApiModelProperty(value = "客户总公司")
	private String headOffice;

	public String getHeadOffice() {
		return headOffice;
	}

	public void setHeadOffice(String headOffice) {
		this.headOffice = headOffice;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	public String getPartyCode() {
		return partyCode;
	}

	public void setPartyCode(String partyCode) {
		this.partyCode = partyCode;
	}
}
