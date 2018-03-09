package com.yikuyi.party.shipAddress.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yikuyi.party.model.PartyContactMech;

@Mapper
public interface PartyContactMechDao {
	/**
	 * 新增地址
	 * 
	 * @author 1044867128@qq.com
	 * @param partyContactMech
	 * @return
	 */
	int insert(PartyContactMech partyContactMech);

	/**
	 * 根据类型和用户id
	 * 
	 * @author 张伟
	 * @param param
	 * @return
	 */
	List<PartyContactMech> selectPartyContactMechByType(Map<String, String> param);

	/**
	 * 查询单个记录
	 * 
	 * @author 张伟
	 * @param param
	 * @return
	 */
	PartyContactMech selectPartyContactMechByIdAndType(Map<String, String> param);
	/**
	 * 过期 地址数据
	 * @author 张伟
	 * @param param
	 */
	void updateDueTime(Map<String, String> param);
	
	/**
	 * 根据地址查找地址使用的类型列表
	 * @author 张伟
	 * @param param
	 * @return
	 */
	List<String> selectAddressTypeByContactMechId(Map<String, String> param);

	/**
	 * 根据contactMechIds查询地址列表
	 * @param substring
	 * @return
	 * @since 2017年7月25日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	List<PartyContactMech> selectPartyContactMechByContactMechIds(@Param("contactMechIds") List<String> contactMechIds);

}