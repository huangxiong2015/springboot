/*
 * Created: 2016年12月20日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
/**
 * 
 */
package com.yikuyi.party.base;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.yikuyi.party.PartyApplication;

/**
 * 单元测试基类
 * @author zr.wanghong
 * @version 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT ,classes = {PartyApplication.class, ApplicationTestConfig.class})
public class PartyApplicationTestBase {
				
	@LocalServerPort
	private String port;
	
	/**
	 * @return the port
	 */
	public final String getPort() {
		return port;
	}

	@Autowired
	private TestRestTemplate restTemplate; 	
	

	
	@Autowired
	private RestTemplate restTemplateMock;
	
	/**
	 * @return the restTemplate
	 */
	public final TestRestTemplate getRestTemplate() {
		return restTemplate;
	}

	/**
	 * @param restTemplate the restTemplate to set
	 */
	public final void setRestTemplate(TestRestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}	
		
	public MockRestServiceServer mockRestServiceServer() {
		return MockRestServiceServer.bindTo(restTemplateMock).build();
	}	
}


