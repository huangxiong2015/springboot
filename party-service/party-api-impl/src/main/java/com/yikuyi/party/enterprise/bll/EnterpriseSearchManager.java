package com.yikuyi.party.enterprise.bll;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.framework.springboot.utils.AuthorizationUtil;
import com.framewrok.springboot.web.RequestHelper;
import com.github.pagehelper.PageInfo;
import com.yikuyi.party.common.utils.PartyAttributeConstants;
import com.yikuyi.party.contact.model.ContactMech;
import com.yikuyi.party.contact.model.PostalAddress;
import com.yikuyi.party.contact.vo.EnterpriseParamVo;
import com.yikuyi.party.contact.vo.EnterpriseVo;
import com.yikuyi.party.contact.vo.UserExtendVo;
import com.yikuyi.party.group.model.PartyGroup.AccountStatus;
import com.yikuyi.party.group.model.PartyGroup.ActiveStatus;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.model.Party.PartyType;
import com.yikuyi.party.model.PartyAttribute;
import com.yikuyi.party.model.PartyContactMech;
import com.yikuyi.party.model.PartyContactMech.PurposeType;
import com.yikuyi.party.model.PartyRelationship;
import com.yikuyi.party.model.PartyRelationship.PartyRelationshipTypeEnum;
import com.yikuyi.party.party.dao.PartyAttributeDao;
import com.yikuyi.party.party.dao.PartyDao;
import com.yikuyi.party.partygroup.dao.PartyGroupDao;
import com.yikuyi.party.partygroup.dao.PartyRelationshipDao;
import com.yikuyi.party.shipAddress.bll.PartyContactMechManager;
import com.yikuyi.workflow.Apply;
import com.yikuyi.workflow.WorkflowClientBuilder;
import com.yikuyi.workflow.vo.ApplyVo;
import com.yikuyi.workflow.vo.ApplyVo.ApplyVoType;

@Service
@Transactional
public class EnterpriseSearchManager {

	
	@Autowired
	private PartyDao partyDao;

	@Autowired
	private PartyAttributeDao partyAttributeDao;

	@Autowired
	private PartyContactMechManager partyContactMechManager;

	// workflow 27090 前缀
	@Value("${api.workflow.serverUrlPrefix}")
	private String workflowUrlPrefix;

	@Autowired
	private AuthorizationUtil authorizationUtil;

	@Autowired
	private WorkflowClientBuilder workflowClientBuilder;

	// portal
	@Value("${portal.serverUrlPrefix}")
	private String portalServerUrlPrefix;

	private static final String ORG_DATA_REVIEW = "ORG_DATA_REVIEW";// 申请认证流程
	private static final String ORG_PROXY_REVIEW = "ORG_PROXY_REVIEW";
	@Autowired
	private PartyRelationshipDao relationshipDao;

	@Autowired
	private PartyGroupDao partyGroupDao;

	/**
	 * 获取公司信息详情
	 * 
	 * @param id
	 * @return EnterpriseVo
	 * @since 2016年1月19日
	 * @author zr.helinmei@yikuyi.com
	 * @throws IOException
	 */
	public EnterpriseVo getPartyDetail(String id) {
		EnterpriseVo entVo = new EnterpriseVo();
		String userId = RequestHelper.getLoginUserId();
		if (!StringUtils.isEmpty(id)) {
			userId = id;
		}
		String partyId;// 企业的partyId
		Party p = partyDao.getPartyPersonDetail(userId);

		if (p != null && p.getCorporationId() != null) {
			partyId = p.getCorporationId();

		} else {
			return null;
		}
		if (null != p.getPerson() && null != p.getPerson().getPersonTypeStatus()) {
			entVo.setAccountsStatus(p.getPerson().getPersonTypeStatus());
		}
		return getPartyDetailByEntId(partyId, entVo, PartyType.CORPORATION);
	}

