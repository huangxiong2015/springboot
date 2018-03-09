package com.yikuyi.party.partygroup.dao;

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

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class })
@Transactional
@Rollback
public class PartyGroupDaoTest {
	
	@Autowired
	private PartyGroupDao partyGroupDao;

	/**
	 * 测试插入
	 * 
	 * @since 2016年10月18日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = { "party_partygroup_sample.xml" })
	public void testGetPartyGroupList() {
		 
		/*List<Party> list = partyGroupDao.getPartyGroupList("1");
		
		Party party = list.get(0);
		
		assertEquals("1", party.getId());
		assertEquals("1", party.getPartyGroup().getComments());
		assertEquals("1", party.getPartyGroup().getDescription());
		assertEquals("深圳市新蕾电子有限公司", party.getPartyGroup().getGroupName());
		assertEquals("1", party.getPartyGroup().getLogoImageUrl());*/
	}
	
	/**
	 * 根据部门ID获取包括本部门及下面的所有子类部门ID
	 * @param partyId
	 * @return
	 * @since 2017年5月9日
	 * @author tb.yumu@yikuyi.com
	 */
	@Test
	public void findSubDeptIdTest(){
		
		partyGroupDao.findSubDeptId("1");
		
	}
	
	@Test
	public void findCustomerByDeptIdTest(){		
		String[] deptId ={"1","2"};		
		RowBounds rowBounds =new RowBounds(0,10);		
		partyGroupDao.findCustomerByDeptId(deptId,rowBounds);
		
	}
	
	@Test
	public void findRoleNameByPartyIdTest(){			
		partyGroupDao.findRoleNameByPartyId("1");
		
	}
	
	

}