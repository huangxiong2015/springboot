package com.yikuyi.product.rule.logistics.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.yikuyi.product.rule.common.dao.ProductPriceRuleDao;
import com.yikuyi.product.rule.common.manager.RuleCommonManager;
import com.yikuyi.rule.logistics.vo.LogisticsCondFee;
import com.yikuyi.rule.logistics.vo.LogisticsCondInfo;
import com.yikuyi.rule.logistics.vo.LogisticsInfo;
import com.yikuyi.rule.price.ProductPriceAction;
import com.yikuyi.rule.price.ProductPriceCond;
import com.yikuyi.rule.price.ProductPriceRule;
import com.yikuyi.rule.price.ProductPriceRule.PricePurposeType;

@Service
@Transactional
public class LogisticsManager {
	
	private static final Logger logger = LoggerFactory.getLogger(LogisticsManager.class);
	private static final String PRC_EQ = "PRC_EQ";
	private static final String CHINA = "CHINA";
	private static final String OUT_SIDE = "OUT_SIDE";
	private static final String IN_SIDE = "IN_SIDE";
	private static final String HONGKONG = "HONGKONG";
	private static final String PRICE_POL = "PRICE_POL";
	
	@Autowired
	private ProductPriceRuleDao productPriceRuleDao;
	
	@Autowired
	private RuleCommonManager ruleCommonManager;
	
	@Autowired
	private CacheManager cacheManager;
	
