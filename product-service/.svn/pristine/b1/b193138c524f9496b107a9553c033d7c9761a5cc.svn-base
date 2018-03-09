/*
 * Created: 2017年2月23日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.goods.manager;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.assertj.core.util.Lists;
import org.jasig.inspektr.audit.annotation.Audit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framework.springboot.utils.AuthorizationUtil;
import com.github.pagehelper.PageInfo;
import com.yikuyi.party.PartyClientBuilder;
import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.product.goods.dao.ProductStandAuditRepository;
import com.yikuyi.product.model.ProductStand;
import com.yikuyi.product.model.ProductStandAudit;
import com.yikuyi.product.vo.ProductStandAuditRequest;
import com.yikuyi.product.vo.ProductStandRequest;
import com.yikuyi.product.vo.RawData;
import com.ykyframework.exception.BaseException;
import com.ykyframework.exception.BusinessException;
import com.ykyframework.mqservice.sender.MsgSender;

/**
 * 物料审核
 * @author injor.huang@yikuyi.com
 * @version 1.0.0
 */
@Service
@Transactional
public class ProductStandAuditManager {
	private static final Logger logger = LoggerFactory.getLogger(ProductStandAuditManager.class);
	
	@Autowired
	private ProductStandAuditRepository productStandAuditRepository;
	
	@Autowired
	private CrawlerExceptionManager crawlerExceptionManager;
	
	@Autowired
	private ProductStandManager productStandManager;
	@Autowired
	private PartyClientBuilder partyClientBuilder;
	
	@Autowired
	private AuthorizationUtil authorizationUtil;
	
	@Value("${mqProduceConfig.createProductSub.topicName}")
	private String createProductTopicName;
	
	@Autowired
	private MsgSender msgSender;
	
	/**
	 * 可信度高的供应商
	 */
	@Value("${credit.high.vendorId}")
	private String creditHighVendorIds;
	
	/**
	 * 根据spuIds查询物料
	 * @param spuIds
	 * @return
	 * @since 2017年8月14日
	 * @author injor.huang@yikuyi.com
	 */
	public List<ProductStandAudit> getProductStandAuditBySpuId(List<String> spuIds){
		return productStandAuditRepository.findProductStandBySpuId(spuIds);
	}
	
	/**
	 * 判断spuId是否存在审核变里面，存在并且是"待审核"状态就要抛出异常,存储错误日志到异常日志表
	 * @param productStandAudits
	 * @param spuId
	 * @throws BaseException
	 * @since 2017年8月14日
	 * @author injor.huang@yikuyi.com
	 */
	public ProductStandAudit existProductStandAudit(List<ProductStandAudit> productStandAudits,RawData data,String from,String errorMsg) throws BusinessException{
		if(CollectionUtils.isEmpty(productStandAudits))
			return null;
		for (ProductStandAudit productStandAudit : productStandAudits) {
			if(!StringUtils.equals(productStandAudit.getSpuId(), data.getSpuId())) continue;
			//如果上传的数据在审核表里面存在并且是"待审核"状态
				if( ProductStandAudit.AuditStatus.WAIT_AUDIT.getValue() == productStandAudit.getAuditStatus()){
					logger.info("exist wait audit  material ,id={}",productStandAudit.getId());
					//插入错误信息到异常日志表
					crawlerExceptionManager.insertErrorlog(data, from, data.getSpuId(), errorMsg);
					throw new BusinessException("WAIT_AUDIT");
				}
				return productStandAudit;
		}
		return null;
	}
	
	/**
	 * 批量保存标准物料
	 * @param list
	 * @since 2017年8月15日
	 * @author injor.huang@yikuyi.com
	 */
	public void insertProductStandAudits(List<ProductStandAudit> list){
		if(CollectionUtils.isEmpty(list)) 
			return;
		productStandAuditRepository.save(list);
	}

