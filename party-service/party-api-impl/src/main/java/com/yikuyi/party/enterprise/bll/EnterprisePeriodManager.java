package com.yikuyi.party.enterprise.bll;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.jasig.inspektr.audit.annotation.Audit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.shade.io.netty.util.internal.StringUtil;
import com.framework.springboot.audit.Param;
import com.framework.springboot.utils.AuthorizationUtil;
import com.framewrok.springboot.web.RequestHelper;
import com.github.pagehelper.PageInfo;
import com.yikuyi.basedata.ShipmentClientBuilder;
import com.yikuyi.basedata.category.model.Category;
import com.yikuyi.message.mail.vo.MailInfoVo;
import com.yikuyi.party.contact.vo.EnterprisePeriodVo;
import com.yikuyi.party.contact.vo.EnterpriseVo;
import com.yikuyi.party.contact.vo.UserExtendVo;
import com.yikuyi.party.credit.dao.PartyCreditDao;
import com.yikuyi.party.credit.model.PartyAttachment;
import com.yikuyi.party.credit.model.PartyAttachment.AttachmentType;
import com.yikuyi.party.credit.model.PartyCredit;
import com.yikuyi.party.credit.vo.PartyCreditParamVo;
import com.yikuyi.party.credit.vo.PartyCreditVo;
import com.yikuyi.party.customer.dao.PersonDao;
import com.yikuyi.party.group.model.PartyGroup;
import com.yikuyi.party.group.model.PartyGroup.AccountPeriodStatus;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.model.PartyRelationship;
import com.yikuyi.party.model.PartyRelationship.PartyRelationshipTypeEnum;
import com.yikuyi.party.party.dao.PartyDao;
import com.yikuyi.party.party.dao.PartyRoleDao;
import com.yikuyi.party.partygroup.dao.PartyGroupDao;
import com.yikuyi.party.partygroup.dao.PartyRelationshipDao;
import com.yikuyi.party.role.model.RoleTypeEnum;
import com.yikuyi.workflow.Apply;
import com.yikuyi.workflow.Apply.ApplyStatus;
import com.yikuyi.workflow.WorkflowClientBuilder;
import com.yikuyi.workflow.vo.ApplyVo;
import com.yikuyi.workflow.vo.ApplyVo.ApplyVoType;
import com.ykyframework.model.IdGen;
import com.ykyframework.mqservice.sender.MsgSender;

@Service
@Transactional
public class EnterprisePeriodManager {
	private static final Logger logger = LoggerFactory.getLogger(EnterprisePeriodManager.class);

	@Autowired
	private MsgSender msgSender;

	@Autowired
	private PartyCreditDao partyCreditDao;

	@Value("${mqConsumeConfig.sendMsgAndEmail.topicName}")
	private String sendMsgAndEmailTopicName;

	@Autowired
	private WorkflowClientBuilder workflowClientBuilder;

	@Autowired
	private ShipmentClientBuilder shipmentClientBuilder;

	private static final String INNERMAIL = "INNERMAIL";// 站内信

	private static final String ACCOUNT_PERIOD_PASS = "ACCOUNT_PERIOD_PASS";// 账期通过
	private static final String ACCOUNT_PERIOD_REJECT = "ACCOUNT_PERIOD_REJECT";// 账期驳回

	private static final String ORG_ACCOUNT_PERIOD_REVIEW = "ORG_ACCOUNT_PERIOD_REVIEW";// 账期流程

	private static final String ACCOUNT_PERIOD_APPLY = "ACCOUNT_PERIOD_APPLY";// 账期申请

	@Autowired
	private AuthorizationUtil authorizationUtil;

	@Autowired
	private PersonDao personDao;

	@Autowired
	private PartyGroupDao partyGroupDao;

	@Autowired
	private PartyRelationshipDao partyRelationshipDao;

	// portal
	@Value("${portal.serverUrlPrefix}")
	private String portalServerUrlPrefix;

	@Autowired
	private PartyDao partyDao;

