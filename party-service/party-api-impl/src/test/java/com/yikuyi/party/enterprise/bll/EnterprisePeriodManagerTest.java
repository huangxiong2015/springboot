package com.yikuyi.party.enterprise.bll;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.junit.Assert;
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
import com.framework.springboot.utils.AuthorizationUtil;
import com.framewrok.springboot.web.LoginUserInjectionInterceptor;
import com.github.pagehelper.PageInfo;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.yikuyi.party.credit.vo.PartyCreditParamVo;
import com.yikuyi.party.credit.vo.PartyCreditVo;
import com.yikuyi.workflow.Apply;
import com.yikuyi.workflow.Apply.ApplyStatus;
import com.yikuyi.workflow.WorkflowClientBuilder;
import com.yikuyi.workflow.resource.ApplyClient;


@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class ,MockitoTestExecutionListener.class})
@Transactional
@Rollback
public class EnterprisePeriodManagerTest {
	@Autowired
	private EnterprisePeriodManager enterprisePeriodManager;
	@SpyBean
	private WorkflowClientBuilder workflowClientBuilder;
	@SpyBean
	private AuthorizationUtil authorizationUtil;
	
	public EnterprisePeriodManagerTest(){
		MockitoAnnotations.initMocks(this);
	}
	@Before
	public void init() {
		LoginUser loginUser = new LoginUser("9999999901", "admin", "9999999901", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));		
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));;
		RequestContextHolder.currentRequestAttributes().setAttribute(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser, RequestAttributes.SCOPE_REQUEST);
