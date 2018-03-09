/*
 * Created: 2017年6月28日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.document.bll;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.yikuyi.party.PartyClientBuilder;
import com.yikuyi.product.vo.ProductVo;

@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class, MockitoTestExecutionListener.class })
@Transactional
@Rollback
@ConfigurationProperties
@RunWith(SpringRunner.class)
@ComponentScan(basePackages = "com.yikuyi.product")
@SpringBootTest
@ActiveProfiles("dev")
public class DocumentManagerTest {

	@Value("${api.basedata.serverUrlPrefix}")
	private String basedataServerUrlPrefix;

	@SpyBean
	private PartyClientBuilder partyClientBuilder;

	@Autowired
	DocumentManager documentManager;

	@Autowired
	DocumentLogManager documentLogManager;

	@Test
	public void updateDocLogsStatusByVoTest() {
		List<ProductVo> list = new ArrayList<ProductVo>();
		ProductVo pvo = new ProductVo();
		pvo.setActivityId("111");
		pvo.setId("111");
		list.add(pvo);
		String errorMsg = null;
		documentLogManager.updateDocLogsStatusByVo(list, errorMsg);
	}

}
