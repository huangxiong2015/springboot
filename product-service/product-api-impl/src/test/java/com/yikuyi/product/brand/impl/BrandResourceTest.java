/*
 * Created: 2017年2月27日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.brand.impl;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import com.yikuyi.brand.model.ProductBrand;
import com.yikuyi.product.base.ProductApplicationTestBase;

/**
 * @see com.yikuyi.product.brand.impl.BrandResource
 * @author zr.wanghong
 * @version 1.0.0
 * @since 2017年2月27日
 */
public class BrandResourceTest extends ProductApplicationTestBase{
	
	@Autowired
	private TestRestTemplate restTemplate; 
	
	private String host;
	
	@Before
	public void setUpBefore() throws Exception {
		this.host = "http://localhost" + ":" + this.getPort();
	}
	
	@Test
	public void testFindById() {
		this.mockPartyService();
		Integer id = 999999;
//		productClientBuilder.brandResource().findById(id);
		restTemplate.exchange(host + "/v1/products/brands/"+id, HttpMethod.GET, null, new ParameterizedTypeReference<ProductBrand>(){});
	}
	
	@Test
	public void testFindAll() {
		this.mockPartyService();
//		productClientBuilder.brandResource().findAll();
		restTemplate.exchange(host + "/v1/products/brands/", HttpMethod.GET, null, new ParameterizedTypeReference<List<ProductBrand>>(){});
	}
}
