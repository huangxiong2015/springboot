/*
 * Created: 2017年1月11日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.customer.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.pagehelper.PageInfo;
import com.yikuyi.party.contact.vo.UserExtendVo;
import com.yikuyi.party.contact.vo.UserParamVo;
import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.party.facility.model.Facility;
import com.yikuyi.party.login.model.UserLogin;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.model.Party.PartyStatus;
import com.yikuyi.party.person.model.Person;
import com.ykyframework.exception.BusinessException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 用户信息接口
 * 
 * @author zr.helinmei@yikuyi.com
 *
 */
public interface ICustomerResource {

	/**
	 * 查询基本信息
	 * @param id
	 * @param loginId
	 * @return
	 * @since 2017年8月23日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "查询基本信息", notes = "查询基本信息", response = UserExtendVo.class)
	public UserExtendVo getBaseInfoDetail(@ApiParam(value = "用户partyId", required = true) String id,
			@ApiParam(value = "用户登录账号", required = false) String loginId);
	
	/**
	 * 根据状态查询用户
	 * @param relationSratus
	 * @return
	 * @since 2017年8月23日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "根据状态查询用户", notes = "根据状态查询用户")
	public List<UserVo> getPersons(@ApiParam(value = "用户状态", required = false) Person.RelationSratus relationSratus);

	/**
	 * 保存基本信息
	 * @param userExtendVo
	 * @since 2017年8月23日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "保存基本信息", notes = "保存用户信息")
	public void save(@ApiParam(value = "用户信息json", required = true) UserExtendVo userExtendVo);
	
	/**
	 * 后台创建账号
	 * @param userVo
	 * @return
	 * @since 2017年8月23日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "后台创建账号", notes = "后台创建账号")
	public Map<String,String> creatAccount(@ApiParam(value = "用户信息", required = true) UserVo userVo);
	
	/**
	 * 注销账号
	 * @param accountId
	 * @return
	 * @since 2017年8月23日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "注销账号", notes = "注销账号")
	public String cancelAccount(@ApiParam(value = "accountId", required = true) String accountId);
	
	/**
	 * 冻结账号/解冻
	 * @param accountId
	 * @param partyStatus
	 * @return
	 * @since 2017年8月23日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "冻结账号/解冻", notes = "冻结账号/解冻")
	public String frozenAccount(@ApiParam(value = "accountId", required = true) String accountId,
			@ApiParam(value = "状态", required = true) Party.PartyStatus partyStatus);
	
	/**
	 * 更新用户信息
	 * @param userExtendVo
	 * @since 2017年8月23日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "更新用户信息", notes = "更新用户信息")
	public void update(@ApiParam(value = "用户信息", required = true) UserExtendVo userExtendVo);

	/**
	 * 根据用户名称查询用户列表
	 * @param username
	 * @return
	 * @since 2017年8月23日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "根据用户名称查询用户列表", notes = "根据用户名称查询用户列表")
	public List<UserVo> getUserByName(@ApiParam(value = "用户名称", required = false) String username);

	/**
	 * 根据用户名称精确查询用户列表
	 * @param username
	 * @return
	 * @since 2017年8月23日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "根据用户名称精确查询用户列表", notes = "根据用户名称精确查询用户列表")
	public UserVo getUsersByName(@ApiParam(value = "用户名称", required = false) String username);

	/**
	 * 根据用户Id更新用户状态信息
	 * @param id
	 * @param relationSratus
	 * @since 2017年8月23日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "根据用户Id更新用户状态信息", notes = "根据用户Id更新用户状态信息")
	public void updateById(@ApiParam(value = "用户id", required = true) String id,
			@ApiParam(value = "用户状态", required = true) Person.RelationSratus relationSratus);

	/**
	 * 查询个人基本信息列表
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
	 * @return
	 * @since 2017年8月23日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "查询个人基本信息列表", notes = "查询个人基本信息列表")
	public PageInfo<UserExtendVo> getUserInfoList(@ApiParam(value = "注册手机", required = false) String phone,
			@ApiParam(value = "邮箱", required = false) String mail,
			@ApiParam(value = "姓名", required = false) String name,
			@ApiParam(value = "状态", required = false) PartyStatus status,
			@ApiParam(value = "注册时间开始", required = false) String registerStart,
			@ApiParam(value = "注册时间结束", required = false) String registerEnd,
			@ApiParam(value = "最后登录时间开始", required = false) String lastLoginStart,
			@ApiParam(value = "最后登录时间结束", required = false) String lastLoginEnd,
			@ApiParam(value = "page", required = false, defaultValue = "1") int page,
			@ApiParam(value = "size", required = false, defaultValue = "10") int size);
	
	/**
	 * 导出个人会员列表
	 * @param phone
	 * @param mail
	 * @param name
	 * @param status
	 * @param registerStart
	 * @param registerEnd
	 * @param lastLoginStart
	 * @param lastLoginEnd
	 * @param response
	 * @throws IOException
	 * @since 2017年8月23日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "导出个人会员列表", notes = "导出个人会员列表")
	public void exportUser(@ApiParam(value = "注册手机", required = false) String phone,
			@ApiParam(value = "邮箱", required = false) String mail,
			@ApiParam(value = "姓名", required = false) String name,
			@ApiParam(value = "状态", required = false) PartyStatus status,
			@ApiParam(value = "注册时间开始", required = false) String registerStart,
			@ApiParam(value = "注册时间结束", required = false) String registerEnd,
			@ApiParam(value = "最后登录时间开始", required = false) String lastLoginStart,
			@ApiParam(value = "最后登录时间结束", required = false) String lastLoginEnd,
			HttpServletResponse response) throws IOException;	
	
	/**
	 * 根据用户ID查询用户姓名
	 * @param userId
	 * @return
	 * @since 2017年8月23日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "根据用户ID查询用户姓名", notes = "根据用户ID查询用户姓名", response = Person.class)
	public Person findPersonInfoByPartyId(@ApiParam(value = "用户ID", required = true) String userId);

	/**
	 * 根据用户partyID查询用户loginId
	 * @param partyId
	 * @return
	 * @since 2017年8月23日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "根据用户partyID查询用户loginId", notes = "根据用户partyID查询用户loginId", response = UserLogin.class)
	public UserLogin findUserLogin(@ApiParam(value = "用户partyID", required = true) String partyId);
	
	/**
	 * 发送邮件（更新账号，验证账号）
	 * @param serVo
	 * @return
	 * @since 2017年8月23日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "发送邮件（更新账号，验证账号）", notes = "发送邮件（更新账号，验证账号）", response = String.class)
	public String sendMail(@ApiParam(value = "用户", required = true) UserVo serVo);
	
	/**
	 * 批量添加子账号
	 * @param accounts
	 * @return
	 * @throws BusinessException
	 * @since 2017年8月23日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "批量添加子账号", notes = "批量添加子账号", response = String.class)
	public String addSubAccount(@ApiParam(value = "子账号",required = true) List<String> accounts)throws BusinessException;
	
	/**
	 * [后端]用户管理-用户启用/停用
	 * @param partyId
	 * @param statusId
	 * @return
	 * @since 2017年8月23日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "[后端]用户管理-用户启用/停用", notes = "[后端]用户管理-用户启用/停用", response = String.class)
	public String updateStateId(@ApiParam(value = "用户partyId", required = true) String partyId,
			@ApiParam(value = "状态ID:1.PARTY_ENABLED 启动2.PARTY_DISABLED停用", required = true) String statusId);
	
	/**
	 * 根据已验证邮箱精确查询数据
	 * @param mail
	 * @return
	 * @since 2017年8月23日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "根据已验证邮箱精确查询数据", notes = "根据已验证邮箱精确查询数据")
	public UserVo getUsersByMail(@ApiParam(value = "用户邮箱", required = true) String mail);
	
	/**
	 * 批量查询用户信息
	 * @param 
	 * @since 2017年12月13日
	 * @author zr.helinmeig@yikuyi.com
	 */
	@ApiOperation(value = "根据用户id查询用户信息", notes = "根据用户id查询用户信息", response = Facility.class, responseContainer="List")
	@RequestMapping(method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public  List<UserParamVo> getCustomersByIds(@ApiParam(value = "ids", required = true) List<String> ids);
	

}