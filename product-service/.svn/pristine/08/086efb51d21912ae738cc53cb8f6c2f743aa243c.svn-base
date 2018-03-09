package com.yikuyi.product.rule.price.manager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.yikuyi.rule.price.model.PriceRuleDetail;
import com.yikuyi.rule.price.model.PriceRuleTemplate;
import com.ykyframework.exception.InvalidDataException;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class })
@Transactional
@Rollback
public class ProductPriceRuleManagerTest {

	@Autowired
	private ProductPriceRuleManager productPriceRuleManager;
	
	/** 注入JUnit框架中的ExpectedException类用于测试异常*/
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	/**
	 * 测试新增定价规则
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value =  "classpath:com/yikuyi/product/rule/price/manager/product_price_rule_manager_sample_result.xml" )
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "classpath:com/yikuyi/product/rule/price/manager/product_price_rule_manager_insert_result.xml")
	public void testInsert() {
		PriceRuleTemplate priceRuleTemplate = new PriceRuleTemplate();
		priceRuleTemplate.setRuleName("testrulename2");
		priceRuleTemplate.setVendorName("digikey");
		priceRuleTemplate.setBrand("Digikey");
		priceRuleTemplate.setWarehouse("美国");
		priceRuleTemplate.setCategory("0011-0021-0031");
		priceRuleTemplate.setCurrencyType("USD");
		priceRuleTemplate.setDescription("DESCRIPTION");
		
		
		PriceRuleDetail priceRuleDetil = new PriceRuleDetail();
		priceRuleDetil.setRuleActionName("REAL_COST");
		priceRuleDetil.setDeliveryPlace("USD");
		priceRuleDetil.setRuleType("PRICE_POL");
		priceRuleDetil.setCalculateType("+");
		priceRuleDetil.setCalculateValue("5.000000");
		PriceRuleDetail priceRuleDetil_1 = new PriceRuleDetail();
		priceRuleDetil_1.setRuleActionName("REAL_COST");
		priceRuleDetil_1.setDeliveryPlace("CNY");
		priceRuleDetil_1.setRuleType("PRICE_POL");
		priceRuleDetil_1.setCalculateType("+");
		priceRuleDetil_1.setCalculateValue("5.000000");
		
		
		PriceRuleDetail priceRuleDetil2 = new PriceRuleDetail();
		priceRuleDetil2.setRuleActionName("RESALE_PRICE");
		priceRuleDetil2.setDeliveryPlace("USD");
		priceRuleDetil2.setRuleType("PRICE_POL");
		priceRuleDetil2.setCalculateType("+");
		priceRuleDetil2.setCalculateValue("5.000000");
		PriceRuleDetail priceRuleDetil2_2 = new PriceRuleDetail();
		priceRuleDetil2_2.setRuleActionName("RESALE_PRICE");
		priceRuleDetil2_2.setDeliveryPlace("CNY");
		priceRuleDetil2_2.setRuleType("PRICE_POL");
		priceRuleDetil2_2.setCalculateType("+");
		priceRuleDetil2_2.setCalculateValue("5.000000");
		
		
		PriceRuleDetail priceRuleDetil3 = new PriceRuleDetail();
		priceRuleDetil3.setRuleActionName("SPECIAL_RESALE_PRICE");
		priceRuleDetil3.setDeliveryPlace("USD");
		priceRuleDetil3.setRuleType("PRICE_POL");
		priceRuleDetil3.setCalculateType("+");
		priceRuleDetil3.setCalculateValue("5.000000");
		PriceRuleDetail priceRuleDetil3_3 = new PriceRuleDetail();
		priceRuleDetil3_3.setRuleActionName("SPECIAL_RESALE_PRICE");
		priceRuleDetil3_3.setDeliveryPlace("CNY");
		priceRuleDetil3_3.setRuleType("PRICE_POL");
		priceRuleDetil3_3.setCalculateType("+");
		priceRuleDetil3_3.setCalculateValue("5.000000");
		
		
		List<PriceRuleDetail> priceRules = new ArrayList<PriceRuleDetail>();
		priceRules.add(priceRuleDetil);
		priceRules.add(priceRuleDetil_1);
		priceRules.add(priceRuleDetil2);
		priceRules.add(priceRuleDetil2_2);
		priceRules.add(priceRuleDetil3);
		priceRules.add(priceRuleDetil3_3);
		
		priceRuleTemplate.setPriceRuleDetails(priceRules);
		productPriceRuleManager.insert(priceRuleTemplate,null);
	}
	
	/**
	 * 测试新增定价规则异常的场景
	 * 期望结果：抛出业务异常InvalidDataException规则名称重复，请重新输入！
	 */
	@Test
	public void testInsertWithException(){
		PriceRuleTemplate priceRuleTemplate = new PriceRuleTemplate();
		priceRuleTemplate.setRuleName("testrulename2");
		priceRuleTemplate.setVendorName("digikey");
		priceRuleTemplate.setWarehouse("美国");
		priceRuleTemplate.setCategory("0011-0021-0031");
		priceRuleTemplate.setCurrencyType("USD");
		priceRuleTemplate.setDescription("DESCRIPTION");
		
		
		PriceRuleDetail priceRuleDetil = new PriceRuleDetail();
		priceRuleDetil.setRuleActionName("REAL_COST");
		priceRuleDetil.setDeliveryPlace("USD");
		priceRuleDetil.setRuleType("PRICE_POL");
		priceRuleDetil.setCalculateType("+");
		priceRuleDetil.setCalculateValue("5.000000");
		PriceRuleDetail priceRuleDetil_1 = new PriceRuleDetail();
		priceRuleDetil_1.setRuleActionName("REAL_COST");
		priceRuleDetil_1.setDeliveryPlace("CNY");
		priceRuleDetil_1.setRuleType("PRICE_POL");
		priceRuleDetil_1.setCalculateType("+");
		priceRuleDetil_1.setCalculateValue("5.000000");
		
		
		PriceRuleDetail priceRuleDetil2 = new PriceRuleDetail();
		priceRuleDetil2.setRuleActionName("RESALE_PRICE");
		priceRuleDetil2.setDeliveryPlace("USD");
		priceRuleDetil2.setRuleType("PRICE_POL");
		priceRuleDetil2.setCalculateType("+");
		priceRuleDetil2.setCalculateValue("5.000000");
		PriceRuleDetail priceRuleDetil2_2 = new PriceRuleDetail();
		priceRuleDetil2_2.setRuleActionName("RESALE_PRICE");
		priceRuleDetil2_2.setDeliveryPlace("CNY");
		priceRuleDetil2_2.setRuleType("PRICE_POL");
		priceRuleDetil2_2.setCalculateType("+");
		priceRuleDetil2_2.setCalculateValue("5.000000");
		
		
		PriceRuleDetail priceRuleDetil3 = new PriceRuleDetail();
		priceRuleDetil3.setRuleActionName("SPECIAL_RESALE_PRICE");
		priceRuleDetil3.setDeliveryPlace("USD");
		priceRuleDetil3.setRuleType("PRICE_POL");
		priceRuleDetil3.setCalculateType("+");
		priceRuleDetil3.setCalculateValue("5.000000");
		PriceRuleDetail priceRuleDetil3_3 = new PriceRuleDetail();
		priceRuleDetil3_3.setRuleActionName("SPECIAL_RESALE_PRICE");
		priceRuleDetil3_3.setDeliveryPlace("CNY");
		priceRuleDetil3_3.setRuleType("PRICE_POL");
		priceRuleDetil3_3.setCalculateType("+");
		priceRuleDetil3_3.setCalculateValue("5.000000");
		
		
		List<PriceRuleDetail> priceRules = new ArrayList<PriceRuleDetail>();
		priceRules.add(priceRuleDetil);
		priceRules.add(priceRuleDetil_1);
		priceRules.add(priceRuleDetil2);
		priceRules.add(priceRuleDetil2_2);
		priceRules.add(priceRuleDetil3);
		priceRules.add(priceRuleDetil3_3);
		
		priceRuleTemplate.setPriceRuleDetails(priceRules);
		
		thrown.expect(InvalidDataException.class);
		thrown.expectMessage("规则名称重复，请重新输入！");
		productPriceRuleManager.insert(priceRuleTemplate,null);
		productPriceRuleManager.insert(priceRuleTemplate,null);
		
	}
	
	
	/**
	 * 测试更新定价规则
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value =  "classpath:com/yikuyi/product/rule/price/manager/product_price_rule_manager_sample_result.xml" )
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "classpath:com/yikuyi/product/rule/price/manager/product_price_rule_manager_update_result.xml")
	public void testUpdate(){
		PriceRuleTemplate priceRuleTemplate = new PriceRuleTemplate();
		priceRuleTemplate.setRuleId("10001");
		priceRuleTemplate.setRuleName("testrulename2");
		priceRuleTemplate.setVendorName("digikey");
		priceRuleTemplate.setBrand("Digikey");
		priceRuleTemplate.setWarehouse("美国");
		priceRuleTemplate.setCategory("0011-0021-0031");
		priceRuleTemplate.setCurrencyType("USD");
		priceRuleTemplate.setDescription("DESCRIPTION");
		
		
		PriceRuleDetail priceRuleDetil = new PriceRuleDetail();
		priceRuleDetil.setRuleActionName("REAL_COST");
		priceRuleDetil.setDeliveryPlace("USD");
		priceRuleDetil.setRuleType("PRICE_POL");
		priceRuleDetil.setCalculateType("+");
		priceRuleDetil.setCalculateValue("5.000000");
		PriceRuleDetail priceRuleDetil_1 = new PriceRuleDetail();
		priceRuleDetil_1.setRuleActionName("REAL_COST");
		priceRuleDetil_1.setDeliveryPlace("CNY");
		priceRuleDetil_1.setRuleType("PRICE_POL");
		priceRuleDetil_1.setCalculateType("+");
		priceRuleDetil_1.setCalculateValue("5.000000");
		
		
		PriceRuleDetail priceRuleDetil2 = new PriceRuleDetail();
		priceRuleDetil2.setRuleActionName("RESALE_PRICE");
		priceRuleDetil2.setDeliveryPlace("USD");
		priceRuleDetil2.setRuleType("PRICE_POL");
		priceRuleDetil2.setCalculateType("+");
		priceRuleDetil2.setCalculateValue("5.000000");
		PriceRuleDetail priceRuleDetil2_2 = new PriceRuleDetail();
		priceRuleDetil2_2.setRuleActionName("RESALE_PRICE");
		priceRuleDetil2_2.setDeliveryPlace("CNY");
		priceRuleDetil2_2.setRuleType("PRICE_POL");
		priceRuleDetil2_2.setCalculateType("+");
		priceRuleDetil2_2.setCalculateValue("5.000000");
		
		
		PriceRuleDetail priceRuleDetil3 = new PriceRuleDetail();
		priceRuleDetil3.setRuleActionName("SPECIAL_RESALE_PRICE");
		priceRuleDetil3.setDeliveryPlace("USD");
		priceRuleDetil3.setRuleType("PRICE_POL");
		priceRuleDetil3.setCalculateType("+");
		priceRuleDetil3.setCalculateValue("5.000000");
		PriceRuleDetail priceRuleDetil3_3 = new PriceRuleDetail();
		priceRuleDetil3_3.setRuleActionName("SPECIAL_RESALE_PRICE");
		priceRuleDetil3_3.setDeliveryPlace("CNY");
		priceRuleDetil3_3.setRuleType("PRICE_POL");
		priceRuleDetil3_3.setCalculateType("+");
		priceRuleDetil3_3.setCalculateValue("5.000000");
		
		
		List<PriceRuleDetail> priceRules = new ArrayList<PriceRuleDetail>();
		priceRules.add(priceRuleDetil);
		priceRules.add(priceRuleDetil_1);
		priceRules.add(priceRuleDetil2);
		priceRules.add(priceRuleDetil2_2);
		priceRules.add(priceRuleDetil3);
		priceRules.add(priceRuleDetil3_3);
		
		priceRuleTemplate.setPriceRuleDetails(priceRules);
		productPriceRuleManager.update(priceRuleTemplate,null);
	}
	
	/**
	 * 测试更新定价规则
	 * 期望结果：更新规则抛出业务异常InvalidDataException规则名称重复，请重新输入！
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value =  "classpath:com/yikuyi/product/rule/price/manager/product_price_rule_manager_sample_result.xml" )
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "classpath:com/yikuyi/product/rule/price/manager/product_price_rule_manager_update_result.xml")
	public void testUpdateWithException(){
		PriceRuleTemplate priceRuleTemplate = new PriceRuleTemplate();
		priceRuleTemplate.setRuleId("10001");
		//设置一个已经存在的名称
		priceRuleTemplate.setRuleName("testrulename2");
		priceRuleTemplate.setVendorName("digikey");
		priceRuleTemplate.setWarehouse("美国");
		priceRuleTemplate.setCategory("0011-0021-0031");
		priceRuleTemplate.setCurrencyType("USD");
		priceRuleTemplate.setDescription("DESCRIPTION");
		
		
		PriceRuleDetail priceRuleDetil = new PriceRuleDetail();
		priceRuleDetil.setRuleActionName("REAL_COST");
		priceRuleDetil.setDeliveryPlace("USD");
		priceRuleDetil.setRuleType("PRICE_POL");
		priceRuleDetil.setCalculateType("+");
		priceRuleDetil.setCalculateValue("5.000000");
		PriceRuleDetail priceRuleDetil_1 = new PriceRuleDetail();
		priceRuleDetil_1.setRuleActionName("REAL_COST");
		priceRuleDetil_1.setDeliveryPlace("CNY");
		priceRuleDetil_1.setRuleType("PRICE_POL");
		priceRuleDetil_1.setCalculateType("+");
		priceRuleDetil_1.setCalculateValue("5.000000");
		
		
		PriceRuleDetail priceRuleDetil2 = new PriceRuleDetail();
		priceRuleDetil2.setRuleActionName("RESALE_PRICE");
		priceRuleDetil2.setDeliveryPlace("USD");
		priceRuleDetil2.setRuleType("PRICE_POL");
		priceRuleDetil2.setCalculateType("+");
		priceRuleDetil2.setCalculateValue("5.000000");
		PriceRuleDetail priceRuleDetil2_2 = new PriceRuleDetail();
		priceRuleDetil2_2.setRuleActionName("RESALE_PRICE");
		priceRuleDetil2_2.setDeliveryPlace("CNY");
		priceRuleDetil2_2.setRuleType("PRICE_POL");
		priceRuleDetil2_2.setCalculateType("+");
		priceRuleDetil2_2.setCalculateValue("5.000000");
		
		
		PriceRuleDetail priceRuleDetil3 = new PriceRuleDetail();
		priceRuleDetil3.setRuleActionName("SPECIAL_RESALE_PRICE");
		priceRuleDetil3.setDeliveryPlace("USD");
		priceRuleDetil3.setRuleType("PRICE_POL");
		priceRuleDetil3.setCalculateType("+");
		priceRuleDetil3.setCalculateValue("5.000000");
		PriceRuleDetail priceRuleDetil3_3 = new PriceRuleDetail();
		priceRuleDetil3_3.setRuleActionName("SPECIAL_RESALE_PRICE");
		priceRuleDetil3_3.setDeliveryPlace("CNY");
		priceRuleDetil3_3.setRuleType("PRICE_POL");
		priceRuleDetil3_3.setCalculateType("+");
		priceRuleDetil3_3.setCalculateValue("5.000000");
		
		
		List<PriceRuleDetail> priceRules = new ArrayList<PriceRuleDetail>();
		priceRules.add(priceRuleDetil);
		priceRules.add(priceRuleDetil_1);
		priceRules.add(priceRuleDetil2);
		priceRules.add(priceRuleDetil2_2);
		priceRules.add(priceRuleDetil3);
		priceRules.add(priceRuleDetil3_3);
		
		priceRuleTemplate.setPriceRuleDetails(priceRules);
		
		thrown.expect(InvalidDataException.class);
		thrown.expectMessage("规则名称重复，请重新输入！");
		productPriceRuleManager.update(priceRuleTemplate,null);
		priceRuleTemplate.setRuleId(null);
		productPriceRuleManager.update(priceRuleTemplate,null);
	}
	
	
	
	/**
	 * 测试根据ID查询定价规则
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value =  "classpath:com/yikuyi/product/rule/price/manager/product_price_rule_manager_sample_result.xml" )
	public void testGetById(){
		PriceRuleTemplate  priceRuleTemplate = productPriceRuleManager.getById("10001");
		assertNotNull(priceRuleTemplate);
	}
	
	/**
	 * 测试根据ID查询定价规则
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value =  "classpath:com/yikuyi/product/rule/price/manager/product_price_rule_manager_sample_result.xml" )
	public void testFindList(){
		PageInfo<PriceRuleTemplate> result = productPriceRuleManager.findList(null,null,null,null,null,"PRODUCT_PRICE", 1, 20);
		assertEquals(1, result.getSize());
	}
	
	/**
	 * 测试setCategoryNames方法
	 */
	@Test
	public void testSetCategoryNames(){
		//PriceRuleTemplate  priceRuleTemplate = productPriceRuleManager.getById("810490032841293824");
		//assertNotNull(priceRuleTemplate);
	}

}
