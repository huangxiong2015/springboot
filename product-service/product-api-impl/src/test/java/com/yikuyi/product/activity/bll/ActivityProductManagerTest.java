/*
 * Created: 2017年4月1日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.activity.bll;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.yikuyi.activity.model.Activity;
import com.yikuyi.activity.model.ActivityPeriods;
import com.yikuyi.activity.model.ActivityProduct;
import com.yikuyi.activity.model.ActivityProductDraft;
import com.yikuyi.product.activity.bll.ActivityManager;
import com.yikuyi.product.activity.bll.ActivityProductManager;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class })
@Transactional
@Rollback
public class ActivityProductManagerTest {

	@Autowired
	ActivityProductManager activityProductManager;

	int number;

	@Autowired
	private ActivityManager activityManager;
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;

	@Before
	public void setUp() {
		request = new MockHttpServletRequest();
		request.setCharacterEncoding("UTF-8");
		response = new MockHttpServletResponse();
	}

	/**
	 * 将草稿活动信息转化成正式活动信息
	 * 
	 * @param activityId
	 * @since 2017年6月9日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:com/yikuyi/product/activity/bll/activity_sample.xml")
	public void testDraftToFormal() throws Exception {
		String str = activityManager.draftToFormal("1", "9999999901", "admin");
		assertEquals(str, "success");
	}

	@Test
	public void testExportProducts() throws Exception {
		activityManager.exportProducts(null, "1", "1", null, response);
	}

	/**
	 * 根据活动商品id查询活动商品信息
	 * 
	 * @param productId
	 * @return
	 * @since 2017年6月9日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:com/yikuyi/product/activity/bll/activity_sample.xml")
	public void testGetProductById() throws Exception {
		ActivityProductDraft activityProductDraft = activityProductManager.getProductById("1");
		assertEquals(activityProductDraft.getActivityProductId(), "1");
	}

	/**
	 * 编辑商品信息
	 * 
	 * @param activityProduct
	 * @since 2017年6月9日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:com/yikuyi/product/activity/bll/activity_sample.xml")
	public void testEditProductInfo() throws Exception {
		ActivityProductDraft activityProduct = new ActivityProductDraft();
		activityProduct.setActivityProductId("1");
		activityProduct.setActivityId("8");
		activityProduct.setImage1("8");
		activityProduct.setManufacturer("8");
		activityProduct.setManufacturerPartNumber("8");
		activityProduct.setSourceName("8");
		activityProduct.setVendorName("8");
		activityProduct.setPeriodsId("8");
		activityProduct.setPriceBreak1("8");
		activityProduct.setTitle("8");
		activityProduct.setTotalQty("8");
		activityProduct.setSubTitle("8");
		activityProductManager.editProductInfo(activityProduct);
	}

	
	
	/**
	 * 把活动信息加入缓存中
	 * 
	 * @since 2017年6月20日
	 * @author zr.wenjiao@yikuyi.com
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	private void putActivityCache(Cache cache) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		List<Activity> newCache = new ArrayList<>();
		ValueWrapper value = cache.get("activitiesInfo");
		if (value != null) {
			newCache = (List<Activity>) value.get();
		}
		// 活动信息
		Activity activity = new Activity();
		activity.setActivityId("111");
		activity.setName("测试");
		Calendar c = Calendar.getInstance();
		activity.setStartDate(format.parse(format.format(c.getTime())));
		c.add(Calendar.DAY_OF_MONTH, 1);
		activity.setEndDate(format.parse(format.format(c.getTime())));
		List<ActivityPeriods> periodsList = new ArrayList<>();
		ActivityPeriods periods = new ActivityPeriods();
		periods.setPeriodsId("22111");
		periods.setStartTime("06:00");
		periods.setEndTime("23:00");
		periodsList.add(periods);
		activity.setPeriodsList(periodsList);
		boolean found = false;
		for (int i = 0; i < newCache.size(); i++) {
			Activity tempac = newCache.get(i);
			if (activity.getActivityId().equals(tempac.getActivityId())) {
				newCache.set(i, activity);
				found = true;
			}
		}
		if (!found) {
			newCache.add(activity);
		}
		cache.put("activitiesInfo", newCache);
		// 活动商品信息
		ActivityProduct product = new ActivityProduct();
		product.setProductId("123456");
		product.setSubTitle("副标题");
		product.setTitle("主标题");
		product.setTotalQty(1111);
		product.setQty(111);
		product.setImage1("111");
		product.setImage2("111");
		product.setImage3("111");
		product.setImage4("111");
		product.setImage5("111");
		cache.put("111-22111-product-872316733963108352", product);
	}

	/**
	 * 测试保存商品到历史表中
	 * 
	 * @since 2017年6月19日
	 * @author tongkun@yikuyi.com
	 */
	@Test
	public void testSaveActivitiesProductsHistory() {
		activityProductManager.saveActivitiesProductsHistory();
	}
	
	
}
