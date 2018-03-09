///*
// * Created: 2017年7月7日
// *
// * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
// * Copyright (c) 2015-2017 
// * License, Version 1.0. Published by Yikuyi IT department.
// *
// * For the convenience of communicating and reusing of codes,
// * any java names, variables as well as comments should be written according to the regulations strictly.
// */
//package com.yikuyi.product.material.bll;
//import static  org.junit.Assert.assertEquals;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.TestExecutionListeners;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
//import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
//import com.yikuyi.brand.model.ProductBrand;
//
//
//
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, 
//	DirtiesContextTestExecutionListener.class,TransactionDbUnitTestExecutionListener.class})
//@Transactional(propagation=Propagation.REQUIRES_NEW)
//@Rollback
//public class ManufactureTransferManagerTest {
//
//	@Autowired
//	ManufactureTransferManager transferManager;
//	
//	@Test
//	public void testTransfer() {
//		transferManager.transfer();
//	}
//	
//	@Test
//	public void testIsNeedTransfer(){
//		transferManager.isNeedTransfer(null, null);
//	}
//	
//	@Test
//	public void testMergeBrandAlias(){
//		ProductBrand oldBrand = new ProductBrand();
//		List<String> brandAlias = new ArrayList<>();
//		brandAlias.add("aaa");
//		brandAlias.add("bbb");
//		oldBrand.setBrandAlias(brandAlias);
//		
//		ProductBrand newBrand = new ProductBrand();
//		List<String> newBrandAlias = new ArrayList<>();
//		newBrandAlias.add("aaa");
//		newBrandAlias.add("ccc");
//		newBrand.setBrandAlias(newBrandAlias);
//		
//		ProductBrand result = transferManager.mergeBrandAlias(oldBrand, newBrand);
//		List<String> resultBrandAlias = result.getBrandAlias();
//		assertEquals("aaa", resultBrandAlias.get(0));
//		assertEquals("ccc", resultBrandAlias.get(1));
//		assertEquals("bbb", resultBrandAlias.get(2));
//		
//		
//	}
//
//}
