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
package com.yikuyi.party.register.bll;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.yikuyi.message.MessageClientBuilder;
import com.yikuyi.message.resource.ImgClient;
import com.yikuyi.message.resource.MailClient;
import com.yikuyi.message.sms.vo.MailAddressValidVO;
import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.workflow.WorkflowClientBuilder;
import com.yikuyi.workflow.resource.TaskClient;
import com.yikuyi.workflow.vo.TaskVo;
import com.yikuyi.workflow.vo.TaskVo.TaskVoType;
import com.ykyframework.exception.BusinessException;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,TransactionDbUnitTestExecutionListener.class,MockitoTestExecutionListener.class })
@Transactional
@Rollback
public class RegisterManagerTest {
	
	
	@SpyBean
	private MessageClientBuilder messageClientBuilder;
	
	
	@SpyBean
	private WorkflowClientBuilder workflowClientBuilder;
	
	@Autowired
	private RegisterManager registerManager;


	@Value("${api.message.serverUrlPrefix}")
	private String serverUrl;
	
	@Value("${api.workflow.serverUrlPrefix}")
	private String workflowUrlPrefix;
	
	
	public RegisterManagerTest(){
		MockitoAnnotations.initMocks(this);
	}
	
	
	
	/**
	 * 个人注册
	 * 
	 * @param userVo
	 * @since 2017年1月12日
	 * @author zr.aoxianbing@yikuyi.com
	 * @throws BusinessException 
	 */
	@Test
	// @DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value =
	// "userlogin_sampledata2.xml")
	// @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value
	// = "userlogin_update_data.xml")
	public void testSave() throws UnsupportedEncodingException, BusinessException {
		
		String uuid = UUID.randomUUID().toString();
		UserVo userVo = new UserVo();
		userVo.setUuid(uuid);
		userVo.setImgCode("ABCD");
		userVo.setMobile("123456789");
		userVo.setPassword("123456");
		
		Mockito.when(messageClientBuilder.imgResource(String.class)).thenReturn(Mockito.mock(ImgClient.class));
		Mockito.when(messageClientBuilder.imgResource(String.class).validVerifyCode(uuid, "ABCD")).thenReturn("SUCCESS");
		
		
		
		registerManager.save(userVo);
	}

	@Test
	// @DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value =
	// "userlogin_sampledata2.xml")
	// @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value
	// = "userlogin_update_data.xml")
	public void testSaveEnt()throws Exception {
		UserVo userVo = new UserVo();
		userVo.setUuid("123456789");
		userVo.setImgCode("ABCD");
		userVo.setMobile("15813513531");
		userVo.setPassword("123456");
		userVo.setMail("sz@test.com");
		
		ImgClient imgClient = Mockito.mock(ImgClient.class);
		Mockito.when(messageClientBuilder.imgResource(String.class)).thenReturn(imgClient);
		Mockito.when(imgClient.validVerifyCode("123456789", "ABCD")).thenReturn("SUCCESS");
		MailAddressValidVO vo = new MailAddressValidVO();
		vo.setUuid("123456789");
		vo.setMailAddress("sz@test.com");
		MailClient mailClient = Mockito.mock(MailClient.class);
		Mockito.when(messageClientBuilder.mailResource(String.class)).thenReturn(mailClient);
		Mockito.when(mailClient.getVerifyCode(Mockito.any(MailAddressValidVO.class))).thenReturn("1234");
		
		
		
		registerManager.saveEnt(userVo);
	}

	@Test
	// @DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value =
	// "userlogin_sampledata2.xml")
	// @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value
	// = "userlogin_update_data.xml")
	public void testVerifyCode() throws Exception {
		UserVo userVo = new UserVo();
		userVo.setUuid("123456789");
		userVo.setImgCode("ABCD");
		userVo.setMobile("15813513532");
		userVo.setPassword("123456");
		
		Mockito.when(messageClientBuilder.imgResource(String.class)).thenReturn(Mockito.mock(ImgClient.class));
		Mockito.when(messageClientBuilder.imgResource(String.class).validVerifyCode("123456789", "ABCD")).thenReturn("SUCCESS");
		
		registerManager.verifyCode(userVo);
	}

