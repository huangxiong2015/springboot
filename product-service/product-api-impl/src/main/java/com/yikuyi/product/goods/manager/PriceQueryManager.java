package com.yikuyi.product.goods.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.function.Function;
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
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yikuyi.activity.model.Activity;
import com.yikuyi.activity.model.ActivityProduct;
import com.yikuyi.activity.vo.ActivityProductVo;
import com.yikuyi.basedata.ShipmentClientBuilder;
import com.yikuyi.basedata.common.model.Currency;
import com.yikuyi.basedata.resource.UomConversionClient;
import com.yikuyi.category.model.ProductCategory;
import com.yikuyi.product.activity.bll.ActivityManager;
import com.yikuyi.product.model.Product;
import com.yikuyi.product.model.ProductPrice;
import com.yikuyi.product.model.ProductPriceLevel;
import com.yikuyi.product.vo.ProductVo;
import com.yikuyi.rule.price.PriceInfo;
import com.yikuyi.rule.price.ProductPriceAction;
import com.yikuyi.rule.price.ProductPriceAction.ProductPriceActionTypeId;
import com.yikuyi.rule.price.ProductPriceAction.ProductPricePurposeId;
import com.yikuyi.rule.price.ProductPriceRule;
import com.yikuyi.rule.price.model.PriceRuleTemplateCache;
import com.yikuyi.rule.price.model.ProductPriceCache;
import com.ykyframework.exception.SystemException;

@Service
@Transactional
public class PriceQueryManager {

	private static final Logger logger = LoggerFactory.getLogger(PriceQueryManager.class);
	@Autowired
	private MongoRepository<Product, String> productRepository;
	
	@Autowired
	private CacheManager cacheManager;
	
	@Autowired
	private ShipmentClientBuilder shipmentClientBuilder;
	
	@Autowired
	@Lazy
	private PriceQueryAsyncManager priceQueryAsyncManager;
	
	@Autowired
	private ProductManager productManager;
	
	@Value("${api.basedata.serverUrlPrefix}")
	private String basedataServerUrlPrefix;
	
	private static final String CNY = "CNY";
	private static final String USD = "USD";
	/** 价格缓存名称前缀**/
	private static final String PRODUCT_PRICE_CACHE_PREFIX = "ProductPriceCache-";
	/** 按固定值**/
	private static final String ACCORDING_THE_FIXED_VALUE = "PRICE_POL";
	
	/**大于*/
	public static final int BIGDECIMAL_GREAT_THAN = 1;
	/**等于*/
	public static final int BIGDECIMAL_EQUAL = 0;
	/**小于*/
	public static final int BIGDECIMAL_LESS_THAN = -1;
	
	private static final String NONE = "none";
	private static final String KEY_EXCHANGERATE = "exchangeRate";
	private static final String KEY_TAXRATE = "taxRate";
	
	/**
	 * 根据商品ID集合查询商品价格（有活动时查询活动价，没活动查询市场销售价）
	 * @param ids 商品ID集合
	 * @return 价格信息
	 */
	public List<PriceInfo> queryPriceWithActivityInfo(List<String> ids){
		Iterator<Product> productInfo = productRepository.findAll(ids).iterator();
		List<ProductVo> productVos = new ArrayList<>();
		while (productInfo.hasNext()) {
			Product product = productInfo.next();
			ProductVo productVo = new ProductVo();
			BeanUtils.copyProperties(product, productVo);
			productVos.add(productVo);
		}
		//合并活动信息
		productManager.mergeActivity(productVos, "N");
		//1.查询活动价和市场价
		return this.queryPriceWithActivity(productVos);
	}
	
	/**
	 * 供应商管理后台-销售中的商品-实时刷新价格无缓存
	 * @param productVos
	 * @return 价格信息
	 * @since 2017年4月26日
	 * @author zr.wanghong
	 */
	public List<PriceInfo> queryPriceNoCache(List<ProductVo> productVos){
		return queryPriceByEntities(productVos,true);
	}
	
	/**
	 * 根据ID集合批量实时查询商品梯度价格
	 * @param ids 商品ID集合
	 * @return 商品价格信息集合
	 * @since 2016年12月6日
	 * @author zr.wanghong
	 */
	public List<PriceInfo> queryPrice(List<String> ids){
		long startTime = System.currentTimeMillis();
		//logger.debug("[method:PriceQueryManager.queryPrice] --- start to find data from mongodb .");
		Iterator<Product> productInfo = productRepository.findAll(ids).iterator();
		List<ProductVo> productVos = new ArrayList<>();
		while (productInfo.hasNext()) {
			Product product = productInfo.next();
			ProductVo productVo = new ProductVo();
			BeanUtils.copyProperties(product, productVo);
			productVos.add(productVo);
		}
		//logger.debug("[method:PriceQueryManager.queryPrice] --- find data from mongodb time: "+ (System.currentTimeMillis()- startTime));
		//1.查询活动价和市场价
		List<PriceInfo> result = this.queryPriceWithActivity(productVos);
		//logger.debug("[method:PriceQueryManager.queryPrice] 查询价格总耗时:{}", System.currentTimeMillis()- startTime);
		return result;
	}
	
	/**
	 * 根据ProductVo实体集合批量实时查询价格<br/>
	 * 1. 获取汇率和税率<br/>
	 * 2. 迭代传入的商品集合<br/>
	 * 3. 获取当前商品对象缓存中价格数据<br/>
	 * 4. 根据当前商品对象从缓存中查找价格模板<br/>
	 * 5. 有模板做根据价格策略模板计算价格信息<br/>
	 * 6. 没有模板则计算一个默认规则（按固定值加价0）计算<br/>
	 * 
	 * @param productVos 商品VO集合
	 * @param isNoCache 是否从缓存中获取，false 取缓存，true不取缓存
	 * @return 商品价格信息集合
	 * @since 2017年6月14日
	 * @author zr.wanghong
	 */
	public List<PriceInfo> queryPriceByEntities(List<ProductVo> productVos,boolean isNoCache){
		List<PriceInfo> priceInfosResult = new ArrayList<>();
		//1.汇率和税率
		final Map<String,Object> rates = this.getExchangeRateAndTaxRate(null);
		
		Cache productPriceCache = cacheManager.getCache("productPriceCache");
		
		productVos.stream().forEach( product -> {
			//过滤掉非标准的商品数据,不参与计算
			if(filterNonstandProduct(priceInfosResult, product))return;	
			//获取缓存中价格数据
			ProductPriceCache priceCache = productPriceCache.get(PRODUCT_PRICE_CACHE_PREFIX+product.getId(),ProductPriceCache.class);
			//从缓存中查找价格模板
			PriceRuleTemplateCache priceTemplate = this.findTemplate(product);
			if(priceTemplate == null){
				this.calPriceWithoutTemplate(priceInfosResult, product, priceCache, productPriceCache,rates, isNoCache);
				return;
			}
			boolean isNeed = this.isNeedCalPriceWithTemplate(isNoCache, priceInfosResult, product, rates, productPriceCache,priceCache, priceTemplate);
			if(!isNeed) return;
			PriceInfo priceInfo =  this.calPriceWithTemplate(priceTemplate, product);
			if(!isNoCache)this.savePriceToCache(product, productPriceCache, priceTemplate, priceInfo);
			//计算汇率和税率
			PriceInfo priceInfoResult = this.calPriceInfoWithRates(priceInfo,rates);
			priceInfosResult.add(priceInfoResult);
	    });
		return priceInfosResult;
	}

