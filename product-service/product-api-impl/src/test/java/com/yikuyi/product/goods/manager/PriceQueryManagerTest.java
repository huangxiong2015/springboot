/*
 * Created: 2017年4月5日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.goods.manager;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yikuyi.basedata.ShipmentClientBuilder;
import com.yikuyi.basedata.resource.UomConversionClient;
import com.yikuyi.category.model.ProductCategory;
import com.yikuyi.product.base.ProductApplicationTestBase;
import com.yikuyi.product.model.Product;
import com.yikuyi.product.model.ProductPrice;
import com.yikuyi.product.model.ProductPriceLevel;
import com.yikuyi.product.model.ProductStand;
import com.yikuyi.product.model.VendorSeries;
import com.yikuyi.product.vo.ProductVo;
import com.yikuyi.rule.price.PriceInfo;
import com.yikuyi.rule.price.ProductPriceRule.RuleStatus;
import com.yikuyi.rule.price.model.PriceRuleDetail;
import com.yikuyi.rule.price.model.PriceRuleTemplate;
import com.yikuyi.rule.price.model.PriceRuleTemplateCache;
import com.ykyframework.exception.InvalidDataException;

/**
 * 
 * @see com.yikuyi.product.goods.manager.PriceQueryManager
 * @see com.yikuyi.product.goods.impl.PriceQueryResouceTest
 * @author zr.wanghong
 * @version 1.0.0
 * @since 2017年4月5日
 */
public class PriceQueryManagerTest extends ProductApplicationTestBase{
	
	@Autowired
	private TestRestTemplate restTemplate; 

	@Autowired
	PriceQueryManager priceQueryManager;
	
	@Autowired
	private MongoRepository<Product, String> productRepository;
	
	@Autowired
	private CacheManager cacheManager;
	
	@SpyBean
	private ShipmentClientBuilder shipmentClientBuilder;
	
	private String host;
	
	/**
	 * @throws java.lang.Exception
	 * @since 2016年12月9日
	 * @author liudian@yikuyi.com
	 */
	@Before
	public void setUpBefore() throws Exception {
		this.host = "http://localhost" + ":" + this.getPort();
	}
	
//	@Test
//	public void testQueryPrice() throws Exception{
//		
//		//初始化规则模板，并启用模板
//		String ruleId = this.initPriceRuleAciton();
//				
//		//初始化商品数据
//		Product product = this.initProductData();
//		Product product2 = this.initProductData2();
//		
//		//mock调用basedata的服务
//		this.mockOtherService();
//		
//		List<String> ids = new ArrayList<>();
//		ids.add(product.getId());
//		ids.add(product2.getId());
//		long startTime = System.currentTimeMillis();
//		List<PriceInfo> priceInfos = priceQueryManager.queryPrice(ids);
//		System.out.println("查询价格总耗时："+(System.currentTimeMillis()-startTime)+" 毫秒");
//		
//		//断言香港价
//		assertEquals("334.84" , priceInfos.get(0).getResalePrices().get(1).getUnitPrice());
//		//断言国内价
//		assertEquals("161.69" , priceInfos.get(0).getResalePrices().get(0).getUnitPrice());
//		
//		//禁用模板
//		this.disabledTemplate(ruleId);
//		
//		//删除模板测试数据
//		this.clearTemplateData(ruleId);
//	}
	
	
	/**
	 * 测试有价格模板时，调用查询价格方法
	 * @throws Exception
	 * @since 2017年4月5日
	 * @author zr.wanghong
	 */
//	@Test
//	public void testQueryPriceNew() throws Exception{
//		
//		//初始化规则模板，并启用模板
//		String ruleId = this.initPriceRuleAciton();
//				
//		//初始化商品数据
//		Product product = this.initProductData();
//		
//		//mock调用basedata的服务
//		this.mockOtherService();
//		
//		ProductVo productVo = new ProductVo();
//		BeanUtils.copyProperties(productVo, product);
//		
//		List<ProductVo> productVos = new ArrayList<>();
//		productVos.add(productVo);
//		priceQueryManager.queryPriceByEntities(productVos,false);
//		
//		
//		//禁用模板
//		this.disabledTemplate(ruleId);
//		
//		//删除模板测试数据
//		this.clearTemplateData(ruleId);
//	}
	
