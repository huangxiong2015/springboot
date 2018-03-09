package com.yikuyi.product.rule.delivery.manager;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.jasig.inspektr.audit.annotation.Audit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.yikuyi.product.rule.common.dao.ProductPriceActionDao;
import com.yikuyi.product.rule.common.dao.ProductPriceCondDao;
import com.yikuyi.product.rule.common.dao.ProductPriceRuleDao;
import com.yikuyi.product.rule.common.manager.RuleCommonManager;
import com.yikuyi.rule.delivery.vo.DeliveryInfo;
import com.yikuyi.rule.price.ProductPriceAction;
import com.yikuyi.rule.price.ProductPriceCond;
import com.yikuyi.rule.price.ProductPriceRule;
import com.yikuyi.rule.price.ProductPriceRule.PricePurposeType;
import com.ykyframework.exception.SystemException;

@Service
@Transactional
public class DeliveryManager {
	
	private static final Logger logger = LoggerFactory.getLogger(DeliveryManager.class);
	
	//等于操作符
	private static final String PRC_EQ = "PRC_EQ";
	
	@Autowired
	private ProductPriceRuleDao productPriceRuleDao;
	
	@Autowired
	private ProductPriceCondDao productPriceCondDao;
	
	@Autowired
	private ProductPriceActionDao productPriceActionDao;
	
	@Autowired
	private CacheManager cacheManager;
	
	@Autowired
	private RuleCommonManager ruleCommonManager;
	
