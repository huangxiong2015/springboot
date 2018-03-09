/*
 * Created: 2017年2月23日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.goods.manager;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.google.common.collect.Lists;
import com.yikuyi.category.model.ProductCategory;
import com.yikuyi.product.log.manager.ProductMappingErrorManager;
import com.ykyframework.context.ContextAttributes;
import com.ykyframework.context.RuntimeContextHolder;
import com.ykyframework.mqservice.MQConstants;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class })
@Transactional(propagation = Propagation.REQUIRES_NEW)
@Rollback
public class ProductMappingErrorTest {

	@Autowired
	private ProductMappingErrorManager productMappingErrorManager;
	
	@Before
	public void setUpBefore() throws Exception {
		RuntimeContextHolder.setContextAttributes(new ContextAttributes());
		RuntimeContextHolder.currentContextAttributes().setAttribute(MQConstants.MSG_HEADER_USER_NAME, "admin");
		RuntimeContextHolder.currentContextAttributes().setAttribute(MQConstants.MSG_HEADER_PASS_WORD, "9999999901");
	}

	/**
	 * 新增品牌
	 */
	@Test
	public void testaddBrand(){
		productMappingErrorManager.addBrand("333","33432", "C&K Components");
	}
	
	public void testbatchUpdate(){
		List<String> list = Lists.newArrayList();
		list.add("");
		productMappingErrorManager.batchUpdate(list, "admin");
	}
	
	/**
	 * 新增分类
	 */
	@Test
	public void testaddCategory(){
		List<ProductCategory> categories = new ArrayList<>();
		ProductCategory category = new ProductCategory();
		category.setName("开发板");
		category.setStatus(1);
		category.setLevel(1);
		categories.add(category);
		
		category = new ProductCategory();
		category.setName("评估板配件");
		category.setStatus(1);
		category.setLevel(2);
		categories.add(category);
		
		category = new ProductCategory();
		category.setName("评估板其他配件");
		category.setStatus(1);
		category.setLevel(3);
		categories.add(category);
		productMappingErrorManager.addCategory("wojiushi", "sdaf",categories);
		
	}
	
}
