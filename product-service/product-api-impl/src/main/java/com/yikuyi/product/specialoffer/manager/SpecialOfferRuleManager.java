package com.yikuyi.product.specialoffer.manager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.jasig.inspektr.audit.annotation.Audit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.framework.springboot.model.LoginUser;
import com.framewrok.springboot.web.RequestHelper;
import com.github.pagehelper.PageInfo;
import com.yikuyi.brand.model.ProductBrand;
import com.yikuyi.category.model.ProductCategory;
import com.yikuyi.party.PartyClientBuilder;
import com.yikuyi.party.facility.model.Facility;
import com.yikuyi.product.brand.dao.BrandRepository;
import com.yikuyi.product.category.dao.CategoryRepository;
import com.yikuyi.product.common.dao.BaseMongoClient;
import com.yikuyi.product.common.utils.BusiErrorCode;
import com.yikuyi.product.common.utils.UtilsHelp;
import com.yikuyi.product.specialoffer.repository.SpecialOfferProductDraftRepository;
import com.yikuyi.product.specialoffer.repository.SpecialOfferProductRepository;
import com.yikuyi.product.specialoffer.repository.SpecialOfferRuleRepository;
import com.yikuyi.specialoffer.model.SpecialOfferProduct;
import com.yikuyi.specialoffer.model.SpecialOfferProduct.Status;
import com.yikuyi.specialoffer.model.SpecialOfferProductDraft;
import com.yikuyi.specialoffer.model.SpecialOfferRule;
import com.yikuyi.specialoffer.model.SpecialOfferRule.RuleType;
import com.yikuyi.specialoffer.vo.SpecialOfferRuleVo;
import com.ykyframework.exception.BusinessException;

import net.sf.json.JSONArray;

