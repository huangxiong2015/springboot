package com.yikuyi.product.promotion.bll;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
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
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.yikuyi.promotion.model.PromoModuleProduct.ModuleProductStatus;
import com.yikuyi.promotion.model.PromoModuleProductDraft;
import com.yikuyi.promotion.vo.PromoModuleProductVo;
import com.ykyframework.exception.BusinessException;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
	TransactionDbUnitTestExecutionListener.class })
@Transactional
@Rollback
public class PromotionCacheManagerTest {
	@Autowired
	private PromotionCacheManager promotionCacheManager;
	
	
	private MockHttpServletRequest request; 
    private MockHttpServletResponse response; 
    
	@Before
	public void config() {
		MockitoAnnotations.initMocks(this);
	}
    
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
	 * 编辑商品草稿模块
	 * 
	 * @param promotion
	 * @param rowBounds
	 * @return
	 * @since 2017年10月11日
	 * @author zr.helinmei@yikuyi.com
	 * @throws BusinessException
	 */
	@Test
	public void saveTest() throws Exception{
		List<PromoModuleProductVo> productsList = new ArrayList<>();
		PromoModuleProductVo productVo = new PromoModuleProductVo();
		productVo.setPromoModuleProductId("928555055026536448");
		productVo.setPromoModuleId("928553103349776384");
		productVo.setProductId("1483692631812187");
		productVo.setManufacturerPartNumber("PF500XL");
		productVo.setManufacturer("3M");
		productVo.setSourceId("digikey-100");
		productVo.setSourceName("Digikey 现货");
		productVo.setVendorId("digikey");
		productVo.setVendorName("digikey");
		productVo.setDiscount(Float.valueOf("0.00000"));
		productVo.setCurrencyUomId("CNY");
		productVo.setQtyBreak1(1);
		productVo.setPriceBreak1(Double.valueOf("20.00000"));
		productVo.setImage1("//img1.ykystatic.com/product/photos/1/large/pf400l,pf400xl,pf400xxl,pf500l,pf500xl.jpg");
		productVo.setTotalQty(8L);
		productVo.setQty(8L);
		productVo.setProcessId("928555049808822272");
		productVo.setPromotionId("928552928992559104");
		productVo.setStatus(ModuleProductStatus.ENABLE);
		productsList.add(productVo);
			
		promotionCacheManager.addPreviewProductCache(productsList);
		
	}
	
	/**
	 * 根据活动ID和商品ID删除预览缓存
	 * @throws Exception
	 * @since 2017年11月23日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void deletePreviewProductCacheTest() throws Exception{
		String promotionId  = "928552928992559104";
		List<PromoModuleProductDraft> list = new ArrayList<>();
		PromoModuleProductDraft draft = new PromoModuleProductDraft();
		draft.setPromoModuleProductId("928555055026536448");
		draft.setPromoModuleId("928553103349776384");
		draft.setProductId("1483692631812187");
		draft.setManufacturerPartNumber("M3KKK-1040K");
		draft.setManufacturer("3M");
		draft.setSourceName("Digikey 现货");
		draft.setVendorName("digikey");
		draft.setDiscount(Float.valueOf("0.00000"));
		draft.setCurrencyUomId("CNY");
		draft.setQtyBreak1("1");
		draft.setPriceBreak1("10.0582");
		draft.setImage1("//img1.ykystatic.com/product/photos/1/large/pf400l,pf400xl,pf400xxl,pf500l,pf500xl.jpg");
		draft.setProcessId("928555049808822272");
		draft.setPromotionId("928552928992559104");
		draft.setStatus(ModuleProductStatus.ENABLE);
		list.add(draft);
		
		promotionCacheManager.deletePreviewProductCache(promotionId, list);
	}
	
	
}
