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
package com.yikuyi.party.person.api.impl;

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

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.springboot.config.ObjectMapperHelper;
import com.github.pagehelper.PageInfo;
import com.yikuyi.party.contact.vo.UserExtendVo;
import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.party.credit.model.PartyCredit;
import com.yikuyi.party.credit.vo.PartyCreditVo;
import com.yikuyi.party.person.model.Person;
import com.yikuyi.party.userLogin.bll.UserLoginManager;
import com.yikuyi.party.vendor.vo.Vendor.Currency;
@RunWith(SpringJUnit4ClassRunner.class)
public class PersonResourceTest {

	private ObjectMapper mapper = ObjectMapperHelper.configeObjectMapper(new ObjectMapper());
	@InjectMocks
	private PersonResource personResource;
	@Mock
	private UserLoginManager userLoginManager;
	
	private MockMvc mockMvc;
	
	public PersonResourceTest(){
		
	}

	
	@Before
	public void setUpBefore() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(personResource).build();
	}
	
	@Test
	public void testSearch() throws Exception {
		PageInfo<UserVo> page = new PageInfo<UserVo>();
		List<UserVo> list = new ArrayList<UserVo>();
		UserVo vo = new UserVo();
		vo.setId("99999999");
		list.add(vo);
		page.setList(list);
		
		when(userLoginManager.search(Mockito.anyString(), Mockito.any(RowBounds.class),
				Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(page);
		
		MockHttpServletResponse rep = mockMvc.perform(get("/v1/person/search")).andExpect(status().isOk())
				.andReturn().getResponse();
		JavaType javaType = mapper.getTypeFactory().constructParametricType(PageInfo.class, UserVo.class);
		page = mapper.readValue(rep.getContentAsString(), javaType);
		Assert.assertEquals(page.getList().get(0).getId(), "99999999");
	}
	
	
	@Test
	public void testGetEmailListByRoleType() throws Exception {
		List<Person> list = new ArrayList<Person>();
		Person person = new Person();
		person.setLastNameLocal("test");
		list.add(person);
		when(userLoginManager.getEmailListByRoleType(Mockito.anyString())).thenReturn(list);
		MockHttpServletResponse rep = mockMvc.perform(get("/v1/person/all?roleType=ADMIN")).andExpect(status().isOk())
		.andReturn().getResponse();
		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, Person.class);
		list = mapper.readValue(rep.getContentAsString(), javaType);
		Assert.assertEquals(list.get(0).getLastNameLocal(), "test");
	}
	
	@Test
	public void testGetReportsTo() throws Exception  {
		List<UserExtendVo> list = new ArrayList<UserExtendVo>();
		UserExtendVo vo = new UserExtendVo();
		vo.setId("9999999");
		list.add(vo);
		when(userLoginManager.getReportsTo(Mockito.anyString())).thenReturn(list);
		MockHttpServletResponse rep = mockMvc.perform(get("/v1/person/reportsto?partyId=9999999901")).andExpect(status().isOk())
		.andReturn().getResponse();
		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, UserExtendVo.class);
		list = mapper.readValue(rep.getContentAsString(), javaType);
		Assert.assertEquals(list.get(0).getId(), "9999999");
	}
	
	
	@Test
	public void testGetPerson() throws Exception {
		UserVo vo = new UserVo();
		vo.setId("9999999");
		when(userLoginManager.getPerson(Mockito.anyString())).thenReturn(vo);
		MockHttpServletResponse rep = mockMvc.perform(get("/v1/person/9999999901")).andExpect(status().isOk())
		.andReturn().getResponse();
		vo = mapper.readValue(rep.getContentAsString(), UserVo.class);
		Assert.assertEquals(vo.getId(), "9999999");
	}
	
	@Test
	public void testUpdatePwd()  throws Exception{
		
		when(userLoginManager.updatePwd(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn("success");
		mockMvc.perform(put("/v1/person/9999999901?passWord=123456")).andExpect(status().isOk());
	}
	
	@Test
	public void testSave() throws Exception{
		UserVo vo = new UserVo();
		when(userLoginManager.save(Mockito.any(UserVo.class))).thenReturn("success");
		mockMvc.perform(post("/v1/person/user").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(mapper.writeValueAsString(vo))).andExpect(status().isOk());
	}
	
	@Test
	public void testUpdate() throws Exception{
		UserVo vo = new UserVo();
		when(userLoginManager.update(Mockito.any(UserVo.class))).thenReturn("success");
		mockMvc.perform(put("/v1/person/user").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(mapper.writeValueAsString(vo))).andExpect(status().isOk());

	}
	
	@Test
	public void addUserTest() throws Exception{
		UserVo vo = new UserVo();
		Mockito.doNothing().when(userLoginManager).addUser(Mockito.any(UserVo.class));
		mockMvc.perform(post("/v1/person/adduser").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(mapper.writeValueAsString(vo))).andExpect(status().isOk());

	}
	
	@Test
	public void getUserDetailTest() throws Exception{
		UserVo vo = new UserVo();
		vo.setId("9999999");
		when(userLoginManager.getUserDetail(Mockito.anyString())).thenReturn(vo);
		
		MockHttpServletResponse rep = mockMvc.perform(get("/v1/person/detail?partyId=863203580700524544")).andExpect(status().isOk())
				.andReturn().getResponse();
		vo = mapper.readValue(rep.getContentAsString(), UserVo.class);
		Assert.assertEquals(vo.getId(), "9999999");

	}
	
	@Test
	public void updateUserTest() throws Exception{
		UserVo vo = new UserVo();
		Mockito.doNothing().when(userLoginManager).updateUser(Mockito.any(UserVo.class));
		mockMvc.perform(put("/v1/person/updateuser").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(mapper.writeValueAsString(vo))).andExpect(status().isOk());
	}
	
	
	
	/**
	 * 根据partyId查询用户的账期信息
	 * @throws Exception
	 * @since 2017年7月28日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void getAccountPeriodInfoTest() throws Exception{
		PartyCreditVo creditVo = new PartyCreditVo();
		creditVo.setPartyId("123456789");
		creditVo.setCurrency(Currency.CNY);
		
		when(userLoginManager.getPartyCreditVo(Mockito.anyString(), Mockito.any(Currency.class))).thenReturn(creditVo);
		
		MockHttpServletResponse response = mockMvc.perform(get("/v1/person/893049126831259648/credit")).andExpect(status().isOk()).andReturn().getResponse();
		creditVo = mapper.readValue(response.getContentAsString(), PartyCreditVo.class);
		Assert.assertEquals(creditVo.getPartyId(), "123456789");
		
	}
	
	
	
	/**
	 * 根据partyId修改用户的账期信息
	 * @throws Exception
	 * @since 2017年8月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void updatePartyCredit() throws Exception{
		PartyCredit partyCredit = new PartyCredit();
		partyCredit.setPartyId("892283194578236642");
		partyCredit.setRealtimeCreditQuota(500.00);
		Mockito.doNothing().when(userLoginManager).updatePartyCredit(Mockito.any(PartyCredit.class));
		mockMvc.perform(put("/v1/person/credit").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(mapper.writeValueAsString(partyCredit))).andExpect(status().isOk());
	}
	
	
	/**
	 * 批量查询用户的账期信息
	 * @throws Exception
	 * @since 2017年11月1日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	/*@Test
	public void getPartyCreditInfoListTest() throws Exception{
		List<PartyCreditVo> creditVoList = new ArrayList<>();
		PartyCreditVo creditVo = new PartyCreditVo();
		creditVo.setPartyId("123456789");
		creditVo.setCurrency(Currency.CNY);
		creditVoList.add(creditVo);
		String partyList = "[{\"partyId\":\"4563289741526\",\"currency\":\"CNY\"}]";
	    when(userLoginManager.getPartyCreditInfoList(partyList)).thenReturn(creditVoList);
	    
	    JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, PartyCreditVo.class);
		List<PartyProductLineVo> mockList =  mapper.readValue(mockMvc.perform(post("/v1/person/credit/list?partyList="+partyList))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), javaType);

		Assert.assertEquals(mockList.get(0).getPartyId(),"123456789");
	}*/
	
}
