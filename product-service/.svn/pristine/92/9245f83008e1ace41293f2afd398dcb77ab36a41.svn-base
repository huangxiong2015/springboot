package com.yikuyi.product.rule.logistics.manager;

import static org.junit.Assert.assertEquals;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.yikuyi.rule.logistics.vo.LogisticsFee;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
	TransactionDbUnitTestExecutionListener.class })
@Rollback
public class LogisticsFeeManagerTest {
	
	@Autowired
	private CacheManager cacheManager;
	
	@Autowired
	private LogisticsFeeManager logisticsFeeManager;
	
	/**
	 * 实时查询商品的运费
	 * 
	 * @since 2016年12月10日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	public void testGetLogisticsFee(){
		Cache cache = cacheManager.getCache("logisticsRule");
		String key = "logisticsRule.user-defined";
		double logisticsFee = 0;
		LogisticsFee fee = new LogisticsFee();
		fee.setFromGeoId(ProvinceConstant.GEO_ID);
		fee.setToGeoId(ProvinceConstant.GEO_ID);
		fee.setAmount(100);
		fee.setCurrency("CNY");
		//往redis中存放为空的运费规则
		Map<String,Object> map = new HashMap<>();
		map.put("HK", "");
		map.put("CH", "");
		cache.put(key, map);
		//redis中有运费规则，但无具体的规则信息
		logisticsFee = logisticsFeeManager.getLogisticsFee(fee);
		assertEquals(0,logisticsFee,0);
		//往redis中存放具体的运费规则
		map.put("HK", "pinkage:100|HKFaltFee:10");
		map.put("CH", "pinkage:100|IN_SIDE:10|OUT_SIDE:20");
		cache.put(key, map);
		//国内（包邮）
		logisticsFee = logisticsFeeManager.getLogisticsFee(fee);
		assertEquals(0,logisticsFee,0);
		//国内（广东省内）
		fee.setAmount(90);
		logisticsFee = logisticsFeeManager.getLogisticsFee(fee);
		assertEquals(10.0,logisticsFee,0);
		//国内（广东省外）
		fee.setToGeoId("11234");
		logisticsFee = logisticsFeeManager.getLogisticsFee(fee);
		assertEquals(20.0,logisticsFee,0);
		//香港（包邮）
		fee.setAmount(100);
		fee.setCurrency("USD");
		logisticsFee = logisticsFeeManager.getLogisticsFee(fee);
		assertEquals(0,logisticsFee,0);
		//香港（不包邮）
		fee.setAmount(90);
		logisticsFee = logisticsFeeManager.getLogisticsFee(fee);
		assertEquals(10.0,logisticsFee,0);
		//其他币种直接返回0
		fee.setCurrency("EUR");
		logisticsFee = logisticsFeeManager.getLogisticsFee(fee);
		assertEquals(0,logisticsFee,0);
		
		map.put("HK", "pinkage:100|HKFaltFee:10");
		map.put("CH", "pinkage:100|OUT_SIDE:10|IN_SIDE:20");
		cache.put(key, map);
		LogisticsFee fee1 = new LogisticsFee();
		fee1.setFromGeoId(ProvinceConstant.GEO_ID);
		fee1.setToGeoId(ProvinceConstant.GEO_ID);
		fee1.setAmount(90);
		fee1.setCurrency("CNY");
		logisticsFee = logisticsFeeManager.getLogisticsFee(fee1);
		assertEquals(20.0,logisticsFee,0);
		cache.evict(key);
		//没有设置运费规则
		LogisticsFee fee2 = new LogisticsFee();
		fee2.setFromGeoId(ProvinceConstant.GEO_ID);
		fee2.setToGeoId(ProvinceConstant.GEO_ID);
		fee2.setAmount(100);
		fee2.setCurrency("CNY");
		logisticsFee = logisticsFeeManager.getLogisticsFee(fee2);
		assertEquals(0,logisticsFee,0);		
	}

}
