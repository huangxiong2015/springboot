package com.yikuyi.product.goods.manager;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.ictrade.enterprisematerial.InventorySearchManager;
import com.ictrade.entity.ResultJSONObject;
import com.yikuyi.party.PartyClientBuilder;
import com.yikuyi.party.group.vo.PartyGroupVo;
import com.yikuyi.party.model.PartyAttributes;
import com.yikuyi.party.resource.PartyGroupClient;
import com.yikuyi.party.vo.PartyVo;
import com.yikuyi.product.vo.ProductVo;

@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
	TransactionDbUnitTestExecutionListener.class, MockitoTestExecutionListener.class  })
@Transactional
@Rollback
@ConfigurationProperties
@RunWith(SpringRunner.class)
@ComponentScan(basePackages = "com.yikuyi.product")
@SpringBootTest
public class SearchManagerTest{
	
	@Autowired
	private SearchManager searchManager;
	
	@SpyBean
	private PartyClientBuilder partyClientBuilder;
	
	@MockBean
	private InventorySearchManager inventorySearchManager;
	
	private static final String[] PRODUCT_RETURN_FIELD = {"_id","maxFactoryLeadTimeHK","maxFactoryLeadTimeML","maxLeadTimeHK",
			"maxLeadTimeML","minFactoryLeadTimeHK","minFactoryLeadTimeML","minLeadTimeHK","minLeadTimeML",
			"prices","qty","sourceId","vendorId","spu.categories","spu.description","spu._id","spu.images",
			"spu.manufacturer","spu.manufacturerId","spu.manufacturerPartNumber","spu.rohs","spu.spuId"};
	
	@Before
	public void config() {
	}
	
	//商品mock数据
	private ResultJSONObject otherMockService(){
		ResultJSONObject obj = new ResultJSONObject();
		JSONArray aggMap = new JSONArray();
		//汇聚后的供应商
		JSONObject vendorIdAgg1 = new JSONObject();
		vendorIdAgg1.put("key", "digikey");
		JSONObject vendorIdAgg2 = new JSONObject();
		vendorIdAgg1.put("key", "future");
		JSONArray vendorId = new JSONArray();
		vendorId.add(vendorIdAgg1);
		vendorId.add(vendorIdAgg2);
		//汇聚后的制造商Id
		JSONObject manufacturerIdAgg1 = new JSONObject();
		manufacturerIdAgg1.put("key", "162");
		JSONObject manufacturerIdAgg2 = new JSONObject();
		manufacturerIdAgg2.put("key", "125");
		JSONObject manufacturerIdAgg3 = new JSONObject();
		manufacturerIdAgg3.put("key", "1");
		JSONArray manufacturerId = new JSONArray();
		manufacturerId.add(manufacturerIdAgg1);
		manufacturerId.add(manufacturerIdAgg2);
		manufacturerId.add(manufacturerIdAgg3);
		//汇聚后的分类
		JSONObject catgoryIdAgg1 = new JSONObject();
		catgoryIdAgg1.put("key", "669");
		JSONObject catgoryIdAgg2 = new JSONObject();
		catgoryIdAgg2.put("key", "677");
		JSONObject catgoryIdAgg3 = new JSONObject();
		catgoryIdAgg3.put("key", "683");
		JSONArray catgoryId = new JSONArray();
		catgoryId.add(catgoryIdAgg1);
		catgoryId.add(catgoryIdAgg2);
		catgoryId.add(catgoryIdAgg3);
		aggMap.add(vendorId);
		aggMap.add(manufacturerId);
		aggMap.add(catgoryId);
		//商品数据
		JSONArray hitsMap = new JSONArray();
		hitsMap.add(initProduct());
		hitsMap.add(initProduct1());
		obj.put("aggs", aggMap);
		obj.put("total", 1);
		obj.put("hits", hitsMap);
		return obj;
	}
	
