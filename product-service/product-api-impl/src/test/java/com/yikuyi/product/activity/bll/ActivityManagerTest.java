package com.yikuyi.product.activity.bll;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.MongoRepository;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.springboot.model.LoginUser;
import com.framewrok.springboot.web.LoginUserInjectionInterceptor;
import com.github.pagehelper.PageInfo;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.yikuyi.activity.model.Activity;
import com.yikuyi.activity.model.Activity.Status;
import com.yikuyi.activity.model.ActivityPeriods;
import com.yikuyi.activity.model.ActivityProductDraft;
import com.yikuyi.activity.vo.ActivityProductDraftVo;
import com.yikuyi.activity.vo.ActivityProductVo;
import com.yikuyi.activity.vo.ActivityVo;
import com.yikuyi.category.model.ProductCategory;
import com.yikuyi.party.PartyClientBuilder;
import com.yikuyi.party.facility.model.Facility;
import com.yikuyi.party.group.model.PartyGroup;
import com.yikuyi.party.resource.FacilityClient;
import com.yikuyi.party.resource.PartyGroupClient;
import com.yikuyi.party.vo.PartyVo;
import com.yikuyi.product.activity.dao.ActivityProductDraftDao;
import com.yikuyi.product.model.Product;
import com.yikuyi.product.model.ProductPrice;
import com.yikuyi.product.model.ProductPriceLevel;
import com.yikuyi.product.model.ProductStand;
import com.yikuyi.product.model.VendorSeries;
import com.ykyframework.exception.BusinessException;


@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
	TransactionDbUnitTestExecutionListener.class, MockitoTestExecutionListener.class })
@Transactional
@Rollback
@ConfigurationProperties
@RunWith(SpringRunner.class)
@ComponentScan(basePackages = "com.yikuyi.product")
@SpringBootTest
//@ActiveProfiles("dev")
public class ActivityManagerTest{
	
	@SpyBean
	private PartyClientBuilder partyClientBuilder;
	
	@Autowired
	private PartyClientBuilder shipmentClientBuilder;
	
	@Autowired
	private ActivityManager activityManager;
	
	@Autowired
	private ActivityProductDraftDao activityProductDraftDao;
	
	@Autowired
	private MongoRepository<Product, String> productRepository;
	
	// party服务地址
	@Value("${api.party.serverUrlPrefix}")
	private String partyUrl;
	
	@Value("${api.basedata.serverUrlPrefix}")
	private String basedataServerUrlPrefix;

	ObjectMapper objectMapper = new ObjectMapper(); // JSON
	

	
	@Before
	public void config() {
		MockitoAnnotations.initMocks(this);
	}
	
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

	private void mockClientService(){
		PartyGroupClient sc = Mockito.spy(PartyGroupClient.class);
		Mockito.when(partyClientBuilder.partyGroupClient()).thenReturn(sc);
		List<PartyVo> list = new ArrayList<>();
		PartyVo partyVo = new PartyVo();
		PartyGroup group = new PartyGroup();
		group.setGroupName("阿里巴巴");
		partyVo.setId("865028795290091520");
		partyVo.setPartyGroup(group);
		list.add(partyVo);
		Mockito.when(sc.getAllPartyGroupList(Mockito.anyObject())).thenReturn(list);
		
		FacilityClient fc = Mockito.spy(FacilityClient.class);
		Mockito.when(shipmentClientBuilder.facilityResource()).thenReturn(fc);
		List<Facility> faList = new ArrayList<>();
		Facility fa = new Facility();
		fa.setId("digikey-100");
		fa.setFacilityName("Digikey 现货");
		faList.add(fa);
		Mockito.when(fc.getFacilityList(Mockito.anyString())).thenReturn(faList);
	}
	
