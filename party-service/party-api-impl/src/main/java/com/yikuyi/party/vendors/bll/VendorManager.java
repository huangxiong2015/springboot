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
package com.yikuyi.party.vendors.bll;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framewrok.springboot.web.RequestHelper;
import com.yikuyi.basedata.ShipmentClientBuilder;
import com.yikuyi.basedata.category.model.Category;
import com.yikuyi.party.acl.api.impl.ACLResource;
import com.yikuyi.party.credit.dao.PartyCreditDao;
import com.yikuyi.party.credit.model.PartyAttachment;
import com.yikuyi.party.credit.model.PartyCredit;
import com.yikuyi.party.customer.dao.PersonDao;
import com.yikuyi.party.facility.bll.FacilityManager;
import com.yikuyi.party.facility.model.Facility;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.model.PartyAttribute;
import com.yikuyi.party.model.PartyRelationship;
import com.yikuyi.party.model.PartyRelationship.PartyRelationshipTypeEnum;
import com.yikuyi.party.party.bll.PartyAttributeManager;
import com.yikuyi.party.party.dao.PartyAttributeDao;
import com.yikuyi.party.partygroup.dao.PartyGroupDao;
import com.yikuyi.party.partygroup.dao.PartyRelationshipDao;
import com.yikuyi.party.vendor.dao.PartyBankAccountDao;
import com.yikuyi.party.vendor.dao.VendorDao;
import com.yikuyi.party.vendor.vo.ContactPersonInfo;
import com.yikuyi.party.vendor.vo.PartyBankAccount;
import com.yikuyi.party.vendor.vo.PartyProductLine;
import com.yikuyi.party.vendor.vo.VendorCreditVo;
import com.yikuyi.party.vendor.vo.VendorInfoVo;
import com.yikuyi.party.vendor.vo.VendorSaleInfoVo;
import com.yikuyi.party.vo.DeptVo;
import com.ykyframework.exception.BusinessException;
/**
 * 供应商信息
 * @author zr.chenxuemin@yikuyi.com
 * @version 1.0.0
 */
@Service
@Transactional
public class VendorManager {
	private static final Logger logger = LoggerFactory.getLogger(VendorManager.class);

	@Autowired
	private ACLResource aCLResource;
	
	@Autowired
	private ShipmentClientBuilder shipmentClientBuilder;
	
	@Autowired
	private VendorDao vendorDao;
	
	@Autowired
	private PartyAttributeManager partyAttributeManager;
	
	@Autowired
	private PartyAttributeDao partyAttributeDao;
	@Autowired
	private PartyCreditDao partyCreditDao;
	
	@Autowired
	private PartyBankAccountDao partyBankAccountDao;
	
	@Autowired
	private PartyProductLineManager partyProductLineManager;
	
	@Autowired
	private PartyGroupDao partyGroupDao;
	
	@Autowired
	private PersonDao personDao;
	
