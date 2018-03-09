package com.yikuyi.party.contact.dao;

import org.apache.ibatis.annotations.Mapper;

import com.yikuyi.party.contact.model.ContactMech;

@Mapper
public interface ContactMechAttributeDao {
	
	//用户地址币种类型CNY(国内) USD(香港)
	int insertUsedCurruncy(ContactMech contactMech);

	//如果新增地址为香港地址(USD)类型，则要添加公司名称信息
	//新：两种类型都添加公司名称 2018-1-11
	int insertUsdCompany(ContactMech contactMech);
}