	/**
	 * 是否需要根据价格策略模板计算价格
	 * @param isNoCache
	 * @param priceInfosResult
	 * @param product
	 * @param rates
	 * @param productPriceCache
	 * @param priceCache
	 * @param priceTemplate
	 * @return
	 * @since 2017年6月28日
	 * @author zr.wanghong
	 */
	private boolean isNeedCalPriceWithTemplate(boolean isNoCache, List<PriceInfo> priceInfosResult, ProductVo product,
			Map<String,Object> rates, Cache productPriceCache, ProductPriceCache priceCache,
			PriceRuleTemplateCache priceTemplate) {
		boolean isVersionNotNull = priceCache !=null && StringUtils.isNotEmpty(priceCache.getVersion());
		boolean isVesionEqual = false;
		if(isVersionNotNull){
			isVesionEqual =  priceCache.getVersion().equals(priceTemplate.getVersion());
		}
		//模板的版本号和价格缓存中的版本号不一致,则需要清除旧的价格缓存
		if(!isVesionEqual){
			productPriceCache.evict(PRODUCT_PRICE_CACHE_PREFIX+product.getId());
		}
		
		//模板的版本号和价格缓存中的版本号一致并且isNoCache=false表示取缓存，不用计算价格
		if(isVesionEqual && !isNoCache){
			//取缓存中的价格数据（不含税）
			PriceInfo priceInfo = priceCache.getPriceInfo();
			//算税率和汇率
			priceInfo = this.calPriceInfoWithRates(priceInfo, rates);
			priceInfosResult.add(priceInfo);
			return false;
		}
		return true;
	}

	/**
	 * 保存价格缓存
	 * @param product
	 * @param productPriceCache
	 * @param priceTemplate
	 * @param priceInfo
	 * @since 2017年6月28日
	 * @author zr.wanghong
	 */
	private void savePriceToCache(ProductVo product, Cache productPriceCache, PriceRuleTemplateCache priceTemplate,
			PriceInfo priceInfo) {
		ProductPriceCache priceCache = new ProductPriceCache();
		priceCache.setVersion(priceTemplate.getVersion());
		priceCache.setProductId(product.getId());
		priceCache.setTemplateKey(priceTemplate.getCacheKey());
		priceCache.setPriceInfo(priceInfo);
		productPriceCache.put(PRODUCT_PRICE_CACHE_PREFIX+product.getId(), priceCache);
	}

	/**
	 * 过滤非标准的商品，不参与计算价格
	 * @param priceInfosResult
	 * @param product
	 * @return
	 * @since 2017年6月28日
	 * @author zr.wanghong
	 */
	private boolean filterNonstandProduct(List<PriceInfo> priceInfosResult, ProductVo product) {
		//如果供应商id为空则跳过
		if(product.getVendorId()==null)
			return true;
		//是否需要计算价格
		boolean isNeedCalculate = this.isNeedCalPrice(product);
		if(!isNeedCalculate){
			//没有原价，原样返回
			PriceInfo priceInfo = new PriceInfo();
			priceInfo.setPrices(product.getPrices());
			priceInfo.setCostPrices(product.getPrices());
			priceInfo.setResalePrices(product.getPrices());
			priceInfo.setSpecialResaleprices(product.getPrices());
			priceInfo.setProductId(product.getId());
			priceInfosResult.add(priceInfo);
			return true;
		}
		return false;
	}
	
	
	/**
	 * 计算价格不使用价格策略模板
	 * @param priceInfosResult
	 * @param product
	 * @param priceCache
	 * @param productPriceCache
	 * @param exchangeRate
	 * @param taxRate
	 * @param isNoCache
	 * @since 2017年6月28日
	 * @author zr.wanghong
	 */
	private void calPriceWithoutTemplate(List<PriceInfo> priceInfosResult,Product product,
			ProductPriceCache priceCache, Cache productPriceCache,Map<String,Object> rates,boolean isNoCache){
		//停用价格模板时，价格缓存同模板缓存存有关联字段，需要被清除
		if( priceCache != null && priceCache.getTemplateKey() != null ){
			productPriceCache.evict(PRODUCT_PRICE_CACHE_PREFIX+priceCache.getProductId());
		}
		
		//第一次计算后价格放入缓存
		if(priceCache == null || priceCache.getTemplateKey() != null || isNoCache){
			PriceInfo priceInfo = new PriceInfo();
			List<ProductPrice> prices = product.getPrices();
			//原价
			List<ProductPrice>  orginalPrices = prices;
			//没有价格模板，计算一个默认的价格（不含汇率和税率）
			List<ProductPrice> defaultPrices = this.calculateDefault(prices);
			
			priceInfo.setPrices(orginalPrices);
			priceInfo.setCostPrices(defaultPrices);
			priceInfo.setResalePrices(defaultPrices);
			priceInfo.setSpecialResaleprices(defaultPrices);
			priceInfo.setProductId(product.getId());
			
			//缓存单个商品的价格信息
			ProductPriceCache newPriceCache = new ProductPriceCache();
			newPriceCache.setPriceInfo(priceInfo);
			newPriceCache.setProductId(product.getId());

			//isNoCache为true表示不取缓存，因此也不需要更新价格缓存
			if(!isNoCache){
				productPriceCache.put(PRODUCT_PRICE_CACHE_PREFIX+product.getId(), newPriceCache);
			}
			
			//算税率和汇率
			priceInfo = this.calPriceInfoWithRates(priceInfo, rates);
			priceInfosResult.add(priceInfo);
		}
		else{
			//取缓存中的价格数据（不含税）
			PriceInfo priceInfo = priceCache.getPriceInfo();
			//算税率和汇率
			priceInfo = this.calPriceInfoWithRates(priceInfo, rates);
			priceInfosResult.add(priceInfo);
		}
	}
	
	/**
	 * 计算价格使用价格策略模板
	 * @param priceTemplate
	 * @param product
	 * @return
	 * @since 2017年6月28日
	 * @author zr.wanghong
	 */
	private PriceInfo calPriceWithTemplate(PriceRuleTemplateCache priceTemplate,Product product){
		List<ProductPriceAction> realCostActions = new ArrayList<>();
		List<ProductPriceAction> resalePriceActions = new ArrayList<>();
		List<ProductPriceAction> specialResalePriceActions = new ArrayList<>();
		//获取成本价、销售价、特价对应的规则
		this.getProductPriceAction(priceTemplate, realCostActions, resalePriceActions, specialResalePriceActions);
		List<ProductPrice> prices = product.getPrices();
		//原价
		List<ProductPrice>  orginalPrices = new ArrayList<>();
		//成本价
		List<ProductPrice> realCostPrices;
		//销售价
		List<ProductPrice> resalePrices;
		//特价
		List<ProductPrice> specialResaleprices;
		//原价
		for (ProductPrice productPrice : prices) {
			orginalPrices.add(productPrice);
		}
		
		//计算成本价、销售价、特价的人民币和美元的价格梯度(不算税)
		//成本价、销售价、特价没有匹配规则，计算默认价格
		if(CollectionUtils.isEmpty(realCostActions)){
			realCostPrices = this.calculateDefault(prices);
		}else{
			realCostPrices = this.calProductPricesWithAction( prices, realCostActions);
		}
		if(CollectionUtils.isEmpty(resalePriceActions)){
			resalePrices = this.calculateDefault(prices);
		}else{
			resalePrices = this.calProductPricesWithAction(prices, resalePriceActions);
		}
		
		if(CollectionUtils.isEmpty(specialResalePriceActions)){
			specialResaleprices = this.calculateDefault(prices);
		}else{
			specialResaleprices = this.calProductPricesWithAction( prices, specialResalePriceActions);
		}
		PriceInfo priceInfo = new PriceInfo();
		priceInfo.setProductId(product.getId());
		priceInfo.setPrices(orginalPrices);
		priceInfo.setCostPrices(realCostPrices);
		priceInfo.setResalePrices(resalePrices);
		priceInfo.setSpecialResaleprices(specialResaleprices);
		return priceInfo;
	}
	
