package com.yikuyi.party.contact.dao;

import org.apache.ibatis.annotations.Mapper;

import com.yikuyi.party.contact.model.ContactMech;

@Mapper
public interface PostalAddressDao {
	
    /**
     * 新增地址详情信息
     * @author 张伟
     * @param contactMech 地址对象
     * @return
     */
    int insert(ContactMech contactMech);

}