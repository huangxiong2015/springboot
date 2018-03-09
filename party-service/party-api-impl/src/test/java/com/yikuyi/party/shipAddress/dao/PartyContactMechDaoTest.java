package com.yikuyi.party.shipAddress.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;

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
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.yikuyi.party.contact.model.ContactMech;
import com.yikuyi.party.model.PartyContactMech;
import com.yikuyi.party.model.PartyContactMech.PurposeType;
@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class })
@Transactional
@Rollback
public class PartyContactMechDaoTest {

	@Autowired
	private PartyContactMechDao partyContactMechDao;
	/**
	 * 测试插入
	 * 
	 * @since 2016年10月18日
	 * @author zr.tongkun@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = { "party_contact_mech_dao_sample_result.xml" })
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "party_contact_mech_dao_insert_result.xml")
	public void testInsert() {
		 
		
		PartyContactMech partyContactMech =new PartyContactMech();
		partyContactMech.setPartyId("888888");
		 SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");
		    
		try {
			partyContactMech.setFromDate(formatter.parse("2016-12-05"));
			partyContactMech.setThruDate(formatter.parse("2016-12-05"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		partyContactMech.setPurposeType(PurposeType.BILLING_LOCATION);
		
		ContactMech contactMech = new ContactMech();
		// 邮箱地址
		contactMech.setId("10002");
		
		partyContactMech.setContactMech(contactMech);

		partyContactMechDao.insert(partyContactMech);
		
	}

}