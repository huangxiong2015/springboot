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
package com.yikuyi.party.shipAddress.api.impl;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import com.yikuyi.party.contact.model.ContactMech;
import com.yikuyi.party.contact.model.ContactMech.MechType;
import com.yikuyi.party.contact.model.PostalAddress;
import com.yikuyi.party.contact.model.TelecomNumber;
import com.yikuyi.party.contact.model.TelecomNumber.MobileTelecomNumber;
import com.yikuyi.party.contact.model.TelecomNumber.PhoneTelecomNumber;
import com.yikuyi.party.contact.model.TelecomNumber.QqTelecomNumber;
import com.yikuyi.party.model.PartyContactMech;
import com.yikuyi.party.model.PartyContactMech.PurposeType;
import com.yikuyi.party.shipAddress.bll.PartyContactMechManager;


/**
 * 
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class PartyAddressResourceTest{
	
	private ObjectMapper mapper = ObjectMapperHelper.configeObjectMapper(new ObjectMapper());
	
	private MockMvc mockMvc;
	
	@InjectMocks
	private PartyAddressResource partyAddressResource;
	
	@Mock
	private PartyContactMechManager mockPartyContactMechManager;
	
	
	LoginUser loginUser = new LoginUser("9999999901", "admin", "9999999901", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));

	
	public PartyAddressResourceTest() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Before
	public void setUpBefore() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(partyAddressResource).build();

	}
	
	@Test
	public void testGetShipAddressList() throws Exception{
		
		List<PartyContactMech> contactMechList = new ArrayList<>();
		PartyContactMech mech = new PartyContactMech();
		mech.setPurposeType(PurposeType.REGISTER_LOCATION);
		contactMechList.add(mech);
		when(mockPartyContactMechManager.selectPartyContactMechByType(PurposeType.REGISTER_LOCATION, null, null)).thenReturn(contactMechList);
		
		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, PartyContactMech.class);
		
		List<PartyContactMech> mockListRst = mapper.readValue(mockMvc.perform(get("/v1/address?purposeType=REGISTER_LOCATION")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), javaType);
		Assert.assertEquals(mockListRst.get(0).getPurposeType().name(),"REGISTER_LOCATION");
		
	}
	
	@Test
	public void testGetShipAddressById() throws Exception{
		
		PartyContactMech partyContact = new PartyContactMech();
		partyContact.setPartyId("9999999901");
		when(mockPartyContactMechManager.selectPartyContactMechByIdAndType("111", PurposeType.REGISTER_LOCATION, null)).thenReturn(partyContact);
		
		PartyContactMech mockPartyContactMech = mapper.readValue(mockMvc.perform(get("/v1/address/111?addressType=REGISTER_LOCATION")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), PartyContactMech.class);
		Assert.assertEquals(mockPartyContactMech.getPartyId(),"9999999901");

		
	}
	
	@Test
	public void testDelete() throws Exception{
		
		Mockito.doNothing().when(mockPartyContactMechManager).deletePartyContactMech("111");
		this.mockMvc.perform(delete("/v1/address/111")).andExpect(status().isOk());   
	}
	
	
	@Test
	public void testSave() throws Exception{
		
		PartyContactMech partyContactMech = new PartyContactMech();
		
		partyContactMech.setPartyId("888888");
		
		partyContactMech.setPurposeType(PurposeType.BILLING_LOCATION);

		ContactMech contactMech = new ContactMech();
		// 邮箱地址
		contactMech.setId("837220512026329088");
		contactMech.setAlias("易库易公司地址");
		contactMech.setEmail("1044867128@qq.com");
		contactMech.setVerified("Y");

		PostalAddress postalAddress = new PostalAddress();
		postalAddress.setToName("张伟");
		postalAddress.setAttnName("前台代收");
		postalAddress.setAddress1("中国深圳市南山区高新技术产业园麻雀岭工业区M-8栋4楼");
		postalAddress.setAddress2("中国深圳市南山区高新技术产业园麻雀岭工业区M-8栋4楼");
		postalAddress.setPostalCode("0755425");
		postalAddress.setCountryGeoName("中国");
		postalAddress.setCountryGeoId("china");
		postalAddress.setProvinceGeoName("1001");
		postalAddress.setProvinceGeoId("广东省");
		postalAddress.setCountyGeoName("深圳");
		postalAddress.setCountyGeoId("2001");
		postalAddress.setCityGeoName("宝安区");
		postalAddress.setCityGeoId("3001");
		postalAddress.setVerified("Y");


		MobileTelecomNumber mobileTelecomNumber = new MobileTelecomNumber();
		mobileTelecomNumber.setAreaCode("45");
		mobileTelecomNumber.setCountryCode("86");
		mobileTelecomNumber.setAreaCode("1614655");
		mobileTelecomNumber.setContactNumber("15813723723");
		mobileTelecomNumber.setAskForName("张伟");
		mobileTelecomNumber.setVerified("Y");
		mobileTelecomNumber.setMechType(MechType.MOBILE);

		PhoneTelecomNumber phoneTelecomNumber = new PhoneTelecomNumber();
		phoneTelecomNumber.setAreaCode("45");
		phoneTelecomNumber.setCountryCode("86");
		phoneTelecomNumber.setAreaCode("1614655");
		phoneTelecomNumber.setContactNumber("13066939619");
		phoneTelecomNumber.setAskForName("张伟");
		phoneTelecomNumber.setVerified("Y");
		phoneTelecomNumber.setMechType(MechType.TELEPHONE);

		QqTelecomNumber qqTelecomNumber = new QqTelecomNumber();
		qqTelecomNumber.setContactNumber("1044867128");
		qqTelecomNumber.setAskForName("张伟");
		qqTelecomNumber.setVerified("Y");
		qqTelecomNumber.setMechType(MechType.QQ);
		
		
		TelecomNumber telecomNumber = new TelecomNumber();
		telecomNumber.setMobileTelecomNumber(mobileTelecomNumber);
		telecomNumber.setQqTelecomNumber(qqTelecomNumber);
		telecomNumber.setPhoneTelecomNumber(phoneTelecomNumber);

		
		contactMech.setPostalAddress(postalAddress);
		contactMech.setTelecomNumber(telecomNumber);
		
		partyContactMech.setContactMech(contactMech);
		
		
		when(mockPartyContactMechManager.insert(partyContactMech)).thenReturn(partyContactMech);
		
		mockMvc.perform(post("/v1/address").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(partyContactMech))).andExpect(status().isOk());


	}
	
	@Test
	public void testUpdate() throws Exception{
	
		
		PartyContactMech partyContactMech = new PartyContactMech();
		partyContactMech.setPartyId("77777");
		ContactMech contactMech = new ContactMech();
		// 邮箱地址
		contactMech.setId("837220512026329088");
		contactMech.setAlias("易库易公司地址");
		contactMech.setEmail("1044867128@qq.com");
		contactMech.setVerified("Y");
		partyContactMech.setContactMech(contactMech);
		
		when(mockPartyContactMechManager.updateDueTime("111", partyContactMech)).thenReturn(partyContactMech);
	
	    mockMvc.perform(put("/v1/address/111").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(partyContactMech))).andExpect(status().isOk());

	}
	
	
	@Test
	public void testGetBatchAddressList() throws Exception{
		List<ContactMech> partyList = new ArrayList<ContactMech>();
		List<String> list = new ArrayList<>();
		list.add("855272640573603840");
		list.add("832430906533740544");
		when(mockPartyContactMechManager.selectPartyContactMechList(list)).thenReturn(partyList);
	    mockMvc.perform(post("/v1/address/batch").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(list))).andExpect(status().isOk());
	}
}
