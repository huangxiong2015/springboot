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
package com.yikuyi.party.shipAddress.bll;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.framewrok.springboot.web.RequestHelper;
import com.yikuyi.basedata.common.model.Currency;
import com.yikuyi.party.contact.bll.ContactMechManager;
import com.yikuyi.party.contact.model.ContactMech;
import com.yikuyi.party.model.PartyContactMech;
import com.yikuyi.party.model.PartyContactMech.PurposeType;
import com.yikuyi.party.model.PartyProfileDefault;
import com.yikuyi.party.model.PartyProfileDefault.DefaultAddressType;
import com.yikuyi.party.profiles.dao.PartyProfileDefaultDao;
import com.yikuyi.party.shipAddress.dao.PartyContactMechDao;
import com.ykyframework.model.IdGen;

@Service
@Transactional
public class PartyContactMechManager {

	public static final ObjectMapper mapper = new ObjectMapper();
	
	@Value("${api.basedata.serverUrlPrefix}")
	private String basedataServerUrlPrefix;

	@Autowired
	private PartyContactMechDao partyContactMechDao;
	
	@Autowired
	private ContactMechManager contactMechManager;
	
	@Autowired
	private PartyProfileDefaultDao partyProfileDefaultDao;
	
	private static final String ADDRESSTYPE = "addressType";
	
	private static final String PARTYID_KEY = "partyId";
	
	private static final String CONTACTMECHID_KEY = "contactMechId";
	
    /**
     * 
     * @author 张伟
     * @param partyContactMech 需要保存的实体对象
     * @return
     */
	public PartyContactMech insert(PartyContactMech partyContactMech) {
		    if(null == partyContactMech){
		    	return null;
		    }
		   // 当前登录用户
		    String partyId = RequestHelper.getLoginUserId();
		    
		    if(!StringUtils.isEmpty(partyContactMech.getPartyId())){
		    	partyId = partyContactMech.getPartyId();
		    }
		    partyContactMech.setPartyId(partyId);
		    partyContactMech.setFromDate(new Date());
		 //自提联系方式，主要地址（注册时候需要验证的地址）公司注册地址 发票地址  需要过期掉历史数据
		  if (partyContactMech.getPurposeType() != PurposeType.SHIPPING_LOCATION) {
			  //保存地址信息
			    ContactMech contactMech =partyContactMech.getContactMech();
			    Long id = IdGen.getInstance().nextId();
			    contactMech.setId(id.toString());
			    contactMechManager.insert(contactMech);
			    
				partyContactMech.setContactMech(contactMech);
				
				 //过期当前人 当前类型所有地址
				 Map<String, String> param = new HashMap<>();
				 //地址类型
				 param.put("purposeType", partyContactMech.getPurposeType().toString());
				 // 当前登录用户
				 param.put(PARTYID_KEY, partyId);
				 //过期调当前地址的现有地址用途类型数据
				 partyContactMechDao.updateDueTime(param);
				
				//保存地址和类型的关系
			    partyContactMechDao.insert(partyContactMech);

		} else {
				// 如果是新增一个全新的地址需要保存 详细的地址信息
				// 否则只要保存地址，人，类型的关系
				ContactMech contactMech =partyContactMech.getContactMech();
				Long id = IdGen.getInstance().nextId();
				            contactMech.setId(id.toString());
				            contactMech = contactMechManager.insert(contactMech);
				partyContactMech.setContactMech(contactMech);
			
			if (null != partyContactMech.getPurposeType() && null != partyContactMech.getContactMech().getId()) {
				// 检查制定类型 制定地址是否已经添加过
				Map<String, String> param = new HashMap<>();
				param.put(ADDRESSTYPE, partyContactMech.getPurposeType().toString());
				param.put(CONTACTMECHID_KEY, partyContactMech.getContactMech().getId());
				List<PartyContactMech> list = partyContactMechDao.selectPartyContactMechByType(param);
				// 未添加过的话就添加
				if (list == null || list.isEmpty()) {
					partyContactMechDao.insert(partyContactMech);
				}
			}
		}
		   
		return partyContactMech;
	}
	
    /**
     * @author 张伟
     * @param addressType 地址使用类型
     * @return
     * @throws IOException
     */
	public List<PartyContactMech> selectPartyContactMechByType(PurposeType addressType,String userId,Currency currency){
		//如果userId为null,获取当前登录用户ID
		String partyId = StringUtils.isBlank(userId) ? RequestHelper.getLoginUserId() : userId;

		Map<String, String> param = new HashMap<>();
		
		param.put(PARTYID_KEY, partyId);
		param.put(ADDRESSTYPE, addressType.toString());
		if(null != currency){
			param.put("currency", currency.toString());	
		}
		List<PartyContactMech> list = partyContactMechDao.selectPartyContactMechByType(param);
		if(null == list || list.isEmpty()){
			return list;
		}
		
		StringBuilder ids = new StringBuilder();
		list.stream().forEach(v -> ids.append(v.getContactMech().getId()).append(","));
		
		List<ContactMech> contactMechList = contactMechManager.getContactMechByIds(ids.toString().substring(0, ids.length() - 1));
			
		for (PartyContactMech partyContactMech : list) {
			for (ContactMech contactMech : contactMechList) {
				if (partyContactMech.getContactMech().getId().equals(contactMech.getId())) {
					partyContactMech.setContactMech(contactMech);
					break;
				}
			}
		}
		return list;
	}
	
