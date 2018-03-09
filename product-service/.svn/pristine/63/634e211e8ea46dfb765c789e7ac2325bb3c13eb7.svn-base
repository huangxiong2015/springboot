package com.yikuyi.product.rule.delivery.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.yikuyi.category.model.ProductCategory;
import com.yikuyi.product.base.ProductApplicationTestBase;
import com.yikuyi.product.model.Product;
import com.yikuyi.product.model.ProductPrice;
import com.yikuyi.product.model.ProductPriceLevel;
import com.yikuyi.product.model.ProductStand;
import com.yikuyi.product.model.VendorSeries;
import com.yikuyi.rule.delivery.vo.ProductInfo;

@RunWith(SpringRunner.class)
@Transactional
@Rollback
public class LeadTimeResourceTest extends ProductApplicationTestBase{
	
	@Autowired
	private TestRestTemplate restTemplate; 
	
	private String host;
	
	@Autowired
	private MongoRepository<Product, String> productRepository;
	
	@Before
	public void setUpBefore() throws Exception {
		this.host = "http://localhost" + ":" + this.getPort();
	}
	
	/**
	 * 测试根据商品Id实时查询交期信息
	 * 
	 * @since 2017年3月17日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	public void testGetLeadTimeList(){
		//初始化商品信息
		Product product = this.initData();
		List<String> ids = new ArrayList<String>();
		ids.add(product.getId());
		HttpEntity<List<String>> entity = new HttpEntity<List<String>>(ids);
		this.mockPartyService();
		ResponseEntity<List<ProductInfo>> response = restTemplate.exchange(host + "/v1/products/batch/leadtime", HttpMethod.POST, entity, new ParameterizedTypeReference<List<ProductInfo>>(){});
		List<ProductInfo> productInfos =  response.getBody();
		assertEquals(1, productInfos.size());
		/*assertEquals("0",productInfos.get(0));
		assertEquals("0",productInfos.get(0));*/
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