	@Test
	// @DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value =
	// "userlogin_sampledata2.xml")
	// @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value
	// = "userlogin_update_data.xml")
	public void testGetVerifyCode() throws UnsupportedEncodingException {
		UserVo userVo = new UserVo();
		userVo.setUuid("123456789");
		userVo.setImgCode("ABCD");
		userVo.setMobile("15813513532");
		userVo.setPassword("123456");
		userVo.setMail("admin@yikuyi.com");
		
		MailAddressValidVO vo = new MailAddressValidVO();
		vo.setUuid("123456789");
		vo.setMailAddress("sz@test.com");
		MailClient mailClient = Mockito.mock(MailClient.class);
		Mockito.when(messageClientBuilder.mailResource(String.class)).thenReturn(mailClient);
		Mockito.when(mailClient.getVerifyCode(Mockito.any(MailAddressValidVO.class))).thenReturn("1234");
		
		registerManager.getVerifyCode(userVo);
	}

	@Test
	public void testSendMail() {
		UserVo userVo = new UserVo();
		userVo.setUuid("123456789");
		userVo.setImgCode("ABCD");
		userVo.setMobile("15813513532");
		userVo.setPassword("123456");
		userVo.setMail("admin@yikuyi.com");
		registerManager.sendMail(userVo);
	}
	/**
	 * 重新发送注册邮件
	 * @param userVo
	 * @since 2017年1月12日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	public void testReSend() {
		UserVo userVo = new UserVo();
		userVo.setUuid("123456789");
		userVo.setImgCode("ABCD");
		userVo.setMobile("15813513532");
		userVo.setPassword("123456");
		userVo.setMail("1111@qq.com");
		registerManager.reSend(userVo);
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "userlogin_sampledata.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "userlogin_update_data.xml")
	public void testSavePerson() {
		UserVo userVo = new UserVo();
		userVo.setUuid("123456789");
		userVo.setImgCode("ABCD");
		userVo.setMobile("15813513532");
		userVo.setPassword("123456");
		userVo.setMail("admin@yikuyi.com");
		registerManager.savePerson(userVo);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "userlogin_sampledata.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "userlogin_update_data2.xml")
	public void testSaveEnterprise() {
		UserVo userVo = new UserVo();
		userVo.setUuid("123456789");
		userVo.setImgCode("ABCD");
		userVo.setMobile("15813513532");
		userVo.setPassword("123456");
		userVo.setMail("admin@yikuyi.com");
		registerManager.saveEnterprise(userVo);
	}
	/**
	 * 加入主账号
	 * @param entId
	 * @param account
	 * @return
	 * @since 2017年5月5日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	public void testJoinMainAccount() throws Exception {
		
		TaskClient taskResource = Mockito.mock(TaskClient.class);
		// 调用workFlow服务
		TaskVo taskVo = new TaskVo();
		Mockito.when(workflowClientBuilder.taskClient()).thenReturn(taskResource);
		Mockito.doNothing().when(taskResource).disposeTask(TaskVoType.APPROVED, "1000", taskVo);
		
		registerManager.joinMainAccount("99999999", "test@126.com", "1000");
		registerManager.joinMainAccount("99999999", "123456@qq.com", "1000");
	}
	/**
	 * 生成登陆账号（根据账号）
	 * @param partyId
	 * @param account
	 * @return
	 * @since 2017年7月28日
	 * @author zr.aoxianbing@yikuyi.com
	 * @throws BusinessException 
	 */
	@Test
	public void testUpgrade() throws Exception {
		registerManager.upgrade("9999999901", "test1111@126.com");
	}
	/**
	 * 根据账号创建子账号(子账号设置密码)
	 * @param account
	 * @param entId
	 * @return
	 * @since 2017年5月5日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "userlogin_sampledata.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "userlogin_update_data2.xml")
	public void testSaveAccout() throws Exception {
		UserVo vo = new UserVo();
		vo.setMail("admin@yikuyi.com");
		vo.setEnterpriseId("111");
		vo.setPassword("123456");
		registerManager.saveAccout(vo);
	}
	
	
}