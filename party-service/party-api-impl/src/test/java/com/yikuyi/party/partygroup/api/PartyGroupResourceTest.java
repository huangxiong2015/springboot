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
package com.yikuyi.party.partygroup.api;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.springboot.config.ObjectMapperHelper;
import com.yikuyi.party.contact.vo.MsgResultVo;
import com.yikuyi.party.group.vo.PartyGroupVo;
import com.yikuyi.party.partygroup.api.impl.PartyGroupResource;
import com.yikuyi.party.partygroup.bll.PartyGroupManager;
import com.yikuyi.party.vo.PartyVo;

/**
 * 
 * @author zr.shuzuo@yikuyi.com
 * @version 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class PartyGroupResourceTest {

	private ObjectMapper mapper = ObjectMapperHelper.configeObjectMapper(new ObjectMapper());
	@InjectMocks
	private PartyGroupResource partyGroupResource;
	@Mock
	private PartyGroupManager partyGroupManager;

	private MockMvc mockMvc;

	public PartyGroupResourceTest() {

	}


	/**
	 * @throws java.lang.Exception
	 * @since 2016年12月9日
	 * @author liudian@yikuyi.com
	 */
	@Before
	public void setUpBefore() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(partyGroupResource).build();
	}

	@Test
	public void testGetPartyGroupList() throws Exception {
		List<PartyVo> list = new ArrayList<>();
		PartyGroupVo party = new PartyGroupVo();
		party.setPartyId("9999999901");
		when(partyGroupManager.getAllPartyGroupList(Mockito.any(PartyGroupVo.class), Mockito.any(RowBounds.class)))
				.thenReturn(list);
		PartyGroupVo vo = new PartyGroupVo();
		mockMvc.perform(put("/v1/partyGroup").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(mapper.writeValueAsString(vo))).andExpect(status().isOk());
	}
	@Test
	public void testGetAllPartyGroupList() throws Exception {
		List<PartyVo> list = new ArrayList<>();
		PartyGroupVo party = new PartyGroupVo();
		party.setPartyId("9999999901");
		when(partyGroupManager.getAllPartyGroupList(Mockito.any(PartyGroupVo.class), Mockito.any(RowBounds.class)))
				.thenReturn(list);
		PartyGroupVo vo = new PartyGroupVo();
		mockMvc.perform(put("/v1/party/allparty").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(mapper.writeValueAsString(vo))).andExpect(status().isOk());
	}
	@Test
	public void testartyGroups() throws Exception {
		List<PartyVo> list = new ArrayList<>();
		PartyGroupVo party = new PartyGroupVo();
		party.setPartyId("9999999901");
		when(partyGroupManager.getAllPartyGroupList(Mockito.any(PartyGroupVo.class), Mockito.any(RowBounds.class)))
				.thenReturn(list);
		PartyGroupVo vo = new PartyGroupVo();
		mockMvc.perform(put("/v1/party/partygroups").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(mapper.writeValueAsString(vo))).andExpect(status().isOk());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testFindPartyGroupByName() throws Exception {

		when(partyGroupManager.findPartyGroupByName(Mockito.anyString())).thenReturn(Mockito.anyList());

	   mockMvc.perform(get("/v1/groups/groupname?groupName=sz_junit_save"))
				.andExpect(status().isOk());

	}
/*
	@Test
	public void testGetAllPartyGroupList() throws Exception {
		List<PartyVo> list = new ArrayList<>();
		PartyVo party = new PartyVo();
		party.setId("999");
		list.add(party);
		when(partyGroupManager.getAllPartyGroupList(Mockito.any(PartyGroupVo.class), Mockito.any(RowBounds.class)))
				.thenReturn(list);

		PartyGroupVo vo = new PartyGroupVo();
		mockMvc.perform(put("/v1/party/allparty").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(mapper.writeValueAsString(vo))).andExpect(status().isOk());
	}*/

	@Test
	public void getOrderPermissions() throws Exception {
		MsgResultVo result = new MsgResultVo();
		result.setCode("10000");
		result.setValue("激活或者关联");
		when(partyGroupManager.orderPermissions(Mockito.anyString())).thenReturn(result);
		MockHttpServletResponse response = mockMvc.perform(get("/v1/party/permissions?partyId=99999999901"))
				.andReturn().getResponse();
		
		result = mapper.readValue(response.getContentAsString(), MsgResultVo.class);
		Assert.assertEquals(result.getCode(),"10000");
	}

	@Test
	public void insertLogisticsCompanyTest() throws Exception {
		PartyGroupVo vo = new PartyGroupVo();
		Mockito.doNothing().when(partyGroupManager).insertLogisticsCompany(Mockito.any(PartyGroupVo.class));
		mockMvc.perform(post("/v1/party/logistics").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(mapper.writeValueAsBytes(vo))).andExpect(status().isOk());
	}

	@Test
	public void updateLogisticsCompanyTest() throws Exception{
		PartyGroupVo vo = new PartyGroupVo();
		Mockito.doNothing().when(partyGroupManager).updateLogisticsCompany(Mockito.any(PartyGroupVo.class));
		mockMvc.perform(put("/v1/party/logistics").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(mapper.writeValueAsBytes(vo))).andExpect(status().isOk());

	}

	@Test
	public void changeLogisticsCompanyStatusTest()  throws Exception{
		PartyGroupVo vo = new PartyGroupVo();
		Mockito.doNothing().when(partyGroupManager).changeLogisticsCompanyStatus(Mockito.any(PartyGroupVo.class));
		mockMvc.perform(put("/v1/party/logistics/status").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(mapper.writeValueAsBytes(vo))).andExpect(status().isOk());

	}

}