	/**
	 * 获取价格策略缓存的策略集合
	 * @param priceTemplate
	 * @param realCostActions
	 * @param resalePriceActions
	 * @param specialResalePriceActions
	 * @since 2017年6月29日
	 * @author zr.wanghong
	 */
	private void getProductPriceAction(PriceRuleTemplateCache priceTemplate , List<ProductPriceAction> realCostActions,
			List<ProductPriceAction> resalePriceActions, List<ProductPriceAction> specialResalePriceActions) {
		
		ProductPriceRule priceRule = priceTemplate.getProductPriceRule();
		//具体应用的规则操作
		List<ProductPriceAction> actions = priceRule.getActions();
		//筛选成本价，销售价，特价对应的规则
		for (ProductPriceAction productPriceAction : actions) {
			//规则目标（原价，成本价，销售价，特价）
			String pricePurposeId = productPriceAction.getProductPricePurposeId();
			
			//成本价
			if(ProductPricePurposeId.REAL_COST.toString().equals(pricePurposeId)){
				realCostActions.add(productPriceAction);
			}
			//销售价
			if(ProductPricePurposeId.RESALE_PRICE.toString().equals(pricePurposeId)){
				resalePriceActions.add(productPriceAction);
			}
			//特价
			if(ProductPricePurposeId.SPECIAL_RESALE_PRICE.toString().equals(pricePurposeId)){
				specialResalePriceActions.add(productPriceAction);
			}
		}
	}
	
	/**
	 * 查询价格和模板信息
	 * @param ids 商品ID集合
	 * @return 价格和模板信息
	 * @since 2017年5月2日
	 * @author zr.wanghong
	 */
	public JSONArray queryPriceTemplate(List<String> ids){
		//查询模板信息
		JSONArray resultJsonArray = new JSONArray();
		ids.stream().forEach( id -> {
			Product product = productRepository.findOne(id);
			if(product!=null){
				PriceRuleTemplateCache priceRuleTpl = this.findTemplate(product);
				JSONObject json  = new JSONObject();
				json.put("id", id);
				json.put("priceTemplate", priceRuleTpl);
				resultJsonArray.add(json);
			}
		});
		return resultJsonArray;
	}
	
	/**
	 * 查询价格和模板信息
	 * @param product 商品对象
	 * @return 价格和模板信息
	 * @since 2017年5月2日
	 * @author zr.wanghong
	 */
	public JSONArray queryPriceTemplate(Product product){
		//查询模板信息
		JSONArray resultJsonArray = new JSONArray();
		if(product!=null){
			PriceRuleTemplateCache priceRuleTpl = this.findTemplate(product);
			JSONObject json  = new JSONObject();
			json.put("id", product.getId());
			json.put("priceTemplate", priceRuleTpl);
			resultJsonArray.add(json);
		}
		return resultJsonArray;
	}