	/**
	 * 
	 * @throws Exception
	 * @since 2017年11月24日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:com/yikuyi/product/activity/bll/activity_updateStatus_data.xml")
	public void updateActivityStatusTest() throws Exception{
		String activityId = "111";
		activityManager.updateActivityStatus(activityId, Status.ENABLE);
	}
	
	/**
	 * 抛异常处理
	 * @throws Exception
	 * @since 2017年11月24日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:com/yikuyi/product/activity/bll/activity_updateStatus_data.xml")
	public void updateActivityStatusTestException() throws Exception{
		try {
			activityManager.updateActivityStatus("85696", Status.ENABLE);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * 抛异常处理
	 * @throws Exception
	 * @since 2017年11月24日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:com/yikuyi/product/activity/bll/activity_updateStatus_data.xml")
	public void updateActivityStatusExceptionOutTest() throws Exception{
		try {
			String activityId = "123";
			activityManager.updateActivityStatus(activityId, Status.ENABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 修改活动信息的状态
	 * @throws ParseException
	 * @since 2017年6月19日
	 * @author zr.wenjiao@yikuyi.com
	 * @throws BusinessException 
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:com/yikuyi/product/activity/bll/activity_updateStatus_data.xml")
	public void updateStatusTest() throws ParseException, BusinessException{
		//需修改状态的活动
		Activity record = new Activity();
		record.setStatus(Activity.Status.ENABLE);
		record.setActivityId("111");
		//需放入缓存的活动
		Activity activityInfo = activityManager.getActivityInfo("111");
		Calendar c = Calendar.getInstance();
		activityInfo.setStartDate(c.getTime());
		c.add(Calendar.MONTH, 1);
		activityInfo.setEndDate(c.getTime());
		//启用
		activityManager.updateStatus(record, activityInfo);
		//停用
		record.setStatus(Activity.Status.UNABLE);
		activityManager.updateStatus(record, activityInfo);
	}
	
	/**
	 * 根据活动Id查询活动详情
	 * 
	 * @since 2017年6月19日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:com/yikuyi/product/activity/bll/activity_updateStatus_data.xml")
	public void testGetActivityInfo(){
		Activity info = activityManager.getActivityInfo("111");
		Assert.assertNotNull(info);
		assertEquals("测试",info.getName());
	}
	
	/**
	 * 
	 * 
	 * @since 2017年6月19日
	 * @author zr.wenjiao@yikuyi.com
	 * @throws BusinessException 
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:com/yikuyi/product/activity/bll/activity_updateStatus_data.xml")
	public void testActivityTask() throws BusinessException{
		activityManager.activityTask();
	}
	
	/**
	 * 根据Id删除数据
	 * 
	 * @since 2017年6月19日
	 * @author tb.lijing@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.DELETE, value = "classpath:com/yikuyi/product/activity/bll/activity_sample.xml")
	public void testDeleteActivityProductDraft(){
		String activityProductId = "1";
		String activityId = "1";
		String periodsId="1";
		activityManager.deleteActivityProductDraft(activityProductId,activityId,periodsId);
	}
	
	/**
	 * 根据Id批量删除数据
	 * 
	 * @since 2017年7月20日
	 * @author jik.shu@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.DELETE, value = "classpath:com/yikuyi/product/activity/bll/activity_sample.xml")
	public void testDeleteActivityProductDrafts(){
		List<String> activityProductId = Arrays.asList("1","2");
		String activityId = "1";
		String periodsId="1";
		activityManager.deleteActivityProductDraft(activityProductId,activityId,periodsId,"10000");
	}
	
	
	
	/**
	 * 解析文件
	 * @throws BusinessException
	 * @since 2017年6月20日
	 * @author tb.lijing@yikuyi.com
	 * @throws JsonProcessingException 
	 */
	@Test
	public void testParseFile() throws BusinessException, JsonProcessingException{
		this.mockClientService();
		String activityId="12345";
		String periodsId = "12345";
		String fileUrl="https://file-uat.ykystatic.com/sit/productUpload/activityProductsUpload/201709/01/c89421b96d81a2b170258230c339f588.xlsx";
		String oriFileName="测试.xlsx";
		activityManager.parseFile(activityId, periodsId, fileUrl, oriFileName);
		ActivityProductDraft vo = new ActivityProductDraft();
		vo.setActivityId(activityId);
		vo.setPeriodsId(periodsId);
		
		List<ActivityProductDraftVo> result = activityProductDraftDao.findActivityProductDraftByCondition(vo,RowBounds.DEFAULT);
		Assert.assertTrue(activityId.equals(result.get(0).getActivityId()));
	}
	
