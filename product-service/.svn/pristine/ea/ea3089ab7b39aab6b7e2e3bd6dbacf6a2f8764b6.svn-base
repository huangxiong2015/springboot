/*
 * Created: 2017年10月17日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.activity.impl;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.springboot.config.ObjectMapperHelper;
import com.framework.springboot.model.LoginUser;
import com.framewrok.springboot.web.LoginUserInjectionInterceptor;
import com.github.pagehelper.PageInfo;
import com.yikuyi.activity.model.Activity;
import com.yikuyi.activity.model.ActivityProductDraft;
import com.yikuyi.activity.vo.ActivityProductDraftVo;
import com.yikuyi.activity.vo.ActivityProductVo;
import com.yikuyi.activity.vo.ActivityVo;
import com.yikuyi.activity.vo.MockActivityDataVo;
import com.yikuyi.product.ProductApplication;
import com.yikuyi.product.activity.bll.ActivityManager;
import com.yikuyi.product.activity.bll.RegisterActivityMockManager;
import com.yikuyi.product.base.ProductApplicationTestBase;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = { ProductApplication.class })
public class ActivityResourceTest extends ProductApplicationTestBase{
	
	private ObjectMapper mapper = ObjectMapperHelper.configeObjectMapper(new ObjectMapper());
	
	private MockMvc mockMvc;
	
	
	LoginUser loginUser = new LoginUser("9999999901", "admin", "9999999901", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
	
	@InjectMocks
	private ActivityResource activityResource;
	
	@Mock
	private ActivityManager mockActivityManager;
	
	@Mock
	private RegisterActivityMockManager mockRegisterActivityMockManager;

	
	public ActivityResourceTest() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Before
	public void setUpBefore() throws Exception {
		
		mockMvc = MockMvcBuilders.standaloneSetup(activityResource).build();
	}
	
	
	/**
	 * 将草稿活动信息转化成正式活动信息
	 * @throws Exception
	 * @since 2017年11月24日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void draftToFormalTest() throws Exception{
		when(mockActivityManager.draftToFormal(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn("success");
        mockMvc.perform(put("/v1/activities/888211460172283/draft").requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(status().isOk());

	}
	
	
	/**
	 * 判断时区是否有效，并更新无活动商品时区的状态为无效
	 * @throws Exception
	 * @since 2017年11月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void updatePeriodsStatusTest() throws Exception{
		Mockito.doNothing().when(mockActivityManager).updatePeriodsStatus(Mockito.anyString());
        mockMvc.perform(put("/v1/activities/888211460172283/periods").requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(status().isOk());

	}
	
	
	/**
	 * 查询活动管理列表
	 * @throws Exception
	 * @since 2017年11月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void listTest() throws Exception{	
		PageInfo<ActivityVo> pageInfo = new PageInfo<>();
		List<ActivityVo> list = new ArrayList<>();
		ActivityVo activityVo = new ActivityVo();
		activityVo.setActivityId("12345689");
		activityVo.setName("hahha");
		list.add(activityVo);
		pageInfo.setList(list);
		
		when(mockActivityManager.findActivityByEntity(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(pageInfo);
		
		JavaType javaType = mapper.getTypeFactory().constructParametricType(PageInfo.class, ActivityVo.class);
		
		pageInfo = mapper.readValue(mockMvc.perform(get("/v1/activities").requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), javaType);
		Assert.assertEquals(pageInfo.getList().get(0).getActivityId(),"12345689");
	}
	
	
	/**
	 * 查询正式活动详情
	 * @throws Exception
	 * @since 2017年11月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void getSearchActivityStandardTest() throws Exception{
		ActivityVo activityVo = new ActivityVo();
		activityVo.setActivityId("456821398");
		
		when(mockActivityManager.findActivityStandardById(Mockito.anyString())).thenReturn(activityVo);
		
		ActivityVo mockActivity = mapper.readValue(mockMvc.perform(get("/v1/activities/456821398/standard").requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), ActivityVo.class);
		Assert.assertEquals(mockActivity.getActivityId(),"456821398");
	}
	
	
	/**
	 * 查询草稿活动详情
	 * @throws Exception
	 * @since 2017年11月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void getSearchActivityDraftTest() throws Exception{
		ActivityVo activityVo = new ActivityVo();
		activityVo.setActivityId("456821398");
		
		when(mockActivityManager.findActivityDraftById(Mockito.anyString())).thenReturn(activityVo);
		
		ActivityVo mockActivity = mapper.readValue(mockMvc.perform(get("/v1/activities/456821398/draft").requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), ActivityVo.class);
		Assert.assertEquals(mockActivity.getActivityId(),"456821398");
	}
	
	
	/**
	 * 修改活动详情
	 * @throws Exception
	 * @since 2017年11月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void updateActivityDraftTest() throws Exception{
		ActivityVo activityVo = new ActivityVo();
		activityVo.setActivityId("456821398");
		
		when(mockActivityManager.updatActivityDraft(Mockito.any(ActivityVo.class))).thenReturn(activityVo);	
	    mockMvc.perform(post("/v1/activities/456821398/draft").requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(activityVo))).andExpect(status().isOk());

	}
	
	

	/**
	 * 保存草稿活动详情
	 * @throws Exception
	 * @since 2017年11月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void saveActivityDraftTest() throws Exception{
		ActivityVo activityVo = new ActivityVo();
		activityVo.setName("wwwhhhhhh");
		
		when(mockActivityManager.saveActivityDraft(Mockito.any(ActivityVo.class))).thenReturn(activityVo);	
	    mockMvc.perform(post("/v1/activities/draft").requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(activityVo))).andExpect(status().isOk());

	}
	
	/**
	 *  保存草稿活动详情
	 * @throws Exception
	 * @since 2017年11月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void saveActivityTest() throws Exception{
		ActivityVo activityVo = new ActivityVo();
		activityVo.setName("wwwhhhhhh");
		
		when(mockActivityManager.saveActivity(Mockito.any(ActivityVo.class))).thenReturn(activityVo);	
	    mockMvc.perform(post("/v1/activities").requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(activityVo))).andExpect(status().isOk());

	}
	
	
	/**
	 * 修改活动详情
	 * @throws Exception
	 * @since 2017年11月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void updateActivityTest() throws Exception{
		Activity activity = new Activity();
		activity.setName("wwwhhhhhh");
		ActivityVo activityVo = new ActivityVo();
		
		when(mockActivityManager.saveActivity(Mockito.any(ActivityVo.class))).thenReturn(activity);	
	    mockMvc.perform(put("/v1/activities/785964268").requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(activityVo))).andExpect(status().isOk());

	}
	
	/**
	 * 查询活动商品已上传的正式表数据
	 * @throws Exception
	 * @since 2017年11月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void findActivityProductTest() throws Exception{
		PageInfo<ActivityProductVo> pageInfo = new PageInfo<>();
		List<ActivityProductVo> list = new ArrayList<>();
		ActivityProductVo productVo = new ActivityProductVo();
		productVo.setActivityId("12458698456");
		productVo.setSourceName("hahahha");
		list.add(productVo);
		pageInfo.setList(list);
		
		when(mockActivityManager.findActivityProductList(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(pageInfo);
		JavaType javaType = mapper.getTypeFactory().constructParametricType(PageInfo.class, ActivityProductVo.class);
		
		pageInfo = mapper.readValue(mockMvc.perform(get("/v1/activities/145896548/products/standard").requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), javaType);
		Assert.assertEquals(pageInfo.getList().get(0).getActivityId(),"12458698456");
	}
	
	
	/**
	 * 查询活动商品已上传的草稿数据
	 * @throws Exception
	 * @since 2017年11月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void testFindActivityProductDraft() throws Exception{
		PageInfo<ActivityProductDraftVo> pageInfo = new PageInfo<>();
		List<ActivityProductDraftVo> list = new ArrayList<>();
		ActivityProductDraftVo draftVo = new ActivityProductDraftVo();
		draftVo.setActivityId("12458698456");
		draftVo.setSourceName("hahahha");
		list.add(draftVo);
		pageInfo.setList(list);
		
		when(mockActivityManager.findActivityProductDraftByCondition(Mockito.any(ActivityProductDraft.class), Mockito.any(RowBounds.class))).thenReturn(pageInfo);
		JavaType javaType = mapper.getTypeFactory().constructParametricType(PageInfo.class, ActivityProductDraftVo.class);
		
		pageInfo = mapper.readValue(mockMvc.perform(get("/v1/activities/145896548/products/draft").requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), javaType);
		Assert.assertEquals(pageInfo.getList().get(0).getActivityId(),"12458698456");
	}
	
	
	
	/**
	 * 删除活动管理
	 * @throws Exception
	 * @since 2017年11月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void deleteActivityTest() throws Exception{
		Mockito.doNothing().when(mockActivityManager).deleteActivity(Mockito.anyString());
		this.mockMvc.perform(delete("/v1/activities/111/delete/activity")).andExpect(status().isOk());   
	}
	
	
	
	/**
	 * 删除活动商品草稿信息
	 * @throws Exception
	 * @since 2017年11月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void deleteActivityProductDraftTest() throws Exception{
		String activityProductId = "88821225660";
		String activityId = "8882114601";
		String periodsId = "888211467458";
		Mockito.doNothing().when(mockActivityManager).deleteActivityProductDraft(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		this.mockMvc.perform(delete("/v1/activities/1118594682/delete?activityProductId="+activityProductId+"&activityId="+activityId+"&periodsId="+periodsId)).andExpect(status().isOk());   
	}
	
	/**
	 *  删除活动商品草稿信息
	 * @throws Exception
	 * @since 2017年11月24日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	/*@Test
	public void deleteActivityProductDraftListTest() throws Exception{
		String activityId = "1118594682";
		String periodsId = "4586923";
		List<String> activityProductIds = new ArrayList<>();
		activityProductIds.add("12345869");
		String activityType = "ENABLE";
		Mockito.doNothing().when(mockActivityManager).deleteActivityProductDraft(Mockito.anyList(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		this.mockMvc.perform(delete("/v1/activities/1118594682/periods/4586923/products?activityId="+activityId+"&periodsId="+periodsId+"&activityType="+activityType)).andExpect(status().isOk());   
	}*/
	
	
	
	/**
	 * 
	 * @throws Exception
	 * @since 2017年11月24日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void getActivityStandardByIdTest() throws Exception{
		ActivityVo activityVo = new ActivityVo();
		activityVo.setActivityId("12456839");
		when(mockActivityManager.getActivityStandardById(Mockito.anyString())).thenReturn(activityVo);
		
		ActivityVo mockActivity = mapper.readValue(mockMvc.perform(get("/v1/activities/todayactivity/12456839").requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), ActivityVo.class);
		Assert.assertEquals(mockActivity.getActivityId(),"12456839");
	}
	
	
	/**
	 * 查询代金券
	 * @throws Exception
	 * @since 2017年11月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void testlistGift() throws Exception{
		PageInfo<MockActivityDataVo> pageInfo = new PageInfo<>();
		List<MockActivityDataVo> list = new ArrayList<>();
		MockActivityDataVo mockActivityDataVo  = new MockActivityDataVo();
		mockActivityDataVo.setCreator("9999999901");
		list.add(mockActivityDataVo);
		pageInfo.setList(list);
		
		when(mockRegisterActivityMockManager.listGift(Mockito.anyString(), Mockito.any(RowBounds.class))).thenReturn(pageInfo);
		JavaType javaType = mapper.getTypeFactory().constructParametricType(PageInfo.class, MockActivityDataVo.class);
		
		pageInfo = mapper.readValue(mockMvc.perform(get("/v1/activities/listGift").requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), javaType);
		Assert.assertEquals(pageInfo.getList().get(0).getCreator(),"9999999901");
	}
	
	/**
	 * 保存代金券
	 * @throws Exception
	 * @since 2017年11月24日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void saveMockAndRealDataTest() throws Exception{
		Mockito.doNothing().when(mockRegisterActivityMockManager).generateMockData();
		mockMvc.perform(get("/v1/activities/generateMockData").requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
	}
}
