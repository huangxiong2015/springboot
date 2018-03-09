package com.yikuyi.party.vendor.vo;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("PartySupplierAlias")
public class PartySupplierAlias extends AbstractEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4909477597995088784L;
	
	@ApiModelProperty(value = "主键ID")
	private String supplierAliasId;
	
	@ApiModelProperty(value = "供应商id")
	private String partyId;
	
	@ApiModelProperty(value = "别名名称")
	private String aliasName;

	public String getSupplierAliasId() {
		return supplierAliasId;
	}

	public void setSupplierAliasId(String supplierAliasId) {
		this.supplierAliasId = supplierAliasId;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
	
	
}
