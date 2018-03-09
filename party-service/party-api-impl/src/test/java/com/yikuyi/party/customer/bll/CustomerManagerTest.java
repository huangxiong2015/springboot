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
package com.yikuyi.party.customer.bll;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
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
import com.framework.springboot.utils.AuthorizationUtil;
import com.framewrok.springboot.web.LoginUserInjectionInterceptor;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.yikuyi.party.contact.vo.EnterpriseParamVo;
import com.yikuyi.party.contact.vo.EnterpriseVo;
import com.yikuyi.party.contact.vo.UserExtendVo;
import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.party.model.Party.PartyStatus;
import com.yikuyi.party.person.model.Person.RelationSratus;
import com.yikuyi.workflow.Apply;
import com.yikuyi.workflow.WorkflowClientBuilder;
import com.yikuyi.workflow.resource.ApplyClient;
@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class,MockitoTestExecutionListener.class })
@Transactional
@Rollback
public class CustomerManagerTest {
	@Autowired
	private CustomerManager customerManager;
	
	@Value("${api.pay.serverUrlPrefix}")
	private String serverUrl;
	@Value("${api.workflow.serverUrlPrefix}")
	private String workflowUrlPrefix;
	
	
	@SpyBean
	private WorkflowClientBuilder workflowClientBuilder;
	
	@SpyBean
	private AuthorizationUtil authorizationUtil;
	
	private MockHttpServletRequest request; 
    private MockHttpServletResponse response; 
	
