/*
 * Created: 2017年5月9日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.document.bll;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;

import com.yikuyi.product.base.ProductApplicationTestBase;
import com.yikuyi.product.goods.dao.ProductRepository;
import com.yikuyi.product.model.Product;

/**
 * 
 * @author zr.wanghong
 * @version 1.0.0
 * @since 2017年5月9日
 * @see com.yikuyi.product.document.bll.SynProductPropertiesManager
 */
public class SynProductPropertiesManagerTest extends ProductApplicationTestBase{

	 @Autowired  
     private SynProductPropertiesManager spuSkuManager;  
	 
	 @Autowired
	 private ProductRepository productRepository;
	 
	@Test
	public void testSynProductProperties() {
		String processId = UUID.randomUUID().toString();
		spuSkuManager.synProductProperties(processId);
	}

	
	@Test
	public void testFindProductByprocessId(){
		String lastId = StringUtils.EMPTY;
		int processedNum = 0;
		while (true) {
			String processId = "20170510";
			PageRequest pageRequest = new PageRequest(0,1000);
			System.out.println("last id is :"+ lastId);
			List<Product> products = productRepository.findByProcessId(processId,lastId,pageRequest);
			if(CollectionUtils.isEmpty(products)){
				break;
			}
			Product lastProduct = products.get(products.size()-1);
			lastId = lastProduct.getId();
			processedNum += products.size();
			System.out.println("processedNum size is :"+processedNum);
		}
		//System.out.println("list size is :"+products.size());
	}
}