/**
 * 专属特价
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@Service
public class SpecialOfferRuleManager {
	
	private static final Logger logger = LoggerFactory.getLogger(SpecialOfferRuleManager.class);
	
	private static String createdTimeMillis = "createdTimeMillis";
	
	@Autowired
	private SpecialOfferRuleRepository specialOfferRuleRepository;
	
	@Autowired
	private BrandRepository brandRepository;
	
	@Autowired
	private PartyClientBuilder partyClientBuilder;
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private SpecialOfferProductRepository specialOfferProductRepository;
	
	@Autowired
	private SpecialOfferProductDraftRepository specialOfferProductDraftRepository;
		
	@Autowired
	private BaseMongoClient baseMongoClient;
	
	@Autowired
	private SpecialOfferCacheManager cacheManager;
		
	
	/**
	 * 专属特价列表查询
	 * @param ruleType
	 * @param createdDateStart
	 * @param createDateEnd
	 * @param page
	 * @param pageSize
	 * @return
	 * @since 2017年12月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public PageInfo<SpecialOfferRuleVo> list(String vendorId,RuleType ruleType, String createdDateStart, String createDateEnd, int page,
			int pageSize) {
		try {
			//拼装查询条件以及排序
			Query query = this.mergeCondition(vendorId,ruleType,createdDateStart,createDateEnd);
			int pageNo = 0;
			if(page>0){
				pageNo = page-1;
			} 
			Sort sort = new Sort(Direction.DESC,"updatedTimeMillis");
			PageRequest pageable = new PageRequest(pageNo,pageSize,sort);
			Page<SpecialOfferRuleVo> pageInfo = specialOfferRuleRepository.findSpecialOfferRuleByPage(query.getQueryObject(), pageable);
			List<SpecialOfferRuleVo> volist = pageInfo.getContent();
			if(!CollectionUtils.isEmpty(volist)){
				volist = handleSpecialOfferRuleList(volist);
			}
			PageInfo<SpecialOfferRuleVo> pageResult = new PageInfo<>(volist);		
			pageResult.setPageNum(page);
			pageResult.setPageSize(pageSize);
			pageResult.setTotal(pageInfo.getTotalElements());	
			return pageResult;
		} catch (Exception e) {
			logger.error("专属特价列表信息：{}", e);
		}
		return null;
	}
	
	/**
	 * 拼装查询条件以及排序
	 * @param ruleType
	 * @param createdDateStart
	 * @param createDateEnd
	 * @return
	 * @since 2017年12月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	private Query mergeCondition(String vendorId,RuleType ruleType, String createdDateStart, String createDateEnd) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Query query = new Query();
		Criteria criteria = new Criteria();
		Optional<Criteria> optCriteria = Optional.ofNullable(criteria);
		if(!optCriteria.isPresent()){
			criteria = new Criteria();
		}
		criteria.and("vendorId").is(vendorId);
		if(ruleType!=null){
			criteria.and("ruleType").is(ruleType.toString());
		}
		try {
		if(StringUtils.isNotBlank(createdDateStart) && StringUtils.isNotBlank(createDateEnd)){
				String startTime = String.valueOf(format.parse(createdDateStart+" 00:00:00").getTime());
				String endTime = String.valueOf(format.parse(createDateEnd +" 23:59:59").getTime());
				criteria.and(createdTimeMillis).gte(startTime).lt(endTime);
		}
		} catch (Exception e) {
			logger.error("时间查询：{}", e);
		}
		query.addCriteria(criteria);
		return query;
	}

	/**
	 * 处理对象数据
	 * @param list
	 * @return
	 * @since 2017年12月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	private List<SpecialOfferRuleVo> handleSpecialOfferRuleList(List<SpecialOfferRuleVo> list) {
		try {
			if(CollectionUtils.isNotEmpty(list)){
				for (SpecialOfferRuleVo specialOfferRuleVo : list) {
					specialOfferRuleVo.setCreatedTimeMillis(UtilsHelp.timeStamp2Date(specialOfferRuleVo.getCreatedTimeMillis()));
					specialOfferRuleVo.setUpdatedTimeMillis(UtilsHelp.timeStamp2Date(specialOfferRuleVo.getUpdatedTimeMillis()));
					//制造商
					String brandName = getMfrName(specialOfferRuleVo.getMfrIds());
					specialOfferRuleVo.setMfrName(brandName);
					//仓库
					String facilityName = getSourceName(specialOfferRuleVo.getSourceIds());
					specialOfferRuleVo.setSourceName(facilityName);
					//大类/小类/次小类
					String categoryName = getCateName(specialOfferRuleVo.getCatIds());
					specialOfferRuleVo.setCatName(categoryName);
				}
			}
		} catch (Exception e) {
			logger.error("时间格式错误：{}", e);
		}
		return list;
	}
	

	/**
	 * 获取制造商名称
	 * @param mfrIds
	 * @since 2017年12月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	private String getMfrName(List<Integer> mfrIds) {
		StringBuilder brandName = new StringBuilder();
		boolean first = true;
		if(CollectionUtils.isNotEmpty(mfrIds)){
			List<ProductBrand> brandList = brandRepository.findBrandByIds(mfrIds);
			if(CollectionUtils.isNotEmpty(brandList)){
				for (ProductBrand productBrand : brandList) {
					String name = productBrand.getBrandName();
					if(first){
						first = false;
					}else{
						brandName.append(",");
					}
					brandName.append(name);
				}
			}
		}
		return brandName.toString();
	}
	
	/**
	 * 获取制造商名称
	 * @param mfrIds
	 * @return
	 * @since 2017年12月22日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	private String getMfrNameArrayList(List<Integer> mfrIds) {
		String result = "";
		JSONArray array = new JSONArray();
		if(CollectionUtils.isNotEmpty(mfrIds)){
			List<ProductBrand> brandList = brandRepository.findBrandByIds(mfrIds);
			if(CollectionUtils.isNotEmpty(brandList)){
				for (ProductBrand productBrand : brandList) {
					JSONObject object = new JSONObject();
					object.put("id", productBrand.getId());
					object.put("name", productBrand.getBrandName());
					array.add(object);
				}
			}
			result = array.toString();
		}
		return result;	
	}
	
	/**
	 * 获取仓库名称
	 * @param sourceIds
	 * @return
	 * @since 2017年12月15日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	private String getSourceName(List<String> sourceIds) {
		StringBuilder facilityName = new StringBuilder();
		boolean first = true;
		if(CollectionUtils.isNotEmpty(sourceIds)){
			List<Facility> facilityList = partyClientBuilder.facilityResource().getFacility(sourceIds);
			if(CollectionUtils.isNotEmpty(facilityList)){
				for (Facility facility : facilityList) {
					String name = facility.getFacilityName();
					if(first){
						first = false;
					}else{
						facilityName.append(",");
					}
					facilityName.append(name);
				}
			}
		}
		return facilityName.toString();
	}
	
	/**
	 * 查询次小类名称
	 * @param catIds
	 * @return
	 * @since 2017年12月22日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	private String getCateName(List<String> catIds) {
		String result = "";
		StringBuilder cateName = new StringBuilder();
		boolean first = true;
		if(CollectionUtils.isNotEmpty(catIds)){
			for (int j = 0; j < catIds.size(); j++) {
					String catId = catIds.get(j);
					String catIdsNew = catId.replace("/", ",");//用逗号替换掉正斜杠
					String[] arr = catIdsNew.split(",");
					if("*/*/*".equals(catId)){
						result = "不限";
						return result;
					}else if(!catId.contains("*")){
						ProductCategory category = categoryRepository.findById(Integer.valueOf(arr[2]));
						String categoryName = category.getName();
						if(first){
							first = false;
						}else{
							cateName.append(",");
						}
						cateName.append(categoryName);
					}else{
						ProductCategory category  = null;
						for (int i = 0; i < arr.length; i++) {
							if(i>0 && (!"*".equals(arr[i-1]))){
								category = categoryRepository.findById(Integer.valueOf(arr[i-1]));
							}		
						}
						if(category!=null){
							String categoryName = category.getName();
							if(first){
								first = false;
							}else{
								cateName.append(",");
							}
							cateName.append(categoryName);
						}
				}
			}
		}
		result = cateName.toString();
		return result;
	}

	/**
	 * 查询次小类名称数组
	 * @param catIds
	 * @return
	 * @since 2017年12月15日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	private List<String> getCateNameArrayList(List<String> catIds) {
		List<String> list = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(catIds)){
			for (int j = 0; j < catIds.size(); j++) {
					String catId = catIds.get(j);
					String catIdsNew = catId.replace("/", ",");//用逗号替换掉正斜杠
					String[] arr = catIdsNew.split(",");
					JSONArray array = new JSONArray();
					for (int i = 0; i < arr.length; i++) {
						if(!"*".equals(arr[i])){
							ProductCategory category = categoryRepository.findById(Integer.valueOf(arr[i]));
							JSONObject object = new JSONObject();
							object.put("id", category.getId());
							object.put("name", category.getName());
							array.add(object);
						}else{
							JSONObject object = new JSONObject();
							object.put("id", "*");
							object.put("name", "*");
							array.add(object);
						}
					}
					list.add(array.toString());
			}
		}
		return list;
	}


	

	/**
	 * 查询专属特价规则详情
	 * @param id
	 * @return
	 * @since 2017年12月19日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public SpecialOfferRuleVo getSpecialOfferRuleVo(String id) {
		SpecialOfferRuleVo ruleVo = new SpecialOfferRuleVo();
		try {
			SpecialOfferRule specialOfferRule = specialOfferRuleRepository.findOne(id);
			BeanUtils.copyProperties(specialOfferRule ,ruleVo);
			ruleVo.setCreatedTimeMillis(UtilsHelp.timeStamp2Date(ruleVo.getCreatedTimeMillis()));
			ruleVo.setUpdatedTimeMillis(UtilsHelp.timeStamp2Date(ruleVo.getUpdatedTimeMillis()));
			//制造商
			String brandName = getMfrName(ruleVo.getMfrIds());
			ruleVo.setMfrName(brandName);
			String brandNameArray = getMfrNameArrayList(ruleVo.getMfrIds());
			ruleVo.setMfrNameArray(brandNameArray);
			//仓库
			String facilityName = getSourceName(ruleVo.getSourceIds());
			ruleVo.setSourceName(facilityName);
			//大类/小类/次小类
			String categoryName = getCateName(ruleVo.getCatIds());
			ruleVo.setCatName(categoryName);
			List<String> categoryNameArrayList = getCateNameArrayList(ruleVo.getCatIds());
			ruleVo.setCatNameArray(categoryNameArrayList);
		} catch (Exception e) {
			logger.error("专属特价查询详情：{}", e);
		}
		return ruleVo;
	}


	
	/**
	 * 删除专属特价规则
	 * @param id
	 * @since 2017年12月15日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Audit(action = "SpecialOfferRule Deleteqqq;;;'#vendorId'qqq;;;删除专属特价规则", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public void deteteRuleById(@com.framework.springboot.audit.Param("id") String id,
			@com.framework.springboot.audit.Param("vendorId") String vendorId) throws BusinessException{
		SpecialOfferRule specialOfferRule = specialOfferRuleRepository.findOne(id);
		if(specialOfferRule == null){
			throw new BusinessException(BusiErrorCode.SPECIAL_OFFER_RULE_NULL, "没有匹配信息");
		}
		if(RuleType.RULE.equals(specialOfferRule.getRuleType())){
			//删除规则缓存
			cacheManager.deleteSpecialOfferProductRuleCache(specialOfferRule, specialOfferRule.getVendorId(), id);
		}
		if(RuleType.MPN.equals(specialOfferRule.getRuleType())){//型号对应的信息在商品表中是没有数据的
			List<SpecialOfferProduct> productList = specialOfferProductRepository.findSpecialOfferProductByRuleId(id); 
			if(CollectionUtils.isNotEmpty(productList)){
				
				for (SpecialOfferProduct specialOfferProduct : productList) {
					//删除商品缓存
					cacheManager.deleteSpecialOfferProductIdCache(specialOfferProduct.getProductId(), specialOfferProduct.getRuleId());
				}
			}
			//删除正式商品表中的相关信息
			specialOfferProductRepository.deleteSpecialOfferProductByruleId(id);
		}
		//删除规则表信息
		specialOfferRuleRepository.delete(id);
		
	}

	/**
	 * 添加专属特价规则信息
	 * @param specialOfferRule
	 * @since 2017年12月18日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Audit(action = "SpecialOfferRule Addqqq;;;'#specialOfferRule.vendorId'qqq;;;添加了专属特价规则", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public void addSpecialOfferRule(@com.framework.springboot.audit.Param("ruleId") String ruleId,@com.framework.springboot.audit.Param("specialOfferRule") SpecialOfferRule specialOfferRule) throws BusinessException{
		LoginUser userInfo = RequestHelper.getLoginUser();
		//添加专属特价规则信息
		specialOfferRule.setId(ruleId);
		specialOfferRule.setCreator(userInfo.getId());
		specialOfferRule.setCreatorName(userInfo.getUsername());
		specialOfferRule.setCreatedTimeMillis(String.valueOf(System.currentTimeMillis()));
		specialOfferRule.setUpdatedTimeMillis(String.valueOf(System.currentTimeMillis()));
		specialOfferRule.setLastUpdateUser(userInfo.getId());
		specialOfferRule.setLastUpdateUserName(userInfo.getUsername());
		if(RuleType.MPN.equals(specialOfferRule.getRuleType())){//
			List<SpecialOfferProductDraft> draftList = specialOfferProductDraftRepository.findSpecialOfferProductDraftByCondition(ruleId, Status.ENABLE.toString());
			if(CollectionUtils.isEmpty(draftList))
			{
				throw new BusinessException(BusiErrorCode.SPECIAL_PRODUCT_ENABLE_NULL, "没有有效的数据，不能保存");
			}
			//将商品的信息从草稿表中迁移到正式表中
			Document result = baseMongoClient.getDatabase().runCommand(new Document("eval","copySpecialProductDraft(\""+ruleId+"\")"));
			if("1.0".equals(result.get("ok").toString())){//迁移数据成功
				//添加specialOfferRule表的mpn字段，取前三个
				String mpnResult = getMpnThree(ruleId);
				specialOfferRule.setMpn(mpnResult);
			}	
		}
			
		specialOfferRuleRepository.insert(specialOfferRule);
		//添加缓存处理
		this.handleSpecialAndProductCache(specialOfferRule);
	}
	
	/**
	 * 设置specialOfferRule表的mpn字段，取list前三个
	 * @param ruleId
	 * @return
	 * @since 2017年12月25日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	private String getMpnThree(String ruleId) {
		//设置specialOfferRule表的mpn字段，取list前三个
		StringBuilder mpnName = new StringBuilder();
		boolean first = true;
		Query query = new Query();
		Criteria criteria = new Criteria();
		criteria.and("ruleId").is(ruleId);
		query.addCriteria(criteria);
		int pageNo = 0;
		int pageSize = 3;
		Sort sort = new Sort(Direction.DESC,"updatedTimeMillis");
		PageRequest pageable = new PageRequest(pageNo,pageSize,sort);
		Page<SpecialOfferProduct> page = specialOfferProductRepository.findSpecialOfferProductByPage(query.getQueryObject(), pageable);
		List<SpecialOfferProduct> list = page.getContent();
		if(CollectionUtils.isNotEmpty(list)){
			for (int i = 0; i < list.size(); i++) {
				if(first){
					first = false;
				}else{
					mpnName.append(",");
				}
				mpnName.append(list.get(i).getMpn());	
			}
		}
		return mpnName.toString();
	}
	
	/**
	 * 处理缓存
	 * @param specialOfferRule
	 * @since 2017年12月26日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	private void handleSpecialAndProductCache(SpecialOfferRule specialOfferRule) {
		if(RuleType.RULE.equals(specialOfferRule.getRuleType())){
			//处理规则缓存
			cacheManager.initSpecialOfferProductRuleCache(specialOfferRule, specialOfferRule.getVendorId(), specialOfferRule.getId());
		}
		//处理商品缓存
		if(RuleType.MPN.equals(specialOfferRule.getRuleType())){
			List<SpecialOfferProduct> productsList = specialOfferProductRepository.findSpecialOfferProductByRuleId(specialOfferRule.getId());
			if(CollectionUtils.isNotEmpty(productsList)){
				for (SpecialOfferProduct specialOfferProduct : productsList) {
					cacheManager.initSpecialOfferProductIdCache(specialOfferProduct.getProductId(), specialOfferRule.getId());
				}
			}
		}
	}

	/**
	 * 编辑专属特价规则
	 * @param specialOfferRule
	 * @return
	 * @since 2017年12月20日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Audit(action = "SpecialOfferRule Modifyqqq;;;'#specialOfferRule.vendorId'qqq;;;编辑了专属特价规则", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public void updateSpecialOfferRule(@com.framework.springboot.audit.Param("specialOfferRule") SpecialOfferRule specialOfferRule) throws BusinessException{
			SpecialOfferRule ruleOld = specialOfferRuleRepository.findOne(specialOfferRule.getId());
			if(ruleOld!=null){//编辑产品线内容信息
				specialOfferRule.setCreator(ruleOld.getCreator());
				specialOfferRule.setCreatorName(ruleOld.getCreatorName());
				specialOfferRule.setCreatedTimeMillis(ruleOld.getCreatedTimeMillis());
				specialOfferRule.setUpdatedTimeMillis(String.valueOf(System.currentTimeMillis()));
				specialOfferRule.setLastUpdateUser(ruleOld.getLastUpdateUser());
				specialOfferRule.setLastUpdateUserName(ruleOld.getLastUpdateUserName());
			}
			if(RuleType.RULE.equals(specialOfferRule.getRuleType())){
				//删除规则缓存
				cacheManager.deleteSpecialOfferProductRuleCache(ruleOld, specialOfferRule.getVendorId(), specialOfferRule.getId());
				
			}
			if(RuleType.MPN.equals(specialOfferRule.getRuleType())){//只有编辑型号数据的时候才会重新处理数据
				//清除正式表中型号类型上传的数据，重新迁移数据
				//1.清除正式表中MPN类型的UPLOAD类型的数据
				List<SpecialOfferProduct> productlist = specialOfferProductRepository.findSpecialOfferProductByRuleId(specialOfferRule.getId());
				if(CollectionUtils.isNotEmpty(productlist)){
					specialOfferProductRepository.deleteSpecialOfferProductByruleId(specialOfferRule.getId());
					//2.清掉缓存中的数据
					for (SpecialOfferProduct specialOfferProduct : productlist) {
						//删除商品缓存
						cacheManager.deleteSpecialOfferProductIdCache(specialOfferProduct.getProductId(), specialOfferProduct.getRuleId());
					}
				}
				//3.如果草稿表中没有有效的数据是不能编辑的
				List<SpecialOfferProductDraft> offerRulesDraftList = specialOfferProductDraftRepository.findSpecialOfferProductDraftByCondition(specialOfferRule.getId(), Status.ENABLE.toString());
				if(CollectionUtils.isEmpty(offerRulesDraftList)){
					throw new BusinessException(BusiErrorCode.SPECIAL_PRODUCT_ENABLE_NULL, "没有有效的数据，不能保存");
				}
				//4.从草稿表中重新迁移数据
				Document result = baseMongoClient.getDatabase().runCommand(new Document("eval","copySpecialProductDraft(\""+specialOfferRule.getId()+"\")"));
				if("1.0".equals(result.get("ok").toString())){//迁移数据成功
					//添加specialOfferRule表的mpn字段，取前三个
					String mpnResult = getMpnThree(specialOfferRule.getId());
					specialOfferRule.setMpn(mpnResult);
				}
			}else{
				specialOfferRule.setMpn(specialOfferRule.getMpn());
			}
			specialOfferRuleRepository.save(specialOfferRule);
			//5.处理缓存
			this.handleSpecialAndProductCache(specialOfferRule);
	}
	
	/**
	 * 将正式商品表中的数据迁移到草稿表中
	 * @param ruleId
	 * @since 2017年12月20日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public void copyProductToDraft(String ruleId) throws BusinessException{
		//在迁移数据之前先清除一下草稿表中的数据
		specialOfferProductDraftRepository.deleteSpecialOfferProductDraftByruleId(ruleId);
		Document result = baseMongoClient.getDatabase().runCommand(new Document("eval","copySpecialProductToDraft(\""+ruleId+"\")"));
		if(!"1.0".equals(result.get("ok").toString())){
			throw new BusinessException(BusiErrorCode.COPY_PRODUCT_TO_DRAFT, " 将商品信息从正式表迁移到草稿表抛异常");
		}
	}
	
			 
}