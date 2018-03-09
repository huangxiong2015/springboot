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

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.yikuyi.material.vo.MaterialVo;
import com.yikuyi.product.base.ProductApplicationTestBase;

@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
	TransactionDbUnitTestExecutionListener.class })
@Transactional
@Rollback
public class ParseImportFileManagerTest extends ProductApplicationTestBase {

	@Autowired
	ParseImportFileManager parseImportFileManager;
	
	@Test
	public void basicMaterialParseImportFileTest(){
		MaterialVo materialVo = new MaterialVo();
		parseImportFileManager.basicMaterialParseImportFile(materialVo);
	}
	
	@Test
	public void importsParseImportFileTest(){
		MaterialVo materialVo = new MaterialVo();
		parseImportFileManager.importsParseImportFile(materialVo);
	}
}
