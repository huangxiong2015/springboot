package com.yikuyi.product.promotion.bll;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
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
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.yikuyi.promotion.model.PromotionModule;
import com.yikuyi.promotion.vo.PromotionPreviewVo;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
	TransactionDbUnitTestExecutionListener.class ,MockitoTestExecutionListener.class})
@Transactional
@Rollback
public class PromotionModuleManagerTest {
	@Autowired
	private PromotionModuleManager moduleManager;
	
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
	 * 查询模块详情
	 * @throws Exception
	 * @since 2017年11月17日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT , value={"classpath:com/yikuyi/product/promotion/promotion_list.xml"})
	public void getPromotionModuleTest() throws Exception{
		String promoModuleId = "91837149208374589";
		PromotionModule module = moduleManager.getPromotionModule(promoModuleId);
	}
	
	/**
	 * 删除促销活动模块数据
	 * @throws Exception
	 * @since 2017年11月17日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT , value={"classpath:com/yikuyi/product/promotion/promotion_list.xml"})
	public void getPromotionDetailTest() throws Exception{
		String promoModuleId = "918371492083793385";
		PromotionPreviewVo previewVo = moduleManager.getPromotionDetail(null, promoModuleId, null, "Y");
		PromotionPreviewVo preview = moduleManager.getPromotionDetail(null, promoModuleId, null, "N");
	}
	
	
}
