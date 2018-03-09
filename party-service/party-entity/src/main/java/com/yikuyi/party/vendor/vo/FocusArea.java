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

import java.util.List;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel("FocusArea")
public class FocusArea extends AbstractEntity {
	
	/**
	 * 供应商仓库
	 */
	private static final long serialVersionUID = 1L;
	
	
	@ApiModelProperty(value = "主键ID")
	private String vendorSourceID;
	
	@ApiModelProperty(value = "供应商Id")
	private String vendorID;
		
	@ApiModelProperty(value = "关注领域")
	private List<String> focusFieldList;
	
	@ApiModelProperty(value = "优势产品类别")
	private List<String> productCategoryList;
	
	@ApiModelProperty(value = "主要客户")
	private List<String> majorClientList;

	public String getVendorSourceID() {
		return vendorSourceID;
	}

	public void setVendorSourceID(String vendorSourceID) {
		this.vendorSourceID = vendorSourceID;
	}

	public String getVendorID() {
		return vendorID;
	}

	public void setVendorID(String vendorID) {
		this.vendorID = vendorID;
	}

	public List<String> getFocusFieldList() {
		return focusFieldList;
	}

	public void setFocusFieldList(List<String> focusFieldList) {
		this.focusFieldList = focusFieldList;
	}

	public List<String> getProductCategoryList() {
		return productCategoryList;
	}

	public void setProductCategoryList(List<String> productCategoryList) {
		this.productCategoryList = productCategoryList;
	}

	public List<String> getMajorClientList() {
		return majorClientList;
	}

	public void setMajorClientList(List<String> majorClientList) {
		this.majorClientList = majorClientList;
	}

	
	
}