	/**
	 * 查询交期模板列表
	 * @param page
	 * @param size
	 * @return
	 * @since 2016年12月8日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public PageInfo<DeliveryInfo> getDeliveryList(String startDate,String endDate,String ruleStatus,String vendorId,String ruleName
			,RowBounds rowBouds,int page,int size){
		logger.info("DeliveryManager---method:getDeliveryList");
		String ruleType = String.valueOf(ProductPriceRule.PricePurposeType.LEAD_TIME);
		List<ProductPriceRule> listInfo = ruleCommonManager.findList(startDate,endDate,ruleStatus,vendorId,ruleName,ruleType,page,size);
		List<DeliveryInfo> ruleList = new ArrayList<>();
		for(ProductPriceRule rule : listInfo){
			DeliveryInfo info =  handleRuleInfo(rule);
			ruleList.add(info);
		}
		PageInfo<DeliveryInfo> pageInfo = new PageInfo<>(ruleList);
		
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
		
		Long listCount = productPriceRuleDao.findListCount(ruleType,startTimed,endTimed,ruleStatus,vendorId,ruleName);
		pageInfo.setTotal(listCount);
		pageInfo.setPageSize(size);
		pageInfo.setPageNum(page);
		return pageInfo;
	}
	
	/**
	 * 新增交期模板
	 * @param info
	 * @return
	 * @since 2016年12月8日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Audit(action = "Leadtime Operationqqq;;;新建了'#info.deliveryRuleName'交期规则", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public DeliveryInfo addDelivery(@com.framework.springboot.audit.Param("info") 
			DeliveryInfo info,String userId,String ruleId){
		logger.info("DeliveryManager---method:addDelivery");
		//检验模板名称是否已经存在
		int count = checkRuleNameIsExist(info.getDeliveryRuleName(),"");
		if(count > 0){
			return null;
		}		
		info.setCreatedDate(new Date());
		//保存模板基本信息
		insertRuleInfo(info,userId,ruleId);
		//保存模板条件信息
		insertCondInfo(info,userId,ruleId);
		//保存模板规则信息
		insertActionInfo(info,userId,ruleId);
		info.setId(ruleId);
		info.setLastUpdateDate(new Date());
		return info;
	}
	
	/**
	 * 根据交期模块Id查询详情
	 * @param ruleId
	 * @return
	 * @since 2016年12月8日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public DeliveryInfo getDeliveryDetail(String ruleId){
		logger.info("DeliveryManager---method:getDeliveryDetail");
		ProductPriceRule ruleInfo = productPriceRuleDao.getDetailById(ruleId);
		return handleRuleInfo(ruleInfo);
	}
	
	/**
	 * 修改交期模板
	 * @param id
	 * @param info
	 * @return
	 * @since 2016年12月8日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Audit(action = "Leadtime Operationqqq;;;修改了'#info.deliveryRuleName'交期规则", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public DeliveryInfo updateDelivery(String id,
			@com.framework.springboot.audit.Param("info") DeliveryInfo info,String userId,String ruleId){
		logger.info("DeliveryManager---method:updateDelivery");
		//检验模板名称是否已经存在
		int count = checkRuleNameIsExist(info.getDeliveryRuleName(),id);
		if(count > 0){
			return null;
		}			
		//先根据id删除原来的交期模板，后在重新保存修改过的交期模板信息
		ProductPriceRule ruleInfo = new ProductPriceRule();
		ruleInfo.setPriceRuleId(id);
		ruleInfo.setRuleStatus(ProductPriceRule.RuleStatus.DELETED);
		ruleInfo.setLastUpdateUser(userId);
		productPriceRuleDao.update(ruleInfo);
		
		
		ProductPriceRule rule = productPriceRuleDao.getDetailById(id);
		info.setCreatedDate(rule.getCreatedDate());
		//保存模板基本信息
		insertRuleInfo(info,userId,ruleId);
		//保存模板条件信息
		insertCondInfo(info,userId,ruleId);
		//保存模板规则信息
		insertActionInfo(info,userId,ruleId);
		info.setId(ruleId);
		info.setLastUpdateDate(new Date());
		return info;
	}
	
	/**
	 * 启用、停用交期模板
	 * @param id
	 * @param stauts
	 * @return
	 * @since 2016年12月8日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Audit(action = "Leadtime Operationqqq;;;修改了'#info.deliveryRuleName'交期规则的状态为：'#stauts'", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public DeliveryInfo updateDeliveryStatus(String id,
			@com.framework.springboot.audit.Param("stauts") ProductPriceRule.RuleStatus stauts,String userId,
			@com.framework.springboot.audit.Param("info") DeliveryInfo info){
		logger.info("DeliveryManager---method:updateDeliveryStatus");
		Cache cache = cacheManager.getCache("leadTimeRule");
		ProductPriceRule ruleInfo = productPriceRuleDao.getDetailById(id);
		ruleInfo.setPriceRuleId(id);
		ruleInfo.setLastUpdateUser(userId);
		//拼装缓存中交期规则的key值
		StringBuilder sbStr = new StringBuilder();
		sbStr.append(info.getVerdonName() + "-");
		sbStr.append(info.getWarehouse() + "-");
		sbStr.append(info.getProductType());
		String key = "leadTimeRule." + sbStr.toString();
		//启用交期模板
		if((ProductPriceRule.RuleStatus.ENABLED).equals(stauts)){
			//先判断是否有其他模板的状态有没有在启用中
			Map<String,Object> map = new HashMap<>();
			map.put("purposeType", ProductPriceRule.PricePurposeType.LEAD_TIME);
			map.put("concatValue", sbStr.toString());
			Map<String,Object> sameRule = productPriceRuleDao.findSameRule(map);
			//没有相同条件的规则模板在启用中，则直接启用
			if(sameRule == null || sameRule.isEmpty()){
				ruleInfo.setRuleStatus(ProductPriceRule.RuleStatus.ENABLED);
				productPriceRuleDao.update(ruleInfo);
				//把当前启用的交期规则放入缓存中
				cache.put(key, info);
				return null;
			}else{    //如果有相同条件的规则模板正在启用中，则直接返回，前端提示停用后在启用
				info = new DeliveryInfo();
				info.setDeliveryRuleName(String.valueOf(sameRule.get("RULENAME")));
				info.setId(String.valueOf(sameRule.get("RULEID")));
				return info;
			}
		}
		//停用交期模板
		if((ProductPriceRule.RuleStatus.DISABLED).equals(stauts)){
			ruleInfo.setRuleStatus(ProductPriceRule.RuleStatus.DISABLED);
			productPriceRuleDao.update(ruleInfo);
			//清除缓存中的交期规则
			cache.evict(key);
			//判断同一个供应商是否还有没有启用的模板了
			Map<String,Object> verdonMap = new HashMap<>();
			verdonMap.put("pricePurposeType", ProductPriceRule.PricePurposeType.LEAD_TIME);
			verdonMap.put("condValue", info.getVerdonName());
			int verdonNum = productPriceRuleDao.getVendorRuleInfo(verdonMap);
			if(verdonNum > 0){
				return info;
			}else{
				return null;
			}
		}
		return info;
	}
	
	/**
	 * 删除交期模板
	 * @param id
	 * @since 2016年12月8日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Audit(action = "Leadtime Operationqqq;;;删除了'#ruleName'交期规则", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public void deleteDelivery(String id,String userId,
			@com.framework.springboot.audit.Param("ruleName") String ruleName){
		logger.info("DeliveryManager---method:deleteDelivery");
		ProductPriceRule ruleInfo = new ProductPriceRule();
		ruleInfo.setPriceRuleId(id);
		ruleInfo.setRuleStatus(ProductPriceRule.RuleStatus.DELETED);
		ruleInfo.setLastUpdateUser(userId);
		productPriceRuleDao.update(ruleInfo);
	}
	
	/**
	 * 处理从Dao返回的模板数据，并重新封装到新的实体中
	 * @param rule
	 * @return
	 * @since 2016年12月8日
	 * @author zr.wenjiao@yikuyi.com
	 */
	private DeliveryInfo handleRuleInfo(ProductPriceRule rule){
		DeliveryInfo info = new DeliveryInfo();
		info.setId(rule.getPriceRuleId());
		info.setDeliveryRuleName(rule.getRuleName());
		info.setLastUpdateDate(rule.getLastUpdateDate());
		info.setCreatedDate(rule.getCreatedDate());
		info.setDescription(rule.getDescription());
		info.setStatus(String.valueOf(rule.getRuleStatus()));
		//应用条件拼装
		String productType = "";
		List<ProductPriceCond> condsList = rule.getConds();
		for(ProductPriceCond cond : condsList){
			String condParam = cond.getInputParamEnumId();
			if((String.valueOf(ProductPriceCond.InputParam.VENDOR_ID)).equals(condParam)){
				info.setVerdonName(cond.getCondValue());
			}
			if((String.valueOf(ProductPriceCond.InputParam.WAREHOUSE)).equals(condParam)){
				info.setWarehouse(cond.getCondValue());
			}
            if((String.valueOf(ProductPriceCond.InputParam.PRODUCT_TYPE)).equals(condParam)){
				productType = cond.getCondValue();
				info.setProductType(Integer.valueOf(cond.getCondValue()));
			}
            if(String.valueOf(ProductPriceCond.InputParam.IS_SHOW_LEADTIME).equals(condParam)){
				info.setIsShowLeadTime(cond.getCondValue());;
			}
		}
		//操作规则拼装
		List<ProductPriceAction> actionsList = rule.getActions();
		for(ProductPriceAction action : actionsList){
			String uomId = action.getUomId();
			if("CNY".equals(uomId)){
				// 0是现货 1是排单
				if("0".equals(productType)){
					info.setLeadTimeMinCH(action.getAmount() != null? action.getAmount().intValue():0);
					info.setLeadTimeMaxCH(action.getExtendAmount() != null ? action.getExtendAmount().intValue():0);
				}else{
					//info.setLeadTimeCH(action.getAmount().intValue());
					//update by wanghong 2017/8/2
					info.setFactoryLeadTimeMinCH(action.getAmount() != null? action.getAmount().intValue():0);
					info.setFactoryLeadTimeMaxCH(action.getExtendAmount() != null ? action.getExtendAmount().intValue():0);
				}
			}else{
				if("0".equals(productType)){
					info.setLeadTimeMinHK(action.getAmount() != null? action.getAmount().intValue():0);
					info.setLeadTimeMaxHK(action.getExtendAmount() != null ? action.getExtendAmount().intValue():0);
				}else{
					//info.setLeadTimeHK(action.getAmount().intValue());
					info.setFactoryLeadTimeMinHK(action.getAmount() != null? action.getAmount().intValue():0);
					info.setFactoryLeadTimeMaxHK(action.getExtendAmount() != null ? action.getExtendAmount().intValue():0);
				}
			}
		}
		return info;
	}
	
