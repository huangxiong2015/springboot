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
package com.yikuyi.party.notice.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yikuyi.party.partyExpand.model.PartyExpand;

@Mapper
public interface NoticeDao {

	/**
	 * 新增通知
	 * @param partyExpand
	 * @since 2017年11月29日
	 * @author zr.helinmei@yikuyi.com
	 */
	public void insert(PartyExpand partyExpand);
	
	/**
	 * 修改通知
	 * @param partyExpand
	 * @since 2017年11月29日
	 * @author zr.helinmei@yikuyi.com
	 */
	public void update(PartyExpand partyExpand);
	
	/**
	 * 删除通知
	 * @param id
	 * @since 2017年11月29日
	 * @author zr.helinmei@yikuyi.com
	 */
	public void deleteNotice(@Param(value="id")String id);
	
	/**
	 * 根据用户id查询通知数据
	 * @param partyId
	 * @return
	 * @since 2017年11月29日
	 * @author zr.helinmei@yikuyi.com
	 */
	public List<PartyExpand> getPartyExpandList(@Param(value="partyId")String partyId);
	
	
	/**
	 * 是否存在重复的邮箱
	 * @param email
	 * @return
	 * @since 2017年12月4日
	 * @author zr.helinmei@yikuyi.com
	 */
	public PartyExpand isExistMail(@Param("email") String email);
	
}