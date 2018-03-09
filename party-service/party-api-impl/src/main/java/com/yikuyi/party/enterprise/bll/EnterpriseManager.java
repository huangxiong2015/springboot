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
package com.yikuyi.party.enterprise.bll;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.jasig.inspektr.audit.annotation.Audit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.shade.io.netty.util.internal.StringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.springboot.audit.Param;
import com.framework.springboot.utils.AuthorizationUtil;
import com.framewrok.springboot.web.RequestHelper;
import com.google.common.collect.Lists;
import com.ictrade.tools.export.ExportFactory;
import com.ictrade.tools.export.ExportProcesser;
import com.yikuyi.basedata.ShipmentClientBuilder;
import com.yikuyi.basedata.category.model.Category;
import com.yikuyi.message.mail.vo.MailInfoVo;
import com.yikuyi.party.common.utils.BusiErrorCode;
import com.yikuyi.party.common.utils.PartyActiveStatus;
import com.yikuyi.party.common.utils.PartyAttributeConstants;
import com.yikuyi.party.contact.model.ContactMech;
import com.yikuyi.party.contact.model.ContactMech.MechType;
import com.yikuyi.party.contact.model.PostalAddress;
import com.yikuyi.party.contact.model.TelecomNumber;
import com.yikuyi.party.contact.model.TelecomNumber.QqTelecomNumber;
import com.yikuyi.party.contact.vo.EnterpriseParamVo;
import com.yikuyi.party.contact.vo.EnterpriseVo;
import com.yikuyi.party.contact.vo.UserExtendVo;
import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.party.credit.dao.PartyCreditDao;
import com.yikuyi.party.credit.model.PartyCredit;
import com.yikuyi.party.customer.dao.PersonDao;
import com.yikuyi.party.group.model.PartyGroup;
import com.yikuyi.party.group.model.PartyGroup.AccountPeriodStatus;
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
import com.yikuyi.party.role.model.RoleTypeEnum;
import com.yikuyi.party.shipAddress.bll.PartyContactMechManager;
import com.yikuyi.party.userLogin.dao.UserLoginDao;
import com.yikuyi.party.vo.EnterpriseExpiredVo;
import com.yikuyi.party.vo.RoleTypeVo;
import com.yikuyi.pay.PayClientBuilder;
import com.yikuyi.pay.coupon.CouponParty;
import com.yikuyi.workflow.Apply;
import com.yikuyi.workflow.Apply.ApplyStatus;
import com.yikuyi.workflow.WorkflowClientBuilder;
import com.ykyframework.exception.BusinessException;
import com.ykyframework.model.IdGen;
import com.ykyframework.mqservice.sender.MsgSender;
import com.ykyframework.oss.AliyunOSSAccount;
import com.ykyframework.oss.AliyunOSSHelper;
import com.ykyframework.oss.AliyunOSSHelper.ImageUrls;

@Service
@Transactional
public class EnterpriseManager {
	private static final Logger logger = LoggerFactory.getLogger(EnterpriseManager.class);
	@Autowired
	private PartyDao partyDao;

	@Autowired
	private PartyAttributeDao partyAttributeDao;

	@Autowired
	private PartyContactMechManager partyContactMechManager;

	@Autowired
	private PartyGroupDao partyGroupDao;

	@Autowired
	private PartyRoleDao partyRoleDao;

	@Autowired
	private PersonDao personDao;

	@Autowired
	private UserLoginDao userLoginDao;

	public static final String YKY_ENTERPRISE_ID = "99999999";//YKY 企业ID

	private static final String TD_END_STR = "</td>";// td结束符号
	private static final String TD_START_STR = "<td>";// td开始符号



	private static final String NOTACTIVE = "notActive";// 申请账户未激活或者关联

	private static final String RELATING = "relating";// 申请账户关联审核中或者驳回中
	private static final String ACTIVING = "activing";// 申请账户激活审核中或者驳回中

	private static final String RELATED = "related";// 申请账户关联审核通过
	private static final String ACTIVED = "actived";// 申请账户激活审核通过

	private static final String EMAIL = "EMAIL";// 邮件类型
	private static final String INNERMAIL = "INNERMAIL";// 站内信

	private static final String COMPANYNAME = "companyName";// 公司名称

	private static final String APPLYREASON = "applyReason";// 驳回原因

	private static final String EXPORT_TEMPLATE_CERTIFICATION = "YKY客户编码,公司名称,公司类型,行业,区域,认证状态,开通权限,审核时间,营业期限,认证备注";
	
	private static final String EXPORT_TEMPLATE_ENT = "邮箱,联系人,手机号,公司名称,账号状态,用户来源,认证状态,账号类型,注册时间,最后登录时间";
	
	private static final String SHEET1 = "Sheet1";  //定义工作薄
	
	private static final String UTF8 = "UTF-8";
	
	private static final String UP_COMPANY = "UP_COMPANY";
	
	private static final String APPROVE_COMPANY = "APPROVE_COMPANY";
	
	@Autowired
	private PartyRelationshipDao relationshipDao;

	private static final String VERIFYURL = "/verify.htm";//发送邮件链接
	
	// workflow 27090 前缀
	@Value("${api.workflow.serverUrlPrefix}")
	private String workflowUrlPrefix;

	// party 前缀
	@Value("${api.party.serverUrlPrefix}")
	private String partyServerUrlPrefix;

	// customer
	@Value("${customer.serverUrlPrefix}")
	private String customerServerUrlPrefix;

	public static final ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private MsgSender msgSender;

	@Value("${mqConsumeConfig.sendMsgAndEmail.topicName}")
	private String sendMsgAndEmailTopicName;

	// 项目前缀
	@Value("${operation.serverUrlPrefix}")
	private String operationUrl;

	@Autowired
	@Qualifier(value = "aliyun.oss.account")
	private AliyunOSSAccount aliyunOSSAccount;

	// portal
	@Value("${portal.serverUrlPrefix}")
	private String portalServerUrlPrefix;

	@Value("${api.basedata.serverUrlPrefix}")
	private String serverUrl;

	@Autowired
	private AuthorizationUtil authorizationUtil;
	
	@Autowired
	private WorkflowClientBuilder workflowClientBuilder;
	
	@Autowired
	private ShipmentClientBuilder shipmentClientBuilder;
	
	@Autowired
	private PayClientBuilder payClientBuilder;
	
	@Value("${customer.serverUrlPrefix}")
	private String customerUrl;
	
	@Autowired
	private PartyCreditDao partyCreditDao;
	/**
	 * 根据保存的Url获取原图、缩略图
	 * 
	 * @param imgUrl
	 * @return
	 * @since 2016年6月15日
	 * @author zr.xuheng@yikuyi.com
	 */
	public Map<String, String> getImgUrlMap(String imgUrl) {
		Map<String, String> map = new HashMap<>();
		if (!StringUtils.isEmpty(imgUrl)) {
			ImageUrls imageUrl = AliyunOSSHelper.getImageUrl(aliyunOSSAccount, imgUrl, 1000L);
			map.put("image", imageUrl.getImage());
			if (imgUrl.toLowerCase().contains(".pdf")) {
				map.put("type", "1");
			} else {
				map.put("type", "0");
			}
		}
		return map;
	}

	/**
	 * 根据保存的Url获取原图、缩略图
	 * 
	 * @param imgUrl
	 * @return
	 * @since 2016年6月15日
	 * @author zr.xuheng@yikuyi.com
	 */
	public String getImagUrl(String url) {
		Map<String, String> map = getImgUrlMap(url);
		return map.get("image");

	}

