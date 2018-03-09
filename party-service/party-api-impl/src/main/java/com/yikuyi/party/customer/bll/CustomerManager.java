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
package com.yikuyi.party.customer.bll;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.jasig.inspektr.audit.annotation.Audit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.framework.springboot.utils.AuthorizationUtil;
import com.framewrok.springboot.web.RequestHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.ictrade.tools.export.ExportFactory;
import com.ictrade.tools.export.ExportProcesser;
import com.yikuyi.message.mail.vo.MailInfoVo;
import com.yikuyi.party.contact.model.ContactMech;
import com.yikuyi.party.contact.model.ContactMech.MechType;
import com.yikuyi.party.contact.model.PostalAddress;
import com.yikuyi.party.contact.model.TelecomNumber;
import com.yikuyi.party.contact.model.TelecomNumber.MobileTelecomNumber;
import com.yikuyi.party.contact.model.TelecomNumber.QqTelecomNumber;
import com.yikuyi.party.contact.vo.EnterpriseParamVo;
import com.yikuyi.party.contact.vo.EnterpriseVo;
import com.yikuyi.party.contact.vo.UserExtendVo;
import com.yikuyi.party.contact.vo.UserParamVo;
import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.party.customer.dao.PersonDao;
import com.yikuyi.party.exception.PartyBusiErrorCode;
import com.yikuyi.party.group.model.PartyGroup;
import com.yikuyi.party.group.model.PartyGroup.AccountStatus;
import com.yikuyi.party.group.model.PartyGroup.ActiveStatus;
import com.yikuyi.party.login.model.UserLogin;
import com.yikuyi.party.login.model.UserLogin.UserLoginMethod;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.model.Party.PartyStatus;
import com.yikuyi.party.model.Party.PartyType;
import com.yikuyi.party.model.PartyAttribute;
import com.yikuyi.party.model.PartyAttributes;
import com.yikuyi.party.model.PartyContactMech;
import com.yikuyi.party.model.PartyContactMech.PurposeType;
import com.yikuyi.party.model.PartyRelationship;
import com.yikuyi.party.model.PartyRelationship.PartyRelationshipStatus;
import com.yikuyi.party.model.PartyRelationship.PartyRelationshipTypeEnum;
import com.yikuyi.party.party.dao.PartyAttributeDao;
import com.yikuyi.party.party.dao.PartyDao;
import com.yikuyi.party.party.dao.PartyRoleDao;
import com.yikuyi.party.partygroup.dao.PartyGroupDao;
import com.yikuyi.party.partygroup.dao.PartyRelationshipDao;
import com.yikuyi.party.person.model.Person;
import com.yikuyi.party.person.model.Person.PersonTypeStatus;
import com.yikuyi.party.person.model.Person.RelationSratus;
import com.yikuyi.party.role.model.RoleTypeEnum;
import com.yikuyi.party.shipAddress.bll.PartyContactMechManager;
import com.yikuyi.party.userLogin.dao.UserLoginDao;
import com.yikuyi.workflow.Apply;
import com.yikuyi.workflow.WorkflowClientBuilder;
import com.ykyframework.exception.BusinessException;
import com.ykyframework.model.IdGen;
import com.ykyframework.mqservice.sender.MsgSender;

@Service
@Transactional
public class CustomerManager {
	private static final Logger logger = LoggerFactory.getLogger(CustomerManager.class);
	@Autowired
	private PartyContactMechManager partyContactMechManager;
	
	@Autowired
	private PersonDao personDao;
	
	@Autowired
	private UserLoginDao userLoginDao;
	
	@Autowired
	private PartyRoleDao partyRoleDao;
	
	@Autowired
	private PartyRelationshipDao partyRelationshipDao;
	@Autowired
	private PartyAttributeDao partyAttributeDao;
	@Autowired
	private PartyDao partyDao;
	
	
	@Value("${api.pay.serverUrlPrefix}")
	private String serverUrl;
	@Value("${customer.serverUrlPrefix}")
	private String customerUrl;
	@Value("${api.workflow.serverUrlPrefix}")
	private String workflowUrlPrefix;
	@Autowired
	private MsgSender msgSender;
	// portal
	@Value("${portal.serverUrlPrefix}")
	private String portalServerUrlPrefix;
	@Value("${mqConsumeConfig.sendMsgAndEmail.topicName}")
	private String sendMsgAndEmailTopicName;
	@Autowired
	private PartyGroupDao partyGroupDao;

	
	private static final String EXPORT_TEMPLATE = "注册手机,邮箱,联系人,会员状态,详细地址";
	public static final String SUCCESS = "success";
	
	private static final String CORPORATION_CATEGORY_ID = "CORPORATION_CATEGORY_ID";//公司类型
	private static final String CORPORATION_CATEGORY_ID_OTHER = "CORPORATION_CATEGORY_ID_OTHER";//公司类型(其他)
	private static final String INDUSTRY_CATEGORY_ID = "INDUSTRY_CATEGORY_ID";//所属行业
	private static final String INDUSTRY_CATEGORY_ID_OTHER = "INDUSTRY_CATEGORY_ID_OTHER";//所属行业其它属性拓展属性
	private static final String WEBSITE_URL = "WEBSITE_URL";// 公司官网
	private static final String D_CODE = "D_CODE";// 邓氏编码
	private static final String CHINA = "china";
	private static final String UTF = "utf-8";
	
	@Autowired
	private AuthorizationUtil authorizationUtil;
	
