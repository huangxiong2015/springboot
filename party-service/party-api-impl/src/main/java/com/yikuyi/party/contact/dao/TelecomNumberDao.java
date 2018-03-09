package com.yikuyi.party.contact.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yikuyi.party.contact.model.ContactMech;
@Mapper
public interface TelecomNumberDao {
    /**
     * 保存地址手机号码信息
     * @author 张伟
     * @param contactMech
     * @return
     */
    int insertMobile(ContactMech contactMech);
    /**
     * 保存地址座机号码信息
     * @author 张伟
     * @param contactMech
     * @return
     */
    int insertPhone(ContactMech contactMech);
    /**
     * 保存地址qq信息号码信息
     * @author 张伟
     * @param contactMech
     * @return
     */
    int insertQQ(ContactMech contactMech);
    
    /**
     * 保存地址传真信息号码信息
     * @author 张伟
     * @param contactMech
     * @return
     */
    int insertFax(ContactMech contactMech);
    
    /**
     * 根据地址类型获取这种联系方式
     * @author 张伟
     * @param id
     * @return
     */
    List<ContactMech> getTelecomNumberByIds(@Param("ids") List<String> ids);
    
}