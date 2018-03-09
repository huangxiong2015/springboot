/*
 * Created: 2017年1月19日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.party.bll;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.yikuyi.party.model.PartyAttribute;
import com.yikuyi.party.party.dao.PartyAttributeDao;

@Service
@Transactional
public class PartyAttributeManager {

	@Autowired
	private PartyAttributeDao partyAttributeDao;

	/**
	 * 根据ID获取当前记录的所有属性的数据集合或者指定字段的集合
	 * 
	 * @author 张伟
	 * @param partyId
	 * @param attrName
	 * @return
	 */
	public List<PartyAttribute> getPartyAttributelist(String partyId, String attrName) {
		List<PartyAttribute> result = new ArrayList<>();
		List<PartyAttribute> list = partyAttributeDao.getPartAttribute(partyId);
		if (!StringUtils.isEmpty(attrName) && CollectionUtils.isNotEmpty(list)) {
			for (PartyAttribute partyAttribute : list) {
				if (attrName.equals(partyAttribute.getKey())) {
					result.add(partyAttribute);
				}
			}
			return result;
		}
		return list;
	}
}