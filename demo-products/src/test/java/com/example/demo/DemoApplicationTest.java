package com.example.demo;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.wanhui.DemoApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class DemoApplicationTest {
	
	@Autowired
	private RestTemplate restTemplate;
	private Logger logger = LoggerFactory.getLogger(DemoApplicationTest.class);
//https://partner.element14.com/docs/Product_Search_API_REST__Description
//api key : ycb4mnn74g5qpfze2jnu5bbn
	@Test
	public void testDemo() {
		String url = "https://api.element14.com/catalog/products?callInfo.responseDataFormat={callInfo.responseDataFormat}&"
				+ "callInfo.omitXmlSchema={callInfo.omitXmlSchema}&term={term}&storeInfo.id={storeInfo.id}&"
				+ "callInfo.apiKey={callInfo.apiKey}&resultsSettings.offset={resultsSettings.offset}&"
				+ "resultsSettings.numberOfResults={resultsSettings.numberOfResults}&resultsSettings.responseGroup={resultsSettings.responseGroup}";
		Map<String,Object> parameterMap = new HashMap<>();
		parameterMap.put("callInfo.responseDataFormat", "json");// 选填  返回数据类型：XML|JSON  
		parameterMap.put("callInfo.omitXmlSchema", false);//省略xml模式
		parameterMap.put("callInfo.callback", null);//
		parameterMap.put("term", "any%3Afuse");//必填 期限
		parameterMap.put("storeInfo.id", "uk.farnell.com");//必填  仓库Id
		parameterMap.put("callInfo.apiKey", "gd8n8b2kxqw6jq5mutsbrvur");//必填  key  ycb4mnn74g5qpfze2jnu5bbn
//		parameterMap.put("userInfo.signature", "");//选填 签名
//		parameterMap.put("userInfo.timestamp", "");//选填
//		parameterMap.put("userInfo.customerId", "");//选填
		parameterMap.put("resultsSettings.offset", 0);//必填
		parameterMap.put("resultsSettings.numberOfResults", 10);//必填
		parameterMap.put("resultsSettings.refinements.filters", null);//选填
		parameterMap.put("resultsSettings.responseGroup", "medium");//选填
		String result="";
		try {
			result = restTemplate.getForObject(url, String.class, parameterMap);
		} catch (Exception e) {
			logger.error("respone error:",e);
		}
		
		logger.info("返回结果："+result);
	}

}