	@Autowired
	private WorkflowClientBuilder workflowClientBuilder;
	
	
	/**
	 * 获取个人基本信息详情
	 * @param id
	 * @return UserExtendVo
	 * @since 2016年1月12日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException
	 */
	public UserExtendVo getBaseInfoDetail(String id , String loginId) {
		UserExtendVo user = new UserExtendVo();
		String partyId = id;
	    //判断是企业还是个人 //如果userType为1为个人，否则为企业
		Integer userType = partyRoleDao.isPersonal(partyId);
		user.setUserType(userType);
		if (userType==0) {
			user.setMail(RequestHelper.getLoginUser().getUsername());
			user.setEntName(partyGroupDao.getEnterpriseNameByPartyId(partyId));
		}else if(userType==1){
			user.setMobile(RequestHelper.getLoginUser().getUsername());
		}
		// 获取个人基本信息的姓名，性别
		Party party = personDao.findPersonById(partyId);
		if (null != party && null != party.getPerson()) {
			// 设置姓名、性别的值
			user.setSex(String.valueOf(party.getPerson().getGender()));
			user.setName(party.getPerson().getLastNameLocal());
			// 邮箱
			user.setMail(party.getPerson().getMail());
			user.setTelNumber(party.getPerson().getTel());
			user.setPersonalTitle(party.getPerson().getPersonalTitle());
			user.setLogoUrl(party.getPerson().getLogoImageUrl());
			user.setMailStatus(party.getPerson().getMailStatus());
			user.setTelStatus(party.getPerson().getTelStatus());
			user.setPersonType(party.getPerson().getPersonTypeStatus()==null?null:party.getPerson().getPersonTypeStatus().toString());
			user.setFixedTel(party.getPerson().getFixedTel());
		}
		
		//调用服务获取个人地址信息
		List<PartyContactMech> partyContactMechList = partyContactMechManager.selectPartyContactMechByType(PurposeType.REGISTER_LOCATION,partyId,null);
		//获取基本信息
		user = getContactMech(user, partyContactMechList);
		
		//获取登录信息
		if (!StringUtils.isEmpty(loginId)) {
			UserLogin login = userLoginDao.findEntityById(loginId);
			Party party1 = partyDao.getPartyDetail(partyId, null);
			user.setRegTime(login.getCreatedDate());
			user.setMobile(login.getId());
			user.setPartyId(partyId);
			user.setStatus(party1.getPartyStatus());
			user.setLastLoginTime(login.getLastUpdateDate());
			user.setLoginCount(login.getSucceedLogins()==null?0:login.getSucceedLogins());
		}
		return user;
	}

	/**
	 * 获取个人基本信息抽取方法
	 * @param id
	 * @return UserExtendVo
	 * @since 2016年1月12日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException
	 */
	private UserExtendVo getContactMech(UserExtendVo user, List<PartyContactMech> partyContactMechList) {
		if(CollectionUtils.isEmpty(partyContactMechList) || null == partyContactMechList.get(0) || null == partyContactMechList.get(0).getContactMech()){
			return user;
		}
		
		PartyContactMech partyContactMech = partyContactMechList.get(0);
		// 地址信息
		PostalAddress postalAddress = partyContactMech.getContactMech().getPostalAddress();
		if (null != postalAddress) {
			user.setAddress(postalAddress.getAddress1());
			user.setProvince(postalAddress.getProvinceGeoId());
			user.setProvinceName(postalAddress.getProvinceGeoName());
			user.setCity(postalAddress.getCityGeoId());
			user.setCityName(postalAddress.getCityGeoName());
			user.setCountry(postalAddress.getCountyGeoId());
			user.setCountryName(postalAddress.getCountyGeoName());
			user.setPostcode(postalAddress.getPostalCode());
		}

		// 获取QQ
		TelecomNumber telecomNumber = partyContactMech.getContactMech().getTelecomNumber();
		if (null != telecomNumber) {
			QqTelecomNumber qq = telecomNumber.getQqTelecomNumber();
			user.setQq(null != qq && org.apache.commons.lang.StringUtils.isNotEmpty(qq.getContactNumber()) ? qq.getContactNumber() : "");
		}
		return user;
	}
	
	
	/**
	 * 保存个人基本信息
	 * @param UserExtendVo
	 * @return UserExtendVo
	 * @since 2016年1月12日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException
	 */
	public void save(UserExtendVo userInfoVo) {
	    if(null !=userInfoVo){
			PartyContactMech partyContactMech = new PartyContactMech();
			partyContactMech.setPurposeType(PurposeType.REGISTER_LOCATION);
			ContactMech contactMech = new ContactMech();
			PostalAddress postalAddress = new PostalAddress();
			postalAddress.setAddress1(userInfoVo.getAddress());
			postalAddress.setPostalCode(userInfoVo.getPostcode());
			postalAddress.setCountryGeoName("中国");
			postalAddress.setCountryGeoId(CHINA);
			
			postalAddress.setProvinceGeoName(userInfoVo.getProvinceName());
			postalAddress.setProvinceGeoId(userInfoVo.getProvince());
			
			postalAddress.setCountyGeoName(userInfoVo.getCountryName());
			postalAddress.setCountyGeoId(userInfoVo.getCountry());
			
			postalAddress.setCityGeoName(userInfoVo.getCityName());
			postalAddress.setCityGeoId(userInfoVo.getCity());
			
			contactMech.setPostalAddress(postalAddress);
			
			//企业
			contactMech.setEmail(userInfoVo.getMail());
			//电话信息
			TelecomNumber telecomNumber = new TelecomNumber();
			//设置手机信息
			MobileTelecomNumber mobile = new MobileTelecomNumber();
			mobile.setContactNumber(userInfoVo.getMobile());
			mobile.setAskForName(userInfoVo.getName());
			telecomNumber.setMobileTelecomNumber(mobile);
		
			
			//qq
			QqTelecomNumber qqTelecomNumber = new QqTelecomNumber();
			qqTelecomNumber.setContactNumber(userInfoVo.getQq());
			qqTelecomNumber.setMechType(MechType.QQ);
			telecomNumber.setQqTelecomNumber(qqTelecomNumber);
			contactMech.setTelecomNumber(telecomNumber);
			 // 当前登录用户
		    String partyId = RequestHelper.getLoginUserId();
		    if (!StringUtils.isEmpty(userInfoVo.getPartyId())) {
		    	partyId = userInfoVo.getPartyId();
		    	partyContactMech.setPartyId(userInfoVo.getPartyId());
			}
			partyContactMech.setContactMech(contactMech);
			partyContactMechManager.insert(partyContactMech);
			
			Party party =personDao.findPersonById(partyId);
			//因为在数据查询不能穿vo需传实体，所以重新给party设置值
			Party partyNew = new Party();
			Person person = new Person();
			if(!StringUtils.isEmpty(userInfoVo.getSex())&&!"null".equals(userInfoVo.getSex())){
				person.setGender(Integer.valueOf(userInfoVo.getSex()));
			}
			person.setLastNameLocal(userInfoVo.getName());
			person.setPersonalTitle(userInfoVo.getPersonalTitle());
		
			//修改或者插入到个人表中
			if(null != party){
				partyNew.setId(partyId);
				person.setLastUpdateDate(new Date());
				person.setLastUpdateUser(partyId);
				person.setMail(userInfoVo.getMail());
				person.setTel(userInfoVo.getMobile());
				person.setFixedTel(userInfoVo.getFixedTel());
				partyNew.setPerson(person);
				personDao.editPerson(partyNew);
			}
			if (userInfoVo.getStatus()!=null) {
				Party party2 = new Party();
				party2.setPartyStatus(userInfoVo.getStatus());
				party2.setId(partyId);
				party2.setLastUpdateDate(new Date());
				party2.setLastUpdateUser(RequestHelper.getLoginUserId());
				partyDao.updateParty(party2);
			}
	    }
	}

	
	/**
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
	 * @param rowBounds
	 * @return
	 * @since 2017年3月4日
	 * @author guowenyao@yikuyi.com
	 */
	/*@Cacheable(value="allUserInfoCache",key="'adminFindCustomerUser.Page[phone:' + #phone+ ', mail:' + #mail +'name:' + #name+'status:'+#status+'registerStart:'+#registerStart+'registerEnd:'+#registerEnd+'lastLoginStart:'+#lastLoginStart+'lastLoginEnd:'+#lastLoginEnd+'page:'+#page+'size:'+#size")*/
	public PageInfo<UserExtendVo> findCustomerUser(EnterpriseParamVo param, RowBounds rowBounds){
		return new PageInfo<>(userLoginDao.findCustomerUser(param,rowBounds));
	}
	
