/*
 * Created: 2016年12月9日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 
*//**
 * 
 */
package com.yikuyi.party.enterprise.api;

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
import org.mockito.ArgumentMatcher;
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

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.springboot.config.ObjectMapperHelper;
import com.framework.springboot.model.LoginUser;
import com.framewrok.springboot.web.LoginUserInjectionInterceptor;
import com.github.pagehelper.PageInfo;
import com.yikuyi.party.contact.vo.EnterpriseParamVo;
import com.yikuyi.party.contact.vo.EnterpriseVo;
import com.yikuyi.party.contact.vo.UserExtendVo;
import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.party.credit.vo.PartyCreditParamVo;
import com.yikuyi.party.credit.vo.PartyCreditVo;
import com.yikuyi.party.enterprise.api.impl.EnterpriseResource;
import com.yikuyi.party.enterprise.bll.EnterpriseManager;
import com.yikuyi.party.enterprise.bll.EnterprisePeriodManager;
import com.yikuyi.party.enterprise.bll.EnterpriseSearchManager;
import com.yikuyi.party.group.model.PartyGroup;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.model.Party.PartyType;
import com.yikuyi.workflow.Apply;


/**
 * 
 * @author zr.shuzuo@yikuyi.com
 * @version 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class EnterpriseResourceTest {

	private ObjectMapper mapper = ObjectMapperHelper.configeObjectMapper(new ObjectMapper());
	
	@InjectMocks
	private EnterpriseResource enterpriseResource;
	
	@Mock
	private EnterpriseManager enterpriseManager;
	@Mock
	private EnterpriseSearchManager enterpriseSearchManager;
	@Mock
	private EnterprisePeriodManager mockEnterprisePeriodManager;
	

	private MockHttpServletResponse response;


	
	LoginUser loginUser = new LoginUser("9999999901", "admin", "9999999901", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
	
	private MockMvc mockMvc;
	
	public EnterpriseResourceTest() {
	}
	

	/**
	 * @throws java.lang.Exception
	 * @since 2016年12月9日
	 * @author liudian@yikuyi.com
	 */
	@Before
	public void setUpBefore() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(enterpriseResource).build();
	}

	@Test
	public void testGetEnterprise() throws Exception{
		EnterpriseVo mockVo = new EnterpriseVo();
		mockVo.setId("9999999901");
		when(enterpriseSearchManager.getPartyDetail("9999999901")).thenReturn(mockVo);
		
	    
		mockVo = mapper.readValue(mockMvc.perform(get("/v1/enterprises/9999999901"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), EnterpriseVo.class);
		Assert.assertEquals(mockVo.getId(),"9999999901");
		

	}
	
	
	@Test
	public void testGetEnterpriseDetailByEntId() throws Exception{
		EnterpriseVo mockVo = new EnterpriseVo();
		mockVo.setCorporationId("709035037437198336");
		mockVo.setName("test");
		
		class EnterpriseVoMatcher extends ArgumentMatcher<EnterpriseVo> {

			@Override
			public boolean matches(Object argument) {
				return EnterpriseVo.class.equals(argument.getClass());
			}			
		};
		
		EnterpriseVoMatcher eVomatcher = new EnterpriseVoMatcher(); 
		
		ArgumentMatcher<PartyType> pTMatcher = new ArgumentMatcher<PartyType>() {

			@Override
			public boolean matches(Object argument) {				
				return (argument instanceof PartyType);
			}			
		};
				
		
		when(enterpriseSearchManager.getPartyDetailByEntId(Mockito.anyString(), Mockito.argThat(eVomatcher),
				Mockito.argThat(pTMatcher))).thenReturn(mockVo);

		MockHttpServletResponse response = mockMvc
				.perform(get("/v1/enterprises/entDetail/709035037437198336/709035037437198336/VIP_CORPORATION"))
				.andExpect(status().isOk()).andReturn().getResponse();

		mockVo = mapper.readValue(response.getContentAsString(), EnterpriseVo.class);
		Assert.assertEquals(mockVo.getName(),"test");
	}
	
	
	@Test
	public void testEnterpriseList() throws Exception {
		PageInfo<EnterpriseVo> pageInfo = new PageInfo<EnterpriseVo>();
		List<EnterpriseVo> list = new ArrayList<EnterpriseVo>();
		EnterpriseVo vo = new EnterpriseVo();
		vo.setId("99999999");
		vo.setName("test");
		list.add(vo);
		pageInfo.setList(list);
		
		when(enterpriseSearchManager.getAccountApplyList(Mockito.any(EnterpriseParamVo.class), Mockito.any(RowBounds.class))).thenReturn(pageInfo);
		
		JavaType javaType = mapper.getTypeFactory().constructParametricType(PageInfo.class, EnterpriseVo.class);
		pageInfo = mapper.readValue(mockMvc.perform(get("/v1/enterprises"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), javaType);
		Assert.assertEquals(pageInfo.getList().get(0).getId(),"99999999");
	}
	
	@Test
	public void testEnterpriseAccountsList() throws Exception {
		List<UserExtendVo> mockList = new ArrayList<UserExtendVo>();
		UserExtendVo vo = new UserExtendVo();
		vo.setId("9999999901");
		mockList.add(vo);
		when(enterpriseSearchManager.getEnterpriseAccountsList("9999999901")).thenReturn(mockList);
		
		
		MockHttpServletResponse response = mockMvc
				.perform(get("/v1/enterprises/9999999901/accounts"))
				.andExpect(status().isOk()).andReturn().getResponse();
		
		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, UserExtendVo.class);
		mockList = mapper.readValue(response.getContentAsString(), javaType);
		
		Assert.assertEquals(mockList.get(0).getId(),"9999999901");
	}
	

	
	@Test
	public void testEditEntApply() throws Exception {
		Apply apply = new Apply();
		apply.setReason("1111");
		Mockito.doNothing().when(enterpriseManager).editEntApply(Mockito.any(Apply.class));
		mockMvc.perform(post("/v1/enterprises/editEntApply").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(apply))).andExpect(status().isOk());

	}
	
	@Test
	public void testEditEntApplySave() throws Exception{
		Apply apply = new Apply();
		apply.setReason("1111");
		Mockito.doNothing().when(enterpriseManager).editEntApplySave(Mockito.any(Apply.class));
		mockMvc.perform(post("/v1/enterprises/editEntApplySave").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(apply))).andExpect(status().isOk());
	}
	
	@Test
	public void testEntAuthorize() throws Exception{
		Apply apply = new Apply();
		apply.setReason("1111");
		Mockito.doNothing().when(enterpriseManager).entAuthorize(Mockito.any(Apply.class));
		mockMvc.perform(post("/v1/enterprises/entAuthorize").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(apply))).andExpect(status().isOk());

	}
	
	@Test
	public void testEntApplyAuthorize() throws Exception{
		Apply apply = new Apply();
		apply.setReason("1111");
		Mockito.doNothing().when(enterpriseManager).entApplyAuthorize(Mockito.any(Apply.class),Mockito.anyString(),Mockito.anyString());
		mockMvc.perform(post("/v1/enterprises/entApplyAuthorize").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(apply))).andExpect(status().isOk());
		
	}
	
	@Test
	public void testUpdateQualifications() throws Exception{
		EnterpriseVo enterpriseVo = new EnterpriseVo();
		enterpriseVo.setId("99999999");
		Mockito.doNothing().when(enterpriseManager).updateQualifications(Mockito.any(EnterpriseVo.class));
		mockMvc.perform(post("/v1/enterprises/updateQualifications").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(enterpriseVo))).andExpect(status().isOk());
	
	}
	
	@Test
	public void testEntCertificationList()  throws Exception{
		PageInfo<EnterpriseVo> pageInfo = new PageInfo<EnterpriseVo>();
		List<EnterpriseVo> list = new ArrayList<EnterpriseVo>();
		EnterpriseVo vo = new EnterpriseVo();
		vo.setId("99999999");
		list.add(vo);
		pageInfo.setList(list);
		
		when(enterpriseSearchManager.entCertificationList(Mockito.any(EnterpriseParamVo.class), Mockito.any(RowBounds.class)))
		.thenReturn(pageInfo);
		
		JavaType javaType = mapper.getTypeFactory().constructParametricType(PageInfo.class, EnterpriseVo.class);
		MockHttpServletResponse rep = mockMvc.perform(get("/v1/enterprises/entCertificationList"))
				.andExpect(status().isOk()).andReturn().getResponse();
		mapper.readValue(rep.getContentAsString(), javaType);
		Assert.assertEquals(pageInfo.getList().get(0).getId(), "99999999");
		
	}

	/**
	 * 
	 * @throws Exception
	 * @since 2017年8月24日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void testGetAccountApplyList()  throws Exception{
		PageInfo<EnterpriseVo> pageInfo = new PageInfo<EnterpriseVo>();
		List<EnterpriseVo> list = new ArrayList<EnterpriseVo>();
		EnterpriseVo vo = new EnterpriseVo();
		vo.setId("99999999");
		list.add(vo);
		pageInfo.setList(list);
		
		when(enterpriseSearchManager.getAccountApplyList(Mockito.any(EnterpriseParamVo.class), Mockito.any(RowBounds.class)))
		.thenReturn(pageInfo);
		
		JavaType javaType = mapper.getTypeFactory().constructParametricType(PageInfo.class, EnterpriseVo.class);
		MockHttpServletResponse rep = mockMvc.perform(get("/v1/enterprises/getAccountApplyList"))
				.andExpect(status().isOk()).andReturn().getResponse();
		
		mapper.readValue(rep.getContentAsString(), javaType);
		Assert.assertEquals(pageInfo.getList().get(0).getId(), "99999999");
	}
	
	@Test
	public void testGetAccountStatus()  throws Exception{
		String mockStr = "enable";
		when(enterpriseSearchManager.getAccountStatus("99999999901"))
		.thenReturn(mockStr);
		
		MockHttpServletResponse rep = mockMvc.perform(get("/v1/enterprises/getAccountStatus/99999999901"))
				.andExpect(status().isOk()).andReturn().getResponse();
		
		String returnStr = rep.getContentAsString();
		Assert.assertTrue(returnStr.equals(mockStr));
	}
	
	@Test
	public void testInvalidAccount() throws Exception{
		Mockito.doNothing().when(enterpriseManager).invalidAccount(Mockito.anyString(), Mockito.anyString());
        mockMvc.perform(put("/v1/enterprises/invalidAccount/9999999901?reason=1111")).andExpect(status().isOk());
	}

	
	/**
	 * 根据partyId查询用户的账期信息
	 * @throws Exception
	 * @since 2017年8月1日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void testGetPartyCreditByCorporationId() throws Exception{
		PartyCreditVo creditVo = new PartyCreditVo();
		creditVo.setPartyId("123456789");
		when(mockEnterprisePeriodManager.getPartyCreditVoByCorporationId(Mockito.anyString())).thenReturn(creditVo);
		
		MockHttpServletResponse response = mockMvc.perform(get("/v1/enterprises/893049126831259648/credit")).andExpect(status().isOk()).andReturn().getResponse();
		creditVo = mapper.readValue(response.getContentAsString(), PartyCreditVo.class);
		Assert.assertEquals(creditVo.getPartyId(), "123456789");
	}

	/**
	 * 账期订单列表查询
	 * @since 2017年8月9日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void testGetPartyCreditVoList() throws Exception{
		PageInfo<PartyCreditVo> pageInfo = new PageInfo<PartyCreditVo>();
		List<PartyCreditVo> list = new ArrayList<PartyCreditVo>();
		PartyCreditVo vo = new PartyCreditVo();
		vo.setPartyId("99999999");
		list.add(vo);
		pageInfo.setList(list);
		
		when(mockEnterprisePeriodManager.getPartyCreditVoList(Mockito.any(PartyCreditParamVo.class), Mockito.any(RowBounds.class)))
		.thenReturn(pageInfo);
		
		JavaType javaType = mapper.getTypeFactory().constructParametricType(PageInfo.class, PartyCreditVo.class);
		MockHttpServletResponse rep = mockMvc.perform(get("/v1/enterprises/credit")).andExpect(status().isOk()).andReturn().getResponse();
		
		mapper.readValue(rep.getContentAsString(), javaType);
		Assert.assertEquals(pageInfo.getList().get(0).getPartyId(), "99999999");
		
	}
	
	
	/**
	 * 根据企业Id查询partyIds
	 * @throws Exception
	 * @since 2017年8月21日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void testGetPartyRelationShipList() throws Exception{
		List<String> list = new ArrayList<String>();
		list.add("897362988506284032");
		when(mockEnterprisePeriodManager.getPartyIdList(Mockito.anyString())).thenReturn(list);
		
		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, String.class);
		MockHttpServletResponse rep = mockMvc.perform(get("/v1/enterprises/897361616515891200/enterprises")).andExpect(status().isOk()).andReturn().getResponse();
		
		mapper.readValue(rep.getContentAsString(), javaType);
		Assert.assertEquals(list.get(0), "897362988506284032");
		
	}
	
	
	/**
	 * 导出企业会员列表
	 * @throws Exception
	 * @since 2017年8月24日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void testExportEnt() throws Exception{
		EnterpriseParamVo param = new EnterpriseParamVo();
		Mockito.doNothing().when(enterpriseManager).exportEnt(param, response);
		mockMvc.perform(get("/v1/enterprises/excel")).andExpect(status().isOk()).andReturn().getResponse();
	}
	
	/**
	 * 导出认证企业管理列表
	 * @throws Exception
	 * @since 2017年8月24日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void testExportEntCertification() throws Exception{
	
		EnterpriseParamVo paramVo = new EnterpriseParamVo();
		Mockito.doNothing().when(enterpriseManager).exportEntCertification(paramVo, response);
		mockMvc.perform(get("/v1/enterprises/certified/excel")).andExpect(status().isOk()).andReturn().getResponse();
	}

	@Test
	public void testUpdateCompanyName() throws Exception{
		Mockito.doNothing().when(enterpriseManager).updateCompanyName(Mockito.anyString(),Mockito.anyString());
		mockMvc.perform(put("/v1/enterprises/1/companyName").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString("aa"))).andExpect(status().isOk());
	}
	
	
	@Test
	public void testAccountPeriodAudit() throws Exception {
		Apply apply = new Apply();
		apply.setReason("1111");
		Mockito.doNothing().when(mockEnterprisePeriodManager).accountPeriodAudit(Mockito.any(Apply.class));
		mockMvc.perform(post("/v1/enterprises/accountPeriodAudit").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(apply))).andExpect(status().isOk());

	}
	
	@Test
	public void testAccountPeriodApply() throws Exception {
		Apply apply = new Apply();
		apply.setReason("1111");
		Mockito.doNothing().when(mockEnterprisePeriodManager).accountPeriodAudit(Mockito.any(Apply.class));
		mockMvc.perform(post("/v1/enterprises/accountPeriodApply").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(apply))).andExpect(status().isOk());

	}
	
	@Test
	public void testEditMemberCompany() throws Exception{
		EnterpriseVo enterpriseVo = new EnterpriseVo();
		enterpriseVo.setId("99999999");
		Mockito.doNothing().when(enterpriseManager).editMemberCompany(Mockito.any(EnterpriseVo.class));
		mockMvc.perform(post("/v1/enterprises/editMemberCompany").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(enterpriseVo))).andExpect(status().isOk());
	
	}
	
	@Test
	public void testEnterpriseDocumentsExpiredJob() throws Exception{
		Mockito.doNothing().when(enterpriseManager).enterpriseDocumentsExpiredJob();
		mockMvc.perform(post("/v1/enterprises/expired").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(status().isOk());
	
	}
	
	@Test
	public void testIsAdmin() throws Exception{
		when(enterpriseManager.isAdmin("9999999901")).thenReturn(Mockito.anyBoolean());
		Boolean bool = mapper.readValue(mockMvc.perform(get("/v1/enterprises/accounts/admin"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), Boolean.class);
		Assert.assertEquals(bool,false);
	}
	

	@Test
	public void testSave() throws Exception {
		Apply apply = new Apply();
		apply.setReason("1111");
		Mockito.doNothing().when(enterpriseManager).save(Mockito.any(Apply.class));
		mockMvc.perform(post("/v1/enterprises").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(apply))).andExpect(status().isOk());

	}
	
	
	@Test
	public void testGetAdmin() throws Exception{
		UserVo mockVo = new UserVo();
		mockVo.setId("9999999901");
		mockVo.setName("法国");
	    when(enterpriseManager.getAdmin(Mockito.anyString(),Mockito.anyString())).thenReturn(mockVo);
	    UserVo user =  mapper.readValue(mockMvc.perform(get("/v1/enterprises/accounts/9999999901?role=test").requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser))
		.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), UserVo.class);
	    Assert.assertEquals(user.getName(),"法国");
	}
	@Test
	public void testSendNodeCreitMail() throws Exception{
		Mockito.doNothing().when(mockEnterprisePeriodManager).sendNodeCreitMail("9999999901");
		mockMvc.perform(get("/v1/enterprises/sendNodeCreitMail/9999999901").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(status().isOk());
	
	}
	@Test
	public void testUpdateCreditStatus() throws Exception {
		Mockito.doNothing().when(mockEnterprisePeriodManager).updateCreditStatus(Mockito.anyString(),Mockito.anyString());
		mockMvc.perform(put("/v1/enterprises/frozenCredit/9999999901/ENABLED").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(status().isOk());

	}
	
	@Test
	public void testGetEntBaseInfo() throws Exception {
		PartyGroup mockVo = new PartyGroup();
		mockVo.setGroupName("test");
	    when(enterpriseManager.getEntBaseInfo(Mockito.anyString())).thenReturn(mockVo);
	    PartyGroup group =  mapper.readValue(mockMvc.perform(get("/v1/enterprises/baseInfo?role=9999999901").requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser))
		.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), PartyGroup.class);
	    Assert.assertEquals(group.getGroupName(),"test");
	}
	
	@Test
	public void testIsFristActive() throws Exception {
		Boolean mockVo = true;
	    when(enterpriseManager.isFristActive(Mockito.anyString())).thenReturn(mockVo);
	    Boolean Boolean = mapper.readValue(mockMvc.perform(get("/v1/enterprises/accounts/isFristActive").requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser))
		.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), Boolean.class);
	    Assert.assertEquals(Boolean,true);
	}
	
	@Test
	public void testIsActivedOrRelationed() throws Exception {
		String mockVo =enterpriseManager.isActivedOrRelationed("9998855");
		Assert.assertEquals(mockVo,null);
	}
	
	@Test
	public void testUpdateStatus() throws Exception {
		Party party = new Party();
		party.setId("9999999901");
		party.setPartyStatus(Party.PartyStatus.PARTY_ENABLED);
		enterpriseManager.updateStatus(party);
	}
	
	@Test
	public void testEditCompany() throws Exception {
		EnterpriseVo vo = new EnterpriseVo();
		vo.setId("9999999901");
		enterpriseManager.editCompany(vo);
	}
	
	@Test
	public void testActiveAccountSave() throws Exception {
		Apply vo = new Apply();
		vo.setApplyId("9999999901");
		enterpriseManager.activeAccountSave(vo);
	}
	
	@Test
	public void testGetImgUrl() throws Exception {
		enterpriseManager.getImagUrl("www.baidu.com");
	}
}