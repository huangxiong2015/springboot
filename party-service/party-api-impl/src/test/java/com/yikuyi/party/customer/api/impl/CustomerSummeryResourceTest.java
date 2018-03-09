package com.yikuyi.party.customer.api.impl;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

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

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.springboot.config.ObjectMapperHelper;
import com.framework.springboot.model.LoginUser;
import com.framewrok.springboot.web.LoginUserInjectionInterceptor;
import com.yikuyi.party.contact.vo.UserExtendVo;
import com.yikuyi.party.contact.vo.UserParamVo;
import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.party.customer.bll.CustomerSummeryManager;

@RunWith(SpringJUnit4ClassRunner.class)
public class CustomerSummeryResourceTest {

	private ObjectMapper mapper = ObjectMapperHelper.configeObjectMapper(new ObjectMapper());

	@InjectMocks
	private CustomerSummeryResource customerSummeryResource;

	private MockMvc mockMvc;

	@Mock
	private CustomerSummeryManager customerSummeryManager;

	public CustomerSummeryResourceTest() {
		MockitoAnnotations.initMocks(this);
	}

	@Before
	public void setUpBefore() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(customerSummeryResource).build();
	}

	LoginUser loginUser = new LoginUser("9999999901", "admin", "9999999901",
			Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));

	@Test
	public void accountSummaryTest() throws Exception {
		// 构造mock返回结构数据
		UserExtendVo mockVo = new UserExtendVo();
		mockVo.setName("我来了");
		mockVo.setId("9999999901");
		when(customerSummeryManager.getUserSummeryInfo(Mockito.anyString(), Mockito.anyString())).thenReturn(mockVo);
		UserExtendVo user = mapper.readValue(mockMvc.perform(get("/v1/customersummery/summary")
				.requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), UserExtendVo.class);
		Assert.assertEquals(user.getName(), "我来了");

	}

	@Test
	public void changePasswordTest() throws Exception {
		// 构造mock返回结构数据
		Mockito.doNothing().when(customerSummeryManager).changePassword(Mockito.anyString(), Mockito.any(UserVo.class));
		UserVo user = new UserVo();
		user.setId("11111111");
		String str = JSONObject.toJSONString(user);
		mockMvc.perform(put("/v1/customersummery/password").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(str).requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

	}

	@Test
	public void checkedOldPwdTest() throws Exception {
		// 构造mock返回结构数据
		Mockito.doNothing().when(customerSummeryManager).checkedOldPassword(Mockito.anyString(), Mockito.anyString());
		UserVo user = new UserVo();
		user.setId("11111111");
		String str = JSONObject.toJSONString(user);
        mockMvc.perform(put("/v1/customersummery/password/checked?password='123456'").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(str)
						.requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

	}

	@Test
	public void getUserLoginInfoTest() throws Exception {
		// 构造mock返回结构数据
		UserExtendVo mockVo = new UserExtendVo();
		mockVo.setName("我来了");
		mockVo.setId("9999999901");
		when(customerSummeryManager.getUserLoginInfo(Mockito.any(LoginUser.class))).thenReturn(mockVo);
		UserExtendVo user = mapper.readValue(mockMvc
				.perform(
						get("/v1/customersummery").requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), UserExtendVo.class);
		Assert.assertEquals(user.getName(), "我来了");
	}
	
	@Test
	public void getAccountByPartyIdTest() throws Exception {
		// 构造mock返回结构数据
		UserParamVo mockVo = new UserParamVo();
		mockVo.setGroupName("test");
		when(customerSummeryManager.getAccountByPartyId(Mockito.anyString())).thenReturn(mockVo);
		UserParamVo user = mapper.readValue(mockMvc
				.perform(get("/v1/customersummery/9999999901/info").requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), UserParamVo.class);
		Assert.assertEquals(user.getGroupName(), "test");
	}

}
