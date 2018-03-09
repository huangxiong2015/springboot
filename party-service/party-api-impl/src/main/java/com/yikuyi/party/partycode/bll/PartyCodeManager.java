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
package com.yikuyi.party.partycode.bll;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framewrok.springboot.web.RequestHelper;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.model.PartyRelationship;
import com.yikuyi.party.model.PartyRelationship.PartyRelationshipTypeEnum;
import com.yikuyi.party.party.dao.PartyDao;
import com.yikuyi.party.partygroup.dao.PartyRelationshipDao;
import com.ykyframework.exception.BusinessException;

@Service
@Transactional
public class PartyCodeManager {
	

	
	@Autowired
	private PartyDao partyDao;
	
	@Autowired
	private PartyRelationshipDao partyRelationshipDao;
	
	/**
	 * 生成PARTY_CODE
	 * @param partyId
	 * @param partyCode
	 * @return
	 * @throws BusinessException
	 * @since 2017年7月20日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public String savePatyCode(String partyId,String partyCode) throws BusinessException {
		if(StringUtils.isBlank(partyCode) || StringUtils.isBlank(partyId)){
			throw new BusinessException("PARAMETER_ERROR");
		}
		Party party = new Party();
		party.setId(partyId);
		party.setPartyCode(partyCode);
		party.setLastUpdateUser(RequestHelper.getLoginUserId());
		party.setLastUpdateDate(new Date());
		partyDao.updateParty(party);
		
		//同时修改认证企业的认证状态
		PartyRelationship relationShip = PartyRelationship.build(PartyRelationshipTypeEnum.ENTERPRISE_CERTIFIED);
		relationShip.setPartyIdTo(partyId);// 企业partyId
		List<PartyRelationship> relationList = partyRelationshipDao.getPartyRelationship(relationShip);
		for(PartyRelationship tempEntity : relationList){
			party.setId(tempEntity.getPartyIdFrom());
			party.setPartyCode(partyCode);
			party.setLastUpdateUser(RequestHelper.getLoginUserId());
			party.setLastUpdateDate(new Date());
			partyDao.updateParty(party);
		}
		return partyCode;
	}

	/**
	 * 获取易库易编码
	 * @param partyCode
	 * @return
	 * @since 2017年7月20日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public String getPartyCode(String partyCode) {
		return partyDao.getPartyCode(partyCode);
	}
}