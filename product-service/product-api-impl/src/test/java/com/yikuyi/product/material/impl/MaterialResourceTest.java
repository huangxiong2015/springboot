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
package com.yikuyi.product.material.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yikuyi.material.vo.MaterialVo;
import com.yikuyi.material.vo.MaterialVo.MaterialVoType;
import com.yikuyi.product.base.ProductApplicationTestBase;
import com.yikuyi.product.vo.ProductVo;
import com.ykyframework.model.IdGen;

/**
 * @author zr.wanghong
 * @version 1.0.0
 * @since 2016-12-21
 * @see com.yikuyi.product.rule.price.impl.PriceRulesResource
 * @see com.yikuyi.product.goods.manager.PriceQueryManager
 * @see com.yikuyi.product.rule.price.manager.ProductPriceRuleManager
 */
public class MaterialResourceTest extends ProductApplicationTestBase{
	
	@Autowired
	private TestRestTemplate restTemplate; // = new TestRestTemplate();	
	
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

	@Test
	public void testupload() {
		MaterialVo vo = new MaterialVo();
		vo.setFileUrl("http://ictrade-private-hz-uat.oss-cn-hangzhou.aliyuncs.com/dev/register/approval/9b/c2a/9bc2a35e74866466e905b1e5758aeaba.xlsx");
		vo.setOriFileName("上传第一个模板teste3.xlsx");
		vo.setVendorId("vendorId");
		vo.setRegionId("regionId");
		HttpEntity<MaterialVo> entity = new HttpEntity<>(vo);
		this.mockPartyService();
		restTemplate.exchange(host + "/v1/imports/notification/material", HttpMethod.POST, entity, new ParameterizedTypeReference<String>(){});
		vo.setVendorId("future");
		this.mockPartyService();
		restTemplate.exchange(host + "/v1/imports/notification/material", HttpMethod.POST, entity, new ParameterizedTypeReference<String>(){});
		vo.setRegionId(null);
		this.mockPartyService();
		restTemplate.exchange(host + "/v1/imports/notification/material", HttpMethod.POST, entity, new ParameterizedTypeReference<String>(){});
	}
	
	@Test
	public void testLogHistoryDownload() {
		this.mockPartyService();
		restTemplate.getForEntity(host + "/v1/imports/history/821572504538578944", null);
	}
	
	@Test
	public void testLogHistory(){
		this.mockPartyService();
		restTemplate.getForEntity(host + "/v1/imports/history", null);
	}
	
