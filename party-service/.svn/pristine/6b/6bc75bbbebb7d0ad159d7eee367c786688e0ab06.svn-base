/*
 * Created: 2017年1月12日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.customer.api.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.framewrok.springboot.web.RequestHelper;
import com.github.pagehelper.PageInfo;
import com.yikuyi.party.contact.vo.EnterpriseParamVo;
import com.yikuyi.party.contact.vo.UserExtendVo;
import com.yikuyi.party.contact.vo.UserParamVo;
import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.party.customer.api.ICustomerResource;
import com.yikuyi.party.customer.bll.CustomerManager;
import com.yikuyi.party.login.model.UserLogin;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.model.Party.PartyStatus;
import com.yikuyi.party.person.model.Person;
import com.ykyframework.exception.BusinessException;

/**
 * 用户信息类
 * 
 * @author helinmei
 *
 */

@RestController
@RequestMapping("v1/customers")
public class CustomerResource implements ICustomerResource {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerResource.class);
	
	@Autowired
	private CustomerManager partyCustomerManager;

	/**
	 * 获取用户详情
	 * 
	 * @param id
	 * @return UserInfoVo
	 * @since 2016年1月11日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public UserExtendVo getBaseInfoDetail(@PathVariable(value = "id") String id,
			@RequestParam(value = "loginId", required = false, defaultValue = "") String loginId) {
		return partyCustomerManager.getBaseInfoDetail(id, loginId);
	}

	/**
	 * 根据用户名称查询用户列表（模糊查询）
	 * 
	 * @param username
	 * @return
	 * @since 2017年2月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/username", method = RequestMethod.GET)
	public List<UserVo> getUserByName(@RequestParam(value = "username", required = false) String username) {
		return partyCustomerManager.getUserByName(username);
	}

	/**
	 * 根据用户名称查询用户列表（精确查询）
	 * 
	 * @param username
	 * @return
	 * @since 2017年2月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/name", method = RequestMethod.GET)
	public UserVo getUsersByName(@RequestParam(value = "name", required = false) String name) {
		return partyCustomerManager.getUsersByName(name);
	}

	/**
	 * 根据状态查询用户
	 * 
	 * @param relationSratus
	 * @return
	 * @since 2017年2月15日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/status", method = RequestMethod.GET)
	public List<UserVo> getPersons(
			@RequestParam(value = "relationSratus", required = false) Person.RelationSratus relationSratus) {
		String partyId = RequestHelper.getLoginUserId();
		return partyCustomerManager.getPersons(relationSratus, partyId);
	}

	
	
	/**
	 * 保存用户信息
	 * 
	 * @param id
	 * @return UserInfoVo
	 * @since 2016年1月11日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(method = RequestMethod.POST)
	public void save(@RequestBody UserExtendVo userInfoVo) {
		partyCustomerManager.save(userInfoVo);
	}
	
	/**
	 * 后台创建账号
	 * @param userVo
	 * @since 2017年5月9日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/account" ,method = RequestMethod.POST)
	public Map<String,String> creatAccount(@RequestBody UserVo userVo) {
		String partyId = RequestHelper.getLoginUserId();
		userVo.setId(partyId);
		return partyCustomerManager.creatAccount(userVo);
	}
	
	/**
	 * 根据用户Id更新用户状态信息
	 * 
	 * @param userInfoVo
	 * @since 2017年2月14日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public void updateById(@PathVariable String id,
			@RequestParam(value = "relationSratus", required = true) Person.RelationSratus relationSratus) {
		String partyId = RequestHelper.getLoginUserId();
		partyCustomerManager.updateById(id, relationSratus, partyId);
	}
	/**
	 * 注销账号
	 * @param accountId
	 * @return
	 * @since 2017年5月8日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	public String cancelAccount(@RequestParam(value = "accountId", required = true) String accountId) {
		String partyId = RequestHelper.getLoginUserId();
		return partyCustomerManager.cancelAccount(accountId,partyId);
	}
	/**
	 * 冻结账号
	 * @param accountId
	 * @return
	 * @since 2017年5月8日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/frozen", method = RequestMethod.POST)
	public String frozenAccount(@RequestParam(value = "accountId", required = true) String accountId,
			@RequestParam(value = "partyStatus", required = true) Party.PartyStatus partyStatus) {
		String partyId = RequestHelper.getLoginUserId();
		return partyCustomerManager.frozenAccount(partyStatus,accountId,partyId);
	}
	/**
	 * 更新用户信息
	 * 
	 * @param userInfoVo
	 * @since 2017年2月14日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(method = RequestMethod.PUT)
	public void update(@RequestBody UserExtendVo userInfoVo) {
		if(StringUtils.isEmpty(userInfoVo.getId())){
			String partyId = RequestHelper.getLoginUserId();
			userInfoVo.setId(partyId);	
		}
		partyCustomerManager.update(userInfoVo);
	}

	/**
	 * 后台查询个人用户列表
	 * 
	 * @param phone
	 * @param mail
	 * @param name
	 * @param status
	 * @param registerStart
	 * @param registerEnd
	 * @param lastLoginStart
	 * @param lastLoginEnd
	 * @param page
	 * @param size
	 * @since 2017年1月12日
	 * @author gongtianyu@yikuyi.com
	 */
	@Override
	@RequestMapping(method = RequestMethod.GET)
	public PageInfo<UserExtendVo> getUserInfoList(@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "mail", required = false) String mail,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "status", required = false) PartyStatus status,
			@RequestParam(value = "registerStart", required = false) String registerStart,
			@RequestParam(value = "registerEnd", required = false) String registerEnd,
			@RequestParam(value = "lastLoginStart", required = false) String lastLoginStart,
			@RequestParam(value = "lastLoginEnd", required = false) String lastLoginEnd,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "size", required = false, defaultValue = "10") int size) {
			EnterpriseParamVo param = new EnterpriseParamVo();
			param.setPhone(phone);
			param.setMail(mail);
			param.setName(name);
			param.setStatus(status);
			param.setRegisterStart(registerStart);
			param.setRegisterEnd(registerEnd);
			param.setLastLoginStart(lastLoginStart);
			param.setLastLoginEnd(lastLoginEnd);
			RowBounds rowBounds = new RowBounds((page-1)*size, size);
		    return partyCustomerManager.findCustomerUser(param,rowBounds);
	}

	/**
	 * 发送邮件（更新账号，验证账号）
	 * @param userVo
	 * @since 2017年2月9日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/sendMail", method = RequestMethod.POST)
	public String sendMail(@RequestBody UserVo userVo) {
		String partyId = RequestHelper.getLoginUserId();
		userVo.setId(partyId);
		return partyCustomerManager.sendMail(userVo);
	}
	
	/**
	 * 根据用户id获取person信息
	 */
	@Override
	@RequestMapping(value = "/{id}/username", method = RequestMethod.GET)
	public Person findPersonInfoByPartyId(@PathVariable(value = "id", required = true) String id) {
		return partyCustomerManager.findPersonInfoByPartyId(id);
	}

	/**
	 * 根据用户ID获取登录密码，登录ID
	 */
	@Override
	@RequestMapping(value = "/{id}/login", method = RequestMethod.GET)
	public UserLogin findUserLogin(@PathVariable(value = "id", required = true) String id) {
		return partyCustomerManager.getUserLoginByPartyid(id);
	}
	/**
	 * 批量添加子账号
	 * @param accounts
	 * @return
	 * @since 2017年5月11日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/batch",method = RequestMethod.POST)
	public String addSubAccount(@RequestBody List<String> accounts)throws BusinessException{
		String userId = RequestHelper.getLoginUserId();
		return partyCustomerManager.addSubAccount(userId,accounts);
	}
	/**
	 * 导出个人会员列表
	 * @param vo
	 * @return
	 * @since 2017年4月13日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/excel",method = RequestMethod.GET)
	public void exportUser(@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "mail", required = false) String mail,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "status", required = false) PartyStatus status,
			@RequestParam(value = "registerStart", required = false) String registerStart,
			@RequestParam(value = "registerEnd", required = false) String registerEnd,
			@RequestParam(value = "lastLoginStart", required = false) String lastLoginStart,
			@RequestParam(value = "lastLoginEnd", required = false) String lastLoginEnd,
			HttpServletResponse response)
			throws IOException {
		String userId = RequestHelper.getLoginUserId();
		if(userId == null || "".equals(userId)){
			logger.error("无法获取用户的id");
			return;
		}
		EnterpriseParamVo param = new EnterpriseParamVo();
		param.setPhone(phone);
		param.setMail(mail);
		param.setName(name);
		param.setStatus(status);
		param.setRegisterStart(registerStart);
		param.setRegisterEnd(registerEnd);
		param.setLastLoginStart(lastLoginStart);
		param.setLastLoginEnd(lastLoginEnd);
		partyCustomerManager.exportUser(param,response);
	}
	
	/**
	 * [后端]用户管理-用户启用/停用
	 * @param partyId
	 * @param statusId
	 * @return
	 * @since 2017年5月10日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/updateStateId",method = RequestMethod.POST)
	public String updateStateId(@RequestParam(value = "partyId", required = true) String partyId,
			@RequestParam(value = "statusId", required = true) String statusId)
			{
		return partyCustomerManager.updateStateId(partyId,statusId);
	}

	/**
	 * 根据已验证邮箱精确查询用户信息
	 */
	@Override
	@RequestMapping(value = "/getUsersByMail", method = RequestMethod.GET)
	public UserVo getUsersByMail(@RequestParam(value = "mail", required = true) String mail) {
		return partyCustomerManager.getUsersByMail(mail);
	}

	@Override
	@RequestMapping(value ="/getCustomersByIds", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public List<UserParamVo> getCustomersByIds(@RequestBody List<String> ids) {
		return partyCustomerManager.getCustomersByIds(ids);
	}


}