	/**
	 * 保存活动草稿
	 * @throws Exception
	 * @since 2017年6月21日
	 * @author zr.zhanghua@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:com/yikuyi/product/activity/bll/activityDraft_sample.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT,value = "classpath:com/yikuyi/product/activity/bll/activityDraft_add_result.xml")
	public void testSaveActivityDraft() throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ActivityVo activityVo = new ActivityVo();
		activityVo.setName("cnm0000");
		activityVo.setStartDates("2020-06-14");
		activityVo.setEndDates("2020-06-14");
		activityVo.setStatus(ActivityVo.Status.UNABLE);
		List<ActivityPeriods> list = new ArrayList<ActivityPeriods>();
		ActivityPeriods ap1 = new ActivityPeriods();
		ap1.setStartTime("05:00");
		ap1.setEndTime("06:00");
		ap1.setStatus(ActivityPeriods.Status.UNABLE);
		ActivityPeriods ap2 = new ActivityPeriods();
		ap2.setStartTime("07:00");
		ap2.setEndTime("08:00");
		list.add(ap1);
		list.add(ap2);
		activityVo.setPeriodsList(list);
		activityManager.saveActivityDraft(activityVo);		
	}
	
	/**
	 * 修改活动草稿
	 * @throws Exception
	 * @since 2017年6月19日
	 * @author zr.zhanghua@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:com/yikuyi/product/activity/bll/activityDraft_sample.xml")
	public void updatActivityDraftTest() throws BusinessException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ActivityVo activityVo = new ActivityVo();
		activityVo.setActivityId("110");
		activityVo.setName("cnmddhahha");
		activityVo.setStartDates("2019-01-18");
		activityVo.setEndDates("2019-01-19");
		activityVo.setStatus(ActivityVo.Status.ENABLE);
		List<ActivityPeriods> list = new ArrayList<ActivityPeriods>();
		ActivityPeriods ap1 = new ActivityPeriods();
		ap1.setActivityId("110");
		ap1.setPeriodsId("11000");
		ap1.setStartTime("01:00");
		ap1.setEndTime("03:00");
		list.add(ap1);
		activityVo.setPeriodsList(list);
		activityManager.updatActivityDraft(activityVo);		
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:com/yikuyi/product/activity/bll/activityDraft_sample.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT,value = "classpath:com/yikuyi/product/activity/bll/activityDraft_delete_result.xml")
	public void deleteActivityTest(){
		activityManager.deleteActivity("110");
	}
	
	/**
	 * 查询活动管理列表
	 * @throws Exception
	 * @since 2017年11月22日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "classpath:com/yikuyi/product/activity/bll/activity_sample.xml")
	public void findActivityByEntityTest() throws Exception{
		String name = "sdwewewew";
		String type = "ENABLE";
		String startTime = "2017-11-16 00:00:00";
		String endTime = "2017-12-30 00:00:00";
		PageInfo<ActivityVo> info = activityManager.findActivityByEntity(name, type, null, startTime, endTime, 1, 10);
		
	}
	
	/**
	 * 查询正式活动管理
	 * @throws Exception
	 * @since 2017年6月21日
	 * @author zr.zhanghua@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "classpath:com/yikuyi/product/activity/bll/activity_sample.xml")
	public void findActivityStandardByIdTest() throws Exception{
		ActivityVo vo = activityManager.findActivityStandardById("CS100");
	}
	
	/**
	 * 查询正式活动管理
	 * @throws Exception
	 * @since 2017年6月21日
	 * @author zr.zhanghua@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "classpath:com/yikuyi/product/activity/bll/activity_sample.xml")
	public void findActivityDraftByIdTest() throws Exception{
		ActivityVo vo = activityManager.findActivityDraftById("001");
	}
	
	/**
	 * 查询当天活动
	 * @throws Exception
	 * @since 2017年6月21日
	 * @author zr.zhanghua@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "classpath:com/yikuyi/product/activity/bll/activity_sample.xml")
	public void getTodayActivityTest() throws Exception{
		ActivityVo vo = activityManager.getTodayActivity();
	}
	
	/**
	 * 将草稿活动信息转化成正式活动信息
	 * @throws Exception
	 * @since 2017年11月16日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:com/yikuyi/product/activity/bll/activity_copy_draft.xml")
	public void draftToFormalTest() throws Exception{
		String activityId = "876729670001754112";
		String userId = "9999999901";
		String userName = "admin";
		String flag = activityManager.draftToFormal(activityId, userId, userName);
		assertEquals("success", flag);
		
		String activityIdnew = "876729670001774859";
		String flagNew = activityManager.draftToFormal(activityIdnew, userId, userName);
		assertEquals("success", flagNew);
	}
	
	/**
	 * 将草稿活动信息转化成正式活动信息(抛异常处理)
	 * @throws Exception
	 * @since 2017年11月22日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:com/yikuyi/product/activity/bll/activity_copy_draft.xml")
	public void draftToFormalExceptionTest(){
		try {
			String activityId = "8767296700017759648";
			String userId = "9999999901";
			String userName = "admin";
			String flag = activityManager.draftToFormal(activityId, userId, userName);
			assertEquals("活动商品为空！", flag);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 判断时区是否有效，并更新无活动商品时区的状态为无效(抛异常)
	 * @throws Exception
	 * @since 2017年11月16日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:com/yikuyi/product/activity/bll/activity_copy_draft.xml")
	public void updatePeriodsStatusExceptionTest() throws Exception{
		try {
			String activityId = "87672967000";
			activityManager.updatePeriodsStatus(activityId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 判断时区是否有效，并更新无活动商品时区的状态为无效
	 * @throws Exception
	 * @since 2017年11月16日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:com/yikuyi/product/activity/bll/activity_copy_draft.xml")
	public void updatePeriodsStatusTest() throws Exception{
		String activityId = "876729670001754112";
		activityManager.updatePeriodsStatus(activityId);
	}
	
	

	/**
	 * 查询当天正式获得详情（秒杀页面）
	 * @throws Exception
	 * @since 2017年11月16日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:com/yikuyi/product/activity/bll/activity_copy_draft.xml")
	public void getActivityStandardByIdTest() throws Exception{
		String activityId = "876729670001754112";
		ActivityVo activityVo = activityManager.getActivityStandardById(activityId);
	}
	
	
	/**
	 * 指定了具体供应商，不需要创建草稿直接进入正式表
	 * @throws Exception
	 * @since 2017年11月16日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void saveActivityTest() throws Exception{

		ActivityVo activityVo = new ActivityVo();
		activityVo.setName("cnm0000hahha");
		activityVo.setType("10000");
		activityVo.setStartDates("2020-06-14");
		activityVo.setEndDates("2020-06-14");
		activityVo.setStatus(ActivityVo.Status.UNABLE);
		List<ActivityPeriods> list = new ArrayList<ActivityPeriods>();
		ActivityPeriods ap1 = new ActivityPeriods();
		ap1.setStartTime("05:00");
		ap1.setEndTime("06:00");
		ap1.setStatus(ActivityPeriods.Status.UNABLE);
		ActivityPeriods ap2 = new ActivityPeriods();
		ap2.setStartTime("07:00");
		ap2.setEndTime("08:00");
		list.add(ap1);
		list.add(ap2);
		activityVo.setPeriodsList(list);
		Activity record = activityManager.saveActivity(activityVo);
	}
	
	/**
	 * 
	 * @throws Exception
	 * @since 2017年11月17日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:com/yikuyi/product/activity/bll/activity_copy_draft.xml")
	public void updatActivityTest() throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ActivityVo activityVo = new ActivityVo();
		activityVo.setActivityId("876729670001754112");
		activityVo.setName("cnmdd");
		activityVo.setStartDates("2018-07-18");
		activityVo.setEndDates("2018-07-20");
		activityVo.setStatus(ActivityVo.Status.ENABLE);
		List<ActivityPeriods> list = new ArrayList<ActivityPeriods>();
		ActivityPeriods ap1 = new ActivityPeriods();
		ap1.setActivityId("110");
		ap1.setPeriodsId("11000");
		ap1.setStartTime("04:00");
		ap1.setEndTime("06:00");
		ActivityPeriods ap2 = new ActivityPeriods();
		ap2.setActivityId("110");
		ap2.setPeriodsId("12000");
		ap2.setStartTime("07:00");
		ap2.setEndTime("09:00");
		list.add(ap1);
		list.add(ap2);
		activityVo.setPeriodsList(list);
		activityManager.updatActivity(activityVo);
	}
	
	/**
	 * 导出活动草稿商品
	 * @throws Exception
	 * @since 2017年11月17日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:com/yikuyi/product/activity/bll/activity_copy_draft.xml")
	public void exportProductsTest() throws Exception{
		String activityId = "876729670001754112";
		String periodsId = "8767296700";
		activityManager.exportProducts(null, activityId, periodsId, ActivityProductDraft.Status.ENABLE, response);
		
		//UNABLE
		activityManager.exportProducts(null, activityId, periodsId, ActivityProductDraft.Status.UNABLE, response);
	}
	
	/**
	 * 查询活动商品草稿数据
	 * @throws Exception
	 * @since 2017年11月17日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:com/yikuyi/product/activity/bll/activity_copy_draft.xml")
	public void testFindActivityProductDraftByCondition() throws Exception{
		int page = 1;
		int pageSize = 1000;
		RowBounds rowBounds = new RowBounds((page - 1) * pageSize, pageSize);
		ActivityProductDraft vo = new ActivityProductDraft();
		vo.setStatus(ActivityProductDraft.Status.ENABLE);
		 
		PageInfo<ActivityProductDraftVo> info = activityManager.findActivityProductDraftByCondition(vo,rowBounds);
		List<ActivityProductDraftVo> list = info.getList();
		assertNotNull(list);
	}
	
	/**
	 * 查询活动商品正式数据
	 * @throws Exception
	 * @since 2017年11月17日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "classpath:com/yikuyi/product/activity/bll/activity_updateStatus_data.xml")
	public void testFindActivityProductByCondition() throws Exception{
		String activityId = "111";
		String periodsId = "22111";
		String priceFlag = "Y";
		Product prod = this.initProductData();
		List<ActivityProductVo> result = activityManager.findActivityProductByCondition(activityId, periodsId, priceFlag);
		assertEquals(1, result.size());
		List<ActivityProductVo> resultNew = activityManager.findActivityProductByCondition(activityId, periodsId, "N");
	}
	
	
	/**
	 * 查询秒杀和促销的数据
	 * @throws Exception
	 * @since 2017年11月17日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:com/yikuyi/product/activity/bll/activity_copy_draft.xml")
	public void findActivityProductListTest() throws Exception{
		//"10001"表示是秒杀
		PageInfo<ActivityProductVo> pageInfo = activityManager.findActivityProductList(null, null, null, "10001");
		//"10000"表示是促销
		PageInfo<ActivityProductVo> pageInfoNew = activityManager.findActivityProductList(null, null, "Y", "10000");
	}
	
	
	/**
	 * 初始化正常的产品测试数据
	 * @return
	 */
	private Product initProductData(){
//		this.initExchangeRate();
		
		Product product = new Product();
		product.setId("123456");
		
		ProductStand spu = new ProductStand();
		spu.setId("1486460348695997");
		spu.setManufacturer("Amphenol");
		spu.setManufacturerId(69);
		spu.setManufacturerAgg("Amphenol");
		spu.setManufacturerPartNumber("TV06DT-25-4HAEREW");
		spu.setDescription("56 Position Circular Connector Plug, Male Pins Crimp Gold");
		spu.setRohs(true);
		
		List<ProductCategory> categories = new ArrayList<>();
		ProductCategory category = new ProductCategory();
		category.setId(700);
		category.setName("开发板");
		category.setStatus(1);
		category.setLevel(1);
		categories.add(category);
		
		category = new ProductCategory();
		category.setId(711);
		category.setName("评估板配件");
		category.setStatus(1);
		category.setLevel(2);
		categories.add(category);
		
		category = new ProductCategory();
		category.setId(714);
		category.setName("配件");
		category.setStatus(1);
		category.setLevel(3);
		categories.add(category);
		spu.setCategories(categories);
		
		product.setSpu(spu);
		product.setSkuId("AATV06DT-25-4HA-ND");
		product.setVendorName("digikey");
		product.setVendorId("digikey");
		product.setVendorDetailsLink("http://www.digikey.com/product-detail/en/amphenol-aerospace-operations/TV06DT-25-4HA/AATV06DT-25-4HA-ND/5539139");
		product.setVendorSeries(new VendorSeries());
		product.setPackaging("Bulk");
		product.setCountryCode("US");
		product.setQty(0L);
		product.setSourceId("digikey-100");
		
		List<ProductPrice> prices = this.initPrices();
		
		product.setPrices(prices);
		product.setMinimumQuantity(1);
		product.setStoreId(99999999L);
		productRepository.delete(product);
		productRepository.insert(product);
		return product;
	}
	