	@Before
	public void init() {
		LoginUser loginUser = new LoginUser("9999999901", "admin", "9999999901", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));		
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));;
		RequestContextHolder.currentRequestAttributes().setAttribute(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser, RequestAttributes.SCOPE_REQUEST);
		request = new MockHttpServletRequest();    
	    request.setCharacterEncoding("UTF-8");    
	    response = new MockHttpServletResponse();    
	}

	
	public CustomerManagerTest(){
		MockitoAnnotations.initMocks(this);
	}
	
	/**
	 * 单元测 -查询企业或个人基本信息
	 * 
	 * @since 2017年2月8日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"userInfo_sampledata.xml"})
	public void testGetBaseInfoDetail(){
		UserExtendVo userVo =customerManager.getBaseInfoDetail("999999990122",null);
		assertEquals(userVo.getAddress(), "中国深圳市南山区高新技术产业园麻雀岭工业区M-8栋4楼");
	}
	
	/**
	 * 保存基本信息
	 * @param UserExtendVo
	 * @return UserExtendVo
	 * @since 2016年2月24日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = { "userInfo_sampledata.xml" })
	public void testSave(){
		UserExtendVo user = new UserExtendVo();
		user.setAddress("中国深圳市南山区高新技术产业园麻雀岭工业区M-8栋4楼");
		user.setPartyId("999999990122");
		user.setCity("10001");
		user.setCityName("深圳市");
		user.setCountry("10002");
		user.setCountryName("南山区");
		user.setAddressLevel("1");
		user.setCreatedDate(new Date());
		user.setId("1");
		user.setLogoUrl("http://www.baidu.com");
		user.setLoginCount(1);
		user.setMail("12345@qq.com");
		user.setMobile("13066939619");
		user.setName("叶良辰");
		user.setPostcode("11111");
		user.setProvince("10000");
		user.setProvinceName("广东省");
		user.setSex("1");
		user.setStatus(PartyStatus.PARTY_ENABLED);
		customerManager.save(user);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT , value={"classpath:com/yikuyi/party/customer/bll/customer/userlogin_sampledata.xml"})
	public void testFindCustomerUser(){
		customerManager.findCustomerUser(new EnterpriseParamVo(), RowBounds.DEFAULT);
	}

	
	/**
	 * 更新用户信息
	 * @param userInfoVo
	 * @since 2017年2月14日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "person_sampledata2.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "person_update_data.xml")
	public void testUpdate(){
		UserExtendVo userInfoVo = new UserExtendVo();
		userInfoVo.setId("9999999901");
		
		userInfoVo.setLogoUrl("www.baidu.com");
		customerManager.update(userInfoVo);
	}
	@Test
	@DatabaseSetup(type=DatabaseOperation.CLEAN_INSERT,value="person_sampledata4.xml")
	@ExpectedDatabase(assertionMode=DatabaseAssertionMode.NON_STRICT,value="person_update_data3.xml")
	public void testCreatAccount(){
		UserVo userVo = new UserVo();
		userVo.setAddress("ssss");
		userVo.setCity("130300");
		userVo.setCityName("秦皇岛市");
		userVo.setComments("ssss");
		userVo.setCompanyName("yikuyi");
		userVo.setCorCategory("2000");
		userVo.setCountry("130303");
		userVo.setCountryName("山海关区");
		userVo.setdCode("www");
		userVo.setId("9999999901");
		userVo.setIndustryCategory("1000");
		userVo.setMail("test@126.com");
		userVo.setMobile("18888888888");
		userVo.setName("wwww");
		userVo.setPersonalTitle("engineer");
		userVo.setProvince("130000");
		userVo.setProvinceName("河北省");
		userVo.setQq("88888888");
		userVo.setReason("sss");
		userVo.setWebSite("www.baidu.com");
		EnterpriseVo entVo = new EnterpriseVo();
		entVo.setProvince("1");
		entVo.setProvinceName("深圳");
		userVo.setEnterpriseVo(entVo);
		customerManager.creatAccount(userVo);
	}
	/**
	 * 根据状态查询用户
	 * @param relationSratus
	 * @return
	 * @since 2017年2月15日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "partyRelationShip_sampledata.xml")
	public void testGetPersons(){
		customerManager.getPersons(RelationSratus.RELATED,"9999999901");
	}
	/**
	 * 批量添加子账号
	 * @param accounts
	 * @return
	 * @since 2017年5月11日
	 * @author zr.aoxianbing@yikuyi.com
	 * @throws Exception 
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "partyRelationShip_sampledata2.xml")
	public void testAddSubAccount() throws Exception{
		List<String> accounts = new ArrayList<String>();
		accounts.add("test@126.com");
		Apply apply = new Apply();
		String applyContation ="{'id':'666665','address':'中国深圳市南山区高新技术产业园麻雀岭工业区M-8栋4楼',"
				+ "'name':'myHome','logo':'http://www.baidu.com','province':'10000',"
				+ "'provinceName':'广东省','city':'10001','cityName':'深圳市','country':'10002','countryName':'南山区','mail':'12345@qq.com','contactUserName':'叶良辰','contactUserTel':'13066939619',"
				+ "'corCategory':'CORPORATION_CATEGORY_ID',"
				+ "'industryCategory':'INDUSTRY_CATEGORY_ID','webSite':'WebSite','description':'PARTY_BRIEF','otherAttrs':[{'INDUSTRY_CATEGORY_ID_OTHER':'测试key'}],"
				+ "'map':{'REG_ADDR':'0','BUSI_LIS_TYPE':'COMMON','BUSI_LIC_PIC':'http://www.baidu.com',"
		+"'TAX_REG_PIC':'http://www.baidu.com','OCC_PIC':'http://www.baidu.com','BUSI_PDF_NAME':'http://www.baidu.com',"
		+"'TAX_PDF_NAME':'http://www.baidu.com','OCC_PDF_NAME':'http://www.baidu.com'}}";
		apply.setApplyId("111");
		apply.setApplyOrgId("666665");
		apply.setApplyUserId("9999999902");
		apply.setApplyContent(applyContation);
		ApplyClient mockApplyResource = Mockito.mock(ApplyClient.class);
		Mockito.when(workflowClientBuilder.applyClient()).thenReturn(mockApplyResource);
		Mockito.when(mockApplyResource.createApply(apply, authorizationUtil.getLoginAuthorization())).thenReturn("1000000");
			
		customerManager.addSubAccount("9999999901", accounts);
	}
	/**
	 * 根据用户Id更新用户状态信息
	 * @param id
	 * @since 2017年2月15日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "person_sampledata3.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "person_update_data2.xml")
	public void testUpdateById(){
		customerManager.updateById("9999999901", RelationSratus.RELATED,"9999999901");
	}
	/**
	 * 根据用户名称查询用户列表
	 * @param username
	 * @return
	 * @since 2017年2月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "person_sampledata3.xml")
	public void testGetUserByName(){
		List<UserVo> list = customerManager.getUserByName("李四");
		assertEquals(1,list.size());
		assertEquals("李四",list.get(0).getName());
	}
	/**
	 * 根据用户名称查询用户列表
	 * @param username
	 * @return
	 * @since 2017年2月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "person_sampledata3.xml")
	public void testGetUsersByName(){
		UserVo vo = customerManager.getUsersByName("李四");
		assertEquals("李四",vo.getName());
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "userlogin_sampledata.xml")
	public void testSendMail(){
		customerManager.sendMail("9999999901","9999999901");
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value ="classpath:com/yikuyi/party/customer/bll/customer/person_sampledata.xml")
	public void testFindPersonInfoByPartyId(){
		customerManager.findPersonInfoByPartyId("9999999901");
		
	}
	
	@Test
	public void testUpdateStateId(){
		customerManager.updateStateId("1","PARTY_ENABLED");
		
	}
	
	/**
	 * 发送邮件（更新账号，验证账号）
	 * @param userVo
	 * @since 2017年2月9日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	public void testSendMail1() {
		UserVo userVo = new UserVo();
		userVo.setId("123456789");
		userVo.setImgCode("ABCD");
		userVo.setType("1");
		userVo.setMail("admin@yikuyi.com");
		customerManager.sendMail(userVo);
	}
	/**
	 * 加入主账号邮件
	 * @param account
	 * @param entId
	 * @return
	 * @since 2017年5月11日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	public void testSendAccountMail() {
		customerManager.sendAccountMail("9999999901","test@126.com","99999999","1000");
	}
	/**
	 * 注销账号
	 * @param accountId
	 * @param userId
	 * @return
	 * @since 2017年5月8日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "userlogin_sampledata2.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "userlogin_update_data.xml")
	public void testCancelAccount() {
		customerManager.cancelAccount("123456@yikuyi.com","9999999901");
	}

	/**
	 * 根据已验证邮箱查询数据
	 * @param username
	 * @return
	 * @since 2017年8月8日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "userlogin_sampledata2.xml")
	public void testGetUsersByMail() {
		customerManager.getUsersByMail("123456@yikuyi.com");
	}
	
	/**
	 * 根据已验证邮箱查询数据
	 * @param username
	 * @return
	 * @since 2017年8月8日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "userlogin_sampledata2.xml")
	public void testFrozenAccount() {
		customerManager.frozenAccount(PartyStatus.PARTY_ENABLED,"1","1");
	}
	/**
	 * 根据用户ID获取登录密码，登录ID
	 * @param partyId
	 * @return
	 * @since 2017年6月29日
	 * @author tb.yumu@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "userlogin_sampledata2.xml")
	public void testGetUserLoginByPartyid() {
		customerManager.getUserLoginByPartyid("1111");
	}
	
	/**
	 * 导出个人会员列表
	 * @param vo
	 * @return
	 * @since 2017年4月13日
	 * @author zr.aoxianbing@yikuyi.com
	 * @throws IOException 
	 */
	@Test
	@Rollback
	public void testExportUser() throws IOException {
		EnterpriseParamVo vo = new EnterpriseParamVo();
		customerManager.exportUser(vo,response);
	}
}
