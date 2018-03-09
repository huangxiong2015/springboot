package com.yikuyi.party.acl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.yikuyi.party.config.BaseTest;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.role.model.RoleTypeEnum;
import com.yikuyi.party.vo.RoleVo;

public class ACLResourceTest extends BaseTest {

	/**
	 * 根据用户ID,查询对应所有角色
	 * @since 2017年9月1日
	 * jik.shu@yikuyi.com
	 */
	@Test
	public void findAll() {
		Set<String> list = super.partyClient.aclClient().getUserRoles("9999999901","YWRtaW46OTk5OTk5OTkwMQ==");
		Assert.assertTrue(list.size() > 0);
	}
	
	/**
	 * 根据角色获取用户
	 * @param role
	 * @param authToken
	 * @return
	 * @since 2017年6月29日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	public void getUserByRole() {
		//List<Party> list = super.partyClient.aclClient().getUserByRole("864327651584638976","YWRtaW46OTk5OTk5OTkwMQ==");
		List<Party> list = super.partyClient.aclClient().getUserByRole(RoleTypeEnum.BUSINESS_ASSISTANT.toString(),"YWRtaW46OTk5OTk5OTkwMQ==");
		Assert.assertTrue(list.size() > 0);
	}
	
	/**
	 * 新增角色和菜单的权限关系
	 * @param vo
	 * @return
	 * @since 2017年6月16日
	 * @author jik.shu@yikuyi.com
	 */
	@Test
	public void addRoelMenuRelaction() {
		RoleVo vo = new RoleVo();
		vo.setMenuType("CUSTOMER");
		vo.setMenuPower("MENU:VIEW:25896");
		vo.setMenuId("11001");
		List<String> list = new ArrayList<String>();
		list.add("111");
		vo.setRoleIdList(list);
		super.partyClient.aclClient().addRoelMenuRelaction(vo,"YWRtaW46OTk5OTk5OTkwMQ==");
	}
	
	/**
	 * 根据partyId获取用户的权限信息
	 * @since 2017年9月1日
	 * @author jik.shu@yikuyi.com
	 */
	@Test
	public void getUserPermissions() {
		Set<String> list = super.partyClient.aclClient().getUserPermissions("9999999901", "YWRtaW46OTk5OTk5OTkwMQ==");
		Assert.assertTrue(list.size() > 0);
	}
}