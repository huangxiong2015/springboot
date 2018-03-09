/*
 * Created: 2017年9月21日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

@SuppressWarnings("serial")
public class ProductRequest implements Serializable {
	@ApiModelProperty(value = "id")
	private String id;
	@ApiModelProperty(value = "型号")
	private String manufacturerPartNumber;
	@ApiModelProperty(value = "品牌")
	private String manufacturer;
	@ApiModelProperty(value = "品牌Id")
	private String manufacturerId;
	@ApiModelProperty(value = "供应商Id")
	private String vendorId;
	@ApiModelProperty(value = "仓库Id")
	private String sourceId;
	@ApiModelProperty(value = "大类")
	private Integer cate1Id;
	@ApiModelProperty(value = "小类")
	private Integer cate2Id;
	@ApiModelProperty(value = "次小类")
	private Integer cate3Id;
	@ApiModelProperty(value = "关键字")
	private String keyword;
	@ApiModelProperty(value = "是否标准")
	private Boolean standard;
	@ApiModelProperty(value = "任务id")
	private String taskId;
	
	@ApiModelProperty(value = "上传时间-开始时间")
	private String startDate;
	
	@ApiModelProperty(value = "上传时间-结束时间")
	private String endDate;
	
	@ApiModelProperty(value = "是否有库存")
	private String hasQty;
	
	@ApiModelProperty(value = "商品是否失效")
	private String isInvalid;
	
	public ProductRequest(){}
	public ProductRequest(String id, String manufacturerPartNumber,
			String manufacturer, String vendorId, String sourceId,
			Integer cate1Id, Integer cate2Id, Integer cate3Id, String keyword,
			String startDate,String endDate,
			Boolean standard, String taskId,String hasQty,String isInvalid) {
		this.id = id;
		this.manufacturerPartNumber = manufacturerPartNumber;
		this.manufacturer = manufacturer;
		this.vendorId = vendorId;
		this.sourceId = sourceId;
		this.cate1Id = cate1Id;
		this.cate2Id = cate2Id;
		this.cate3Id = cate3Id;
		this.keyword = keyword;
		this.startDate = startDate;
		this.endDate = endDate;
		this.standard = standard;
		this.taskId = taskId;
		this.hasQty = hasQty;
		this.isInvalid = isInvalid;
	}
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
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getVendorId() {
		return vendorId;
	}
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	
	public Integer getCate1Id() {
		return cate1Id;
	}

	public void setCate1Id(Integer cate1Id) {
		this.cate1Id = cate1Id;
	}

	public Integer getCate2Id() {
		return cate2Id;
	}

	public void setCate2Id(Integer cate2Id) {
		this.cate2Id = cate2Id;
	}

	public Integer getCate3Id() {
		return cate3Id;
	}

	public void setCate3Id(Integer cate3Id) {
		this.cate3Id = cate3Id;
	}

	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public Boolean getStandard() {
		return standard;
	}
	public void setStandard(Boolean standard) {
		this.standard = standard;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getHasQty() {
		return hasQty;
	}
	public void setHasQty(String hasQty) {
		this.hasQty = hasQty;
	}
	public String getIsInvalid() {
		return isInvalid;
	}
	public void setIsInvalid(String isInvalid) {
		this.isInvalid = isInvalid;
	}
	public String getManufacturerId() {
		return manufacturerId;
	}
	public void setManufacturerId(String manufacturerId) {
		this.manufacturerId = manufacturerId;
	}
	
}
