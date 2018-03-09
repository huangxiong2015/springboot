package com.yikuyi.product.rule.delivery.manager;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
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
import com.yikuyi.rule.delivery.vo.DeliveryInfo;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
	TransactionDbUnitTestExecutionListener.class })
@Transactional
@Rollback
public class DeliveryManagerTest {
	
	@Autowired
	private DeliveryManager deliveryManager;
	
	/**
	 * 测试交期模板列表信息
	 * 
	 * @since 2016年12月9日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"productPriceRule_sampleData.xml"})
	public void testGetDeliveryList(){
		RowBounds rowBouds = new RowBounds();
		PageInfo<DeliveryInfo> infoListPage = deliveryManager.getDeliveryList(null,null,null,null,null,rowBouds,1,20);
		List<DeliveryInfo> infoList = infoListPage.getList();
		assertEquals(2,infoList.size());
	}
	
	/**
	 * 测试新增交期模板
	 * 
	 * @since 2016年12月9日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"productPriceRule_sampleData.xml"})
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "productPriceRule_add_result.xml")
	public void testAddDelivery(){
		String userId = "test";
		DeliveryInfo info = new DeliveryInfo();
		info.setDeliveryRuleName("jiaoqimoban111");
		info.setVerdonName("futrue");
		info.setWarehouse("none");
		info.setIsShowLeadTime("Y");
		info.setProductType(0);
		info.setLeadTimeMinCH(2);
		info.setLeadTimeMaxCH(5);
		info.setLeadTimeMinHK(5);
		info.setLeadTimeMaxHK(10);
		info.setDescription("jiaoqimoban444");
		//模板名称相同
		DeliveryInfo resultInfo = deliveryManager.addDelivery(info, userId,"1004");
		assertEquals(true, resultInfo==null);
		//商品类型为现货
		info.setDeliveryRuleName("jiaoqimoban444");
		deliveryManager.addDelivery(info, userId,"1004");
		//商品类型为排单
		DeliveryInfo info1 = new DeliveryInfo();
		info1.setDeliveryRuleName("jiaoqimoban555");
		info1.setVerdonName("futrue");
		info1.setWarehouse("meiguo");
		info1.setProductType(1);
		info1.setIsShowLeadTime("Y");
		//info1.setLeadTimeCH(2);
		//info1.setLeadTimeHK(5);
		info1.setFactoryLeadTimeMinCH(2);
		info1.setFactoryLeadTimeMaxCH(3);
		info1.setFactoryLeadTimeMinHK(5);
		info1.setFactoryLeadTimeMaxHK(6);
		info1.setDescription("jiaoqimoban555");
		deliveryManager.addDelivery(info1, userId,"1005");
	}
	
	/**
	 * 测试根据交期模块Id查询详情
	 * 
	 * @since 2016年12月9日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = {"productPriceRule_sampleData.xml"})
	public void testGetDeliveryDetail(){
		DeliveryInfo info = deliveryManager.getDeliveryDetail("1001");
		assertEquals(true, info!=null);
		assertEquals("jiaoqimoban111", info.getDeliveryRuleName());
	}
	
	/**
	 * 测试修改交期模板
	 * 
	 * @since 2016年12月9日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"productPriceRule_sampleData.xml"})
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "productPriceRule_update_result.xml")
	public void testUpdateDelivery(){
		String userId = "test";
		DeliveryInfo info = new DeliveryInfo();
		info.setDeliveryRuleName("jiaoqimoban111");
		info.setVerdonName("futrue");
		info.setWarehouse("none");
		info.setProductType(0);
		info.setIsShowLeadTime("Y");
		info.setLeadTimeMinCH(2);
		info.setLeadTimeMaxCH(5);
		info.setLeadTimeMinHK(5);
		info.setLeadTimeMaxHK(10);
		info.setDescription("jiaoqimoban444");
		//模板名称相同
		DeliveryInfo resultInfo = deliveryManager.updateDelivery("1002", info, userId, "1004");
		assertEquals(true, resultInfo==null);
		//商品类型为现货
		info.setDeliveryRuleName("jiaoqimoban444");
		deliveryManager.updateDelivery("1002", info, userId, "1004");
		//商品类型为排单
		DeliveryInfo info1 = new DeliveryInfo();
		info1.setDeliveryRuleName("jiaoqimoban555");
		info1.setVerdonName("futrue");
		info1.setWarehouse("meiguo");
		info1.setProductType(1);
		info1.setIsShowLeadTime("Y");
		//info1.setLeadTimeCH(2);
		//info1.setLeadTimeHK(5);
		info1.setFactoryLeadTimeMinCH(2);
		info1.setFactoryLeadTimeMaxCH(3);
		info1.setFactoryLeadTimeMinHK(5);
		info1.setFactoryLeadTimeMaxHK(6);
		
		info1.setDescription("jiaoqimoban555");
		deliveryManager.updateDelivery("1002", info1, userId, "1005");
	}
	
	/**
	 * 测试启用、停用交期模板
	 * 
	 * @since 2016年12月9日
	 * @author zr.wenjiao@yikuyi.com
	 */
	/*@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"productPriceRule_updateStatus_sampleData.xml"})
	public void testUpdateDeliveryStatus(){
		String userId = "test";
		DeliveryInfo info = new DeliveryInfo();
		
		//没有传入状态时
		DeliveryInfo info1 = new DeliveryInfo();
		info1.setVerdonName("digikey");
		info1.setProductType(0);
		info1.setWarehouse("none");
		info1.setDeliveryRuleName("jiaoqimoban222");
		info1.setId("1002");
		info = deliveryManager.updateDeliveryStatus("1002", null, userId,info1);
		assertEquals(true, info!=null);
		assertEquals("jiaoqimoban222", info.getDeliveryRuleName());
		//启用
		//存在相同规则的模板时则不做任何操作，直接返回
		DeliveryInfo info2 = new DeliveryInfo();
		info2.setVerdonName("digikey");
		info2.setProductType(0);
		info2.setWarehouse("none");
		info2.setDeliveryRuleName("jiaoqimoban111");
		info2.setId("1006");
		info = deliveryManager.updateDeliveryStatus("1006", ProductPriceRule.RuleStatus.ENABLED, userId,info2);
		assertEquals(true, info!=null);
		assertEquals("jiaoqimoban111", info.getDeliveryRuleName());
		//没有相同规则的模板则启用（商品类型为现货）
		DeliveryInfo info3 = new DeliveryInfo();
		info3.setVerdonName("futrue");
		info3.setProductType(0);
		info3.setWarehouse("meiguo");
		info3.setDeliveryRuleName("jiaoqimoban444");
		info3.setId("1004");
		info = deliveryManager.updateDeliveryStatus("1004", ProductPriceRule.RuleStatus.ENABLED, userId,info3);
		assertEquals(true, info==null);
		//没有相同规则的模板则启用（商品类型为排单）
		DeliveryInfo info4 = new DeliveryInfo();
		info4.setVerdonName("futrue");
		info4.setProductType(1);
		info4.setWarehouse("meiguo");
		info4.setDeliveryRuleName("jiaoqimoban333");
		info4.setId("1003");
		info = deliveryManager.updateDeliveryStatus("1003", ProductPriceRule.RuleStatus.ENABLED, userId,info4);
		assertEquals(true, info==null);
		//停用(同一供应商还有其他模板正在启用中)
		DeliveryInfo info5 = new DeliveryInfo();
		info5.setVerdonName("digikey");
		info5.setProductType(0);
		info5.setWarehouse("none");
		info5.setDeliveryRuleName("jiaoqimoban111");
		info5.setId("1001");
		info = deliveryManager.updateDeliveryStatus("1001", ProductPriceRule.RuleStatus.DISABLED, userId,info5);
		assertEquals(true, info!=null);
		assertEquals("jiaoqimoban111", info.getDeliveryRuleName());
		//停用(同一供应商所有模板都停用)
		DeliveryInfo info6 = new DeliveryInfo();
		info6.setVerdonName("digikey");
		info6.setProductType(1);
		info6.setWarehouse("none");
		info6.setDeliveryRuleName("jiaoqimoban555");
		info6.setId("1005");
		info = deliveryManager.updateDeliveryStatus("1005", ProductPriceRule.RuleStatus.DISABLED, userId,info6);
		assertEquals(true, info!=null);
	}*/
	
	/**
	 * 测试删除交期模板
	 * 
	 * @since 2016年12月9日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"productPriceRule_sampleData.xml"})
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "productPriceRule_deleteById_result.xml")
	public void testDeleteDelivery(){
		deliveryManager.deleteDelivery("1002", "test","testRuleName");
	}
}
