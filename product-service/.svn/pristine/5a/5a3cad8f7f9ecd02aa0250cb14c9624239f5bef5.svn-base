/*
 * Created: 2016年12月9日
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
package com.yikuyi.product.goods.impl;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.yikuyi.basedata.ShipmentClientBuilder;
import com.yikuyi.basedata.resource.UomConversionClient;
import com.yikuyi.category.model.ProductCategory;
import com.yikuyi.party.PartyClientBuilder;
import com.yikuyi.product.base.ProductApplicationTestBase;
import com.yikuyi.product.model.Product;
import com.yikuyi.product.model.ProductPrice;
import com.yikuyi.product.model.ProductPriceLevel;
import com.yikuyi.product.model.ProductStand;
import com.yikuyi.product.model.VendorSeries;
import com.yikuyi.product.rule.price.manager.ProductPriceRuleManager;
import com.yikuyi.rule.price.PriceInfo;
import com.yikuyi.rule.price.ProductPriceRule.RuleStatus;
import com.yikuyi.rule.price.model.PriceRuleDetail;
import com.yikuyi.rule.price.model.PriceRuleTemplate;
import com.yikuyi.rule.price.model.PriceRuleTemplateCache;

/**
 * @author zr.wanghong
 * @version 1.0.0
 * @since 2016-12-21
 * @see com.yikuyi.product.rule.price.impl.PriceRulesResource
 * @see com.yikuyi.product.goods.manager.PriceQueryManager
 * @see com.yikuyi.product.rule.price.manager.ProductPriceRuleManager
 */
public class PriceQueryResouceTest extends ProductApplicationTestBase{
	
	@Autowired
	private TestRestTemplate restTemplate; // = new TestRestTemplate();	
	
	private String host;
	
	@Autowired
	private MongoRepository<Product, String> productRepository;
	
	@Autowired
	private CacheManager cacheManager;
	
	@Autowired
	private ProductPriceRuleManager productPriceRuleManager;
	
	@SpyBean
	public PartyClientBuilder partyClientBuilder;
	
	@SpyBean
	public ShipmentClientBuilder shipmentClientBuilder;
	
	/**
	 * @throws java.lang.Exception
	 * @since 2016年12月9日
	 * @author liudian@yikuyi.com
	 */
	@Before
	public void setUpBefore() throws Exception {
		this.host = "http://localhost" + ":" + this.getPort();
	}
	
	/**
	 * 初始化设置汇率
	 * @return
	 * @since 2017年2月28日
	 * @author zr.wanghong
	 */
	public boolean initExchangeRate(){
		JSONObject json = new JSONObject();
		json.put("uomId", "USD");
		json.put("uomIdTo", "CNY");
		json.put("conversionFactor", 0.67);
		json.put("createdDate", "2016-01-01 08:00:00");
		json.put("creator", "9999999901");
		json.put("lastUpdateDate", "2016-01-01 08:00:00");
		json.put("lastUpdateUser", "9999999901");
		json.put("thruDate", "");
		HttpEntity<JSONObject> entity = new HttpEntity<>(json);
		String host = "http://localhost:27081";
		ResponseEntity<JSONObject> response = restTemplate.exchange(host + "/v1/basedata/exchangerate", HttpMethod.POST, entity, new ParameterizedTypeReference<JSONObject>(){});
		JSONObject result = response.getBody();
		if(result != null ){
			System.out.println(String.format("初始化设置汇率成功,汇率:[%s]", result.toJSONString()));
			return true;
		}else{
			System.out.println("汇率设置失败!");
			return false;
		}
		
	}
	
	/**
	 * 测试有梯度价格的商品,币种类型为USD,并启用了定价模板
	 * 输入参数：商品ID
	 * 期望结果：返回按定价模板计算后的商品价格梯度
	 * @throws JsonProcessingException 
	 */
//	@Test
//	public void testQueryPriceWithTemplate() throws JsonProcessingException {
//		//HttpHeaders header = new HttpHeaders();
//		//header.add("Authorization", "Basic YWRtaW46OTk5OTk5OTkwMQ==");
//		
//		//初始化规则模板，并启用模板
//		String ruleId = this.initPriceRuleAciton();
//		
//		//初始化商品数据
//		Product product = this.initProductData();
//		
//		this.mockOtherService();
//		
//		List<String> ids = new ArrayList<String>();
//		ids.add(product.getId());
//		
//		System.out.println("\n调用价格查询服务---"+host + "/v1/products/batch/price"+",请求参数：ids="+ids);
//		HttpEntity<List<String>> entity = new HttpEntity<List<String>>(ids);
//		ResponseEntity<List<PriceInfo>> response = restTemplate.exchange(host + "/v1/products/batch/price", HttpMethod.POST, entity, new ParameterizedTypeReference<List<PriceInfo>>(){});
//		List<PriceInfo> priceInfos =  response.getBody();
//		assertEquals(1, priceInfos.size());
//		
//		//断言成本价
//		//香港价
//		//assertEquals("330.55128" , priceInfos.get(0).getCosePrices().get(1).getUnitPrice());
//		//国内价
//		//assertEquals("0.23831" , priceInfos.get(0).getCosePrices().get(0).getUnitPrice());
//		
//		//断言销售价
//		//香港价
////		assertEquals("334.84416" , priceInfos.get(0).getResalePrices().get(1).getUnitPrice());
//		//国内价
////		assertEquals("161.69035" , priceInfos.get(0).getResalePrices().get(0).getUnitPrice());
//		
//		//断言特价
//		//香港价
//		//assertEquals("258.06000" , priceInfos.get(0).getSpecialResaleprices().get(1).getUnitPrice());
//		//国内价
//		//assertEquals("0.30089" , priceInfos.get(0).getSpecialResaleprices().get(0).getUnitPrice());
//	
//		//禁用模板
//		this.disabledTemplate(ruleId);
//		
//		//删除模板测试数据
//		clearTemplateData(ruleId);
//	}

