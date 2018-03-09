package com.yikuyi.party.profiles.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yikuyi.party.model.PartyProfileDefault;

@Mapper
public interface PartyProfileDefaultDao {

	/**
	 * 
	 * @author 张伟
	 * @param paratyId
	 *            用户id
	 * @return
	 */
	PartyProfileDefault selectProfileDefault(Map<String, String> param);

	/**
	 * 新增地址
	 * @author 张伟
	 * @param partyProfileDefault
	 *            设置默认的对象
	 * @return
	 */
	int insertProfileDefault(PartyProfileDefault partyProfileDefault);

	/**
	 * 更新默认信息
	 * @author 张伟
	 * @param partyProfileDefault
	 *            需要更新 的对象
	 * @return
	 */
	int updateProfileDefault(PartyProfileDefault partyProfileDefault);
	
	/**
	 * 删除指定类型的的默认信息
	 * @author 张伟
	 * @return
	 */
	int deleteProfileDefault(@Param("defaultType") String defaultType,@Param("partyId") String partyId);

}