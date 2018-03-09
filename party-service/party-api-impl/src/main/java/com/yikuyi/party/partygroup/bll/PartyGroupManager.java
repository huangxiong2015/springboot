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
package com.yikuyi.party.partygroup.bll;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.framewrok.springboot.web.RequestHelper;
import com.yikuyi.info.InfoClientBuilder;
import com.yikuyi.news.model.News;
import com.yikuyi.party.contact.vo.MsgResultVo;
import com.yikuyi.party.customer.dao.PersonDao;
import com.yikuyi.party.facility.dao.FacilityDao;
import com.yikuyi.party.facility.model.Facility;
import com.yikuyi.party.group.model.PartyGroup;
import com.yikuyi.party.group.model.PartyGroup.ActiveStatus;
import com.yikuyi.party.group.vo.PartyGroupVo;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.model.Party.PartyStatus;
import com.yikuyi.party.model.Party.PartyType;
import com.yikuyi.party.model.PartyAttribute;
import com.yikuyi.party.model.PartyAttributes;
import com.yikuyi.party.model.PartyRelationship;
import com.yikuyi.party.model.PartyRelationship.PartyRelationshipTypeEnum;
import com.yikuyi.party.party.bll.PartyAttributeManager;
import com.yikuyi.party.party.dao.PartyDao;
import com.yikuyi.party.party.dao.PartyRoleDao;
import com.yikuyi.party.partygroup.dao.PartyGroupDao;
import com.yikuyi.party.partygroup.dao.PartyRelationshipDao;
import com.yikuyi.party.role.model.RoleTypeEnum;
import com.yikuyi.party.vo.PartyVo;
import com.ykyframework.model.IdGen;

@Service
@Transactional
public class PartyGroupManager {
	private static final Logger logger = LoggerFactory.getLogger(PartyGroupManager.class);

	@Autowired
	private PartyGroupDao partyGroupDao;
	@Autowired
	private PartyRoleDao partyRoleDao;
	@Autowired
	private PartyDao partyDao;

	@Autowired
	private PartyAttributeManager partyAttributeManager;

	@Value("${api.product.serverUrlPrefix}")
	private String productServerUrlPrefix;

	@Autowired
	private InfoClientBuilder infoClientBuilder;

	@Autowired
	private PartyRelationshipDao partyRelationshipDao;

	@Autowired
	private PersonDao personDao;

	@Autowired
	private FacilityDao facilityDao;

	/**
	 * 根据条件查找详细的party信息
	 * 
	 * @author 张伟
	 * @param paramPartyGroup
	 * @return
	 */
	
