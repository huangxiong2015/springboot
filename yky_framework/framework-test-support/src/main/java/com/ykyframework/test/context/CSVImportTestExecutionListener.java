/*
 * Created: 2015年12月21日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2015 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ykyframework.test.context;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class CSVImportTestExecutionListener extends AbstractTestExecutionListener {
	
	@Override
	public void beforeTestMethod(TestContext testContext) throws Exception {
		//System.out.println("before test method.");
		//1. getAnnotation
		//2. 获取csv的数据
	}
	
	@Override
	public void afterTestMethod(TestContext testContext) throws Exception {
		//System.out.println("after test method.");
	}

}
