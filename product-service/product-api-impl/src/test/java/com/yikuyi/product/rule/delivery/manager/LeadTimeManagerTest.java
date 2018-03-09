package com.yikuyi.product.rule.delivery.manager;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.yikuyi.category.model.ProductCategory;
import com.yikuyi.product.model.Product;
import com.yikuyi.product.model.ProductPrice;
import com.yikuyi.product.model.ProductPriceLevel;
import com.yikuyi.product.model.ProductStand;
import com.yikuyi.product.model.VendorSeries;
import com.yikuyi.rule.delivery.vo.DeliveryInfo;
import com.yikuyi.rule.delivery.vo.ProductInfo;
import com.yikuyi.rule.delivery.vo.ProductLeadTimeVo;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
	TransactionDbUnitTestExecutionListener.class })
@Rollback
public class LeadTimeManagerTest {
	
	@Autowired
	private LeadTimeManager leadTimeManager;
	
	@Autowired
	private CacheManager cacheManager;
	
	@Autowired
	private MongoRepository<Product, String> productRepository;
	
	/**
	 * 测试实时查询商品的交期
	 * 
	 * @since 2016年12月16日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	public void testGetLeadTimeList(){
		//初始化商品数据
		Product product = this.initData();
		Cache cache = cacheManager.getCache("leadTimeRule");
		//查询商品详情
		List<String> ids = new ArrayList<>();
		ids.add("1234");
		//没有找到相应的商品信息
		List<ProductInfo> infoList = leadTimeManager.getLeadTimeList(ids);
		//assertEquals(0,infoList.size());
		//交期规则为排单类型
		String key1 = "leadTimeRule.digikey-none-1";
		initCacheType1(key1,cache);
		ids = new ArrayList<>();
		ids.add(product.getId());
		List<ProductInfo> infoList2 = leadTimeManager.getLeadTimeList(ids);
		//assertEquals(2,infoList2.size());
		ProductInfo info1 = infoList2.get(0);
		//assertEquals("10-11",info1.getLeadTimeCH());
		cache.evict(key1);
		//交期规则为现货类型
		String key2 = "leadTimeRule.digikey-none-0";
		initCacheType0(key2,cache);
		ids = new ArrayList<>();
		product.setId("1482150087598925");
		product.setQty(123L);
		product.setSourceId("");
		product.setQuickFindKey("1482150087598925digkey");
		productRepository.delete(product);
		productRepository.insert(product);
		ids.add(product.getId());
		List<ProductInfo> infoList3 = leadTimeManager.getLeadTimeList(ids);
		//assertEquals(1,infoList3.size());
		ProductInfo info3 = infoList3.get(0);
		//assertEquals("0",info3.getLeadTimeCH());
		cache.evict(key2);
		//缓存中没有交期规则
		ids = new ArrayList<>();
		ids.add(product.getId());
		List<ProductInfo> infoList1 = leadTimeManager.getLeadTimeList(ids);
		//assertEquals(1,infoList1.size());
		ProductInfo info = infoList1.get(0);
		//assertEquals("0",info.getLeadTimeCH());		
	}
	
	/**
	 * 测试根据商品信息查询交期策略
	 * 
	 * @since 2017年3月17日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	public void testGetLeadTimeByProdcut(){
		Cache cache = cacheManager.getCache("leadTimeRule");
		//交期规则为排单类型
		String key1 = "leadTimeRule.digikey-none-1";
		initCacheType1(key1,cache);
		//交期规则为现货类型
		String key2 = "leadTimeRule.digikey-none-0";
		initCacheType0(key2,cache);
		List<ProductLeadTimeVo> infoList = new ArrayList<>();
		ProductLeadTimeVo info = new ProductLeadTimeVo();
		info.setVendorId("digikey");
		//排单
		info.setQty(0L);
		info.setSourceId("digikey-100");
		infoList.add(info);
		List<ProductLeadTimeVo> result1 = leadTimeManager.getLeadTimeByProdcut(infoList);
		assertEquals("现货交期",result1.get(0).getTemplateName());
		assertEquals("2-5",result1.get(0).getRealityList().get(1).getRealityLeadTime());
		assertEquals("5-10",result1.get(0).getRealityList().get(0).getRealityLeadTime());
		//现货
		info.setQty(123L);
		infoList = new ArrayList<>();
		infoList.add(info);
		List<ProductLeadTimeVo> result3 = leadTimeManager.getLeadTimeByProdcut(infoList);
		assertEquals("现货交期",result3.get(0).getTemplateName());
		assertEquals("2-5",result3.get(0).getRealityList().get(1).getRealityLeadTime());
		assertEquals("5-10",result3.get(0).getRealityList().get(0).getRealityLeadTime());
		//清除缓存
		cache.evict(key1);
		cache.evict(key2);
		//无交期策略
		List<ProductLeadTimeVo> result5 = leadTimeManager.getLeadTimeByProdcut(infoList);
		assertEquals("0",result5.get(0).getRealityList().get(0).getRealityLeadTime());
		assertEquals("0",result5.get(0).getRealityList().get(1).getRealityLeadTime());
	}
	
	/**
	 * 初始化现货缓存
	 * 
	 * @since 2017年3月17日
	 * @author zr.wenjiao@yikuyi.com
	 */
	private void initCacheType0(String key,Cache cache){
		DeliveryInfo info = new DeliveryInfo();
		info.setId("000");
		info.setDeliveryRuleName("现货交期");
		info.setProductType(0);
		info.setVerdonName("digikey");
		info.setWarehouse("none");
		info.setLeadTimeMinCH(2);
		info.setLeadTimeMaxCH(5);
		info.setLeadTimeMinHK(5);
		info.setLeadTimeMaxHK(10);
		cache.put(key, info);
	}
	
