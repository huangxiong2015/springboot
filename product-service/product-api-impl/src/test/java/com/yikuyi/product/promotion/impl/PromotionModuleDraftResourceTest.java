
package com.yikuyi.product.promotion.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import com.yikuyi.product.base.ProductApplicationTestBase;
import com.yikuyi.promotion.model.PromotionModuleContentDraft;
import com.yikuyi.promotion.model.PromotionModuleDraft;
import com.yikuyi.promotion.model.PromotionModuleDraft.PromoModuleStatus;
import com.yikuyi.promotion.model.PromotionModuleDraft.PromoModuleType;

/**
 * 
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
public class PromotionModuleDraftResourceTest extends ProductApplicationTestBase{

	
	private String host;

	@Autowired
	private TestRestTemplate restTemplate; 
	

	@Before
	public void setUpBefore() throws Exception {
		this.host = "http://localhost" + ":" + this.getPort();
	}
	
	/**
	 * 
	 * @throws Exception
	 * @since 2017年11月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void testInsert() throws Exception{
		this.mockPartyService();
		PromotionModuleDraft draft = new PromotionModuleDraft();
		draft.setPromoModuleId("918040973987020800");
		draft.setPromoModuleStatus(PromoModuleStatus.ENABLE);
		draft.setPromoModuleType(PromoModuleType.COUPON);
		restTemplate.postForEntity(host+"/v1/promotionModuleDraft", draft, null);	
	}
	
	/**
	 * 编辑模块草稿
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
		restTemplate.put(host+"/v1/promotionModuleDraft", contentDraft);
		
	}
	
	/**
	 * 
	 * @throws Exception
	 * @since 2017年11月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void  testPromotinModuleOrderSeq() throws Exception{
		this.mockPartyService();
		List<String> ids = new ArrayList<>();
		ids.add("918077258290167808");
		restTemplate.postForEntity(host+"/v1/promotionModuleDraft/orderSeq", ids, null);	
	}
	
	/**
	 *  删除促销活动模块数据
	 * @throws Exception
	 * @since 2017年10月17日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void testDeletePromotionModuleAndDraft() throws Exception{
		this.mockPartyService();
		String promoModuleId = "918000728922914816";
		restTemplate.delete(host+"/v1/promotionModuleDraft/"+promoModuleId, Void.class);
	}

	/**
	 * 查询促销模块草稿详情
	 * @throws Exception
	 * @since 2017年11月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void testGetPromotionModuleDraft() throws Exception{
		this.mockPartyService();
		String promoModuleId = "918040973987020800";
		restTemplate.exchange(host+"v1/promotionModuleDraft/"+promoModuleId+"/draft", HttpMethod.GET, null, new ParameterizedTypeReference<PromotionModuleDraft>(){});	
	}
	
}