	// 项目前缀
	@Value("${operation.serverUrlPrefix}")
	private String operationUrl;

	@Value("${customer.serverUrlPrefix}")
	private String customerUrl;

	@Autowired
	private PartyRoleDao partyRoleDao;

	/**
	 * 账期申请
	 * 
	 * @param apply
	 * @return void
	 * @since 2017年7月20日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException
	 */
	@Audit(action = "Account Periodqqq;;;'#applyOrgId'qqq;;;'#reason'", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public void accountPeriodApply(Apply apply, @Param("applyOrgId") String applyOrgId,
			@Param("reason") String reason) {

		if (null == apply) {
			return;
		}
		String applyContent = apply.getApplyContent();
		// 把获取的内容转化为json格式
		JSONObject json = JSON.parseObject(applyContent);

		// 把jsonObject转化成对应的实体
		EnterprisePeriodVo periodVo = JSONObject.parseObject(json.toString(), EnterprisePeriodVo.class);
		if (null == periodVo) {
			return;
		}
		try {

			// 获取企业id
			String vipId = apply.getApplyOrgId();
			Party party = new Party();
			party.setId(vipId);
			String userId = RequestHelper.getLoginUserId();
			Party partyGroup = partyGroupDao.getPartyGroupByGroupId(vipId);
			// 如果当前企业已经开通过了账期，不修改状态
			if (null != partyGroup && null != partyGroup.getPartyGroup()
					&& null != partyGroup.getPartyGroup().getAccountPeriodStatus()
					&& AccountPeriodStatus.PERIOD_VERIFIED == partyGroup.getPartyGroup().getAccountPeriodStatus()) {
				// 数据不做处理
			} else {
				// 修改partyGroup表设置为待审核状态
				updatePartyGroup(party, null, userId, null, new Date(), null, AccountPeriodStatus.PERIOD_WAIT_APPROVE);
			}
			// 设置流程账期
			apply.setProcessId(ORG_ACCOUNT_PERIOD_REVIEW);
			// 回调函数提交审核
			workflowClientBuilder.applyClient().createApply(apply, authorizationUtil.getLoginAuthorization());
			// 给认证经理发送邮件
			List<Party> partyList = personDao.findDataByRole(Arrays.asList(RoleTypeEnum.CREDIT_MANAGER.toString()));
			if (CollectionUtils.isEmpty(partyList)) {
				logger.error("账期审核申请party数据查询角色为空:{}", applyOrgId);
				return;
			}
			// 发送邮件
			sendPeriodMail(partyList, periodVo.getName(), ACCOUNT_PERIOD_APPLY, periodVo);

		} catch (Exception e) {
			logger.error("调用workflow服务异常：{}", e.getMessage(), e);
		}

	}

	void sendPeriodMail(List<Party> mailList, String companyName, String template, EnterprisePeriodVo periodVo) {
		String linkUrl = operationUrl + "/credit.htm";
		for (int i = 0; i < mailList.size(); i++) {
			Party partyMail = mailList.get(i);
			if (null != partyMail && null != partyMail.getPerson()) {
				// 获取收件人的名字
				String userName = partyMail.getPerson().getLastNameLocal();
				// 要发送的邮件
				String email = partyMail.getPerson().getMail();
				logger.info("循环发送账期申请邮箱{},{}", JSONObject.toJSON(periodVo).toString(), email);
				priodMail(template, email, userName, periodVo, companyName, null, linkUrl);
			}
		}

	}

	public void updatePartyGroup(Party party, String comments, String userId, String name, Date applyDate,
			Date approvedDate, AccountPeriodStatus accountPeriodStatus) {
		PartyGroup partyGroup = new PartyGroup();
		partyGroup.setLastUpdateUser(userId);
		partyGroup.setLastUpdateDate(new Date());
		partyGroup.setComments(comments);// 备注
		partyGroup.setGroupName(name);
		partyGroup.setApplyDate(applyDate);
		partyGroup.setApprovedDate(approvedDate);
		partyGroup.setAccountPeriodStatus(accountPeriodStatus);
		party.setPartyGroup(partyGroup);
		partyGroupDao.updatePartyGroup(party);
	}

	/**
	 * 账期申请通过或者驳回
	 * 
	 * @param apply
	 * @return void
	 * @since 2017年7月20日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException
	 */
	public void accountPeriodAudit(Apply apply) {
		if (null == apply) {
			return;
		}
		try {
			String userId = RequestHelper.getLoginUserId();
			Party party = new Party();
			party.setId(apply.getApplyOrgId());
			String applyContent = apply.getApplyContent();
			// 把jsonObject转化成对应的实体
			EnterprisePeriodVo periodVo = JSONObject.parseObject(applyContent, EnterprisePeriodVo.class);
			if (null == periodVo) {
				return;
			}
			// 获取用户名和邮箱
			Party partyApplyPerson = personDao.getPersonByUserId(apply.getApplyUserId());
			String applyName = "";
			String applyEmail = "";
			if (null != partyApplyPerson && null != partyApplyPerson.getPerson()) {
				applyName = partyApplyPerson.getPerson().getLastNameLocal();
				applyEmail = partyApplyPerson.getPerson().getMail();
			}
			String linkUrl = operationUrl + "/certificationEntKf.htm";
			// 驳回
			if (ApplyStatus.REJECT == apply.getStatus()) {
				// 修改partyGroup表设置为驳回状态
				updatePartyGroup(party, null, userId, null, null, new Date(), AccountPeriodStatus.PERIOD_REJECTED);
				// 发送驳回邮件
				priodMail(ACCOUNT_PERIOD_REJECT, applyEmail, applyName, periodVo, periodVo.getName(), apply.getReason(),
						linkUrl);
				return;
			}
			// 通过
			if (ApplyStatus.APPROVED == apply.getStatus()) {
				// 修改vip账号状态
				updatePartyGroup(party, null, userId, null, null, new Date(), AccountPeriodStatus.PERIOD_VERIFIED);
				// 账期数据插入
				saveCredit(userId, periodVo);
				// 修改所有相关账号状态
				updateAccountStatus(apply, userId, periodVo);
				// 给申请人发送邮件
				try {
					priodMail(ACCOUNT_PERIOD_PASS, applyEmail, applyName, periodVo, null, null, linkUrl);
				} catch (Exception e) {
					logger.error("账期给申请人发送邮件失败:{}", e);
				}
			}
		} catch (Exception e) {
			logger.error("workflow回调异常:{},{}" + JSONObject.toJSON(apply).toString(), e);
		}
	}

	private void updateAccountStatus(Apply apply, String userId, EnterprisePeriodVo periodVo) {
		// 查询所有子账号
		List<UserExtendVo> list = partyDao.getEnterpriseAccountsList(apply.getApplyOrgId());
		if (CollectionUtils.isNotEmpty(list)) {
			for (UserExtendVo userVo : list) {
				// 根据用户id找企业id
				Party newParty = partyDao.getPartyPersonDetail(userVo.getId());
				if (null != newParty && !StringUtil.isNullOrEmpty(newParty.getCorporationId())) {
					Party newPartys = new Party();
					newPartys.setId(newParty.getCorporationId());
					// 设置账期状态都为通过
					updatePartyGroup(newPartys, null, userId, null, null, new Date(),
							AccountPeriodStatus.PERIOD_VERIFIED);
				}
				// 先删除用户账期角色
				List<String> listRole = new ArrayList<>();
				listRole.add(RoleTypeEnum.ACCOUNT_CUST.toString());
				partyRoleDao.deletePartyRoleByType(userVo.getId(), listRole);
				// 插入角色
				partyRoleDao.insert(userVo.getId(), RoleTypeEnum.ACCOUNT_CUST.toString(), userId, new Date(), userId,
						new Date());
				// 给所有子账号发送邮件
				try {
					// 发送邮件到账期订单页面
					String orderMailUrl = customerUrl + "/accountperiod.htm";
					priodMail(ACCOUNT_PERIOD_PASS, userVo.getMail(), userVo.getName(), periodVo, null, null,
							orderMailUrl);
					String innerUrl = customerUrl + "/accountperiod.htm";
					sendInnerMail(ACCOUNT_PERIOD_PASS, innerUrl, periodVo, userVo.getId());
				} catch (Exception e) {
					logger.error("账期审核调用邮箱接口失败:{}", e);
				}
			}
		}
	}

	public void sendNodeCreitMail(String partyId) {
		try {
			// 查看是否所有节点审核通过
			ApplyVo applyVo = new ApplyVo();
			applyVo.setPage(1);
			applyVo.setPageSize(1);
			applyVo.setApplyOrgId(partyId);
			PageInfo<Apply> page = workflowClientBuilder.applyClient().queryApplyByEntity(ApplyVoType.PROCESS,
					ORG_ACCOUNT_PERIOD_REVIEW, applyVo, authorizationUtil.getLoginAuthorization());
			if (null == page || CollectionUtils.isEmpty(page.getList())) {
				return;
			}
			List<Apply> listPage = page.getList();
			Apply apply = listPage.get(0);
			if (null != apply && StringUtils.isNotBlank(apply.getStatus().name())
					&& "WAIT_APPROVE".equals(apply.getStatus().name())) {
				String applyContent = apply.getApplyContent();
				// 把获取的内容转化为json格式
				JSONObject json = JSON.parseObject(applyContent);

				// 把jsonObject转化成对应的实体
				EnterprisePeriodVo periodVo = JSONObject.parseObject(json.toString(), EnterprisePeriodVo.class);
				if (null == periodVo) {
					return;
				}

				// 给当前节点的角色发送邮件
				List<Party> partyList = personDao.findDataByRole(Arrays.asList(apply.getcRoleId()));
				if (CollectionUtils.isEmpty(partyList)) {
					logger.error("账期审核申请party数据查询角色为空{}", apply.getcRoleId());
					return;
				}
				// 发送邮件
				sendPeriodMail(partyList, apply.getCompanyName(), ACCOUNT_PERIOD_APPLY, periodVo);
			}

		} catch (Exception e) {
			logger.error("账期审核申请发送节点角色邮件失败:{}", e);
		}
	}

	private void saveCredit(String userId, EnterprisePeriodVo periodVo) {
		PartyCredit partyCredit = new PartyCredit();
		partyCredit.setPartyId(periodVo.getPartyId());
		partyCredit.setCheckCycle(periodVo.getCheckCycle());
		partyCredit.setCheckDate(periodVo.getCheckDate());
		partyCredit.setCommon(periodVo.getCommon());
		partyCredit.setCurrency(periodVo.getCurrency());
		partyCredit.setCreditDeadline(periodVo.getCreditDeadline());
		partyCredit.setRealtimeCreditQuota(periodVo.getCreditQuota());
		partyCredit.setCreditQuota(periodVo.getCreditQuota());
		partyCredit.setPayDate(periodVo.getPayDate());
		partyCredit.setApplyUser(periodVo.getApplyUser());
		partyCredit.setApplyMail(periodVo.getApplyMail());
		partyCredit.setApplyInformation(periodVo.getApplyInformation());
		partyCredit.setFromDate(new Date());
		// 如果存在先失效再新增
		PartyCredit creditVo = partyCreditDao.getPartyCredit(periodVo.getPartyId(), null);
		if (null != creditVo && !StringUtil.isNullOrEmpty(creditVo.getPartyId())) {
			// 失效数据
			PartyCredit partyCredit1 = new PartyCredit();
			partyCredit1.setPartyCreditId(creditVo.getPartyCreditId());
			partyCredit1.setLastUpdateUser(userId);
			partyCredit1.setLastUpdateDate(new Date());
			partyCredit1.setThruDate(new Date());
			partyCreditDao.update(partyCredit1);
		}
		// 调用接口把数据插入到正式表中
		String id = String.valueOf(IdGen.getInstance().nextId());
		partyCredit.setPartyCreditId(id);
		partyCredit.setCreator(userId);
		partyCredit.setCreatedDate(new Date());
		partyCredit.setLastUpdateDate(new Date());
		partyCredit.setLastUpdateUser(userId);
		logger.info("审核通过插入账期数据:{}", JSONObject.toJSON(partyCredit).toString());
		partyCreditDao.insert(partyCredit);
		// 插入附件
		if (null != periodVo.getCreditAttachmentList()) {
			List<PartyAttachment> attachmentList = periodVo.getCreditAttachmentList();
			for (PartyAttachment attachment : attachmentList) {
				String attachmentId = String.valueOf(IdGen.getInstance().nextId());
				partyCreditDao.insertAttrchment(attachmentId, attachment.getAttachmentName(),
						attachment.getAttachmentUrl(), userId, AttachmentType.CREDIT_APPLY.toString(), id);
			}
		}

	}

	public void priodMail(String templateId, String toMail, String userName, EnterprisePeriodVo periodVo,
			String companyName, String reason, String linkUrl) {
		MailInfoVo mailInfoVo = new MailInfoVo();
		mailInfoVo.setTemplateId(templateId);
		mailInfoVo.setType(MailInfoVo.Type.EMAIL.toString());
		mailInfoVo.setTo(toMail);
		JSONObject object = new JSONObject();
		object.put("userName", userName);// 用户名
		object.put("quota", periodVo.getCreditQuota());// 额度
		object.put("term", periodVo.getCreditDeadline());// 期限
		object.put("reconciliationDate", periodVo.getCheckDate());// 对账日期
		object.put("reconciliationCycle", periodVo.getCheckCycle());// 对账周期
		object.put("paymentDate", periodVo.getPayDate());// 付款日期
		object.put("companyName", companyName);// 公司名称
		object.put("reason", reason);// 原因
		object.put("linkUrl", linkUrl);// 链接地址
		object.put("currency", periodVo.getCurrency().toString());
		mailInfoVo.setContent(object);
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
	public void sendInnerMail(String emplateId, String url, EnterprisePeriodVo vo, String toId) {
		try {
			MailInfoVo mailInfoVo = new MailInfoVo();
			mailInfoVo.setTemplateId(emplateId);
			mailInfoVo.setType(INNERMAIL);
			mailInfoVo.setFrom(vo.getPartyId());
			JSONObject content = new JSONObject();
			content.put("url", url);
			mailInfoVo.setTo(toId);
			mailInfoVo.setContent(content);
			msgSender.sendMsg(sendMsgAndEmailTopicName, mailInfoVo, null);
		} catch (Exception e) {
			logger.error("账期站内信发送失败：{}", e);
		}
	}

	/**
	 * 根据企业Id查询用户的账期信息(住公司)
	 * 
	 * @param corporationId
	 * @return
	 * @since 2017年8月3日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public PartyCreditVo getPartyCreditVoByCorporationId(String corporationId) {
		PartyCreditVo partyCreditVo = null;
		try {
			Party party = partyDao.getPartyDetail(corporationId, null);
			PartyCredit partyCredit = partyCreditDao.getPartyCredit(corporationId, null);
			partyCreditVo = new PartyCreditVo();
			if (partyCredit == null) {
				return null;
			}
			BeanUtils.copyProperties(partyCredit,partyCreditVo);
			if (party != null && party.getPartyGroup() != null) {
				partyCreditVo.setGroupName(party.getPartyGroup().getGroupName());
				if (party.getPartyGroup().getAccountPeriodStatus() != null) {
					partyCreditVo.setAccountPeriodStatus(party.getPartyGroup().getAccountPeriodStatus());
				}
			}

			if (party != null) {
				partyCreditVo.setPartyCode(party.getPartyCode());
				// 查询公司类型与所属行业
				EnterpriseVo enterpriseVo = partyDao.getEnterpriseVoInfo(party.getId());
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
			}

		} catch (Exception e) {
			logger.error("根据企业id查询用户的账期信息:{}", e);
		}
		return partyCreditVo;
	}

	/**
	 * 账期订单列表
	 * 
	 * @param param
	 * @param rowBounds
	 * @return
	 * @since 2017年8月9日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public PageInfo<PartyCreditVo> getPartyCreditVoList(PartyCreditParamVo param, RowBounds rowBounds) {
		PageInfo<PartyCreditVo> pageInfo = new PageInfo<>(partyCreditDao.getPartyCreditVoList(param, rowBounds));

		if (CollectionUtils.isEmpty(pageInfo.getList())) {
			return null;
		}
		for (PartyCreditVo partyCreditVo : pageInfo.getList()) {
			// 查询企业Id
			String enterpriseId = "";
			if (partyCreditVo.getPartyId() != null) {
				PartyRelationship partyRelationship = PartyRelationship
						.build(PartyRelationshipTypeEnum.ENTERPRISE_CERTIFIED);
				partyRelationship.setPartyIdTo(partyCreditVo.getPartyId());
				List<PartyRelationship> relationshipList = partyRelationshipDao.getPartyRelationship(partyRelationship);
				if (relationshipList != null && CollectionUtils.isNotEmpty(relationshipList)) {
					for (PartyRelationship ship : relationshipList) {
						enterpriseId = ship.getPartyIdFrom();
					}
				}
			}
			partyCreditVo.setEnterpriseId(enterpriseId);
		}

		pageInfo.setList(pageInfo.getList());
		return pageInfo;
	}

	/**
	 * 冻结账期
	 * 
	 * @param partyId
	 * @param status
	 * @return
	 * @since 2017年8月9日
	 * @author zr.helinmei@yikuyi.com
	 */
	public void updateCreditStatus(String partyId, String status) {
		// 解冻PERIOD_ENABLED，冻结PERIOD_DISABLED
		String userId = RequestHelper.getLoginUserId();
		Party party = new Party();
		party.setId(partyId);
		AccountPeriodStatus newStatus;
		// 如果为解冻，则需要把账期状态改为账期审核通过状态
		if ("PERIOD_ENABLED".equals(status)) {
			newStatus = AccountPeriodStatus.PERIOD_VERIFIED;
		} else {
			newStatus = AccountPeriodStatus.PERIOD_DISABLED;
		}

		// 修改自身为冻结
		updatePartyGroup(party, null, userId, null, null, null, newStatus);

		// 所有相关子账号平行账号的账期修改为冻结或解冻状态
		List<UserExtendVo> list = partyDao.getEnterpriseAccountsList(partyId);
		if (!CollectionUtils.isNotEmpty(list)) {
			return;
		}
		for (UserExtendVo userVo : list) {
			// 根据用户id找企业id
			Party newParty = partyDao.getPartyPersonDetail(userVo.getId());
			if (null != newParty && !StringUtil.isNullOrEmpty(newParty.getCorporationId())) {
				Party newPartys = new Party();
				newPartys.setId(newParty.getCorporationId());
				// 设置账期状态都为冻结
				updatePartyGroup(newPartys, null, userId, null, null, null, newStatus);
			}
		}
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
	 * 根据企业id查询partyIds
	 * 
	 * @param corporationId
	 * @return
	 * @since 2017年8月21日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public List<String> getPartyIdList(String corporationId) {
		List<String> list = new ArrayList<>();
		PartyRelationship partyRelationship = PartyRelationship.build(PartyRelationshipTypeEnum.ENTERPRISE_CERTIFIED);
		partyRelationship.setPartyIdTo(corporationId);
		List<PartyRelationship> relationshipList = partyRelationshipDao.getPartyRelationship(partyRelationship);
		if (relationshipList != null && CollectionUtils.isNotEmpty(relationshipList)) {
			for (PartyRelationship ship : relationshipList) {
				list.add(ship.getPartyIdFrom());
			}
		}
		return list;
	}

}