	/**
	 * 新增运费模板
	 * @param info
	 * @param userId
	 * @param ruleId
	 * @return
	 * @since 2016年12月10日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Audit(action = "Logistics Operationqqq;;;新建了'#info.logisticsRuleName'运费模板", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public LogisticsInfo addLogistics(@com.framework.springboot.audit.Param("info") LogisticsInfo info,String userId){
		logger.info("LogisticsManager---method:addLogistics");
		//检验模板名称是否已经存在
		int count = checkRuleNameIsExist(info.getLogisticsRuleName(),"");
		if(count > 0){
			return null;
		}
		//保存运费模板基本信息
		ProductPriceRule ruleInfo = insertRuleInfo(info,userId);
		info.setId(ruleInfo.getPriceRuleId());
		info.setLastUpdateDate(new Date());
		return info;
	} 
	
	/**
	 * 查询运费模板列表
	 * @return
	 * @since 2016年12月10日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public PageInfo<LogisticsInfo> getLogisticsList(){
		logger.info("LogisticsManager---method:getLogisticsList");
		String ruleType = String.valueOf(ProductPriceRule.PricePurposeType.SHIPMENT_PRICE);
		List<ProductPriceRule> listInfo = productPriceRuleDao.findList(ruleType,RowBounds.NO_ROW_OFFSET,RowBounds.NO_ROW_LIMIT,null,null,null,null,null);
		List<LogisticsInfo> infoList = new ArrayList<>();
		for(ProductPriceRule rule : listInfo){
			LogisticsInfo info = handleRuleInfo(rule);
			infoList.add(info);
		}
		PageInfo<LogisticsInfo> pageInfo = new PageInfo<>(infoList);
		return pageInfo;
	}
	
	/**
	 * 根据运费模块Id查询详情
	 * @param ruleId
	 * @return
	 * @since 2016年12月10日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public LogisticsInfo getLogisticsDetail(String ruleId){
		logger.info("LogisticsManager---method:getLogisticsDetail");
		ProductPriceRule ruleInfo = productPriceRuleDao.getDetailById(ruleId);
		return handleRuleInfo(ruleInfo);
	}
	
	/**
	 * 修改运费模板
	 * @param id
	 * @param info
	 * @param userId
	 * @param ruleId
	 * @return
	 * @since 2016年12月10日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Audit(action = "Logistics Operationqqq;;;修改了'#info.logisticsRuleName'运费模板", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public LogisticsInfo updateLogistics(String id,
			@com.framework.springboot.audit.Param("info") LogisticsInfo info,String userId){
		logger.info("LogisticsManager---method:updateLogistics");
		//检验模板名称是否已经存在
		int count = checkRuleNameIsExist(info.getLogisticsRuleName(),id);
		if(count > 0){
			return null;
		}		
		//先根据id删除原来的运费模板，后在重新保存修改过的运费模板信息
		ProductPriceRule ruleInfo = new ProductPriceRule();
		ruleInfo.setPriceRuleId(id);
		ruleInfo.setRuleStatus(ProductPriceRule.RuleStatus.DELETED);
		ruleInfo.setLastUpdateUser(userId);
		productPriceRuleDao.update(ruleInfo);	
				
		//保存运费模块的基本信息
		ProductPriceRule rule = insertRuleInfo(info,userId);
		info.setId(rule.getPriceRuleId());
		info.setLastUpdateDate(new Date());
		return info;
	}
	
	/**
	 * 启用、停用运费模板
	 * @param id
	 * @param stauts
	 * @param userId
	 * @return
	 * @since 2016年12月10日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Audit(action = "Logistics Operationqqq;;;修改了'#info.logisticsRuleName'运费模板的状态为：'#stauts'", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public LogisticsInfo updateLogisticsStatus(String id,
			@com.framework.springboot.audit.Param("stauts") ProductPriceRule.RuleStatus stauts,String userId,
			@com.framework.springboot.audit.Param("info") LogisticsInfo info){
		logger.info("LogisticsManager---method:updateLogisticsStatus");
		Cache cache = cacheManager.getCache("logisticsRule");
		String key = "logisticsRule.user-defined";
		ProductPriceRule ruleInfo = productPriceRuleDao.getDetailById(id);
		ruleInfo.setPriceRuleId(id);
		ruleInfo.setLastUpdateUser(userId);
		//启用交期模板
		if((ProductPriceRule.RuleStatus.ENABLED).equals(stauts)){
			//先判断是否有其他模板的状态有没有在启用中
			Map<String,Object> map = new HashMap<>();
			map.put("purposeType", ProductPriceRule.PricePurposeType.SHIPMENT_PRICE);
			Map<String,Object> sameRule = productPriceRuleDao.findSameRule(map);
			//没有相同条件的规则模板在启用中，则直接启用
			if(sameRule == null || sameRule.isEmpty()){
				ruleInfo.setRuleStatus(ProductPriceRule.RuleStatus.ENABLED);
				productPriceRuleDao.update(ruleInfo);
				//把当前启用的交期规则放入缓存中
				Map<String,Object> cacheMap = getCacheValue(info);
				if(cacheMap != null){
					cache.put(key, cacheMap);
				}
				return null;
			}else{     //如果有相同条件的规则模板正在启用中，则直接返回，前端提示停用后在启用
				info = new LogisticsInfo();
				info.setLogisticsRuleName(String.valueOf(sameRule.get("RULENAME")));
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
			return null;
		}	
		return info;
	}
	
	/**
	 * 删除运费模板
	 * @param id
	 * @param userId
	 * @since 2016年12月10日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Audit(action = "Logistics Operationqqq;;;删除了'#ruleName'运费模板", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public void deleteLogistics(String id,String userId,
			@com.framework.springboot.audit.Param("ruleName") String ruleName){
		logger.info("LogisticsManager---method:deleteLogistics");
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
	 * @since 2016年12月12日
	 * @author zr.wenjiao@yikuyi.com
	 */
	private LogisticsInfo handleRuleInfo(ProductPriceRule rule){
		Map<String,List<ProductPriceCond>> map = new HashMap<>();
		LogisticsInfo info = new LogisticsInfo();
		info.setId(rule.getPriceRuleId());
		info.setLogisticsRuleName(rule.getRuleName());
		info.setDescription(rule.getDescription());
		info.setStatus(String.valueOf(rule.getRuleStatus()));
		info.setLastUpdateDate(rule.getLastUpdateDate());
		//运费模板应用条件
		List<ProductPriceCond> condsList = rule.getConds();
		//运费模板规则
		List<ProductPriceAction> actionsList = rule.getActions();
		if(condsList != null && !condsList.isEmpty()){
			if(condsList.size() > 1){
				info.setPinkage(false);
			}else{
				info.setPinkage(true);
			}
			for(ProductPriceCond cond : condsList){
				String condSeqId = cond.getProductPriceCondSeqId();
				if(map.containsKey(condSeqId)){
					List<ProductPriceCond> list = map.get(condSeqId);
					list.add(cond);
					map.put(condSeqId, list);
				}else{
					List<ProductPriceCond> list = new ArrayList<>();
					list.add(cond);
					map.put(condSeqId, list);
				}
			}
		}
		info.setCondInfo(handleRuleCondInfo(actionsList,map));
		return info;
	}
	
