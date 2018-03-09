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
package com.yikuyi.party.vendors.bll;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.jasig.inspektr.audit.annotation.Audit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.framework.springboot.audit.Param;
import com.framewrok.springboot.web.RequestHelper;
import com.github.pagehelper.PageInfo;
import com.yikuyi.info.InfoClientBuilder;
import com.yikuyi.news.model.News;
import com.yikuyi.party.acl.api.impl.ACLResource;
import com.yikuyi.party.common.utils.Constants;
import com.yikuyi.party.contact.vo.User;
import com.yikuyi.party.contact.vo.UserManage;
import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.party.facility.bll.FacilityManager;
import com.yikuyi.party.facility.model.Facility;
import com.yikuyi.party.group.model.PartyGroup;
import com.yikuyi.party.group.model.SupplierLeadtimeRate;
import com.yikuyi.party.group.vo.PartyGroupVo;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.model.Party.PartyStatus;
import com.yikuyi.party.model.Party.PartyType;
import com.yikuyi.party.model.PartyAttribute;
import com.yikuyi.party.model.PartyAttributes;
import com.yikuyi.party.model.PartyRelationship;
import com.yikuyi.party.model.PartyRelationship.PartyRelationshipStatus;
import com.yikuyi.party.model.PartyRelationship.PartyRelationshipTypeEnum;
import com.yikuyi.party.party.bll.PartyAttributeManager;
import com.yikuyi.party.party.dao.PartyAttributeDao;
import com.yikuyi.party.party.dao.PartyDao;
import com.yikuyi.party.party.dao.PartyRoleDao;
import com.yikuyi.party.partySupplier.dao.PartySupplierDao;
import com.yikuyi.party.partygroup.dao.PartyGroupDao;
import com.yikuyi.party.partygroup.dao.PartyRelationshipDao;
import com.yikuyi.party.partygroup.dao.SupplierLeadtimeRateDao;
import com.yikuyi.party.role.model.RoleTypeEnum;
import com.yikuyi.party.user.bll.UserManager;
import com.yikuyi.party.vendor.vo.PartySupplier;
import com.yikuyi.party.vo.FacilityVo;
import com.yikuyi.party.vo.PartyVo;
import com.yikuyi.party.vo.SupplierVo;
import com.yikuyi.party.vo.VendorVo;
import com.ykyframework.model.IdGen;


/**
 * 供应商管理
 * 
 * @author 张伟
 *
 */
@Service
@Transactional
public class VendorsManager {
	
	private static final Logger logger = LoggerFactory.getLogger(VendorsManager.class);

	@Autowired
	private FacilityManager facilityManager;

	@Autowired
	private InfoClientBuilder infoClientBuilder;


	@Value("${api.basedata.serverUrlPrefix}")
	private String basedataServerUrlPrefix;

	@Value("${api.product.serverUrlPrefix}")
	private String productServerUrlPrefix;

	@Value("${api.transaction.serverUrlPrefix}")
	private String transactionServerUrlPrefix;
	
	// 项目前缀
	@Value("${operation.serverUrlPrefix}")
	private String operationUrl;
	
	
	@Value("${mqConsumeConfig.sendMsgAndEmail.topicName}")
	private String sendMsgAndEmailTopicName;
	
	

	@Autowired
	private UserManager userManager;

	@Autowired
	private PartyDao partyDao;

	@Autowired
	private PartyRoleDao partyRoleDao;

	@Autowired
	private PartyGroupDao partyGroupDao;

	@Autowired
	private PartyRelationshipDao partyRelationshipDao;

	@Autowired
	private PartyAttributeDao partyAttributeDao;

	@Autowired
	private PartyAttributeManager partyAttributeManager;
	
	@Autowired
	private ACLResource aCLResource;

	private static final String IS_SHOW_NAME = "IS_SHOW_NAME";

	private static final String IS_SUP_PRICE = "IS_SUP_PRICE";
		
	@Autowired
	private SupplierLeadtimeRateDao supplierLeadtimeRateDao;
	
	@Autowired
	private PartySupplierDao partySupplierDao;

