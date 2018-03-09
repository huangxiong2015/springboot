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

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.assertj.core.util.Lists;
import org.hibernate.validator.constraints.NotEmpty;
import org.jasig.inspektr.audit.annotation.Audit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.alibaba.fastjson.JSON;
import com.framework.springboot.utils.AuthorizationUtil;
import com.framewrok.springboot.web.RequestHelper;
import com.github.pagehelper.PageInfo;
import com.ictrade.tools.export.CsvFilePrinter;
import com.yikuyi.brand.model.ProductBrand;
import com.yikuyi.category.model.ProductCategory;
import com.yikuyi.category.model.ProductCategoryAlias;
import com.yikuyi.category.vo.ProductCategoryParent;
import com.yikuyi.material.vo.MaterialVo;
import com.yikuyi.material.vo.MaterialVo.MaterialVoType;
import com.yikuyi.message.MessageClientBuilder;
import com.yikuyi.message.task.AsyncTaskInfo;
import com.yikuyi.party.PartyClientBuilder;
import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.product.brand.manager.BrandManager;
import com.yikuyi.product.category.manager.CategoryManager;
import com.yikuyi.product.common.utils.BusiErrorCode;
import com.yikuyi.product.document.bll.DocumentLogManager;
import com.yikuyi.product.goods.dao.ProductStandExtendRepository;
import com.yikuyi.product.goods.dao.ProductStandRepository;
import com.yikuyi.product.log.manager.ProductMappingErrorManager;
import com.yikuyi.product.model.ProductDocument;
import com.yikuyi.product.model.ProductImage;
import com.yikuyi.product.model.ProductParameter;
import com.yikuyi.product.model.ProductStand;
import com.yikuyi.product.model.ProductStandAudit;
import com.yikuyi.product.model.ProductStandExtend;
import com.yikuyi.product.model.SpuWhiteList;
import com.yikuyi.product.vo.ExcelTemplate;
import com.yikuyi.product.vo.ProductStandRequest;
import com.yikuyi.product.vo.RawData;
import com.ykyframework.exception.BusinessException;
import com.ykyframework.exception.SystemException;
import com.ykyframework.model.IdGen;
import com.ykyframework.mqservice.sender.MsgSender;
import com.ykyframework.oss.AliyunOSSAccount;
import com.ykyframework.oss.AliyunOSSFileUploadSetting;
import com.ykyframework.oss.AliyunOSSFileUploadType;

/**
 * 
 * @author zr.wujiajun
 * @2017年2月23日
 */
@Service
public class ProductStandManager {
	private static final Logger logger = LoggerFactory.getLogger(ProductStandManager.class);
	
	@Autowired
	private ProductManager productManager;
	@Autowired
	private ProductStandRepository productStandRepository;
	

	@Autowired
	private ProductStandExtendRepository standExtendRepository;
	@Autowired
	private BrandManager brandManager;
	@Autowired
	private MongoOperations mongoOptions;
	@Autowired
	private CacheManager cacheManager;
	@Autowired
	private DocumentLogManager documentLogManager;
	@Autowired
	private MsgSender msgSender;
	@Autowired
	private CrawlerExceptionManager crawlerExceptionManager;
	@Autowired
	private ProductStandAuditManager productStandAuditManager;
	@Autowired
	private CategoryManager categoryManager;
	@Value("${mqProduceConfig.syncElasticsearchProduct.topicName}")
	private String syncElasticsearchProductTopicName;
	
	@Value("${dns-prefetchs}")
	private String dnsPrefetchs;
	
	@Autowired
	private ExcelExportManager excelExportManager;
	@Autowired
	private PartyClientBuilder partyClientBuilder;
	
	@Autowired
	private AuthorizationUtil authorizationUtil;
	
	@Autowired
	private MessageClientBuilder messageClientBuilder;
	
	@Value("${mqProduceConfig.parseImportFileSub.topicName}")
	private String parseImportFileTopicName;
	
	@Autowired
	@Qualifier(value = "aliyun.OSSFileUploadSetting")
	private AliyunOSSFileUploadSetting ossFileUploadSetting;
	
	@Autowired
	@Qualifier(value = "aliyun.oss.account")
	private AliyunOSSAccount aliyunOSSAccount;
	
	@Autowired
	private ProductMappingErrorManager productMappingErrorManager;
	
	// 注入HashOperations对象
	@Resource(name = "redisTemplate")
	private HashOperations<String, String, String> aliasSpuWhiteListOps;
	
	//特殊spu缓存
	public static final String SPU_WHITE_LIST_CACHE_KEY = "spuWhiteListCache";
	
	private static final String BRANCH_ERROR_DESC = "品牌非标准";
	private static final String CATEGORY_ERROR_DESC = "分类非标准";
	private static final String WAIT_AUDIT_ERROR_DESC = "物料待审核";
	
	/**
	 * 可信度高的供应商
	 */
	@Value("${credit.high.vendorId}")
	private String creditHighVendorIds;
	
	/**
	 * 最多查询20W条数据
	 */
	private static final Integer LIMIT_QUERY_CNT = 200000;
	
	public PageInfo<ProductStand> list(ProductStandRequest standRequest ,int page,int pageSize){
		//拼装查询条件以及排序
		Query query = this.mergeCondition(standRequest,null);
		if(!Optional.ofNullable(query).isPresent()){
			return new PageInfo<>(Collections.emptyList());
		}
		int queryPage = 0;
		if(page>0)
			queryPage = page-1;	
		Sort sort = new Sort(Direction.DESC,"updatedTimeMillis");
		long time1 = System.currentTimeMillis();
		query.with(sort);
		query.skip(queryPage * pageSize);
		query.limit(pageSize);
//		((Object) mongoOptions).setSocketTimeout(120000);//查询的时候加个超时时间
		List<ProductStand> productStands= mongoOptions.find(query, ProductStand.class);
		long total = mongoOptions.count(query, ProductStand.class);
		logger.info("2查询物料列表耗时:"+(System.currentTimeMillis()-time1));
		if(CollectionUtils.isEmpty(productStands)){
			return new PageInfo<>(Collections.emptyList());
		}
		//合并数据
		this.mergeData(productStands);
		PageInfo<ProductStand> pageResult = new PageInfo<>(productStands);		
		pageResult.setPageNum(page);
		pageResult.setPageSize(pageSize);
		pageResult.setTotal(total);		
		return pageResult;
	}
	
