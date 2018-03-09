package com.yikuyi.product.goods.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONObject;
import com.yikuyi.product.ProductApplication;
import com.yikuyi.product.ProductClientBuilder;

/**
 * @see com.yikuyi.product.goods.impl.SingleDetailCrawlerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = { ProductApplication.class })
public class SingleDetailCrawlerResourceTest {

	@SpyBean
	private ProductClientBuilder productClientBuilder;
	
	@Autowired
	private TestRestTemplate restTemplate; // = new TestRestTemplate();	
	
	@Test
	public void testCrawlerSimgleDetial() {
		String param = "https://www.digikey.com/product-detail/en/amphenol-commercial-products/MP-6ARJ45SNNG-007/MP-6ARJ45SNNG-007-ND/6052324";
		String url = "/v1/crawler/detail?param="+param;
		ResponseEntity<JSONObject> response = restTemplate.exchange(url, HttpMethod.GET,null,new ParameterizedTypeReference<JSONObject>(){});
		JSONObject result = response.getBody();
		System.out.println(result.get("message"));
	}
}