	/**
	 * 查找所有供应商信息
	 * 
	 * @author 张伟
	 * @return
	 */
	public PageInfo<PartyVo> getPartyList(PartyGroupVo param, RowBounds rowBounds) {

		String partyId = RequestHelper.getLoginUserId();
		
		Set<String> set= aCLResource.getUserRoleList(partyId);
		List<String> list1 = new ArrayList<String>(set);
		//判断是否是主管
		if(!list1.contains("INQUIRY_QUOTATION_MG")){
			param.setPartyIdFrom(partyId);
		}
		List<PartyVo> partylist = partyGroupDao.getVendorVoList(param, rowBounds);
		List<String> vendorIdList = partylist.stream().map(Party::getId).collect(Collectors.toList());
		List<News> newsList = this.getNewsByVnedorId(vendorIdList);
		Map<String, News> newsMap = newsList.stream().collect(Collectors.toMap(News::getNewsId, Function.identity()));
		for (PartyVo party : partylist) {
			String ownerPartyId = party.getId();
			PartyAttributes partyAttributes = new PartyAttributes();
			// 如果存在key 则设置显示
			if (newsMap.containsKey(ownerPartyId)) {
				partyAttributes.setIsVendorDetail("Y");
			}
//			List<PartyAttribute> partyAttributelist = partyAttributeManager.getPartyAttributelist(ownerPartyId, IS_SHOW_NAME);
//			if (CollectionUtils.isNotEmpty(partyAttributelist)) {
//				partyAttributes.setIsShowName(partyAttributelist.get(0));
//			}
			
			PartyAttribute partyAttribute = new PartyAttribute();
			partyAttribute.setKey("IS_SHOW_NAME");
			if(!StringUtils.isEmpty(party.getIsShowName()) && "Y".equals(party.getIsShowName()))
			{
				partyAttribute.setValue(party.getIsShowName());
				partyAttributes.setIsShowName(partyAttribute);
			}else {
				partyAttribute.setValue("N");
				partyAttributes.setIsShowName(partyAttribute);
			}

			party.setPartyAttributes(partyAttributes);
			// 查找每个供应商的仓库
			List<Facility> facilitylist = this.getFacilityByPartyId(ownerPartyId);
			PartyGroup partyGroup = party.getPartyGroup();
			if (null == partyGroup) {
				partyGroup = new PartyGroup();
				partyGroup.setFacilitylist(facilitylist);
				party.setPartyGroup(partyGroup);
			} else {
				party.getPartyGroup().setFacilitylist(facilitylist);
			}
		}
		return new PageInfo<>(partylist);
	}

	public List<PartyVo> getVendorNameListByIds(List<String> ids) {
		return CollectionUtils.isEmpty(ids) ? Collections.emptyList() : partyGroupDao.getVendorNameListByIds(ids);
	}
	
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
	 * 获取单个供应商信息
	 */
	public Party getPartyByPartyId(String id, String partyId) {
		PartyGroupVo param = new PartyGroupVo();
		param.setPartyId(id);
		param.setPartyIdFrom(partyId);
		List<Party> partylist = partyGroupDao.getAllPartyGroupList(param, RowBounds.DEFAULT);
		if (null != partylist && partylist.size() == 1) {

			Party party = partylist.get(0);

			String ownerPartyId = party.getId();
			// 是否显示供应商名称
			List<PartyAttribute> partyAttributelist = partyAttributeManager.getPartyAttributelist(ownerPartyId, IS_SHOW_NAME);
			if (CollectionUtils.isNotEmpty(partyAttributelist)) {
				PartyAttributes partyAttributes = new PartyAttributes();
				partyAttributes.setIsShowName(partyAttributelist.get(0));
				party.setPartyAttributes(partyAttributes);
			}

			// 查找每个供应商的仓库
			List<Facility> facilitylist = this.getFacilityByPartyId(ownerPartyId);
			PartyGroup partyGroup = party.getPartyGroup();
			if (null == partyGroup) {
				partyGroup = new PartyGroup();
				partyGroup.setFacilitylist(facilitylist);
				party.setPartyGroup(partyGroup);
			} else {
				party.getPartyGroup().setFacilitylist(facilitylist);
			}
			return party;
		}
		return null;
	}
	

