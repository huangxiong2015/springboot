package com.yikuyi.product.rule.mov.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.yikuyi.product.base.ProductApplicationTestBase;
import com.yikuyi.promotion.model.Promotion;
import com.yikuyi.promotion.model.Promotion.PromotionStatus;
import com.yikuyi.promotion.vo.PromotionProductVo;
import com.yikuyi.rule.mov.vo.MovRuleTemplate;
import com.yikuyi.rule.price.ProductPriceRule;
import com.yikuyi.rule.price.ProductPriceRule.RuleStatus;


@RunWith(SpringRunner.class)
@Transactional
@Rollback
public class MovRulesResourceV2Test extends ProductApplicationTestBase{

	@Autowired
	private TestRestTemplate restTemplate; 
	
	private String host;
	
	@Before
	public void setUpBefore() throws Exception {
		this.host = "http://localhost" + ":" + this.getPort();
	}

	/**
	 * 新增mov策略
	 * @throws Exception
	 * @since 2017年11月23日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void addTest() throws Exception{
		this.mockPartyService();
		MovRuleTemplate movRuleTemplate = new MovRuleTemplate();
		movRuleTemplate.setRuleId("832064737826074526");
		movRuleTemplate.setDescription("hahahhahahha");
		movRuleTemplate.setVendorId("digikey-100");
		movRuleTemplate.setCnyMovAmount("98.00856");
		movRuleTemplate.setUsdMovAmount("156.11253");
		
		restTemplate.postForEntity(host+"/v2/rules/mov", movRuleTemplate, null);
	}
	
	/**
	 * 删除mov策略
	 * @throws Exception
	 * @since 2017年11月23日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void deleteTest() throws Exception{
		this.mockPartyService();
		String ruleId = "832064737826074526";
		restTemplate.delete(host+"/v2/rules/mov/"+ruleId, MovRuleTemplate.class);	
	}
	
	/**
	 * 修改mov策略
	 * @throws Exception
	 * @since 2017年11月23日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void updateTest() throws Exception{
		this.mockPartyService();
		MovRuleTemplate movRuleTemplate = new MovRuleTemplate();
		movRuleTemplate.setRuleId("832064737826045952");
		movRuleTemplate.setDescription("哈哈哈哈哈哈哈");
		movRuleTemplate.setVendorId("RESALE_PRICE");
		movRuleTemplate.setCnyMovAmount("98.00856");
		movRuleTemplate.setUsdMovAmount("156.11253");
		
		restTemplate.put(host+"/v2/rules/mov", movRuleTemplate);
	}
	
	/**
	 * 根据策略ID查询策略模板
	 * @throws Exception
	 * @since 2017年11月23日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void findByIdTest() throws Exception{
		this.mockPartyService();
		String ruleId = "809305860672061440";
		restTemplate.exchange(host+"/v2/rules/mov/"+ruleId, HttpMethod.GET, null, new ParameterizedTypeReference<MovRuleTemplate>(){});
	}
	
	/**
	 * 列表查询mov策略
	 * @throws Exception
	 * @since 2017年11月23日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void findListTest() throws Exception{
		int page = 1;
		int pageSize = 20;
		this.mockPartyService();
		restTemplate.exchange(host+"/v2/rules/mov?page="+page+"&pageSize="+pageSize, HttpMethod.GET, null, new ParameterizedTypeReference<PageInfo<MovRuleTemplate>>(){});	
	}
	
	/**
	 * 启用、禁用或删除mov策略
	 * @throws Exception
	 * @since 2017年11月23日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void updateStatusTest() throws Exception{
		String ruleId = "832064737826045952";
		restTemplate.put(host+"/v2/rules/mov/"+ruleId+"/status?ruleId="+ruleId+"&status="+ProductPriceRule.RuleStatus.ENABLED, MovRuleTemplate.class);

	}

}
