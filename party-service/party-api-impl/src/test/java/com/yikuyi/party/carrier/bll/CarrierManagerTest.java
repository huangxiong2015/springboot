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
package com.yikuyi.party.carrier.bll;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.apache.ibatis.session.RowBounds;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.yikuyi.party.model.Party.PartyStatus;
import com.yikuyi.party.vo.PartyCarrierParamVo;
import com.yikuyi.party.vo.PartyCarrierVo;
@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class,MockitoTestExecutionListener.class })
@Transactional
@Rollback
public class CarrierManagerTest {
	@Autowired
	private CarrierManager carrierManager;
	
	

	@SpyBean
	private AuthorizationUtil authorizationUtil;
	
	@Before
	public void init() {
		LoginUser loginUser = new LoginUser("9999999901", "admin", "9999999901", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));		
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));;
		RequestContextHolder.currentRequestAttributes().setAttribute(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser, RequestAttributes.SCOPE_REQUEST);
	}

	
	public CarrierManagerTest(){
		MockitoAnnotations.initMocks(this);
	}
	
	/**
	 * 物流列表信息
	 * @since 2017年7月26日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT , value={"classpath:com/yikuyi/party/carrier/bll/party_carrier_list.xml"})
	public void getPartyCarrierListTest(){
		PageInfo<PartyCarrierVo> list = carrierManager.getPartyCarrierList(new PartyCarrierParamVo(), RowBounds.DEFAULT);
		assertEquals(3,list.getTotal());
		PartyCarrierVo e = list.getList().get(0);
		assertEquals("美国测试公司",e.getGroupName());
	}
	
	
	
	/**
	 * 根据partyId获取相应的物流信息
	 * @since 2017年7月26日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"classpath:com/yikuyi/party/carrier/bll/party_carrier_list.xml"})
	public void getPartyCarrierVoInfoTest(){
		PartyCarrierVo carrierVo = carrierManager.getPartyCarriorVoInfo("8888889");
		assertEquals(carrierVo.getGroupName(), "天宇测试公司");
	}
	
	/**
	 * 物流信息-启用/停用
	 * @since 2017年7月26日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void testUpdateStatusId(){
		PartyCarrierVo carrierVo = new PartyCarrierVo();
		carrierVo.setPartyStatus(PartyStatus.PARTY_ENABLED);
		carrierManager.updateStatusId("889673708757581824",carrierVo);
		PartyCarrierVo carrierVo2 = new PartyCarrierVo();
		carrierVo2.setPartyStatus(PartyStatus.PARTY_DISABLED);
		carrierManager.updateStatusId("889673708757581824",carrierVo2);
		
		PartyCarrierVo carrierVo1 = new PartyCarrierVo();
		carrierVo1.setGroupName("test");
		carrierManager.updateStatusId("889673708757581824",carrierVo1);
		
	}
}