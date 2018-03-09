/*
 * Created: 2017年1月11日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.notice.api;

import java.util.List;

import com.yikuyi.party.partyExpand.model.PartyExpand;
import com.ykyframework.exception.BusinessException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 通知提醒
 * @author zr.helinmei@yikuyi.com
 */
public interface INoticeResource {
	
	
	/**
	 * 新增通知
	 * @param partyExpand
	 * @since 2017年11月29日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = " 新增通知", notes = " 新增通知")
	public void insert(@ApiParam(value = " 新增通知", required = true)PartyExpand partyExpand) throws BusinessException;
	
	/**
	 * 修改通知
	 * @param partyExpand
	 * @since 2017年11月29日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = " 修改通知", notes = " 修改通知")
	public void update(@ApiParam(value = " 修改通知", required = true)PartyExpand partyExpand)throws BusinessException;
	
	
	/**
	 * 查询用户通知
	 * @param partyId
	 * @return
	 * @since 2017年11月29日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "查询用户通知", notes = "查询用户通知", response = List.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public List<PartyExpand> getPartyExpandList(@ApiParam(value="partyId",required=true)String partyId);
	
	/**
	 * 删除通知
	 * @param partyExpand
	 * @since 2017年11月29日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = " 删除通知", notes = " 删除通知")
	public void deleteNotice(@ApiParam(value = "删除通知", required = true)String id);
	
	
	/**
	 * 是否存在重复的邮箱
	 * @param email
	 * @return
	 * @since 2017年12月4日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "是否存在重复的邮箱", notes = "是否存在重复的邮箱", response = Boolean.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Boolean.class) 
			})
	public PartyExpand isExistMail(@ApiParam(value = "email", required = true) String email);
	
}