	/**
	 * 获取汇率和税率
	 * @param rates
	 * @return
	 * @since 2017年1月4日
	 * @author zr.wanghong
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getExchangeRateAndTaxRate(Map<String, Object> rates) {
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
	 * 是否需要计算梯度
	 * @param product 商品对象
	 * @return true 需要计算，false不需要计算
	 */
	private boolean isNeedCalPrice(Product product) {
		//非标准物料不需要计算价格
		String spuId = product.getSpu().getId();
		if(StringUtils.isEmpty(spuId)){
			return false;
		}
		//是否需要计算梯度
		List<ProductPrice> productPrices =  product.getPrices();
		if(CollectionUtils.isEmpty(productPrices)){
			return false;
		}
		
		Map<String, ProductPrice> pricesMap = this.getCNYAndUSDPriceMap(product.getPrices());
		ProductPrice priceCNY = pricesMap.get(CNY);
		ProductPrice priceUSD = pricesMap.get(USD);
		String currencyType = getCurrencyType(priceCNY, priceUSD);
	
		ProductPrice productPrice =	this.getCalPriceBase(currencyType, priceCNY, priceUSD);
		if(productPrice == null){
			return false;
		}
		List<ProductPriceLevel> levels = productPrice.getPriceLevels();
		if(CollectionUtils.isEmpty(levels)){
			return false;
		}
		for (int index = 0; index < levels.size(); index++) {
			ProductPriceLevel productPriceLevel = levels.get(index);
			if(productPriceLevel == null || StringUtils.isEmpty(productPriceLevel.getPrice())){
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * 使用汇率和税率计算价格信息 
	 * @param priceInfo 价格信息
	 * @param rates 汇率和税率
	 * @return 价格信息
	 * @since 2017年7月3日
	 * @author zr.wanghong
	 */
	public PriceInfo calPriceInfoWithRates(PriceInfo priceInfo,Map<String, Object> rates){
		
		PriceInfo returnPriceInfo = new PriceInfo();
		BeanUtils.copyProperties(priceInfo, returnPriceInfo);
		//原价
		List<ProductPrice>  orginalPrices = returnPriceInfo.getPrices();
		//成本价
		List<ProductPrice> realCostPrices = returnPriceInfo.getCostPrices();
		//销售价
		List<ProductPrice> resalePrices =  returnPriceInfo.getResalePrices();
		//特价
		List<ProductPrice> specialResaleprices =  returnPriceInfo.getSpecialResaleprices();
		
		BigDecimal exchangeRate = new BigDecimal(rates.get(KEY_EXCHANGERATE).toString());
		BigDecimal taxRate = new BigDecimal( rates.get(KEY_TAXRATE).toString());
		//计算成本价（含汇率和税率后的价格）
		realCostPrices = this.calProductPricesWithRates(realCostPrices, exchangeRate, taxRate);
		//计算销售价（含汇率和税率后的价格）
		resalePrices = this.calProductPricesWithRates(resalePrices, exchangeRate, taxRate);
		//计算特价（含汇率和税率后的价格）
		specialResaleprices = this.calProductPricesWithRates(specialResaleprices, exchangeRate, taxRate);
		returnPriceInfo.setPrices(orginalPrices);
		returnPriceInfo.setCostPrices(realCostPrices);
		returnPriceInfo.setResalePrices(resalePrices);
		returnPriceInfo.setSpecialResaleprices(specialResaleprices);
		return returnPriceInfo;
	}
	
	
	/**
	 * 根据商品上的人民币和美元价格判断计算币种类型
	 * @param priceCNY 人民价格
	 * @param priceUSD 美元价格
	 * @return 币种类型
	 */
	private String getCurrencyType(ProductPrice priceCNY,ProductPrice priceUSD){
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
			logger.debug("无法获取到币种类型,priceCNY:{},priceUSD:{}",JSONObject.toJSONString(priceCNY)
					,JSONObject.toJSONString(priceUSD));
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
	 * 默认计算价格梯度方法
	 * @param prices
	 * @param exchangeRate
	 * @param taxRate
	 * @return
	 */
	private List<ProductPrice> calculateDefault( List<ProductPrice> prices ) {
		Map<String, ProductPrice> pricesMap = this.getCNYAndUSDPriceMap(prices);
		
		ProductPrice priceCNY = pricesMap.get(CNY);
		ProductPrice priceUSD = pricesMap.get(USD);
		//没有人民币和美元价格，不计算
		if(priceCNY == null && priceUSD ==null){
			return prices;
		}
		//获取计算币种类型
		String currencyType = this.getCurrencyType(priceCNY, priceUSD);
		
		//计算基数，规则中币种设置的是人民币，则匹配的商品只有人民币价格。规则中币种设置的是美元，则匹配的商品人民币美元价格都有
		//如果币种是人民币则价格计算的基数是去商品上的人民币价格，如果币种是美元则计算的基数是商品上美元的价格
		ProductPrice calPriceBase = null;
		if(CNY.equals(currencyType)){
			calPriceBase = new ProductPrice();
			BeanUtils.copyProperties(priceCNY, calPriceBase);
		}
		if(USD.equals(currencyType)){
			calPriceBase = new ProductPrice();
			BeanUtils.copyProperties(priceUSD, calPriceBase);
		}
		return calDefaultPricesByCurrencyType(currencyType, calPriceBase);
	}

	/**
	 * 根据币种计算默认价格
	 * @param currencyType
	 * @param calPriceBase
	 * @return
	 * @since 2017年6月29日
	 * @author zr.wanghong
	 */
	private List<ProductPrice> calDefaultPricesByCurrencyType(String currencyType, ProductPrice calPriceBase) {
		ProductPrice newPriceCNY = null;
		ProductPrice newPriceUSD = null;
		BigDecimal amount = new BigDecimal("0");
		List<ProductPriceLevel> priceLevelsParam = calPriceBase != null ? calPriceBase.getPriceLevels():null;
		
		//币种为人民币
		if(Currency.CNY.toString().equals(currencyType)){
			//国内交货地价格
			newPriceCNY = calDefaultProductPrice(amount, priceLevelsParam);
			newPriceCNY.setCurrencyCode(CNY);
			
		}
		//币种类型为美元,返回的价格会有两个
		if(Currency.USD.toString().equals(currencyType)){
			//国内交货地价格
			newPriceCNY = calDefaultProductPrice(amount, priceLevelsParam);
			newPriceCNY.setCurrencyCode(CNY);
			
			//香港交货地价格
			newPriceUSD =  new ProductPrice();
			BeanUtils.copyProperties(newPriceCNY, newPriceUSD);
			newPriceUSD.setCurrencyCode(USD);
			
		}
		return getReturnProductPrices(newPriceCNY, newPriceUSD);
	}


	/**
	 * 计算默认的币种价格
	 * @param amount
	 * @param priceLevelsParam
	 * @return
	 * @since 2017年6月29日
	 * @author zr.wanghong
	 */
	private ProductPrice calDefaultProductPrice(BigDecimal amount, List<ProductPriceLevel> priceLevelsParam) {
		List<ProductPriceLevel> priceLevels = this.calPriceLevelByType( priceLevelsParam, amount,ACCORDING_THE_FIXED_VALUE);
		ProductPrice productPrice = new ProductPrice();
		if(!CollectionUtils.isEmpty(priceLevels)){
			productPrice.setPriceLevels(priceLevels);
			//单价
			if(priceLevels.get(0)!= null)
			productPrice.setUnitPrice(priceLevels.get(0).getPrice());
		}
		return productPrice;
	}

	/**
	 * 根据价格基数梯度和计算类型（按固定值/按百分比）计算价格梯度
	 * @param priceLevels 价格基数梯度
	 * @param amount 计算金额
	 * @param calculateType 计算类型（按固定值/按百分比）
	 * @return 价格梯度
	 */
	private List<ProductPriceLevel> calPriceLevelByType(
			List<ProductPriceLevel> priceLevels, BigDecimal amount,String calculateType) {
		
		List<ProductPriceLevel> returnPriceLevels = new ArrayList<>();
		//计算价格梯度
		if(!CollectionUtils.isEmpty(priceLevels)){
			priceLevels.stream().forEach(productPriceLevel -> {
				BigDecimal calAmount = this.calculateMethod( amount, productPriceLevel.getPrice(), calculateType);
				
				//过滤价格为零的梯度
				if(BigDecimal.ZERO.compareTo(calAmount)== BIGDECIMAL_EQUAL){
					return;
				}
				
				ProductPriceLevel targetProductPriceLevel = new ProductPriceLevel();
				//复制梯度
				BeanUtils.copyProperties(productPriceLevel, targetProductPriceLevel);
				targetProductPriceLevel.setPrice(calAmount != null ?calAmount.toString():null);
				returnPriceLevels.add(targetProductPriceLevel);
			});
		}
		return returnPriceLevels;
	}
	
	/**
	 * 获取用于计算的币种价格
	 * @param currencyType
	 * @param priceCNY
	 * @param priceUSD
	 * @return
	 * @since 2017年6月29日
	 * @author zr.wanghong
	 */
	private ProductPrice getCalPriceBase(String currencyType,ProductPrice priceCNY, ProductPrice priceUSD){
		ProductPrice calPriceBase = null;
		
		if(CNY.equals(currencyType)){
			calPriceBase = new ProductPrice();
			BeanUtils.copyProperties(priceCNY, calPriceBase);
		}
		
		if(USD.equals(currencyType)){
			calPriceBase = new ProductPrice();
			BeanUtils.copyProperties(priceUSD, calPriceBase);
		}
		return calPriceBase;
	}
	
	
	/**
	 * 计算人民币和美元的价格梯度
	 * @param prices 用于计算的价格梯度信息
	 * @param exchangeRate 汇率
	 * @param taxRate 税率
	 * @param amount 计算金额
	 * @param calculateType 计算类型（按固定值/按百分比）
	 * @return 计算后的价格梯度信息
	 */
	private List<ProductPrice> calProductPricesWithAction( List<ProductPrice> prices ,List<ProductPriceAction> actions) {
		//没有价格梯度不计算
		if(prices == null ){
			return prices;
		}
		
		Map<String, ProductPrice> pricesMap = this.getCNYAndUSDPriceMap(prices);
		//商品上的人民币和美元价格
		ProductPrice priceCNY = pricesMap.get(CNY);
		ProductPrice priceUSD = pricesMap.get(USD);
		
		//没有人民币和美元价格，不计算
		if(priceCNY == null && priceUSD ==null){
			return prices;
		}
		
		//根据策略计算后的人民币和美元价格
		ProductPrice newPriceCNY = null;
		ProductPrice newPriceUSD = null;
		
		if(!CollectionUtils.isEmpty(actions)){
			//币种类型
			String currencyType = this.getCurrencyType(priceCNY, priceUSD);
			for (ProductPriceAction productPriceAction : actions) {
				Map<String, ProductPrice> newPricesMap = calUSDAndCNYPriceByAction(productPriceAction, currencyType, priceCNY, priceUSD);
				if(newPricesMap.get(CNY) != null){
					newPriceCNY =  newPricesMap.get(CNY);
				}
				if(newPricesMap.get(USD) != null){
					newPriceUSD = newPricesMap.get(USD);
				}
			}
		}
		
		return getReturnProductPrices(newPriceCNY, newPriceUSD);
	}

	
	/**
	 * 根据规则计算人民币和美元价格
	 * @param productPriceAction 价格策略
	 * @param currencyType 币种类型
	 * @param priceCNY 人民币价格
	 * @param priceUSD 美元价格
	 * @return 计算后的价格信息Map
	 * @since 2017年7月3日
	 * @author zr.wanghong
	 */
	private Map<String, ProductPrice> calUSDAndCNYPriceByAction(ProductPriceAction productPriceAction ,String currencyType,ProductPrice priceCNY,ProductPrice priceUSD){
		
		//计算类型（按固定值，按百分比）
		String calculateType = productPriceAction.getProductPriceActionTypeId();
	
		//计算值（金额或百分数）
		BigDecimal amount = productPriceAction.getAmount();
		amount = amount == null? new BigDecimal("0"):amount;
		
		//交货地是国内/香港
		String uomId = productPriceAction.getUomId();
		
		//计算后的价格梯度
		List<ProductPriceLevel> priceLevels = null;
	
		
		//计算基数，规则中币种设置的是人民币，则匹配的商品只有人民币价格。规则中币种设置的是美元，则匹配的商品人民币美元价格都有
		//如果币种是人民币则价格计算的基数是去商品上的人民币价格，如果币种是美元则计算的基数是商品上美元的价格
		ProductPrice calPriceBase = this.getCalPriceBase(currencyType, priceCNY, priceUSD);
		
		//计算价格梯度
		if(calPriceBase != null){
			//根据计算类型（按固定值/按百分比）计算价格梯度
			priceLevels = this.calPriceLevelByType(calPriceBase.getPriceLevels(), amount, calculateType);
		}

		//币种类型为人民币，返回一个价格；币种类型为美元，返回的价格会有两个
		//国内交货地价格(不算税)
		Map<String, ProductPrice> retMap = new HashMap<>();
		if(Currency.CNY.toString().equals(uomId) ){
			retMap.put(CNY, setProductPrice(priceLevels,CNY));
		}
		//香港交货地价格
		if(Currency.USD.toString().equals(uomId)){
			retMap.put(USD, setProductPrice(priceLevels,USD));
		}
		return retMap;
	}
	
	
	
	/**
	 * 设置价格信息
	 * @param priceLevels 梯度
	 * @param currency 币种类型
	 * @return 价格实体
	 * @since 2017年6月29日
	 * @author zr.wanghong
	 */
	private ProductPrice setProductPrice(List<ProductPriceLevel> priceLevels,String currency) {
		ProductPrice productPrice = new ProductPrice();
		productPrice.setPriceLevels(priceLevels);
		productPrice.setCurrencyCode(currency);
		//单价
		if(!CollectionUtils.isEmpty(priceLevels)  && priceLevels.get(0) != null){
			productPrice.setUnitPrice(priceLevels.get(0).getPrice());
		}
		return productPrice;
	}

	/**
	 * 计算汇率和税率后的价格梯度
	 * @param exchangeRate
	 * @param taxRate
	 * @param calAmount
	 * @param currency
	 * @return
	 */
	private List<ProductPrice> calProductPricesWithRates( List<ProductPrice> prices , BigDecimal exchangeRate, BigDecimal taxRate) {
		//没有价格梯度不计算
		if(prices == null ){
			return prices;
		}
		Map<String, ProductPrice> pricesMap = this.getCNYAndUSDPriceMap(prices);
		ProductPrice priceCNY = pricesMap.get(CNY);
		ProductPrice priceUSD = pricesMap.get(USD);
		//币种类型
		String currencyType = this.getCurrencyType(priceCNY, priceUSD);
		//交易币种类型为人民币表示内地交货，表示商品只有人民币价格，不计算汇率
		if(CNY.equals(currencyType)){
			if (priceCNY!=null) {
				priceCNY.getPriceLevels().stream().forEach(priceLevel -> {
					if(priceLevel!=null){
						priceLevel.setPrice(this.keep4EffectiveNumber(new BigDecimal(priceLevel.getPrice())).toString());
					}
				});
				if(!CollectionUtils.isEmpty(priceCNY.getPriceLevels())){
					priceCNY.setUnitPrice(priceCNY.getPriceLevels().get(0).getPrice());
				}
			}
			return getReturnProductPrices(priceCNY, priceUSD);
		}
		//人民币计算税率
		if(priceCNY != null && !CollectionUtils.isEmpty(priceCNY.getPriceLevels())){
				calProductPriceWithRates(priceCNY,exchangeRate, taxRate, priceCNY.getPriceLevels(),CNY);
		}
		//美元价格精确五位小数
		if(priceUSD != null && !CollectionUtils.isEmpty(priceUSD.getPriceLevels())){
				calProductPriceWithRates(priceUSD,exchangeRate, taxRate, priceUSD.getPriceLevels(),USD);
		}
		return getReturnProductPrices(priceCNY, priceUSD);
	}

	/**
	 * 获取返回价格
	 * @param priceCNY
	 * @param priceUSD
	 * @return
	 * @since 2017年6月30日
	 * @author zr.wanghong
	 */
	private List<ProductPrice> getReturnProductPrices(ProductPrice priceCNY, ProductPrice priceUSD) {
		List<ProductPrice> returnPrices = new ArrayList<>();
		if(priceCNY != null){
			returnPrices.add(priceCNY);
		}
		if(priceUSD != null){
			returnPrices.add(priceUSD);
		}
		return returnPrices;
	}

	/**
	 * 计算税率和汇率后的价格
	 * @param productPrice 价格
	 * @param exchangeRate 汇率
	 * @param taxRate 税率
	 * @param priceLevels 价格梯度
	 * @param currency 币种
	 * @since 2017年7月3日
	 * @author zr.wanghong
	 */
	private void calProductPriceWithRates(ProductPrice productPrice,BigDecimal exchangeRate, BigDecimal taxRate,
			List<ProductPriceLevel> priceLevels,String currency) {
		
		List<ProductPriceLevel> returnPriceLevels = new ArrayList<>();
		priceLevels.stream().forEach( productPriceLevel -> {
			//没有价格不计算
			if(productPriceLevel == null || productPriceLevel.getPrice() == null){
				return;
			}
			BigDecimal calAmount = new BigDecimal(productPriceLevel.getPrice());
			//计算汇率和税率
			calAmount = this.calculateMethodWithRates(exchangeRate, taxRate, calAmount, currency);
			
			//过滤价格为零的梯度
			if(BigDecimal.ZERO.compareTo(calAmount)== BIGDECIMAL_EQUAL){
				return;
			}
			
			//复制梯度
			ProductPriceLevel targetProductPriceLevel = new ProductPriceLevel();
			BeanUtils.copyProperties(productPriceLevel, targetProductPriceLevel);
			targetProductPriceLevel.setPrice(calAmount != null ? calAmount.toString() : null);
			returnPriceLevels.add(targetProductPriceLevel);
		});
		//单价
		if(!CollectionUtils.isEmpty(returnPriceLevels) && returnPriceLevels.get(0) != null){
			productPrice.setUnitPrice(returnPriceLevels.get(0).getPrice());
		}
		productPrice.setPriceLevels(returnPriceLevels);
	}
	
	/**
	 * 按不同方式计算金额
	 * @param exchangeRate 汇率
	 * @param taxRate 税率
	 * @param amount 计算金额
	 * @param price 价格
	 * @param calculateType 计算类型（按固定值/按百分比）
	 * @param currency 币种
	 * @return 计算后的金额
	 */
	private BigDecimal calculateMethod( BigDecimal amount,
			String price ,String calculateType) {
		//没有价格不计算
		if(price == null){
			return null;
		}
		
		BigDecimal calAmount = null;
		
		//按固定值  原价 +数值
		if(ProductPriceActionTypeId.PRICE_POL.toString().equals(calculateType)){
			calAmount = new BigDecimal(price).add(amount);
			//logger.debug("按固定值  原价 +数值计算，计算公式： {},计算结果：{}",price+" + "+amount.toString(),calAmount.toString());
			
		}
		//按百分比 加价： 原价 /（1-xx%） 降价：原价 *（1-xx%）
		if(ProductPriceActionTypeId.PRICE_FLAT.toString().equals(calculateType)){
			//-1表示小于,0是等于,1是大于，大于0表示加价
			if(amount.compareTo(new BigDecimal("0")) == BIGDECIMAL_GREAT_THAN){
				calAmount = new BigDecimal(price).divide(new BigDecimal("1").subtract(amount.abs()),8, BigDecimal.ROUND_HALF_EVEN);
				//logger.debug("按百分比 加价： 原价 /（1-xx%），计算公式： {},计算结果：{}",price+"/(1-"+amount.abs().toString()+")",calAmount.toString());
			}
			if(amount.compareTo(new BigDecimal("0")) == BIGDECIMAL_LESS_THAN){
				calAmount = new BigDecimal(price).multiply(new BigDecimal("1").subtract(amount.abs()));
				//logger.debug("按百分比 降价：原价 *（1-xx%），计算公式： {},计算结果：{}",price+"*(1-"+amount.abs().toString()+")",calAmount.toString());
			}
			if(amount.compareTo(new BigDecimal("0")) == BIGDECIMAL_EQUAL){
				calAmount = new BigDecimal(price);
				//logger.debug("按百分比 计算价格为0，计算公式： {},计算结果：{}","0",calAmount.toString());
			}
		}
		return calAmount;
	}
	
	/**
	 * 计算汇率和税率的金额
	 * @param exchangeRate 汇率
	 * @param taxRate 税率
	 * @param calAmount 计算后金额
	 * @param currency 币种类型
	 * @return
	 */
	private BigDecimal calculateMethodWithRates(BigDecimal exchangeRate, BigDecimal taxRate, 
			BigDecimal calAmount ,String currency) {
		if(calAmount == null){
			 return new BigDecimal("0");
		}
		BigDecimal returnAmount = calAmount;
		//logger.debug("计算汇率和税率的金额,币种类型{}:",currency==null?"null":currency);
		//人民币需要计算汇率和税率
		if(Currency.CNY.toString().equals(currency)){
			returnAmount = calAmount.multiply(exchangeRate).multiply(new BigDecimal("1").add(taxRate));
			//logger.debug("人民币需要计算汇率和税率,计算公式：{}",calAmount.toString()+"*"+exchangeRate.toString()+"*"+taxRate.toString());
		}
		
		//保留4位有效数字
		returnAmount = keep4EffectiveNumber(returnAmount);
		
		//最后计算价格结果小于0,置为0
		if(returnAmount != null &&returnAmount.compareTo(new BigDecimal("0")) == BIGDECIMAL_LESS_THAN){
			returnAmount = new BigDecimal("0");
		}
		//logger.debug("计算结果:{}",returnAmount!= null ? returnAmount.toString():"null");
		return returnAmount;
	}

	/**
	 * 保留4位有效数字（价格 >= 100只处理为保留2位小数）
	 * @param amount 原始金额
	 * @return 返回保留4位有效数字后的金额
	 * @since 2017年7月4日
	 * @author zr.wanghong
	 */
	private BigDecimal keep4EffectiveNumber(BigDecimal amount) {
		BigDecimal returnAmount;
		if(amount.compareTo(new BigDecimal("0")) == BIGDECIMAL_GREAT_THAN 
				&& amount.compareTo(new BigDecimal("0.1")) == BIGDECIMAL_LESS_THAN){
			//0 < 价格 < 0.1 保留5位小数 0.012345 => 0.01235
			returnAmount = amount.setScale(5, BigDecimal.ROUND_HALF_UP);
		}else if((amount.compareTo(new BigDecimal("0.1")) == BIGDECIMAL_GREAT_THAN 
				|| amount.compareTo(new BigDecimal("0.1")) == BIGDECIMAL_EQUAL) 
				&& amount.compareTo(new BigDecimal("1")) == BIGDECIMAL_LESS_THAN){
			//0.1 <= 价格  < 1 保留4位小数
			returnAmount = amount.setScale(4, BigDecimal.ROUND_HALF_UP);
		}else if((amount.compareTo(new BigDecimal("1")) == BIGDECIMAL_GREAT_THAN 
				|| amount.compareTo(new BigDecimal("1")) == BIGDECIMAL_EQUAL) 
				&& amount.compareTo(new BigDecimal("10")) == BIGDECIMAL_LESS_THAN ){
			//1 <= 价格  < 10 和 保留3位小数
			returnAmount = amount.setScale(3, BigDecimal.ROUND_HALF_UP);
		}else {
			//10 <= 价格  < 100 和100 <= 价格保留2位小数
			returnAmount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		
		//取5位小数，不足五位小数部分补零
		returnAmount = returnAmount.setScale(5, BigDecimal.ROUND_DOWN);
		
		//如果最后计算出来的商品价格超过两位小数就不需要补零，否则用零补足两位小数
		String amountStr = returnAmount.stripTrailingZeros().toPlainString();
		if(amountStr.lastIndexOf(".") != -1){
			String decimalStr = amountStr.substring(amountStr.lastIndexOf(".")+1);
			if(decimalStr.length() < 2 ){
				returnAmount = returnAmount.setScale(2, BigDecimal.ROUND_DOWN);
			}else{
				returnAmount =  returnAmount.stripTrailingZeros();
			}
		}else{
			returnAmount = returnAmount.setScale(2, BigDecimal.ROUND_DOWN);
		}
		return returnAmount;
	}
	
	/**
	 * 查找模板
	 * @param product 商品对象
	 * @return 模板缓存对象
	 */
	public PriceRuleTemplateCache findTemplate(Product product){
		String vendor = product.getVendorId();
		String warehouse = product.getSourceId() == null ? NONE : product.getSourceId();
		Integer brand = product.getSpu().getManufacturerId();
		Map<String, String> cateMap = this.getCategoriesMap(product.getSpu().getCategories());
		String cate1 = cateMap.get("cate1");
		String cate2 = cateMap.get("cate2");
		String cate3 = cateMap.get("cate3");
		
		Map<String, ProductPrice> pricesMap = this.getCNYAndUSDPriceMap(product.getPrices());
		//商品上的人民币和美元价格
		ProductPrice priceCNY = pricesMap.get(CNY);
		ProductPrice priceUSD = pricesMap.get(USD);
		
		//币种类型
		String currencyType = this.getCurrencyType(priceCNY, priceUSD);
		
		List<String> canNoneParams = new ArrayList<>();
		canNoneParams.add(vendor);
		canNoneParams.add(warehouse);
		canNoneParams.add(brand != null?brand.toString():NONE);
		canNoneParams.add(cate1);
		canNoneParams.add(cate2);
		canNoneParams.add(cate3);//可以为NONE
		
		List<String> canNotNoneParams = new ArrayList<>();
		canNotNoneParams.add(currencyType);//不可以为NONE
		
		List<String> keys = getKeyList(canNoneParams,canNotNoneParams);
		return this.findTemplateByKey(keys);
	}
	
	/**
	 * 获取类别Map
	 * @param categories
	 * @return
	 * @since 2017年7月3日
	 * @author zr.wanghong
	 */
	public Map<String, String> getCategoriesMap(List<ProductCategory> categories){
		String cate1 = NONE;
		String cate2 = NONE;
		String cate3 = NONE;
		if(!CollectionUtils.isEmpty(categories)){
			for (ProductCategory productCategory : categories) {
				if(productCategory != null && productCategory.getLevel() == 1){
					cate1 = productCategory.getId().toString();
				}
				if(productCategory != null && productCategory.getLevel() == 2){
					cate2 = productCategory.getId().toString();
				}
				if(productCategory != null && productCategory.getLevel() == 3){
					cate3 = productCategory.getId().toString();
				}
			}
		}
		Map<String, String> cateMap = new HashMap<>();
		cateMap.put("cate1", cate1);
		cateMap.put("cate2", cate2);
		cateMap.put("cate3", cate3);
		return cateMap;
	}
	
	/**
	 * 从缓存中查询一个优先级最高的模板
	 * @param keys 缓存key
	 * @return 模板缓存对象
	 */
	@SuppressWarnings("unchecked")
	public PriceRuleTemplateCache findTemplateByKey(List<String> keys){
		Cache cache = cacheManager.getCache("priceRuleTemplateCache");
		ValueWrapper valueWrapper = cache.get("PriceRuleTemplateCacheMap");
		    
	    Map<String, PriceRuleTemplateCache> priceRuleTemplateCacheMap;
	    if(valueWrapper != null){
	    	priceRuleTemplateCacheMap = (Map<String, PriceRuleTemplateCache>) cache.get("PriceRuleTemplateCacheMap").get();
	    	 if(priceRuleTemplateCacheMap == null || priceRuleTemplateCacheMap.size() == 0){
	 	    	return null;
	 	    }
	 	    
	 		PriceRuleTemplateCache priceTemplate = null;
	 		for (int i = 0; i < keys.size(); i++) {
	 			//如果缓存中能获取，则表示获取到的是一个优先级最高的模板
	 			priceTemplate  = priceRuleTemplateCacheMap.get(keys.get(i));
	 			if(priceTemplate != null){
	 				break;
	 			}
	 		}
	 		return priceTemplate;
	    }else{
	    	return null;
	    }
	}

	@SuppressWarnings("unchecked")
	private static List<String> getKeyList(List<String> canNoneParams,List<String> lastCantNoneParams){
		//固定顺序参数有3个
		int lastParamNumber = 3;
		List<String> result = new ArrayList<>();
		List<String> firstCantNoneParams;
		List<String> noneParams;
		int noneIndex = -1;
		
		int foreachCount = (canNoneParams.size()-lastParamNumber)*2;
		for(int j = lastParamNumber;j <= foreachCount;j++){
			firstCantNoneParams = (List<String>)((ArrayList<String>)canNoneParams).clone();
			noneParams = new ArrayList<>();
			if(noneIndex >0)
				firstCantNoneParams.set(noneIndex, "none");
				
			if(j == canNoneParams.size()){
				firstCantNoneParams.set(noneIndex+1, "none");
			}
			
			for(int i = 0;i <= lastParamNumber;i++){
				if(i>0){
					noneParams.add(0, firstCantNoneParams.get(firstCantNoneParams.size()-1));
					firstCantNoneParams.remove(firstCantNoneParams.size()-1);
				}
				result.add(getKey(firstCantNoneParams,noneParams,lastCantNoneParams));
			}
			if(canNoneParams.size()-j-1 > 0)
			noneIndex = canNoneParams.size()-j-1;
		}
		return result;
	}
	
	private static String getKey(List<String> firstCantNoneParams,List<String> noneParams,List<String> lastCantNoneParams){
		StringBuilder key = new StringBuilder();
		
		firstCantNoneParams.forEach( fc -> {
			if(key.length()>0)
				key.append("-");
			key.append(fc);
		});
		noneParams.stream().forEach( np -> {
			if(key.length()>0)
				key.append("-");
			key.append(NONE);
		});
		
		lastCantNoneParams.stream().forEach(lcnp -> {
			if(key.length()>0)
				key.append("-");
			key.append(lcnp);
		});
		return key.toString();
	}
	
	
	
	/**
	 * 秒杀专场-价格查询
	 * @param productVos productVo中必填字段：商品ID，活动ID，区间ID
	 * @return 价格信息集合
	 * @since 2017年6月14日
	 * @author zr.wanghong
	 */
	public List<PriceInfo> queryPriceForSeckill(List<ProductVo> productVos){
		List<ProductVo> prodVos = new ArrayList<>();
		
		List<String> productIds = productVos.stream().map(ProductVo::getId).collect(Collectors.toList());
		Map<String,ProductVo> productMaps = productVos.stream().collect(Collectors.toMap(ProductVo::getId, Function.identity(), (key1, key2) -> key2));
		//为传入的productVo补充mongodb物料的基础属性
		Iterable<Product> cursor = productRepository.findAll(productIds);
		cursor.forEach( tempProduct -> {
			ProductVo newProductVo = new ProductVo();
			ProductVo oldProductVo = productMaps.get(tempProduct.getId());
			BeanUtils.copyProperties(tempProduct, newProductVo);
			newProductVo.setActivityId(oldProductVo.getActivityId());
			newProductVo.setPeriodsId(oldProductVo.getPeriodsId());
			newProductVo.setActivityType(oldProductVo.getActivityType());
			newProductVo.setPromoDiscountStatus(oldProductVo.getPromoDiscountStatus());
			newProductVo.setPromoDiscount(oldProductVo.getPromoDiscount());
			newProductVo.setActivityProductVo(oldProductVo.getActivityProductVo());
			prodVos.add(newProductVo);
		});
		//查询活动价或市场价
		return this.queryPriceWithActivity(prodVos);
	}
	
	
	
	/**
	 * portal搜索结果页，查询价格方法<br>
	 * 1. 有活动返回活动价格<br>
	 * 2. 没有活动返回实际价格策略计算价格<br>
	 * 3. 增加异步处理机制
	 * @param productVos 商品对象集合
	 * @return 价格信息集合
	 * @since 2017年6月13日
	 * @author zr.wanghong
	 */
	public List<PriceInfo> queryPriceWithActivity(List<ProductVo> productVos){
		List<PriceInfo> priceinfos = new ArrayList<>();
		List<Future<Void>> resultList = new ArrayList<>();
		
		//1.查找当天生效的活动
		//List<Activity> actList = activityProductManager.getEffectActivity(cacheManager.getCache("activity"));
		
		productVos.forEach( productVo -> 
			resultList.add(priceQueryAsyncManager.asyncSetPricesV2(priceinfos,productVo))
		);
		//确保异步执行完成，主线程继续往下走
		for(Future<Void> futureInfo : resultList){
			try {
				futureInfo.get();
			} catch (Exception e) {
				logger.error("异步获取价格异常:{},{}",e.getMessage(),e);
			}
		}
		return priceinfos;
	}
	
	/**
	 * 设置价格信息<br>
	 * 1.计算商品的市场销售价，被放入备份字段中<br>
	 * 2.查询商品是否有活动价，有活动价，则覆盖市场销售价字段为活动价<br>
	 * @param productVo
	 * @param priceInfo
	 * @since 2017年6月30日
	 * @author zr.wanghong
	 */
	public void setPriceInfoByCache(ProductVo productVo, PriceInfo priceInfo,List<Activity> actList) {
		if(CollectionUtils.isEmpty(actList)){
			return;
		}
		String activityId = productVo.getActivityId();
		String periodsId = productVo.getPeriodsId();
		// 折扣状态(Y/N)
		String promoDiscountStatus = String.valueOf(productVo.getPromoDiscountStatus());
		// 折扣值
		double promoDiscount = productVo.getPromoDiscount();

		// 缓存中的活动ID
		String newActivityId = "";
		// 缓存中的时段ID
		String newPeriodsId = "";
		String newPromoDiscountStatus = "";
		double newPromoDiscount = 0;
		if (!CollectionUtils.isEmpty(actList)) {//促销活动的商品才需要查询优先级关系
			ActivityProduct ac = productManager.getActivityProductVoOrdery(productVo, actList);
			newActivityId = null != ac ? ac.getActivityId() : "";
			newPeriodsId = null != ac ? ac.getPeriodsId() : "";

			newPromoDiscountStatus = null != ac ? String.valueOf(ac.getPromoDiscountStatus()) : "";
			newPromoDiscount = null != ac ? ac.getPromoDiscount() : 0;
		}
		// 传入活动，并且是秒杀活动优先
		// 活动ID
		// 以下代码主要是兼容，秒杀活动页面，过期了也要显示秒杀的活动价格
		String curActivityId = activityId != null && ActivityManager.ACTIVITY_SPIKE_TYPE.equals(productVo.getActivityType()) ? activityId : newActivityId;
		// 时段ID
		String curPeriodsId = periodsId != null && ActivityManager.ACTIVITY_SPIKE_TYPE.equals(productVo.getActivityType()) ? periodsId : newPeriodsId;

		promoDiscountStatus = StringUtils.isNotEmpty(promoDiscountStatus) && ActivityManager.ACTIVITY_SPIKE_TYPE.equals(productVo.getActivityType()) ? promoDiscountStatus : newPromoDiscountStatus;
		promoDiscount = promoDiscount > 0 && ActivityManager.ACTIVITY_SPIKE_TYPE.equals(productVo.getActivityType()) ? promoDiscount : newPromoDiscount;

		// 商品ID
		String productId = productVo.getId();

		//活动缓存
		Cache activityCache = cacheManager.getCache("activity");
		// 4.查找缓存中当前时段活动生效的商品
		StringBuilder sb = new StringBuilder();
		String productCacheKey = sb.append(curActivityId + "-").append(curPeriodsId + "-").append("product-" + productId).toString();
		//logger.info("商品活动缓存key:{}", productCacheKey);
		ActivityProduct activityProductCache = activityCache.get(productCacheKey, ActivityProduct.class);
		if (activityProductCache == null) {
			return;
		}
		//logger.info("成功获取商品活动缓存 activityProductCache,{}", productCacheKey);

		// 启用折扣，并且有折扣值，则按折扣计算活动价格信息
		if ("Y".equals(promoDiscountStatus) && promoDiscount > 0) {
			getDiscountPriceInfo(priceInfo, promoDiscount);
		} else {
			// 设置活动价格梯度
			List<ProductPriceLevel> priceLevels = new ArrayList<>();
			this.getActivityPriceLevel(priceLevels, activityProductCache);

			ProductPrice productPrice = new ProductPrice();
			productPrice.setPriceLevels(priceLevels);
			if (!CollectionUtils.isEmpty(priceLevels)) {
				productPrice.setUnitPrice(priceLevels.get(0).getPrice());
			}
			productPrice.setCurrencyCode(activityProductCache.getCurrencyUomId());

			// 汇率和税率
			Map<String, Object> rates = null;
			rates = this.getExchangeRateAndTaxRate(rates);
			BigDecimal exchangeRate = new BigDecimal(rates.get(KEY_EXCHANGERATE).toString());
			BigDecimal taxRate = new BigDecimal(rates.get(KEY_TAXRATE).toString());

			List<ProductPrice> activityPrices = new ArrayList<>();
			activityPrices.add(productPrice);
			activityPrices = this.calculateDefault(activityPrices);

			// 5.计算活动价税率和汇率
			activityPrices = this.calProductPricesWithRates(activityPrices, exchangeRate, taxRate);
			// 活动价
			priceInfo.setResalePrices(activityPrices);
		}
		priceInfo.setProductId(productVo.getId());
	}
	
	
	public void setPriceInfoByCacheV2(ProductVo productVo, PriceInfo priceInfo) {
		ActivityProductVo activityProductVo = productVo.getActivityProductVo();
		if(null == activityProductVo){
			return;
		}
		// 折扣状态(Y/N)
		String promoDiscountStatus = String.valueOf(activityProductVo.getPromoDiscountStatus());
		// 折扣值
		double promoDiscount = activityProductVo.getPromoDiscount();
		promoDiscountStatus = StringUtils.isNotEmpty(promoDiscountStatus)? promoDiscountStatus : "";
		promoDiscount = promoDiscount > 0 ? promoDiscount : 0;
		
		// 启用折扣，并且有折扣值，则按折扣计算活动价格信息
		if ("Y".equals(promoDiscountStatus) && promoDiscount > 0) {
			getDiscountPriceInfo(priceInfo, promoDiscount);
		} else {
			// 设置活动价格梯度
			List<ProductPriceLevel> priceLevels = new ArrayList<>();
			this.getActivityPriceLevel(priceLevels, activityProductVo);

			ProductPrice productPrice = new ProductPrice();
			productPrice.setPriceLevels(priceLevels);
			if (!CollectionUtils.isEmpty(priceLevels)) {
				productPrice.setUnitPrice(priceLevels.get(0).getPrice());
			}else{
				//如果活动中没有上传商品的梯度价，则直接返回mongo计算策略后的价格
				return;
			}
			productPrice.setCurrencyCode(activityProductVo.getCurrencyUomId());

			// 汇率和税率
			Map<String, Object> rates = null;
			rates = this.getExchangeRateAndTaxRate(rates);
			BigDecimal exchangeRate = new BigDecimal(rates.get(KEY_EXCHANGERATE).toString());
			BigDecimal taxRate = new BigDecimal(rates.get(KEY_TAXRATE).toString());

			List<ProductPrice> activityPrices = new ArrayList<>();
			activityPrices.add(productPrice);
			activityPrices = this.calculateDefault(activityPrices);

			// 5.计算活动价税率和汇率
			activityPrices = this.calProductPricesWithRates(activityPrices, exchangeRate, taxRate);
			// 活动价
			priceInfo.setResalePrices(activityPrices);
		}
		priceInfo.setProductId(productVo.getId());
	}

	/**
	 * 获取活动折扣价
	 * @param priceInfo 价格信息
	 * @param promoDiscount 折扣系数值
	 * @since 2017年8月4日
	 * @author zr.wanghong
	 */
	private void getDiscountPriceInfo(PriceInfo priceInfo, double promoDiscount) {
		
		List<ProductPrice> activityPrices = new ArrayList<>();
		
		//销售价
		List<ProductPrice> resalePrices = priceInfo.getResalePrices();
		for (ProductPrice productPrice : resalePrices) {
			List<ProductPriceLevel> activityPriceLevels = new ArrayList<>();
			ProductPrice activityProductPrice = new ProductPrice();
			BeanUtils.copyProperties(productPrice, activityProductPrice);
			List<ProductPriceLevel> priceLevels = productPrice.getPriceLevels();
			for (ProductPriceLevel priceLevel : priceLevels) {
				ProductPriceLevel newPriceLevel = new ProductPriceLevel();
				BeanUtils.copyProperties(priceLevel, newPriceLevel);
				
				String price = priceLevel.getPrice();
				if(StringUtils.isNotEmpty(price)){
					price = keep4EffectiveNumber(new BigDecimal(price).multiply(BigDecimal.valueOf(promoDiscount))).toString();
				}
				newPriceLevel.setPrice(price);
				activityPriceLevels.add(newPriceLevel);
			}
			activityProductPrice.setPriceLevels(activityPriceLevels);
			if(!CollectionUtils.isEmpty(activityPriceLevels)){
				activityProductPrice.setUnitPrice(activityPriceLevels.get(0).getPrice());
			}
			activityPrices.add(activityProductPrice);
		}
		priceInfo.setResalePrices(activityPrices);
	}
	

	/**
	 * 获取活动价格梯度
	 * @param priceLevels
	 * @param activityProductCache
	 * @since 2017年6月13日
	 * @author zr.wanghong
	 */
	private void getActivityPriceLevel(List<ProductPriceLevel> priceLevels, ActivityProduct activityProductCache) {
		ProductPriceLevel priceLevel = new ProductPriceLevel();
		if(activityProductCache.getQtyBreak1() != null && activityProductCache.getQtyBreak1()!= 0){
			priceLevel.setBreakQuantity(activityProductCache.getQtyBreak1().longValue());
			String amount = this.keep4EffectiveNumber(new BigDecimal(activityProductCache.getPriceBreak1().toString())).toString();
			priceLevel.setPrice(amount);
			priceLevels.add(priceLevel);
		}
		if(activityProductCache.getQtyBreak2() != null && activityProductCache.getQtyBreak2() != 0){
			priceLevel = new ProductPriceLevel();
			priceLevel.setBreakQuantity(activityProductCache.getQtyBreak2().longValue());
			String amount = this.keep4EffectiveNumber(new BigDecimal(activityProductCache.getPriceBreak2().toString())).toString();
			priceLevel.setPrice(amount);
			priceLevels.add(priceLevel);
		}
		if(activityProductCache.getQtyBreak3() != null && activityProductCache.getQtyBreak3() != 0){
			priceLevel = new ProductPriceLevel();
			priceLevel.setBreakQuantity(activityProductCache.getQtyBreak3().longValue());
			String amount = this.keep4EffectiveNumber(new BigDecimal(activityProductCache.getPriceBreak3().toString())).toString();
			priceLevel.setPrice(amount);
			priceLevels.add(priceLevel);
		}
		if(activityProductCache.getQtyBreak4() != null && activityProductCache.getQtyBreak4() != 0){
			priceLevel = new ProductPriceLevel();
			priceLevel.setBreakQuantity(activityProductCache.getQtyBreak4().longValue());
			String amount = this.keep4EffectiveNumber(new BigDecimal(activityProductCache.getPriceBreak4().toString())).toString();
			priceLevel.setPrice(amount);
			priceLevels.add(priceLevel);
		}
		if(activityProductCache.getQtyBreak5() != null && activityProductCache.getQtyBreak5()!=0){
			priceLevel = new ProductPriceLevel();
			priceLevel.setBreakQuantity(activityProductCache.getQtyBreak5().longValue());
			String amount = this.keep4EffectiveNumber(new BigDecimal(activityProductCache.getPriceBreak5().toString())).toString();
			priceLevel.setPrice(amount);
			priceLevels.add(priceLevel);
		}
	}
}