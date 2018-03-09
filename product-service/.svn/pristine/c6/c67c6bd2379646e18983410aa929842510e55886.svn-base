/*
 * Created: 2017年2月23日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.goods.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.yikuyi.category.model.ProductCategory;
import com.yikuyi.product.model.ProductStand;
import com.yikuyi.product.model.ProductStandAudit;
import com.yikuyi.product.model.ProductStandAudit.AuditStatus;
import com.yikuyi.product.vo.ProductStandAuditRequest;
import com.yikuyi.product.vo.ProductStandRequest;
import com.yikuyi.product.vo.RawData;
import com.ykyframework.context.ContextAttributes;
import com.ykyframework.context.RuntimeContextHolder;
import com.ykyframework.mqservice.MQConstants;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class })
@Transactional(propagation = Propagation.REQUIRES_NEW)
@Rollback
public class ProductStandAuditManagerTest {

	@Autowired
	private ProductStandAuditManager productStandAuditManager;
	
	@Autowired
	private MongoRepository<ProductStand, String> productStandRepository;
	
	@Before
	public void setUpBefore() throws Exception {
		RuntimeContextHolder.setContextAttributes(new ContextAttributes());
		RuntimeContextHolder.currentContextAttributes().setAttribute(MQConstants.MSG_HEADER_USER_NAME, "admin");
		RuntimeContextHolder.currentContextAttributes().setAttribute(MQConstants.MSG_HEADER_PASS_WORD, "9999999901");
	}

	/**
	 * 根据spuIds查询物料
	 * @throws Exception
	 * @since 2017年12月7日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void testGetProductStandAuditBySpuId() throws Exception{
		List<String> spuIds = new ArrayList<>();
		spuIds.add("45216389");
		spuIds.add("45896111");
		productStandAuditManager.getProductStandAuditBySpuId(spuIds);
	}
	
	/**
	 * 判断spuId是否存在审核变里面，存在并且是"待审核"状态就要抛出异常,存储错误日志到异常日志表
	 * @throws Exception
	 * @since 2017年12月7日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void testExistProductStandAudit() throws Exception{
			List<ProductStandAudit> productStandList = new ArrayList<>();
		
			List<ProductStandAudit> standAuditsList = new ArrayList<>();
			ProductStandAudit stand = new ProductStandAudit();
			stand.setSpuId("123456789");
			stand.setManufacturer("2");
			stand.setManufacturerPartNumber("1");
			stand.setAuditStatus(AuditStatus.PASS.getValue());
			standAuditsList.add(stand);
		
			List<ProductStandAudit> productStandAudits = new ArrayList<>();
			ProductStandAudit standAudit = new ProductStandAudit();
			standAudit.setSpuId("123456789");
			standAudit.setManufacturer("2");
			standAudit.setManufacturerPartNumber("1");
			standAudit.setAuditStatus(AuditStatus.WAIT_AUDIT.getValue());
			productStandAudits.add(standAudit);
			
			List<ProductCategory> listCat = new ArrayList<>();
			ProductCategory cat = new ProductCategory();
			cat.setId(1);
			cat.setName("大类");
			cat.setLevel(1);
			cat.setStatus(1);
			listCat.add(cat);
			ProductCategory cat1 = new ProductCategory();
			cat1.setId(2);
			cat1.setName("小类");
			cat1.setLevel(2);
			cat1.setStatus(1);
			listCat.add(cat1);
			
			RawData psInfo = new RawData();
			psInfo.setManufacturer("PANJIT");
			psInfo.setManufacturerPartNumber("Manufact"+new Random().nextInt(1000));
			psInfo.setDescription("DescriptionDescription");
			psInfo.setRohs("true");
			psInfo.setFrom("BASIS_FILE_UPLOAD");
			psInfo.setSpuId("123456789");
			psInfo.setVendorCategories(listCat);
			
			String from = "Basic_Upload";
			String errorMsg= "hahahha";
			productStandAuditManager.existProductStandAudit(productStandList, psInfo, from, errorMsg);
			
			productStandAuditManager.existProductStandAudit(standAuditsList, psInfo, from, errorMsg);
		try {	
			productStandAuditManager.existProductStandAudit(productStandAudits, psInfo, from, errorMsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 批量保存标准物料
	 * @throws Exception
	 * @since 2017年12月7日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void testInsertProductStandAudits() throws Exception{
		List<ProductStandAudit> standAudits = new ArrayList<>();
		
		List<ProductStandAudit> list = new ArrayList<>();
		ProductStandAudit standAudit = new ProductStandAudit();
		standAudit.setSpuId("123456789");
		standAudit.setManufacturer("2");
		standAudit.setManufacturerPartNumber("1");
		standAudit.setAuditStatus(AuditStatus.WAIT_AUDIT.getValue());
		list.add(standAudit);
		
		productStandAuditManager.insertProductStandAudits(standAudits);
		
		productStandAuditManager.insertProductStandAudits(list);
	}
	
	/**
	 * 更新物料的数据
	 * @throws Exception
	 * @since 2017年12月7日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void testUpdateProductStandAudit() throws Exception{
		String id = "897379241920823296";
		String userName = "admin";
		String userId = "9999999901";
		ProductStandAudit productStandAudit = new ProductStandAudit();
		productStandAudit.setSpuId("1234567457");
		productStandAudit.setManufacturer("Bourns");
		productStandAudit.setManufacturerPartNumber("3352P-1-201LFYY");
		productStandAudit.setAuditStatus(AuditStatus.WAIT_AUDIT.getValue());
		
		String idNew = "14836897586942";
		ProductStandAudit productStand = new ProductStandAudit();
		productStand.setSpuId("1234567457");
		productStand.setManufacturer("2");
		productStand.setManufacturerPartNumber("1");
		productStand.setAuditStatus(AuditStatus.WAIT_AUDIT.getValue());
		
		try {
			productStandAuditManager.updateProductStandAudit(id, userName, userId, productStandAudit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		productStandAuditManager.updateProductStandAudit(idNew, userName, userId, productStandAudit);
		
	}
	
	
	/**
	 * 根据ID查询物料信息
	 * @throws Exception
	 * @since 2017年12月7日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void testGetProductStandById() throws Exception{
		String id = "897379241920823296";
		String idNew = "14836897586942";
		
		productStandAuditManager.getProductStandById(id);
		try {
			productStandAuditManager.getProductStandById(idNew);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 分页查询	
	 * @throws Exception
	 * @since 2017年12月7日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void testFindProductStandAuditByPage() throws Exception{
		ProductStandRequest standRequest = new ProductStandRequest();
		standRequest.setAuditStatus(AuditStatus.PASS.getValue());
		
		PageInfo<ProductStandAudit> pageInfo = productStandAuditManager.findProductStandAuditByPage(standRequest, 1, 5);
	}
	
	
	/**
	 * 根据Ids批量审核
	 * @throws Exception
	 * @since 2017年12月7日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void auditTest() throws Exception{
		ProductStandAuditRequest auditRequest = new ProductStandAuditRequest();
		List<String> ids = new ArrayList<>();
		ids.add("897379241920823296");
		auditRequest.setIds(ids);
		auditRequest.setAuditStats(AuditStatus.PASS.getValue());
		
		productStandAuditManager.audit(auditRequest, "9999999901", "admin");
	}
}
