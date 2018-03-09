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
package com.yikuyi.party.customer.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yikuyi.party.contact.vo.CompanyInfoVo;
import com.yikuyi.party.contact.vo.CreditVo;
import com.yikuyi.party.contact.vo.EnterpriseParamVo;
import com.yikuyi.party.contact.vo.PersonInfoVo;
import com.yikuyi.party.contact.vo.UserParamVo;
import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.model.PartyRelationship;
import com.yikuyi.party.person.model.Person;
import com.yikuyi.party.vendor.vo.ContactPersonInfo;

@Mapper
public interface PersonDao {
	/**
	 * 获取个人基础信息(单表)
	 * @param id
	 * @return
	 * @since 2016年1月12日
	 * @author zr.helinmei@yikuyi.com
	 */
	public Party findPersonById(String id);
	
	/**
	 * 新增一条个人基础信息数据
	 * @param party
	 * @since 2016年1月12日
	 * @author zr.helinmei@yikuyi.com
	 */
	public void insert(Party party);
	
	/**
	 * 修改一条个人基础信息数据
	 * @param Party
	 * @since 2016年1月12日
	 * @author zr.helinmei@yikuyi.com
	 */
	public void editPerson(Party party);
	/**
	 * 根据状态查询用户
	 * @param relationSratus
	 * @return
	 * @since 2017年2月15日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public List<UserVo> getPersons(Party party);

	/**
	 * 查询用户列表（模糊）
	 * @param username
	 * @return
	 * @since 2017年2月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public List<UserVo> getUser(Party party);
	/**
	 * 查询用户列表（精确）
	 * @param username
	 * @return
	 * @since 2017年2月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public UserVo getUsers(Party party);
	
	/**
	 * 查询认证部列表
	 * @param 
	 * @return
	 * @since 2017年2月20日
	 * @author zr.helinmei@yikuyi.com
	 */
	public List<Party> findDataByRole(@Param("roleList")List<String> roleList);
	
	/**
	 * 查询用户姓名
	 * @param userId
	 * @return
	 * @since 2017年3月4日
	 * @author tb.yumu@yikuyi.com
	 */
	public Party getPersonByUserId(@Param("userId")String userId);
	
	public List<Person> getEmailListByRoleType(@Param("roleType")String roleType);
	
	List<Party> getReportsTo(@Param("partyId")String partyId,@Param("roleTypeFrom")String roleTypeFrom);

	public List<UserVo> getPartyByIds(@Param("ids") List<String> ids);

	public List<UserVo> getUserList(EnterpriseParamVo vo);
	
	
	/**
	 * 新增一条个人基础信息数据   供应商管理新增
	 * @param ContactPersonInfo
	 * @since 2017年8月15日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	public void insertContactPersonInfo(ContactPersonInfo contactPersonInfo);
	
	
	/**
	 * 删除供应商绑定的联系人
	 * @param ContactPersonInfo
	 * @since 2017年8月17日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	public void delContactPersonInfo(@Param("partyIdTo")String partyIdTo,@Param("partyRelationshipTypeId")String partyRelationshipTypeId);
	
	/**
	 *查找供应商Id下绑定的联系人
	 * @param ContactPersonInfo
	 * @since 2017年8月17日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	public List<PartyRelationship> findPartyRelationship(@Param("partyIdTo")String partyIdTo,@Param("partyRelationshipTypeId")String partyRelationshipTypeId);
	
	/**
	 * 删除   根据PARTY_ID_FROM和PARTY_RELATIONSHIP_TYPE_ID 
	 * @param 
	 * @since 2017年8月21日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	public void delByPartyIdFrom(@Param("partyIdFrom")String partyIdFrom,@Param("partyRelationshipTypeId")String partyRelationshipTypeId);
	public void delByPartyIdTo(@Param("partyIdTo")String partyIdTo,@Param("partyRelationshipTypeId")String partyRelationshipTypeId);

	
	/**
	 * 删除联系人 根据partyId   
	 * @param 
	 * @since 2017年8月21日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	public void del(@Param("partyId")String partyId);
	
	/**
	 *查找联系人Id下绑定的产品线ID
	 * @param 
	 * @since 2017年8月22日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	public List<PartyRelationship> findProductLineByPartyId(@Param("partyIdFrom")String partyIdFrom,@Param("partyRelationshipTypeId")String partyRelationshipTypeId);
		
	
	/**
	 *批量查询用户信息
	 * @param 
	 * @since 2017年12月13日
	 * @author zr.helinmeig@yikuyi.com
	 */
	public List<UserParamVo> getCustomersByIds(@Param("ids") List<String> ids);
	
	/**
	 * 通过用户id查询企业名称
	 * @param 
	 * @since 2017年12月13日
	 * @author zr.helinmeig@yikuyi.com
	 */
	public String getPartyGroupNameById(@Param("id") String id);
	
	/**
	 * 根据用户id查询用户登陆id
	 * @param 
	 * @since 2017年12月13日
	 * @author zr.helinmeig@yikuyi.com
	 */
	public String getUserLoginIdById(@Param("id") String id);
	
	/**
	 * 查询企业下的所有用户同步数据方法
	 * @param rowBounds
	 * @return List<ContactsVo>
	 * @since 2017年12月14日
	 * @author zr.helinmei@yikuyi.com
	 */
	public List<PersonInfoVo> getUserByIdList(@Param("id")String id);
	
	/**
	 * 根据登陆用户id查询企业信息
	 * @param id
	 * @return
	 * @since 2018年1月9日
	 * @author zr.helinmei@yikuyi.com
	 */
	public CompanyInfoVo getCompanyInfoById(@Param("id")String id);
	
	/**
	 * 根据企业id查询账期信息
	 * @param id
	 * @return
	 * @since 2018年1月9日
	 * @author zr.helinmei@yikuyi.com
	 */
	public CreditVo getPartyCreditByEntId(@Param("id")String id);
	
	/**
	 * 根据登陆用户id查询总公司
	 * @param id
	 * @return
	 * @since 2018年1月9日
	 * @author zr.helinmei@yikuyi.com
	 */
	public String getHeadOfficeByEntId(@Param("id")String id);
	
	
	
	
}