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
package com.yikuyi.party.audit.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.yikuyi.party.audit.model.Audit;
import com.yikuyi.party.audit.vo.AuditVo;

@Mapper
public interface AuditDao {

	/**
	 * 添加一个审计日志
	 * @param audit
	 * @since 2017年3月17日
	 * @author guowenyao@yikuyi.com
	 */
	public void insert(Audit audit);
	
	/**
	 * 根据审计日志的条件查询所有的审计日志
	 * @param audit
	 * @return
	 * @since 2017年3月17日
	 * @author guowenyao@yikuyi.com
	 */
	public List<AuditVo> getAuditListByEntity(Audit audit,RowBounds rowBounds);

	/**
	 * 查询系统时间的前一天
	 * @return
	 * @since 2017年12月21日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public List<Audit> getLastDayAuditList(RowBounds rowBounds);

	/**
	 * 查询某个时间点到当前时间的list
	 * @param auditTime
	 * @return
	 * @since 2017年12月21日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public List<Audit> getAuditListByAudDate(@Param("auditTime")String auditTime,RowBounds rowBounds);
}