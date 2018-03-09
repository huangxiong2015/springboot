/*
 * Created: 2017年1月18日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.model;


import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("PartyAttribute")
public class PartyAttribute extends AbstractEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6394967529883601832L;
	
	public static final String IS_SHOW_NAME = "IS_SHOW_NAME";

	public static final String IS_SUP_PRICE = "IS_SUP_PRICE";
	
	public static final  String VENDOR_MOV_STATUS = "VENDOR_MOV_STATUS";
	
	public static final  String SKU_MOV_STATUS = "SKU_MOV_STATUS";
	
	@ApiModelProperty(value = "会员ID")
	private String partyId;

	@ApiModelProperty(value = "属性key")
	private String key;
	
	@ApiModelProperty(value = "属性值")
	private String value;
	
	@ApiModelProperty(value = "公司资质")
	private String attrId;
	
	@ApiModelProperty(value = "公司资质父属性id")
	private String parentAttrId;
	
	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public String getAttrId() {
		return attrId;
	}

	public void setAttrId(String attrId) {
		this.attrId = attrId;
	}

	public String getParentAttrId() {
		return parentAttrId;
	}

	public void setParentAttrId(String parentAttrId) {
		this.parentAttrId = parentAttrId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "PartyAttribute [key=" + key + ", value=" + value + "]";
	}

	
	
}
