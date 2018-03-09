package com.yikuyi.product.rule.common.manager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yikuyi.product.rule.common.dao.ProductPriceActionDao;
import com.yikuyi.product.rule.common.dao.ProductPriceCondDao;
import com.yikuyi.product.rule.common.dao.ProductPriceRuleDao;
import com.yikuyi.product.rule.delivery.manager.LeadTimeManager;
import com.yikuyi.rule.price.ProductPriceAction;
import com.yikuyi.rule.price.ProductPriceCond;
import com.yikuyi.rule.price.ProductPriceRule;
import com.yikuyi.rule.price.ProductPriceRule.PricePurposeType;
import com.yikuyi.rule.price.ProductPriceRule.RuleStatus;
import com.ykyframework.exception.SystemException;
import com.ykyframework.model.IdGen;

@Service
@Transactional
public class RuleCommonManager {

	private static final Logger logger = LoggerFactory.getLogger(LeadTimeManager.class);
	
	@Autowired
	private ProductPriceRuleDao productPriceRuleDao;
	
	@Autowired
	private ProductPriceCondDao productPriceCondDao;
	
	@Autowired
	private ProductPriceActionDao productPriceActionDao;
	
	/**
	 * 新增规则
	 * @param productPriceRule
	 * @return
	 */
	public ProductPriceRule insert(ProductPriceRule productPriceRule) {
		String ruleId = String.valueOf(IdGen.getInstance().nextId());
		productPriceRule.setPriceRuleId(ruleId);
		//默认禁用状态
		productPriceRule.setRuleStatus(RuleStatus.DISABLED);
		productPriceRuleDao.insert(productPriceRule);
		List<ProductPriceAction> actions = productPriceRule.getActions();
		for (ProductPriceAction productPriceAction : actions) {
			productPriceAction.setProductPriceRuleId(ruleId);
			productPriceActionDao.insert(productPriceAction);
		}
		List<ProductPriceCond> conds = productPriceRule.getConds();
		for (ProductPriceCond productPriceCond : conds) {
			productPriceCond.setProductPriceRuleId(ruleId);
			productPriceCondDao.insert(productPriceCond);
		}
		logger.debug("add");
		return productPriceRule;
	}
	
	/**
	 * 修改规则
	 * @param productPriceRule
	 * @return
	 */
	public ProductPriceRule update(ProductPriceRule productPriceRule) {
		productPriceRule.setRuleStatus(RuleStatus.DELETED);
		productPriceRuleDao.update(productPriceRule);
		
		//更新，创建时间不变
		ProductPriceRule rule = productPriceRuleDao.getDetailById(productPriceRule.getPriceRuleId());
		productPriceRule.setCreatedDate(rule.getCreatedDate());
		this.insert(productPriceRule);
		logger.debug("add");
		return productPriceRule;
	}
	
	/**
	 * 根据ID查询规则模板
	 * @param ruleId
	 * @return
	 */
	public ProductPriceRule getDetailById(String ruleId) {
		ProductPriceRule productPriceRule = new ProductPriceRule();
		productPriceRule.setPriceRuleId(ruleId);
		return productPriceRuleDao.getDetailById(ruleId);
	}
	
	/**
	 * 启用、禁用、删除模板，更新状态
	 * @param ruleId 规则ID
	 * @param status 状态
	 * @return
	 */
	public ProductPriceRule enabled(ProductPriceRule productPriceRule) {
		productPriceRuleDao.update(productPriceRule);
		return productPriceRule;
	}
	
	/**
	 * 列表查询
	 * @param pricePurposeType
	 * @param page
	 * @param size
	 * @return
	 */
	public List<ProductPriceRule> findList(String startDate,String endDate,String ruleStatus,String vendorId,String ruleName,
			String pricePurposeType,int page, int size){
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
		
		RowBounds rowBouds = new RowBounds((page-1)*size, size);
		return productPriceRuleDao.findList(pricePurposeType,rowBouds.getOffset(),rowBouds.getLimit(),startTimed,endTimed,ruleStatus,vendorId,ruleName);
	}
	
	/**
	 * 列表查询
	 * @param pricePurposeType
	 * @param page
	 * @param size
	 * @return
	 */
	public List<ProductPriceRule> findList(Map<String, Object> paramMap,int page, int size){
		RowBounds rowBouds = new RowBounds((page-1)*size, size);
		paramMap.put("offset", rowBouds.getOffset());
		paramMap.put("limit", rowBouds.getLimit());
		return productPriceRuleDao.findList(paramMap);
	}
	
	/**
	 * 列表查询统计
	 * @param paramMap 参数map
	 * @return
	 * @since 2017年8月12日
	 * @author zr.wanghong
	 */
	public Long findListCount(Map<String, Object> paramMap){
		return productPriceRuleDao.findListCount(paramMap);
	}
	/**
	 * 列表查询统计
	 * @param pricePurposeType
	 * @return
	 */
	public Long findListCount(String startDate,String endDate,String ruleStatus,String vendorId,String ruleName,String pricePurposeType){
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
		return productPriceRuleDao.findListCount(pricePurposeType,startTimed,endTimed,ruleStatus,vendorId,ruleName);
	}
	
	/**
	 * 检验模板规则名称是否存在
	 * @param ruleName 规则名称
	 * @param ruleId 规则ID
	 * @param pricePurposeType 规则策略类型
	 * @return
	 */
	public int checkRuleNameIsExist(String ruleName,String ruleId,PricePurposeType pricePurposeType){
		ProductPriceRule ruleInfo = new ProductPriceRule();
		ruleInfo.setPricePurposeType(pricePurposeType);
		ruleInfo.setRuleName(ruleName.trim());
		ruleInfo.setPriceRuleId(ruleId);
		return productPriceRuleDao.checkRuleNameIsExist(ruleInfo);
	}
	
}