	@Autowired
	private FacilityManager facilityManager;
	
	
	@Autowired
	private PartyRelationshipDao partyRelationshipDao;
	/**
	 * 根据partyId获取供应商基本信息
	 * @param partyId
	 * @return
	 * @since 2017年8月11日
	 * @author zr.chenxuemin@yikuyi.com
	 */
	public VendorInfoVo getVendorInfo(String partyId) throws BusinessException {
		VendorInfoVo vendorInfoVo=vendorDao.getVendorInfo(partyId);
		try {
			if(null != vendorInfoVo){
				//所属分类
				if(null != vendorInfoVo.getCategory()){
					Category category=shipmentClientBuilder.categoryResource().findById(vendorInfoVo.getCategory());
					if(null != category){
						vendorInfoVo.setCategoryName(category.getCategoryName());
					}
				}
				//所属地区
				if(null !=vendorInfoVo.getRegion()){
					Category categoryRegion=shipmentClientBuilder.categoryResource().findById(vendorInfoVo.getRegion());
					if(null != categoryRegion){
						vendorInfoVo.setRegionName(categoryRegion.getCategoryName());
					}
				}
				
				//公司类型
				if(null != vendorInfoVo.getGenre()){
					Category categoryGenre=shipmentClientBuilder.categoryResource().findById(vendorInfoVo.getGenre());
					if(null != categoryGenre){
						vendorInfoVo.setGenreName(categoryGenre.getCategoryName());
					}
				}
			}
		} catch (Exception e) {
			logger.error("获取公司类型名称报错:{}",e);
			throw new BusinessException("获取公司类型名称报错:{}",e.getMessage());	
		}
		// 供应商官网,公司法人,注册资金,注册地址,员工人数
		
		try {
			List<PartyAttribute> partyAttributelist = partyAttributeManager.getPartyAttributelist(partyId, null);
			if(CollectionUtils.isNotEmpty(partyAttributelist)){
				Map<String, String> targetMap = new HashMap<>();
				Map<String, String> map = getIdNameMap(partyAttributelist);
				for (Map.Entry<String, String> entry : map.entrySet()) {
					if(entry.getKey().equals("VENDOR_INFO_LEGALPERSON") || 
							entry.getKey().equals("VENDOR_INFO_REGPRICE") || 
							entry.getKey().equals("VENDOR_INFO_REGRADDRESS") ||	
							entry.getKey().equals("VENDOR_INFO_EMPLOYEENUM") ||
							entry.getKey().equals("VENDOR_INFO_WEBSITE")){
						targetMap.put(entry.getKey(), entry.getValue());
					}
				}
				vendorInfoVo.setVendorInfoAttributeMap(targetMap);
			}

			//需要 查询分管部门，负责人，询价员
			getPartyRelationshipInfo(vendorInfoVo,partyId);
		} catch (Exception e) {
			logger.error("获取公司类型名称报错:{}",e);
			throw new BusinessException("获取公司类型名称报错:{}",e.getMessage());	
		}
		return vendorInfoVo;
	}
	
	/**
	 * 查询分管部门，负责人，询价员，报价员
	 * @param vendorInfoVo
	 * @param partyId
	 * @since 2017年8月16日
	 * @author zr.chenxuemin@yikuyi.com
	 */
	public void getPartyRelationshipInfo(VendorInfoVo vendorInfoVo,String partyId){
		//部门
		String deptId= partyCreditDao.getPartyIdFrom(partyId,PartyRelationshipTypeEnum.VENDOR_DEPT_REL.name());
		if(StringUtils.isNotEmpty(deptId))
		{
			vendorInfoVo.setDeptId(deptId);
			DeptVo deptVo=partyGroupDao.findDeptInfo(deptId);
			if(null != deptVo){
				vendorInfoVo.setDeptName(deptVo.getName());
			}
		}
		
		//负责人
		String principalId= partyCreditDao.getPartyIdFrom(partyId,PartyRelationshipTypeEnum.DEVELOPMENT_BY.name());
		if(StringUtils.isNotEmpty(principalId)){
			vendorInfoVo.setPrincipalId(principalId);
			Party party=personDao.findPersonById(principalId);
			if(null != party){
				vendorInfoVo.setPrincipalName(party.getPerson().getLastNameLocal());
			}
		}
		//询价员
		setOfferInquire(vendorInfoVo,partyId,PartyRelationshipTypeEnum.VENDOR_ENQUIRY_REL.name(),"0");
		
		//报价员
		setOfferInquire(vendorInfoVo,partyId,PartyRelationshipTypeEnum.VENDOR_OFFER_REL.name(),"1");
	}
	
	public void setOfferInquire(VendorInfoVo vendorInfoVo,String partyId,String typeName,String personType){
		List<PartyRelationship> listEnquiry= partyRelationshipDao.listPartyIdFrom(partyId,typeName);
		if(CollectionUtils.isNotEmpty(listEnquiry)){
			List<String> listId = new ArrayList<>();//id集合
			StringBuilder nameSb =new StringBuilder();
			listEnquiry.stream().forEach(partyRelationship ->{
				if(null !=partyRelationship && StringUtils.isNotEmpty(partyRelationship.getPartyIdFrom())){
					listId.add(partyRelationship.getPartyIdFrom());
					Party party=personDao.findPersonById(partyRelationship.getPartyIdFrom());
					if(null != party && null !=party.getPerson() && StringUtils.isNotEmpty(party.getPerson().getLastNameLocal())){
						nameSb.append(party.getPerson().getLastNameLocal()+",");
					}
				}
			
			});
			if(StringUtils.isNotEmpty(nameSb.toString())){
				String enquiryName = nameSb.substring(0,nameSb.length()-1);
				if("0".equals(personType)){
					vendorInfoVo.setEnquiryName(enquiryName);
					vendorInfoVo.setEnquiryList(listId);
				}else{
					vendorInfoVo.setOfferName(enquiryName);
					vendorInfoVo.setOfferList(listId);
					
				}
				
			}
			
		}
	}
	
