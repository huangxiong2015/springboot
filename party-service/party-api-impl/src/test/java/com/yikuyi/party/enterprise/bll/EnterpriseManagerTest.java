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
package com.yikuyi.party.enterprise.bll;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.model.Party.PartyStatus;
import com.yikuyi.pay.PayClientBuilder;
import com.yikuyi.pay.api.base.ResponseMsg;
import com.yikuyi.pay.resource.CouponResource;
import com.yikuyi.workflow.Apply;
import com.yikuyi.workflow.Apply.ApplyStatus;
import com.yikuyi.workflow.WorkflowClientBuilder;
import com.yikuyi.workflow.resource.ApplyClient;
import com.ykyframework.exception.BusinessException;
@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class,MockitoTestExecutionListener.class })
@Transactional
@Rollback
public class EnterpriseManagerTest {
	@Autowired
	private EnterpriseManager enterpriseManager;

	
	@SpyBean
	private PayClientBuilder payClientBuilder;
	
	@SpyBean
	private AuthorizationUtil authorizationUtil;
	
	@SpyBean
	private WorkflowClientBuilder workflowClientBuilder;
	
	private MockHttpServletRequest request; 
    private MockHttpServletResponse response; 
	

	public EnterpriseManagerTest(){
		MockitoAnnotations.initMocks(this);
	}
    
