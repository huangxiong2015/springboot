package com.yikuyi.product.rule.common.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.yikuyi.rule.price.ProductPriceAction;
import com.yikuyi.rule.price.ProductPriceCond;
import com.yikuyi.rule.price.ProductPriceRule;
import com.yikuyi.rule.price.ProductPriceRule.PricePurposeType;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class })
@Transactional
@Rollback
public class RuleCommonManagerTest {

	@Autowired
	private RuleCommonManager ruleCommonManager;
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value =  "classpath:com/yikuyi/product/rule/common/manager/product_rule_common_manager_sample_result.xml" )
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "classpath:com/yikuyi/product/rule/common/manager/product_rule_common_manager_insert_result.xml")
	public void testInsert() {
		
		ProductPriceRule productPriceRule = new ProductPriceRule();
		productPriceRule.setRuleName("testrulename2");
		productPriceRule.setPricePurposeType(PricePurposeType.PRODUCT_PRICE);
//		productPriceRule.setRuleStatus(RuleStatus.ENABLED);
		productPriceRule.setDescription("DESCRIPTION");
		
		ProductPriceCond productPriceCond = new ProductPriceCond();
		productPriceCond.setInputParamEnumId("VENDOR_ID");
		productPriceCond.setCondValue("digikey");
		productPriceCond.setProductPriceCondSeqId("01");
		productPriceCond.setOperatorEnumId("PRC_EQ");
		
		ProductPriceCond productPriceCond2 = new ProductPriceCond();
		productPriceCond2.setInputParamEnumId("SPECIAL_RESALE_PRICE");
		productPriceCond2.setCondValue("SPECIAL_RESALE_PRICE");
		productPriceCond2.setProductPriceCondSeqId("02");
		productPriceCond2.setOperatorEnumId("PRC_EQ");
		
		List<ProductPriceCond> conds = new ArrayList<ProductPriceCond>();
		conds.add(productPriceCond);
		conds.add(productPriceCond2);
		productPriceRule.setConds(conds);
		
		ProductPriceAction productPriceAction = new ProductPriceAction();
		productPriceAction.setProductPriceActionTypeId("PRICE_POL");
		productPriceAction.setProductPriceCondSeqId("02");
		productPriceAction.setProductPriceActionSeqId("01");
		productPriceAction.setUomId("USD");
		productPriceAction.setAmount(new BigDecimal("5.000000"));
		productPriceAction.setProductPricePurposeId("SPECIAL_RESALE_PRICE");
		List<ProductPriceAction> actions = new ArrayList<ProductPriceAction>();
		actions.add(productPriceAction);
		productPriceRule.setActions(actions);
		
		ruleCommonManager.insert(productPriceRule);
	}

}
