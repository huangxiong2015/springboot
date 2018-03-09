/*
 * Created: 2016年2月4日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ykyframework.test.context.junit4;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ykyframework.test.context.SpringActiveProfileResolver;

/**
 * 基本的spring test测试基类，注入了profiles的属性
 * @author liaoke@yikuyi.com
 * @version 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(resolver = SpringActiveProfileResolver.class)
public class BaseSpringTest {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

}
