/*
 * Created: 2017年4月1日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.profiles.api.impl;

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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.springboot.config.ObjectMapperHelper;
import com.framework.springboot.model.LoginUser;
import com.framewrok.springboot.web.LoginUserInjectionInterceptor;
import com.yikuyi.party.model.PartyProfileDefault;
import com.yikuyi.party.profiles.bll.PartyProfileDefaultManager;

/**
 * 
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class ProfilesResourceTest {

	private ObjectMapper mapper = ObjectMapperHelper.configeObjectMapper(new ObjectMapper());

	@InjectMocks
	private ProfilesResource profilesResource;

	@Mock
	private PartyProfileDefaultManager mockPartyProfileDefaultManager;

	LoginUser loginUser = new LoginUser("9999999901", "admin", "9999999901", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));

	private MockMvc mockMvc;

	public ProfilesResourceTest() {
		MockitoAnnotations.initMocks(this);
	}

	@Before
	public void setUpBefore() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(profilesResource).build();
	}

	@Test
	public void testDefaultAdress() throws Exception {
		// 构造mock返回数据
		PartyProfileDefault mockEntity = new PartyProfileDefault();
		mockEntity.setPartyId("9999999901");

		when(mockPartyProfileDefaultManager.insertProfileDefault(Mockito.any(PartyProfileDefault.class))).thenReturn(mockEntity);

		mockEntity = mapper.readValue(mockMvc.perform(put("/v1/profiles/default?id=111&defaultAddress=DEFAULT_SHIP_ADDR_CNY").requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser)).andExpect(status().isOk()).andReturn()
				.getResponse().getContentAsString(), PartyProfileDefault.class);
		Assert.assertEquals(mockEntity.getPartyId(), "9999999901");
		
		
		// 构造mock返回数据
		PartyProfileDefault mockEntity1 = new PartyProfileDefault();
		mockEntity1.setPartyId("9999999901");

		when(mockPartyProfileDefaultManager.insertProfileDefault(Mockito.any(PartyProfileDefault.class))).thenReturn(mockEntity1);

		mockEntity1 = mapper.readValue(mockMvc.perform(put("/v1/profiles/default?id=111&defaultAddress=DEFAULT_SHIP_ADDR_HK").requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser)).andExpect(status().isOk()).andReturn()
				.getResponse().getContentAsString(), PartyProfileDefault.class);
		Assert.assertEquals(mockEntity1.getPartyId(), "9999999901");
	}

	@Test
	public void testGetDefaultAdress() throws Exception {
		PartyProfileDefault mockEntity = new PartyProfileDefault();
		mockEntity.setPartyId("9999999901");
		when(mockPartyProfileDefaultManager.getProfileDefault("9999999901")).thenReturn(mockEntity);

		mockEntity = mapper.readValue(mockMvc.perform(get("/v1/profiles").requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString(),
				PartyProfileDefault.class);
		Assert.assertEquals(mockEntity.getPartyId(), "9999999901");
	}
}