	/**
	 * 初始化数据
	 * @return
	 * @since 2017年6月29日
	 * @author zr.wenjiao@yikuyi.com
	 */
	private JSONObject initProduct(){
		JSONObject product = new JSONObject();
		product.put("_ID", "841110823278477312");
		JSONArray priceMap = new JSONArray();
		JSONObject price = new JSONObject();
		price.put("unitPrice","0.322");
		price.put("currencyCode","USD");
		JSONArray priceLevelsMap = new JSONArray();
		JSONObject priceLevels = new JSONObject();
		priceLevels.put("breakQuantity",2700);
		priceLevels.put("price","0.322");
		JSONObject priceLevels1 = new JSONObject();
		priceLevels1.put("breakQuantity",5400);
		priceLevels1.put("price","0.3105");
		priceLevelsMap.add(priceLevels);
		priceLevelsMap.add(priceLevels1);
		price.put("priceLevels", priceLevelsMap);
		priceMap.add(price);
		product.put("prices", priceMap);
		product.put("qty",2700);
		product.put("sourceId","digikey-100");
		product.put("vendorId","digikey");
		product.put("vendorName","digikey");
		JSONObject spu = new JSONObject();
		JSONArray categoriesMap = new JSONArray();
		JSONObject categories = new JSONObject();
		categories.put("_id",669);
		categories.put("cateName","电源供应与电路保护");
		categories.put("cateLevel",1);
		JSONObject categories1 = new JSONObject();
		categories1.put("_id",677);
		categories1.put("cateName","电路保护");
		categories1.put("cateLevel",2);
		JSONObject categories2 = new JSONObject();
		categories2.put("_id",683);
		categories2.put("cateName","气体放电管");
		categories2.put("cateLevel",3);
		categoriesMap.add(categories);
		categoriesMap.add(categories1);
		categoriesMap.add(categories2);
		spu.put("categories", categoriesMap);
		spu.put("description", "Gas Discharge Tube 600V 3000A (3kA) ±20% 2 Pole Surface Mount");
		spu.put("_id","1486459301048521");
		spu.put("manufacturer","Bourns");
		spu.put("manufacturerId",162);
		spu.put("manufacturerPartNumber","2053-60-SM-RPLF");
		spu.put("rohs",true);
		spu.put("spuId","2053-60-SM-RPLF-BOURNS");
		JSONArray imagesMap = new JSONArray();
		JSONObject images = new JSONObject();
		images.put("type","thumbnail");
		images.put("url","//media.digikey.com/Photos/Bourns%20Photos/2053-23-SM-RPLF_sml.jpg");
		JSONObject images1 = new JSONObject();
		images1.put("type","large");
		images1.put("url","/product/Photos/Bourns%20Photos/2053-23-SM-RPLF.jpg");
		imagesMap.add(images);
		imagesMap.add(images1);
		spu.put("images", imagesMap);
		product.put("spu", spu);
		return product;
	}
	
