package com.yikuyi.party.notice.bll;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
import com.yikuyi.party.partyExpand.model.PartyExpand;
import com.ykyframework.exception.BusinessException;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class,MockitoTestExecutionListener.class })
@Transactional
@Rollback
public class NoticeManagerTest {
	@Autowired
	private NoticeManager noticeManager;
	
	private MockHttpServletRequest request; 
	
	@Before
	public void init() {
		LoginUser loginUser = new LoginUser("9999999901", "admin", "9999999901", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));		
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));;
		RequestContextHolder.currentRequestAttributes().setAttribute(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser, RequestAttributes.SCOPE_REQUEST);
		request = new MockHttpServletRequest();    
	    request.setCharacterEncoding("UTF-8");    
	}

	
	public NoticeManagerTest(){
		MockitoAnnotations.initMocks(this);
	}
	
	/**
	 * 单元测 -订单通知
	 * 
	 * @since 2017年12月21日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = {"classpath:com/yikuyi/party/enterprise/bll/partyExtend_sample_result.xml"})
	public void testGetPartyExpandList(){
		List<PartyExpand> list =noticeManager.getPartyExpandList("1");
		assertEquals(list.size(), 1);
	}
	
	/**
	 * 单元测 -订单通知
	 * 
	 * @since 2017年12月21日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = {"classpath:com/yikuyi/party/enterprise/bll/partyExtend_sample_result.xml"})
	public void testInsert(){
		PartyExpand partyExpand = new PartyExpand();
		partyExpand.setId("1");
		partyExpand.setPartyId("2");
		partyExpand.setUserName("1");
		partyExpand.setEmail("22@qq.com");
		partyExpand.setCreatedDate(new Date());
		partyExpand.setCreator("9999999901");
	   try {
	    	noticeManager.insert(partyExpand);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 单元测 -订单通知
	 * 
	 * @since 2017年12月21日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = {"classpath:com/yikuyi/party/enterprise/bll/partyExtend_sample_result.xml"})
	public void testUpdate(){
		PartyExpand partyExpand = new PartyExpand();
		partyExpand.setId("1");
		partyExpand.setPartyId("2");
		partyExpand.setUserName("1");
		partyExpand.setEmail("22@qq.com");
		partyExpand.setCreatedDate(new Date());
		partyExpand.setCreator("9999999901");
	   try {
	    	noticeManager.update(partyExpand);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 单元测 -订单通知
	 * 
	 * @since 2017年12月21日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = {"classpath:com/yikuyi/party/enterprise/bll/partyExtend_sample_result.xml"})
	public void testDeleteNotice(){
	   noticeManager.deleteNotice("1");
	}
	
	/**
	 * 单元测 -订单通知
	 * 
	 * @since 2017年12月21日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = {"classpath:com/yikuyi/party/enterprise/bll/partyExtend_sample_result.xml"})
	public void testIsExistMail(){
		PartyExpand partyExpand = noticeManager.isExistMail("1@qq.com");
		assertEquals(partyExpand.getEmail(), "1@qq.com");
	}
}
