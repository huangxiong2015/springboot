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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.jasig.inspektr.audit.annotation.Audit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framework.springboot.audit.Param;
import com.framework.springboot.utils.AuthorizationUtil;
import com.framewrok.springboot.web.RequestHelper;
import com.github.pagehelper.PageInfo;
import com.ictrade.tools.export.ExportFactory;
import com.ictrade.tools.export.ExportProcesser;
import com.ictrade.tools.export.ExportProcesserXls;
import com.yikuyi.basedata.ShipmentClientBuilder;
import com.yikuyi.basedata.category.model.Category;
import com.yikuyi.brand.model.ProductBrand;
import com.yikuyi.category.model.ProductCategory.ProductCategoryLevel;
import com.yikuyi.category.vo.ProductCategoryChild;
import com.yikuyi.category.vo.ProductCategoryParent;
import com.yikuyi.message.MessageClientBuilder;
import com.yikuyi.message.task.AsyncTaskInfo;
import com.yikuyi.party.acl.api.impl.ACLResource;
import com.yikuyi.party.common.utils.BusiErrorCode;
import com.yikuyi.party.common.utils.ImageToAliyunUtils;
import com.yikuyi.party.contact.vo.User;
import com.yikuyi.party.credit.dao.PartyCreditDao;
import com.yikuyi.party.credit.model.PartyAttachment;
import com.yikuyi.party.credit.model.PartyAttachment.AttachmentType;
import com.yikuyi.party.credit.model.PartyCredit;
import com.yikuyi.party.customer.dao.PersonDao;
import com.yikuyi.party.facility.dao.FacilityDao;
import com.yikuyi.party.facility.model.Facility;
import com.yikuyi.party.group.model.PartyGroup;
import com.yikuyi.party.group.model.PartyGroup.AccountStatus;
import com.yikuyi.party.group.model.PartyGroup.ActiveStatus;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.model.Party.PartyStatus;
import com.yikuyi.party.model.Party.PartyType;
import com.yikuyi.party.model.PartyAttribute;
import com.yikuyi.party.model.PartyRelationship;
import com.yikuyi.party.model.PartyRelationship.PartyRelationshipStatus;
import com.yikuyi.party.model.PartyRelationship.PartyRelationshipTypeEnum;
import com.yikuyi.party.party.dao.PartyAttributeDao;
import com.yikuyi.party.party.dao.PartyDao;
import com.yikuyi.party.party.dao.PartyRoleDao;
import com.yikuyi.party.partySupplier.dao.PartySupplierDao;
import com.yikuyi.party.partygroup.dao.PartyGroupDao;
import com.yikuyi.party.role.model.RoleTypeEnum;
import com.yikuyi.party.vendor.dao.PartyBankAccountDao;
import com.yikuyi.party.vendor.dao.PartyProductLineDao;
import com.yikuyi.party.vendor.dao.PartySupplierAliasDao;
import com.yikuyi.party.vendor.dao.VendorDao;
import com.yikuyi.party.vendor.vo.CheckStartOrLose;
import com.yikuyi.party.vendor.vo.CheckStartOrLose.StartOrLose;
import com.yikuyi.party.vendor.vo.CheckVendorInfoVo;
import com.yikuyi.party.vendor.vo.ContactPersonInfo;
import com.yikuyi.party.vendor.vo.PartyBankAccount;
import com.yikuyi.party.vendor.vo.PartyProductLine;
import com.yikuyi.party.vendor.vo.PartyProductLine.Select;
import com.yikuyi.party.vendor.vo.PartyProductLine.Status;
import com.yikuyi.party.vendor.vo.PartyProductLineJS;
import com.yikuyi.party.vendor.vo.PartyProductLineRequest;
import com.yikuyi.party.vendor.vo.PartyProductLineVo;
import com.yikuyi.party.vendor.vo.PartyProductLineVo.Type;
import com.yikuyi.party.vendor.vo.PartySupplier;
import com.yikuyi.party.vendor.vo.PartySupplierAlias;
import com.yikuyi.party.vendor.vo.Vendor;
import com.yikuyi.party.vendor.vo.Vendor.SupplierStatus;
import com.yikuyi.party.vendor.vo.Vendor.VendorApplyType;
import com.yikuyi.party.vendor.vo.VendorCreditVo;
import com.yikuyi.party.vendor.vo.VendorInfoVo;
import com.yikuyi.party.vendor.vo.VendorQueryVo;
import com.yikuyi.party.vendor.vo.VendorResponVo;
import com.yikuyi.party.vendor.vo.VendorSaleInfoVo;
import com.yikuyi.party.vendorManage.api.impl.UpFileManager;
import com.yikuyi.party.vendors.bll.VendorsManager;
import com.yikuyi.party.vo.DeptVo;
import com.yikuyi.product.ProductClientBuilder;
import com.yikuyi.workflow.Apply;
import com.yikuyi.workflow.Apply.ApplyStatus;
import com.yikuyi.workflow.WorkflowClientBuilder;
import com.yikuyi.workflow.vo.ApplyExtendsVo;
import com.yikuyi.workflow.vo.ApplyVo;
import com.yikuyi.workflow.vo.ApplyVo.ApplyVoType;
import com.ykyframework.exception.BusinessException;
import com.ykyframework.exception.SystemException;
import com.ykyframework.model.IdGen;
import com.ykyframework.mqservice.sender.MsgSender;
import com.ykyframework.oss.AliyunOSSAccount;
import com.ykyframework.oss.AliyunOSSFileUploadSetting;
import com.ykyframework.oss.AliyunOSSFileUploadType;

import net.sf.json.JSONObject;

@Service
@Transactional
public class VendorManage2 {

	private static final String VENDOR_INFO_LEGALPERSON = "VENDOR_INFO_LEGALPERSON"; // 公司法人
	private static final String VENDOR_INFO_REGPRICE = "VENDOR_INFO_REGPRICE"; // 注册资金
	private static final String VENDOR_INFO_REGRADDRESS = "VENDOR_INFO_REGRADDRESS"; // 注册地址
	private static final String VENDOR_INFO_EMPLOYEENUM = "VENDOR_INFO_EMPLOYEENUM"; // 员工人数
	private static final String VENDOR_INFO_WEBSITE = "VENDOR_INFO_WEBSITE"; // 供应商官网
	private static final String VENDOR_CREDIT_PURCHASEDEAL = "VENDOR_CREDIT_PURCHASEDEAL"; // 采购协议
	private static final String VENDOR_CREDIT_PURCHASEDEALDATE = "VENDOR_CREDIT_PURCHASEDEALDATE"; // 采购协议有效期
	private static final String VENDOR_CREDIT_SECRECYPROTOCOL = "VENDOR_CREDIT_SECRECYPROTOCOL"; // 保密协议
	private static final String VENDOR_CREDIT_SECRECYPROTOCOLDATE = "VENDOR_CREDIT_SECRECYPROTOCOLDATE"; // 保密协议有效期
	private static final String VENDOR_SALE_INFOVO_FOCUSFIELDS = "VENDOR_SALE_INFOVO_FOCUSFIELDS"; // 关注领域
	private static final String VENDOR_SALE_INFOVO_PRODUCTCATEGORYS = "VENDOR_SALE_INFOVO_PRODUCTCATEGORYS"; // 优势产品类别
	private static final String VENDOR_SALE_INFOVO_MAJORCLIENTS = "VENDOR_SALE_INFOVO_MAJORCLIENTS"; // 主要客户

	// 走审核的内容
	private static final String ORG_SUPPLIER_ARCHIVES_REVIEW = "ORG_SUPPLIER_ARCHIVES_REVIEW";// 建档
	private static final String ORG_SUPPLIER_INFO_CHANGE_REVIEW = "ORG_SUPPLIER_INFO_CHANGE_REVIEW";// 基本信息变更
	private static final String ORG_SUPPLIER_PRODUCTLINE_CHANGE_REVIEW = "ORG_SUPPLIER_PRODUCTLINE_CHANGE_REVIEW";// 产品线信息变更
	private static final String ORG_SUPPLIER_INVALID_REVIEW = "ORG_SUPPLIER_INVALID_REVIEW";// 失效
	private static final String ORG_SUPPLIER_ENABLED_REVIEW = "ORG_SUPPLIER_ENABLED_REVIEW";// 启用

	private static final String Y = "Y"; // 产品线校验使用
	private static final String N = "N"; // 产品线校验使用
	private static final String SIX_ZERO = "000000"; // 产品线校验使用
	
	private static final String EXPORT_TEMPLATE = "原厂,大类,小类,次小类";

	@Resource(name = "redisTemplate")
	private HashOperations<String, String, String> aliasFacilityOps;

	private static final Logger logger = LoggerFactory.getLogger(VendorManage2.class);

	@Autowired
	private PartyDao partyDao;

	@Autowired
	private PartyGroupDao partyGroupDao;

	@Autowired
	private PartySupplierDao partySupplierDao;

	@Autowired
	private PartyAttributeDao partyAttributeDao;

	@Autowired
	private PartyCreditDao partyCreditDao;

	@Autowired
	private PartyProductLineDao partyProductLineDao;

	@Autowired
	private PartyBankAccountDao partyBankAccountDao;

	@Autowired
	private PersonDao personDao;

	@Autowired
	private FacilityDao facilityDao;

	@Autowired
	private VendorDao vendorDao;

	@Autowired
	private UpFileManager upFileManager;

	@Autowired
	private PartyRoleDao partyRoleDao;

	@Autowired
	private PartySupplierAliasDao supplierAliasDao;

	@Autowired
	private WorkflowClientBuilder workflowClientBuilder;

	@Autowired
	private ProductClientBuilder productClientBuilder;

	@Autowired
	private ACLResource aCLResource;

	@Autowired
	private AuthorizationUtil authorizationUtil;

	@Autowired
	private ShipmentClientBuilder shipmentClientBuilder;

	@Autowired
	private SendMail sendMail;

	@Autowired
	private VendorsManager vendorsManager;

	@Autowired
	@Qualifier(value = "aliyun.oss.account")
	public AliyunOSSAccount aliyunOSSAccount;
	
	@Autowired
	private MessageClientBuilder messageClientBuilder;
	
	@Autowired
	private MsgSender msgSender;
	
	@Value("${mqConsumeConfig.partyExport.topicName}")
	private String partyCommonTopicName;
	
	@Autowired
	@Qualifier(value = "aliyun.OSSFileUploadSetting")
	private AliyunOSSFileUploadSetting ossFileUploadSetting;

