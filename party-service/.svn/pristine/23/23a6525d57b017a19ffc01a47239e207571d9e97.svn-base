package com.yikuyi.party.customerssync.bll;


import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class })
@Transactional
@Rollback
public class CustomersSyncManagerTest {
	@Autowired
	private CustomersSyncManager customersSyncManager;
	
	private MockHttpServletRequest request; 
	
	public CustomersSyncManagerTest(){
		MockitoAnnotations.initMocks(this);
	}
	
	@Before
	public void init() {
		LoginUser loginUser = new LoginUser("9999999901", "admin", "9999999901", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));		
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));;
		RequestContextHolder.currentRequestAttributes().setAttribute(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser, RequestAttributes.SCOPE_REQUEST);
		request = new MockHttpServletRequest();    
	    request.setCharacterEncoding("UTF-8");    
	}
	/**
	 * 全量查询企业信息全量同步数据方法
	 * @param rowBounds
	 * @return List<CustomersSyncVo>
	 * @since 2017年12月13日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value ="classpath:com/yikuyi/party/customersync/bll/customerSync_sample_result.xml")
	public void testGetAllEntList(){
		customersSyncManager.getAllEntList(1, 1);
	}
	
	/**
	 * 查询企业数据增量同步数据方法
	 * 
	 * @param selectStart
	 * @param selectEnd
	 * @return List<CustomersSyncVo>
	 * @since 2017年12月13日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value ="classpath:com/yikuyi/party/customersync/bll/customerSync_sample_result.xml")
	public void testGetIncrementEntList(){
		customersSyncManager.getIncrementEntList("2017-12-21","2017-12-21",1, 1);
	}
	
	
	/**
	 * 查询企业数据增量同步数据方法
	 * 
	 * @param selectStart
	 * @param selectEnd
	 * @return List<CustomersSyncVo>
	 * @since 2017年12月13日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value ="classpath:com/yikuyi/party/customersync/bll/customerSync_sample_result.xml")
	public void testGetAllPersonalList(){
		customersSyncManager.getAllPersonalList(1, 1);
	}
	
	
	/**
	 * 查询企业数据增量同步数据方法
	 * 
	 * @param selectStart
	 * @param selectEnd
	 * @return List<CustomersSyncVo>
	 * @since 2017年12月13日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value ="classpath:com/yikuyi/party/customersync/bll/customerSync_sample_result.xml")
	public void testGetIncrementPersonalList(){
		customersSyncManager.getIncrementPersonalList("2017-12-21","2017-12-21",1, 1);
	}
}
