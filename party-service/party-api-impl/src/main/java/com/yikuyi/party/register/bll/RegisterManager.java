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
package com.yikuyi.party.register.bll;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.yikuyi.message.MessageClientBuilder;
import com.yikuyi.message.mail.vo.MailInfoVo;
import com.yikuyi.message.sms.vo.MailAddressValidVO;
import com.yikuyi.party.contact.model.ContactMech;
import com.yikuyi.party.contact.model.PostalAddress;
import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.party.customer.bll.CustomerSummeryManager;
import com.yikuyi.party.customer.dao.PersonDao;
import com.yikuyi.party.exception.PartyBusiErrorCode;
import com.yikuyi.party.group.model.PartyGroup;
import com.yikuyi.party.group.model.PartyGroup.AccountStatus;
import com.yikuyi.party.group.model.PartyGroup.ActiveStatus;
import com.yikuyi.party.login.model.UserLogin;
import com.yikuyi.party.login.model.UserLogin.PwdStrength;
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
import com.yikuyi.workflow.WorkflowClientBuilder;
import com.yikuyi.workflow.vo.TaskVo;
import com.yikuyi.workflow.vo.TaskVo.TaskVoType;
import com.ykyframework.exception.BusinessException;
import com.ykyframework.model.IdGen;
import com.ykyframework.mqservice.sender.MsgSender;

@Service
@Transactional
public class RegisterManager {
	private static final Logger logger = LoggerFactory.getLogger(RegisterManager.class);
	@Autowired
	private PartyDao partyDao;
	@Autowired
	private PersonDao personDao;
	@Autowired
	private UserLoginDao userLoginDao;
	@Autowired
	private PartyRoleDao partyRoleDao;
	@Autowired
	private PartyGroupDao partyGroupDao;
	@Autowired
	private PartyAttributeDao partyAttributeDao;
	@Autowired
	private PartyRelationshipDao relationshipDao;

	@Autowired
	private PartyContactMechManager partyContactMechManager;
	@Autowired
	private CustomerSummeryManager customerSummeryManager;

	@Autowired
	private MessageClientBuilder messageClientBuilder;

	@Autowired
	private CacheManager cacheManager;
	public static final String SUCCESS = "success";

	@Value("${api.message.serverUrlPrefix}")
	private String serverUrl;
	@Value("${customer.serverUrlPrefix}")
	private String customerUrl;
	@Value("${api.workflow.serverUrlPrefix}")
	private String workflowUrlPrefix;
	@Autowired
	private MsgSender msgSender;

	@Value("${mqConsumeConfig.sendMsgAndEmail.topicName}")
	private String sendMsgAndEmailTopicName;
	private static final String CORPORATION_CATEGORY_ID = "CORPORATION_CATEGORY_ID";// 公司类型
	private static final String CORPORATION_CATEGORY_ID_OTHER = "CORPORATION_CATEGORY_ID_OTHER";// 公司类型(其他)

	private static final String INDUSTRY_CATEGORY_ID = "INDUSTRY_CATEGORY_ID";// 所属行业
	private static final String INDUSTRY_CATEGORY_ID_OTHER = "INDUSTRY_CATEGORY_ID_OTHER";// 所属行业其它属性拓展属性
	private static final String SENDACTIVEMAIL = "sendActiveMail_";// 缓存前缀

	
	@Autowired
	private WorkflowClientBuilder workflowClientBuilder;