	/**
	 * 更新用户信息
	 * @param userInfoVo
	 * @since 2017年2月14日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public void update(UserExtendVo userInfoVo) {
		String partyId = userInfoVo.getId();
		Party party = new Party();
		party.setId(partyId);
		Person person = new Person();
		person.setLogoImageUrl(userInfoVo.getLogoUrl());
		person.setLogoImageUrlSmall(userInfoVo.getLogoUrl());
		person.setLastUpdateDate(new Date());
		person.setLastUpdateUser(partyId);
		person.setTelStatus(userInfoVo.getTelStatus());
		person.setMailStatus(userInfoVo.getMailStatus());
		party.setPerson(person);
		personDao.editPerson(party);
	}
	/**
	 * 根据状态查询用户
	 * @param relationSratus
	 * @return
	 * @since 2017年2月15日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public List<UserVo> getPersons(RelationSratus relationSratus,String partyId) {
		List<UserVo> list = new ArrayList<>();
		PartyRelationship partyRelationship = PartyRelationship.build(PartyRelationshipTypeEnum.COS_PLAY);
		partyRelationship.setPartyIdFrom(partyId);
		List<PartyRelationship> shipList = partyRelationshipDao.getPartyRelationship(partyRelationship);
		if(CollectionUtils.isNotEmpty(shipList)){
			partyRelationship = shipList.get(0);
			Party party = new Party();
			party.setId(partyId);
			party.setCorporationId(partyRelationship.getPartyIdTo());
			Person person = new Person();
			person.setRelationSratus(relationSratus);
			party.setPerson(person);
			list = personDao.getPersons(party);
		}
		return list;
	}

	/**
	 * 根据用户Id更新用户状态信息
	 * @param id
	 * @since 2017年2月15日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public void updateById(String id, RelationSratus relationSratus,String login) {
		Party party = new Party();
		party.setId(id);
		Person person = new Person();
		person.setRelationSratus(relationSratus);
		person.setLastUpdateDate(new Date());
		person.setLastUpdateUser(login);
		party.setPerson(person);
		personDao.editPerson(party);
		if((RelationSratus.NOT_RELATED).equals(relationSratus)){
			party.setPartyStatus(PartyStatus.PARTY_DISABLED);
			partyDao.updateParty(party);
			//发送邮件
			sendMail(id,login);
		}
		if((RelationSratus.RELATED).equals(relationSratus)){
			party.setPartyStatus(PartyStatus.PARTY_ENABLED);
			partyDao.updateParty(party);
		}
	}

	public void sendMail(String id, String login) {
		//查询邮箱
		String email = userLoginDao.findUserLogin(id,"EMAIL");
		String loginEmail = userLoginDao.findUserLogin(login,"EMAIL");
		//查询关联企业
		PartyRelationship partyRelationship = PartyRelationship.build(PartyRelationshipTypeEnum.EMPLOYMENT);
		partyRelationship.setPartyIdFrom(login);
		List<PartyRelationship> relationshipList = partyRelationshipDao.getPartyRelationship(partyRelationship);
		String entName ="";
		if(CollectionUtils.isNotEmpty(relationshipList)){
			partyRelationship = relationshipList.get(0);
			//企业id
			String entId = partyRelationship.getPartyIdTo();
			
			if(!StringUtils.isEmpty(entId)){
				Party party = partyGroupDao.findPartyGroupByPartyId(entId);
				if(party != null && party.getPartyGroup() != null){
					entName = party.getPartyGroup().getGroupName();
				}
			}
			
		}
		 MailInfoVo mailInfoVo = new MailInfoVo();
		  mailInfoVo.setTemplateId(MailInfoVo.TemplateId.RELIEVE_RELEVANCY.toString());
		  mailInfoVo.setType(MailInfoVo.Type.EMAIL.toString());
		  mailInfoVo.setTo(email);
		  mailInfoVo.setOperateUser(loginEmail);
		  JSONObject content = new JSONObject();
		  content.put("companyName", entName);
		  content.put("operateUser", loginEmail);
		  mailInfoVo.setContent(content);

		  msgSender.sendMsg(sendMsgAndEmailTopicName,mailInfoVo, null);
	}

	/**
	 * 根据用户名称查询用户列表
	 * @param username
	 * @return
	 * @since 2017年2月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public List<UserVo> getUserByName(String username) {
		Party party = new Party();
		Person person = new Person();
		person.setLastNameLocal(username);
		party.setPerson(person);
		return personDao.getUser(party);
	}

	/**
	 * 根据用户名称精确查询用户列表
	 * @param username
	 * @return
	 * @since 2017年2月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public UserVo getUsersByName(String name) {
		Party party = new Party();
		Person person = new Person();
		person.setLastNameLocal(name);
		party.setPerson(person);
		return personDao.getUsers(party);
	}
	
	/**
	 * 根据用户ID获取person信息
	 * @param userId
	 * @return
	 * @since 2017年6月29日
	 * @author tb.yumu@yikuyi.com
	 */
	public Person findPersonInfoByPartyId(String userId){
		Party party = personDao.getPersonByUserId(userId);
		if (party!=null) {
			return party.getPerson();
		}
		return new Person();
	}
	
