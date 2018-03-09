package com.yikuyi.party.partycode.dao;

import org.apache.ibatis.annotations.Mapper;

import com.yikuyi.party.partycode.model.PartyCode;

@Mapper
public interface PartyCodeDao {
	
	/**
	 * 新生成PartyCode
	 * @param entity
	 * @return
	 * @since 2017年5月9日
	 * @author zr.shuzuo@yikuyi.com
	 */
	int insertPartyCode(PartyCode entity);
	
	/**
	 * 更新PartyCode
	 * 这个方法只更新CodeNum,并且只是在原来基础上+1
	 * @param entity
	 * @return
	 * @since 2017年5月9日
	 * @author zr.shuzuo@yikuyi.com
	 */
	void updatePartyCode(PartyCode entity);
	
	/**
	 * 根据前缀获取最新编码(Code_Num会在原来的基础上+1)
	 * @param entity
	 * @return
	 * @since 2017年5月16日
	 * @author zr.shuzuo@yikuyi.com
	 */
	String getNumByPrefix(String codePrefix);
}