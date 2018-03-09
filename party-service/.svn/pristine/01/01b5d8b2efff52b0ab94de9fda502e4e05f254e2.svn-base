/*
 * Created: 2017年1月11日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.acl.api;

import java.util.List;
import java.util.Set;

import com.yikuyi.party.model.Party;
import com.yikuyi.party.vo.RoleTypeVo;
import com.yikuyi.party.vo.RoleVo;
import com.ykyframework.exception.BusinessException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 用户权限信息接口
 * @author zr.helinmei@yikuyi.com
 */
public interface IACLResource {
	
	/**
	 * 查询用户已有的角色信息
	 * @param partyId
	 * @return
	 * @since 2017年8月23日
	 */
	@ApiOperation(value = "查询用户已有的角色信息", notes = "查询用户已有的角色信息")
	public Set<String> getUserRoleList(@ApiParam(value = "用户ID", required = true) String partyId);
	
	/**
	 * 查询用户已有的权限信息
	 * @param partyId
	 * @return
	 * @since 2017年8月23日
	 */
	@ApiOperation(value = "查询用户已有的权限信息", notes = "查询用户已有的权限信息")
	public Set<String> getUserPermissions(@ApiParam(value = "用户ID", required = true) String partyId);
	
	/**
	 * 根据角色ID查询用户
	 * @param role
	 * @return
	 * @since 2017年8月23日
	 */
	@ApiOperation(value = "根据角色ID查询用户", notes = "根据角色ID查询用户(目前只支持用户名称返回,后续需要可以直接增加sql的字段)")
	public List<Party> getUserByRole(@ApiParam(value = "用户角色", required = true) String role);
	
	/**
	 * 查询配置支持配置菜单的所有角色
	 * @param roleType
	 * @return
	 * @since 2017年8月23日
	 */
	@ApiOperation(value = "查询配置支持配置菜单的所有角色", notes = "查询配置支持配置菜单的所有角色")
	public List<RoleTypeVo> getMenuRole(@ApiParam(value = "查询菜单角色", required = true) String roleType);
	
	/**
	 * 添加角色菜单关系
	 * @param roleVo
	 * @return
	 * @since 2017年8月23日
	 */
	@ApiOperation(value = "添加角色菜单关系", notes = "添加角色菜单关系")
	public void addRoelMenuRelaction(@ApiParam(value = "角色菜单VO", required = true)RoleVo roleVo) throws BusinessException;
}