/*
 * Created: 2017年3月17日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.audit.api.impl;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.framewrok.springboot.web.RequestHelper;
import com.github.pagehelper.PageInfo;
import com.yikuyi.party.audit.api.IAuditResource;
import com.yikuyi.party.audit.bll.AuditManager;
import com.yikuyi.party.audit.vo.AuditVo;

/**
 * 审计日志接口定义类
 * 
 * @author jik.shu@yikuyi.com
 * @version 1.0.0
 */
@RestController
@RequestMapping("v1/audit")
public class AuditResource implements IAuditResource {

	@Autowired
	private AuditManager auditManager;

	/**
	 * 根据条件查询审计日志
	 */
	@Override
	@RequestMapping(method = RequestMethod.GET)
	public PageInfo<AuditVo> getAuditListByEntity(@ModelAttribute(value = "audit") AuditVo audit,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "size", required = false, defaultValue = "10") int size) {
		audit.setCurrenUserId(RequestHelper.getLoginUserId());
		return auditManager.getAduitListByEntity(audit, new RowBounds((page - 1) * size, size));
	}
	
	
	/**
	 * 定时器：将客户信息的IP地址录入到mongodb
	 */
	@Override
	@RequestMapping(value="/ip/annal", method = RequestMethod.POST)
	public void handleAuditLog(@RequestParam(value = "auditTime" , required = false)String auditTime,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "size", required = false, defaultValue = "100") int size){
		auditManager.handleAuditLog(auditTime,page,size);
		
	}
}