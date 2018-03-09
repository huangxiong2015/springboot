/*
 * Created: 2017年3月30日
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
import com.yikuyi.material.vo.MaterialVo;
import com.yikuyi.product.document.bll.SyncElasticsearchProductManager;
import com.yikuyi.product.vo.ProductVo;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class })
@Transactional(propagation = Propagation.REQUIRES_NEW)
@Rollback
public class SyncElasticsearchProductManagerTest {
	
	@Autowired
	private SyncElasticsearchProductManager syncElasticsearchProductManager;
	@Test
	public void testSyncElasticsearchProduct(){
		MaterialVo materialVo = new MaterialVo();
		//materialVo.setMsg("545454jijiji");
		syncElasticsearchProductManager.syncElasticsearchProduct(materialVo);
	}
	
	@Test
	public void syncElasticsearchProductUpdate(){
		MaterialVo materialVo = new MaterialVo();
		materialVo.setMsg(new ArrayList<ProductVo>());
		syncElasticsearchProductManager.syncElasticsearchProductUpdate(materialVo);
	}
}
