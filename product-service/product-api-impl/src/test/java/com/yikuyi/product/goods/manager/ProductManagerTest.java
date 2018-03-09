package com.yikuyi.product.goods.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.google.common.collect.Lists;
import com.yikuyi.brand.model.ProductBrand;
import com.yikuyi.category.model.ProductCategory;
import com.yikuyi.category.model.ProductCategoryAlias;
import com.yikuyi.category.vo.ProductCategoryParent;
import com.yikuyi.material.vo.MaterialVo;
import com.yikuyi.material.vo.MaterialVo.MaterialVoType;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.vo.PartyVo;
import com.yikuyi.product.base.ProductApplicationTestBase;
import com.yikuyi.product.brand.manager.BrandManager;
import com.yikuyi.product.category.manager.CategoryManager;
import com.yikuyi.product.goods.dao.ProductRepository;
import com.yikuyi.product.model.Product;
import com.yikuyi.product.model.ProductAttachment;
import com.yikuyi.product.model.ProductDocument;
import com.yikuyi.product.model.ProductImage;
import com.yikuyi.product.model.ProductParameter;
import com.yikuyi.product.model.ProductPrice;
import com.yikuyi.product.model.ProductPriceLevel;
import com.yikuyi.product.model.ProductStand;
import com.yikuyi.product.model.Stock;
import com.yikuyi.product.model.VendorSeries;
import com.yikuyi.product.vo.ProductRequest;
import com.yikuyi.product.vo.ProductVo;
import com.yikuyi.product.vo.RawData;
import com.yikuyi.rule.mov.vo.MovInfo;
import com.yikuyi.rule.price.PriceInfo;
import com.ykyframework.exception.BusinessException;
import com.ykyframework.mqservice.sender.MsgSender;

@RunWith(SpringRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
	TransactionDbUnitTestExecutionListener.class })
@Transactional
@Rollback
public class ProductManagerTest extends ProductApplicationTestBase{

	@Autowired
	private ProductManager productManager;
	
	@Autowired
	private ProductStandManager productStandManager;
	
	@Autowired
	private BrandManager brandManager;
	@Autowired
	private CategoryManager categoryManager;
	@Autowired
	private RedisCacheManager redisCacheManagerNoTransaction;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private MsgSender msgSender;
	@Value("${mqProduceConfig.syncElasticsearchProduct.topicName}")
	private String syncElasticsearchProductTopicName;
	private String host;
	
	@Autowired
	private MovQueryManagerV2 movQueryManager;


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
	 * 
	 * 查询商品基本信息
	 * @since 2016年12月29日
	 * @author zr.wujiajun@yikuyi.com
	 */	
	@Test
	public void testFindBasicInfo(){
		List<String> ids = new ArrayList<String>();
		ids.add("1482150087598948");	
		List<Product> result = productManager.findBasicInfo(ids);
		if(CollectionUtils.isNotEmpty(result)){
			Product p1 = result.get(0);
			assertEquals(p1.getId(), "1482150087598948");	
			assertEquals(p1.getQty().longValue(), 10L);	
			assertEquals(p1.getVendorId(), "digikey");	
			assertEquals(p1.getPackaging(), "Bulk");
			assertEquals(p1.getVendorName(), "digikey");
			assertEquals(p1.getSpu().getRohs(), true);	
			assertEquals(p1.getSpu().getManufacturer(), "Mallory Sonalert Products Inc.");				
			assertEquals(true,CollectionUtils.isEmpty(p1.getPrices()));			
		}else{
			assertEquals(true,CollectionUtils.isEmpty(result));	
		}
	}
	
	/**
	 * 销售中的商品 导出cvs
	 * 
	 * @since 2017年9月15日
	 * @author injor.huang@yikuyi.com
	 */
	@Test
	public void exportExcelByConditionTest(){
//		File file = productManager.exportExcelByCondition("1483689358626021", "X-5328-P-C-LW165-R", "PUI Audio, Inc.", "digikey", null,
//				478, 527, 530,
//				"X-5328-P-C-LW165-R", null, null, true, null, "test.cvs");
//		assertEquals(true,file != null);	
	}
	