	/**
	 * 初始化有价格的产品测试数据
	 * @return
	 */
	private List<ProductPrice> initPrices() {
		List<ProductPrice> prices = new ArrayList<>();
		//人民币
		ProductPrice price = new ProductPrice();
		price.setCurrencyCode("CNY");
		price.setUnitPrice("2096.544645");
		List<ProductPriceLevel> priceLevels = new ArrayList<>();
		ProductPriceLevel priceLevel = new ProductPriceLevel();
		priceLevel.setPrice("2096.544645");
		priceLevel.setBreakQuantity(1l);
		priceLevels.add(priceLevel);
		priceLevel = new ProductPriceLevel();
		priceLevel.setPrice("1980.865926");
		priceLevel.setBreakQuantity(5l);
		priceLevels.add(priceLevel);
		priceLevel = new ProductPriceLevel();
		priceLevel.setPrice("1966.408119");
		priceLevel.setBreakQuantity(10l);
		priceLevels.add(priceLevel);
		priceLevel = new ProductPriceLevel();
		priceLevel.setPrice("1923.0281928");
		priceLevel.setBreakQuantity(50l);
		priceLevels.add(priceLevel);
		price.setPriceLevels(priceLevels);
		
		prices.add(price);
		
		//美元
		price = new ProductPrice();
		price.setCurrencyCode("USD");
		price.setUnitPrice("257.83");
		priceLevels = new ArrayList<>();
		priceLevel = new ProductPriceLevel();
		priceLevel.setPrice("257.83");
		priceLevel.setBreakQuantity(1l);
		priceLevels.add(priceLevel);
		priceLevel = new ProductPriceLevel();
		priceLevel.setPrice("243.604");
		priceLevel.setBreakQuantity(5l);
		priceLevels.add(priceLevel);
		priceLevel = new ProductPriceLevel();
		priceLevel.setPrice("241.826");
		priceLevel.setBreakQuantity(10l);
		priceLevels.add(priceLevel);
		priceLevel = new ProductPriceLevel();
		priceLevel.setPrice("236.4912");
		priceLevel.setBreakQuantity(50l);
		priceLevels.add(priceLevel);
		price.setPriceLevels(priceLevels);

		prices.add(price);
		return prices;
	}
	
	@Test
	public void test() throws Exception{
		try {
			activityManager.parseFile(null, null, "//ictrade-public-hz-uat.oss-cn-hangzhou.aliyuncs.com/sit/productUpload/activityProductsUpload/201707/27/5bc5b79e5bd5720d3521d3771fbba661.xlsx", "秒杀模板.xlsx");
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
}
