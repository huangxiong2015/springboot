package com.yikuyi.party.acl.api.impl;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.springboot.config.ObjectMapperHelper;
import com.yikuyi.party.acl.bll.ACLManager;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.vo.RoleTypeVo;
import com.yikuyi.party.vo.RoleVo;

@RunWith(SpringJUnit4ClassRunner.class)
public class ACLResourceTest {

	private ObjectMapper mapper = ObjectMapperHelper.configeObjectMapper(new ObjectMapper());

	@InjectMocks
	private ACLResource aclResource;

	@Mock
	private ACLManager mockAclManager;

	private MockMvc mockMvc;

	public ACLResourceTest() {
		MockitoAnnotations.initMocks(this);
	}

	@Before
	public void setUpBefore() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(aclResource).build();
	}

	/**
	 * 查询用户已有的角色信息
	 * @throws Exception
	 * @since 2017年8月24日
	 */
	@Test
	public void testGetUserRoleList() throws Exception {
		// 构造mock返回结构数据
		Set<String> mockSet = new HashSet<>();
		mockSet.add("ADMIN");
		mockSet.add("ENTERPRISE_CUST");
		when(mockAclManager.getUserRoleList("9999999901")).thenReturn(mockSet);

		mockMvc.perform(get("/v1/acl/roles?partyId=9999999901")).andExpect(status().isOk()).andExpect(content().string(mapper.writeValueAsString(mockSet)));
	}

	/**
	 * 查询用户已有的权限信息
	 * @throws Exception
	 * @since 2017年8月24日
	 */
	@Test
	public void testGetUserPermissionList() throws Exception {
		// 构造mock返回结构数据
		Set<String> mockSet = new HashSet<>();
		mockSet.add("MENU:VIEW:123");
		mockSet.add("MENU:VIEW:243");
		when(mockAclManager.getUserPermissionList("9999999901")).thenReturn(mockSet);

		mockMvc.perform(get("/v1/acl/permissions?partyId=9999999901")).andExpect(status().isOk()).andExpect(content().string(mapper.writeValueAsString(mockSet)));
	}

	/**
	 * 根据角色ID查询用户
	 * @throws Exception
	 * @since 2017年8月24日
	 */
	@Test
	public void getGetUserByRole() throws Exception {
		// 构造mock返回结构数据
		List<Party> mockList = new ArrayList<>();
		Party party = new Party();
		party.setPartyCode("YKY100");
		mockList.add(party);
		when(mockAclManager.getUserByRole("ADMIN")).thenReturn(mockList);

		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, Party.class);
		
		List<Party> mockListRst = mapper.readValue(mockMvc.perform(get("/v1/acl/person?role=ADMIN")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString(),javaType);
		Assert.assertEquals(mockListRst.get(0).getPartyCode(),"YKY100");
	}
	
	/**
	 * 查询配置支持配置菜单的所有角色
	 * @throws Exception
	 * @since 2017年8月24日
	 */
	@Test
	public void testGetMenuRole() throws Exception {
		//构造mock返回结构数据
	    List<RoleTypeVo> mockList = new ArrayList<>();
	    RoleTypeVo roleType = new RoleTypeVo();
	    roleType.setRoleTypeId("OPERATION_SPECIALIST");
	    roleType.setParentTypeId("OPERATION_FUNCTION");
	    roleType.setDescription("运营维护员");
		mockList.add(roleType);
	    when(mockAclManager.getMenuRole("OPERATION_FUNCTION")).thenReturn(mockList);
	    

	    JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, RoleTypeVo.class);
	    
	    List<RoleTypeVo> mockListRst = mapper.readValue(mockMvc.perform(get("/v1/acl/getMenuRole/OPERATION_FUNCTION")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), javaType);
		Assert.assertEquals(mockListRst.get(0).getRoleTypeId(),"OPERATION_SPECIALIST");
	}

	/**
	 * 添加角色菜单关系
	 * @throws Exception
	 * @since 2017年8月24日
	 */
	@Test
	public void testAddRoelMenuRelaction() throws Exception {
		//构造mock返回结构数据
		RoleVo roleVo = new RoleVo();
		Mockito.doNothing().when(mockAclManager).addRoelMenuRelaction(roleVo);
	    
	    mockMvc.perform(post("/v1/acl/menurole").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(roleVo))).andExpect(status().isOk());
	}

}