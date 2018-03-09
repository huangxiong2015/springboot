/*
 * Created: 2016年12月9日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
/**
 * 
 */
package com.yikuyi.party.partycode.api;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.yikuyi.party.partycode.api.impl.PartyCodeResource;
import com.yikuyi.party.partycode.bll.PartyCodeManager;

/**
 * @author zr.shuzuo@yikuyi.com
 * @version 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class PartyCodeResourceTest {


	@InjectMocks
	private PartyCodeResource partyCodeResource;
	
	@Mock
	private PartyCodeManager partyCodeManager;
	
	private MockMvc mockMvc;
	public PartyCodeResourceTest(){
		
	}


	/**
	 * @throws java.lang.Exception
	 * @since 2016年12月9日
	 * @author liudian@yikuyi.com
	 */
	@Before
	public void setUpBefore() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(partyCodeResource).build();

	}

	@Test
	public void testSavePartyCode() throws Exception {
	
		when(partyCodeManager.savePatyCode("1008618", "hahahhah")).thenReturn("hahahhah");
		mockMvc.perform(post("/v1/partycode?partyId=1008618&partyCode=hahahhah").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(status().isOk());

	}
	
	/**
	 * 验证YKY客户编码(partyCode)的唯一性
	 * @throws Exception
	 * @since 2017年7月20日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void testGetPartyCode() throws Exception{
		when(partyCodeManager.getPartyCode("www123")).thenReturn("www123");
		mockMvc.perform(get("/v1/partycode/www123")).andExpect(status().isOk()).andExpect(content().string("www123"));
	}
}