	/**
	 * 个人注册
	 * 
	 * @param userVo
	 * @since 2017年1月12日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public String save(UserVo userVo) throws BusinessException {
		// a.校验图片验证码
		String flag = verifyCode(userVo);
		if (!SUCCESS.equalsIgnoreCase(flag)) {
			throw new BusinessException(PartyBusiErrorCode.VERIFY_FAIL);
		}
		return savePerson(userVo);
	}

	public String savePerson(UserVo userVo) {
		// b.保存接口： PARTY表里面生成记录， PARTY_TYPE 设置为： PERSON ，STATUS_ID 初始化为: ACTIVE；
		String id = String.valueOf(IdGen.getInstance().nextId());
		Party party = new Party();
		party.setId(id);
		party.setPartyType(PartyType.PERSON);
		party.setPartyStatus(PartyStatus.PARTY_ENABLED);
		party.setCreator(id);
		party.setCreatedDate(new Date());
		party.setLastUpdateDate(new Date());
		party.setLastUpdateUser(id);
		partyDao.insert(party);
		// PERSON 表生成记录 PARTY_ID 和 PARTY表一致。
		Person person = new Person();
		person.setMail(userVo.getMail());
		person.setTel(userVo.getMobile());
		person.setTelStatus("Y");
		person.setMailStatus("N");
		person.setPersonTypeStatus(PersonTypeStatus.COMMON);
		party.setPerson(person);
		personDao.insert(party);
		// c.生成登录数据USER_LOGIN 字段： ENABLED = ‘Y’ , PARTY_ID 和 PARTY表一致，
		// USER_LOGIN_ID = 刚刚注册的手机号。
		UserLogin userLogin = new UserLogin();
		userLogin.setId(userVo.getMobile());
		userLogin.setParty(party);
		userLogin.setCurrentPassword(userVo.getPassword());
		userLogin.setEnabled("Y");
		userLogin.setIsSystem("N");
		userLogin.setRequirePasswordChange("N");
		userLogin.setUserLoginMethod(UserLoginMethod.MOBILE.toString());
		userLogin.setCreatedDate(new Date());
		userLogin.setLastUpdateDate(new Date());
		// 密码强度
		PwdStrength pwdStrength = customerSummeryManager.checkedPwdStrong(userVo.getPassword());
		userLogin.setPwdStrength(pwdStrength);
		userLoginDao.insert(userLogin);

		// d.生成 PARTY_ROLE 数据 ROLE_TYPE_ID 为：CUSTOMER
		partyRoleDao.insert(id, RoleTypeEnum.INDIVIDUAL_CUST.toString(), id, new Date(), id, new Date());
		return id;

	}

	/**
	 * 校验验证码
	 * 
	 * @param userVo
	 * @return
	 * @since 2017年4月28日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public String verifyCode(UserVo userVo) {
		String flag = "fail";
		if (StringUtils.isEmpty(userVo.getUuid()) || StringUtils.isEmpty(userVo.getImgCode())) {
			return flag;
		}
		try {
			flag = messageClientBuilder.imgResource(String.class).validVerifyCode(userVo.getUuid(), userVo.getImgCode());
		} catch (Exception e) {
			logger.error("调用图片验证码失败:{}", e);
		}
		return flag;

	}

	/**
	 * 获取验证码
	 * 
	 * @param userVo
	 * @return
	 * @since 2017年4月28日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public String getVerifyCode(UserVo userVo) {
		String code = "";
		if (StringUtils.isEmpty(userVo.getUuid()) || StringUtils.isEmpty(userVo.getMail())) {
			return code;
		}
		try {
			MailAddressValidVO vo = new MailAddressValidVO();
			vo.setUuid(userVo.getUuid());
			vo.setMailAddress(userVo.getMail());
			code = messageClientBuilder.mailResource(String.class).getVerifyCode(vo);
		} catch (Exception e) {
			logger.error("调用获取邮箱验证码失败:{}", e);
		}
		return code;
	}

	public HttpHeaders getHttpHeaders() {
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(type);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		return headers;
	}

	/**
	 * 企业注册
	 * 
	 * @param userVo
	 * @since 2017年1月12日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public String saveEnt(UserVo userVo) throws BusinessException {
		if (StringUtils.isEmpty(userVo.getMail())) {
			throw new BusinessException(PartyBusiErrorCode.EMAIL_EMPTY);
		}
		// a.校验图片验证码
		String flag = verifyCode(userVo);

		if ("fail".equals(flag)) {
			throw new BusinessException(PartyBusiErrorCode.VERIFY_FAIL);
		}
		// 获取邮箱验证码
		String code = this.getVerifyCode(userVo);
		logger.info("获取邮箱验证码 code = " + code);
		if (StringUtils.isEmpty(code)) {
			throw new BusinessException(PartyBusiErrorCode.CODE_EMPTY);
		}
		userVo.setImgCode(code);
		// 判断之前是否注册过，并且注册没有成功（根据邮箱）
		UserLogin login = userLoginDao.findEntityById(userVo.getMail());
		if (login != null && "Y".equals(login.getEnabled())) {
			throw new BusinessException(PartyBusiErrorCode.EMAIL_EXIST);
		}
		// 发送邮件给用户
		flag = sendMail(userVo);

		if (!SUCCESS.equals(flag)) {
			return null;
		}
		// 判断是否为重复发送邮件
		if ("Y".equals(userVo.getReSend())) {
			return null;
		}
		// b.保存接口： PARTY表里面生成记录， PARTY_TYPE 设置为： PERSON ，STATUS_ID
		return saveEnterprise(userVo);
	}

	/**
	 * 重新发送注册邮件
	 * 
	 * @param userVo
	 * @since 2017年1月12日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public String reSend(UserVo userVo) {
		// 判断是否激活
		// 查询用户是否存在，如果不存在报用户不存在的错误，如果存在查看是否有效，如果有效则不修改密码
		String falg;
		String uuid = UUID.randomUUID().toString();
		userVo.setUuid(uuid);
		// 判断有没有缓存
		Cache cache = null;
		try {
			cache = cacheManager.getCache("sendActiveMailCache");
			falg = cache.get(SENDACTIVEMAIL + userVo.getMail(), String.class);
			//OpreationSystem 为1说明是后台,每次点击发送邮件都发送
			if("1".equals(userVo.getOpreationSystem())){
				// 获取邮箱验证码
				String code = this.getVerifyCode(userVo);
				logger.info("获取邮箱验证码code = " + code);
				if (StringUtils.isEmpty(code)) {
					falg = "codeEmpty";
					return falg;
				}
				userVo.setImgCode(code);
				falg = sendMail(userVo);
				if (SUCCESS.equals(falg)) {
					cache.put(SENDACTIVEMAIL + userVo.getMail(), falg);
				}
				return falg;
			}else{
				if (StringUtils.isEmpty(falg)) {
					// 获取邮箱验证码
					String code = this.getVerifyCode(userVo);
					logger.info("获取邮箱验证码code = " + code);
					if (StringUtils.isEmpty(code)) {
						falg = "codeEmpty";
						return falg;
					}
					userVo.setImgCode(code);
					falg = sendMail(userVo);
					if (SUCCESS.equals(falg)) {
						cache.put(SENDACTIVEMAIL + userVo.getMail(), falg);
					}
					return falg;
				}
			}
		} catch (Exception e) {
			falg = "false";
			logger.error(e.getMessage(), e);
			return falg;
		}
		return SUCCESS;
	}

	/**
	 * 企业注册保存
	 * 
	 * @param userVo
	 * @since 2017年4月28日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public String saveEnterprise(UserVo userVo) {
		// 1.保存企业信息
		String entId = this.createEnt(userVo);
		// 2.保存账号信息
		String userId = this.createPerson(userVo, entId);
		// 3.保存企业与账号的关联关系
		saveRelationShip(userId, entId);
		return userId;
	}

	/**
	 * 保存账号信息
	 * 
	 * @param userVo
	 * @return
	 * @since 2017年4月28日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	private String createPerson(UserVo userVo, String entId) {
		// 1.保存账号信息
		String id = String.valueOf(IdGen.getInstance().nextId());
		Party party = new Party();
		party.setId(id);
		party.setPartyType(PartyType.PERSON);
		party.setPartyStatus(PartyStatus.PARTY_NOT_VERIFIED);
		party.setCreator(id);
		party.setCreatedDate(new Date());
		party.setLastUpdateDate(new Date());
		party.setLastUpdateUser(id);
		party.setCorporationId(entId);
		partyDao.insert(party);
		// PERSON 表生成记录 PARTY_ID 和 PARTY表一致。
		Person person = new Person();
		person.setLastNameLocal(userVo.getName());
		person.setMail(userVo.getMail());
		person.setPersonalTitle(userVo.getPersonalTitle());
		person.setTel(userVo.getMobile());
		person.setTelStatus("Y");
		person.setMailStatus("Y");
		person.setCreator(id);
		person.setCreatedDate(new Date());
		person.setLastUpdateDate(new Date());
		person.setLastUpdateUser(id);
		person.setPersonTypeStatus(PersonTypeStatus.COMMON);
		party.setPerson(person);
		personDao.insert(party);
		// c.生成登录数据USER_LOGIN 字段： ENABLED = ‘Y’ , PARTY_ID 和 PARTY表一致，
		// USER_LOGIN_ID = 刚刚注册的手机号。
		UserLogin userLogin = new UserLogin();
		userLogin.setId(userVo.getMail());
		userLogin.setParty(party);
		userLogin.setEnabled("Y");
		userLogin.setIsSystem("N");
		userLogin.setRequirePasswordChange("N");
		userLogin.setUserLoginMethod(UserLoginMethod.EMAIL.toString());
		userLogin.setCreator(id);
		userLogin.setCreatedDate(new Date());
		userLogin.setLastUpdateUser(id);
		userLogin.setLastUpdateDate(new Date());
		userLogin.setCurrentPassword(userVo.getPassword());
		// 密码强度
		PwdStrength pwdStrength = customerSummeryManager.checkedPwdStrong(userVo.getPassword());
		userLogin.setPwdStrength(pwdStrength);
		userLoginDao.insert(userLogin);

		// d.生成 PARTY_ROLE 数据 ROLE_TYPE_ID 为：REGISTER
		partyRoleDao.insert(id, RoleTypeEnum.ENTERPRISE_CUST.toString(), id, new Date(), id, new Date());
		// 创建个人注册地址
		this.saveAddr(userVo, id);
		return id;
	}

	/**
	 * 保存企业信息
	 * 
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
		party.setCreator(id);
		party.setCreatedDate(new Date());
		party.setLastUpdateDate(new Date());
		party.setLastUpdateUser(id);
		partyDao.insert(party);

		PartyGroup partyGroup = new PartyGroup();
		partyGroup.setGroupName(userVo.getCompanyName());
		partyGroup.setCreator(id);
		partyGroup.setCreatedDate(new Date());
		partyGroup.setLastUpdateUser(id);
		partyGroup.setLastUpdateDate(new Date());
		partyGroup.setActiveStatus(ActiveStatus.PARTY_NOT_VERIFIED);
		partyGroup.setAccountStatus(AccountStatus.ACCOUNT_NOT_VERIFIED);
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
		// 公司类型(其他)
		if (!StringUtils.isEmpty(userVo.getCorCategoryOther())) {
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
		// 设置other值
		PartyAttributes otherAttrs = new PartyAttributes();
		PartyAttribute otherAttr = new PartyAttribute();
		otherAttr.setKey(INDUSTRY_CATEGORY_ID_OTHER);
		otherAttr.setValue(userVo.getOtherAttr());
		otherAttrs.setOtherAttrs(otherAttr);
		party.setPartyAttributes(otherAttrs);
		partyAttributeDao.insertOtherAttrs(party);

		// 创建企业注册地址
		this.saveAddr(userVo, id);

		return id;
	}

	/**
	 * 创建用户注册地址
	 * 
	 * @param userVo
	 * @param entId
	 * @since 2017年4月28日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	private void saveAddr(UserVo userVo, String partyId) {
		PartyContactMech partyContactMech = new PartyContactMech();
		partyContactMech.setPurposeType(PurposeType.REGISTER_LOCATION);
		ContactMech contactMech = new ContactMech();
		PostalAddress postalAddress = new PostalAddress();
		postalAddress.setAddress1(userVo.getAddress());
		postalAddress.setCountryGeoName("中国");
		postalAddress.setCountryGeoId("china");

		postalAddress.setProvinceGeoName(userVo.getProvinceName());
		postalAddress.setProvinceGeoId(userVo.getProvince());

		postalAddress.setCountyGeoName(userVo.getCountryName());
		postalAddress.setCountyGeoId(userVo.getCountry());

		postalAddress.setCityGeoName(userVo.getCityName());
		postalAddress.setCityGeoId(userVo.getCity());

		contactMech.setPostalAddress(postalAddress);

		partyContactMech.setContactMech(contactMech);
		partyContactMech.setPartyId(partyId);
		partyContactMechManager.insert(partyContactMech);

	}

	/**
	 * 保存企业与账号的关联关系
	 * 
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
		relationshipDao.insert(relationShip);
	}

	/**
	 * 发送邮件
	 * 
	 * @param userVo
	 * @since 2017年2月9日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	// @Cacheable(value="sendActiveMailCache", key="'sendMail_' +
	// #userVo.getMail()")
	public String sendMail(UserVo userVo) {
		String flag = SUCCESS;
		try {
			logger.info("给用户发送一封邮件" + userVo.getMail());
			// parms :第一个参数：发送时间 第二个参数：邮箱 第三个参数：验证码
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			String senddate = format.format(new Date());
			String mail = userVo.getMail();
			String imgCode = userVo.getImgCode();
			String sid = senddate + "---" + mail + "---" + imgCode;
			byte[] b = Base64.encodeBase64(sid.getBytes("utf-8"), true);
			sid = new String(b, "utf-8");
			String url = customerUrl + "/reg.htm?action=fromMail&sid=" + sid;
			MailInfoVo mailInfoVo = new MailInfoVo();
			mailInfoVo.setTemplateId(MailInfoVo.TemplateId.REGISTER.toString());
			mailInfoVo.setType(MailInfoVo.Type.EMAIL.toString());
			mailInfoVo.setTo(userVo.getMail());
			JSONObject content = new JSONObject();
			content.put("url", url);
			mailInfoVo.setContent(content);
			msgSender.sendMsg(sendMsgAndEmailTopicName, mailInfoVo, null);
			logger.info("给用户发送一封邮件MailInfoVo: {}", JSONObject.toJSON(mailInfoVo));
		} catch (Exception e) {
			flag = "sendMailFail";
			logger.error("发送注册邮件MQ出错:{}", e);
		}
		return flag;
	}

	/**
	 * 加入主账号
	 * 
	 * @param entId
	 * @param account
	 * @return
	 * @since 2017年5月5日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public String joinMainAccount(String entId, String account, String applyId) {
		String falg;
		if (StringUtils.isEmpty(entId) || StringUtils.isEmpty(account) || StringUtils.isEmpty(applyId)) {
			falg = "entIdOrAccountOrApplyIdEmpty";
			return falg;
		}
		// 验证企业是否存在
		Party party = partyGroupDao.findPartyGroupByPartyId(entId);
		if (party == null || party.getPartyGroup() == null) {
			falg = "entNotExsit";
			return falg;
		}
		// 验证企业是否认证
		if (party.getPartyGroup().getActiveStatus() != ActiveStatus.PARTY_VERIFIED) {
			falg = "entNotVerified";
			return falg;
		}
		

		// 验证账号是否存在
		UserLogin userLogin = userLoginDao.findEntityById(account);
		String userId;

		if (userLogin == null) {
			// 审核流程
			String status = this.approveJoinWorkflow(applyId);
			logger.info("加入企业审核流程status ：{} " + status);
			falg = "newAccount";
			return falg;
		}

		if (userLogin.getParty() == null || StringUtils.isEmpty(userLogin.getParty().getId())) {
			falg = "userIdEmpty";
			return falg;
		}

		Party p = partyDao.getPartyPersonDetail(userLogin.getParty().getId());
		if (p == null) {
			falg = "userIdEmpty";
			return falg;
		}
		if (p.getPartyStatus() == PartyStatus.PARTY_DISABLED) {
			falg = "partyDisabled";
			return falg;
		}
		userId = userLogin.getParty().getId();
		// 判断是否为主账号
		PartyRelationship partyRelationship = PartyRelationship.build(PartyRelationshipTypeEnum.COS_PLAY);
		partyRelationship.setPartyIdFrom(userId);
		List<PartyRelationship> shipList = relationshipDao.getPartyRelationship(partyRelationship);
		if (CollectionUtils.isNotEmpty(shipList)) {
			falg = "mainAccount";
			return falg;
		}
		// 判断是不是已经加入企业
		PartyRelationship partyRelationship1 = PartyRelationship.build(PartyRelationshipTypeEnum.REPORTS_TO);
		partyRelationship1.setPartyIdFrom(userId);
		List<PartyRelationship> shipList1 = relationshipDao.getPartyRelationship(partyRelationship1);
		if (CollectionUtils.isNotEmpty(shipList1)) {
			falg = "joined";
			return falg;
		}
		// 失效之前的企业关系
		PartyRelationship partyRelationship2 = PartyRelationship.build(PartyRelationshipTypeEnum.EMPLOYMENT);
		partyRelationship2.setPartyIdFrom(userId);
		partyRelationship2.setThruDate(new Date());
		partyRelationship2.setStatusId(PartyRelationshipStatus.DISABLED);
		relationshipDao.updateRelationShip(partyRelationship2);
		// update party表的 企业id
		Party user = userLogin.getParty();
		user.setCorporationId(entId);
		user.setLastUpdateDate(new Date());
		partyDao.updateParty(user);
		// 修改person状态
		Person person = new Person();
		person.setLastUpdateDate(new Date());
		person.setRelationSratus(RelationSratus.RELATED);
		person.setPersonTypeStatus(PersonTypeStatus.SON);
		user.setPerson(person);
		personDao.editPerson(user);

		// 根据企业账号查询主账号
		PartyRelationship relationship = PartyRelationship.build(PartyRelationshipTypeEnum.COS_PLAY);
		relationship.setPartyIdTo(entId);
		List<PartyRelationship> list = relationshipDao.getPartyRelationship(relationship);
		String mainId = null;
		if (CollectionUtils.isNotEmpty(list)) {
			relationship = list.get(0);
			mainId = relationship.getPartyIdFrom();
		}
		if (!StringUtils.isEmpty(mainId)) {
			// 账号和主账号的关联关系
			this.saveMainRelationShip(mainId, userId);
			// 账号和企业的关联关系
			this.saveRelationShip(userId, entId);
		}
		 falg = SUCCESS;
		// 审核流程
		String status = this.approveJoinWorkflow(applyId);
		logger.info("加入企业审核流程status1 ：{} " + status);
		return falg;
	}

	/**
	 * 审核加入主账号流程
	 * 
	 * @param applyId
	 * @return
	 * @since 2017年5月12日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public String approveJoinWorkflow(String applyId) {
		String falg = SUCCESS;
		try {
			// 调用workFlow服务
			TaskVo taskVo = new TaskVo();
			workflowClientBuilder.taskClient().disposeTask(TaskVoType.APPROVED, applyId, taskVo);
		} catch (Exception e) {
			falg = "fail";
			logger.error("审核加入主账号流程出错:{}", e);
		}
		return falg;
	}

	/**
	 * 账号和主账号的关联关系
	 * 
	 * @param loginId
	 *            主账号
	 * @param userId
	 *            账号
	 * @param id
	 * @since 2017年3月17日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	private void saveMainRelationShip(String loginId, String userId) {
		PartyRelationship relationShip = PartyRelationship.build(PartyRelationshipTypeEnum.REPORTS_TO);
		relationShip.setPartyIdFrom(userId);
		relationShip.setPartyIdTo(loginId);
		relationShip.setFromDate(new Date());
		relationShip.setThruDate(null);
		relationShip.setCreator(loginId);
		relationShip.setCreatedDate(new Date());
		relationShip.setLastUpdateUser(loginId);
		relationShip.setLastUpdateDate(new Date());
		relationshipDao.insert(relationShip);
	}

	/**
	 * 根据账号创建子账号(子账号设置密码)
	 * 
	 * @param account
	 * @param entId
	 * @return
	 * @since 2017年5月5日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public String saveAccout(UserVo vo) {
		if (vo == null) {
			return "userVoEmpty";
		}
		String account = vo.getMail();
		String entId = vo.getEnterpriseId();
		String password = vo.getPassword();
		if (StringUtils.isEmpty(account) || StringUtils.isEmpty(entId) || StringUtils.isEmpty(password)) {
			return "accountOrEntIdOrPasswordEmpty";
		}
		UserLogin login = userLoginDao.findEntityById(account);
		if (login != null) {
			return "accountExsit";
		}
		// 1.保存账号信息
		String id = String.valueOf(IdGen.getInstance().nextId());
		Party party = new Party();
		party.setId(id);
		party.setPartyType(PartyType.PERSON);
		party.setPartyStatus(PartyStatus.PARTY_ENABLED);
		party.setCreator(id);
		party.setCreatedDate(new Date());
		party.setLastUpdateDate(new Date());
		party.setLastUpdateUser(id);
		party.setCorporationId(entId);
		partyDao.insert(party);
		// PERSON 表生成记录 PARTY_ID 和 PARTY表一致。
		Person person = new Person();
		person.setLastNameLocal(account);
		person.setMail(account);
		person.setTelStatus("N");
		person.setMailStatus("Y");
		person.setCreator(id);
		person.setCreatedDate(new Date());
		person.setLastUpdateDate(new Date());
		person.setLastUpdateUser(id);
		person.setRelationSratus(RelationSratus.RELATED);
		person.setPersonTypeStatus(PersonTypeStatus.SON);
		party.setPerson(person);
		personDao.insert(party);
		// c.生成登录数据USER_LOGIN 字段： ENABLED = ‘Y’ , PARTY_ID 和 PARTY表一致，
		// USER_LOGIN_ID = 刚刚注册的手机号。
		UserLogin userLogin = new UserLogin();
		userLogin.setId(account);
		userLogin.setParty(party);
		userLogin.setEnabled("Y");
		userLogin.setIsSystem("N");
		userLogin.setRequirePasswordChange("N");
		userLogin.setUserLoginMethod(UserLoginMethod.EMAIL.toString());
		userLogin.setCreator(id);
		userLogin.setCreatedDate(new Date());
		userLogin.setLastUpdateUser(id);
		userLogin.setLastUpdateDate(new Date());
		userLogin.setCurrentPassword(password);
		// 密码强度
		PwdStrength pwdStrength = customerSummeryManager.checkedPwdStrong(password);
		userLogin.setPwdStrength(pwdStrength);
		userLoginDao.insert(userLogin);

		// d.生成 PARTY_ROLE 数据 ROLE_TYPE_ID 为：REGISTER
		partyRoleDao.insert(id, RoleTypeEnum.ENTERPRISE_CUST.toString(), id, new Date(), id, new Date());

		// 根据企业账号查询主账号
		PartyRelationship relationship = PartyRelationship.build(PartyRelationshipTypeEnum.COS_PLAY);
		relationship.setPartyIdTo(entId);
		List<PartyRelationship> list = relationshipDao.getPartyRelationship(relationship);
		String mainId = null;
		if (CollectionUtils.isNotEmpty(list)) {
			relationship = list.get(0);
			mainId = relationship.getPartyIdFrom();
		}
		if (!StringUtils.isEmpty(mainId)) {
			// 账号和主账号的关联关系
			this.saveMainRelationShip(mainId, id);
			// 账号和企业的关联关系
			this.saveRelationShip(id, entId);
		}
		return id;
	}

	/**
	 * 生成登陆账号（根据账号）
	 * @param partyId
	 * @param account
	 * @return
	 * @since 2017年7月28日
	 * @author zr.aoxianbing@yikuyi.com
	 * @throws BusinessException 
	 */
	public void upgrade(String partyId, String account) throws BusinessException {
		if(StringUtils.isEmpty(partyId) || StringUtils.isEmpty(account)){
			throw new BusinessException(PartyBusiErrorCode.PARTY_OR_ACCOUNT_EMPTY,"用户id或者账号为空!");
		}
		// 判断之前是否注册过，并且注册没有成功（根据邮箱）
		UserLogin login = userLoginDao.findEntityById(account);
		if (login != null && "Y".equals(login.getEnabled())) {
			throw new BusinessException(PartyBusiErrorCode.EMAIL_EXIST,"邮箱已经存在!");
		}
		//生成登陆账号
		//1.根据用户id获取企业id，和手机号码用于初始化密码
		Party party = partyDao.getPartyPersonDetail(partyId);
		if(party == null || party.getPerson() == null){
			throw new BusinessException(PartyBusiErrorCode.PARTYID_NOT_EXIST,"用户不存在!");
		}
		String tel = party.getPerson().getTel();
		if(StringUtils.isEmpty(tel)){
			throw new BusinessException(PartyBusiErrorCode.TEL_EMPTY,"手机为空!");
		}
		//生成账号
		this.createUserLogin(tel, partyId, account);
	}
	
