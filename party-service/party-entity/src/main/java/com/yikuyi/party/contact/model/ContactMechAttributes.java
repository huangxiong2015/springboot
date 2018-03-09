/*
 * Created: 2017年1月17日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.contact.model;


import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 企业属性表
 * 
 * @author zr.helinmei@yikuyi.com
 *
 */
@ApiModel("ContactMechAttributes")
public class ContactMechAttributes extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8562230060084519811L;
	
	@ApiModelProperty(value = "地址币种 CNY(国内),USD(海外)")
	private ContactMechAttribute usedCurruncy;
	
	@ApiModelProperty(value = "USD(海外)对应的公司名称")
	private ContactMechAttribute usdCompany;

	public ContactMechAttribute getUsedCurruncy() {
		return usedCurruncy;
	}

	public void setUsedCurruncy(ContactMechAttribute usedCurruncy) {
		this.usedCurruncy = usedCurruncy;
	}
	
	
	public ContactMechAttribute getUsdCompany() {
		return usdCompany;
	}

	public void setUsdCompany(ContactMechAttribute usdCompany) {
		this.usdCompany = usdCompany;
	}

	@Override
	public String toString() {
		return "ContactMechAttributes [usedCurruncy=" + usedCurruncy + ",usdCompany="+ usdCompany + "]";
	}
	
}
