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
package com.yikuyi.party.supplier;

import java.util.Set;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModelProperty;

/**
 * 供应商邮箱信息vo
 * 
 * @author jik.shu@yikuyi.com
 * @version 1.0.0
 */
public class SupplierMailVo extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "供应商ID")
	private String supplierId;
	
	@ApiModelProperty(value = "供应商对外显示名称")
	private String supplierName;
	
	@ApiModelProperty(value = "责任人邮箱")
	private Set<String> principalMails;

	@ApiModelProperty(value = "询价员邮箱")
	private Set<String> inquiryMails;
	
	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	public Set<String> getPrincipalMails() {
		return principalMails;
	}

	public void setPrincipalMails(Set<String> principalMails) {
		this.principalMails = principalMails;
	}

	public Set<String> getInquiryMails() {
		return inquiryMails;
	}

	public void setInquiryMails(Set<String> inquiryMails) {
		this.inquiryMails = inquiryMails;
	}
}