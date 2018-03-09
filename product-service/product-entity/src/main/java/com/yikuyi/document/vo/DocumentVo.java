/*
 * Created: 2017年2月7日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.document.vo;

import org.springframework.util.StringUtils;

import com.yikuyi.document.model.Document;

public class DocumentVo extends Document {

	private static final long serialVersionUID = -4287417531644125357L;
	// 供应商名称
	private String partyName;
	// 仓库名称
	private String facilityName;
	
	private String isCancelVal;
	
	public String getIsCancelVal() {
		return isCancelVal;
	}
	public void setIsCancelVal(String isCancelVal) {
		if(StringUtils.isEmpty(isCancelVal) || !"1".equals(isCancelVal)){
			this.isCancelVal = "No";
		}else if("1".equals(isCancelVal)){ 
			this.isCancelVal = "Yes";
		}
	}
	public String getPartyName() {
		return partyName;
	}

	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}

	public String getFacilityName() {
		return facilityName;
	}

	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}

}
