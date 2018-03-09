package com.yikuyi.product.goods.impl;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yikuyi.brand.model.ProductBrand;
import com.yikuyi.category.model.ProductCategory;
import com.yikuyi.product.base.ProductApplicationTestBase;
import com.yikuyi.product.brand.manager.BrandManager;
import com.yikuyi.product.goods.manager.ProductManager;
import com.yikuyi.product.goods.manager.ProductStandManager;
import com.yikuyi.product.model.Product;
import com.yikuyi.product.model.ProductAttachment;
import com.yikuyi.product.model.ProductDocument;
import com.yikuyi.product.model.ProductImage;
import com.yikuyi.product.model.ProductParameter;
import com.yikuyi.product.model.ProductStand;
import com.yikuyi.product.vo.RawData;

@RunWith(SpringRunner.class)
public class ProductStandResourceTest extends ProductApplicationTestBase{
	
	@Autowired
	private TestRestTemplate restTemplate; 
	
	@Autowired
	private MongoRepository<ProductStand, String> productStandRepository;
	
	@Autowired
	private ProductManager productManager;
	
	@Autowired
	private ProductStandManager productStandManager;
	
	@Autowired
	private BrandManager brandManager;
	
	@LocalServerPort
	private int port;
	
	private String host;
		
	/**
	 * @throws java.lang.Exception
	 * @since 2016年12月9日
	 * @author liudian@yikuyi.com
	 */
	@Before
	public void setUpBefore() throws Exception {
		this.host = "http://localhost" + ":" + this.port;
		
		RestTemplate rt = this.restTemplate.getRestTemplate();
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		ObjectMapper om = new ObjectMapper();
		om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
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
	}
	
	/**
	 * 测试物料列表
	 * 
	 * @since 2017年3月31日
	 * @author zr.wujiajun@yikuyi.com
	 
	@Test
	public void testList(){
		String manufacturerPartNumber="PK-35N29PQ";
		String keyword ="PK-35N";			
		restTemplate.getForObject(host+"/v1/products/stand?manufacturerPartNumber="+manufacturerPartNumber+"&keyword="+keyword, PageInfo.class);			
	}*/
	
	
	/**
	 * 测试查询单个物料数据
	 * 
	 * @since 2017年2月23日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	public void testGetProductStand(){
		ProductStand stand = this.initData();
		System.out.println("\n调用查询单个物料数据--(GET)"+host + "/v1/products/stand/"+stand.getId());
		
		this.mockPartyService();
		
		ResponseEntity<ProductStand> response = restTemplate.exchange(host + "/v1/products/stand/"+stand.getId(), HttpMethod.GET, null, ProductStand.class);
		ProductStand info = response.getBody();
		Assert.assertEquals("ManufacturerName",info.getManufacturer());
		Assert.assertEquals("spuId112233",info.getSpuId());
		productStandRepository.delete(stand);
	}

	
	/**
	 * 测试创建单个物料
	 * @since 2017年3月4日
	 * @author tongkun@yikuyi.com
	 */
//	@Test
	public void testCreateProductStand(){
		//获得品牌映射
		Map<String,ProductBrand> brandAliasMap = brandManager.getAliasBrandMap();
		RawData data = ProductResourceTest.createTestData();//获得测试数据
		data.setCantCreateStand(true);//不创建标准数据
		List<RawData> list = new ArrayList<>();
		list.add(data);
		
		this.mockPartyService();
		
		//调用接口创建数据
		HttpEntity<List<RawData>> entity = new HttpEntity<>(list);
		restTemplate.exchange(host + "/v1/products", HttpMethod.POST, entity, new ParameterizedTypeReference<String>(){});
		//品牌错误的
		data.setSkuId(null);
		data.setManufacturer("abcdefghijklmn");
		entity = new HttpEntity<>(list);
		
		this.mockPartyService();
		
		restTemplate.exchange(host + "/v1/products/stand", HttpMethod.POST, entity, new ParameterizedTypeReference<String>(){});
		//测试结果
		data.setSpuId(ProductStandManager.calSpuId(data, brandAliasMap.get(data.getManufacturer().toUpperCase())));
		List<ProductStand> sList = productStandManager.findProductStandBySpuId(data, null);
		assertEquals(0, sList.size());//失败
		//分类错误的
		data.setSkuId(null);
		data.setManufacturer("Knowles");
		List<ProductCategory> cates = new ArrayList<ProductCategory>();
		ProductCategory cate = new ProductCategory();
		cate.setLevel(1);
		cate.setName("mmmssss");
		cates.add(cate);
		cate = new ProductCategory();
		cate.setLevel(2);
		cate.setName("dddddd");
		cates.add(cate);
		data.setVendorCategories(cates);
		entity = new HttpEntity<>(list);
		
		this.mockPartyService();
		restTemplate.exchange(host + "/v1/products/stand", HttpMethod.POST, entity, new ParameterizedTypeReference<String>(){});
		//测试结果
		data.setSpuId(ProductStandManager.calSpuId(data, brandAliasMap.get(data.getManufacturer().toUpperCase())));
		sList = productStandManager.findProductStandBySpuId(data, null);
		assertEquals(0, sList.size());//失败
		//正确创建
		data.setSkuId(null);
		cates = new ArrayList<ProductCategory>();
		cate = new ProductCategory();
		cate.setLevel(1);
		cate.setName("INTEGRATED CIRCUITS (ICS)");
		cates.add(cate);
		cate = new ProductCategory();
		cate.setLevel(2);
		cate.setName("PMIC - CURRENT REGULATION/MANAGEMENT");
		cates.add(cate);
		data.setVendorCategories(cates);
		entity = new HttpEntity<>(list);
		
		this.mockPartyService();
		
		restTemplate.exchange(host + "/v1/products/stand", HttpMethod.POST, entity, new ParameterizedTypeReference<String>(){});
		//测试结果
		data.setSpuId(ProductStandManager.calSpuId(data, brandAliasMap.get(data.getManufacturer().toUpperCase())));
		sList = productStandManager.findProductStandBySpuId(data, null);
		assertEquals(1, sList.size());//成功
		
		//清除数据
		List<Product> result = productManager.findProductByRawDatas(list, brandAliasMap);
		if(result.size()>0){
			for(Product p:result)
				productManager.deleteProduct(p);
		}
		for(ProductStand ps:sList){
			productStandManager.deleteProductStand(ps);
		}
	}
	