	/**
	 * 处理运费模板的应用条件数据
	 * @param actionsList
	 * @param map
	 * @return
	 * @since 2016年12月13日
	 * @author zr.wenjiao@yikuyi.com
	 */
	private List<LogisticsCondInfo> handleRuleCondInfo(List<ProductPriceAction> actionsList,Map<String,List<ProductPriceCond>> map){
		LogisticsCondInfo condCH = new LogisticsCondInfo();
		LogisticsCondInfo condHK = new LogisticsCondInfo();
		List<LogisticsCondFee> feeListCH = new ArrayList<>();
		List<LogisticsCondFee> feeListHK = new ArrayList<>();
		List<LogisticsCondInfo> listCond = new ArrayList<>();
		for(Map.Entry<String,List<ProductPriceCond>> entry:map.entrySet()){    
			List<ProductPriceCond> condList = entry.getValue();
			BigDecimal orderAmount = null;
			String shipAddress = "";
			String shipProvince = "";
			String seqId = "";
			for(ProductPriceCond cond : condList){
				seqId = cond.getProductPriceCondSeqId();
				String inputEnumId = cond.getInputParamEnumId();
				switch(inputEnumId){
				     case "ORDER_AMOUNT":
				    	 orderAmount = new BigDecimal(cond.getCondValue());
				    	 break;
				     case "SHIP_ADDRESS":
				    	 shipAddress = cond.getCondValue();
				    	 break;
				     case "SHIP_PROVINCE":
				    	 shipProvince = cond.getCondValue();
				    	 break;
				     default:
				    	 break;
				}
			}
			if((CHINA).equals(shipAddress)){
				condCH.setPinkageAmount(orderAmount);
				condCH.setDeliveryPlace("CH");
				if(!shipProvince.isEmpty()){
					feeListCH.addAll(handleActionInfo(actionsList,seqId,shipProvince));
				}
			}else{
				condHK.setDeliveryPlace("HK");
				condHK.setPinkageAmount(orderAmount);
				feeListHK.addAll(handleActionInfo(actionsList,seqId,HONGKONG));
			}
		}
		if(feeListCH != null && !feeListCH.isEmpty()){
			condCH.setFaltPinkageAmount(feeListCH);
			listCond.add(condCH);
		}
		if(feeListHK != null && !feeListHK.isEmpty()){
			condHK.setFaltPinkageAmount(feeListHK);
			listCond.add(condHK);
		}
		return listCond;
	}
	
	/**
	 * 处理运费模板的规则信息
	 * @param actionsList
	 * @param seqId
	 * @param shipProvince
	 * @return
	 * @since 2016年12月13日
	 * @author zr.wenjiao@yikuyi.com
	 */
	private List<LogisticsCondFee> handleActionInfo(List<ProductPriceAction> actionsList,String seqId,String shipProvince){
		List<LogisticsCondFee> feeList = new ArrayList<>();
		for(ProductPriceAction action : actionsList){
			LogisticsCondFee fee = new LogisticsCondFee();
			if(seqId.equals(action.getProductPriceCondSeqId())){
				fee.setShipSite(shipProvince);
				fee.setFaltAmount(action.getAmount());
				feeList.add(fee);
			}
		}
		return feeList;
	}
	
	/**
	 * 保存运费模板基本信息
	 * @param info
	 * @param userId
	 * @param ruleId
	 * @since 2016年12月10日
	 * @author zr.wenjiao@yikuyi.com
	 */
	private ProductPriceRule insertRuleInfo(LogisticsInfo info,String userId){
		List<LogisticsCondInfo> condList = info.getCondInfo();
		ProductPriceRule ruleInfo = new ProductPriceRule();
		ruleInfo.setPricePurposeType(ProductPriceRule.PricePurposeType.SHIPMENT_PRICE);
		ruleInfo.setRuleName(info.getLogisticsRuleName().trim());
		ruleInfo.setDescription(info.getDescription());
		ruleInfo.setCreator(userId);
		ruleInfo.setCreatedDate(new Date());
		ruleInfo.setLastUpdateUser(userId);
		ruleInfo.setLastUpdateDate(new Date());
		List<ProductPriceCond> conds = new ArrayList<>();
		List<ProductPriceAction> actions = new ArrayList<>();
		ProductPriceCond ppc = new ProductPriceCond();
		if(info.getPinkage()){
			ppc.setProductPriceCondSeqId("01");
			ppc.setInputParamEnumId(ProductPriceCond.InputParam.SHIP_CHARGE_MODE.toString());
			ppc.setOperatorEnumId(PRC_EQ);
			ppc.setCondValue("SELLER_BEAR");
			conds.add(ppc);
		}else{
			for(LogisticsCondInfo cond : condList){
				String deliveryPlace = cond.getDeliveryPlace();
				List<LogisticsCondFee> feeList = cond.getFaltPinkageAmount();
				if("CH".equals(deliveryPlace)){
					conds.addAll(insertCondInfo(cond,feeList,userId,CHINA));
					actions.addAll(insertActionInfo(feeList,userId,cond.getPinkageAmount()));
				}else{
					conds.addAll(insertCondInfo(cond,feeList,userId,HONGKONG));
					actions.addAll(insertActionInfo(feeList,userId,cond.getPinkageAmount()));
				}
			}
		}
		ruleInfo.setConds(conds);
		ruleInfo.setActions(actions);
		ruleInfo = ruleCommonManager.insert(ruleInfo);
		return ruleInfo;
	}
	