	@Cacheable(value="partyGroupGetAllPartyGroupListCache", key="'getAllPartyGroupList.List[roleType:'+ #param.roleType+',status:'+ #param.status+',partyId:'+ #param.partyId+',groupName:'+ #param.groupName+',partyType:'+ #param.partyType+',partyIdFrom:'+ #param.partyIdFrom+',partyIdTo:'+ #param.partyIdTo+',roleTypeIdFrom:'+ #param.roleTypeIdFrom+',roleTypeIdTo:'+ #param.roleTypeIdTo+', offset:' + #rowBounds.offset + ', limit:' + #rowBounds.limit + ']'")
	public List<PartyVo> getAllPartyGroupList(PartyGroupVo param, RowBounds rowBounds) {
		// 查询所有供应商
		List<PartyVo> list = partyGroupDao.getAllPartyVoList(param, rowBounds);
		if (!CollectionUtils.isNotEmpty(list)) {
			return Collections.emptyList();
		}
		// 获取所有供应商id
		List<String> vendorIdList = list.stream().map(Party::getId).collect(Collectors.toList());

		// 调用接口获取分销商数据
		List<News> newsList = this.getNewsByVnedorId(vendorIdList);
		Map<String, News> newsMap = newsList.stream().collect(Collectors.toMap(News::getNewsId, Function.identity()));
		List<PartyVo> newList = new ArrayList<>();
		for (PartyVo party : list) {
			PartyAttributes attr = new PartyAttributes();
			String ownerPartyId = party.getId();
			// 设置仓库
			List<Facility> facilityList = facilityDao.getFacilityList(party.getId());
			if (CollectionUtils.isNotEmpty(facilityList)) {
				party.setFacilityList(facilityList);
			}

			// 获取负责人邮箱
			PartyRelationship principalShip = PartyRelationship.build(PartyRelationshipTypeEnum.DEVELOPMENT_BY);
			principalShip.setPartyIdTo(party.getId());
			List<PartyRelationship> principalShipList = partyRelationshipDao.getPartyRelationship(principalShip);
			if (CollectionUtils.isNotEmpty(principalShipList) && null != principalShipList.get(0)
					&& !StringUtils.isEmpty(principalShipList.get(0).getPartyIdFrom())) {
				// 根据负责人Id查询邮箱
				Party partyApplyPerson = personDao.getPersonByUserId(principalShipList.get(0).getPartyIdFrom());
				if (null != partyApplyPerson && null != partyApplyPerson.getPerson()) {
					party.setPrincipalMail(partyApplyPerson.getPerson().getMail());
				}
			}

			// 获取询价人邮箱
			PartyRelationship relationShipInquiry = PartyRelationship.build(PartyRelationshipTypeEnum.VENDOR_ENQUIRY_REL);
			relationShipInquiry.setPartyIdTo(party.getId());
			List<PartyRelationship> relationInquiryList = partyRelationshipDao.getPartyRelationship(relationShipInquiry);
			if (CollectionUtils.isNotEmpty(relationInquiryList)) {
				List<String> inquiryMailList = new ArrayList<>();
				for (int j = 0; j < relationInquiryList.size(); j++) {
					
					String principalId = relationInquiryList.get(j).getPartyIdFrom();
					Party inquiryPerson = personDao.getPersonByUserId(principalId);
					if (null != inquiryPerson && null != inquiryPerson.getPerson()
							&& !StringUtils.isEmpty(inquiryPerson.getPerson().getMail())) {
						inquiryMailList.add(inquiryPerson.getPerson().getMail());
					
					}
					
				}
				party.setInquiryMailList(inquiryMailList);
			
			}
			if ("Y".equals(party.getIsShowName())) {
				party.setDisplayName(party.getPartyGroup()==null?null:party.getPartyGroup().getGroupName());
			} else {
				party.setDisplayName(party.getPartyCode());
			}
			// 如果存在key 则设置显示
			if (newsMap.containsKey(ownerPartyId)) {
				attr.setIsVendorDetail("Y");
			} else {
				PartyRelationship relationShip = PartyRelationship.build(PartyRelationshipTypeEnum.AFFILIATED_REL);
				relationShip.setPartyIdFrom(ownerPartyId);
				List<PartyRelationship> listRelation = partyRelationshipDao.getPartyRelationship(relationShip);
				if (!CollectionUtils.isEmpty(listRelation) && newsMap.containsKey(listRelation.get(0).getPartyIdTo())) {
					attr.setIsVendorDetail("Y");
				} else {
					attr.setIsVendorDetail("N");
				}

			}

			PartyAttribute isShowName = new PartyAttribute();
			isShowName.setKey("IS_SHOW_NAME");
			if (!StringUtils.isEmpty(party.getIsShowName()) && "Y".equals(party.getIsShowName())) {
				isShowName.setValue(party.getIsShowName());
				attr.setIsShowName(isShowName);
			} else {
				isShowName.setValue("N");
				attr.setIsShowName(isShowName);
			}

			PartyAttribute vendorMovStatus = new PartyAttribute();
			vendorMovStatus.setKey("VENDOR_MOV_STATUS");
			if (!StringUtils.isEmpty(party.getVendorMovStatus()) && "Y".equals(party.getVendorMovStatus())) {
				vendorMovStatus.setValue(party.getVendorMovStatus());
				attr.setVendorMovStatus(vendorMovStatus);
			} else {
				vendorMovStatus.setValue("N");
				attr.setVendorMovStatus(vendorMovStatus);
			}

			PartyAttribute skuMovStatus = new PartyAttribute();
			skuMovStatus.setKey("SKU_MOV_STATUS");
			if (!StringUtils.isEmpty(party.getSkuMovStatus()) && "Y".equals(party.getSkuMovStatus())) {
				skuMovStatus.setValue(party.getSkuMovStatus());
				attr.setSkuMovStatus(skuMovStatus);
			} else {
				skuMovStatus.setValue("N");
				attr.setSkuMovStatus(skuMovStatus);
			}

			// 物流官网
			List<PartyAttribute> urls = partyAttributeManager.getPartyAttributelist(party.getId(), null);
			for (PartyAttribute partyAttribute : urls) {
				if ("WEBSITE_URL".equals(partyAttribute.getKey())
						|| "VENDOR_INFO_WEBSITE".equals(partyAttribute.getKey())) {// 物流官网
					attr.setWebSite(partyAttribute);
				}
			}
			party.setPartyAttributes(attr);
			newList.add(party);
		}
		return newList;
	}