	/**
	 * 初始化数据
	 * @return
	 * @since 2017年6月29日
	 * @author zr.wenjiao@yikuyi.com
	 */
	private JSONObject initProduct1(){
		JSONObject product = new JSONObject();
		product.put("_ID", "1483690001878175");
		JSONArray priceMap = new JSONArray();
		JSONObject price = new JSONObject();
		price.put("unitPrice","0.322");
		price.put("currencyCode","CNY");
		JSONArray priceLevelsMap = new JSONArray();
		JSONObject priceLevels = new JSONObject();
		priceLevels.put("breakQuantity",2700);
		priceLevels.put("price","0.322");
		JSONObject priceLevels1 = new JSONObject();
		priceLevels1.put("breakQuantity",5400);
		priceLevels1.put("price","0.3105");
		priceLevelsMap.add(priceLevels);
		priceLevelsMap.add(priceLevels1);
		price.put("priceLevels", priceLevelsMap);
		priceMap.add(price);
		product.put("prices", priceMap);
		product.put("qty",2700);
		product.put("sourceId","1111");
		product.put("vendorId","1111");
		product.put("vendorName","digikey");
		JSONObject spu = new JSONObject();
		JSONArray categoriesMap = new JSONArray();
		JSONObject categories = new JSONObject();
		categories.put("_id",669);
		categories.put("cateName","电源供应与电路保护");
		categories.put("cateLevel",1);
		JSONObject categories1 = new JSONObject();
		categories1.put("_id",677);
		categories1.put("cateName","电路保护");
		categories1.put("cateLevel",2);
		JSONObject categories2 = new JSONObject();
		categories2.put("_id",683);
		categories2.put("cateName","气体放电管");
		categories2.put("cateLevel",3);
		categoriesMap.add(categories);
		categoriesMap.add(categories1);
		categoriesMap.add(categories2);
		spu.put("categories", categoriesMap);
		spu.put("description", "Gas Discharge Tube 600V 3000A (3kA) ±20% 2 Pole Surface Mount");
		spu.put("_id","1483690001878174");
		spu.put("manufacturer","Bourns");
		spu.put("manufacturerId",162);
		spu.put("manufacturerPartNumber","2053-60-SM-RPLF");
		spu.put("rohs",true);
		spu.put("spuId","2053-60-SM-RPLF-BOURNS");
		JSONArray imagesMap = new JSONArray();
		JSONObject images = new JSONObject();
		images.put("type","thumbnail");
		images.put("url","//media.digikey.com/Photos/Bourns%20Photos/2053-23-SM-RPLF_sml.jpg");
		JSONObject images1 = new JSONObject();
		images1.put("type","large");
		images1.put("url","/product/Photos/Bourns%20Photos/2053-23-SM-RPLF.jpg");
		imagesMap.add(images);
		imagesMap.add(images1);
		spu.put("images", imagesMap);
		product.put("spu", spu);
		product.put("minLeadTimeML", 1);
		product.put("maxLeadTimeML", 2);
		product.put("minLeadTimeHK", 3);
		product.put("maxLeadTimeHK", 4);
		product.put("minFactoryLeadTimeML", 5);
		product.put("maxFactoryLeadTimeML", 6);
		product.put("minFactoryLeadTimeHK", 7);
		product.put("maxFactoryLeadTimeHK", 8);
		return product;
	}
	
	//供应商mock数据
	private List<PartyVo> otherMockService1(){
		List<PartyVo> vendorList = new ArrayList<>();
		PartyVo vo = new PartyVo();
		vo.setId("digikey");
		vo.setDisplayName("digikey");
		PartyAttributes partyAttributes = new PartyAttributes();
		partyAttributes.setIsVendorDetail("Y");
		vo.setPartyAttributes(partyAttributes);
		vendorList.add(vo);
		return vendorList;
	}
	
	/**
	 * 根据关键字查询商品
	 * 
	 * @since 2017年6月26日
	 * @author zr.wenjiao@yikuyi.com
	 * @throws JsonProcessingException 
	 * @throws UnsupportedEncodingException 
	 */
	@Test
	public void testGetProductInfoByKeyWord() throws JsonProcessingException, UnsupportedEncodingException, InterruptedException{
		//商品Mock数据
		Mockito.when(inventorySearchManager.search(Mockito.any(JSONObject.class))).thenReturn(new ResultJSONObject());
		//供应商Mock数据
		PartyGroupClient vendor = Mockito.spy(PartyGroupClient.class);
		Mockito.when(partyClientBuilder.partyGroupClient()).thenReturn(vendor);
		List<PartyVo> vendorMock = otherMockService1();
		Mockito.when(vendor.getAllPartyGroupList(Mockito.any(PartyGroupVo.class))).thenReturn(vendorMock);
		//无法查询到商品
		Map<String, Object> result = searchManager.getProductInfo("", "123", "", "", "", 2, 20, "","");
		List<ProductVo> resultInfo = (List<ProductVo>)result.get("productInfo");
		assertEquals(0, resultInfo.size());
		
		//商品Mock数据
		ResultJSONObject productMock = otherMockService();
		Mockito.when(inventorySearchManager.search(Mockito.any(JSONObject.class))).thenReturn(productMock);
		//供应商Mock数据
		vendor = Mockito.spy(PartyGroupClient.class);
		Mockito.when(partyClientBuilder.partyGroupClient()).thenReturn(vendor);
		Mockito.when(vendor.getAllPartyGroupList(Mockito.any(PartyGroupVo.class))).thenReturn(vendorMock);
		//根据关键字查询商品
		result = searchManager.getProductInfo("Bourns", "", "", "", "", 2, 20, "","");
		resultInfo = (List<ProductVo>)result.get("productInfo");
		JSONObject info = (JSONObject) productMock.getJSONArray("hits").get(0);
		Assert.assertTrue(resultInfo.get(0).getId().equals(info.getString("id")));
	}
	
