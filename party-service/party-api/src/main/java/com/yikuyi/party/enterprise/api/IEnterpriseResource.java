/*
 * Created: 2017年1月18日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.enterprise.api;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.yikuyi.party.contact.vo.EnterpriseVo;
import com.yikuyi.party.contact.vo.UserExtendVo;
import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.party.credit.vo.PartyCreditVo;
import com.yikuyi.party.group.model.PartyGroup;
import com.yikuyi.party.group.model.PartyGroup.AccountPeriodStatus;
import com.yikuyi.party.group.model.PartyGroup.AccountStatus;
import com.yikuyi.party.group.model.PartyGroup.ActiveStatus;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.model.Party.PartyStatus;
import com.yikuyi.party.model.Party.PartyType;
import com.yikuyi.party.person.model.Person.PersonTypeStatus;
import com.yikuyi.workflow.Apply;
import com.ykyframework.exception.BusinessException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 用户信息接口
 * @author helinmei
 *
 */
public interface IEnterpriseResource {

	/**
	 * 公司信息查询
	 * @param id
	 * @return
	 * @since 2016年1月18日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "公司信息查询", notes = "公司信息查询", response = EnterpriseVo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public EnterpriseVo getEnterprise(@ApiParam(value = "用户登录id", required = true) String id);
	
	
	/**
	 * 公司基本信息查询
	 * @param partyId
	 * @return
	 * @since 2017年7月20日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "公司基本信息查询", notes = "公司基本信息查询", response = PartyGroup.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public PartyGroup getEntBaseInfo(@ApiParam(value = "用户登录id", required = false) String partyId);
	
	
	/**
	 * 查询企业详细信息
	 * @param entId
	 * @param corporationId
	 * @param partyType
	 * @return
	 * @since 2017年7月20日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "查询企业详细信息", notes = "查询企业详细信息", response = UserExtendVo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public EnterpriseVo getEnterpriseDetailByEntId(@ApiParam(value = "企业id", required = true) String entId,@ApiParam(value = "父企业id", required = true) String corporationId,@ApiParam(value = "企业类型", required = true) PartyType partyType);
	
	
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
	@ApiOperation(value = "查询后台企业用户管理列表", notes = "查询后台企业用户管理列表", response = List.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public PageInfo<EnterpriseVo> enterpriseList(@ApiParam(value = "邮箱")String mail,
			@ApiParam(value="联系人")String contactUserName,
			@ApiParam(value="手机号码")String contactUserTel,
			@ApiParam(value="公司名称")String name,
			@ApiParam(value="账号状态")PartyStatus status,
			@ApiParam(value="认证状态")ActiveStatus verifyStatus,
			@ApiParam(value="主账号状态")PersonTypeStatus accountStatus,
			@ApiParam(value = "注册时间开始", required = false)String registerStart,
			@ApiParam(value = "注册时间结束", required = false)String registerEnd,
			@ApiParam(value = "最后登录时间开始", required = false)String lastLoginStart,
			@ApiParam(value = "最后登录时间结束", required = false)String lastLoginEnd,
			@ApiParam(value="page",required=false,defaultValue="1") int page,
			@ApiParam(value="size",required=false,defaultValue="10") int size);
	
	
	/**
	 * 导出企业会员列表
	 * @param verifyStatus
	 * @param registerStart
	 * @param registerEnd
	 * @param response
	 * @throws IOException
	 * @since 2017年7月24日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "导出企业会员列表", notes = "导出企业会员列表", response = Void.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void exportEnt(@ApiParam(value = "认证状态")ActiveStatus verifyStatus,
			@ApiParam(value = "注册时间开始", required = false)String registerStart,
			@ApiParam(value = "注册时间结束", required = false)String registerEnd,
			HttpServletResponse response) throws IOException;
	
	
	/**
	 * 查询企业账号及子账号
	 * @param id
	 * @return
	 * @since 2017年1月19日
	 * @author tb.yumu@yikuyi.com
	 */
	@ApiOperation(value = "查询企业账号及子账号", notes = "查询企业账号及子账号", response = List.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public List<UserExtendVo> enterpriseAccountsList(@ApiParam(value="企业id",required=true)String id);
	

	/**
	 * 根据账号查询管理员信息
	 * 
	 * @param id
	 * @param role
	 * @return
	 * @since 2017年1月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@ApiOperation(value = "根据用户id查询管理员账号", notes = "根据用户id查询管理员账号", response = UserVo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = UserVo.class)
			})
	public UserVo getAdmin(@ApiParam(value = "用户登录id", required = true) String id,@RequestParam(value="role" ) String role);

	
	/**
	 * 根据用户的id判断是否为首次激活或者关联
	 * 
	 * @param id
	 * @param role
	 * @return
	 * @since 2017年1月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@ApiOperation(value = "根据用户的id判断是否为首次激活或者关联", notes = "根据用户的id判断是否为首次激活或者关联", response = Boolean.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Boolean.class)
			})
	public Boolean isFristActive();
	
	
	/**
	 * 根据用户的id判断是否为激活或者关联
	 * 
	 * @param id
	 * @param role
	 * @return
	 * @since 2017年1月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@ApiOperation(value = "根据用户的id判断是否为激活或者关联", notes = "根据用户的id判断是否为激活或者关联", response = String.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = String.class)
			})
	public String isActivedOrRelationed();
	
	
	/**
	 * 根据用户的id判断是否为管理员
	 * 
	 * @param id
	 * @param role
	 * @return
	 * @since 2017年1月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@ApiOperation(value = "根据用户的id判断是否为管理员", notes = "根据用户的id判断是否为管理员", response = Boolean.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Boolean.class)
			})
	public Boolean isAdmin();
	
	
	/**
	 * 根据id更新状态
	 * @param id
	 * @param status
	 * @return
	 * @since 2017年1月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@ApiOperation(value ="根据id更新状态",notes="根据id更新状态",response = Void.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
			})
	public void updateStatus(@ApiParam(value="id",required = true)String id,@ApiParam(value="status",required = true) Party.PartyStatus status);
	
	
	/**
	 * 公司信息保存
	 * @param id
	 * @return
	 * @since 2016年1月18日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "公司认证审核", notes = "公司认证审核", response = Apply.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void save(@ApiParam(value = "公司认证审核", required = true) Apply apply);
	
	
	/**
	 * 修改公司信息
	 * @param id
	 * @return
	 * @since 2016年1月18日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "公司信息保存", notes = "公司信息ObjectJson保存", response = EnterpriseVo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void editCompany(@ApiParam(value = "公司信息表单", required = true) EnterpriseVo enterpriseVo);
	
	
	/**
	 * 账户激活首次申请保存数据
	 * @param id
	 * @return
	 * @since 2016年1月18日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "企业账户激活申请保存", notes = "企业账户激活申请保存", response = EnterpriseVo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void activeAccountSave(@ApiParam(value = "公司信息表单", required = true)Apply apply)throws BusinessException;
	
	
	/**
	 * 根据数据库中保存的url从阿里云获取私有图片url
	 * @param id
	 * @return
	 * @since 2016年2月18日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "从阿里云获取私有图片url", notes = "从阿里云获取私有图片url", response = EnterpriseVo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public String getImgUrl(@ApiParam(value = "url", required = true) JSONObject jsonObject);

	
	/**
	 * 企业会员管理修改申请
	 * @param Apply
	 * @return
	 * @since 2017年4月13日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "企业会员管理修改申请", notes = "企业会员管理修改申请", response = EnterpriseVo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void editEntApply(@ApiParam(value = "公司信息表单", required = true)Apply apply);
  
	
	/**
	 * 后台管理企业账户修改审核成功调用方法
	 * @param ObjectJson
	 * @return
	 * @since 2017年4月13日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "后台管理企业账户修改审核成功调用方法", notes = "后台管理企业账户修改审核成功调用方法", response = EnterpriseVo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void editEntApplySave(@ApiParam(value = "后台管理企业账户修改审核成功调用方法", required = true) Apply apply);
	
	
	/**
	 * 企业授权委托书审核接口
	 * @param id
	 * @return
	 * @since 2017年5月2日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "企业授权委托书审核接口", notes = "企业授权委托书审核接口", response = EnterpriseVo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void entAuthorize(@ApiParam(value = "企业授权委托书审核接口", required = true) Apply apply);
	
	
	/**
	 * 企业授权委托书审核申请
	 * @param id
	 * @return
	 * @since 2017年5月2日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "企业授权委托书审核申请", notes = "企业授权委托书审核申请", response = EnterpriseVo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void entApplyAuthorize(@ApiParam(value = "企业授权委托书审核申请", required = true)Apply apply);

	
	/**
	 * 修改资质信息
	 * @param id
	 * @return
	 * @since 2017年5月4日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "修改资质信息", notes = "修改资质信息", response = EnterpriseVo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void updateQualifications(@ApiParam(value = "修改资质信息", required = true) EnterpriseVo enterpriseVo);
	
	
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
	@ApiOperation(value = "查询后台认证企业列表", notes = "查询后台认证企业列表", response = List.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public PageInfo<EnterpriseVo> entCertificationList(@ApiParam(value = "YKY客户编码")String partyCode,
			@ApiParam(value="公司名称")String name,
			@ApiParam(value="公司类型")String corCategory,
			@ApiParam(value="认证状态")ActiveStatus verifyStatus,
			@ApiParam(value="子账号功能")AccountStatus accountStatus,
			@ApiParam(value="行业")String industry,
			@ApiParam(value="行业其他")String industryOther,
			@ApiParam(value="公司类型其他")String corCategoryOther,
			@ApiParam(value="账期状态")AccountPeriodStatus accountPeriodStatus,
			@ApiParam(value="省份")String province,
			@ApiParam(value="城市")String city,
			@ApiParam(value="县")String county,
			@ApiParam(value = "审核开始时间")String applyDateStart,
			@ApiParam(value = "审核结束时间")String applyDateEnd,
			@ApiParam(value = "营业期限开始时间")String orgLimitStart,
			@ApiParam(value = "营业期限结束时间")String orgLimitEnd,
			@ApiParam(value="page",required=false,defaultValue="1") int page,
			@ApiParam(value="size",required=false,defaultValue="10") int size);
	
	
	
	/**
	 * 导出认证企业管理列表
	 * @param partyCode
	 * @param name
	 * @param corCategory
	 * @param verifyStatus
	 * @param industry
	 * @param industryOther
	 * @param province
	 * @param city
	 * @param county
	 * @param applyDateStart
	 * @param applyDateEnd
	 * @param orgLimitStart
	 * @param orgLimitEnd
	 * @param response
	 * @throws IOException
	 * @since 2017年7月21日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "导出认证企业管理列表", notes = "导出认证企业管理列表", response = Void.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void exportEntCertification(@ApiParam(value="公司类型")String corCategory,
			@ApiParam(value="省份")String province,
			HttpServletResponse response) throws IOException;
	

	
	/**
	 * 查询后台账户审核列表
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
	@ApiOperation(value = "查询后台账户审核列表", notes = "查询后台账户审核列表", response = List.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public PageInfo<EnterpriseVo> getAccountApplyList(
			@ApiParam(value = "yky客户编码")String partyCode,
			@ApiParam(value="公司名称")String name,
			@ApiParam(value="账户状态")PartyStatus status,
			@ApiParam(value="认证状态")ActiveStatus verifyStatus,
			@ApiParam(value="子账号功能")AccountStatus accountStatus,
			@ApiParam(value = "申请开始时间")String applyDateStart,
			@ApiParam(value = "申请结束时间")String applyDateEnd,
			@ApiParam(value = "审核开始时间")String approvedDateStart,
			@ApiParam(value = "审核结束时间")String approvedDateEnd,
			@ApiParam(value="page",required=false,defaultValue="1") int page,
			@ApiParam(value="size",required=false,defaultValue="10") int size);
	
	
	/**
	 * 查询认证过的子账号状态
	 * @param id
	 * @return
	 * @since 2017年5月10日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "查询认证过的子账号状态", notes = "查询认证过的子账号状态", response = String.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public String getAccountStatus(@ApiParam(value = "企业id", required = true)String id);
	
	
	/**
	 * 失效账号
	 * @param id
	 * @return
	 * @since 2017年5月10日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "失效账号", notes = "失效账号", response = String.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void invalidAccount(@ApiParam(value = "企业id", required = true)String id,@ApiParam(value="reason",required = true) String reason);
	
	
	/**
	 * 企业证件失效定时Job
	 * 
	 * @since 2017年5月10日
	 * @author zr.shuzuo@yikuyi.com
	 */
	@ApiOperation(value = "企业证件失效定时job", notes = "企业证件失效定时job", response = Void.class)
	@ApiResponses(value ={ @ApiResponse(code = 400, message = "请求参数不正确", response = Void.class)})
	public void enterpriseDocumentsExpiredJob();
	
	
	/**
	 * 修改公司信息
	 * @param id
	 * @return
	 * @since 2016年1月18日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "公司信息保存", notes = "公司信息ObjectJson保存", response = EnterpriseVo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void editMemberCompany(@ApiParam(value = "公司信息表单", required = true) EnterpriseVo enterpriseVo);
	
	
	/**
	 * 账期申请
	 * @param Apply
	 * @return
	 * @since 2017年7月20日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "账期申请", notes = "账期申请", response = EnterpriseVo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void accountPeriodApply(@ApiParam(value = "申请信息", required = true)Apply apply);


	/**
	 * 账期审核通过或驳回接口
	 * @param id
	 * @return
	 * @since 2017年7月20日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "账期审核通过或驳回", notes = "账期审核通过或驳回", response = Apply.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void accountPeriodAudit(@ApiParam(value = "账期审核通过或驳回", required = true) Apply apply);
	
		
	
	/**
	 * 根据企业Id查询用户的账期信息
	 * @param corporationId
	 * @return
	 * @since 2017年7月28日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "根据企业Id查询用户的账期信息", notes = "根据企业Id查询用户的账期信息", response = PartyCreditVo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public PartyCreditVo getPartyCreditVoByCorporationId(@ApiParam(value = "企业id", required = true) String corporationId);
	

	/**
	 * 账期订单列表（运营后台）查询
	 * @param partyCode  YKY客户编码
	 * @param groupName  公司名称
	 * @param accountPeriodStatus  账期状态
	 * @return
	 * @since 2017年8月9日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "账期订单列表查询", notes = "账期订单列表查询", response = List.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public PageInfo<PartyCreditVo> getPartyCreditVoList(@ApiParam(value = "YKY客户编码", required = false) String partyCode,
			@ApiParam(value = "公司名称", required = false) String groupName,
			@ApiParam(value = "账期状态", required = false) AccountPeriodStatus accountPeriodStatus,
			@ApiParam(value="page",required=false,defaultValue="1") int page,
			@ApiParam(value="size",required=false,defaultValue="10") int size);

	
	
	/**
	 * 冻结账期
	 * @param id
	 * @param status
	 * @return
	 * @since 2017年8月9日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "冻结账期", notes = "冻结账期", response = Void.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	public void updateCreditStatus(@ApiParam(value="冻结账期partyId",required=true) String partyId,
			@ApiParam(value="状态",required = true) String status)  throws BusinessException;
	
	
	/**
	 * 客服认证修改公司名称
	 * @param id
	 * @param status
	 * @return
	 * @since 2017年8月9日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "客服认证修改公司名称", notes = "客服认证修改公司名称", response = Void.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	public void updateCompanyName(@ApiParam(value="partyId",required=true) String partyId,
			@ApiParam(value="公司名称",required = true) String name)  throws BusinessException;
	
	
	
	/**
	 * 根据企业Id查询partyIds
	 * @param corporationId
	 * @return
	 * @since 2017年8月21日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "根据vip企业Id查询partyIds", notes = "根据vip企业Id查询partyIds", response = List.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public List<String> getPartyIdList(@ApiParam(value = "企业id", required = true) String vipCorporationId);

	/**
	 * 发送账期审核节点邮件
	 * @param id
	 * @return
	 * @since 2017年11月12日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "发送账期审核节点邮件", notes = "发送账期审核节点邮件", response = EnterpriseVo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void sendNodeCreitMail(@ApiParam(value = "企业id", required = true)String id);
	
}

