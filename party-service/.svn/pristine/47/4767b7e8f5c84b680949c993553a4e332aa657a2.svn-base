package com.yikuyi.party.contact.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.yikuyi.party.contact.model.ContactMech;

@Mapper
public interface ContactMechDao {
    /**
     * 保持地址信息
     * @author 张伟
     * @param contactMech 需要保存的地址对象
     * @return
     */
    int insert(ContactMech contactMech);
    
    /**
     * 根据ids集合查找一个地址列表
     * @author 张伟
     * @param ids 地址集合对象
     * @return
     */
    List<ContactMech> getContactMechByIds(List<String> ids);

}