	/**
	 * 获取供应商信用
	 * @param partyId
	 * @return
	 * @since 2017年8月14日
	 * @author zr.chenxuemin@yikuyi.com
	 */
	public VendorCreditVo  getVendorCredit(String partyId){
		VendorCreditVo vendorCreditVo = new VendorCreditVo();

		PartyCredit partyCredit = partyCreditDao.getPartyCredit(partyId, null);
		if(partyCredit!=null){
			try {
				
				BeanUtils.copyProperties(partyCredit,vendorCreditVo);
			List<PartyAttachment>	partyCreditAttachmentList= partyAttributeDao.findAttrchmentList(partyId,"VENDOR_CREDIT");
			if(null != partyCreditAttachmentList && CollectionUtils.isNotEmpty(partyCreditAttachmentList)){
				vendorCreditVo.setCreditAttachmentList(partyCreditAttachmentList);
			}
			
			List<PartyBankAccount> partyBankAccountList= partyBankAccountDao.findPartyBankAccountList(partyId);
			if (null != partyBankAccountList && CollectionUtils.isNotEmpty(partyBankAccountList)) {
				vendorCreditVo.setPartyBankAccount(partyBankAccountList);
			}
			
			//采购协议,采购协议有效期,保密协议，保密协议有效期,
			List<PartyAttribute> partyAttributelist = partyAttributeManager.getPartyAttributelist(partyId, null);
			if(CollectionUtils.isNotEmpty(partyAttributelist)){				
				Map<String, String> targetMap = new HashMap<>();
				Map<String, String> map = getIdNameMap(partyAttributelist);
				for (Map.Entry<String, String> entry : map.entrySet()) {
					if(entry.getKey().equals("VENDOR_CREDIT_PURCHASEDEAL") || 
							entry.getKey().equals("VENDOR_CREDIT_PURCHASEDEALDATE") || 
							entry.getKey().equals("VENDOR_CREDIT_SECRECYPROTOCOL") ||	
							entry.getKey().equals("VENDOR_CREDIT_SECRECYPROTOCOLDATE") 
							){
						targetMap.put(entry.getKey(), entry.getValue());
					}
				}
				vendorCreditVo.setCreditAttributeMap(targetMap);
				
			}

			}  catch (Exception e) {
				logger.error("转换信用报错：{}",e);
			}
		}
		return vendorCreditVo;
	}
	
	
	/**
	 * 查询分管部门，负责人，询价员，报价员
	 * @param vendorInfoVo
	 * @param partyId
	 * @since 2017年8月16日
	 * @author zr.chenxuemin@yikuyi.com
	 */
	public void getPartyRelationshipInfo(VendorSaleInfoVo vendorInfoVo,String partyId){
		//部门
		String deptId= partyCreditDao.getPartyIdFrom(partyId,PartyRelationshipTypeEnum.VENDOR_DEPT_REL.name());
		if(StringUtils.isNotEmpty(deptId))
		{
			vendorInfoVo.setDeptId(deptId);
			DeptVo deptVo=partyGroupDao.findDeptInfo(deptId);
			if(null != deptVo){
				vendorInfoVo.setDeptName(deptVo.getName());
			}
		}
		
		//负责人
		String principalId= partyCreditDao.getPartyIdFrom(partyId,PartyRelationshipTypeEnum.DEVELOPMENT_BY.name());
		if(StringUtils.isNotEmpty(principalId)){
			vendorInfoVo.setPrincipalId(principalId);
			Party party=personDao.findPersonById(principalId);
			if(null != party){
				vendorInfoVo.setPrincipalName(party.getPerson().getLastNameLocal());
			}
		}
		
		//询价员
		setOfferInquireOne(vendorInfoVo,partyId,PartyRelationshipTypeEnum.VENDOR_ENQUIRY_REL.name(),"0");
		
		//报价员
		setOfferInquireOne(vendorInfoVo,partyId,PartyRelationshipTypeEnum.VENDOR_OFFER_REL.name(),"1");

			
		
	}
	public void setOfferInquireOne(VendorSaleInfoVo vendorInfoVo,String partyId,String typeName,String personType){
		List<PartyRelationship> listEnquiry= partyRelationshipDao.listPartyIdFrom(partyId,typeName);
		if(CollectionUtils.isNotEmpty(listEnquiry)){
			List<String> listId = new ArrayList<>();//id集合
			StringBuilder nameSb =new StringBuilder();
			listEnquiry.stream().forEach(partyRelationship ->{
				if(null !=partyRelationship && StringUtils.isNotEmpty(partyRelationship.getPartyIdFrom())){
					listId.add(partyRelationship.getPartyIdFrom());
					Party party=personDao.findPersonById(partyRelationship.getPartyIdFrom());
					if(null != party && null !=party.getPerson() && StringUtils.isNotEmpty(party.getPerson().getLastNameLocal())){
						nameSb.append(party.getPerson().getLastNameLocal()+",");
					}
				}
			
			});
			if(StringUtils.isNotEmpty(nameSb.toString())){
				String enquiryName = nameSb.substring(0,nameSb.length()-1);
				if("0".equals(personType)){
					vendorInfoVo.setEnquiryName(enquiryName);
					vendorInfoVo.setEnquiryList(listId);
				}else{
					vendorInfoVo.setOfferName(enquiryName);
					vendorInfoVo.setOfferList(listId);
					
				}
				
			}
			
		}
	}
	
	
	
