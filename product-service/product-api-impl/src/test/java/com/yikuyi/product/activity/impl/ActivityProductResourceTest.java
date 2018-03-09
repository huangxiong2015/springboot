/*
 * Created: 2017年10月17日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.activity.impl;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import com.yikuyi.activity.model.ActivityProductDraft;
import com.yikuyi.product.base.ProductApplicationTestBase;

public class ActivityProductResourceTest extends ProductApplicationTestBase{
	@Autowired
	private TestRestTemplate restTemplate;
	
	private String host;
	
	@Before
	public void setUpBefore() throws Exception {
		this.host = "http://localhost" + ":" + this.getPort();
	}
	
	
	/**
	 * 根据活动商品id查询活动商品信息
	 * @since 2017年11月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void testGetProductById() throws Exception{
		String activityProductId = "888212256410566656";
		this.mockPartyService();
		restTemplate.exchange(host+"/v1/activityproducts/"+activityProductId, HttpMethod.GET, null, new ParameterizedTypeReference<ActivityProductDraft>(){});	
	}
	
	/**
	 * 编辑商品信息
	 * @since 2017年11月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void testEditProductInfo() throws Exception{
		String activityProductId = "888212256410566656";
		ActivityProductDraft draft = new ActivityProductDraft();
		draft.setActivityProductId("888212256410566656");
		draft.setManufacturer("DDTM");
		draft.setManufacturerPartNumber("CD143A-SR05LC");
		draft.setCategory3Name("板对板连接器 - 插座、母插口");
		draft.setCategory3Id("398");
		this.mockPartyService();
		restTemplate.put(host+"/v1/activityproducts/"+activityProductId, ActivityProductDraft.class);
	}
	
	/**
	 * 保存商品历史记录
	 * @throws Exception
	 * @since 2017年11月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void testSaveActivitiesProductsHistory() throws Exception{
		this.mockPartyService();
		restTemplate.put(host+"/v1/activityproducts/history", String.class);
	}
	
}