	/**
	 * 保存运费模板应用条件信息
	 * @param info
	 * @param userId
	 * @param ruleId
	 * @since 2016年12月12日
	 * @author zr.wenjiao@yikuyi.com
	 */
	private List<ProductPriceCond> insertCondInfo(LogisticsCondInfo cond,List<LogisticsCondFee> feeList,String userId,String address){
		List<ProductPriceCond> ppcSave = new ArrayList<>();
		if(cond.getPinkageAmount() != null){
			String condSeqId ;
			if((CHINA).equals(address)){
				condSeqId = "01";
			}else{
				condSeqId = "04";
			}
			ppcSave.addAll(handleSaveConds(condSeqId,address,cond.getPinkageAmount(),userId));
			for(LogisticsCondFee fee : feeList){
				if((IN_SIDE).equals(fee.getShipSite())){
					ppcSave.addAll(handleSaveConds("02",address,cond.getPinkageAmount(),userId));
					ProductPriceCond ppc = new ProductPriceCond();
					ppc.setCreator(userId);
					ppc.setCreatedDate(new Date());
					ppc.setLastUpdateUser(userId);
					ppc.setLastUpdateDate(new Date());
					ppc.setProductPriceCondSeqId("02");
					ppc.setInputParamEnumId(ProductPriceCond.InputParam.SHIP_PROVINCE.toString());
					ppc.setOperatorEnumId(PRC_EQ);
					ppc.setCondValue(IN_SIDE);
					ppcSave.add(ppc);
				}else if((OUT_SIDE).equals(fee.getShipSite())){
					ppcSave.addAll(handleSaveConds("03",address,cond.getPinkageAmount(),userId));
					ProductPriceCond ppc = new ProductPriceCond();
					ppc.setCreator(userId);
					ppc.setCreatedDate(new Date());
					ppc.setLastUpdateUser(userId);
					ppc.setLastUpdateDate(new Date());
					ppc.setProductPriceCondSeqId("03");
					ppc.setInputParamEnumId(ProductPriceCond.InputParam.SHIP_PROVINCE.toString());
					ppc.setOperatorEnumId(PRC_EQ);
					ppc.setCondValue(OUT_SIDE);
					ppcSave.add(ppc);
				}else{
					ppcSave.addAll(handleSaveConds("05",address,cond.getPinkageAmount(),userId));
				}
			}
		}
		return ppcSave;
	}
	
