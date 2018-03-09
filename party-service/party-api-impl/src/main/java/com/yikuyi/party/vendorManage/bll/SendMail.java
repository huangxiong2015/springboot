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
package com.yikuyi.party.vendorManage.bll;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yikuyi.message.mail.vo.MailInfoVo;
import com.yikuyi.party.credit.dao.PartyCreditDao;
import com.yikuyi.party.customer.dao.PersonDao;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.model.PartyRelationship.PartyRelationshipTypeEnum;
import com.yikuyi.party.role.model.RoleTypeEnum;
import com.yikuyi.party.vendor.vo.CheckStartOrLose;
import com.yikuyi.party.vendor.vo.CheckVendorInfoVo;
import com.yikuyi.party.vendor.vo.PartyProductLineVo;
import com.yikuyi.party.vendor.vo.Vendor;
import com.yikuyi.party.vendor.vo.Vendor.VendorApplyType;
import com.yikuyi.party.vendor.vo.VendorInfoVo;
import com.yikuyi.party.vendors.bll.VendorManager;
import com.yikuyi.workflow.Apply;
import com.ykyframework.exception.BusinessException;
import com.ykyframework.mqservice.sender.MsgSender;

@Service
@Transactional
public class SendMail {

	private static final Logger logger = LoggerFactory.getLogger(SendMail.class);

	@Autowired
	private PersonDao personDao;

	@Autowired
	private MsgSender msgSender;

	@Autowired
	private VendorManager vendorManager;

	@Autowired
	private PartyCreditDao partyCreditDao;

	@Value("${mqConsumeConfig.sendMsgAndEmail.topicName}")
	private String sendMsgAndEmailTopicName;

	private static final String VENDOR_NEW_PASS = "VENDOR_NEW_PASS"; // 建档通过
	private static final String VENDOR_NEW_NOT_PASS = "VENDOR_NEW_NOT_PASS"; // 建档不通过
	private static final String VENDOR_CHANGE_PASS = "VENDOR_CHANGE_PASS"; // 变更通过
	private static final String VENDOR_CHANGE_NOT_PASS = "VENDOR_CHANGE_NOT_PASS"; // 变更不通过
	private static final String VENDOR_START_PASS = "VENDOR_START_PASS"; // 启用通过
	private static final String VENDOR_START_NOT_PASS = "VENDOR_START_NOT_PASS"; // 启用不通过
	private static final String VENDOR_LOSE_PASS = "VENDOR_LOSE_PASS"; // 失效通过
	private static final String VENDOR_LOSE_NOT_PASS = "VENDOR_LOSE_NOT_PASS"; // 失效不通过
	private static final String VENDOR_COMMON_APPLY = "VENDOR_COMMON_APPLY"; // 申请

	// 发送邮件设置
	private static final String EMAIL = "EMAIL";
	private static final String ACCOUNT = "account"; // 用户的邮箱
	private static final String GROUPNAME = "groupName"; // 公司名称
	private static final String REASON = "reason"; // 原因
	// 项目前缀
	@Value("${operation.serverUrlPrefix}")
	private String operationUrl;

	private static final String SALESVENDOR = "/salesVendor.htm"; // 路径

	private static final String VENDOR = "/vendor.htm"; // 路径