   /**
    * 
    * @author 张伟
    * @param contactMechId  地址id
    * @param addressType  地址类型
    * @return
    * @throws IOException
    */
	public PartyContactMech selectPartyContactMechByIdAndType(String contactMechId, PurposeType addressType,Currency currency)
			throws IOException {
         
		String partyId = RequestHelper.getLoginUserId();
        
		Map<String, String> param = new HashMap<>();

		param.put(PARTYID_KEY, partyId);
		param.put(CONTACTMECHID_KEY, contactMechId);
		param.put(ADDRESSTYPE, addressType.toString());
		if(null != currency){
			param.put("currency", currency.toString());	
		}
        
		PartyContactMech entity = partyContactMechDao.selectPartyContactMechByIdAndType(param);

		if (null != entity) {			
			List<ContactMech> contactMechList=contactMechManager.getContactMechByIds(contactMechId);
			entity.setContactMech(contactMechList.get(0));
		}

		return entity;
	}
	
	/**
	 * 修改地址  过期 重新新增
	 * @author 1044867128@qq.com
	 * @param contactMechId  地址id
	 * @param addressType  地址类型
	 * @param partyContactMech 需要修改的对象
	 * @return
	 */
	public PartyContactMech updateDueTime(String contactMechId,PartyContactMech partyContactMech) {
		 Map<String, String> param = new HashMap<>();
		 param.put(CONTACTMECHID_KEY, contactMechId);
		 // 当前登录用户
		 String partyId = RequestHelper.getLoginUserId();
		 param.put(PARTYID_KEY, partyId);
		 //过期调当前地址的现有地址用途类型数据
		 partyContactMechDao.updateDueTime(param);
		 
		    // 保存地址详细信息
		    ContactMech contactMech = partyContactMech.getContactMech();
		    Long id = IdGen.getInstance().nextId();
            contactMech.setId(id.toString());
		    contactMech = contactMechManager.insert(contactMech);
			
		    //查找过期的所有地址类型
		    List<String> types = partyContactMechDao.selectAddressTypeByContactMechId(param);
		    //保存当前地址的现有地址用途类型数据
		    for (String type : types) {
		    	PartyContactMech temppartyContactMech=new PartyContactMech();
		    	temppartyContactMech.setPartyId(partyId);
		    	temppartyContactMech.setContactMech(contactMech);
		    	temppartyContactMech.setPurposeType(PurposeType.valueOf(type));
		    	temppartyContactMech.setFromDate(new Date());
				partyContactMechDao.insert(temppartyContactMech);
			}
			partyContactMech.setContactMech(contactMech);
		
			return partyContactMech;
	}
	
	/**
	 * 
	 * @author 张伟
	 * @param contactMechId 需要删除的地址id
	 * @param addressType 需要删除的地址类型
	 */
	public void deletePartyContactMech(String contactMechId) {
		 Map<String, String> param = new HashMap<>();
		 param.put(CONTACTMECHID_KEY, contactMechId);
		 String partyId = RequestHelper.getLoginUserId();
		 
		 param.put(PARTYID_KEY, partyId);
		 //过期调数据
		 partyContactMechDao.updateDueTime(param);
		 
		 PartyProfileDefault partyProfileDefault =partyProfileDefaultDao.selectProfileDefault(param);
			if(null != partyProfileDefault){
				ContactMech addrCNY = partyProfileDefault.getDefaultShipAddrCNY();
				ContactMech addrUSD = partyProfileDefault.getDefaultShipAddrHK();
				if(null != addrCNY && contactMechId.equals(addrCNY.getId())){
					partyProfileDefaultDao.deleteProfileDefault(DefaultAddressType.DEFAULT_SHIP_ADDR_CNY.toString(),partyId);
				}
				if(null != addrUSD && contactMechId.equals(addrUSD.getId())){
					partyProfileDefaultDao.deleteProfileDefault(DefaultAddressType.DEFAULT_SHIP_ADDR_HK.toString(),partyId);
				}
			}
	}
	
	/**
	 * 查询SDK用户地址list
	 * @param contactMechIds
	 * @return
	 * @since 2017年7月25日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public List<ContactMech> selectPartyContactMechList(List<String> contactMechIds) {
	   return contactMechManager.getContactMechByIds(String.join(",",contactMechIds));
	}
}