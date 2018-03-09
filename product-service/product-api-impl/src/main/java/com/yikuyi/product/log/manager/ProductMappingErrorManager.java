/*
 * Created: 2017年11月2日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.log.manager;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.framework.springboot.utils.AuthorizationUtil;
import com.github.pagehelper.PageInfo;
import com.yikuyi.brand.model.ProductBrand;
import com.yikuyi.category.model.ProductCategory;
import com.yikuyi.category.model.ProductCategoryAlias;
import com.yikuyi.category.vo.ProductCategoryParent;
import com.yikuyi.log.vo.ProductMappingError;
import com.yikuyi.party.PartyClientBuilder;
import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.party.vendor.vo.VendorInfoVo;
import com.yikuyi.product.brand.manager.BrandManager;
import com.yikuyi.product.category.manager.CategoryManager;
import com.yikuyi.product.externalclient.PartyClientUtils;
import com.yikuyi.product.goods.manager.ProductStandManager;
import com.yikuyi.product.log.dao.ProductMappingErrorRepository;
import com.yikuyi.product.vo.RawData;

@Service
public class ProductMappingErrorManager {
	@Autowired
	private ProductMappingErrorRepository productMappingErrorRepository;
	@Autowired
	private MongoOperations mongoOperations;
	
	@Autowired
	private CategoryManager categoryManager;
	@Autowired
	private BrandManager brandManager;
	
	@Autowired
	private PartyClientBuilder partyClientBuilder;
	@Autowired
	private ProductStandManager productStandManager;
	
	@Autowired
	private AuthorizationUtil authorizationUtil;
	@Autowired
	private PartyClientUtils partyClientUtils;
	
	@Autowired
	private MongoOperations mongoOptions;
	
	private static final Logger logger = LoggerFactory.getLogger(ProductMappingErrorManager.class);

	/**
	 * 列表查询
	 * @param vendorId
	 * @param startDate
	 * @param endDate
	 * @param status
	 * @param oprUserName
	 * @param page
	 * @param pageSize
	 * @return
	 * @since 2017年11月2日
	 * @author injor.huang@yikuyi.com
	 */
	public PageInfo<ProductMappingError> findMappingErrorLogByPage(String brandName,String vendorId,
			String startDate, String endDate, Integer status,String dataType,
			String oprUserName, int page, int pageSize) {
		//拼装查询条件以及排序
		Query query = this.mergeCondition(brandName,vendorId, startDate, endDate, status,dataType, oprUserName);
		if(!Optional.ofNullable(query).isPresent()){
			return new PageInfo<>(Collections.emptyList());
		}
		int queryPage = 0;
		if(page>0) {
			queryPage = page - 1;
		}
		Sort sort = new Sort(Direction.DESC,"createdTimeMillis");
		query.with(sort);
		query.skip(queryPage * pageSize);
		query.limit(pageSize);
		logger.info("findMappingErrorLogByPagequery filter:"+JSON.toJSONString(query.getQueryObject()));
		List<ProductMappingError> errorLogs = mongoOperations.find(query, ProductMappingError.class);
		long total = mongoOperations.count(query, ProductMappingError.class);
		if(CollectionUtils.isEmpty(errorLogs)){
			return new PageInfo<>(Collections.emptyList());
		}
		this.mergeData(errorLogs);
		PageInfo<ProductMappingError> pageResult = new PageInfo<>(errorLogs);		
		pageResult.setPageNum(page);
		pageResult.setPageSize(pageSize);
		pageResult.setTotal(total);		
		return pageResult;
	}
	
	public void mergeData(List<ProductMappingError> errorLogs){
		List<String> userIds = Lists.newArrayList();
		List<String> vendorIds = Lists.newArrayList();
		errorLogs.stream().forEach(a -> {
			userIds.add(a.getLastUpdateUser());
			if(!vendorIds.contains(a.getVendorId())){
				vendorIds.add(a.getVendorId());
			}
		});
		List<UserVo> userVos = Lists.newArrayList();
		try{
			//根据用户IDs查询批量查询用户信息
			userVos = partyClientBuilder.personClient().getPartyByIds(userIds, authorizationUtil.getLoginAuthorization());
		}catch(Exception e){
			logger.error("查询用户信息异常：",e);
		}
		//根据ids查询供应商信息
		Map<String,VendorInfoVo> vendorInfoMap = partyClientUtils.getVendorInfoMap(vendorIds);

		for (ProductMappingError b : errorLogs) {
			if(StringUtils.isNotBlank(b.getUpdatedTimeMillis())){
				b.setUpdatedTimeMillis(DateFormatUtils.format(Long.valueOf(b.getUpdatedTimeMillis()), "yyyy-MM-dd HH:mm:ss"));
			}
			Optional<VendorInfoVo> optional = Optional.ofNullable(vendorInfoMap.get(b.getVendorId()));
			if(MapUtils.isNotEmpty(vendorInfoMap) && optional.isPresent()){
				b.setVendorName(optional.get().getGroupName());
			}

			if(CollectionUtils.isEmpty(userVos) ){
				continue;
			}
			userVos.stream().forEach(user -> {
				if(StringUtils.equalsIgnoreCase(b.getLastUpdateUser(), user.getPartyId())){
					b.setOprName(user.getName());
				}
			});
		}
	}
	private String escapeExprSpecialWord(String keyword) {  
	    String result = keyword;
		if (StringUtils.isNotBlank(result)) {  
	        String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };  
	        for (String key : fbsArr) {  
	            if (result.contains(key)) {  
	            	result = result.replace(key, "\\" + key);  
	            }  
	        }  
	    }  
	    return result;  
	}
	/**
	 * 查询条件拼装
	 * @param vendorId
	 * @param startDate
	 * @param endDate
	 * @param status
	 * @param oprUserName
	 * @return
	 * @since 2017年11月2日
	 * @author injor.huang@yikuyi.com
	 */
	public Query mergeCondition(String brandName,String vendorId,
			String startDate, String endDate, Integer status,String dataType,
			String oprUserName){
		Query query = new Query();
		Criteria criteria = new Criteria();
		if(StringUtils.isNotBlank(brandName)){
			criteria.and("brandName").regex("^"+this.escapeExprSpecialWord(brandName));
		}
		if(StringUtils.isNotBlank(vendorId)){
			criteria.and("vendorId").is(vendorId);
		}
		if(StringUtils.isNotBlank(dataType)){
			criteria.and("dataType").is(dataType);
		}	
		if(StringUtils.isNotBlank(oprUserName)){
			List<UserVo> userVos = partyClientBuilder.personClient().getUserByName(oprUserName, authorizationUtil.getLoginAuthorization());
			if(CollectionUtils.isEmpty(userVos)){
				return null;
			}
			criteria.and("lastUpdateUser").in(userVos.stream().map(UserVo :: getId).collect(Collectors.toList()));
		}
		Optional<Integer> opt = Optional.ofNullable(status);
		if(opt.isPresent()){
			criteria.and("status").is(opt.get());
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			String startTime = StringUtils.EMPTY;
			String endTime = StringUtils.EMPTY;
			if(StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)){
				logger.info("findMappingErrorLogByPagequeryone");
				startTime = String.valueOf(format.parse(startDate).getTime());
				endTime = String.valueOf(format.parse(format.format(DateUtils.addDays(format.parse(endDate),1))).getTime());
				criteria.and("updatedTimeMillis").gte(startTime).lt(endTime);
			}else if(StringUtils.isNotBlank(startDate)){
				logger.info("indMappingErrorLogByPagequerytwo");
				startTime = String.valueOf(format.parse(startDate).getTime());
				criteria.and("updatedTimeMillis").gte(startTime);
			}else if(StringUtils.isNotBlank(endDate)){
				logger.info("indMappingErrorLogByPagequerythree");
				endTime = String.valueOf(format.parse(format.format(DateUtils.addDays(format.parse(endDate),1))).getTime());
				criteria.and("updatedTimeMillis").lt(endTime);
			}
		} catch (Exception e) {
			logger.error("时间格式化错误");
		}
		logger.info("findMappingErrorLogByPagequery filter:"+JSON.toJSONString(query.getQueryObject()));
		query.addCriteria(criteria);
		return query;
	}
	
	/**
	 * 根据Ids批量更新
	 * @param ids
	 * @return
	 * @since 2017年11月2日
	 * @author injor.huang@yikuyi.com
	 */
	public Integer batchUpdate(List<String> ids,String userId) {
		//更新成功条数
		int successCnt = 0;
		//获得品牌映射
		Map<String,ProductBrand> brandAliasMap = brandManager.getAliasBrandMap();
		
		List<ProductMappingError> mappingErrors = productMappingErrorRepository.findMappingErrorLogByIds(ids);
		if(CollectionUtils.isEmpty(mappingErrors)){
			return successCnt;
		}
		Set<String> cachekey = new HashSet<>();
		mappingErrors.stream().forEach(a -> cachekey.addAll(RawData.getVendorCategoryRedisKey(a.getProductCategories())));
		//获得分类映射
		Map<String, ProductCategoryParent> categoryAliasMap = categoryManager.getCategoryByAliasName(cachekey);
		cachekey.clear();
		
		//需要更新的list
		List<String> errors = Lists.newArrayList();
		Date date = new Date();
		boolean canUpdate = false;
		for (ProductMappingError mappingError : mappingErrors) {
			if(StringUtils.equals(mappingError.getDataType(), ProductMappingError.DataType.BRAND.name())){//品牌
				String brandName = mappingError.getBrandName();
				//判断制造商是否存在
				if(brandAliasMap.get(brandManager.getAliasKey(mappingError.getVendorId(),brandName)) != null ||
						brandAliasMap.get(brandManager.getAliasKey(null,brandName)) != null ){
					canUpdate = true;
				}
			}else{//分类
				//判断分类是否存在
				if(this.isExsitCategory(mappingError.getProductCategories(), categoryAliasMap)){
					canUpdate = true;
				}
			}
			//能否更新
			if(canUpdate){
				errors.add(mappingError.getId());
				successCnt++;
				canUpdate = false;
			}
		}
		if(CollectionUtils.isNotEmpty(errors)){
			mongoOptions.updateMulti(query(where("_id").in(errors)), this.updateMappingError(date, userId), ProductMappingError.class);
		}
		return successCnt;
	}
	
	private Update updateMappingError(Date date,String userId){
		Update up = update("status",ProductMappingError.Status.STANDARD.getValue());
		up.set("updatedTimeMillis", String.valueOf(date.getTime()));
		up.set("lastUpdateDate", date);
		up.set("lastUpdateUser", userId);
		return up;
	}
	
	/**
	 * 判断分类是否存在
	 * @param categories
	 * @param categoryAliasMap
	 * @return
	 * @since 2017年11月2日
	 * @author injor.huang@yikuyi.com
	 */
	public boolean isExsitCategory(List<ProductCategory> categories,Map<String, ProductCategoryParent> categoryAliasMap){
		boolean isExsit = false;
		//获取解析出来的分类
		ProductCategoryParent categoryParent = productStandManager.getParseCategory(categories, categoryAliasMap);
		Optional<ProductCategoryParent> opt = Optional.ofNullable(categoryParent);
		if(opt.isPresent()){
			isExsit = true;
		}
		return isExsit;
	}
	
	public Set<String>  getCategoryRedisKey(String cate1Name,String cate2Name){
		Set<String> cahceKey = new HashSet<>();
		// 分类
		ProductCategoryAlias alias = new ProductCategoryAlias();
		ProductCategoryAlias aliasLast = new ProductCategoryAlias();
		alias.setLevel1(StringUtils.isBlank(cate1Name)? cate1Name : cate1Name.toUpperCase());
		alias.setLevel2(StringUtils.isBlank(cate2Name)? cate2Name : cate2Name.toUpperCase());
		aliasLast.setLevel2(StringUtils.isBlank(cate2Name)?cate2Name:cate2Name.toUpperCase());
		cahceKey.add(String.valueOf(alias.hashCode()));
		cahceKey.add(String.valueOf(aliasLast.hashCode()));
		return cahceKey;
	}
	
	/**
	 * 新增未映射的品牌或者分类
	 */
	public void add(String vendorId,String vendorName,String dataType,ProductMappingError mappingError){
		Date date = new Date();
		mappingError.setVendorId(vendorId);
		mappingError.setVendorName(vendorName);
		mappingError.setDataType(dataType);
		mappingError.setStatus(ProductMappingError.Status.UNSTANDARD.getValue());
		mappingError.setCreatedTimeMillis(String.valueOf(date.getTime()));
		mappingError.setCreatedDate(date);
		try {
			mongoOperations.save(mappingError);
		} catch (Exception e) {
			logger.error("创建未映射数据异常：",e);
		}
	}
	/**
	 * 新增品牌
	 * @param brandName
	 * @param map
	 */
	public void addBrand(String vendorId,String vendorName,String brandName){
		ProductMappingError mappingError = new ProductMappingError();
		mappingError.setBrandName(brandName);//品牌
		mappingError.setId(this.getKey(vendorId, brandName).toUpperCase());
		this.add(vendorId,vendorName,ProductMappingError.DataType.BRAND.name(),mappingError);
	}
	/**
	 * 新增分类
	 * @param alias
	 * @param map
	 */
	public void addCategory(String vendorId,String vendorName,List<ProductCategory> categories){
		ProductMappingError mappingError = new ProductMappingError();
		mappingError.setProductCategories(categories);
		mappingError.setId(this.getKeyId(vendorId, categories).toUpperCase());
		this.add(vendorId,vendorName,ProductMappingError.DataType.CATEGORY.name(),mappingError);
	}
	
	private String getKey(String vendorId,String value){
		return new StringBuilder(vendorId).append("-").append(value).toString();
	}
	private String getKeyId(String vendorId ,List<ProductCategory> categories){
		StringBuilder sb = new StringBuilder();
		sb.append(vendorId);
		categories.stream().forEach((obj)->sb.append("-").append(obj.getName()));
		return sb.toString();
	}
}