	/**
	 * 根据用户ID获取登录密码，登录ID
	 * @param partyId
	 * @return
	 * @since 2017年6月29日
	 * @author tb.yumu@yikuyi.com
	 */
	public UserLogin getUserLoginByPartyid(String partyId){
		List<UserLogin> loginList = userLoginDao.findUserPassword(partyId);
		if (CollectionUtils.isNotEmpty(loginList)) {
			return loginList.get(0);
		}
		return new UserLogin();
	}
	/**
	 * 导出个人会员列表
	 * @param vo
	 * @return
	 * @since 2017年4月13日
	 * @author zr.aoxianbing@yikuyi.com
	 * @throws IOException 
	 */
	public void exportUser(EnterpriseParamVo vo, HttpServletResponse response) throws IOException {
		// 查询数据
		List<UserVo> list = personDao.getUserList(vo);
		response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");  
   	    response.addHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", "UserList.xls"));  
 		response.addHeader("Pragma", "no-cache");  
 		response.addHeader("Expires", "0"); 
 		this.exportExcelXls(list, response.getOutputStream());
		
	}
	/**
	 * 导出企业会员数据
	 * @param list
	 * @param os
	 * @since 2017年4月13日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	 void exportExcelXls(List<UserVo> list, OutputStream os) {
		ExportProcesser  processer = null;
		try{
        	processer = ExportFactory.getProcesser(ExportFactory.TYPE_XLS, os);
        	//标题
            processer.writeLine("Sheet1", EXPORT_TEMPLATE.split(","));
            List<List<String>> rowDataList;
        	rowDataList = this.userDataList(list);
        	for(List<String> rowData:rowDataList){
            	processer.writeLine("Sheet1", rowData);
        	}
        	processer.output();
		} catch (Exception e) {
			logger.error("导出企业会员数据：{}",e);
		} finally {
			if(null!=processer){
				processer.close();
			}
		}
		
	}
	/**
	 * 重构数据
	 * @param UserVo
	 * @return
	 * @since 2017年4月13日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	private List<List<String>> userDataList(List<UserVo> list) {
		List<List<String>> rowDataList = Lists.newArrayList();
		if(CollectionUtils.isEmpty(list)){
			return rowDataList;
		}
		List<String> rowData ;
		for(UserVo userVo:list){
			rowData = Lists.newArrayList();
			//注册手机
			rowData.add(userVo.getMobile());
			//邮箱
			rowData.add(userVo.getMail());
			//联系人
			rowData.add(userVo.getName());
			//会员状态
			rowData.add(userVo.getStatusId());
			//详细地址
			rowData.add(userVo.getAddress());
			rowDataList.add(rowData);
			
		}
		return rowDataList;
	}

	/**
	 * 发送邮件（更新账号，验证账号）
	 * @param userVo
	 * @since 2017年2月9日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public String sendMail(UserVo userVo) {
		String flag = SUCCESS;
		try {
			logger.info("修改账号邮件：{}"+userVo.getMail());
			//parms :第一个参数：发送时间    第二个参数：邮箱    第三个参数：验证码  第四个参数：修改类型
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			String senddate = format.format(new Date());
			String mail = userVo.getMail();
			String imgCode = userVo.getImgCode();
			String type = userVo.getType();
			String partyId = userVo.getId();
			String sid = senddate + "---" + mail + "---" + imgCode + "---" + type + "---" + partyId;
			byte[] b=Base64.encodeBase64(sid.getBytes(UTF),true);
			sid = new String(b,UTF);
			String url = customerUrl+"/accountSafe.htm?action=fromMail&sid="+sid;
			MailInfoVo mailInfoVo = new MailInfoVo();
			mailInfoVo.setTemplateId("VALIDATE_MAIL");
			mailInfoVo.setType(MailInfoVo.Type.EMAIL.toString());
			mailInfoVo.setTo(userVo.getMail());
			JSONObject content = new JSONObject();
			content.put("url", url);
			content.put("mail", mail);
			String logoPrefix = portalServerUrlPrefix;// portal项目前缀
			content.put("portalUrl", logoPrefix);// 公司logoUrl
			mailInfoVo.setContent(content);
			msgSender.sendMsg(sendMsgAndEmailTopicName, mailInfoVo, null);
			logger.info("更新账号，验证账号发送邮件MailInfoVo:{}"+JSONObject.toJSON(mailInfoVo).toString());
		} catch (Exception e) {
			flag = "sendMailFail";
			logger.error("发送更新账号，验证账号邮件MQ出错:{}", e);
		}
		return flag;
	}
	/**
	 * 注销账号
	 * @param accountId
	 * @param userId
	 * @return
	 * @since 2017年5月8日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public String cancelAccount(String accountId, String userId) {
		if(StringUtils.isEmpty(accountId) || StringUtils.isEmpty(userId)){
			return "accountOrUserIdEmpty";
		}
		Party party = partyDao.getPartyPersonDetail(userId);
		if(party == null){
			return "userNotExsit";
		}
		UserLogin userLogin = userLoginDao.findEntityById(accountId);
		if(userLogin==null || null == userLogin.getParty()){
			return "accountNotExsit";
		}
		String subId = userLogin.getParty().getId();
		String entId = party.getCorporationId();
		if(!StringUtils.isEmpty(entId)){
			//失效之前的企业关系
			PartyRelationship partyRelationship = PartyRelationship.build(PartyRelationshipTypeEnum.EMPLOYMENT);
			partyRelationship.setPartyIdFrom(subId);
			partyRelationship.setPartyIdTo(entId);
			partyRelationship.setThruDate(new Date());
			partyRelationship.setStatusId(PartyRelationshipStatus.DISABLED);
			partyRelationshipDao.updateRelationShip(partyRelationship);
		}
		//失效主账号关系
		PartyRelationship partyRelationship = PartyRelationship.build(PartyRelationshipTypeEnum.REPORTS_TO);
		partyRelationship.setPartyIdFrom(subId);
		partyRelationship.setPartyIdTo(userId);
		partyRelationship.setThruDate(new Date());
		partyRelationship.setStatusId(PartyRelationshipStatus.DISABLED);
		partyRelationshipDao.updateRelationShip(partyRelationship);

		// 失效账号
		party.setId(subId);
		party.setPartyStatus(PartyStatus.PARTY_DISABLED);
		party.setLastUpdateDate(new Date());
		party.setLastUpdateUser(userId);
		partyDao.updateParty(party);
		return SUCCESS;
	}
	/**
	 * 冻结账号
	 * @param accountId
	 * @param userId
	 * @return
	 * @since 2017年5月8日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public String frozenAccount(Party.PartyStatus partyStatus,String accountId, String userId) {
		Party party = new Party();
		// 失效账号
		party.setId(accountId);
		party.setPartyStatus(partyStatus);
		party.setLastUpdateDate(new Date());
		party.setLastUpdateUser(userId);
		partyDao.updateParty(party);
		return SUCCESS;
	}
	/**
	 * 新增用户账号（后台）
	 * @param userVo
	 * @since 2017年4月28日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Audit(action = "Enterprise Addqqq;;;'#userVo.id'qqq;;;'#userVo.reason'", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public Map<String,String> creatAccount(UserVo userVo) {
		Map<String,String> map = new HashMap<>();
		//1.保存企业信息
		String entId = this.createEnt(userVo);
		//2.保存账号信息
		String userId = this.createPerson(userVo,entId);
		//3.保存企业与账号的关联关系
		saveRelationShip(userId,entId);
		map.put("entId", entId);
		map.put("userId", userId);
		return map;
	}
	
	/**
	 * 保存企业信息
	 * @param userVo
	 * @return
	 * @since 2017年4月28日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	private String createEnt(UserVo userVo) {
		String id = String.valueOf(IdGen.getInstance().nextId());
		Party party = new Party();
		party.setId(id);
		party.setPartyStatus(PartyStatus.PARTY_ENABLED);
		party.setPartyType(PartyType.CORPORATION);
		party.setCreator(userVo.getId());
		party.setCreatedDate(new Date());
		party.setLastUpdateDate(new Date());
		party.setLastUpdateUser(userVo.getId());
		partyDao.insert(party);

		PartyGroup partyGroup = new PartyGroup();
		partyGroup.setGroupName(userVo.getCompanyName());
		partyGroup.setCreator(userVo.getId());
		partyGroup.setCreatedDate(new Date());
		partyGroup.setLastUpdateUser(userVo.getId());
		partyGroup.setLastUpdateDate(new Date());
		partyGroup.setActiveStatus(ActiveStatus.PARTY_NOT_VERIFIED);
		partyGroup.setAccountStatus(AccountStatus.ACCOUNT_NOT_VERIFIED);
		partyGroup.setComments(userVo.getComments());
		party.setPartyGroup(partyGroup);
		partyGroupDao.insert(party);

		// 插入属性表,公司类型
		PartyAttributes corCategoryAttrs = new PartyAttributes();
		PartyAttribute corCategoryAttr = new PartyAttribute();
		corCategoryAttr.setKey(CORPORATION_CATEGORY_ID);
		corCategoryAttr.setValue(userVo.getCorCategory());
		corCategoryAttrs.setCorporationCategory(corCategoryAttr);
		party.setPartyAttributes(corCategoryAttrs);
		partyAttributeDao.insertCorporationCategory(party);
		//公司类型(其他)
		if(!StringUtils.isEmpty(userVo.getCorCategoryOther())){
			PartyAttributes corCategoryOtherAttrs = new PartyAttributes();
			PartyAttribute corCategoryOtherAttr = new PartyAttribute();
			corCategoryOtherAttr.setKey(CORPORATION_CATEGORY_ID_OTHER);
			corCategoryOtherAttr.setValue(userVo.getCorCategoryOther());
			corCategoryOtherAttrs.setCorporationCategory(corCategoryOtherAttr);
			party.setPartyAttributes(corCategoryOtherAttrs);
			partyAttributeDao.insertCorporationCategory(party);
		}
		// 插入属性表,所属行业
		PartyAttributes industryAttrs = new PartyAttributes();
		PartyAttribute industryAttr = new PartyAttribute();
		industryAttr.setKey(INDUSTRY_CATEGORY_ID);
		industryAttr.setValue(userVo.getIndustryCategory());
		industryAttrs.setIndustryCategory(industryAttr);
		party.setPartyAttributes(industryAttrs);
		partyAttributeDao.insertIndustryCategory(party);
		//设置other值  
		PartyAttributes otherAttrs = new PartyAttributes();
		PartyAttribute otherAttr = new PartyAttribute();
		otherAttr.setKey(INDUSTRY_CATEGORY_ID_OTHER);
		otherAttr.setValue(userVo.getOtherAttr());
		otherAttrs.setOtherAttrs(otherAttr);
		party.setPartyAttributes(otherAttrs);
		partyAttributeDao.insertOtherAttrs(party);
		//公司官网
		insertCer(WEBSITE_URL, userVo.getWebSite(), party, userVo.getId());
		//邓氏编码
		insertCer(D_CODE, userVo.getdCode(), party, userVo.getId());
		//创建企业注册地址
		this.saveEntAddr(userVo.getEnterpriseVo(),id);
		return id;
	}
	 void insertCer(String key, String value, Party party, String userId) {
		PartyAttributes loaAttrs = new PartyAttributes();
		PartyAttribute loaAttr = new PartyAttribute();
		loaAttr.setKey(key);
		loaAttr.setValue(value);
		loaAttr.setCreator(userId);
		loaAttr.setCreatedDate(new Date());
		loaAttrs.setLoa(loaAttr);
		party.setPartyAttributes(loaAttrs);
		partyAttributeDao.insertLoa(party);
	}
	/**
	 * 创建个人注册地址
	 * @param userVo
	 * @param entId
	 * @since 2017年4月28日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	private void saveAddr(UserVo userVo,String id) {
		PartyContactMech partyContactMech = new PartyContactMech();
		partyContactMech.setPurposeType(PurposeType.REGISTER_LOCATION);
		ContactMech contactMech = new ContactMech();
		PostalAddress postalAddress = new PostalAddress();
		postalAddress.setAddress1(userVo.getAddress());
		postalAddress.setCountryGeoName("中国");
		postalAddress.setCountryGeoId(CHINA);

		postalAddress.setProvinceGeoName(userVo.getProvinceName());
		postalAddress.setProvinceGeoId(userVo.getProvince());

		postalAddress.setCountyGeoName(userVo.getCountryName());
		postalAddress.setCountyGeoId(userVo.getCountry());

		postalAddress.setCityGeoName(userVo.getCityName());
		postalAddress.setCityGeoId(userVo.getCity());

		contactMech.setPostalAddress(postalAddress);
		//qq
		TelecomNumber telecomNumber = new TelecomNumber();
		QqTelecomNumber qqTelecomNumber = new QqTelecomNumber();
		qqTelecomNumber.setContactNumber(userVo.getQq());
		qqTelecomNumber.setMechType(MechType.QQ);
		telecomNumber.setQqTelecomNumber(qqTelecomNumber);
		
		contactMech.setTelecomNumber(telecomNumber);
		
		partyContactMech.setContactMech(contactMech);
		partyContactMech.setPartyId(id);
		partyContactMechManager.insert(partyContactMech);
		
	}
	/**
	 * 创建企业注册地址
	 * @param userVo
	 * @param entId
	 * @since 2017年4月28日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	private void saveEntAddr(EnterpriseVo entVo, String partyId) {
		if (null != entVo) {
			PartyContactMech partyContactMech = new PartyContactMech();
			partyContactMech.setPurposeType(PurposeType.REGISTER_LOCATION);
			ContactMech contactMech = new ContactMech();
			PostalAddress postalAddress = new PostalAddress();
			postalAddress.setAddress1(entVo.getAddress());
			postalAddress.setCountryGeoName("中国");
			postalAddress.setCountryGeoId(CHINA);

			postalAddress.setProvinceGeoName(entVo.getProvinceName());
			postalAddress.setProvinceGeoId(entVo.getProvince());

			postalAddress.setCountyGeoName(entVo.getCountryName());
			postalAddress.setCountyGeoId(entVo.getCountry());

			postalAddress.setCityGeoName(entVo.getCityName());
			postalAddress.setCityGeoId(entVo.getCity());

			contactMech.setPostalAddress(postalAddress);

			partyContactMech.setContactMech(contactMech);
			partyContactMech.setPartyId(partyId);
			partyContactMechManager.insert(partyContactMech);

		}
	}
	
	/**
	 * 保存账号信息
	 * @param userVo
	 * @return
	 * @since 2017年4月28日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	private String createPerson(UserVo userVo,String entId) {
		//1.保存账号信息
		String id = String.valueOf(IdGen.getInstance().nextId());
		Party party = new Party();
		party.setId(id);
		party.setPartyType(PartyType.PERSON);
		party.setPartyStatus(PartyStatus.PARTY_DISABLED);
		party.setCreator(userVo.getId());
		party.setCreatedDate(new Date());
		party.setLastUpdateDate(new Date());
		party.setLastUpdateUser(userVo.getId());
		party.setCorporationId(entId);
		partyDao.insert(party);
        //PERSON 表生成记录 PARTY_ID 和  PARTY表一致。
		Person person = new Person();
		person.setLastNameLocal(userVo.getName());
		person.setMail(userVo.getMail());
		person.setPersonalTitle(userVo.getPersonalTitle());
		person.setTel(userVo.getMobile());
		person.setTelStatus("Y");
		person.setMailStatus("Y");
		person.setCreator(userVo.getId());
		person.setCreatedDate(new Date());
		person.setLastUpdateDate(new Date());
		person.setLastUpdateUser(userVo.getId());
		person.setPersonTypeStatus(PersonTypeStatus.COMMON);
		party.setPerson(person);
		personDao.insert(party);
		//c.生成登录数据USER_LOGIN  字段： ENABLED = ‘Y’ , PARTY_ID 和 PARTY表一致， USER_LOGIN_ID =  刚刚注册的手机号。
		UserLogin userLogin = new UserLogin();
		userLogin.setId(userVo.getMail());
		userLogin.setParty(party);
		userLogin.setEnabled("N");
		userLogin.setIsSystem("N");
		userLogin.setRequirePasswordChange("N");
		userLogin.setUserLoginMethod(UserLoginMethod.EMAIL.toString());
		userLogin.setCreator(userVo.getId());
		userLogin.setCreatedDate(new Date());
		userLogin.setLastUpdateUser(userVo.getId());
		userLogin.setLastUpdateDate(new Date());
		userLoginDao.insert(userLogin);
        
        //d.生成 PARTY_ROLE 数据  ROLE_TYPE_ID 为：REGISTER
		partyRoleDao.insert(id, RoleTypeEnum.ENTERPRISE_CUST.toString(),userVo.getId(),new Date(),userVo.getId(),new Date());
		
		this.saveAddr(userVo, id);
		return id;
	}
	
	/**
	 * 保存企业与账号的关联关系
	 * @param userId
	 * @param id
	 * @since 2017年4月28日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	private void saveRelationShip(String userId, String id) {
		// 插入企业与注册用户的关联关系 主账号
		PartyRelationship relationShip = PartyRelationship.build(PartyRelationshipTypeEnum.EMPLOYMENT);
		relationShip.setPartyIdFrom(userId);// 当前的登录用户partyId
		relationShip.setPartyIdTo(id);// 企业partyId
		relationShip.setFromDate(new Date());
		relationShip.setCreator(userId);
		relationShip.setCreatedDate(new Date());
		relationShip.setLastUpdateUser(userId);
		relationShip.setLastUpdateDate(new Date());
		partyRelationshipDao.insert(relationShip);
	}

	/**
	 * 批量添加子账号
	 * @param accounts
	 * @return
	 * @since 2017年5月11日
	 * @author zr.aoxianbing@yikuyi.com
	 * @throws BusinessException 
	 */
	public String addSubAccount(String userId,List<String> accounts) throws BusinessException {
		if(CollectionUtils.isEmpty(accounts) || StringUtils.isEmpty(userId)){
			throw new BusinessException(PartyBusiErrorCode.ACCOUNT_SON_EMPTY);
		}
		//判断用户是否为主账号，如果不是返回
		PartyRelationship partyRelationship = PartyRelationship.build(PartyRelationshipTypeEnum.COS_PLAY);
		partyRelationship.setPartyIdFrom(userId);
		List<PartyRelationship> shipList = partyRelationshipDao.getPartyRelationship(partyRelationship);
		if(CollectionUtils.isEmpty(shipList)){
			throw new BusinessException(PartyBusiErrorCode.ACCOUNT_NOT_MAIN);
		}
		//获取当前用户的企业id
		PartyRelationship ship = shipList.get(0);
		String entId = ship.getPartyIdTo();
		if(StringUtils.isEmpty(entId)){
			throw new BusinessException(PartyBusiErrorCode.ACCOUNT_ENTID_EMPTY);
		}
		
		//判断是否已经添加到主账号或者企业
		for(String account:accounts){
			//获取用户id
			UserLogin userLogin = userLoginDao.findEntityById(account);
			if(userLogin == null){
				continue;
			}
			String partyId = userLogin.getParty().getId();
			PartyRelationship relationship = PartyRelationship.build(PartyRelationshipTypeEnum.COS_PLAY);
			relationship.setPartyIdFrom(partyId);
			List<PartyRelationship> list = partyRelationshipDao.getPartyRelationship(relationship);
			if(CollectionUtils.isNotEmpty(list)){
				throw new BusinessException(PartyBusiErrorCode.ACCOUNT_MAIN_REJOINED);
			}
			//判断是不是已经加入企业
			PartyRelationship partyRelationship1 = PartyRelationship.build(PartyRelationshipTypeEnum.REPORTS_TO);
			partyRelationship1.setPartyIdFrom(partyId);
			List<PartyRelationship> shipList1 = partyRelationshipDao.getPartyRelationship(partyRelationship1);
			if(CollectionUtils.isNotEmpty(shipList1)){
				throw new BusinessException(PartyBusiErrorCode.ACCOUNT_SON_REJOINED);
			}
		}
		
		for(String account:accounts){
			//启动关联流程
			String applyId = this.submitWorkflow(userId, entId, account);
			//发送邮件
			if(!StringUtils.isEmpty(applyId)){
				this.sendAccountMail(userId,account, entId,applyId);
			}
		}
		return SUCCESS;
	}