	@Before
	public void init() {
		LoginUser loginUser = new LoginUser("9999999901", "admin", "9999999901", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));		
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));;
		RequestContextHolder.currentRequestAttributes().setAttribute(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser, RequestAttributes.SCOPE_REQUEST);
		request = new MockHttpServletRequest();    
	    request.setCharacterEncoding("UTF-8");    
	    response = new MockHttpServletResponse();    
	}
	
	/**
	 * 根据账号查询管理员信息
	 * 
	 * @param id
	 * @param role
	 * @return
	 * @since 2017年1月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "enterprise_sampledata.xml")
	public void testGetAdmin(){
		UserVo userVo = enterpriseManager.getAdmin("9999999902", "admin");
		assertEquals("123456@yikuyi.com",userVo.getMail());
		assertEquals("易库易",userVo.getCompanyName());
		assertEquals("admin",userVo.getName());
	}
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "enterprise_sampledata.xml")
	public void testGetUser(){
		UserVo userVo = enterpriseManager.getUser("55555555");
		assertEquals("12345@qq.com",userVo.getMail());
	}
	/**
	 * 根据id更新状态
	 * 
	 * @param id
	 * @param status
	 * @return
	 * @since 2017年1月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "enterprise_sampledata1.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "enterprise_update_data.xml")
	public void testUpdateStatus(){
		Party party = new Party();
		party.setId("9999999901");
		party.setPartyStatus(PartyStatus.PARTY_DISABLED);
		enterpriseManager.updateStatus(party);
	}
	
	/**
	 * 修改企业信息
	 * 
	 * @param id
	 * @param status
	 * @return
	 * @since 2017年1月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"party_edit_result.xml"})
	public void testEditCompany() throws IOException{
		EnterpriseVo entVo = new EnterpriseVo();
		entVo.setId("88888");
		entVo.setName("myHome");
		entVo.setLogo("http://www.baidu.com");
		entVo.setdCode("dCode");
		entVo.setCorCategory("CORPORATION_CATEGORY_ID");
		entVo.setIndustryCategory("INDUSTRY_CATEGORY_ID");
		entVo.setWebSite("WebSite");
		entVo.setDescription("PARTY_BRIEF");
		
		entVo.setAddress("中国深圳市南山区高新技术产业园麻雀岭工业区M-8栋4楼");
		entVo.setProvince("10000");
		entVo.setProvinceName("广东省");
		entVo.setCity("10001");
		entVo.setCityName("深圳市");
		entVo.setCountry("10002");
		entVo.setCountryName("南山区");
		entVo.setMail("12345@qq.com");
		
		entVo.setContactUserName("叶良辰");
		//entVo.setContactUserQQ("1249720810");
		List<Map<String, String>> otherAttrs = new ArrayList<>();
		Map<String, String> map = new HashMap<>();
		map.put("INDUSTRY_CATEGORY_ID_OTHER", "这是我的other");
		otherAttrs.add(map);
		entVo.setContactUserTel("13066939619");
		entVo.setOtherAttrs(otherAttrs);
		enterpriseManager.editCompany(entVo);;
	} 

	
	/**
	 * 修改vip企业信息
	 * 
	 * @param id
	 * @param status
	 * @return
	 * @since 2017年1月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"party_edit_result.xml"})
	public void testUpdateQualifications() throws IOException{
		EnterpriseVo entVo = new EnterpriseVo();
		entVo.setId("88888");
		entVo.setName("myHome");
		entVo.setLogo("http://www.baidu.com");
		entVo.setdCode("dCode");
		entVo.setCorCategory("CORPORATION_CATEGORY_ID");
		entVo.setIndustryCategory("INDUSTRY_CATEGORY_ID");
		entVo.setWebSite("WebSite");
		entVo.setDescription("PARTY_BRIEF");
		
		entVo.setAddress("中国深圳市南山区高新技术产业园麻雀岭工业区M-8栋4楼");
		entVo.setProvince("10000");
		entVo.setProvinceName("广东省");
		entVo.setCity("10001");
		entVo.setCityName("深圳市");
		entVo.setCountry("10002");
		entVo.setCountryName("南山区");
		entVo.setMail("12345@qq.com");
		
		entVo.setContactUserName("叶良辰");
		//entVo.setContactUserQQ("1249720810");
		List<Map<String, String>> otherAttrs = new ArrayList<>();
		Map<String, String> map = new HashMap<>();
		map.put("INDUSTRY_CATEGORY_ID_OTHER", "这是我的other");
		otherAttrs.add(map);
		entVo.setContactUserTel("13066939619");
		entVo.setOtherAttrs(otherAttrs);
		enterpriseManager.updateQualifications(entVo);
	} 
	/**
	 * 根据用户的id判断是否为管理员
	 * 
	 * @param id
	 * @return
	 * @since 2017年2月7日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "enterprise_sampledata.xml")
//	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "userlogin_update_data.xml")
	public void testIsAdmin(){
		Boolean falg = enterpriseManager.isAdmin("9999999901");
		assertEquals(true,falg);
	}
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "enterprise_sampledata.xml")
//	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "userlogin_update_data.xml")
	public void testIsFristActive(){
		Boolean falg = enterpriseManager.isFristActive("9999999901");
		assertEquals(false,falg);
	}
	
	/**
	 * 根据用户的id判断是否为激活或者关联，返回true：已经激活或者关联，fail：未激活或者未关联
	 * 
	 * @param id
	 * @return
	 * @since 2017年1月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "enterprise_sampledata.xml")
//	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "userlogin_update_data.xml")
	public void testIsActivedOrRelationed(){
		String falg = enterpriseManager.isActivedOrRelationed("9999999901");
		assertEquals("actived",falg);
	}

	
	/**
	 * 修改公司信息
	 * 
	 * @param id
	 * @return EnterpriseVo
	 * @since 2016年1月19日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"party_edit_result.xml"})
	public void testUpdateParty() throws IOException{
		EnterpriseVo entVo = new EnterpriseVo();
		entVo.setId("88888");
		entVo.setName("myHome");
		entVo.setLogo("http://www.baidu.com");
		entVo.setdCode("dCode");
		entVo.setCorCategory("CORPORATION_CATEGORY_ID");
		entVo.setIndustryCategory("INDUSTRY_CATEGORY_ID");
		entVo.setWebSite("WebSite");
		entVo.setDescription("PARTY_BRIEF");
		
		entVo.setAddress("中国深圳市南山区高新技术产业园麻雀岭工业区M-8栋4楼");
		entVo.setProvince("10000");
		entVo.setProvinceName("广东省");
		entVo.setCity("10001");
		entVo.setCityName("深圳市");
		entVo.setCountry("10002");
		entVo.setCountryName("南山区");
		entVo.setMail("12345@qq.com");
		
		entVo.setContactUserName("叶良辰");
		//entVo.setContactUserQQ("1249720810");
		List<Map<String, String>> otherAttrs = new ArrayList<>();
		Map<String, String> map = new HashMap<>();
		map.put("INDUSTRY_CATEGORY_ID_OTHER", "这是我的other");
		otherAttrs.add(map);
		entVo.setContactUserTel("13066939619");
		entVo.setOtherAttrs(otherAttrs);
		//entVo.setFax("0755-123456");
		enterpriseManager.updateParty(entVo, 0);
	} 

	/**
	 * 账户激活审核成功保存数据
	 * 
	 * @param id
	 * @return EnterpriseVo
	 * @since 2016年1月19日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"party_add_result.xml"})
	public void testSave() {
		Apply apply = new Apply();
		//審核通過
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
		ResponseMsg msg = null;
		CouponResource mockApplyResource = Mockito.mock(CouponResource.class);
		Mockito.when(payClientBuilder.couponResource()).thenReturn(mockApplyResource);
		Mockito.when(mockApplyResource.regOrUpSendCoupon("APPROVE_COMPANY", "9999999902", authorizationUtil.getLoginAuthorization())).thenReturn(msg);
			
		enterpriseManager.save(apply);
		
		
		Apply apply1 = new Apply();
		//審核駁回
		String applyContation1 ="{'id':'9999999901','address':'中国深圳市南山区高新技术产业园麻雀岭工业区M-8栋4楼',"
				+ "'name':'myHome','logo':'http://www.baidu.com','province':'10000',"
				+ "'provinceName':'广东省','city':'10001','cityName':'深圳市','country':'10002','countryName':'南山区','mail':'12345@qq.com','contactUserName':'叶良辰','contactUserTel':'13066939619',"
				+ "'corCategory':'CORPORATION_CATEGORY_ID',"
				+ "'industryCategory':'INDUSTRY_CATEGORY_ID','webSite':'WebSite','description':'PARTY_BRIEF','status':'REJECT','otherAttrs':[{'INDUSTRY_CATEGORY_ID_OTHER':'测试key'}]}";
		apply1.setApplyId("111");
		apply1.setApplyOrgId("9999999901");
		apply1.setApplyUserId("9999999901");
		apply1.setStatus(ApplyStatus.REJECT);
		apply1.setApplyContent(applyContation1);
		enterpriseManager.save(apply1);
		
		Apply apply2 = new Apply();
		//審核駁回
		String applyContation2 ="{'id':'9999999901','address':'中国深圳市南山区高新技术产业园麻雀岭工业区M-8栋4楼',"
				+ "'name':'易库易','logo':'http://www.baidu.com','province':'10000',"
				+ "'provinceName':'广东省','city':'10001','cityName':'深圳市','country':'10002','countryName':'南山区','mail':'12345@qq.com','contactUserName':'叶良辰','contactUserTel':'13066939619',"
				+ "'corCategory':'CORPORATION_CATEGORY_ID',"
				+ "'industryCategory':'INDUSTRY_CATEGORY_ID','webSite':'WebSite','description':'PARTY_BRIEF','status':'REJECT','otherAttrs':[{'INDUSTRY_CATEGORY_ID_OTHER':'测试key'}]}";
		apply2.setApplyId("111");
		apply2.setApplyOrgId("9999999901");
		apply2.setApplyUserId("9999999901");
		apply2.setStatus(ApplyStatus.APPROVED);
		apply2.setApplyContent(applyContation2);
		ResponseMsg msg1 = null;
		CouponResource mockApplyResource1 = Mockito.mock(CouponResource.class);
		Mockito.when(payClientBuilder.couponResource()).thenReturn(mockApplyResource1);
		Mockito.when(mockApplyResource1.regOrUpSendCoupon("UP_COMPANY", "9999999901", authorizationUtil.getLoginAuthorization())).thenReturn(msg1);
		
		enterpriseManager.save(apply2);
		
		
	}

	/**
	 * 账户激活审核成功保存数据
	 * 
	 * @param id
	 * @return EnterpriseVo
	 * @since 2016年1月19日
	 * @author zr.helinmei@yikuyi.com
	 * @throws BusinessException 
	 * @throws IOException
	 */
	@Test
	@Rollback
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"party_add_result.xml"})
	public void testActiveAccountSave() {
		
		String applyContation ="{'id':'9999999901','address':'中国深圳市南山区高新技术产业园麻雀岭工业区M-8栋4楼',"
				+ "'name':'myHome','logo':'http://www.baidu.com','province':'10000',"
				+ "'provinceName':'广东省','city':'10001','cityName':'深圳市','country':'10002','countryName':'南山区','mail':'12345@qq.com','contactUserName':'叶良辰','contactUserTel':'13066939619',"
				+ "'corCategory':'CORPORATION_CATEGORY_ID',"
				+ "'industryCategory':'INDUSTRY_CATEGORY_ID','otherAttrs': [ {'INDUSTRY_CATEGORY_ID_OTHER':'11111'} ],'webSite':'WebSite','description':'PARTY_BRIEF'}";
		Apply apply = new Apply();
		apply.setApplyContent(applyContation);
		apply.setApplyId("111");
		apply.setApplyUserId("9999999901");
		apply.setApplyOrgId("9999999901");
		String id="1";
		ApplyClient applyClient =Mockito.mock(ApplyClient.class);
		Mockito.when(workflowClientBuilder.applyClient()).thenReturn(applyClient);
		Mockito.when(applyClient.createApply(apply, authorizationUtil.getLoginAuthorization())).thenReturn(id);
		try {
			enterpriseManager.activeAccountSave(apply);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		
		//个人升级企业
		String applyContationPerson ="{'id':'666666','address':'中国深圳市南山区高新技术产业园麻雀岭工业区M-8栋4楼',"
				+ "'name':'myHome','logo':'http://www.baidu.com','province':'10000',"
				+ "'provinceName':'广东省','city':'10001','cityName':'深圳市','country':'10002','countryName':'南山区','mail':'12345@qq.com','contactUserName':'叶良辰','contactUserTel':'13066939619',"
				+ "'CORPORATION_CATEGORY_ID':'CORPORATION_CATEGORY_ID',"
				+ "'INDUSTRY_CATEGORY_ID':'INDUSTRY_CATEGORY_ID','otherAttrs': [ {'INDUSTRY_CATEGORY_ID_OTHER':'11111'} ],'webSite':'WebSite','description':'PARTY_BRIEF'}";
		Apply applyPerson = new Apply();
		applyPerson.setApplyContent(applyContationPerson);
		applyPerson.setApplyId("111");
		applyPerson.setApplyUserId("666666");
		applyPerson.setApplyOrgId("666666");
		String id1="1";
		ApplyClient applyClient1 =Mockito.mock(ApplyClient.class);
		Mockito.when(workflowClientBuilder.applyClient()).thenReturn(applyClient1);
		Mockito.when(applyClient1.createApply(applyPerson, authorizationUtil.getLoginAuthorization())).thenReturn(id1);
		try {
			enterpriseManager.activeAccountSave(applyPerson);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetImagUrl(){
		enterpriseManager.getImagUrl("http://ictrade-private-hz-uat.img-cn-hangzhou.aliyuncs.com/dev/ent/certificates/201702/16/4aa157b778a70ce9d0c8bddd377767c9.jpg?Expires=1487242582&OSSAccessKeyId=STS.BzgkDB5JgDPYFZHxtLhfy8fBX&Signature=G2jbTNx5cypBp/qDbvao83AT53o%3D&security-token=CAISkQJ1q6Ft5B2yfSjIo7nSIP722JVG85KyRHz5nHQZZOlV16Ppujz2IHpMenBhA%2B4avv83n2lT6/salol0UIQATEfPYNBrq49a6higZIyE4pDlsuZY0cH7RjTJUkZVPnwPr6arIunGc9KBNnrm9EYqs5aYGBymW1u6S%2B7r7bdsctUQWCShcDNCH604DwB%2BqcgcRxCzXLTXRXyMuGfLC1dysQdRkH527b/FoveR8R3Dllb3uIR3zsbTWsH8Mpk9YMYnDI3ug7EnLfH7vXQOu0QQxsBfl7dZ/DrLhNaZDmRK7g%2BOW%2BiuqYc3clYoPPZmS/Uc96GnyKAgoJbOnpiyzA1WNOpeXj/EAZqnxMbUjStshAKy7d8agAFrEGfVV7pgVRMhSfcgziCyyYUhJ6Wei6fKIivFoJ1cCpHp7mpAXd9a36iXVNpXA7FAqd0pKi3x4xgOA%2B9vr7PXRIDcgybadttFyXeLRAfCSDgTpf1MsCThTuRBxfnWWPRv4u/YK8hUjNigoOZeWj3KvJDt2kauvbKhNyIiiGypnw%3D%3D");
		
		enterpriseManager.getImagUrl("http://ictrade-private-hz-uat.img-cn-hangzhou.aliyuncs.com/dev/ent/certificates/201702/16/4aa157b778a70ce9d0c8bddd377767c9.pdf?Expires=1487242582&OSSAccessKeyId=STS.BzgkDB5JgDPYFZHxtLhfy8fBX&Signature=G2jbTNx5cypBp/qDbvao83AT53o%3D&security-token=CAISkQJ1q6Ft5B2yfSjIo7nSIP722JVG85KyRHz5nHQZZOlV16Ppujz2IHpMenBhA%2B4avv83n2lT6/salol0UIQATEfPYNBrq49a6higZIyE4pDlsuZY0cH7RjTJUkZVPnwPr6arIunGc9KBNnrm9EYqs5aYGBymW1u6S%2B7r7bdsctUQWCShcDNCH604DwB%2BqcgcRxCzXLTXRXyMuGfLC1dysQdRkH527b/FoveR8R3Dllb3uIR3zsbTWsH8Mpk9YMYnDI3ug7EnLfH7vXQOu0QQxsBfl7dZ/DrLhNaZDmRK7g%2BOW%2BiuqYc3clYoPPZmS/Uc96GnyKAgoJbOnpiyzA1WNOpeXj/EAZqnxMbUjStshAKy7d8agAFrEGfVV7pgVRMhSfcgziCyyYUhJ6Wei6fKIivFoJ1cCpHp7mpAXd9a36iXVNpXA7FAqd0pKi3x4xgOA%2B9vr7PXRIDcgybadttFyXeLRAfCSDgTpf1MsCThTuRBxfnWWPRv4u/YK8hUjNigoOZeWj3KvJDt2kauvbKhNyIiiGypnw%3D%3D");
	}
	
	/**
	 * 后台企业会员管理修改审核
	 * 
	 * @param id
	 * @return EnterpriseVo
	 * @since 2016年4月14日
	 * @author zr.helinmei@yikuyi.com
	 * @throws UnsupportedEncodingException 
	 * @throws IOException
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"party_add_result.xml"})
	public void testEditEntApply() throws UnsupportedEncodingException{
		String applyContation ="{'id':'9999999901','address':'中国深圳市南山区高新技术产业园麻雀岭工业区M-8栋4楼',"
				+ "'name':'myHome','logo':'http://www.baidu.com','province':'10000',"
				+ "'provinceName':'广东省','city':'10001','cityName':'深圳市','country':'10002','countryName':'南山区','mail':'12345@qq.com','contactUserName':'叶良辰','contactUserTel':'13066939619',"
				+ "'corCategory':'CORPORATION_CATEGORY_ID',"
				+ "'industryCategory':'INDUSTRY_CATEGORY_ID','webSite':'WebSite','description':'PARTY_BRIEF'}";
		Apply apply = new Apply();
		apply.setApplyContent(applyContation);
		apply.setApplyId("111");
		apply.setApplyUserId("9999999901");
		apply.setApplyOrgId("9999999901");
		
		String id="1";
		ApplyClient applyClient =Mockito.mock(ApplyClient.class);
		Mockito.when(workflowClientBuilder.applyClient()).thenReturn(applyClient);
		Mockito.when(applyClient.createApply(apply, authorizationUtil.getLoginAuthorization())).thenReturn(id);

		enterpriseManager.editEntApply(apply);
		
		String applyContationPerson ="{'id':'666666','entUserId':'6666669','address':'中国深圳市南山区高新技术产业园麻雀岭工业区M-8栋4楼',"
				+ "'name':'myHome','logo':'http://www.baidu.com','province':'10000',"
				+ "'provinceName':'广东省','city':'10001','cityName':'深圳市','country':'10002','countryName':'南山区','mail':'12345@qq.com','contactUserName':'叶良辰','contactUserTel':'13066939619',"
				+ "'CORPORATION_CATEGORY_ID':'CORPORATION_CATEGORY_ID',"
				+ "'INDUSTRY_CATEGORY_ID':'INDUSTRY_CATEGORY_ID','otherAttrs': [ {'INDUSTRY_CATEGORY_ID_OTHER':'11111'} ],'webSite':'WebSite','description':'PARTY_BRIEF'}";
		Apply applyPerson = new Apply();
		applyPerson.setApplyContent(applyContationPerson);
		applyPerson.setApplyId("111");
		applyPerson.setApplyUserId("666666");
		applyPerson.setApplyOrgId("666666");
		String id1="1";
		ApplyClient applyClient1 =Mockito.mock(ApplyClient.class);
		Mockito.when(workflowClientBuilder.applyClient()).thenReturn(applyClient1);
		Mockito.when(applyClient1.createApply(applyPerson, authorizationUtil.getLoginAuthorization())).thenReturn(id1);

		enterpriseManager.editEntApply(applyPerson);
		
	}
	
	
	/**
	 * 账户激活后企业修改审核成功保存数据
	 * 
	 * @param id
	 * @return EnterpriseVo
	 * @since 2017年4月14日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"party_add_result.xml"})
	public void testEditEntApplySave() {
		//審核通過
		String applyContation ="{'id':'9999999901','address':'中国深圳市南山区高新技术产业园麻雀岭工业区M-8栋4楼',"
				+ "'name':'myHome','logo':'http://www.baidu.com','province':'10000',"
				+ "'provinceName':'广东省','city':'10001','cityName':'深圳市','country':'10002','countryName':'南山区','mail':'12345@qq.com','contactUserName':'叶良辰','contactUserTel':'13066939619',"
				+ "'corCategory':'CORPORATION_CATEGORY_ID',"
				+ "'industryCategory':'INDUSTRY_CATEGORY_ID','webSite':'WebSite','description':'PARTY_BRIEF','otherAttrs':[{'INDUSTRY_CATEGORY_ID_OTHER':'测试key'}]}";
		Apply apply = new Apply();
		apply.setApplyId("111");
		apply.setApplyUserId("9999999901");
		apply.setApplyOrgId("9999999901");
		apply.setApplyContent(applyContation);
		enterpriseManager.editEntApplySave(apply);
		
		ResponseMsg msg = null;
		CouponResource mockApplyResource = Mockito.mock(CouponResource.class);
		Mockito.when(payClientBuilder.couponResource()).thenReturn(mockApplyResource);
		Mockito.when(mockApplyResource.regOrUpSendCoupon("APPROVE_COMPANY", "9999999902", authorizationUtil.getLoginAuthorization())).thenReturn(msg);
		
	
		//審核驳回
		String applyContation1 ="{'id':'9999999901','address':'中国深圳市南山区高新技术产业园麻雀岭工业区M-8栋4楼',"
				+ "'name':'myHome','logo':'http://www.baidu.com','province':'10000',"
				+ "'provinceName':'广东省','city':'10001','cityName':'深圳市','country':'10002','countryName':'南山区','mail':'12345@qq.com','contactUserName':'叶良辰','contactUserTel':'13066939619',"
				+ "'corCategory':'CORPORATION_CATEGORY_ID',"
				+ "'industryCategory':'INDUSTRY_CATEGORY_ID','webSite':'WebSite','description':'PARTY_BRIEF','otherAttrs':[{'INDUSTRY_CATEGORY_ID_OTHER':'测试key'}]}";
		Apply apply1 = new Apply();
		apply1.setApplyId("111");
		apply1.setApplyUserId("9999999901");
		apply1.setApplyOrgId("9999999901");
		apply1.setStatus(ApplyStatus.REJECT);
		apply1.setApplyContent(applyContation1);
		
		ResponseMsg msg1 = null;
		CouponResource mockApplyResource1 = Mockito.mock(CouponResource.class);
		Mockito.when(payClientBuilder.couponResource()).thenReturn(mockApplyResource);
		Mockito.when(mockApplyResource1.regOrUpSendCoupon("APPROVE_COMPANY", "9999999902", authorizationUtil.getLoginAuthorization())).thenReturn(msg1);
		
		enterpriseManager.editEntApplySave(apply1);
	}
	
	
	/**
	 * 账号审核通过保存
	 * 
	 * @param id
	 * @return EnterpriseVo
	 * @since 2017年4月14日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"party_addson_result.xml"})
	public void testEntAuthorize() {
		//審核通過
		String applyContation ="{'id':'888888555','entUserId':'1','address':'中国深圳市南山区高新技术产业园麻雀岭工业区M-8栋4楼',"
				+ "'name':'myHome','logo':'http://www.baidu.com','province':'10000',"
				+ "'provinceName':'广东省','city':'10001','cityName':'深圳市','country':'10002','countryName':'南山区','mail':'12345@qq.com','contactUserName':'叶良辰','contactUserTel':'13066939619',"
				+ "'corCategory':'CORPORATION_CATEGORY_ID',"
				+ "'industryCategory':'INDUSTRY_CATEGORY_ID','webSite':'WebSite','description':'PARTY_BRIEF','otherAttrs':[{'INDUSTRY_CATEGORY_ID_OTHER':'测试key'}],"
				+ "'map':{'LOA':'http://www.baidu.com',"
		+"'LOA_PDF_NAME':'http://www.baidu.com'}}";
		Apply apply = new Apply();
		apply.setApplyId("111");
		apply.setApplyUserId("9999999902");
		apply.setApplyOrgId("666665");
		apply.setApplyContent(applyContation);
		enterpriseManager.entAuthorize(apply);
		
	
		//審核驳回
		String applyContation1 ="{'id':'888888555','entUserId':'1','address':'中国深圳市南山区高新技术产业园麻雀岭工业区M-8栋4楼',"
				+ "'name':'myHome','logo':'http://www.baidu.com','province':'10000',"
				+ "'provinceName':'广东省','city':'10001','cityName':'深圳市','country':'10002','countryName':'南山区','mail':'12345@qq.com','contactUserName':'叶良辰','contactUserTel':'13066939619',"
				+ "'corCategory':'CORPORATION_CATEGORY_ID',"
				+ "'industryCategory':'INDUSTRY_CATEGORY_ID','webSite':'WebSite','description':'PARTY_BRIEF','otherAttrs':[{'INDUSTRY_CATEGORY_ID_OTHER':'测试key'}],"
				+ "'map':{'LOA':'http://www.baidu.com',"
		+"'LOA_PDF_NAME':'http://www.baidu.com'}}";
		Apply apply1 = new Apply();
		apply1.setApplyId("111");
		apply1.setApplyUserId("9999999902");
		apply1.setApplyOrgId("666665");
		apply1.setApplyContent(applyContation1);
		apply1.setStatus(ApplyStatus.REJECT);
		enterpriseManager.entAuthorize(apply1);
	}

	/**
	 * 子账号审核
	 * 
	 * @param id
	 * @return EnterpriseVo
	 * @since 2017年4月14日
	 * @author zr.helinmei@yikuyi.com
	 * @throws UnsupportedEncodingException 
	 * @throws IOException
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"party_addson_result.xml"})
	public void testEntApplyAuthorize() throws UnsupportedEncodingException {
		 String applyContation ="{'id':'888888555','entUserId':'1','address':'中国深圳市南山区高新技术产业园麻雀岭工业区M-8栋4楼',"
				+ "'name':'myHome','logo':'http://www.baidu.com','province':'10000',"
				+ "'provinceName':'广东省','city':'10001','cityName':'深圳市','country':'10002','countryName':'南山区','mail':'12345@qq.com','contactUserName':'叶良辰','contactUserTel':'13066939619',"
				+ "'corCategory':'CORPORATION_CATEGORY_ID',"
				+ "'industryCategory':'INDUSTRY_CATEGORY_ID','webSite':'WebSite','description':'PARTY_BRIEF','otherAttrs':[{'INDUSTRY_CATEGORY_ID_OTHER':'测试key'}],"
				+ "'map':{'LOA':'http://www.baidu.com',"
		+"'LOA_PDF_NAME':'http://www.baidu.com'}}";
		Apply apply = new Apply();
		apply.setApplyContent(applyContation);
		apply.setApplyId("111");
		apply.setApplyUserId("9999999909");
		apply.setApplyOrgId("9999999909");
		String id="1";
		ApplyClient applyClient =Mockito.mock(ApplyClient.class);
		Mockito.when(workflowClientBuilder.applyClient()).thenReturn(applyClient);
		Mockito.when(applyClient.createApply(apply, authorizationUtil.getLoginAuthorization())).thenReturn(id);

		enterpriseManager.entApplyAuthorize(apply,"1","账期申请");
		
	}
	

	/**
	 * 账户激活后企业修改审核成功保存数据
	 * 
	 * @param id
	 * @return EnterpriseVo
	 * @since 2017年4月14日
	 * @author zr.helinmei@yikuyi.com
	 * @throws UnsupportedEncodingException 
	 * @throws IOException
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"party_add_result.xml"})
	public void testInvalidAccount() throws UnsupportedEncodingException {
		enterpriseManager.invalidAccount("10086","test");
	}
	

	/**
	 * 获取企业基本信息
	 * 
	 * @param id
	 * @return EnterpriseVo
	 * @since 2017年4月14日
	 * @author zr.helinmei@yikuyi.com
	 * @throws UnsupportedEncodingException 
	 * @throws IOException
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"party_add_result.xml"})
	public void testGetEntBaseInfo() throws UnsupportedEncodingException {
		enterpriseManager.getEntBaseInfo("666666");
	}
	
	/**
	 * 修改企业信息
	 * 
	 * @param id
	 * @param status
	 * @return
	 * @since 2017年1月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"party_edit_result.xml"})
	public void testEditMemberCompany() throws IOException{
		EnterpriseVo entVo = new EnterpriseVo();
		entVo.setId("88888");
		entVo.setName("myHome");
		entVo.setLogo("http://www.baidu.com");
		entVo.setdCode("dCode");
		entVo.setCorCategory("CORPORATION_CATEGORY_ID");
		entVo.setIndustryCategory("INDUSTRY_CATEGORY_ID");
		entVo.setWebSite("WebSite");
		entVo.setDescription("PARTY_BRIEF");
		
		entVo.setAddress("中国深圳市南山区高新技术产业园麻雀岭工业区M-8栋4楼");
		entVo.setProvince("10000");
		entVo.setProvinceName("广东省");
		entVo.setCity("10001");
		entVo.setCityName("深圳市");
		entVo.setCountry("10002");
		entVo.setCountryName("南山区");
		entVo.setMail("12345@qq.com");
		
		entVo.setContactUserName("叶良辰");
		List<Map<String, String>> otherAttrs = new ArrayList<>();
		Map<String, String> map = new HashMap<>();
		map.put("INDUSTRY_CATEGORY_ID_OTHER", "这是我的other");
		otherAttrs.add(map);
		entVo.setContactUserTel("13066939619");
		entVo.setOtherAttrs(otherAttrs);
		enterpriseManager.editMemberCompany(entVo);
	} 
	
	/**
	 * 会员中心认证判断是否重复申请
	 * 
	 * @param id
	 * @return EnterpriseVo
	 * @since 2017年7月17日
	 * @author zr.helinmei@yikuyi.com
	 * @throws UnsupportedEncodingException 
	 * @throws IOException
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"party_add_result.xml"})
	public void testIsReApplyCer() throws UnsupportedEncodingException {
		try {
			enterpriseManager.isReApplyCer("666666");
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 账户激活审核成功保存数据
	 * 
	 * @param id
	 * @return EnterpriseVo
	 * @since 2016年1月19日
	 * @author zr.helinmei@yikuyi.com
	 * @throws BusinessException 
	 * @throws IOException
	 */
	@Test
	@Rollback
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"party_add_result.xml"})
	public void testUpdateCompanyName() {
		enterpriseManager.updateCompanyName("988888", "易库易");
	}
	
	/**
	 * 导出认证企业管理列表
	 * @since 2017年9月15日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@Rollback
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"party_add_result.xml"})
	public void testExportEntCertification() throws Exception{
		EnterpriseParamVo vo = new EnterpriseParamVo();
		enterpriseManager.exportEntCertification(vo, response);
	}

	
	/**
	 * 账户激活审核成功保存数据
	 * 
	 * @param id
	 * @return EnterpriseVo
	 * @since 2017年1月19日
	 * @author zr.helinmei@yikuyi.com
	 * @throws BusinessException 
	 * @throws IOException
	 */
	@Test
	@Rollback
	public void testExportEnt() throws IOException {
		EnterpriseParamVo vo = new EnterpriseParamVo();
		enterpriseManager.exportEnt(vo,response);
	}
	
	
	@Test
	@Rollback
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {"expired_entAttr_sample.xml"})
	public void testEnterpriseDocumentsExpiredJob(){
		enterpriseManager.enterpriseDocumentsExpiredJob();
	}

}