/*
 * Created: 2017年8月14日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.goods.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.yikuyi.activity.vo.ActivityProductVo;
import com.yikuyi.basedata.ShipmentClientBuilder;
import com.yikuyi.basedata.common.model.Currency;
import com.yikuyi.basedata.resource.UomConversionClient;
import com.yikuyi.party.PartyClientBuilder;
import com.yikuyi.party.supplier.SupplierVo;
import com.yikuyi.product.model.Product;
import com.yikuyi.product.model.ProductPrice;
import com.yikuyi.product.vo.ProductVo;
import com.yikuyi.rule.mov.vo.MovInfo;
import com.yikuyi.rule.mov.vo.MovRuleTemplate;
import com.yikuyi.rule.mov.vo.MovValidResult;
import com.yikuyi.rule.mov.vo.MovValidationParam;
import com.ykyframework.exception.BusinessException;
import com.ykyframework.exception.SystemException;

@Service
@Transactional
public class MovQueryManagerV2 {
	
	private static final Logger logger = LoggerFactory.getLogger(MovQueryManagerV2.class);
	
	private static final String KEY_MOVRULE_TEMPLATE_CACHEMAP = "MovRuleTemplateCacheMap";
	
	private static final String KEY_EXCHANGERATE = "exchangeRate";
	private static final String KEY_TAXRATE = "taxRate";
	
	private static final String CNY = "CNY";
	private static final String USD = "USD";
	
	/** USD转CNY */
	private static final String USD_TO_CNY = "0";
	/** CNY转USD*/
	private static final String CNY_TO_USD = "1";
	/** 本位币*/
	private static final String BASE_CURRENCY_TRANSFER = "2";
	
	/**大于*/
	public static final int BIGDECIMAL_GREAT_THAN = 1;
	/**等于*/
	public static final int BIGDECIMAL_EQUAL = 0;
	/**小于*/
	public static final int BIGDECIMAL_LESS_THAN = -1;
	
	@Value("${api.basedata.serverUrlPrefix}")
	private String basedataServerUrlPrefix;

	@Autowired
	private CacheManager cacheManager;
	
	@Autowired
	private ShipmentClientBuilder shipmentClientBuilder;
	
	@Autowired
	private MongoRepository<Product, String> productRepository;
	
	@Autowired
	private PartyClientBuilder partyClientBuilder;
	
	@Autowired
	private ProductManager productManager;
	
	/**
	 * 根据实体集合查询MOV信息
	 * @param productVos
	 * @return
	 * @since 2017年8月14日
	 * @author zr.wanghong
	 */
	public List<MovInfo> queryByEntities(List<ProductVo> productVos){
		//1.汇率和税率
		final Map<String,Object> rates = this.getExchangeRateAndTaxRate(null);
		
		Set<String> supplierIds = productVos.stream().map(ProductVo::getVendorId).collect(Collectors.toSet());
		Map<String,SupplierVo> suppliers = partyClientBuilder.supplierClient().getSupplierSimple(supplierIds);
		
		List<MovInfo> resultMovInfos = new ArrayList<>();
		List<Future<Void>> resultList = new ArrayList<>();
		productVos.stream().forEach(productVo ->  resultList.add(calculateMov(rates, resultMovInfos, productVo,suppliers)) );
		
		//确保异步执行完成，主线程继续往下走
		for(Future<Void> futureInfo : resultList){
			try {
				futureInfo.get();
			} catch (Exception e) {
				logger.error("异步获取MOV价格异常:{},{}",e.getMessage(),e);
			}
		}
		return resultMovInfos;
	}
	
	/**
	 * 根据商品ID集合查询MOV信息
	 * @param ids
	 * @return
	 * @since 2017年8月14日
	 * @author zr.wanghong
	 */
	public List<MovInfo> queryByIds(List<String> ids){
		Iterator<Product> productInfo = productRepository.findAll(ids).iterator();
		List<ProductVo> productVos = new ArrayList<>();
		while (productInfo.hasNext()) {
			Product product = productInfo.next();
			ProductVo productVo = new ProductVo();
			BeanUtils.copyProperties(product, productVo);
			productVos.add(productVo);
		}
		
		//有活动则合并活动信息
		productManager.mergeActivity(productVos,"N");
		return this.queryByEntities(productVos);
	}
	
	/**
	 * 计算MOV信息
	 * @param rates
	 * @param resultMovInfos
	 * @param productVo
	 * @since 2017年8月14日
	 * @author zr.wanghong
	 */
	@Async
	private Future<Void> calculateMov(final Map<String, Object> rates, List<MovInfo> resultMovInfos, ProductVo productVo,Map<String, SupplierVo> vendorMap) {
		List<MovRuleTemplate> movRuleTemplates = this.findMovRuleTemplate(productVo);
		MovInfo returnMovInfo = new MovInfo();
		returnMovInfo.setProductId(productVo.getId());
		returnMovInfo.setVendorId(productVo.getVendorId());
		returnMovInfo.setBrand(String.valueOf(productVo.getSpu().getManufacturerId()));
		returnMovInfo.setWarehouse(productVo.getSourceId());
		this.setMovStatus(productVo, vendorMap, returnMovInfo);
		
		
		List<MovRuleTemplate> productMovRuleTemplates = new ArrayList<>();
		//同一个商品可能同时满足多个mov模板
		if(!CollectionUtils.isEmpty(movRuleTemplates)){
			movRuleTemplates.stream().forEach( movRuleTemplate -> {
				String movCNY = null;
				String movUSD = null;
				if(StringUtils.isNotEmpty(movRuleTemplate.getCnyMovAmount()) && isNum(movRuleTemplate.getCnyMovAmount())) {
					movCNY = this.keep2EffectiveNumber(new BigDecimal(movRuleTemplate.getCnyMovAmount())).toString();
				}
				if(StringUtils.isNotEmpty(movRuleTemplate.getUsdMovAmount()) && isNum(movRuleTemplate.getUsdMovAmount())) {
					movUSD = this.keep2EffectiveNumber(new BigDecimal(movRuleTemplate.getUsdMovAmount())).toString();
				}
				String ruleType = movRuleTemplate.getRuleType();
				//ruleType 1:商品mov 0:供应商mov
				if("0".equals(ruleType)){
					String movType = movRuleTemplate.getMovType();
					//设置供应商Mov 
					if("vendor".equals(movType)){
						returnMovInfo.setVendorMovCNY(movCNY);
						returnMovInfo.setVendorMovUSD(movUSD);
					}
					//设置仓库Mov 
					if("warehouse".equals(movType)){
						returnMovInfo.setWarehouseMovCNY(movCNY);
						returnMovInfo.setWarehouseMovUSD(movUSD);
					}
					//设置品牌Mov 
					if("brand".equals(movType)){
						returnMovInfo.setBrandMovCNY(movCNY);
						returnMovInfo.setBrandMovUSD(movUSD);
					}
				}
				if("1".equals(ruleType)){
					productMovRuleTemplates.add(movRuleTemplate);
				}
				
			});
		}
		
		//查询商品是否在做活动,有活动则按活动的币种类型计算
		ActivityProductVo ac = productVo.getActivityProductVo();
		String currencyType = "";
		if(ac != null){
			//活动中商品可能改变了币种类型
			currencyType = ac.getCurrencyUomId();
			if(StringUtils.isEmpty(currencyType)){
				currencyType = this.getCurrencyType(productVo);
			}
		}else{
			currencyType = this.getCurrencyType(productVo);
		}
		
		//获取商品本身设定的mov
		String productMov = this.calProductMovByCurrency(productVo, rates);
		
		//商品本身设置了mov字段值,则根据这个值计算mov
		if(StringUtils.isNotEmpty(productMov) && isNum(productMov)){
			//交易币种类型为人民币则只有国内mov，为美元则有国内和国外mov
			if(CNY.equals(currencyType)){
				returnMovInfo.setProductMovCNY(StringUtils.isNotEmpty(productMov) && isNum(productMov) ? this.keep2EffectiveNumber(new BigDecimal(productMov)).toString():productMov);
			}
			if(USD.equals(currencyType) && StringUtils.isNotEmpty(productMov) && isNum(productMov)){
		    	BigDecimal productMovUSD = new BigDecimal(productMov);
		    	BigDecimal exchangeRate = new BigDecimal(rates.get(KEY_EXCHANGERATE).toString());
				BigDecimal taxRate = new BigDecimal( rates.get(KEY_TAXRATE).toString());
				BigDecimal productMovCNY = productMovUSD.multiply(exchangeRate).multiply(new BigDecimal("1").add(taxRate));
				productMovUSD = this.keep2EffectiveNumber(productMovUSD);
				productMovCNY = this.keep2EffectiveNumber(productMovCNY);
				returnMovInfo.setProductMovCNY(productMovCNY.toString());
				returnMovInfo.setProductMovUSD(productMovUSD.toString());
			}
		}else{
			MovRuleTemplate movRuleTemplate = this.getProductMovTemplate(productVo, productMovRuleTemplates, currencyType);
			//商品字段是没有mov值，则根据策略设置的值
			if(movRuleTemplate != null){
				if(StringUtils.isNotEmpty(movRuleTemplate.getCnyMovAmount()) && isNum(movRuleTemplate.getCnyMovAmount())){
					returnMovInfo.setProductMovCNY(this.keep2EffectiveNumber(new BigDecimal(movRuleTemplate.getCnyMovAmount())).toString());
				}
				if(StringUtils.isNotEmpty(movRuleTemplate.getUsdMovAmount()) && isNum(movRuleTemplate.getUsdMovAmount())){
					returnMovInfo.setProductMovUSD(this.keep2EffectiveNumber(new BigDecimal(movRuleTemplate.getUsdMovAmount())).toString());
				}
			}
		}
		resultMovInfos.add(returnMovInfo);
		return new AsyncResult<>(null);
	}
	
	/**
	 * 获取商品本身的mov设定金额，活动的中的商品可能转换了币种类型
	 * 如果币种类型改变，需要计算转换汇率和税率后的金额
	 * @param productVo
	 * @param rates
	 * @return
	 */
	private String calProductMovByCurrency(ProductVo productVo ,final Map<String, Object> rates){
		String currencyTransferType = this.getCurrencyTransferType(productVo);
		String productMov = productVo.getMov();
		if(StringUtils.isEmpty(productMov)){
			return productMov;
		}
		BigDecimal exchangeRate = new BigDecimal(rates.get(KEY_EXCHANGERATE).toString());
		BigDecimal taxRate = new BigDecimal( rates.get(KEY_TAXRATE).toString());
		BigDecimal mov = new BigDecimal(productMov);
		if(USD_TO_CNY.equals(currencyTransferType)){
			if(StringUtils.isNotEmpty(productMov) && isNum(productMov)){
				mov = mov.multiply(exchangeRate).multiply(new BigDecimal("1").add(taxRate));
				return mov.toString();
			}
		}
		if(CNY_TO_USD.equals(currencyTransferType)){
			if(StringUtils.isNotEmpty(productMov) && isNum(productMov)){
				mov = mov.divide(exchangeRate).divide(new BigDecimal("1").add(taxRate));
				return mov.toString();
			}
		}
		if(BASE_CURRENCY_TRANSFER.equals(currencyTransferType)){
			return mov.toString();
		}
		return "";
		
	}
	
	/**
	 * 获取活动中的商品币种转换类型
	 * @param productVo
	 * @return
	 */
	private String getCurrencyTransferType(ProductVo productVo){
		ActivityProductVo ac = productVo.getActivityProductVo();
		String oldCurrencyType = this.getCurrencyType(productVo);
		String newCurrencyType = this.getCurrencyType(productVo);
		String currencyTransferType = "";
		
		if(ac != null){
			//活动中商品可能改变了币种类型
			newCurrencyType = ac.getCurrencyUomId();
		}
		//USD 转CNY
		if(StringUtils.isNotEmpty(oldCurrencyType) && StringUtils.isNotEmpty(newCurrencyType) 
				&& "USD".equals(oldCurrencyType) && "CNY".equals(newCurrencyType)){
			currencyTransferType = USD_TO_CNY;
		}
		//CNY 转USD
		if(StringUtils.isNotEmpty(oldCurrencyType) && StringUtils.isNotEmpty(newCurrencyType) 
				&& "CNY".equals(oldCurrencyType) && "USD".equals(newCurrencyType)){
			currencyTransferType = CNY_TO_USD;
		}
		//本位币转换
		if(StringUtils.isNotEmpty(oldCurrencyType) && StringUtils.isNotEmpty(newCurrencyType) 
				&& oldCurrencyType.equals(newCurrencyType)){
			currencyTransferType = BASE_CURRENCY_TRANSFER;
		}
		return currencyTransferType;
	}
	
	
	/**
	 * 获取商品mov优先级最高的mov策略模板
	 * @param productVo
	 * @param productMovRuleTemplates
	 * @param currencyType
	 * @return
	 */
	private MovRuleTemplate getProductMovTemplate(ProductVo productVo,List<MovRuleTemplate> productMovRuleTemplates,String currencyType){
		List<MovRuleTemplate> movRuleTemplates = new ArrayList<>();
		Map<String, MovRuleTemplate> templateMap  = new HashMap<>();
		productMovRuleTemplates.stream().forEach(temp -> templateMap.put(temp.getCacheKey(), temp));
		if(!templateMap.isEmpty() ){
		    	List<String> keys = this.getKeys(productVo);
		    	MovRuleTemplate movRuleTemplate = null;
		    	for (String key : keys) {
		    		movRuleTemplate = templateMap.get(key);
		    		if(movRuleTemplate!=null){
		    			movRuleTemplate.setCacheKey(key);
		    			movRuleTemplates.add(movRuleTemplate);
		    		}
				}
	    }
		//获取优先级高的商品的mov
		if(CollectionUtils.isEmpty(movRuleTemplates)){
			return null;
		}
		return movRuleTemplates.get(movRuleTemplates.size() - 1);
	}
	

	private void setMovStatus(ProductVo productVo, Map<String, SupplierVo> vendorMap, MovInfo returnMovInfo) {
		if(vendorMap!= null ){
			SupplierVo tempVo = vendorMap.get(productVo.getVendorId());
			if(tempVo == null)return;
			String isValidVendorMOv = tempVo.getVendorMovStatus() != null ? 
					tempVo.getVendorMovStatus() : "";
			String isValidSkuMov = tempVo.getSkuMovStatus() != null ? 
					tempVo.getSkuMovStatus() : "";
			returnMovInfo.setVendorMovStatus(isValidVendorMOv);
			returnMovInfo.setSkuMovStatus(isValidSkuMov);
		}
	}
	
	public boolean isNum(String str){
		//可以判断正负、整数小数  
		return str.matches("-?[0-9]+.*[0-9]*");  
	}
	
	/**
	 * 从缓存中查询MOV策略模板
	 * @param product
	 * @return
	 * @since 2017年8月14日
	 * @author zr.wanghong
	 */
	@SuppressWarnings("unchecked")
	public List<MovRuleTemplate> findMovRuleTemplate(Product product){
		Cache cache = cacheManager.getCache("movRuleTemplateCache");
	    Map<String, MovRuleTemplate> movRuleTemplateCacheMap = null;
	    List<MovRuleTemplate> movRuleTemplates = new ArrayList<>();
	    
		ValueWrapper valueWrapper = cache.get(KEY_MOVRULE_TEMPLATE_CACHEMAP);
	    if(valueWrapper != null){
	    	movRuleTemplateCacheMap = (Map<String, MovRuleTemplate>) cache.get(KEY_MOVRULE_TEMPLATE_CACHEMAP).get();
	    }
	    if(movRuleTemplateCacheMap != null ){
	    	List<String> keys = this.getKeys(product);
	    	MovRuleTemplate movRuleTemplate = null;
	    	for (String key : keys) {
	    		movRuleTemplate = movRuleTemplateCacheMap.get(key);
	    		if(movRuleTemplate!=null){
	    			movRuleTemplate.setCacheKey(key);
	    			movRuleTemplates.add(movRuleTemplate);
	    		}
			}
	    	return movRuleTemplates;
	    }else{
	    	return new ArrayList<>();
	    }
	}
	
	
	private List<String> getKeys(Product product){
		String vendorId = product.getVendorId();
    	String sourceId = product.getSourceId();
    	String brandId = String.valueOf(product.getSpu().getManufacturerId());
    	List<String> params = new ArrayList<>();
    	params.add(vendorId);
    	params.add(sourceId);
    	params.add(brandId);
    	List<String> keys = new ArrayList<>();
    	
    	StringBuilder cacheKey = new StringBuilder();
    	cacheKey.append(vendorId).append("-none-").append("none");
    	keys.add(cacheKey.toString());
    	
    	cacheKey = new StringBuilder();
    	cacheKey.append(vendorId).append("-"+sourceId+"-").append("none");
    	keys.add(cacheKey.toString());
    	
    	cacheKey = new StringBuilder();
    	cacheKey.append(vendorId).append("-none-").append(brandId);
    	keys.add(cacheKey.toString());
    	
    	cacheKey = new StringBuilder();
    	cacheKey.append(vendorId).append("-"+sourceId+"-").append(brandId);
    	keys.add(cacheKey.toString());
    	
    	List<String> ruleTypes = new ArrayList<>();
    	ruleTypes.add("1");
    	ruleTypes.add("0");
    	
    	List<String> result = new ArrayList<>();
    	for (String ruleType : ruleTypes) {
			for (String key : keys) {
				result.add(ruleType + "-" +key);
			}
		}
    	return result;
	}
	
	/**
	 * 获取汇率和税率
	 * @param rates
	 * @return
	 * @since 2017年1月4日
	 * @author zr.wanghong
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getExchangeRateAndTaxRate(Map<String, Object> rates) {
		Map<String, Object> returnRates = null;
		try {
			if(rates == null){
				Cache exchangeRateCache = cacheManager.getCache("exchangeRateCache");
				returnRates = exchangeRateCache.get("exchangeRateCache-USD-CNY",Map.class);
				UomConversionClient client = shipmentClientBuilder.uomConversionResource();
				returnRates = returnRates == null ? client.getById(Currency.USD, Currency.CNY) : returnRates;
			}else{
				return rates;
			}
		} catch (Exception e) {
			logger.error("请求URL：{}",basedataServerUrlPrefix+"/v1/basedata/exchangerate/USD/CNY");
			logger.error("获取汇率和税率失败!错误信息{}",e.getMessage());
			throw new SystemException("获取汇率和税率失败!错误信息："+e.getMessage(), e);
		}
		
		return returnRates;
	}
	
	
	/**
	 * 根据商品上的人民币和美元价格判断计算币种类型
	 * @param priceCNY 人民价格
	 * @param priceUSD 美元价格
	 * @return 币种类型
	 */
	public String getCurrencyType(Product product){
		Map<String, ProductPrice> pricesMap = this.getCNYAndUSDPriceMap(product.getPrices());
		ProductPrice priceCNY = pricesMap.get(CNY);
		ProductPrice priceUSD = pricesMap.get(USD);
		//币种类型
		String currencyType = "";
		
		//是否有人民币梯度
		boolean hasCNY;
		
		//是否有美元梯度
		boolean hasUSD;
		
		if(priceCNY !=null && !CollectionUtils.isEmpty(priceCNY.getPriceLevels())){
			hasCNY = true;
		}else{
			hasCNY = false;
		}
		
		if(priceUSD !=null && !CollectionUtils.isEmpty(priceUSD.getPriceLevels())){
			hasUSD = true;
		}else{
			hasUSD = false;
		}
		
		//只有人民币
		if(hasCNY && !hasUSD){
			currencyType = CNY;
		}
		//有美元
		if(hasUSD){
			currencyType = USD;
		}
		
		if(StringUtils.isEmpty(currencyType))
			logger.debug("无法获取到币种类型,productId:{}",product.getId());
		return currencyType;
	}
	
	/**
	 * 获取人民币和美元价格Map
	 * @param prices
	 * @return
	 * @since 2017年7月3日
	 * @author zr.wanghong
	 */
	private Map<String, ProductPrice> getCNYAndUSDPriceMap(List<ProductPrice> prices){
		Map<String, ProductPrice> retMap = new HashMap<>();
		if(!CollectionUtils.isEmpty(prices)){
			for (int i = 0; i < prices.size(); i++) {
				ProductPrice productPrice = prices.get(i);
				if(productPrice != null && CNY.equals(productPrice.getCurrencyCode())){
					ProductPrice newProductPrice = new ProductPrice();
					BeanUtils.copyProperties(productPrice, newProductPrice);
					retMap.put(CNY, newProductPrice);
				}
				if(productPrice != null && USD.equals(productPrice.getCurrencyCode())){
					ProductPrice newProductPrice = new ProductPrice();
					BeanUtils.copyProperties(productPrice, newProductPrice);
					retMap.put(USD, newProductPrice);
				}
			}
		}
		return retMap;
	}
	
	/**
	 * 保留4位有效数字（价格 >= 100只处理为保留2位小数）
	 * @param amount 原始金额
	 * @return 返回保留4位有效数字后的金额
	 * @since 2017年7月4日
	 * @author zr.wanghong
	 */
	private BigDecimal keep2EffectiveNumber(BigDecimal amount) {
		BigDecimal returnAmount;
		returnAmount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
		return returnAmount;
	}
	
	/**
	 * 校验MOV
	 * @param movValidationParams MOV校验参数
	 * @return 校验结果
	 * @since 2017年8月15日
	 * @author zr.wanghong
	 * @throws BusinessException 
	 */
	public List<MovValidResult> validMov(List<MovValidationParam> movValidationParams) throws BusinessException{
		List<MovValidResult> result = new ArrayList<>();
		if(CollectionUtils.isEmpty(movValidationParams)){
			throw new BusinessException("mov校验参数为空!");
		}
		//查询商品信息
		List<String> ids = new ArrayList<>();
		movValidationParams.stream().forEach(param -> ids.add(param.getProductId()));
		Iterator<Product> productInfo = productRepository.findAll(ids).iterator();
		List<ProductVo> productVos = new ArrayList<>();
		while (productInfo.hasNext()) {
			ProductVo productVo = new ProductVo();
			BeanUtils.copyProperties(productInfo.next(), productVo);
			productVos.add(productVo);
		}
		if(!CollectionUtils.isEmpty(productVos)){
			List<MovInfo> movInfos = this.queryByEntities(productVos);
			//查询所有供应商
			Set<String> supplierIds = productVos.stream().map(ProductVo::getVendorId).collect(Collectors.toSet());
			Map<String,SupplierVo> suppliers = partyClientBuilder.supplierClient().getSupplierSimple(supplierIds);
			
			//校验币种类型
			String currency = movValidationParams.get(0).getCurrency();
			
			//按供应商分组并校验MOV
			result = this.validMovGroupbyVendor(movValidationParams, currency, movInfos,suppliers);
		}
		return result;
	}

	/**
	 * 校验供应商MOV
	 * @param movValidationParams
	 * @param movValidationResult
	 * @param currency
	 * @param movInfos
	 * @since 2017年8月15日
	 * @author zr.wanghong
	 */
	@SuppressWarnings("unchecked")
	private List<MovValidResult> validMovGroupbyVendor(List<MovValidationParam> movValidationParams,String currency, 
												List<MovInfo> movInfos,Map<String, SupplierVo> vendorMap) {
		//校验结果集合
		List<MovValidResult> validResults = new ArrayList<>();
		
		//根据供应商进行分组MOV信息
		Map<String, List<MovInfo>> movInfoMapsByVendor = movInfos.stream().collect(Collectors.groupingBy(MovInfo::getVendorId));
		
		//根据供应商进行分组校验参数
		Map<String, List<MovValidationParam>> movValidParamMap = movValidationParams.stream().collect(Collectors.groupingBy(MovValidationParam::getVendorId));
		
		//校验供应商分组后的商品订单金额是否符合供应商mov
		for (String vendorId : movValidParamMap.keySet()) {
			MovValidResult movValidResult = new MovValidResult();
			movValidResult.setVendorId(vendorId);
			
			List<Object> vendorMovValidResult = new ArrayList<>();
			List<Map<String, Object>> warehouseMovValidResult = new ArrayList<>();
			List<Map<String, Object>> brandMovValidResult = new ArrayList<>();
			
			//供应商MOV校验开关是否开启,不为Y则不校验
			SupplierVo vendor = vendorMap.get(vendorId);
			if(null == vendor || !"Y".equals(vendor.getVendorMovStatus())){
				continue;
			}
			//指定供应商的校验参数集合
			List<MovValidationParam> vendorMovValidList = movValidParamMap.get(vendorId);
			
			//校验参数是否所有商品都是限购商品 true是，false不是
			boolean isAllLimitedPurchase = true;
			for (MovValidationParam mvp : vendorMovValidList) {
				if(!"Y".equals(mvp.getIsLimitedPurchase())){
					isAllLimitedPurchase = false;
				}
			}
			if(isAllLimitedPurchase){
				//所有商品都为限购，不做任何mov校验
				continue;
			}
			
			//校验商品的MOV
			List<Map<String, Object>> productMovValidResult = validProductMov(vendorMovValidList, currency, movInfos, vendorMap);
			
			//校验供应商mov
			Map<String, Object> validResult = validAmount(currency, vendorMovValidList, movInfoMapsByVendor.get(vendorId).get(0),"vendor",vendorId);
			if(validResult.get(vendorId) !=null && validResult.get(vendorId) instanceof List){
				movValidResult.setVendorMov(String.valueOf(validResult.get("mov")));
				vendorMovValidResult = (List<Object>) validResult.get(vendorId);
			}
			
			//二次分组按仓库分组校验参数
			Map<String, List<MovValidationParam>> wareHouseValidParamMap = vendorMovValidList.stream().collect(Collectors.groupingBy(MovValidationParam::getWareHouse));
			List<MovInfo> vendorMovs = movInfoMapsByVendor.get(vendorId);
			//按仓库分组mov信息
			Map<String, List<MovInfo>> movInfoMapsByWarehouse = vendorMovs.stream().collect(Collectors.groupingBy(MovInfo::getWarehouse));
			
			
			for (String wareHouse : wareHouseValidParamMap.keySet()) {
				List<MovValidationParam> wareHouseValidParamList =  wareHouseValidParamMap.get(wareHouse);
				if(StringUtils.isEmpty(wareHouse)){
					continue;
				}
				//校验仓库mov
				validResult = validAmount(currency, wareHouseValidParamList, movInfoMapsByWarehouse.get(wareHouse).get(0),"warehouse",wareHouse);
				if(!validResult.isEmpty())
					warehouseMovValidResult.add(validResult); 
				
				//最后按品牌分组
				Map<String, List<MovValidationParam>> brandValidParamMap = wareHouseValidParamList.stream().collect(Collectors.groupingBy(MovValidationParam::getBrand));
				List<MovInfo> wareHouseMovs = movInfoMapsByWarehouse.get(wareHouse);
				Map<String, List<MovInfo>> movInfoMapsByBrand = wareHouseMovs.stream().collect(Collectors.groupingBy(MovInfo::getBrand));
				
				for (String  brand : brandValidParamMap.keySet()) {
					List<MovValidationParam> brandValidParamList = brandValidParamMap.get(brand);
					//校验品牌mov
					if(StringUtils.isNotEmpty(brand)){
						validResult = validAmount(currency, brandValidParamList, movInfoMapsByBrand.get(brand).get(0), "brand",brand);
						if(!validResult.isEmpty())
							brandMovValidResult.add(validResult);
					}
				}
				
			}
			
			movValidResult.setValidProductIds(productMovValidResult);
			movValidResult.setValidVendorIds(vendorMovValidResult);
			movValidResult.setValidWarehouseIds(warehouseMovValidResult);
			movValidResult.setValidBrandIds(brandMovValidResult);
			validResults.add(movValidResult);
		}
		return validResults;
	}

	private BigDecimal getTotalAmount(List<MovValidationParam> vendorMovList) {
		BigDecimal totalAmount = new BigDecimal("0");
		for (MovValidationParam param : vendorMovList) {
			Double amount = param.getAmount();
			totalAmount = totalAmount.add(new BigDecimal(amount));
		}
		totalAmount = totalAmount.setScale(5, BigDecimal.ROUND_DOWN);
		return totalAmount;
	}

	private Map<String, Object> validAmount(String currency, List<MovValidationParam> vendorMovValidList, MovInfo movInfo,String validType,String key) {
		BigDecimal totalAmount = this.getTotalAmount(vendorMovValidList);
		BigDecimal movAmount = new BigDecimal("0");
		if("vendor".equals(validType)){
			if(CNY.equals(currency) && StringUtils.isNotEmpty(movInfo.getVendorMovCNY()) &&  isNum(movInfo.getVendorMovCNY())){
				movAmount = new BigDecimal(movInfo.getVendorMovCNY());
			}
			if(USD.equals(currency) && StringUtils.isNotEmpty(movInfo.getVendorMovUSD()) && isNum(movInfo.getVendorMovUSD())){
				movAmount = new BigDecimal(movInfo.getVendorMovUSD());
			}
		}
		if("warehouse".equals(validType)){
			if(CNY.equals(currency) && StringUtils.isNotEmpty(movInfo.getWarehouseMovCNY()) &&  isNum(movInfo.getWarehouseMovCNY())){
				movAmount = new BigDecimal(movInfo.getWarehouseMovCNY());
			}
			if(USD.equals(currency) && StringUtils.isNotEmpty(movInfo.getWarehouseMovUSD()) && isNum(movInfo.getWarehouseMovUSD())){
				movAmount = new BigDecimal(movInfo.getWarehouseMovUSD());
			}
		}
		
		if("brand".equals(validType)){
			if(CNY.equals(currency) && StringUtils.isNotEmpty(movInfo.getBrandMovCNY()) &&  isNum(movInfo.getBrandMovCNY())){
				movAmount = new BigDecimal(movInfo.getBrandMovCNY());
			}
			if(USD.equals(currency) && StringUtils.isNotEmpty(movInfo.getBrandMovUSD()) && isNum(movInfo.getBrandMovUSD())){
				movAmount = new BigDecimal(movInfo.getBrandMovUSD());
			}
		}
		//总金额小于mov
		
		Map<String, Object> result = new HashMap<>();
		if(movAmount.doubleValue() > 0 && totalAmount.compareTo(movAmount) == BIGDECIMAL_LESS_THAN){
				result.put(key, vendorMovValidList.stream().map(MovValidationParam::getProductId).collect(Collectors.toList()));
				result.put("mov", movAmount.doubleValue());
				result.put("type", validType);
		}
		return result;
	}

	/**
	 * 校验商品MOV
	 * @param movValidationParams
	 * @param movValidationResult
	 * @param currency
	 * @param movInfos
	 * @since 2017年8月15日
	 * @author zr.wanghong
	 */
	private List<Map<String, Object>> validProductMov(List<MovValidationParam> movValidationParams,String currency, 
				List<MovInfo> movInfos,Map<String, SupplierVo> vendorMap) {
		List<Map<String, Object>> movValidationResults = new ArrayList<>();
		//根据商品id分组mov信息
		Map<String, List<MovInfo>> movInfoMaps = movInfos.stream().collect(Collectors.groupingBy(MovInfo::getProductId));
		for (MovValidationParam param : movValidationParams) {
			//限购的商品不做商品的mov校验
			if(StringUtils.isNotEmpty(param.getIsLimitedPurchase()) && "Y".equals(param.getIsLimitedPurchase())){
				continue;
			}
			
			//商品MOV校验开关是否开启,不为Y则不校验
			SupplierVo vendor = vendorMap.get(param.getVendorId());
			if(null == vendor || !"Y".equals(vendor.getSkuMovStatus())){
				continue;
			}
			MovInfo movInfo  = null;
			if(!CollectionUtils.isEmpty(movInfoMaps.get(param.getProductId()))){
				movInfo = movInfoMaps.get(param.getProductId()).get(0);
			}
			//商品的小计
			BigDecimal amount = new BigDecimal(param.getAmount());
			amount = amount.setScale(5, BigDecimal.ROUND_DOWN);
			if(null != movInfo){
				BigDecimal movProductAmount = new BigDecimal("0");
				if(CNY.equals(currency)){
					if(StringUtils.isNotEmpty(movInfo.getProductMovCNY()) && isNum(movInfo.getProductMovCNY())){
						movProductAmount = new BigDecimal(movInfo.getProductMovCNY());
					}
				}else{
					if(StringUtils.isNotEmpty(movInfo.getProductMovUSD()) && isNum(movInfo.getProductMovUSD())){
						movProductAmount = new BigDecimal(movInfo.getProductMovUSD());
					}
				}
				
				//校验商品的小计是否符合商品的mov
				if( movProductAmount.doubleValue() >0 && amount.compareTo(movProductAmount) == BIGDECIMAL_LESS_THAN ){
					Map<String, Object> result = new HashMap<>();
					result.put("productId", param.getProductId());
					result.put("mov", movProductAmount.doubleValue());
					result.put("manufacturerPartNumber", param.getManufacturerPartNumber());
					movValidationResults.add(result);
				}
			}
		}
		return movValidationResults;
	}
}