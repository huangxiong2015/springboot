/*
 * Created: 2017年6月27日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.goods.manager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.yikuyi.product.base.ProductApplicationTestBase;
import com.yikuyi.product.model.ProductSearchResult;

@RunWith(SpringRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
	TransactionDbUnitTestExecutionListener.class })
@Transactional
@Rollback
public class ProductSearchResultManagerTest extends ProductApplicationTestBase {

	@Autowired
	private ProductSearchResultManager productSearchResultManager;
	
	/**
	 * 测试搜索结果页新增日志记录
	 * 
	 * @since 2017年6月23日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:com/yikuyi/product/goods/productSearchResult_sample.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT,value = "classpath:com/yikuyi/product/goods/productSearchResult_add_result.xml")
	public void testInsert(){
		ProductSearchResult searchResult = new ProductSearchResult();
		searchResult.setSearchResultId("1234");
		searchResult.setPartyId("9999999901");
		searchResult.setKeyWord("aaa");
		searchResult.setPartyName("admin");
		searchResult.setHasResult("Y");
		productSearchResultManager.insert(searchResult);
	}
}
