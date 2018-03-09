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
package com.yikuyi.party.audit.api;

import org.springframework.web.bind.annotation.ModelAttribute;

import com.github.pagehelper.PageInfo;
import com.yikuyi.party.audit.vo.AuditVo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


public interface IAuditResource {

	/**
	 * 查询审计日志
	 * @param auditVo
	 * @param page
	 * @param size
	 * @return
	 * @since 2017年8月23日
	 */
	@ApiOperation(value = "查询审计日志", notes = "查询审计日志")
	public PageInfo<AuditVo> getAuditListByEntity(@ModelAttribute("auditVo") AuditVo auditVo,
			@ApiParam(value = "page", required = false, defaultValue = "1") int page,
			@ApiParam(value = "size", required = false, defaultValue = "10") int size);
	
	
	/**
	 * 定时器：将客户信息的IP地址录入到mongodb
	 * @param auditTime
	 * @since 2017年12月8日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "定时器：将客户信息的IP地址录入到mongodb", notes = "定时器：将客户信息的IP地址录入到mongodb")
	public void handleAuditLog(@ApiParam(value = "查询时间</br>格式：yyyy-MM-dd")String auditTime,
			@ApiParam(value = "page", required = false, defaultValue = "1") int page,
			@ApiParam(value = "size", required = false, defaultValue = "100") int size);
}