	public Party getVendorDetail(String id) {
		Party party = partyGroupDao.getVendorDetail(id);
		if (party != null) {
			// 是否显示供应商名称
			String ownerPartyId = party.getId();
			PartySupplier partySupplier =partySupplierDao.getSupplierDetail(ownerPartyId);
			if(null != partySupplier){
				PartyAttributes partyAttrs = new PartyAttributes();
				PartyAttribute attrShowName = new PartyAttribute();
				attrShowName.setKey("IS_SHOW_NAME");
				attrShowName.setValue(partySupplier.getIsShowName());
				partyAttrs.setIsShowName(attrShowName);
				
				PartyAttribute attrIsSupPrice = new PartyAttribute();
				attrIsSupPrice.setKey("IS_SUP_PRICE");
				attrIsSupPrice.setValue(partySupplier.getIsSupPrice());
				partyAttrs.setIsSupPrice(attrIsSupPrice);
				
				
				PartyAttribute attrMov = new PartyAttribute();
				attrMov.setKey("VENDOR_MOV_STATUS");
				attrMov.setValue(partySupplier.getVendorMovStatus());
				partyAttrs.setVendorMovStatus(attrMov);
				
				
				PartyAttribute attrSku = new PartyAttribute();
				attrSku.setKey("SKU_MOV_STATUS");
				attrSku.setValue(partySupplier.getSkuMovStatus());
				partyAttrs.setSkuMovStatus(attrSku);;
				
				
				party.setPartyAttributes(partyAttrs);
				
			/*	PartyAttribute attrIsSupPrice = new PartyAttribute();
				attrIsSupPrice.setKey("IS_SHOW_NAME");
				attrIsSupPrice.setValue(partySupplier.getIsSupPrice());
				partyIsShowName.setIsSupPrice(attrIsSupPrice);*/
			
				
			}
			/*List<PartyAttribute> partyAttributelist = partyAttributeManager.getPartyAttributelist(ownerPartyId, IS_SHOW_NAME);
			// 是否支持价格策略 Y(显示),N(不显示)
			List<PartyAttribute> partyAttributelist1 = partyAttributeManager.getPartyAttributelist(ownerPartyId, IS_SUP_PRICE);*/
	/*	List<PartyAttribute> partyAttributelist = partyAttributeDao.getPartyAttributesByKeys(ownerPartyId,Arrays.asList(IS_SHOW_NAME,IS_SUP_PRICE,VENDOR_MOV_STATUS,SKU_MOV_STATUS));
			if (CollectionUtils.isNotEmpty(partyAttributelist)) {
				PartyAttributes partyAttributes = new PartyAttributes();
				partyAttributelist.stream().forEach(v->{
					switch (v.getKey()) {
					case IS_SHOW_NAME:
						partyAttributes.setIsShowName(v);
						break;
					case IS_SUP_PRICE:
						partyAttributes.setIsSupPrice(v);
						break;
					case VENDOR_MOV_STATUS:
						partyAttributes.setVendorMovStatus(v);
						break;
					case SKU_MOV_STATUS:
						partyAttributes.setSkuMovStatus(v);
						break;
					default:
						break;
					}
				});
				party.setPartyAttributes(partyAttributes);
			}*/
			// 查找每个供应商的仓库
			List<Facility> facilitylist = this.getFacilityByPartyId(ownerPartyId);
			PartyGroup partyGroup = party.getPartyGroup();
			if (null == partyGroup) {
				partyGroup = new PartyGroup();
				partyGroup.setFacilitylist(facilitylist);
				party.setPartyGroup(partyGroup);
			} else {
				party.getPartyGroup().setFacilitylist(facilitylist);
			}
			return party;
		}
		return new Party();
	}

