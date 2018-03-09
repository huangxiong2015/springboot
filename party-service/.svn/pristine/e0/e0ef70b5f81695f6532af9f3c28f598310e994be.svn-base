package com.yikuyi.party.customer.api.impl;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

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
import com.framework.springboot.model.LoginUser;
import com.framewrok.springboot.web.LoginUserInjectionInterceptor;
import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.party.customer.bll.CustomerSummeryManager;
@RunWith(SpringJUnit4ClassRunner.class)
public class FindPasswordResourceTest {


	@InjectMocks
	private FindPasswordResource findPasswordResource;
	
	private MockMvc mockMvc;
	
	@Mock
	private CustomerSummeryManager customerSummeryManager;

	public FindPasswordResourceTest() {
		MockitoAnnotations.initMocks(this);
	}

	@Before
	public void setUpBefore() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(findPasswordResource).build();
	}
	
	LoginUser loginUser = new LoginUser("9999999901", "admin", "9999999901", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));

	@Test
	public void reSetPasswordTest()throws Exception{
		// 构造mock返回结构数据
		Mockito.doNothing().when(customerSummeryManager).findPassword(Mockito.anyString(), Mockito.any(UserVo.class));
		UserVo user = new UserVo();
		user.setNewPassword("123456");
		String str = JSONObject.toJSONString(user);
        mockMvc.perform(put("/v1/findpassword/bWFsYW4=/password")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(str)
						.requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

	
	}
	
	@Test
	public void sendMailTest()throws Exception{
		// 构造mock返回结构数据

		Mockito.doNothing().when(customerSummeryManager).sendMail(Mockito.anyString());
		mockMvc.perform(put("/v1/findpassword/MTAwMDBAcXEuY29t/mail")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content("11")
						.requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser))
				.andExpect(status().isOk());
	}

}