	/**
	 * 初始化物料数据
	 * @return
	 * @since 2017年2月23日
	 * @author zr.wenjiao@yikuyi.com
	 */
	private ProductStand initData(){
		ProductStand psInfo = new ProductStand();
		psInfo.setId("112233");
		psInfo.setSpuId("spuId112233");
		psInfo.setManufacturer("ManufacturerName");
		psInfo.setManufacturerId(1234);
		psInfo.setManufacturerPartNumber("ManufacturerPartNumber");
		psInfo.setDescription("DescriptionDescription");
		psInfo.setManufacturerAgg("setManufacturerAgg");
		psInfo.setRohs(true);
		//分类
		List<ProductCategory> listCat = new ArrayList<>();
		ProductCategory cat = new ProductCategory();
		cat.setId(1);
		cat.setName("大类");
		cat.setLevel(1);
		cat.setStatus(1);
		listCat.add(cat);
		ProductCategory cat1 = new ProductCategory();
		cat1.setId(2);
		cat1.setName("小类");
		cat1.setLevel(2);
		cat1.setStatus(1);
		listCat.add(cat1);
		ProductCategory cat2 = new ProductCategory();
		cat2.setId(3);
		cat2.setName("次小类");
		cat2.setLevel(3);
		cat2.setStatus(1);
		listCat.add(cat2);
		psInfo.setCategories(listCat);
		//文档
		List<ProductDocument> pdList = new ArrayList<>();
 		ProductDocument pd = new ProductDocument();
 		List<ProductAttachment> attachList = new ArrayList<>();
 		ProductAttachment attach = new ProductAttachment();
 		attach.setName("AttachmentName");
 		attach.setUrl("AttachmentUrl");
 		attachList.add(attach);
 		pd.setAttaches(attachList);
 		pd.setDescription("documentDescription");
 		pd.setName("documentName");
 		pd.setType("documentType");
 		pd.setUrl("documentUrl");
 		pdList.add(pd);
 		psInfo.setDocuments(pdList);
 		//图片
 		List<ProductImage> imageList = new ArrayList<>();
 		ProductImage image = new ProductImage();
 		image.setType("imageType");
 		image.setUrl("imageUrl");
 		imageList.add(image);
 		psInfo.setImages(imageList);
 		//物料参数
 		List<ProductParameter> paramList = new ArrayList<>();
 		ProductParameter param = new ProductParameter();
 		param.setCode("paramCode111");
 		param.setName("paramName111");
 		param.setValue("paramValue111");
 		paramList.add(param);
 		ProductParameter param1 = new ProductParameter();
 		param1.setCode("paramCode222");
 		param1.setName("paramName222");
 		param1.setValue("paramValue222");
 		paramList.add(param1);
 		psInfo.setParameters(paramList);
 		productStandRepository.delete(psInfo);
 		productStandRepository.insert(psInfo);
		return psInfo;
	}
}