	/**
	 * 根据分类查询商品
	 * @throws JsonProcessingException
	 * @throws UnsupportedEncodingException
	 * @throws InterruptedException
	 * @since 2017年7月11日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	public void testGetProductInfoByCat() throws JsonProcessingException, UnsupportedEncodingException, InterruptedException{
		//商品Mock数据
		ResultJSONObject productMock = otherMockService();
		Mockito.when(inventorySearchManager.search(Mockito.any(JSONObject.class))).thenReturn(productMock);
		//供应商Mock数据
		PartyGroupClient vendor = Mockito.spy(PartyGroupClient.class);
		Mockito.when(partyClientBuilder.partyGroupClient()).thenReturn(vendor);
		List<PartyVo> vendorMock = otherMockService1();
		Mockito.when(vendor.getAllPartyGroupList(Mockito.any(PartyGroupVo.class))).thenReturn(vendorMock);
		//根据分类查询商品
		Map<String, Object> result = searchManager.getProductInfo("", "", "", "1", "", 2, 20, "","");
		List<ProductVo> resultInfo = (List<ProductVo>)result.get("productInfo");
		JSONObject info = (JSONObject) productMock.getJSONArray("hits").get(0);
		Assert.assertTrue(resultInfo.get(0).getId().equals(info.getString("id")));
	}
	
	/**
	 * 根据制造商查询商品
	 * @throws JsonProcessingException
	 * @throws UnsupportedEncodingException
	 * @throws InterruptedException
	 * @since 2017年7月11日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	public void testGetProductInfoByBrand() throws JsonProcessingException, UnsupportedEncodingException, InterruptedException{
		//商品Mock数据
		ResultJSONObject productMock = otherMockService();
		Mockito.when(inventorySearchManager.search(Mockito.any(JSONObject.class))).thenReturn(productMock);
		//供应商Mock数据
		PartyGroupClient vendor = Mockito.spy(PartyGroupClient.class);
		Mockito.when(partyClientBuilder.partyGroupClient()).thenReturn(vendor);
		List<PartyVo> vendorMock = otherMockService1();
		Mockito.when(vendor.getAllPartyGroupList(Mockito.any(PartyGroupVo.class))).thenReturn(vendorMock);
		//根据制造商查询商品
		Map<String, Object> result = searchManager.getProductInfo("", "", "27", "", "", 2, 20, "","");
		List<ProductVo> resultInfo = (List<ProductVo>)result.get("productInfo");
		JSONObject info = (JSONObject) productMock.getJSONArray("hits").get(0);
		Assert.assertTrue(resultInfo.get(0).getId().equals(info.getString("id")));
	}
	
	/**
	 * 测试设置了搜索推广信息后查询商品
	 * @throws JsonProcessingException
	 * @throws UnsupportedEncodingException
	 * @throws InterruptedException
	 * @since 2017年6月29日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:com/yikuyi/product/goods/recommendation_sample.xml")
	public void testGetProductInfo2() throws JsonProcessingException, UnsupportedEncodingException, InterruptedException{
		//商品Mock数据
		ResultJSONObject productMock = otherMockService();
		Mockito.when(inventorySearchManager.search(Mockito.any(JSONObject.class))).thenReturn(productMock);
		//供应商Mock数据
		PartyGroupClient vendor = Mockito.spy(PartyGroupClient.class);
		Mockito.when(partyClientBuilder.partyGroupClient()).thenReturn(vendor);
		List<PartyVo> vendorMock = otherMockService1();
		Mockito.when(vendor.getAllPartyGroupList(Mockito.any(PartyGroupVo.class))).thenReturn(vendorMock);
		
		Map<String, Object> result = searchManager.getProductInfo("", "", "163", "", "", 1, 20, "","0");
		List<ProductVo> resultInfo = (List<ProductVo>)result.get("productInfo");
		JSONObject info = (JSONObject) productMock.getJSONArray("hits").get(0);
		Assert.assertTrue(resultInfo.get(0).getId().equals(info.getString("id")));
	}
}
