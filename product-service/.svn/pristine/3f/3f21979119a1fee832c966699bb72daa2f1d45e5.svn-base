package com.yikuyi.product.goods.impl;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.springboot.config.ObjectMapperHelper;
import com.framework.springboot.model.LoginUser;
import com.framewrok.springboot.web.LoginUserInjectionInterceptor;
import com.yikuyi.category.model.ProductCategory;
import com.yikuyi.product.ProductApplication;
import com.yikuyi.product.base.ProductApplicationTestBase;
import com.yikuyi.product.goods.manager.ProductManager;
import com.yikuyi.product.model.Product;
import com.yikuyi.product.model.ProductAttachment;
import com.yikuyi.product.model.ProductDocument;
import com.yikuyi.product.model.ProductImage;
import com.yikuyi.product.model.ProductParameter;
import com.yikuyi.product.model.ProductPrice;
import com.yikuyi.product.model.ProductPriceLevel;
import com.yikuyi.product.model.Stock;
import com.yikuyi.product.model.VendorSeries;
import com.yikuyi.product.vo.RawData;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = { ProductApplication.class })
public class ProductResourceTest extends ProductApplicationTestBase{
	
	
	private ObjectMapper mapper = ObjectMapperHelper.configeObjectMapper(new ObjectMapper());
	
	private MockMvc mockMvc;
	
	LoginUser loginUser = new LoginUser("9999999901", "admin", "9999999901", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
	
	@InjectMocks
	private ProductResource productResource;
	
	@Mock
	private ProductManager mockProductManager;
	
	public ProductResourceTest() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Before
	public void setUpBefore() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(productResource).build();
	}
	
	
	/**
	 * 
	 * @throws Exception
	 * @since 2017年11月27日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void testGetBasicInfoByIds() throws Exception{
		List<Product> list  = new ArrayList<>();
		Product product = new Product();
		product.setId("12345685623");
		list.add(product);
		
		List<String> listStr = new ArrayList<>();
		listStr.add("12345689265");
		
		when(mockProductManager.findBasicInfo(Mockito.anyList())).thenReturn(list);
		
	    mockMvc.perform(post("/v1/products/batch/basic").requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(listStr))).andExpect(status().isOk());

	}
	
	
/*	@Autowired
	private TestRestTemplate restTemplate; 

	@Autowired
	private RedisCacheManager redisCacheManagerNoTransaction;
	
	@Autowired
	private ProductManager productManager;
	
	@Autowired
	private BrandManager brandManager;
	
	@Autowired
	private ProductStandManager productStandManager;
	
	
	private String host;*/

	/**
	 * @throws java.lang.Exception
	 * @since 2016年12月9日
	 * @author liudian@yikuyi.com
	 */
	/*@Before*/
	/*public void setUpBefore() throws Exception {
		this.host = "http://localhost" + ":" + this.getPort();
		RestTemplate rt = this.restTemplate.getRestTemplate();
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		ObjectMapper om = new ObjectMapper();
		om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		converter.setObjectMapper(om);
		rt.setMessageConverters(Arrays.asList(new HttpMessageConverter[]{converter}));
		rt.setRequestFactory(new SimpleClientHttpRequestFactory() {
			@Override
        	public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
        		ClientHttpRequest request = super.createRequest(uri, httpMethod);
        		HttpHeaders header = request.getHeaders();
        		header.add("Authorization", "Basic " + Base64Utils.encodeToString(("admin"+":"+"9999999901").getBytes()));
        		return request;
        	}
		});
		
	}*/
	
	
	
	
	
	/**
	 * 创建一条测试数据
	 * @return
	 * @since 2016年12月17日
	 * @author tongkun@yikuyi.com
	 */
	public static RawData createTestData(){
		RawData data = new RawData();
		data.setCountryCode("HK");
		data.setDescription("abc.");
		data.setFrom("upload");
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
		data.setManufacturer("sony");
		data.setManufacturerPartNumber("abcdefg");
		data.setSpuId((data.getManufacturerPartNumber()+"-"+data.getManufacturer()).toUpperCase());
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
		level.setBreakQuantity(123l);
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
		cate.setName("RESISTOR HARDWARE");
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
		data.setQuickFindKey("abcdefg");
		
		return data;
	}
	
	
	
}
