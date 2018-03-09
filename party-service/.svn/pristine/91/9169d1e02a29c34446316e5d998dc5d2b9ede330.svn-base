package com.yikuyi.party.party.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yikuyi.party.model.Party;
import com.yikuyi.party.vo.RoleTypeVo;


/**
 * 会员角色管理
 * 
 * @author 张伟
 *
 */
@Mapper
public interface PartyRoleDao {
	/**
	 * 添加会员角色
	 */
	public void insert(@Param("partyId") String partyId, @Param("roleTypeId") String roleTypeId
			,@Param("creator") String creator
			,@Param("createdDate") Date createdDate
			,@Param("lastUpdateUser") String lastUpdateUser
			,@Param("lastUpdateDate") Date lastUpdateDate);

	/**
	 * 根據partyId查詢用戶是否用户角色CUSTOMER
	 * 
	 * @param id
	 * @return String
	 * @since 2017年1月20日
	 * @author zr.helinmei@yikuyi.com
	 */
	public Integer isPersonal(String id);
	

	/**
	 * @param id
	 * @return
	 */
	public Set<String> findRoleByPartyId(String partyId);
	/**
	 * 根据角色id查询用户
	 * @param role
	 * @return
	 * @since 2017年2月28日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public List<Party> getUserByRole(@Param("role") String role);
	
	/**
	 * 查询菜单角色
	 * @param role
	 * @return
	 * @since 2017年4月17日
	 * @author zr.helinmei@yikuyi.com
	 */
	public List<RoleTypeVo> getMenuRole(@Param("roleType") String roleType);
	
	/**
	 * 删除用户已关联的角色
	 * @param partyId
	 * @since 2017年5月10日
	 * @author tb.yumu@yikuyi.com
	 */
	public void deletePartyRole(@Param("partyId") String partyId);
	
	/**
	 * 删除用户已关联的角色
	 * @param partyId
	 * @since 2017年5月10日
	 * @author tb.yumu@yikuyi.com
	 */
	public void deletePartyRoleByType(@Param("partyId") String partyId,@Param("roleList")List<String> roleList);
	
	/**
	 * 根据角色id查询用户
	 * @param role
	 * @return
	 * @since 2017年7月26日
	 * @author zr.helinmei@yikuyi.com
	 */
	public RoleTypeVo getRoleType(@Param("id") String id,@Param("roleTypeId") String roleTypeId);
	
}