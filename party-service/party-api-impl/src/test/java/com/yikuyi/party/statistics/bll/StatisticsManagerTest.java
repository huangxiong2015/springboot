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
package com.yikuyi.party.statistics.bll;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.yikuyi.party.statistics.vo.StatisticsVo.StatisticsType;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class })
@Transactional
@Rollback
public class StatisticsManagerTest {
	@Autowired
	private StatisticsManager statisticsManager;

	@Test
	// @DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value =
	// "statistics_sampledata.xml")
	// @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value
	// = "userlogin_update_data2.xml")
	public void testSaveEnterprise() {
		Long num = statisticsManager.getStatisticsNumByType(StatisticsType.REG_NUM);
		Long num1 = statisticsManager.getStatisticsNumByType(StatisticsType.LOGIN_NUM);
		Assert.assertTrue(num >= 0);
		Assert.assertTrue(num1 >= 0);
	}
}
