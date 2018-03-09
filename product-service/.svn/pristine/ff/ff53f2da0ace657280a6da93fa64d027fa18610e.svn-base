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

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import com.yikuyi.material.vo.MaterialVo;
import com.yikuyi.material.vo.MaterialVo.MaterialVoType;
import com.yikuyi.product.base.ProductApplicationTestBase;
import com.yikuyi.product.basicmaterial.bll.BasicMaterialManager;
import com.ykyframework.model.IdGen;

/**
 * @author zr.wanghong
 * @version 1.0.0
 * @since 2016-12-21
 * @see com.yikuyi.product.rule.price.impl.PriceRulesResource
 * @see com.yikuyi.product.goods.manager.PriceQueryManager
 * @see com.yikuyi.product.rule.price.manager.ProductPriceRuleManager
 */
public class BasicMaterialResourceTest extends ProductApplicationTestBase{
	
	@Autowired
	private TestRestTemplate restTemplate; // = new TestRestTemplate();	
	
	@Autowired
	private BasicMaterialManager basicMaterialManager;
	
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
		this.host = "http://localhost" + ":" + this.getPort();
	}

	@Test
	public void testupload() {
		this.mockPartyService();
		MaterialVo vo = new MaterialVo();
		vo.setFileUrl("http://ictrade-private-hz-uat.oss-cn-hangzhou.aliyuncs.com/sit/register/approval/5f/a2c/5fa2c4d00eb7658e1258bc2398e3d228.xlsx");
		vo.setOriFileName("JUnit test");
		HttpEntity<MaterialVo> entity = new HttpEntity<>(vo);
		restTemplate.exchange(host + "/v1/basicmaterial/notification/upload", HttpMethod.POST, entity, new ParameterizedTypeReference<String>(){});
	}
	
	//@Test
	//@DatabaseSetup(type = DatabaseOperation.REFRESH, value =  "classpath:com/yikuyi/product/material/fileUpload_sample.xml" )
	public void testparse() {
		this.mockPartyService();
		MaterialVo vo = new MaterialVo();
		vo.setFileUrl("https://ictrade-private-hz-uat.oss-cn-hangzhou.aliyuncs.com/sit/productUpload/basicMaterialUpload/201709/01/0ebafd3dd6da67c7e6ae8da0b5276cfa.xlsx");
		vo.setOriFileName("JUnitTest.xlsx");
		vo.setType(MaterialVoType.BASIS_FILE_UPLOAD);
		vo.setDocId(IdGen.getInstance().nextId()+"");
		/*HttpEntity<MaterialVo> entity = new HttpEntity<>(vo);
		restTemplate.exchange(host + "/v1/basicmaterial/notification/parse", HttpMethod.POST, entity, new ParameterizedTypeReference<String>(){});
		vo.setFileUrl("http://ictrade-biz-public-hz-uat.oss-cn-hangzhou.aliyuncs.com/sit/approval/privateRead/201702/25/202ddb22a16ece6eb7f51b9116a04795.xlsx");
		vo.setDocId(IdGen.getInstance().nextId()+"");
		restTemplate.exchange(host + "/v1/basicmaterial/notification/parse", HttpMethod.POST, entity, new ParameterizedTypeReference<String>(){});
		vo.setFileUrl("http://ictrade-biz-public-hz-uat.oss-cn-hangzhou.aliyuncs.com/sit/approval/privateRead/201702/25/009f5d950d58fa0a87c7fc033646f394.xlsx");
		vo.setDocId(IdGen.getInstance().nextId()+"");
		restTemplate.exchange(host + "/v1/basicmaterial/notification/parse", HttpMethod.POST, entity, new ParameterizedTypeReference<String>(){});*/
		basicMaterialManager.basicMaterialParseImportFile(vo);
	}
}