	/**
	 * 
	 * 根据条件删除所有数据
	 * @since 2017年9月15日
	 * @author injor.huang@yikuyi.com
	 */
	@Test
	public void deleteAllTest(){
		boolean res = productManager.deleteAll("1483689358626193", "CSS-I4B20-SMT-TR", "CUI INC", "digikey", 
				null, -1, -2, -3, "CSS-I4B20-SMT-TR", null, null, true, null,null,null);
		assertEquals(true,res);	
	}
	
	/**
	 * 测试价格排序
	 * @since 2017年4月1日
	 * @author tongkun@yikuyi.com
	 */
	@Test
	public void testSortPriceLevel(){
		List<ProductPrice> oriPrices = new ArrayList<>();
		ProductPrice cny = new ProductPrice();
		oriPrices.add(cny);
		cny.setUnitPrice("CNY");
		List<ProductPriceLevel> levels = new ArrayList<>(); 
		cny.setPriceLevels(levels);
		cny.setPriceLevels(levels);
		ProductPriceLevel ppl = new ProductPriceLevel();
		ppl.setBreakQuantity(10l);
		ppl.setPrice("111.2");
		levels.add(ppl);
		ppl = new ProductPriceLevel();
		ppl.setBreakQuantity(1l);
		ppl.setPrice("122");
		levels.add(ppl);
		ppl = new ProductPriceLevel();
		ppl.setBreakQuantity(1l);
		ppl.setPrice("122");
		levels.add(ppl);
		ppl = new ProductPriceLevel();
		ppl.setBreakQuantity(333l);
		ppl.setPrice("mmm");
		levels.add(ppl);
		ppl = new ProductPriceLevel();
		ppl.setBreakQuantity(2l);
		ppl.setPrice("1");
		levels.add(ppl);
		List<ProductPrice> newPriceLevel = productManager.sortPriceLevel(oriPrices);
		assertEquals(1, newPriceLevel.size());
		assertEquals("CNY", newPriceLevel.get(0).getUnitPrice());
		assertEquals(3, newPriceLevel.get(0).getPriceLevels().size());
		assertEquals(new Long(1), newPriceLevel.get(0).getPriceLevels().get(0).getBreakQuantity());
		assertEquals("122", newPriceLevel.get(0).getPriceLevels().get(0).getPrice());
	}
	