	/**
	 * 获取供应商信用
	 * @param partyId
	 * @return
	 * @since 2017年8月14日
	 * @author zr.chenxuemin@yikuyi.com
	 */
	public VendorSaleInfoVo getVendorSaleInfoVo(String partyId) throws BusinessException {
		//供应商基本信息
		VendorSaleInfoVo vendorSaleInfoVo;
		try {
			vendorSaleInfoVo = vendorDao.getVendorSaleInfoVo(partyId);
			
			//没有销售信息也要查询 部门负责人等信息
			if(null ==vendorSaleInfoVo)
			{
				vendorSaleInfoVo= new VendorSaleInfoVo();
				getPartyRelationshipInfo(vendorSaleInfoVo,partyId);
				return vendorSaleInfoVo;
			}
			
			getPartyRelationshipInfo(vendorSaleInfoVo,partyId);
			
			// 获取供应商所有的产品线
			PartyProductLine partyProductLine= new PartyProductLine();
			partyProductLine.setPartyId(partyId);
			List<PartyProductLine> partyProductLineList =partyProductLineManager.findByEntity(partyProductLine);
			if(CollectionUtils.isNotEmpty(partyProductLineList))
			{
			vendorSaleInfoVo.setPartyProductLineList(partyProductLineList);
			}
			
			// 仓库
			List<Facility> facilities=facilityManager.getFacilityList(partyId);
			if(CollectionUtils.isNotEmpty(facilities))
			{
			 vendorSaleInfoVo.setFacilityList(facilities);
			}
			// 关注领域,优势产品类别,主要客户
			List<PartyAttribute> partyAttributelist = partyAttributeManager.getPartyAttributelist(partyId, null);
			if(CollectionUtils.isNotEmpty(partyAttributelist)){
				Map<String, String> targetMap = new HashMap<>();
				Map<String, String> map = getIdNameMap(partyAttributelist);
				for (Map.Entry<String, String> entry : map.entrySet()) {
					if(entry.getKey().equals("VENDOR_SALE_INFOVO_FOCUSFIELDS") || 
							entry.getKey().equals("VENDOR_SALE_INFOVO_PRODUCTCATEGORYS") || 
							entry.getKey().equals("VENDOR_SALE_INFOVO_MAJORCLIENTS")){
						targetMap.put(entry.getKey(), entry.getValue());
					}
				}
				vendorSaleInfoVo.setSaleAttributeMap(targetMap);
			}
			
			
			//权限校验
			int s = 0;	
			//是否为报价员  0否1是 
			int offer = 0;
			//创建人
			Party party = partyGroupDao.findPartyGroupByPartyId(partyId);
			if(StringUtils.isNotEmpty(party.getCreator()) && party.getCreator().equals(RequestHelper.getLoginUserId())){
				s = 1;
			}
						
			if(s == 0){
				//负责人
				List<PartyRelationship> list1= personDao.findPartyRelationship(partyId,PartyRelationshipTypeEnum.DEVELOPMENT_BY.name());
				if(CollectionUtils.isNotEmpty(list1)){
					for(PartyRelationship p:list1){
						if(p.getPartyIdFrom().equals(RequestHelper.getLoginUserId())){
							s = 1;							
							break;
						}
					}
				}
			}
			if(s == 0){
				//询价员
				List<PartyRelationship> list2=personDao.findPartyRelationship(partyId,PartyRelationshipTypeEnum.VENDOR_ENQUIRY_REL.name());
				if(CollectionUtils.isNotEmpty(list2)){
					for(PartyRelationship p:list2){
						if(p.getPartyIdFrom().equals(RequestHelper.getLoginUserId())){
							s = 1;								
							break;
						}
					}
				}
			}
			

			if(s == 0){
				//授权人
				List<PartyRelationship> list3= personDao.findPartyRelationship(partyId,PartyRelationshipTypeEnum.AGENT.name());
				if(CollectionUtils.isNotEmpty(list3)){
					for(PartyRelationship p:list3){
						if(p.getPartyIdFrom().equals(RequestHelper.getLoginUserId())){
							s = 1;
							break;
						}
					}
				}
			}
			
			if(s == 0){
				//判断是否是超级管理员
			
				//获取用户权限
				Set<String> set= aCLResource.getUserRoleList(RequestHelper.getLoginUserId());
				List<String> list1 = new ArrayList<>(set);
				if(list1.contains("ADMIN")){
					s = 1;
				}
			}
			
			if(s == 0){
				//报价员
				List<PartyRelationship> list4=personDao.findPartyRelationship(partyId,PartyRelationshipTypeEnum.VENDOR_OFFER_REL.name());
				if(CollectionUtils.isNotEmpty(list4)){
					for(PartyRelationship p:list4){
						if(p.getPartyIdFrom().equals(RequestHelper.getLoginUserId())){
							s = 1;
							offer=1;
							break;
						}
					}
				}
			}

			if(offer==0){//当前登录人不为报价员不查询联系人信息
			// 查找PARTY_RELATIONSHIP  供应商绑定的联系人
			List<ContactPersonInfo> contactPersonInfos =new ArrayList<>();
			List<PartyRelationship> findPartyRelationship = personDao.findPartyRelationship(partyId, PartyRelationshipTypeEnum.USER_DEPT_REL.name());
			if(null!=findPartyRelationship && CollectionUtils.isNotEmpty(findPartyRelationship)){			
				for(PartyRelationship p:findPartyRelationship){
					ContactPersonInfo vendorPerson = vendorDao.getVendorPersonList(p.getPartyIdFrom());
					if(null != vendorPerson){
						contactPersonInfos.add(vendorPerson);
					}				
				}
				
			}

			//供应商绑定的联系人对应的产品线
			if(CollectionUtils.isNotEmpty(contactPersonInfos))
			{
				//循环联系人
				for (ContactPersonInfo contactPersonInfo : contactPersonInfos) {
					List<PartyRelationship> partyRelationshipList1 = personDao.findProductLineByPartyId(contactPersonInfo.getPartyId(),PartyRelationshipTypeEnum.USER_PRODUCTLINE_REL.name());
				
					//如果联系人负责的产品线不为空则将产品线ID加入联系人信息中
					if(CollectionUtils.isNotEmpty(partyRelationshipList1)){
						List<String> partyProductLines= partyRelationshipList1.stream().map(PartyRelationship:: getPartyIdTo).collect(Collectors.toList());	
						contactPersonInfo.setPartyProductLineIdList(partyProductLines);
					}					
				}
			}
			vendorSaleInfoVo.setContactPersonInfoList(contactPersonInfos);
}
		} catch (Exception e) {
			logger.error("获取公司类型名称报错:{}",e);
			throw new BusinessException("获取公司类型名称报错:{}",e.getMessage());	
		}
		return vendorSaleInfoVo;
	}
	
//	/**
//	 *  根据联系人查询供应商联系人产品线
//	 * @param partyProductLineList
//	 * @param partyProductLineId
//	 * @param partyId
//	 * @return
//	 * @since 2017年8月15日
//	 * @author zr.chenxuemin@yikuyi.com
//	 */
//	public List<String> partyProductLines(List<PartyProductLine> partyProductLineList,List<PartyRelationship> PartyRelationshipList1){
//		List<String> list=new ArrayList<>();
//			for (PartyProductLine partyProductLine2 : partyProductLineList) {
//				for (PartyRelationship partyRelationship : PartyRelationshipList1) {
//					//联系人的产品与 供应商的产品对比，如果拥有则设置为Y
//					if(partyProductLine2.getPartyProductLineId().equals(partyRelationship.getPartyIdTo()))
//					{
//						list.add(partyProductLine2.getPartyProductLineId());
//						continue;
//					}
//				}
//			}
//		
//		return list;
//	}
	
