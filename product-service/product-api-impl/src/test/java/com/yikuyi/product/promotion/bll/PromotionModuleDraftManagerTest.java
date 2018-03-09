package com.yikuyi.product.promotion.bll;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

import com.alibaba.fastjson.JSONObject;
import com.framework.springboot.model.LoginUser;
import com.framewrok.springboot.web.LoginUserInjectionInterceptor;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.yikuyi.promotion.model.PromotionModuleContentDraft;
import com.yikuyi.promotion.model.PromotionModuleDraft;
import com.yikuyi.promotion.model.PromotionModuleDraft.PromoModuleStatus;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
	TransactionDbUnitTestExecutionListener.class ,MockitoTestExecutionListener.class})
@Transactional
@Rollback
public class PromotionModuleDraftManagerTest {
	@Autowired
	private PromotionModuleDraftManager draftManager;
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
	 * 创建活动
	 * @param promotion
	 * @return
	 * @since 2017年10月9日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	public void testInsert(){
		PromotionModuleDraft promotion = new PromotionModuleDraft();
		promotion.setPromoModuleId("1");
		promotion.setPromoModuleStatus(PromoModuleStatus.ENABLE);
		promotion.setPromotionId("1");
		draftManager.insert(promotion);
	}
	/**
	 * 编辑草稿模块
	 * @param promotion
	 * @return
	 * @since 2017年10月9日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	public void testSave(){
		PromotionModuleContentDraft draft = new PromotionModuleContentDraft();
		draft.setPromoModuleId("111");
		draft.setPromotionId("111");
		draft.setPromoModuleType("BANNER");
		
		String jsonString ="{'showSet': {'title': 'tt','showTitle': true,'desc': '<p>天文台问题</p>'},'contentSet': {'discount': {'value': '0.8','isOpen': 'true'},'search': {'text': '搜索更多XX产品'},'page': {'pageNum': '50','perPage': '10'},'dataSource': 'GET_BY_UPLOAD','uploadData': {'useStockQty': 'false','fileUrl': '//ictrade-public-hz-uat.oss-cn-hangzhou.aliyuncs.com/dev/productUpload/activityProductsUpload/201710/12/a0a6c43c156766e13d9303c6972bceac.xlsx','fileName': 'dev1条有效 .xlsx'},'promotionFlag': {'show': 'true','type': '1'},'condition': {'vendor': ['1'],'brand': ['1'],'category': ['1']},'fields': [{'value': 'id','textName': '型号','alias': 'xx','seq': 0},{'value': 'operator','textName': '操作','alias': '快来点我呀','seq': 6}]}}";
		JSONObject jsonObject =JSONObject.parseObject(jsonString);
		draft.setPromotionContent(jsonObject);
		draftManager.save(draft);
		
		PromotionModuleContentDraft draft1 = new PromotionModuleContentDraft();
		draft1.setPromotionId("1113");
		draft1.setPromoModuleType("BANNER");
		draft1.setPromoModuleId("555555555");
		String jsonString1 ="{'showSet': {'title': 'tt','showTitle': true,'desc': '<p>天文台问题</p>'},'contentSet': {'discount': {'value': '0.8','isOpen': 'true'},'search': {'text': '搜索更多XX产品'},'page': {'pageNum': '50','perPage': '10'},'dataSource': 'GET_BY_UPLOAD','uploadData': {'useStockQty': 'false','fileUrl': '//ictrade-public-hz-uat.oss-cn-hangzhou.aliyuncs.com/dev/productUpload/activityProductsUpload/201710/12/a0a6c43c156766e13d9303c6972bceac.xlsx','fileName': 'dev1条有效 .xlsx'},'promotionFlag': {'show': 'true','type': '1'},'condition': {'vendor': ['1'],'brand': ['1'],'category': ['1']},'fields': [{'value': 'id','textName': '型号','alias': 'xx','seq': 0},{'value': 'operator','textName': '操作','alias': '快来点我呀','seq': 6}]}}";
		JSONObject jsonObject1 =JSONObject.parseObject(jsonString1);
		draft1.setPromotionContent(jsonObject1);
		draftManager.save(draft1);
	}
	
	/**
	 * 创建活动
	 * @param promotion
	 * @return
	 * @since 2017年10月9日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	public void testPromotinModuleOrderSeq(){
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
		draftManager.promotinModuleOrderSeq(list);
	}
	
	/**
	 * 删除促销活动模块数据
	 * @throws Exception
	 * @since 2017年11月17日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT , value={"classpath:com/yikuyi/product/promotion/promotion_list.xml"})
	public void deletePromotionModuleAndDraftTest() throws Exception{
		String promoModuleId = "918371492083793920";
		draftManager.deletePromotionModuleAndDraft(promoModuleId);
	}
	
	/**
	 * 查询促销模块草稿详情
	 * @throws Exception
	 * @since 2017年11月17日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT , value={"classpath:com/yikuyi/product/promotion/promotion_list.xml"})
	public void getPromotionModuleDraftTest() throws Exception{
		String promoModuleId = "918371492083793920";
		PromotionModuleDraft draft = draftManager.getPromotionModuleDraft(promoModuleId);
	}
}