	/**
	 * 建档通过
	 * 
	 * @param apply
	 * @since 2017年9月1日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	public void newPass(Apply apply) {
		try {
			MailInfoVo mailInfoVo = new MailInfoVo();
			mailInfoVo.setTemplateId(VENDOR_NEW_PASS);
			mailInfoVo.setType(EMAIL);

			String applyMail = "";
			// 申请人
			if (StringUtils.isNotBlank(apply.getApplyUserId())) {
				applyMail = getAppayMail(apply.getApplyUserId());
			}

			// 获取（申请人，负责人，询价员，报价员，认证经理角色）的ID
			Set<String> ids = getAppayIds(apply);
			if (CollectionUtils.isEmpty(ids)) {
				return;
			}
			for (String applyUserId : ids) {
				Party p = personDao.getPersonByUserId(applyUserId);
				if (null == p || null == p.getPerson() || StringUtils.isBlank(p.getPerson().getMail())) {
					continue;
				}
				JSONObject object = new JSONObject();
				object.put(ACCOUNT, p.getPerson().getMail());
				object.put(GROUPNAME, apply.getCompanyName());
				String lgTime = "";
				if (null != apply.getCreatedDate()) {
					SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 24小时制
					lgTime = sdformat.format(apply.getCreatedDate());
				}
				// 把获取的内容转化为json格式
				JSONObject json = JSON.parseObject(apply.getApplyContent());

				// 把jsonObject转化成对应的实体
				Vendor vendor = JSONObject.parseObject(json.toString(), Vendor.class);

				object.put("handleTime", lgTime);
				object.put("handleAccount", applyMail);
				object.put("handleDeclare", vendor.getDescribe());
				int munber = findRolle(apply.getApplyOrgId(), applyUserId);
				if (munber == 1) {
					object.put("url", operationUrl + SALESVENDOR);
				} else {
					object.put("url", operationUrl + VENDOR);
				}
				mailInfoVo.setContent(object);
				mailInfoVo.setTo(p.getPerson().getMail());
				msgSender.sendMsg(sendMsgAndEmailTopicName, mailInfoVo, null);

			}

		} catch (Exception e) {
			logger.error("建档通过发送邮件出错：{}", e);
		}
	}

	/**
	 * 建档不通过
	 * 
	 * @param apply
	 * @since 2017年9月1日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	public void newNotPass(Apply apply) {

		try {
			MailInfoVo mailInfoVo = new MailInfoVo();
			mailInfoVo.setTemplateId(VENDOR_NEW_NOT_PASS);
			mailInfoVo.setType(EMAIL);
			// 获取（申请人，负责人，询价员，报价员，认证经理角色）的ID
			Set<String> ids = getAppayIds(apply);
			if (CollectionUtils.isEmpty(ids)) {
				return;
			}
			for (String applyUserId : ids) {
				Party p = personDao.getPersonByUserId(applyUserId);
				if (null == p || null == p.getPerson() || StringUtils.isBlank(p.getPerson().getMail())) {
					continue;
				}
				JSONObject object = new JSONObject();
				object.put(ACCOUNT, p.getPerson().getMail());
				object.put(GROUPNAME, apply.getCompanyName());
				object.put(REASON, apply.getReason());
				int munber = findRolle(apply.getApplyOrgId(), applyUserId);
				if (munber == 1) {
					object.put("url", operationUrl + SALESVENDOR);
				} else {
					object.put("url", operationUrl + VENDOR);
				}
				mailInfoVo.setContent(object);
				mailInfoVo.setTo(p.getPerson().getMail());
				msgSender.sendMsg(sendMsgAndEmailTopicName, mailInfoVo, null);

			}
		} catch (Exception e) {
			logger.error("建档不通过发送邮件出错：{}", e);
		}
	}

	/**
	 * 变更通过
	 * 
	 * @param apply
	 * @since 2017年9月1日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	public void changePass(Apply apply) {

		try {
			MailInfoVo mailInfoVo = new MailInfoVo();
			mailInfoVo.setTemplateId(VENDOR_CHANGE_PASS);
			mailInfoVo.setType(EMAIL);
			String applyMail = "";
			// 申请人
			if (StringUtils.isNotBlank(apply.getApplyUserId())) {
				applyMail = getAppayMail(apply.getApplyUserId());
			}
			// 获取（申请人，负责人，询价员，报价员，认证经理角色）的ID
			Set<String> ids = getAppayIds(apply);
			if (CollectionUtils.isEmpty(ids)) {
				return;
			}
			for (String applyUserId : ids) {
				Party p = personDao.getPersonByUserId(applyUserId);
				if (null == p || null == p.getPerson() || StringUtils.isBlank(p.getPerson().getMail())) {
					continue;
				}
				JSONObject object = new JSONObject();
				object.put(ACCOUNT, p.getPerson().getMail());
				object.put(GROUPNAME, apply.getCompanyName());
				String lgTime = "";
				if (null != apply.getCreatedDate()) {
					SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 24小时制
					lgTime = sdformat.format(apply.getCreatedDate());
				}
				String reason = "";
				// 把获取的内容转化为json格式
				JSONObject json = JSON.parseObject(apply.getApplyContent());
				if (apply.getProcessId().equals(VendorApplyType.ORG_SUPPLIER_INFO_CHANGE_REVIEW.name())) {

					// 把jsonObject转化成对应的实体
					CheckVendorInfoVo checkVendorInfoVo = JSONObject.parseObject(json.toString(),
							CheckVendorInfoVo.class);
					reason = checkVendorInfoVo.getDescribe();
				} else if (apply.getProcessId().equals(VendorApplyType.ORG_SUPPLIER_PRODUCTLINE_CHANGE_REVIEW.name())) {
					// 把jsonObject转化成对应的实体
					PartyProductLineVo vo = JSONObject.parseObject(json.toString(), PartyProductLineVo.class);
					reason = vo.getDescribe();
				}

				object.put("handleTime", lgTime);
				object.put("handleAccount", applyMail);
				object.put("handleDeclare", reason);
				int munber = findRolle(apply.getApplyOrgId(), applyUserId);
				if (munber == 1) {
					object.put("url", operationUrl + SALESVENDOR);
				} else {
					object.put("url", operationUrl + VENDOR);
				}
				mailInfoVo.setContent(object);
				mailInfoVo.setTo(p.getPerson().getMail());
				msgSender.sendMsg(sendMsgAndEmailTopicName, mailInfoVo, null);
			}

		} catch (Exception e) {
			logger.error("变更通过发送邮件出错：{}", e);
		}
	}

	/**
	 * 变更不通过
	 * 
	 * @param apply
	 * @since 2017年9月1日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	public void changeNotPass(Apply apply) {
		try {
			MailInfoVo mailInfoVo = new MailInfoVo();
			mailInfoVo.setTemplateId(VENDOR_CHANGE_NOT_PASS);
			mailInfoVo.setType(EMAIL);
			// 获取（申请人，负责人，询价员，报价员，认证经理角色）的ID
			Set<String> ids = getAppayIds(apply);
			if (CollectionUtils.isEmpty(ids)) {
				return;
			}
			for (String applyUserId : ids) {
				Party p = personDao.getPersonByUserId(applyUserId);
				if (null == p || null == p.getPerson() || StringUtils.isBlank(p.getPerson().getMail())) {
					continue;
				}
				JSONObject object = new JSONObject();
				object.put(ACCOUNT, p.getPerson().getMail());
				object.put(GROUPNAME, apply.getCompanyName());
				object.put(REASON, apply.getReason());
				int munber = findRolle(apply.getApplyOrgId(), applyUserId);
				if (munber == 1) {
					object.put("url", operationUrl + SALESVENDOR);
				} else {
					object.put("url", operationUrl + VENDOR);
				}
				mailInfoVo.setContent(object);
				mailInfoVo.setTo(p.getPerson().getMail());
				msgSender.sendMsg(sendMsgAndEmailTopicName, mailInfoVo, null);
			}

		} catch (Exception e) {
			logger.error("变更不通过发送邮件出错：{}", e);
		}
	}

	/**
	 * 启动通过
	 * 
	 * @param apply
	 * @since 2017年9月1日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	public void startPass(Apply apply) {

		try {
			MailInfoVo mailInfoVo = new MailInfoVo();
			mailInfoVo.setTemplateId(VENDOR_START_PASS);
			mailInfoVo.setType(EMAIL);
			// 获取（申请人，负责人，询价员，报价员，认证经理角色）的ID
			Set<String> ids = getAppayIds(apply);
			if (CollectionUtils.isEmpty(ids)) {
				return;
			}
			for (String applyUserId : ids) {
				Party p = personDao.getPersonByUserId(applyUserId);
				if (null == p || StringUtils.isBlank(p.getPerson().getMail())) {
					continue;
				}
				JSONObject object = new JSONObject();
				object.put(ACCOUNT, p.getPerson().getMail());
				object.put(GROUPNAME, apply.getCompanyName());
				int munber = findRolle(apply.getApplyOrgId(), applyUserId);
				if (munber == 1) {
					object.put("url", operationUrl + SALESVENDOR);
				} else {
					object.put("url", operationUrl + VENDOR);
				}
				mailInfoVo.setContent(object);
				mailInfoVo.setTo(p.getPerson().getMail());
				msgSender.sendMsg(sendMsgAndEmailTopicName, mailInfoVo, null);
			}

		} catch (Exception e) {
			logger.error("启动通过发送邮件出错：{}", e);
		}
	}

	/**
	 * 启动不通过
	 * 
	 * @param apply
	 * @since 2017年9月1日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	public void startNotPass(Apply apply) {
		try {
			MailInfoVo mailInfoVo = new MailInfoVo();
			mailInfoVo.setTemplateId(VENDOR_START_NOT_PASS);
			mailInfoVo.setType(EMAIL);
			// 获取（申请人，负责人，询价员，报价员，认证经理角色）的ID
			Set<String> ids = getAppayIds(apply);
			if (CollectionUtils.isEmpty(ids)) {
				return;
			}
			for (String applyUserId : ids) {
				Party p = personDao.getPersonByUserId(applyUserId);
				if (null == p || StringUtils.isBlank(p.getPerson().getMail())) {
					continue;
				}
				JSONObject object = new JSONObject();
				object.put(ACCOUNT, p.getPerson().getMail());
				object.put(GROUPNAME, apply.getCompanyName());
				object.put(REASON, apply.getReason());
				int munber = findRolle(apply.getApplyOrgId(), applyUserId);
				if (munber == 1) {
					object.put("url", operationUrl + SALESVENDOR);
				} else {
					object.put("url", operationUrl + VENDOR);
				}
				mailInfoVo.setContent(object);
				mailInfoVo.setTo(p.getPerson().getMail());
				msgSender.sendMsg(sendMsgAndEmailTopicName, mailInfoVo, null);
			}
		} catch (Exception e) {
			logger.error("启动不通过发送邮件出错：{}", e);
		}
	}

	/**
	 * 失效通过
	 * 
	 * @param apply
	 * @since 2017年9月1日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	public void losePass(Apply apply) {

		try {
			MailInfoVo mailInfoVo = new MailInfoVo();
			mailInfoVo.setTemplateId(VENDOR_LOSE_PASS);
			mailInfoVo.setType(EMAIL);
			// 获取（申请人，负责人，询价员，报价员，认证经理角色）的ID
			Set<String> ids = getAppayIds(apply);
			if (CollectionUtils.isNotEmpty(ids)) {
				return;
			}
			for (String applyUserId : ids) {
				Party p = personDao.getPersonByUserId(applyUserId);
				if (null == p || StringUtils.isBlank(p.getPerson().getMail())) {
					continue;
				}
				JSONObject object = new JSONObject();
				object.put(ACCOUNT, p.getPerson().getMail());
				object.put(GROUPNAME, apply.getCompanyName());

				// 把获取的内容转化为json格式
				JSONObject json = JSON.parseObject(apply.getApplyContent());

				// 把jsonObject转化成对应的实体
				CheckStartOrLose checkStartOrLose = JSONObject.parseObject(json.toString(), CheckStartOrLose.class);

				object.put(REASON, checkStartOrLose.getDescribe());
				int munber = findRolle(apply.getApplyOrgId(), applyUserId);
				if (munber == 1) {
					object.put("url", operationUrl + SALESVENDOR);
				} else {
					object.put("url", operationUrl + VENDOR);
				}
				mailInfoVo.setContent(object);
				mailInfoVo.setTo(p.getPerson().getMail());
				msgSender.sendMsg(sendMsgAndEmailTopicName, mailInfoVo, null);
			}
		} catch (Exception e) {
			logger.error("实效通过发送邮件出错：{}", e);
		}
	}

	/**
	 * 失效不通过
	 * 
	 * @param apply
	 * @since 2017年9月1日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	public void loseNotPass(Apply apply) {

		try {
			MailInfoVo mailInfoVo = new MailInfoVo();
			mailInfoVo.setTemplateId(VENDOR_LOSE_NOT_PASS);
			mailInfoVo.setType(EMAIL);
			// 获取（申请人，负责人，询价员，报价员，认证经理角色）的ID
			Set<String> ids = getAppayIds(apply);
			if (CollectionUtils.isEmpty(ids)) {
				return;
			}
			for (String applyUserId : ids) {
				Party p = personDao.getPersonByUserId(applyUserId);
				if (null == p || StringUtils.isBlank(p.getPerson().getMail())) {
					continue;
				}
				JSONObject object = new JSONObject();
				object.put(ACCOUNT, p.getPerson().getMail());
				object.put(GROUPNAME, apply.getCompanyName());
				object.put(REASON, apply.getReason());
				int munber = findRolle(apply.getApplyOrgId(), applyUserId);
				if (munber == 1) {
					object.put("url", operationUrl + SALESVENDOR);
				} else {
					object.put("url", operationUrl + VENDOR);
				}
				mailInfoVo.setContent(object);
				mailInfoVo.setTo(p.getPerson().getMail());
				msgSender.sendMsg(sendMsgAndEmailTopicName, mailInfoVo, null);
			}
		} catch (Exception e) {
			logger.error("失效不通过发送邮件出错：{}", e);
		}
	}

	/**
	 * 审核申请，通用版
	 * 
	 * @param companyName
	 * @param roleTypeEnum
	 * @param vendorApplyType
	 * @return
	 * @since 2017年9月1日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	public void checkApply(String companyName, RoleTypeEnum roleTypeEnum, VendorApplyType vendorApplyType,
			String applyUserId) throws BusinessException {

		Party p = personDao.getPersonByUserId(applyUserId);
		String content = "";

		if (vendorApplyType.name().equals(VendorApplyType.ORG_SUPPLIER_ARCHIVES_REVIEW.name())) {
			content = "申请供应商建档【" + companyName + "】";
		} else if (vendorApplyType.name().equals(VendorApplyType.ORG_SUPPLIER_INFO_CHANGE_REVIEW.name())) {
			content = "申请基本信息变更【" + companyName + "】";
		} else if (vendorApplyType.name().equals(VendorApplyType.ORG_SUPPLIER_PRODUCTLINE_CHANGE_REVIEW.name())) {
			content = "申请产品线信息变更【" + companyName + "】";
		} else if (vendorApplyType.name().equals(VendorApplyType.ORG_SUPPLIER_ENABLED_REVIEW.name())) {
			content = "申请供应商启用【" + companyName + "】";
		} else if (vendorApplyType.name().equals(VendorApplyType.ORG_SUPPLIER_INVALID_REVIEW.name())) {
			content = "申请供应商失效【" + companyName + "】";
		}
		String contentMail = p.getPerson().getMail() + content;

		List<Party> partyList = personDao.findDataByRole(Arrays.asList(roleTypeEnum.toString()));
		if (CollectionUtils.isEmpty(partyList)) {
			logger.error("账期审核申请party数据查询角色为空");
			throw new BusinessException("当前角色没有用户", "当前角色没有用户");
		}

		for (int i = 0; i < partyList.size(); i++) {
			logger.error("测试建档案发送有邮件大小：{}", partyList.size());
			Party partyMail = partyList.get(i);
			if (null == partyMail || null == partyMail.getPerson()) {
				continue;
			}
			try {
				if (StringUtils.isNotBlank(partyMail.getPerson().getMail())) {
					logger.error("测试建档案发送有邮件111：{}", partyMail.getPerson().getMail() + "==" + partyMail.getId());
					MailInfoVo mailInfoVo = new MailInfoVo();
					mailInfoVo.setTemplateId(VENDOR_COMMON_APPLY);
					mailInfoVo.setType(EMAIL);
					JSONObject object = new JSONObject();
					object.put(ACCOUNT, partyMail.getPerson().getMail());
					object.put("content", contentMail);
					mailInfoVo.setContent(object);
					mailInfoVo.setTo(partyMail.getPerson().getMail());
					logger.error("测试建档案发送有邮件发送模板:{}", JSONObject.toJSON(mailInfoVo));
					msgSender.sendMsg(sendMsgAndEmailTopicName, mailInfoVo, null);
					logger.error("测试建档案发送有邮件发送成功");
				}
			} catch (Exception e) {
				logger.error("发送邮件出错：{}", e);
			}

		}
	}

	// 获取（申请人，负责人，询价员，报价员，认证经理角色）的ID
	public Set<String> getAppayIds(Apply apply) {

		Set<String> set = new HashSet<>();
		VendorInfoVo vendorInfoVo = new VendorInfoVo();
		vendorManager.getPartyRelationshipInfo(vendorInfoVo, apply.getApplyOrgId());
		// 负责人
		if (StringUtils.isNotBlank(vendorInfoVo.getPrincipalId())) {
			set.add(vendorInfoVo.getPrincipalId());
		}
		// 询价员
		if (CollectionUtils.isNotEmpty(vendorInfoVo.getEnquiryList())) {
			List<String> list = vendorInfoVo.getEnquiryList();
			list.stream().forEach(enquiryId -> {
				set.add(enquiryId);
			});

		}

		// 报价员
		if (CollectionUtils.isNotEmpty(vendorInfoVo.getOfferList())) {
			List<String> list = vendorInfoVo.getOfferList();
			list.stream().forEach(offerId -> {
				set.add(offerId);
			});
		}

		// 认证经理角色
		if ("CEO".equals(apply.getcRoleId())) {
			List<Party> partyList = personDao.findDataByRole(Arrays.asList(RoleTypeEnum.SUPPLIER_MANAGER.toString()));
			if (CollectionUtils.isNotEmpty(partyList)) {
				for (Party p : partyList) {
					if (StringUtils.isNotBlank(p.getId())) {
						set.add(p.getId());
					}
				}
			}
		}
		// 申请人
		if (StringUtils.isNotBlank(apply.getApplyUserId())) {
			set.add(apply.getApplyUserId());
		}

		return set;
	}

	// 获取邮箱
	public String getAppayMail(String userId) {
		String applyMail = "";
		// 申请人
		if (StringUtils.isNotBlank(userId)) {
			Party p = personDao.getPersonByUserId(userId);
			if (null != p) {
				applyMail = p.getPerson().getMail();
			}
		}
		return applyMail;
	}

	// 是负责人 询价员 报价员 返回1 否则返回0
	public int findRolle(String partyId, String userId) {
		// 负责人
		String principalId = partyCreditDao.getPartyIdFrom(partyId,
				PartyRelationshipTypeEnum.VENDOR_PRINCIPAL_REL.name());
		if (StringUtils.isNotEmpty(principalId) && principalId.equals(userId)) {
			return 1;
		}

		// 询价员
		String enquiryId = partyCreditDao.getPartyIdFrom(partyId, PartyRelationshipTypeEnum.VENDOR_ENQUIRY_REL.name());
		if (StringUtils.isNotEmpty(enquiryId) && enquiryId.equals(userId)) {
			return 1;
		}

		// 报价员
		String offerId = partyCreditDao.getPartyIdFrom(partyId, PartyRelationshipTypeEnum.VENDOR_OFFER_REL.name());
		if (StringUtils.isNotEmpty(offerId) && offerId.equals(userId)) {
			return 1;
		}
		return 0;
	}

}
