/*
 * Created: 2017年2月8日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.userlogin.bll;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.boot.test.mock.mockito.SpyBean;
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
import com.github.pagehelper.PageInfo;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.yikuyi.basedata.ShipmentClientBuilder;
import com.yikuyi.basedata.category.model.Category;
import com.yikuyi.basedata.resource.CategoryClient;
import com.yikuyi.party.contact.vo.AccountVo;
import com.yikuyi.party.contact.vo.User;
import com.yikuyi.party.contact.vo.UserExtendVo;
import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.party.credit.model.PartyCredit;
import com.yikuyi.party.login.model.UserLogin;
import com.yikuyi.party.userLogin.bll.UserLoginManager;
import com.yikuyi.party.vendor.vo.Vendor.Currency;
import com.yikuyi.transaction.order.model.OrderRole.RoleType;
@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class,MockitoTestExecutionListener.class })
@Transactional
@Rollback
public class UserLoginManagerTest {
	@Autowired
	private UserLoginManager userLoginManager;

	@SpyBean
	private ShipmentClientBuilder shipmentClientBuilder;
	public UserLoginManagerTest() {
		MockitoAnnotations.initMocks(this);
	}
	@Before
	public void init() {
		LoginUser loginUser = new LoginUser("9999999901", "admin", "9999999901", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));		
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));;
		RequestContextHolder.currentRequestAttributes().setAttribute(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser, RequestAttributes.SCOPE_REQUEST);
	}
	/**
	 * 根据账号判断是否存在（手机、邮箱）
	 * @param account
	 * @return
	 * @since 2017年1月12日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "userlogin_sampledata.xml")
	public void testIsExist(){
		Boolean falg = userLoginManager.isExist("MTIzNDU2Nzg5MTA=");
		assertEquals(true,falg);
	}
	/**
	 *  查询账号列表
	 * @param name
	 * @return
	 * @since 2017年3月17日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "userlogin_sampledata.xml")
	public void testSearch(){
		PageInfo<UserVo> page = userLoginManager.search("", RowBounds.DEFAULT, "9999999901", 1, 10);
		List<UserVo> list = page.getList();
		//assertEquals(0, list.size());
		
		 userLoginManager.search("", RowBounds.DEFAULT, "", 1, 10);
	}
	/**
	 *  查询账号列表
	 * @param name
	 * @return
	 * @since 2017年3月17日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "userlogin_sampledata.xml")
	public void testSearch1(){
		PageInfo<UserVo> page = userLoginManager.search("1111", RowBounds.DEFAULT, "111", 1, 10);
		List<UserVo> list = page.getList();
		assertEquals(0, list.size());
	}
	/**
	 *  查询账号列表
	 * @param name
	 * @return
	 * @since 2017年3月17日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "userlogin_sampledata3.xml")
	public void testSearch2(){
		/*PageInfo<UserVo> page = userLoginManager.search("123456@yikuyi.com", RowBounds.DEFAULT, "9999999901", 1, 10);
		List<UserVo> list = page.getList();
		assertEquals(1, list.size());
		UserVo uservo = list.get(0);
		assertEquals("李四", uservo.getName());
		assertEquals("123456@yikuyi.com", uservo.getMail());
		assertEquals("PARTY_ENABLED", uservo.getStatusId());*/
	}
	/**
	 * 根据账号判断是否存在（手机、邮箱）
	 * @param account
	 * @return
	 * @since 2017年1月12日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "userlogin_sampledata.xml")
	public void testGetAccount(){
		Boolean falg = userLoginManager.getAccount("12345678910");
		assertEquals(true,falg);
	}
	/**
	 * 根据账号id查询用户
	 * @param userVo
	 * @return
	 * @since 2017年1月19日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "userlogin_sampledata.xml")
	public void testGetAccountById(){
		UserLogin falg = userLoginManager.getAccountById("12345678910");
		assertEquals("12345678910",falg.getId());
	}
	/**
	 * 根据partyid和类型查询用户
	 * @param id
	 * @param type
	 * @return
	 * @since 2017年3月16日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "userlogin_sampledata3.xml")
	public void testGetAccountByIdAndType(){
		String falg = userLoginManager.getAccountByIdAndType("9999999902","EMAIL");
		assertEquals("123456@yikuyi.com",falg);
	}
	
	/**
	 * 保存密码
	 * @param userVo
	 * @return
	 * @since 2017年1月19日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "userlogin_sampledata2.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "userlogin_update_data.xml")
	public void testInitPassWord(){
		UserVo userVo = new UserVo();
		userVo.setMail("123456@yikuyi.com");
		userVo.setPassword("123456");
		userVo = userLoginManager.initPassWord(userVo);
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "userlogin_sampledata4.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "userlogin_update_data3.xml")
	public void testUpdateAccount(){
		AccountVo accountVo = new AccountVo();
		accountVo.setAccount("234567@yikuyi.com");
		accountVo.setPartyId("9999999901");
		accountVo.setType("2");
		userLoginManager.updateAccount(accountVo);
	}
	
	/**
	 * 新增账号
	 * @param userVo
	 * @return
	 * @since 2017年3月17日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "userlogin_sampledata3.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "userlogin_save_data.xml")
	public void testSave(){
		UserVo userVo = new UserVo();
		userVo.setMail("23456@yikuyi.com");
		userVo.setPassword("123456");
		userVo.setTelNumber("13513513532");
		userVo.setName("王二");
		userVo.setId("9999999901");
		userLoginManager.save(userVo);
	}
	/**
	 * 根据id查询用户
	 * @param id
	 * @return
	 * @since 2017年3月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "userlogin_sampledata3.xml")
	public void testGetPerson(){
		UserVo uservo = userLoginManager.getPerson("9999999902");
		assertEquals("李四", uservo.getName());
		assertEquals("123456@yikuyi.com", uservo.getMail());
		assertEquals("13513513531", uservo.getTelNumber());
	}
	/**
	 * 修改账号
	 * @param userVo
	 * @return
	 * @since 2017年3月17日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "userlogin_sampledata3.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "userlogin_update_data2.xml")
	public void testUpdate(){
		UserVo userVo = new UserVo();
		userVo.setTelNumber("13513513533");
		userVo.setName("李四四");
		userVo.setId("9999999902");
		userLoginManager.update(userVo);
	}
	/**
	 * 重置密码
	 * @param userVo
	 * @return
	 * @since 2017年3月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "userlogin_sampledata3.xml")
	public void testUpdatePwd(){
		userLoginManager.updatePwd("9999999901","123456","9999999902");
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "partyRelationShip_sampledata.xml")
	public void testgetReportsTo(){
		userLoginManager.getReportsTo("1001");
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "user_sampledata.xml")
	public void addUserTest(){
		UserVo userVo = new UserVo();
		userVo.setMail("159852147@qq.com");
		userVo.setPassword("123456");
		userVo.setName("张三丰");
		userVo.setSex("0");
		userVo.setDeptId("1001");
		userVo.setManager(true);
		userVo.setTelNumber("13652365236");
		List<String> roleList = new ArrayList<>();
		roleList.add("1001");
		userVo.setRoleList(roleList);
		userLoginManager.addUser(userVo);
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "user_sampledata.xml")
	public void addUserTest1(){
		UserVo userVo = new UserVo();
		userVo.setMail("1234561@yikuyi.com");
		userVo.setPassword("123456");
		userVo.setName("张三丰");
		userVo.setSex("0");
		userVo.setDeptId("1001");
		userVo.setManager(false);
		userVo.setTelNumber("13652365236");
		List<String> roleList = new ArrayList<>();
		roleList.add("1001");
		userVo.setRoleList(roleList);
		userLoginManager.addUser(userVo);
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "user_sampledata.xml")
	public void getUserDetailTest(){
		userLoginManager.getUserDetail("9999999902");
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "user_sampledata.xml")
	public void updateUserTest(){
		UserVo userVo = new UserVo();
		userVo.setId("9999999902");
		userVo.setMail("159852147@qq.com");
		userVo.setPassword("123456");
		userVo.setName("张三丰");
		userVo.setSex("0");
		userVo.setDeptId("1001");
		userVo.setManager(true);
		userVo.setTelNumber("13652365236");
		List<String> roleList = new ArrayList<>();
		roleList.add("1001");
		userVo.setRoleList(roleList);
		userLoginManager.updateUser(userVo);
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "user_sampledata.xml")
	public void updateUserTest1(){
		UserVo userVo = new UserVo();
		userVo.setId("9999999902");
		userVo.setMail("1234561@yikuyi.com");
		userVo.setPassword("123456");
		userVo.setName("张三丰");
		userVo.setSex("0");
		userVo.setDeptId("1001");
		userVo.setManager(false);
		userVo.setTelNumber("13652365236");
		List<String> roleList = new ArrayList<>();
		roleList.add("1001");
		userVo.setRoleList(roleList);
		userLoginManager.updateUser(userVo);
	}
	
	/**
	 * 发送邮件（更新账号，验证账号）
	 * @param userVo
	 * @since 2017年2月9日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	public void testSendMail() {
		UserVo userVo = new UserVo();
		userVo.setId("123456789");
		userVo.setImgCode("ABCD");
		userVo.setType("1");
		userVo.setMail("admin@yikuyi.com");
		userLoginManager.sendMail(userVo);
	}
	
	@Test
	public void testUpdatePerson(){
		UserExtendVo vo = new UserExtendVo();
		vo.setPartyId("1");
		vo.setLogoUrl("http://www.baidu.com");
		vo.setMail("111@qq.com");
		 userLoginManager.updatePerson(vo);
	}
	
	@Test
	public void testSendCreateMail() {
		UserVo userVo = new UserVo();
		userVo.setId("123456789");
		userVo.setImgCode("ABCD");
		userVo.setType("1");
		userVo.setMail("admin@yikuyi.com");
		userLoginManager.sendCreateMail(userVo);
	}
	
	@Test
	public void testGetReportsTo() {
		UserVo userVo = new UserVo();
		userVo.setId("123456789");
		userVo.setImgCode("ABCD");
		userVo.setType("1");
		userVo.setMail("admin@yikuyi.com");
		userLoginManager.getReportsTo("11");
	}
	
	@Test
	public void testGetEmailListByRoleType() {
		userLoginManager.getEmailListByRoleType(RoleType.BUYER.name());
	}
	
	@Test
	public void testGetPartyByIds() {
		userLoginManager.getPartyByIds(java.util.Arrays.asList("1"));
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"classpath:com/yikuyi/party/customer/bll/userlogin_sampledata.xml"})
	public void testGetPartyCreditVo() {
		List<Category> cat = new ArrayList<>();
		Category category = new Category();
		category.setCategoryId("1");
		cat.add(category);
		CategoryClient categoryClient =Mockito.mock(CategoryClient.class);
		Mockito.when(shipmentClientBuilder.categoryResource()).thenReturn(categoryClient);
		Mockito.when(categoryClient.categoryList("1")).thenReturn(cat);
		userLoginManager.getPartyCreditVo("9999999908",Currency.CNY);
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = {"classpath:com/yikuyi/party/customer/bll/userlogin_sampledata.xml"})
	public void testGetPartyCreditInfoList() {
		userLoginManager.getPartyCreditInfoList("[{'partyId':'9999999908','currency':'CNY'}]");
	}
	
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = {"classpath:com/yikuyi/party/customer/bll/userlogin_sampledata.xml"})
	public void testUpdatePartyCredit() {
		PartyCredit partyCredit = new PartyCredit();
		partyCredit.setPartyId("22");
		partyCredit.setCurrency(Currency.CNY);
		userLoginManager.updatePartyCredit(partyCredit);
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = {"classpath:com/yikuyi/party/customer/bll/userlogin_sampledata.xml"})
	public void testUserLoginListener() {
		User user = new User();
		userLoginManager.userLoginListener(user);
	}
}
