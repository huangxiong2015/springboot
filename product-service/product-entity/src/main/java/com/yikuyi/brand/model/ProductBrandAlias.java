/*
 * Created: 2017年11月9日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.brand.model;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModelProperty;

/**
 * 制造商（品牌）别名
 * @author tongkun@yikuyi.com
 * @version 1.0.0
 */
public class ProductBrandAlias extends AbstractEntity{

	private static final long serialVersionUID = 3524223979115933800L;

	@ApiModelProperty(value="别名")
	private String name;

	@ApiModelProperty(value="专用的供应商id，为空表示非专用")
	private String vendorId;

	@ApiModelProperty(value="专用的供应商名称，冗余字段")
	private String vendorName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
}