	/**
	 * 初始化排单缓存
	 * 
	 * @since 2017年3月17日
	 * @author zr.wenjiao@yikuyi.com
	 */
	private void initCacheType1(String key,Cache cache){
		DeliveryInfo info = new DeliveryInfo();
		info.setId("111");
		info.setDeliveryRuleName("排单交期");
		info.setProductType(1);
		info.setVerdonName("digikey");
		info.setWarehouse("none");
		//update by wanghong
		//info.setLeadTimeCH(10);
		//info.setLeadTimeHK(15);
		info.setFactoryLeadTimeMinCH(10);
		info.setFactoryLeadTimeMaxCH(11);
		info.setFactoryLeadTimeMinHK(15);
		info.setFactoryLeadTimeMaxHK(16);
		cache.put(key, info);
	}
	
	/**
	 * 初始化价格为Null产品测试数据
	 * @return
	 */
	private Product initData(){
		Product product = new Product();
		product.setId("1481342363311000");
		
		ProductStand spu = new ProductStand();
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
		product.setLeadTime("2");
		
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
		priceLevel.setBreakQuantity(1L);
		priceLevels.add(priceLevel);
		priceLevel = new ProductPriceLevel();
		priceLevel.setPrice("1980.865926");
		priceLevel.setBreakQuantity(5L);
		priceLevels.add(priceLevel);
		priceLevel = new ProductPriceLevel();
		priceLevel.setPrice("1966.408119");
		priceLevel.setBreakQuantity(10L);
		priceLevels.add(priceLevel);
		priceLevel = new ProductPriceLevel();
		priceLevel.setPrice("1923.0281928");
		priceLevel.setBreakQuantity(50L);
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
		priceLevel.setBreakQuantity(1L);
		priceLevels.add(priceLevel);
		priceLevel = new ProductPriceLevel();
		priceLevel.setPrice("243.604");
		priceLevel.setBreakQuantity(5L);
		priceLevels.add(priceLevel);
		priceLevel = new ProductPriceLevel();
		priceLevel.setPrice("241.826");
		priceLevel.setBreakQuantity(10L);
		priceLevels.add(priceLevel);
		priceLevel = new ProductPriceLevel();
		priceLevel.setPrice("236.4912");
		priceLevel.setBreakQuantity(50L);
		priceLevels.add(priceLevel);
		price.setPriceLevels(priceLevels);

		prices.add(price);
		return prices;
	}

}