	/**
	 * 测试没有梯度价格的商品
	 * 输入参数：商品ID
	 * 期望结果：商品价格梯度不计算
	 * @throws JsonProcessingException 
	 */
	@Test
	public void testQueryPriceNotLevel() throws JsonProcessingException {
		//HttpHeaders header = new HttpHeaders();
		//header.add("Authorization", "Basic YWRtaW46OTk5OTk5OTkwMQ==");
		
		Product product = this.initDataNotPriceLevel();
		
		this.mockOtherService();
		
		List<String> ids = new ArrayList<String>();
		ids.add(product.getId());
		HttpEntity<List<String>> entity = new HttpEntity<List<String>>(ids);
		ResponseEntity<List<PriceInfo>> response = restTemplate.exchange(host + "/v1/products/batch/price", HttpMethod.POST, entity, new ParameterizedTypeReference<List<PriceInfo>>(){});
		List<PriceInfo> priceInfos =  response.getBody();
		//System.out.println("-----------------------" + priceInfos.get(0).getCosePrices().get(0).getUnitPrice());
		assertEquals(1, priceInfos.size());
	}
	
	/**
	 * 测试有梯度价格，但价格为Null的商品
	 * 输入参数：商品ID
	 * 期望结果：商品价格梯度不计算
	 * @throws JsonProcessingException 
	 */
	@Test
	public void testQueryPriceWithPriceNull() throws JsonProcessingException {
		//HttpHeaders header = new HttpHeaders();
		//header.add("Authorization", "Basic YWRtaW46OTk5OTk5OTkwMQ==");
		
		Product product = this.initDataWithPriceNull();
		
		this.mockOtherService();
		
		List<String> ids = new ArrayList<String>();
		ids.add(product.getId());
		HttpEntity<List<String>> entity = new HttpEntity<List<String>>(ids);
		ResponseEntity<List<PriceInfo>> response = restTemplate.exchange(host + "/v1/products/batch/price", HttpMethod.POST, entity, new ParameterizedTypeReference<List<PriceInfo>>(){});
		List<PriceInfo> priceInfos =  response.getBody();
		//System.out.println("-----------------------" + priceInfos.get(0).getCosePrices().get(0).getUnitPrice());
		assertEquals(1, priceInfos.size());
	}
	
	/**
	 * 测试有梯度价格，但价格为空字符串的商品
	 * 输入参数：商品ID
	 * 期望结果：商品价格梯度不计算
	 * @throws JsonProcessingException 
	 */
	@Test
	public void testQueryPriceWithPriceNullStr() throws JsonProcessingException {
		//HttpHeaders header = new HttpHeaders();
		//header.add("Authorization", "Basic YWRtaW46OTk5OTk5OTkwMQ==");
		
		Product product = this.initDataWithPriceNullStr();
		
		this.mockOtherService();
		
		List<String> ids = new ArrayList<String>();
		ids.add(product.getId());
		HttpEntity<List<String>> entity = new HttpEntity<List<String>>(ids);
		ResponseEntity<List<PriceInfo>> response = restTemplate.exchange(host + "/v1/products/batch/price", HttpMethod.POST, entity, new ParameterizedTypeReference<List<PriceInfo>>(){});
		List<PriceInfo> priceInfos =  response.getBody();
		//System.out.println("-----------------------" + priceInfos.get(0).getCosePrices().get(0).getUnitPrice());
		assertEquals(1, priceInfos.size());
	}
	
	
	
	@Autowired
	private RestTemplate restTemplateMock;
	
	public MockRestServiceServer mockRestServiceServer() {
		return MockRestServiceServer.bindTo(restTemplateMock).build();
	}	
	
	@Value("${api.basedata.serverUrlPrefix}")
	private String basedataServerUrlPrefix;
	
	//party服务地址
	@Value("${api.party.serverUrlPrefix}")
	private String partyUrl;
	
	 ObjectMapper objectMapper = new ObjectMapper(); //JSON
	
