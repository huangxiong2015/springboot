package com.yikuyi.party.customer.bll;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.yikuyi.party.contact.vo.UserVo;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class })
@Transactional
@Rollback
public class CustomerSummeryManagerTest {

	@Autowired
	private CustomerSummeryManager customerSummeryManager;

	@Value("${api.message.serverUrlPrefix}")
	private String serverUrl;

	/*@Autowired
	private RestTemplate restTemplateMock;

	public MockRestServiceServer mockRestServiceServer() {
		return MockRestServiceServer.createServer(restTemplateMock);
	}
*/
	@Before
	public void init() {
		LoginUser loginUser = new LoginUser("9999999901", "admin", "9999999901",
				Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
		;
		RequestContextHolder.currentRequestAttributes().setAttribute(LoginUserInjectionInterceptor.LOGIN_USER_KEY,
				loginUser, RequestAttributes.SCOPE_REQUEST);
	}


	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"classpath:com/yikuyi/party/customer/bll/party_sampledata.xml"})
	public void testGetUserSummeryInfo_customer() {
		customerSummeryManager.getUserSummeryInfo("123456@yikuyi.com", "9999999909");
		customerSummeryManager.getUserSummeryInfo("123456", "1");
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {
			"classpath:com/yikuyi/party/customer/bll/partygroup_sampledata.xml" })
	public void testGetCompanyNameByPartyId() {
		customerSummeryManager.getCompanyNameByPartyId("9999999901");
	}

	/**
	 * 单元测试 - 修改密码 - 成功
	 * 
	 * @since 2017年1月16日
	 * @author tb.yumu@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {
			"classpath:com/yikuyi/party/customer/bll/userlogin_sampledata.xml" })
	public void testChangePassword() throws Exception{
		UserVo userVo = new UserVo();
		userVo.setPassword("123456");
		userVo.setNewPassword("123.qwe");
		customerSummeryManager.changePassword("9999999901", userVo);
	}

	/**
	 * 单元测 - 修改密码（验证原密码失败）
	 * 
	 * @since 2017年1月16日
	 * @author tb.yumu@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {
			"classpath:com/yikuyi/party/customer/bll/userlogin_sampledata.xml" })
	public void testChangePassword_passFailed() throws Exception{
		try {
			UserVo userVo = new UserVo();
			userVo.setPassword("111111");
			customerSummeryManager.changePassword("9999999901", userVo);
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	/**
	 * 单元测试 - 修改密码（验证原密码） - 原密码为null
	 * 
	 * @since 2017年1月16日
	 * @author tb.yumu@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {
			"classpath:com/yikuyi/party/customer/bll/userlogin_sampledata.xml" })
	public void testChangePassword_passNull() throws Exception{
		try {
			UserVo userVo = new UserVo();
			userVo.setPassword("");
			customerSummeryManager.changePassword("9999999901", userVo);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 单元测试 - 找回密码 - 成功
	 * 
	 * @since 2017年1月16日
	 * @author tb.yumu@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value ="classpath:com/yikuyi/party/customer/bll/userlogin_sampledata.xml")
	public void testFindPassword() throws Exception{
		UserVo userVo = new UserVo();
		userVo.setNewPassword("123456");
		customerSummeryManager.findPassword("MTIzNDU2QHlpa3V5aS5jb20=", userVo);// 1001
	}

	/**
	 * 单元测试 -找回密码 id为null
	 * 
	 * @since 2017年1月16日
	 * @author tb.yumu@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {
			"classpath:com/yikuyi/party/customer/bll/userlogin_sampledata.xml" })
	public void testFindPassword_idNull() throws Exception{
		try {
			UserVo userVo = new UserVo();
			userVo.setNewPassword("123456");
			customerSummeryManager.findPassword("", userVo);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 单元测试 - 校验原密码是否正确 - 成功
	 * 
	 * @since 2017年1月16日
	 * @author tb.yumu@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {
			"classpath:com/yikuyi/party/customer/bll/userlogin_sampledata.xml" })
	public void testCheckedOldPassword_success() throws Exception{
		customerSummeryManager.checkedOldPassword("9999999901", "123456");
	}

	/**
	 * 单元测试 - 校验原密码 - 失败
	 * 
	 * @since 2017年1月16日
	 * @author tb.yumu@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {
			"classpath:com/yikuyi/party/customer/bll/userlogin_sampledata.xml" })
	public void testCheckedOldPassword_failed() throws Exception{
		try {
			customerSummeryManager.checkedOldPassword("9999999901", "123qwe");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 单元测试 - 修改密码 - 成功
	 * 
	 * @since 2017年1月16日
	 * @author tb.yumu@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {
			"classpath:com/yikuyi/party/customer/bll/userlogin_sampledata.xml" })
	public void testChangePasswordByLoginId_success() throws Exception{
		UserVo userVo = new UserVo();
		userVo.setNewPassword("123qwe");
		customerSummeryManager.changePasswordByLoginId("9999999901", userVo);
	}

	/**
	 * 单元测试 - 修改密码 - 修改失败
	 * 
	 * @since 2017年1月16日
	 * @author tb.yumu@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {
			"classpath:com/yikuyi/party/customer/bll/userlogin_sampledata.xml" })
	public void testChangePasswordByLoginId_failed() throws Exception{
		try {
			UserVo userVo = new UserVo();
			userVo.setNewPassword("123qwe");
			customerSummeryManager.changePasswordByLoginId("999999901", userVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 单元测试 - 修改密码 - 原密码为空
	 * 
	 * @since 2017年1月16日
	 * @author tb.yumu@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {
			"classpath:com/yikuyi/party/customer/bll/userlogin_sampledata.xml" })
	public void testChangePasswordByLoginId_passNull() throws Exception{
		try {
			UserVo userVo = new UserVo();
			userVo.setNewPassword("");
			customerSummeryManager.changePasswordByLoginId("99999999901", userVo);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testSendMail() {
		customerSummeryManager.sendMail("MTIzNDU2QHFxLmNvbQ==");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"classpath:com/yikuyi/party/customer/bll/party_sampledata.xml"})
	public void testGetUserLoginInfo_MainRole() {
		Collection cn = new ConcurrentLinkedQueue<>();
		LoginUser user = new LoginUser("9999999909", "123456@qq.com", "123456", cn);
		customerSummeryManager.getUserLoginInfo(user);
		
		Collection cn1 = new ConcurrentLinkedQueue<>();
		LoginUser user1 = new LoginUser("1", "123456@qq.com", "123456", cn1);
		customerSummeryManager.getUserLoginInfo(user1);
	}


	@Test
	public void testGetMailCode() throws Exception {
		customerSummeryManager.getMailCode("123456@qq.com");
	}

	/**
	 * 单元测试 - 隐藏邮箱
	 * 
	 * @since 2017年2月23日
	 * @author tb.yumu@yikuyi.com
	 */
	@Test
	public void testHideMail_success() {
		customerSummeryManager.hideMail("123456@qq.com");
	}

	/**
	 * 单元测试 - 隐藏手机号
	 * 
	 * @since 2017年2月23日
	 * @author tb.yumu@yikuyi.com
	 */
	@Test
	public void testHideMail_failed() {
		customerSummeryManager.hideMail("13544781256");
	}
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = {"classpath:com/yikuyi/party/customer/bll/party_sampledata.xml"})
	public void testGetAccountByPartyId() {
		customerSummeryManager.getAccountByPartyId("1");
		customerSummeryManager.getAccountByPartyId("9999999909");
	}
}