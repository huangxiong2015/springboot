package com.yikuyi.party.contact.vo;

import java.util.List;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModelProperty;

public class CompanyInfoVo extends AbstractEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@ApiModelProperty(value = "联系人集合")
	private List<PersonInfoVo> personalList;
	
	@ApiModelProperty(value = "公司id")
	private String id;
	
	@ApiModelProperty(value = "公司名称")
	private String companyName;
	
	@ApiModelProperty(value = "企业类型")
	private String customerType;

	@ApiModelProperty(value = "YKY编码")
	private String partyCode;
	
	
	@ApiModelProperty(value = "公司类型其他")
	private String customerTypeOther;
	

	public String getCustomerTypeOther() {
		return customerTypeOther;
	}

	public void setCustomerTypeOther(String customerTypeOther) {
		this.customerTypeOther = customerTypeOther;
	}

	public List<PersonInfoVo> getPersonalList() {
		return personalList;
	}

	public void setPersonalList(List<PersonInfoVo> personalList) {
		this.personalList = personalList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getPartyCode() {
		return partyCode;
	}

	public void setPartyCode(String partyCode) {
		this.partyCode = partyCode;
	}


}
