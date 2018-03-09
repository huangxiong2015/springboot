package com.yikuyi.party.party.dao;

import java.util.Set;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * 会员角色管理
 * 
 * @author 张伟
 *
 */
@Mapper
public interface PartyPermissionDao {

	/**
	 * @param partyGroupId
	 * @param partyId
	 * @return
	 * @since 2017年6月16日
	 * @author jik.shu@yikuyi.com
	 */
	public Set<String> findPermissionByPartyId(@Param("partyGroupId") String  partyGroupId, @Param("partyId") String partyId);

}