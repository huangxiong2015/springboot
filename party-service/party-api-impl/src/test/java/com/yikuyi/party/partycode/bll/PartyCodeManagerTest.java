package com.yikuyi.party.partycode.bll;


import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.framework.springboot.model.LoginUser;
import com.framewrok.springboot.web.LoginUserInjectionInterceptor;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.ykyframework.exception.BusinessException;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class,MockitoTestExecutionListener.class })
@Transactional
@Rollback
public class PartyCodeManagerTest {
	@Autowired
	private PartyCodeManager partyCodeManager;
	
	public PartyCodeManagerTest(){
		MockitoAnnotations.initMocks(this);
	}
	@Before
	public void init() {
		LoginUser loginUser = new LoginUser("9999999901", "admin", "9999999901", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));		
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));;
		RequestContextHolder.currentRequestAttributes().setAttribute(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser, RequestAttributes.SCOPE_REQUEST);
	}
	
	/**
	 * 根据账号查询管理员信息
	 * 
	 * @param id
	 * @param role
	 * @return
	 * @since 2017年11月13日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "classpath:com/yikuyi/party/enterprise/bll/enterprise_sampledata.xml")
	public void testSavePatyCode(){
		try {
			 partyCodeManager.savePatyCode("9999999909", "11111");
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "classpath:com/yikuyi/party/enterprise/bll/enterprise_sampledata.xml")
	public void testGetPartyCode(){
		String str= partyCodeManager.getPartyCode("1");
		assertEquals(str,"1");
		
	}
}
