/*
 * Created: 2017年8月25日
 *
 * Shenzhen Yikuyi Co.; Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License; Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes;
 * any java names; variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

@SuppressWarnings("serial")
public class ProductStandRequest implements Serializable{
	
	@ApiModelProperty(value = "型号")
	private String manufacturerPartNumber; 
	@ApiModelProperty(value = "型号（精确）")
	private String manufacturerPartNumberExact; 
	@ApiModelProperty(value = "品牌")
	private String manufacturer;
	@ApiModelProperty(value = "开始日期")
	private String startDate;
	@ApiModelProperty(value = "结束日期")
	private String endDate;
	@ApiModelProperty(value = "状态")
	private Integer status;
	@ApiModelProperty(value = "审核状态")
	private Integer auditStatus;
	@ApiModelProperty(value = "大类")
	private Integer cate1Name;
	@ApiModelProperty(value = "小类")
	private Integer cate2Name;
	@ApiModelProperty(value = "次小类")
	private Integer cate3Name;
	@ApiModelProperty(value = "审核人名")
	private String auditUserName;
	
	private String ids;
	
	@ApiModelProperty(value = "任务id")
	private String taskId;
	
	public ProductStandRequest(){}
	
	public ProductStandRequest(String manufacturerPartNumber,String manufacturerPartNumberExact, String manufacturer, String startDate, String endDate,
			Integer status, Integer auditStatus, Integer cate1Name, Integer cate2Name, Integer cate3Name,
			String auditUserName,String ids) {
		this.manufacturerPartNumber = manufacturerPartNumber;
		this.manufacturerPartNumberExact = manufacturerPartNumberExact;
		this.manufacturer = manufacturer;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.auditStatus = auditStatus;
		this.cate1Name = cate1Name;
		this.cate2Name = cate2Name;
		this.cate3Name = cate3Name;
		this.auditUserName = auditUserName;
		this.ids = ids;
	}
	
	public String getTaskId() {
		return taskId;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}


	public String getManufacturerPartNumber() {
		return manufacturerPartNumber;
	}
	public void setManufacturerPartNumber(String manufacturerPartNumber) {
		this.manufacturerPartNumber = manufacturerPartNumber;
	}
	
	public String getManufacturerPartNumberExact() {
		return manufacturerPartNumberExact;
	}

	public void setManufacturerPartNumberExact(String manufacturerPartNumberExact) {
		this.manufacturerPartNumberExact = manufacturerPartNumberExact;
	}

	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}
	public Integer getCate1Name() {
		return cate1Name;
	}
	public void setCate1Name(Integer cate1Name) {
		this.cate1Name = cate1Name;
	}
	public Integer getCate2Name() {
		return cate2Name;
	}
	public void setCate2Name(Integer cate2Name) {
		this.cate2Name = cate2Name;
	}
	public Integer getCate3Name() {
		return cate3Name;
	}
	public void setCate3Name(Integer cate3Name) {
		this.cate3Name = cate3Name;
	}
	public String getAuditUserName() {
		return auditUserName;
	}
	public void setAuditUserName(String auditUserName) {
		this.auditUserName = auditUserName;
	}
}
