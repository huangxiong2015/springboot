package com.yikuyi.product.rule.mov.manager;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jasig.inspektr.audit.annotation.Audit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.yikuyi.product.rule.common.manager.RuleCommonManager;
import com.yikuyi.product.rule.delivery.manager.LeadTimeManager;
import com.yikuyi.rule.mov.vo.MovRuleTemplate;
import com.yikuyi.rule.price.ProductPriceAction;
import com.yikuyi.rule.price.ProductPriceAction.ProductPricePurposeId;
import com.yikuyi.rule.price.ProductPriceCond;
import com.yikuyi.rule.price.ProductPriceCond.InputParam;
import com.yikuyi.rule.price.ProductPriceRule;
import com.yikuyi.rule.price.ProductPriceRule.PricePurposeType;
import com.yikuyi.rule.price.ProductPriceRule.RuleStatus;
import com.ykyframework.exception.DataNotFoundException;
import com.ykyframework.exception.InvalidDataException;
import com.ykyframework.exception.SystemException;

@Service
@Transactional
public class MovRulesManager {
	
	private static final Logger logger = LoggerFactory.getLogger(LeadTimeManager.class);
	
	private static final String PRC_EQ = "PRC_EQ";
	
	private static final String SEQ_ONE = "01";
	private static final String SEQ_TWO = "02";
	
	private static final String USD = "USD";
	private static final String CNY = "CNY";
	
	private static final String KEY_MOVRULE_TEMPLATE_CACHEMAP = "MovRuleTemplateCacheMap";
	
	@Autowired
	private RuleCommonManager ruleCommonManager;
	
	@Autowired
	private CacheManager cacheManager;
	
