/*
 * Created: 2016年11月28日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.userLogin.bll;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.jasig.inspektr.audit.annotation.Audit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.framework.springboot.audit.Param;
import com.framewrok.springboot.web.RequestHelper;
import com.github.pagehelper.PageInfo;
import com.yikuyi.basedata.ShipmentClientBuilder;
import com.yikuyi.basedata.category.model.Category;
import com.yikuyi.message.mail.vo.MailInfoVo;
import com.yikuyi.party.acl.bll.ACLManager;
import com.yikuyi.party.contact.vo.AccountVo;
import com.yikuyi.party.contact.vo.EnterpriseVo;
import com.yikuyi.party.contact.vo.User;
import com.yikuyi.party.contact.vo.UserExtendVo;
import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.party.credit.dao.PartyCreditDao;
import com.yikuyi.party.credit.model.PartyCredit;
import com.yikuyi.party.credit.vo.PartyCreditVo;
import com.yikuyi.party.customer.bll.CustomerSummeryManager;
import com.yikuyi.party.customer.dao.PersonDao;
import com.yikuyi.party.enterprise.bll.EnterpriseManager;
import com.yikuyi.party.login.model.UserLogin;
import com.yikuyi.party.login.model.UserLogin.PwdStrength;
import com.yikuyi.party.login.model.UserLogin.UserLoginMethod;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.model.Party.PartyStatus;
import com.yikuyi.party.model.Party.PartyType;
import com.yikuyi.party.model.PartyAttribute;
import com.yikuyi.party.model.PartyAttributes;
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
import com.yikuyi.party.role.model.RoleTypeEnum;
import com.yikuyi.party.userLogin.dao.UserLoginDao;
import com.yikuyi.party.vendor.vo.Vendor.Currency;
import com.ykyframework.model.IdGen;
import com.ykyframework.mqservice.sender.MsgSender;

import net.sf.json.JSONArray;


@Service
@Transactional
public class UserLoginManager {
	
	private static final Logger logger = LoggerFactory.getLogger(UserLoginManager.class);
	
	@Autowired
	private UserLoginDao userLoginDao;
	@Autowired
	private CustomerSummeryManager customerSummeryManager;
	@Autowired
	private PartyRelationshipDao partyRelationshipDao;
	@Autowired
	private PartyDao partyDao;
	@Autowired
	private PartyGroupDao partyGroupDao;
	@Autowired
	private PersonDao personDao;
	@Autowired
	private PartyRoleDao partyRoleDao;
	@Autowired
	private PartyCreditDao partyCreditDao;
	
	@Autowired
	private ACLManager aCLManager;
	
	@Autowired
	private PartyAttributeDao partyAttributeDao;
	
	private static final String ROLETYPEFROM ="REPORTS_TO";
	private static final String SUCCESS ="success";
	private static final String YIKUYI_ENT_ID ="99999999";
	private static final String ENTERPRISE_CUST ="ENTERPRISE_CUST";
	
	private static final String UTF8 ="utf-8";
	
	
	@Value("${customer.serverUrlPrefix}")
	private String customerUrl;
	@Autowired
	private MsgSender msgSender;
	// portal
	@Value("${portal.serverUrlPrefix}")
	private String portalServerUrlPrefix;
	@Value("${mqConsumeConfig.sendMsgAndEmail.topicName}")
	private String sendMsgAndEmailTopicName;
	
	@Autowired
	private ShipmentClientBuilder shipmentClientBuilder;
	
	@Value("${mqConsumeConfig.userLoginFirstListenner.topicName}")
	private String userLoginFirstListennerTopicName;

	/**
	 * 根据账号判断是否存在（手机、邮箱）
	 * @param account
	 * @return
	 * @since 2017年1月12日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public Boolean isExist(String account) {
		//base64解密 
		String jaccount = null;
		try {
			byte[] result = Base64.decodeBase64(account);
			jaccount = new String(result);
		} catch (Exception e) {
			logger.error("base64解密  error:{},account:{}",e,account);
		}
		Integer count = userLoginDao.isExist(jaccount);
		if(count > 0){
			return true;
		}
		return false;
	}
	/**
	 * 根据账号判断是否存在（手机、邮箱）
	 * @param account
	 * @return
	 * @since 2017年1月12日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public Boolean getAccount(String account) {
		Integer count = userLoginDao.isExist(account);
		if(count > 0){
			return true;
		}
		return false;
	}
	/**
	 * 保存密码
	 * @param userVo
	 * @return
	 * @since 2017年1月19日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public UserVo initPassWord(UserVo userVo) {
//		//判断密码是否为空
		//查询用户是否存在，如果不存在报用户不存在的错误，如果存在查看是否有效，如果有效则不修改密码
		UserLogin user = userLoginDao.findEntityById(userVo.getMail());
		if(user == null){
			return userVo;
		}
		Party party = user.getParty();
		UserLogin userLogin = new UserLogin();
		userLogin.setId(userVo.getMail());
		userLogin.setEnabled("Y");
		userLogin.setParty(party);
		userLogin.setLastUpdateDate(new Date());
		userLoginDao.updateByPartyId(userLogin);
		party.setPartyStatus(PartyStatus.PARTY_ENABLED);
		party.setLastUpdateDate(new Date());
		partyDao.updateParty(party);
		return userVo;
	}
	/**
	 * 根据账号id查询用户
	 * @param userVo
	 * @return
	 * @since 2017年1月19日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public UserLogin getAccountById(String account) {
		return userLoginDao.findEntityById(account);
	}
	
	/**
	 * 根据partyid和类型查询用户
	 * @param id
	 * @param type
	 * @return
	 * @since 2017年3月16日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public String getAccountByIdAndType(String id,String type) {
		return userLoginDao.findUserLogin(id,type);
	}
	/**
	 *  查询账号列表
	 * @param name
	 * @return
	 * @since 2017年3月17日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public PageInfo<UserVo> search(String name,RowBounds rowBouds,String userId,int page,int size) {
		PageInfo<UserVo> pageInfo = new PageInfo<>();
		List<UserVo> userList = new ArrayList<>();
		pageInfo.setPageSize(size);
		pageInfo.setPageNum(page);
		if(StringUtils.isEmpty(userId)){
			pageInfo.setTotal(0);
			pageInfo.setPageNum(0);
			pageInfo.setList(userList);
			return pageInfo;
		}
		//如果name不为空 查询出对应的id
		List<String> userIds = null;
		if(!StringUtils.isEmpty(name)){
			userIds = userLoginDao.getUserIdsByName(name);
			//如果没有数据返回空
			if(userIds==null || userIds.isEmpty()){
				pageInfo.setTotal(0);
				pageInfo.setPageNum(0);
				pageInfo.setList(userList);
				return pageInfo;
			}
		}

		//根据查询条件查询所有的记录，计算出总记录
		List<String> allIds = partyRelationshipDao.getAllPartyRelationship(userId,userIds,RowBounds.DEFAULT);
		if(allIds.isEmpty()){
			pageInfo.setTotal(0);
			pageInfo.setPageNum(0);
			pageInfo.setList(userList);
			return pageInfo;
		}
		//根据分页去查询记录，将符合的记录查询出来
		List<String> ids = partyRelationshipDao.getAllPartyRelationship(userId,userIds,rowBouds);
		//根据查询出来的id集合去查询列表信息
		userList = userLoginDao.getUserLoginsByIds(ids);
		if(userList != null && !userList.isEmpty()){
			pageInfo.setTotal(allIds.size());
			pageInfo.setList(userList);
		}else{
			pageInfo.setTotal(0);
			pageInfo.setPageNum(0);
			pageInfo.setList(userList);
		}
		return pageInfo;
	}
	/**
	 * 新增账号(新增易库易账号)
	 * @param userVo
	 * @return
	 * @since 2017年3月17日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Audit(action = "User Modifyqqq;;;新增'#userVo.mail'账号", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public String save(@Param("userVo")UserVo userVo) {
		String flag;
		//判断之前是否注册过，并且注册没有成功（根据邮箱）
		UserLogin login = userLoginDao.findEntityById(userVo.getMail());
		if(login != null && "Y".equals(login.getEnabled())){
			flag = "exist";
			return flag;
		}
        //b.保存接口： PARTY表里面生成记录， PARTY_TYPE  设置为： PERSON ，STATUS_ID 
		saveEnterprise(userVo);
		flag = SUCCESS;
		return flag;
	}
	
	/**
	 * 保存(易库易账号)
	 * @param userVo
	 * @since 2017年3月17日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public void saveEnterprise(UserVo userVo) {
		String id = String.valueOf(IdGen.getInstance().nextId());
		Party party = new Party();
		party.setId(id);
		party.setPartyType(PartyType.PERSON);
		party.setPartyStatus(PartyStatus.PARTY_ENABLED);
		party.setCreator(userVo.getId());
		party.setCreatedDate(new Date());
		party.setLastUpdateDate(new Date());
		party.setLastUpdateUser(id);
		partyDao.insert(party);
        //PERSON 表生成记录 PARTY_ID 和  PARTY表一致。
		Person person = new Person();
		person.setLastNameLocal(userVo.getName());
		person.setCreator(userVo.getId());
		person.setLastUpdateUser(userVo.getId());
		person.setCreatedDate(new Date());
		person.setLastUpdateDate(new Date());
		person.setTel(userVo.getTelNumber());
		person.setMail(userVo.getMail());
		party.setPerson(person);
		personDao.insert(party);
		//c.生成登录数据USER_LOGIN  字段： ENABLED = ‘Y’ , PARTY_ID 和 PARTY表一致， USER_LOGIN_ID =  刚刚注册的手机号。
		UserLogin userLogin = new UserLogin();

		userLogin.setId(userVo.getMail());
		userLogin.setParty(party);
		userLogin.setCurrentPassword(userVo.getPassword());
		userLogin.setEnabled("Y");
		userLogin.setIsSystem("N");
		userLogin.setRequirePasswordChange("N");
		userLogin.setUserLoginMethod(UserLoginMethod.EMAIL.toString());
		userLogin.setCreator(userVo.getId());
		userLogin.setLastUpdateUser(userVo.getId());
		userLogin.setCreatedDate(new Date());
		userLogin.setLastUpdateDate(new Date());
		//密码强度 
		PwdStrength pwdStrength = customerSummeryManager.checkedPwdStrong(userVo.getPassword());
		userLogin.setPwdStrength(pwdStrength);
		
		UserLogin login = userLoginDao.findEntityById(userVo.getMail());
		if(login != null && "N".equals(login.getEnabled())){
			userLoginDao.update(userLogin);
		}else{
			userLoginDao.insert(userLogin);
		}
        
        //d.生成 PARTY_ROLE 数据  ROLE_TYPE_ID 为：REGISTER
		partyRoleDao.insert(id, RoleTypeEnum.OPERATION_REP.toString(),userVo.getId(),new Date(),userVo.getId(),new Date());
		//把这个用户挂靠到某个卖家 （关系）
		//获取买家
		PartyRelationship partyRelationship = PartyRelationship.build(PartyRelationshipTypeEnum.EMPLOYMENT);
		partyRelationship.setPartyIdFrom(userVo.getId());
		List<PartyRelationship> list = partyRelationshipDao.getPartyRelationship(partyRelationship);
		if(list != null && !list.isEmpty()){
			partyRelationship = list.get(0);
		}
		String entId = partyRelationship.getPartyIdTo();
		if(StringUtils.isEmpty(entId)){
			entId = YIKUYI_ENT_ID;
		}
		//账号和企业的关联关系
		this.saveEntRelationShip(userVo.getId(),id,entId);
	}
	
	/**
	 * 账号和企业的关联关系
	 * @param loginId
	 * @param userId
	 * @param id
	 * @since 2017年3月17日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	void saveEntRelationShip(String loginId,String userId, String id) {
		// 插入企业与注册用户的关联关系
		PartyRelationship relationShip = PartyRelationship.build(PartyRelationshipTypeEnum.EMPLOYMENT);
		relationShip.setPartyIdFrom(userId);// 当前的登录用户partyId
		relationShip.setPartyIdTo(id);// 企业partyId
		relationShip.setFromDate(new Date());
		relationShip.setThruDate(null);
		relationShip.setCreator(loginId);
		relationShip.setCreatedDate(new Date());
		relationShip.setLastUpdateUser(loginId);
		relationShip.setLastUpdateDate(new Date());
		partyRelationshipDao.insert(relationShip);
	}
	
	/**
	 * 根据id查询用户
	 * @param id
	 * @return
	 * @since 2017年3月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public UserVo getPerson(String id) {
		return userLoginDao.getPerson(id);
	}
	/**
	 * 修改账号
	 * @param userVo
	 * @return
	 * @since 2017年3月17日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Audit(action = "User Modifyqqq;;;修改'#userVo.mail'账号", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public String update(@Param("userVo")UserVo userVo) {
		String flag;
		Party party = new Party();
		party.setId(userVo.getId());
		party.setLastUpdateDate(new Date());
		party.setLastUpdateUser(userVo.getPartyId());
        //PERSON 表生成记录 PARTY_ID 和  PARTY表一致。
		Person person = new Person();
		person.setLastNameLocal(userVo.getName());
		person.setLastUpdateUser(userVo.getPartyId());
		person.setLastUpdateDate(new Date());
		person.setTel(userVo.getTelNumber());
		party.setPerson(person);
		personDao.editPerson(party);
		flag = SUCCESS;
		return flag;
	}
	/**
	 * 更新用户信息
	 * @param userInfoVo
	 * @since 2017年2月14日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public void updatePerson(UserExtendVo userInfoVo) {
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
		person.setMail(userInfoVo.getMail());
		person.setTel(userInfoVo.getTelNumber());
		party.setPerson(person);
		personDao.editPerson(party);
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
			logger.info("修改账号邮件:{}"+userVo.getMail());
			//parms :第一个参数：发送时间    第二个参数：邮箱    第三个参数：验证码  第四个参数：修改类型
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			String senddate = format.format(new Date());
			String mail = userVo.getMail();
			String imgCode = userVo.getImgCode();
			String type = userVo.getType();
			String partyId = userVo.getId();
			String sid = senddate + "---" + mail + "---" + imgCode + "---" + type + "---" + partyId;
			byte[] b=Base64.encodeBase64(sid.getBytes(UTF8),true);
			sid = new String(b,UTF8);
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
	 * 发送创建账号邮件
	 * @param userVo
	 * @return
	 * @since 2017年7月28日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public String sendCreateMail(UserVo userVo) {
		String flag = SUCCESS;
		try {
			logger.info("个人升级企业再次发邮件:{}"+userVo.getMail());
			//parms :第一个参数：发送时间    第二个参数：邮箱    第三个参数：用户id
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			String senddate = format.format(new Date());
			String mail = userVo.getMail();
			String partyId = userVo.getId();
			String sid = senddate + "---" + mail + "---" + partyId;
			byte[] b=Base64.encodeBase64(sid.getBytes(UTF8),true);
			sid = new String(b,UTF8);
			String url = customerUrl+"/accountSafe.htm?action=fromMailCreate&sid="+sid;
			MailInfoVo mailInfoVo = new MailInfoVo();
			mailInfoVo.setTemplateId("PERSON_UP_ENT");
			mailInfoVo.setType(MailInfoVo.Type.EMAIL.toString());
			mailInfoVo.setTo(userVo.getMail());
			JSONObject content = new JSONObject();
			content.put("userName", userVo.getMail());// 用户名
			content.put("linkUrl", url);// 链接
			mailInfoVo.setContent(content);
			msgSender.sendMsg(sendMsgAndEmailTopicName, mailInfoVo, null);
			logger.info("个人升级企业再次发邮件MailInfoVo:{}"+JSONObject.toJSON(mailInfoVo).toString());
		} catch (Exception e) {
			flag = "sendMailFail";
			logger.error("个人升级企业再次发邮件MQ出错:{}", e);
		}
		return flag;
	} 
	/**
	 * 重置密码
	 * @param userVo
	 * @return
	 * @since 2017年3月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public String updatePwd(String userId,String passWord,String partyId) {
		String flag;
		String jpartyId = null;
		try {
			byte[] result = Base64.decodeBase64(partyId);
			jpartyId = new String(result);
		} catch (Exception e) {
			logger.error("account base64解密 error:{},partyId:{}",e,partyId);
		}
		UserLogin userLogin = new UserLogin();
		userLogin.setCurrentPassword(passWord);
		Party party = new Party();
		party.setId(jpartyId);
		userLogin.setParty(party);
		userLogin.setLastUpdateDate(new Date());
		userLogin.setLastUpdateUser(userId);
		//密码强度 
		PwdStrength pwdStrength = customerSummeryManager.checkedPwdStrong(passWord);
		userLogin.setPwdStrength(pwdStrength);
		userLoginDao.updateByPartyId(userLogin);
		flag = SUCCESS;
		return flag;
	}
	
	/**
	 * 获取自己创建的人
	 * @param partyId
	 * @return
	 * @since 2017年3月21日
	 * @author tb.yumu@yikuyi.com
	 */
	public List<UserExtendVo> getReportsTo(String partyId){
		List<Party> partyList = personDao.getReportsTo(partyId, ROLETYPEFROM);
		if (partyList!=null&&!partyList.isEmpty()) {
			List<UserExtendVo> list = new ArrayList<>();
			UserExtendVo userExtendVo;
			for (Party party : partyList) {
				userExtendVo = new UserExtendVo();
				userExtendVo.setId(party.getId());
				userExtendVo.setName(party.getPerson().getLastNameLocal());
				list.add(userExtendVo);
			}
			return list;
		}
		return new ArrayList<>();
	}
	/**
	 * 根据角色类型获取所有个人用户的邮箱
	 * @param roleType
	 * @return
	 * @since 2017年3月23日
	 * @author gongtianyu@yikuyi.com
	 */
	public List<Person> getEmailListByRoleType(String roleType) {
		return personDao.getEmailListByRoleType(roleType);
	}
	
	public List<UserVo> getPartyByIds(List<String> ids) {
		return personDao.getPartyByIds(ids);
	}
	/**
	 * 更改账号
	 * @param accountVo
	 * @return
	 * @since 2017年5月3日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public String updateAccount(AccountVo accountVo) {
		try {
			if(accountVo == null){
				return "fail";
			}
			if(StringUtils.isEmpty(accountVo.getType()) || StringUtils.isEmpty(accountVo.getPartyId())){
				return "partyIdOrTypeEmpty";
			}
			String account = accountVo.getAccount();
			if(StringUtils.isEmpty(account)){
				return "accountEmpty";
			}
			//判断是否为企业账号
			Set<String> list = aCLManager.getUserRoleList(accountVo.getPartyId());
			if(!list.contains(ENTERPRISE_CUST)){
				Boolean isExist = this.getAccount(account);
				if(isExist){
					return "accountExist";
				}
			}
			
			//更新person表
			Party party = new Party();
			party.setId(accountVo.getPartyId());
			Person person = new Person();
			if("1".equals(accountVo.getType())){
				person.setTel(account);
			}else if("2".equals(accountVo.getType())){
				person.setMail(account);
			}
			party.setPerson(person);
			personDao.editPerson(party);
			
			if(list.contains(ENTERPRISE_CUST)){
				return SUCCESS;
			}
			//更新userlogin表
			String userLoginMethod ;
			if("1".equals(accountVo.getType())){
				userLoginMethod = UserLoginMethod.MOBILE.toString();
			}else if("2".equals(accountVo.getType())){
				userLoginMethod = UserLoginMethod.EMAIL.toString();
			}else{
				return SUCCESS;
			}
			//查询账号
			UserLogin userLogin = userLoginDao.findEntityByIdAndType(accountVo.getPartyId(),userLoginMethod);
			if(userLogin != null){
				userLogin.setId(null);
				userLogin.setEnabled("N");
				userLoginDao.updateAccount(userLogin);
				
			}else{
				userLogin = new UserLogin();
				userLogin.setParty(party);
			}
			UserLogin login = userLoginDao.findEntityByIdAndAccount(accountVo.getPartyId(),account);
			if(login != null){
				String enabled = login.getEnabled();
				if("N".equals(enabled)){
					login.setEnabled("Y");
					login.setUserLoginMethod(userLoginMethod);
					login.setLastUpdateDate(new Date());
					userLoginDao.update(login);
				}
				return SUCCESS;
			}
			//新增账号
			userLogin.setId(account);
			userLogin.setEnabled("Y");
			userLogin.setCreatedDate(new Date());
			userLogin.setLastUpdateDate(new Date());
			userLoginDao.insert(userLogin);
			return SUCCESS;
		} catch (Exception e) {
			logger.error("更改账号：{}",e);
		}
		return SUCCESS;
	}
	
	/**
	 * 新增部门下的用户
	 * @param userVo
	 * @since 2017年5月9日
	 * @author tb.yumu@yikuyi.com
	 */
	public void addUser(UserVo userVo){
		String id = String.valueOf(IdGen.getInstance().nextId());
		String userId = RequestHelper.getLoginUserId();
		Date date = new Date();
		
		Party party = new Party();
		party.setId(id);
		party.setCorporationId(YIKUYI_ENT_ID);
		party.setPartyType(PartyType.PERSON);
		party.setPartyStatus(PartyStatus.PARTY_ENABLED);
		party.setIsSystem("N");
		party.setCreator(userId);
		party.setCreatedDate(date);
		party.setLastUpdateDate(date);
		party.setLastUpdateUser(userId);
		partyDao.insert(party);
		
		Person person = new Person();
		person.setLastNameLocal(userVo.getName());
		person.setCreator(userId);
		person.setLastUpdateUser(userId);
		person.setGender(Integer.valueOf(userVo.getSex()));
		person.setCreatedDate(date);
		person.setLastUpdateDate(date);
		person.setTel(userVo.getTelNumber());
		person.setMail(userVo.getMail());
		person.setMailStatus("Y");
		person.setPersonTypeStatus(PersonTypeStatus.SON);
		party.setPerson(person);
		personDao.insert(party);
		
		UserLogin userLogin = new UserLogin();
		userLogin.setId(userVo.getMail());
		userLogin.setParty(party);
		userLogin.setCurrentPassword(userVo.getPassword());
		userLogin.setEnabled("Y");
		userLogin.setIsSystem("Y");
		userLogin.setRequirePasswordChange("N");
		userLogin.setUserLoginMethod(UserLoginMethod.EMAIL.toString());
		userLogin.setCreator(userId);
		userLogin.setLastUpdateUser(userId);
		userLogin.setCreatedDate(date);
		userLogin.setLastUpdateDate(date);
		//密码强度 
		PwdStrength pwdStrength = customerSummeryManager.checkedPwdStrong(userVo.getPassword());
		userLogin.setPwdStrength(pwdStrength);
		
		UserLogin login = userLoginDao.findEntityById(userVo.getMail());
		if(login != null && "N".equals(login.getEnabled())){
			userLoginDao.update(userLogin);
		}else{
			userLoginDao.insert(userLogin);
		}
		PartyRelationship relationship;
		//如果是部门主管，则设置为部门主管角色，负责设置为用户与部门的关系
		if (userVo.isManager()) {
			//人与部门的关系
			relationship = PartyRelationship.build(PartyRelationshipTypeEnum.EXECUTIVE_DEPT_REL);
			//添加部门主管角色关系
			partyRoleDao.insert(id, "EXECUTIVE_DIRECTOR", userId, date, userId, date);
		}else {
			relationship = PartyRelationship.build(PartyRelationshipTypeEnum.USER_DEPT_REL);
		}
		relationship.setPartyIdFrom(id);
		relationship.setPartyIdTo(userVo.getDeptId());
		relationship.setFromDate(date);
		relationship.setCreator(userId);
		relationship.setCreatedDate(date);
		relationship.setLastUpdateUser(userId);
		relationship.setLastUpdateDate(date);
		partyRelationshipDao.insert(relationship);
		
		//保存公司与企业的关系
		PartyRelationship ship = PartyRelationship.build(PartyRelationshipTypeEnum.EMPLOYMENT);
		ship.setPartyIdFrom(id);
		ship.setPartyIdTo(YIKUYI_ENT_ID);
		ship.setFromDate(date);
		ship.setCreator(userId);
		ship.setCreatedDate(date);
		ship.setLastUpdateUser(userId);
		ship.setLastUpdateDate(date);
		partyRelationshipDao.insert(ship);
		//人与角色的关系
		for (String  roleId : userVo.getRoleList()) {
			partyRoleDao.insert(id, roleId, userId, date, userId, date);
		}
		
		//添加企业用户角色标识
		partyRoleDao.insert(id, ENTERPRISE_CUST, userId, date, userId, date);
		
		//生成六位随机数属性
		PartyAttribute  industryCategory = new PartyAttribute();
		industryCategory.setKey("SALES-COUPON-CODE");
		industryCategory.setValue(getFixLenthString(6));
		industryCategory.setCreator(userId);
		industryCategory.setCreatedDate(date);
		industryCategory.setLastUpdateUser(userId);
		industryCategory.setLastUpdateDate(date);
		PartyAttributes attributes = new PartyAttributes();
		attributes.setIndustryCategory(industryCategory);
		party.setPartyAttributes(attributes);
		partyAttributeDao.insertIndustryCategory(party);
	}
	
	/**
	 * 查询部门下用户详情
	 * @param id
	 * @return
	 * @since 2017年5月10日
	 * @author tb.yumu@yikuyi.com
	 */
	public UserVo getUserDetail(String id){
		List<UserVo> listUser = userLoginDao.getUserDetail(id);
		UserVo vo = null;
		if(CollectionUtils.isNotEmpty(listUser)){
			 vo = listUser.get(0);
			if (vo!=null) {
				if (!StringUtils.isEmpty(vo.getManagerRole()) && RoleTypeEnum.EXECUTIVE_DIRECTOR.toString().equals(vo.getManagerRole())) {
					vo.setManager(true);
				}else {
					vo.setManager(false);
				}
			}
		}
		return vo;
	}
	
	/**
	 * 跟新用户信息
	 * @param userVo
	 * @since 2017年5月10日
	 * @author tb.yumu@yikuyi.com
	 */
	public void updateUser(UserVo userVo){
		String userId = RequestHelper.getLoginUserId();
		Date date = new Date();
		String id = userVo.getId();
		
		Party party = new Party();
		party.setId(id);
		
		Person person = new Person();
		person.setLastNameLocal(userVo.getName());
		person.setLastUpdateUser(userId);
		person.setGender(Integer.valueOf(userVo.getSex()));
		person.setLastUpdateDate(date);
		person.setTel(userVo.getTelNumber());
		person.setMail(userVo.getMail());
		party.setPerson(person);
		personDao.editPerson(party);
		
		//失效掉之前的人与部门的关系
		PartyRelationship relationship = PartyRelationship.build(PartyRelationshipTypeEnum.USER_DEPT_REL);
		relationship.setPartyIdFrom(id);
		relationship.setThruDate(date);
		relationship.setStatusId(PartyRelationshipStatus.DISABLED);
		partyRelationshipDao.updateRelationShip(relationship);
		//失效掉之前的主管与部门的关系
		relationship = PartyRelationship.build(PartyRelationshipTypeEnum.EXECUTIVE_DEPT_REL);
		relationship.setPartyIdFrom(id);
		relationship.setThruDate(date);
		relationship.setStatusId(PartyRelationshipStatus.DISABLED);
		partyRelationshipDao.updateRelationShip(relationship);
		
		//先删除之前的用户与角色的相关信息
		partyRoleDao.deletePartyRole(id);
		
		
		//如果是部门主管，则设置为部门主管角色，负责设置为用户与部门的关系
		if (userVo.isManager()) {
			relationship = PartyRelationship.build(PartyRelationshipTypeEnum.EXECUTIVE_DEPT_REL);
			//添加部门主管角色关系
			partyRoleDao.insert(id, RoleTypeEnum.EXECUTIVE_DIRECTOR.toString(), userId, date, userId, date);
		}else {
			relationship = PartyRelationship.build(PartyRelationshipTypeEnum.USER_DEPT_REL);
		}
		
		//重新添加人与部门的关系
		relationship.setPartyIdFrom(id);
		relationship.setPartyIdTo(userVo.getDeptId());
		relationship.setFromDate(date);
		relationship.setThruDate(null);
		relationship.setCreator(userId);
		relationship.setCreatedDate(date);
		relationship.setLastUpdateUser(userId);
		relationship.setLastUpdateDate(date);
		partyRelationshipDao.insert(relationship);
		
		for (String roleId : userVo.getRoleList()) {
		    if(RoleTypeEnum.EXECUTIVE_DIRECTOR.toString().equals(roleId)){
		    	continue;
		    }
			partyRoleDao.insert(id, roleId, userId, date, userId, date);
		}
		//添加会员中心角色
		partyRoleDao.insert(id, ENTERPRISE_CUST, userId, date, userId, date);
	}
	
	/**
	 * 生成六位随机数
	 * @param strLength
	 * @return
	 * @since 2017年5月22日
	 * @author tb.yumu@yikuyi.com
	 */
	private String getFixLenthString(int strLength) {  
	    Random rm = new Random();  
	    // 获得随机数  
	    double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);  
	    // 将获得的获得随机数转化为字符串  
	    String fixLenthString = String.valueOf(pross);  
	    // 返回固定的长度的随机数  
	    return fixLenthString.substring(1, strLength + 1);  

	}
 


	
	/**
	 * 根据partyId查询用户的账期信息
	 * 
	 * @param partyId
	 * @param currency
	 * @return
	 * @since 2017年8月8日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public PartyCreditVo getPartyCreditVo(String partyId, Currency currency) {
		PartyCreditVo partyCreditVo = null;
		try {
			Party party = partyDao.getPartyDetail(partyId, null);
			partyCreditVo = new PartyCreditVo();
			if (party == null || party.getCorporationId() == null) {
				return null;
			}
			PartyRelationship partyRelationship = PartyRelationship
					.build(PartyRelationshipTypeEnum.ENTERPRISE_CERTIFIED);
			partyRelationship.setPartyIdFrom(party.getCorporationId());
			List<PartyRelationship> list = partyRelationshipDao.getPartyRelationship(partyRelationship);
			if (CollectionUtils.isEmpty(list)) {
				return null;
			}
			String partyNew = list.get(0).getPartyIdTo();
			PartyCredit partyCredit = partyCreditDao.getPartyCredit(partyNew, currency);

			Party group = partyGroupDao.findPartyGroupByPartyId(party.getCorporationId());

			if (partyCredit != null) {
				BeanUtils.copyProperties(partyCredit,partyCreditVo);
				if (group.getPartyType() != null) {
					partyCreditVo.setPartyType(group.getPartyType());
				}
				if (group.getPartyGroup() != null && group.getPartyGroup().getAccountPeriodStatus() != null) {
					partyCreditVo.setAccountPeriodStatus(group.getPartyGroup().getAccountPeriodStatus());
				}

				// 查询公司类型与所属行业
				EnterpriseVo enterpriseVo = partyDao.getEnterpriseVoInfo(partyNew);
				if (enterpriseVo != null) {
					List<Category> corCategoryList = this.getCategorys("CORPORATION_CATEGORY");
					Map<String, String> corMap = EnterpriseManager.categoryToMap(corCategoryList);
					List<Category> industryCategoryList = this.getCategorys("INDUSTRY_CATEGORY");
					Map<String, String> industryMap = EnterpriseManager.categoryToMap(industryCategoryList);
					// 公司类型
					String corCategory = EnterpriseManager.getCategory(corMap, enterpriseVo.getCorCategory());
					partyCreditVo.setCorCategory(corCategory);
					// 行业
					String industryCategory = EnterpriseManager.getCategory(industryMap,
							enterpriseVo.getIndustryCategory());
					partyCreditVo.setIndustryCategory(industryCategory);
				}
			} else {
				return null;
			}

		} catch (Exception e) {
			logger.error("根据用户id查询用户的企业的账期:{}", e);
		}
		return partyCreditVo;
	}  

	/**
	 * 批量查询用户的账期信息
	 * 
	 * @param partyMaps
	 * @return
	 * @since 2017年10月30日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public List<PartyCreditVo> getPartyCreditInfoList(String partyList) {
		List<PartyCreditVo> list = new ArrayList<>();
		if (CollectionUtils.isEmpty(list)) {
			return Collections.emptyList();
		}
		JSONArray array = JSONArray.fromObject(partyList);
		if (CollectionUtils.isEmpty(array)) {
			return Collections.emptyList();
		}
		for (int i = 0; i < array.size(); i++) {
			net.sf.json.JSONObject job = array.getJSONObject(i); // 遍历 jsonarray
			String partyId = job.getString("partyId");
			String currency = job.getString("currency");
			if (partyId != null && partyId != "" && currency != null && currency != "") {
				PartyCreditVo creditVo = getPartyCreditVo(partyId, Currency.valueOf(currency));
				if (creditVo != null) {
					creditVo.setUserId(partyId);
					list.add(creditVo);
				}
			}
		}

		return list;
	}
	
	
	/**
	 * 根据业务类型获取维度数据
	 * 
	 * @param category
	 * @return
	 * @since 2017年4月14日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public List<Category> getCategorys(String category) {
		List<Category> list = null;
		try {
			return shipmentClientBuilder.categoryResource().categoryList(category);
		} catch (Exception e) {
			logger.error("调用查询业务类型失败：{}", e);
		}
		return list;
	}
	
	
	/**
	 * 根据partyId修改用户的账期信息
	 * @param partyCreditVo
	 * @since 2017年8月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public void updatePartyCredit(PartyCredit partyCredit) {
		try {
			if(partyCredit.getPartyId()!=null){
				PartyCredit credit = partyCreditDao.getPartyCredit(partyCredit.getPartyId(), partyCredit.getCurrency());
				if(credit!=null){
					partyCredit.setCreditQuota(credit.getCreditQuota());
				}
				partyCreditDao.updatePartyCreditByPartyId(partyCredit);
			}
		} catch (Exception e) {
			logger.error("用户销账操作：{}",e);
		}
		
	}
	
	/**
	 * 监控用户是否为首次登陆
	 * 
	 * @param account
	 * @return
	 * @since 2017年8月29日
	 * @author zr.helinmei@yikuyi.com
	 */
	public void userLoginListener(User user) {
	  msgSender.sendMsg(userLoginFirstListennerTopicName, user, null);
	}
	
	/**
	 * 获取个人当天注册数或者总数
	 * @param flag 不为空则查询当天个人会员注册数
	 * @return
	 * @since 2017年11月29日
	 * @author zr.helinmei@yikuyi.com
	 */
	public Long getPersonalData(String flag) {
	  return userLoginDao.getPersonalData(flag);
	}
	
	/**
	 * 获取认证会员当天数或者总数
	 * @param flag 不为空则查询当天认证会员数
	 * @return
	 * @since 2017年11月29日
	 * @author zr.helinmei@yikuyi.com
	 */
	public Long getAuthenticationData(String flag) {
	  return userLoginDao.getAuthenticationData(flag);
	}
	/**
	 * 新增昨天登陆数。
	 * @return Long
	 * @since 2017年11月29日
	 * @author zr.helinmei@yikuyi.com
	 */
	public Long getLoginNumToday() {
	  return userLoginDao.getLoginNumYesterday();
	}
	
}
