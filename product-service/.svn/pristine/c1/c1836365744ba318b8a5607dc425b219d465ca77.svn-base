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
package com.yikuyi.product.activity.bll;

import java.util.Arrays;

import org.apache.ibatis.session.RowBounds;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
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
import com.framewrok.springboot.web.LoginUserInjectionInterceptor;
import com.github.pagehelper.PageInfo;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.yikuyi.activity.vo.MockActivityDataVo;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class })
@Transactional
@Rollback
public class RegisterActivityMockManagerTest {
	
	@Autowired
	private RegisterActivityMockManager registerActivityMockManager;

	private MockHttpServletRequest request; 
    private MockHttpServletResponse response; 
	@Before
	public void init() {
		LoginUser loginUser = new LoginUser("9999999901", "admin", "9999999901", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));		
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));;
		RequestContextHolder.currentRequestAttributes().setAttribute(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser, RequestAttributes.SCOPE_REQUEST);
		request = new MockHttpServletRequest();    
	    request.setCharacterEncoding("UTF-8");    
	    response = new MockHttpServletResponse();    
	}
	
	/**
	 * 创建mock数据
	 * @throws Exception
	 * @since 2017年11月23日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void generateMockDataTest() throws Exception{
		registerActivityMockManager.generateMockData();
	}
	
	
	/**
	 * 查询代金券
	 * @throws Exception
	 * @since 2017年11月23日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void listGiftTest() throws Exception{
		String activity = "REGISTER";
		int page = 1;
		int size = 20;
		RowBounds rowBounds = new RowBounds((page - 1) * size, size);
		PageInfo<MockActivityDataVo> pageInfo = registerActivityMockManager.listGift(activity, rowBounds);
		
		PageInfo<MockActivityDataVo> pageInfoNew = registerActivityMockManager.listGift("890027463512424448", rowBounds);
	}
	
}