	/**
	 * 查询供应商下的分销商
	 * 
	 * @return
	 * @since 2017年4月17日
	 * @author zr.helinmei@yikuyi.com
	 */
	public List<News> getNewsByVnedorId(List<String> list) {
		News news = new News();
		news.setNewsIds(list);
		news.setStatus("PUBLISHED");
		news.setCategoryTypeId("VENDOR");
		String url = productServerUrlPrefix + "/v1/news/newsbatch";
		logger.info("供应商调用查询分销商服务：{}", url);
		List<News> newsList = new ArrayList<>();
		try {
			newsList = infoClientBuilder.builderNewsClient().newsBatch(news);
		} catch (Exception e) {
			logger.error("供应商调用查询分销商服务异常：{}，服务地址：{}", e, url);
		}

		return newsList;
	}

	/**
	 * 根据组织简称查找记录
	 * 
	 * @author 张伟
	 * @param groupName
	 * @return
	 */
	public List<Party> findPartyGroupByName(String groupName) {
		return partyGroupDao.findPartyGroupByName(groupName);
	}

	/**
	 * 根据组织全名查找记录
	 * 
	 * @author 张伟
	 * @param groupName
	 * @return
	 */
	public List<Party> findPartyGroupByNameFull(String groupNameFull) {
		return partyGroupDao.findPartyGroupByNameFull(groupNameFull);
	}

	/**
	 * 根据partyGroupId获取party
	 * 
	 * @param partyGroupId
	 * @return
	 */
	public Party getPartyGroupByGroupId(String partyGroupId) {
		return partyGroupDao.getPartyGroupByGroupId(partyGroupId);
	}

	/**
	 * @author 张伟
	 * @param partyId
	 * @return
	 */
	public MsgResultVo orderPermissions(String partyId) {
		MsgResultVo result = new MsgResultVo();
		// 是否是管理员
		Set<String> partyRoles = partyRoleDao.findRoleByPartyId(partyId);
		if (partyRoles.contains(RoleTypeEnum.MAIN_ROLE.toString())) {
			result.setCode("10000");
			result.setValue("主账号");
			return result;
		} else {
			result.setCode("10003");
			result.setValue("非主账号");
			return result;
		}
	}

	/**
	 * 新增物流公司信息
	 * 
	 * @param vo
	 * @since 2017年5月3日
	 * @author tb.yumu@yikuyi.com
	 */
	@CacheEvict(value = "partyGroupGetAllPartyGroupListCache", allEntries = true, beforeInvocation = true)
	public void insertLogisticsCompany(PartyGroupVo vo) {
		Party party = new Party();
		String id = String.valueOf(IdGen.getInstance().nextId());
		String userId = RequestHelper.getLoginUserId();
		Date date = new Date();
		party.setId(id);
		party.setPartyType(PartyType.CARRIER);
		party.setPartyStatus(PartyStatus.PARTY_ENABLED);
		party.setIsSystem("N");
		party.setCreator(userId);
		party.setCreatedDate(date);
		party.setLastUpdateDate(date);
		party.setLastUpdateUser(userId);
		partyDao.insert(party);
		PartyGroup group = new PartyGroup();
		group.setGroupName(vo.getGroupName());
		group.setActiveStatus(ActiveStatus.PARTY_VERIFIED);
		group.setCreator(userId);
		group.setCreatedDate(date);
		group.setLastUpdateUser(userId);
		group.setLastUpdateDate(date);
		party.setPartyGroup(group);
		partyGroupDao.insert(party);
		partyRoleDao.insert(id, "CARRIER", userId, date, userId, date);
	}

	/**
	 * 编辑物流公司信息
	 * 
	 * @param vo
	 * @since 2017年5月3日
	 * @author tb.yumu@yikuyi.com
	 */
	@CacheEvict(value = "partyGroupGetAllPartyGroupListCache", allEntries = true, beforeInvocation = true)
	public void updateLogisticsCompany(PartyGroupVo vo) {
		Party party = new Party();
		party.setId(vo.getPartyId());
		PartyGroup group = new PartyGroup();
		group.setGroupName(vo.getGroupName());
		group.setLastUpdateDate(new Date());
		group.setLastUpdateUser(RequestHelper.getLoginUserId());
		party.setPartyGroup(group);
		partyGroupDao.updatePartyGroup(party);
	}

	/**
	 * 启用停用物流公司信息
	 * 
	 * @param vo
	 * @since 2017年5月3日
	 * @author tb.yumu@yikuyi.com
	 */
	@CacheEvict(value = "partyGroupGetAllPartyGroupListCache", allEntries = true, beforeInvocation = true)
	public void changeLogisticsCompanyStatus(PartyGroupVo vo) {
		Party party = new Party();
		party.setId(vo.getPartyId());
		party.setPartyStatus(vo.getStatus());
		party.setLastUpdateDate(new Date());
		party.setLastUpdateUser(RequestHelper.getLoginUserId());
		partyDao.updateParty(party);
	}

}