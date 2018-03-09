package com.yikuyi.party.vendorManage.bll;


import java.util.Arrays;
import java.util.Date;

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
import com.yikuyi.party.role.model.RoleTypeEnum;
import com.yikuyi.party.vendor.vo.Vendor.VendorApplyType;
import com.yikuyi.workflow.Apply;
import com.ykyframework.exception.BusinessException;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class,MockitoTestExecutionListener.class })
@Transactional
@Rollback
public class SendMailTest {
	
	@Autowired
	private SendMail sendMail;
	
	public SendMailTest() {
		MockitoAnnotations.initMocks(this);
	}
	
	
	@Before
	public void init() {
		LoginUser loginUser = new LoginUser("9999999901", "admin", "9999999901", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));		
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));;
		RequestContextHolder.currentRequestAttributes().setAttribute(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser, RequestAttributes.SCOPE_REQUEST);
	}
	
	/**
	 * 发送邮件
	 * @since 2017年11月22日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value ="classpath:com/yikuyi/party/vendors/bll/vendorManager_sampledata.xml")
	public void testNewPass(){
		Apply apply = new Apply();
		apply.setApplyOrgId("1");
		apply.setApplyUserId("1");
		apply.setcRoleId("CEO");
		apply.setCreatedDate(new Date());
		apply.setApplyContent("{'describe':'11'}");
		sendMail.newPass(apply);
	}
	
	
	/**
	 * 建档不通过
	 * @since 2017年11月22日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value ="classpath:com/yikuyi/party/vendors/bll/vendorManager_sampledata.xml")
	public void testNewNotPass(){
		Apply apply = new Apply();
		apply.setApplyOrgId("1");
		apply.setApplyUserId("1");
		apply.setcRoleId("CEO");
		apply.setCreatedDate(new Date());
		sendMail.newNotPass(apply);;
	}
	
	/**
	 * 变更通过
	 * @since 2017年11月22日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value ="classpath:com/yikuyi/party/vendors/bll/vendorManager_sampledata.xml")
	public void testChangePass(){
		Apply apply = new Apply();
		apply.setApplyOrgId("1");
		apply.setApplyUserId("1");
		apply.setcRoleId("CEO");
		apply.setCreatedDate(new Date());
		apply.setProcessId(VendorApplyType.ORG_SUPPLIER_INFO_CHANGE_REVIEW.name());
		apply.setApplyContent("{'checkVendorInfoVo':{'describe':'11'}}");
		sendMail.changePass(apply);;
		
		Apply apply1 = new Apply();
		apply1.setApplyOrgId("1");
		apply1.setApplyUserId("1");
		apply1.setcRoleId("CEO");
		apply1.setCreatedDate(new Date());
		apply.setApplyContent("{'checkVendorInfoVo':{'describe':'11'}}");
		apply1.setProcessId(VendorApplyType.ORG_SUPPLIER_PRODUCTLINE_CHANGE_REVIEW.name());
		sendMail.newPass(apply1);
	}
	
	
	/**
	 * 变更通过
	 * @since 2017年11月22日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value ="classpath:com/yikuyi/party/vendors/bll/vendorManager_sampledata.xml")
	public void testChangeNotPass(){
		Apply apply = new Apply();
		apply.setApplyOrgId("1");
		apply.setApplyUserId("1");
		apply.setCreatedDate(new Date());
		sendMail.changeNotPass(apply);
		
	}
	
	
	/**
	 * 启动通过
	 * @since 2017年11月22日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value ="classpath:com/yikuyi/party/vendors/bll/vendorManager_sampledata.xml")
	public void testStartPass(){
		Apply apply = new Apply();
		apply.setApplyOrgId("1");
		apply.setApplyUserId("1");
		apply.setCreatedDate(new Date());
		sendMail.startPass(apply);
		
	}
	/**
	 * 启动不通过
	 * @since 2017年11月22日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value ="classpath:com/yikuyi/party/vendors/bll/vendorManager_sampledata.xml")
	public void testStartNotPass(){
		Apply apply = new Apply();
		apply.setApplyOrgId("1");
		apply.setApplyUserId("1");
		apply.setCreatedDate(new Date());
		sendMail.startNotPass(apply);
		
	}
	
	/**
	 * 失效通过
	 * @since 2017年11月22日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value ="classpath:com/yikuyi/party/vendors/bll/vendorManager_sampledata.xml")
	public void testLosePass(){
		Apply apply = new Apply();
		apply.setApplyOrgId("1");
		apply.setApplyUserId("1");
		apply.setCreatedDate(new Date());
		apply.setApplyContent("{'CheckStartOrLose':{'describe':'11'}}");
		sendMail.losePass(apply);
		
	}
	
	/**
	 * 失效通过
	 * @since 2017年11月22日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value ="classpath:com/yikuyi/party/vendors/bll/vendorManager_sampledata.xml")
	public void testLoseNotPass(){
		Apply apply = new Apply();
		apply.setApplyOrgId("1");
		apply.setApplyUserId("1");
		apply.setCreatedDate(new Date());
		sendMail.loseNotPass(apply);
		
	}
	
	/**
	 * 审核申请，通用版
	 * @since 2017年11月22日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value ="classpath:com/yikuyi/party/vendors/bll/vendorManager_sampledata.xml")
	public void testCheckApply(){
		Apply apply = new Apply();
		apply.setApplyOrgId("1");
		apply.setApplyUserId("1");
		apply.setCreatedDate(new Date());
		try {
			sendMail.checkApply("1",RoleTypeEnum.SUPPLIER_MANAGER,VendorApplyType.ORG_SUPPLIER_ARCHIVES_REVIEW,"1");
			
			sendMail.checkApply("1",RoleTypeEnum.SUPPLIER_MANAGER,VendorApplyType.ORG_SUPPLIER_INFO_CHANGE_REVIEW,"1");
			
			sendMail.checkApply("1",RoleTypeEnum.SUPPLIER_MANAGER,VendorApplyType.ORG_SUPPLIER_PRODUCTLINE_CHANGE_REVIEW,"1");
			sendMail.checkApply("1",RoleTypeEnum.SUPPLIER_MANAGER,VendorApplyType.ORG_SUPPLIER_ENABLED_REVIEW,"1");
			sendMail.checkApply("1",RoleTypeEnum.SUPPLIER_MANAGER,VendorApplyType.ORG_SUPPLIER_INVALID_REVIEW,"1");
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		
	}
}