//		Assert.assertNotNull(RequestHelper.getLoginUserId());
	}
	/**
	 * 账期申请
	 * 
	 * @param id
	 * @return EnterpriseVo
	 * @since 2017年7月26日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException
	 */
	@Test
	@Rollback
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"accountPried_add_sample.xml"})
	public void testAccountPeriodApply()  {
		String applyContation ="{\"partyId\":\"16666688888\",\"mail\":\"applyName@qq.com\",\"name\":\"千千阙歌33444\",\"contactUserName\":\"admin\",\"checkCycle\":\"1\",\"checkDate\":\"1\",\"common\":\"申请账期测试\",\"creator\":\"9999999901\",\"creditDeadline\":\"11\",\"creditQuota\":\"33\",\"currency\":\"CNY\",\"payDate\":\"111\",\"realtimeCreditQuota\":\"11111\",\"creditAttachmentList\":[{\"attachmentName\":\"附件名称\",\"attachmentUrl\":\"附件链接url\"},{\"attachmentName\":\"附件名称test1\",\"attachmentUrl\":\"附件链接url test1\"}]}";
		Apply apply = new Apply();
		apply.setApplyContent(applyContation);
		apply.setApplyId("111");
		apply.setApplyUserId("9999999901");
		apply.setApplyOrgId("16666688888");
		String id="1";
		ApplyClient applyClient =Mockito.mock(ApplyClient.class);
		Mockito.when(workflowClientBuilder.applyClient()).thenReturn(applyClient);
		Mockito.when(applyClient.createApply(apply, authorizationUtil.getLoginAuthorization())).thenReturn(id);
		enterprisePeriodManager.accountPeriodApply(apply,"1","账期申请记录描述");
		
	/*	String applyContation1 ="";
		Apply apply1 = new Apply();
		apply1.setApplyContent(applyContation1);
		apply1.setApplyId("111");
		apply1.setApplyUserId("9999999901");
		apply1.setApplyOrgId("16666688888");
		String id1="1";
		ApplyClient applyClient1 =Mockito.mock(ApplyClient.class);
		Mockito.when(workflowClientBuilder.applyClient()).thenReturn(applyClient1);
		Mockito.when(applyClient1.createApply(apply1, authorizationUtil.getLoginAuthorization())).thenReturn(id1);
		enterprisePeriodManager.accountPeriodApply(apply1,"1","账期申请记录描述");
	*/
	}
	/**
	 * 账期审核
	 * 
	 * @param id
	 * @return EnterpriseVo
	 * @since 2017年7月26日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException
	 */
	@Test
	@Rollback
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"accountPried_add_sample.xml"})
	public void testAccountPeriodAudit()  {
		String applyContation ="{\"partyId\":\"16666688888\",\"mail\":\"applyName@qq.com\",\"name\":\"千千阙歌33444\",\"contactUserName\":\"admin\",\"checkCycle\":\"1\",\"checkDate\":\"1\",\"common\":\"申请账期测试\",\"creator\":\"9999999901\",\"creditDeadline\":\"11\",\"creditQuota\":\"33\",\"currency\":\"CNY\",\"payDate\":\"111\",\"realtimeCreditQuota\":\"11111\",\"creditAttachmentList\":[{\"attachmentName\":\"附件名称\",\"attachmentUrl\":\"附件链接url\"},{\"attachmentName\":\"附件名称test1\",\"attachmentUrl\":\"附件链接url test1\"}]}";
		Apply apply = new Apply();
		apply.setApplyContent(applyContation);
		apply.setApplyId("111");
		apply.setApplyUserId("9999999901");
		apply.setApplyOrgId("16666688888");
		apply.setStatus(ApplyStatus.APPROVED);
		enterprisePeriodManager.accountPeriodAudit(apply);
		
		String applyContation1 ="{\"partyId\":\"16666688888\",\"mail\":\"applyName@qq.com\",\"name\":\"千千阙歌33444\",\"contactUserName\":\"admin\",\"checkCycle\":\"1\",\"checkDate\":\"1\",\"common\":\"申请账期测试\",\"creator\":\"9999999901\",\"creditDeadline\":\"11\",\"creditQuota\":\"33\",\"currency\":\"CNY\",\"payDate\":\"111\",\"realtimeCreditQuota\":\"11111\",\"creditAttachmentList\":[{\"attachmentName\":\"附件名称\",\"attachmentUrl\":\"附件链接url\"},{\"attachmentName\":\"附件名称test1\",\"attachmentUrl\":\"附件链接url test1\"}]}";
		Apply apply1 = new Apply();
		apply1.setApplyContent(applyContation1);
		apply1.setApplyId("111");
		apply1.setApplyUserId("9999999901");
		apply1.setApplyOrgId("16666688888");
		apply1.setStatus(ApplyStatus.REJECT);
		enterprisePeriodManager.accountPeriodAudit(apply1);
	}
	/**
	 * 冻结账期
	 * @param partyId
	 * @param status
	 * @return
	 * @since 2017年8月9日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	@Rollback
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"accountPried_add_sample.xml"})
	public void testUpdateCreditStatus()  {
		enterprisePeriodManager.updateCreditStatus("16666688888","PERIOD_DISABLED");
		
	}
	
	
	/**
	 * 根据企业id查询用户的账期
	 * @since 2017年8月7日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"classpath:com/yikuyi/party/enterprise/bll/party_credit_vo.xml"})
	public void testGetPartyCreditVoByCorporationId(){
		PartyCreditVo partyCredit = enterprisePeriodManager.getPartyCreditVoByCorporationId("123456789");
		/*if(partyCredit!=null){
			Assert.assertEquals(partyCredit.getCreator(), "9999999901");
		}*/
		
	}
	
	/**
	 * 
	 * 账期订单查询
	 * @since 2017年8月9日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = { "classpath:com/yikuyi/party/enterprise/bll/party_credit_vo.xml" })
	public void testGetPartyCreditVoList() {
		PageInfo<PartyCreditVo> pageInfo = enterprisePeriodManager.getPartyCreditVoList(new PartyCreditParamVo(), RowBounds.DEFAULT);
		assertEquals(pageInfo.getList().get(0).getGroupName(),"千千阙歌33444");
	}
	
	/**
	 * 根据企业id查询partyIds
	 * @since 2017年9月8日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@Rollback
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"partyrelactionship_sampledata.xml"})
	public void testGetPartyRelationShipList() {
		List<String> list = enterprisePeriodManager.getPartyIdList("16666688888");
		Assert.assertTrue(list.size()>0);
	}
	
	
	
}