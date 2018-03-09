package com.yikuyi.product.rule.mov.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.jasig.inspektr.audit.annotation.Audit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aliyun.openservices.shade.com.alibaba.rocketmq.shade.io.netty.util.internal.StringUtil;
import com.framework.springboot.audit.Param;
import com.github.pagehelper.PageInfo;
import com.yikuyi.brand.model.ProductBrand;
import com.yikuyi.party.PartyClientBuilder;
import com.yikuyi.party.facility.model.Facility;
import com.yikuyi.product.brand.dao.BrandRepository;
import com.yikuyi.product.common.utils.BusiErrorCode;
import com.yikuyi.product.rule.delivery.manager.LeadTimeManager;
import com.yikuyi.product.rule.mov.dao.MovRuleTemplateRepository;
import com.yikuyi.rule.mov.vo.MovRuleTemplate;
import com.yikuyi.rule.price.ProductPriceRule;
import com.yikuyi.rule.price.ProductPriceRule.RuleStatus;
import com.ykyframework.exception.BusinessException;
import com.ykyframework.exception.DataNotFoundException;
import com.ykyframework.exception.InvalidDataException;
import com.ykyframework.model.IdGen;

@Service
@Transactional
public class MovRulesManagerV2 {
	
	private static final Logger logger = LoggerFactory.getLogger(LeadTimeManager.class);
	
	private static final String KEY_MOVRULE_TEMPLATE_CACHEMAP = "MovRuleTemplateCacheMap";
	
	@Autowired
	private MovRuleTemplateRepository movRuleTemplateRepository;
	
	@Autowired
	private CacheManager cacheManager;
	
	@Autowired
	private PartyClientBuilder partyClientBuilder;
	
	@Autowired
	private BrandRepository brandRepository;
	