	/**
	 * 生成账号
	 * @param tel
	 * @param partyId
	 * @param account
	 * @since 2017年7月28日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	private void createUserLogin(String tel, String partyId,String account) throws BusinessException {
		Party party = new Party();
		party.setId(partyId);
		Person p = new Person();
		p.setMailStatus("Y");
		party.setPerson(p);
		//修改邮箱验证状态
		personDao.editPerson(party);
		// c.生成登录数据USER_LOGIN 字段： ENABLED = ‘Y’ , PARTY_ID 和 PARTY表一致，
		UserLogin userLogin = new UserLogin();
		userLogin.setId(account);
		userLogin.setParty(party);
		userLogin.setEnabled("Y");
		userLogin.setIsSystem("N");
		userLogin.setRequirePasswordChange("N");
		userLogin.setUserLoginMethod(UserLoginMethod.EMAIL.toString());
		userLogin.setCreator(partyId);
		userLogin.setCreatedDate(new Date());
		userLogin.setLastUpdateUser(partyId);
		userLogin.setLastUpdateDate(new Date());
		UserLogin userL = userLoginDao.findEntityById(tel);
		if(userL == null || StringUtils.isEmpty(userL.getBcryptPassword())){
			throw new BusinessException(PartyBusiErrorCode.PARTYID_NOT_EXIST,"用户不存在!");
		}
		userLogin.setCurrentPassword(userL.getBcryptPassword());
		// 密码强度
		userLogin.setPwdStrength(userL.getPwdStrength());
		
		userLoginDao.insert(userLogin);
	}
}