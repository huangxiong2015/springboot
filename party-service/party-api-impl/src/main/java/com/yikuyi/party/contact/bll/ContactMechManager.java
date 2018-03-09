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
package com.yikuyi.party.contact.bll;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yikuyi.party.contact.dao.ContactMechAttributeDao;
import com.yikuyi.party.contact.dao.ContactMechDao;
import com.yikuyi.party.contact.dao.PostalAddressDao;
import com.yikuyi.party.contact.dao.TelecomNumberDao;
import com.yikuyi.party.contact.model.ContactMech;
import com.yikuyi.party.contact.model.ContactMech.MechType;

@Service
@Transactional
public class ContactMechManager {

	@Autowired
	private ContactMechDao contactMechDao;

	@Autowired
	private PostalAddressDao postalAddressDao;

	@Autowired
	private TelecomNumberDao telecomNumberDao;

	@Autowired
	private ContactMechAttributeDao contactMechAttributeDao;
	
	/**
	 * 保证地址信息
	 * @author 张伟
	 * @param contactMech 需要保持的地址对象
	 * @return
	 */
	public ContactMech insert(ContactMech contactMech) {
		// 地址主表
		contactMechDao.insert(contactMech);
		
		insertPostalAddress(contactMech);
		insertMobile(contactMech);
		insertPhone(contactMech);
		insertQq(contactMech);
		insertFax(contactMech);
		insertontactMechAttribute(contactMech);
		
		return contactMech;
	}
	
	
	// 地址信息
	private void insertPostalAddress(ContactMech contactMech) {
		if (null != contactMech.getPostalAddress()) {
			postalAddressDao.insert(contactMech);
		}
	}

	// 手机
	private void insertMobile(ContactMech contactMech) {
		if (null != contactMech.getTelecomNumber() && null != contactMech.getTelecomNumber().getMobileTelecomNumber()) {
			contactMech.getTelecomNumber().getMobileTelecomNumber().setMechType(MechType.MOBILE);
			telecomNumberDao.insertMobile(contactMech);
		}
	}

	// 座机
	private void insertPhone(ContactMech contactMech) {
		if (null != contactMech.getTelecomNumber() && null != contactMech.getTelecomNumber().getPhoneTelecomNumber()) {
			contactMech.getTelecomNumber().getPhoneTelecomNumber().setMechType(MechType.TELEPHONE);
			telecomNumberDao.insertPhone(contactMech);
		}
	}

	// qq
	private void insertQq(ContactMech contactMech) {
		if (null != contactMech.getTelecomNumber() && null != contactMech.getTelecomNumber().getQqTelecomNumber()) {
			contactMech.getTelecomNumber().getQqTelecomNumber().setMechType(MechType.QQ);
			telecomNumberDao.insertQQ(contactMech);
		}
	}
	
	// 传真
	private void insertFax(ContactMech contactMech) {
		if (null != contactMech.getTelecomNumber() && null != contactMech.getTelecomNumber().getFaxTelecomNumber()) {
			contactMech.getTelecomNumber().getFaxTelecomNumber().setMechType(MechType.FAX);
			telecomNumberDao.insertFax(contactMech);
		}
	}
	
	// 地址币种类型保存
	private void insertontactMechAttribute(ContactMech contactMech) {
		if(null != contactMech.getContactMechAttributes()
			&& null != contactMech.getContactMechAttributes().getUsedCurruncy()){		
			contactMech.getContactMechAttributes().getUsedCurruncy().setKey("USED_CURRUNCY");
			contactMechAttributeDao.insertUsedCurruncy(contactMech);
			
			if(null != contactMech.getContactMechAttributes().getUsedCurruncy().getValue()
				 && null !=contactMech.getContactMechAttributes().getUsdCompany()){
				//则要添加公司信息
					contactMech.getContactMechAttributes().getUsdCompany().setKey("USD_COMPANY");
					contactMechAttributeDao.insertUsdCompany(contactMech);
				
			}


		}
		
	}
	
	
	

	
	/**
	 * 根据ids查找详细id信息
	 * 
	 * @author 1044867128@qq.com
	 * @param ids eg： 1215,46
	 * @return
	 */
	public List<ContactMech> getContactMechByIds(String ids) {
		if (StringUtils.isBlank(ids)) {
			return Collections.emptyList();
		}
		List<String> pids = Arrays.asList(ids.split(","));

		// 查询地址列表
		List<ContactMech> list = contactMechDao.getContactMechByIds(pids);
		// 查询地址联系方式列表
		List<ContactMech> telecomNumbers = telecomNumberDao.getTelecomNumberByIds(pids);
	
		/**
		 * 逐条查找地址联系方式
		 */
		for (ContactMech contactMech : list) {
			for (ContactMech telecomNumber : telecomNumbers) {
				if (contactMech.getId().equals(telecomNumber.getId())) {
					contactMech.setTelecomNumber(telecomNumber.getTelecomNumber());
					break;
				}
			}
		}
		return list;
	}
}