	/**
	 * 保存交期模板基本信息
	 * @param info
	 * @param userId
	 * @since 2016年12月8日
	 * @author zr.wenjiao@yikuyi.com
	 */
	private void insertRuleInfo(DeliveryInfo info,String userId,String ruleId){
		ProductPriceRule ruleInfo = new ProductPriceRule();
		ruleInfo.setPriceRuleId(ruleId);
		ruleInfo.setPricePurposeType(ProductPriceRule.PricePurposeType.LEAD_TIME);
		ruleInfo.setRuleName(info.getDeliveryRuleName());
		ruleInfo.setDescription(info.getDescription());
		ruleInfo.setRuleStatus(ProductPriceRule.RuleStatus.DISABLED);
		ruleInfo.setCreator(userId);
		ruleInfo.setCreatedDate(info.getCreatedDate());
		ruleInfo.setLastUpdateUser(userId);
		ruleInfo.setLastUpdateDate(new Date());
		productPriceRuleDao.insert(ruleInfo);
	}
	
	/**
	 * 保存交期模板应用条件信息
	 * @param info
	 * @param userId
	 * @param ruleId
	 * @since 2016年12月8日
	 * @author zr.wenjiao@yikuyi.com
	 */
	private void insertCondInfo(DeliveryInfo info,String userId,String ruleId){
		ProductPriceCond condInfo = new ProductPriceCond();
		condInfo.setProductPriceRuleId(ruleId);
		condInfo.setCreator(userId);
		condInfo.setCreatedDate(new Date());
		condInfo.setLastUpdateUser(userId);
		condInfo.setLastUpdateDate(new Date());
		//保存供应商应用条件信息
		condInfo.setProductPriceCondSeqId("01");
		condInfo.setInputParamEnumId(String.valueOf(ProductPriceCond.InputParam.VENDOR_ID));
		condInfo.setOperatorEnumId(PRC_EQ);
		condInfo.setCondValue(info.getVerdonName());
		productPriceCondDao.insert(condInfo);
		//保存仓库应用条件信息
		condInfo.setProductPriceCondSeqId("02");
		condInfo.setInputParamEnumId(String.valueOf(ProductPriceCond.InputParam.WAREHOUSE));
		condInfo.setOperatorEnumId(PRC_EQ);
		condInfo.setCondValue(info.getWarehouse());
		productPriceCondDao.insert(condInfo);
		//保存商品类型应用条件信息
		condInfo.setProductPriceCondSeqId("03");
		condInfo.setInputParamEnumId(String.valueOf(ProductPriceCond.InputParam.PRODUCT_TYPE));
		condInfo.setOperatorEnumId(PRC_EQ);
		condInfo.setCondValue(String.valueOf(info.getProductType()));
		productPriceCondDao.insert(condInfo);
		
		//保存是否显示交期
		condInfo.setProductPriceCondSeqId("04");
		condInfo.setInputParamEnumId(ProductPriceCond.InputParam.IS_SHOW_LEADTIME.toString());
		condInfo.setOperatorEnumId(PRC_EQ);
		condInfo.setCondValue(info.getIsShowLeadTime());
		
		productPriceCondDao.insert(condInfo);
	}
	
