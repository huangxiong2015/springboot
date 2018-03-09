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
package com.ykyframework.api.persist;

import java.util.Date;

import com.ykyframework.model.Entity;

/**
 * 框架定义的抽象实体类，包含了一些基本的公用字段。 原有易库易框架的entity定义
 * 
 * 请使用 com.ykyframework.model.AbstractEntity
 * @author liaoke@yikuyi.com
 * @version 1.0.0
 * @see com.ykyframework.model.AbstractEntity
 */
@Deprecated
@SuppressWarnings("serial")
public abstract class AbstractEntity implements Entity {
	/**
	 * 创建时间
	 */
	private Date createdDate;
	/**
	 * 创建人
	 */
	private Long creator;
	/**
	 * 最近更新时间
	 */
	private Date lastUpdateDate;
	/**
	 * 最近操作人
	 */
	private Long lastUpdateUser;
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Long getCreator() {
		return creator;
	}
	public void setCreator(Long creator) {
		this.creator = creator;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public Long getLastUpdateUser() {
		return lastUpdateUser;
	}
	public void setLastUpdateUser(Long lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}
	
	
	
}
