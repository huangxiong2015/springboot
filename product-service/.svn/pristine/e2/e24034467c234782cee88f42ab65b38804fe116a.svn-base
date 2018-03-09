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
package com.yikuyi.product.base;

import java.util.Base64;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yikuyi.product.ProductApplication;

/**
 * 单元测试基类
 * @author zr.wanghong
 * @version 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT ,classes = {ProductApplication.class, ApplicationTestConfig.class})
public class ProductApplicationTestBase {
				
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
	
	@Test
	public void test(){
		
	}
	
	
	//请求验证用户的用户名
	String userCode = Base64.getUrlEncoder().encodeToString("admin".getBytes());
	//验证用户所返回的用户id
	String userId = "9999999901";
	
	public ObjectMapper objectMapper = new ObjectMapper() ;
	
	//party服务地址
	@Value("${api.party.serverUrlPrefix}")
	private String partyUrl;
	
	
	public String getPartyUrl() {
		return partyUrl;
	}

	/**
	 * mock调用party服务，需要登录的服务都需要调用一次此方法
	 * @since 2017年4月5日
	 * @author zr.wanghong
	 */
	public void mockPartyService(){
		MockRestServiceServer server = this.mockRestServiceServer();
		//创建pary 的模拟rest服务，设定其响应url以及返回(参照业务实际要调用的其他应用的服务进行设计，返回该测试用例预期验证的结果）
		server.expect(MockRestRequestMatchers.requestTo(partyUrl.concat("/v1/users/validated/").concat(userCode)))
				.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
				.andRespond(MockRestResponseCreators.withSuccess(userId, MediaType.TEXT_PLAIN));
	}
}