	/**
	 * 根据企业ID获取企业相关信息
	 * 
	 * @param entId
	 * @param entVo
	 * @return
	 * @since 2017年2月16日
	 * @author gongtianyu@yikuyi.com
	 * @throws IOException
	 */
	public EnterpriseVo getPartyDetailByEntId(String corporationId, EnterpriseVo entVo, PartyType partyType) {

		Party party = partyDao.getPartyDetail(corporationId, partyType);// 获取状态，公司名，图片
		if (null != party) {
			entVo.setId(party.getId());
			if (null != party.getPartyGroup()) {
				entVo.setAccountStatus(party.getPartyGroup().getAccountStatus());// 账号状态主账号或子账号
				entVo.setActiveStatus(party.getPartyGroup().getActiveStatus());// 认证状态认证未认证
				entVo.setName(party.getPartyGroup().getGroupName());
				entVo.setLogo(party.getPartyGroup().getLogoImageUrl());
				entVo.setComments(party.getPartyGroup().getComments());
				entVo.setCreditComments(party.getPartyGroup().getCreditComments());
				
			}
			entVo.setPartyCode(party.getPartyCode());
			entVo.setPartyStatus(party.getPartyStatus());// 账号启用或停用
			// 获取公司属性
			getEntAttr(corporationId, entVo);

			// 调用服务获取企业联系信息
			List<PartyContactMech> partyContactMechList = partyContactMechManager
					.selectPartyContactMechByType(PurposeType.REGISTER_LOCATION, corporationId, null);
			if (null != partyContactMechList && !partyContactMechList.isEmpty()) {
				PartyContactMech partyContactMech = partyContactMechList.get(0);
				// 获取基本信息
				getContactMech(entVo, partyContactMech);
			}
			if (entVo.getActiveStatus() == ActiveStatus.INVALID) {
				getInvalidReson(entVo);

			}
			// 如果是不是审核状态企业三证从mongodb中获取数据
			
			if (entVo.getActiveStatus() == ActiveStatus.WAIT_APPROVE
					|| entVo.getActiveStatus() == ActiveStatus.REJECTED) {
				// 查询认证流程数据
				getProcessData(entVo, corporationId, ORG_DATA_REVIEW);
			}

			// 企业授权委托书为驳回或者待审核从monggodb中获取
			if (entVo.getAccountStatus() == AccountStatus.ACCOUNT_REJECTED
					|| entVo.getAccountStatus() == AccountStatus.ACCOUNT_WAIT_APPROVE) {
				// 查询企业授权委托证流程数据
				getProcessData(entVo, corporationId, ORG_PROXY_REVIEW);
			}

		}

		return entVo;
	}

	private void getInvalidReson(EnterpriseVo entVo) {
		PartyRelationship relationShip = PartyRelationship.build(PartyRelationshipTypeEnum.ENTERPRISE_CERTIFIED);
		relationShip.setPartyIdFrom(entVo.getId()); // 找认证id
		List<PartyRelationship> relationList = relationshipDao.getPartyRelationship(relationShip);
		if (CollectionUtils.isNotEmpty(relationList)) {
			String pratyIdTo = relationList.get(0).getPartyIdTo();// 查找认证的企业
			Party p = partyGroupDao.getPartyGroupByGroupId(pratyIdTo);
			if (null != p && null != p.getPartyGroup()) {
				entVo.setReason(p.getPartyGroup().getComments());// 失效原因
			}
		}
	}

	private void getEntAttr(String corporationId, EnterpriseVo entVo) {
		List<PartyAttribute> attrList;
		// 公司属性
		attrList = partyAttributeDao.getPartAttribute(corporationId);
		if (null != attrList && CollectionUtils.isNotEmpty(attrList)) {
			Map<String, String> map = new HashMap<>();
			for (PartyAttribute attr : attrList) {
				if (!StringUtils.isEmpty(attr.getValue())) {
					map.put(attr.getKey(), attr.getValue());
				}

			}
			entVo.setMap(map);
		}
	}

	public EnterpriseVo getProcessData(EnterpriseVo entVo, String corporationId, String type) {

		ApplyVo applyVo = new ApplyVo();
		applyVo.setPage(1);
		applyVo.setPageSize(1);
		applyVo.setApplyOrgId(corporationId);

		PageInfo<Apply> pageinfo = workflowClientBuilder.applyClient().queryApplyByEntity(ApplyVoType.PROCESS, type, applyVo,
				authorizationUtil.getLoginAuthorization());

		if (null == pageinfo || null == pageinfo.getList() || pageinfo.getList().isEmpty()) {
			return entVo;
		}
		Apply apply = pageinfo.getList().get(0);
		if (null == apply || StringUtils.isEmpty(apply.getApplyContent())) {
			return entVo;
		}

		String processId = apply.getProcessId();
		// 把jsonObject转化成对应的实体
		EnterpriseVo enterpriseVo = JSONObject.parseObject(apply.getApplyContent(), EnterpriseVo.class);
		//获取企业3证认证申请流程数据
		getOrgData(entVo, apply, processId, enterpriseVo);
		//获取企业授权委托书申请流程数据
		getOrgProxyData(entVo, apply, processId, enterpriseVo);
		return entVo;
	}

	@SuppressWarnings("rawtypes")
	private void getOrgProxyData(EnterpriseVo entVo, Apply apply, String processId, EnterpriseVo enterpriseVo) {
		if (processId.equals(ORG_PROXY_REVIEW)) {
			// 将老的LOa移除掉，重新封装
			Map<String, String> map = entVo.getMap();
			// 从mongodb中获取的数据
			Map<String, String> newMap1 = enterpriseVo.getMap();

			map.remove(PartyAttributeConstants.LOA);// 组织机构名字
			map.remove(PartyAttributeConstants.LOA_PDF_NAME);// 税务登记pdf名字

			Map<String, String> newMap2 = new HashMap<>();
			for (Object map2 : newMap1.entrySet()) {
				if (!StringUtils.isEmpty(((Map.Entry) map2).getValue())) {
					newMap2.put(((Map.Entry) map2).getKey().toString(), ((Map.Entry) map2).getValue().toString());
				}
			}
			map.putAll(newMap2);
			entVo.setMap(map);
			// 流程id
			entVo.setAccountApplyId(apply.getApplyId());
			// 用来判断是从会员中心来的还是从operation来的
			entVo.setIsVipCenter(enterpriseVo.getIsVipCenter());
		}
	}

