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

import com.alibaba.fastjson.JSONObject;
import com.framework.springboot.model.LoginUser;
import com.framewrok.springboot.web.LoginUserInjectionInterceptor;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.yikuyi.promotion.model.PromoModuleProduct.ModuleProductStatus;
import com.yikuyi.promotion.model.PromotionModuleContentDraft;
import com.ykyframework.exception.BusinessException;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
	TransactionDbUnitTestExecutionListener.class })
@Transactional
@Rollback
public class PromoModuleProductDraftManagerTest {
	@Autowired
	private PromoModuleProductDraftManager draftManager;
	
	
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
		PromotionModuleContentDraft draft = new PromotionModuleContentDraft();
		draft.setPromoModuleId("111");
		draft.setPromotionId("111");
		draft.setPromoModuleType("BANNER");
		
		String jsonString ="{'showSet': {'title': 'tt','showTitle': true,'desc': '<p>天文台问题</p>'},'contentSet': {'dataSource':'GET_BY_UPLOAD','discount': {'value': '0.8','isOpen': 'true'},'search': {'text': '搜索更多XX产品'},'page': {'pageNum': '50','perPage': '10'},'dataSource': 'GET_BY_UPLOAD','uploadData': {'useStockQty': 'false','fileUrl': '//ictrade-public-hz-uat.oss-cn-hangzhou.aliyuncs.com/dev/productUpload/activityProductsUpload/201710/12/a0a6c43c156766e13d9303c6972bceac.xlsx','fileName': 'dev1条有效 .xlsx'},'promotionFlag': {'show': 'true','type': '1'},'condition': {'vendor': ['1'],'brand': ['1'],'category': [{'id':'1','name':'aa'}]},'fields': [{'value': 'id','textName': '型号','alias': 'xx','seq': 0},{'value': 'operator','textName': '操作','alias': '快来点我呀','seq': 6}]}}";
		JSONObject jsonObject =JSONObject.parseObject(jsonString);
		draft.setPromotionContent(jsonObject);
		try {
			draftManager.save(draft);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		
		PromotionModuleContentDraft draft1 = new PromotionModuleContentDraft();
		draft1.setPromoModuleId("111");
		draft1.setPromotionId("111");
		draft1.setPromoModuleType("BANNER");
		
		String jsonString1 ="{'showSet': {'title': 'tt','showTitle': true,'desc': '<p>天文台问题</p>'},'contentSet': {'dataSource':'GET_BY_CONDITION','discount': {'value': '0.8','isOpen': 'true'},'search': {'text': '搜索更多XX产品'},'page': {'pageNum': '50','perPage': '10'},'uploadData': {'useStockQty': 'false','fileUrl': '//ictrade-public-hz-uat.oss-cn-hangzhou.aliyuncs.com/dev/productUpload/activityProductsUpload/201710/12/a0a6c43c156766e13d9303c6972bceac.xlsx','fileName': 'dev1条有效 .xlsx'},'promotionFlag': {'show': 'true','type': '1'},'condition': {'vendor': ['1'],'brand': ['1'],'category': [{'id':'1','name':'aa'}]},'fields': [{'value': 'id','textName': '型号','alias': 'xx','seq': 0},{'value': 'operator','textName': '操作','alias': '快来点我呀','seq': 6}]}}";
		JSONObject jsonObject1 =JSONObject.parseObject(jsonString1);
		draft1.setPromotionContent(jsonObject1);
		try {
			draftManager.save(draft1);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 商品上传的文件解析
	 * @throws Exception
	 * @since 2017年11月17日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void parseFileTest() throws Exception{
		String activityId="12345";
		String periodsId = "12345";
		String fileUrl="http://ictrade-public-hz-uat.oss-cn-hangzhou.aliyuncs.com/sit/productUpload/activityProductsUpload/201711/17/045d9bff061259ed0ae65ca2fae77528.xlsx";
		String oriFileName="活动装修模板 (1).xlsx";
		draftManager.parseFile(activityId, periodsId, fileUrl, oriFileName);
	}
	
	/**
	 * 批量删除活动装修商品草稿信息
	 * @throws Exception
	 * @since 2017年11月17日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void deletePromoModuleProductDraftTest() throws Exception{
		String promoModuleId = "923028162302967808";
		String promotionId =  "923023388140634112";
		List<String> promoModuleProductIds = new ArrayList<String>();
		promoModuleProductIds.add("923028412837134336");
		promoModuleProductIds.add("923028414951063552");
		promoModuleProductIds.add("923028416691699712");
		draftManager.deletePromoModuleProductDraft(promoModuleId, promotionId, promoModuleProductIds);
	}
	
	/**
	 * 导出活动装修商品草稿信息
	 * @throws Exception
	 * @since 2017年11月17日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void exportProductsTest() throws Exception{
		draftManager.exportProducts(null, null, null, ModuleProductStatus.ENABLE, response);
	}
}
