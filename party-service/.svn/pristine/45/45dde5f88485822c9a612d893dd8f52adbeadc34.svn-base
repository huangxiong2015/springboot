package com.yikuyi.party.acl.api.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yikuyi.party.acl.api.IACLResource;
import com.yikuyi.party.acl.bll.ACLManager;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.vo.RoleTypeVo;
import com.yikuyi.party.vo.RoleVo;
import com.ykyframework.exception.BusinessException;

/**
 * 用户权限信息接口
 * 
 * @author elvin.tang@yikuyi.com
 */
@RestController
@RequestMapping("v1/acl")
public class ACLResource implements IACLResource {

	@Autowired
	private ACLManager aclManager;

	/**
	 * 查询用户已有的角色信息
	 */
	@Override
	@RequestMapping(value = "roles", method = RequestMethod.GET)
	public Set<String> getUserRoleList(@RequestParam("partyId") String partyId) {
		return aclManager.getUserRoleList(partyId);
	}

	/**
	 * 查询用户已有的权限信息
	 */
	@Override
	@RequestMapping(value = "permissions", method = RequestMethod.GET)
	public Set<String> getUserPermissions(@RequestParam("partyId") String partyId) {
		return aclManager.getUserPermissionList(partyId);
	}

	/**
	 * 根据角色ID查询用户
	 */
	@Override
	@RequestMapping(value = "/person", method = RequestMethod.GET)
	public List<Party> getUserByRole(@RequestParam("role") String role) {
		return aclManager.getUserByRole(role);
	}

	/**
	 * 查询配置支持配置菜单的所有角色
	 */
	@Override
	@RequestMapping(value = "/getMenuRole/{roleType}", method = RequestMethod.GET)
	public List<RoleTypeVo> getMenuRole(@PathVariable("roleType") String roleType) {
		return aclManager.getMenuRole(roleType);
	}

	/**
	 * 添加角色菜单关系
	 */
	@Override
	@RequestMapping(value = "/menurole", method = RequestMethod.POST)
	public void addRoelMenuRelaction(@RequestBody RoleVo roleVo) throws BusinessException{
		aclManager.addRoelMenuRelaction(roleVo);
	}
}