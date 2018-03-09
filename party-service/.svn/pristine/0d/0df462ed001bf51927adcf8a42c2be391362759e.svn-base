package com.yikuyi.party.facility.api.impl;

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
import com.framewrok.springboot.web.LoginUserInjectionInterceptor;
import com.yikuyi.party.facility.bll.FacilityManager;
import com.yikuyi.party.facility.model.Facility;

/**
 * 
 * @author zr.shuzuo@yikuyi.com
 * @version 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class FacilityResourceTest {
	private ObjectMapper mapper = ObjectMapperHelper.configeObjectMapper(new ObjectMapper());
	@InjectMocks
	private FacilityResource facilityResource;
	@Mock
	private FacilityManager facilityManager;
	private MockMvc mockMvc;
	LoginUser loginUser = new LoginUser("9999999901", "admin", "9999999901", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));

	public FacilityResourceTest() {
	}
	/**
	 * @throws java.lang.Exception
	 * @since 2016年12月9日
	 * @author liudian@yikuyi.com
	 */
	@Before
	public void setUpBefore() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(facilityResource).build();
	}
	@Test
	public void testGetFacilityList() throws Exception{
		List<Facility> facilityList = new ArrayList<>();
		Facility facility = new Facility();
		facility.setFacilityName("test");
		facilityList.add(facility);
		when(facilityManager.getFacilityList("9999999901")).thenReturn(facilityList);
		 JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, Facility.class);
	
		facilityList = mapper.readValue(mockMvc.perform(get("/v1/facility?id=9999999901"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), javaType);
		Assert.assertEquals(facilityList.get(0).getFacilityName(),"test");

	}
	
	
	@Test
	public void testGetFacility() throws Exception{
		List<Facility> facilityList = new ArrayList<>();
		Facility facility = new Facility();
		facility.setFacilityName("test");
		facilityList.add(facility);
		
		when(facilityManager.getFacility(Arrays.asList("1"))).thenReturn(facilityList);
		 JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, Facility.class);
	
		facilityList = mapper.readValue(mockMvc.perform(post("/v1/facility/batch").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(Arrays.asList("1"))))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), javaType);
		
		Assert.assertEquals(facilityList.get(0).getFacilityName(),"test");

	}
	
	@Test
	public void testAddFacility() throws Exception{
		Facility facility = new Facility();
		facility.setFacilityName("test");
		facility.setOwnerPartyId("111");
		when(facilityManager.addFacility(Mockito.any(Facility.class))).thenReturn(facility);
		facility = mapper.readValue(mockMvc.perform(post("/v1/facility").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(facility)).requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), Facility.class);
		Assert.assertEquals(facility.getFacilityName(),"test");
	}
	
	
	@Test
	public void testAddFacilityFromLeadMaterial() throws Exception{
		Facility facility = new Facility();
		facility.setFacilityName("test");
		facility.setOwnerPartyId("111");
		when(facilityManager.addFacility(Mockito.any(Facility.class))).thenReturn(facility);
		facility = mapper.readValue(mockMvc.perform(post("/v1/facility/leadMaterial").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(facility)).requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), Facility.class);
		Assert.assertEquals(facility.getFacilityName(),"test");
	}
	
	@Test
	public void testDelFacilityById() throws Exception{
		Facility facility = new Facility();
		facility.setFacilityName("test");
		facility.setOwnerPartyId("111");
		boolean flag=false;
		
		flag = mapper.readValue(mockMvc.perform(delete("/v1/facility/9999999901").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(facility)).requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), boolean.class);
		Assert.assertEquals(flag,true);
		
	}
	
	@Test
	public void testUpFacilityById() throws Exception{
		Facility facility = new Facility();
		facility.setFacilityName("test");
		facility.setOwnerPartyId("111");
		boolean flag=false;
		flag = mapper.readValue(mockMvc.perform(put("/v1/facility/9999999901").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(facility)).requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), boolean.class);
		Assert.assertEquals(flag,true);
		
	}
	
	@Test
	public void testDelFacilityByPartyId() throws Exception{
		Facility facility = new Facility();
		facility.setFacilityName("test");
		facility.setOwnerPartyId("111");
		boolean flag=false;
		flag = mapper.readValue(mockMvc.perform(delete("/v1/facility/ownerParty/9999999901").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), boolean.class);
		Assert.assertEquals(flag,true);
		
	}
}
