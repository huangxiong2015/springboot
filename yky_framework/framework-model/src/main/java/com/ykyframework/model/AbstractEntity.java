/*
 * Created: 2015年10月25日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2015 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ykyframework.model;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * 框架定义的抽象实体类，包含了一些基本的公用字段。
 * @author liaoke@yikuyi.com
 * @version 1.0.0
 */
@SuppressWarnings("serial")
public abstract class AbstractEntity implements Entity {
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间", example="2016-01-01 08:00:00")
	private Date createdDate;
	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人ID")
	private String creator;
	/**
	 * 最近更新时间
	 */
	@ApiModelProperty(value = "最近更新时间", example="2016-01-01 08:00:00")
	private Date lastUpdateDate;
	/**
	 * 最近操作人
	 */
	@ApiModelProperty(value = "最近操作人ID")
	private String lastUpdateUser;
	
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public String getLastUpdateUser() {
		return lastUpdateUser;
	}
	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}
	
	
	
}