	/**
	 * 账户认证申请保存信息
	 * 
	 * @param apply
	 * @return void
	 * @since 2016年1月19日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException
	 */
	@Audit(action = "Account Authenticationqqq;;;'#apply.applyOrgId'qqq;;;'#apply.reason'", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public void activeAccountSave(@Param("apply")Apply apply) throws BusinessException{
		if(null == apply){
			return;
		}
		String userId = RequestHelper.getLoginUserId();
		//判断当前账号是否是待审核如果是待审核则不允许提交抛出异常
		isReApplyCer(userId);
		// 获取内容
		String applyContent = apply.getApplyContent();
		// 把获取的内容转化为json格式
		JSONObject json = JSON.parseObject(applyContent);
		
		// 把jsonObject转化成对应的实体
		EnterpriseVo enterpriseVo = JSONObject.parseObject(json.toString(), EnterpriseVo.class);
		if (null == enterpriseVo) {
			return;
		}
		try {
			// 判断是企业还是个人 ,如果userType等于1为个人，否则为企业
			Integer userType = partyRoleDao.isPersonal(userId);
			String id;
			//usertype 等于1 为个人升级企业认证，否则为企业申请认证
			if (userType == 1) {
				// 先创建企业
				id = String.valueOf(IdGen.getInstance().nextId());
				enterpriseVo.setId(id);
				// 生成企业信息
				createEnt(userId, enterpriseVo, 0);
				// 修改个人信息
				createPerson(enterpriseVo, userId);
				// 修改个人注册地址
				saveBaseInfo(enterpriseVo, userId);
				// 插入企业与账户的关联
				saveRelationShip(userId, id, 1);

			} else {
				//根据当前登录id获取企业id
				Party p = partyDao.getPartyPersonDetail(userId);
				id = p.getCorporationId();
				// 判断是否有企业id，如果没有则创建
				if (StringUtils.isEmpty(id)) {
					// 先创建企业
					id = String.valueOf(IdGen.getInstance().nextId());
					enterpriseVo.setId(id);
					// 生成企业信息
					createEnt(userId, enterpriseVo, 0);
					// 插入企业与账户的关联
					saveRelationShip(apply.getApplyUserId(), id, 1);
				}
				enterpriseVo.setId(id);
				Party party = new Party();
				party.setId(id);// 设置企业id
				party.setLastUpdateDate(new Date());
				party.setLastUpdateUser(userId);
				partyDao.updateParty(party);
				// 修改partyGroup表设置为待审核状态
				updatePartyGroup(party, ActiveStatus.WAIT_APPROVE, null, null, userId, enterpriseVo.getName(),
						enterpriseVo.getLogo(), new Date(), null,null);
				// 修改属性
				updatePartyAttr(enterpriseVo, party);
				// 修改公司注册地址
				saveBaseInfo(enterpriseVo, id);

				// 修改账号信息person信息
				Party party1 = new Party();
				party1.setId(apply.getApplyUserId());// 设置企业id
				Person person = new Person();
				person.setLastNameLocal(enterpriseVo.getContactUserName());
				person.setTel(enterpriseVo.getContactUserTel());
				person.setMail(enterpriseVo.getMail());
				person.setPersonalTitle(enterpriseVo.getPersonalTitle());
				party1.setPerson(person);
				personDao.editPerson(party1);

			}
			apply.setApplyOrgId(id);
			// 调用workFlow服务
			workflowClientBuilder.applyClient().createApply(apply, authorizationUtil.getLoginAuthorization());
			
			// 给认证部发送邮件
			String urlMail = operationUrl + VERIFYURL;
			sendCerMail(urlMail, enterpriseVo.getName(), "资质", "APPLY_ACTIVE");
			//个人升级企业邮箱验证发送邮件
			if (userType == 1) {
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
				String senddate = format.format(new Date());
				String sid = senddate + "---" + enterpriseVo.getMail() + "---" + apply.getApplyUserId();
				byte[] b = Base64.encodeBase64(sid.getBytes(UTF8), true);
				sid = new String(b, UTF8);
				String url = customerUrl+"/accountSafe.htm?action=fromMailCreate&sid="+sid;
				sendPersonUpEntMail("PERSON_UP_ENT",enterpriseVo.getMail(),enterpriseVo.getContactUserName(),url);
			}

		} catch (Exception e) {
			logger.error("调用workflow服务异常：{},{}", e.getMessage(), e);
		}

	}
	public void sendPersonUpEntMail(String templateId,String toMail,String userName,String linkUrl){
		MailInfoVo mailInfoVo = new MailInfoVo();
		mailInfoVo.setTemplateId(templateId);
		mailInfoVo.setType(MailInfoVo.Type.EMAIL.toString());
		mailInfoVo.setTo(toMail);
		JSONObject object = new JSONObject();
		object.put("userName", userName);// 用户名
		object.put("linkUrl", linkUrl);// 链接
		mailInfoVo.setContent(object);
		logger.info("前台会员中心个人升级企业发送邮件参数：{}", JSONObject.toJSON(mailInfoVo).toString());
		msgSender.sendMsg(sendMsgAndEmailTopicName, mailInfoVo, null);
	}

	/**
	 * 个人升级企业创建一个企业
	 * 
	 * @param entUserId 企业用户id,enterpriseVo,isOldData是否是老数据
	 * @return EnterpriseVo
	 * @since 2017年5月10日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException
	 */
	private void createEnt(String entUserId, EnterpriseVo enterpriseVo, int isOldData) {
		String userId = RequestHelper.getLoginUserId();// 当前申请修改的人
		//生成一条party企业数据
		Party party = new Party();
		party.setId(enterpriseVo.getId());
		party.setPartyType(PartyType.CORPORATION);
		party.setPartyStatus(PartyStatus.PARTY_NOT_VERIFIED);
		party.setCreator(userId);
		party.setCreatedDate(new Date());
		party.setLastUpdateDate(new Date());
		party.setLastUpdateUser(userId);
		partyDao.insert(party);
		// 修改把当前个人用户变为企业用户即给corporationId设置id(企业id)
		Party partyNew = new Party();
		partyNew.setId(entUserId);
		partyNew.setCorporationId(enterpriseVo.getId());
		partyDao.updateParty(partyNew);
		// 新增partyGroup
		PartyGroup partyGroup = new PartyGroup();
		partyGroup.setGroupName(enterpriseVo.getName());
		partyGroup.setCreator(userId);
		partyGroup.setCreatedDate(new Date());
		partyGroup.setComments(enterpriseVo.getComments());
		partyGroup.setLastUpdateUser(userId);
		partyGroup.setLastUpdateDate(new Date());
		partyGroup.setApplyDate(new Date());
		partyGroup.setCreditComments(enterpriseVo.getCreditComments());
		if (isOldData == 0) {
			partyGroup.setActiveStatus(ActiveStatus.WAIT_APPROVE);
		}
		partyGroup.setAccountStatus(AccountStatus.ACCOUNT_NOT_VERIFIED);
		party.setPartyGroup(partyGroup);
		partyGroupDao.insert(party);
		// 插入公司官网
		insertCer(PartyAttributeConstants.WEBSITE_URL, enterpriseVo.getWebSite(), party, userId);
		// 公司类型
		insertCer(PartyAttributeConstants.CORPORATION_CATEGORY_ID, enterpriseVo.getCorCategory(), party, userId);
		// 所属行业
		insertCer(PartyAttributeConstants.INDUSTRY_CATEGORY_ID, enterpriseVo.getIndustryCategory(), party, userId);
		// 邓氏编码
		insertCer(PartyAttributeConstants.D_CODE, enterpriseVo.getdCode(), party, userId);
		// 修改公司注册地址
		saveBaseInfo(enterpriseVo, enterpriseVo.getId());
		// 设置other值
		List<Map<String, String>> otherAttrs = enterpriseVo.getOtherAttrs();
		// 如果为空则不执行后面的操作
		if (!CollectionUtils.isNotEmpty(otherAttrs)) {
			return;
		}
		// 循环插入值有公司类型其他和所属行业其他
		for (Map<String, String> map : otherAttrs) {
			Iterator<String> iterator = map.keySet().iterator();
			while (iterator.hasNext()) {
				String mapKey = iterator.next();
				if (!StringUtils.isEmpty(mapKey)) {
					insertCer(mapKey, map.get(mapKey), party, userId);
				}
			}

		}

	}

	/**
	 * 保存账号信息
	 * 
	 * @param entVo
	 * @param accountId 企业中的个人账户id
	 * @return
	 * @since 2017年4月28日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	private void createPerson(EnterpriseVo entVo, String accountId) {

		// PERSON 表生成记录 PARTY_ID 和 PARTY表一致。
		Party party = new Party();
		party.setId(accountId);
		Person person = new Person();
		person.setLastNameLocal(entVo.getApplyUser());
		person.setMail(entVo.getMail());
		person.setPersonalTitle(entVo.getPersonalTitle());
		person.setTel(entVo.getContactUserTel());
		person.setTelStatus("Y");
		person.setMailStatus("N");
		person.setCreator(accountId);
		person.setCreatedDate(new Date());
		person.setLastUpdateDate(new Date());
		person.setLastUpdateUser(accountId);
		person.setPersonTypeStatus(PersonTypeStatus.COMMON);
		person.setFixedTel(entVo.getFixedTel());
		party.setPerson(person);
		personDao.editPerson(party);

		// 删除个人用户原有的角色
		List<String> list = new ArrayList<>();
		list.add(RoleTypeEnum.CUSTOMER.toString());
		list.add(RoleTypeEnum.INDIVIDUAL_CUST.toString());
		partyRoleDao.deletePartyRoleByType(accountId, list);

		// d.生成 PARTY_ROLE 数据 ROLE_TYPE_ID 为：ENTERPRISE_CUST
		partyRoleDao.insert(accountId, RoleTypeEnum.ENTERPRISE_CUST.toString(), accountId, new Date(), accountId,
			new Date());
		// 给个人用户转企业，特殊角色标记
		partyRoleDao.insert(accountId, RoleTypeEnum.UPGRADE_CUST.toString(), accountId, new Date(), accountId,
			new Date());
	}

	/**
	 * 账户激活审核成功保存数据
	 * 
	 * @param jsonObject
	 * @return void
	 * @since 2016年1月19日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException
	 */
	public void save(Apply apply) {
		String userId = RequestHelper.getLoginUserId();
		if (null != apply) {
			// 获取内容
			String applyContent = apply.getApplyContent();
			// 把jsonObject转化成对应的实体
			EnterpriseVo enterpriseVo = JSONObject.parseObject(applyContent, EnterpriseVo.class);
			//根据申请用户id查询用户名和邮箱
			Party p = personDao.findPersonById(apply.getApplyUserId());
			String cerName = "";
			String mail = "";
			if (null != p && !StringUtils.isEmpty(p.getPerson())) {
				cerName = p.getPerson().getLastNameLocal();
				mail = p.getPerson().getMail();
			}
			// 驳回
			if (apply.getStatus() == ApplyStatus.REJECT) {
				Party party = new Party();
				party.setId(apply.getApplyOrgId());
				// 修改partyGrop的状态为驳回
				updatePartyGroup(party, ActiveStatus.REJECTED, null, null, userId, null, null, null, new Date(),null);
				// 给申请人发送驳回邮件
				String urlMail = customerServerUrlPrefix + "/enterprise/enterpriseInfo.htm";
				commonMsg(MailInfoVo.TemplateId.ACTIVE_REJECT.toString(), mail, urlMail, enterpriseVo.getName(),
						apply.getReason(), cerName, null);
				sendInnerMail("ACTIVE_REJECT", urlMail, enterpriseVo);
			} else {
				// 审核成功
				if (null != enterpriseVo) {
					saveApply(enterpriseVo);
					// 发送邮件
					commonMsg(MailInfoVo.TemplateId.ACTIVE_PASS.toString(), mail, null, enterpriseVo.getName(), null,
							cerName, null);
					sendInnerMail("ACTIVE_PASS", null, enterpriseVo);
					//发送代金券
					sendCoupon(apply);
			   }
			}
		}
	
	}

	private void sendCoupon(Apply apply) {
		try{
			
				//个人升级企业，企业认证成功后发代金券
				RoleTypeVo roleType = partyRoleDao.getRoleType(apply.getApplyUserId(),RoleTypeEnum.UPGRADE_CUST.toString());
				if(null != roleType){
					//先判断当前用户有没有领过代金券
				   CouponParty coupon = payClientBuilder.couponResource().getBybusinessTypeCoupon(apply.getApplyUserId(), UP_COMPANY);
		           if(null == coupon){
					 payClientBuilder.couponResource().regOrUpSendCoupon(UP_COMPANY, apply.getApplyUserId(), authorizationUtil.getLoginAuthorization());
		           }
		         }else{
		        	//先判断当前用户有没有领过代金券
					CouponParty coupon1 = payClientBuilder.couponResource().getBybusinessTypeCoupon(apply.getApplyUserId(), APPROVE_COMPANY);
					if(null == coupon1){
					  payClientBuilder.couponResource().regOrUpSendCoupon(APPROVE_COMPANY, apply.getApplyUserId(), authorizationUtil.getLoginAuthorization());
					}
				}
		   
		}catch (Exception e) {
			logger.error("会员中心提交申请，审核回调地址异常：{},{}",e.getMessage(),e);
		}
	}
	
	/**
	 * 抽取激活账户审核成功信息
	 * 
	 * @param enterpriseVo
	 * @return void
	 * @since 2016年1月19日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException
	 */
	public void saveApply(EnterpriseVo entVo) {
		String userId = RequestHelper.getLoginUserId();
		Party party = new Party();
		party.setId(entVo.getId());
		party.setLastUpdateDate(new Date());
		party.setLastUpdateUser(userId);
		partyDao.updateParty(party);
		// 修改partygroup状态为已审核
		updatePartyGroup(party, ActiveStatus.PARTY_VERIFIED, null, null, userId, entVo.getName(), entVo.getLogo(), null,
				new Date(),null);

		// 插入资质信息
		insertCertificate(entVo, userId, party);
		// 根据企业id判断当前企业是否有生成过组织代码 ,一个企业只能插入一条数据
		// 如果没有组织机构代码则根据公司名称查询 用户名，组织机构代码，key
		createVipEnt(entVo);

	}

	private void createVipEnt(EnterpriseVo entVo) {
		if (!StringUtils.isEmpty(entVo.getOccCode())) {
			// 如果有组织机构代码则按照名称和组织机构代码去查询
			List<EnterpriseVo> entList = partyAttributeDao.findPartyCodeList(null, entVo.getOccCode(), PartyAttributeConstants.ORG_CODE);
			if (!CollectionUtils.isNotEmpty(entList)) {
				// 新增一个vip企业
				newEnt(entVo);
			} else {
				// 先失效原有的关联关系，重新生成新的关联关系
				createNewEntRelation(entVo, entList);
			}
		} else {
			List<EnterpriseVo> entList = partyAttributeDao.findPartyCodeList(entVo.getName(), null, PartyAttributeConstants.ORG_CODE);
			if (!CollectionUtils.isNotEmpty(entList)) {
				// 新增一个vip企业
				newEnt(entVo);
			} else {
				createNewEntRelation(entVo, entList);
			}
		}
	}

	// 新建关联关系
	private void createNewEntRelation(EnterpriseVo entVo, List<EnterpriseVo> entList) {
		String userId = RequestHelper.getLoginUserId();
		Party party = new Party();
		party.setId(entVo.getId());
		// 失效被审核企业与原有VIP企业的关联关系
		PartyRelationship relationShip = PartyRelationship.build(PartyRelationshipTypeEnum.ENTERPRISE_CERTIFIED);
		relationShip.setPartyIdFrom(entVo.getId());
		List<PartyRelationship> relationList = relationshipDao.getPartyRelationship(relationShip);
		//失效原企业的账期
		if (CollectionUtils.isNotEmpty(relationList)) {
			String vipId = relationList.get(0).getPartyIdTo();//获取vipId
			PartyCredit partyCredit = partyCreditDao.getPartyCredit(vipId, null);
	        if(null != partyCredit){
	        	//失效账期
	        	partyCredit.setThruDate(new Date());
	        	partyCredit.setLastUpdateDate(new Date());
	        	partyCredit.setLastUpdateUser(userId);
	        	partyCreditDao.update(partyCredit);
	        	//修改当前企业的状态为未开通
	        	PartyGroup partyGroup = new PartyGroup();
	    		partyGroup.setAccountPeriodStatus(AccountPeriodStatus.PERIOD_NOT_VERIFIED);
	    		party.setPartyGroup(partyGroup);
	    		partyGroupDao.updatePartyGroup(party);
	    		//删除该企业下所有的用户的账期角色
	    		//通过企业id找用户id
	    		// 查询所有子账号
				List<UserExtendVo> userList = partyDao.getEnterpriseAccountsList(vipId);
	    		if(CollectionUtils.isNotEmpty(userList)){
	    			 for (UserExtendVo userVo : userList) {
	    				// 先删除用户账期角色
	    				List<String> list = new ArrayList<>();
	    				list.add(RoleTypeEnum.ACCOUNT_CUST.toString());
	    				partyRoleDao.deletePartyRoleByType(userVo.getId(), list);
	    			}
	    		}
	        }
	        
		}
		relationShip.setThruDate(new Date());
		relationShip.setStatusId(PartyRelationshipStatus.DISABLED);
		relationshipDao.updateRelationShip(relationShip);
		

		// 建立被审核通过的企业与已经存在的VIP企业关系
		saveRelationShip(entVo.getId(), entList.get(0).getId(), 2);
		// 修改yky编码
		party.setLastUpdateDate(new Date());
		party.setPartyCode(entList.get(0).getPartyCode());
		party.setLastUpdateUser(userId);
		partyDao.updateParty(party);
		
		//查看关联的企业是否开通账期如果开通，则当前企业也享有账期的权限
		PartyCredit partyCurrentCredit = partyCreditDao.getPartyCredit(entList.get(0).getId(), null);
        if(null != partyCurrentCredit){
        	//修改当前企业的状态为账期通过
        	PartyGroup partyGroup = new PartyGroup();
    		partyGroup.setAccountPeriodStatus(AccountPeriodStatus.PERIOD_VERIFIED);
    		party.setPartyGroup(partyGroup);
    		partyGroupDao.updatePartyGroup(party);
    		//给关联过来的企业新增账期角色
    		List<UserExtendVo> userList = partyDao.listAccountByEntId(entVo.getId());
    		if(CollectionUtils.isNotEmpty(userList)){
    			 for (UserExtendVo userVo : userList) {
    				// 先删除用户账期角色
    				List<String> list = new ArrayList<>();
    				list.add(RoleTypeEnum.ACCOUNT_CUST.toString());
    				partyRoleDao.deletePartyRoleByType(userVo.getId(), list);
    				// 插入角色表
    				partyRoleDao.insert(userVo.getId(), RoleTypeEnum.ACCOUNT_CUST.toString(), userId, new Date(),
    						userId, new Date());
    			}
    		}
    		
        }
	}

	// 新增一个vip企业
	void newEnt(EnterpriseVo entVo) {
		String userId = RequestHelper.getLoginUserId(); // 重新备份一份数据到party表中
		// 插入到party表
		Party partyNew = new Party();
		String id = String.valueOf(IdGen.getInstance().nextId());
		partyNew.setId(id);
		partyNew.setPartyType(PartyType.VIP_CORPORATION);
		partyNew.setPartyStatus(PartyStatus.PARTY_ENABLED);
		partyNew.setCreatedDate(new Date());
		partyNew.setCreator(userId);
		partyDao.insert(partyNew);

		// 插入group表
		PartyGroup partyGroupNew = new PartyGroup();
		partyGroupNew.setGroupName(entVo.getName());
		partyGroupNew.setCreator(userId);
		partyGroupNew.setCreatedDate(new Date());
		partyGroupNew.setApplyDate(new Date());
		partyGroupNew.setApprovedDate(new Date());
		partyGroupNew.setActiveStatus(ActiveStatus.PARTY_VERIFIED);
		partyGroupNew.setAccountStatus(AccountStatus.ACCOUNT_NOT_VERIFIED);
		partyNew.setPartyGroup(partyGroupNew);
		partyGroupDao.insert(partyNew);

		// 失效审核的企业与老的VIsP企业关系
		String oldId = entVo.getId();
		PartyRelationship relationShip = PartyRelationship.build(PartyRelationshipTypeEnum.ENTERPRISE_CERTIFIED);
		relationShip.setPartyIdFrom(oldId);// 生成新的企业id
		relationShip.setThruDate(new Date());
		relationShip.setStatusId(PartyRelationshipStatus.DISABLED);
		relationshipDao.updateRelationShip(relationShip);

		// 插入新企业与老企业的关系
		saveRelationShip(entVo.getId(), id, 2);
        //给属性设置值
		setEntAttr(entVo, partyNew);
		// 调用服务获取企业联系信息
		List<PartyContactMech> partyContactMechList = partyContactMechManager
				.selectPartyContactMechByType(PurposeType.REGISTER_LOCATION, entVo.getId(), null);
		if (null != partyContactMechList && !partyContactMechList.isEmpty()) {
			PartyContactMech partyContactMech = partyContactMechList.get(0);
			// 获取基本信息
			getContactMech(entVo, partyContactMech);
		}
		// 修改注册地
		saveBaseInfo(entVo, id);
		// 插入组织机构代码信息和注册地
		insertCertificate(entVo, userId, partyNew);

	}

	private void setEntAttr(EnterpriseVo entVo, Party partyNew) {
		List<PartyAttribute> attrList = partyAttributeDao.getPartAttribute(entVo.getId());
		if (null != attrList && CollectionUtils.isNotEmpty(attrList)) {
			List<Map<String, String>> otherAttrs = new ArrayList<>();
			for (PartyAttribute attr : attrList) {
				if (attr.getKey().equals(PartyAttributeConstants.CORPORATION_CATEGORY_ID)) {
					entVo.setCorCategory(attr.getValue());
				}
				if (attr.getKey().equals(PartyAttributeConstants.INDUSTRY_CATEGORY_ID)) {
					entVo.setIndustryCategory(attr.getValue());
				}
				if (attr.getKey().equals(PartyAttributeConstants.WEBSITE_URL)) {
					entVo.setWebSite(attr.getValue());
				}
				if (attr.getKey().equals(PartyAttributeConstants.D_CODE)) {
					entVo.setdCode(attr.getValue());
				}
				if (attr.getKey().equals(PartyAttributeConstants.INDUSTRY_CATEGORY_ID_OTHER) || attr.getKey().equals(PartyAttributeConstants.CORPORATION_CATEGORY_ID_OTHER)) {
					Map<String, String> otherMap = new HashMap<>();
					otherMap.put(attr.getKey(), attr.getValue());
					otherAttrs.add(otherMap);
					entVo.setOtherAttrs(otherAttrs);
				}

			}
			// 新增或修改属性
			updatePartyAttr(entVo, partyNew);

		}
	}

	/**
	 * 获取公司信息抽取方法
	 * 
	 * @param entVo
	 * @param partyContactMech
	 * @return UserExtendVo
	 * @since 2016年1月12日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException
	 */
	private void getContactMech(EnterpriseVo entVo, PartyContactMech partyContactMech) {
		if (null != partyContactMech) {
			ContactMech contactMech = partyContactMech.getContactMech();
			if (null != contactMech.getId()) {
				// 地址信息
				PostalAddress postalAddress = contactMech.getPostalAddress();
				if (null != postalAddress) {
					entVo.setAddress(postalAddress.getAddress1());
					entVo.setProvince(postalAddress.getProvinceGeoId());
					entVo.setProvinceName(postalAddress.getProvinceGeoName());
					entVo.setCity(postalAddress.getCityGeoId());
					entVo.setCityName(postalAddress.getCityGeoName());
					entVo.setCountry(postalAddress.getCountyGeoId());
					entVo.setCountryName(postalAddress.getCountyGeoName());
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	void insertCertificate(EnterpriseVo entVo, String userId, Party party) {
		// 删除资质
		Map<String, String> map = entVo.getMap();
		if (null == map) {
			return;
		}
		List<String> delKeyList = new ArrayList<>();
		PartyAttributes attrList = new PartyAttributes();
		for (Object map1 : map.entrySet()) {
			delKeyList.add(((Map.Entry) map1).getKey().toString());
		}
		attrList.setKeyList(delKeyList);
		party.setPartyAttributes(attrList);
		// 先删除资质信息
		partyAttributeDao.deleteByIdOrKey(party);

		PartyAttributes cerAttr;
		PartyAttribute cer;
		Iterator<String> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			cerAttr = new PartyAttributes();
			cer = new PartyAttribute();
			// 插入其他值
			String mapKey = iterator.next();
			if(!StringUtil.isNullOrEmpty(map.get(mapKey))){
				cer.setKey(mapKey);// 将map的key设置给partyAttribute的key
				cer.setValue(map.get(mapKey));// 将map的value设置给partyAttribute的value
				cer.setCreator(userId);
				cer.setCreatedDate(new Date());
				cerAttr.setCertificateAttrs(cer);
				party.setPartyAttributes(cerAttr);
				partyAttributeDao.insertCerAttrs(party);
			}
		}
			

	}

	void sendCerMail(String urlMail, String name, String type, String template) {
		// 查询认证部邮件
		List<String> list = new ArrayList<>();
		list.add(RoleTypeEnum.CUST_CERT_SPECIALIST.toString());
		List<Party> cerPartyList = personDao.findDataByRole(list);
		if (null != cerPartyList && !cerPartyList.isEmpty()) {
			for (int i = 0; i < cerPartyList.size(); i++) {
				Party partyMail = cerPartyList.get(i);
				if (null != partyMail && null != partyMail.getPerson()) {
					// 获取认证部的名字
					String cerName = partyMail.getPerson().getLastNameLocal();
					// 要发送的邮件
					String email = partyMail.getPerson().getMail();
					commonMsg(template, email, urlMail, name, null, cerName, type);
				}
			}
		}
	}

	public void commonMsg(String templateId, String toMail, String herfUrl, String companyName, String reson,
			String cerName, String type) {
		MailInfoVo mailInfoVo = new MailInfoVo();
		mailInfoVo.setTemplateId(templateId);
		mailInfoVo.setType(MailInfoVo.Type.EMAIL.toString());
		mailInfoVo.setTo(toMail);
		JSONObject object = new JSONObject();
		object.put("cerName", cerName);// 用户名
		object.put("urlMail", herfUrl);// 链接地址
		object.put(COMPANYNAME, companyName);// 公司名称
		object.put(APPLYREASON, reson);// 驳回原因
		object.put("type", type);// 判断是资质还是子账号
		String logoPrefix = portalServerUrlPrefix;// portal项目前缀
		object.put("portalUrl", logoPrefix);// 公司logoUrl
		mailInfoVo.setContent(object);
		logger.info("激活账户邮件参数：{}", JSONObject.toJSON(mailInfoVo));
		msgSender.sendMsg(sendMsgAndEmailTopicName, mailInfoVo, null);
	}

	/**
	 * 发送站内信
	 * 
	 * @param emplateId
	 * @param url
	 * @param vo
	 * @since 2017年2月21日
	 * @author zr.helinmei@yikuyi.com
	 */
	public void sendInnerMail(String emplateId, String url, EnterpriseVo vo) {
		try {
			MailInfoVo mailInfoVo = new MailInfoVo();
			mailInfoVo.setTemplateId(emplateId);
			mailInfoVo.setType(INNERMAIL);
			mailInfoVo.setTo(vo.getId());
			JSONObject content = new JSONObject();
			content.put("url", url);
			mailInfoVo.setTo(vo.getId());
			msgSender.sendMsg(sendMsgAndEmailTopicName, mailInfoVo, null);
		} catch (Exception e) {
			logger.error("站内信邮件发送失败：{}", e);
		}
	}

	/**
	 * 保存地址信息
	 * 
	 * @param UserExtendVo
	 * @return UserExtendVo
	 * @since 2016年1月12日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException
	 */
	void saveBaseInfo(EnterpriseVo entVo, String partyId) {
		if (null != entVo) {
			PartyContactMech partyContactMech = new PartyContactMech();
			partyContactMech.setPurposeType(PurposeType.REGISTER_LOCATION);
			ContactMech contactMech = new ContactMech();
			PostalAddress postalAddress = new PostalAddress();
			postalAddress.setAddress1(entVo.getAddress());
			postalAddress.setCountryGeoName("中国");
			postalAddress.setCountryGeoId("china");

			postalAddress.setProvinceGeoName(entVo.getProvinceName());
			postalAddress.setProvinceGeoId(entVo.getProvince());

			postalAddress.setCountyGeoName(entVo.getCountryName());
			postalAddress.setCountyGeoId(entVo.getCountry());

			postalAddress.setCityGeoName(entVo.getCityName());
			postalAddress.setCityGeoId(entVo.getCity());
			//电话信息
			TelecomNumber telecomNumber = new TelecomNumber();
			//qq
			QqTelecomNumber qqTelecomNumber = new QqTelecomNumber();
			qqTelecomNumber.setContactNumber(entVo.getContactUserQQ());
			qqTelecomNumber.setMechType(MechType.QQ);
			telecomNumber.setQqTelecomNumber(qqTelecomNumber);
			contactMech.setTelecomNumber(telecomNumber);
			contactMech.setPostalAddress(postalAddress);

			partyContactMech.setContactMech(contactMech);
			partyContactMech.setPartyId(partyId);
			partyContactMechManager.insert(partyContactMech);

		}
	}

	/**
	 * 修改公司信息
	 * 
	 * @param enterpriseVo
	 * @return
	 * @since 2017年2月7日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Audit(action = "Enterprise Modifyqqq;;;'#enterpriseVo.id'qqq;;;'#enterpriseVo.reason'", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public void editCompany(@Param("enterpriseVo") EnterpriseVo enterpriseVo) {
		if (null != enterpriseVo) {
			updateParty(enterpriseVo, 0);
		}
	}
	/**
	 * 修改公司信息不加日志
	 * 
	 * @param enterpriseVo
	 * @return void
	 * @since 2017年2月7日
	 * @author zr.helinmei@yikuyi.com
	 */
	public void editMemberCompany(EnterpriseVo enterpriseVo) {
		if (null != enterpriseVo) {
			updateParty(enterpriseVo, 0);
		}
	}
	/**
	 * 修改公司信息抽取方法
	 * 
	 * @param entVo
	 * @param isOldData 老数据
	 * @return void
	 * @since 2016年1月19日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException
	 */
	public void updateParty(EnterpriseVo entVo, int isOldData) {
		String userId = RequestHelper.getLoginUserId();
		// 兼容老数据
		Party p = partyDao.getPartyPersonDetail(entVo.getId());// 根据提交的企业用户id查找企业id
		String corporationId = p.getCorporationId();// 企业id
		String partyType = p.getPartyType().toString();
		// 认证企业也没有企业id所以区分。
		if (StringUtils.isEmpty(p.getCorporationId()) && partyType != PartyType.VIP_CORPORATION.toString()) {
			// 老数据,重新生成一个企业账户
			String id = String.valueOf(IdGen.getInstance().nextId());
			// 插入企业与账户的关联地一个为企业用户id,第二个为企业id
			saveRelationShip(entVo.getId(), id, 1);
			entVo.setId(id);// 因为后面调用的方法entVo里面的id都为企业id,所以把entVo的id设置为企业id
			// 生成企业信息
			createEnt(userId, entVo, 1);
		} else {
			// 认证页面的修改
			if (isOldData != 1) {
				entVo.setId(corporationId);
			}
			Party party = new Party();
			party.setId(entVo.getId());
			party.setLastUpdateDate(new Date());
			party.setLastUpdateUser(userId);
			party.setPartyCode(entVo.getPartyCode());// yky编码
			partyDao.updateParty(party);

			// 修改企业名和logo
			updatePartyGroup(party, null, null, entVo.getComments(), userId, entVo.getName(), entVo.getLogo(), null,
					null,entVo.getCreditComments());

			// 修改企業屬性
			updatePartyAttr(entVo, party);

			// 修改公司注册信息
			saveBaseInfo(entVo, entVo.getId());
		}

	}

	/**
	 * 抽取方法修改企业属性信息
	 * 
	 * @param entVo,party
	 * @return void
	 * @since 2016年1月19日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException
	 */
	private void updatePartyAttr(EnterpriseVo entVo, Party party) {
		// 修改企业属性
		// 公司类型
		if (!StringUtils.isEmpty(entVo.getCorCategory())) {
			partyAttributeDao.save(party.getId(), PartyAttributeConstants.CORPORATION_CATEGORY_ID, entVo.getCorCategory());
		}
		// 所属行业
		if (!StringUtils.isEmpty(entVo.getIndustryCategory())) {
			partyAttributeDao.save(party.getId(), PartyAttributeConstants.INDUSTRY_CATEGORY_ID, entVo.getIndustryCategory());
		}
		// 判断公司官网是否有插入值如果没插入就插入，插入了就修改
		if (!StringUtils.isEmpty(entVo.getWebSite())) {
			partyAttributeDao.save(party.getId(), PartyAttributeConstants.WEBSITE_URL, entVo.getWebSite());
		}
		// 判断邓氏编码是否有插入值如果没插入就插入，插入了就修改
		if (!StringUtils.isEmpty(entVo.getdCode())) {
			partyAttributeDao.save(party.getId(), PartyAttributeConstants.D_CODE, entVo.getdCode());
		}

		// 如果原本是图片后来修改成pdf则需要新增pdf名字，否则修改pdf名字
		// 设置other值
		List<Map<String, String>> otherAttrs = entVo.getOtherAttrs();
		if (!CollectionUtils.isNotEmpty(otherAttrs)) {
			return;
		}
		PartyAttributes otherAttr;
		PartyAttribute other;
		// 先删除other,然后新增
		List<String> delKeyList = new ArrayList<>();
		PartyAttributes attrList = new PartyAttributes();
		delKeyList.add(PartyAttributeConstants.INDUSTRY_CATEGORY_ID_OTHER);
		delKeyList.add(PartyAttributeConstants.CORPORATION_CATEGORY_ID_OTHER);
		attrList.setKeyList(delKeyList);
		party.setPartyAttributes(attrList);
		partyAttributeDao.deleteByIdOrKey(party);
		// 循环得到key
		for (Map<String, String> map : otherAttrs) {
			Iterator<String> iterator = map.keySet().iterator();
			while (iterator.hasNext()) {
				// 插入其他值
				String mapKey = iterator.next();
				otherAttr = new PartyAttributes();
				other = new PartyAttribute();
				if(!StringUtil.isNullOrEmpty(map.get(mapKey))){
					other.setKey(mapKey);// 将map的key设置给partyAttribute的key
					other.setValue(map.get(mapKey));// 将map的value设置给partyAttribute的value
					otherAttr.setOtherAttrs(other);
					party.setPartyAttributes(otherAttr);
					partyAttributeDao.insertOtherAttrs(party);
				}

			}
		}
	}

	void updateAttr(String key, String value, Party party) {
		party.setExternalId(key);
		party.setDescription(value);
		partyAttributeDao.updateEnterpriseAttribute(party);
	}

	/**
	 * 修改认证会员企业信息
	 * 
	 * @param entVo
	 * @return void
	 * @since 2017年5月3日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException
	 */
	@Audit(action = "EntQualifications Modifyqqq;;;'#entVo.id'qqq;;;'#entVo.reason'", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public void updateQualifications(@Param("entVo") EnterpriseVo entVo) {
		if(StringUtils.isEmpty(entVo.getReason())){
			entVo.setReason("--");
		}
		updateParty(entVo, 1);// 用来区分新老数据和认证企业信息
		String userId = RequestHelper.getLoginUserId();
		Party party = new Party();
		party.setId(entVo.getId());
		// 新增资质
		insertCertificate(entVo, userId, party);

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
	public UserVo getAdmin(String id, String role) {
		UserVo userVo = new UserVo();
		if (!"admin".equals(role)) {
			return userVo;
		}
		// 查询企业id,关联时间
		PartyRelationship partyRelationship = PartyRelationship.build(PartyRelationshipTypeEnum.EMPLOYMENT);
		partyRelationship.setPartyIdFrom(id);
		List<PartyRelationship> relationshipList = relationshipDao.getPartyRelationship(partyRelationship);
		if (relationshipList == null || relationshipList.isEmpty()) {
			return userVo;
		}
		partyRelationship = relationshipList.get(0);
		// 企业id
		String entId = partyRelationship.getPartyIdTo();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 根据企业id查询企业名称
		Party party = partyDao.getPartyDetail(entId, PartyType.CORPORATION);
		if (party != null && party.getPartyGroup() != null) {
			String companyName = party.getPartyGroup().getGroupName();
			userVo.setCompanyName(companyName);
		}
		// 关联时间
		String relationDate = formatter.format(partyRelationship.getFromDate());
		userVo.setRelationDate(relationDate);
		// 根据企业id查询管理员id
		PartyRelationship partyRelationship1 = PartyRelationship.build(PartyRelationshipTypeEnum.COS_PLAY);
		partyRelationship1.setPartyIdTo(entId);
		relationshipList = relationshipDao.getPartyRelationship(partyRelationship1);
		if (relationshipList == null || relationshipList.isEmpty()) {
			return userVo;
		}
		String adminId = relationshipList.get(0).getPartyIdFrom();

		// 根据管理员id查询邮箱地址,名称
		Party party1 = partyDao.getPartyPersonDetail(adminId);

		if (party1 != null && party1.getPerson() != null) {
			userVo.setName(party1.getPerson().getLastNameLocal());
		}

		// 获取企业联系信息
		String email = userLoginDao.findUserLogin(adminId, EMAIL);
		userVo.setMail(email);

		return userVo;
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
	@Audit(action = "User Modifyqqq;;;修改'#party.person.mail'账号状态为'#party.partyStatus'", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public void updateStatus(@Param("party") Party party) {
		partyDao.updateParty(party);
	}

	/**
	 * 根据用户的id判断是否为管理员
	 * 
	 * @param id
	 * @return
	 * @since 2017年2月7日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public Boolean isAdmin(String id) {
		Boolean falg = false;
		// 查询企业id,关联时间
		PartyRelationship partyRelationship = PartyRelationship.build(PartyRelationshipTypeEnum.COS_PLAY);
		partyRelationship.setPartyIdFrom(id);
		List<PartyRelationship> relationshipList = relationshipDao.getPartyRelationship(partyRelationship);
		if (relationshipList != null && !relationshipList.isEmpty()) {
			partyRelationship = relationshipList.get(0);
			String entId = partyRelationship.getPartyIdTo();
			Party party = partyGroupDao.findPartyGroupByPartyId(entId);
			if (party == null) {
				return falg;
			}
			if (party.getPartyGroup().getActiveStatus() != null && ActiveStatus.PARTY_VERIFIED.toString()
					.equals(party.getPartyGroup().getActiveStatus().toString())) {
				falg = true;
			} else {
				falg = false;
			}

		}
		return falg;
	}

	/**
	 * 根据用户ID获取企业ID
	 * @param employeeId
	 * @return
	 * @since 2017年6月16日
	 * @author jik.shu@yikuyi.com
	 */
	public String getEnterpriseIdByPartyId(String partyId) {
		PartyRelationship pr = PartyRelationship.build(PartyRelationshipTypeEnum.EMPLOYMENT);
		pr.setPartyIdFrom(partyId);
		List<PartyRelationship> relationshipList = relationshipDao.getPartyRelationship(pr);
		// 这里排除一个员工受雇于多家公司的情况
		return CollectionUtils.isNotEmpty(relationshipList) ? relationshipList.get(0).getPartyIdTo() : null;
	}

	/**
	 * 根据用户的id判断是否为激活或者关联，返回true：已经激活或者关联，fail：未激活或者未关联
	 * 
	 * @param id
	 * @return
	 * @since 2017年1月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public String isActivedOrRelationed(String id) {
		// 没有激活
		String falg = NOTACTIVE;
		// 判断关联
		Party p = personDao.findPersonById(id);
		if (p != null && p.getPerson() != null && p.getPerson().getRelationSratus() != null
				&& Person.RelationSratus.RELATED.toString().equals(p.getPerson().getRelationSratus().toString())) {
			// 已关联
			falg = RELATED;
			return falg;
		}
		if (p != null && p.getPerson() != null && p.getPerson().getRelationSratus() != null) {
			// 关联中
			falg = RELATING;
			// 判断激活
			PartyRelationship partyRelationship = PartyRelationship.build(PartyRelationshipTypeEnum.COS_PLAY);
			partyRelationship.setPartyIdFrom(id);
			List<PartyRelationship> relationshipList = relationshipDao.getPartyRelationship(partyRelationship);
			if (relationshipList != null && !relationshipList.isEmpty()) {
				// 激活中
				falg = ACTIVING;
				partyRelationship = relationshipList.get(0);
				String entId = partyRelationship.getPartyIdTo();
				Party party = partyGroupDao.findPartyGroupByPartyId(entId);
				if (party != null && party.getPartyGroup().getActiveStatus() != null && ActiveStatus.PARTY_VERIFIED
						.toString().equals(party.getPartyGroup().getActiveStatus().toString())) {
					// 已激活
					falg = ACTIVED;
					return falg;
				}
			}
			return falg;
		}
		// 判断激活
		PartyRelationship partyRelationship = PartyRelationship.build(PartyRelationshipTypeEnum.COS_PLAY);
		partyRelationship.setPartyIdFrom(id);
		partyRelationship.setRoleTypeIdFrom(RoleTypeEnum.MAIN_ROLE.toString());
		partyRelationship.setRoleTypeIdTo(RoleTypeEnum.CORPORATION.toString());
		List<PartyRelationship> relationshipList = relationshipDao.getPartyRelationship(partyRelationship);
		if (relationshipList != null && !relationshipList.isEmpty()) {
			// 激活中
			falg = ACTIVING;
			partyRelationship = relationshipList.get(0);
			String entId = partyRelationship.getPartyIdTo();
			Party party = partyGroupDao.findPartyGroupByPartyId(entId);
			if (party != null && party.getPartyGroup().getActiveStatus() != null && ActiveStatus.PARTY_VERIFIED
					.toString().equals(party.getPartyGroup().getActiveStatus().toString())) {
				// 已激活
				falg = ACTIVED;
				return falg;
			}
		}
		Party party1 = partyDao.getPartyPersonDetail(id);
		if (null != party1) {
			String entId = party1.getCorporationId();// 获取企业Id
			if (!StringUtils.isEmpty(entId)) {
				Party party2 = partyDao.getPartyDetail(entId, PartyType.CORPORATION);
				if (null != party2 && null != party2.getPartyGroup() && null != party2.getPartyGroup().getActiveStatus()
						&& party2.getPartyGroup().getActiveStatus() == PartyGroup.ActiveStatus.REJECTED) {
					// 激活中
					falg = ACTIVING;
				}
			}

		}

		return falg;
	}

	/**
	 * 根据用户的id判断是否为首次激活或者关联
	 * 
	 * @return
	 * @since 2017年2月17日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public Boolean isFristActive(String id) {
		Boolean falg = false;
		// 查询企业id,关联时间
		PartyRelationship partyRelationship = PartyRelationship.build(PartyRelationshipTypeEnum.EMPLOYMENT);
		partyRelationship.setPartyIdFrom(id);
		List<PartyRelationship> relationshipList = relationshipDao.getPartyRelationship(partyRelationship);
		if (relationshipList != null && !relationshipList.isEmpty()) {
			return falg;
		}
		Party party = personDao.findPersonById(id);
		if (party != null && party.getPerson() != null && party.getPerson().getRelationSratus() != null) {
			return falg;
		}
		party = partyDao.getPartyPersonDetail(id);
		if (party != null && !StringUtils.isEmpty(party.getCorporationId())) {
			return falg;
		}
		falg = true;
		return falg;
	}

	/**
	 * 获取用户信息
	 * 
	 * @param id
	 * @return
	 * @since 2017年3月28日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public UserVo getUser(String id) {
		return userLoginDao.getPerson(id);
	}

	/**
	 * 导出企业会员列表
	 * 
	 * @param vo
	 * @return
	 * @since 2017年4月13日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public void exportEnt(EnterpriseParamVo vo, HttpServletResponse response) throws IOException {
		// 查询数据
		List<EnterpriseVo> list = partyDao.getEntList(vo);
		response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Content-Disposition", "attachment; filename="+ new String("企业会员信息列表".getBytes("gb2312"), "ISO8859-1") + ".xls");
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Expires", "0");
		this.exportExcelXls(list, response.getOutputStream());
	}

	/**
	 * 导出企业会员数据
	 * 
	 * @param list
	 * @param os
	 * @since 2017年4月13日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	 void exportExcelXls(List<EnterpriseVo> list, OutputStream os) {
		ExportProcesser processer = null;
		try {
			processer = ExportFactory.getProcesser(ExportFactory.TYPE_XLS, os);

			// 标题
			processer.writeLine(SHEET1, EXPORT_TEMPLATE_ENT.split(","));
			List<List<String>> rowDataList;
			rowDataList = this.enterpriseDataList(list);
			for (List<String> rowData : rowDataList) {
				processer.writeLine(SHEET1, rowData);
			}
			processer.output();
		} catch (Exception e) {
			logger.error("导出企业会员信息列表：{}", e);
		} finally {
			if (null != processer) {
				processer.close();
			}
		}

	}

	/**
	 * 重构数据
	 * 
	 * @param enterpriseVo
	 * @return
	 * @since 2017年4月13日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	private List<List<String>> enterpriseDataList(List<EnterpriseVo> list) {
		List<List<String>> rowDataList = Lists.newArrayList();
		if (!CollectionUtils.isNotEmpty(list)) {
			return rowDataList;
		}
		List<String> rowData;
		for (EnterpriseVo enterpriseVo : list) {
			rowData = Lists.newArrayList();
			//邮箱
			rowData.add(enterpriseVo.getMail()==null?"":enterpriseVo.getMail());
			//联系人
			rowData.add(enterpriseVo.getContactUserName()==null?"":enterpriseVo.getContactUserName());
			//手机号
			rowData.add(enterpriseVo.getContactUserTel()==null?"":enterpriseVo.getContactUserTel());
			//公司名称
			rowData.add(enterpriseVo.getName()==null?"":enterpriseVo.getName());
			//账号状态(有效：PARTY_ENABLED，冻结：PARTY_DISABLED，未激活：PARTY_NOT_VERIFIED)
			rowData.add(enterpriseVo.getStatus()==null?"":PartyActiveStatus.enum2Desc(enterpriseVo.getStatus().toString()));
		    //用户来源(非UPGRADE_CUST（个人升级企业客户）状态下都是线上状态)
			String userResource;
			if(enterpriseVo.getRoleType()!=null &&  ((RoleTypeEnum.UPGRADE_CUST.toString()).equals(enterpriseVo.getRoleType()))){
				userResource="个人升级";
			}else{
				userResource="线上注册";
			}
			rowData.add(userResource);
			
			//认证状态
			rowData.add(enterpriseVo.getActiveStatus()==null?"":PartyActiveStatus.enum2Desc(enterpriseVo.getActiveStatus().toString()));
			//账号类型
			rowData.add(enterpriseVo.getAccountStatus()==null?"":PartyActiveStatus.enum2Desc(enterpriseVo.getAccountStatus().toString()));
			DateFormat  df  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String newRegTime = "";
			if(enterpriseVo.getRegTime() !=null){
				newRegTime = df.format(enterpriseVo.getRegTime());
			}
			//注册时间
			rowData.add(newRegTime);
			String newLastLoginTime = "";
			//最后登录时间
			if(enterpriseVo.getLastLoginTime() !=null){
				newLastLoginTime = df.format(enterpriseVo.getLastLoginTime());
			}
			rowData.add(newLastLoginTime);
			rowDataList.add(rowData);
		}
		return rowDataList;
	}

	/**
	 * 
	 * @param categoryStr
	 * @param category
	 * @return
	 * @since 2017年4月14日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public static String getCategory(Map<String, String> map, String categoryStr) {
		String cateStr = "";
		StringBuilder strBuffer = new StringBuilder();
		Map<String, String> newMap = map;
		if (StringUtils.isEmpty(categoryStr)) {
			return strBuffer.toString();
		}
		if (newMap == null) {
			newMap = new HashMap<>();
		}
		String[] arrStr = categoryStr.split(",");
		for (String str : arrStr) {
			if (StringUtils.isEmpty(str)) {
				continue;
			}
			String value = newMap.get(str);
			if (StringUtils.isEmpty(value)) {
				strBuffer.append(","+str);
			} else {
				strBuffer.append(","+value);
			}

		}
		if (!StringUtils.isEmpty(strBuffer)) {
			cateStr = strBuffer.toString();
			cateStr = cateStr.substring(1);
		}
		return cateStr;
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
	 * 业务类型转换map
	 * 
	 * @param list
	 * @return
	 * @since 2017年4月14日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public static  Map<String, String> categoryToMap(List<Category> list) {
		Map<String, String> map = new HashMap<>();
		if (!CollectionUtils.isNotEmpty(list)) {
			return null;
		}
		for (Category category : list) {
			if (!StringUtils.isEmpty(category.getCategoryId())) {
				map.put(category.getCategoryId(), category.getCategoryName());
			}
		}
		return map;
	}

	/**
	 * 账户激活后企业修改审核成功保存数据
	 * 
	 * @param JSONObject
	 * @return void
	 * @since 2017年4月13日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException
	 */
	public void editEntApplySave(Apply apply) {
		String userId = RequestHelper.getLoginUserId();// 当前申请修改的人
		if (null == apply) {
			return;
		}
		
		// 获取内容
		String applyContent = apply.getApplyContent();
		// 把jsonObject转化成对应的实体
		EnterpriseVo enterpriseVo = JSONObject.parseObject(applyContent, EnterpriseVo.class);
        if (null == enterpriseVo) {
        	return;
		}
		String email = "";
		Party partyData = personDao.findPersonById(apply.getApplyUserId());
		if (null != partyData && null != partyData.getPerson()
				&& !StringUtils.isEmpty(partyData.getPerson().getMail())) {
			email = partyData.getPerson().getMail();
		}
		Party p = personDao.findPersonById(apply.getApplyUserId());
		String cerName = "";
		String mail = "";
		if (null != p && null != p.getPerson() && !StringUtils.isEmpty(p.getPerson())) {
			cerName = p.getPerson().getLastNameLocal();
			mail = p.getPerson().getMail();
		}
		// 驳回
		if (apply.getStatus() == ApplyStatus.REJECT) {
			Party partys = new Party();
			partys.setId(apply.getApplyOrgId());
			// 修改partyGrop的状态
			updatePartyGroup(partys, ActiveStatus.REJECTED, null, null, userId, null, null, null, new Date(),null);
			String urlMail = operationUrl + "/enterprise.htm";

			// 驳回发送邮件
			commonMsg(MailInfoVo.TemplateId.ACTIVE_REJECT.toString(), mail, urlMail, enterpriseVo.getName(),
					apply.getReason(), cerName, null);

			return;
		}
		// 审核成功
		// 修改partyGroup状态为审核通过
		Party partyObj = new Party();
		partyObj.setId(enterpriseVo.getId());
		updatePartyGroup(partyObj, ActiveStatus.PARTY_VERIFIED, null, null, userId, null, null, null, new Date(),null);

		// 新增资质
		insertCertificate(enterpriseVo, userId, partyObj);
		// 新增企业
		// 根据企业id判断当前企业是否有生成过组织代码 ,一个企业只能插入一条数据
		// 如果没有组织机构代码则根据公司名称查询 用户名，组织机构代码，key
		createVipEnt(enterpriseVo);
		// 发送审核通过邮件
		commonMsg(MailInfoVo.TemplateId.ACTIVE_PASS.toString(), email, null, enterpriseVo.getName(), null, cerName,
				null);
		sendInnerMail("ACTIVE_PASS", null, enterpriseVo);
		
		//个人升级企业，企业认证成功后发代金券
		try{	
			//个人升级企业，企业认证成功后发代金券
			RoleTypeVo roleType = partyRoleDao.getRoleType(enterpriseVo.getEntUserId(),RoleTypeEnum.UPGRADE_CUST.toString());
			if(null != roleType){
				//先判断当前用户有没有领过代金券
			   CouponParty coupon = payClientBuilder.couponResource().getBybusinessTypeCoupon(enterpriseVo.getEntUserId(), UP_COMPANY);
               if(null == coupon){
				 payClientBuilder.couponResource().regOrUpSendCoupon(UP_COMPANY, enterpriseVo.getEntUserId(), authorizationUtil.getLoginAuthorization());
               }
             }else{
            	//先判断当前用户有没有领过代金券
				CouponParty coupon1 = payClientBuilder.couponResource().getBybusinessTypeCoupon(enterpriseVo.getEntUserId(), APPROVE_COMPANY);
					if(null == coupon1){
				     payClientBuilder.couponResource().regOrUpSendCoupon(APPROVE_COMPANY, enterpriseVo.getEntUserId(), authorizationUtil.getLoginAuthorization());
					}
				}
			
			
		}catch (Exception e) {
			logger.error("后台提交申请，审核回调地址异常:{},{}",e.getMessage(),e);
		}
	}

	/**
	 * 后台企业用户会员管理3证修改审核
	 * 
	 * @param apply
	 * @return void
	 * @since 2017年4月13日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException
	 */
	@Audit(action = "Enterprise Modifyqqq;;;'#apply.applyOrgId'qqq;;;'#apply.reason'", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public void editEntApply(@Param("apply") Apply apply) {
		if (null != apply) {
			String reason = apply.getReason();
			// 获取内容
			String applyContent = apply.getApplyContent();
			// 把获取的内容转化为json格式
			JSONObject json = JSON.parseObject(applyContent);
			// 把jsonObject转化成对应的实体
			EnterpriseVo enterpriseVo = JSONObject.parseObject(json.toString(), EnterpriseVo.class);
			if (null == enterpriseVo) {
				return;
			}
			try {
				String userId = RequestHelper.getLoginUserId();// 当前申请修改的人
				// 判断是企业还是个人 //如果userType为1为个人，否则为企业
				Integer userType = partyRoleDao.isPersonal(enterpriseVo.getEntUserId());
				String id;
				if (userType == 1) {
					// 先创建企业
					id = String.valueOf(IdGen.getInstance().nextId());
					enterpriseVo.setId(id);
					// 生成企业信息
					createEnt(enterpriseVo.getEntUserId(), enterpriseVo, 0);
					// 修改个人信息
					createPerson(enterpriseVo, enterpriseVo.getEntUserId());
					// 修改个人注册地址
					enterpriseVo.setProvince(enterpriseVo.getPersonProvince());
					enterpriseVo.setProvinceName(enterpriseVo.getPersonProvinceName());
					enterpriseVo.setCity(enterpriseVo.getPersonCity());
					enterpriseVo.setCityName(enterpriseVo.getPersonCityName());
					enterpriseVo.setCountry(enterpriseVo.getPersonCountry());
					enterpriseVo.setCountryName(enterpriseVo.getCountryName());
					enterpriseVo.setAddress(enterpriseVo.getPersonAddress());
					enterpriseVo.setContactUserQQ(enterpriseVo.getContactUserQQ());
					saveBaseInfo(enterpriseVo, enterpriseVo.getEntUserId());
					
					// 插入企业与账户的关联
					saveRelationShip(enterpriseVo.getEntUserId(), id, 1);
					apply.setApplyOrgId(id);//因个人用户没有企业id，所以需重新设置个
					apply.setReason(null);//个人升级企业，取消reason字段传递
				}else if(userType != 1 && "3".equals(enterpriseVo.getIsApply())){
					//从后台直接新增企业
					id = String.valueOf(IdGen.getInstance().nextId());
					addEntInfo(enterpriseVo,userId,id);
					apply.setApplyOrgId(id);
					apply.setReason(null);//个人升级企业，取消reason字段传递
				}else{
					// 修改企业信息
					Party party = new Party();
					party.setId(apply.getApplyOrgId());
					 if(!"0".equals(enterpriseVo.getIsApply())){
						// 修改partyGroup状态
						updatePartyGroup(party, ActiveStatus.WAIT_APPROVE, null, null, userId, null, null, new Date(),
								null,null);
					 }
					//因为在修改企业信息的时候为了兼容老数据把暂时把企业id设置为用户id,修改完又把企业id还原回来
					enterpriseVo.setId(enterpriseVo.getEntUserId());
					 //修改企业信息和个人信息
					updateEntOrPersonInfo(enterpriseVo,userId);
					enterpriseVo.setId(apply.getApplyOrgId());
					
				}
				
				//如果没有修改资质信息则不需要提交审核
				 if("0".equals(enterpriseVo.getIsApply())){
					 return;
				 }
				apply.setReason(null);
				// 调用workFlow服务
				logger.info("激活账户邮件参数：{}", new ObjectMapper().writeValueAsString(apply));
				workflowClientBuilder.applyClient().createApply(apply, authorizationUtil.getLoginAuthorization());
				 apply.setReason(reason);
				
				String urlMail = operationUrl + VERIFYURL;
				// 给认证部发送邮件
				sendCerMail(urlMail, enterpriseVo.getName(), "资质", "APPLY_ACTIVE");
			
				//个人升级企业邮箱验证发送邮件
				if (userType == 1) {
					
					SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
					String senddate = format.format(new Date());
					String sid = senddate + "---" + enterpriseVo.getMail() +"---"+enterpriseVo.getEntUserId();
					byte[] b = Base64.encodeBase64(sid.getBytes(UTF8), true);
					sid = new String(b, UTF8);
					String url = customerUrl+"/accountSafe.htm?action=fromMailCreate&sid="+sid;
					sendPersonUpEntMail("PERSON_UP_ENT",enterpriseVo.getMail(),enterpriseVo.getContactUserName(),url);
				}


			} catch (Exception e) {
				logger.error("后台企业会员管理修改审核调用workflow服务异常：{},{}", e.getMessage(), e);
			}

		}

	}
	/**
	 * 后台直接新增企业
	 * 
	 * @param enterpriseVo
	 * @param userId
	 * @param entId
	 * @return void
	 * @since 2017年9月19日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException
	 */
	public void addEntInfo(EnterpriseVo  enterpriseVo,String userId,String entId){
		//创建个人登录信息
	    String entUserId = addPersonInfo(enterpriseVo,userId); 
		
		enterpriseVo.setId(entId);
		// 生成企业信息
		createEnt(entUserId, enterpriseVo, 0);
		// 修改企业注册地址
		saveBaseInfo(enterpriseVo, entId);
		
		enterpriseVo.setProvince(enterpriseVo.getPersonProvince());
		enterpriseVo.setProvinceName(enterpriseVo.getPersonProvinceName());
		enterpriseVo.setCity(enterpriseVo.getPersonCity());
		enterpriseVo.setCityName(enterpriseVo.getPersonCityName());
		enterpriseVo.setCountry(enterpriseVo.getPersonCountry());
		enterpriseVo.setCountryName(enterpriseVo.getCountryName());
		enterpriseVo.setAddress(enterpriseVo.getPersonAddress());
		enterpriseVo.setContactUserQQ(enterpriseVo.getContactUserQQ());
		// 修改个人注册地址
		saveBaseInfo(enterpriseVo, entUserId);
		// 插入企业与账户的关联
		saveRelationShip(entUserId, entId, 1);
	}
	/**
	 * 保存账号信息
	 * @param userVo
	 * @return
	 * @since 2017年9月19日
	 * @author zr.helinmei@yikuyi.com
	 */
	private String addPersonInfo(EnterpriseVo  enterpriseVo,String userId) {
		//1.保存账号信息
		String id = String.valueOf(IdGen.getInstance().nextId());
		Party party = new Party();
		party.setId(id);
		party.setPartyType(PartyType.PERSON);
		party.setPartyStatus(PartyStatus.PARTY_DISABLED);
		party.setCreator(userId);
		party.setCreatedDate(new Date());
		party.setLastUpdateDate(new Date());
		party.setLastUpdateUser(userId);
		partyDao.insert(party);
        //PERSON 表生成记录 PARTY_ID 和  PARTY表一致。
		Person person = new Person();
		person.setLastNameLocal(enterpriseVo.getPersonName());
		person.setMail(enterpriseVo.getMail());
		person.setPersonalTitle(enterpriseVo.getPersonalTitle());
		person.setTel(enterpriseVo.getContactUserTel());
		person.setFixedTel(enterpriseVo.getFixedTel());
		person.setTelStatus("Y");
		person.setMailStatus("Y");
		person.setCreator(userId);
		person.setCreatedDate(new Date());
		person.setLastUpdateDate(new Date());
		person.setLastUpdateUser(userId);
		person.setPersonTypeStatus(PersonTypeStatus.COMMON);
		party.setPerson(person);
		personDao.insert(party);
		//c.生成登录数据USER_LOGIN  字段： ENABLED = ‘Y’ , PARTY_ID 和 PARTY表一致， USER_LOGIN_ID =  刚刚注册的手机号。
		UserLogin userLogin = new UserLogin();
		userLogin.setId(enterpriseVo.getMail());
		userLogin.setParty(party);
		userLogin.setEnabled("N");
		userLogin.setIsSystem("N");
		userLogin.setRequirePasswordChange("N");
		userLogin.setUserLoginMethod(UserLoginMethod.EMAIL.toString());
		userLogin.setCreator(userId);
		userLogin.setCreatedDate(new Date());
		userLogin.setLastUpdateUser(userId);
		userLogin.setLastUpdateDate(new Date());
		userLoginDao.insert(userLogin);
        
        //d.生成 PARTY_ROLE 数据  ROLE_TYPE_ID 为：REGISTER
		partyRoleDao.insert(id, RoleTypeEnum.ENTERPRISE_CUST.toString(),userId,new Date(),userId,new Date());
		return id;
	}
	public  void  updateEntOrPersonInfo(EnterpriseVo  enterpriseVo,String userId){
		//修改企业信息
		updateParty(enterpriseVo, 0);
		//修改个人信息
		Party party = new Party();
		party.setId(enterpriseVo.getEntUserId());// 设置个人用户id
		Person person = new Person();
		person.setLastNameLocal(enterpriseVo.getPersonName());
		person.setTel(enterpriseVo.getContactUserTel());
		person.setFixedTel(enterpriseVo.getFixedTel());
		person.setMail(enterpriseVo.getMail());
		person.setPersonalTitle(enterpriseVo.getPersonalTitle());
		person.setLastUpdateUser(userId);
		person.setLastUpdateDate(new Date());
		party.setPerson(person);
		personDao.editPerson(party);
		enterpriseVo.setProvince(enterpriseVo.getPersonProvince());
		enterpriseVo.setProvinceName(enterpriseVo.getPersonProvinceName());
		enterpriseVo.setCity(enterpriseVo.getPersonCity());
		enterpriseVo.setCityName(enterpriseVo.getPersonCityName());
		enterpriseVo.setCountry(enterpriseVo.getPersonCountry());
		enterpriseVo.setCountryName(enterpriseVo.getCountryName());
		enterpriseVo.setAddress(enterpriseVo.getPersonAddress());
		enterpriseVo.setContactUserQQ(enterpriseVo.getContactUserQQ());
		//修改地址
		saveBaseInfo(enterpriseVo, enterpriseVo.getEntUserId());
		
	}
	/**
	 * 
	 * @param party
	 * @param status
	 * @param accountStatus
	 * @param comments
	 * @param userId
	 * @param name
	 * @param logo
	 * @param applyDate
	 * @param approvedDate
	 * @param creditComments
	 * @since 2017年7月20日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public void updatePartyGroup(Party party, ActiveStatus status, AccountStatus accountStatus, String comments, String userId,
			String name, String logo, Date applyDate, Date approvedDate,String creditComments) {
		PartyGroup partyGroup = new PartyGroup();
		partyGroup.setActiveStatus(status);
		partyGroup.setAccountStatus(accountStatus);
		partyGroup.setLastUpdateUser(userId);
		partyGroup.setLastUpdateDate(new Date());
		partyGroup.setComments(comments);// 备注
		partyGroup.setGroupName(name);
		partyGroup.setLogoImageUrl(logo);
		partyGroup.setApplyDate(applyDate);
		partyGroup.setApprovedDate(approvedDate);
		partyGroup.setCreditComments(creditComments);
		party.setPartyGroup(partyGroup);
		partyGroupDao.updatePartyGroup(party);
	}

	/**
	 * 获取企业基本信息
	 * 
	 * @param partyId
	 * @return
	 * @since 2017年5月3日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public PartyGroup getEntBaseInfo(String partyId) {
		PartyGroup partyGroup = null;
		// 获取企业id
		Party party = partyDao.getPartyPersonDetail(partyId);
		String entId;
		if(null !=party && !StringUtil.isNullOrEmpty(party.getCorporationId())){
			entId = party.getCorporationId();
			Party enterprise = partyDao.getPartyDetail(entId, PartyType.CORPORATION);
			if (null != enterprise) {
				partyGroup = enterprise.getPartyGroup();
			}
		}
		return partyGroup;
	}

	/**
	 * 企业授权委托书审核接口
	 * 
	 * @param jsonObject
	 * @return void
	 * @since 2017年5月2日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException
	 */
	public void entAuthorize(Apply apply) {
		String userId = RequestHelper.getLoginUserId();
		if (null != apply) {
			// 获取内容
			String applyContent = apply.getApplyContent();
			// 把jsonObject转化成对应的实体
			EnterpriseVo enterpriseVo = JSONObject.parseObject(applyContent, EnterpriseVo.class);
			if (null == enterpriseVo) {
				return;
			}
			Party party;
			// 路径链接
			String urlMail = operationUrl + "/enterprise.htm";
			// 驳回
			if (apply.getStatus() == ApplyStatus.REJECT) {
				party = new Party();
				party.setId(enterpriseVo.getId());
				// 修改party_group状态
				updatePartyGroup(party, null, AccountStatus.ACCOUNT_REJECTED, null, userId, null, null, null, null,null);

				
				//更新子账号企业的状态 update by zr.aoxianbing@yikuyi.com 2017.07.24
				Party person = partyDao.getPartyDetail(enterpriseVo.getEntUserId(), PartyType.PERSON);
				if(null != person && !StringUtils.isEmpty(person.getCorporationId()) ){
					Party partyGroup = new Party();
					partyGroup.setId(person.getCorporationId());
					updatePartyGroup(partyGroup, null, AccountStatus.ACCOUNT_REJECTED, null, userId, null, null,
							new Date(), null,null);
				}
				

				// 获取申请的用户名
				Party p = personDao.findPersonById(apply.getApplyUserId());
				String userName = "";
				String mail = "";
				if (null != p && null != p.getPerson()) {
					userName = p.getPerson().getLastNameLocal();
					mail = p.getPerson().getMail();
				}
				commonMsg("LICENSE_REJECT", mail, urlMail, enterpriseVo.getName(), null, userName, null);
			} else {
				// 审核成功
				party = new Party();
				party.setId(enterpriseVo.getId());
				// 修改partyGroup状态
				updatePartyGroup(party, null, AccountStatus.ACCOUNT_VERIFIED, null, userId, null, null, null,
						new Date(),null);

				//通过选择的主账号id，更新自身企业的状态 update by zr.aoxianbing@yikuyi.com 2017.07.24
				Party pe = partyDao.getPartyDetail(enterpriseVo.getEntUserId(), PartyType.PERSON);
				if(null != pe && !StringUtils.isEmpty(pe.getCorporationId()) ){
					Party partyGroup = new Party();
					partyGroup.setId(pe.getCorporationId());
					updatePartyGroup(partyGroup, null, AccountStatus.ACCOUNT_VERIFIED, null, userId, null, null,
							new Date(), null,null);
				}
				// 新增资质
				insertCertificate(enterpriseVo, userId, party);

				if(null != pe && !StringUtils.isEmpty(pe.getCorporationId()) ){
					//update by zr.aoxianbing@yikuyi.com 2017.08.04
					updateMainAccount(userId, enterpriseVo, pe);
					// 插入关系表生成主账号
					saveRelationShip(enterpriseVo.getEntUserId(), pe.getCorporationId(), 0);
				}

				// 先删除角色表
				List<String> list = new ArrayList<>();
				list.add(RoleTypeEnum.MAIN_ROLE.toString());
				partyRoleDao.deletePartyRoleByType(enterpriseVo.getEntUserId(), list);
				// 插入角色表
				partyRoleDao.insert(enterpriseVo.getEntUserId(), RoleTypeEnum.MAIN_ROLE.toString(), userId, new Date(),
						userId, new Date());
				// 修改person表状态

				Party partyNew = new Party();
				Person person = new Person();
				partyNew.setId(enterpriseVo.getEntUserId());
				person.setLastUpdateDate(new Date());
				person.setLastUpdateUser(userId);
				person.setPersonTypeStatus(PersonTypeStatus.MAIN);// 生成主账号
				partyNew.setPerson(person);
				personDao.editPerson(partyNew);

				// 获取申请的用户名
				Party p = personDao.findPersonById(apply.getApplyUserId());
				String userName = "";
				String mail = "";
				if (null != p && null != p.getPerson() && !StringUtils.isEmpty(p.getPerson())) {
					userName = p.getPerson().getLastNameLocal();
					mail = p.getPerson().getMail();
				}
				// 发送邮件
				commonMsg("LICENSE_PASS", mail, urlMail, enterpriseVo.getName(), null, userName, null);
			}
		}
	}

	/**
	 * 更换主账号
	 * @param userId
	 * @param enterpriseVo
	 * @param pe
	 * @since 2017年8月4日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	private void updateMainAccount(String userId, EnterpriseVo enterpriseVo, Party pe) {
		//通过vipId查询企业
		PartyRelationship vipRelationship = PartyRelationship.build(PartyRelationshipTypeEnum.ENTERPRISE_CERTIFIED);
		vipRelationship.setPartyIdTo(enterpriseVo.getId());
		List<PartyRelationship> vipPrList = relationshipDao.getPartyRelationship(vipRelationship);
		
		if(!CollectionUtils.isNotEmpty(vipPrList)){
			return;
		}
	
		for(PartyRelationship relationship:vipPrList){
			//通过企业id查询之前的主账号信息
			PartyRelationship partyRelationship = PartyRelationship.build(PartyRelationshipTypeEnum.COS_PLAY);
			partyRelationship.setPartyIdTo(relationship.getPartyIdFrom());//通过企业id查询用户
			List<PartyRelationship> prList = relationshipDao.getPartyRelationship(partyRelationship);
			if(CollectionUtils.isNotEmpty(prList)){
				//失效老的主账号
				partyRelationship = prList.get(0);
				partyRelationship.setThruDate(new Date());
				partyRelationship.setStatusId(PartyRelationshipStatus.DISABLED);
				relationshipDao.updateRelationShip(partyRelationship);
				//失效老的账号与企业的关系
				PartyRelationship oldHireRelationShip = PartyRelationship.build(PartyRelationshipTypeEnum.EMPLOYMENT);
				oldHireRelationShip.setPartyIdFrom(partyRelationship.getPartyIdFrom());
				List<PartyRelationship> cancelOldHireRelationShipList = relationshipDao.getPartyRelationship(oldHireRelationShip);
				if(CollectionUtils.isNotEmpty(cancelOldHireRelationShipList)){
					for(PartyRelationship cancelOldHireRelationShip:cancelOldHireRelationShipList){
						cancelOldHireRelationShip.setThruDate(new Date());
						cancelOldHireRelationShip.setStatusId(PartyRelationshipStatus.DISABLED);
						relationshipDao.updateRelationShip(cancelOldHireRelationShip);
						
					}
				}
				//维护老的主账号与新主账号企业的雇佣关系
				PartyRelationship newHireRelationShip = PartyRelationship.build(PartyRelationshipTypeEnum.EMPLOYMENT);
				newHireRelationShip.setPartyIdFrom(partyRelationship.getPartyIdFrom());
				newHireRelationShip.setPartyIdTo(pe.getCorporationId());//新的主账号企业id
				newHireRelationShip.setFromDate(new Date());
				newHireRelationShip.setCreator(userId);
				newHireRelationShip.setCreatedDate(new Date());
				newHireRelationShip.setLastUpdateUser(userId);
				newHireRelationShip.setLastUpdateDate(new Date());
				relationshipDao.insert(newHireRelationShip);
				logger.info("结束维护老的主账号和企业的关系");
				
				//修改账户表中主账号状态为子账号
				Party partyNew = new Party();
				Person person = new Person();
				partyNew.setId(partyRelationship.getPartyIdFrom());
				person.setLastUpdateDate(new Date());
				person.setLastUpdateUser(userId);
				person.setPersonTypeStatus(PersonTypeStatus.SON);// 修改为子账号
				partyNew.setPerson(person);
				personDao.editPerson(partyNew);
				
				//删除原主账号的角色
				List<String> list = new ArrayList<>();
				list.add(RoleTypeEnum.MAIN_ROLE.toString());
				partyRoleDao.deletePartyRoleByType(partyRelationship.getPartyIdFrom(), list);	
				
				//维护老的主账号和新的主账号关系
				if(!partyRelationship.getPartyIdFrom().equals(enterpriseVo.getEntUserId())){
					PartyRelationship disableShip = PartyRelationship.build(PartyRelationshipTypeEnum.REPORTS_TO);
					disableShip.setPartyIdFrom(partyRelationship.getPartyIdFrom());
					disableShip.setPartyIdTo(enterpriseVo.getEntUserId());
					disableShip.setFromDate(new Date());
					disableShip.setCreator(userId);
					disableShip.setCreatedDate(new Date());
					disableShip.setLastUpdateUser(userId);
					disableShip.setLastUpdateDate(new Date());
					relationshipDao.insert(disableShip);
				
			
					logger.info("结束插入维护老的主账号和新的主账号关系");
					logger.info("开始维护老的主账号和企业的关系");
					//先失效掉原主账号中的子账号的关系,再插入
					PartyRelationship ship = PartyRelationship.build(PartyRelationshipTypeEnum.REPORTS_TO);
					ship.setPartyIdTo(partyRelationship.getPartyIdFrom());
					List<PartyRelationship> accountAndAccountSonList = relationshipDao.getPartyRelationship(ship);
					if(CollectionUtils.isNotEmpty(accountAndAccountSonList)){
						for(PartyRelationship accountSon :accountAndAccountSonList){
							logger.info("开始重新添加子账号{}",JSONObject.toJSON(accountSon).toString());
							//失效原子账号的关系
							accountSon.setThruDate(new Date());
							accountSon.setLastUpdateDate(new Date());
							accountSon.setStatusId(PartyRelationshipStatus.DISABLED);
							relationshipDao.updateRelationShip(accountSon);
						   if(!accountSon.getPartyIdFrom().equals(enterpriseVo.getEntUserId())){
								//重新添加子账号关系
								accountSon.setThruDate(null);
								accountSon.setPartyIdTo(enterpriseVo.getEntUserId());
								accountSon.setCreatedDate(new Date());
								accountSon.setFromDate(new Date());
								accountSon.setCreator(userId);
								relationshipDao.insert(accountSon);
								logger.info("结束重新添加子账号{}",JSONObject.toJSON(accountSon).toString());
							}
						}
					}
				}
				
			}
		
		}

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
	 * 生成关联关系
	 * 
	 * @param userId
	 * @param id(剛生成party的企業id)
	 * @param type  1.有普通的认证会员 2.vip会员 3.生成主账号
	 * @return void
	 * @since 2017年5月3日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException
	 */
	void saveRelationShip(String userId, String id, int type) {
		// 插入企业与注册用户的关联关系 主账号
		PartyRelationship relationShip;
		if (type == 1) {
			// 说明是申请企业
			relationShip = PartyRelationship.build(PartyRelationshipTypeEnum.EMPLOYMENT);
		} else if (type == 2 || type == 3) {
			// vip企业
			relationShip = PartyRelationship.build(PartyRelationshipTypeEnum.ENTERPRISE_CERTIFIED);
		} else {
			relationShip = PartyRelationship.build(PartyRelationshipTypeEnum.COS_PLAY);
		}
		relationShip.setPartyIdFrom(userId);// 当前的登录用户partyId
		relationShip.setPartyIdTo(id);// 企业partyId
		relationShip.setFromDate(new Date());
		if (type == 3) {
			relationShip.setThruDate(new Date());// 如果为3先失效调企业关系
		} else {
			relationShip.setThruDate(null);
		}

		relationShip.setCreator(userId);
		relationShip.setCreatedDate(new Date());
		relationShip.setLastUpdateUser(userId);
		relationShip.setLastUpdateDate(new Date());
		// 主账号和员工关系 子账号
		relationshipDao.insert(relationShip);
	}

	/**
	 * 企业授权委托书审核申请
	 * 
	 * @param apply
	 * @return void
	 * @since 2017年5月2日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException
	 */
	@Audit(action = "Sun Account Applyqqq;;;'#applyOrgId'qqq;;;'#reason'", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public void entApplyAuthorize(@Param("apply")Apply apply,@Param("applyOrgId")String applyOrgId,@Param("reason")String reason) {
		if(apply == null){
			return ;
		}
			// 主账号申请走审批流程
			// 获取内容
			String applyContent = apply.getApplyContent();
			// 把jsonObject转化成对应的实体
			EnterpriseVo enterpriseVo = JSONObject.parseObject(applyContent, EnterpriseVo.class);
			if (null != enterpriseVo) {
				try {
					String userId = RequestHelper.getLoginUserId();
					Party party = new Party();
					party.setId(enterpriseVo.getId());// 设置企业id
					// 修改partyGroup表设置为子账号状态为待审核状态
					updatePartyGroup(party, null, AccountStatus.ACCOUNT_WAIT_APPROVE, null, userId, null, null,
							new Date(), null,null);
					//更新子账号企业的状态 update by zr.aoxianbing@yikuyi.com 2017.07.24
					Party person = partyDao.getPartyDetail(enterpriseVo.getEntUserId(), PartyType.PERSON);
					if(null != person && !StringUtils.isEmpty(person.getCorporationId()) ){
						Party partyGroup = new Party();
						partyGroup.setId(person.getCorporationId());
						updatePartyGroup(partyGroup, null, AccountStatus.ACCOUNT_WAIT_APPROVE, null, userId, null, null,
								new Date(), null,null);
					}
					// 回调workFlow服务
					workflowClientBuilder.applyClient().createApply(apply, authorizationUtil.getLoginAuthorization());
					//给认证部发送邮件
					String urlMail = operationUrl + VERIFYURL;
					sendCerMail(urlMail, enterpriseVo.getName(), "子账号管理功能", "APPLY_LICENSE");

				} catch (Exception e) {
					logger.error("调用workflow服务异常：{}, {}", e.getMessage(), e);
				}
			}
	}

	/**
	 * 失效账号
	 * 
	 * @param id
	 * @param reason
	 * @return
	 * @since 2017年5月10日
	 * @author zr.helinmei@yikuyi.com
	 */
	public void invalidAccount(String id, String reason) {
		// 自己失效
		Party party = new Party();
		party.setId(id);
		// 修改状态为失效
		PartyGroup partyGroup = new PartyGroup();
		partyGroup.setActiveStatus(ActiveStatus.INVALID);
		partyGroup.setComments(reason);
		party.setPartyGroup(partyGroup);
		partyGroupDao.updatePartyGroup(party);
		//失效原始企业
		partyGroupDao.invalidAccount(id,reason,ActiveStatus.INVALID);
	}

	/**
	 * 查询失效的企业,并且发送邮件给认证员和客服
	 * 
	 * @since 2017年5月11日
	 * @author zr.shuzuo@yikuyi.com
	 */
	public void enterpriseDocumentsExpiredJob() {
		List<EnterpriseExpiredVo> list = partyDao.getEnterpriseDocumentsExpiredList();
		if (CollectionUtils.isEmpty(list)) {
			return;
		}
		List<Party> partyList = personDao.findDataByRole(Arrays.asList(RoleTypeEnum.CUST_CERT_SPECIALIST.toString(), RoleTypeEnum.CUSTOMER_SERVICE.toString()));
		if (CollectionUtils.isEmpty(partyList)) {
			logger.error("认证和客户没有维护人员,请注意!");
			return;
		}
		StringBuilder tableStr = new StringBuilder();
		for (EnterpriseExpiredVo temp : list) {
			tableStr.append("<tr>").append("<td class='no_br'>").append(temp.getPartyCode()).append(TD_END_STR)
					.append(TD_START_STR).append(temp.getName()).append(TD_END_STR).append(TD_START_STR)
					.append(org.apache.commons.lang.StringUtils.isEmpty(temp.getOrgLimit()) ? temp.getHkEffectiveDate()
							: temp.getOrgLimit())
					.append(TD_END_STR).append("</tr>");
		}
		for (Party tempVo : partyList) {
			if (null == tempVo.getPerson()
					|| org.apache.commons.lang.StringUtils.isEmpty(tempVo.getPerson().getMail())) {
				continue;
			}
			MailInfoVo mailInfoVo = new MailInfoVo();
			JSONObject object = new JSONObject();
			object.put("tableValue", tableStr.toString());// 公司名称
			object.put("taskUrl", operationUrl + "/enterprise.htm?action=certificationEnt");// 链接
			object.put("toMail", tempVo.getPerson().getMail());
			object.put("portalUrl", portalServerUrlPrefix);// 公司logoUrl
			mailInfoVo.setTemplateId("DOCUMENT_EXPIRED");
			mailInfoVo.setContent(object);
			mailInfoVo.setTo(tempVo.getPerson().getMail());
			mailInfoVo.setType(EMAIL);
			msgSender.sendMsg(sendMsgAndEmailTopicName, mailInfoVo, null);
		}
	}
	
	/**
	 * 获取企业基本信息
	 * 
	 * @param partyId
	 * @return
	 * @since 2017年5月3日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public void isReApplyCer(String partyId) throws BusinessException{
		// 获取企业id
		Party party = partyDao.getPartyPersonDetail(partyId);
		String entId;
		if(null !=party && !StringUtil.isNullOrEmpty(party.getCorporationId())){
			entId = party.getCorporationId();
			Party enterprise = partyDao.getPartyDetail(entId, PartyType.CORPORATION);
			if (null != enterprise) {
				PartyGroup	partyGroup = enterprise.getPartyGroup();
				if(null != partyGroup &&  ActiveStatus.WAIT_APPROVE ==partyGroup.getActiveStatus()){
					throw new BusinessException(BusiErrorCode.EXIST_ENT_AUTHENTICATION);
				}
			}
		}
	}

	/**
	 * 导出认证企业管理列表
	 * @param param
	 * @param response
	 * @since 2017年7月21日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public void exportEntCertification(EnterpriseParamVo vo, HttpServletResponse response) throws IOException{
		// 查询数据
		List<EnterpriseVo> list = partyDao.getEntCertificationList(vo);
		response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Content-Disposition", "attachment; filename="+ new String("认证企业信息列表".getBytes("gb2312"), "ISO8859-1") + ".xls");
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Expires", "0");
		this.exportEntCerExcelXls(list, response.getOutputStream());
		
	}

	/**
	 * 
	 * @param list
	 * @param os
	 * @since 2017年8月3日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	void exportEntCerExcelXls(List<EnterpriseVo> list, ServletOutputStream os) {
		ExportProcesser processer = null;
		try {
			processer = ExportFactory.getProcesser(ExportFactory.TYPE_XLS, os);
			// 标题
			processer.writeLine(SHEET1, EXPORT_TEMPLATE_CERTIFICATION.split(","));
			List<List<String>> rowDataList;
			rowDataList = this.entCerDataList(list);
			for (List<String> rowData : rowDataList) {
				processer.writeLine(SHEET1, rowData);
			}
			processer.output();
		} catch (Exception e) {
			logger.error("导出认证企业管理列表：{}", e);
		} finally {
			if (null != processer) {
				processer.close();
			}
		}
		
	}
	
	
	private List<List<String>> entCerDataList(List<EnterpriseVo> list) {
		List<List<String>> rowDataList = Lists.newArrayList();
		if (!CollectionUtils.isNotEmpty(list)) {
			return rowDataList;
		}
		List<String> rowData;
		List<Category> corCategoryList = this.getCategorys("CORPORATION_CATEGORY");
		Map<String, String> corMap = categoryToMap(corCategoryList);
		List<Category> industryCategoryList = this.getCategorys("INDUSTRY_CATEGORY");
		Map<String, String> industryMap = categoryToMap(industryCategoryList);
		for (EnterpriseVo enterpriseVo : list) {
			rowData = Lists.newArrayList();
			//YKY客户编码
			rowData.add(enterpriseVo.getPartyCode()==null?"":enterpriseVo.getPartyCode());
			// 公司名称
			rowData.add(enterpriseVo.getName()==null?"":enterpriseVo.getName());
			// 公司类型
			String corCategory = getCategory(corMap, enterpriseVo.getCorCategory());
			rowData.add(corCategory);
			// 行业
			String industryCategory = getCategory(industryMap, enterpriseVo.getIndustryCategory());
			if (!StringUtils.isEmpty(enterpriseVo.getOtherAttr())) {
				industryCategory = industryCategory + "(" + enterpriseVo.getOtherAttr() + ")";
			}
			rowData.add(industryCategory);
			
			//区域 
			rowData.add(enterpriseVo.getAddress()==null?"":enterpriseVo.getAddress());
			
			// 认证状态
			rowData.add(enterpriseVo.getActiveStatus()==null?"":PartyActiveStatus.enum2Desc(enterpriseVo.getActiveStatus().toString()));
			
			//子账号
			String childAccount = "";
			if(null != enterpriseVo.getAccountStatus() && AccountStatus.ACCOUNT_VERIFIED.equals(enterpriseVo.getAccountStatus())){
				childAccount = "子账号";
			}
			//账期
			String accPeSta = "";
			if(null != enterpriseVo.getAccountPeriodStatus() && AccountPeriodStatus.PERIOD_VERIFIED.equals(enterpriseVo.getAccountPeriodStatus())){
				accPeSta = "账期";
			}
			//开通权限
			String authority;
			if(childAccount=="" && accPeSta==""){
				authority="--";
			}else if(childAccount!="" && accPeSta!=""){
				authority="子账号、账期";
			}else{
				authority = childAccount+accPeSta;
			}
			rowData.add(authority);
			
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd mm:hh:ss");  
			// 审核时间
			rowData.add(enterpriseVo.getApplyDate()==null ?"":sdf.format(enterpriseVo.getApplyDate()));
			// 营业期限
			rowData.add(enterpriseVo.getOrgLimit());
			// 认证备注
			rowData.add(enterpriseVo.getCreditComments());
			rowDataList.add(rowData);

		}
		return rowDataList;
	}
	
	/**
	 * 客服认证修改公司名称
	 * @param id
	 * @param status
	 * @return
	 * @since 2017年8月9日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Audit(action = "EntUpdateName Modifyqqq;;;'#partyId'qqq;;;'#partyId'", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public void updateCompanyName(@Param("partyId")String partyId,String name) {
		Party party = new Party();
		party.setId(partyId);
		String userId = RequestHelper.getLoginUserId();
		updatePartyGroup(party,null, null, null,userId,name, null, null, null,null);
	}
	
}