	/**
	 * 插入定价规则模板
	 * @param priceRuleTemplate
	 * @return
	 */
	@Audit(action = "Price Operationqqq;;;新建了'#priceRuleTemplate.ruleName'MOV规则", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public MovRuleTemplate insert(@com.framework.springboot.audit.Param("movRuleTemplate") MovRuleTemplate movRuleTemplate,
			String userId){
		ProductPriceRule productPriceRule = transferVOToPO(movRuleTemplate,userId);
		ruleCommonManager.insert(productPriceRule);
		movRuleTemplate.setRuleId(productPriceRule.getPriceRuleId());
		return movRuleTemplate;
	}
	
	/**
	 *  转换值对象成数据库持久化对象
	 * @param movRuleTemplate
	 * @param userId
	 * @return
	 * @since 2017年8月12日
	 * @author zr.wanghong
	 */
	private ProductPriceRule transferVOToPO(MovRuleTemplate movRuleTemplate,String userId){
		ProductPriceRule productPriceRule = new ProductPriceRule();
		productPriceRule.setPriceRuleId(movRuleTemplate.getRuleId());
		productPriceRule.setPricePurposeType(PricePurposeType.MOV);
		productPriceRule.setDescription(movRuleTemplate.getDescription());
		// 当前登录用户
	    productPriceRule.setLastUpdateUser(userId);
	    productPriceRule.setCreatedDate(new Date());
	    
	    //设置持久化对象PriceRuleCond表字段
	    this.setProductPriceCond(movRuleTemplate, productPriceRule, userId);
		
		//设置持久化对象PriceRuleAction表字段
		this.setProductPriceAction(movRuleTemplate, productPriceRule, userId);
		
		return productPriceRule;
	}
	
	/**
	 * 设置持久化对象ProductPriceCond字段值
	 * @param movRuleTemplate
	 * @param productPriceRule
	 * @param userId
	 * @since 2017年8月12日
	 * @author zr.wanghong
	 */
	private void setProductPriceCond(MovRuleTemplate movRuleTemplate, ProductPriceRule productPriceRule,String userId) {
		//条件参数
		List<ProductPriceCond> conds = new ArrayList<>();
		ProductPriceCond cond = new ProductPriceCond();
		cond.setInputParamEnumId(InputParam.VENDOR_ID.toString());
		cond.setOperatorEnumId(PRC_EQ);
		cond.setCondValue(movRuleTemplate.getVendorId());
		cond.setProductPriceCondSeqId(SEQ_ONE);
		cond.setLastUpdateUser(userId);
		conds.add(cond);
		
		/*cond = new ProductPriceCond();
		cond.setInputParamEnumId(InputParam.WAREHOUSE.toString());
		cond.setOperatorEnumId(PRC_EQ);
		cond.setCondValue(movRuleTemplate.getWarehouse());
		cond.setProductPriceCondSeqId(SEQ_TWO);
		cond.setLastUpdateUser(userId);
		conds.add(cond);*/
		
		/*cond = new ProductPriceCond();
		cond.setInputParamEnumId(InputParam.BRAND.toString());
		cond.setOperatorEnumId(PRC_EQ);
		cond.setCondValue(movRuleTemplate.getBrand());
		cond.setProductPriceCondSeqId(SEQ_TWO);
		cond.setLastUpdateUser(userId);
		conds.add(cond);*/
		
		cond = new ProductPriceCond();
		cond.setInputParamEnumId("MOV");
		cond.setOperatorEnumId(PRC_EQ);
		cond.setCondValue("MOV");
		cond.setProductPriceCondSeqId(SEQ_TWO);
		cond.setLastUpdateUser(userId);
		conds.add(cond);
		
		productPriceRule.setConds(conds);
	}
	
	
	/**
	 * 设置持久化对象ProductPriceAction字段值
	 * @param movRuleTemplate
	 * @param productPriceRule
	 * @param userId
	 * @since 2017年8月12日
	 * @author zr.wanghong
	 */
	private void setProductPriceAction(MovRuleTemplate movRuleTemplate,ProductPriceRule productPriceRule,String userId) {
		List<ProductPriceAction> actions = new ArrayList<>();
		
		if(StringUtils.isNotEmpty(movRuleTemplate.getCnyMovAmount())){
			ProductPriceAction action = new ProductPriceAction();
			//Action
			action.setProductPricePurposeId(ProductPricePurposeId.MOV_CNY.toString());
			action.setProductPriceActionSeqId(SEQ_ONE);
			action.setProductPriceCondSeqId(SEQ_TWO);
			action.setUomId(CNY);
			BigDecimal amount = new BigDecimal(movRuleTemplate.getCnyMovAmount()).setScale(2, BigDecimal.ROUND_DOWN);;
			action.setAmount(amount);
			action.setLastUpdateUser(userId);
			actions.add(action);
		}
		if(StringUtils.isNotEmpty(movRuleTemplate.getUsdMovAmount())){
			ProductPriceAction action = new ProductPriceAction();
			//Action
			action.setProductPricePurposeId(ProductPricePurposeId.MOV_USD.toString());
			action.setProductPriceActionSeqId(SEQ_TWO);
			action.setProductPriceCondSeqId(SEQ_TWO);
			action.setUomId(USD);
			BigDecimal amount = new BigDecimal(movRuleTemplate.getUsdMovAmount()).setScale(2, BigDecimal.ROUND_DOWN);;
			action.setAmount(amount);
			action.setLastUpdateUser(userId);
			actions.add(action);
		}
		
		productPriceRule.setActions(actions);
	}
	
	
	/**
	 * 修改MOV策略
	 * @param movRuleTemplate
	 * @param userId
	 * @return
	 * @since 2017年8月12日
	 * @author zr.wanghong
	 */
	@Audit(action = "Price Operationqqq;;;修改了'#movRuleTemplate.ruleName'MOV规则", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public MovRuleTemplate update(@com.framework.springboot.audit.Param("movRuleTemplate") MovRuleTemplate movRuleTemplate,
			String userId) {
		ProductPriceRule productPriceRule = transferVOToPO(movRuleTemplate,userId);
		ruleCommonManager.update(productPriceRule);
		movRuleTemplate.setRuleId(productPriceRule.getPriceRuleId());
		logger.debug("add success");
		return movRuleTemplate;
	}
	
	/**
	 * 启用、禁用或删除MOV策略
	 * @param ruleId
	 * @param status
	 * @param userId
	 * @param movRuleTemplate
	 * @return
	 * @since 2017年8月12日
	 * @author zr.wanghong
	 */
	@SuppressWarnings("unchecked")
	@Audit(action = "Price Operationqqq;;;修改了'#movRuleTemplate.ruleName'MOV规则的状态为：'#status'", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public MovRuleTemplate updateStatus(String ruleId,
			@com.framework.springboot.audit.Param("status") ProductPriceRule.RuleStatus status,String userId,
			@com.framework.springboot.audit.Param("movRuleTemplate") MovRuleTemplate movRuleTemplate) {
		
		if(movRuleTemplate == null){
			throw new DataNotFoundException("此MOV规则已不存在！");
		}
		
		if(status.equals(RuleStatus.ENABLED) && status.toString().equals(movRuleTemplate.getStatus())){
			throw new InvalidDataException("此MOV规则已启用，不可重复启用！");
		}
		
		Cache cache = cacheManager.getCache("movRuleTemplateCache");
		//拼装缓存中的key值
		StringBuilder key = new StringBuilder();
		//获取规则模板在缓存中的key
		this.getRuleTemplateCacheKey(movRuleTemplate, key);
		
		ProductPriceRule productPriceRule = this.transferVOToPO(movRuleTemplate,userId);
		
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
				MovRuleTemplate ruleTemplate = movRuleTemplateCacheMap.get(key.toString());
				if(ruleTemplate != null){
					throw new InvalidDataException("此MOV规则已启用，不可重复启用！");
				}
			}
			//修改状态为启用
			productPriceRule.setRuleStatus(RuleStatus.ENABLED);
			ruleCommonManager.enabled(productPriceRule);
			movRuleTemplate.setStatus(status.toString());
			
			//缓存MOV规则策略模板
			if(movRuleTemplateCacheMap == null){
				movRuleTemplateCacheMap = new HashMap<>();
			}
			//清除map
			cache.evict(KEY_MOVRULE_TEMPLATE_CACHEMAP);
			movRuleTemplateCacheMap.put(key.toString(), movRuleTemplate);
			cache.put(KEY_MOVRULE_TEMPLATE_CACHEMAP, movRuleTemplateCacheMap);
		}
		
		//禁用或删除
		if(ProductPriceRule.RuleStatus.DISABLED.equals(status)
						|| ProductPriceRule.RuleStatus.DELETED.equals(status)){
			
			if(movRuleTemplateCacheMap != null){
				//缓存Map中清除模板
				movRuleTemplateCacheMap.remove(key.toString());
				cache.evict(KEY_MOVRULE_TEMPLATE_CACHEMAP);
				cache.put(KEY_MOVRULE_TEMPLATE_CACHEMAP, movRuleTemplateCacheMap);
			}
			productPriceRule.setRuleStatus(status);
			ruleCommonManager.enabled(productPriceRule);
			movRuleTemplate.setStatus(status.toString());
			
			//停用时检验同一个供应商还有没有正在启用的模板
			/*if(ProductPriceRule.RuleStatus.DISABLED.equals(status)){
				Map<String,Object> verdonMap = new HashMap<>();
				verdonMap.put("pricePurposeType", ProductPriceRule.PricePurposeType.MOV);
				verdonMap.put("condValue", vendor);
				int verdonNum = productPriceRuleDao.getVendorRuleInfo(verdonMap);
				if(verdonNum == 0){
					//已停用所有模板
					movRuleTemplate.setDisableAll(true);
				}
			}	*/	
		}
		return movRuleTemplate;
	}
	
	/**
	 * 获取规则模板在缓存中的key
	 * @param priceRuleTemplate
	 * @param key
	 * @param vendor
	 */
	private void getRuleTemplateCacheKey(MovRuleTemplate movRuleTemplate, StringBuilder key) {
		String vendor = movRuleTemplate.getVendorId();
		/*String warehouse = movRuleTemplate.getWarehouse();
		String brand = movRuleTemplate.getBrand();*/
		key.append(vendor);
		/*key.append(warehouse).append("-");
		key.append(brand);*/
	}
	
	/**
	 * 根据策略ID查找MOV策略模板
	 * @param id
	 * @return
	 * @since 2017年8月12日
	 * @author zr.wanghong
	 */
	public MovRuleTemplate getById(String id) {
		ProductPriceRule productPriceRule = ruleCommonManager.getDetailById(id);
		MovRuleTemplate movRuleTemplate = this.transferPOToVO(productPriceRule);
		return movRuleTemplate;
	}
	
	/**
	 *  转换数据库持久化对象成值对象
	 * @param productPriceRule
	 * @return
	 * @since 2017年8月12日
	 * @author zr.wanghong
	 */
	private MovRuleTemplate transferPOToVO(ProductPriceRule productPriceRule) {
		if(productPriceRule == null){
			return null;
		}
		MovRuleTemplate movRuleTemplate = new MovRuleTemplate();
		//设置条件
		this.setRuleTemplateCondition(productPriceRule,movRuleTemplate);
		//设置Action
		this.setRuleTemplateAction(productPriceRule, movRuleTemplate);
		
		movRuleTemplate.setLastUpdateDate(productPriceRule.getLastUpdateDate());
		movRuleTemplate.setLastUpdateUser(productPriceRule.getLastUpdateUser());
		movRuleTemplate.setCreatedDate(productPriceRule.getCreatedDate());
		movRuleTemplate.setCreator(productPriceRule.getCreator());
		
		movRuleTemplate.setDescription(productPriceRule.getDescription());
		movRuleTemplate.setRuleId(productPriceRule.getPriceRuleId());
		movRuleTemplate.setStatus(productPriceRule.getRuleStatus().toString());
		return movRuleTemplate;
	}
	
	/**
	 * 设置ProductPriceCond字段到movRuleTemplate
	 * @param productPriceRule
	 * @param movRuleTemplate
	 * @return
	 * @since 2017年8月12日
	 * @author zr.wanghong
	 */
	private MovRuleTemplate setRuleTemplateCondition(ProductPriceRule productPriceRule,MovRuleTemplate movRuleTemplate) {
		List<ProductPriceCond> conds = productPriceRule.getConds();
		for (ProductPriceCond productPriceCond : conds) {
			String condName = productPriceCond.getInputParamEnumId();
			String value = productPriceCond.getCondValue();
			if(InputParam.VENDOR_ID.toString().equals(condName)){
				movRuleTemplate.setVendorId(value);
			}
			/*if(InputParam.WAREHOUSE.toString().equals(condName)){
				movRuleTemplate.setWarehouse(value);
			}
			if(InputParam.BRAND.toString().equals(condName)){
				movRuleTemplate.setBrand(value);
			}*/
		}
		return movRuleTemplate;
	}
	
	/**
	 * 设置ProductPriceAction字段到movRuleTemplate
	 * @param productPriceRule
	 * @param movRuleTemplate
	 * @since 2017年8月12日
	 * @author zr.wanghong
	 */
	private void setRuleTemplateAction(ProductPriceRule productPriceRule, MovRuleTemplate movRuleTemplate) {
		List<ProductPriceAction> actions = productPriceRule.getActions();
		for (ProductPriceAction productPriceAction : actions) {
			if(ProductPricePurposeId.MOV_CNY.toString().equals(productPriceAction.getProductPricePurposeId())){
				movRuleTemplate.setCnyMovAmount(productPriceAction.getAmount() != null ? productPriceAction.getAmount().toString() : null);
			}
			if(ProductPricePurposeId.MOV_USD.toString().equals(productPriceAction.getProductPricePurposeId())){
				movRuleTemplate.setUsdMovAmount(productPriceAction.getAmount() != null ? productPriceAction.getAmount().toString() : null);
			}
		}
	}
	
	/**
	 * 列表查询MOV策略模板
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @param ruleStatus 状态
	 * @param vendorId 供应商ID
	 * @param brandId 制造商ID
	 * @param ruleName 规则名称
	 * @param pricePurposeType 应用策略目标
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果集
	 * @since 2017年8月12日
	 * @author zr.wanghong
	 */
	public PageInfo<MovRuleTemplate> findList(String startDate,String endDate,String ruleStatus,String vendorId,String brandId,String ruleName
			,String pricePurposeType,int page, int size){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startTimed = null;
		if (StringUtils.isNotBlank(startDate)) {
			try {
				startTimed = sdf.parse(startDate);
			} catch (ParseException e) {
				logger.error("时间转换异常",e);
				throw new SystemException(e);
			}
		}
		Date endTimed = null;;
		if (StringUtils.isNotBlank(endDate)) {
			try {
				endTimed = sdf.parse(endDate);
			} catch (ParseException e) {
				logger.error("时间转换异常",e);
				throw new SystemException(e);
			}
		}
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("pricePurposeType", pricePurposeType);
		paramMap.put("startDate", startTimed);
		paramMap.put("endDate", endTimed);
		paramMap.put("ruleStatus", ruleStatus);
		paramMap.put("vendorId", vendorId);
		paramMap.put("ruleName", ruleName);
		List<ProductPriceRule> priceRules = ruleCommonManager.findList(paramMap,page,size);
		
		//结果集
		List<MovRuleTemplate> result = new ArrayList<>();
		for (ProductPriceRule productPriceRule : priceRules) {
			MovRuleTemplate priceRuleTemplate = transferPOToVO(productPriceRule);
			result.add(priceRuleTemplate);
		}
		//结果总条数
		Long listCount = ruleCommonManager.findListCount(paramMap);
		PageInfo<MovRuleTemplate> pageInfo = new PageInfo<>(result);
		pageInfo.setTotal(listCount);
		pageInfo.setPageSize(size);
		return  pageInfo;
	}
	
}
