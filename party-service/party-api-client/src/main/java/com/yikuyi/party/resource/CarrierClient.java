/*
 * Created: 2017年6月29日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.resource;

import org.springframework.web.bind.annotation.RequestBody;

import com.github.pagehelper.PageInfo;
import com.yikuyi.party.model.Party.PartyStatus;
import com.yikuyi.party.vo.PartyCarrierVo;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * 物流信息管理
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@Headers({ "Content-Type: application/json;charset=UTF-8" })
public interface CarrierClient {
	
	
	
	
	
	
	/**
	 * 查询物流信息列表
	 * @param groupName
	 * @param partyStatus
	 * @param createDateStart
	 * @param createDateEnd
	 * @param lastUpdateDateStart
	 * @param lastUpdateDateEnd
	 * @param page
	 * @param size
	 * @param authToken
	 * @return
	 * @since 2017年8月2日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@RequestLine("GET /v1/carrier?groupName={groupName}&partyStatus={partyStatus}&createDateStart={createDateStart}"
			+ "&createDateEnd={createDateEnd}&lastUpdateDateStart={lastUpdateDateStart}&lastUpdateDateEnd={lastUpdateDateEnd}"
			+ "&page={page}&size={size}")
	@Headers({ "Authorization: Basic {authToken}" })
	public PageInfo<PartyCarrierVo> getPartyCarrierList(@Param("groupName") String groupName,
			@Param("partyStatus") PartyStatus partyStatus,
			@Param("createDateStart") String createDateStart,
			@Param("createDateEnd") String createDateEnd,
			@Param("lastUpdateDateStart") String lastUpdateDateStart,
			@Param("lastUpdateDateEnd") String lastUpdateDateEnd,
			@Param("page") int page,
			@Param("size") int size,
			@Param("authToken") String authToken);
	
	
	/**
	 * 根据partyId查询相应的物流信息
	 * @param partyId
	 * @param authToken
	 * @return
	 * @since 2017年8月2日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@RequestLine("GET /v1/carrier/{partyId}")
	@Headers({ "Authorization: Basic {authToken}" })
	public PartyCarrierVo getPartyCarrierVoInfo(@Param("partyId") String partyId,@Param("authToken") String authToken);
	
	
	/**
	 * 物流公司信息-启用/停用
	 * @param partyId
	 * @param partyCarrierVo
	 * @param authToken
	 * @since 2017年8月2日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@RequestLine("PUT /v1/carrier/{partyId}")
	@Headers({ "Authorization: Basic {authToken}" })
	public void updateStateId(@Param("partyId") String partyId,@RequestBody PartyCarrierVo partyCarrierVo, @Param("authToken") String authToken);
}