	@Test
	public void tescancelImport(){
		this.mockPartyService();
		HttpEntity<String> entity = new HttpEntity<>(String.valueOf(IdGen.getInstance().nextId()));
		restTemplate.exchange(host + "/v1/imports/notification/cancel", HttpMethod.POST, entity, new ParameterizedTypeReference<String>(){});
	}
	
	
	@Test
	public void testsyncupdate() throws Exception {
		MaterialVo vo = new MaterialVo();
		vo.setDocId("");
		vo.setType(MaterialVoType.UPDATE_DATA);
		vo.setMsg(new ArrayList<ProductVo>());
		HttpEntity<MaterialVo> entity = new HttpEntity<>(vo);
		this.mockPartyService();
		restTemplate.exchange(host + "/v1/imports/notification/syncupdate", HttpMethod.POST, entity, new ParameterizedTypeReference<String>(){});
		ObjectMapper mapper = new ObjectMapper();
		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, ProductVo.class);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		List<ProductVo> voList = mapper.readValue(			"[{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"spu\":{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"spuId\":\"MSE14LSU2-MALLORY\",\"manufacturer\":\"MALLORY\",\"manufacturerId\":\"710\",\"manufacturerPartNumber\":\"MSE14LSU2\",\"manufacturerShort\":null,\"description\":\"DC Buzzer Speed Up, Fast 3.9kHz Piezo 7 ~ 14V 85dB @ 7V, 30cm\",\"manufacturerAgg\":\"MALLORY\",\"rohs\":true,\"categories\":[{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"status\":1,\"cateAlias\":null,\"parent\":null,\"_id\":478,\"cateName\":\"机电产品\",\"cateLevel\":1},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"status\":1,\"cateAlias\":null,\"parent\":{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"status\":1,\"cateAlias\":null,\"parent\":null,\"_id\":478,\"cateName\":\"机电产品\",\"cateLevel\":1},\"_id\":527,\"cateName\":\"音频产品\",\"cateLevel\":2},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"status\":1,\"cateAlias\":[{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"level1\":null,\"level2\":\"音频产品 - 指示器、警报器\"},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"level1\":null,\"level2\":\"AUDIO PRODUCTS - INDICATORS & ALERTS\"},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"level1\":\"AUDIO PRODUCTS\",\"level2\":\"ALARMS, BUZZERS, AND SIRENS\"},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"level1\":null,\"level2\":\"AUDIO INDICATORS & ALERTS\"},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"level1\":null,\"level2\":\"音频产品 - 指示器、警报器\"},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"level1\":null,\"level2\":\"AUDIO PRODUCTS - INDICATORS & ALERTS\"},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"level1\":\"AUDIO PRODUCTS\",\"level2\":\"BUZZER ELEMENTS, PIEZO BENDERS\"},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"level1\":null,\"level2\":\"AUDIO INDICATORS & ALERTS\"}],\"parent\":{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"status\":1,\"cateAlias\":null,\"parent\":{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"status\":1,\"cateAlias\":null,\"parent\":null,\"_id\":478,\"cateName\":\"机电产品\",\"cateLevel\":1},\"_id\":527,\"cateName\":\"音频产品\",\"cateLevel\":2},\"_id\":530,\"cateName\":\"音频产品 - 指示器、警报器\",\"cateLevel\":3}],\"documents\":[{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"attaches\":[{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"name\":\"MSE14LSU2\",\"url\":\"http://www.mallory-sonalert.com/specifications/MSE14LSU2.PDF\"}],\"description\":\"\",\"name\":\"Datasheets\",\"type\":\"datasheets\",\"url\":\"http://www.mallory-sonalert.com/specifications/MSE14LSU2.PDF\"},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"attaches\":[{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"name\":\"MSESU2\",\"url\":\"http://mallory-sonalert.com/sounds/MSESU2.wav\"}],\"description\":\"\",\"name\":\"Sound File\",\"type\":\"soundFile\",\"url\":\"http://mallory-sonalert.com/sounds/MSESU2.wav\"},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"attaches\":[{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"name\":\"MSE Series Miniature Board-Mount Buzzers\",\"url\":\"http://www.digikey.com/en/product-highlight/m/mallory-sonalert/mse-series-miniature-board-mount-buzzers\"}],\"description\":\"\",\"name\":\"Featured Product\",\"type\":\"featuredProduct\",\"url\":\"http://www.digikey.com/en/product-highlight/m/mallory-sonalert/mse-series-miniature-board-mount-buzzers\"},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"attaches\":[{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"name\":\"MSO.igs\",\"url\":\"http://www.mallory-sonalert.com/3D/MSO%20IGS.zip\"},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"name\":\"MSO.stp\",\"url\":\"http://www.mallory-sonalert.com/3D/MSO%20STP.zip\"},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"name\":\"MSO.sat\",\"url\":\"http://www.mallory-sonalert.com/3D/MSO%20SAT.zip\"}],\"description\":\"\",\"name\":\"3D Model\",\"type\":\"3DModel\",\"url\":\"http://www.mallory-sonalert.com/3D/MSO%20IGS.zip\"},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"attaches\":[{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"name\":\"MSE\",\"url\":\"http://www.digikey.com/catalog/en/partgroup/mse/49376?mpart=MSE14LSU2&vendor=458\"}],\"description\":\"\",\"name\":\"Online Catalog\",\"type\":\"onlineCatalog\",\"url\":\"http://www.digikey.com/catalog/en/partgroup/mse/49376?mpart=MSE14LSU2&vendor=458\"}],\"images\":[{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"type\":\"thumbnail\",\"url\":\"//media.digikey.com/Photos/Mallory%20Sonalert%20Products%20Photos/MSE14LSU2_sml.jpg\"},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"type\":\"large\",\"url\":\"http://media.digikey.com/Photos/Mallory%20Sonalert%20Products%20Photos/MSE14LSU2.JPG\"}],\"parameters\":[{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"code\":\"driverCircuitry\",\"name\":\"Driver Circuitry\",\"value\":\"Indicator, Internally Driven\"},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"code\":\"inputType\",\"name\":\"Input Type\",\"value\":\"DC\"},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"code\":\"voltageRated\",\"name\":\"Voltage - Rated\",\"value\":\"-\"},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"code\":\"voltageRange\",\"name\":\"Voltage Range\",\"value\":\"7 ~ 14V\"},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"code\":\"frequency\",\"name\":\"Frequency\",\"value\":\"3.9kHz\"},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"code\":\"technology\",\"name\":\"Technology\",\"value\":\"Piezo\"},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"code\":\"operatingMode\",\"name\":\"Operating Mode\",\"value\":\"Speed Up, Fast\"},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"code\":\"duration\",\"name\":\"Duration\",\"value\":\"-\"},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"code\":\"soundPressureLevel\",\"name\":\"Sound Pressure Level\",\"value\":\"85dB @ 7V, 30cm\"},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"code\":\"currentSupply\",\"name\":\"Current - Supply\",\"value\":\"50mA\"},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"code\":\"portLocation\",\"name\":\"Port Location\",\"value\":\"Top\"},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"code\":\"operatingTemperature\",\"name\":\"Operating Temperature\",\"value\":\"-40°C ~ 65°C\"},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"code\":\"approvals\",\"name\":\"Approvals\",\"value\":\"-\"},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"code\":\"ratings\",\"name\":\"Ratings\",\"value\":\"-\"},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"code\":\"mountingType\",\"name\":\"Mounting Type\",\"value\":\"Through Hole\"},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"code\":\"termination\",\"name\":\"Termination\",\"value\":\"PC Pins\"},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"code\":\"sizeDimension\",\"name\":\"Size / Dimension\",\"value\":\"23.0mm L x 23.0mm W\"}],\"createdTimeMillis\":\"1488873785387\",\"updatedTimeMillis\":\"1488873785387\",\"id\":\"839023584000081920\"},\"skuId\":\"458-1325-ND\",\"vendorName\":\"digikey\",\"vendorId\":\"digikey\",\"vendorDetailsLink\":\"http://www.digikey.com/product-detail/en/mallory-sonalert-products-inc/MSE14LSU2/458-1325-ND/4895318\",\"vendorSeries\":{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"link\":\"\",\"name\":\"-\"},\"packaging\":\"Bulk\",\"countryCode\":\"US\",\"minimumQuantity\":1,\"qty\":6,\"spq\":null,\"mov\":null,\"currencyCode\":null,\"unitPrice\":null,\"unit\":null,\"sourceId\":\"digikey-100\",\"leadTime\":\"0\",\"quickFindKey\":\"b2bfc9c9bb72feff9960810ce09a2ffa\",\"storeId\":99999999,\"prices\":[{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"currencyCode\":\"CNY\",\"unitPrice\":null,\"priceLevels\":null},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"currencyCode\":\"USD\",\"unitPrice\":\"14.04\",\"priceLevels\":[{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"packaging\":\"\",\"price\":\"14.04\",\"breakQuantity\":\"1\"},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"packaging\":\"\",\"price\":\"12.799\",\"breakQuantity\":\"10\"},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"packaging\":\"\",\"price\":\"11.5608\",\"breakQuantity\":\"25\"},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"packaging\":\"\",\"price\":\"10.7348\",\"breakQuantity\":\"50\"},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"packaging\":\"\",\"price\":\"10.5284\",\"breakQuantity\":\"100\"},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"packaging\":\"\",\"price\":\"10.11556\",\"breakQuantity\":\"250\"},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"packaging\":\"\",\"price\":\"9.90912\",\"breakQuantity\":\"500\"}]}],\"costPrices\":null,\"resalePrices\":null,\"specialResaleprices\":null,\"processId\":null,\"partStatus\":\"Active\",\"createdTimeMillis\":\"1485779696928\",\"updatedTimeMillis\":\"1485779696928\",\"vendorCategories\":[{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"status\":null,\"cateAlias\":null,\"_id\":null,\"cateName\":null,\"cateLevel\":null},{\"createdDate\":null,\"creator\":null,\"lastUpdateDate\":null,\"lastUpdateUser\":null,\"status\":null,\"cateAlias\":null,\"_id\":null,\"cateName\":null,\"cateLevel\":null}],\"id\":\"1486457802359085\"}]".getBytes(), javaType);
		vo.setMsg(voList);
		HttpEntity<MaterialVo> entity2 = new HttpEntity<>(vo);
		this.mockPartyService();
		restTemplate.exchange(host + "/v1/imports/notification/syncupdate", HttpMethod.POST, entity2, new ParameterizedTypeReference<String>(){});
	}
	
}