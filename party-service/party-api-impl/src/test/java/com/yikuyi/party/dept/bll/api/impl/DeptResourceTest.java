package com.yikuyi.party.dept.bll.api.impl;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.springboot.config.ObjectMapperHelper;
import com.framework.springboot.model.LoginUser;
import com.framewrok.springboot.web.LoginUserInjectionInterceptor;
import com.github.pagehelper.PageInfo;
import com.yikuyi.party.contact.vo.UserManage;
import com.yikuyi.party.dept.api.impl.DeptResource;
import com.yikuyi.party.dept.bll.DeptManager;
import com.yikuyi.party.person.model.Person;
import com.yikuyi.party.vo.DeptVo;
import com.yikuyi.party.vo.RoleVo;

@RunWith(SpringJUnit4ClassRunner.class)
public class DeptResourceTest {
	private ObjectMapper mapper = ObjectMapperHelper.configeObjectMapper(new ObjectMapper());

	@InjectMocks
	private DeptResource deptResource;

	private MockMvc mockMvc;

	@Mock
	private DeptManager deptManager;

	public DeptResourceTest() {
		MockitoAnnotations.initMocks(this);
	}

	@Before
	public void setUpBefore() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(deptResource).build();
	}

	LoginUser loginUser = new LoginUser("9999999901", "admin", "9999999901",
			Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));

	@Test
	public void findRoleVoByDeptIdTest() throws Exception {
		// 构造mock返回结构数据
		List<RoleVo> mockList = new ArrayList<>();
		RoleVo roleVo = new RoleVo();
		roleVo.setAccount("秘密");
		mockList.add(roleVo);
		when(deptManager.findRoleVoByDeptId(Mockito.anyString())).thenReturn(mockList);
		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, RoleVo.class);
		List<RoleVo> mockListRst = mapper.readValue(mockMvc.perform(get("/v1/dept/findRoleVoByDeptId?deptId=1"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), javaType);
		Assert.assertEquals(mockListRst.get(0).getAccount(), "秘密");

	}

	@Test
	public void findCustomerByDeptIdTest() throws Exception {
		UserManage vo = new UserManage();
		vo.setDeptName("研发部");
		PageInfo<UserManage> page = new PageInfo<UserManage>();
		List<UserManage> listUser = new ArrayList<>();
		listUser.add(vo);
		page.setList(listUser);
		when(deptManager.findCustomerByDeptId(Mockito.anyString(), Mockito.any(RowBounds.class))).thenReturn(page);

		JavaType javaType = mapper.getTypeFactory().constructParametricType(PageInfo.class, UserManage.class);
		PageInfo<UserManage> resultPage = mapper
				.readValue(mockMvc.perform(get("/v1/dept/findCustomerByDeptId?deptId=1&page=1&size=10"))
						.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), javaType);
		Assert.assertEquals(resultPage.getList().get(0).getDeptName(), "研发部");
	}

	@Test
	public void deptSaveTest() throws Exception {
		DeptVo vo = new DeptVo();
		vo.setName("认证部");
		vo.setParentId("99999999");
		// 构造mock返回结构数据
		mockMvc.perform(post("/v1/dept").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(mapper.writeValueAsString(vo))
				.requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser)).andExpect(status().isOk());
		Mockito.verify(deptManager).deptSave(Mockito.any(DeptVo.class));

	}

	@Test
	public void deptUpdateTest() throws Exception {
		DeptVo vo = new DeptVo();
		vo.setName("认证部");
		vo.setId("10001");
		vo.setParentId("99999999");
		mockMvc.perform(put("/v1/dept").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(mapper.writeValueAsString(vo))
				.requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser)).andExpect(status().isOk());
		Mockito.verify(deptManager).deptUpdate(Mockito.any(DeptVo.class));

	}

	@Test
	public void deptListTest() throws Exception {
		// 构造mock返回结构数据
		List<DeptVo> mockList = new ArrayList<>();
		DeptVo deptVo = new DeptVo();
		deptVo.setId("111111111");
		deptVo.setName("malan");
		mockList.add(deptVo);
		when(deptManager.deptList(Mockito.anyString())).thenReturn(mockList);
		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, DeptVo.class);
		List<DeptVo> mockListRst = mapper.readValue(mockMvc.perform(get("/v1/dept/list")).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString(), javaType);
		Assert.assertEquals(mockListRst.get(0).getId(), "111111111");

	}

	@Test
	public void findSonDeptListTest() throws Exception {
		// 构造mock返回结构数据
		List<DeptVo> mockList = new ArrayList<>();
		DeptVo deptVo = new DeptVo();
		deptVo.setId("111111111");
		deptVo.setName("malan");
		mockList.add(deptVo);
		when(deptManager.findSonDeptList(Mockito.anyString())).thenReturn(mockList);
		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, DeptVo.class);
		List<DeptVo> mockListRst = mapper.readValue(mockMvc.perform(get("/v1/dept/sonlist?parentId=1001"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), javaType);
		Assert.assertEquals(mockListRst.get(0).getId(), "111111111");

	}

	@Test
	public void deptDetailTest() throws Exception {
		// 构造mock返回结构数据
		DeptVo vo = new DeptVo();
		vo.setName("认证部");
		vo.setParentId("99999999");
		when(deptManager.deptDetail(Mockito.anyString())).thenReturn(vo);
		DeptVo user = mapper.readValue(mockMvc
				.perform(get("/v1/dept/detail?partyId=863203580700524544")
						.requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), DeptVo.class);
		Assert.assertEquals(user.getName(), "认证部");

	}

	@Test
	public void roleSaveTest() throws Exception {
		RoleVo vo = new RoleVo();
		vo.setName("CMO");
		List<DeptVo> deptList = new ArrayList<>();
		DeptVo vo1 = new DeptVo();
		vo1.setId("9999999901");
		DeptVo vo2 = new DeptVo();
		vo2.setId("9999999902");
		deptList.add(vo1);
		deptList.add(vo2);
		vo.setDeptVoList(deptList);
		String str = mapper.writeValueAsString(vo);
		// 构造mock返回结构数据
		mockMvc.perform(post("/v1/dept/role").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(str)
				.requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser)).andExpect(status().isOk());
		Mockito.verify(deptManager).roleSave(Mockito.any(RoleVo.class));

	}

	@Test
	public void roleUpdateTest() throws Exception {
		RoleVo vo = new RoleVo();
		vo.setName("CMO");
		vo.setId("1001");
		List<DeptVo> deptList = new ArrayList<>();
		DeptVo vo1 = new DeptVo();
		vo1.setId("9999999901");
		DeptVo vo2 = new DeptVo();
		vo2.setId("9999999902");
		deptList.add(vo1);
		deptList.add(vo2);
		vo.setDeptVoList(deptList);
		String str = mapper.writeValueAsString(vo);
		mockMvc.perform(put("/v1/dept/role").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(str)
				.requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser)).andExpect(status().isOk());
		Mockito.verify(deptManager).roleUpdate(Mockito.any(RoleVo.class));

	}

	@Test
	public void roleListTest() throws Exception {
		RoleVo vo = new RoleVo();
		vo.setDeptName("研发部");
		PageInfo<RoleVo> page = new PageInfo<RoleVo>();
		List<RoleVo> listUser = new ArrayList<>();
		listUser.add(vo);
		page.setList(listUser);
		when(deptManager.roleList(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(),
				Mockito.anyInt())).thenReturn(page);

		JavaType javaType = mapper.getTypeFactory().constructParametricType(PageInfo.class, RoleVo.class);
		PageInfo<RoleVo> resultPage = mapper.readValue(mockMvc.perform(get("/v1/dept/role/list"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), javaType);
		Assert.assertEquals(resultPage.getList().get(0).getDeptName(), "研发部");

	}

	@Test
	public void roleDetailTest() throws Exception {
		// 构造mock返回结构数据
		RoleVo mockVo = new RoleVo();
		mockVo.setName("我来了");
		mockVo.setId("9999999901");
		when(deptManager.roleDetail(Mockito.anyString())).thenReturn(mockVo);
		RoleVo role = mapper.readValue(mockMvc
				.perform(get("/v1/dept/role/detail?id=9999999914")
						.requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), RoleVo.class);
		Assert.assertEquals(role.getName(), "我来了");

	}

	@Test
	public void findPersonByRoleNameTest() throws Exception {
		// 构造mock返回结构数据
		List<Person> mockList = new ArrayList<>();
		Person person = new Person();
		person.setComments("单元测试");
		mockList.add(person);
		when(deptManager.findPersonByRoleName("单元测试")).thenReturn(mockList);
		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, Person.class);
		List<Person> mockListRst = mapper.readValue(mockMvc.perform(get("/v1/dept/findPersonByRoleName?roleName=单元测试"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), javaType);
		Assert.assertEquals(mockListRst.get(0).getComments(), "单元测试");

	}
}
