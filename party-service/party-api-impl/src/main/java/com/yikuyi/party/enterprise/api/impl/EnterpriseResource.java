/*
 * Created: 2017年1月19日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.enterprise.api.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.framewrok.springboot.web.RequestHelper;
import com.github.pagehelper.PageInfo;
import com.yikuyi.party.contact.vo.EnterpriseParamVo;
import com.yikuyi.party.contact.vo.EnterpriseVo;
import com.yikuyi.party.contact.vo.UserExtendVo;
import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.party.credit.vo.PartyCreditParamVo;
import com.yikuyi.party.credit.vo.PartyCreditVo;
import com.yikuyi.party.enterprise.api.IEnterpriseResource;
import com.yikuyi.party.enterprise.bll.EnterpriseManager;
import com.yikuyi.party.enterprise.bll.EnterprisePeriodManager;
import com.yikuyi.party.enterprise.bll.EnterpriseSearchManager;
import com.yikuyi.party.group.model.PartyGroup;
import com.yikuyi.party.group.model.PartyGroup.AccountPeriodStatus;
import com.yikuyi.party.group.model.PartyGroup.AccountStatus;
import com.yikuyi.party.group.model.PartyGroup.ActiveStatus;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.model.Party.PartyStatus;
import com.yikuyi.party.model.Party.PartyType;
import com.yikuyi.party.person.model.Person;
import com.yikuyi.party.person.model.Person.PersonTypeStatus;
import com.yikuyi.workflow.Apply;
import com.ykyframework.exception.BusinessException;


/**
 * 企业信息类
 * 
 * @author helinmei
 *
 */


@RestController
@RequestMapping("v1/enterprises")
public  class EnterpriseResource implements IEnterpriseResource{
	

	
	@Autowired
	private EnterpriseManager enterpriseManager;
	
	@Autowired
	private EnterpriseSearchManager entSearchManager;
	
	@Autowired
	private EnterprisePeriodManager enterprisePeriodManager;
	
	
	/**
	 * 获取企业详情
	 * @param id
	 * @return EnterpriseVo
	 * @since 2016年1月19日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException 
	 */
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public EnterpriseVo getEnterprise(@PathVariable(value = "id")String id){
		return entSearchManager.getPartyDetail(id);
	}
	/**
	 * 获取企业基本信息
	 * @param partyId
	 * @return
	 * @since 2017年5月3日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/baseInfo", method = RequestMethod.GET)
	public PartyGroup getEntBaseInfo(@RequestParam(value = "partyId" , required = false)String partyId){
		String newPartyId = partyId;
		if(StringUtils.isEmpty(newPartyId)){
			newPartyId = RequestHelper.getLoginUserId();
		}
		return enterpriseManager.getEntBaseInfo(newPartyId);
	}
	
	/**
	 * 获取企业信息详情
	 * @param partyId
	 * @return
	 * @since 2017年5月5日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/entDetail/{id}/{corporationId}/{partyType}", method = RequestMethod.GET)
	public EnterpriseVo getEnterpriseDetailByEntId(@PathVariable(value = "id")String id,@PathVariable(value = "corporationId")String corporationId,@PathVariable(value = "partyType")PartyType partyType) {
		return entSearchManager.getPartyDetailByEntId(corporationId, new EnterpriseVo(), partyType);
	}

	
	/**
	 * 保存apple
	 */
	@Override
	@RequestMapping(method = RequestMethod.POST)
	public void save(@RequestBody Apply apply) {
		enterpriseManager.save(apply);
	}

