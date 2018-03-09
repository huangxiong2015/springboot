package com.yikuyi.product.rule.price.impl;

import static org.junit.Assert.assertNotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.yikuyi.product.base.ProductApplicationTestBase;
import com.yikuyi.rule.price.ProductPriceRule.RuleStatus;
import com.yikuyi.rule.price.model.PriceRuleDetail;
import com.yikuyi.rule.price.model.PriceRuleTemplate;
import com.ykyframework.exception.InvalidDataException;

/**
 * @author zr.wanghong
 * @version 1.0.0
 * @since 2016-12-21
 * @see com.yikuyi.product.rule.price.impl.PriceRulesResource
 * @see com.yikuyi.product.rule.price.manager.ProductPriceRuleManager
 */
@RunWith(SpringRunner.class)
@Transactional
@Rollback
public class PriceRulesResourceTest extends ProductApplicationTestBase{

	@Autowired
	private TestRestTemplate restTemplate; 
	
	private String host;
	
	@Before
	public void setUpBefore() throws Exception {
		this.host = "http://localhost" + ":" + this.getPort();
	}
	
	/**
	 * 测试新增定价规则服务
	 * @return
	 */
	@Test
	public void testAdd(){
		PriceRuleTemplate priceRuleTemplate = new PriceRuleTemplate();
		
		//成本价   香港按百分比价格加价22%，国内按百分比降价21%
		PriceRuleDetail priceRuleDetil = new PriceRuleDetail();
		priceRuleDetil.setRuleActionName("REAL_COST");
		priceRuleDetil.setDeliveryPlace("USD");
		priceRuleDetil.setRuleType("PRICE_FLAT");
		priceRuleDetil.setCalculateType("+");
		priceRuleDetil.setCalculateValue("0.22");
		PriceRuleDetail priceRuleDetil_1 = new PriceRuleDetail();
		priceRuleDetil_1.setRuleActionName("REAL_COST");
		priceRuleDetil_1.setDeliveryPlace("CNY");
		priceRuleDetil_1.setRuleType("PRICE_FLAT");
		priceRuleDetil_1.setCalculateType("-");
		priceRuleDetil_1.setCalculateValue("0.21");
		
		//销售价  香港按百分比加价23%，国内按百分比降价20%
		PriceRuleDetail priceRuleDetil2 = new PriceRuleDetail();
		priceRuleDetil2.setRuleActionName("RESALE_PRICE");
		priceRuleDetil2.setDeliveryPlace("USD");
		priceRuleDetil2.setRuleType("PRICE_FLAT");
		priceRuleDetil2.setCalculateType("+");
		priceRuleDetil2.setCalculateValue("0.23");
		PriceRuleDetail priceRuleDetil2_2 = new PriceRuleDetail();
		priceRuleDetil2_2.setRuleActionName("RESALE_PRICE");
		priceRuleDetil2_2.setDeliveryPlace("CNY");
		priceRuleDetil2_2.setRuleType("PRICE_FLAT");
		priceRuleDetil2_2.setCalculateType("-");
		priceRuleDetil2_2.setCalculateValue("0.20");
		
		//特价 香港按固定值加价0.23美元，国内按固定值降价0.66美元
		PriceRuleDetail priceRuleDetil3 = new PriceRuleDetail();
		priceRuleDetil3.setRuleActionName("SPECIAL_RESALE_PRICE");
		priceRuleDetil3.setDeliveryPlace("USD");
		priceRuleDetil3.setRuleType("PRICE_POL");
		priceRuleDetil3.setCalculateType("+");
		priceRuleDetil3.setCalculateValue("0.23");
		PriceRuleDetail priceRuleDetil3_3 = new PriceRuleDetail();
		priceRuleDetil3_3.setRuleActionName("SPECIAL_RESALE_PRICE");
		priceRuleDetil3_3.setDeliveryPlace("CNY");
		priceRuleDetil3_3.setRuleType("PRICE_POL");
		priceRuleDetil3_3.setCalculateType("-");
		priceRuleDetil3_3.setCalculateValue("0.66");
		
		List<PriceRuleDetail> priceRules = new ArrayList<PriceRuleDetail>();
		priceRules.add(priceRuleDetil);
		priceRules.add(priceRuleDetil_1);
		priceRules.add(priceRuleDetil2);
		priceRules.add(priceRuleDetil2_2);
		priceRules.add(priceRuleDetil3);
		priceRules.add(priceRuleDetil3_3);
		
		priceRuleTemplate.setPriceRuleDetails(priceRules);
		priceRuleTemplate.setVendorName("digikey");
		priceRuleTemplate.setWarehouse("digikey-100");
		priceRuleTemplate.setCategory("700-711-714");
		priceRuleTemplate.setCurrencyType("USD");
		priceRuleTemplate.setRuleName("Junit测试模板-测试新增定价规则-"+new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
		priceRuleTemplate.setDescription("Junit测试模板-测试新增定价规则-"+new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
		//priceRuleTemplate.setStatus("ENABLED");
		
		HttpEntity<PriceRuleTemplate> entity = new HttpEntity<PriceRuleTemplate>(priceRuleTemplate);
		
		//新增模板
		ResponseEntity<PriceRuleTemplate> responsePriceRuleTemplate = null;
		try {
			System.out.println("\n调用新增模板服务--(POST)"+host + "/v1/rules/price");
			this.mockPartyService();
			responsePriceRuleTemplate = restTemplate.exchange(host + "/v1/rules/price", HttpMethod.POST, entity,PriceRuleTemplate.class);
		} catch (InvalidDataException e){
			System.out.println(String.format("初始化，调用新增模板服务失败！错误信息[%s]", e.getMessage()));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		PriceRuleTemplate ruleTemplate = responsePriceRuleTemplate.getBody();
		assertNotNull(ruleTemplate.getRuleId());
		
		//删除测试数据
		this.removeData(ruleTemplate.getRuleId());
		
		//启用模板
//		ResponseEntity<PriceRuleTemplate> response = null;
//		try {
//			System.out.println("\n调用启用模板服务--(PUT)"+host + "/v1/rules/price/"+ruleTemplate.getRuleId()+"/status?status="+RuleStatus.ENABLED);
//			response = restTemplate.exchange(host + "/v1/rules/price/"+ruleTemplate.getRuleId()+"/status?status="+RuleStatus.ENABLED, HttpMethod.PUT, null, PriceRuleTemplate.class);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		response.getBody();
//		
//		System.out.println("启用模板成功，priceRuleTemplate-----ruleId="+ruleTemplate.getRuleId());
	}
	
	
	public PriceRuleTemplate initData(){
		PriceRuleTemplate priceRuleTemplate = new PriceRuleTemplate();
		
		//成本价   香港按百分比价格加价22%，国内按百分比降价21%
		PriceRuleDetail priceRuleDetil = new PriceRuleDetail();
		priceRuleDetil.setRuleActionName("REAL_COST");
		priceRuleDetil.setDeliveryPlace("USD");
		priceRuleDetil.setRuleType("PRICE_FLAT");
		priceRuleDetil.setCalculateType("+");
		priceRuleDetil.setCalculateValue("0.22");
		PriceRuleDetail priceRuleDetil_1 = new PriceRuleDetail();
		priceRuleDetil_1.setRuleActionName("REAL_COST");
		priceRuleDetil_1.setDeliveryPlace("CNY");
		priceRuleDetil_1.setRuleType("PRICE_FLAT");
		priceRuleDetil_1.setCalculateType("-");
		priceRuleDetil_1.setCalculateValue("0.21");
		
		//销售价  香港按百分比加价23%，国内按百分比降价20%
		PriceRuleDetail priceRuleDetil2 = new PriceRuleDetail();
		priceRuleDetil2.setRuleActionName("RESALE_PRICE");
		priceRuleDetil2.setDeliveryPlace("USD");
		priceRuleDetil2.setRuleType("PRICE_FLAT");
		priceRuleDetil2.setCalculateType("+");
		priceRuleDetil2.setCalculateValue("0.23");
		PriceRuleDetail priceRuleDetil2_2 = new PriceRuleDetail();
		priceRuleDetil2_2.setRuleActionName("RESALE_PRICE");
		priceRuleDetil2_2.setDeliveryPlace("CNY");
		priceRuleDetil2_2.setRuleType("PRICE_FLAT");
		priceRuleDetil2_2.setCalculateType("-");
		priceRuleDetil2_2.setCalculateValue("0.20");
		
		//特价 香港按固定值加价0.23美元，国内按固定值降价0.66美元
		PriceRuleDetail priceRuleDetil3 = new PriceRuleDetail();
		priceRuleDetil3.setRuleActionName("SPECIAL_RESALE_PRICE");
		priceRuleDetil3.setDeliveryPlace("USD");
		priceRuleDetil3.setRuleType("PRICE_POL");
		priceRuleDetil3.setCalculateType("+");
		priceRuleDetil3.setCalculateValue("0.23");
		PriceRuleDetail priceRuleDetil3_3 = new PriceRuleDetail();
		priceRuleDetil3_3.setRuleActionName("SPECIAL_RESALE_PRICE");
		priceRuleDetil3_3.setDeliveryPlace("CNY");
		priceRuleDetil3_3.setRuleType("PRICE_POL");
		priceRuleDetil3_3.setCalculateType("-");
		priceRuleDetil3_3.setCalculateValue("0.66");
		
		List<PriceRuleDetail> priceRules = new ArrayList<PriceRuleDetail>();
		priceRules.add(priceRuleDetil);
		priceRules.add(priceRuleDetil_1);
		priceRules.add(priceRuleDetil2);
		priceRules.add(priceRuleDetil2_2);
		priceRules.add(priceRuleDetil3);
		priceRules.add(priceRuleDetil3_3);
		
		priceRuleTemplate.setPriceRuleDetails(priceRules);
		priceRuleTemplate.setVendorName("digikey");
		priceRuleTemplate.setWarehouse("digikey-100");
		priceRuleTemplate.setCategory("700-711-714");
		priceRuleTemplate.setCurrencyType("USD");
		priceRuleTemplate.setRuleName("Junit测试模板"+new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
		priceRuleTemplate.setDescription("Junit测试模板"+new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
		//priceRuleTemplate.setStatus("ENABLED");
		
		HttpEntity<PriceRuleTemplate> entity = new HttpEntity<PriceRuleTemplate>(priceRuleTemplate);
		
		//新增模板
		ResponseEntity<PriceRuleTemplate> responsePriceRuleTemplate = null;
		try {
			System.out.println("\n调用新增模板服务--(POST)"+host + "/v1/rules/price");
			this.mockPartyService();
			responsePriceRuleTemplate = restTemplate.exchange(host + "/v1/rules/price", HttpMethod.POST, entity,PriceRuleTemplate.class);
		} catch (InvalidDataException e){
			System.out.println(String.format("初始化，调用新增模板服务失败！错误信息[%s]", e.getMessage()));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		PriceRuleTemplate ruleTemplate = responsePriceRuleTemplate.getBody();
		assertNotNull(ruleTemplate.getRuleId());
		
		return ruleTemplate;
		//启用模板
//		ResponseEntity<PriceRuleTemplate> response = null;
//		try {
//			System.out.println("\n调用启用模板服务--(PUT)"+host + "/v1/rules/price/"+ruleTemplate.getRuleId()+"/status?status="+RuleStatus.ENABLED);
//			response = restTemplate.exchange(host + "/v1/rules/price/"+ruleTemplate.getRuleId()+"/status?status="+RuleStatus.ENABLED, HttpMethod.PUT, null, PriceRuleTemplate.class);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		response.getBody();
//		
//		System.out.println("启用模板成功，priceRuleTemplate-----ruleId="+ruleTemplate.getRuleId());
	}
	
	
	public void removeData(String ruleId){
		ResponseEntity<PriceRuleTemplate> response = null;
		try {
			System.out.println("\n调用删除模板服务--(DELETE)"+host + "/v1/rules/price/"+ruleId);
			this.mockPartyService();
			response = restTemplate.exchange(host + "/v1/rules/price/"+ruleId, HttpMethod.DELETE, null, PriceRuleTemplate.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.getBody();
	}

	/**
	 * 测试调用查询单个模板服务
	 */
	@Test
	public void testQeury(){
		
		PriceRuleTemplate priceRuleTemplate = this.initData();
		
		ResponseEntity<PriceRuleTemplate> response = null;
		try {
			System.out.println("\n调用查询单个模板服务--(GET)"+host + "/v1/rules/price/"+priceRuleTemplate.getRuleId());
			this.mockPartyService();
			response = restTemplate.exchange(host + "/v1/rules/price/"+priceRuleTemplate.getRuleId(), HttpMethod.GET, null, PriceRuleTemplate.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.getBody();
		
		//删除测试数据
		this.removeData(priceRuleTemplate.getRuleId());
	}

	
	/**
	 * 测试调用修改模板服务
	 */
	@Test
	public void testUpdate(){
		PriceRuleTemplate priceRuleTemplate = this.initData();
		priceRuleTemplate.setCurrencyType("CNY");
		HttpEntity<PriceRuleTemplate> entity = new HttpEntity<PriceRuleTemplate>(priceRuleTemplate);
		
		ResponseEntity<PriceRuleTemplate> response = null;
		try {
			System.out.println("\n调用修改模板服务--(PUT)"+host + "/v1/rules/price");
			this.mockPartyService();
			response = restTemplate.exchange(host + "/v1/rules/price", HttpMethod.PUT, entity, PriceRuleTemplate.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.getBody();
		
		//删除测试数据
		this.removeData(priceRuleTemplate.getRuleId());
	}
	
	/**
	 * 测试调用查询模板列表服务
	 */
	@Test
	public void testQueryList(){
		ResponseEntity<PageInfo<PriceRuleTemplate>> response = null;
		System.out.println("\n调用修改模板服务--(PUT)"+host + "/v1/rules/price");
		this.mockPartyService();
		response = restTemplate.exchange(host + "/v1/rules/price", HttpMethod.GET, null, new ParameterizedTypeReference<PageInfo<PriceRuleTemplate>>(){});
		PageInfo<PriceRuleTemplate> result = response.getBody();
		assertNotNull(result);
	}
	
	
	/**
	 * 测试调用启用模板服务
	 * @param ruleId
	 */
	@Test
	public void testEnabledTemplate(){
		PriceRuleTemplate priceRuleTemplate = this.initData();
		ResponseEntity<PriceRuleTemplate> response = null;
		try {
			System.out.println("\n调用启用模板服务--(PUT)"+host + "/v1/rules/price/"+priceRuleTemplate.getRuleId()+"/status?status="+RuleStatus.ENABLED);
			this.mockPartyService();
			response = restTemplate.exchange(host + "/v1/rules/price/"+priceRuleTemplate.getRuleId()+"/status?status="+RuleStatus.ENABLED, HttpMethod.PUT, null, PriceRuleTemplate.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.getBody();
		
		//删除测试数据
		this.removeData(priceRuleTemplate.getRuleId());
	}
	
	
	/**
	 * 测试调用禁用模板服务
	 * @param ruleId
	 */
	@Test
	public void testDisabledTemplate(){
		PriceRuleTemplate priceRuleTemplate = this.initData();
		
		ResponseEntity<PriceRuleTemplate> response1 = null;
		try {
			System.out.println("\n调用启用模板服务--(PUT)"+host + "/v1/rules/price/"+priceRuleTemplate.getRuleId()+"/status?status="+RuleStatus.ENABLED);
			this.mockPartyService();
			response1 = restTemplate.exchange(host + "/v1/rules/price/"+priceRuleTemplate.getRuleId()+"/status?status="+RuleStatus.ENABLED, HttpMethod.PUT, null, PriceRuleTemplate.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		response1.getBody();
		
		ResponseEntity<PriceRuleTemplate> response = null;
		try {
			System.out.println("\n调用禁用模板服务--(PUT)"+host + "/v1/rules/price/"+priceRuleTemplate.getRuleId()+"/status?status="+RuleStatus.DISABLED);
			this.mockPartyService();
			response = restTemplate.exchange(host + "/v1/rules/price/"+priceRuleTemplate.getRuleId()+"/status?status="+RuleStatus.DISABLED, HttpMethod.PUT, null, PriceRuleTemplate.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.getBody();
		
		//删除测试数据
		this.removeData(priceRuleTemplate.getRuleId());
	}
	
	/**
	 * 测试调用删除模板服务
	 * @param ruleId
	 */
	@Test
	public void testDelete(){
		PriceRuleTemplate priceRuleTemplate = this.initData();
		ResponseEntity<PriceRuleTemplate> response = null;
		try {
			System.out.println("\n调用删除模板服务--(DELETE)"+host + "/v1/rules/price/"+priceRuleTemplate.getRuleId());
			this.mockPartyService();
			response = restTemplate.exchange(host + "/v1/rules/price/"+priceRuleTemplate.getRuleId(), HttpMethod.DELETE, null, PriceRuleTemplate.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.getBody();
	}
	
	

}
