package com.yikuyi.product.rule.logistics.manager;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.yikuyi.rule.logistics.vo.LogisticsCondFee;
import com.yikuyi.rule.logistics.vo.LogisticsCondInfo;
import com.yikuyi.rule.logistics.vo.LogisticsInfo;
import com.yikuyi.rule.price.ProductPriceRule;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
	TransactionDbUnitTestExecutionListener.class })
@Transactional
@Rollback
public class LogisticsManagerTest {
	
	@Autowired
	private LogisticsManager logisticsManager;
	
	/**
	 * 测试新增运费模板
	 * 
	 * @since 2016年12月10日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"productPriceRule_sampleData.xml"})
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "productPriceRule_add_result.xml")
	public void testAddLogistics(){
		String userId = "test";
		LogisticsInfo info = new LogisticsInfo();
		info.setLogisticsRuleName("yunfeimoban111");
		info.setPinkage(false);
		info.setDescription("yunfeimoban333");
		List<LogisticsCondInfo> condInfoList = new ArrayList<>();
		//国内运费信息
		LogisticsCondInfo condInfo = new LogisticsCondInfo();
		condInfo.setDeliveryPlace("CH");
		condInfo.setPinkageAmount(new BigDecimal(500));
		List<LogisticsCondFee> feeList = new ArrayList<>();
		LogisticsCondFee feeInfo = new LogisticsCondFee();
		feeInfo.setShipSite("IN_SIDE");
		feeInfo.setFaltAmount(new BigDecimal(50));
		feeList.add(feeInfo);
		LogisticsCondFee feeInfo2 = new LogisticsCondFee();
		feeInfo2.setShipSite("OUT_SIDE");
		feeInfo2.setFaltAmount(new BigDecimal(60));
		feeList.add(feeInfo2);
		condInfo.setFaltPinkageAmount(feeList);
		condInfoList.add(condInfo);
		//香港运费信息
		LogisticsCondInfo condInfo1 = new LogisticsCondInfo();
		condInfo1.setDeliveryPlace("HK");
		condInfo1.setPinkageAmount(new BigDecimal(500));
		List<LogisticsCondFee> feeList1 = new ArrayList<>();
		LogisticsCondFee feeInfo1 = new LogisticsCondFee();
		feeInfo1.setShipSite("HONGKONG");
		feeInfo1.setFaltAmount(new BigDecimal(50));
		feeList1.add(feeInfo1);
		condInfo1.setFaltPinkageAmount(feeList1);
		condInfoList.add(condInfo1);
		info.setCondInfo(condInfoList);
		//模板名称相同
		LogisticsInfo resultInfo = logisticsManager.addLogistics(info, userId);
		assertEquals(true, resultInfo==null);
		//保存运费模板（自定义运费-国内和香港同时设置）
		info.setLogisticsRuleName("yunfeimoban333");
		logisticsManager.addLogistics(info, userId);
		//保存运费模板（自定义运费-只设置国内或者香港）
		info.setLogisticsRuleName("yunfeimoban555");
		info.setDescription("yunfeimoban555");
		condInfo1.setDeliveryPlace("HK");
		condInfo1.setPinkageAmount(null);
		List<LogisticsCondFee> feeList2 = new ArrayList<>();
		LogisticsCondFee feeInfo3 = new LogisticsCondFee();
		feeInfo3.setShipSite("HONGKONG");
		feeInfo3.setFaltAmount(null);
		feeList2.add(feeInfo3);
		condInfo1.setFaltPinkageAmount(feeList2);
		condInfoList.add(condInfo1);
		info.setCondInfo(condInfoList);
		logisticsManager.addLogistics(info, userId);
		//保存运费模板（包邮）
		LogisticsInfo info1 = new LogisticsInfo();
		info1.setLogisticsRuleName("yunfeimoban444");
		info1.setPinkage(true);
		info1.setDescription("yunfeimoban444");
		logisticsManager.addLogistics(info1, userId);
	}
	
	/**
	 * 测试查询运费模板列表
	 * 
	 * @since 2016年12月10日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"productPriceRule_sampleData.xml"})
	public void testGetLogisticsList(){
		PageInfo<LogisticsInfo> infoListPage = logisticsManager.getLogisticsList();
		List<LogisticsInfo> infoList = infoListPage.getList();
		assertEquals(2,infoList.size());
	}
	
	/**
	 * 测试根据运费模块Id查询详情
	 * 
	 * @since 2016年12月10日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.INSERT, value = {"productPriceRule_sampleData.xml"})
    public void testGetLogisticsDetail(){
		LogisticsInfo info = logisticsManager.getLogisticsDetail("1001");
		assertEquals(true, info!=null);
		assertEquals("yunfeimoban111", info.getLogisticsRuleName());
    }
	
	/**
	 * 测试修改运费模板
	 * 
	 * @since 2016年12月10日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"productPriceRule_sampleData.xml"})
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "productPriceRule_update_result.xml")
	public void testUpdateLogistics(){
		String userId = "test";
		LogisticsInfo info = new LogisticsInfo();
		info.setLogisticsRuleName("yunfeimoban111");
		info.setPinkage(false);
		info.setDescription("yunfeimoban333");
		List<LogisticsCondInfo> condInfoList = new ArrayList<>();
		//国内运费信息
		LogisticsCondInfo condInfo = new LogisticsCondInfo();
		condInfo.setDeliveryPlace("CH");
		condInfo.setPinkageAmount(new BigDecimal(500));
		List<LogisticsCondFee> feeList = new ArrayList<>();
		LogisticsCondFee feeInfo = new LogisticsCondFee();
		feeInfo.setShipSite("IN_SIDE");
		feeInfo.setFaltAmount(new BigDecimal(50));
		feeList.add(feeInfo);
		LogisticsCondFee feeInfo2 = new LogisticsCondFee();
		feeInfo2.setShipSite("OUT_SIDE");
		feeInfo2.setFaltAmount(new BigDecimal(60));
		feeList.add(feeInfo2);
		condInfo.setFaltPinkageAmount(feeList);
		condInfoList.add(condInfo);
		//香港运费信息
		LogisticsCondInfo condInfo1 = new LogisticsCondInfo();
		condInfo1.setDeliveryPlace("HK");
		condInfo1.setPinkageAmount(new BigDecimal(500));
		List<LogisticsCondFee> feeList1 = new ArrayList<>();
		LogisticsCondFee feeInfo1 = new LogisticsCondFee();
		feeInfo1.setShipSite("HONGKONG");
		feeInfo1.setFaltAmount(new BigDecimal(50));
		feeList1.add(feeInfo1);
		condInfo1.setFaltPinkageAmount(feeList1);
		condInfoList.add(condInfo1);
		info.setCondInfo(condInfoList);
		//模板名称相同
		LogisticsInfo resultInfo = logisticsManager.updateLogistics("1002", info, userId);
		assertEquals(true, resultInfo==null);
		//保存运费模板（自定义运费）
		info.setLogisticsRuleName("yunfeimoban333");
		logisticsManager.updateLogistics("1002", info, userId);
		//保存运费模板（包邮）
		LogisticsInfo info1 = new LogisticsInfo();
		info1.setLogisticsRuleName("yunfeimoban444");
		info1.setPinkage(true);
		info1.setDescription("yunfeimoban444");
		logisticsManager.updateLogistics("1002", info1, userId);
	}
	
	/**
	 * 测试启用、停用运费模板
	 * 
	 * @since 2016年12月10日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"productPriceRule_updateStatus_sampleData.xml"})
	public void testUpdateLogisticsStatus(){
		String userId = "test";
		LogisticsInfo info = new LogisticsInfo();
		//没有传入状态时
		LogisticsInfo info1 = new LogisticsInfo();
		info1.setLogisticsRuleName("yunfeimoban222");
		info1.setId("1002");
		info = logisticsManager.updateLogisticsStatus("1002", null, userId, info1);
		assertEquals(true, info!=null);
		assertEquals("yunfeimoban222", info.getLogisticsRuleName());
		//启用
		//存在已经启用的模板时则不做任何操作，直接返回
		info = logisticsManager.updateLogisticsStatus("1002", ProductPriceRule.RuleStatus.ENABLED, userId, info1);
		assertEquals(true, info!=null);
		assertEquals("yunfeimoban111", info.getLogisticsRuleName());
		//停用
		LogisticsInfo info2 = new LogisticsInfo();
		info2.setLogisticsRuleName("yunfeimoban111");
		info2.setId("1001");
		info = logisticsManager.updateLogisticsStatus("1001", ProductPriceRule.RuleStatus.DISABLED, userId, info2);
		assertEquals(true, info==null);
		//启用
		//不存在启用的模板
		info2.setPinkage(false);
		List<LogisticsCondInfo> condList = new ArrayList<>();
		LogisticsCondInfo cond1 = new LogisticsCondInfo();
		cond1.setDeliveryPlace("CH");
		cond1.setPinkageAmount(new BigDecimal(500));
		List<LogisticsCondFee> feeList = new ArrayList<>();
		LogisticsCondFee fee1 = new LogisticsCondFee();
		fee1.setShipSite("IN_SIDE");
		fee1.setFaltAmount(new BigDecimal(500));
		feeList.add(fee1);
		LogisticsCondFee fee2 = new LogisticsCondFee();
		fee2.setShipSite("OUT_SIDE");
		fee2.setFaltAmount(new BigDecimal(500));
		feeList.add(fee2);
		cond1.setFaltPinkageAmount(feeList);
		condList.add(cond1);
		
		LogisticsCondInfo cond2 = new LogisticsCondInfo();
		cond2.setDeliveryPlace("HK");
		cond2.setPinkageAmount(new BigDecimal(500));
		List<LogisticsCondFee> feeList1 = new ArrayList<>();
		LogisticsCondFee fee3 = new LogisticsCondFee();
		fee3.setShipSite("HONGKONG");
		fee3.setFaltAmount(new BigDecimal(500));
		feeList1.add(fee3);
		cond2.setFaltPinkageAmount(feeList1);
		condList.add(cond2);
		info2.setCondInfo(condList);
		info = logisticsManager.updateLogisticsStatus("1001", ProductPriceRule.RuleStatus.ENABLED, userId, info2);
		assertEquals(true, info==null);
	}
	
	/**
	 * 测试删除运费模板
	 * 
	 * @since 2016年12月10日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"productPriceRule_sampleData.xml"})
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "productPriceRule_deleteById_result.xml")
	public void testDeleteLogistics(){
		logisticsManager.deleteLogistics("1002", "test", "yunfeimoban222");
	}
}