	/**
	 * 合并数据
	 * 
	 * @since 2017年8月16日
	 * @author injor.huang@yikuyi.com
	 */
	public void mergeData(List<ProductStand> productStands){
		List<String> userIds = Lists.newArrayList();
		List<String> spuIds = Lists.newArrayList();
		productStands.stream().forEach(stand -> {
			userIds.add(stand.getCreator());
			userIds.add(stand.getLastUpdateUser());
			spuIds.add(stand.getSpuId());
		});
		long time = System.currentTimeMillis();
		List<UserVo> userVos = Lists.newArrayList();
		try{
			//根据用户IDs查询批量查询用户信息
			userVos = partyClientBuilder.personClient().getPartyByIds(userIds, authorizationUtil.getLoginAuthorization());
		}catch(Exception e){
			logger.error("查询用户信息异常：",e);
		}
		logger.info("查询用户表耗时:"+(System.currentTimeMillis()-time));
		//根据用户spuId查询批量查询物料审核信息
		List<ProductStandAudit> audits = productStandAuditManager.getProductStandAuditBySpuId(spuIds);
		for (ProductStand origin : productStands) {
			if(StringUtils.isNotBlank(origin.getUpdatedTimeMillis())){
				origin.setUpdatedTimeMillis(DateFormatUtils.format(Long.valueOf(origin.getUpdatedTimeMillis()), "yyyy-MM-dd HH:mm:ss"));
			}
			
			if( CollectionUtils.isNotEmpty(audits)){
				audits.stream().forEach(audit -> {
					if(StringUtils.equals(origin.getId(), audit.getId())){
						origin.setAudit(audit.getAuditStatus());
					}
				});
			}
			if(CollectionUtils.isEmpty(userVos) ){
				continue;
			}
			userVos.stream().forEach(user -> {
				if(StringUtils.equalsIgnoreCase(origin.getCreator(), user.getPartyId())){
					origin.setCreatorName(user.getName());
				}
				if(StringUtils.equalsIgnoreCase(origin.getLastUpdateUser(), user.getPartyId())){
					origin.setUpdateName(user.getName());
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
	 * 拼装查询条件以及排序
	 * @param standRequest
	 * @param criteria
	 * @return
	 * @since 2017年8月25日
	 * @author injor.huang@yikuyi.com
	 */
	public Query mergeCondition(ProductStandRequest standRequest,Criteria criteria){
		/**使用正则表达式查询
		  * Criteria.where("Password").regex(re, options);其中re,option 都是字符串,
		  * option可以选值为：i,m,x,s   i表示不区分大小写，m表示能使用^以及$等正则表达式来识别数据库中使用\n换行的每一行开始字符以及字符。
		  * x
		  * 具体原文介绍http://docs.mongodb.org/manual/reference/operator/query/regex/
		  */
		Query query = new Query();
		Optional<Criteria> optCriteria = Optional.ofNullable(criteria);
		if(!optCriteria.isPresent()){
			criteria = new Criteria();
		}
		//不区分大小写，右模糊查询  栗子：Xxx%
		if(StringUtils.isNotBlank(standRequest.getManufacturerPartNumber())){
			criteria.and("manufacturerPartNumber").regex("(?i)^"+this.escapeExprSpecialWord(standRequest.getManufacturerPartNumber()));
			query.withHint("manufacturerPartNumber_1");//强制走索引
		}
		if(StringUtils.isNotBlank(standRequest.getManufacturerPartNumberExact())){
			criteria.and("manufacturerPartNumber").is(standRequest.getManufacturerPartNumberExact());
			query.withHint("manufacturerPartNumber_1");//强制走索引
		}
		if(StringUtils.isNotBlank(standRequest.getManufacturer())){
			Map<String,ProductBrand> map = brandManager.getAliasBrandMap();
			if(!Optional.ofNullable(map.get(standRequest.getManufacturer().toUpperCase())).isPresent()){
				return null;
			}
			criteria.and("manufacturerId").is(map.get(standRequest.getManufacturer().toUpperCase()).getId());
		}
			
		if(StringUtils.isNotBlank(standRequest.getAuditUserName())){
			List<UserVo> userVos = partyClientBuilder.personClient().getUserByName(standRequest.getAuditUserName(), authorizationUtil.getLoginAuthorization());
			if(CollectionUtils.isEmpty(userVos)){
				return null;
			}
			criteria.and("lastUpdateUser").in(userVos.stream().map(UserVo :: getId).collect(Collectors.toList()));
		}
		Optional<Integer> opt = Optional.ofNullable(standRequest.getStatus());
		if(opt.isPresent()){
			//有效的包含数据没有状态的
			if(ProductStand.Status.VALID.getValue() == opt.get()){
				criteria.and("status").ne(ProductStand.Status.INVALID.getValue());
			}else{
				criteria.and("status").is(opt.get());
			}
		}
		opt = Optional.ofNullable(standRequest.getCate1Name());
		if(opt.isPresent()){
			criteria.and("categories.0._id").is(standRequest.getCate1Name());
			
		}
		opt = Optional.ofNullable(standRequest.getCate2Name());
		if(opt.isPresent()){
			criteria.and("categories.1._id").is(standRequest.getCate2Name());
		}
		opt = Optional.ofNullable(standRequest.getCate3Name());
		if(opt.isPresent()){
			criteria.and("categories.2._id").is(standRequest.getCate3Name());
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			String startTime = "";
			String endTime = "";
			if(StringUtils.isNotBlank(standRequest.getStartDate()) && StringUtils.isNotBlank(standRequest.getEndDate())){
				startTime = String.valueOf(format.parse(standRequest.getStartDate()).getTime());
				endTime = String.valueOf(format.parse(format.format(DateUtils.addDays(format.parse(standRequest.getEndDate()),1))).getTime());
				criteria.and("updatedTimeMillis").gte(startTime).lt(endTime);
			}else if(StringUtils.isNotBlank(standRequest.getStartDate())){
				startTime = String.valueOf(format.parse(standRequest.getStartDate()).getTime());
				criteria.and("updatedTimeMillis").gte(startTime);
			}else if(StringUtils.isNotBlank(standRequest.getEndDate())){
				endTime = String.valueOf(format.parse(format.format(DateUtils.addDays(format.parse(standRequest.getEndDate()),1))).getTime());
				criteria.and("updatedTimeMillis").lt(endTime);
			}
		} catch (Exception e) {
			logger.error("时间格式化错误");
		}
		query.addCriteria(criteria);
		return query;
	}
	/**
	 * 查询单个物料数据
	 * @param id
	 * @return
	 * @since 2017年2月23日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public ProductStand getProductStand(String id) {
		ProductStand p = productStandRepository.findOne(id);
		List<ProductImage> spuImage = p.getImages();
		//图片地址加上前缀后返回
		List<ProductImage> spuImageNew = productManager.handleImageUrl(spuImage);
		p.setImages(spuImageNew);
		//物料扩展信息
		ProductStandExtend pse = standExtendRepository.findOne(id);
		p.setExtendInfo(pse);
		return p;
	}

	/**
	 * 查询spu信息
	 * 
	 * @param datas
	 * @param brandAliasMap
	 * @return
	 * @since 2016年12月9日
	 * @author tongkun@yikuyi.com
	 */
	public List<ProductStand> findProductStandByRawDatas(List<RawData> datas,Map<String,ProductBrand> brandAliasMap,MaterialVoType type) {
		//首先判断物料是否为特殊物料
		//如果是，就需要以特殊方式查询物料

		List<ProductStand> listInfo = productStandRepository.findProductStandBySpuId(this.getSpuIds(datas, brandAliasMap,type));
		//处理图片
		this.mergeImage(listInfo);
		return listInfo;
	}
	
	
	/**
	 * 获取spuIds
	 * @param datas
	 * @param brandAliasMap
	 * @return
	 * @since 2017年8月14日
	 * @author injor.huang@yikuyi.com
	 */
	public List<String> getSpuIds(List<RawData> datas,Map<String,ProductBrand> brandAliasMap,MaterialVoType type){
		List<String> keys = new ArrayList<>();
		for (RawData data : datas) {
			ProductBrand brand = productManager.getBrandName(data.getManufacturer(), brandAliasMap);
			String spuId = calSpuId(data,brand);
			spuId = spuId.toUpperCase();
			data.setSpuId(spuId);
			keys.add(spuId);
		}
		if(type != null && (StringUtils.equalsIgnoreCase(type.name(), MaterialVoType.UPDATE_DATA.name()))){
			//获取特殊spu
			Map<String,String>  specialSpuMap = this.getSpecialSpuMap(keys);
			if(MapUtils.isEmpty(specialSpuMap))
				return keys;
			for ( RawData data: datas) {
				String spuId = data.getSpuId();
				if(keys.contains(spuId) && StringUtils.isNotBlank(specialSpuMap.get(spuId))){
					String newSpuId = (spuId.concat("-").concat(data.getSkuId())).toUpperCase();
					data.setSpecialSpu(true);//特殊spu标记
					keys.remove(spuId);
					keys.add(newSpuId);
				}
			}
		}
		return keys;
	}

	/**
	 * 根据spuId来查询特殊spu
	 * @param alias
	 * @return
	 */
	public Map<String,String> getSpecialSpuMap(@NotEmpty Collection<String> keys){
		List<String> tempList = aliasSpuWhiteListOps.multiGet(SPU_WHITE_LIST_CACHE_KEY, keys);
		Iterator<String> iterator =  keys.iterator();
		int i = 0;
		Map<String,String> resultMap = new HashMap<>();
		while (iterator.hasNext()) {
			//过滤掉没有的数据
			if(tempList.get(i) != null){
				resultMap.put(iterator.next(), tempList.get(i));
			}else{
				iterator.next();//必须调用一次next，否则次数和总数会对应不上
			}
			i++;
		}
		return resultMap;
	}
	
	public List<ProductStandAudit> findProductStandAuditBySpuIds(List<String> spuIds){
		List<ProductStandAudit> listInfo =  productStandAuditManager.getProductStandAuditBySpuId(spuIds);
		//处理图片
		this.mergeImage(listInfo);
		return listInfo;
	}
	
	/**
	 * 处理图片
	 * @param listInfo
	 * @since 2017年8月15日
	 * @author injor.huang@yikuyi.com
	 */
	public void mergeImage(List<?> listInfo){
		//处理图片
		if(CollectionUtils.isEmpty(listInfo)){
			return;
		}
		List<ProductImage> spuImage = null;
			for(Object p : listInfo){
				if(p instanceof ProductStand){
					ProductStand productStand = (ProductStand) p;
					spuImage = productStand.getImages();
					//图片地址加上前缀后返回
					List<ProductImage> spuImageNew = productManager.handleImageUrl(spuImage);
					productStand.setImages(spuImageNew);
				}else{
					ProductStandAudit productStandAudit = (ProductStandAudit) p;
					spuImage = productStandAudit.getImages();
					//图片地址加上前缀后返回
					List<ProductImage> spuImageNew = productManager.handleImageUrl(spuImage);
					productStandAudit.setImages(spuImageNew);
				}
			}
	}
	
	/**
	 * 计算spuId
	 * @param data
	 * @param brand
	 * @return
	 * @since 2017年2月27日
	 * @author tongkun@yikuyi.com
	 */
	public static String calSpuId(Object data,ProductBrand brand){
		if(brand == null){
			brand = new ProductBrand();
		}
		if(data instanceof RawData){
			RawData rdata = (RawData)data;
			return (rdata.getManufacturerPartNumber()+"-"+(brand.getBrandShort()==null?brand.getBrandName():brand.getBrandShort())).toUpperCase();
		}else if(data instanceof ProductStand){
			ProductStand pdata = (ProductStand)data;
			return (pdata.getManufacturerPartNumber()+"-"+(brand.getBrandShort()==null?brand.getBrandName():brand.getBrandShort())).toUpperCase();
		}else{
			return null;
		}
	}
	
	/**
	 * 根据spuid查询spu信息
	 * @param manufacturerPartNumber
	 * @return
	 * @since 2017年2月8日
	 * @author zr.wanghong
	 */
	public List<ProductStand> findProductStandBySpuId(RawData data,ProductBrand brand){
		List<String> keys = new ArrayList<>();
		if(data.getSpuId()==null)
			keys.add(ProductStandManager.calSpuId(data, brand));
		else
			keys.add(data.getSpuId());
		return productStandRepository.findProductStandBySpuId(keys);
	}
	
	/**
	 * 根据型号查询spu信息
	 * @param manufacturerPartNumber
	 * @return
	 * @since 2017年2月8日
	 * @author zr.wanghong
	 */
	public List<ProductStand> findProductStandByNo(String manufacturerPartNumber){
		return productStandRepository.findProductStandByNo(manufacturerPartNumber);
	}

	/**
	 * 根据raw创建商品spu
	 * 
	 * @param data
	 *            rawData
	 * @param brandAliasMap
	 *            品牌别名映射
	 * @param categoryAliasMap
	 *            类别别名映射
	 * @param oriProductStand
	 *            原spu
	 * @return 创建的商品spu
	 * @since 2016年12月9日
	 * @author tongkun@yikuyi.com
	 * @throws BusinessException 
	 */
	public ProductStand createProductStand(RawData data, Map<String, ProductBrand> brandAliasMap,
			Map<String, ProductCategoryParent> categoryAliasMap, 
			ProductStand oriProductStand,MaterialVoType type,Set<String> autoIntegrateQtyVendorIds) throws BusinessException {
		ProductStand result = oriProductStand;
		//是否是修改物料 （如果物料已经存在就是修改）
		Boolean isModify = true;
		Date date =new Date();
		if (result == null) {
			result = new ProductStand();
			// 如果不能生成标准数据，则不生成id
			if(data.getCantCreateStand()!=null && !data.getCantCreateStand()){
				isModify = false;
				result.setId(String.valueOf(IdGen.getInstance().nextId()));
			}
			result.setCreatedTimeMillis(Long.toString(date.getTime()));//创建时间
			result.setCreatedDate(date);
			result.setCreator(data.getUserId());
		}
		result.setUpdatedTimeMillis(Long.toString(date.getTime()));//更新时间
		result.setLastUpdateDate(date);
		result.setLastUpdateUser(data.getUserId());
		// 型号
		if (data.getManufacturerPartNumber() != null) {
			result.setManufacturerPartNumber(data.getManufacturerPartNumber());
		}
		// 来源更新
		if(data.getFrom()!=null){
			result.setFrom(data.getFrom());
		}else if(data.getVendorId()!=null){
			result.setFrom(data.getVendorId());
		}else if(oriProductStand!=null && oriProductStand.getFrom()!=null){
			result.setFrom(oriProductStand.getFrom());
		}
		// 品牌
		ProductBrand brand = null;
		
		if (oriProductStand==null && data.getManufacturer() != null) {
			brand = brandAliasMap.get(brandManager.getAliasKey(data.getVendorId(), data.getManufacturer()));
			if(brand==null){
				brand = brandAliasMap.get(brandManager.getAliasKey(null, data.getManufacturer()));
			}
			//如果不是标准物料且是品牌则赋予标准品牌
			if (brand != null) {
				result.setManufacturer(brand.getBrandName());
				result.setManufacturerId(brand.getId());
				result.setManufacturerAgg(brand.getBrandName());
				result.setManufacturerShort(brand.getBrandShort());
			} 
			//不创建物料且不是标准品牌则赋予品牌名  
			//商品上传或爬虫才会走这个判断
			else if(type != null && (StringUtils.equalsIgnoreCase(type.name(), MaterialVoType.UPDATE_DATA.name())
					|| StringUtils.equalsIgnoreCase(type.name(), MaterialVoType.FILE_UPLOAD.name()))){
				logger.info("商品上传或爬虫品牌没匹配到存入到待标准化表=型号={}，品牌={}，供应商={}",data.getManufacturerPartNumber(),data.getManufacturer(),data.getVendorId());
				result.setManufacturer(data.getManufacturer());
				brand = new ProductBrand();
				brand.setBrandName(data.getManufacturer());
				//爬虫过来未映射的品牌需要存储仅限可信度高的供应商
				//或者是自动集成库供应商
				if(Arrays.asList(creditHighVendorIds.split(",")).contains(data.getVendorId())
						|| (CollectionUtils.isNotEmpty(autoIntegrateQtyVendorIds) && autoIntegrateQtyVendorIds.contains(data.getVendorId()))){
					productMappingErrorManager.addBrand(data.getVendorId(),data.getVendorName(),data.getManufacturer());
				}
				data.setCantCreateStand(true);
			}
			//创建物料且不是标准品牌则报错 
			else{
				if(data.getCantCreateStand()!=null && !data.getCantCreateStand() && StringUtils.isNotBlank(data.getSkuId())){
					//插入错误信息到异常日志表
					crawlerExceptionManager.insertErrorlog(data, result.getFrom(), data.getSpuId()!=null?data.getSpuId():calSpuId(data, brand), ProductStandManager.BRANCH_ERROR_DESC);
				}
				throw new BusinessException("NO_BRAND");
			}
		}
		// spu id
		if (data.getSpuId()!=null){
			result.setSpuId(data.getSpuId());
		}else{
			result.setSpuId(calSpuId(data, brand));
		}
		// 分类
		if (CollectionUtils.isNotEmpty(data.getVendorCategories())) {
			//分类验证
			this.categoryValidate(data, result, categoryAliasMap, type, isModify);
		}

		// rohs
		if(null == data.getRohs()){
			result.setRohs(null);
		}else if (data.getRohs() != null) {
			result.setRohs(Boolean.valueOf(data.getRohs()));
		}
		// 限制物料
		if(StringUtils.isNotBlank(data.getRestrictMaterialType())){
			result.setRestrictMaterialType(data.getRestrictMaterialType());
		}else if(StringUtils.isBlank(result.getRestrictMaterialType())){
			result.setRestrictMaterialType(ProductStand.RestrictMaterialType.NO.getValue());//默认非限制物料
		}
		// 标准描述
		if (StringUtils.isNotBlank(data.getDescription())) {
			result.setDescription(data.getDescription());
		}
		// 文档
		if (data.getDocuments() != null && !data.getDocuments().isEmpty()) {
			result.setDocuments(data.getDocuments());
		} else if(result.getDocuments() == null) {
			result.setDocuments(new ArrayList<ProductDocument>());
		}
		// 图片
		if (data.getImages() != null && !data.getImages().isEmpty()) {
			result.setImages(data.getImages());
		} else if(result.getImages() == null){
			result.setImages(new ArrayList<ProductImage>());
		}
		// 参数
		if (data.getParameters() != null && !data.getParameters().isEmpty()) {
			result.setParameters(data.getParameters());
			/*List<ProductParameter> productParameters = data.getParameters();
			List<ProductParameter> orignParameters = result.getParameters();
			if(CollectionUtils.isNotEmpty(orignParameters)){
				// 把新参数值合并到旧参数值里面
				this.mergeParameterData(productParameters, orignParameters);
			}else{
				result.setParameters(productParameters);
			}*/
		} else if(result.getParameters() == null){
			result.setParameters(new ArrayList<ProductParameter>());
		}
		return result;
	}
	
	/**
	 * 分类验证
	 * @param data
	 * @param result
	 * @param categoryAliasMap
	 * @param type
	 * @param isModify
	 * @throws BusinessException
	 */
	public void categoryValidate(RawData data,ProductStand result,Map<String, ProductCategoryParent> categoryAliasMap,
			MaterialVoType type,boolean isModify) throws BusinessException{
		//获取解析出来的分类
		ProductCategoryParent c3 = this.getParseCategory(data.getVendorCategories(), categoryAliasMap);
		// 如果有确定的分类,由次小类找小类和大类
		if (c3 != null) {
			//是否修改 并且状态是无效，分类是未分类  就不能替换分类内容  
			// !isModify 代表新增数据，所以是赋值
			//如果是爬虫进来的数据也也赋值
			if(c3.getId() > 0 || !isModify ||
					(type != null && StringUtils.equalsIgnoreCase(type.name(), MaterialVoType.UPDATE_DATA.name()))){
				List<ProductCategory> list = new ArrayList<>();
				ProductCategoryParent c2 = c3.getParent();
				ProductCategory c1 = c2.getParent();
				list.add(new ProductCategory(c1));
				list.add(new ProductCategory(c2));
				list.add(new ProductCategory(c3));
				result.setCategories(list);
			}
		}
		//商品上传或爬虫才会走这个判断
		else if(type != null && (StringUtils.equalsIgnoreCase(type.name(), MaterialVoType.UPDATE_DATA.name())
				|| StringUtils.equalsIgnoreCase(type.name(), MaterialVoType.FILE_UPLOAD.name()))){
			logger.info("商品上传或爬虫分类没匹配到存入到待标准化表型号={},供应商名={},分类={}",data.getManufacturerPartNumber(),data.getVendorName(),JSON.toJSONString(data.getVendorCategories()));
			//爬虫过来未映射的分类需要存储仅限可信度高的供应商
			if(Arrays.asList(creditHighVendorIds.split(",")).contains(data.getVendorId())){
				productMappingErrorManager.addCategory(data.getVendorId(),data.getVendorName(), data.getVendorCategories());
			}
			//未匹配到默认是未分类
			if(StringUtils.isBlank(result.getId()))
				this.setDefaultCategories(result);
		}
		//如果创建标准物料没有标准分类则要报错
		else if(result.getId()!=null){
			if(data.getCantCreateStand()!=null && !data.getCantCreateStand() && StringUtils.isNotBlank(data.getSkuId())){
				//插入错误信息到异常日志表
				crawlerExceptionManager.insertErrorlog(data, result.getFrom(), data.getSpuId(), ProductStandManager.CATEGORY_ERROR_DESC);
			}
			//未匹配到默认是未分类
			this.setDefaultCategories(result);
			throw new BusinessException("NO_CATEGORY");
		}
	
	}
	/**
	 * 获取解析出来的分类数据
	 * @param data
	 * @param categoryAliasMap
	 * @return
	 */
	public ProductCategoryParent getParseCategory(List<ProductCategory> categories,Map<String, ProductCategoryParent> categoryAliasMap){
		if(CollectionUtils.isEmpty(categories)){
			return null;
		}
		ProductCategoryAlias alias = new ProductCategoryAlias();
		ProductCategoryAlias aliasLast = new ProductCategoryAlias();
		Integer minLevel = -1;
		for (ProductCategory cate : categories) {
			Integer level = cate.getLevel();
			if(level==null)
				continue;
			if (minLevel<level) {
				alias.setLevel1(alias.getLevel2());
				alias.setLevel2(cate.getName().toUpperCase());
				aliasLast.setLevel2(cate.getName().toUpperCase());
				minLevel = level;
			}
		}
		//类别
		ProductCategoryParent c3 = categoryAliasMap.get(String.valueOf(alias.hashCode()));//根据小类和次小类来找数据
		//如果根据小类和次小类定位不到，则只根据次小类来定位
		if(c3==null){
			c3 = categoryAliasMap.get(String.valueOf(aliasLast.hashCode()));
		}
		return c3;
	}
	
	/**
	 * 设置未分类
	 * @param result
	 */
	public void setDefaultCategories(ProductStand result){
		List<ProductCategoryParent> categoryParents = categoryManager.getDefaultCategories();
		if(CollectionUtils.isEmpty(categoryParents)){
			return;
		}
		List<ProductCategory> list = new ArrayList<>();
		categoryParents.stream().forEach(a -> {
			list.add(new ProductCategory(a));
		});
		result.setCategories(list);
	}
	
	
	/**
	 * 把新参数值合并到旧参数值里面
	 * @param parameters
	 * @param orignParameters
	 * @return
	 * @since 2017年8月31日
	 * @author injor.huang@yikuyi.com
	 */
	public List<ProductParameter> mergeParameterData(List<ProductParameter> parameters,List<ProductParameter> orignParameters){
		Map<String,Object> map = new HashMap<String, Object>();
		for (ProductParameter orign : orignParameters) {
			map.put(orign.getName(), orign);
		}
		for (ProductParameter param : parameters) {
			if(map.get(param.getName().trim()) != null){//包含
				//删除原来的数据
				orignParameters.remove(map.get(param.getName().trim()));
			}
			orignParameters.add(param);
		}
		return orignParameters;
	}
	
	/**
	 * 批量保存标准物料
	 * @param list
	 * @since 2017年2月28日
	 * @author tongkun@yikuyi.com
	 */
	public void saveProductStands(List<ProductStand> list){
		if(CollectionUtils.isEmpty(list)) 
			return;
		//保存物料扩展信息
		List<ProductStandExtend> extendInfos = new ArrayList<>();
		list.stream().forEach(stand -> {
			if(stand.getExtendInfo() != null){
				ProductStandExtend extendInfo = stand.getExtendInfo();
				extendInfo.setId(stand.getId());
				extendInfos.add(extendInfo);
				//清除扩展信息，不冗余到物料表
				stand.setExtendInfo(null);
			}
		});
		standExtendRepository.save(extendInfos);
		productStandRepository.save(list);
	}
	
	/**
	 * 删除一个标准物料
	 * @param ps
	 * @since 2017年3月4日
	 * @author tongkun@yikuyi.com
	 */
	public void deleteProductStand(ProductStand ps){
		productStandRepository.delete(ps);
	}
	
	/**
	 * 根据品牌id更新物料中的品牌
	 * @param productStand 
	 * @return 所有影响的商品数据
	 * @since 2017年3月21日
	 * @author tongkun@yikuyi.com
	 * @throws BusinessException 
	 */
	public void updateProductStandByBrandId(ProductStand productStand) throws BusinessException{
//		//没有id则报错
//		if(productStand.getManufacturerId()==null){
//			throw new BusinessException("NO_ID");
//		}
//		//获取品牌
//		ProductBrand pb = brandManager.findById(productStand.getManufacturerId());
//		//没有数据
//		if(pb == null){
//			throw new BusinessException("NO_DATA");
//		}
//		//通过品牌查询到物料
//		List<ProductStand> pss = productStandRepository.findProductStandByManufacturerId(productStand.getManufacturerId());
//		//每一个物料进行更新
//		for(ProductStand ps : pss){
//			mongoOptions.updateFirst(query(where("_id").is(ps.getId())), update("manufacturer",pb.getBrandName()).set("manufacturerAgg", pb.getBrandName()).set("spuId", ProductStandManager.calSpuId(ps, pb)), ProductStand.class);			
//		}
	}
	
	/**
	 * 更新物料的数据
	 * @param productStand
	 * @since 2017年3月30日
	 * @author tb.lijing@yikuyi.com
	 */
	public void updateProductStand(@RequestBody ProductStand productStand) {
		try {
			updateProductStandByBrandId(productStand);
		} catch (BusinessException e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 创建物料
	 * @param rawdatas
	 * @return
	 * @since 2017年3月30日
	 * @author tb.lijing@yikuyi.com
	 */
	public String createProductStand(@RequestBody List<RawData> rawdatas) {
		logger.info("创建标准物料开始");
		Date time1 = new Date();
		//回调用的vo
		MaterialVo materialVo = new MaterialVo();
		materialVo.setType(MaterialVo.MaterialVoType.BASIS_FILE_UPLOAD);
		materialVo.setDocId(rawdatas.get(0).getProcessId());
		materialVo.setSize(rawdatas.size());
		logger.info("开始加载缓存");
		//获得分类映射
		Map<String, ProductCategoryParent> categoryAliasMap = null;
		Map<String, ProductBrand> brandAliasMap = null;
		try{
			//获得品牌映射
			List<String> keys = new ArrayList<>();
			rawdatas.stream().forEach(v->{
				if(v.getManufacturer()!=null){
					if(v.getVendorId()!=null)
						keys.add(brandManager.getAliasKey(v.getVendorId(), v.getManufacturer()));
					keys.add(brandManager.getAliasKey(null, v.getManufacturer()));
				}
			});
			brandAliasMap = brandManager.getBrandByAliasName(keys);
			Set<String> cahceKey = new HashSet<>();
			rawdatas.stream().forEach(v -> cahceKey.addAll(RawData.getVendorCategoryRedisKey(v.getVendorCategories())));
			categoryAliasMap = categoryManager.getCategoryByAliasName(cahceKey);// 获得分类映射
			cahceKey.clear();
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			throw new SystemException("Exception",e);
		}finally{
			//分类加载失败的异常
			if(categoryAliasMap==null){
				documentLogManager.updateDocLogsStatusByRawAndSheet(rawdatas.stream().map(vo -> vo.setErrorMsgRst("分类数据无法加载")).collect(Collectors.toList()));
				msgSender.sendMsg(syncElasticsearchProductTopicName, materialVo, null);
			}
		}
		//获取spuIds
		List<String> spuIds = this.getSpuIds(rawdatas, brandAliasMap,materialVo.getType());
		//获取物料审核数据
		List<ProductStandAudit> productStandAudits = this.findProductStandAuditBySpuIds(spuIds);
		logger.info("加载缓存完毕，开始查询原表数据");
		Date time2 = new Date();
		//原spu
		List<ProductStand> oriProductStands = null;
		try{
			oriProductStands = findProductStandByRawDatas(rawdatas, brandAliasMap,materialVo.getType());
		}catch(Exception e){
			logger.error("Exception",e);
			throw new SystemException("Exception",e);
		}finally{
			//原spu加载失败的异常
			if(oriProductStands==null){
				documentLogManager.updateDocLogsStatusByRawAndSheet(rawdatas.stream().map(vo -> vo.setErrorMsgRst("原spu数据无法加载")).collect(Collectors.toList()));
				msgSender.sendMsg(syncElasticsearchProductTopicName, materialVo, null);
			}
		}
		logger.info("查询原表数据结束，开始获取新数据");
		Date time3 = new Date();
		//循环原始数据
		List<ProductStand> standList = new ArrayList<>();//不需要审核的物料
		List<ProductStandAudit> standAuditList = new ArrayList<>();//需要审核的物料
		List<RawData> errorList = new ArrayList<>();//处理失败的list
		if(oriProductStands == null){
			oriProductStands = new ArrayList<>();
		}		
		for(RawData data:rawdatas){
			try {
				// 查找用对象
				RawData rawData = new RawData();
				rawData.setSpuId(data.getSpuId());
				//物料审核表 和物料表都没有的数据是直接插入到物料表，不需要审核
				boolean isAudit = false;//默认不审核
				// 查找原spu
				ProductStand oriProductStand = null;
				int index = oriProductStands.indexOf(rawData);
				if(index>=0){
					isAudit =true;
					oriProductStand = oriProductStands.get(index);
				}
				//如果是新数据或者是来源和数据库现有的数据来源一致，创建或更新数据
				//如果是digikey或者mouser标准数据已经存在，则不进行修改直接返回
				if (index < 0 ||
						(StringUtils.isNotBlank(data.getFrom()) && oriProductStand != null && data.getFrom().equals(oriProductStand.getFrom()))) {
					data.setCantCreateStand(false);
					//1.根据spuId查询 product_stand_audit存在 并且状态为"待审核"，就提示，物料应该提示，未审核，请勿重复上传
					//判断在物料审核表是否存在
					productStandAuditManager.existProductStandAudit(productStandAudits, data, data.getFrom(),ProductStandManager.WAIT_AUDIT_ERROR_DESC);
					//根据raw创建商品spu 以及数据验证组装
					ProductStand ps = this.createProductStand(data, brandAliasMap, categoryAliasMap, oriProductStand,null,null);
					if(isAudit){
						ProductStandAudit audit = new ProductStandAudit();
						BeanUtils.copyProperties(ps, audit);
						audit.setStatus(ProductStand.Status.VALID.getValue());//默认是有效
						audit.setAuditStatus(ProductStandAudit.AuditStatus.WAIT_AUDIT.getValue());//待审核
						standAuditList.add(audit);
					}else{
						ps.setStatus(ProductStand.Status.VALID.getValue());//默认是有效
						standList.add(ps);
					}
					
				}else{
					data.setErrorMsg("已存在的数据无法覆盖");
					errorList.add(data);
				}
			} catch(BusinessException e){
				if("NO_BRAND".equals(e.getCode()))
					data.setErrorMsg(ProductStandManager.BRANCH_ERROR_DESC);
				else if("NO_CATEGORY".equals(e.getCode()))
					data.setErrorMsg(ProductStandManager.CATEGORY_ERROR_DESC);
				else if("WAIT_AUDIT".equals(e.getCode()))
					data.setErrorMsg(ProductStandManager.WAIT_AUDIT_ERROR_DESC);
				errorList.add(data);
			} catch (Exception e) {
				data.setErrorMsg(e.getMessage());
				errorList.add(data);
				logger.error(e.getMessage(),e);
			}
		}
		logger.info("获取新数据完毕，开始保存数据到数据库");
		Date time4 = new Date();
		//保存到数据库
		try{
			//不用审核直接保存到物料表
			this.saveProductStands(standList);
			//需要审核
			productStandAuditManager.insertProductStandAudits(standAuditList);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			errorList = this.dealErrorList(standList, standAuditList, rawdatas, e);
		}
		//更新错误日志
		if(!errorList.isEmpty()){
			documentLogManager.updateDocLogsStatusByRawAndSheet(errorList);
		}
		logger.info("保存数据到数据库完毕");
		Date time5 = new Date();
		logger.info("执行时间：加载缓存耗时："+(time2.getTime()-time1.getTime())+" 查询原表数据耗时:"+(time3.getTime()-time2.getTime())+" 获取新数据耗时："+(time4.getTime()-time3.getTime())+" 保存数据耗时："+(time5.getTime()-time4.getTime()));
		msgSender.sendMsg(syncElasticsearchProductTopicName, materialVo, null);
		return "";
	}
	
	/**
	 * 保存错误日志
	 * @param standList
	 * @param standAuditList
	 * @param rawdatas
	 * @param e
	 * @return
	 * @since 2017年8月16日
	 * @author injor.huang@yikuyi.com
	 */
	public List<RawData> dealErrorList(List<ProductStand> standList,List<ProductStandAudit> standAuditList,List<RawData> rawdatas,Exception e){
		List<RawData> errorList = new ArrayList<>();//处理失败的list
		if(CollectionUtils.isNotEmpty(standList)){
			for(ProductStand ps:standList){
				RawData data = rawdatas.get(rawdatas.indexOf(ps));
				data.setErrorMsg(e);
				errorList.add(data);
			}
		}
		if(CollectionUtils.isNotEmpty(standAuditList)){
			for(ProductStandAudit ps:standAuditList){
				RawData data = rawdatas.get(rawdatas.indexOf(ps));
				data.setErrorMsg(e);
				errorList.add(data);
			}
		}
		return errorList;
	}
	/**
	 * 导出excel
	 * @return
	 * @since 2017年8月18日
	 * @author injor.huang@yikuyi.com
	 */
	public File exportExcelByCondition(ProductStandRequest standRequest, String ids,String fileName)throws BusinessException {
		//根据查询 条件获取物料数据
		return this.getProductStandByCondition(standRequest,fileName,ids,LIMIT_QUERY_CNT);
//		if(CollectionUtils.isEmpty(stands)){
//			return null;
//		}
//		stands.sort((x,y) ->{
//			if(y.getUpdatedTimeMillis() != null && x.getUpdatedTimeMillis() != null){
//				return y.getUpdatedTimeMillis().compareTo(x.getUpdatedTimeMillis());
//			}
//			return 0;
//		});
//		return excelExportManager.exportProductStandDataFile(stands,fileName);
	}
	
	/**
	 * 根据查询 条件获取物料数据
	 * @param manufacturerPartNumber
	 * @param manufacturer
	 * @param startDate
	 * @param endDate
	 * @param status
	 * @param auditStatus
	 * @param cate1Name
	 * @param cate2Name
	 * @param cate3Name
	 * @param ids
	 * @return
	 * @since 2017年8月18日
	 * @author injor.huang@yikuyi.com
	 */
	public File getProductStandByCondition(ProductStandRequest standRequest,String fileName, String ids,Integer limitQueryCnt)throws BusinessException{
		File file = FileUtils.getFile(fileName);
		CsvFilePrinter filePrinter;
		try {
			filePrinter = new CsvFilePrinter(file,fileName, ExcelTemplate.Template.EXPORT_PRODUCT_STAND_TEMPLATE.getValue().split(","));
		} catch (IOException e) {
			logger.error("生成模板失败:{}",e);
			throw new BusinessException("生成模板失败:"+e.getMessage());
		}
		long time = System.currentTimeMillis();
		if(StringUtils.isNotBlank(ids)){
			List<ProductStand> productStands = productStandRepository.findProductStandByIds(Arrays.asList(ids.split(",")));
			//写文件
			excelExportManager.writeMaterialData(productStands, filePrinter);
		}else{
			Query query = this.mergeCondition(standRequest,null);
			if(!Optional.ofNullable(query).isPresent()){
				return null;
			}
			//根据查询条件写文件数据
			this.writeProductStandByCondition(query,standRequest.getCate1Name(), filePrinter, limitQueryCnt);
		}
		 logger.info("写csv,耗时:"+(System.currentTimeMillis()-time));
		return file;
	}
	
	/**
	 * 根据查询条件写文件数据
	 * 
	 * @since 2017年9月25日
	 * @author injor.huang@yikuyi.com
	 */
	public void writeProductStandByCondition(Query query,Integer cate1Name,CsvFilePrinter filePrinter,Integer limitQueryCnt)throws BusinessException{
		Sort sort = new Sort(Direction.DESC,"updatedTimeMillis");
		int size = 1000;//最大可以查询1佰W条数据
		int pageSize = 1000;
		for (int i = 0; i < size; i++) {
			PageRequest pageable = new PageRequest(i,pageSize,sort);//限制最大条数20W
			Page<ProductStand> page = productStandRepository.listProductStandByPage(query.getQueryObject(),pageable);
			if(CollectionUtils.isEmpty(page.getContent())){
				break;
			}
			//写文件
			excelExportManager.writeMaterialData(page.getContent(), filePrinter);
			long total = page.getTotalElements();
			if((i+1)*pageSize >= total){
				break;
			}
			if(limitQueryCnt != null && (i+1)*pageSize >= limitQueryCnt){
				break;
			}
		}
	}
	
	/**
	 * 导出
	 * @param productStandRequest
	 * @since 2017年9月21日
	 * @author injor.huang@yikuyi.com
	 * @throws BusinessException 
	 */
	@Audit(action = "Material Exportqqq;;;'#id'qqq;;;'#userName'导出", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public void export(ProductStandRequest productStandRequest,
			@com.framework.springboot.audit.Param("userName")String userName) throws BusinessException {
		//检验能否下载
		String key = JSON.toJSONString(productStandRequest) + RequestHelper.getLoginUserId();
		Cache cache = cacheManager.getCache("materialExport");
		ValueWrapper valueWrapper = cache.get(key);
		if(valueWrapper != null){
			throw new BusinessException("请勿重复导出");
		}
		cache.put(key, key);
		//创建下载任务
		AsyncTaskInfo asyncTaskInfo = this.createTask(AsyncTaskInfo.BizType.MATERIAL.name(),AsyncTaskInfo.Action.DOWNLOAD.name());
		productStandRequest.setTaskId(asyncTaskInfo.getId());
		//异步调用下载
		msgSender.sendMsg(parseImportFileTopicName,productStandRequest, null);
	}
	
	/**
	 * 创建下载任务
	 * 
	 * @since 2017年9月21日
	 * @author injor.huang@yikuyi.com
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
	 * 执行下载任务
	 * @param productStandRequest
	 * @since 2017年9月21日
	 * @author injor.huang@yikuyi.com
	 */
	public void doExport(ProductStandRequest productStandRequest) {
		AsyncTaskInfo asyncTaskInfo = new AsyncTaskInfo();
		asyncTaskInfo.setId(productStandRequest.getTaskId());
		try{
			asyncTaskInfo.setStatus(AsyncTaskInfo.Status.PROCESSING.name());//处理中
			messageClientBuilder.asyncTaskResource().update(asyncTaskInfo,authorizationUtil.getMockAuthorization());
			//根据查询条件生成本地文件
			File excelFile = this.exportExcelByCondition(productStandRequest,productStandRequest.getIds(), "material"+System.currentTimeMillis()+".csv");
			if(excelFile == null){
				throw new BusinessException("export file error");
			}
			asyncTaskInfo.setStatus(AsyncTaskInfo.Status.SUCCESS.name());//默认成功
	    	//上传本地文件到阿里云并获取文件路径
			asyncTaskInfo.setUrl(this.uploadFileAliyun(excelFile,"material.export"));
			Date date =new Date();
			asyncTaskInfo.setEndTime(date);
			asyncTaskInfo.setLastUpdateDate(date);
		}catch(BusinessException e){
			logger.error("material task {}update fail：{}",productStandRequest.getTaskId(),e);
			asyncTaskInfo.setStatus(AsyncTaskInfo.Status.FAIL.name());
			asyncTaskInfo.setLastUpdateDate(new Date());
			asyncTaskInfo.setMessage(e.getCode());
		}
		//更新任务状态
		messageClientBuilder.asyncTaskResource().update( asyncTaskInfo,authorizationUtil.getMockAuthorization());
	}
	
	/**
	 * 上传本地文件到阿里云并获取文件路径
	 * @param excelFile
	 * @return
	 * @throws BusinessException
	 * @since 2017年9月21日
	 * @author injor.huang@yikuyi.com
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
	/**
	 * 合并物料数据
	 */
	public void mergeProductStandData(ProductStand ps){
		 Date date = new Date();
		 //ps = productStandManager.createProductStand()有对 是否能生成ID做判断，没有去那个方法里面修改。感觉改不动了。故在这里生成新的id
		 if(StringUtils.isBlank(ps.getId())){
			 ps.setId(String.valueOf(IdGen.getInstance().nextId()));
			 ps.setCreatedTimeMillis(String.valueOf(date.getTime()));
			 ps.setCreatedDate(date);
		 }
		 ps.setStatus(ProductStand.Status.VALID.getValue());
		 ps.setUpdatedTimeMillis(String.valueOf(date.getTime()));
		 ps.setLastUpdateDate(date);
		//设置hashCode
		 ps.setObjHashCode(this.getHashValue(ps));
	}

	/**
	 * 批量验证型号是否存在
	 * @param manufacturerPartNumberList
	 * @return
	 * @since 2017年11月27日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public List<String> existManufacturerPartNumberList(List<String> manufacturerPartNumberList) {
		List<String> resultList = new ArrayList<>();
		try {
			if(CollectionUtils.isEmpty(manufacturerPartNumberList)){
					throw new BusinessException(BusiErrorCode.MANUFACTUREPARTNUMBER_LIST_NULL,"信号信息不存在");
			}
			//将集合中的小写全部转化为大写
			List<String> manuList = new ArrayList<>();
			Iterator<String> it = manufacturerPartNumberList.iterator();
			while(it.hasNext()){
				manuList.add(String.valueOf(it.next()).toUpperCase());
			}
			List<ProductStand> productStandsList = productStandRepository.existManufacturerPartNumberList(manuList);
			if(CollectionUtils.isNotEmpty(productStandsList)){
				List<String> manguRelustList = new ArrayList<>();
				for (int i = 0; i < productStandsList.size(); i++) {
					manguRelustList.add(productStandsList.get(i).getManufacturerPartNumber());
				}
				for (int j = 0; j < manuList.size(); j++) {
					if(CollectionUtils.isNotEmpty(manguRelustList)){
						if(manguRelustList.contains(manuList.get(j))){
							resultList.add("true");
						}else{
							resultList.add("false");
						}
					}
				}
			}else{
				for (int i = 0; i < manufacturerPartNumberList.size(); i++) {
					resultList.add("false");
				}
			}
			return resultList;
		} catch (Exception e) {
			logger.error("批量验证型号是否存在:{}", e);
		}
		return new ArrayList<>();
	}
	
	/**
	 * 是否能修改物料
	 * @param stand
	 * @param origStand
	 * @return
	 */
	public boolean isCanUpdate(ProductStand stand,ProductStand origStand){
		boolean restul_b = false;
		int hashCode = this.getHashValue(stand);
		if(origStand.getObjHashCode() == 0 || origStand.getObjHashCode() != hashCode){
			restul_b = true;
		}
		return restul_b;
	}
	
	/**
	 * 获取 hashcode
	 * @param origStand
	 * @return
	 */
	public int getHashValue(ProductStand origStand){
		ProductStand stand = new ProductStand();
		try {
			BeanUtils.copyProperties(origStand, stand);
		} catch (Exception e) {
			logger.error("stand set value exception:{}",e);
			return 0;
		}
		stand.setCreatedDate(null);
		stand.setCreatedTimeMillis(null);
		stand.setCreator(null);
		stand.setCreatorName(null);
		stand.setUpdatedTimeMillis(null);
		stand.setUpdateName(null);
		stand.setLastUpdateDate(null);
		stand.setLastUpdateUser(null);
		stand.setFrom(null);
		stand.setObjHashCode(0);
		return JSON.toJSONString(stand).hashCode();
	}
	
	/**
	 * 新增特殊SPU
	 * @param manufacturerPartNumber
	 * @param manufacturerId
	 * @since 2017年12月8日
	 * @author tb.lijing@yikuyi.com
	 * @throws BusinessException 
	 */
	public void saveWhiteList(SpuWhiteList spuWhiteList) throws BusinessException{
		List<SpuWhiteList> result = mongoOptions.find(new Query(new Criteria("manufacturerPartNumber").is(spuWhiteList.getManufacturerPartNumber()).and("manufacturerId").is(spuWhiteList.getManufacturerId())), SpuWhiteList.class);
		if(CollectionUtils.isNotEmpty(result)){
			throw new BusinessException(BusiErrorCode.SPU_WHITE_LIST_EXIST, "数据已存在，请修改数据！");
		}
		String userId = RequestHelper.getLoginUserId();
		String userName = RequestHelper.getLoginUser().getUsername();
		String id = String.valueOf(IdGen.getInstance().nextId());
		String currentTimeMillis = Long.toString(new Date().getTime());
		spuWhiteList.setId(id);
		spuWhiteList.setCreator(userId);
		spuWhiteList.setCreatorName(userName);
		spuWhiteList.setLastUpdateUser(userId);
		spuWhiteList.setLastUpdateUserName(userName);
		spuWhiteList.setCreatedTimeMillis(currentTimeMillis);
		spuWhiteList.setUpdatedTimeMillis(currentTimeMillis);
		mongoOptions.save(spuWhiteList);
		StringBuilder str = new StringBuilder();
		str.append(spuWhiteList.getManufacturerPartNumber());
		str.append("-");
		str.append(null==spuWhiteList.getBrandShort()?spuWhiteList.getManufacturer():spuWhiteList.getBrandShort());
		aliasSpuWhiteListOps.put(SPU_WHITE_LIST_CACHE_KEY, str.toString().toUpperCase(), str.toString().toUpperCase());
	}
	
	/**
	 * 查询特殊SPU
	 * @return
	 * @since 2017年12月8日
	 * @author tb.lijing@yikuyi.com
	 */
	public PageInfo<SpuWhiteList> findWhiteListInfo(String manufacturerPartNumber,String manufacturer,int page,int pageSize){
		SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		Query query = this.mergeSpuWhiteListCondition(manufacturerPartNumber, manufacturer, page, pageSize);
		List<SpuWhiteList> spuWhiteLists= mongoOptions.find(query, SpuWhiteList.class);
		long total = mongoOptions.count(query, SpuWhiteList.class);
		if(CollectionUtils.isEmpty(spuWhiteLists)){
			return new PageInfo<>(Collections.emptyList());
		}
		List<Integer> ids = new ArrayList<>();
		for(SpuWhiteList spuWhiteList : spuWhiteLists){
			ids.add(spuWhiteList.getManufacturerId());
		}
		Map<Integer,ProductBrand> map = brandManager.findByIds(ids);
		for(SpuWhiteList spuWhiteList : spuWhiteLists){
			Date createdDate = new Date(Long.valueOf(spuWhiteList.getCreatedTimeMillis()));
			Date lastUpdateDate = new Date(Long.valueOf(spuWhiteList.getUpdatedTimeMillis()));
			spuWhiteList.setCreatedTime(format.format(createdDate));
			spuWhiteList.setUpdatedTime(format.format(lastUpdateDate));
			if(map.containsKey(spuWhiteList.getManufacturerId())){
				spuWhiteList.setManufacturer(map.get(spuWhiteList.getManufacturerId()).getBrandName());
			}
		}
		PageInfo<SpuWhiteList> pageResult = new PageInfo<>(spuWhiteLists);		
		pageResult.setPageNum(page);
		pageResult.setPageSize(pageSize);
		pageResult.setTotal(total);		
		return pageResult;
	}
	
	/**
	 * 拼装查询条件
	 * @param manufacturerPartNumber
	 * @param manufacturerId
	 * @param page
	 * @param pageSize
	 * @return
	 * @since 2017年12月8日
	 * @author tb.lijing@yikuyi.com
	 */
	public Query mergeSpuWhiteListCondition(String manufacturerPartNumber,String manufacturer,int page,int pageSize){
		Query query = new Query();
		Criteria criteria = new Criteria();
		if(StringUtils.isNotBlank(manufacturerPartNumber)){
			criteria.and("manufacturerPartNumber").regex("^.*" + manufacturerPartNumber + ".*$", "i");
		}
		if(StringUtils.isNotBlank(manufacturer)){
			criteria.and("manufacturer").regex("^.*" + manufacturer + ".*$", "i");
		}
		int queryPage = 0;
		if(page>0){
			queryPage = page-1;	
		}
		Sort sort = new Sort(Direction.DESC,"updatedTimeMillis");
		query.with(sort);
		query.skip(queryPage * pageSize);
		query.limit(pageSize);
		query.addCriteria(criteria);
		return query;
		
	}
	
}