	/**
	 * 保存供应商信息
	 */
	@Audit(action = "Vendor Modifyqqq;;;新增了'#vendorVo.vendorName'供应商", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public Party save(@Param("vendorVo") VendorVo vendorVo, String partyId) {
		Date createDate = new Date();
		Party party = new Party();
		party.setId(vendorVo.getVendorId());
		party.setPartyCode(vendorVo.getCode());
		party.setPartyType(PartyType.SUPPLIER);
		party.setPartyStatus(PartyStatus.PARTY_ENABLED);
		party.setCreator(partyId);
		party.setCreatedDate(new Date());
		party.setLastUpdateUser(partyId);
		party.setLastUpdateDate(new Date());
		partyDao.insert(party);

		PartyAttributes partyAttributes = new PartyAttributes();
		PartyAttribute isShowName = new PartyAttribute();
		Long attrId = IdGen.getInstance().nextId();
		isShowName.setAttrId(attrId.toString());
		isShowName.setKey(IS_SHOW_NAME);
		isShowName.setValue(vendorVo.getIsShowName());
		isShowName.setCreator(partyId);
		isShowName.setCreatedDate(new Date());
		isShowName.setLastUpdateUser(partyId);
		isShowName.setLastUpdateDate(new Date());
		partyAttributes.setIsShowName(isShowName);
		// 添加属性
		party.setPartyAttributes(partyAttributes);
		partyAttributeDao.insertIsShowName(party);

		// 添加属性 是否支持价格策略
		PartyAttribute isSupPrice = new PartyAttribute();
		isSupPrice.setAttrId(attrId.toString());
		isSupPrice.setKey(IS_SUP_PRICE);
		isSupPrice.setValue(vendorVo.getSupPrice());
		isSupPrice.setCreator(partyId);
		isSupPrice.setCreatedDate(new Date());
		isSupPrice.setLastUpdateUser(partyId);
		isSupPrice.setLastUpdateDate(new Date());
		partyAttributes.setIsShowName(isSupPrice);
		// 添加属性
		party.setPartyAttributes(partyAttributes);
		partyAttributeDao.insertIsShowName(party);
		
		if(CollectionUtils.isNotEmpty(vendorVo.getPartyAttributes())){
			vendorVo.getPartyAttributes().stream().forEach(v->{
				v.setPartyId(vendorVo.getVendorId());
				v.setCreator(partyId);
				v.setCreatedDate(createDate);
				v.setLastUpdateUser(partyId);
				v.setLastUpdateDate(createDate);
			});
			partyAttributeDao.insertVendorField(vendorVo.getPartyAttributes());
		}
		
		// 供应商信息
		PartyGroup partyGroup = new PartyGroup();
		partyGroup.setGroupName(vendorVo.getVendorName());
		partyGroup.setGroupNameFull(vendorVo.getVendorNameFull());
		partyGroup.setLogoImageUrl(vendorVo.getLogoUrl());
		partyGroup.setLogoImageUrlSmall(vendorVo.getLogoUrl());
		partyGroup.setCreator(partyId);
		partyGroup.setCreatedDate(new Date());
		partyGroup.setLastUpdateUser(partyId);
		partyGroup.setLastUpdateDate(new Date());
		party.setPartyGroup(partyGroup);
		partyGroupDao.insert(party);
		partyRoleDao.insert(party.getId(), RoleTypeEnum.SUPPLIER.toString(), partyId, new Date(), partyId, new Date());

		List<FacilityVo> facilityVolist = vendorVo.getItem();

		if (CollectionUtils.isNotEmpty(facilityVolist)) {
			// 仓库列表
			List<Facility> facilitylist = new ArrayList<>();
			for (FacilityVo facilityVo : facilityVolist) {
				Facility facility = new Facility();
				facility.setFacilityName(facilityVo.getName());
				facility.setOwnerPartyId(vendorVo.getVendorId());
				facility = this.saveFacility(facility);
				facilitylist.add(facility);
			}
			// 保存供应商仓库的信息
			partyGroup.setFacilitylist(facilitylist);
		}

		party.setPartyGroup(partyGroup);

		// 当前人创建了那个供应商
		PartyRelationship pls1 = PartyRelationship.build(PartyRelationshipTypeEnum.DEVELOPMENT_BY);
		pls1.setPartyIdFrom(partyId);
		pls1.setPartyIdTo(party.getId());
		pls1.setCreator(partyId);
		pls1.setCreatedDate(new Date());
		pls1.setLastUpdateUser(partyId);
		pls1.setLastUpdateDate(new Date());
		pls1.setFromDate(new Date());
		partyRelationshipDao.insert(pls1);
		// 供应商为哪个公司供货
		PartyRelationship pls2 = PartyRelationship.build(PartyRelationshipTypeEnum.SUPPLIER_REL);

		User user = getUser(partyId);
		pls2.setPartyIdFrom(party.getId());
		pls2.setPartyIdTo(user.getEnterpriseId());
		pls2.setCreator(partyId);
		pls2.setCreatedDate(new Date());
		pls2.setLastUpdateUser(partyId);
		pls2.setLastUpdateDate(new Date());
		pls2.setFromDate(new Date());
		partyRelationshipDao.insert(pls2);

		return party;
	}

	/**
	 * 根据id修改供应商信息
	 */
	@Audit(action = "Vendor Modifyqqq;;;编辑了'#vendorVo.vendorName'供应商", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public Party update(@Param("vendorVo") VendorVo vendorVo, String partyId) {
		Party party = new Party();
		try {
			Date updateDate = new Date();
			
			party.setId(vendorVo.getVendorId());
			party.setPartyCode(vendorVo.getCode());

			partyDao.updateParty(party);

			PartyAttributes partyAttributes = new PartyAttributes();
			PartyAttribute isShowName = new PartyAttribute();
			// 更新是否显示供应商名称
			isShowName.setKey(IS_SHOW_NAME);
			isShowName.setValue(vendorVo.getIsShowName());
			partyAttributes.setIsShowName(isShowName);
			// 添加属性
			party.setPartyAttributes(partyAttributes);
			partyAttributeDao.updateIsShowName(party);

			// 更新是否支持价格策略
			isShowName.setKey(IS_SUP_PRICE);
			isShowName.setValue(StringUtils.isEmpty(vendorVo.getSupPrice()) ? "N":vendorVo.getSupPrice());//兼容老数据
			partyAttributes.setIsShowName(isShowName);
			// 添加属性
			party.setPartyAttributes(partyAttributes);
			int num = partyAttributeDao.updateIsSupPrice(party);
			if (num == 0) {
				// 添加属性 是否支持价格策略
				PartyAttribute isSupPrice = new PartyAttribute();
				isSupPrice.setKey(IS_SUP_PRICE);
				isSupPrice.setValue(vendorVo.getSupPrice());
				isSupPrice.setCreator(partyId);
				isSupPrice.setCreatedDate(new Date());
				isSupPrice.setLastUpdateUser(partyId);
				isSupPrice.setLastUpdateDate(new Date());
				partyAttributes.setIsShowName(isSupPrice);
				// 添加属性
				party.setPartyAttributes(partyAttributes);
				partyAttributeDao.insertIsShowName(party);

			}
			
			if(CollectionUtils.isNotEmpty(vendorVo.getPartyAttributes())){
				vendorVo.getPartyAttributes().stream().forEach(v->{
					v.setPartyId(vendorVo.getVendorId());
					v.setCreator(partyId);
					v.setLastUpdateDate(updateDate);
				});
				partyAttributeDao.batchReplace(vendorVo.getPartyAttributes());
			}
			
			// 供应商信息
			PartyGroup partyGroup = new PartyGroup();
			partyGroup.setGroupName(vendorVo.getVendorName());
			partyGroup.setGroupNameFull(vendorVo.getVendorNameFull());
			partyGroup.setLogoImageUrl(vendorVo.getLogoUrl());
			partyGroup.setLogoImageUrlSmall(vendorVo.getLogoUrl());
			partyGroup.setLastUpdateUser(partyId);
			partyGroup.setLastUpdateDate(new Date());
			party.setPartyGroup(partyGroup);
			// 更新partyGroup
			partyGroupDao.updatePartyGroup(party);

			List<Facility> facilitylist = new ArrayList<>();
			// 修改数据
			// new
			List<FacilityVo> facilityVolist = vendorVo.getItem();
			// history 当前供应商的历史仓库
			List<Facility> historyfacilitylist = this.getFacilityByPartyId(party.getId());

			for (Facility facility : historyfacilitylist) {
				String historyId = facility.getId();
				boolean exists = false;
				for (FacilityVo facilityVo : facilityVolist) {
					String newId = facilityVo.getId();
					// 历史当前用户历史仓库，和 前端传个过来的供应商ID比较
					if (historyId.equals(newId)) {// 有找到对应的仓库ID需要修改根据仓库ID修改仓库名称
						facility.setFacilityName(facilityVo.getName());
						this.updateFacility(historyId, facility);
						facilitylist.add(facility);
						exists = true;
					}
				}
				if (!exists) {
					this.delFacilityById(historyId);
				}
			}
			// 新增数据
			for (FacilityVo facilityVo : facilityVolist) {
				if (StringUtils.isEmpty(facilityVo.getId())) {
					// 保存供应商
					Facility facility = new Facility();
					String namenew = facilityVo.getName();
					facility.setFacilityName(namenew);
					String ownpartyId = party.getId();
					facility.setOwnerPartyId(ownpartyId);
					facility = this.saveFacility(facility);
					facilitylist.add(facility);
				}
			}
			party.setPartyGroup(partyGroup);
			return party;
		} catch (Exception e) {
			logger.error("更新供应商信息：{},{}",e.getMessage(),e);
			return null;
		}

	}

	/**
	 * 删除当前人的供应商信息
	 * 
	 * @author 张伟
	 * @param ids
	 *            供应商ID
	 * @param partyId
	 *            当前登录人
	 */
	public void delete(List<String> ids, String partyId) {
		for (String id : ids) {
			Party party = new Party();
			party.setId(id);
			party.setPartyStatus(PartyStatus.PARTY_DISABLED);// 停用party
			party.setCreator(partyId);
			party.setLastUpdateDate(new Date());
			party.setLastUpdateUser(partyId);
			partyDao.updateParty(party);
			// 删除供应商对应的仓库
			this.delFacilityByPartyId(id);

		}
	}

	/**
	 * 保存供应商的仓库信息
	 * 
	 * @author 张伟
	 * @param facility
	 * @return
	 */
	public Facility saveFacility(Facility facility) {
		Facility fatyInfo = null;
		try {
			fatyInfo = facilityManager.addFacility(facility);
		} catch (Exception e) {
			logger.error("保存供应商仓库信息异常：{}", e);
		}
		return fatyInfo;

	}

	/**
	 * 根据仓库ID修改仓库信息
	 * 
	 * @param id
	 * @param vo
	 * @return
	 */
	private void updateFacility(String id, Facility vo) {
		// 商品信息调用
		try {
			vo.setId(id);
			vo.setLastUpdateDate(new Date());
			vo.setLastUpdateUser(RequestHelper.getLoginUser().getId());
			facilityManager.update(vo);
		} catch (Exception e) {
			logger.error("根据仓库ID修改仓库信息异常：{}", e);
		}
	}

	/**
	 * 根据用户ID获取用户信息
	 * 
	 * @author 张伟
	 * @param userId
	 * @return
	 */
	public User getUser(String userId) {
		return userManager.getUserInfo(userId);
	}

	/**
	 * 根据供应商ID查找对应的仓库信息
	 * 
	 * @author 张伟
	 * @param ownerPartyId
	 * @return
	 */
	public List<Facility> getFacilityByPartyId(String ownerPartyId) {
		List<Facility> facilityList = new ArrayList<>();
		try {
			facilityList = facilityManager.getFacilityList(ownerPartyId);
			return facilityList;
		} catch (Exception e) {
			logger.error("根据供应商ID查找对应的仓库信息异常：{}", e);
		}
		return facilityList;

	}

	/**
	 * 根据partyID删除供应商的仓库
	 * 
	 * @author 张伟
	 * @param ownerPartyId
	 * @return
	 */
	public void delFacilityByPartyId(String ownerPartyId) {
		try {
			Facility facility= new Facility();
			facility.setOwnerPartyId(ownerPartyId);
			facility.setThruDate(new Date());
			facility.setLastUpdateDate(new Date());
			facility.setLastUpdateUser(RequestHelper.getLoginUser().getId());
			facilityManager.update(facility);
		} catch (Exception e) {
			logger.error("根据partyID删除供应商的仓库异常：{}", e);
		}
	}

	/**
	 * 根据仓库id删除仓库
	 * 
	 * @param Id
	 * @return
	 */
	public void delFacilityById(String id) {
		Facility facility= new Facility();
		facility.setId(id);
		facility.setThruDate(new Date());
		facility.setLastUpdateDate(new Date());
		facility.setLastUpdateUser(RequestHelper.getLoginUser().getId());
		facilityManager.update(facility);
	}

	/**
	 * 保存分享关系
	 * 
	 * @param id
	 * @param userIds
	 * @since 2017年3月21日
	 * @author tb.yumu@yikuyi.com
	 */
	public String saveSupplierShare(@Param(value = "id") String id, @Param(value = "userVo") UserVo userVo) {
		String result = "";
		try {
			// 先过期掉之前的分配
			PartyRelationship relationship = PartyRelationship.build(PartyRelationshipTypeEnum.AGENT);
			relationship.setPartyIdTo(id);
			relationship.setStatusId(PartyRelationshipStatus.DISABLED);
			relationship.setThruDate(new Date());
			partyRelationshipDao.updateRelationShip(relationship);

			// 重新进行分配
			if (!StringUtils.isEmpty(userVo.getId())) {
				String[] ids = userVo.getId().split(",");
				PartyRelationship record = PartyRelationship.build(PartyRelationshipTypeEnum.AGENT);
				String userId = RequestHelper.getLoginUserId();
				for (String strId : ids) {
					record.setPartyIdFrom(strId);
					record.setPartyIdTo(id);
					record.setFromDate(new Date());
					record.setCreator(userId);
					record.setLastUpdateUser(userId);
					record.setCreatedDate(new Date());
					record.setLastUpdateDate(new Date());
					partyRelationshipDao.insert(record);
				}
			}
			result = Constants.SUCCESS;
		} catch (Exception e) {
			logger.error("新增分配异常：{}", e);
			result = Constants.FAILED;
		}
		return result;
	}

	/**
	 * 查询供应商待分配关系
	 * 
	 * @return
	 * @since 2017年3月21日
	 * @author tb.yumu@yikuyi.com
	 */
	public List<PartyRelationship> findSupplierShare(String id) {
		return partyRelationshipDao.getAgentByPartyfromTo(id, PartyRelationshipTypeEnum.AGENT.toString());
	}

	/**
	 * 获取供应商列表
	 * 
	 * @param partyId
	 * @param page
	 * @param size
	 * @return
	 * @since 2017年3月30日
	 * @author tb.yumu@yikuyi.com
	 */
	public PageInfo<SupplierVo> getVendorList(String partyId, String vendorName, String vendorCode, int page, int size) {
		RowBounds rowBounds = new RowBounds((page - 1) * size, size);
		List<PartyRelationship> relationshipList = partyRelationshipDao.getUserRelationship(partyId);
		List<SupplierVo> vendorVoList;
		// 如果是部门主管，则查询个人创建，代理，及下属创建的供应商，否则，则只查询自己创建，代理的供应商
		if (!relationshipList.isEmpty() && relationshipList.get(0).getPartyRelationshipTypeId() == PartyRelationshipTypeEnum.EXECUTIVE_DEPT_REL) {
			vendorVoList = partyGroupDao.getVendorList(partyId, vendorName, vendorCode, rowBounds);
		} else {
			vendorVoList = partyGroupDao.getMyVendorList(partyId, vendorName, vendorCode, rowBounds);
		}
		for (SupplierVo supplierVo : vendorVoList) {
			String id = supplierVo.getId();
			// 查询是否显示name
			List<PartyAttribute> partyAttributelist = partyAttributeManager.getPartyAttributelist(id, IS_SHOW_NAME);
			if (null != partyAttributelist && !partyAttributelist.isEmpty()) {
				supplierVo.setIsShowName(partyAttributelist.get(0).getValue());
			}

			// 是否支持价格策略
			List<PartyAttribute> partyAttributelist1 = partyAttributeManager.getPartyAttributelist(id, IS_SUP_PRICE);
			if (null != partyAttributelist1 && !partyAttributelist1.isEmpty()) {
				supplierVo.setIsSupPrice(partyAttributelist1.get(0).getValue());
			}
			// 获取供应商对应的仓库
			List<Facility> facilitylist = this.getFacilityByPartyId(id);
			if (CollectionUtils.isNotEmpty(facilitylist)) {
				supplierVo.setVendorWarehouseList(facilitylist.stream().map(Facility::getFacilityName).collect(Collectors.toList()));
			}
		}
		PageInfo<SupplierVo> infoList = new PageInfo<>(vendorVoList);
		infoList.setPageSize(size);
		infoList.setPageNum(page);
		return infoList;
	}
	/**
	 * 查询分销商的父子关系
	 * @param id
	 * @param relationshipType
	 * @return
	 * @since 2017年8月11日
	 * @author zr.helinmei@yikuyi.com
	 */
	public PartyRelationship getParentRelationInfo(String id,PartyRelationshipTypeEnum  relationshipType){
		PartyRelationship relationShip = PartyRelationship.build(relationshipType);
		relationShip.setPartyIdFrom(id);
		List<PartyRelationship> list = partyRelationshipDao.getPartyRelationship(relationShip);
		return  CollectionUtils.isEmpty(list) ? null : list.get(0);
	}

	

	
	
	
	/**
	 * 根据用户ID 获取 可查询的供应商ID列表
	 * @param partyId
	 * @return
	 * @since 2017年9月4日
	 * @author zr.chenxuemin@yikuyi.com
	 */
	public List<String> getVendorPartyIds(String partyId) {
		List<String> partyIds = new ArrayList<>();
		partyIds.add(partyId);

		// 授权分享给自己的供应商
		List<String> agent = agentPartyIds(partyId);
		if (null != agent) {
			partyIds.addAll(agent);
		}

		// 登陆人如果是主管则查询部门下所有的供应商
//		List<String> deptPartyIds = deptPartyIds(partyId);
//		if (null != deptPartyIds) {
//			partyIds.addAll(deptPartyIds);
//		}
		
		
		//登陆人如果是负责人，查询负责人对应的所有供应商
		List<String> principals= getPartyRelationshipFromList(partyId,PartyRelationshipTypeEnum.DEVELOPMENT_BY);
		if (null != principals) {
			partyIds.addAll(principals);
		}
		//登陆人如果是销售员，查询销售员对应的所有供应商
		List<String> enquirys= getPartyRelationshipFromList(partyId,PartyRelationshipTypeEnum.VENDOR_ENQUIRY_REL);
		if (null != enquirys) {
			partyIds.addAll(enquirys);
		}
		
		//去重
		return new ArrayList<String>(new TreeSet<String>(partyIds)); 
	}
	
	/**
	 * 根据用户ID 获取 可查询的销售权限ID列表
	 * @param partyId
	 * @return
	 * @since 2017年9月4日
	 * @author zr.chenxuemin@yikuyi.com
	 */
	public List<String> getSellPartyIds(String partyId) {
		List<String> partyIds = new ArrayList<>();
		partyIds.add(partyId);
	
		//登陆人如果是负责人，查询负责人对应的所有供应商
		List<String> principals= getPartyRelationshipFromList(partyId,PartyRelationshipTypeEnum.DEVELOPMENT_BY);
		if (null != principals) {
			partyIds.addAll(principals);
		}
		//登陆人如果是销售员，查询销售员对应的所有供应商
		List<String> enquirys= getPartyRelationshipFromList(partyId,PartyRelationshipTypeEnum.VENDOR_ENQUIRY_REL);
		if (null != enquirys) {
			partyIds.addAll(enquirys);
		}
		
		//登陆人如果是销售员，查询销售员对应的所有供应商
		List<String> offerId= getPartyRelationshipFromList(partyId,PartyRelationshipTypeEnum.VENDOR_OFFER_REL);
		if (null != offerId) {
			partyIds.addAll(offerId);
		}
		
		//去重
		return new ArrayList<String>(new TreeSet<String>(partyIds)); 
	}
     
	/**
	 * 查询分享给我的供应商
	 * 
	 * @param partyId
	 * @return
	 * @since 2017年9月4日
	 * @author zr.chenxuemin@yikuyi.com
	 */
	public List<String> agentPartyIds(String partyId) {
		List<String> strings=null;
		// 查询分享给我的供应商
		PartyRelationship relationShip = PartyRelationship.build(PartyRelationshipTypeEnum.AGENT);
		relationShip.setPartyIdFrom(partyId);
		relationShip.setStatusId(PartyRelationshipStatus.ENABLE);
		List<PartyRelationship> list = partyRelationshipDao.getPartyRelationship(relationShip);
		if(null != list){
			strings = list.stream().map(PartyRelationship::getPartyIdTo).collect(Collectors.toList());
		}
		return strings;
	}
	
	/**
	 * 获取部门下关系为隶属 的所有人员
	 * @param partyId
	 * @return
	 * @since 2017年9月4日
	 * @author zr.chenxuemin@yikuyi.com
	 */
	public List<String> deptPartyIds(String partyId){
		List<String> strings=null;
		PartyRelationship pr=	getParentRelationInfo(partyId,PartyRelationshipTypeEnum.EXECUTIVE_DEPT_REL);
		//如果是主管则返回部门所有人员信息
		if(null != pr)
		{
			String subDeptId = partyGroupDao.findSubDeptId(pr.getPartyIdTo());
			String[] arr = subDeptId.split(",");
			List<UserManage> list=partyGroupDao.findCustomerByDeptId(arr, RowBounds.DEFAULT);
			if(null != list){
				strings=list.stream().map(UserManage:: getPartyId).collect(Collectors.toList());	
			}
		}
		return strings;
	}
	
	/**
	 * 返回PartyIdTo 数据
	 * @param partyId
	 * @param relationshipType
	 * @return
	 * @since 2017年9月4日
	 * @author zr.chenxuemin@yikuyi.com
	 */
	public List <String> getPartyRelationshipFromList(String partyId,PartyRelationshipTypeEnum relationshipType){
		List<String> strings=null;
		PartyRelationship relationShip = PartyRelationship.build(relationshipType);
		relationShip.setPartyIdFrom(partyId);
		List<PartyRelationship> list = partyRelationshipDao.getPartyRelationship(relationShip);
		if(null != list){
			strings=list.stream().map(PartyRelationship:: getPartyIdTo).collect(Collectors.toList());	
		}
		return strings;
	}

	/**
	 * 查询供应商列表，交期准确率数据
	 * @param vendorName
	 * @return PageInfo<PartyVo>
	 * @since 2017年11月14日
	 * @author zr.helinmei@yikuyi.com
	 */
	public PageInfo<SupplierVo> getPartySupplierList(String vendorName,String orderSq,int page, int size) {
		RowBounds rowBounds = new RowBounds((page - 1) * size, size);
		List<SupplierVo> vendorVoList = partyGroupDao.getPartySupplierList( vendorName, orderSq, rowBounds);
		return  new PageInfo<>(vendorVoList);
	}
	
	/**
	 * 新增/编辑/删除交期正确率
	 * @param PartyGroup
	 * @return void
	 * @since 2017年11月14日
	 * @author zr.helinmei@yikuyi.com
	 */
	public void updateLeadTime(PartyGroup partyGroup) {
		partyGroupDao.updateVendor(partyGroup);
		//新增历史记录
		if(null != partyGroup){
			String userId = RequestHelper.getLoginUserId();
			SupplierLeadtimeRate supplierLeadtimeRate = new SupplierLeadtimeRate();
			supplierLeadtimeRate.setComments(partyGroup.getComments());
			supplierLeadtimeRate.setRate(partyGroup.getLeadtimeAccuracyrate());
			supplierLeadtimeRate.setPartyId(partyGroup.getPartyId());
			supplierLeadtimeRate.setCreator(userId);
			supplierLeadtimeRate.setCreatedDate(new Date());
			supplierLeadtimeRate.setLastUpdateUser(userId);
			supplierLeadtimeRate.setLastUpdateDate(new Date());
			supplierLeadtimeRateDao.insert(supplierLeadtimeRate);
		}
	}
	
	/**
	 * 定时波动供应商交期准确率
	 * @param 
	 * @return void
	 * @since 2017年11月14日
	 * @author zr.helinmei@yikuyi.com
	 */
	public void supplierLeadTimejob() {
		//查询出所有分销商
		List<SupplierVo> vendorVoList = partyGroupDao.getPartySupplierList( null, null, RowBounds.DEFAULT);
		//波动
		vendorVoList.stream().forEach(supplierVo ->{
			if(!StringUtils.isEmpty(supplierVo.getLeadtimeAccuracyrate())){
				//交期值上下波动
				String leadTimeValue = getFluctuationValue(supplierVo.getLeadtimeAccuracyrate());
				if(!StringUtils.isEmpty(leadTimeValue)){
					//新增历史记录
					String userId = RequestHelper.getLoginUserId();
					SupplierLeadtimeRate supplierLeadtimeRate = new SupplierLeadtimeRate();
					supplierLeadtimeRate.setComments("系统波动计算");
					supplierLeadtimeRate.setRate(leadTimeValue);
					supplierLeadtimeRate.setPartyId(supplierVo.getId());
					supplierLeadtimeRate.setCreator(userId);
					supplierLeadtimeRate.setCreatedDate(new Date());
					supplierLeadtimeRate.setLastUpdateUser(userId);
					supplierLeadtimeRate.setLastUpdateDate(new Date());
					supplierLeadtimeRateDao.insert(supplierLeadtimeRate);
					
				}
				
			}
		
		});
		
		
	}
	
	public String getFluctuationValue(String originalValue){
		if(!StringUtils.isEmpty(originalValue)){
			 int max=10;
	         int min=1;
	         Random random = new Random();
	         int numRandom = random.nextInt(max)%(max-min+1) + min;
		     Double num = Double.parseDouble(originalValue);
		     Double lastNum=0.0;
		     if(num>=90.3 && num <99.7){
				if(numRandom>5){
					lastNum =num+0.3;
				}else{
					lastNum =num-0.3;
				}
			  }else if(num <90.3){
			    	lastNum = num +0.3;
			  }else if(num >=99.7){
			    	lastNum = num-0.3;
			  }
		     DecimalFormat decimalFormat = new DecimalFormat("###################.###########");  
		     return decimalFormat.format(lastNum);
		}
		return null;
	}
	
}