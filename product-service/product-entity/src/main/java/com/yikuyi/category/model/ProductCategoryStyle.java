/*
 * Created: 2017年11月20日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.category.model;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModelProperty;

public class ProductCategoryStyle extends AbstractEntity{

	private static final long serialVersionUID = 8824214485578438933L;
	
	@ApiModelProperty(value="字体颜色")
	private String fontColor;

	public String getFontColor() {
		return fontColor;
	}

	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}

}
