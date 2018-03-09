package com.yikuyi.product.rule.common.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.assertEquals;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.yikuyi.rule.price.ProductPriceAction;
import com.yikuyi.rule.price.ProductPriceCond;
import com.yikuyi.rule.price.ProductPriceRule;
import com.yikuyi.rule.price.ProductPriceRule.PricePurposeType;
import com.yikuyi.rule.price.ProductPriceRule.RuleStatus;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class })
@Transactional
@Rollback
public class ProductPriceRuleDaoTest {

	
	@Autowired
	private ProductPriceRuleDao productPriceRuleDao;
	@Autowired
	private ProductPriceCondDao productPriceCondDao;
	@Autowired
	private ProductPriceActionDao productPriceActionDao;
	/**
	 * 测试插入
	 * 
	 * @since 2016年10月18日
	 * @author zr.wanghong@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value =  "classpath:com/yikuyi/product/rule/common/dao/product_price_rule_dao_sample_result.xml" )
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "classpath:com/yikuyi/product/rule/common/dao/product_price_rule_dao_insert_result.xml")
	public void testInsert() {
		
		ProductPriceRule productPriceRule = new ProductPriceRule();
		productPriceRule.setPriceRuleId("10002");
		productPriceRule.setRuleName("testrulename2");
		productPriceRule.setPricePurposeType(PricePurposeType.PRODUCT_PRICE);
		productPriceRule.setRuleStatus(RuleStatus.DISABLED);
		productPriceRule.setDescription("DESCRIPTION");
		productPriceRuleDao.insert(productPriceRule);
		
		ProductPriceCond productPriceCond = new ProductPriceCond();
		productPriceCond.setProductPriceRuleId("10002");
		productPriceCond.setInputParamEnumId("VENDOR_ID");
		productPriceCond.setCondValue("digikey");
		productPriceCond.setProductPriceCondSeqId("01");
		productPriceCond.setOperatorEnumId("PRC_EQ");
		productPriceCondDao.insert(productPriceCond);
		
		ProductPriceAction productPriceAction = new ProductPriceAction();
		productPriceAction.setProductPriceRuleId("10002");
		productPriceAction.setProductPriceCondSeqId("01");
		productPriceAction.setProductPriceActionTypeId("PRICE_POL");
		productPriceAction.setProductPriceActionSeqId("01");
		productPriceAction.setUomId("USD");
		productPriceAction.setAmount(new BigDecimal("5.000000"));
		productPriceAction.setProductPricePurposeId("SPECIAL_RESALE_PRICE");
		productPriceActionDao.insert(productPriceAction);
	}
	
	/**
	 * 测试根据模板类型统计总数
	 * 
	 * @since 2016年12月15日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value =  "classpath:com/yikuyi/product/rule/common/dao/product_price_rule_dao_sample_result2.xml" )
	public void testFindListCount(){
		//模板类型不为空
		Long listCount = productPriceRuleDao.findListCount(PricePurposeType.PRODUCT_PRICE.toString(),null,null,null,null,null);
		assertEquals(Long.valueOf("1"),listCount);
		//模板类型为空
		Long listCount1 = productPriceRuleDao.findListCount(null,null,null,null,null,null);
		assertEquals(Long.valueOf("3"),listCount1);
		
	}
	
	/**
	 * 测试查询列表数据
	 * 
	 * @since 2016年12月15日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value =  "classpath:com/yikuyi/product/rule/common/dao/product_price_rule_dao_sample_result2.xml" )
	public void testFindList(){
		//模板类型不为空
		List<ProductPriceRule> listInfo = productPriceRuleDao.findList(PricePurposeType.PRODUCT_PRICE.toString(), 0, 20,null,null,null,null,null);
		assertEquals(1,listInfo.size());
		//模板类型为空
		List<ProductPriceRule> listInfo1 = productPriceRuleDao.findList(null, 0, 20,null,null,null,null,null);
		assertEquals(3,listInfo1.size());
	}
	
	/**
	 * 测试检验模板名称是否存在
	 * 
	 * @since 2016年12月15日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value =  "classpath:com/yikuyi/product/rule/common/dao/product_price_rule_dao_sample_result2.xml" )
	public void testCheckRuleNameIsExist(){
		ProductPriceRule ruleInfo = new ProductPriceRule();
		ruleInfo.setPricePurposeType(PricePurposeType.PRODUCT_PRICE);
		ruleInfo.setRuleName("testrulename");
		//Id为空
		int count1 = productPriceRuleDao.checkRuleNameIsExist(ruleInfo);
		assertEquals(1,count1);
		//Id不为空
		ruleInfo.setPriceRuleId("10001");
		int count2 = productPriceRuleDao.checkRuleNameIsExist(ruleInfo);
		assertEquals(0,count2);
	}
	
	/**
	 * 测试根据Id查询详情
	 * 
	 * @since 2016年12月15日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.INSERT, value =  "classpath:com/yikuyi/product/rule/common/dao/product_price_rule_dao_sample_result2.xml" )
	public void testGetDetailById(){
		ProductPriceRule ruleInfo = productPriceRuleDao.getDetailById("10001");
		assertEquals("testrulename",ruleInfo.getRuleName());
	}

	/**
	 * 测试是否有相同规则的模板正在启用状态中
	 * 
	 * @since 2016年12月15日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value =  "classpath:com/yikuyi/product/rule/common/dao/product_price_rule_dao_sample_result2.xml" )
	public void testFindSameRule(){
		Map<String,Object> map = new HashMap<>();
		//不存在相同的
		map.put("purposeType", PricePurposeType.PRODUCT_PRICE.toString());
		Map<String,Object> mapInfo1 = productPriceRuleDao.findSameRule(map);
		assertEquals(true,mapInfo1 == null);
		//存在相同的
		map.put("purposeType", PricePurposeType.LEAD_TIME.toString());
		map.put("concatValue", "digikey");
		Map<String,Object> mapInfo = productPriceRuleDao.findSameRule(map);
		assertEquals(true,mapInfo != null);
		assertEquals("testrulenamejiaoqi",(String)mapInfo.get("RULENAME"));
	}
	
	/**
	 * 测试修改
	 * 
	 * @since 2016年12月15日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value =  "classpath:com/yikuyi/product/rule/common/dao/product_price_rule_dao_sample_result.xml" )
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "classpath:com/yikuyi/product/rule/common/dao/product_price_rule_dao_update_result.xml")
	public void testUpdate(){
		ProductPriceRule ruleInfo = new ProductPriceRule();
		ruleInfo.setRuleStatus(ProductPriceRule.RuleStatus.ENABLED);
		ruleInfo.setPriceRuleId("10001");
		ruleInfo.setLastUpdateUser("test");
		productPriceRuleDao.update(ruleInfo);
	}
	
	/**
	 * 查询同一供应商是否有其他规则模板正在启用中
	 * 
	 * @since 2016年12月16日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value =  "classpath:com/yikuyi/product/rule/common/dao/product_price_rule_dao_sample_result2.xml" )
	public void testGetVendorRuleInfo(){
		Map<String,Object> map = new HashMap<>();
		map.put("pricePurposeType", ProductPriceRule.PricePurposeType.LEAD_TIME);
		map.put("condValue", "digikey");
		//存在同一供应商还有在启用的模板
		int count = productPriceRuleDao.getVendorRuleInfo(map);
		assertEquals(1,count);
		//不存在同一供应商还有在启用的模板
		ProductPriceRule ruleInfo = new ProductPriceRule();
		ruleInfo.setRuleStatus(ProductPriceRule.RuleStatus.DISABLED);
		ruleInfo.setPriceRuleId("10003");
		ruleInfo.setLastUpdateUser("test");
		productPriceRuleDao.update(ruleInfo);
		int count1 = productPriceRuleDao.getVendorRuleInfo(map);
		assertEquals(0,count1);
	}
}
