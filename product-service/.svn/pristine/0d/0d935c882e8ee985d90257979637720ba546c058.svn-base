/*
 * Created: 2017年10月17日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.promotion.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;

import com.yikuyi.product.base.ProductApplicationTestBase;
import com.yikuyi.promotion.model.PromotionModuleContentDraft;

public class PromoModuleProductDraftResourceTest extends ProductApplicationTestBase{
	@Autowired
	private TestRestTemplate restTemplate;
	
	private String host;
	
	@Before
	public void setUpBefore() throws Exception {
		this.host = "http://localhost" + ":" + this.getPort();
	}
	
	/**
	 * 创建商品模块保存草稿
	 * @throws Exception
	 * @since 2017年11月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void testSave() throws Exception{
		this.mockPartyService();
		PromotionModuleContentDraft contentDraft = new PromotionModuleContentDraft();
		contentDraft.setPromotionId("918076987619147776");
		contentDraft.setStatus("ENABLE");
		restTemplate.put(host+"/v1/promoModuleProductDraft", contentDraft);
	}
	
	/**
	 * 商品上传的文件解析
	 * @since 2017年11月13日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void parseFileTest(){
		String promoModuleId = "1234";
		String promotionId = "1234";
		String fileUrl="http://ictrade-public-hz-uat.oss-cn-hangzhou.aliyuncs.com/sit/productUpload/activityProductsUpload/201711/17/045d9bff061259ed0ae65ca2fae77528.xlsx";
		String oriFileName="活动装修模板 (1).xlsx";
		restTemplate.exchange(host + "/v1/promoModuleProductDraft/products/parse?promoModuleId="+promoModuleId+"&promotionId="+promotionId+"&fileUrl="+fileUrl+"&oriFileName="+oriFileName, HttpMethod.POST, null, void.class);
	}
	
	/**
	 * 批量删除活动装修商品草稿信息
	 * @since 2017年11月13日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void deletePromoModuleProductDraftTest(){
		String promoModuleId = "1234";
		String promotionId = "1234";
		List<String> promoModuleProductIds = new ArrayList<>();
		promoModuleProductIds.add("5555");
		this.mockPartyService();
		restTemplate.delete(host+"/v1/promoModuleProductDraft/"+promoModuleId+"/promotionId/"+promotionId+"/products/delete?promoModuleId="
				+promoModuleId+"&promotionId="+promotionId+"&promoModuleProductIds="+promoModuleProductIds, Void.class);	
	}
	
	
}
