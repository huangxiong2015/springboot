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
package com.yikuyi.party.listener;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;

import com.yikuyi.basedata.ShipmentClientBuilder;
import com.yikuyi.basedata.category.model.Category;
import com.yikuyi.party.customerssync.dao.CustomersSyncDao;
import com.yikuyi.party.group.model.PartyGroup.ActiveStatus;
import com.yikuyi.party.integration.ContactsVo;
import com.yikuyi.party.integration.CustomersSyncVo;
import com.yikuyi.party.integration.CustomersSyncVo.Flag;
import com.yikuyi.party.integration.CustomersVo;
import com.yikuyi.party.integration.PartyIntegrationClientBuilder;
import com.ykyframework.mqservice.receiver.MsgReceiveListener;

@Service
public class CustomersSyncReceiveListener implements MsgReceiveListener {

	private static final Logger logger = LoggerFactory.getLogger(CustomersSyncReceiveListener.class);

	@Autowired
	private PartyIntegrationClientBuilder corporationClient;
	

	@Autowired
	private ShipmentClientBuilder shipmentClientBuilder;

	@Autowired
	private CustomersSyncDao customersSyncDao;

	@Override
	public void onMessage(Serializable msg) {
		try {
			if (null != msg && msg instanceof CustomersSyncVo) {
				CustomersSyncVo msgVo = (CustomersSyncVo) msg;
				//区分调用mq,1为全量同步企业，2增量同步企业，3全量同步个人，4增量同步个人
				if (Flag.CUSTOMERS_SYNC_ALL_ENT.equals(msgVo.getFlag()) || Flag.CUSTOMERS_SYNC_INCREMENT_ENT.equals(msgVo.getFlag())) {
					// 全量或者增量同步企业数据
					CustomersSyncVo vo = getAllEntData(msgVo);
					//批量同步数据
					corporationClient.customerClient().pushCustomerInfo(vo);
				} else if (Flag.CUSTOMERS_SYNC_ALL_PERSONAL.equals(msgVo.getFlag()) || Flag.CUSTOMERS_SYNC_INCREMENT_PERSONAL.equals(msgVo.getFlag())) {
					// 全量或者增量同步所有个人用户数
					corporationClient.customerClient().pushCustomerInfo(msgVo);
				}

			}
		} catch (RestClientException e) {
			logger.error("调用集成接口异常：{},{}", e.getMessage(), e);
		}
	}

	public CustomersSyncVo getAllEntData(CustomersSyncVo msgVo) {
		List<Category> corCategoryList = this.getCategorys("CORPORATION_CATEGORY");
		Map<String, String> corMap = categoryToMap(corCategoryList);
		List<Category> industryCategoryList = this.getCategorys("INDUSTRY_CATEGORY");
		Map<String, String> industryMap = categoryToMap(industryCategoryList);
		
		// 获取企业数据
		List<CustomersVo> listCustomer = msgVo.getCustomers();
		if (CollectionUtils.isNotEmpty(listCustomer)) {
			// 获取企业中用户的数据
			listCustomer.stream().forEach(customersSyncVo -> {
				setCustomersData(corMap, industryMap, customersSyncVo);

			});

		}
		return msgVo;
	}

	private void setCustomersData(Map<String, String> corMap, Map<String, String> industryMap,
			CustomersVo customersSyncVo) {
		//设置行业类型
		if (ActiveStatus.PARTY_VERIFIED.toString().equals(customersSyncVo.getApproveStatus())) {
			customersSyncVo.setApproveStatus("Y");
		} else {
			customersSyncVo.setApproveStatus("N");
		}
		// 获取行业类型名字
		if (!StringUtils.isEmpty(customersSyncVo.getIndustry())) {
			String industryCategory = getCategory(industryMap, customersSyncVo.getIndustry());
			if (customersSyncVo.getIndustry().contains("1008")) {
				customersSyncVo.setIndustry(industryCategory + "(" + customersSyncVo.getIndustryOther() + ")");
			} else {
				customersSyncVo.setIndustry(industryCategory);
			}
		}
		// 获取公司类型
		if (!StringUtils.isEmpty(customersSyncVo.getCustomerType())) {
			String customerType = getCategory(corMap, customersSyncVo.getCustomerType());
			if (customersSyncVo.getCustomerType().contains("2006")) {
				customersSyncVo
						.setCustomerType(customerType + "(" + customersSyncVo.getCustomerTypeOther() + ")");
			} else {
				customersSyncVo.setCustomerType(customerType);
			}
		}
		List<ContactsVo> contactsVoList = customersSyncDao.getUserByIdList(customersSyncVo.getCustomerId());
		if (CollectionUtils.isNotEmpty(contactsVoList)) {
			// 设置企业人数
			customersSyncVo.setEmployeeQty(String.valueOf(contactsVoList.size()));
			customersSyncVo.setContacts(contactsVoList);
		}
	}
	

	/**
	 * 
	 * @param categoryStr
	 * @param category
	 * @return
	 * @since 2017年12月13日
	 * @author zr.helinmei@yikuyi.com
	 */
	public static String getCategory(Map<String, String> map, String categoryStr) {
		String cateStr = "";
		StringBuilder strBuffer = new StringBuilder();
		Map<String, String> newMap = map;
		if (StringUtils.isEmpty(categoryStr)) {
			return null;
		}
		if (newMap == null) {
			newMap = new HashMap<>();
		}
		String[] arrStr = categoryStr.split(",");
		for (String str : arrStr) {
			if (StringUtils.isEmpty(str)) {
				continue;
			}
			String value = newMap.get(str);
			if (StringUtils.isEmpty(value)) {
				strBuffer.append("/" + str);
			} else {
				strBuffer.append("/" + value);
			}

		}
		if (!StringUtils.isEmpty(strBuffer)) {
			cateStr = strBuffer.toString();
			cateStr = cateStr.substring(1);
		}
		return cateStr;
	}

	/**
	 * 业务类型转换map
	 * 
	 * @param list
	 * @return
	 * @since 2017年4月14日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public static Map<String, String> categoryToMap(List<Category> list) {
		if (CollectionUtils.isEmpty(list)) {
			return Collections.emptyMap();
		}
		return list.stream().filter(v-> !StringUtils.isEmpty(v.getCategoryId())).collect(Collectors.toMap(Category::getCategoryId, Category::getCategoryName));
	}

	/**
	 * 根据业务类型获取维度数据
	 * 
	 * @param category
	 * @return
	 * @since 2017年4月14日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public List<Category> getCategorys(String category) {
		List<Category> list = null;
		try {
			return shipmentClientBuilder.categoryResource().categoryList(category);
		} catch (Exception e) {
			logger.error("调用查询业务类型失败：{}", e);
		}
		return list;
	}
	
}