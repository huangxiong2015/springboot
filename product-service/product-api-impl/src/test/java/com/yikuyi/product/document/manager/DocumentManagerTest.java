/*
 * Created: 2017年1月18日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.document.manager;

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

import com.github.pagehelper.PageInfo;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.yikuyi.document.model.Document;
import com.yikuyi.document.vo.DocumentVo;
import com.yikuyi.product.document.bll.DocumentManager;
@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
	TransactionDbUnitTestExecutionListener.class })
@Transactional
@Rollback
public class DocumentManagerTest {
		
	@Autowired
	private DocumentManager documentManager;
		
	/**
	 * 上传历史记录
	 * 
	 * @since 2017年2月24日
	 * @author zr.wujiajun@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value =  "classpath:com/yikuyi/product/document/logHistory.xml" )
	public void testLogHistory(){
		PageInfo<DocumentVo> result = documentManager.findByType(Document.DocumentType.VENDOR_SPU_DOCUMENT,1, 2);
		Assert.assertEquals(2, result.getList().size());
		Assert.assertEquals(1, result.getPageNum());
		Assert.assertEquals(2, result.getTotal());
		DocumentVo  doc1 = result.getList().get(0);
		Assert.assertEquals("上传第一个模板teste3.xlsx", doc1.getDocumentName());
		Assert.assertEquals("VENDOR_SPU_DOCUMENT", doc1.getTypeId().toString());
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value =  "classpath:com/yikuyi/product/document/logHistoryDownload.xml" )
	public void testLogHistoryDownload(){
		documentManager.logHistoryDownload("821572504538578944", System.out);
		documentManager.logHistoryDownload("785512121212121217", System.out);
	}
	
}