	/**
	 * 批量查询商品全部信息
	 * @since 2016年12月29日
	 * @author zr.wujiajun@yikuyi.com
	 */
	@Test
	public void testFindFullInfo(){
		//初始化商品数据
		Product product = this.initProductData();
		List<String> ids = new ArrayList<String>();
		ids.add(product.getId());
		
		//模拟party服务
		this.mockPartyService();	
		List<ProductVo> result = productManager.findFullInfo(ids);
		
		if(CollectionUtils.isNotEmpty(result)){
			ProductVo p1 = result.get(0);
			assertEquals(p1.getId(), "1481342363311000");	
			assertEquals(p1.getQty().longValue(), 0L);	
			assertEquals(p1.getVendorId(), "digikey");	
			assertEquals(p1.getSpu().getRohs(), true);	
			assertEquals(p1.getSpu().getManufacturer(), "Amphenol");				
			assertEquals(true,p1.getPrices().size()>0);	//存在销售价格			
			//assertEquals(true,p1.getRealLeadTime().getLeadTimeCH().length() >0);			
		}else{
			assertEquals(true,CollectionUtils.isEmpty(result));	
		}		
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
		product.setStatus(1);
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
	 * 查询商品信息
	 * 
	 * @since 2017年6月27日
	 * @author tb.lijing@yikuyi.com
	 */
	@Test
	public void testFindProducts(){
		//分类映射
		Map<ProductCategoryAlias, ProductCategoryParent> cateMap = categoryManager.getAliasCategoryMap();
		//品牌映射
		Cache cache = redisCacheManagerNoTransaction.getCache("aliasMap");
		if(cache!=null){
			cache.evict("aliasBrandMap");
		}
		Map<String, ProductBrand> brandMap = brandManager.getAliasBrandMap();
		RawData data = createTestData();
		List<RawData> datas = new ArrayList<RawData>();
		datas.add(data);
		List<Product> pros = productManager.findProductByRawDatas(datas, brandMap);
	/*	assertEquals(1, pros.size());
		List<ProductStand> spus = productManager.findProductStandByRawDatas(datas);
		assertEquals(0, spus.size());*/
	}
	/**
	 * 创建一条测试数据
	 * @return
	 * @since 2016年12月17日
	 * @author tongkun@yikuyi.com
	 */
	private RawData createTestData(){
		RawData data = new RawData();
		data.setCountryCode("HK");
		data.setDescription("abc.");
		List<ProductDocument> docs = new ArrayList<>();
		ProductDocument doc = new ProductDocument();
		doc.setType("datasheet");
		doc.setName("eee");
		doc.setUrl("what?");
		List<ProductAttachment> attas = new ArrayList<>();
		ProductAttachment att = new ProductAttachment();
		att.setName("eee");
		att.setUrl("what?");
		attas.add(att);
		doc.setAttaches(attas);
		docs.add(doc);
		data.setDocuments(docs);
		List<ProductImage> images = new ArrayList<ProductImage>();
		ProductImage img = new ProductImage();
		img.setType("jpg");
		img.setUrl("hahaha!");
		images.add(img);
		data.setImages(images);
		data.setManufacturer("PANJIT");
		data.setManufacturerPartNumber("abcdefg");
		data.setMinimumQuantity(123);
		List<ProductParameter> params = new ArrayList<>();
		ProductParameter param = new ProductParameter();
		param.setCode("code");
		param.setName("lalala");
		param.setValue("5xxx");
		params.add(param);
		data.setParameters(params);
		data.setPartStatus("Active");
		List<ProductPrice> prices = new ArrayList<>();
		ProductPrice price = new ProductPrice();
		price.setUnitPrice("1");
		price.setCurrencyCode("CNY");
		List<ProductPriceLevel> levels = new ArrayList<ProductPriceLevel> ();
		ProductPriceLevel level = new ProductPriceLevel();
		level.setBreakQuantity(123L);
		level.setPackaging("wawawa");
		level.setPrice("1");
		levels.add(level);
		price.setPriceLevels(levels);
		prices.add(price);
		data.setPrices(prices);
		data.setRohs("true");
		data.setSkuId("sku-aaa");
		List<Stock> stocks = new ArrayList<>();
		Stock stock = new Stock();
		stock.setLeadTime("0");
		stock.setQuantity(100l);
		stock.setSource("100");
		stocks.add(stock);
		data.setStocks(stocks);
		data.setUnit("pcs");
		List<ProductCategory> cates = new ArrayList<>();
		ProductCategory cate = new ProductCategory();
		cate.setName("VFD DRIVERS");
		cate.setLevel(2);
		cate.setStatus(1);
		cates.add(cate);
		data.setVendorCategories(cates);
		data.setVendorDetailsLink("links");
		data.setVendorId("1000");
		data.setVendorName("digikey");
		VendorSeries se = new VendorSeries();
		se.setName("ah?");
		se.setLink("huhuhu...");
		data.setVendorSeries(se);
		data.setCantCreateStand(false);
		data.setPackaging("package");
		data.setCostPrices(prices);
		data.setResalePrices(prices);
		data.setSpecialResaleprices(prices);
		data.setProcessId("aabb123");
		data.setRegion("local");
		data.setCurrencyCode("USD");
		data.setLineNo(100l);
		
		return data;
	}
	
	
	/**
	 * 查询是否存在商品
	 * @param 供应商id
	 */
	@Test
	public void testHasProduct(){		
		boolean  flag = productManager.isHasProduct("digikey");
		assertEquals(true, flag);
		boolean flag2 = productManager.isHasProduct("digikey7895");
		assertEquals(false, flag2);
	}
	
	/**
	 * 查询销售中的商品 
	 * 
	 * @since 2017年6月27日
	 * @author tb.lijing@yikuyi.com
	 */
	@Test
	public void testProductSpeed(){
		long time = new Date().getTime();
		productManager.getSaleProductList(null, null, null, "837185337124978688", null, 138, null, null, "", "", "",true, 1, 20,null,null);
		System.out.println(new Date().getTime()-time);
	}
	
	
	/**
	 * 销售中的商品
	 * 要点:不传条件；
	 * 		传条件.
	 * @since 2017年2月22日
	 * @author zr.wujiajun@yikuyi.com
	*/
	/*@SuppressWarnings("unchecked")
	@Test
	public void testFindSaleProduct(){
		Map<String,Object> param = new HashMap<>();
		//1.不传条件时间，直接返回提示信息;不执行查询
		Map<String,Object> noCond =restTemplate.getForObject(host +"v1/products/getSaleProductList?"+"page=1&pageSize=20", Map.class);		
		assertNotNull(noCond.get("reason"));		
		
		//2.根据id查询
		String id="1486457802360465";		
		Map<String,Object> datas =restTemplate.getForObject(host +"v1/products/getSaleProductList?id="+id+"&page=1&pageSize=20", Map.class);
		assertEquals(1, (int)datas.get("page"));
		assertEquals(20, (int)datas.get("pageSize"));		
		//3.根据关键字查询
		String keyword = "SD160709";
		param.put("keyword", keyword);
		datas = restTemplate.getForObject(host +"v1/products/getSaleProductList?id="+id+"&page=1&pageSize=30&keyword="+keyword, Map.class);
		assertEquals(1, (int)datas.get("page"));
		assertEquals(30, (int)datas.get("pageSize"));
	}
	 */
	
	/**
	 * 商品推荐
	 * @since 2017年3月1日
	 * @author zr.wujiajun@yikuyi.com
	 */
	@Test
	public void testRecommendOthers(){
		Product product = this.initProductData();
		//查询当前商品
		List<Product> currentList = productManager.findBasicInfo(Arrays.asList(product.getId()));
		if(CollectionUtils.isNotEmpty(currentList)){
			Product currProduct = currentList.get(0);	
			List<ProductCategory> currCateList = currProduct.getSpu().getCategories();
			int cate3Id= -1;
			if(CollectionUtils.isNotEmpty(currCateList)){
				for(ProductCategory pc : currCateList){
					if(pc.getLevel() !=null && pc.getLevel()==3){
						cate3Id = pc.getId();
					}				
				}
			}			
			if(currProduct !=null){	
				List<ProductVo> datas = productManager.recommendOthers(product.getId(),String.valueOf(product.getSpu().getCategories().get(0).getId()));
				if(CollectionUtils.isNotEmpty(datas)){
					for(ProductVo vo:datas){
						//取相关商品的次小类
						List<ProductCategory> cateList = vo.getSpu().getCategories();
						int voCate3 = -1;
						if(CollectionUtils.isNotEmpty(cateList)){
							for(ProductCategory pc : cateList){
								if(pc.getLevel() !=null && pc.getLevel()==3){
									voCate3 = pc.getId();
								}				
							}
						}
						//验证次小类跟原来的一致
						assertEquals(cate3Id, voCate3);
						//相关商品id和原来商品不能相同
						assertNotEquals(Long.parseLong(currProduct.getId()),Long.parseLong(vo.getId()));
					}				
				}
			}
		}else{			
			assertTrue(CollectionUtils.isEmpty(currentList));
		}
	}
	
	/**
	 * 
	 *  更新产品
	 * @since 2017年6月27日
	 * @author tb.lijing@yikuyi.com
	 */
	@Test
	public void testUpdateProductInfo() {
		Product product = new Product();
		product.setId("1482150087598948");
		// 更新数据
		Product opProduct;
		try {
			opProduct = productManager.updateProduct(product,true);
			// 搜索引擎同步
			List<ProductVo> list = new ArrayList<>();
			MaterialVo materialVo = new MaterialVo();
			materialVo.setType(MaterialVo.MaterialVoType.UPDATE_DATA);
//			ObjectMapper mapper = new ObjectMapper();
			try {
				ProductVo productVo = new ProductVo();
				BeanUtils.copyProperties(opProduct,productVo);
				list.add(productVo);
				materialVo.setMsg(list);
			} catch (Exception e) {
				e.printStackTrace();
			}
			materialVo.setSize(1);
			msgSender.sendMsg(syncElasticsearchProductTopicName, materialVo, null);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 批量更新商品库存和价格
	 * 
	 * @since 2017年11月15日
	 * @author tb.lijing@yikuyi.com
	 */
	@Test
	public void testUpdateProductInfoBatch(){
		List<RawData> rawDatas = new ArrayList<>();
		RawData rawData = new RawData();
		rawData.setId("1482150087598948");
		rawDatas.add(rawData);
		productManager.updateProductInfoBatch(rawDatas);
	}
	
	/**
	 * 
	 * 字符串转int
	 * @since 2017年6月27日
	 * @author tb.lijing@yikuyi.com
	 */
	@Test
	public void testParseInt(){
		String str = "1.15";
		int qty = productManager.parseInt(str);
	}
	
	
	/**
	 * 测试根据ID查询商品
	 * @since 2017年6月27日
	 * @author zr.wanghong
	 */
	@Test
	public void testFindProductById(){
		Product product = this.initProductData();
		productManager.findProductById(product.getId());
	}

	/**
	 *测试删除销售中的商品 
	 * @since 2017年6月27日
	 * @author zr.wanghong
	 */
	@Test
	public void testDeleteSaleProduct(){
		Product product = this.initProductData();
		productManager.deleteSaleProduct(product.getId());
	}
	
	/**
	 * 测试查询根据制造商和型号查询商品信息
	 * @throws JsonProcessingException
	 * @since 2017年6月27日
	 * @author zr.wanghong
	 */
	@Test
	public void testFindFacturerAndPartNumber() throws JsonProcessingException{
		this.mockPartyService();
		JSONObject json = new JSONObject();
		json.put("id", "866936154572718080");
		json.put("displayName", "866936154572718080");
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(json);
		String responseStr = this.objectMapper.writeValueAsString(jsonArray);
		
		MockRestServiceServer server = this.mockRestServiceServer();
		//mock调用获取供应商的服务
		//创建paryty 的模拟rest服务，设定其响应url以及返回(参照业务实际要调用的其他应用的服务进行设计，返回该测试用例预期验证的结果）
		String partyUrl = this.getPartyUrl();
		String expectedUri = partyUrl.concat("/v1/party/allparty");
		server.expect(MockRestRequestMatchers.requestTo(expectedUri))
				.andExpect(MockRestRequestMatchers.method(HttpMethod.PUT))
				.andRespond(MockRestResponseCreators.withSuccess(responseStr, MediaType.TEXT_PLAIN));
		
		
		//初始化商品数据
		Product product = this.initProductData();
		String manufacturer = product.getSpu().getManufacturer();
		String manufacturerPartNumber = product.getSpu().getManufacturerPartNumber();
		List<ProductVo> productVos = productManager.findFacturerAndPartNumber(manufacturer, manufacturerPartNumber);
		//assertEquals(1, productVos.size());
	}
	
	@Test
	public void testAssociaWord(){
		String keyword = "1";
		productManager.associaWord(keyword);
	}
	
	/**
	 * 
	 * 管制商品
	 * @since 2017年6月27日
	 * @author tb.lijing@yikuyi.com
	 */
	@Test
	public void testSaleControl(){
		//初始化商品数据
		Product product = this.initProductData();
		productManager.saleControl(product.getId());
	}
	
	/**
	 * 查询商品全部信息,包含交期,实时价格等
	 * 
	 * @since 2017年6月27日
	 * @author tb.lijing@yikuyi.com
	 */
	@Test
	public void testFindFullInfoV2(){
		 Product product = this.initProductData();
		 List<String> ids = new ArrayList<>();
		 ids.add(product.getId());
		 productManager.findFullInfoV2(ids);
	}
	
//	/**
//	 * 
//	 * 根据spuId来查询商品
//	 * @since 2017年6月27日
//	 * @author tb.lijing@yikuyi.com
//	 */
//	@Test
//	public void testFindProductBySpuIds(){
//		Product product = this.initProductData();
//		List<String> spuIds = new ArrayList<>();
//		spuIds.add(product.getSpu().getSpuId());
//		
//		List<Product> list = productManager.findProductBySpuIds(spuIds);
//		assertNotNull(list);
//	}
	/**
	 * 生成文件
	 */
	@Test
	public void testExportExcelByCondition(){
		try {
			File file = productManager.exportExcelByCondition(new ProductRequest("1483689358853013", "AST1750MATRQ", "Mallory Sonalert Products Inc.", 
					"digikey", null,478, 527, 530, null, null, null, 
					null, null, null, null),"test");
			assertNotNull(file);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 根据型号查询商品
	 * @since 2017年6月27日
	 * @author tb.lijing@yikuyi.com
	 */
	@Test
	public void testMatchingProduct(){
		List<Product> list = new ArrayList<>();
		Product product = new Product();
		ProductImage productImage = new ProductImage();
		ProductStand productStand = new ProductStand();
		productStand.setManufacturer("测试制造商");
		productStand.setManufacturerPartNumber("测试型号");
		productImage.setType("large");
		productImage.setUrl("测试Url");
		List<ProductImage> imageList = new ArrayList<>();
		imageList.add(productImage);
		productStand.setImages(imageList);
		product.setSpu(productStand);
		product.setQuickFindKey("测试quickFindKey");
		list.add(product);
		productManager.saveProducts(list);
		String partNum = productStand.getManufacturerPartNumber();
		Product productResult = productManager.matchingProduct(partNum);
		assertNotNull(productResult);
		productManager.deleteProduct(product);
	}
	
	/**
	 * 
	 * 
	 * @since 2017年6月27日
	 * @author tb.lijing@yikuyi.com
	 */
	@Test
	public void testDeleteProductStand(){
		List<ProductStand> list = new ArrayList<>();
		ProductStand productStand = new ProductStand();
		productStand.setManufacturer("测试");
		productStand.setManufacturerPartNumber("测试001");
		productStand.setId("1222331454554");
		productStand.setSpuId("测试spuId");
		list.add(productStand);
		productStandManager.saveProductStands(list);
		productManager.deleteProductStand(productStand);
	}
	
	@Test
	public void testgetcount(){
		Long cnt = productManager.getCount("1511744401000");
		assertNotNull(cnt);
	}
	/**
	 * 批量创建sku信息
	 */
	@Test
	public void testCreateProduct1(){
		RawData data = this.initRawData();
		String result = productManager.createProduct(Arrays.asList(data), MaterialVoType.UPDATE_DATA);
		assertTrue(StringUtils.isBlank(result));
	}
	
	@Test
	public void testCreateProduct(){
		int size=2;
		for(int i=0;i<size;i++){
			List<RawData> datas = Lists.newArrayList();
			for (int j=0;j<3;j++) {
				datas.add(this.initRawData());
			}
//			Thread a = new Thread(new Runnable(){
//				@Override
//				public void run() {
//					try {
//						Thread.sleep(1);
//						synchronized (this) {
							productManager.createProduct(datas, MaterialVoType.UPDATE_DATA);
//						}
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			});
//			a.start();
		}
//		assertTrue(StringUtils.isBlank(result));
	}
	
	/**
	 * 初始化爬虫数据
	 * @return
	 */
	private RawData initRawData(){
		RawData data = new RawData();
		data.setManufacturer("AKRON");
		data.setManufacturerPartNumber("2241000059y");
//		data.setManufacturerPartNumber("TC-32349-000");
//		data.setSkuId("MP-ND"+new Random().nextInt(1000));
		data.setSkuId("1196525");
		data.setVendorName("956373757054681088");
		data.setVendorId("956373757054681088");
		data.setPackaging("Bulk");
		data.setCountryCode("US");
		data.setRohs("true");
		
		List<ProductCategory> categories = new ArrayList<>();
		ProductCategory category = new ProductCategory();
		category.setName("开发板");
		category.setStatus(1);
		category.setLevel(1);
		categories.add(category);
		
		category = new ProductCategory();
		category.setName("评估板配件");
		category.setStatus(1);
		category.setLevel(2);
		categories.add(category);
		
		category = new ProductCategory();
		category.setName("评估板其他配件");
		category.setStatus(1);
		category.setLevel(3);
		categories.add(category);
		data.setVendorCategories(categories);
		
		//参数
		List<ProductParameter> paramList = new ArrayList<>();
 		ProductParameter param = new ProductParameter();
 		param.setCode("paramCode111");
 		param.setName("paramName111");
 		param.setValue("paramValue111");
 		paramList.add(param);
 		ProductParameter param1 = new ProductParameter();
 		param1.setCode("paramCode222");
 		param1.setName("paramName2229");
 		param1.setValue("paramValue222");
 		paramList.add(param1);
 		ProductParameter param2 = new ProductParameter();
 		param2.setCode("paramCode99");
 		param2.setName("paramName99");
 		param2.setValue("paramValue99");
 		paramList.add(param2);
		data.setParameters(paramList);
		//图片
		List<ProductImage> productImages  = Lists.newArrayList();
		ProductImage  productImage = new ProductImage();
		productImage.setType("thumbnail");
		productImage.setUrl("//media.digikey.com/Photos/Knowles%20Acoustics%20Photos/TC-32349-000_sml.jpg");
		productImages.add(productImage);
		ProductImage  productImage1 = new ProductImage();
		productImage1.setType("standard");
		productImage1.setUrl("//media.digikey.com/Photos/Knowles%20Acoustics%20Photos/TC-32349-000_sml.jpg");
		productImages.add(productImage1);
		ProductImage  productImage2 = new ProductImage();
		productImage2.setType("large");
		productImage2.setUrl("//media.digikey.com/Photos/Knowles%20Acoustics%20Photos/TC-32349-000.jpg");
		productImages.add(productImage2);
		data.setImages(productImages);
		
		List<ProductPrice> prices = this.initPrices();
		data.setPrices(prices);
		data.setMinimumQuantity(1);
		List<Stock> stocks = Lists.newArrayList();
		Stock s = new Stock();
		s.setQuantity(89);
		s.setSource("英国仓");
		stocks.add(s);
		s = new Stock();
		s.setQuantity(88);
		s.setSource("美国仓");
		stocks.add(s);
		data.setStocks(stocks);
		return data;
	}
	/**
	 * 测试自动下架商品
	 * @author tongkun@yikuyi.com
	 */
	@Test
	public void testDownShelfStock(){
		//1、创建测试数据（不允许重复）
		List<Product> list = new ArrayList<>();
		for(int i = 1;i <= 5;i++){
			Product p = new Product();
			p.setId(String.valueOf(i));
			p.setUpdatedTimeMillis("1");
			p.setQuickFindKey(p.getId());
			p.setVendorId("test");
			p.setStatus(1);
			ProductStand spu = new ProductStand();
			spu.setId(String.valueOf(i));
			p.setSpu(spu);
			list.add(p);
		}
		productRepository.save(list);
		
		//2、执行
		productManager.downShelfStock("test", 2L);
		//不知道zeromq什么时候处理完成，就先等待2秒钟吧
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//3、验证
		Product first = productRepository.findOne("1");
		assertEquals("expired",first.getPriceStatus());
		assertEquals(first.getPrices().size(), 0);
		
		//删除测试数据
		productRepository.delete(list);
	}
	
	 /**
	  * 测试将商品更新为失效
	 * @author tongkun@yikuyi.com
	 */
	@Test
	public void testUpdateInvalidProduct(){
		//1、创建测试数据（不允许重复）
		List<Product> list = new ArrayList<>();
		for(int i = 1;i <= 5;i++){
			Product p = new Product();
			p.setId(String.valueOf(i));
			p.setUpdatedTimeMillis(String.valueOf(new Date().getTime()));
			p.setQuickFindKey(p.getId());
			p.setStatus(1);
			list.add(p);
		}
		productRepository.save(list);
		
		//2、执行
		productManager.updateInvalidProduct(list);
		
		//3、验证
		Product first = productRepository.findOne("1");
		assertEquals(first.getStatus(), new Integer(1));
		assertEquals(first.getPrices().size(), 0);
		assertNotNull(first.getPriceStatus());
		
		//删除测试数据
		productRepository.delete(list);
	 }
	
	@Test
	public void testSetActivityMiniQty(){
		ProductVo vo = new ProductVo();
		PriceInfo price = new PriceInfo();
		RawData data = createTestData();
		vo.setActivityId("12345");
		price.setResalePrices(data.getResalePrices());
		if(StringUtils.isNotEmpty(vo.getActivityId())){
			if(!CollectionUtils.isEmpty(price.getResalePrices())){
				price.getResalePrices().forEach( resalePrice -> {
					if("CNY".equals(resalePrice.getCurrencyCode())){
						int minimumQuantity = !CollectionUtils.isEmpty(resalePrice.getPriceLevels()) ? resalePrice.getPriceLevels().get(0).getBreakQuantity().intValue():1;
						vo.setMinimumQuantity(minimumQuantity);
					}
				});
			}else{
				vo.setMinimumQuantity(1);
			}
		}
	}
	
	@Test
	public void testHandleMinimumQuantity(){
		Product product = this.initProductData();
		productManager.handleMinimumQuantity(product);
		assertNotNull(product.getMinimumQuantity());
	}
	
	@Test
	public void testMergeMovInfo(){
		Product product = this.initProductData();
		ProductVo productVo = new ProductVo();
		BeanUtils.copyProperties(product, productVo);	
		List<ProductVo> productVos = new ArrayList<>();
		productVos.add(productVo);
		List<MovInfo> movInfos = movQueryManager.queryByEntities(productVos);
		}
	
	@Test
	public void testMergeSpecialOfferText(){
		Product product = this.initProductData();
		ProductVo productVo = new ProductVo();
		BeanUtils.copyProperties(product ,productVo);
		List<ProductVo> productVos = new ArrayList<>();
		productVos.add(productVo);
		productManager.mergeSpecialOfferText(productVos);
	}
	
	@Test
	public void testFindBatchMfrIdAndMpm(){
		List<ProductRequest> productRequests = Lists.newArrayList();
		ProductRequest productRequest = new ProductRequest();
		productRequest.setManufacturerId("1242");
		productRequest.setManufacturerPartNumber("1N4744A");
		productRequests.add(productRequest);
		
		ProductRequest productRequest2 = new ProductRequest();
		productRequest2.setManufacturerId("786");
		productRequest2.setManufacturerPartNumber("44440");
		productRequests.add(productRequest2);
		
		List<ProductVo> list = productManager.findBatchMfrIdAndMpm(productRequests);
		
		assertNotNull(list);
	}
}