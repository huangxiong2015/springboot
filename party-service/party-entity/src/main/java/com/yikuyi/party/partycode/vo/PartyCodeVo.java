/*
 * Created: 2017年5月9日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.partycode.vo;

import com.ykyframework.model.AbstractEntity;

public class PartyCodeVo extends AbstractEntity  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String partyId;       //主键, Spirit ID ,用于描述人、组织、团体的ID。
	
	private String codePrefix;    //编码前缀
	
	private String codeNum;       //编码流水号
	

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public String getCodePrefix() {
		return codePrefix;
	}

	public void setCodePrefix(String codePrefix) {
		this.codePrefix = codePrefix;
	}

	public String getCodeNum() {
		return codeNum;
	}

	public void setCodeNum(String codeNum) {
		this.codeNum = codeNum;
	}
	
}