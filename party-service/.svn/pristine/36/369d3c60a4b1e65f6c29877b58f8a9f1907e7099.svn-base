package com.yikuyi.party.party.bll;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
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

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.yikuyi.party.model.PartyAttribute;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class })
@Transactional
@Rollback
public class PartyAttributeManagerTest {
	@Autowired
	private PartyAttributeManager attrManager;
	
	/**
	 * 
	 * 
	 * @param
	 * @return void
	 * @since 2016年1月19日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "attribute_sample.xml")
	public void testGetPartyAttributelist() {
		List<PartyAttribute> attrList = attrManager.getPartyAttributelist("88888888", "D_CODE");
		if(null != attrList && attrList.size()>0){
			PartyAttribute attr = attrList.get(0);
			assertEquals("dCode",attr.getValue());
		}
		attrManager.getPartyAttributelist("88888888", "");
	}
}