	/**
	 * 测试有梯度价格的商品,币种类型为USD,没有价格模板,并且不从缓存中取价格数据
	 * 输入参数：商品ID
	 * 期望结果：返回商品价格梯度
	 * @throws UnsupportedEncodingException 
	 * @throws JsonProcessingException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testQueryPriceNoRedisCache() throws UnsupportedEncodingException, JsonProcessingException {
		//HttpHeaders header = new HttpHeaders();
		//header.add("Authorization", "Basic YWRtaW46OTk5OTk5OTkwMQ==");
		
		Product product = this.initProductData();
		
		
		
		//清除价格缓存
		Cache productPriceCache = cacheManager.getCache("productPriceCache");
		productPriceCache.evict("ProductPriceCache-"+product.getId());
		
		//清楚规则模板缓存
		Cache cache = cacheManager.getCache("priceRuleTemplateCache");
		ValueWrapper valueWrapper = cache.get("PriceRuleTemplateCacheMap");
		Map<String, PriceRuleTemplateCache> priceRuleTemplateCacheMap = null;
	    if(valueWrapper != null){
	    	priceRuleTemplateCacheMap = (Map<String, PriceRuleTemplateCache>) cache.get("PriceRuleTemplateCacheMap").get();
	    	if(priceRuleTemplateCacheMap != null)
	    	priceRuleTemplateCacheMap.remove("digikey-digikey-100-69.0-700-711-714-USD");
	    }
		cache.evict("PriceRuleTemplateCacheMap");
		cache.put("PriceRuleTemplateCacheMap", priceRuleTemplateCacheMap);
		
		this.mockOtherService();
		
		List<String> ids = new ArrayList<String>();
		ids.add(product.getId());
		HttpEntity<List<String>> entity = new HttpEntity<List<String>>(ids);
		ResponseEntity<List<PriceInfo>> response = restTemplate.exchange(host + "/v1/products/batch/price", HttpMethod.POST, entity, new ParameterizedTypeReference<List<PriceInfo>>(){});
		List<PriceInfo> priceInfos =  response.getBody();
		assertEquals(1, priceInfos.size());
		//断言成本价
		//香港价
		//assertEquals("257.83000" , priceInfos.get(0).getCosePrices().get(1).getUnitPrice());
		//国内价
		//assertEquals("0.30166" , priceInfos.get(0).getCosePrices().get(0).getUnitPrice());
		
		//断言销售价
		//香港价
		//assertEquals("257.83000" , priceInfos.get(0).getResalePrices().get(1).getUnitPrice());
		//国内价
		//assertEquals("202.11294" , priceInfos.get(0).getResalePrices().get(0).getUnitPrice());
		
		//断言特价
		//香港价
		//assertEquals("257.83000" , priceInfos.get(0).getSpecialResaleprices().get(1).getUnitPrice());
		//国内价
		//assertEquals("0.30166" , priceInfos.get(0).getSpecialResaleprices().get(0).getUnitPrice());
	}

	private void mockOtherService() throws JsonProcessingException {
		this.mockPartyService();
		//请求验证用户的用户名
		
		//验证用户所返回的用户id
		
		//创建pary 的模拟rest服务，设定其响应url以及返回(参照业务实际要调用的其他应用的服务进行设计，返回该测试用例预期验证的结果）
	/*	server.expect(MockRestRequestMatchers.requestTo(partyUrl.concat("/v1/users/validated/").concat(userCode)))
				.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
				.andRespond(MockRestResponseCreators.withSuccess(userId.getBytes(), MediaType.TEXT_PLAIN));*/
		
