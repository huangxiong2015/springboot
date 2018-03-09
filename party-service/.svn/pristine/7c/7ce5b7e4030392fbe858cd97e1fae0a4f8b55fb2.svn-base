/*
 * Created: 2016年11月28日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.contact.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yikuyi.party.contact.api.IContactMechResource;
import com.yikuyi.party.contact.bll.ContactMechManager;
import com.yikuyi.party.contact.model.ContactMech;


@RestController
@RequestMapping("v1/contactMechs")
public class ContactMechResource implements IContactMechResource {

	@Autowired
	private ContactMechManager contactMechManager;  

	/**
	 * 根据地址ID查询地址详情
	 * ids 需要查询的地址id  eg:122,12512
	 */
	@Override
	@RequestMapping(method = RequestMethod.GET)
	public List<ContactMech> getContactMechList(@RequestParam(required = true) String ids) {
		return contactMechManager.getContactMechByIds(ids);
	}
}