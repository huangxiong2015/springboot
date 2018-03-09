/*
 * Created: 2016年3月29日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ykyframework.cache;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import com.ykyframework.test.context.junit4.BaseSpringTest;

@ContextConfiguration(locations = { "classpath:com/ykyframework/cache/spring-cache-ConcurrentMap.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class})
public class AccountDAOTest extends BaseSpringTest {
	@Autowired
	AccountDAO accountDAO;
	
	@Test
	public void testGetAccountById() {
		int id = 1;
		logger.info("get account for the 1st time");
		logger.info("{}", accountDAO.getAccountById(id));
		logger.info("get account for the 2nd time"); //从缓存读
		logger.info("{}", accountDAO.getAccountById(id));
		
		Account acc = new Account();
		acc.setId(id);
		acc.setName("mock name2");
		accountDAO.saveAccount(acc); //写入缓存
		logger.info("get account for the 3rd time");
		logger.info("{}", accountDAO.getAccountById(id));
		logger.info("get account for the 4rd time");
		logger.info("{}", accountDAO.getAccountById(id));
		
		accountDAO.deleteAccountById(id);//删除缓存
		logger.info("get account for the 5th time");
		logger.info("{}", accountDAO.getAccountById(id));
		logger.info("get account for the 6th time");
		logger.info("{}", accountDAO.getAccountById(id));
		
	}

}
