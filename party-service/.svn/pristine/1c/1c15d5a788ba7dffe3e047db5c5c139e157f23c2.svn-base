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

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 产品线
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@ApiModel("PartyProductLineModel")
public class PartyProductLineModel extends AbstractEntity {

	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "产品线主键ID")
	private String partyProductLineId;

	@ApiModelProperty(value = "供应商外键ID")
	private String partyId;

	@ApiModelProperty(value = "品牌ID")
	private String brandId;

	@ApiModelProperty(value = "品牌名称")
	private String brandName;
	
	@ApiModelProperty(value = "大类ID")
	private String category1Id;

	@ApiModelProperty(value = "大类名称")
	private String category1Name;
	
	@ApiModelProperty(value = "小类ID")
	private String category2Id;

	@ApiModelProperty(value = "小类名称")
	private String category2Name;
	
	@ApiModelProperty(value = "次小类ID")
	private String category3Id;

	@ApiModelProperty(value = "次小类名称")
	private String category3Name;
	
	@ApiModelProperty(value = "状态(ENABLE/UNABLE/DELETE)")
	private Status status;
	
	public enum Status {
		/**
		 * 有效
		 */
		ENABLE,
		/**
		 * 无效
		 */
		UNABLE,
		/**
		 * 删除
		 */
		DELETE;
	}

	public String getPartyProductLineId() {
		return partyProductLineId;
	}

	public void setPartyProductLineId(String partyProductLineId) {
		this.partyProductLineId = partyProductLineId;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getCategory1Id() {
		return category1Id;
	}

	public void setCategory1Id(String category1Id) {
		this.category1Id = category1Id;
	}

	public String getCategory1Name() {
		return category1Name;
	}

	public void setCategory1Name(String category1Name) {
		this.category1Name = category1Name;
	}

	public String getCategory2Id() {
		return category2Id;
	}

	public void setCategory2Id(String category2Id) {
		this.category2Id = category2Id;
	}

	public String getCategory2Name() {
		return category2Name;
	}

	public void setCategory2Name(String category2Name) {
		this.category2Name = category2Name;
	}

	public String getCategory3Id() {
		return category3Id;
	}

	public void setCategory3Id(String category3Id) {
		this.category3Id = category3Id;
	}

	public String getCategory3Name() {
		return category3Name;
	}

	public void setCategory3Name(String category3Name) {
		this.category3Name = category3Name;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}


}