	/**
	 * 测试没有价格模板和价格缓存时，调用查询价格方法
	 * @throws Exception
	 * @since 2017年4月5日
	 * @author zr.wanghong
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testQueryPriceNoRedisCache() throws Exception {
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
	    }
		priceRuleTemplateCacheMap.remove("digikey-digikey-100-69.0-700-711-714-USD");
		cache.evict("PriceRuleTemplateCacheMap");
		cache.put("PriceRuleTemplateCacheMap", priceRuleTemplateCacheMap);
		
		//mock调用basedata的服务
		this.mockOtherService();
		
		ProductVo productVo = new ProductVo();
		BeanUtils.copyProperties(product,productVo);
		List<ProductVo> productVos = new ArrayList<>();
		productVos.add(productVo);
		List<PriceInfo> priceInfos = priceQueryManager.queryPriceByEntities(productVos,false);
		
		//断言香港价
		//assertEquals("257.83000" , priceInfos.get(0).getResalePrices().get(1).getUnitPrice());
		//断言国内价
		//assertEquals("202.11000" , priceInfos.get(0).getResalePrices().get(0).getUnitPrice());
	}
	
	
	
	
	/**
	 * 调用删除模板服务
	 * @param ruleId
	 */
	private void clearTemplateData(String ruleId){
		this.mockPartyService();
		
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
		this.mockPartyService();
		
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
		priceRuleTemplate.setBrand("69");
		priceRuleTemplate.setCategory("700-711-714");
		priceRuleTemplate.setCurrencyType("USD");
		priceRuleTemplate.setRuleName("Junit测试模板");
		priceRuleTemplate.setDescription("Junit测试模板");
		//priceRuleTemplate.setStatus("ENABLED");
		
		HttpEntity<PriceRuleTemplate> entity = new HttpEntity<PriceRuleTemplate>(priceRuleTemplate);
		
		
		this.mockPartyService();
		
		//1.新增模板
		ResponseEntity<PriceRuleTemplate> responsePriceRuleTemplate = null;
		try {
			System.out.println("\n调用新增模板服务--(POST)"+host + "/v1/rules/price");
			responsePriceRuleTemplate = restTemplate.exchange(host + "/v1/rules/price", HttpMethod.POST, entity,PriceRuleTemplate.class);
		} catch (InvalidDataException e){
			System.out.println(String.format("初始化，调用新增模板服务失败！错误信息[%s]", e.getMessage()));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		PriceRuleTemplate ruleTemplate = responsePriceRuleTemplate.getBody();
		
		
		//2.清楚规则模板缓存
		Cache cache = cacheManager.getCache("priceRuleTemplateCache");
		ValueWrapper valueWrapper = cache.get("PriceRuleTemplateCacheMap");
		Map<String, PriceRuleTemplateCache> priceRuleTemplateCacheMap = null;
	    if(valueWrapper != null){
	    	priceRuleTemplateCacheMap = (Map<String, PriceRuleTemplateCache>) cache.get("PriceRuleTemplateCacheMap").get();
	    }
		priceRuleTemplateCacheMap.remove("digikey-digikey-100-69-700-711-714-USD");
		cache.evict("PriceRuleTemplateCacheMap");
		cache.put("PriceRuleTemplateCacheMap", priceRuleTemplateCacheMap);
		
		this.mockPartyService();
		//3.启用模板
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
	
	
	@Value("${api.basedata.serverUrlPrefix}")
	private String basedataServerUrlPrefix;
	
	ObjectMapper objectMapper = new ObjectMapper(); //JSON
	
	private void mockOtherService() throws JsonProcessingException {

		this.mockPartyService();
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("taxRate", new BigDecimal("0.17"));
		resultMap.put("exchangeRate",new Double(0.67));
		
		Cache exchangeRateCache = cacheManager.getCache("exchangeRateCache");
		exchangeRateCache.put("exchangeRateCache-USD-CNY",resultMap);
		
		/*String ratesJson = objectMapper.writeValueAsString(resultMap); 
		server.expect(MockRestRequestMatchers.requestTo(basedataServerUrlPrefix.concat("/v1/basedata/exchangerate/USD/CNY")))
				.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
				.andRespond(MockRestResponseCreators.withSuccess(ratesJson, MediaType.APPLICATION_JSON_UTF8));*/
		UomConversionClient uc = Mockito.spy(UomConversionClient.class);
		Mockito.when(shipmentClientBuilder.uomConversionResource()).thenReturn(uc);
		Mockito.when(uc.getById(Mockito.anyObject(),Mockito.anyObject())).thenReturn(resultMap);
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
	 * 初始化正常的产品测试数据
	 * @return
	 */
	private Product initProductData2(){
//		this.initExchangeRate();
		
		Product product = new Product();
		product.setId("14813423633110002");
		
		ProductStand spu = new ProductStand();
		spu.setId("1486460348695997");
		spu.setManufacturer("Amphenol2");
		spu.setManufacturerId(69);
		spu.setManufacturerAgg("Amphenol");
		spu.setManufacturerPartNumber("TV06DT-25-4HA");
		spu.setDescription("56 Position Circular Connector Plug, Male Pins Crimp Gold");
		spu.setRohs(true);
		
		List<ProductCategory> categories = new ArrayList<>();
		ProductCategory category = new ProductCategory();
		category.setId(700);
		category.setName("开发板2");
		category.setStatus(1);
		category.setLevel(1);
		categories.add(category);
		
		category = new ProductCategory();
		category.setId(711);
		category.setName("评估板配件2");
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
		product.setQuickFindKey("DIGIKEY-100-Amphenol2-AATV06DT-25-4HA-ND");
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
	
	/**
	 * 测试获取价格模板
	 * 
	 * @since 2017年6月20日
	 * @author zr.wanghong
	 */
	@Test
	public void testQueryPriceTemplate(){
		//初始化商品数据
		Product product = this.initProductData();
		List<String> ids = new ArrayList<>();
		ids.add(product.getId());
		priceQueryManager.queryPriceTemplate(ids);
	}
	/**
	 * 测试获取价格模板
	 * 
	 * @since 2017年6月20日
	 * @author zr.wanghong
	 */
	@Test
	public void testQueryPriceTemplateByProduct(){
		//初始化商品数据
		Product product = this.initProductData();
		priceQueryManager.queryPriceTemplate(product);
	}
	
	/**
	 * 测试查询活动价格
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @since 2017年6月20日
	 * @author zr.wanghong
	 */
	@Test
	public void testQueryPriceWithActivity() throws IllegalAccessException, InvocationTargetException{
		//初始化商品数据
		Product product = this.initProductData();
		ProductVo productVo = new ProductVo();
		BeanUtils.copyProperties(product,productVo);
		List<ProductVo> productVos = new ArrayList<>();
		productVos.add(productVo);
		priceQueryManager.queryPriceWithActivity(productVos);
	}
	
	@Test
	public void testQueryActivityPrice() throws IllegalAccessException, InvocationTargetException{
		//初始化商品数据
		Product product = this.initProductData();
		ProductVo productVo = new ProductVo();
		BeanUtils.copyProperties(product,productVo);
		List<ProductVo> productVos = new ArrayList<>();
		productVos.add(productVo);
		priceQueryManager.queryPriceWithActivity(productVos);
	}
	
	
	/**
	 * 测试金额转换为保留4位有效数字（价格 >= 100只处理为保留2位小数）<br>
	 * 得到结果取5位小数，小数不足五位补零
	 * @throws Exception
	 * @since 2017年7月4日
	 * @author zr.wanghong
	 */
	@Test
	public void testKeep4EffectiveNumber() throws Exception{
		Class<PriceQueryManager> clazz = PriceQueryManager.class;
		PriceQueryManager instance = clazz.newInstance();
		Method method = clazz.getDeclaredMethod("keep4EffectiveNumber", new Class[]{BigDecimal.class});
		method.setAccessible(true);
		Object result = method.invoke(instance, new Object[]{new BigDecimal("0.012345689")} );
		assertEquals(new BigDecimal("0.01235"), result); 
		
		result = method.invoke(instance, new Object[]{new BigDecimal("0.12345689")} );
		assertEquals(new BigDecimal("0.1235"), result); 
		
		result = method.invoke(instance, new Object[]{new BigDecimal("1.23456789")} );
		assertEquals(new BigDecimal("1.235"), result); 
		
		result = method.invoke(instance, new Object[]{new BigDecimal("12.3456789")} );
		assertEquals(new BigDecimal("12.35"), result); 
		
		result = method.invoke(instance, new Object[]{new BigDecimal("123.456789")} );
		assertEquals(new BigDecimal("123.46"), result); 
		
		result = method.invoke(instance, new Object[]{new BigDecimal("0.1")} );
		assertEquals(new BigDecimal("0.10"), result); 
		
		result = method.invoke(instance, new Object[]{new BigDecimal("1")} );
		assertEquals(new BigDecimal("1.00"), result); 
		
		result = method.invoke(instance, new Object[]{new BigDecimal("10")} );
		assertEquals(new BigDecimal("10.00"), result); 
		
	}
	
}
