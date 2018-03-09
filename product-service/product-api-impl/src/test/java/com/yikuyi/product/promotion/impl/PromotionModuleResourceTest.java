
package com.yikuyi.product.promotion.impl;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import com.yikuyi.product.base.ProductApplicationTestBase;
import com.yikuyi.promotion.model.PromotionModule;
import com.yikuyi.promotion.vo.PromotionPreviewVo;

/**
 * 
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
public class PromotionModuleResourceTest extends ProductApplicationTestBase{

	
	private String host;

	@Autowired
	private TestRestTemplate restTemplate; 
	

	@Before
	public void setUpBefore() throws Exception {
		this.host = "http://localhost" + ":" + this.getPort();
	}
	
	
	
	/**
	 * 查询促销模块详情
	 * @throws Exception
	 * @since 2017年11月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void testGetPromotionModule() throws Exception{
		this.mockPartyService();
		String promoModuleId = "918000728922914816";
		restTemplate.exchange(host+"/v1/promotionModule/"+promoModuleId, HttpMethod.GET, null, new ParameterizedTypeReference<PromotionModule>(){});	
	}
	
	/**
	 * 
	 * @throws Exception
	 * @since 2017年11月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void testGetPromotionDetail() throws Exception{
		this.mockPartyService();
		String promotionId = "918040973987020800";
		String promoModuleId = "918077427912015872";
		String promoModuleType = "PRODUCT_LIST";
		restTemplate.exchange(host+"/v1/promotionModule/getPromotionDetail/"+promotionId+"/"+promoModuleId+"/"+promoModuleType, HttpMethod.GET, null, new ParameterizedTypeReference<PromotionPreviewVo>(){});	
	}

	
}