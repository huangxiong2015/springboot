package com.yikuyi.party.customer.api.impl;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.springboot.config.ObjectMapperHelper;
import com.framework.springboot.model.LoginUser;
import com.framewrok.springboot.web.LoginUserInjectionInterceptor;
import com.github.pagehelper.PageInfo;
import com.yikuyi.party.contact.vo.EnterpriseParamVo;
import com.yikuyi.party.contact.vo.UserExtendVo;
import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.party.customer.bll.CustomerManager;
import com.yikuyi.party.login.model.UserLogin;
import com.yikuyi.party.model.Party.PartyStatus;
import com.yikuyi.party.person.model.Person;

@RunWith(SpringJUnit4ClassRunner.class)
public class CustomerResourceTest {
	private ObjectMapper mapper = ObjectMapperHelper.configeObjectMapper(new ObjectMapper());

	@InjectMocks
	private CustomerResource customerResource;

	private MockMvc mockMvc;

	@Mock
	private CustomerManager customerManager;

	private MockHttpServletResponse response;

	public CustomerResourceTest() {
		MockitoAnnotations.initMocks(this);
	}

	@Before
	public void setUpBefore() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(customerResource).build();
	}

	LoginUser loginUser = new LoginUser("9999999901", "admin", "9999999901",
			Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));

	/**
	 * 获取用户详情
	 * 
	 * @throws Exception
	 * @since 2017年8月24日
	 */
	@Test
	public void getBaseInfoDetailTest() throws Exception {
		// 构造mock返回结构数据
		UserExtendVo mockVo = new UserExtendVo();
		mockVo.setName("我来了");
		mockVo.setId("9999999901");
		when(customerManager.getBaseInfoDetail(Mockito.anyString(), Mockito.anyString())).thenReturn(mockVo);
		UserExtendVo user = mapper.readValue(mockMvc
				.perform(get("/v1/customers/9999999901?id=709035037445586944&loginId=malan")
						.requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), UserExtendVo.class);
		Assert.assertEquals(user.getName(), "我来了");
	}

	/**
	 * 根据用户名称查询用户列表（模糊查询）
	 * 
	 * @throws Exception
	 * @since 2017年8月24日
	 */
	@Test
	public void getUserByNameTest() throws Exception {
		// 构造mock返回结构数据
		List<UserVo> mockList = new ArrayList<>();
		UserVo userVo = new UserVo();
		userVo.setId("111111111");
		userVo.setName("malan");
		mockList.add(userVo);
		when(customerManager.getUserByName("malan")).thenReturn(mockList);
		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, UserVo.class);
		List<UserVo> mockListRst = mapper.readValue(mockMvc.perform(get("/v1/customers/username?username=malan"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), javaType);
		Assert.assertEquals(mockListRst.get(0).getId(), "111111111");

		UserVo user = new UserVo();
		user.setName("admin");
		user.setId("1111");
		when(customerManager.getUsersByName("admin")).thenReturn(user);
		UserVo userResult = mapper.readValue(mockMvc.perform(get("/v1/customers/name?name=admin"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), UserVo.class);
		Assert.assertEquals(userResult.getId(), "1111");

	}

	/**
	 * 根据状态查询用户
	 * 
	 * @throws Exception
	 * @since 2017年8月24日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void getPersonsTest() throws Exception {
		// 构造mock返回结构数据
		List<UserVo> mockList = new ArrayList<>();
		UserVo userVo = new UserVo();
		userVo.setId("111111111");
		userVo.setName("malan");
		mockList.add(userVo);
		when(customerManager.getPersons(Mockito.any(Person.RelationSratus.class), Mockito.anyString()))
				.thenReturn(mockList);
		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, UserVo.class);
		List<UserVo> mockListRst = mapper.readValue(mockMvc.perform(get("/v1/customers/status?relationSratus=RELATED"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), javaType);
		Assert.assertEquals(mockListRst.get(0).getId(), "111111111");

	}

	/**
	 * 保存用户信息
	 * 
	 * @throws Exception
	 * @since 2017年8月24日
	 */
	@Test
	public void saveTest() throws Exception {
		UserExtendVo user = new UserExtendVo();
		user.setAddress("中国深圳市南山区高新技术产业园麻雀岭工业区M-8栋4楼");
		user.setPartyId("709035037445586944");
		user.setCity("10001");
		user.setCityName("深圳市");
		user.setCountry("10002");
		user.setCountryName("南山区");
		user.setAddressLevel("1");
		user.setId("1");
		user.setLogoUrl("http://www.baidu.com");
		user.setLoginCount(1);
		user.setMail("12345@qq.com");
		user.setMobile("13066939619");
		user.setName("叶良辰");
		user.setPostcode("11111");
		user.setProvince("10000");
		user.setProvinceName("广东省");
		user.setSex("1");
		user.setStatus(PartyStatus.PARTY_ENABLED);
		String str = mapper.writeValueAsString(user);
		// 构造mock返回结构数据
		mockMvc.perform(post("/v1/customers").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(str)
				.requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser)).andExpect(status().isOk());

		Mockito.verify(customerManager).save(Mockito.any(UserExtendVo.class));

	}

	/**
	 * 根据用户Id更新用户状态信息
	 * 
	 * @throws Exception
	 * @since 2017年8月24日
	 */
	@Test
	public void updateByIdTest() throws Exception {
		mockMvc.perform(put("/v1/customers/709035037445586944?relationSratus=NOT_RELATED")
				.requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser)).andExpect(status().isOk());
		Mockito.verify(customerManager).updateById(Mockito.anyString(), Mockito.any(Person.RelationSratus.class),
				Mockito.anyString());
	}

	/**
	 * 更新用户信息
	 * 
	 * @throws Exception
	 * @since 2017年8月24日
	 */
	@Test
	public void updateTest() throws Exception {
		UserExtendVo userInfoVo = new UserExtendVo();
		userInfoVo.setId("9999999901");
		userInfoVo.setLogoUrl("www.baidu.com");
		String str = JSONObject.toJSONString(userInfoVo);
		mockMvc.perform(put("/v1/customers").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(str)
				.requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser)).andExpect(status().isOk());
		Mockito.verify(customerManager).update(Mockito.any(UserExtendVo.class));
	}

	/**
	 * 根据用户id获取person信息
	 * 
	 * @throws Exception
	 * @since 2017年8月24日
	 */
	@Test
	public void findPersonInfoByPartyIdTest() throws Exception {
		Person person = new Person();
		person.setMail("1234@qq.com");
		when(customerManager.findPersonInfoByPartyId(Mockito.anyString())).thenReturn(person);
		Person personResult = mapper.readValue(mockMvc.perform(get("/v1/customers/709035037445586944/username"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), Person.class);
		Assert.assertEquals(personResult.getMail(), "1234@qq.com");
	}

	/**
	 * 根据用户ID获取登录密码，登录ID
	 * 
	 * @throws Exception
	 * @since 2017年8月24日
	 */
	@Test
	public void findUserLoginTest() throws Exception {
		UserLogin userLogin = new UserLogin();
		userLogin.setId("9999999901");
		when(customerManager.getUserLoginByPartyid(Mockito.anyString())).thenReturn(userLogin);
		UserLogin userResult = mapper.readValue(mockMvc
				.perform(get("/v1/customers/{id}/login", "709035037445586944")
						.requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), UserLogin.class);
		Assert.assertEquals(userResult.getId(), "9999999901");

	}

	/**
	 * [后端]用户管理-用户启用/停用
	 * 
	 * @throws Exception
	 * @since 2017年8月24日
	 */
	@Test
	public void updateStateIdTest() throws Exception {
		// 构造mock返回结构数据
		mockMvc.perform(post("/v1/customers/updateStateId?partyId=1&statusId=PARTY_ENABLED")
				.requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser)).andExpect(status().isOk());
		Mockito.verify(customerManager).updateStateId(Mockito.anyString(), Mockito.anyString());
	}

	/**
	 * 导出个人会员列表
	 * 
	 * @throws Exception
	 * @since 2017年8月24日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void exportUserTest() throws Exception {
		EnterpriseParamVo param = new EnterpriseParamVo();
		Mockito.doNothing().when(customerManager).exportUser(param, response);
		mockMvc.perform(get("/v1/customers/excel").requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser))
				.andExpect(status().isOk()).andReturn().getResponse();
	}

	/**
	 * 根据已验证邮箱查询用户信息
	 * 
	 * @throws Exception
	 * @since 2017年8月24日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void getUsersByMailTest() throws Exception {
		UserVo userVo = new UserVo();
		userVo.setId("9999999901");
		userVo.setMail("1454690@qq.com");
		when(customerManager.getUsersByMail(Mockito.anyString())).thenReturn(userVo);
		UserVo vo = mapper.readValue(mockMvc.perform(get("/v1/customers/getUsersByMail?mail=1454690@qq.com"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), UserVo.class);
		Assert.assertEquals(vo.getId(), "9999999901");
	}

	/**
	 * 批量添加子账号
	 * 
	 * @throws Exception
	 * @since 2017年8月24日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void addSubAccountTest() throws Exception {
		List<String> list = new ArrayList<String>();
		list.add("15354689558");
		list.add("13685429886");
		when(customerManager.addSubAccount("9999999901", list)).thenReturn("success");

		mockMvc.perform(post("/v1/customers/batch").requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser)
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(list)))
				.andExpect(status().isOk());

	}

	/**
	 * 批量添加子账号
	 * 
	 * @throws Exception
	 * @since 2017年11月13日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	public void creatAccountTest() throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("1", "1");
		UserVo userVo = new UserVo();
		when(customerManager.creatAccount(userVo)).thenReturn(map);

		JavaType javaType = mapper.getTypeFactory().constructParametricType(Map.class, String.class, String.class);

		map = mapper.readValue(mockMvc
				.perform(post("/v1/customers/account").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(mapper.writeValueAsString(userVo)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), javaType);

	}

	/**
	 * 批量添加子账号
	 * 
	 * @throws Exception
	 * @since 2017年11月13日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	public void cancelAccountTest() throws Exception {
		String str = "1";
		when(customerManager.cancelAccount("9999999901", "9999999901")).thenReturn(str);

		str = mapper.readValue(mockMvc
				.perform(post("/v1/customers/cancel?accountId=9999999901")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(str))
						.requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), String.class);
		Assert.assertEquals(str, "1");
	}

	/**
	 * 批量添加子账号
	 * 
	 * @throws Exception
	 * @since 2017年11月13日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	public void frozenAccountTest() throws Exception {
		String str = "1";
		when(customerManager.frozenAccount(PartyStatus.PARTY_DISABLED, "9999999901", "9999999901")).thenReturn(str);

		str = mapper.readValue(mockMvc
				.perform(post("/v1/customers/frozen?accountId=9999999901&partyStatus=PARTY_DISABLED")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(str))
						.requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), String.class);
		Assert.assertEquals(str, "1");
	}

	/**
	 * 批量添加子账号
	 * 
	 * @throws Exception
	 * @since 2017年11月13日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	public void getUserInfoListTest() throws Exception {
		PageInfo<UserExtendVo> pageInfo = new PageInfo<>();
		List<UserExtendVo> userList = new ArrayList<>();
		UserExtendVo userExtendVo = new UserExtendVo();
		userExtendVo.setName("test");
		userList.add(userExtendVo);
		pageInfo.setList(userList);
		EnterpriseParamVo param = new EnterpriseParamVo();
		param.setName("test");
		when(customerManager.findCustomerUser(Mockito.any(EnterpriseParamVo.class), Mockito.any(RowBounds.class)))
				.thenReturn(pageInfo);
		JavaType javaType = mapper.getTypeFactory().constructParametricType(PageInfo.class, UserExtendVo.class);

		PageInfo<UserExtendVo> resultPage = mapper.readValue(mockMvc.perform(get("/v1/customers"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), javaType);
		Assert.assertEquals(resultPage.getList().get(0).getName(), "test");
	}

	/**
	 * 批量添加子账号
	 * 
	 * @throws Exception
	 * @since 2017年11月13日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	public void sendMailTest() throws Exception {
		UserVo userVo = new UserVo();
		String str = "1";
		when(customerManager.sendMail(Mockito.any(UserVo.class))).thenReturn(str);
		str = mapper.readValue(mockMvc
				.perform(post("/v1/customers/sendMail").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(mapper.writeValueAsString(userVo))
						.requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), String.class);
		Assert.assertEquals(str, "1");
	}
}