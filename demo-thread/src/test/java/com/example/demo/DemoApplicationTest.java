package com.example.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.wanhui.CrawlerApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CrawlerApplication.class)
public class DemoApplicationTest {
	
	@Autowired
	private RestTemplate restTemplate;
	private Logger logger = LoggerFactory.getLogger(DemoApplicationTest.class);
//https://partner.element14.com/docs/Product_Search_API_REST__Description
	//https://partner.element14.com/io-docs
//api key : ycb4mnn74g5qpfze2jnu5bbn
	@Test
	public void testDemo() {
		String url = "https://api.element14.com/catalog/products?callInfo.responseDataFormat={callInfo.responseDataFormat}&"
				+ "callInfo.omitXmlSchema={callInfo.omitXmlSchema}&term={term}&storeInfo.id={storeInfo.id}&"
				+ "callInfo.apiKey={callInfo.apiKey}&resultsSettings.offset={resultsSettings.offset}&"
				+ "resultsSettings.numberOfResults={resultsSettings.numberOfResults}&resultsSettings.responseGroup={resultsSettings.responseGroup}";
//		String url = "http://api.element14.com/catalog/products?term=any:fuse&storeInfo.id=uk.farnell.com&resultsSettings.offset=0&resultsSettings.numberOfResults=10&resultsSettings.refinements.filters=&resultsSettings.responseGroup=small&callInfo.omitXmlSchema=false&callInfo.callback=&callInfo.responseDataFormat=json&callinfo.apiKey=ycb4mnn74g5qpfze2jnu5bbn";
//		
//		String url="http://192.168.1.110:27082/v1/account/id?account=admin";
		
//		String url = "https://api.element14.com/catalog/products?callInfo.responseDataFormat="+callInfo.responseDataFormat+"&"
//				+ "callInfo.omitXmlSchema={callInfo.omitXmlSchema}&term={term}&storeInfo.id={storeInfo.id}&"
//				+ "callInfo.apiKey={callInfo.apiKey}&resultsSettings.offset={resultsSettings.offset}&"
//				+ "resultsSettings.numberOfResults={resultsSettings.numberOfResults}&resultsSettings.responseGroup={resultsSettings.responseGroup}";
		Map<String,Object> parameterMap = new HashMap<>();
		parameterMap.put("callInfo.responseDataFormat", "json");// 选填  返回数据类型：XML|JSON  
		parameterMap.put("callInfo.omitXmlSchema", false);//省略xml模式
//		parameterMap.put("callInfo.callback", "");//
		parameterMap.put("term", "any:fuse");//必填 期限
		parameterMap.put("storeInfo.id", "uk.farnell.com");//必填  仓库Id
		parameterMap.put("callInfo.apiKey", "ycb4mnn74g5qpfze2jnu5bbn");//必填  key  ycb4mnn74g5qpfze2jnu5bbn
//		parameterMap.put("userInfo.signature", "");//选填 签名
//		parameterMap.put("userInfo.timestamp", "");//选填
//		parameterMap.put("userInfo.customerId", "");//选填
		parameterMap.put("resultsSettings.offset", 0);//必填
		parameterMap.put("resultsSettings.numberOfResults", 10);//必填
//		parameterMap.put("resultsSettings.refinements.filters", "");//选填
		parameterMap.put("resultsSettings.responseGroup", "medium");//选填
		String result="";
//		 TestRestTemplate restTemplate = new TestRestTemplate();
		try {
			result = restTemplate.getForObject(url, String.class,parameterMap);
//			 HttpEntity<String> entity = new HttpEntity<>(null, getHttpHeaders());
//			 ResponseEntity<String> responseEntity = restTemplate.exchange(
//			            url, HttpMethod.GET, entity,
//			            String.class);
//			 responseEntity.getBody();
		} catch (Exception e) {
			logger.error("respone error:",e);
		}
		
		
		
		
		logger.info("返回结果："+result);
	}
	
	/**
	 * 获取HttpHeaders
	 * @return
	 * @since 
	 * @author 
	 */
	private HttpHeaders getHttpHeaders(){
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(type);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		return headers ;
	}
	
	public static void main(String[] args) {
		Optional<String> name = Optional.ofNullable("232");
		name.ifPresent((value) -> System.out.println(value));
		System.out.println(name.orElse("is me"));
		
		Optional<String> upperName = name.map((value) -> value.toUpperCase());
		System.out.println(upperName.orElse("No value found"));
		
		upperName = name.flatMap((value) -> Optional.of(value.toUpperCase()));
		System.out.println(upperName.orElse("No value found"));//输出SANAULLA
		
		Optional<String> longName = name.filter((value) -> value.length() > 2);
		System.out.println(longName.orElse("The name is less than 6 characters"));
		
	}

}
