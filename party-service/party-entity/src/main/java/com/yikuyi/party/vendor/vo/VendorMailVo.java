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
package com.yikuyi.party.vendor.vo;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModelProperty;

/**
 * 供应商添加合作发送邮件信息
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */

public class VendorMailVo extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "公司名称")
	private String groupName;
	
	@ApiModelProperty(value = "公司类型")
	private String groupTypeName;
	
	@ApiModelProperty(value = "产品线")
	private String productLineName;
	
	@ApiModelProperty(value = "联系人")
	private String personName;
	
	@ApiModelProperty(value = "联系人电话")
	private String personTel;
	
	@ApiModelProperty(value = "商品详情id")
	private String recommendationId;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupTypeName() {
		return groupTypeName;
	}

	public void setGroupTypeName(String groupTypeName) {
		this.groupTypeName = groupTypeName;
	}

	public String getProductLineName() {
		return productLineName;
	}

	public void setProductLineName(String productLineName) {
		this.productLineName = productLineName;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getPersonTel() {
		return personTel;
	}

	public void setPersonTel(String personTel) {
		this.personTel = personTel;
	}

	public String getRecommendationId() {
		return recommendationId;
	}

	public void setRecommendationId(String recommendationId) {
		this.recommendationId = recommendationId;
	}
	
	
	
	
}
