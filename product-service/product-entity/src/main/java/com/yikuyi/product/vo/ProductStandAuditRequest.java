/*
 * Created: 2017年8月16日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class ProductStandAuditRequest {
	
	@ApiModelProperty(value="ids")
	private List<String> ids;
	/**
	 * 审核状态
	 */
	@ApiModelProperty(value="审核状态 0:待审核 1:未通过	2:通过")
	private Integer auditStats;
	/**
	 * 描述
	 */
	@ApiModelProperty(value="描述")
	private String descr;
	public List<String> getIds() {
		return ids;
	}
	public void setIds(List<String> ids) {
		this.ids = ids;
	}
	public Integer getAuditStats() {
		return auditStats;
	}
	public void setAuditStats(Integer auditStats) {
		this.auditStats = auditStats;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	
	
}
