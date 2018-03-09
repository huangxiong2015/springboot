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

import java.util.List;

import com.yikuyi.party.contact.model.ContactMech;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * 联系地址的接口定义
 * @author jik.shu@yikuyi.com
 * @version 1.0.0
 */
public interface ContactMechClient {
	
	
	
	/**
	 * 根据地址ID查询地址详情
	 * @param ids 需要查询的地址id  eg:122,12512
	 * @param authToken
	 * @return
	 * @since 2017年6月29日
	 * @author tb.yumu@yikuyi.com
	 */
	@RequestLine("GET /v1/contactMechs?ids={ids}")
	@Headers({ "Authorization: Basic {authToken}" })
	public List<ContactMech> getContactMechList(@Param("ids") String ids,@Param("authToken") String authToken);

}