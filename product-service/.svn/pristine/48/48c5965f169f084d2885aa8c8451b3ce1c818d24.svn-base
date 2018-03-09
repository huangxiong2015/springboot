package com.yikuyi.product.rule.price.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jasig.inspektr.audit.annotation.Audit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageInfo;
import com.yikuyi.category.model.ProductCategory;
import com.yikuyi.product.category.manager.CategoryManager;
import com.yikuyi.product.rule.common.dao.ProductPriceRuleDao;
import com.yikuyi.product.rule.common.manager.RuleCommonManager;
import com.yikuyi.product.rule.delivery.manager.LeadTimeManager;
import com.yikuyi.rule.price.ProductPriceAction;
import com.yikuyi.rule.price.ProductPriceAction.ProductPricePurposeId;
import com.yikuyi.rule.price.ProductPriceCond;
import com.yikuyi.rule.price.ProductPriceCond.InputParam;
import com.yikuyi.rule.price.ProductPriceRule;
import com.yikuyi.rule.price.ProductPriceRule.PricePurposeType;
import com.yikuyi.rule.price.ProductPriceRule.RuleStatus;
import com.yikuyi.rule.price.model.PriceRuleDetail;
import com.yikuyi.rule.price.model.PriceRuleTemplate;
import com.yikuyi.rule.price.model.PriceRuleTemplateCache;
import com.ykyframework.exception.DataNotFoundException;
import com.ykyframework.exception.InvalidDataException;

@Service
@Transactional
public class ProductPriceRuleManager {

	private static final Logger logger = LoggerFactory.getLogger(LeadTimeManager.class);
	private static final String USD = "USD";
	private static final String CNY = "CNY";
	private static final String SEQ_ONE = "01";
	private static final String SEQ_TWO = "02";
	private static final String SEQ_THREE = "03";
	private static final String SEQ_FOUR = "04";
	private static final String SEQ_FIVE = "05";
	private static final String SEQ_SIX = "06";
	private static final String SEQ_SEVEN = "07";
	private static final String SEQ_EIGHT = "08";
	private static final String KEY_PRICERULE_TEMPLATE_CACHEMAP = "PriceRuleTemplateCacheMap";
	
	@Autowired
	private RuleCommonManager ruleCommonManager;
	
	@Autowired
	private CategoryManager categoryManager;
	
	@Autowired
	private ProductPriceRuleDao productPriceRuleDao;
	
	@Autowired
	private CacheManager cacheManager;
	