	public static Map<String, String> getIdNameMap(List<PartyAttribute> attribute) {
	    return attribute.stream().collect(Collectors.toMap(PartyAttribute::getKey, PartyAttribute::getValue));
	}
	
	
	
	/**
	 * 根据批量供应商id查询数据
	 * @param ids
	 * @return
	 * @since 2017年12月21日
	 * @author zr.helinmei@yikuyi.com
	 */
	public List<VendorInfoVo> vendorBatchList(List<String> ids){
		List<VendorInfoVo> vendorInfoVoList = vendorDao.vendorBatchList(ids);
		if(CollectionUtils.isEmpty(vendorInfoVoList)){
			return Collections.emptyList();
		}
		vendorInfoVoList.stream().forEach(vendorInfoVo->{
			try {
				getCategoryData(vendorInfoVo);
				getSupplierData(vendorInfoVo);
			} catch (Exception e) {
				logger.error("调用供应商信息报错:{}",e);
			}
		});
	
		return vendorInfoVoList;
	}

	private void getSupplierData(VendorInfoVo vendorInfoVo){
		// 供应商官网,公司法人,注册资金,注册地址,员工人数
		List<PartyAttribute> partyAttributelist = partyAttributeManager.getPartyAttributelist(vendorInfoVo.getPartyId(), null);
		if(CollectionUtils.isNotEmpty(partyAttributelist)){
			Map<String, String> targetMap = new HashMap<>();
			Map<String, String> map = getIdNameMap(partyAttributelist);
			for (Map.Entry<String, String> entry : map.entrySet()) {
				if(entry.getKey().equals("VENDOR_INFO_LEGALPERSON") || 
						entry.getKey().equals("VENDOR_INFO_REGPRICE") || 
						entry.getKey().equals("VENDOR_INFO_REGRADDRESS") ||	
						entry.getKey().equals("VENDOR_INFO_EMPLOYEENUM") ||
						entry.getKey().equals("VENDOR_INFO_WEBSITE")){
					targetMap.put(entry.getKey(), entry.getValue());
				}
			}
			vendorInfoVo.setVendorInfoAttributeMap(targetMap);
		}

		//需要 查询分管部门，负责人，询价员
		getPartyRelationshipInfo(vendorInfoVo,vendorInfoVo.getPartyId());
		
	}

	private void getCategoryData(VendorInfoVo vendorInfoVo) throws BusinessException {
		//所属分类
		if(null != vendorInfoVo.getCategory()){
			Category category=shipmentClientBuilder.categoryResource().findById(vendorInfoVo.getCategory());
			if(null != category){
				vendorInfoVo.setCategoryName(category.getCategoryName());
			}
		}
		//所属地区
		if(null !=vendorInfoVo.getRegion()){
			Category categoryRegion=shipmentClientBuilder.categoryResource().findById(vendorInfoVo.getRegion());
			if(null != categoryRegion){
				vendorInfoVo.setRegionName(categoryRegion.getCategoryName());
			}
		}
		
		//公司类型
		if(null != vendorInfoVo.getGenre()){
			Category categoryGenre=shipmentClientBuilder.categoryResource().findById(vendorInfoVo.getGenre());
			if(null != categoryGenre){
				vendorInfoVo.setGenreName(categoryGenre.getCategoryName());
			}
		}
	}
}