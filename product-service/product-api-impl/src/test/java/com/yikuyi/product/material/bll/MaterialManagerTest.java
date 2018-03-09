/*
 * Created: 2016年12月19日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.material.bll;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.yikuyi.category.model.ProductCategory;
import com.yikuyi.document.model.Document.DocumentStatus;
import com.yikuyi.material.vo.MaterialVo;
import com.yikuyi.material.vo.MaterialVo.MaterialVoType;
import com.yikuyi.party.PartyClientBuilder;
import com.yikuyi.party.facility.model.Facility;
import com.yikuyi.party.resource.FacilityClient;
import com.yikuyi.product.basicmaterial.bll.BasicMaterialManager;
import com.yikuyi.product.model.ProductAttachment;
import com.yikuyi.product.model.ProductDocument;
import com.yikuyi.product.model.ProductImage;
import com.yikuyi.product.model.ProductParameter;
import com.yikuyi.product.model.ProductPrice;
import com.yikuyi.product.model.ProductPriceLevel;
import com.yikuyi.product.model.ProductStand;
import com.yikuyi.product.model.Stock;
import com.yikuyi.product.vo.ProductVo;
import com.yikuyi.product.vo.RawData;
import com.yikuyi.rule.delivery.vo.ProductInfo;
import com.ykyframework.model.IdGen;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, 
	DirtiesContextTestExecutionListener.class,TransactionDbUnitTestExecutionListener.class, MockitoTestExecutionListener.class})
@Transactional(propagation=Propagation.REQUIRES_NEW)
@Rollback
public class MaterialManagerTest {
	
	@Autowired
	private MaterialManager materialManager;
	
	@Autowired
	private CacheManager redisCacheManagerNoTransaction;
	
	@Autowired
	private BasicMaterialManager basicMaterialManager;
	
	@SpyBean
	public PartyClientBuilder shipmentClientBuilder;
	
	//TODO
	//这个单元测试因为文件被迁移而无法通过，需要找一个新的文件，从本地上传后再通过阿里云解析。不能再使用原方法
//	@Test
	//@DatabaseSetup(type = DatabaseOperation.REFRESH, value =  "classpath:com/yikuyi/product/material/parseImportFile_sample.xml" )
	//@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "classpath:com/yikuyi/product/material/parseImportFile_result.xml")
	public void testParseImportFile(){
		String fileUrl = "https://ictrade-private-hz-uat.oss-cn-hangzhou.aliyuncs.com/sit/productUpload/materialUpload/201710/27/4b522b3f70dbb0b1bb5acad136e5ba8a.xlsx";
		String docId = String.valueOf(IdGen.getInstance().nextId());
		String vendorId = "sunrayhk";
		MaterialVo materialVo = new MaterialVo();
		materialVo.setDocId(docId);
		materialVo.setFileUrl(fileUrl);
		materialVo.setVendorId(vendorId);
		
		List<Facility> fs = new ArrayList<>();
		FacilityClient fc = Mockito.spy(FacilityClient.class);
		Mockito.when(shipmentClientBuilder.facilityResource()).thenReturn(fc);
		Mockito.when(fc.getFacilityList(Mockito.anyObject())).thenReturn(fs);
		
		String fileName = materialManager.parseImportFile(materialVo);
		Assert.assertNotNull(fileName);
	}
	
	@Test
//	@DatabaseSetup(type = DatabaseOperation.REFRESH, value =  "classpath:com/yikuyi/product/material/syncElasticsearchProduct_sample.xml" )
//	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "classpath:com/yikuyi/product/material/syncElasticsearchProduct_result.xml")
	public void testSyncElasticsearchProduct() throws JsonProcessingException, UnsupportedEncodingException{
		String docId = "8547545751215564";
		
		Cache materialImportCache = redisCacheManagerNoTransaction.getCache(MaterialManager.MATERIAL_IMPORT_CACHE_NAME_KEY);
		materialImportCache.put(String.format(MaterialManager.MATERIAL_IMPORT_CACHE_KEY, docId), 1);
		materialImportCache.put(String.format(MaterialManager.MATERIAL_IMPORT_CACHE_STATUS_KEY, docId), DocumentStatus.DOC_IN_PROCESS);
		
		List<ProductVo> voList = new ArrayList<>();
		//产品 1.
		ProductVo p1 = new ProductVo();
		p1.setId("8754212121");
		p1.setSkuId("AMS3459LS24-10PW-ND");
		p1.setVendorId("vendorId");
		p1.setVendorName("vendorId");
		p1.setPackaging("packaging");
		p1.setCountryCode("US");
		p1.setMinimumQuantity(2);
		p1.setStoreId(9999999L);
		p1.setQty(50L);
		
		//产品1 库存
		Stock s1 = new Stock();
		s1.setSource("100");
		s1.setQuantity(2);
		s1.setLeadTime("112");
		
		Stock s2 = new Stock();
		s2.setSource("200");
		s2.setQuantity(50);
		s2.setLeadTime("400");
		List<Stock> stocks = new ArrayList<>();
		stocks.add(s1);
		stocks.add(s2);
		
		ProductStand ps = new ProductStand();
		ps.setId("88745545454");
		ps.setManufacturer("Amphenol Aerospace Operations");
		ps.setManufacturerId(72);
		ps.setManufacturerPartNumber("MS3459LS24-10PW");
		ps.setRohs(true);
		ps.setDescription("7 Position Circular Connector Plug, Male Pins Crimp Silver");
				
		//图片信息
		List<ProductImage> images = new ArrayList<ProductImage>();
		ProductImage large = new ProductImage();
		large.setType("large");
		large.setUrl("//media.digikey.com/photos/nophoto/pna_en.jpg");
		
		ProductImage samll = new ProductImage();
		samll.setType("samll");
		samll.setUrl("//media.digikey.com/photos/Spectra%20Symbol/FS-L-0112-103-ST_sml.jpg");
		
		ProductImage thumbnail = new ProductImage();
		thumbnail.setType("thumbnail");
		thumbnail.setUrl("//media.digikey.com/photos/nophoto/pna_en.jpg");
		
		images.add(large);images.add(samll);images.add(thumbnail);
		ps.setImages(images);
		
		//类被信息
		List<ProductCategory> cateList = new ArrayList<ProductCategory>();
		//大类
		ProductCategory level1 = new ProductCategory();
		level1.setId(1);
		level1.setLevel(1);
		level1.setName("大类1");
		
		ProductCategory level2 = new ProductCategory();
		level2.setId(11);
		level2.setLevel(2);
		level2.setName("小类2");
//		level2.setParent(level1);
		
		ProductCategory level3 = new ProductCategory();
		level3.setId(111);
		level3.setLevel(3);
		level3.setName("次小类2");
//		level3.setParent(level2);
	
		cateList.add(level1);cateList.add(level2);cateList.add(level3);
		ps.setCategories(cateList);
		
		//参数信息
		List<ProductParameter> parameters = new ArrayList<ProductParameter>();
		
		ProductParameter  pa1 = new ProductParameter();
		pa1.setCode("code1");
		pa1.setName("name1");
		pa1.setValue("value1");
		
		ProductParameter  pa2 = new ProductParameter();
		pa2.setCode("code2");
		pa2.setName("name2");
		pa2.setValue("value2");
		
		parameters.add(pa1);parameters.add(pa2);
		
		ps.setParameters(parameters);
		
		//附件数据
		ProductAttachment pat1 = new ProductAttachment();
		pat1.setName("doc1");
		pat1.setUrl("www.asdfasdf.pdf");
		
		ProductAttachment pat2 = new ProductAttachment();
		pat2.setName("doc1");
		pat2.setUrl("www.hao123.com");
		
		ProductDocument pdoc =  new ProductDocument();
		pdoc.setType("datasheets");
		pdoc.setUrl("asdfsa.pdf");
		List<ProductAttachment> patList = new ArrayList<ProductAttachment>();
		patList.add(pat1);patList.add(pat2);
		
		pdoc.setAttaches(patList);
		
		List<ProductDocument> pdocList = new ArrayList<ProductDocument>();
		pdocList.add(pdoc);
		
		ps.setDocuments(pdocList);
		
		p1.setSpu(ps);
		
		//价格信息
		List<ProductPrice> prices1 = new ArrayList<ProductPrice>(); 
		//美元价格梯度
		ProductPrice pp1 = new ProductPrice();
		pp1.setCurrencyCode("USD");
		pp1.setUnitPrice("4554.41");
		
		List<ProductPriceLevel> usdpl1 = new ArrayList<ProductPriceLevel>();
		ProductPriceLevel usdppl1 = new ProductPriceLevel();
		usdppl1.setBreakQuantity(4l);
		usdppl1.setPrice("55.4522");
		
		ProductPriceLevel usdppl2 = new ProductPriceLevel();
		usdppl2.setBreakQuantity(100l);
		usdppl2.setPrice("35.4522");
		
		usdpl1.add(usdppl1); usdpl1.add(usdppl2);
		pp1.setPriceLevels(usdpl1);
		
		//人民币价格梯度
		ProductPrice ppcny = new ProductPrice();
		ppcny.setCurrencyCode("CNY");
		ppcny.setUnitPrice("4554.41");
		
		List<ProductPriceLevel> cnypl1 = new ArrayList<ProductPriceLevel>();
		ProductPriceLevel cnyppl1 = new ProductPriceLevel();
		cnyppl1.setBreakQuantity(4l);
		cnyppl1.setPrice("145.4522");
		
		ProductPriceLevel cnyppl2 = new ProductPriceLevel();
		cnyppl2.setBreakQuantity(100l);
		cnyppl2.setPrice("135.8522");		
		
		cnypl1.add(cnyppl1); cnypl1.add(cnyppl2);
		ppcny.setPriceLevels(cnypl1);
		
		prices1.add(ppcny); prices1.add(pp1);
		
		p1.setPrices(prices1);
		//附件信息		
		
		ProductInfo pif  = new ProductInfo();
		pif.setLeadTimeCH("3天到5天");
		pif.setLeadTimeHK("6天到8天");
		pif.setProductId(p1.getId());
		p1.setRealLeadTime(pif);
		
		p1.setProcessId(docId);
		p1.setLineNo(1L);
		voList.add(p1);
		
		MaterialVo materialVo = new MaterialVo();
		materialVo.setMsg(voList);
		materialVo.setType(MaterialVoType.FILE_UPLOAD);
		materialVo.setSize(1);
		materialManager.syncElasticsearchProduct(materialVo);
	}
	
/*	@Test
	public void testDeleteFile(){
		String fileName = "//appdata/tes.xml";
		Assert.assertFalse(materialManager.deleteFile(fileName));
	}*/
	
	@Test
	public void testCancelImport(){
		materialManager.cancelImport(String.valueOf(IdGen.getInstance().nextId()));
	}
	
	@Test
	public void testCreateProductCancelTheJudgment(){
		List<RawData> rawDatas = new ArrayList<>();
		RawData rawData = new RawData();
		rawData.setProcessId("1234564562322243");
		rawDatas.add(rawData);
		
		Cache materialImportCache = redisCacheManagerNoTransaction.getCache(MaterialManager.MATERIAL_IMPORT_CACHE_NAME_KEY);
		materialImportCache.put(String.format(MaterialManager.MATERIAL_IMPORT_CACHE_KEY, "1234564562322243"), 1);
		materialImportCache.put(String.format(MaterialManager.MATERIAL_IMPORT_CACHE_STATUS_KEY, "1234564562322243"), DocumentStatus.DOC_CANCEL);
		
		materialManager.createProductCancelTheJudgment(rawDatas);
	}
		
	//party服务地址
	@Value("${api.party.serverUrlPrefix}")
	private String partyUrl;

	@Autowired
	private RestTemplate restTemplateMock;
	
	public MockRestServiceServer mockRestServiceServer() {
		return MockRestServiceServer.bindTo(restTemplateMock).build();
	}
		
	/**
	 * 测试物料检测
	 * @throws UnsupportedEncodingException
	 * @since 2017年2月25日
	 * @author zr.wanghong
	 */
	@Test
	public void testMaterialDetectionNew() throws UnsupportedEncodingException{
		String fileUrl = "https://ictrade-private-hz-uat.oss-cn-hangzhou.aliyuncs.com/sit/productUpload/basicMaterialUpload/201710/27/7c082b23a2822e95c26f2acf8761ddac.xlsx";
		materialManager.materialDetectionNew(fileUrl, System.out);
	}
	
	@Test
	public void testBasicMaterialParseImportFile(){
		MaterialVo materialVo = new MaterialVo();
		materialVo.setDocId("5454545454");
		basicMaterialManager.basicMaterialParseImportFile(materialVo);
	}
	
	/**
	 * 
	 * @since 2017年11月23日
	 * @author tb.lijing@yikuyi.com
	 */
	@Test
	public void testFindFacilityInfo(){
		String facilityId = materialManager.findFacilityId("future", "Region","123");
		Assert.assertNotNull(facilityId);
	}
	
}