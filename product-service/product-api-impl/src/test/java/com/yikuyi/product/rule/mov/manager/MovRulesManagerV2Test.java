package com.yikuyi.product.rule.mov.manager;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.framework.springboot.model.LoginUser;
import com.framewrok.springboot.web.LoginUserInjectionInterceptor;
import com.github.pagehelper.PageInfo;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.yikuyi.rule.mov.vo.MovRuleTemplate;
import com.yikuyi.rule.price.ProductPriceRule;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class })
@Transactional
@Rollback
public class MovRulesManagerV2Test {

	@Autowired
	private MovRulesManagerV2 movRulesManagerV2;
	
	private MockHttpServletRequest request; 
    private MockHttpServletResponse response; 
	@Before
	public void init() {
		LoginUser loginUser = new LoginUser("9999999901", "admin", "9999999901", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));		
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));;
		RequestContextHolder.currentRequestAttributes().setAttribute(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser, RequestAttributes.SCOPE_REQUEST);
		request = new MockHttpServletRequest();    
	    request.setCharacterEncoding("UTF-8");    
	    response = new MockHttpServletResponse();    
	}
	
	/**
	 * 插入定价规则模板
	 * @throws Exception
	 * @since 2017年11月23日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void insertTest() throws Exception{
		String userId = "9999999901";
		MovRuleTemplate movRuleTemplate = new MovRuleTemplate();
		movRuleTemplate.setRuleId("832064737826074526");
		movRuleTemplate.setDescription("hahahhahahha");
		movRuleTemplate.setVendorId("digikey-100");
		movRuleTemplate.setCnyMovAmount("98.00856");
		movRuleTemplate.setUsdMovAmount("156.11253");
		
		movRulesManagerV2.insert(movRuleTemplate, userId);
	}
	
	/**
	 * 修改MOV策略
	 * @throws Exception
	 * @since 2017年11月23日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT , value={"classpath:com/yikuyi/product/rule/mov/manager/mov_rules_result.xml"})
	public void updateTest() throws Exception{
		String userId = "9999999901";
		MovRuleTemplate movRuleTemplate = new MovRuleTemplate();
		movRuleTemplate.setRuleId("832064737826074526");
		movRuleTemplate.setDescription("哈哈哈哈哈哈哈");
		movRuleTemplate.setVendorId("RESALE_PRICE");
		movRuleTemplate.setCnyMovAmount("98.00856");
		movRuleTemplate.setUsdMovAmount("156.11253");
		
		movRulesManagerV2.update(movRuleTemplate, userId);
	}
	
	/**
	 * 启用、禁用或删除MOV策略
	 * @throws Exception
	 * @since 2017年11月23日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT , value={"classpath:com/yikuyi/product/rule/mov/manager/mov_rules_result.xml"})
	public void updateStatusTest() throws Exception{
		String ruleId = "832064737826074526";
		String userId = "9999999901";
		MovRuleTemplate movRuleTemplate = new MovRuleTemplate();
		movRuleTemplate.setRuleId("832064737826074526");
		movRuleTemplate.setDescription("哈哈哈哈哈哈哈");
		movRuleTemplate.setVendorId("RESALE_PRICE");
		movRuleTemplate.setCnyMovAmount("98.00856");
		movRuleTemplate.setUsdMovAmount("156.11253");
		movRuleTemplate.setStatus("DISABLED");
		
		MovRuleTemplate ruleTemplateEnable = movRulesManagerV2.updateStatus(ruleId, ProductPriceRule.RuleStatus.ENABLED, userId, movRuleTemplate);
		
		MovRuleTemplate ruleTemplateDisable = movRulesManagerV2.updateStatus(ruleId, ProductPriceRule.RuleStatus.DISABLED, userId, movRuleTemplate);
	}
	
	/**
	 * 根据策略ID查找MOV策略模板
	 * @throws Exception
	 * @since 2017年11月23日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT , value={"classpath:com/yikuyi/product/rule/mov/manager/mov_rules_result.xml"})
	public void getByIdTest() throws Exception{
		String id = "832064737826074526";
		MovRuleTemplate movRuleTemplate = movRulesManagerV2.getById(id);
	
	}

	/**
	 * 列表查询MOV策略模板
	 * @throws Exception
	 * @since 2017年11月23日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT , value={"classpath:com/yikuyi/product/rule/mov/manager/mov_rules_result.xml"})
	public void findListTes() throws Exception{
		
		String vendorId="digikey";
		int page=1; 
		int size = 20;
		PageInfo<MovRuleTemplate> pageInfo = movRulesManagerV2.findList(vendorId, page, size);
	}
}
