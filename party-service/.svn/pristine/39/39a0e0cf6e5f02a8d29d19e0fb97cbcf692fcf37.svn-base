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
package com.yikuyi.party.profiles.bll;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yikuyi.party.contact.bll.ContactMechManager;
import com.yikuyi.party.contact.model.ContactMech;
import com.yikuyi.party.model.PartyProfileDefault;
import com.yikuyi.party.profiles.dao.PartyProfileDefaultDao;

@Service
@Transactional
public class PartyProfileDefaultManager {

	public static final ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private PartyProfileDefaultDao partyProfileDefaultDao;
	

	@Autowired
	private ContactMechManager contactMechManager;
	
	/**
	 * 新增地址
	 * 
	 * @author 张伟
	 * @param partyProfileDefault
	 *            设置默认的对象
	 * @return
	 */
	public PartyProfileDefault insertProfileDefault(PartyProfileDefault partyProfileDefault) {
		
		Map<String, String> param=new HashMap<>();
		param.put("partyId", partyProfileDefault.getPartyId());
		
		PartyProfileDefault entity = partyProfileDefaultDao.selectProfileDefault(param);
		if (null == entity) {
			 partyProfileDefaultDao.insertProfileDefault(partyProfileDefault);
		} else {
			 partyProfileDefaultDao.updateProfileDefault(partyProfileDefault);
		}
		return partyProfileDefault;
	}
	/**
	 * 获取默认地址
	 * @author 张伟
	 * @param partyId
	 * @return
	 */
	public PartyProfileDefault getProfileDefault(String partyId) {
		Map<String, String> param=new HashMap<>();
		param.put("partyId", partyId);
		PartyProfileDefault partyProfileDefault =partyProfileDefaultDao.selectProfileDefault(param);
		if(null != partyProfileDefault){
			ContactMech addrCNY = partyProfileDefault.getDefaultShipAddrCNY();
			ContactMech addrUSD = partyProfileDefault.getDefaultShipAddrHK();
			if(null != addrCNY){
				List<ContactMech>  cny = contactMechManager.getContactMechByIds(addrCNY.getId());
				if(CollectionUtils.isNotEmpty(cny)){
					partyProfileDefault.setDefaultShipAddrCNY(cny.get(0));	
				}
			}
			if(null != addrUSD){
				List<ContactMech>  usd = contactMechManager.getContactMechByIds(addrUSD.getId());
				if(CollectionUtils.isNotEmpty(usd)){
					partyProfileDefault.setDefaultShipAddrHK(usd.get(0));
				}
			}
		}
		return  partyProfileDefault;
	}
}