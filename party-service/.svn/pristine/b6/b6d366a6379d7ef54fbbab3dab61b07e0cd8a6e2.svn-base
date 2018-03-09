package com.yikuyi.party.userlogin.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.yikuyi.party.login.model.UserLogin;
import com.yikuyi.party.userLogin.dao.UserLoginDao;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class })
@Transactional
@Rollback
public class UserLoginDaoTest {
	
	@Autowired
	private UserLoginDao userLoginDao;
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = { "classpath:com/yikuyi/party/userlogin/dao/userlogin_sampledata.xml",
			"classpath:com/yikuyi/party/userlogin/dao/party_sampledata.xml",
			"classpath:com/yikuyi/party/userlogin/dao/partyRole_sampledata.xml",
			"classpath:com/yikuyi/party/userlogin/dao/person_sampledata.xml"
			})
	public void testGetUserSummeryInfo(){
		userLoginDao.getUserSummeryInfo("123456@yikuyi.com");
		//UserLogin userLogin = userLoginDao.getUserSummeryInfo("123456@yikuyi.com");
		//assertEquals(userLogin.getId(),"123456@yikuyi.com");
	}
	
	/**
	 * 单元测试 - 查询原密码
	 * 
	 * @since 2017年1月16日
	 * @author tb.yumu@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = { "classpath:com/yikuyi/party/userlogin/dao/userlogin_sampledata.xml" })
	public void testFindUserPassword(){
		List<UserLogin> login = userLoginDao.findUserPassword("9999999902");
		String pass = login.get(0).getBcryptPassword();
		boolean flag = BCrypt.checkpw("123qwe", pass);
		assertEquals(true ,flag );
	}
	
	/**
	 * 单元测试 - 修改密码
	 * 
	 * @since 2017年1月16日
	 * @author tb.yumu@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = { "classpath:com/yikuyi/party/userlogin/dao/userlogin_sampledata.xml" })
	public void testChangePasswordByLoginId(){
		UserLogin login = new UserLogin();
		login.setCurrentPassword("123qwe");
		userLoginDao.changePasswordByLoginId(login.getCurrentPassword(), "MDAIAM", "9999999901");
		List<UserLogin> user = userLoginDao.findUserPassword("9999999901");
		String pass = user.get(0).getBcryptPassword();
		boolean flag = BCrypt.checkpw("123qwe", pass);
		assertEquals(true ,flag );
	}

}
