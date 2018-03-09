package com.yikuyi.party.profiles.bll;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

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
import com.yikuyi.party.model.PartyProfileDefault;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class })
@Transactional
@Rollback
public class PartyProfileDefaultManagerTest {
	@Autowired
	private PartyProfileDefaultManager profileManager;

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
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "party_profile_sample.xml")
	public void testInsertProfileDefault() {
		PartyProfileDefault profile = new PartyProfileDefault();
		profile.setPartyId("88888888");
		profile.setCreator("111");
		PartyProfileDefault profiles = profileManager.insertProfileDefault(profile);
		assertEquals("88888888",profiles.getPartyId());
		
		
		PartyProfileDefault profile1 = new PartyProfileDefault();
		profile1.setCreator("111");
		profile1.setPartyId("888888883");
		PartyProfileDefault profiles1 = profileManager.insertProfileDefault(profile1);
		assertEquals("888888883",profiles1.getPartyId());
	}
	
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
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "party_profile_sample.xml")
	public void testGetProfileDefault() {
		profileManager.getProfileDefault("9999");
	}
}