	/**
	 * 拼装运费模板应用条件的数据
	 * @param seqId
	 * @param address
	 * @param pinkageAmount
	 * @param userId
	 * @return
	 * @since 2016年12月13日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public List<ProductPriceCond> handleSaveConds(String seqId,String address,BigDecimal pinkageAmount,String userId){
		List<ProductPriceCond> ppcSave = new ArrayList<>();
		ProductPriceCond ppc = new ProductPriceCond();
		ppc.setCreator(userId);
		ppc.setCreatedDate(new Date());
		ppc.setLastUpdateUser(userId);
		ppc.setLastUpdateDate(new Date());
		ppc.setProductPriceCondSeqId(seqId);
		ppc.setInputParamEnumId(ProductPriceCond.InputParam.SHIP_CHARGE_MODE.toString());
		ppc.setOperatorEnumId(PRC_EQ);
		ppc.setCondValue("CUSTOM");
		ppcSave.add(ppc);
		ProductPriceCond ppc1 = new ProductPriceCond();
		ppc1.setCreator(userId);
		ppc1.setCreatedDate(new Date());
		ppc1.setLastUpdateUser(userId);
		ppc1.setLastUpdateDate(new Date());
		ppc1.setProductPriceCondSeqId(seqId);
		ppc1.setInputParamEnumId(ProductPriceCond.InputParam.SHIP_ADDRESS.toString());
		ppc1.setOperatorEnumId(PRC_EQ);
		ppc1.setCondValue(address);
		ppcSave.add(ppc1);
		ProductPriceCond ppc2 = new ProductPriceCond();
		ppc2.setCreator(userId);
		ppc2.setCreatedDate(new Date());
		ppc2.setLastUpdateUser(userId);
		ppc2.setLastUpdateDate(new Date());
		ppc2.setProductPriceCondSeqId(seqId);
		ppc2.setInputParamEnumId(ProductPriceCond.InputParam.ORDER_AMOUNT.toString());
		ppc2.setOperatorEnumId("PRC_LEQ");
		ppc2.setCondValue(String.valueOf(pinkageAmount));
		ppcSave.add(ppc2);
		return ppcSave;
	}
	
	/**
	 * 保存运费模板计算规则信息
	 * @param info
	 * @param userId
	 * @param ruleId
	 * @since 2016年12月12日
	 * @author zr.wenjiao@yikuyi.com
	 */
	private List<ProductPriceAction> insertActionInfo(List<LogisticsCondFee> feeList,String userId,BigDecimal pinkageAmount){
		List<ProductPriceAction> actionList = new ArrayList<>();
		if(pinkageAmount != null){
			for(LogisticsCondFee fee : feeList){
				if((IN_SIDE).equals(fee.getShipSite())){
					ProductPriceAction action = new ProductPriceAction();
					action.setCreator(userId);
					action.setCreatedDate(new Date());
					action.setLastUpdateUser(userId);
					action.setLastUpdateDate(new Date());
					action.setProductPriceActionSeqId("01");
					action.setProductPriceActionTypeId(PRICE_POL);
					action.setProductPriceCondSeqId("02");
					action.setUomId("CNY");
					action.setAmount(fee.getFaltAmount());
					actionList.add(action);
				}else if((OUT_SIDE).equals(fee.getShipSite())){
					ProductPriceAction action = new ProductPriceAction();
					action.setCreator(userId);
					action.setCreatedDate(new Date());
					action.setLastUpdateUser(userId);
					action.setLastUpdateDate(new Date());
					action.setProductPriceActionSeqId("01");
					action.setProductPriceActionTypeId(PRICE_POL);
					action.setProductPriceCondSeqId("03");
					action.setUomId("CNY");
					action.setAmount(fee.getFaltAmount());
					actionList.add(action);
				}else{
					ProductPriceAction action = new ProductPriceAction();
					action.setCreator(userId);
					action.setCreatedDate(new Date());
					action.setLastUpdateUser(userId);
					action.setLastUpdateDate(new Date());
					action.setProductPriceActionSeqId("01");
					action.setProductPriceActionTypeId(PRICE_POL);
					action.setProductPriceCondSeqId("05");
					action.setUomId("USD");
					action.setAmount(fee.getFaltAmount());
					actionList.add(action);
				}
			}
		}
		return actionList;
	}
	
	/**
	 * 拼装存放redis中的运费规则信息
	 * @param info
	 * @return
	 * @since 2016年12月13日
	 * @author zr.wenjiao@yikuyi.com
	 */
	private Map<String,Object> getCacheValue(LogisticsInfo info){
		Map<String,Object> map = new HashMap<>();
		List<LogisticsCondInfo> condList = info.getCondInfo();
		for(LogisticsCondInfo cond : condList){
			StringBuilder str = new StringBuilder();
			str.append("pinkage:" + cond.getPinkageAmount());
			List<LogisticsCondFee> feeList = cond.getFaltPinkageAmount();
			for(LogisticsCondFee fee : feeList){
				if(("HK").equals(cond.getDeliveryPlace())){
					str.append("|HKFaltFee:" + fee.getFaltAmount());
				}else{
					str.append("|" + fee.getShipSite());
					str.append(":" + fee.getFaltAmount());
				}
			}
			map.put(cond.getDeliveryPlace(), str.toString());
		}
		return map;
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
		ruleInfo.setPricePurposeType(PricePurposeType.SHIPMENT_PRICE);
		ruleInfo.setRuleName(ruleName.trim());
		ruleInfo.setPriceRuleId(ruleId);
		return productPriceRuleDao.checkRuleNameIsExist(ruleInfo);
	}

}
