package com.yikuyi.product.promotion.bll;


import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Date;

import org.apache.ibatis.session.RowBounds;
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
import com.github.pagehelper.PageInfo;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.yikuyi.promotion.model.Promotion;
import com.yikuyi.promotion.model.Promotion.PromotionStatus;
import com.yikuyi.promotion.vo.PromotionParamVo;
import com.yikuyi.promotion.vo.PromotionProductVo;
import com.yikuyi.promotion.vo.PromotionVo;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
	TransactionDbUnitTestExecutionListener.class ,MockitoTestExecutionListener.class})
@Transactional
@Rollback
public class PromotionManagerTest {
	@Autowired
	private PromotionManager promotionManager;
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
	 * 查询促销活动列表
	 * @throws Exception
	 * @since 2017年11月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT , value={"classpath:com/yikuyi/product/promotion/promotion_list.xml"})
	public void getPromotionListTest() throws Exception{
		PromotionParamVo paramVo = new PromotionParamVo();
		PageInfo<PromotionVo> pageInfo = promotionManager.getPromotionList(paramVo, RowBounds.DEFAULT);
		assertEquals("11111111111111111111111",pageInfo.getList().get(0).getPromotionName());
	}
	
	/**
	 * 创建活动
	 * @param promotion
	 * @return
	 * @since 2017年10月9日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	public void insertTest(){
		Promotion promotion = new Promotion();
		promotion.setPromotionName("BANNER");
		promotion.setStartDate(new Date());
		promotion.setEndDate(new Date());
		promotionManager.insert(promotion);
	}
	
	
	/**
	 * 在草稿表中复制促销活动草稿信息
	 * @throws Exception
	 * @since 2017年11月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT , value={"classpath:com/yikuyi/product/promotion/promotion_list.xml"})
	public void draftCopyTest() throws Exception{
		String promotionId = "918319040785547264";
		String userId = "9999999901";
		promotionManager.draftCopy(promotionId, userId);
	}
	
	
	/**
	 * 修改活动
	 * @param promotion
	 * @return
	 * @since 2017年10月9日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	public void updateTest() throws Exception{
		Promotion promotion = new Promotion();
		promotion.setPromotionId("111");
		promotion.setPromotionName("BANNER");
		promotion.setStartDate(new Date());
		promotion.setEndDate(new Date());
		promotionManager.update(promotion);
	}
	
	/**
	 * 查询模块商品列表
	 * @param promotion
	 * @param rowBounds
	 * @return
	 * @since 2017年10月9日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = { "classpath:com/yikuyi/product/promotion/product_sample.xml" })
	public void listModuleProductTest(){
		PromotionProductVo promotion = new PromotionProductVo();
		PageInfo<PromotionProductVo> list = promotionManager.listModuleProduct("Y","N",promotion, RowBounds.DEFAULT);
	}
	
	/**
	 * 查询活动
	 * @throws Exception
	 * @since 2017年11月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT , value={"classpath:com/yikuyi/product/promotion/promotion_list.xml"})
	public void getPromotionTest() throws Exception{
		String promotionId = "918319040785547264";
		Promotion promotion = promotionManager.getPromotion(promotionId);
		assertEquals("11111111111111111111111",promotion.getPromotionName());
	}
	
	/**
	 * 根据活动ID和状态修改状态
	 * @throws Exception
	 * @since 2017年11月17日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT , value={"classpath:com/yikuyi/product/promotion/promotion_list.xml"})
	public void updatePromotionStatusTest() throws Exception{
		String promotionId = "918319040785547264";
		promotionManager.updatePromotionStatus(promotionId, PromotionStatus.ENABLE);
	}
	
	
	/**
	 * 根据prmotionId查询所有的草稿模块json数据
	 * @throws Exception
	 * @since 2017年11月17日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT , value={"classpath:com/yikuyi/product/promotion/promotion_list.xml"})
	public void getPromotionModuleDraft2JsonTest() throws Exception{
		String promotionId = "918319040785547264";
		String result = promotionManager.getPromotionModuleDraft2Json(promotionId);
	}
	
	/**
	 * 根据prmotionId查询所有的模块json数据
	 * @throws Exception
	 * @since 2017年11月17日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT , value={"classpath:com/yikuyi/product/promotion/promotion_list.xml"})
	public void getPromotionModule2JsonTest() throws Exception{
		String promotionId = "918319040785547264";
		String result = promotionManager.getPromotionModule2Json(promotionId);
	}
	
	/**
	 * 定时器处理缓存中的活动信息
	 * @throws Exception
	 * @since 2017年11月17日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT , value={"classpath:com/yikuyi/product/promotion/promotion_list.xml"})
	public void handlePromotionCacheTest() throws Exception{
		promotionManager.handlePromotionCache(null);
	}
	
}
