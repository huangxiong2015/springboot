/*
 * Created: 2017年12月8日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModelProperty;

@Document(collection = "spu_white_list")
public class SpuWhiteList extends AbstractEntity{

	private static final long serialVersionUID = 7386169761727786111L;
	
	@Id
	@ApiModelProperty(value = "ID")
	@JsonProperty("id")
	private String id;
	
	@ApiModelProperty(value = "型号")
	private String manufacturerPartNumber;
	
	@ApiModelProperty(value = "制造商ID")
	private Integer manufacturerId;
	
	@ApiModelProperty(value = "制造商名称")
	private String manufacturer;
	
	@ApiModelProperty(value = "制造商简称")
	private String brandShort;
	
	@ApiModelProperty(value="创建时间戳")
	private String createdTimeMillis;
	
	@ApiModelProperty(value="创建时间")
	private String createdTime;
	
	@ApiModelProperty(value="更新时间戳")
	private String updatedTimeMillis;
	
	@ApiModelProperty(value="更新时间")
	private String updatedTime;
	
	@ApiModelProperty(value="创建人名称")
	private String creatorName;
	
	@ApiModelProperty(value="最近操作人名称")
	private String lastUpdateUserName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getManufacturerPartNumber() {
		return manufacturerPartNumber;
	}

	public void setManufacturerPartNumber(String manufacturerPartNumber) {
		this.manufacturerPartNumber = manufacturerPartNumber;
	}

	public Integer getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(Integer manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public String getCreatedTimeMillis() {
		return createdTimeMillis;
	}

	public void setCreatedTimeMillis(String createdTimeMillis) {
		this.createdTimeMillis = createdTimeMillis;
	}

	public String getUpdatedTimeMillis() {
		return updatedTimeMillis;
	}

	public void setUpdatedTimeMillis(String updatedTimeMillis) {
		this.updatedTimeMillis = updatedTimeMillis;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getLastUpdateUserName() {
		return lastUpdateUserName;
	}

	public void setLastUpdateUserName(String lastUpdateUserName) {
		this.lastUpdateUserName = lastUpdateUserName;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getBrandShort() {
		return brandShort;
	}

	public void setBrandShort(String brandShort) {
		this.brandShort = brandShort;
	}
	

}
