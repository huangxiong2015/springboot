package com.yikuyi.party.facility.bll;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
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
import com.yikuyi.party.facility.model.Facility;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class })
@Transactional
@Rollback
public class FacilityManagerTest {
	
	@Autowired
	private FacilityManager facilityManager;
	
	/**
	 * 
	 * 
	 * @param
	 * @return void
	 * @since 2017年11月3日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "party_facility_sample.xml")
	public void testGetFacilityList() {
		List<Facility> list = facilityManager.getFacilityList("1");
		if(CollectionUtils.isNotEmpty(list)){
			Facility facility = list.get(0);
			assertEquals("深圳",facility.getFacilityName());
		}
	}
	/**
	 * 
	 * 
	 * @param
	 * @return void
	 * @since 2017年11月3日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "party_facility_sample.xml")
	public void testFacilityFlagYList() {
		List<Facility> list = facilityManager.getFacilityFlagYList("1");
	}
	
	/**
	 * 
	 * 
	 * @param
	 * @return void
	 * @since 2017年11月3日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "party_facility_sample.xml")
	public void testGetFacility() {
		List<String> ids = new ArrayList<String>();
		ids.add("1");
		List<Facility> list = facilityManager.getFacility(ids);
		if(CollectionUtils.isNotEmpty(list)){
			Facility facility = list.get(0);
			assertEquals("深圳",facility.getFacilityName());
		}
	}
	
	
	/**
	 * 
	 * 
	 * @param
	 * @return void
	 * @since 2017年11月3日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "party_facility_sample.xml")
	public void testAddFacility() {
		Facility  facility = new Facility();
		facility.setFacilityName("测试仓库oop");
		facility.setCreatedDate(new Date());
		facility.setFacilityNameAlia("测试仓库oop");
		facility.setId("5");
		facility.setIsShowaFacility("Y");
		facility.setOwnerPartyId("999678");
		facility.setDisplayFlag("Y");
		facility.setFromDate(new Date());
	    facilityManager.addFacility(facility);
	}
	
	/**
	 * 
	 * 
	 * @param
	 * @return void
	 * @since 2017年11月3日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "party_facility_sample.xml")
	public void testDelete() {
		Facility  facility = new Facility();
		facility.setId("5");
	    facilityManager.delete(facility);
	}
	
	/**
	 * 
	 * 
	 * @param
	 * @return void
	 * @since 2017年11月3日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "party_facility_sample.xml")
	public void testUpdate() {
		Facility  facility = new Facility();
		facility.setId("5");
		facility.setOwnerPartyId("1");
	    facilityManager.update(facility);
	}
}
