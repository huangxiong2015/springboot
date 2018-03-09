package com.yikuyi.party.party.dao;

import static org.junit.Assert.assertTrue;
import java.util.Set;
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
public class PartyPermissionDaoTest {

	@Autowired
	private PartyPermissionDao partyPermissionDao;
	/**
	 * 测试插入
	 * 
	 * @since 2016年10月18日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = { "party_permission_sample.xml" })
	public void testGetPartyGroupList() {
		String partyGroupId = "123456789";
		String partyId = "123456789-01";
		Set<String> permissions = partyPermissionDao.findPermissionByPartyId(partyGroupId, partyId);
		assertTrue(permissions.size()>0);
	}

}