	/**
	 * 保存交期模板计算规则信息
	 * @param info
	 * @param userId
	 * @param ruleId
	 * @since 2016年12月8日
	 * @author zr.wenjiao@yikuyi.com
	 */
	private void insertActionInfo(DeliveryInfo info,String userId,String ruleId){
		ProductPriceAction actionInfo = new ProductPriceAction();
		actionInfo.setProductPriceRuleId(ruleId);
		actionInfo.setProductPriceActionTypeId(String.valueOf(ProductPriceAction.ProductPriceActionTypeId.PRICE_POL));
		actionInfo.setCreator(userId);
		actionInfo.setCreatedDate(new Date());
		actionInfo.setLastUpdateUser(userId);
		actionInfo.setLastUpdateDate(new Date());
		//商品类型为现货
		if(info.getProductType() == 0){
			//保存商品类型为现货的香港交期时间
			actionInfo.setProductPriceCondSeqId("03");
			actionInfo.setProductPriceActionSeqId("01");
			actionInfo.setUomId("USD");
			actionInfo.setAmount(info.getLeadTimeMinHK() == null ? new BigDecimal(0) : new BigDecimal(info.getLeadTimeMinHK()));
			actionInfo.setExtendAmount(info.getLeadTimeMaxHK() == null ? new BigDecimal(0) : new BigDecimal(info.getLeadTimeMaxHK()));
			productPriceActionDao.insert(actionInfo);
			//保存商品类型为现货的国内交期时间
			actionInfo.setProductPriceCondSeqId("03");
			actionInfo.setProductPriceActionSeqId("02");
			actionInfo.setUomId("CNY");
			actionInfo.setAmount(info.getLeadTimeMinCH() == null ? new BigDecimal(0) : new BigDecimal(info.getLeadTimeMinCH()));
			actionInfo.setExtendAmount(info.getLeadTimeMaxCH() == null ? new BigDecimal(0) : new BigDecimal(info.getLeadTimeMaxCH()));
			productPriceActionDao.insert(actionInfo);
		}else{   //商品类型为排单
			//保存商品类型为排单的香港交期时间
			actionInfo.setProductPriceCondSeqId("03");
			actionInfo.setProductPriceActionSeqId("01");
			actionInfo.setUomId("USD");
			actionInfo.setAmount(info.getFactoryLeadTimeMinHK() == null ? new BigDecimal(0) : new BigDecimal(info.getFactoryLeadTimeMinHK()));
			actionInfo.setExtendAmount(info.getFactoryLeadTimeMaxHK() == null ? new BigDecimal(0) : new BigDecimal(info.getFactoryLeadTimeMaxHK()));
			productPriceActionDao.insert(actionInfo);
			//保存商品类型为排单的国内交期时间
			actionInfo.setProductPriceCondSeqId("03");
			actionInfo.setProductPriceActionSeqId("02");
			actionInfo.setUomId("CNY");
			actionInfo.setAmount(info.getFactoryLeadTimeMinCH() == null ? new BigDecimal(0) : new BigDecimal(info.getFactoryLeadTimeMinCH()));
			actionInfo.setExtendAmount(info.getFactoryLeadTimeMaxCH() == null ? new BigDecimal(0) : new BigDecimal(info.getFactoryLeadTimeMaxCH()));
			productPriceActionDao.insert(actionInfo);
		}
	}
	
	/**
	 * 检验模板名称是否存在
	 * @param ruleName
	 * @param ruleId
	 * @return
	 * @since 2016年12月13日
	 * @author zr.wenjiao@yikuyi.com
	 */
	private int checkRuleNameIsExist(String ruleName,String ruleId){
		ProductPriceRule ruleInfo = new ProductPriceRule();
		ruleInfo.setPricePurposeType(PricePurposeType.LEAD_TIME);
		ruleInfo.setRuleName(ruleName.trim());
		ruleInfo.setPriceRuleId(ruleId);
		return productPriceRuleDao.checkRuleNameIsExist(ruleInfo);
	}

}
