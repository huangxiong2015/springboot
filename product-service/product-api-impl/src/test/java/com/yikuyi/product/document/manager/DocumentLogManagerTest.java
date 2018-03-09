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

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.yikuyi.document.model.Document.DocumentStatus;
import com.yikuyi.product.document.bll.DocumentLogManager;
import com.yikuyi.product.vo.RawData;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class })
@Transactional(propagation=Propagation.NOT_SUPPORTED)
@Rollback
public class DocumentLogManagerTest {

	@Autowired
	private DocumentLogManager documentLogManager;

	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "classpath:com/yikuyi/product/document/logQuery1.xml")
	public void test1findDocStatusByDocId() {
		Assert.assertEquals(DocumentStatus.DOC_PRO_SUCCESS, documentLogManager.findDocStatusByDocId("821572504538578944"));
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "classpath:com/yikuyi/product/document/logQuery2.xml")
	public void test2findDocStatusByDocId() {
		Assert.assertEquals(DocumentStatus.DOC_PRO_FAILED, documentLogManager.findDocStatusByDocId("821572504538578944"));
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "classpath:com/yikuyi/product/document/logQuery3.xml")
	public void test3findDocStatusByDocId() {
		Assert.assertEquals(DocumentStatus.DOC_PRO_PART_SUCCESS, documentLogManager.findDocStatusByDocId("821572504538578944"));
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "classpath:com/yikuyi/product/document/logQuery4.xml")
	public void test4findDocStatusByDocId() {
		Assert.assertEquals(DocumentStatus.DOC_PRO_FAILED, documentLogManager.findDocStatusByDocId("821572504538578944"));
		Assert.assertEquals(DocumentStatus.DOC_PRO_FAILED, documentLogManager.findDocStatusByDocId("821572504538578943"));
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:com/yikuyi/product/document/logOldUpdate.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "classpath:com/yikuyi/product/document/logOldUpdate_result.xml")
	public void testupdateDocLogsStatusByRaw() {
		List<RawData> list = new ArrayList<>();
		RawData r1 = new RawData();
		r1.setProcessId("8215725045385789442");
		r1.setLineNo(2L);
		r1.setErrorMsg("test2");
		list.add(r1);
		
		RawData r2 = new RawData();
		r2.setProcessId("8215725045385789442");
		r2.setLineNo(3L);
		r2.setErrorMsg("test3");
		list.add(r2);
		documentLogManager.updateDocLogsStatusByRaw(list);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:com/yikuyi/product/document/logNewUpdate.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "classpath:com/yikuyi/product/document/logNewUpdate_result.xml")
	public void testupdateDocLogsStatusByRawAndSheet() {
		List<RawData> list = new ArrayList<>();
		RawData r1 = new RawData();
		r1.setProcessId("8215725045385789442");
		r1.setSheetIndex(0);
		r1.setLineNo(2L);
		r1.setErrorMsg("test0");
		list.add(r1);
		
		RawData r2 = new RawData();
		r2.setProcessId("8215725045385789442");
		r2.setSheetIndex(1);
		r2.setLineNo(2L);
		r2.setErrorMsg("test1");
		list.add(r2);
		
		RawData r3 = new RawData();
		r3.setProcessId("8215725045385789442");
		r3.setSheetIndex(2);
		r3.setLineNo(2L);
		r3.setErrorMsg("test2");
		list.add(r3);
		documentLogManager.updateDocLogsStatusByRawAndSheet(list);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:com/yikuyi/product/document/logOldUpdate.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "classpath:com/yikuyi/product/document/logOldUpdate_result.xml")
	public void testupdateDocLogsStatusByDoc() {
		List<Document> list = new ArrayList<>();
		Document r1 = new Document();
		r1.append("processId", "8215725045385789442");
		r1.append("lineNo", "2");
		r1.append("errorMsg", "test2");
		list.add(r1);
		
		Document r2 = new Document();
		r2.append("processId", "8215725045385789442");
		r2.append("lineNo", "3");
		r2.append("errorMsg", "test3");
		list.add(r2);
		documentLogManager.updateDocLogsStatusByDoc(list);
	}
}