	/**
	 * 提交流程
	 * @param userId
	 * @param entId
	 * @param account
	 * @since 2017年5月11日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	private String submitWorkflow(String userId, String entId, String account) {
		Apply apply = new Apply();
		apply.setApplyUserId(userId);
		apply.setProcessId("ORG_SUB_ACCOUNT_REVIEW");
		apply.setApplyOrgId(entId);
		UserVo vo = new UserVo();
		vo.setMail(account);
		String applyId = null;
		try{
			JSONObject json = (JSONObject)JSON.toJSON(vo);
			apply.setApplyContent(json.toString());
			// 调用workFlow服务
			applyId = workflowClientBuilder.applyClient().createApply(apply, authorizationUtil.getLoginAuthorization());
		    
		}catch(Exception e){
			logger.error("提交添加子账号流程出错:{}", e);
		}

		return applyId;
	}
	
	/**
	 * 加入主账号邮件
	 * @param account
	 * @param entId
	 * @return
	 * @since 2017年5月11日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public String sendAccountMail(String userId,String account,String entId,String applyId) {
		String flag = SUCCESS;
		try {
			logger.info("给用户发送加入主账号邮件:{}"+account);
			//parms :第一个参数：发送时间    第二个参数：邮箱    第三个参数：验证码
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			String senddate = format.format(new Date());
			String sid = senddate + "---" + account + "---" + entId + "---" + applyId;
			byte[] b=Base64.encodeBase64(sid.getBytes(UTF),true);
			sid = new String(b,UTF);
			String url = customerUrl+"/reg.htm?action=fromMailJoin&sid="+sid;
			//账号
			Party party = personDao.findPersonById(userId);
			String loginId = "";
			if(party != null && party.getPerson() != null ){
				loginId = party.getPerson().getMail();
			}
			String entName = "";
			Party p = partyDao.getPartyDetail(entId, null);
			if(p != null && p.getPartyGroup() != null ){
				entName = p.getPartyGroup().getGroupName();
			}
			
			MailInfoVo mailInfoVo = new MailInfoVo();
			mailInfoVo.setTemplateId("ADD_SUB_ACCOUNT");
			mailInfoVo.setType(MailInfoVo.Type.EMAIL.toString());
			mailInfoVo.setTo(account);
			JSONObject content = new JSONObject();
			content.put("url", url);
			content.put("loginId", loginId);
			content.put("entName", entName);
			content.put("mail", account);
			String logoPrefix = portalServerUrlPrefix;// portal项目前缀
			content.put("portalUrl", logoPrefix);// 公司logoUrl
			mailInfoVo.setContent(content);
			msgSender.sendMsg(sendMsgAndEmailTopicName, mailInfoVo, null);
			logger.info("给用户发送加入主账号邮件MailInfoVo:{}"+JSONObject.toJSON(mailInfoVo).toString());
		} catch (Exception e) {
			flag = "sendMailFail";
			logger.error("加入主账号邮件MQ出错:{}", e);
		}
		return flag;
	}
	
	/**
	 * [后端]用户管理-用户启用/停用/删除接口
	 * @param partyId
	 * @param statusId
	 * @return
	 * @since 2017年5月10日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	public String updateStateId(String partyId, String statusId) {
		
		if("PARTY_ENABLED".equals(statusId) || "PARTY_DISABLED".equals(statusId)){
			partyDao.updateStateId(partyId,statusId);
			return "SUCCEED";
		}
		return "FAILD";
	}
	
	
	/**
	 * 根据已验证邮箱查询数据
	 * @param username
	 * @return
	 * @since 2017年8月8日
	 * @author zr.helinmei@yikuyi.com
	 */
	public UserVo getUsersByMail(String mail) {
		Party party = new Party();
		Person person = new Person();
		person.setMail(mail);
		party.setPerson(person);
		return personDao.getUsers(party);
	}
	
	public List<UserParamVo> getCustomersByIds(List<String> ids) {
		List<UserParamVo> list = personDao.getCustomersByIds(ids);
		if(CollectionUtils.isNotEmpty(list)){
			list.stream().forEach(userParamVo ->{
				String name ="";
				//判断是个人用户还是企业用户
				if(StringUtils.isNotBlank(userParamVo.getUserType()) && "企业用户".equals(userParamVo.getUserType())){
					//查询企业公司名称
				   name =	personDao.getPartyGroupNameById(userParamVo.getPartyId());
				}else if(StringUtils.isNotBlank(userParamVo.getUserType()) && "个人用户".equals(userParamVo.getUserType())){
				   name =	personDao.getUserLoginIdById(userParamVo.getPartyId());
				}
				 userParamVo.setGroupName(name);
			});
		}
		return personDao.getCustomersByIds(ids);
	}
}