package com.yikuyi.party.resource;

import java.util.List;
import java.util.Set;

import org.springframework.web.bind.annotation.RequestBody;

import com.yikuyi.party.model.Party;
import com.yikuyi.party.vo.RoleVo;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * 权限相关的接口调用定义
 * 
 * @author jik.shu@yikuyi.com
 */
@Headers({ "Content-Type: application/json;charset=UTF-8" })
public interface AclClient {

	/**
	 * 根据用户ID,查询对应所有角色
	 * @param id
	 * @param authToken
	 * @return
	 * @since 2017年6月28日
	 * @author jik.shu@yikuyi.com
	 */
	@RequestLine("GET /v1/acl/roles?partyId={partyId}")
	@Headers({ "Authorization: Basic {authToken}" })
	public Set<String> getUserRoles(@Param("partyId") String partyId,@Param("authToken") String authToken);
	
	/**
	 * 根据角色获取用户
	 * @param role
	 * @param authToken
	 * @return
	 * @since 2017年6月29日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@RequestLine("GET /v1/acl/person?role={role}")
	@Headers({ "Authorization: Basic {authToken}" })
	public List<Party> getUserByRole(@Param("role") String role,@Param("authToken") String authToken);
	
	/**
	 * 新增partyId和角色的关系
	 * @param roleVo
	 * @param authToken
	 * @return
	 */
	@RequestLine("POST /v1/acl/menurole")
	@Headers({ "Authorization: Basic {authToken}" })
	public void addRoelMenuRelaction(@RequestBody RoleVo roleVo,@Param("authToken") String authToken);
	
	/**
	 * 根据partyId获取用户的权限信息
	 * @param roleVo
	 * @param authToken
	 * @return
	 */
	@RequestLine("GET /v1/acl/permissions?partyId={partyId}")
	@Headers({ "Authorization: Basic {authToken}" })
	public Set<String> getUserPermissions(@Param("partyId") String partyId,@Param("authToken") String authToken);
}