	/**
	 * 导出企业会员列表
	 * @param vo
	 * @return
	 * @since 2017年4月13日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/excel",method = RequestMethod.GET)
	public void exportEnt(@RequestParam(value = "verifyStatus" , required = false)ActiveStatus verifyStatus, 
			@RequestParam(value = "registerStart" , required = false)String registerStart, 
			@RequestParam(value = "registerEnd" , required = false)String registerEnd, 
			HttpServletResponse response) throws IOException {
		
		EnterpriseParamVo param = new EnterpriseParamVo();
		param.setVerifyStatus(verifyStatus);
		param.setRegisterStart(registerStart);
		param.setRegisterEnd(registerEnd);
		enterpriseManager.exportEnt(param,response);
	}
	
	/**
	 * 查询后台企业用户管理列表
	 * @param mail
	 * @param name
	 * @param vaildStatus
	 * @param status
	 * @param registerStart
	 * @param registerEnd
	 * @param lastLoginStart
	 * @param lastLoginEnd
	 * @param page
	 * @param size
	 * @return
	 * @since 2017年5月5日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(method = RequestMethod.GET)
	public PageInfo<EnterpriseVo> enterpriseList(@RequestParam(value = "mail" , required = false)String mail, 
			@RequestParam(value = "contactUserName" , required = false)String contactUserName, 
			@RequestParam(value = "contactUserTel" , required = false)String contactUserTel, 
			@RequestParam(value = "name" , required = false)String name, 
			@RequestParam(value = "status" , required = false)PartyStatus status,
			@RequestParam(value = "verifyStatus" , required = false)ActiveStatus verifyStatus,
			@RequestParam(value = "accountsStatus" , required = false)PersonTypeStatus accountStatus,
			@RequestParam(value = "registerStart" , required = false)String registerStart, 
			@RequestParam(value = "registerEnd" , required = false)String registerEnd, 
			@RequestParam(value = "lastLoginStart" , required = false)String lastLoginStart, 
			@RequestParam(value = "lastLoginEnd" , required = false)String lastLoginEnd, 
			@RequestParam(value = "page" , defaultValue = "1")int page, 
			@RequestParam(value = "size" , defaultValue = "10")int size) {
		EnterpriseParamVo param = new EnterpriseParamVo();
		param.setMail(mail);
		param.setName(name);
		param.setContactUserName(contactUserName);
		param.setContactUserTel(contactUserTel);
		param.setStatus(status);
		param.setVerifyStatus(verifyStatus);
		param.setAccountsStatus(accountStatus);
		param.setRegisterStart(registerStart);
		param.setRegisterEnd(registerEnd);
		param.setLastLoginStart(lastLoginStart);
		param.setLastLoginEnd(lastLoginEnd);
		RowBounds rowBounds = new RowBounds((page-1)*size, size);
		return entSearchManager.getAccountApplyList(param, rowBounds);
		
	}

	
	/**
	 * 查询企业账号及子账号
	 */
	@Override
	@RequestMapping(value="{id}/accounts" , method = RequestMethod.GET)
	public List<UserExtendVo> enterpriseAccountsList(@PathVariable(value = "id" , required = true)String id) {
		return entSearchManager.getEnterpriseAccountsList(id);
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
	@Override
	@RequestMapping(value="/accounts/{id}",method=RequestMethod.GET)
	public UserVo getAdmin(@PathVariable("id") String id, @RequestParam(value="role",required=true) String role) {
		return enterpriseManager.getAdmin(id,role);
	}
	
	
	/**
	 * 根据用户的id判断是否为管理员
	 * 
	 * @param id
	 * @return
	 * @since 2017年1月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/accounts/admin",method=RequestMethod.GET)
	public Boolean isAdmin() {
		String id = RequestHelper.getLoginUserId();
		return enterpriseManager.isAdmin(id);
	}
	/**
	 * 根据用户的id判断是否为首次去激活或者关联
	 * 
	 * @param id
	 * @return
	 * @since 2017年1月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/accounts/isFristActive",method=RequestMethod.GET)
	public Boolean isFristActive() {
		String id = RequestHelper.getLoginUserId();
		return enterpriseManager.isFristActive(id);
	}
	
	/**
	 * 根据用户的id判断是否为激活或者关联，返回true：已经激活或者关联，fail：未激活或者未关联
	 * 
	 * @param id
	 * @return
	 * @since 2017年1月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/accounts/isActivedOrRelationed",method=RequestMethod.GET)
	public String isActivedOrRelationed() {
		String id = RequestHelper.getLoginUserId();
		return enterpriseManager.isActivedOrRelationed(id);
	}
	
	/**
	 * 根据id更新状态
	 * @param id
	 * @param status
	 * @return
	 * @since 2017年1月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/{id}",params="action=updateStatus",method=RequestMethod.PUT)
	public void updateStatus(@PathVariable("id") String id,@RequestParam(value="status",required=true)Party.PartyStatus status) {
		Party party = new Party();
		party.setId(id);
		party.setPartyStatus(status);
		UserVo userVo = enterpriseManager.getUser(id);
		if(userVo != null){
			Person p = new Person();
			p.setMail(userVo.getMail());
			party.setPerson(p);
		}
		enterpriseManager.updateStatus(party);
	}

	/**
	 * 修改公司信息
	 * @param EnterpriseVo
	 * @return
	 * @since 2017年1月20日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/editCompany",method = RequestMethod.POST)
	public void editCompany(@RequestBody EnterpriseVo enterpriseVo) {
		enterpriseManager.editCompany(enterpriseVo);
	}
	


	/**
	 * 修改公司信息
	 * @param EnterpriseVo
	 * @return
	 * @since 2017年1月20日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/activeAccountSave",method = RequestMethod.POST)
	public void activeAccountSave(@RequestBody Apply apply) throws BusinessException{
		 enterpriseManager.activeAccountSave(apply);
	}


	/**
	 * 获取资质私有图片地址
	 * @param id
	 * @return EnterpriseVo
	 * @since 2016年1月19日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException 
	 */
	@Override
	@RequestMapping(value = "/getImgUrl", method = RequestMethod.POST)
	public String getImgUrl(@RequestBody JSONObject jsonObject) {
		String url=jsonObject.getString("id");
		return enterpriseManager.getImagUrl(url);
	}

	/**
	 * 后台企业管理修改公司信息
	 * @param EnterpriseVo
	 * @return
	 * @since 2017年4月13日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/editEntApply",method = RequestMethod.POST)
	public void editEntApply(@RequestBody Apply apply) {
		 enterpriseManager.editEntApply(apply);
	}
	/**
	 * 后台管理企业账户修改审核成功调用方法
	 * @param EnterpriseVo
	 * @return
	 * @since 2017年4月13日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/editEntApplySave",method = RequestMethod.POST)
	public void editEntApplySave(@RequestBody Apply apply) {
		enterpriseManager.editEntApplySave(apply);
	}

	/**
	 * 企业授权委托书审核接口
	 * 
	 * @param id
	 * @return EnterpriseVo
	 * @since 2017年5月2日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException  
	 */
	@Override
	@RequestMapping(value="/entAuthorize",method = RequestMethod.POST)
	public void entAuthorize(@RequestBody Apply apply) {
		enterpriseManager.entAuthorize(apply);
	}
	/**
	 * 授权委托书提交审核
	 * @param EnterpriseVo
	 * @return
	 * @since 2017年5月3日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/entApplyAuthorize",method = RequestMethod.POST)
	public void entApplyAuthorize(@RequestBody Apply apply) {
		enterpriseManager.entApplyAuthorize(apply,apply.getApplyOrgId(),"子账号申请记录描述");
	}

	
	/**
	 * 修改资质信息
	 * @param EnterpriseVo
	 * @return
	 * @since 2017年5月4日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/updateQualifications",method = RequestMethod.POST)
	public void updateQualifications(@RequestBody EnterpriseVo enterpriseVo) {
		enterpriseManager.updateQualifications(enterpriseVo);
	}
	/**
	 * 查询后台认证企业列表
	 * @param mail
	 * @param name
	 * @param vaildStatus
	 * @param status
	 * @param registerStart
	 * @param registerEnd
	 * @param lastLoginStart
	 * @param lastLoginEnd
	 * @param page
	 * @param size
	 * @return
	 * @since 2017年5月5日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/entCertificationList",method = RequestMethod.GET)
	public PageInfo<EnterpriseVo> entCertificationList(
			@RequestParam(value = "partyCode" , required = false)String partyCode, 
			@RequestParam(value = "name" , required = false)String name, 
			@RequestParam(value = "corCategory" , required = false)String corCategory,
			@RequestParam(value = "verifyStatus" , required = false)ActiveStatus verifyStatus,
			@RequestParam(value = "accountStatus" , required = false)AccountStatus accountStatus, 
			@RequestParam(value = "industry" , required = false)String industry,
			@RequestParam(value = "industryOther" , required = false)String industryOther,
			@RequestParam(value = "corCategoryOther" , required = false)String corCategoryOther,
			@RequestParam(value = "accountPeriodStatus" , required = false)AccountPeriodStatus accountPeriodStatus,
			@RequestParam(value = "province" , required = false)String province, 
			@RequestParam(value = "city" , required = false)String city, 
			@RequestParam(value = "county" , required = false)String county, 
			@RequestParam(value = "applyDateStart" , required = false)String applyDateStart, 
			@RequestParam(value = "applyDateEnd" , required = false)String applyDateEnd, 
			@RequestParam(value = "orgLimitStart" , required = false)String orgLimitStart, 
			@RequestParam(value = "orgLimitEnd" , required = false)String orgLimitEnd, 
			@RequestParam(value = "page" , defaultValue = "1")int page, 
			@RequestParam(value = "size" , defaultValue = "10")int size) {
		EnterpriseParamVo param = new EnterpriseParamVo();
		param.setName(name);
		param.setPartyCode(partyCode);
		param.setCorCategory(corCategory);
		param.setVerifyStatus(verifyStatus);
		param.setAccountStatus(accountStatus);
		List<String>  activeTypeList = new ArrayList<>();
		if(!StringUtils.isEmpty(industry)){
			
			String[] strL = industry.split(",");
			for(int i=0;i<strL.length;i++){
				activeTypeList.add(strL[i]);
			}
		}
		
		param.setCorCategoryOther(corCategoryOther);
		param.setActiveTypeList(activeTypeList);
		param.setIndustryOther(industryOther);
		param.setAccountPeriodStatus(accountPeriodStatus);
		param.setProvince(province);
		param.setCity(city);
		param.setCounty(county);
		param.setApplyDateStart(applyDateStart);
		param.setApplyDateEnd(applyDateEnd);
		param.setOrgLimitStart(orgLimitStart);
		param.setOrgLimitEnd(orgLimitEnd);
		RowBounds rowBounds = new RowBounds((page-1)*size, size);
		return entSearchManager.entCertificationList(param, rowBounds);
		
	}
	
	/**
	 * 导出认证企业管理列表
	 */
	@Override
	@RequestMapping(value="certified/excel",method = RequestMethod.GET)
	public void exportEntCertification(@RequestParam(value = "corCategory" , required = false)String corCategory,
			@RequestParam(value = "province" , required = false)String province, 
			HttpServletResponse response) throws IOException
	{
			
			EnterpriseParamVo param = new EnterpriseParamVo();
			param.setCorCategory(corCategory);
			param.setProvince(province);
			enterpriseManager.exportEntCertification(param,response);
			
	}
	
	
	
	/**
	 * 查询后台企业账户审核列表
	 * @param mail
	 * @param name
	 * @param vaildStatus
	 * @param status
	 * @param registerStart
	 * @param registerEnd
	 * @param lastLoginStart
	 * @param lastLoginEnd
	 * @param page
	 * @param size
	 * @return
	 * @since 2017年5月5日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/getAccountApplyList",method = RequestMethod.GET)
	public PageInfo<EnterpriseVo> getAccountApplyList(
			@RequestParam(value = "partyCode" , required = false)String partyCode, 
			@RequestParam(value = "name" , required = false)String name, 
			@RequestParam(value = "status" , required = false)PartyStatus status,
			@RequestParam(value = "verifyStatus" , required = false)ActiveStatus verifyStatus,
			@RequestParam(value = "accountStatus" , required = false)AccountStatus accountStatus, 
			@RequestParam(value = "applyDateStart" , required = false)String applyDateStart, 
			@RequestParam(value = "applyDateEnd" , required = false)String applyDateEnd, 
			@RequestParam(value = "approvedDateStart" , required = false)String approvedDateStart, 
			@RequestParam(value = "approvedDateEnd" , required = false)String approvedDateEnd, 
			@RequestParam(value = "page" , defaultValue = "1")int page, 
			@RequestParam(value = "size" , defaultValue = "10")int size) {
		EnterpriseParamVo param = new EnterpriseParamVo();
		param.setName(name);
		param.setStatus(status);
		param.setVerifyStatus(verifyStatus);
		param.setAccountStatus(accountStatus);
		param.setPartyCode(partyCode);
		param.setApplyDateStart(applyDateStart);
		param.setApplyDateEnd(applyDateEnd);
		param.setApprovedDateStart(approvedDateStart);
		param.setApprovedDateEnd(approvedDateEnd);
		List<String> activeTypeList = new ArrayList<>();
		activeTypeList.add("WAIT_APPROVE");
		activeTypeList.add("REJECTED");
		activeTypeList.add("PARTY_VERIFIED");
		activeTypeList.add("INVALID");
		param.setActiveTypeList(activeTypeList);
		RowBounds rowBounds = new RowBounds((page-1)*size, size);
		return entSearchManager.getAccountApplyList(param, rowBounds);
		
	}
	
	/**
	 * 查询认证过的子账号状态
	 * @param id
	 * @return
	 * @since 2017年5月10日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/getAccountStatus/{id}",method = RequestMethod.GET)
	public String getAccountStatus(@PathVariable(value = "id")String id) {
		return entSearchManager.getAccountStatus(id);
	}
	/**
	 * 失效账号
	 * @param id
	 * @return
	 * @since 2017年5月10日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/invalidAccount/{id}",method = RequestMethod.PUT)
	public void invalidAccount(@PathVariable(value = "id")String id,@RequestParam(value="reason",required=true)String reason) {
		enterpriseManager.invalidAccount(id,reason);
		
	}
	
	/**
	 *企业证件失效定时Job 
	 */
	@Override
	@RequestMapping(value="/expired",method = RequestMethod.POST)
	public void enterpriseDocumentsExpiredJob() {
		enterpriseManager.enterpriseDocumentsExpiredJob();
	}
	
	/**
	 * 修改公司信息
	 * @param id
	 * @return
	 * @since 2016年1月18日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/editMemberCompany",method = RequestMethod.POST)
	public void editMemberCompany(@RequestBody EnterpriseVo enterpriseVo) {
		enterpriseManager.editMemberCompany(enterpriseVo);
	}
	
	
	/**
	 * 账期申请
	 * @param Apply
	 * @return
	 * @since 2017年7月20日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/accountPeriodApply",method = RequestMethod.POST)
	public void accountPeriodApply(@RequestBody Apply apply) {
		enterprisePeriodManager.accountPeriodApply(apply,apply.getApplyOrgId(),"账期申请记录描述");
	}

	
	/**
	 * 账期申请
	 * @param Apply
	 * @return
	 * @since 2017年7月20日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/accountPeriodAudit",method = RequestMethod.POST)
	public void accountPeriodAudit(@RequestBody Apply apply) {
		enterprisePeriodManager.accountPeriodAudit(apply);
	}
	
	
	/**
	 * 根据企业id(corporationId查询用户的账期信息)
	 *
	 */
	@Override
	@RequestMapping(value="/{corporationId}/credit",method = RequestMethod.GET)
	public PartyCreditVo getPartyCreditVoByCorporationId(@PathVariable(value = "corporationId")String corporationId) {
		return enterprisePeriodManager.getPartyCreditVoByCorporationId(corporationId);
	}

	
	
	/**
	 * 账期订单列表查询
	 */
	@Override
	@RequestMapping(value="/credit",method = RequestMethod.GET)
	public PageInfo<PartyCreditVo> getPartyCreditVoList(@RequestParam(value = "partyCode" , required = false)String partyCode, 
			@RequestParam(value = "groupName" , required = false)String groupName,
			@RequestParam(value = "accountPeriodStatus" , required = false)AccountPeriodStatus accountPeriodStatus,
			@RequestParam(value = "page" , defaultValue = "1")int page, 
			@RequestParam(value = "size" , defaultValue = "10")int size) {
		PartyCreditParamVo param = new PartyCreditParamVo();
		param.setPartyCode(partyCode);
		param.setGroupName(groupName);
		param.setAccountPeriodStatus(accountPeriodStatus);
		RowBounds rowBounds = new RowBounds((page-1)*size, size);
		return enterprisePeriodManager.getPartyCreditVoList(param,rowBounds);
	}

	
	
	/**
	 * 冻结账期
	 * @param partyId
	 * @param status
	 * @return
	 * @since 2017年8月9日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/frozenCredit/{partyId}/{status}",method=RequestMethod.PUT)
	public void updateCreditStatus(@PathVariable(required=true)String partyId, @PathVariable(required=true)String status){
		 enterprisePeriodManager.updateCreditStatus(partyId, status);
	}
	
	
	/**
	 * 客服认证修改公司名称
	 * @param id
	 * @param status
	 * @return
	 * @since 2017年8月9日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/{partyId}/companyName",method=RequestMethod.PUT)
	public void updateCompanyName(@PathVariable(required=true)String partyId,@RequestBody(required=true)String name) throws BusinessException {
		enterpriseManager.updateCompanyName(partyId,name);
	}
	
	
	/**
	 * 根据企业id查询partyIds
	 */
	@Override
	@RequestMapping(value="/{vipCorporationId}/enterprises",method=RequestMethod.GET)
	public List<String> getPartyIdList(@PathVariable(value = "vipCorporationId")String vipCorporationId) {
		return enterprisePeriodManager.getPartyIdList(vipCorporationId);
	}
	/**
	 * 发送账期审核节点邮件
	 * @param id
	 * @return
	 * @since 2017年11月12日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/sendNodeCreitMail/{id}", method = RequestMethod.GET)
	public void sendNodeCreitMail(@PathVariable(required=true)String id){
		enterprisePeriodManager.sendNodeCreitMail(id);
		
	}
	

	
	
	
	
}