		//mock调用获取汇率的服务
		//创建basedata 的模拟rest服务，设定其响应url以及返回(参照业务实际要调用的其他应用的服务进行设计，返回该测试用例预期验证的结果）
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("taxRate", new BigDecimal("0.17"));
		resultMap.put("exchangeRate",new Double(0.67));
		
/*		String ratesJson = objectMapper.writeValueAsString(resultMap); 
		server.expect(MockRestRequestMatchers.requestTo(basedataServerUrlPrefix.concat("/v1/basedata/exchangerate/USD/CNY")))
				.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
				.andRespond(MockRestResponseCreators.withSuccess(ratesJson, MediaType.APPLICATION_JSON_UTF8));*/
		
		
		UomConversionClient uc = Mockito.spy(UomConversionClient.class);
		Mockito.when(shipmentClientBuilder.uomConversionResource()).thenReturn(uc);
		Mockito.when(uc.getById(Mockito.anyObject(),Mockito.anyObject())).thenReturn(resultMap);
	}
	
	/**
	 * 测试有梯度价格的商品,币种类型为USD,没有价格模板,取缓存价格数据
	 * 输入参数：商品ID
	 * 期望结果：返回商品价格梯度
	 * @throws JsonProcessingException 
	 */
	@Test
	public void testQueryPriceWithRedisCache() throws JsonProcessingException {
		//HttpHeaders header = new HttpHeaders();
		//header.add("Authorization", "Basic YWRtaW46OTk5OTk5OTkwMQ==");
		
		Product product = this.initProductData();
		
		this.mockOtherService();
		
		List<String> ids = new ArrayList<String>();
		ids.add(product.getId());
		HttpEntity<List<String>> entity = new HttpEntity<List<String>>(ids);
		ResponseEntity<List<PriceInfo>> response = restTemplate.exchange(host + "/v1/products/batch/price", HttpMethod.POST, entity, new ParameterizedTypeReference<List<PriceInfo>>(){});
		List<PriceInfo> priceInfos =  response.getBody();
		assertEquals(1, priceInfos.size());
		//assertEquals("257.83000" , priceInfos.get(0).getResalePrices().get(0).getUnitPrice());
		
		//断言成本价
		//香港价
		//assertEquals("257.83000" , priceInfos.get(0).getCosePrices().get(1).getUnitPrice());
		//国内价
		//assertEquals("0.30166" , priceInfos.get(0).getCosePrices().get(0).getUnitPrice());
		
		//断言销售价
		//香港价
		//assertEquals("257.83000" , priceInfos.get(0).getResalePrices().get(1).getUnitPrice());
		//国内价
		//assertEquals("202.11294" , priceInfos.get(0).getResalePrices().get(0).getUnitPrice());
		
		//断言特价
		//香港价
		//assertEquals("257.83000" , priceInfos.get(0).getSpecialResaleprices().get(1).getUnitPrice());
		//国内价
		//assertEquals("0.30166" , priceInfos.get(0).getSpecialResaleprices().get(0).getUnitPrice());
	}
	
	
	
	/**
	 * 调用删除模板服务
	 * @param ruleId
	 */
	private void clearTemplateData(String ruleId){
		MockRestServiceServer server = this.mockRestServiceServer();
		//请求验证用户的用户名
		String userCode = Base64.getUrlEncoder().encodeToString("admin".getBytes());
		//验证用户所返回的用户id
		String userId = "9999999901";
		//创建pary 的模拟rest服务，设定其响应url以及返回(参照业务实际要调用的其他应用的服务进行设计，返回该测试用例预期验证的结果）
		server.expect(MockRestRequestMatchers.requestTo(partyUrl.concat("/v1/users/validated/").concat(userCode)))
				.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
				.andRespond(MockRestResponseCreators.withSuccess(userId.getBytes(), MediaType.TEXT_PLAIN));
		
		
		ResponseEntity<PriceRuleTemplate> response = null;
		try {
			System.out.println("\n调用删除模板服务--(DELETE)"+host + "/v1/rules/price/"+ruleId);
			response = restTemplate.exchange(host + "/v1/rules/price/"+ruleId, HttpMethod.DELETE, null, PriceRuleTemplate.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.getBody();
	}
	
	/**
	 * 调用禁用模板服务
	 * @param ruleId
	 */
	private void disabledTemplate(String ruleId){
		
		MockRestServiceServer server = this.mockRestServiceServer();
		//请求验证用户的用户名
		String userCode = Base64.getUrlEncoder().encodeToString("admin".getBytes());
		//验证用户所返回的用户id
		String userId = "9999999901";
		//创建pary 的模拟rest服务，设定其响应url以及返回(参照业务实际要调用的其他应用的服务进行设计，返回该测试用例预期验证的结果）
		server.expect(MockRestRequestMatchers.requestTo(partyUrl.concat("/v1/users/validated/").concat(userCode)))
				.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
				.andRespond(MockRestResponseCreators.withSuccess(userId.getBytes(), MediaType.TEXT_PLAIN));
		
		ResponseEntity<PriceRuleTemplate> response = null;
		try {
			System.out.println("\n调用禁用模板服务--(PUT)"+host + "/v1/rules/price/"+ruleId+"/status?status="+RuleStatus.DISABLED);
			response = restTemplate.exchange(host + "/v1/rules/price/"+ruleId+"/status?status="+RuleStatus.DISABLED, HttpMethod.PUT, null, PriceRuleTemplate.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.getBody();
	}
	
	/**
	 * 初始化一个价格模板并启用模板
	 * @return
	 */
	private String initPriceRuleAciton(){
		PriceRuleTemplate priceRuleTemplate = new PriceRuleTemplate();
		
		//成本价   香港按百分比价格加价22%，国内按百分比降价21%
		PriceRuleDetail priceRuleDetil = new PriceRuleDetail();
		priceRuleDetil.setRuleActionName("REAL_COST");
		priceRuleDetil.setDeliveryPlace("USD");
		priceRuleDetil.setRuleType("PRICE_FLAT");
		priceRuleDetil.setCalculateType("+");
		priceRuleDetil.setCalculateValue("0.22");
		PriceRuleDetail priceRuleDetil_1 = new PriceRuleDetail();
		priceRuleDetil_1.setRuleActionName("REAL_COST");
		priceRuleDetil_1.setDeliveryPlace("CNY");
		priceRuleDetil_1.setRuleType("PRICE_FLAT");
		priceRuleDetil_1.setCalculateType("-");
		priceRuleDetil_1.setCalculateValue("0.21");
		
		//销售价  香港按百分比加价23%，国内按百分比降价20%
		PriceRuleDetail priceRuleDetil2 = new PriceRuleDetail();
		priceRuleDetil2.setRuleActionName("RESALE_PRICE");
		priceRuleDetil2.setDeliveryPlace("USD");
		priceRuleDetil2.setRuleType("PRICE_FLAT");
		priceRuleDetil2.setCalculateType("+");
		priceRuleDetil2.setCalculateValue("0.23");
		PriceRuleDetail priceRuleDetil2_2 = new PriceRuleDetail();
		priceRuleDetil2_2.setRuleActionName("RESALE_PRICE");
		priceRuleDetil2_2.setDeliveryPlace("CNY");
		priceRuleDetil2_2.setRuleType("PRICE_FLAT");
		priceRuleDetil2_2.setCalculateType("-");
		priceRuleDetil2_2.setCalculateValue("0.20");
		
		//特价 香港按固定值加价0.23美元，国内按固定值降价0.66美元
		PriceRuleDetail priceRuleDetil3 = new PriceRuleDetail();
		priceRuleDetil3.setRuleActionName("SPECIAL_RESALE_PRICE");
		priceRuleDetil3.setDeliveryPlace("USD");
		priceRuleDetil3.setRuleType("PRICE_POL");
		priceRuleDetil3.setCalculateType("+");
		priceRuleDetil3.setCalculateValue("0.23");
		PriceRuleDetail priceRuleDetil3_3 = new PriceRuleDetail();
		priceRuleDetil3_3.setRuleActionName("SPECIAL_RESALE_PRICE");
		priceRuleDetil3_3.setDeliveryPlace("CNY");
		priceRuleDetil3_3.setRuleType("PRICE_POL");
		priceRuleDetil3_3.setCalculateType("-");
		priceRuleDetil3_3.setCalculateValue("0.66");
		
		List<PriceRuleDetail> priceRules = new ArrayList<PriceRuleDetail>();
		priceRules.add(priceRuleDetil);
		priceRules.add(priceRuleDetil_1);
		priceRules.add(priceRuleDetil2);
		priceRules.add(priceRuleDetil2_2);
		priceRules.add(priceRuleDetil3);
		priceRules.add(priceRuleDetil3_3);
		
		priceRuleTemplate.setPriceRuleDetails(priceRules);
		priceRuleTemplate.setVendorName("digikey");
		priceRuleTemplate.setWarehouse("digikey-100");
		priceRuleTemplate.setBrand("69.0");
		priceRuleTemplate.setCategory("700-711-714");
		priceRuleTemplate.setCurrencyType("USD");
		priceRuleTemplate.setRuleName("Junit测试模板");
		priceRuleTemplate.setDescription("Junit测试模板");
		//priceRuleTemplate.setStatus("ENABLED");
		
		HttpEntity<PriceRuleTemplate> entity = new HttpEntity<PriceRuleTemplate>(priceRuleTemplate);
		
		
		//创建pary 的模拟rest服务，设定其响应url以及返回(参照业务实际要调用的其他应用的服务进行设计，返回该测试用例预期验证的结果）
		MockRestServiceServer server = this.mockRestServiceServer();
		//请求验证用户的用户名
		String userCode = Base64.getUrlEncoder().encodeToString("admin".getBytes());
		//验证用户所返回的用户id
//		String userId = "9999999901";
//		server.expect(MockRestRequestMatchers.requestTo(partyUrl.concat("/v1/users/validated/").concat(userCode)))
//				.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
//				.andRespond(MockRestResponseCreators.withSuccess(userId.getBytes(), MediaType.TEXT_PLAIN));
		String userId = "9999999901";
//		UserResource ur = Mockito.spy(UserResource.class);
//		Mockito.when(partyClient.userResource()).thenReturn(ur);
//		Mockito.when(ur.getUserByAccount(Mockito.anyString())).thenReturn(userId);
		this.mockPartyService();
		
		//新增模板
		ResponseEntity<PriceRuleTemplate> responsePriceRuleTemplate = null;
		PriceRuleTemplate ruleTemplate = null;
		try {
			System.out.println("\n调用新增模板服务--(POST)"+host + "/v1/rules/price");
			responsePriceRuleTemplate = restTemplate.exchange(host + "/v1/rules/price", HttpMethod.POST, entity,PriceRuleTemplate.class);
			ruleTemplate = responsePriceRuleTemplate.getBody();
		} catch (Exception e){
			System.out.println(String.format("初始化，调用新增模板服务失败！错误信息[%s]", e.getMessage()));
			PageInfo<PriceRuleTemplate> page = productPriceRuleManager.findList(null, null, "ENABLIED", null, "Junit测试模板", null, 1, 1);
			if(page.getSize()!=0){
				ruleTemplate = page.getList().get(0);
			}
		}
		
		
		//清楚规则模板缓存
		Cache cache = cacheManager.getCache("priceRuleTemplateCache");
		ValueWrapper valueWrapper = cache.get("PriceRuleTemplateCacheMap");
		Map<String, PriceRuleTemplateCache> priceRuleTemplateCacheMap = null;
	    if(valueWrapper != null){
	    	priceRuleTemplateCacheMap = (Map<String, PriceRuleTemplateCache>) cache.get("PriceRuleTemplateCacheMap").get();
	    	if(priceRuleTemplateCacheMap != null )
	    	priceRuleTemplateCacheMap.remove("digikey-digikey-100-69.0-700-711-714-USD");
	    }
		cache.evict("PriceRuleTemplateCacheMap");
		cache.put("PriceRuleTemplateCacheMap", priceRuleTemplateCacheMap);
		
		
		server = this.mockRestServiceServer();
		//创建pary 的模拟rest服务，设定其响应url以及返回(参照业务实际要调用的其他应用的服务进行设计，返回该测试用例预期验证的结果）
		this.mockPartyService();
		
		//启用模板
		ResponseEntity<PriceRuleTemplate> response = null;
		try {
			System.out.println("\n调用启用模板服务--(PUT)"+host + "/v1/rules/price/"+ruleTemplate.getRuleId()+"/status?status="+RuleStatus.ENABLED);
			response = restTemplate.exchange(host + "/v1/rules/price/"+ruleTemplate.getRuleId()+"/status?status="+RuleStatus.ENABLED, HttpMethod.PUT, null, PriceRuleTemplate.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.getBody();
		
		System.out.println("启用模板成功，priceRuleTemplate-----ruleId="+ruleTemplate.getRuleId());
		return ruleTemplate.getRuleId();
	}
	
	
	/**
	 * 初始化正常的产品测试数据
	 * @return
	 */
	private Product initProductData(){
//		this.initExchangeRate();
		
		Product product = new Product();
		product.setId("1481342363311000");
		
		ProductStand spu = new ProductStand();
		spu.setId("1486460348695997");
		spu.setManufacturer("Amphenol");
		spu.setManufacturerId(69);
		spu.setManufacturerAgg("Amphenol");
		spu.setManufacturerPartNumber("TV06DT-25-4HA");
		spu.setDescription("56 Position Circular Connector Plug, Male Pins Crimp Gold");
		spu.setRohs(true);
		
		List<ProductCategory> categories = new ArrayList<>();
		ProductCategory category = new ProductCategory();
		category.setId(700);
		category.setName("开发板");
		category.setStatus(1);
		category.setLevel(1);
		categories.add(category);
		
		category = new ProductCategory();
		category.setId(711);
		category.setName("评估板配件");
		category.setStatus(1);
		category.setLevel(2);
		categories.add(category);
		
		category = new ProductCategory();
		category.setId(714);
		category.setName("配件");
		category.setStatus(1);
		category.setLevel(3);
		categories.add(category);
		spu.setCategories(categories);
		
		product.setSpu(spu);
		product.setSkuId("AATV06DT-25-4HA-ND");
		product.setVendorName("digikey");
		product.setVendorId("digikey");
		product.setVendorDetailsLink("http://www.digikey.com/product-detail/en/amphenol-aerospace-operations/TV06DT-25-4HA/AATV06DT-25-4HA-ND/5539139");
		product.setVendorSeries(new VendorSeries());
		product.setPackaging("Bulk");
		product.setCountryCode("US");
		product.setQty(0L);
		product.setSourceId("digikey-100");
		
		List<ProductPrice> prices = this.initPrices();
		
		product.setPrices(prices);
		product.setMinimumQuantity(1);
		product.setStoreId(99999999L);
		product.setQuickFindKey("5158dde100c5906bfc589a17a4b451b9");
		productRepository.delete(product);
		productRepository.insert(product);
		return product;
	}
	/**
	 * 初始化没有价格梯度的产品测试数据
	 * @return
	 */
	private Product initDataNotPriceLevel(){
		Product product = new Product();
		product.setId("1481342363311000");
		
		ProductStand spu = new ProductStand();
		spu.setId("1486460348695997");
		spu.setManufacturer("Amphenol Aerospace Operations");
		spu.setManufacturerAgg("Amphenol Aerospace Operations");
		spu.setManufacturerPartNumber("TV06DT-25-4HA");
		spu.setDescription("56 Position Circular Connector Plug, Male Pins Crimp Gold");
		spu.setRohs(true);
		List<ProductCategory> categories = new ArrayList<>();
		ProductCategory category = new ProductCategory();
		category.setId(700);
		category.setName("开发板");
		category.setStatus(1);
		category.setLevel(1);
		categories.add(category);
		
		category = new ProductCategory();
		category.setId(711);
		category.setName("评估板配件");
		category.setStatus(1);
		category.setLevel(2);
		categories.add(category);
		
		category.setId(714);
		category.setName("配件");
		category.setStatus(1);
		category.setLevel(3);
		categories.add(category);
		spu.setCategories(categories);
		
		product.setSpu(spu);
		product.setSkuId("AATV06DT-25-4HA-ND");
		product.setVendorName("digikey");
		product.setVendorId("digikey");
		product.setVendorDetailsLink("http://www.digikey.com/product-detail/en/amphenol-aerospace-operations/TV06DT-25-4HA/AATV06DT-25-4HA-ND/5539139");
		product.setVendorSeries(new VendorSeries());
		product.setPackaging("Bulk");
		product.setCountryCode("US");
		product.setQty(0L);
		product.setSourceId("digikey-100");
		
		List<ProductPrice> prices = this.initPricesNotPriceLevel();
		
		product.setPrices(prices);
		product.setMinimumQuantity(1);
		product.setStoreId(99999999L);
		product.setQuickFindKey("5158dde100c5906bfc589a17a4b451b9");
		productRepository.delete(product);
		productRepository.insert(product);
		return product;
	}
	
	
	/**
	 * 初始化价格为Null产品测试数据
	 * @return
	 */
	private Product initDataWithPriceNull(){
		Product product = new Product();
		product.setId("1481342363311000");
		
		ProductStand spu = new ProductStand();
		spu.setId("1486460348695997");
		spu.setManufacturer("Amphenol Aerospace Operations");
		spu.setManufacturerAgg("Amphenol Aerospace Operations");
		spu.setManufacturerPartNumber("TV06DT-25-4HA");
		spu.setDescription("56 Position Circular Connector Plug, Male Pins Crimp Gold");
		spu.setRohs(true);
		
		List<ProductCategory> categories = new ArrayList<>();
		ProductCategory category = new ProductCategory();
		category.setId(700);
		category.setName("开发板");
		category.setStatus(1);
		category.setLevel(1);
		categories.add(category);
		
		category = new ProductCategory();
		category.setId(711);
		category.setName("评估板配件");
		category.setStatus(1);
		category.setLevel(2);
		categories.add(category);
		
		category.setId(714);
		category.setName("配件");
		category.setStatus(1);
		category.setLevel(3);
		categories.add(category);
		spu.setCategories(categories);
		
		product.setSpu(spu);
		product.setSkuId("AATV06DT-25-4HA-ND");
		product.setVendorName("digikey");
		product.setVendorId("digikey");
		product.setVendorDetailsLink("http://www.digikey.com/product-detail/en/amphenol-aerospace-operations/TV06DT-25-4HA/AATV06DT-25-4HA-ND/5539139");
		product.setVendorSeries(new VendorSeries());
		product.setPackaging("Bulk");
		product.setCountryCode("US");
		product.setQty(0L);
		product.setSourceId("digikey-100");
		
		List<ProductPrice> prices = this.initPricesWithPriceNull();
		
		product.setPrices(prices);
		product.setMinimumQuantity(1);
		product.setStoreId(99999999L);
		product.setQuickFindKey("5158dde100c5906bfc589a17a4b451b9");
		productRepository.delete(product);
		productRepository.insert(product);
		return product;
	}
	
	/**
	 * 初始化价格为空字符串产品测试数据
	 * @return
	 */
	private Product initDataWithPriceNullStr(){
		Product product = new Product();
		product.setId("1481342363311000");
		
		ProductStand spu = new ProductStand();
		spu.setId("1486460348695997");
		spu.setManufacturer("Amphenol Aerospace Operations");
		spu.setManufacturerAgg("Amphenol Aerospace Operations");
		spu.setManufacturerPartNumber("TV06DT-25-4HA");
		spu.setDescription("56 Position Circular Connector Plug, Male Pins Crimp Gold");
		spu.setRohs(true);
		
		List<ProductCategory> categories = new ArrayList<>();
		ProductCategory category = new ProductCategory();
		category.setId(700);
		category.setName("开发板");
		category.setStatus(1);
		category.setLevel(1);
		categories.add(category);
		
		category = new ProductCategory();
		category.setId(711);
		category.setName("评估板配件");
		category.setStatus(1);
		category.setLevel(2);
		categories.add(category);
		
		category.setId(714);
		category.setName("配件");
		category.setStatus(1);
		category.setLevel(3);
		categories.add(category);
		spu.setCategories(categories);
		
		product.setSpu(spu);
		product.setSkuId("AATV06DT-25-4HA-ND");
		product.setVendorName("digikey");
		product.setVendorId("digikey");
		product.setVendorDetailsLink("http://www.digikey.com/product-detail/en/amphenol-aerospace-operations/TV06DT-25-4HA/AATV06DT-25-4HA-ND/5539139");
		product.setVendorSeries(new VendorSeries());
		product.setPackaging("Bulk");
		product.setCountryCode("US");
		product.setQty(0L);
		product.setSourceId("digikey-100");
		
		List<ProductPrice> prices = this.initPricesWithPriceNullStr();
		
		product.setPrices(prices);
		product.setMinimumQuantity(1);
		product.setStoreId(99999999L);
		product.setQuickFindKey("5158dde100c5906bfc589a17a4b451b9");
		productRepository.delete(product);
		productRepository.insert(product);
		return product;
	}

	/**
	 * 初始化有价格的产品测试数据
	 * @return
	 */
	private List<ProductPrice> initPrices() {
		List<ProductPrice> prices = new ArrayList<>();
		//人民币
		ProductPrice price = new ProductPrice();
		price.setCurrencyCode("CNY");
		price.setUnitPrice("2096.544645");
		List<ProductPriceLevel> priceLevels = new ArrayList<>();
		ProductPriceLevel priceLevel = new ProductPriceLevel();
		priceLevel.setPrice("2096.544645");
		priceLevel.setBreakQuantity(1l);
		priceLevels.add(priceLevel);
		priceLevel = new ProductPriceLevel();
		priceLevel.setPrice("1980.865926");
		priceLevel.setBreakQuantity(5l);
		priceLevels.add(priceLevel);
		priceLevel = new ProductPriceLevel();
		priceLevel.setPrice("1966.408119");
		priceLevel.setBreakQuantity(10l);
		priceLevels.add(priceLevel);
		priceLevel = new ProductPriceLevel();
		priceLevel.setPrice("1923.0281928");
		priceLevel.setBreakQuantity(50l);
		priceLevels.add(priceLevel);
		price.setPriceLevels(priceLevels);
		
		prices.add(price);
		
		//美元
		price = new ProductPrice();
		price.setCurrencyCode("USD");
		price.setUnitPrice("257.83");
		priceLevels = new ArrayList<>();
		priceLevel = new ProductPriceLevel();
		priceLevel.setPrice("257.83");
		priceLevel.setBreakQuantity(1l);
		priceLevels.add(priceLevel);
		priceLevel = new ProductPriceLevel();
		priceLevel.setPrice("243.604");
		priceLevel.setBreakQuantity(5l);
		priceLevels.add(priceLevel);
		priceLevel = new ProductPriceLevel();
		priceLevel.setPrice("241.826");
		priceLevel.setBreakQuantity(10l);
		priceLevels.add(priceLevel);
		priceLevel = new ProductPriceLevel();
		priceLevel.setPrice("236.4912");
		priceLevel.setBreakQuantity(50l);
		priceLevels.add(priceLevel);
		price.setPriceLevels(priceLevels);

		prices.add(price);
		return prices;
	}
	
	//没有梯度的数据
	private List<ProductPrice> initPricesNotPriceLevel() {
		List<ProductPrice> prices = new ArrayList<>();
		//人民币
		ProductPrice price = new ProductPrice();
		price.setCurrencyCode("CNY");
		price.setUnitPrice(null);
		List<ProductPriceLevel> priceLevels = new ArrayList<>();
		price.setPriceLevels(priceLevels);
		
		prices.add(price);
		
		//美元
		price = new ProductPrice();
		price.setCurrencyCode("USD");
		price.setUnitPrice(null);
		priceLevels = new ArrayList<>();
		price.setPriceLevels(priceLevels);

		prices.add(price);
		return prices;
	}
	
	//有梯度，梯度上有价格，价格为空
	private List<ProductPrice> initPricesWithPriceNull() {
		List<ProductPrice> prices = new ArrayList<>();
		//人民币
		ProductPrice price = new ProductPrice();
		price.setCurrencyCode("CNY");
		price.setUnitPrice(null);
		List<ProductPriceLevel> priceLevels = new ArrayList<>();
		ProductPriceLevel priceLevel = new ProductPriceLevel();
		priceLevel.setPrice(null);
		priceLevel.setBreakQuantity(1l);
		priceLevels.add(priceLevel);
		priceLevel = new ProductPriceLevel();
		priceLevel.setPrice(null);
		priceLevel.setBreakQuantity(5l);
		priceLevels.add(priceLevel);
		priceLevel = new ProductPriceLevel();
		priceLevel.setPrice(null);
		priceLevel.setBreakQuantity(10l);
		priceLevels.add(priceLevel);
		priceLevel = new ProductPriceLevel();
		priceLevel.setPrice(null);
		priceLevel.setBreakQuantity(50l);
		priceLevels.add(priceLevel);
		price.setPriceLevels(priceLevels);
		
		prices.add(price);
		
		//美元
		price = new ProductPrice();
		price.setCurrencyCode("USD");
		price.setUnitPrice(null);
		priceLevels = new ArrayList<>();
		priceLevel = new ProductPriceLevel();
		priceLevel.setPrice(null);
		priceLevel.setBreakQuantity(1l);
		priceLevels.add(priceLevel);
		priceLevel = new ProductPriceLevel();
		priceLevel.setPrice(null);
		priceLevel.setBreakQuantity(5l);
		priceLevels.add(priceLevel);
		priceLevel = new ProductPriceLevel();
		priceLevel.setPrice(null);
		priceLevel.setBreakQuantity(10l);
		priceLevels.add(priceLevel);
		priceLevel = new ProductPriceLevel();
		priceLevel.setPrice(null);
		priceLevel.setBreakQuantity(50l);
		priceLevels.add(priceLevel);
		price.setPriceLevels(priceLevels);

		prices.add(price);
		return prices;
	}
	
	//有梯度，梯度上价格为空字符串
	private List<ProductPrice> initPricesWithPriceNullStr() {
		List<ProductPrice> prices = new ArrayList<>();
		//人民币
		ProductPrice price = new ProductPrice();
		price.setCurrencyCode("CNY");
		price.setUnitPrice("");
		List<ProductPriceLevel> priceLevels = new ArrayList<>();
		ProductPriceLevel priceLevel = new ProductPriceLevel();
		priceLevel.setPrice("");
		priceLevel.setBreakQuantity(1l);
		priceLevels.add(priceLevel);
		priceLevel = new ProductPriceLevel();
		priceLevel.setPrice("");
		priceLevel.setBreakQuantity(5l);
		priceLevels.add(priceLevel);
		priceLevel = new ProductPriceLevel();
		priceLevel.setPrice("");
		priceLevel.setBreakQuantity(10l);
		priceLevels.add(priceLevel);
		priceLevel = new ProductPriceLevel();
		priceLevel.setPrice("");
		priceLevel.setBreakQuantity(50l);
		priceLevels.add(priceLevel);
		price.setPriceLevels(priceLevels);
		
		prices.add(price);
		
		//美元
		price = new ProductPrice();
		price.setCurrencyCode("USD");
		price.setUnitPrice("");
		priceLevels = new ArrayList<>();
		priceLevel = new ProductPriceLevel();
		priceLevel.setPrice("");
		priceLevel.setBreakQuantity(1l);
		priceLevels.add(priceLevel);
		priceLevel = new ProductPriceLevel();
		priceLevel.setPrice("");
		priceLevel.setBreakQuantity(5l);
		priceLevels.add(priceLevel);
		priceLevel = new ProductPriceLevel();
		priceLevel.setPrice("");
		priceLevel.setBreakQuantity(10l);
		priceLevels.add(priceLevel);
		priceLevel = new ProductPriceLevel();
		priceLevel.setPrice("");
		priceLevel.setBreakQuantity(50l);
		priceLevels.add(priceLevel);
		price.setPriceLevels(priceLevels);

		prices.add(price);
		return prices;
	}
}