	@SuppressWarnings("rawtypes")
	private void getOrgData(EnterpriseVo entVo, Apply apply, String processId, EnterpriseVo enterpriseVo) {
		if (processId.equals(ORG_DATA_REVIEW)) {
			Map<String, String> map = entVo.getMap();
			map.remove(PartyAttributeConstants.REG_ADDR);// 注册地
			map.remove(PartyAttributeConstants.BUSI_LIS_TYPE);// 注册类型
			map.remove(PartyAttributeConstants.BUSI_LIC_PIC);// 营业执照
			map.remove(PartyAttributeConstants.OCC_PIC);// 组织机构
			map.remove(PartyAttributeConstants.TAX_REG_PIC);// 税务登记
			map.remove(PartyAttributeConstants.BUSI_PDF_NAME);// 营业执照pdf名字
			map.remove(PartyAttributeConstants.OCC_PDF_NAME);// 组织机构pdf名字
			map.remove(PartyAttributeConstants.TAX_PDF_NAME);// 税务登记pdf名字

			// 从mongoDB获取的数据
			Map<String, String> newMap1 = enterpriseVo.getMap();
			// 去掉空值的存在
			Map<String, String> newMap2 = new HashMap<>();
			for (Object map2 : newMap1.entrySet()) {
				if (!StringUtils.isEmpty(((Map.Entry) map2).getValue())) {
					newMap2.put(((Map.Entry) map2).getKey().toString(), ((Map.Entry) map2).getValue().toString());
				}
			}
			map.putAll(newMap2);
			entVo.setMap(map);
			// 流程id
			entVo.setApplyId(apply.getApplyId());
			// 用来判断是从会员中心来的还是从operation来的
			entVo.setIsVipCenter(enterpriseVo.getIsVipCenter());
			// 驳回原因
			entVo.setReason(apply.getReason());
		}
	}

	/**
	 * 获取公司信息抽取方法
	 * 
	 * @param id
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

	/**
	 * 获取后台认证企业列表
	 * 
	 * @param param
	 * @param rowBounds
	 * @return
	 * @since 2017年5月5日
	 * @author zr.helinmei@yikuyi.com
	 */
	public PageInfo<EnterpriseVo> entCertificationList(EnterpriseParamVo param, RowBounds rowBounds) {
		return new PageInfo<>(partyDao.entCertificationList(param, rowBounds));
	}

	/**
	 * 获取后台企业账户审核列表
	 * 
	 * @param param
	 * @param rowBounds
	 * @return
	 * @since 2017年5月5日
	 * @author zr.helinmei@yikuyi.com
	 */
	public PageInfo<EnterpriseVo> getAccountApplyList(EnterpriseParamVo param, RowBounds rowBounds) {
		return new PageInfo<>(partyDao.getAccountApplyList(param, rowBounds));
	}

	/**
	 * 查询认证企业的授权委托书授权状态
	 * 
	 * @param param
	 * @param rowBounds
	 * @return
	 * @since 2017年5月5日
	 * @author zr.helinmei@yikuyi.com
	 */
	public String getAccountStatus(String id) {
		PartyRelationship relationShip = PartyRelationship.build(PartyRelationshipTypeEnum.ENTERPRISE_CERTIFIED);
		relationShip.setPartyIdFrom(id);// 生成新的企业id
		List<PartyRelationship> relationList = relationshipDao.getPartyRelationship(relationShip);
		PartyRelationship partyRelationship;
		if (CollectionUtils.isNotEmpty(relationList)) {
			partyRelationship = relationList.get(0);
			// 企业id
			String entId = partyRelationship.getPartyIdTo();

			if (StringUtils.isEmpty(entId)) {
				return null;
			}
			Party party = partyGroupDao.findPartyGroupByPartyId(entId);
			if (null != party && null != party.getPartyGroup() && null != party.getPartyGroup().getAccountStatus()) {
				return party.getPartyGroup().getAccountStatus().toString();
			}
		}
		return null;
	}

	/**
	 * 根据企业账号获取子账号信息
	 * 
	 * @param entId
	 * @return
	 * @since 2017年5月13日
	 * @author zr.helinmei@yikuyi.com
	 */
	public List<UserExtendVo> getEnterpriseAccountsList(String entId) {
		List<UserExtendVo> list = partyDao.getEnterpriseAccountsList(entId);
		return CollectionUtils.isEmpty(list) ? Collections.emptyList() : list;
	}
}