	/**
	 * 插入定价规则模板
	 * @param priceRuleTemplate
	 * @return
	 */
	@Audit(action = "Price Operationqqq;;;新建了'#priceRuleTemplate.ruleName'定价规则", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public PriceRuleTemplate insert(@com.framework.springboot.audit.Param("priceRuleTemplate") PriceRuleTemplate priceRuleTemplate,
			String userId){
		
		//检验模板名称是否已经存在
		int count = ruleCommonManager.checkRuleNameIsExist(priceRuleTemplate.getRuleName(),"",PricePurposeType.PRODUCT_PRICE);
		if(count > 0){
			throw new InvalidDataException("规则名称重复，请重新输入！");
		}	
		ProductPriceRule productPriceRule = transferVOToPO(priceRuleTemplate,userId);
		ruleCommonManager.insert(productPriceRule);
		priceRuleTemplate.setRuleId(productPriceRule.getPriceRuleId());
		return priceRuleTemplate;
	}
	
	
	/**
	 * 更新定价规则模板
	 * @param priceRuleTemplate
	 * @return
	 */
	@Audit(action = "Price Operationqqq;;;修改了'#priceRuleTemplate.ruleName'定价规则", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public PriceRuleTemplate update(@com.framework.springboot.audit.Param("priceRuleTemplate") PriceRuleTemplate priceRuleTemplate,
			String userId) {
		//检验模板名称是否已经存在
		int count = ruleCommonManager.checkRuleNameIsExist(priceRuleTemplate.getRuleName(),priceRuleTemplate.getRuleId(),PricePurposeType.PRODUCT_PRICE);
		if(count > 0){
			throw new InvalidDataException("规则名称重复，请重新输入！");
		}	
		ProductPriceRule productPriceRule = transferVOToPO(priceRuleTemplate,userId);
		ruleCommonManager.update(productPriceRule);
		logger.debug("add success");
		return priceRuleTemplate;
	}
	/**
	 * 根据ID查询定价规则模板
	 * @param priceRuleTemplate
	 * @return
	 */
	public PriceRuleTemplate getById(String id) {
		ProductPriceRule productPriceRule = ruleCommonManager.getDetailById(id);
		PriceRuleTemplate priceRuleTemplate = this.transferPOToVO(productPriceRule);
		
		//设置分类
		this.setCategoryNames(priceRuleTemplate);
		
		return priceRuleTemplate;
	}


	/**
	 * 设置类别名称
	 * @param priceRuleTemplate
	 */
	private void setCategoryNames(PriceRuleTemplate priceRuleTemplate) {
		String category = priceRuleTemplate.getCategory();
		String[] categoryIds = category != null ? category.split("-"): null;
		
		if( categoryIds == null || categoryIds.length != 3){
			return;
		}
		
		String categoryName1 = "";
		String categoryName2 = "";
		String categoryName3 = "";
		for (int i = 0; i < categoryIds.length; i++) {
			String cateId = categoryIds[i];
			if(!"none".equals(cateId) && cateId!= null){
				Integer categoryId = null;
				try {
					categoryId = Integer.valueOf(cateId);
				} catch (NumberFormatException e) {
					logger.error("ProductPriceRuleManager--method:setCategorys--类别ID格式不正确,categoryId:{}",cateId);
				}
				ProductCategory productCategory = null;
				if(categoryId != null){
					productCategory = categoryManager.findById(categoryId);
				}
				if(productCategory != null && productCategory.getLevel() == 1){
					categoryName1 = productCategory.getName();
				}
				if(productCategory != null && productCategory.getLevel() == 2){
					categoryName2 = productCategory.getName();
				}
				if(productCategory != null && productCategory.getLevel() == 3){
					categoryName3 = productCategory.getName();
				}
			}else{
				if(i==0){
					categoryName1 = "不限";
				}
				if(i==1){
					categoryName2 = "不限";
				}
				if(i==2){
					categoryName3 = "不限";
				}
			}
		}
		priceRuleTemplate.setCategoryName1(categoryName1);
		priceRuleTemplate.setCategoryName2(categoryName2);
		priceRuleTemplate.setCategoryName3(categoryName3);
	}
	
	
	/**
	 * 启用、禁用定价规则模板
	 * @param ruleId 规则ID
	 * @param status 状态
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Audit(action = "Price Operationqqq;;;修改了'#priceRuleTemplate.ruleName'定价规则的状态为：'#status'", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public PriceRuleTemplate updateStatus(String ruleId,
			@com.framework.springboot.audit.Param("status") ProductPriceRule.RuleStatus status,String userId,
			@com.framework.springboot.audit.Param("priceRuleTemplate") PriceRuleTemplate priceRuleTemplate) {
		
		if(priceRuleTemplate == null){
			throw new DataNotFoundException("此定价规则已不存在！");
		}
		
		if(status.equals(RuleStatus.ENABLED) && status.toString().equals(priceRuleTemplate.getStatus())){
			throw new InvalidDataException("此定价规则已启用，不可重复启用！");
		}
		
		Cache cache = cacheManager.getCache("priceRuleTemplateCache");
		
		
		
		//拼装缓存中的key值
		StringBuilder key = new StringBuilder();
		String vendor = priceRuleTemplate.getVendorName();
		
		//获取规则模板在缓存中的key
		this.getRuleTemplateCacheKey(priceRuleTemplate, key, vendor);
		
		ProductPriceRule productPriceRule = this.transferVOToPO(priceRuleTemplate,userId);
		
		// 当前登录用户
	    productPriceRule.setLastUpdateUser(userId);
	    
	    ValueWrapper valueWrapper = cache.get(KEY_PRICERULE_TEMPLATE_CACHEMAP);
	    
	    Map<String, PriceRuleTemplateCache> priceRuleTemplateCacheMap = null;
	    if(valueWrapper != null){
	    	priceRuleTemplateCacheMap = (Map<String, PriceRuleTemplateCache>) cache.get(KEY_PRICERULE_TEMPLATE_CACHEMAP).get();
	    }
		//启用
		if(ProductPriceRule.RuleStatus.ENABLED.equals(status)){
			
			//缓存中存在模板则不能启用
			if(priceRuleTemplateCacheMap != null){
				PriceRuleTemplateCache priceTemplate = priceRuleTemplateCacheMap.get(key.toString());
				if(priceTemplate != null){
					throw new InvalidDataException("此定价规则已启用，不可重复启用！");
				}
			}
			productPriceRule.setRuleStatus(RuleStatus.ENABLED);
			ruleCommonManager.enabled(productPriceRule);
			
			//缓存价格规则模板
			PriceRuleTemplateCache priceTemplateCache = new PriceRuleTemplateCache();
			priceTemplateCache.setProductPriceRule(productPriceRule);
			//缓存版本号
			priceTemplateCache.setVersion(UUID.randomUUID().toString());
			priceTemplateCache.setCacheKey(key.toString());
			
			if(priceRuleTemplateCacheMap == null){
				priceRuleTemplateCacheMap = new HashMap<>();
			}
			//清除map
			cache.evict(KEY_PRICERULE_TEMPLATE_CACHEMAP);
			priceRuleTemplateCacheMap.put(key.toString(), priceTemplateCache);
			cache.put(KEY_PRICERULE_TEMPLATE_CACHEMAP, priceRuleTemplateCacheMap);
		}
		//禁用或删除
		if(ProductPriceRule.RuleStatus.DISABLED.equals(status)
						|| ProductPriceRule.RuleStatus.DELETED.equals(status)){
			
			if(priceRuleTemplateCacheMap != null){
				//清除缓存模板
				priceRuleTemplateCacheMap.remove(key.toString());
				cache.evict(KEY_PRICERULE_TEMPLATE_CACHEMAP);
				cache.put(KEY_PRICERULE_TEMPLATE_CACHEMAP, priceRuleTemplateCacheMap);
				
			}
			productPriceRule.setRuleStatus(status);
			ruleCommonManager.enabled(productPriceRule);
			//停用时检验同一个供应商还有没有正在启用的模板
			if(ProductPriceRule.RuleStatus.DISABLED.equals(status)){
				Map<String,Object> verdonMap = new HashMap<>();
				verdonMap.put("pricePurposeType", ProductPriceRule.PricePurposeType.PRODUCT_PRICE);
				verdonMap.put("condValue", vendor);
				int verdonNum = productPriceRuleDao.getVendorRuleInfo(verdonMap);
				if(verdonNum == 0){
					//已停用所有模板
					priceRuleTemplate.setDisableAll(true);
				}
			}		
		}
		return priceRuleTemplate;
	}

	/**
	 * 获取规则模板在缓存中的key
	 * @param priceRuleTemplate
	 * @param key
	 * @param vendor
	 */
	private void getRuleTemplateCacheKey(PriceRuleTemplate priceRuleTemplate, StringBuilder key, String vendor) {
		String warehouse = priceRuleTemplate.getWarehouse();
		String brand = priceRuleTemplate.getBrand();
		String category = priceRuleTemplate.getCategory();
		String cate1 = "none";
		String cate2 = "none";
		String cate3 = "none";
		String[] cates = category.split("-");
		if(cates != null && cates.length == 3){
			cate1 = cates[0];
			cate2 = cates[1];
			cate3 = cates[2];
		}
		String currencyType = priceRuleTemplate.getCurrencyType();
		
		key.append(vendor).append("-");
		key.append(warehouse).append("-");
		key.append(brand).append("-");
		key.append(cate1).append("-");
		key.append(cate2).append("-");
		key.append(cate3).append("-");
		key.append(currencyType);
	}
	
	/**
	 * 列表查询定价规则模板
	 * @param pricePurposeType
	 * @param page
	 * @param size
	 * @return
	 */
	public PageInfo<PriceRuleTemplate> findList(String startDate,String endDate,String ruleStatus,String vendorId,String ruleName
			,String pricePurposeType,int page, int size){
		List<ProductPriceRule> priceRules = ruleCommonManager.findList(startDate,endDate,ruleStatus,vendorId,ruleName,pricePurposeType, page, size);
		
		List<PriceRuleTemplate> result = new ArrayList<>();
		
		for (ProductPriceRule productPriceRule : priceRules) {
			PriceRuleTemplate priceRuleTemplate = transferPOToVO(productPriceRule);
			//设置分类
			this.setCategoryNames(priceRuleTemplate);
			result.add(priceRuleTemplate);
		}
		Long listCount = ruleCommonManager.findListCount(startDate,endDate,ruleStatus,vendorId,ruleName,pricePurposeType);
		PageInfo<PriceRuleTemplate> pageInfo = new PageInfo<>(result);
		pageInfo.setTotal(listCount);
		pageInfo.setPageSize(size);
		return  pageInfo;
	}
	
	

	/**
	 * 转换值对象成数据库持久化对象
	 * @param priceRuleTemplate
	 * @return
	 */
	private ProductPriceRule transferVOToPO(PriceRuleTemplate priceRuleTemplate,String userId) {
		ProductPriceRule productPriceRule = new ProductPriceRule();
		productPriceRule.setPriceRuleId(priceRuleTemplate.getRuleId());
		productPriceRule.setRuleName(priceRuleTemplate.getRuleName());
		productPriceRule.setPricePurposeType(PricePurposeType.PRODUCT_PRICE);
		productPriceRule.setDescription(priceRuleTemplate.getDescription());
		// 当前登录用户
	    productPriceRule.setLastUpdateUser(userId);
	    productPriceRule.setCreatedDate(new Date());
	    
	    //规则明细
	    List<PriceRuleDetail> detils = priceRuleTemplate.getPriceRuleDetails();
	    
	    //设置持久化对象PriceRuleCond表字段
	    this.setProductPriceCond(priceRuleTemplate, userId, productPriceRule, detils);
		
		//设置持久化对象PriceRuleAction表字段
		this.setProductPriceAction(userId, productPriceRule, detils);
		
		return productPriceRule;
	}


	/**
	 * 设置持久化对象ProductPriceAction字段值
	 * @param userId
	 * @param productPriceRule
	 * @param detils
	 */
	private void setProductPriceAction(String userId, ProductPriceRule productPriceRule, List<PriceRuleDetail> detils) {
		List<ProductPriceAction> actions = new ArrayList<>();
		for (int i = 0; !CollectionUtils.isEmpty(detils) && i < detils.size() ; i++) {
			PriceRuleDetail detil = detils.get(i);
			if( detil == null){
				continue;
			}
			ProductPriceAction action = new ProductPriceAction();
			action.setProductPricePurposeId(detil.getRuleActionName());
			
			//Action
			if(ProductPricePurposeId.REAL_COST.toString().equals(detil.getRuleActionName()) ){
				if(USD.equals(detil.getDeliveryPlace())){
					action.setProductPriceActionSeqId(SEQ_ONE);
				}
				if(CNY.equals(detil.getDeliveryPlace())){
					action.setProductPriceActionSeqId(SEQ_TWO);
				}
				action.setProductPriceCondSeqId(SEQ_FIVE);
			}
			
			if(ProductPricePurposeId.RESALE_PRICE.toString().equals(detil.getRuleActionName())){
				if(USD.equals(detil.getDeliveryPlace())){
					action.setProductPriceActionSeqId(SEQ_THREE);
				}
				if(CNY.equals(detil.getDeliveryPlace())){
					action.setProductPriceActionSeqId(SEQ_FOUR);
				}
				action.setProductPriceCondSeqId(SEQ_SIX);
			}
			if(ProductPricePurposeId.SPECIAL_RESALE_PRICE.toString().equals(detil.getRuleActionName())){
				
				if(USD.equals(detil.getDeliveryPlace())){
					action.setProductPriceActionSeqId(SEQ_FIVE);
				}
				if(CNY.equals(detil.getDeliveryPlace())){
					action.setProductPriceActionSeqId(SEQ_SIX);
				}
				action.setProductPriceCondSeqId(SEQ_SEVEN);
			}
			action.setUomId(detil.getDeliveryPlace());
			action.setProductPriceActionTypeId(detil.getRuleType());
			String calculateValue = detil.getCalculateValue();
			if(calculateValue != null){
				calculateValue = calculateValue.replaceAll("-", "");
				action.setAmount(new BigDecimal(detil.getCalculateType() + calculateValue));
			}
			action.setLastUpdateUser(userId);
			actions.add(action);
		}
		productPriceRule.setActions(actions);
	}


	/**
	 * 设置持久化对象ProductPriceCond字段值
	 * @param priceRuleTemplate
	 * @param userId
	 * @param productPriceRule
	 * @param detils
	 */
	private void setProductPriceCond(PriceRuleTemplate priceRuleTemplate, String userId, ProductPriceRule productPriceRule,
			List<PriceRuleDetail> detils) {
		//条件参数
		List<ProductPriceCond> conds = new ArrayList<>();
		ProductPriceCond cond = new ProductPriceCond();
		cond.setInputParamEnumId(InputParam.VENDOR_ID.toString());
		cond.setCondValue(priceRuleTemplate.getVendorName());
		cond.setProductPriceCondSeqId(SEQ_ONE);
		cond.setLastUpdateUser(userId);
		conds.add(cond);
		
		cond = new ProductPriceCond();
		cond.setInputParamEnumId(InputParam.WAREHOUSE.toString());
		cond.setCondValue(priceRuleTemplate.getWarehouse());
		cond.setProductPriceCondSeqId(SEQ_TWO);
		cond.setLastUpdateUser(userId);
		conds.add(cond);
		
		cond = new ProductPriceCond();
		cond.setInputParamEnumId(InputParam.CATEGORY.toString());
		cond.setCondValue(priceRuleTemplate.getCategory());
		cond.setProductPriceCondSeqId(SEQ_THREE);
		cond.setLastUpdateUser(userId);
		conds.add(cond);
		
		cond = new ProductPriceCond();
		cond.setInputParamEnumId("CURRENCY_TYPE");
		cond.setCondValue(priceRuleTemplate.getCurrencyType());
		cond.setProductPriceCondSeqId(SEQ_FOUR);
		cond.setLastUpdateUser(userId);
		conds.add(cond);
		
		cond = new ProductPriceCond();
		cond.setInputParamEnumId(InputParam.BRAND.toString());
		cond.setCondValue(priceRuleTemplate.getBrand());
		cond.setProductPriceCondSeqId(SEQ_EIGHT);
		cond.setLastUpdateUser(userId);
		conds.add(cond);
		
		
		//是否勾选成本价
		boolean isCheckRealCost = false;
		//是否勾选销售价
		boolean isCheckResalePrice = false;
		//是否勾选特价
		boolean isCheckSpecialResalePrice = false;
		
		for (int i = 0; !CollectionUtils.isEmpty(detils) && i < detils.size() ; i++) {
			PriceRuleDetail detil = detils.get(i);
			if( detil == null){
				continue;
			}
			
			//条件REAL_COST
			if(ProductPricePurposeId.REAL_COST.toString().equals(detil.getRuleActionName()) && !isCheckRealCost){
					cond = new ProductPriceCond();
					cond.setInputParamEnumId(detil.getRuleActionName());
					cond.setCondValue(detil.getRuleActionName());
					cond.setProductPriceCondSeqId(SEQ_FIVE);
					cond.setLastUpdateUser(userId);
					conds.add(cond);
					isCheckRealCost = true;
			}
			//条件RESALE_PRICE
			if(ProductPricePurposeId.RESALE_PRICE.toString().equals(detil.getRuleActionName()) && !isCheckResalePrice){
					cond = new ProductPriceCond();
					cond.setInputParamEnumId(detil.getRuleActionName());
					cond.setCondValue(detil.getRuleActionName());
					cond.setProductPriceCondSeqId(SEQ_SIX);
					cond.setLastUpdateUser(userId);
					conds.add(cond);
					isCheckResalePrice = true;
			}
			//条件SPECIAL_RESALE_PRICE
			if(ProductPricePurposeId.SPECIAL_RESALE_PRICE.toString().equals(detil.getRuleActionName()) && !isCheckSpecialResalePrice){
				cond = new ProductPriceCond();
				cond.setInputParamEnumId(detil.getRuleActionName());
				cond.setCondValue(detil.getRuleActionName());
				cond.setProductPriceCondSeqId(SEQ_SEVEN);
				cond.setLastUpdateUser(userId);
				conds.add(cond);
				isCheckSpecialResalePrice = true;
			}
		}
		productPriceRule.setConds(conds);
	}
	
	
	
	/**
	 * 转换数据库持久化对象成值对象
	 * @param productPriceRule
	 * @return
	 */
	private PriceRuleTemplate transferPOToVO(ProductPriceRule productPriceRule) {
		if(productPriceRule == null){
			return null;
		}
		PriceRuleTemplate priceRuleTemplate = new PriceRuleTemplate();
		//设置条件
		this.setRuleTemplateCondition(productPriceRule,priceRuleTemplate);
		//设置Action
		this.setRuleTemplateAction(productPriceRule, priceRuleTemplate);
		
		priceRuleTemplate.setLastUpdateDate(productPriceRule.getLastUpdateDate());
		priceRuleTemplate.setLastUpdateUser(productPriceRule.getLastUpdateUser());
		priceRuleTemplate.setCreatedDate(productPriceRule.getCreatedDate());
		priceRuleTemplate.setCreator(productPriceRule.getCreator());
		
		priceRuleTemplate.setDescription(productPriceRule.getDescription());
		priceRuleTemplate.setRuleId(productPriceRule.getPriceRuleId());
		priceRuleTemplate.setRuleName(productPriceRule.getRuleName());
		priceRuleTemplate.setStatus(productPriceRule.getRuleStatus().toString());
		return priceRuleTemplate;
	}


	/**
	 * 设置Action
	 * @param productPriceRule
	 * @param priceRuleTemplate
	 */
	private void setRuleTemplateAction(ProductPriceRule productPriceRule, PriceRuleTemplate priceRuleTemplate) {
		List<ProductPriceAction> actions = productPriceRule.getActions();
		List<PriceRuleDetail> priceRuleDetils = new ArrayList<>();
		for (ProductPriceAction productPriceAction : actions) {
			
			PriceRuleDetail priceRuleDetil = new PriceRuleDetail();
			String pricePurposeId = productPriceAction.getProductPricePurposeId();
			String uomId = productPriceAction.getUomId();
			String priceActionTypeId = productPriceAction.getProductPriceActionTypeId();
			BigDecimal amount = productPriceAction.getAmount();
			String calculateType = null;
			if(amount != null){
				calculateType = amount.compareTo(new BigDecimal("0")) > -1 ? "+":"-";
			}
			priceRuleDetil.setRuleActionName(pricePurposeId);
			priceRuleDetil.setDeliveryPlace(uomId);
			priceRuleDetil.setRuleType(priceActionTypeId);
			priceRuleDetil.setCalculateType(calculateType);
			priceRuleDetil.setCalculateValue(amount != null? amount.toString():null);
			priceRuleDetils.add(priceRuleDetil);
			priceRuleTemplate.setPriceRuleDetails(priceRuleDetils);
		}
	}


	/**
	 * 设置priceRuleTemplate条件
	 * @param productPriceRule
	 * @param priceRuleTemplate
	 * @return
	 */
	private PriceRuleTemplate setRuleTemplateCondition(ProductPriceRule productPriceRule,PriceRuleTemplate priceRuleTemplate) {
		
		List<ProductPriceCond> conds = productPriceRule.getConds();
		for (ProductPriceCond productPriceCond : conds) {
			String condName = productPriceCond.getInputParamEnumId();
			String value = productPriceCond.getCondValue();
			if(InputParam.VENDOR_ID.toString().equals(condName)){
				priceRuleTemplate.setVendorName(value);
			}
			if(InputParam.WAREHOUSE.toString().equals(condName)){
				priceRuleTemplate.setWarehouse(value);
			}
			if(InputParam.CATEGORY.toString().equals(condName)){
				priceRuleTemplate.setCategory(value);
			}
			if(InputParam.CURRENCY_TYPE.toString().equals(condName)){
				priceRuleTemplate.setCurrencyType(value);
			}
			if(InputParam.BRAND.toString().equals(condName)){
				priceRuleTemplate.setBrand(value);
			}
		}
		return priceRuleTemplate;
	}
	
}