	/**
	 * 更新物料的数据
	 * @param id
	 * @param productStandAudit
	 * @since 2017年8月15日
	 * @author injor.huang@yikuyi.com
	 * @throws BusinessException 
	 */
	@Audit(action = "Material Modifyqqq;;;'#id'qqq;;;'#userName'编辑", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public ProductStandAudit updateProductStandAudit(@com.framework.springboot.audit.Param("id")String id,
			@com.framework.springboot.audit.Param("userName")String userName,String userId, ProductStandAudit productStandAudit) throws BusinessException {
		//修改有两种情况，一种是product_stand_audit有值，第二是没有值，没有值就需要新增
		//checke Id 
		//现在都可以修改
//		ProductStand productStand = productStandRepository.findOne(id);
//		Optional<ProductStand> pot = Optional.ofNullable(productStand);
//		if(pot.isPresent()){
//			if(Arrays.asList(creditHighVendorIds.split(",")).contains(pot.get().getFrom())){
//				throw new BusinessException("digikey/mouser不能被修改");
//			}
//		}
		ProductStandAudit pAudit = this.checkExistById(id);
		Optional<ProductStandAudit> optional = Optional.ofNullable(pAudit);
		Date date = new Date();
		if(optional.isPresent()){
			if(ProductStandAudit.AuditStatus.WAIT_AUDIT.getValue() == pAudit.getAuditStatus()){
				throw new BusinessException("物料待审核");
			}
		}else{
			//新增
			productStandAudit.setCreatedTimeMillis(String.valueOf(date.getTime()));
			productStandAudit.setCreatedDate(date);
			productStandAudit.setCreator(userId);
		}
		productStandAudit.setUpdatedTimeMillis(String.valueOf(date.getTime()));
		productStandAudit.setLastUpdateDate(date);
		productStandAudit.setLastUpdateUser(userId);
		productStandAudit.setAuditStatus(ProductStandAudit.AuditStatus.WAIT_AUDIT.getValue());
		productStandAuditRepository.save(productStandAudit);
		return productStandAudit;
	}

	/**
	 * 根据ID查询物料信息
	 * @param id
	 * @return
	 * @since 2017年8月15日
	 * @author injor.huang@yikuyi.com
	 */
	public ProductStandAudit getProductStandById(String id) throws BusinessException {
		//checke Id 
		ProductStandAudit pAudit = this.checkExistById(id);
		Optional<ProductStandAudit> optional = Optional.ofNullable(pAudit);
		if(!optional.isPresent())
			throw new BusinessException("id not exist");
		//图片处理
		productStandManager.mergeImage(Arrays.asList(pAudit));
		return pAudit;
	}
	
	public ProductStandAudit checkExistById(String id)throws BusinessException{
		return productStandAuditRepository.findOne(id);
	}

/**
 * 分页查询	
 * @param manufacturerPartNumber
 * @param keyword
 * @param page
 * @param pageSize
 * @return
 * @since 2017年8月16日
 * @author injor.huang@yikuyi.com
 */
	public PageInfo<ProductStandAudit> findProductStandAuditByPage(ProductStandRequest standRequest,int page, int pageSize) {
		//拼装查询条件以及排序
		Query query = this.mergeCondition(standRequest);
		if(!Optional.ofNullable(query).isPresent()){
			return new PageInfo<>(Collections.EMPTY_LIST);
		}
		int pageNo = 0;
		if(page>0) 
			pageNo = page-1;
		Sort sort = new Sort(Direction.DESC,"updatedTimeMillis");
		PageRequest pageable = new PageRequest(pageNo,pageSize,sort);
		Page<ProductStandAudit> pageInfo = productStandAuditRepository.findProductStandAuditByPage(query.getQueryObject(), pageable);
		List<ProductStandAudit> productStandAudits = pageInfo.getContent();
		if(CollectionUtils.isEmpty(productStandAudits)){
			return new PageInfo<>(Collections.EMPTY_LIST);
		}
		//合并数据
		this.mergeData(productStandAudits);
		//图片处理
		productStandManager.mergeImage(productStandAudits);
		PageInfo<ProductStandAudit> pageResult = new PageInfo<>(productStandAudits);		
		pageResult.setPageNum(page);
		pageResult.setPageSize(pageSize);
		pageResult.setTotal(pageInfo.getTotalElements());		
		return pageResult;
	}
	
	/**
	 * 合并数据
	 * 
	 * @since 2017年8月16日
	 * @author injor.huang@yikuyi.com
	 */
	public void mergeData(List<ProductStandAudit> standAudits){
		List<String> userIds = Lists.newArrayList();
		standAudits.stream().forEach(standAudit -> {
			userIds.add(standAudit.getCreator());
			userIds.add(standAudit.getLastUpdateUser());
		});
		if(CollectionUtils.isEmpty(userIds)){
			return;
		}
		//根据用户IDs查询批量查询用户信息
		List<UserVo> userVos = partyClientBuilder.personClient().getPartyByIds(userIds, authorizationUtil.getLoginAuthorization());
		if(CollectionUtils.isEmpty(userVos)){
			return;
		}
		standAudits.stream().forEach(origin ->{
			if(StringUtils.isNotBlank(origin.getUpdatedTimeMillis())){
				origin.setUpdatedTimeMillis(DateFormatUtils.format(Long.valueOf(origin.getUpdatedTimeMillis()), "yyyy-MM-dd HH:mm:ss"));
			}
			userVos.stream().forEach(user -> {
				if(StringUtils.equalsIgnoreCase(origin.getCreator(), user.getPartyId())){
					origin.setCreatorName(user.getName());
				}
				if(StringUtils.equalsIgnoreCase(origin.getLastUpdateUser(), user.getPartyId())){
					origin.setUpdateName(user.getName());
				}
			});
		});
	}
	
	/**
	 * 拼装查询条件
	 * @param manufacturerPartNumber
	 * @param manufacturer
	 * @param startDate
	 * @param endDate
	 * @param status
	 * @param auditStatus
	 * @param auditUserName
	 * @param cate1Name
	 * @param cate2Name
	 * @param cate3Name
	 * @return
	 * @since 2017年8月16日
	 * @author injor.huang@yikuyi.com
	 */
	private Query mergeCondition(ProductStandRequest standRequest){
		Criteria criteria = new Criteria();
		Optional<Integer> opt = Optional.ofNullable(standRequest.getAuditStatus());
		if(opt.isPresent()){
			criteria.and("auditStatus").is(standRequest.getAuditStatus());
		}else{
			criteria.and("auditStatus").ne(ProductStandAudit.AuditStatus.PASS.getValue());
		}
		return productStandManager.mergeCondition(standRequest, criteria);
	}
	/**
	 * 根据Ids批量审核
	 * @param ids
	 * @param userId
	 * @since 2017年8月16日
	 * @author injor.huang@yikuyi.com
	 */
	public void audit(ProductStandAuditRequest auditRequest, String userId,String userName)throws BusinessException {
		//数据验证并返回查询数据
		List<ProductStandAudit> standAudits = this.validateData(auditRequest);
		//物料表
		List<ProductStand> stands = Lists.newArrayList();
		standAudits.stream().forEach(standAudit ->{
			//添加审计日志
			this.auditLog(standAudit.getId(), userName);
			standAudit.setAuditStatus(auditRequest.getAuditStats());
			standAudit.setReason(auditRequest.getDescr());
			//如果审核通过就需要更新到 物料表
			if(ProductStandAudit.AuditStatus.PASS.getValue() == auditRequest.getAuditStats()){
				ProductStand stand = new ProductStand();
				Date date = new Date();
				try {
					PropertyUtils.copyProperties(stand, standAudit);
				} catch (Exception e) {
					logger.error("copy properties error:{}",e);
				}
				stand.setUpdatedTimeMillis(String.valueOf(date.getTime()));
				stand.setLastUpdateDate(date);
				stands.add(stand);
			}
		});
		//更新物料审核表
		productStandAuditRepository.save(standAudits);
		if(CollectionUtils.isNotEmpty(stands)){
			//更细物料表
			productStandManager.saveProductStands(stands);
			//Mq更新产品信息数据
			msgSender.sendMsg(this.createProductTopicName, (Serializable) stands, null);
		}
		
	}
	/**
	 * 数据验证并返回查询数据
	 * @param auditRequest
	 * @return
	 * @throws BusinessException
	 * @since 2017年9月4日
	 * @author injor.huang@yikuyi.com
	 */
	public List<ProductStandAudit> validateData(ProductStandAuditRequest auditRequest) throws BusinessException{
		Optional<ProductStandAuditRequest> opt = Optional.ofNullable(auditRequest);
		if(!opt.isPresent())
			throw new BusinessException("no data");
		List<String> ids = auditRequest.getIds();
		if(CollectionUtils.isEmpty(ids))
			throw new BusinessException("parameter [ids] no data");
		//查询表物料审核表
		List<ProductStandAudit> standAudits = productStandAuditRepository.findProductStandByIds(ids);
		if(CollectionUtils.isEmpty(standAudits))
			throw new BusinessException("ids parameter query no data");
		return standAudits;
	}
	
	@Audit(action = "Material Modifyqqq;;;'#id'qqq;;;'#userName'审核", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public void auditLog(@com.framework.springboot.audit.Param("id")String id,
			@com.framework.springboot.audit.Param("userName")String userName){
		
	}
	
}