	/**
	 * 插入定价规则模板
	 * @param priceRuleTemplate
	 * @return
	 * @throws BusinessException 
	 */
	@Audit(action = "Price Operationqqq;;;'#movRuleTemplate.vendorId'qqq;;;新增供应商MOV规则", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public MovRuleTemplate insert(@Param("movRuleTemplate") MovRuleTemplate movRuleTemplate,
			String userId) throws BusinessException{
		String ruleId = String.valueOf(IdGen.getInstance().nextId());
		movRuleTemplate.setRuleId(ruleId);
		//默认禁用状态
		movRuleTemplate.setStatus(RuleStatus.DISABLED.toString());
		movRuleTemplate.setCreatedTimeMillis(String.valueOf(new Date().getTime()));
		movRuleTemplate.setUpdatedTimeMillis(String.valueOf(new Date().getTime()));
		movRuleTemplate.setCreatedDate(new Date());
		movRuleTemplate.setCreator(userId);
		movRuleTemplate.setLastUpdateDate(new Date());
		movRuleTemplate.setLastUpdateUser(userId);
		//如果有相同条件的则不许重复添加
		isExist(movRuleTemplate,0);
		movRuleTemplateRepository.save(movRuleTemplate);
		return movRuleTemplate;
	}
	

	private void isExist(MovRuleTemplate movRuleTemplate,int isUpdate) throws BusinessException{
		List<String> statusList = new ArrayList<>();
		statusList.add("ENABLED");
		statusList.add("DISABLED");
		List<MovRuleTemplate> movList = movRuleTemplateRepository.findVendorList(movRuleTemplate.getVendorId(),movRuleTemplate.getRuleType(),statusList);
		if(CollectionUtils.isNotEmpty(movList)){
			List<String> nowSourceIds = movRuleTemplate.getSourceIds();
			List<String> nowManufacturerIds = movRuleTemplate.getManufacturerIds();
			
			for(int k=0;k<movList.size();k++){
				MovRuleTemplate mov =movList.get(k);
				//如果是修改自己则剔除再做重复校验
				if(isUpdate == 1){
					if(mov.getRuleId().equals(movRuleTemplate.getRuleId())){
						continue;
					}
				}
				StringBuilder sb = new StringBuilder();
				if("0".equals(movRuleTemplate.getRuleType())){
					sb.append("供应商MOV重复,重复值为：");
				}else{
					sb.append("商品MOV重复,重复值为：");
				}
				int flag =0;//仓库标示
				int flag1 =0;//制造商表示
				List<String> sourceIds = mov.getSourceIds();//仓库
				List<String> manufacturerIds = mov.getManufacturerIds();//制造商
				
				//找出相同的仓库数据
				List<String> listSource = new ArrayList<>();
				if(CollectionUtils.isNotEmpty(nowSourceIds) && CollectionUtils.isNotEmpty(sourceIds)){
					listSource = getIntersection(nowSourceIds,sourceIds);
				}
			
				List<String> listManu = new ArrayList<>();
				if(CollectionUtils.isNotEmpty(nowManufacturerIds) && CollectionUtils.isNotEmpty(manufacturerIds)){
					listManu = getIntersection(nowManufacturerIds,manufacturerIds);
				}
				 
				 /*	Collection realSource = null;
				if(CollectionUtils.isNotEmpty(nowSourceIds)){
					realSource = new ArrayList<>(nowSourceIds);//现在的source
				}
				Collection realSource1 = null;
				if(CollectionUtils.isNotEmpty(sourceIds)){
					realSource1 = new ArrayList<>(sourceIds);//老的source
					if(realSource!=null){
						realSource.retainAll(realSource1); //找出相同的
					}
				}*/
			   // List<String> listSource = (List<String>)realSource;
			    //取出相同的数据
			    if(CollectionUtils.isNotEmpty(listSource)){
			    	flag =1;
			    }
				//取出相同的制造商数据
			/*	Collection realA = null;
				if(CollectionUtils.isNotEmpty(nowManufacturerIds)){
					realA = new ArrayList<>(nowManufacturerIds);//现在的source
				}	
				Collection realB = null;
				if(CollectionUtils.isNotEmpty(manufacturerIds)){
					realB = new ArrayList<>(manufacturerIds);//老的source
					if(CollectionUtils.isNotEmpty(realA)){
						realA.retainAll(realB); //找出相同的
					}
				}
			    List<String> listManu = (List<String>)realA;*/
			    //说明有相同的数据
			    if(CollectionUtils.isNotEmpty(listManu)){
			    	flag1 =1;
			    }
			    //说明有相同的数据
				if(flag ==1 && flag1==1){
					//查询仓库名字
					if(listSource!=null && listSource.contains("none")){
						sb.append("仓库:不限;");
					}else{
						StringBuilder sbSource = new StringBuilder();
						List<Facility> facilityList = partyClientBuilder.facilityResource().getFacility(listSource);
						facilityList.stream().forEach(facility ->{
							sbSource.append(facility.getFacilityName()+",");
						});
						if(!StringUtil.isNullOrEmpty(sbSource.toString())){
						  String sourceValue =	sbSource.substring(0, sbSource.length()-1);
						   sb.append("仓库:").append(sourceValue+";");
						}
						
					}
					
					//查询制造商名字
					if(listManu!=null && listManu.contains("none")){
						sb.append("制造商:不限;");
					}else{
						StringBuilder sbManu = new StringBuilder();
						List<Long> manufacturerList = null;
						if(listManu!=null && listManu.stream()!=null){
							manufacturerList  = listManu.stream().map(Long::parseLong).collect(Collectors.toList());
						}
						
        				Iterable<ProductBrand> brandList = brandRepository.findAll(manufacturerList);
        				brandList.forEach(brand ->{
        					sbManu.append(brand.getBrandName()+",");
        				});
        				if(!StringUtil.isNullOrEmpty(sbManu.toString())){
        					  String manuValue =	sbManu.substring(0, sbManu.length()-1);
        					  sb.append("制造商:").append(manuValue+";");
        				}
					}
					throw new BusinessException(BusiErrorCode.SOURCE_BRAND_EXIST, sb.toString());
				}
			}
			
		}

	}

	/**
	 * 修改MOV策略
	 * @param movRuleTemplate
	 * @param userId
	 * @return
	 * @since 2017年8月12日
	 * @author zr.wanghong
	 * @throws BusinessException 
	 */
	@Audit(action = "Price Operationqqq;;;'#movRuleTemplate.vendorId'qqq;;;修改商品MOV规则", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public MovRuleTemplate update(@Param("movRuleTemplate") MovRuleTemplate movRuleTemplate,
			String userId) throws BusinessException {
        MovRuleTemplate getMov = movRuleTemplateRepository.findMovById(movRuleTemplate.getRuleId());
    	//如果有相同的仓库和制造商不许添加
		isExist(movRuleTemplate,1);
		if(getMov!=null){
			movRuleTemplate.setCreatedDate(getMov.getCreatedDate());
			movRuleTemplate.setCreator(getMov.getCreator());
			movRuleTemplate.setCreatedTimeMillis(getMov.getCreatedTimeMillis());
		}
		movRuleTemplate.setLastUpdateDate(new Date());
		movRuleTemplate.setLastUpdateUser(userId);
		movRuleTemplate.setUpdatedTimeMillis(String.valueOf(new Date().getTime()));
		movRuleTemplateRepository.save(movRuleTemplate);
		logger.debug("update success");
		return movRuleTemplate;
	}
	
	/**
	 * 启用、禁用或删除MOV策略
	 * @param ruleId 策略ID
	 * @param status 状态
	 * @param userId 用户ID
	 * @param movRuleTemplate 策略实体
	 * @return 策略实体
	 */
	@SuppressWarnings("unchecked")
	@Audit(action = "Price Operationqqq;;;'#movRuleTemplate.vendorId'qqq;;;'#status'商品MOV规则", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public MovRuleTemplate updateStatus(String ruleId,
			@Param("status") ProductPriceRule.RuleStatus status,String userId,
			@Param("movRuleTemplate") MovRuleTemplate movRuleTemplate) {
		if(movRuleTemplate == null){
			throw new DataNotFoundException("此MOV规则已不存在！");
		}
		if(status.equals(RuleStatus.ENABLED) && status.toString().equals(movRuleTemplate.getStatus())){
			throw new InvalidDataException("此MOV规则已启用，不可重复启用！");
		}
	
		Cache cache = cacheManager.getCache("movRuleTemplateCache");
		//获取规则模板在缓存中的key
		List<String> keys = this.getRuleTemplateCacheKey(movRuleTemplate);
		//获取策略缓存Map
	    ValueWrapper valueWrapper = cache.get(KEY_MOVRULE_TEMPLATE_CACHEMAP);
	    Map<String, MovRuleTemplate> movRuleTemplateCacheMap = null;
	    if(valueWrapper != null){
	    	movRuleTemplateCacheMap = (Map<String, MovRuleTemplate>) cache.get(KEY_MOVRULE_TEMPLATE_CACHEMAP).get();
	    }
	    
		//启用
		if(ProductPriceRule.RuleStatus.ENABLED.equals(status)){
			//缓存中存在模板则不能启用
			if(movRuleTemplateCacheMap != null){
				for (String key :keys) {
					MovRuleTemplate ruleTemplate = movRuleTemplateCacheMap.get(key);
					if(ruleTemplate != null) throw new InvalidDataException(String.format("规则名称：[%s],此MOV规则已启用，不可重复启用！", ruleTemplate.getRuleType()));
				}
			}else{
				movRuleTemplateCacheMap = new HashMap<>();
			}
			//修改状态为启用
			movRuleTemplate.setStatus(status.toString());
			movRuleTemplate.setLastUpdateDate(new Date());
			movRuleTemplate.setLastUpdateUser(userId);
			movRuleTemplateRepository.save(movRuleTemplate);
			//清空并重置map
			cache.evict(KEY_MOVRULE_TEMPLATE_CACHEMAP);
			for (String key :keys) {
				//设置movType用来判断portal页面mov展示
				String movType = getMovType(movRuleTemplate);
				movRuleTemplate.setMovType(movType);
				movRuleTemplateCacheMap.put(key, movRuleTemplate);
			}
			cache.put(KEY_MOVRULE_TEMPLATE_CACHEMAP, movRuleTemplateCacheMap);
		}
		
		//禁用或删除
		if(ProductPriceRule.RuleStatus.DISABLED.equals(status)
						|| ProductPriceRule.RuleStatus.DELETED.equals(status)){
			//修改状态为删除
			movRuleTemplate.setStatus(status.toString());
			movRuleTemplate.setLastUpdateDate(new Date());
			movRuleTemplate.setLastUpdateUser(userId);
			movRuleTemplateRepository.save(movRuleTemplate);	
			
			if(movRuleTemplateCacheMap != null){
				//缓存Map中清除模板
				for (String key :keys) {
					movRuleTemplateCacheMap.remove(key);
				}
				cache.evict(KEY_MOVRULE_TEMPLATE_CACHEMAP);
				cache.put(KEY_MOVRULE_TEMPLATE_CACHEMAP, movRuleTemplateCacheMap);
			}
		}
		return movRuleTemplate;
	}
	/**
	 * 通过缓存key获取movType
	 * @param priceRuleTemplate
	 * @param key
	 * @param vendor
	 */
	private String getMovType(MovRuleTemplate movRuleTemplate){
		String movType = "vendor";
		List<String> sourceIds = movRuleTemplate.getSourceIds();
		List<String> manufacturerIds = movRuleTemplate.getManufacturerIds();
		if(CollectionUtils.isNotEmpty(sourceIds)){
			for (String sourceId : sourceIds) {
				if(!"none".equals(sourceId)) {
					movType = "warehouse";
					break;
				}
			}
		}
		if(CollectionUtils.isNotEmpty(manufacturerIds)){
			for (String manufacturerId : manufacturerIds) {
				if(!"none".equals(manufacturerId)) {
					movType = "brand";
					break;
				}
			}
		}
		
		/*String[] keySplit = key.split("-");
		String firstData = keySplit[1];
		String secondData = keySplit[2];
		if("none".equals(firstData) && "none".equals(secondData)){
			return "vendor";
		}else if(!"none".equals(firstData) && "none".equals(secondData)){
			return "warehouse";
		}else if(!"none".equals(secondData)){
			return "brand";
		}*/
		return movType;
	}
	/**
	 * 获取规则模板在缓存中的key
	 * @param priceRuleTemplate
	 * @param key
	 * @param vendor
	 */
	private List<String> getRuleTemplateCacheKey(MovRuleTemplate movRuleTemplate) {
		//拼装缓存中的key值
		StringBuilder key = new StringBuilder();
		List<String> keys = new ArrayList<>();
		//供应商id
		String vendorId = movRuleTemplate.getVendorId();
		
		//仓库id集合
		List<String> sourceIds = movRuleTemplate.getSourceIds();
		String ruleType= movRuleTemplate.getRuleType();
		if(CollectionUtils.isNotEmpty(sourceIds)){
			sourceIds.stream().forEach(sourceId -> {
				//制造商集合
				List<String> manufacturerIds = movRuleTemplate.getManufacturerIds();
				manufacturerIds.stream().forEach(manufacturerId -> {
					key.append(ruleType+"-");
					key.append(vendorId);
					key.append("-"+sourceId);
					key.append("-"+manufacturerId);
					keys.add(key.toString());
					key.delete(0, key.length());
				});
			});
		}

		return keys;
	}
	
	/**
	 * 根据策略ID查找MOV策略模板
	 * @param id
	 * @return
	 * @since 2017年8月12日
	 * @author zr.wanghong
	 */
	public MovRuleTemplate getById(String id) {
		return movRuleTemplateRepository.findMovById(id);
	}
	
	
	/**
	 * @param vendorId 供应商ID
	 * @param page 页码
	 * @param size 页大小
	 * @return MovRuleTemplate
	 * @since 2017年10月10日
	 * @author zr.helinmei@yikuyi.com
	 */
	public PageInfo<MovRuleTemplate> findList(String vendorId,int page, int size){
		//分页
		int queryPage = 0;
		if(page > 0){
			queryPage = page-1;
		}
		Sort sort = new Sort(Direction.DESC,"createdDate");
		PageRequest pageable = new PageRequest(queryPage,size,sort);
		List<String> statusList = new ArrayList<>();
		statusList.add("ENABLED");
		statusList.add("DISABLED");
		//结果集
		Page<MovRuleTemplate> result = movRuleTemplateRepository.findList(vendorId,statusList, pageable);
		List<MovRuleTemplate> movList = result.getContent();
		movList.stream().forEach( movRule ->{
			StringBuilder facilityStr = new StringBuilder();
		    //通过仓库id查询仓库名称
			getFacility(movRule, facilityStr);
			
			getBrand(movRule);
		});
		
		PageInfo<MovRuleTemplate> pageInfo = new PageInfo<>(movList);
		Long listCount = result.getTotalElements();
		pageInfo.setTotal(listCount);
		pageInfo.setPageSize(size);
		pageInfo.setPageNum(page);
		return pageInfo;
	}


	private void getBrand(MovRuleTemplate movRule) {
		StringBuilder brandStr = new StringBuilder();
		if(CollectionUtils.isNotEmpty(movRule.getManufacturerIds()) && !StringUtil.isNullOrEmpty(movRule.getManufacturerIds().get(0)) && !"none".equals(movRule.getManufacturerIds().get(0))){
			List<Long> manufacturerList = movRule.getManufacturerIds().stream().map(Long::parseLong).collect(Collectors.toList());
			Iterable<ProductBrand> brandList = brandRepository.findAll(manufacturerList);
			brandList.forEach(brand ->{
				if(!StringUtil.isNullOrEmpty(brand.getBrandName())){
					brandStr.append(brand.getBrandName()+",");
				}
			});
		    //设置制造商名称
			String resultBrand ="";
			if(!StringUtil.isNullOrEmpty(brandStr.toString())){
				resultBrand =brandStr.substring(0, brandStr.toString().length()-1);
			}
			movRule.setManufacturerName(resultBrand);
		}
	}


	private void getFacility(MovRuleTemplate movRule, StringBuilder facilityStr) {
		if(CollectionUtils.isNotEmpty(movRule.getSourceIds()) && !StringUtil.isNullOrEmpty(movRule.getSourceIds().get(0)) && !"none".equals(movRule.getSourceIds().get(0))){
			List<Facility> facilityList = partyClientBuilder.facilityResource().getFacility(movRule.getSourceIds());
			facilityList.stream().forEach( facility ->{
				if(!StringUtil.isNullOrEmpty(facility.getFacilityName())){
					facilityStr.append(facility.getFacilityName()+",");
				}
			});
			String resultFacility ="";
			if(!StringUtil.isNullOrEmpty(facilityStr.toString())){
				resultFacility =facilityStr.substring(0, facilityStr.toString().length()-1);
			}
			//设置仓库名称
			movRule.setSourceName(resultFacility);
		}
	}


	 public static List<String> getIntersection(List<String> list1,
	            List<String> list2) {
	        List<String> result = new ArrayList<>();
	        for (String integer : list2) {//遍历list1
	            if (list1.contains(integer)) {//如果存在这个数
	                result.add(integer);//放进一个list里面，这个list就是交集
	            }
	        }
	        return result;
	    }

}
