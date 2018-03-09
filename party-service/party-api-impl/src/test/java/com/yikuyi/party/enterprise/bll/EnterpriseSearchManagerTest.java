/*
 * Created: 2017年2月8日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.enterprise.bll;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.framework.springboot.model.LoginUser;
import com.framework.springboot.utils.AuthorizationUtil;
import com.framewrok.springboot.web.LoginUserInjectionInterceptor;
import com.github.pagehelper.PageInfo;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.yikuyi.party.contact.vo.EnterpriseParamVo;
import com.yikuyi.party.contact.vo.EnterpriseVo;
import com.yikuyi.party.contact.vo.UserExtendVo;
import com.yikuyi.workflow.Apply;
import com.yikuyi.workflow.WorkflowClientBuilder;
import com.yikuyi.workflow.resource.ApplyClient;
import com.yikuyi.workflow.vo.ApplyVo;
import com.yikuyi.workflow.vo.ApplyVo.ApplyVoType;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class,MockitoTestExecutionListener.class })
@Transactional
@Rollback
public class EnterpriseSearchManagerTest {
	@Autowired
	private EnterpriseSearchManager entSearchManager;
	

	@Value("${api.workflow.serverUrlPrefix}")
	private String serverUrl;

	@SpyBean
	private AuthorizationUtil authorizationUtil;
	
	@SpyBean
	private WorkflowClientBuilder workflowClientBuilder;
	
	public EnterpriseSearchManagerTest(){
		MockitoAnnotations.initMocks(this);
	}
	@Before
	public void init() {
		LoginUser loginUser = new LoginUser("9999999901", "admin", "9999999901", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
		RequestContextHolder.currentRequestAttributes().setAttribute(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser, RequestAttributes.SCOPE_REQUEST);
	}

	/**
	 * 查询公司信息
	 * 
	 * @param id
	 * @return EnterpriseVo
	 * @since 2016年1月19日
	 * @author zr.helinmei@yikuyi.com
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = { "partyInfo_sampledata.xml" })
	public void testGetPartyDetail() throws UnsupportedEncodingException {
		entSearchManager.getPartyDetail("9999999901");
		entSearchManager.getPartyDetail("9999999903");
		entSearchManager.getPartyDetail("9999999904");
		
		PageInfo<Apply> pageInfo = new PageInfo<>();
		ApplyVo applyVo = new ApplyVo();
		applyVo.setPage(1);
		applyVo.setPageSize(1);
		applyVo.setApplyOrgId("9999999902");
		List<Apply> list = new ArrayList<>();
		Apply  apply = new Apply();
		apply.setApplyOrgId("9999999902");
		list.add(apply);
		pageInfo.setList(list);
		ApplyClient applyClient =Mockito.mock(ApplyClient.class);
		Mockito.when(workflowClientBuilder.applyClient()).thenReturn(applyClient);
		Mockito.when(applyClient.queryApplyByEntity(ApplyVoType.PROCESS, "ORG_DATA_REVIEW", applyVo, authorizationUtil.getLoginAuthorization())).thenReturn(pageInfo);

		ApplyClient applyClient1 =Mockito.mock(ApplyClient.class);
		Mockito.when(workflowClientBuilder.applyClient()).thenReturn(applyClient1);
		Mockito.when(applyClient1.queryApplyByEntity(ApplyVoType.PROCESS, "ORG_PROXY_REVIEW", applyVo, authorizationUtil.getLoginAuthorization())).thenReturn(pageInfo);

		
	}

	/**
	 * 查询公司信息
	 * 
	 * @param id
	 * @return EnterpriseVo
	 * @since 2016年1月19日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = { "classpath:com/yikuyi/party/enterprise/bll/admin_ent_list_allSample.xml" })
	public void testEntCertificationList() {
		PageInfo<EnterpriseVo> list = entSearchManager.entCertificationList(new EnterpriseParamVo(), RowBounds.DEFAULT);
		/*assertEquals(1, list.getTotal());
		EnterpriseVo e = list.getList().get(0);
		assertEquals("天宇测试公司", e.getName());
		assertEquals("1002", e.getCorCategory());*/
	}

	/**
	 * 查询公司信息
	 * 
	 * @param id
	 * @return EnterpriseVo
	 * @since 2016年1月19日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = { "classpath:com/yikuyi/party/enterprise/bll/admin_ent_list_allSample.xml" })
	public void testGetAccountApplyList() {
		PageInfo<EnterpriseVo> list = entSearchManager.getAccountApplyList(new EnterpriseParamVo(), RowBounds.DEFAULT);
		assertEquals(0, list.getTotal());
	}

	/**
	 * 查询公司信息
	 * 
	 * @param id
	 * @return EnterpriseVo
	 * @since 2016年1月19日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = { "classpath:com/yikuyi/party/enterprise/bll/admin_ent_list_allSample.xml" })
	public void testGetAccountStatus() {
		String status = entSearchManager.getAccountStatus("6666688");
		assertEquals("ACCOUNT_VERIFIED", status);
	}

	/**
	 * 根据企业账号获取子账号信息
	 * 
	 * @param entId
	 * @return
	 * @since 2017年5月23日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = { "classpath:com/yikuyi/party/enterprise/bll/admin_ent_list_allSample.xml" })
	public void testGetEnterpriseAccountsList() {
		List<UserExtendVo> list = entSearchManager.getEnterpriseAccountsList("88888866");
		assertTrue(list.size() > 0);
	}
	

}