	@Audit(action = "Vendor recordqqq;;;'#vendor.partyId'qqq;;;创建档案    '#vendor.describe'", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public Vendor addVendor(@Param("vendor") Vendor vendor) throws BusinessException {

		// 供应商主键
		String partyId = String.valueOf(IdGen.getInstance().nextId());

		// 如果有供应商ID传过来 则判断是第二次提交审核，删除历史数据，然后再新增
		if (StringUtils.isNotBlank(vendor.getPartyId())) {

			partyId = vendor.getPartyId();

			ApplyVo applyVo = new ApplyVo();
			applyVo.setApplyOrgId(partyId);
			// 判断是否在建档审核中
			PageInfo<Apply> page3 = workflowClientBuilder.applyClient().queryApplyByEntity(ApplyVoType.PROCESS,
					ORG_SUPPLIER_ARCHIVES_REVIEW, applyVo, authorizationUtil.getLoginAuthorization());

			if (null != page3 && CollectionUtils.isNotEmpty(page3.getList())) {
				List<Apply> list3 = page3.getList();
				for (Apply apply3 : list3) {
					if (StringUtils.isNotBlank(apply3.getStatus().name())
							&& ApplyStatus.WAIT_APPROVE == apply3.getStatus()) {
						throw new BusinessException("该供应商ID" + partyId + "建档审核正在审核中的流程，请勿提交",
								"该供应商ID" + partyId + "建档审核正在审核中的流程，请勿提交");
					}

				}
			}
			deleteVendorInfo(partyId);
		}

		// 供应商基础信息
		VendorInfoVo vendorInfoVo = vendor.getVendorInfoVo();
		// 产品线信息
		Set<PartyProductLine> partyProductLineList = vendor.getPartyProductLineList();
		// 供应商信用信息
		VendorCreditVo vendorCreditVo = vendor.getVendorCreditVo();
		// 属性字段
		Map<String, String> map = vendor.getMap();

		// 校验供应商简称唯一性
		PartyGroup par = findGroupName(null, vendorInfoVo.getGroupName());
		if (null != par) {
			throw new BusinessException("供应商简称已经存在，请重新填写", "供应商简称已经存在，请重新填写");
		}

		vendor.setPartyId(partyId);

		// 校验产品线
		// verifyProductLine(partyProductLineList);

		// 插入APPLY 调用审核，建档状态
		// insertApply(partyId, vendor);

		// 校验产品线
		verifyProductLine(partyProductLineList);

		// 插入APPLY 调用审核，建档状态
		insertApply(partyId, vendor);

		// 插入PARTY 基础表
		insertParty(partyId, vendorInfoVo);

		// 插入PARTY_GROUP 供应商基础表
		insertPartyGroup(partyId, vendorInfoVo);

		// 插入PARTY_SUPPLIER 供应商属性表1
		insertPartySupplier(partyId, vendorInfoVo);

		// 插入PARTY_ATTRIBUTE 供应商属性表 2
		insertPartyAttribute(partyId, map);

		// 插入PARTY_RELATIONSHIP 分管部门
		insertpartyRelationship(vendorInfoVo.getDeptId(), partyId, PartyRelationshipTypeEnum.VENDOR_DEPT_REL);

		// 插入PARTY_RELATIONSHIP 负责人
		insertpartyRelationship(vendorInfoVo.getPrincipalId(), partyId, PartyRelationshipTypeEnum.DEVELOPMENT_BY);

		// 优化 询价员支持多个 add by helinmei
		String partyId1 = partyId;
		if (CollectionUtils.isNotEmpty(vendorInfoVo.getEnquiryList())) {
			HashSet<String> tempset = new HashSet<>(vendorInfoVo.getEnquiryList());
			tempset.stream().forEach(enquiryId -> {
				// 插入PARTY_RELATIONSHIP 询价员
				insertpartyRelationship(enquiryId, partyId1, PartyRelationshipTypeEnum.VENDOR_ENQUIRY_REL);

			});
		}

		// 优化 报价员支持多个 add by helinmei
		if (CollectionUtils.isNotEmpty(vendorInfoVo.getOfferList())) {
			HashSet<String> tempset = new HashSet<>(vendorInfoVo.getOfferList());
			tempset.stream().forEach(offerId -> {
				// 插入PARTY_RELATIONSHIP 报价员
				insertpartyRelationship(offerId, partyId1, PartyRelationshipTypeEnum.VENDOR_OFFER_REL);
			});
		}

		// 插入PARTY_RELATIONSHIP 供应商为哪个公司供货
		User user = vendorsManager.getUser(RequestHelper.getLoginUserId());
		insertpartyRelationship(partyId, user.getEnterpriseId(), PartyRelationshipTypeEnum.SUPPLIER_REL);

		// 插入PARTY_ROLE 角色表
		partyRoleDao.insert(partyId, RoleTypeEnum.SUPPLIER.toString(), RequestHelper.getLoginUserId(), new Date(),
				RequestHelper.getLoginUserId(), new Date());

		// 插入PARTY_PRODUCT_LINE 供应商产品线表
		insertpartyRelationship(partyId, partyProductLineList);

		// 插入PARTY_CREDIT 供应商账期
		insertPartyCredit(partyId, vendorCreditVo);

		// 插入PARTY_BANK_ACCOUNT 银行资料
		insertPartyBankAccount(partyId, vendorCreditVo.getPartyBankAccount());

		// 插入PARTY_ATTACHMENT 供应商附件表
		insertPartyCreditAttachment(partyId, vendorCreditVo.getCreditAttachmentList());

		// 插入PERSON 供应商联系人
		// insertContactPersonInfo(partyId,
		// vendorSaleInfoVo.getContactPersonInfoList());

		// 插入FACILITY 供应商仓库
		// insertFacility(partyId, vendorSaleInfoVo.getFacilityList());

		// 插入APPLY 调用审核，建档状态
		// insertApply(partyId, vendor);

		return vendor;

	}

	// 插入PARTY 基础表 分支
	public void insertParty(String partyId, VendorInfoVo vendorInfoVo) {
		// 插入PARTY 基础表
		Party party = new Party();
		party.setId(partyId);
		/* 类型供应商 */
		party.setPartyType(PartyType.SUPPLIER);
		/* 状态 */
		party.setPartyStatus(PartyStatus.PARTY_ENABLED);
		/* 供应商编码 */
		party.setPartyCode(vendorInfoVo.getPartyCode());
		party.setCreator(RequestHelper.getLoginUserId());
		party.setCreatedDate(new Date());
		party.setLastUpdateUser(RequestHelper.getLoginUserId());
		party.setLastUpdateDate(new Date());
		partyDao.insert(party);
	}

	// 插入PARTY_GROUP 供应商基础表 分支
	public void insertPartyGroup(String partyId, VendorInfoVo vendorInfoVo) {
		// 插入PARTY_GROUP 供应商基础表
		PartyGroup partyGroup = new PartyGroup();
		partyGroup.setPartyId(partyId);
		/* 供应商简称 */
		partyGroup.setGroupName(vendorInfoVo.getGroupName());
		/* 供应商全称 */
		partyGroup.setGroupNameFull(vendorInfoVo.getGroupNameFull());
		/* 供应商logo地址 */
		partyGroup.setLogoImageUrl(vendorInfoVo.getLogoImageUrl());

		partyGroup.setAccountStatus(AccountStatus.INEFFECTIVE);
		partyGroup.setActiveStatus(ActiveStatus.INVALID);
		partyGroup.setCreator(RequestHelper.getLoginUserId());
		partyGroup.setCreatedDate(new Date());
		partyGroup.setLastUpdateUser(RequestHelper.getLoginUserId());
		partyGroup.setLastUpdateDate(new Date());
		partyGroupDao.insertPartyGroup(partyGroup);
	}

	// 插入PARTY_SUPPLIER 供应商基础表 分支
	public void insertPartySupplier(String partyId, VendorInfoVo vendorInfoVo) {
		// 插入PARTY_SUPPLIER 供应商属性表
		PartySupplier partySupplier = new PartySupplier();
		partySupplier.setPartyId(partyId);
		/* 所属地区 */
		partySupplier.setRegion(vendorInfoVo.getRegion());
		/* 所属分类 */
		partySupplier.setCategory(vendorInfoVo.getCategory());
		/* 供应商总公司 */
		partySupplier.setGeneralHeadquarters(vendorInfoVo.getGeneralHeadquarters());
		/* 公司类型 */
		partySupplier.setGenre(vendorInfoVo.getGenre());
		/* 是否上市 */
		partySupplier.setListed(vendorInfoVo.getListed());
		/* 上市的股票代码 */
		partySupplier.setStockCode(vendorInfoVo.getStockCode());
		/* 核心供应商 */
		partySupplier.setIsCore(vendorInfoVo.getIsCore());
		/* 是否显示销售价格(Y/N) */
		partySupplier.setIsSupPrice("Y");
		/* 是否显示名称(Y/N) */
		partySupplier.setIsShowName("Y");
		/* 状态(ENABLE/UNABLE/DELETE) */
		partySupplier.setSupplierStatus(SupplierStatus.ENABLE);
		partySupplier.setCreator(RequestHelper.getLoginUserId());
		partySupplier.setCreatedDate(new Date());
		partySupplier.setLastUpdateUser(RequestHelper.getLoginUserId());
		partySupplier.setLastUpdateDate(new Date());
		partySupplierDao.insert(partySupplier);
	}

	// 插入PARTY_ATTRIBUTE 供应商属性表2 分支
	public void insertPartyAttribute(String partyId, Map<String, String> map) {
		if (null != map && map.size() > 0) {
			List<PartyAttribute> arrayList = new ArrayList<>();
			for (Map.Entry<String, String> entry : map.entrySet()) {
				PartyAttribute partyAttribute = getPartyAttribute(partyId, entry.getKey(), entry.getValue());
				arrayList.add(partyAttribute);
			}
			partyAttributeDao.insertVendorField(arrayList);
		}
	}

	// 插入PARTY_RELATIONSHIP 分支
	public void insertpartyRelationship(String partyIdFrom, String partyIdTo,
			PartyRelationshipTypeEnum partyRelationshipTypeEnum) {
		// 插入PARTY_RELATIONSHIP
		PartyRelationship partyRelationship = PartyRelationship.build(partyRelationshipTypeEnum);
		/* 供应商联系人主键 */
		partyRelationship.setPartyIdFrom(partyIdFrom);
		/* 供应商主键 */
		partyRelationship.setPartyIdTo(partyIdTo);
		partyRelationship.setFromDate(new Date());
		partyRelationship.setStatusId(PartyRelationshipStatus.ENABLE);
		partyRelationship.setCreator(RequestHelper.getLoginUserId());
		partyRelationship.setCreatedDate(new Date());
		partyRelationship.setLastUpdateUser(RequestHelper.getLoginUserId());
		partyRelationship.setLastUpdateDate(new Date());
		partyCreditDao.insertVendorRel(partyRelationship);
	}

	// 插入PARTY_PRODUCT_LINE 供应商产品线表 分支
	@SuppressWarnings("rawtypes")
	public void insertpartyRelationship(String partyId, Set<PartyProductLine> partyProductLineList)
			throws BusinessException {
		// 插入PARTY_PRODUCT_LINE 供应商产品线表
		if (null != partyProductLineList && CollectionUtils.isNotEmpty(partyProductLineList)) {
			List<PartyProductLine> pList = new ArrayList<>();
			for (PartyProductLine p : partyProductLineList) {
				PartyProductLine partyProductLine = new PartyProductLine();
				/* 产品线主键ID */
				partyProductLine.setPartyProductLineId(String.valueOf(IdGen.getInstance().nextId()));
				/* 供应商外键ID */
				partyProductLine.setPartyId(partyId);
				/* 品牌ID */
				partyProductLine.setBrandId(p.getBrandId());
				/* 品牌名称 */
				partyProductLine.setBrandName(p.getBrandName());
				/* 大类ID */
				partyProductLine.setCategory1Id(p.getCategory1Id());
				/* 大类名称 */
				partyProductLine.setCategory1Name(p.getCategory1Name());
				/* 小类ID */
				partyProductLine.setCategory2Id(p.getCategory2Id());
				/* 小类名称 */
				partyProductLine.setCategory2Name(p.getCategory2Name());
				/* 次小类ID */
				partyProductLine.setCategory3Id(p.getCategory3Id());
				/* 次小类名称 */
				partyProductLine.setCategory3Name(p.getCategory3Name());
				/* 状态(ENABLE/UNABLE/DELETE) */
				partyProductLine.setStatus(Status.ENABLE);
				partyProductLine.setType(p.getType());
				partyProductLine.setCreator(RequestHelper.getLoginUserId());
				partyProductLine.setCreatedDate(new Date());
				partyProductLine.setLastUpdateUser(RequestHelper.getLoginUserId());
				partyProductLine.setLastUpdateDate(new Date());
				pList.add(partyProductLine);
			}
			partyProductLineDao.insertLineList(pList);
		}
	}

	// 插入PARTY_CREDIT 供应商账期 分支
	public void insertPartyCredit(String partyId, VendorCreditVo vendorCreditVo) {
		// 插入PARTY_CREDIT 供应商账期
		PartyCredit partyCredit = new PartyCredit();
		partyCredit.setPartyCreditId(String.valueOf(IdGen.getInstance().nextId()));
		partyCredit.setPartyId(partyId);
		/* 信用额度币种 */
		partyCredit.setCurrency(vendorCreditVo.getCurrency());
		/* 授信额度 */
		partyCredit.setCreditQuota(vendorCreditVo.getCreditQuota());
		/* 授信余额 */
		partyCredit.setRealtimeCreditQuota(vendorCreditVo.getRealtimeCreditQuota());
		/* 授信期限（月结xxx天） */
		partyCredit.setCreditDeadline(vendorCreditVo.getCreditDeadline());
		/* 对账日期（纯数字，代表每月几号） */
		partyCredit.setCheckDate(vendorCreditVo.getCheckDate());
		/* 对账周期 */
		if (StringUtils.isBlank(vendorCreditVo.getCheckCycle())) {
			partyCredit.setCheckCycle("30");
		} else {
			partyCredit.setCheckCycle(vendorCreditVo.getCheckCycle());
		}
		/* 付款日期（纯数字，代表每月几号） */
		partyCredit.setPayDate(vendorCreditVo.getPayDate());

		/* 付款方式 付款条件 */
		partyCredit.setPaymentTerms(vendorCreditVo.getPaymentTerms());

		/* 有效时间 */
		partyCredit.setFromDate(new Date());

		/* 结算方式 */
		partyCredit.setSettlementMethod(vendorCreditVo.getSettlementMethod());

		/* 申请人 */
		partyCredit.setApplyUser(RequestHelper.getLoginUserId());
		/* 申请人邮箱 */
		partyCredit.setApplyMail(null);
		/* 申请人联系方式 */
		partyCredit.setApplyInformation(null);
		partyCredit.setCreator(RequestHelper.getLoginUserId());
		partyCredit.setCreatedDate(new Date());
		partyCredit.setLastUpdateUser(RequestHelper.getLoginUserId());
		partyCredit.setLastUpdateDate(new Date());
		partyCreditDao.insert(partyCredit);
	}

	// 插入PARTY_BANK_ACCOUNT 银行资料 分支
	public void insertPartyBankAccount(String partyId, List<PartyBankAccount> partyBankAccountList) {
		// 插入PARTY_BANK_ACCOUNT 银行资料
		if (null != partyBankAccountList && CollectionUtils.isNotEmpty(partyBankAccountList)) {
			// 判断默认 0非默认 1默认
			int flag = 0;
			for (PartyBankAccount p : partyBankAccountList) {
				PartyBankAccount partyBankAccount = new PartyBankAccount();
				/* 主键ID */
				if (StringUtils.isNotBlank(p.getPartyBankAccountId())) {
					partyBankAccount.setPartyBankAccountId(p.getPartyBankAccountId());
				} else {
					partyBankAccount.setPartyBankAccountId(String.valueOf(IdGen.getInstance().nextId()));
				}

				/* party_id外键 */
				partyBankAccount.setPartyId(partyId);
				/* 币种 */
				partyBankAccount.setCurrency(p.getCurrency());
				/* 账户名称 */
				partyBankAccount.setAccountName(p.getAccountName());
				/* 银行账号 */
				partyBankAccount.setBankAccount(p.getBankAccount());
				/* 银行名称 */
				partyBankAccount.setBankName(p.getBankName());
				/* 税号 */
				partyBankAccount.setTaxNumber(p.getTaxNumber());
				/* 地址 */
				partyBankAccount.setAddress(p.getAddress());
				/* 电话 */
				partyBankAccount.setContactNumber(p.getContactNumber());
				/* 状态(ENABLE/UNABLE/DELETE) */
				partyBankAccount.setStatus(Status.ENABLE);
				/* 是否默认银行,是:Y,否：N */
				if (flag == 0 && null != p.getIsDefault() && Y.equals(p.getIsDefault())) {
					partyBankAccount.setIsDefault(Y);
					flag = 1;
				} else {
					partyBankAccount.setIsDefault(N);
				}
				partyBankAccount.setSwiftCode(p.getSwiftCode());
				partyBankAccount.setCreator(RequestHelper.getLoginUserId());
				partyBankAccount.setCreatedDate(new Date());
				partyBankAccount.setLastUpdateUser(RequestHelper.getLoginUserId());
				partyBankAccount.setLastUpdateDate(new Date());
				partyBankAccountDao.insert(partyBankAccount);
			}
		}
	}

	// 插入PARTY_ATTACHMENT 供应商附件表 分支
	public void insertPartyCreditAttachment(String partyId, List<PartyAttachment> creditAttachmentList) {
		// 插入PARTY_ATTACHMENT 供应商附件表
		if (null != creditAttachmentList && CollectionUtils.isNotEmpty(creditAttachmentList)) {
			for (PartyAttachment p : creditAttachmentList) {
				PartyAttachment partyCreditAttachment = new PartyAttachment();
				/* 账期主键ID */
				if (StringUtils.isNotBlank(p.getPartyAttachmentId())) {
					partyCreditAttachment.setPartyAttachmentId(p.getPartyAttachmentId());
				} else {
					partyCreditAttachment.setPartyAttachmentId(String.valueOf(IdGen.getInstance().nextId()));
				}
				/* 外键关联 */
				partyCreditAttachment.setPartyId(partyId);
				/* 附件名称 */
				partyCreditAttachment.setAttachmentName(p.getAttachmentName());
				/* 附件地址 */
				partyCreditAttachment.setAttachmentUrl(p.getAttachmentUrl());
				/* 账期附件类型 */
				partyCreditAttachment.setAttachmentType(AttachmentType.VENDOR_CREDIT);
				partyCreditAttachment.setCreator(RequestHelper.getLoginUserId());
				partyCreditAttachment.setCreatedDate(new Date());
				partyCreditAttachment.setLastUpdateUser(RequestHelper.getLoginUserId());
				partyCreditAttachment.setLastUpdateDate(new Date());
				partyCreditDao.insertVo(partyCreditAttachment);
			}
		}
	}

	// 插入PERSON 供应商联系人 分支
	public void insertContactPersonInfo(String partyId, List<ContactPersonInfo> contactPersonInfoList) {
		// 插入PERSON 供应商联系人
		if (null != contactPersonInfoList && CollectionUtils.isNotEmpty(contactPersonInfoList)) {

			// 判断默认 0非默认 1默认
			int flag = 0;
			for (ContactPersonInfo c : contactPersonInfoList) {

				/* 供应商联系人主键 */
				String id = null;
				if (StringUtils.isNotBlank(c.getPartyId())) {
					id = c.getPartyId();
				} else {
					id = String.valueOf(IdGen.getInstance().nextId());
				}
				ContactPersonInfo contactPersonInfo = new ContactPersonInfo();
				contactPersonInfo.setPartyId(id);
				/* 供应商联系人名称 */
				contactPersonInfo.setLastNameLocal(c.getLastNameLocal());
				/* 联系人职位 */
				contactPersonInfo.setOccupation(c.getOccupation());
				/* 联系人职能 询价ENQUIRY 下单ORDER 不限NOT_LIMIT */
				contactPersonInfo.setPersonalTitle(c.getPersonalTitle());
				/* 邮箱 */
				contactPersonInfo.setMail(c.getMail());
				/* 电话 */
				contactPersonInfo.setFixedtel(c.getFixedtel());
				/* 手机 */
				contactPersonInfo.setTel(c.getTel());
				/* 地址 */
				contactPersonInfo.setAddress(c.getAddress());
				/* 是否默认联系人：是:Y,否：N */
				if (flag == 0 && null != c.getIsDefault() && Y.equals(c.getIsDefault())) {
					contactPersonInfo.setIsDefault(Y);
					flag = 1;
				} else {
					contactPersonInfo.setIsDefault(N);
				}
				contactPersonInfo.setCreator(RequestHelper.getLoginUserId());
				contactPersonInfo.setCreatedDate(new Date());
				contactPersonInfo.setLastUpdateUser(RequestHelper.getLoginUserId());
				contactPersonInfo.setLastUpdateDate(new Date());
				personDao.insertContactPersonInfo(contactPersonInfo);

				// 插入PARTY_RELATIONSHIP 联系人与供应商的关系表
				insertpartyRelationship(id, partyId, PartyRelationshipTypeEnum.USER_DEPT_REL);

				// 插入PARTY_RELATIONSHIP 联系人与产品线的关系表
				List<String> partyProductLineIdList = c.getPartyProductLineIdList();
				if (CollectionUtils.isNotEmpty(partyProductLineIdList)) {
					for (String s : partyProductLineIdList) {
						insertpartyRelationship(id, s, PartyRelationshipTypeEnum.USER_PRODUCTLINE_REL);
					}
				}
			}
		}
	}

	// 插入FACILITY 供应商仓库 分支
	public void insertFacility(String partyId, List<Facility> facilityList) {
		// 查询供应商下的所有仓库
		// List<Facility> newList = facilityDao.getFacilityList(partyId);
		// 插入FACILITY 供应商仓库
		if (null != facilityList && CollectionUtils.isNotEmpty(facilityList)) {
			for (Facility p : facilityList) {
				if (StringUtils.isNotBlank(p.getFacilityName()) || StringUtils.isNotBlank(p.getId())) {
					Facility facility = new Facility();
					facility.setFacilityName(p.getFacilityName());
					facility.setIsShowaFacility(p.getIsShowaFacility());
					facility.setOwnerPartyId(partyId);

					/* 仓库ID */
					if (StringUtils.isNotBlank(p.getId())) {
						facility.setId(p.getId());
						facility.setLastUpdateUser(RequestHelper.getLoginUserId());
						facility.setLastUpdateDate(new Date());
						facilityDao.update(facility);
					} else {
						facility.setId(String.valueOf(IdGen.getInstance().nextId()));
						/* Spirit ID ,用于描述人、组织、团体的ID */
						facility.setCreator(RequestHelper.getLoginUserId());
						facility.setCreatedDate(new Date());
						/* 有效期 */
						facility.setFromDate(new Date());

						// 判断是否有相同的仓库名称
						facilityDao.add(facility);

					}
					aliasFacilityOps.getOperations().delete("materialFacilityCache");
				}
			}
		}
	}

	// 插入APPLY 调用审核，建档状态
	public void insertApply(String partyId, Vendor vendor) throws BusinessException {

		try {
			Party party = personDao.getPersonByUserId(RequestHelper.getLoginUserId());
			if (null != party && null != party.getPerson()) {
				vendor.setApplyName(party.getPerson().getLastNameLocal());
				vendor.setApplyMail(party.getPerson().getMail());
				vendor.setContactUserName(party.getPerson().getLastNameLocal());
			}

			vendor.setPartyCode(vendor.getVendorInfoVo().getPartyCode());
			vendor.setRegion(vendor.getVendorInfoVo().getRegionName());
			vendor.setName(vendor.getVendorInfoVo().getGroupNameFull());

			Apply apply = new Apply();
			JSONObject jsonObject = JSONObject.fromObject(vendor);
			apply.setApplyContent(jsonObject.toString());
			apply.setApplyUserId(RequestHelper.getLoginUserId());
			apply.setProcessId(ORG_SUPPLIER_ARCHIVES_REVIEW);
			apply.setApplyOrgId(partyId);
			apply.setApplyPageUrl("");
			apply.setCallBackUrl("");
			apply.setReason(vendor.getDescribe());
			workflowClientBuilder.applyClient().createApply(apply, authorizationUtil.getLoginAuthorization());
			try {
				sendMail.checkApply(vendor.getVendorInfoVo().getGroupName(), RoleTypeEnum.SUPPLIER_MANAGER,
						VendorApplyType.ORG_SUPPLIER_ARCHIVES_REVIEW, RequestHelper.getLoginUserId());
			} catch (Exception e) {
				logger.error("申请邮件发送时时异常：{}", e.getMessage());
			}
		} catch (Exception e) {
			throw new BusinessException("调用workflow服务异常：{}", e.getMessage());
		}
	}

	/**
	 * 产品线导入文件解析接口，（2次优化）
	 * 
	 * @param partyId
	 * @param fileUrl
	 * @param type
	 * @return
	 * @throws BusinessException
	 * @since 2018年2月2日
	 * @author jik.shu@yikuyi.com
	 */
	public PageInfo<PartyProductLine> parseProductsFile(String partyId, String fileUrl,Type type) throws BusinessException {
		PageInfo<PartyProductLine> pageInfo = new PageInfo<>();
		if (StringUtils.isBlank(fileUrl)) {
			return pageInfo;
		}
		List<List<String>> data2 = upFileManager.fileDownload(fileUrl);// 从阿里云下载文件
		if (CollectionUtils.isEmpty(data2) || data2.size()<3) {
			return pageInfo;
		}
		
		if (data2.size()<2 || StringUtils.isBlank(data2.get(0).get(0))){
			throw new BusinessException("标题与模版不符合", "标题出错，请参照模版");// 当标题错误时，抛出异常
		}
		if (data2.size()<2 || data2.get(1).size()<4 || (StringUtils.isBlank(data2.get(1).get(0)) || StringUtils.isBlank(data2.get(1).get(1)) || StringUtils.isBlank(data2.get(1).get(2)) || StringUtils.isBlank(data2.get(1).get(3)))) {
			throw new BusinessException("标题与模版不符合", "标题出错，请参照模版");// 当标题错误时，抛出异常
		}
		
		Set<String> brandNames = data2.stream().skip(2).filter(v-> !v.isEmpty()).map(v->StringUtils.upperCase(v.get(0))).collect(Collectors.toSet());
		Map<String,ProductBrand> aliasBrandMap = new CaseInsensitiveMap<>(productClientBuilder.brandResource().getAliasBrandMapByNames(brandNames, authorizationUtil.getLoginAuthorization()));
		
		Set<String> cat3Names = data2.stream().skip(2).filter(v-> !v.isEmpty()).filter(v-> StringUtils.isNotEmpty(v.get(3))).map(v->v.get(3)).collect(Collectors.toSet());
		Set<String> cat2Names = data2.stream().skip(2).filter(v-> !v.isEmpty()).filter(v-> StringUtils.isEmpty(v.get(3))).map(v->v.get(2)).collect(Collectors.toSet());
		Set<String> cat1Names = data2.stream().skip(2).filter(v-> !v.isEmpty()).filter(v-> StringUtils.isEmpty(v.get(3)) && StringUtils.isEmpty(v.get(2))).map(v->v.get(1)).collect(Collectors.toSet());
		Map<String,ProductCategoryParent> cate1Names = Collections.emptyMap();
		Map<String,ProductCategoryParent> cate2Names = Collections.emptyMap();
		Map<String,ProductCategoryParent> cate3Names = Collections.emptyMap();
		if(CollectionUtils.isNotEmpty(cat1Names)){
			cate1Names = productClientBuilder.categoryResource().getParentsByNames(cat1Names, ProductCategoryLevel.CAT_ELEVEL_1).stream()
					.collect(Collectors.toMap(ProductCategoryParent::getName, Function.identity(), (key1, key2) -> key2, CaseInsensitiveMap::new));
		}
		if(CollectionUtils.isNotEmpty(cat2Names)){
			cate2Names = productClientBuilder.categoryResource().getParentsByNames(cat2Names, ProductCategoryLevel.CAT_ELEVEL_2).stream()
					.collect(Collectors.toMap(ProductCategoryParent::getName, Function.identity(), (key1, key2) -> key2, CaseInsensitiveMap::new));
		}
		if(CollectionUtils.isNotEmpty(cat3Names)){
			cate3Names = productClientBuilder.categoryResource().getParentsByNames(cat3Names, ProductCategoryLevel.CAT_ELEVEL_3).stream()
					.collect(Collectors.toMap(ProductCategoryParent::getName, Function.identity(), (key1, key2) -> key2, CaseInsensitiveMap::new));
		}
		/**
		 * 处理读取到的数据
		 */
		// 取重后的商品列表
		List<PartyProductLine> partyProductLineLsit = new ArrayList<>();
		for (int rowNum = 2; rowNum < data2.size(); rowNum++) {
			List<String> temp = data2.get(rowNum);
			// 品牌ID
			String brandId = SIX_ZERO;
			// 品牌
			String brandName = temp.get(0);
			// 大类
			String category1Name = temp.get(1);
			// 小类
			String category2Name = temp.get(2);
			// 次小类
			String category3Name = temp.get(3);

			// 遇到null行结束处理，品牌为Null忽略本行
			if (StringUtils.isBlank(brandName) && StringUtils.isBlank(category1Name) && StringUtils.isBlank(category2Name) && StringUtils.isBlank(category3Name)) {
				break;
			} else if (StringUtils.isBlank(brandName)) {
				continue;
			}
			if (!aliasBrandMap.containsKey(brandName)) {
				partyProductLineLsit.add(addErrorVo(brandId, brandName, category1Name, category2Name, category3Name, type));
			} else {
				brandId = String.valueOf(aliasBrandMap.get(brandName).getId());
				brandName = aliasBrandMap.get(brandName).getBrandName();

				PartyProductLine partyProductLine = new PartyProductLine();
				partyProductLine.setBrandId(brandId);
				partyProductLine.setBrandName(brandName);
				partyProductLine.setBrandSign(N);
				partyProductLine.setCategory1Name(category1Name);
				partyProductLine.setCategory2Name(category2Name);
				partyProductLine.setCategory3Name(category3Name);
				partyProductLine.setType(type);

				category1Name = StringUtils.isBlank(category1Name) ? "*" : category1Name;
				category2Name = StringUtils.isBlank(category2Name) ? "*" : category2Name;
				category3Name = StringUtils.isBlank(category3Name) ? "*" : category3Name;

				List<String> cateNames = Arrays.asList(category1Name, category2Name, category3Name);

				if ("*".equals(category1Name) && "*".equals(category2Name) && "*".equals(category3Name)) {
					partyProductLine = addSuccerVo(brandId, brandName, null, null, null, null, null, null, type);
				} else if (!"*".equals(category3Name)) {
					if (cate3Names.containsKey(category3Name)) {
						partyProductLine = cateMatch(cateNames, cate3Names.get(category3Name), ProductCategoryLevel.CAT_ELEVEL_3.getValue(), partyProductLine);
					} else {
						partyProductLine = addErrorVo(brandId, brandName, category1Name, category2Name, category3Name, type);
					}
				} else if (!"*".equals(category2Name)) {
					if (cate2Names.containsKey(category2Name)) {
						partyProductLine = cateMatch(cateNames, cate2Names.get(category2Name), ProductCategoryLevel.CAT_ELEVEL_2.getValue(), partyProductLine);
					} else {
						partyProductLine = addErrorVo(brandId, brandName, category1Name, category2Name, category3Name, type);
					}
				} else if (!"*".equals(category1Name)) {
					if (cate1Names.containsKey(category1Name)) {
						partyProductLine = cateMatch(cateNames, cate1Names.get(category1Name), ProductCategoryLevel.CAT_ELEVEL_1.getValue(), partyProductLine);
					} else {
						partyProductLine = addErrorVo(brandId, brandName, category1Name, category2Name, category3Name, type);
					}
				}
				partyProductLineLsit.add(partyProductLine);
			}
		}
		//去重
		Set<PartyProductLine> ts = new LinkedHashSet<>();
		if(StringUtils.isNotEmpty(partyId)){
			ts.addAll(partyProductLineDao.findByEntity(new PartyProductLine().setPartyId(partyId)));
		}
		//去重
		pageInfo.setList(new ArrayList<>(partyProductLineLsit.stream().filter(v->!ts.contains(v)).collect(Collectors.toCollection(LinkedHashSet::new))));
		return pageInfo;
	}
	
	private PartyProductLine cateMatch(List<String> cateNames , ProductCategoryParent parent, int cateLevel,PartyProductLine partyProductLine){
		//递归结束
		if(null == parent){
			return partyProductLine;
		}
		String cateName = cateNames.get(cateLevel-1);
		if(cateLevel == parent.getLevel() && cateName.equals(parent.getName())){
			if(cateLevel == ProductCategoryLevel.CAT_ELEVEL_1.getValue()){
				partyProductLine.setCategory1Id(String.valueOf(parent.getId()));
				partyProductLine.setCategory1Name(cateName);
				partyProductLine.setCategory1Sign(N);
			}
			if(cateLevel == ProductCategoryLevel.CAT_ELEVEL_2.getValue()){
				partyProductLine.setCategory2Id(String.valueOf(parent.getId()));
				partyProductLine.setCategory2Name(cateName);
				partyProductLine.setCategory2Sign(N);
			}
			if(cateLevel == ProductCategoryLevel.CAT_ELEVEL_3.getValue()){
				partyProductLine.setCategory3Id(String.valueOf(parent.getId()));
				partyProductLine.setCategory3Name(cateName);
				partyProductLine.setCategory3Sign(N);
			}
		}else{
			if(cateLevel == ProductCategoryLevel.CAT_ELEVEL_1.getValue()){
				partyProductLine.setCategory1Name(cateName);
				partyProductLine.setCategory1Sign(Y);
			}
			if(cateLevel == ProductCategoryLevel.CAT_ELEVEL_2.getValue()){
				partyProductLine.setCategory1Name("*".equals(cateNames.get(0)) ? null : cateNames.get(0));
				partyProductLine.setCategory1Sign(Y);
				partyProductLine.setCategory2Name(cateName);
				partyProductLine.setCategory2Sign(Y);
			}
			if(cateLevel == ProductCategoryLevel.CAT_ELEVEL_3.getValue()){
				partyProductLine.setCategory1Name("*".equals(cateNames.get(0)) ? null : cateNames.get(0));
				partyProductLine.setCategory1Sign(Y);
				partyProductLine.setCategory1Name("*".equals(cateNames.get(1)) ? null : cateNames.get(1));
				partyProductLine.setCategory2Sign(Y);
				partyProductLine.setCategory3Name(cateName);
				partyProductLine.setCategory3Sign(Y);
			}
		}
		ProductCategoryParent tempParent = parent.getParent();
		return cateMatch(cateNames,tempParent , cateLevel -1 , partyProductLine);
	}
	
	private PartyProductLine addErrorVo(String brandId ,String brandName,String cat1Name,String cat2Name,String cat3Name,Type type){
		PartyProductLine partyProductLine = new PartyProductLine();
		partyProductLine.setBrandId(SIX_ZERO.equals(brandId) ? null : brandId);
		partyProductLine.setBrandName(brandName);
		partyProductLine.setBrandSign(SIX_ZERO.equals(brandId) ? Y : N);
		partyProductLine.setCategory1Name("*".equals(cat1Name) ? null : cat1Name);
		partyProductLine.setCategory1Sign(Y);
		partyProductLine.setCategory2Name("*".equals(cat2Name) ? null : cat2Name);
		partyProductLine.setCategory2Sign(Y);
		partyProductLine.setCategory3Name("*".equals(cat3Name) ? null : cat3Name);
		partyProductLine.setCategory3Sign(Y);
		partyProductLine.setType(type);
		return partyProductLine;
	}
	
	private PartyProductLine addSuccerVo(String brandId ,String brandName,String category1Id ,String cat1Name,String category2Id ,String cat2Name,String category3Id ,String cat3Name,Type type){
		PartyProductLine partyProductLine = new PartyProductLine();
		partyProductLine.setBrandId(brandId);
		partyProductLine.setBrandName(brandName);
		partyProductLine.setBrandSign(N);
		partyProductLine.setCategory1Id(category1Id);
		partyProductLine.setCategory3Name(StringUtils.isEmpty(cat1Name) ? partyProductLine.getCategory1Name() : cat1Name);
		partyProductLine.setCategory1Sign(SIX_ZERO.equals(category1Id) ? Y : N);
		partyProductLine.setCategory2Id(category2Id);
		partyProductLine.setCategory3Name(StringUtils.isEmpty(cat2Name) ? partyProductLine.getCategory2Name() : cat2Name);
		partyProductLine.setCategory1Sign(SIX_ZERO.equals(category2Id) ? Y : N);
		partyProductLine.setCategory3Id(category3Id);
		partyProductLine.setCategory3Name(StringUtils.isEmpty(cat3Name) ? partyProductLine.getCategory3Name() : cat3Name);
		partyProductLine.setCategory1Sign(SIX_ZERO.equals(category3Id) ? Y : N);
		partyProductLine.setType(type);
		return partyProductLine;
	}

	/*
	 * 供应商基础信息【变更】
	 */
	@Audit(action = "Vendor recordqqq;;;'#vendorInfoVo.partyId'qqq;;;修改基本信息    '#vendorInfoVo.describe'", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public VendorInfoVo updateVendorInfoVo(@Param(value = "vendorInfoVo") VendorInfoVo vendorInfoVo)
			throws BusinessException {
		String partyId = vendorInfoVo.getPartyId();
		if (StringUtils.isBlank(partyId)) {
			throw new BusinessException("partyId不能为空", "partyId不能为空");
		}

		// 权限校验，
		int s = 0;
		// 创建人登录
		Party party = partyGroupDao.findPartyGroupByPartyId(partyId);
		if (StringUtils.isNotEmpty(party.getCreator()) && party.getCreator().equals(RequestHelper.getLoginUserId())) {
			s = 1;
		}
		// 授权人
		if (s == 0) {
			// 授权人List
			List<PartyRelationship> list3 = personDao.findPartyRelationship(partyId,
					PartyRelationshipTypeEnum.AGENT.name());
			if (CollectionUtils.isNotEmpty(list3)) {
				for (PartyRelationship p : list3) {
					// 如果登录用户是授权人列表
					if (p.getPartyIdFrom().equals(RequestHelper.getLoginUserId())) {
						s = 1;
						break;
					}
				}
			}
		}

		if (s == 0) {
			// 判断是否是超级管理员

			// 获取登录用户权限表
			Set<String> set = aCLResource.getUserRoleList(RequestHelper.getLoginUserId());
			List<String> list1 = new ArrayList<String>(set);

			if (list1.contains("ADMIN")) {
				s = 1;
			}
		}
		if (s == 0) {
			throw new BusinessException("创建人可修改供应商所有信息，业务负责人和询价员可修改销售信息。", "当前用户无操作权限");
		}

		// 更新PARTY_GROUP 供应商基础表
		PartyGroup partyGroup = new PartyGroup();
		partyGroup.setPartyId(partyId);
		/* 供应商logo地址 */
		partyGroup.setLogoImageUrl(vendorInfoVo.getLogoImageUrl());
		partyGroup.setLastUpdateUser(RequestHelper.getLoginUserId());
		partyGroup.setLastUpdateDate(new Date());
		partyGroupDao.updateVendor(partyGroup);

		// 更新PARTY_SUPPLIER 供应商属性表
		PartySupplier partySupplier = new PartySupplier();
		partySupplier.setPartyId(partyId);
		/* 所属地区 */
		partySupplier.setRegion(vendorInfoVo.getRegion());
		/* 供应商总公司 */
		partySupplier.setGeneralHeadquarters(vendorInfoVo.getGeneralHeadquarters());
		/* 公司类型 */
		partySupplier.setGenre(vendorInfoVo.getGenre());
		/* 是否上市 */
		partySupplier.setListed(vendorInfoVo.getListed());
		/* 上市的股票代码 */
		partySupplier.setStockCode(vendorInfoVo.getStockCode());
		partySupplier.setLastUpdateUser(RequestHelper.getLoginUserId());
		partySupplier.setLastUpdateDate(new Date());
		partySupplierDao.updateVendorInfoVo(partySupplier);

		// 删除PARTY_RELATIONSHIP 分管部门
		personDao.delByPartyIdTo(partyId, PartyRelationshipTypeEnum.VENDOR_DEPT_REL.name());
		// 删除PARTY_RELATIONSHIP 负责人
		personDao.delByPartyIdTo(partyId, PartyRelationshipTypeEnum.DEVELOPMENT_BY.name());
		// 删除PARTY_RELATIONSHIP 询价员
		personDao.delByPartyIdTo(partyId, PartyRelationshipTypeEnum.VENDOR_ENQUIRY_REL.name());
		// 删除PARTY_RELATIONSHIP 报价员
		personDao.delByPartyIdTo(partyId, PartyRelationshipTypeEnum.VENDOR_OFFER_REL.name());

		// 插入PARTY_RELATIONSHIP 分管部门

		insertpartyRelationship(vendorInfoVo.getDeptId(), partyId, PartyRelationshipTypeEnum.VENDOR_DEPT_REL);
		// 插入PARTY_RELATIONSHIP 负责人
		insertpartyRelationship(vendorInfoVo.getPrincipalId(), partyId, PartyRelationshipTypeEnum.DEVELOPMENT_BY);
		/*
		 * // 插入PARTY_RELATIONSHIP 询价员
		 * insertpartyRelationship(vendorInfoVo.getEnquiryId(), partyId,
		 * PartyRelationshipTypeEnum.VENDOR_ENQUIRY_REL); //
		 * 插入PARTY_RELATIONSHIP 报价员
		 * insertpartyRelationship(vendorInfoVo.getOfferId(),partyId,
		 * PartyRelationshipTypeEnum.VENDOR_OFFER_REL);
		 */
		// 优化 询价员支持多个 add by helinmei
		String partyId1 = partyId;
		if (CollectionUtils.isNotEmpty(vendorInfoVo.getEnquiryList())) {
			vendorInfoVo.getEnquiryList().stream().forEach(enquiryId -> {
				// 插入PARTY_RELATIONSHIP 询价员
				insertpartyRelationship(enquiryId, partyId1, PartyRelationshipTypeEnum.VENDOR_ENQUIRY_REL);

			});
		}
		// 优化 报价员支持多个 add by helinmei
		if (CollectionUtils.isNotEmpty(vendorInfoVo.getOfferList())) {
			vendorInfoVo.getOfferList().stream().forEach(offerId -> {
				// 插入PARTY_RELATIONSHIP 报价员
				insertpartyRelationship(offerId, partyId1, PartyRelationshipTypeEnum.VENDOR_OFFER_REL);
			});
		}

		// 删除协议 PARTY_ATTRIBUTE
		List<String> list = new ArrayList<>();
		list.add(VENDOR_INFO_LEGALPERSON);// 公司法人
		list.add(VENDOR_INFO_REGPRICE); // 注册资金
		list.add(VENDOR_INFO_REGRADDRESS);// 注册地址
		list.add(VENDOR_INFO_EMPLOYEENUM);// 员工人数
		list.add(VENDOR_INFO_WEBSITE);// 供应商官网
		partyAttributeDao.delByPartyIdAndName(partyId, list);

		Map<String, String> map = vendorInfoVo.getVendorInfoAttributeMap();
		insertPartyAttribute(partyId, map);

		// 查找关键审批内容 对比是否需要走审核变更
		VendorInfoVo checkInfo = partySupplierDao.getCheckInfo(partyId);
		// 变更的内容
		CheckVendorInfoVo checkVendorInfoVo = new CheckVendorInfoVo();
		// 标记
		int sign = 0;

		if (null != checkInfo) {
			// 供应商全称
			if (StringUtils.isNotBlank(checkInfo.getGroupNameFull())) {
				if (!checkInfo.getGroupNameFull().equals(vendorInfoVo.getGroupNameFull())) {
					checkVendorInfoVo.setGroupNameFull(vendorInfoVo.getGroupNameFull());
					sign = 1;
				}
			} else {
				checkVendorInfoVo.setGroupNameFull(vendorInfoVo.getGroupNameFull());
				sign = 1;
			}

			// 供应商简称
			if (StringUtils.isNotBlank(checkInfo.getGroupName())) {
				if (!checkInfo.getGroupName().equals(vendorInfoVo.getGroupName())) {
					checkVendorInfoVo.setGroupName(vendorInfoVo.getGroupName());
					sign = 1;
				}
			} else {
				checkVendorInfoVo.setGroupName(vendorInfoVo.getGroupName());
				sign = 1;
			}

			// 供应商编码
			if (StringUtils.isNotBlank(checkInfo.getPartyCode())) {
				if (!checkInfo.getPartyCode().equals(vendorInfoVo.getPartyCode())) {
					checkVendorInfoVo.setNewPartyCode(vendorInfoVo.getPartyCode());
					sign = 1;
				}
			} else {
				checkVendorInfoVo.setNewPartyCode(vendorInfoVo.getPartyCode());
				sign = 1;
			}

			// 所属分类
			if (StringUtils.isNotBlank(checkInfo.getCategory())) {
				if (!checkInfo.getCategory().equals(vendorInfoVo.getCategory())) {
					checkVendorInfoVo.setCategory(vendorInfoVo.getCategory());
					sign = 1;
				}
			} else {
				checkVendorInfoVo.setCategory(vendorInfoVo.getCategory());
				sign = 1;
			}

			// 是否核心Y/N
			if (StringUtils.isNotBlank(checkInfo.getIsCore())) {
				if (!checkInfo.getIsCore().equals(vendorInfoVo.getIsCore())) {
					checkVendorInfoVo.setIsCore(vendorInfoVo.getIsCore());
					sign = 1;
				}
			} else {
				checkVendorInfoVo.setIsCore(vendorInfoVo.getIsCore());
				sign = 1;
			}
		}

		if (sign == 1) {
			try {

				// 判断是否在审核中
				try {
					ApplyVo applyVo = new ApplyVo();
					applyVo.setApplyOrgId(partyId);
					PageInfo<Apply> page = workflowClientBuilder.applyClient().queryApplyByEntity(ApplyVoType.PROCESS,
							ORG_SUPPLIER_INFO_CHANGE_REVIEW, applyVo, authorizationUtil.getLoginAuthorization());
					List<Apply> list1 = page.getList();
					if (CollectionUtils.isNotEmpty(list1)) {
						for (Apply apply : list1) {
							if (StringUtils.isNotBlank(apply.getStatus().name())
									&& "WAIT_APPROVE".equals(apply.getStatus().name())) {
								throw new BusinessException("该供应商ID" + partyId + "基本信息存在审核中的流程，请勿重复提交",
										"该供应商ID" + partyId + "基本信息存在审核中的流程，请勿重复提交");
							}

						}
					}

					PageInfo<Apply> page2 = workflowClientBuilder.applyClient().queryApplyByEntity(ApplyVoType.PROCESS,
							ORG_SUPPLIER_INVALID_REVIEW, applyVo, authorizationUtil.getLoginAuthorization());
					List<Apply> list2 = page2.getList();
					if (CollectionUtils.isNotEmpty(list2)) {
						for (Apply apply2 : list2) {
							if (StringUtils.isNotBlank(apply2.getStatus().name())
									&& "WAIT_APPROVE".equals(apply2.getStatus().name())) {
								throw new BusinessException("该供应商ID" + partyId + "失效审核正在审核中的流程，请勿提交变更信息",
										"该供应商ID" + partyId + "失效审核正在审核中的流程，请勿提交变更信息");
							}

						}
					}

				} catch (Exception e1) {
					throw new BusinessException("调用workflow服务判断是否在审核中时异常：{}", e1.getMessage());

				}

				Party p = personDao.getPersonByUserId(RequestHelper.getLoginUserId());
				if (null != p && null != p.getPerson()) {
					checkVendorInfoVo.setApplyName(p.getPerson().getLastNameLocal());
					checkVendorInfoVo.setApplyMail(p.getPerson().getMail());
					checkVendorInfoVo.setContactUserName(p.getPerson().getLastNameLocal());

				}
				PartySupplier pa = partySupplierDao.findCodeAndRegion(partyId);
				if (null != partySupplier) {
					checkVendorInfoVo.setPartyCode(pa.getCategory());
				}

				// 获取所有地区
				List<Category> regions = shipmentClientBuilder.categoryResource().categoryList("VENDOR_THE_REGION");
				if (null != partySupplier && CollectionUtils.isNotEmpty(regions)) {
					String regionStr = partySupplier.getRegion() != null ? partySupplier.getRegion() : "";
					// 设置地区
					for (Category region : regions) {
						if (regionStr.equals(region.getCategoryId())) {
							checkVendorInfoVo.setRegion(region.getCategoryName());
							break;
						}
					}
				}
				checkVendorInfoVo.setDescribe(vendorInfoVo.getDescribe());
				checkVendorInfoVo.setPartyId(partyId);

				DeptVo deptVo = partyGroupDao.findDeptInfo(partyId);
				checkVendorInfoVo.setName(deptVo.getNameFull());
				Apply apply = new Apply();
				JSONObject jsonObject = JSONObject.fromObject(checkVendorInfoVo);
				apply.setApplyContent(jsonObject.toString());
				apply.setApplyUserId(RequestHelper.getLoginUserId());
				apply.setProcessId(ORG_SUPPLIER_INFO_CHANGE_REVIEW);
				apply.setApplyOrgId(partyId);
				apply.setApplyPageUrl("");
				apply.setCallBackUrl("");
				apply.setReason(vendorInfoVo.getDescribe());
				workflowClientBuilder.applyClient().createApply(apply, authorizationUtil.getLoginAuthorization());
				try {
					sendMail.checkApply(deptVo.getName(), RoleTypeEnum.SUPPLIER_MANAGER,
							VendorApplyType.ORG_SUPPLIER_INFO_CHANGE_REVIEW, RequestHelper.getLoginUserId());
				} catch (Exception e) {
					logger.error("申请邮件发送时时异常：{}", e.getMessage());
				}
			} catch (Exception e) {
				logger.error("调用workflow服务异常：{}", e.getMessage());
				throw new BusinessException("调用workflow服务异常：{}", e.getMessage());
			}
		}
		return vendorInfoVo;
	}

	/*
	 * 供应商产品线【变更】
	 */
	@SuppressWarnings("rawtypes")
	@Audit(action = "Vendor recordqqq;;;'#partyProductLineVo.partyId'qqq;;;修改产品线信息    '#partyProductLineVo.describe'", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public PartyProductLineVo updatePartyProductLine(
			@Param(value = "partyProductLineVo") PartyProductLineVo partyProductLineVo) throws BusinessException {

		List<PartyProductLine> partyProductLineList = partyProductLineVo.getPartyProductLineList();

		String partyId = partyProductLineVo.getPartyId();

		// if(CollectionUtils.isEmpty(partyProductLineList)){
		// throw new BusinessException("不能传空值过来");
		// }

		if (StringUtils.isBlank(partyId)) {
			throw new BusinessException("供应商产品线变更，供应商partyId不能为空");
		}

		// 权限校验
		int s = 0;
		// 创建人
		Party party = partyGroupDao.findPartyGroupByPartyId(partyId);
		if (null!=party && StringUtils.isNotEmpty(party.getCreator()) && party.getCreator().equals(RequestHelper.getLoginUserId())) {
			s = 1;
		}

		if (s == 0) {
			// 授权人
			List<PartyRelationship> list3 = personDao.findPartyRelationship(partyId,
					PartyRelationshipTypeEnum.AGENT.name());
			if (CollectionUtils.isNotEmpty(list3)) {
				for (PartyRelationship p : list3) {
					if (p.getPartyIdFrom().equals(RequestHelper.getLoginUserId())) {
						s = 1;
						break;
					}
				}
			}
		}

		if (s == 0) {
			// 判断是否是超级管理员

			// 获取用户权限
			Set<String> set = aCLResource.getUserRoleList(RequestHelper.getLoginUserId());
			List<String> list1 = new ArrayList<String>(set);
			if (list1.contains("ADMIN")) {
				s = 1;
			}
		}
		if (s == 0) {
			throw new BusinessException("创建人可修改供应商所有信息，业务负责人和询价员可修改销售信息。", "当前用户无操作权限");
		}

		// 判断是否在审核中
		try {
			ApplyVo applyVo = new ApplyVo();
			applyVo.setApplyOrgId(partyId);
			PageInfo<Apply> page = workflowClientBuilder.applyClient().queryApplyByEntity(ApplyVoType.PROCESS,
					ORG_SUPPLIER_PRODUCTLINE_CHANGE_REVIEW, applyVo, authorizationUtil.getLoginAuthorization());
			List<Apply> list = page.getList();
			if (CollectionUtils.isNotEmpty(list)) {
				for (Apply apply : list) {
					if (StringUtils.isNotBlank(apply.getStatus().name())
							&& "WAIT_APPROVE".equals(apply.getStatus().name())) {
						throw new BusinessException("该供应商ID" + partyId + "产品线存在审核中的流程，请勿重复提交",
								"该供应商ID" + partyId + "产品线存在审核中的流程，请勿重复提交");
					}

				}
			}

			PageInfo<Apply> page2 = workflowClientBuilder.applyClient().queryApplyByEntity(ApplyVoType.PROCESS,
					ORG_SUPPLIER_INVALID_REVIEW, applyVo, authorizationUtil.getLoginAuthorization());
			List<Apply> list2 = page2.getList();
			if (CollectionUtils.isNotEmpty(list2)) {
				for (Apply apply2 : list2) {
					if (StringUtils.isNotBlank(apply2.getStatus().name())
							&& "WAIT_APPROVE".equals(apply2.getStatus().name())) {
						throw new BusinessException("该供应商ID" + partyId + "失效审核正在审核中的流程，请勿提交变更信息",
								"该供应商ID" + partyId + "失效审核正在审核中的流程，请勿提交变更信息");
					}

				}
			}

		} catch (Exception e1) {
			throw new BusinessException("调用workflow服务判断是否在审核中时异常：{}", e1.getMessage());

		}

		List<PartyProductLine> list = new ArrayList<>();
		// 查找已存在的产品线
		if (StringUtils.isNotBlank(partyId)) {
			PartyProductLine productLine = new PartyProductLine();
			productLine.setPartyId(partyId);
			list = partyProductLineDao.findByEntity(productLine);
		}
		// 新增的
		List<PartyProductLine> newList = new ArrayList<>();
		// 删除的
		List<PartyProductLine> oldList = new ArrayList<>();

		// 校验
		// 获取供应商列表
		Map<String, ProductBrand> aliasBrandMap = new HashMap<>();
		try {
			aliasBrandMap = productClientBuilder.brandResource()
					.getAliasBrandMap(authorizationUtil.getLoginAuthorization());
		} catch (Exception e) {
			throw new BusinessException("获取供应商列表服务异常：{}", e.getMessage());
		}

		// 获取分类层级信息

		// 查询全部分类
		List<ProductCategoryChild> allCategory;
		try {
			allCategory = productClientBuilder.categoryResource().getAllCategory(Collections.emptyList());
		} catch (Exception e) {
			throw new BusinessException("查询全部分类服务异常：{}", e.getMessage());
		}

		if (aliasBrandMap == null || aliasBrandMap.isEmpty()) {
			throw new BusinessException("获取供应商列表数据为空{}，无法校验数据是否存在", "获取供应商列表数据为空{}，无法校验数据是否存在");
		}
		if (CollectionUtils.isEmpty(allCategory)) {
			throw new BusinessException("查询全部分类数据为空{}，无法校验数据是否存在", "查询全部分类数据为空{}，无法校验数据是否存在");
		}

		int number = 1;

		if (CollectionUtils.isNotEmpty(partyProductLineList)) {
			// 校验传过来的数据是否正确
			for (PartyProductLine pa : partyProductLineList) {

				// 品牌ID
				String brandId = pa.getBrandId();
				// 品牌
				String brandName = pa.getBrandName();
				// 品牌标志
				String brandSign = N;
				// 大类ID
				String category1Id = StringUtils.isBlank(pa.getCategory1Id()) ? SIX_ZERO : pa.getCategory1Id();
				// 大类
				String category1Name = StringUtils.isBlank(pa.getCategory1Name()) ? "*" : pa.getCategory1Name();
				// 大类标志
				String category1Sign = N;
				// 小类ID
				String category2Id = StringUtils.isBlank(pa.getCategory2Id()) ? SIX_ZERO : pa.getCategory2Id();
				// 小类
				String category2Name = StringUtils.isBlank(pa.getCategory2Name()) ? "*" : pa.getCategory2Name();
				// 小类标志
				String category2Sign = N;
				// 次小类ID
				String category3Id = StringUtils.isBlank(pa.getCategory3Id()) ? SIX_ZERO : pa.getCategory3Id();
				// 次小类
				String category3Name = StringUtils.isBlank(pa.getCategory3Name()) ? "*" : pa.getCategory3Name();
				// 次小类标志
				String category3Sign = N;

				// 类校验使用标记 判断是否找到匹配
				int sign = 0;

				// 循环标记 跳出最外层
				int s1 = 0;

				if (StringUtils.isBlank(brandId) || StringUtils.isBlank(brandName)) {
					throw new BusinessException("产品线第" + number + "行的品牌ID和品牌名称必传", "产品线第" + number + "行的品牌ID和品牌名称必传");
				}

				// 校验品牌是否存在 有则brandId有值
				Iterator iter = aliasBrandMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					ProductBrand val = (ProductBrand) entry.getValue();
					if (brandName.equals(val.getBrandName())) {
						brandId = val.getId().toString();
						brandSign = N;
						break;
					} else {
						brandSign = Y;
					}
				}

				int brandNameSign = 0;
				// 校验品牌别名是否存在 有则brandId有值
				if (brandSign.equals(Y)) {
					// 校验品牌是否存在 有则brandId有值
					Iterator iter1 = aliasBrandMap.entrySet().iterator();
					while (iter1.hasNext()) {
						if (brandNameSign == 1) {
							break;
						}
						Map.Entry entry1 = (Map.Entry) iter1.next();
						ProductBrand val1 = (ProductBrand) entry1.getValue();
						List<String> brandAlias = val1.getBrandAlias();
						if (null != val1 && CollectionUtils.isNotEmpty(val1.getBrandAlias())) {
							for (String ss : val1.getBrandAlias()) {
								if (brandName.equals(ss)) {
									brandId = val1.getId().toString();
									brandName = val1.getBrandName();
									brandSign = N;
									brandNameSign = 1;
									break;
								} else {
									brandSign = Y;
								}
							}
						}
					}
				}

				if (brandSign.equals(N)) {
					if ("*".equals(category1Name) && "*".equals(category2Name) && "*".equals(category3Name)) {

					} else {
						if (sign == 0) {
							// 次小类检验
							if (CollectionUtils.isNotEmpty(allCategory)) {
								if (!"*".equals(category3Name)) {
									if (s == 0) {
										for (ProductCategoryChild p1 : allCategory) {
											if (CollectionUtils.isNotEmpty(p1.getChildren())) {
												if (s == 0) {
													for (ProductCategoryChild p2 : p1.getChildren()) {
														if (CollectionUtils.isNotEmpty(p2.getChildren())) {
															if (s == 0) {
																for (ProductCategoryChild p3 : p2.getChildren()) {
																	if (p3.getName().equals(category3Name)) {
																		sign = 1;
																		category3Sign = N;
																		category3Id = p3.getId().toString();
																		category2Id = p2.getId().toString();
																		category1Id = p1.getId().toString();
																		if (!"*".equals(category2Name) && !category2Name
																				.equals(p2.getName())) {
																			category2Sign = Y;
																		}
																		if (!"*".equals(category1Name) && !category1Name
																				.equals(p1.getName())) {
																			category1Sign = Y;
																		}
																		s = 1;
																		break;
																	} else {
																		category3Sign = Y;
																	}
																}
															}
														}
													}
												}
											}

										}
									}
								}
							}
						}

						s = 0;
						if (sign == 0 && category3Sign.equals(N)) {
							// 小类检验
							if (CollectionUtils.isNotEmpty(allCategory)) {
								if (!"*".equals(category2Name)) {
									if (s == 0) {
										for (ProductCategoryChild p1 : allCategory) {
											if (CollectionUtils.isNotEmpty(p1.getChildren())) {
												if (s == 0) {
													for (ProductCategoryChild p2 : p1.getChildren()) {
														if (p2.getName().equals(category2Name)) {
															sign = 1;
															category2Sign = N;
															category2Id = p2.getId().toString();
															category1Id = p1.getId().toString();
															if (!"*".equals(category1Name)
																	&& !category1Name.equals(p1.getName())) {
																category1Sign = Y;
															}
															s = 1;
															break;
														} else {
															category2Sign = Y;
														}
													}
												}
											}
										}
									}
								}
							}
						}

						if (sign == 0 && category3Sign.equals(N)) {
							// 大类检验
							if (CollectionUtils.isNotEmpty(allCategory)) {
								if (!"*".equals(category1Name)) {
									for (ProductCategoryChild p1 : allCategory) {
										if (p1.getName().equals(category1Name)) {
											sign = 1;
											category1Sign = N;
											category1Id = p1.getId().toString();
											break;
										} else {
											category1Sign = Y;
										}
									}
								}
							}
						}
					}
				}

				// 判断产品线是否正确
				if (brandSign.equals(Y) || category1Sign.equals(Y) || category2Sign.equals(Y)
						|| category3Sign.equals(Y)) {
					throw new BusinessException("产品线第" + number + "行数据有误，请传准确数据", "产品线第" + number + "行数据有误，请传准确数据");
				}
				number++;
			}
		}

		if (CollectionUtils.isNotEmpty(partyProductLineList)) {
			// 查找新增的
			for (PartyProductLine p : partyProductLineList) {
				if (CollectionUtils.isNotEmpty(list)) {
					// 标志
					int sign = 0;
					for (PartyProductLine l : list) {
						if (p.equals(l)) {
							sign = 1;
							break;
						}
					}
					if (sign == 0) {
						newList.add(p);
					}
				} else {
					newList.add(p);
				}
			}
		}
		// 查找删除的
		for (PartyProductLine l : list) {
			// 标志
			int sign = 0;
			if (CollectionUtils.isNotEmpty(partyProductLineList)) {
				for (PartyProductLine p : partyProductLineList) {
					if (p.equals(l)) {
						sign = 1;
						break;
					}
				}
			}
			if (sign == 0) {
				oldList.add(l);
			}
		}
		// 审核的全部内容
		List<PartyProductLine> selectList = new ArrayList<>();

		// 审核的新增内容
		if (CollectionUtils.isNotEmpty(newList)) {
			for (PartyProductLine nn : newList) {
				if (StringUtils.isBlank(nn.getPartyProductLineId())) {
					nn.setPartyProductLineId(String.valueOf(IdGen.getInstance().nextId()));
				}
				nn.setPartyId(partyId);
				nn.setSelect(Select.NEW);
				nn.setCreatedDate(null);
				nn.setLastUpdateDate(null);
				selectList.add(nn);
			}
		}

		// 审核的删除内容
		if (CollectionUtils.isNotEmpty(oldList)) {
			for (PartyProductLine oo : oldList) {
				if (StringUtils.isBlank(oo.getPartyProductLineId())) {
					oo.setPartyProductLineId(String.valueOf(IdGen.getInstance().nextId()));
				}
				oo.setPartyId(partyId);
				oo.setSelect(Select.DEL);
				oo.setCreatedDate(null);
				oo.setLastUpdateDate(null);
				selectList.add(oo);
			}
		}

		try {
			if (CollectionUtils.isNotEmpty(selectList)) {

				PartyProductLineVo vo = new PartyProductLineVo();
				vo.setPartyProductLineList(selectList);
				Party p = personDao.getPersonByUserId(RequestHelper.getLoginUserId());
				if (null != p && null != p.getPerson()) {
					vo.setApplyName(p.getPerson().getLastNameLocal());
					vo.setApplyMail(p.getPerson().getMail());
				}
				PartySupplier partySupplier = partySupplierDao.findCodeAndRegion(partyId);
				if (null != partySupplier) {
					vo.setPartyCode(partySupplier.getCategory());
				}
				// 获取所有地区
				List<Category> regions = shipmentClientBuilder.categoryResource().categoryList("VENDOR_THE_REGION");
				if (null != partySupplier && CollectionUtils.isNotEmpty(regions)) {
					String regionStr = partySupplier.getRegion() != null ? partySupplier.getRegion() : "";
					// 设置地区
					for (Category region : regions) {
						if (regionStr.equals(region.getCategoryId())) {
							vo.setRegion(region.getCategoryName());
							break;
						}
					}
				}
				vo.setDescribe(partyProductLineVo.getDescribe());
				vo.setPartyId(partyId);
				if (p != null && p.getPerson() != null) {
					vo.setContactUserName(p.getPerson().getLastNameLocal());
				}
				DeptVo deptVo = partyGroupDao.findDeptInfo(partyId);
				vo.setName(deptVo.getNameFull());
				Apply apply = new Apply();
				JSONObject jsonObject = JSONObject.fromObject(vo);
				apply.setApplyContent(jsonObject.toString());
				apply.setApplyUserId(RequestHelper.getLoginUserId());
				apply.setProcessId(ORG_SUPPLIER_PRODUCTLINE_CHANGE_REVIEW);
				apply.setApplyOrgId(partyId);
				apply.setApplyPageUrl("");
				apply.setCallBackUrl("");
				apply.setReason(partyProductLineVo.getDescribe());
				workflowClientBuilder.applyClient().createApply(apply, authorizationUtil.getLoginAuthorization());
				try {
					sendMail.checkApply(deptVo.getName(), RoleTypeEnum.SUPPLIER_MANAGER,
							VendorApplyType.ORG_SUPPLIER_PRODUCTLINE_CHANGE_REVIEW, RequestHelper.getLoginUserId());
				} catch (Exception e) {
					logger.error("申请邮件发送时时异常：{}", e.getMessage());
				}
			}
		} catch (Exception e) {
			logger.error("调用workflow服务异常：{}", e.getMessage());
			throw new BusinessException("调用workflow服务异常：{}", e.getMessage());
		}
		return partyProductLineVo;
	}

	/*
	 * 供应商信用信息【变更】
	 */
	@Audit(action = "Vendor recordqqq;;;'#vendorCreditVo.partyId'qqq;;;修改信用信息    '#vendorCreditVo.describe'", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public VendorCreditVo updateVendorCreditVo(@Param(value = "vendorCreditVo") VendorCreditVo vendorCreditVo)
			throws BusinessException {
		String partyId = vendorCreditVo.getPartyId();
		if (StringUtils.isBlank(partyId)) {
			throw new BusinessException("partyId不能为空", "partyId不能为空");
		}

		// 权限校验
		int s = 0;
		// 创建人
		Party party = partyGroupDao.findPartyGroupByPartyId(partyId);
		if (StringUtils.isNotEmpty(party.getCreator()) && party.getCreator().equals(RequestHelper.getLoginUserId())) {
			s = 1;
		}

		if (s == 0) {
			// 授权人
			List<PartyRelationship> list3 = personDao.findPartyRelationship(partyId,
					PartyRelationshipTypeEnum.AGENT.name());
			if (CollectionUtils.isNotEmpty(list3)) {
				for (PartyRelationship p : list3) {
					if (p.getPartyIdFrom().equals(RequestHelper.getLoginUserId())) {
						s = 1;
						break;
					}
				}
			}
		}

		if (s == 0) {
			// 判断是否是超级管理员

			// 获取用户权限
			Set<String> set = aCLResource.getUserRoleList(RequestHelper.getLoginUserId());
			List<String> list1 = new ArrayList<String>(set);
			if (list1.contains("ADMIN")) {
				s = 1;
			}
		}
		if (s == 0) {
			throw new BusinessException("创建人可修改供应商所有信息，业务负责人和询价员可修改销售信息。", "当前用户无操作权限");
		}

		// 更新PARTY_CREDIT 供应商账期
		VendorCreditVo partyCredit = new VendorCreditVo();
		partyCredit.setPartyId(partyId);
		/* 信用额度币种 */
		partyCredit.setCurrency(vendorCreditVo.getCurrency());
		/* 授信额度 */
		partyCredit.setCreditQuota(vendorCreditVo.getCreditQuota());
		/* 授信余额 */
		partyCredit.setRealtimeCreditQuota(vendorCreditVo.getRealtimeCreditQuota());
		/* 授信期限（月结xxx天） */
		partyCredit.setCreditDeadline(vendorCreditVo.getCreditDeadline());
		/* 对账日期（纯数字，代表每月几号） */
		partyCredit.setCheckDate(vendorCreditVo.getCheckDate());
		/* 对账周期 */
		if (StringUtils.isBlank(vendorCreditVo.getCheckCycle())) {
			partyCredit.setCheckCycle("30");
		} else {
			partyCredit.setCheckCycle(vendorCreditVo.getCheckCycle());
		}
		/* 付款日期（纯数字，代表每月几号） */
		partyCredit.setPayDate(vendorCreditVo.getPayDate());

		/* 付款方式 付款条件 */
		partyCredit.setPaymentTerms(vendorCreditVo.getPaymentTerms());

		/* 结算方式 */
		partyCredit.setSettlementMethod(vendorCreditVo.getSettlementMethod());

		partyCredit.setLastUpdateUser(RequestHelper.getLoginUserId());
		partyCredit.setLastUpdateDate(new Date());

		PartyCredit partyCredit2 = partyCreditDao.getPartyCredit(partyId, null);

		if (null != partyCredit2) {
			partyCreditDao.updateByPartyId(partyCredit);
		} else {
			insertPartyCredit(partyId, partyCredit);
		}

		// 删除PARTY_BANK_ACCOUNT 银行资料
		partyBankAccountDao.delByPartyId(partyId);
		// 插入PARTY_BANK_ACCOUNT 银行资料
		insertPartyBankAccount(partyId, vendorCreditVo.getPartyBankAccount());

		// 删除 PARTY_ATTACHMENT 供应商附件表
		partyCreditDao.delByPartyId(partyId, AttachmentType.VENDOR_CREDIT.name());

		// 插入PARTY_ATTACHMENT 供应商附件表
		insertPartyCreditAttachment(partyId, vendorCreditVo.getCreditAttachmentList());

		// 删除协议 PARTY_ATTRIBUTE
		List<String> list = new ArrayList<>();
		list.add(VENDOR_CREDIT_PURCHASEDEAL);// 采购协议
		list.add(VENDOR_CREDIT_PURCHASEDEALDATE);// 采购协议有效期
		list.add(VENDOR_CREDIT_SECRECYPROTOCOL);// 保密协议
		list.add(VENDOR_CREDIT_SECRECYPROTOCOLDATE);// 保密协议有效期
		partyAttributeDao.delByPartyIdAndName(partyId, list);

		Map<String, String> map = vendorCreditVo.getCreditAttributeMap();
		insertPartyAttribute(partyId, map);
		return vendorCreditVo;
	}

	/*
	 * 供应商编辑销售信息【变更】
	 */
	@Audit(action = "Vendor recordqqq;;;'#vendorSaleInfoVo.partyId'qqq;;;修改销售信息    '#vendorSaleInfoVo.describe'", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public VendorSaleInfoVo updateVendorSaleInfoVo(@Param(value = "vendorSaleInfoVo") VendorSaleInfoVo vendorSaleInfoVo)
			throws BusinessException {

		String partyId = vendorSaleInfoVo.getPartyId();
		if (StringUtils.isBlank(partyId)) {
			throw new BusinessException("partyId不能为空", "partyId不能为空");
		}
		// 权限校验
		int s = 0;
		// 是否为报价员 0否1是 是的话无法修改联系人
		int offer = 0;
		// 创建人
		Party party = partyGroupDao.findPartyGroupByPartyId(partyId);
		if (StringUtils.isNotEmpty(party.getCreator()) && party.getCreator().equals(RequestHelper.getLoginUserId())) {
			s = 1;
		}

		if (s == 0) {
			// 负责人
			List<PartyRelationship> list1 = personDao.findPartyRelationship(partyId,
					PartyRelationshipTypeEnum.DEVELOPMENT_BY.name());
			if (CollectionUtils.isNotEmpty(list1)) {
				for (PartyRelationship p : list1) {
					if (p.getPartyIdFrom().equals(RequestHelper.getLoginUserId())) {
						s = 1;
						break;
					}
				}
			}
		}
		if (s == 0) {
			// 询价员
			List<PartyRelationship> list2 = personDao.findPartyRelationship(partyId,
					PartyRelationshipTypeEnum.VENDOR_ENQUIRY_REL.name());
			if (CollectionUtils.isNotEmpty(list2)) {
				for (PartyRelationship p : list2) {
					if (p.getPartyIdFrom().equals(RequestHelper.getLoginUserId())) {
						s = 1;
						break;
					}
				}
			}
		}

		if (s == 0) {
			// 授权人
			List<PartyRelationship> list3 = personDao.findPartyRelationship(partyId,
					PartyRelationshipTypeEnum.AGENT.name());
			if (CollectionUtils.isNotEmpty(list3)) {
				for (PartyRelationship p : list3) {
					if (p.getPartyIdFrom().equals(RequestHelper.getLoginUserId())) {
						s = 1;
						break;
					}
				}
			}
		}

		if (s == 0) {
			// 判断是否是超级管理员

			// 获取用户权限
			Set<String> set = aCLResource.getUserRoleList(RequestHelper.getLoginUserId());
			List<String> list1 = new ArrayList<String>(set);
			if (list1.contains("ADMIN")) {
				s = 1;
			}
		}

		if (s == 0) {
			// 报价员
			List<PartyRelationship> list4 = personDao.findPartyRelationship(partyId,
					PartyRelationshipTypeEnum.VENDOR_OFFER_REL.name());
			if (CollectionUtils.isNotEmpty(list4)) {
				for (PartyRelationship p : list4) {
					if (p.getPartyIdFrom().equals(RequestHelper.getLoginUserId())) {
						s = 1;
						offer = 1;
						break;
					}
				}
			}
		}

		if (s == 0) {
			throw new BusinessException("创建人可修改供应商所有信息，业务负责人和询价员可修改销售信息。", "当前用户无操作权限");
		}

		// 更新PARTY_SUPPLIER 供应商属性表1
		VendorSaleInfoVo vendorS = new VendorSaleInfoVo();
		vendorS.setPartyId(partyId);
		/* 是否启用供应商MOV策略(Y/N) */
		vendorS.setVendorMovStatus(vendorSaleInfoVo.getVendorMovStatus());
		/* 是否启用供应商SKU的MOV策略(Y/N) */
		vendorS.setSkuMovStatus(vendorSaleInfoVo.getSkuMovStatus());
		/* 是否显示名称(Y/N) */
		vendorS.setIsShowName(vendorSaleInfoVo.getIsShowName());
		vendorS.setOrderVerify(vendorSaleInfoVo.getOrderVerify());
		vendorS.setSupportCurrency(vendorSaleInfoVo.getSupportCurrency());
		vendorS.setDescription(vendorSaleInfoVo.getDescription());
		vendorS.setMinOrderPriceCNY(vendorSaleInfoVo.getMinOrderPriceCNY());
		vendorS.setMinOrderPriceUSD(vendorSaleInfoVo.getMinOrderPriceUSD());
		vendorS.setLastUpdateUser(RequestHelper.getLoginUserId());
		vendorS.setLastUpdateDate(new Date());
		vendorS.setProductInvalidDays(vendorSaleInfoVo.getProductInvalidDays());
		vendorS.setIsAutoIntegrateQty(vendorSaleInfoVo.getIsAutoIntegrateQty());
		partySupplierDao.updateVendorSaleInfoVo(vendorS);

		// 删除PARTY_RELATIONSHIP 负责人
		personDao.delByPartyIdTo(partyId, PartyRelationshipTypeEnum.DEVELOPMENT_BY.name());

		// 删除PARTY_RELATIONSHIP 询价员
		personDao.delByPartyIdTo(partyId, PartyRelationshipTypeEnum.VENDOR_ENQUIRY_REL.name());

		// 删除PARTY_RELATIONSHIP 报价员
		personDao.delByPartyIdTo(partyId, PartyRelationshipTypeEnum.VENDOR_OFFER_REL.name());

		// 插入PARTY_RELATIONSHIP 负责人
		if (StringUtils.isNotBlank(vendorSaleInfoVo.getPrincipalId())) {
			insertpartyRelationship(vendorSaleInfoVo.getPrincipalId(), partyId,
					PartyRelationshipTypeEnum.DEVELOPMENT_BY);
		}
		/*
		 * // 插入PARTY_RELATIONSHIP 询价员
		 * if(StringUtils.isNotBlank(vendorSaleInfoVo.getEnquiryId())){
		 * insertpartyRelationship(vendorSaleInfoVo.getEnquiryId(), partyId,
		 * PartyRelationshipTypeEnum.VENDOR_ENQUIRY_REL); } //
		 * 插入PARTY_RELATIONSHIP 报价员
		 * if(StringUtils.isNotBlank(vendorSaleInfoVo.getOfferId())){
		 * insertpartyRelationship(vendorSaleInfoVo.getOfferId(), partyId,
		 * PartyRelationshipTypeEnum.VENDOR_OFFER_REL); }
		 */
		// 优化 询价员支持多个 add by helinmei
		String partyId1 = partyId;
		if (CollectionUtils.isNotEmpty(vendorSaleInfoVo.getEnquiryList())) {
			HashSet<String> tempset = new HashSet<>(vendorSaleInfoVo.getEnquiryList());
			tempset.stream().forEach(enquiryId -> {
				// 插入PARTY_RELATIONSHIP 询价员
				insertpartyRelationship(enquiryId, partyId1, PartyRelationshipTypeEnum.VENDOR_ENQUIRY_REL);

			});
		}
		// 优化 报价员支持多个 add by helinmei
		if (CollectionUtils.isNotEmpty(vendorSaleInfoVo.getOfferList())) {
			HashSet<String> tempset = new HashSet<>(vendorSaleInfoVo.getOfferList());
			tempset.stream().forEach(offerId -> {
				// 插入PARTY_RELATIONSHIP 报价员
				insertpartyRelationship(offerId, partyId1, PartyRelationshipTypeEnum.VENDOR_OFFER_REL);
			});
		}
		// 删除协议 PARTY_ATTRIBUTE
		List<String> list = new ArrayList<>();
		list.add(VENDOR_SALE_INFOVO_FOCUSFIELDS);// 关注领域
		list.add(VENDOR_SALE_INFOVO_PRODUCTCATEGORYS);// 优势产品类别
		list.add(VENDOR_SALE_INFOVO_MAJORCLIENTS);// 主要客户
		partyAttributeDao.delByPartyIdAndName(partyId, list);

		Map<String, String> map = vendorSaleInfoVo.getSaleAttributeMap();
		insertPartyAttribute(partyId, map);

		if (offer == 0) {
			// 查找PARTY_RELATIONSHIP 供应商绑定的联系人
			List<PartyRelationship> findPartyRelationship = personDao.findPartyRelationship(partyId,
					PartyRelationshipTypeEnum.USER_DEPT_REL.name());
			if (null != findPartyRelationship && CollectionUtils.isNotEmpty(findPartyRelationship)) {
				for (PartyRelationship p : findPartyRelationship) {
					// 删除 PARTY_RELATIONSHIP 供应商联系人绑定的产品线Id
					personDao.delByPartyIdFrom(p.getPartyIdFrom(),
							PartyRelationshipTypeEnum.USER_PRODUCTLINE_REL.name());
					// 删除 Person
					personDao.del(p.getPartyIdFrom());
				}

			}
			// 删除 PARTY_RELATIONSHIP 供应商的联系人 关系表
			personDao.delContactPersonInfo(partyId, PartyRelationshipTypeEnum.USER_DEPT_REL.name());

			// 插入PERSON 供应商联系人
			insertContactPersonInfo(partyId, vendorSaleInfoVo.getContactPersonInfoList());
		}

		// 删除FACILITY 供应商仓库
		Facility facility = new Facility();
		facility.setOwnerPartyId(partyId);

		// facilityDao.delete(facility);
		// 插入FACILITY 供应商仓库
		insertFacility(partyId, vendorSaleInfoVo.getFacilityList());

		return vendorSaleInfoVo;
	}

	// 启动或失效 审核申请
	@Audit(action = "Vendor recordqqq;;;'#check.partyId'qqq;;;更改'#check.startLose'状态信息    '#check.describe'", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public CheckStartOrLose startOrLose(String partyId, StartOrLose startOrLose, String describe,
			@Param(value = "check") CheckStartOrLose check) throws BusinessException {

		if (StringUtils.isBlank(describe)) {
			check.setDescribe(" ");
		} else {
			check.setDescribe(describe);
		}
		check.setPartyId(partyId);
		if (startOrLose.name().equals(StartOrLose.START.name())) {
			check.setStartLose("启动");
		} else if (startOrLose.name().equals(StartOrLose.LOSE.name())) {
			check.setStartLose("失效");
		}
		// 权限校验
		int s = 0;
		// 创建人
		Party party = partyGroupDao.findPartyGroupByPartyId(partyId);
		if (StringUtils.isNotEmpty(party.getCreator()) && party.getCreator().equals(RequestHelper.getLoginUserId())) {
			s = 1;
		}
		if (s == 0) {
			// 授权人
			List<PartyRelationship> list3 = personDao.findPartyRelationship(partyId,
					PartyRelationshipTypeEnum.AGENT.name());
			if (CollectionUtils.isNotEmpty(list3)) {
				for (PartyRelationship p : list3) {
					if (p.getPartyIdFrom().equals(RequestHelper.getLoginUserId())) {
						s = 1;
						break;
					}
				}
			}
		}

		if (s == 0) {
			// 判断是否是超级管理员

			// 获取用户权限
			Set<String> set = aCLResource.getUserRoleList(RequestHelper.getLoginUserId());
			List<String> list1 = new ArrayList<String>(set);
			if (list1.contains("ADMIN")) {
				s = 1;
			}
		}

		if (s == 0) {
			throw new BusinessException("启动或失效必须为创建人操作", "当前用户无操作权限");
		}
		if (startOrLose == StartOrLose.LOSE) {
			// 判断是否全部商品已经下架
			Map<String, Object> map = productClientBuilder.searchResource().search(null, partyId, null, null, null, 1,
					1,null, null);
			if (null != map) {
				Object object = map.get("productInfo");
				if (null != object && object.toString().length() != 2) {
					throw new BusinessException("该商品没有全部下架,请下架后再操作", "该商品没有全部下架,请下架后再操作");
				}
			}

		}

		PartyGroup partyGroup = partyGroupDao.findStatus(partyId);
		if (null != partyGroup && null != partyGroup.getActiveStatus()) {
			if (partyGroup.getActiveStatus().name().equals(startOrLose.name())) {
				throw new BusinessException("不能更改为同一状态", "不能更改为同一状态");
			}
		}

		// 判断是否在审核中 启动和失效
		try {
			ApplyVo applyVo = new ApplyVo();
			applyVo.setApplyOrgId(partyId);
			PageInfo<Apply> page1 = workflowClientBuilder.applyClient().queryApplyByEntity(ApplyVoType.PROCESS,
					ORG_SUPPLIER_ENABLED_REVIEW, applyVo, authorizationUtil.getLoginAuthorization());

			if (null != page1 && CollectionUtils.isNotEmpty(page1.getList())) {
				List<Apply> list1 = page1.getList();
				for (Apply apply : list1) {
					if (StringUtils.isNotBlank(apply.getStatus().name())
							&& "WAIT_APPROVE".equals(apply.getStatus().name())) {
						throw new BusinessException("该供应商ID" + partyId + "启用审核正在审核中的流程，请勿重复提交",
								"该供应商ID" + partyId + "启用审核正在审核中的流程，请勿重复提交");
					}

				}
			}

			PageInfo<Apply> page2 = workflowClientBuilder.applyClient().queryApplyByEntity(ApplyVoType.PROCESS,
					ORG_SUPPLIER_INVALID_REVIEW, applyVo, authorizationUtil.getLoginAuthorization());

			if (null != page2 && CollectionUtils.isNotEmpty(page2.getList())) {
				List<Apply> list2 = page2.getList();
				for (Apply apply2 : list2) {
					if (StringUtils.isNotBlank(apply2.getStatus().name())
							&& "WAIT_APPROVE".equals(apply2.getStatus().name())) {
						throw new BusinessException("该供应商ID" + partyId + "失效审核正在审核中的流程，请勿重复提交",
								"该供应商ID" + partyId + "失效审核正在审核中的流程，请勿重复提交");
					}

				}
			}

			// 判断是否在建档审核中
			PageInfo<Apply> page3 = workflowClientBuilder.applyClient().queryApplyByEntity(ApplyVoType.PROCESS,
					ORG_SUPPLIER_ARCHIVES_REVIEW, applyVo, authorizationUtil.getLoginAuthorization());
			if (null != page3 && CollectionUtils.isNotEmpty(page3.getList())) {
				List<Apply> list3 = page3.getList();
				for (Apply apply3 : list3) {
					if (StringUtils.isNotBlank(apply3.getStatus().name())
							&& "WAIT_APPROVE".equals(apply3.getStatus().name())) {
						throw new BusinessException("该供应商ID" + partyId + "建档审核正在审核中的流程，请勿提交",
								"该供应商ID" + partyId + "建档审核正在审核中的流程，请勿提交");
					}

				}
			}

		} catch (Exception e1) {
			throw new BusinessException("调用workflow服务判断是否在审核中时异常：{}", e1.getMessage());

		}

		CheckStartOrLose checkStartOrLose = new CheckStartOrLose();
		try {
			Party p = personDao.getPersonByUserId(RequestHelper.getLoginUserId());
			if (null != p && null != p.getPerson()) {
				checkStartOrLose.setApplyName(p.getPerson().getLastNameLocal());
				checkStartOrLose.setApplyMail(p.getPerson().getMail());
				checkStartOrLose.setContactUserName(p.getPerson().getLastNameLocal());
			}
			PartySupplier pa = partySupplierDao.findCodeAndRegion(partyId);
			if (null != pa) {
				checkStartOrLose.setPartyCode(pa.getCategory());
			}
			// 获取所有地区
			List<Category> regions = shipmentClientBuilder.categoryResource().categoryList("VENDOR_THE_REGION");
			if (null != pa && CollectionUtils.isNotEmpty(regions)) {
				String regionStr = pa.getRegion() != null ? pa.getRegion() : "";
				// 设置地区
				for (Category region : regions) {
					if (regionStr.equals(region.getCategoryId())) {
						checkStartOrLose.setRegion(region.getCategoryName());
						break;
					}
				}
			}
			checkStartOrLose.setDescribe(describe);
			checkStartOrLose.setPartyId(partyId);
			checkStartOrLose.setStartOrLose(startOrLose);
			DeptVo deptVo = partyGroupDao.findDeptInfo(partyId);
			checkStartOrLose.setName(deptVo.getNameFull());

			Apply apply = new Apply();
			JSONObject jsonObject = JSONObject.fromObject(checkStartOrLose);
			apply.setApplyContent(jsonObject.toString());
			apply.setApplyUserId(RequestHelper.getLoginUserId());
			if (startOrLose.name().equals(StartOrLose.START.name())) {
				apply.setProcessId(ORG_SUPPLIER_ENABLED_REVIEW);
			} else if (startOrLose.name().equals(StartOrLose.LOSE.name())) {
				apply.setProcessId(ORG_SUPPLIER_INVALID_REVIEW);
			}
			apply.setApplyOrgId(partyId);
			apply.setApplyPageUrl("");
			apply.setCallBackUrl("");
			apply.setReason(describe);
			workflowClientBuilder.applyClient().createApply(apply, authorizationUtil.getLoginAuthorization());
			try {
				if (startOrLose.name().equals(StartOrLose.START.name())) {
					sendMail.checkApply(deptVo.getName(), RoleTypeEnum.SUPPLIER_MANAGER,
							VendorApplyType.ORG_SUPPLIER_ENABLED_REVIEW, RequestHelper.getLoginUserId());
				} else if (startOrLose.name().equals(StartOrLose.LOSE.name())) {
					sendMail.checkApply(deptVo.getName(), RoleTypeEnum.SUPPLIER_MANAGER,
							VendorApplyType.ORG_SUPPLIER_INVALID_REVIEW, RequestHelper.getLoginUserId());
				}
			} catch (Exception e) {
				logger.error("申请邮件发送时时异常：{}", e.getMessage());
			}
		} catch (Exception e) {
			logger.error("调用workflow服务异常：{}", e.getMessage());
			throw new BusinessException("调用workflow服务异常：{}", e.getMessage());
		}
		return checkStartOrLose;

	}

	// PartyAttribute 组装属性
	public PartyAttribute getPartyAttribute(String partyId, String key, String value) {
		PartyAttribute partyAttribute = new PartyAttribute();
		partyAttribute.setPartyId(partyId);
		partyAttribute.setKey(key);
		partyAttribute.setValue(value);
		partyAttribute.setCreator(RequestHelper.getLoginUserId());
		partyAttribute.setCreatedDate(new Date());
		partyAttribute.setLastUpdateUser(RequestHelper.getLoginUserId());
		partyAttribute.setLastUpdateDate(new Date());
		return partyAttribute;
	}

	/**
	 * 查询供管理应商列表
	 * 
	 * @param vendorResponVo
	 * @return
	 * @since 2017年8月17日
	 * @author zr.zhanghua@yikuyi.com
	 */
	public PageInfo<VendorResponVo> getVendorManageList(VendorQueryVo vendorQueryVo, RowBounds rowBounds) {
		String userId = RequestHelper.getLoginUserId();
		// 获取用户权限
		Set<String> set = aCLResource.getUserRoleList(userId);
		List<String> list1 = new ArrayList<String>(set);
		// 判断是否是超级管理员
		// 超级管理员查询所有数据不是超级管理员查询授权的相关数据
		if (list1.contains("ADMIN")) {
			vendorQueryVo.setIsSys(Y);
		} else {
			List<String> partyIds = vendorsManager.getVendorPartyIds(userId);
			vendorQueryVo.setPartyIds(partyIds);

			// 根据创建者查询
			List<String> createrPartyIds = new ArrayList<>();
			createrPartyIds.add(userId);
			// 如果是主管可以查询部门下所有的创建者
			List<String> createrPartyIdList = vendorsManager.deptPartyIds(userId);
			if (CollectionUtils.isNotEmpty(createrPartyIdList)) {
				createrPartyIds.addAll(createrPartyIdList);
			}
			vendorQueryVo.setCreaterPartyIds(createrPartyIds);
		}

		PageInfo<VendorResponVo> result = new PageInfo<>(vendorDao.getVendorManageList(vendorQueryVo, rowBounds));
		// 获取所有分类
		List<Category> categorys = shipmentClientBuilder.categoryResource().categoryList("VENDOR_THE_TYPE");
		// 获取所有地区
		List<Category> regions = shipmentClientBuilder.categoryResource().categoryList("VENDOR_THE_REGION");

		List<VendorResponVo> target = result.getList();
		// 设置分类
		for (VendorResponVo vendorResponVo : target) {
			for (Category category : categorys) {
				String categoryStr = vendorResponVo.getCategory() != null ? vendorResponVo.getCategory() : "";
				if (categoryStr.equals(category.getCategoryId())) {
					vendorResponVo.setCategory(category.getCategoryName());
					break;
				}
			}
		}
		// 设置地区
		for (VendorResponVo vendorResponVo : target) {
			for (Category region : regions) {
				String regionStr = vendorResponVo.getRegion() != null ? vendorResponVo.getRegion() : "";
				if (regionStr.equals(region.getCategoryId())) {
					vendorResponVo.setRegion(region.getCategoryName());
					break;
				}
			}
		}
		// 查询审核状态
		ApplyExtendsVo applyExtendsVo = new ApplyExtendsVo();
		List<String> partyIds = new ArrayList<>();

		applyExtendsVo.setApplyOrgIdList(partyIds);
		List<ApplyExtendsVo> applyList = workflowClientBuilder.applyClient().listApplyByVo(applyExtendsVo,
				authorizationUtil.getLoginAuthorization());

		// 设置默认审核状态为不在审核中 N
		// 设置审核状态
		if (null != applyList && CollectionUtils.isNotEmpty(applyList)) {
			for (ApplyExtendsVo applyExtendsVo2 : applyList) {
				for (VendorResponVo vendorResponVo : target) {
					if (vendorResponVo.getPartyId().equals(applyExtendsVo2.getApplyOrgId())) {
						vendorResponVo.setApplyStatus(Y);
					}
				}
			}
		}
		result.setList(target);
		return result;
	}

	/**
	 * 查询销售商管理列表
	 * 
	 * @param vendorResponVo
	 * @return
	 * @since 2017年8月17日
	 * @author zr.zhanghua@yikuyi.com
	 */
	public PageInfo<VendorResponVo> getSellManageList(VendorQueryVo vendorQueryVo, RowBounds rowBounds) {
		String userId = RequestHelper.getLoginUserId();
		// 获取用户权限
		Set<String> set = aCLResource.getUserRoleList(userId);
		List<String> list1 = new ArrayList<String>(set);
		// 判断是否是超级管理员
		// 超级管理员查询所有数据不是超级管理员查询授权的相关数据
		if (list1.contains("ADMIN") || list1.contains("COO") || list1.contains("OPERATION_MANAGER")
				|| list1.contains("CEO")) {
			vendorQueryVo.setIsSys(Y);
		} else {
			List<String> partyIds = vendorsManager.getSellPartyIds(userId);
			vendorQueryVo.setPartyIds(partyIds);

			// 根据创建者查询
			List<String> createrPartyIds = new ArrayList<>();
			createrPartyIds.add(userId);
			// 如果是主管可以查询部门下所有的创建者
			List<String> createrPartyIdList = vendorsManager.deptPartyIds(userId);
			if (CollectionUtils.isNotEmpty(createrPartyIdList)) {
				createrPartyIds.addAll(createrPartyIdList);
			}
			vendorQueryVo.setCreaterPartyIds(createrPartyIds);
		}

		PageInfo<VendorResponVo> result = new PageInfo<>(vendorDao.getSellManageList(vendorQueryVo, rowBounds));
		// 获取所有分类
		List<Category> categorys = shipmentClientBuilder.categoryResource().categoryList("VENDOR_THE_TYPE");
		// 获取所有地区
		List<Category> regions = shipmentClientBuilder.categoryResource().categoryList("VENDOR_THE_REGION");

		List<VendorResponVo> target = result.getList();
		// 设置分类
		for (VendorResponVo vendorResponVo : target) {
			for (Category category : categorys) {
				String categoryStr = vendorResponVo.getCategory() != null ? vendorResponVo.getCategory() : "";
				if (categoryStr.equals(category.getCategoryId())) {
					vendorResponVo.setCategory(category.getCategoryName());
					break;
				}
			}
		}
		// 设置地区
		for (VendorResponVo vendorResponVo : target) {
			for (Category region : regions) {
				String regionStr = vendorResponVo.getRegion() != null ? vendorResponVo.getRegion() : "";
				if (regionStr.equals(region.getCategoryId())) {
					vendorResponVo.setRegion(region.getCategoryName());
					break;
				}
			}
		}
		// 查询审核状态
		ApplyExtendsVo applyExtendsVo = new ApplyExtendsVo();
		List<String> partyIds = new ArrayList<>();

		applyExtendsVo.setApplyOrgIdList(partyIds);
		List<ApplyExtendsVo> applyList = workflowClientBuilder.applyClient().listApplyByVo(applyExtendsVo,
				authorizationUtil.getLoginAuthorization());

		// 设置默认审核状态为不在审核中 N
		// 设置审核状态
		if (null != applyList && CollectionUtils.isNotEmpty(applyList)) {
			for (ApplyExtendsVo applyExtendsVo2 : applyList) {
				for (VendorResponVo vendorResponVo : target) {
					if (vendorResponVo.getPartyId().equals(applyExtendsVo2.getApplyOrgId())) {
						vendorResponVo.setApplyStatus(Y);
					}
				}
			}
		}
		result.setList(target);
		return result;
	}

	/**
	 * 查询供应商信息
	 * 
	 * @param partyIds
	 * @return
	 * @since 2017年8月22日
	 * @author zr.zhanghua@yikuyi.com
	 */
	public List<PartySupplier> getOrderVerify(List<String> partyIds) {
		return partySupplierDao.getOrderVerify(partyIds);
	}

	/**
	 * 供应商列表失效启用操作
	 * 
	 * @param commd
	 * @return
	 * @since 2017年8月22日
	 * @author zr.zhanghua@yikuyi.com
	 */
	public void enableOrlose(Party party) {
		partyDao.updateParty(party);
	}

	/**
	 * 供应商建档信息 审核通过保存
	 * 
	 * @param vendorInfo
	 * @since 2017年8月28日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	public void editVendorApplySave(Apply apply) throws BusinessException {
		if (null == apply) {
			throw new BusinessException("空数据", "空数据");
		}
		try {
			if (ApplyStatus.APPROVED.name().equals(apply.getStatus().name())) {
				String partyId = apply.getApplyOrgId();
				if (StringUtils.isBlank(partyId)) {
					throw new BusinessException("apply.getApplyOrgId()为空值", "apply.getApplyOrgId()为空值");
				}
				PartyGroup partyGroup = new PartyGroup();
				partyGroup.setPartyId(partyId);
				partyGroup.setAccountStatus(AccountStatus.VALID);
				partyGroup.setActiveStatus(ActiveStatus.START);
				partyGroup.setLastUpdateUser(RequestHelper.getLoginUserId());
				partyGroup.setLastUpdateDate(new Date());
				partyGroupDao.updateStatus(partyGroup);

				// 发送通过邮件
				sendMail.newPass(apply);
			} else if (ApplyStatus.REJECT.name().equals(apply.getStatus().name())) {
				// 发送不通过邮件
				sendMail.newNotPass(apply);
			}
		} catch (Exception e) {
			throw new BusinessException("供应商建档出错:{}", e.getMessage());
		}
	}

	/**
	 * 供应商启动通过保存
	 * 
	 * @param vendorInfo
	 * @since 2017年8月28日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	public void editStartApplySave(Apply apply) throws BusinessException {
		if (null == apply) {
			throw new BusinessException("空数据", "空数据");
		}
		try {
			if (ApplyStatus.APPROVED.name().equals(apply.getStatus().name())) {
				String partyId = apply.getApplyOrgId();
				if (StringUtils.isBlank(partyId)) {
					throw new BusinessException("apply.getApplyOrgId()为空值", "apply.getApplyOrgId()为空值");
				}
				PartyGroup partyGroup = new PartyGroup();
				partyGroup.setPartyId(partyId);
				partyGroup.setAccountStatus(AccountStatus.VALID);
				partyGroup.setActiveStatus(ActiveStatus.START);
				partyGroup.setLastUpdateUser(RequestHelper.getLoginUserId());
				partyGroup.setLastUpdateDate(new Date());
				partyGroupDao.updateStatus(partyGroup);

				// 发送通过邮件
				sendMail.startPass(apply);
			} else {
				// 发送不通过邮件
				sendMail.startNotPass(apply);
			}
		} catch (Exception e) {
			throw new BusinessException("供应商启用{} 出错", e.getMessage());
		}
	}

	/**
	 * 供应商失效 审核通过保存
	 * 
	 * @param vendorInfo
	 * @since 2017年8月28日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	public void editLoseApplySave(Apply apply) throws BusinessException {
		if (null == apply) {
			throw new BusinessException("空数据", "空数据");
		}
		try {
			if (ApplyStatus.APPROVED.name().equals(apply.getStatus().name())) {
				String partyId = apply.getApplyOrgId();
				if (StringUtils.isBlank(partyId)) {
					throw new BusinessException("apply.getApplyOrgId()为空值", "apply.getApplyOrgId()为空值");
				}
				PartyGroup partyGroup = new PartyGroup();
				partyGroup.setPartyId(partyId);
				partyGroup.setAccountStatus(AccountStatus.NOT_VALID);
				partyGroup.setActiveStatus(ActiveStatus.INVALID);
				partyGroup.setLastUpdateUser(RequestHelper.getLoginUserId());
				partyGroup.setLastUpdateDate(new Date());
				partyGroupDao.updateStatus(partyGroup);
				// 发送通过邮件
				sendMail.losePass(apply);
			} else {
				// 发送不通过邮件
				sendMail.loseNotPass(apply);
			}
		} catch (Exception e) {
			throw new BusinessException("供应商失效 出错:{}", e.getMessage());
		}

	}

	/**
	 * 供应商审核基本信息通过保存
	 * 
	 * @param vendorInfo
	 * @since 2017年8月22日
	 * @author zr.zhanghua@yikuyi.com
	 */
	public void editVendorInfoApplySave(CheckVendorInfoVo checkVendorInfoVo) {

		Party party = new Party();
		party.setId(checkVendorInfoVo.getPartyId());

		// 供应商编码
		if (StringUtils.isNotBlank(checkVendorInfoVo.getNewPartyCode())) {
			party.setPartyCode(checkVendorInfoVo.getNewPartyCode());
			partyDao.updateParty(party);
		}
		// 供应商全称，供应商简称
		if (StringUtils.isNotBlank(checkVendorInfoVo.getGroupName())
				|| StringUtils.isNotBlank(checkVendorInfoVo.getGroupNameFull())) {

			PartyGroup partyGroup = new PartyGroup();
			if (StringUtils.isNotBlank(checkVendorInfoVo.getGroupName())) {
				partyGroup.setGroupName(checkVendorInfoVo.getGroupName());
			}
			if (StringUtils.isNotBlank(checkVendorInfoVo.getGroupNameFull())) {
				partyGroup.setGroupNameFull(checkVendorInfoVo.getGroupNameFull());
			}
			party.setPartyGroup(partyGroup);
			partyGroupDao.updatePartyGroup(party);
		}

		// 核心供应商
		if (StringUtils.isNotBlank(checkVendorInfoVo.getIsCore())
				|| StringUtils.isNotBlank(checkVendorInfoVo.getCategory())) {
			PartySupplier partySupplier = new PartySupplier();
			partySupplier.setPartyId(checkVendorInfoVo.getPartyId());
			if (StringUtils.isNotBlank(checkVendorInfoVo.getIsCore())) {
				partySupplier.setIsCore(checkVendorInfoVo.getIsCore());
			}
			if (StringUtils.isNotBlank(checkVendorInfoVo.getCategory())) {
				partySupplier.setCategory(checkVendorInfoVo.getCategory());
			}
			partySupplierDao.update(partySupplier);
		}

	}

	/**
	 * 供应商审核产品线通过编辑
	 * 
	 * @param partyProductLine
	 * @since 2017年8月22日
	 * @author zr.zhanghua@yikuyi.com
	 */
	public void editProductLineApplySave(List<PartyProductLineJS> partyProductLineList) {

		for (PartyProductLineJS partyProductLineJS : partyProductLineList) {
			if (Select.DEL.name().equals(partyProductLineJS.getSelect().name())) {
				partyProductLineDao.deleteById(partyProductLineJS.getPartyProductLineId());
			} else {
				// 涉及表 PARTY_PRODUCT_LINE，根据partyID 先删除再新增
				if (StringUtils.isNotBlank(partyProductLineJS.getPartyProductLineId())) {
					partyProductLineDao.deleteById(partyProductLineJS.getPartyProductLineId());
				}
				partyProductLineJS.setLastUpdateUser(RequestHelper.getLoginUserId());
				partyProductLineDao.insert(partyProductLineJS);
			}
		}
	}

	/**
	 * 供应商审核产品线通过编辑
	 * 
	 * @param partyProductLine
	 * @since 2017年8月22日
	 * @author zr.zhanghua@yikuyi.com
	 */
	public PartyProductLineVo findProductLineByPartyId(String partyId) {

		// 查找关键审批内容 对比是否需要走审核变更

		PartyProductLineVo partyProductLineVo = new PartyProductLineVo();
		VendorInfoVo checkInfo = partySupplierDao.getCheckInfo(partyId);
		if (null != checkInfo) {
			partyProductLineVo.setGroupNameFull(checkInfo.getGroupNameFull());
			partyProductLineVo.setGroupName(checkInfo.getGroupName());
		}
		partyProductLineVo.setPartyId(partyId);
		PartyProductLine pp = new PartyProductLine();
		pp.setPartyId(partyId);
		List<PartyProductLine> pList = partyProductLineDao.findByEntity(pp);
		if (CollectionUtils.isNotEmpty(pList)) {
			partyProductLineVo.setPartyProductLineList(pList);
		}
		return partyProductLineVo;
	}

	/**
	 * 供应商第二次提交建档审核，先删除历史记录
	 * 
	 * @param
	 * @since 2017年9月7日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	public void deleteVendorInfo(String partyId) {

		// 删除PARTY 基础表
		partyDao.deleteByPartyId(partyId);

		// 删除PARTY_GROUP 供应商基础表
		partyGroupDao.deleteByPartyId(partyId);

		// 删除PARTY_SUPPLIER 供应商属性表1
		partySupplierDao.deleteByPartyId(partyId);

		// 删除协议 PARTY_ATTRIBUTE 供应商属性表 2
		List<String> list = new ArrayList<>();
		list.add(VENDOR_INFO_LEGALPERSON);// 公司法人
		list.add(VENDOR_INFO_REGPRICE); // 注册资金
		list.add(VENDOR_INFO_REGRADDRESS);// 注册地址
		list.add(VENDOR_INFO_EMPLOYEENUM);// 员工人数
		list.add(VENDOR_INFO_WEBSITE);// 供应商官网
		list.add(VENDOR_CREDIT_PURCHASEDEAL);// 采购协议
		list.add(VENDOR_CREDIT_PURCHASEDEALDATE);// 采购协议有效期
		list.add(VENDOR_CREDIT_SECRECYPROTOCOL);// 保密协议
		list.add(VENDOR_CREDIT_SECRECYPROTOCOLDATE);// 保密协议有效期
		list.add(VENDOR_SALE_INFOVO_FOCUSFIELDS);// 关注领域
		list.add(VENDOR_SALE_INFOVO_PRODUCTCATEGORYS);// 优势产品类别
		list.add(VENDOR_SALE_INFOVO_MAJORCLIENTS);// 主要客户
		partyAttributeDao.delByPartyIdAndName(partyId, list);

		// 删除PARTY_RELATIONSHIP 分管部门
		personDao.delByPartyIdTo(partyId, PartyRelationshipTypeEnum.VENDOR_DEPT_REL.toString());
		// 删除PARTY_RELATIONSHIP 负责人
		personDao.delByPartyIdTo(partyId, PartyRelationshipTypeEnum.DEVELOPMENT_BY.toString());
		// 删除PARTY_RELATIONSHIP 询价员
		personDao.delByPartyIdTo(partyId, PartyRelationshipTypeEnum.VENDOR_ENQUIRY_REL.toString());
		// 删除PARTY_RELATIONSHIP 报价员
		personDao.delByPartyIdTo(partyId, PartyRelationshipTypeEnum.VENDOR_OFFER_REL.toString());
		// 删除PARTY_RELATIONSHIP 供应商为哪个公司供货
		personDao.delByPartyIdFrom(partyId, PartyRelationshipTypeEnum.SUPPLIER_REL.toString());

		// 删除PARTY_ROLE 角色表
		List<String> list2 = new ArrayList<>();
		list2.add(RoleTypeEnum.SUPPLIER.toString());
		partyRoleDao.deletePartyRoleByType(partyId, list2);

		// 删除PARTY_PRODUCT_LINE 供应商产品线表
		partyProductLineDao.deleteByPartyId(partyId);

		// 删除PARTY_CREDIT 供应商账期
		partyCreditDao.deleteByPartyId(partyId);

		// 删除PARTY_BANK_ACCOUNT 银行资料
		partyBankAccountDao.delByPartyId(partyId);

		// 删除 PARTY_ATTACHMENT 供应商附件表
		partyCreditDao.delByPartyId(partyId, AttachmentType.VENDOR_CREDIT.name());

		// 查找PARTY_RELATIONSHIP 供应商绑定的联系人
		List<PartyRelationship> findPartyRelationship = personDao.findPartyRelationship(partyId,
				PartyRelationshipTypeEnum.USER_DEPT_REL.name());
		if (null != findPartyRelationship && CollectionUtils.isNotEmpty(findPartyRelationship)) {
			for (PartyRelationship p : findPartyRelationship) {
				// 删除 PARTY_RELATIONSHIP 供应商联系人绑定的产品线Id
				personDao.delByPartyIdFrom(p.getPartyIdFrom(), PartyRelationshipTypeEnum.USER_PRODUCTLINE_REL.name());
				// 删除 Person
				personDao.del(p.getPartyIdFrom());
			}

		}
		// 删除 PARTY_RELATIONSHIP 供应商的联系人 关系表
		personDao.delContactPersonInfo(partyId, PartyRelationshipTypeEnum.USER_DEPT_REL.name());

		// 删除FACILITY 供应商仓库
		Facility facility = new Facility();
		facility.setOwnerPartyId(partyId);
		facilityDao.delete(facility);

	}

	public PartyGroup findGroupName(String partyId, String groupName) throws BusinessException {
		PartyGroup partyGroup = partyGroupDao.findGroupName(partyId, groupName);
		return partyGroup;
	}

	public void verifyProductLine(Set<PartyProductLine> partyProductLineList) throws BusinessException {
		if (null == partyProductLineList || CollectionUtils.isEmpty(partyProductLineList)) {
			return;
		}

		// 校验
		// 获取供应商列表
		// productClientBuilder.setBaseUrl("http://192.168.1.110:27083");
		Map<String, ProductBrand> aliasBrandMap = new HashMap<>();
		try {
			aliasBrandMap = productClientBuilder.brandResource()
					.getAliasBrandMap(authorizationUtil.getLoginAuthorization());
		} catch (Exception e) {
			throw new BusinessException("获取供应商列表服务异常：{}", e.getMessage());
		}

		// 获取分类层级信息
		// List<Long> ids= new ArrayList<>();
		// List<ProductCategoryParent> listByIds =
		// productClientBuilder.categoryResource().getListByIds(ids);

		// 查询全部分类
		List<ProductCategoryChild> allCategory;
		try {
			allCategory = productClientBuilder.categoryResource().getAllCategory(Collections.emptyList());
		} catch (Exception e) {
			throw new BusinessException("查询全部分类服务异常：{}", e.getMessage());
		}

		if (aliasBrandMap == null || aliasBrandMap.isEmpty()) {
			throw new BusinessException("获取供应商列表数据为空{}，无法校验数据是否存在", "获取供应商列表数据为空{}，无法校验数据是否存在");
		}
		if (CollectionUtils.isEmpty(allCategory)) {
			throw new BusinessException("查询全部分类数据为空{}，无法校验数据是否存在", "查询全部分类数据为空{}，无法校验数据是否存在");
		}

		int number = 1;
		// 校验传过来的数据是否正确
		for (PartyProductLine pa : partyProductLineList) {

			// 品牌ID
			String brandId = pa.getBrandId();
			// 品牌
			String brandName = pa.getBrandName();
			// 品牌标志
			String brandSign = N;
			// 大类ID
			String category1Id = StringUtils.isBlank(pa.getCategory1Id()) ? SIX_ZERO : pa.getCategory1Id();
			// 大类
			String category1Name = StringUtils.isBlank(pa.getCategory1Name()) ? "*" : pa.getCategory1Name();
			// 大类标志
			String category1Sign = N;
			// 小类ID
			String category2Id = StringUtils.isBlank(pa.getCategory2Id()) ? SIX_ZERO : pa.getCategory2Id();
			// 小类
			String category2Name = StringUtils.isBlank(pa.getCategory2Name()) ? "*" : pa.getCategory2Name();
			// 小类标志
			String category2Sign = N;
			// 次小类ID
			String category3Id = StringUtils.isBlank(pa.getCategory3Id()) ? SIX_ZERO : pa.getCategory3Id();
			// 次小类
			String category3Name = StringUtils.isBlank(pa.getCategory3Name()) ? "*" : pa.getCategory3Name();
			// 次小类标志
			String category3Sign = N;

			// 类校验使用标记 判断是否找到匹配
			int sign = 0;

			// 循环标记 跳出最外层
			int s = 0;

			if (StringUtils.isBlank(brandId) || StringUtils.isBlank(brandName)) {
				throw new BusinessException("产品线第" + number + "行的品牌ID和品牌名称必传", "产品线第" + number + "行的品牌ID和品牌名称必传");
			}
			// 校验品牌是否存在 有则brandId有值
			Iterator iter = aliasBrandMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				ProductBrand val = (ProductBrand) entry.getValue();
				if (brandName.equals(val.getBrandName())) {
					brandId = val.getId().toString();
					brandSign = N;
					break;
				} else {
					brandSign = Y;
				}
			}

			int brandNameSign = 0;
			// 校验品牌别名是否存在 有则brandId有值
			if (brandSign.equals(Y)) {
				// 校验品牌是否存在 有则brandId有值
				Iterator iter1 = aliasBrandMap.entrySet().iterator();
				while (iter1.hasNext()) {
					if (brandNameSign == 1) {
						break;
					}
					Map.Entry entry1 = (Map.Entry) iter1.next();
					ProductBrand val1 = (ProductBrand) entry1.getValue();
					List<String> brandAlias = val1.getBrandAlias();
					if (null != val1 && CollectionUtils.isNotEmpty(val1.getBrandAlias())) {
						for (String ss : val1.getBrandAlias()) {
							if (brandName.equals(ss)) {
								brandId = val1.getId().toString();
								brandName = val1.getBrandName();
								brandSign = N;
								brandNameSign = 1;
								break;
							} else {
								brandSign = Y;
							}
						}
					}
				}
			}

			if (brandSign.equals(N)) {
				if ("*".equals(category1Name) && "*".equals(category2Name) && "*".equals(category3Name)) {

				} else {
					if (sign == 0) {
						// 次小类检验
						if (CollectionUtils.isNotEmpty(allCategory)) {
							if (!"*".equals(category3Name)) {
								if (s == 0) {
									for (ProductCategoryChild p1 : allCategory) {
										if (CollectionUtils.isNotEmpty(p1.getChildren())) {
											if (s == 0) {
												for (ProductCategoryChild p2 : p1.getChildren()) {
													if (CollectionUtils.isNotEmpty(p2.getChildren())) {
														if (s == 0) {
															for (ProductCategoryChild p3 : p2.getChildren()) {
																if (p3.getName().equals(category3Name)) {
																	sign = 1;
																	category3Sign = N;
																	category3Id = p3.getId().toString();
																	category2Id = p2.getId().toString();
																	category1Id = p1.getId().toString();
																	if (!"*".equals(category2Name)
																			&& !category2Name.equals(p2.getName())) {
																		category2Sign = Y;
																	}
																	if (!"*".equals(category1Name)
																			&& !category1Name.equals(p1.getName())) {
																		category1Sign = Y;
																	}
																	s = 1;
																	break;
																} else {
																	category3Sign = Y;
																}
															}
														}
													}
												}
											}
										}

									}
								}
							}
						}
					}

					s = 0;
					if (sign == 0 && category3Sign.equals(N)) {
						// 小类检验
						if (CollectionUtils.isNotEmpty(allCategory)) {
							if (!"*".equals(category2Name)) {
								if (s == 0) {
									for (ProductCategoryChild p1 : allCategory) {
										if (CollectionUtils.isNotEmpty(p1.getChildren())) {
											if (s == 0) {
												for (ProductCategoryChild p2 : p1.getChildren()) {
													if (p2.getName().equals(category2Name)) {
														sign = 1;
														category2Sign = N;
														category2Id = p2.getId().toString();
														category1Id = p1.getId().toString();
														if (!"*".equals(category1Name)
																&& !category1Name.equals(p1.getName())) {
															category1Sign = Y;
														}
														s = 1;
														break;
													} else {
														category2Sign = Y;
													}
												}
											}
										}
									}
								}
							}
						}
					}

					if (sign == 0 && category3Sign.equals(N)) {
						// 大类检验
						if (CollectionUtils.isNotEmpty(allCategory)) {
							if (!"*".equals(category1Name)) {
								for (ProductCategoryChild p1 : allCategory) {
									if (p1.getName().equals(category1Name)) {
										sign = 1;
										category1Sign = N;
										category1Id = p1.getId().toString();
										break;
									} else {
										category1Sign = Y;
									}
								}
							}
						}
					}
				}
			}

			// 判断产品线是否正确
			if (brandSign.equals(Y) || category1Sign.equals(Y) || category2Sign.equals(Y) || category3Sign.equals(Y)) {
				throw new BusinessException("产品线第" + number + "行数据有误，请传准确数据", "产品线第" + number + "行数据有误，请传准确数据");
			}
			number++;
		}
	}

	/**
	 * 查询供管理应商列表
	 * 
	 * @param vendorResponVo
	 * @return
	 * @since 2017年10月30日
	 * @author zr.helinmei@yikuyi.com
	 */
	public List<VendorResponVo> listVendorManage(List<String> partyIds) {
		List<VendorResponVo> list = vendorDao.listVendorManage(partyIds);
		List<VendorResponVo> newList = new ArrayList<>();

		if (CollectionUtils.isNotEmpty(list)) {
			for (int i = 0; i < list.size(); i++) {
				VendorResponVo vo = list.get(i);
				// 因为图片的私有图片需要转换
				if (null != vo && StringUtils.isNotEmpty(vo.getLogoImageUrl())) {
					String logoImage = ImageToAliyunUtils.imageToAliyun(vo.getLogoImageUrl(), aliyunOSSAccount);
					vo.setLogoImageUrl(logoImage);
				}
				newList.add(i, vo);
			}

		}
		return newList;
	}

	/**
	 * 供应商别名保存
	 * 
	 * @param
	 * @return
	 * @since 2017年12月8日
	 * @author zr.helinmei@yikuyi.com
	 */
	public void saveSupplierAlias(List<PartySupplierAlias> supplierAliasList) throws BusinessException {
		if (CollectionUtils.isEmpty(supplierAliasList)) {
			return;
		}
		String userId = RequestHelper.getLoginUserId();
		for (PartySupplierAlias partySupplierAlias : supplierAliasList) {
			// 如果主键不为空则修改，否则新增
			if (StringUtils.isNotBlank(partySupplierAlias.getSupplierAliasId())) {
				partySupplierAlias.setLastUpdateUser(userId);
				partySupplierAlias.setLastUpdateDate(new Date());
				PartySupplierAlias isExistSupplier = supplierAliasDao
						.isExistAliasName(partySupplierAlias.getAliasName());
				if (null != isExistSupplier
						&& !partySupplierAlias.getSupplierAliasId().equals(isExistSupplier.getSupplierAliasId())) {
					throw new BusinessException(BusiErrorCode.EXIST_SUPPLIERALIAS_NAME);
				}
				supplierAliasDao.update(partySupplierAlias);
			} else {
				String id = String.valueOf(IdGen.getInstance().nextId());
				partySupplierAlias.setSupplierAliasId(id);
				partySupplierAlias.setCreatedDate(new Date());
				partySupplierAlias.setCreator(userId);
				PartySupplierAlias isExistSupplier = supplierAliasDao
						.isExistAliasName(partySupplierAlias.getAliasName());
				if (null != isExistSupplier) {
					throw new BusinessException(BusiErrorCode.EXIST_SUPPLIERALIAS_NAME);
				}
				supplierAliasDao.insert(partySupplierAlias);
			}
		}

	}

	/**
	 * 供应商别名是否存在
	 * 
	 * @param
	 * @return
	 * @since 2017年12月8日
	 * @author zr.helinmei@yikuyi.com
	 */
	public PartySupplierAlias isExistSupplier(String aliasName) {
		return supplierAliasDao.isExistAliasName(aliasName);
	}

	/**
	 * 根据别名模糊搜索
	 * 
	 * @param supplierAlias
	 * @since 2017年11月29日
	 * @author zr.helinmei@yikuyi.com
	 */
	public List<PartySupplierAlias> supperAliasNameList(String aliasName, String partyId) {
		return supplierAliasDao.supperAliasNameList(aliasName, partyId);
	}

	/**
	 * 删除
	 * 
	 * @param id
	 * @since 2017年12月8日
	 * @author zr.helinmei@yikuyi.com
	 */
	public void deleteSupperAlias(String supplierAliasId) {
		supplierAliasDao.deleteSupperAlias(supplierAliasId);
	}
	
	/**
	 * 导出代理和不代理产品线数据
	 * @since 2018年1月10日
	 * @author tb.lijing@yikuyi.com
	 */
	public void exportProductLines(String partyId,String partyName) {
		// 创建下载任务
		AsyncTaskInfo asyncTaskInfo = this.createTask(AsyncTaskInfo.BizType.SUPPER_PRODUCT_LINE.name(),
				AsyncTaskInfo.Action.DOWNLOAD.name());
		PartyProductLineRequest partyProductLineRequest = new PartyProductLineRequest();
		partyProductLineRequest.setTaskId(asyncTaskInfo.getId());
		partyProductLineRequest.setPartyId(partyId);
		partyProductLineRequest.setPartyName(partyName);
		//异步调用下载
		msgSender.sendMsg(partyCommonTopicName,partyProductLineRequest, null);
	}
	
	/**
	 * 创建下载任务
	 * @param bizType
	 * @param action
	 * @return
	 * @since 2018年1月10日
	 * @author tb.lijing@yikuyi.com
	 */
	public AsyncTaskInfo createTask(String bizType,String action){
		//满足条件就创建一个下载任务
		String userId = RequestHelper.getLoginUserId();
		AsyncTaskInfo asyncTaskInfo = new AsyncTaskInfo();
		Date date = new Date();
		asyncTaskInfo.setId(String.valueOf(IdGen.getInstance().nextId()));
		asyncTaskInfo.setBizType(bizType);
		asyncTaskInfo.setAction(action);
		asyncTaskInfo.setStatus(AsyncTaskInfo.Status.INITIAL.name());
		asyncTaskInfo.setBeginTime(date);
		asyncTaskInfo.setCreator(userId);
		asyncTaskInfo.setCreatedDate(date);
		messageClientBuilder.asyncTaskResource().add(asyncTaskInfo,authorizationUtil.getLoginAuthorization());
		return asyncTaskInfo;
	}
	
	/**
	 * 产品线导出
	 * @param partyProductLineRequest
	 * @throws BusinessException
	 * @since 2018年1月16日
	 * @author tb.lijing@yikuyi.com
	 */
	public void doExport(PartyProductLineRequest partyProductLineRequest) throws BusinessException {
		AsyncTaskInfo asyncTaskInfo = new AsyncTaskInfo();
		asyncTaskInfo.setId(partyProductLineRequest.getTaskId());
		asyncTaskInfo.setStatus(AsyncTaskInfo.Status.PROCESSING.name());// 处理中
		messageClientBuilder.asyncTaskResource().update(asyncTaskInfo, authorizationUtil.getMockAuthorization());
		try {
			String aliyunUrl = this.getProductLines(partyProductLineRequest);
			if (StringUtils.isBlank(aliyunUrl)) {
				throw new BusinessException("export file error");
			}
			// 默认成功
			asyncTaskInfo.setStatus(AsyncTaskInfo.Status.SUCCESS.name());
			// 上传本地文件到阿里云并获取文件路径
			asyncTaskInfo.setUrl(aliyunUrl);
			Date date = new Date();
			asyncTaskInfo.setEndTime(date);
			asyncTaskInfo.setLastUpdateDate(date);
		} catch (IOException e) {
			logger.error("productLine task {}update fail：{}", partyProductLineRequest.getTaskId(), e);
			asyncTaskInfo.setStatus(AsyncTaskInfo.Status.FAIL.name());
			asyncTaskInfo.setMessage(e.getMessage());
			asyncTaskInfo.setLastUpdateDate(new Date());
		}
		// 更新任务状态
		messageClientBuilder.asyncTaskResource().update(asyncTaskInfo, authorizationUtil.getMockAuthorization());
	}
	
	/**
	 * 获取产品线数据写入文件，上传文件到阿里云
	 * @param partyProductLineRequest
	 * @return
	 * @throws IOException
	 * @since 2018年1月16日
	 * @author tb.lijing@yikuyi.com
	 */
	public String getProductLines(PartyProductLineRequest partyProductLineRequest) throws IOException {
		PartyProductLine partyProductLine = new PartyProductLine();
		partyProductLine.setPartyId(partyProductLineRequest.getPartyId());
		int page = 1;
		int pageSize = 1000;
		List<PartyProductLine> partyProductLines = null;
		// 供应商名称
		String partyName = partyProductLineRequest.getPartyName();
		ExportProcesser processer = null;
		String fileName = partyName + System.currentTimeMillis() + "." + ExportProcesserXls.FILE_TYPE;
		File excelFile = FileUtils.getFile(fileName);
		FileOutputStream fos = null;
		fos = new FileOutputStream(excelFile);
		processer = ExportFactory.getProcesserByOutputStream(ExportProcesserXls.FILE_TYPE, fos);
		try {
			do {
				RowBounds rowBouds = new RowBounds((page - 1) * pageSize, pageSize);
				// 查询代理线数据
				partyProductLines = partyProductLineDao.findByPage(partyProductLine, rowBouds);
				// 没有查询到数据，直接返回
				if (1 == page && CollectionUtils.isEmpty(partyProductLines)) {
					return null;
				}
				List<List<String>> proxyRowdatalist = new ArrayList<>();
				List<List<String>> notProxyRowdatalist = new ArrayList<>();
				for (PartyProductLine temp : partyProductLines) {
					if (null == temp.getType()) {
						continue;
					}
					List<String> rowData = new ArrayList<>();
					rowData.add(temp.getBrandName());
					rowData.add(temp.getCategory1Name());
					rowData.add(temp.getCategory2Name());
					rowData.add(temp.getCategory3Name());
					if (Type.PROXY.name().equalsIgnoreCase(temp.getType().name())) {
						proxyRowdatalist.add(rowData);
					} else if (Type.NOT_PROXY.name().equalsIgnoreCase(temp.getType().name())) {
						notProxyRowdatalist.add(rowData);
					}
				}
				this.writeExcel(partyProductLines, proxyRowdatalist, notProxyRowdatalist, excelFile, EXPORT_TEMPLATE,
						page, processer, fos);
				page++;
			} while (CollectionUtils.isNotEmpty(partyProductLines));
			if (excelFile.exists() && excelFile.length() > 0) {
				// 上传文件到阿里云并返回文件地址
				return this.uploadFileAliyun(excelFile, "productLine.export");
			}
		} catch (Exception e) {
			IOUtils.closeQuietly(processer);
			IOUtils.closeQuietly(fos);
			FileUtils.deleteQuietly(excelFile);
			logger.error("导出供应商代理线excel错误,错误消息是 ：{}", e.getMessage());
			throw new SystemException(e);
		}
		return null;
	}
	
	/**
	 * 写入文件
	 * @param partyProductLines
	 * @param proxyRowdatalist
	 * @param notProxyRowdatalist
	 * @param excelFile
	 * @param template
	 * @param page
	 * @param processer
	 * @param fos
	 * @since 2018年1月16日
	 * @author tb.lijing@yikuyi.com
	 */
	public void writeExcel(List<PartyProductLine> partyProductLines,List<List<String>> proxyRowdatalist,List<List<String>> notProxyRowdatalist,File excelFile,String template,int page,ExportProcesser processer,FileOutputStream fos){
		try{
			if(1==page){
				processer.writeLine("代理产品线",template.split(","));
				processer.writeLine("不代理产品线",template.split(","));
			}
			for (List<String> proxyRowData : proxyRowdatalist) {
				processer.writeLine("代理产品线", proxyRowData);
			}
			for(List<String> notProxyRowdata : notProxyRowdatalist){
				processer.writeLine("不代理产品线", notProxyRowdata);
			}			if(CollectionUtils.isEmpty(partyProductLines)){
				processer.output();
			}
		}catch(Exception e){
			logger.error("写入供应商代理线excel异常，{}",e);
		}finally {
			if(CollectionUtils.isEmpty(partyProductLines)){
				IOUtils.closeQuietly(processer);
				IOUtils.closeQuietly(fos);
			}
		}
	}
	
	/**
	 * 上传文件到阿里云
	 * @param excelFile
	 * @param perfixUlr
	 * @return
	 * @throws BusinessException
	 * @since 2018年1月16日
	 * @author tb.lijing@yikuyi.com
	 */
	public String  uploadFileAliyun(File excelFile,String perfixUlr) throws BusinessException{
		String url = null;
		AliyunOSSFileUploadType ossFileUploadType = ossFileUploadSetting.getUploadTypes().get(perfixUlr);
		//上传本地文件到阿里云并获取文件路径
		try(InputStream inputStream = new BufferedInputStream( new FileInputStream(excelFile)); ){
			url = ossFileUploadType.getDirectory().putObject(ossFileUploadType.getTargetPath(), excelFile.getName(), ossFileUploadType.getHashType(), inputStream);
			FileUtils.deleteQuietly(excelFile);
		} catch (Exception e) {
			logger.error("本地文件上传阿里云异常{}",e);
			throw new BusinessException("本地文件上传阿里云异常"+e.getMessage());
		}
		return url;
	}
}
