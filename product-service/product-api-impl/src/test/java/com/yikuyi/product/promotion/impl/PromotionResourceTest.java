
package com.yikuyi.product.promotion.impl;

import java.text.SimpleDateFormat;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.github.pagehelper.PageInfo;
import com.yikuyi.product.base.ProductApplicationTestBase;
import com.yikuyi.promotion.model.Promotion;
import com.yikuyi.promotion.model.Promotion.PromotionStatus;
import com.yikuyi.promotion.vo.PromotionProductVo;
import com.yikuyi.promotion.vo.PromotionVo;

/**
 * 
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
public class PromotionResourceTest extends ProductApplicationTestBase{

	
	private String host;

	@Autowired
	private TestRestTemplate restTemplate; 
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	

	@Before
	public void setUpBefore() throws Exception {
		this.host = "http://localhost" + ":" + this.getPort();
	}
	
	
	/**
	 * 活动列表查询
	 * @throws Exception
	 * @since 2017年10月17日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void testList() throws Exception{		
		int page = 1;
		int pageSize = 20;	
		this.mockPartyService();
		restTemplate.exchange(host+"/v1/promotions?page="+page+"&pageSize="+pageSize, HttpMethod.GET, null, new ParameterizedTypeReference<PageInfo<PromotionVo>>(){});	
	}
	
	/**
	 * 创建活动
	 * @throws Exception
	 * @since 2017年11月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void testSave() throws Exception{
		this.mockPartyService();
		Promotion promotion = new Promotion();
		promotion.setPromotionName("wwwhhh");
		promotion.setPromotionStatus(PromotionStatus.ENABLE);
		promotion.setStartDate(sdf.parse("2017-11-09 00:00:00"));
		promotion.setEndDate(sdf.parse("2017-11-20 00:00:00"));
		promotion.setPromotionUrl("//www.baidu.com");
		restTemplate.postForEntity(host+"/v1/promotions", promotion, null);
	}
	
	
	/**
	 * 在草稿表中复制促销活动草稿信息
	 * @throws Exception
	 * @since 2017年10月17日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void testDraftCopy() throws Exception{
		String promotionId="918040973987020800";
		this.mockPartyService();
		restTemplate.postForLocation(host+"/v1/promotions/reproduction/"+promotionId, String.class);
	}
	
	/**
	 * 编辑活动
	 * @throws Exception
	 * @since 2017年11月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void testUpdate() throws Exception{
		Promotion promotion = new Promotion();
		promotion.setPromotionId("123456787");
		promotion.setPromotionName("wwwhhh");
		promotion.setPromotionStatus(PromotionStatus.ENABLE);
		promotion.setStartDate(sdf.parse("2017-11-09 00:00:00"));
		promotion.setEndDate(sdf.parse("2017-11-20 00:00:00"));
		promotion.setPromotionUrl("//www.baidu.com");
		this.mockPartyService();
		restTemplate.put(host+"/v1/promotions", promotion);
	}
	
	/**
	 * 
	 * @throws Exception
	 * @since 2017年11月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void testUpdatePromotionStatus() throws Exception{
		String promoId = "123456787";
		this.mockPartyService();
		restTemplate.put(host+"/v1/promotions/"+promoId+"/status?promoId="+promoId+"&status="+PromotionStatus.ENABLE, null);
	}
	
	/**
	 * 单个活动查询
	 * @throws Exception
	 * @since 2017年11月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void testGetPromotion() throws Exception{
		String promotionId = "123456787";
		this.mockPartyService();
		restTemplate.exchange(host+"/v1/promotions/"+promotionId, HttpMethod.GET, null, new ParameterizedTypeReference<Promotion>(){});	
	}
	
	/**
	 * 
	 * @throws Exception
	 * @since 2017年11月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void testListModuleProduct() throws Exception{
		String promotionId = "918406158916517888";
		String promoModuleId = "111";
		int page = 1;
		int pageSize = 20;
		this.mockPartyService();
		restTemplate.exchange(host+"/v1/promotions/"+promotionId+"/module/"+promoModuleId+"/product?promotionId="+promotionId+"&promoModuleId="+promoModuleId
				+"&page="+page+"&pageSize="+pageSize, HttpMethod.GET, null, new ParameterizedTypeReference<PageInfo<PromotionProductVo>>(){});	
	}
	
	/**
	 * 根据prmotionId查询所有的草稿模块json数据
	 * @throws Exception
	 * @since 2017年10月17日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void testGetPromotionModuleDraft2Json() throws Exception{
		this.mockPartyService();
		String promotionId = "918040973987020800";
		ResponseEntity<String> response = restTemplate.getForEntity(host + "/v1/promotions/"+promotionId+"/module/draft", null, new ParameterizedTypeReference<String>(){});			
		System.out.println(response);
	}
	
	/**
	 * 根据prmotionId查询所有的模块json数据
	 * @throws Exception
	 * @since 2017年10月17日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void testGetPromotionModule2Json() throws Exception{
		this.mockPartyService();
		String promotionId = "918040973987020800";
		ResponseEntity<String> response = restTemplate.getForEntity(host + "/v1/promotions/"+promotionId+"/module", null, new ParameterizedTypeReference<String>(){});			
		System.out.println(response);
	}
	
	/**
	 * 定时器处理缓存中的活动信息
	 * @throws Exception
	 * @since 2017年10月17日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void testHandlePromotionCache() throws Exception{
		this.mockPartyService();
		restTemplate.put(host+"/v1/promotions/cache/", Void.class);
	}

	
}