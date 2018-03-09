package com.yikuyi.product.goods.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.springboot.model.LoginUser;
import com.framework.springboot.utils.AuthorizationUtil;
import com.framewrok.springboot.web.RequestHelper;
import com.google.common.collect.Lists;
import com.yikuyi.brand.model.ProductBrand;
import com.yikuyi.category.vo.ProductCategoryParent;
import com.yikuyi.info.InfoClientBuilder;
import com.yikuyi.party.PartyClientBuilder;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.model.PartyAttribute;
import com.yikuyi.party.model.PartyAttributes;
import com.yikuyi.party.supplier.SupplierVo;
import com.yikuyi.party.vo.PartyVo;
import com.yikuyi.product.category.manager.CategoryManager;
import com.yikuyi.product.model.ProductImage;
import com.yikuyi.product.model.ProductPrice;
import com.yikuyi.product.model.ProductPriceLevel;
import com.yikuyi.product.model.ProductSearchResult;
import com.yikuyi.product.model.ProductStand;
import com.yikuyi.product.rule.delivery.manager.LeadTimeManager;
import com.yikuyi.product.vo.ProductVo;
import com.yikuyi.recommend.model.Recommendation;
import com.yikuyi.recommend.vo.RecommendVo;
import com.yikuyi.rule.delivery.vo.ProductInfo;
import com.yikuyi.rule.delivery.vo.ProductLeadTimeVo;
import com.yikuyi.rule.price.PriceInfo;
import com.ykyframework.model.IdGen;

/**
 * 搜索相关业务
 * @author zr.wenjiao@yikuyi.com
 * @version 1.0.0
 */
@Service
public class SearchManagerV2 {
	
	private static final Logger logger = LoggerFactory.getLogger(SearchManagerV2.class);
	
	@Value("${config.leadTime.vendorId}")
	private String leadTimeVendorId;
	
	@Value("${dns-prefetchs}")
	private String dnsPrefetchs;
	
	@Autowired
	private PriceQueryManager priceQueryManager;
	
	@Autowired
	private LeadTimeManager leadTimeManager;
	
	@Autowired
	private CategoryManager categoryManager;
	
	@Autowired
	private SearchManager searchManager;
	
	@Autowired
	private ProductSearchResultManager productSearchResultManager;
	
	@Autowired
	private SearchAsyncManager searchAsyncManager;
	
	@Autowired
	private PartyClientBuilder partyClientBuilder;
	
	@Autowired
	private InfoClientBuilder infoClientBuilder;
		
	@Autowired
	private ProductManager productManager;
	
	@Autowired
	private AuthorizationUtil authorizationUtil;
	
	@Autowired
	private KeyworkMatchManager keyworkMatchManager;
	
	private static final String STR_SEPARATE = "qqq;;;";
	
	private static final String VENDORID = "vendorId";
	
	private static final String MANUFACTURER = "manufacturer";
	
	private static final String SPUID = "spuId"; 
	
	private static final String SHOWQTY = "showQty";  
	
	private static final String SEARCHRESULT = "searchResult";  
	
	private static final String KEYWORD = "keyword";
	
	private static final String RETURN = "return";
	
	private static final String CATEGORY_TYPE= "categoryType";
	
	private static final String[] PRODUCT_RETURN_FIELD = {"_id","maxFactoryLeadTimeHK","maxFactoryLeadTimeML","maxLeadTimeHK",
			"maxLeadTimeML","minFactoryLeadTimeHK","minFactoryLeadTimeML","minLeadTimeHK","minLeadTimeML",
			"prices","qty","sourceId","vendorId","spu.categories","spu.description","spu._id","spu.images",
			"spu.manufacturer","spu.manufacturerId","spu.manufacturerPartNumber","spu.rohs","spu.spuId","mov","spu.restrictMaterialType"};
	
	/**
	 * 获取商品相关信息
	 * @param keyword       关键字
	 * @param vendorId      供应商Id
	 * @param manufacturer  制造商
	 * @param spuId         spu的唯一标识
	 * @param cat           分类
	 * @param sort          排序字段（暂时只支持按库存）
	 * @param page          当前页
	 * @param pageSize      每页条数
	 * @param showQty       是否只显示有库存的数据
	 * @return
	 * @since 2017年5月17日
	 * @author zr.wenjiao@yikuyi.com
	 * @throws InterruptedException 
	 */
	public Map<String, Object> getProductInfo(String keyword,String vendorId,String manufacturer,String cat,String sort,int page,int pageSize,String showQty,String isPreview) throws InterruptedException{
		long l = System.currentTimeMillis();
//		KeywordMatchVo keywordMatch = keyworkMatchManager.keywordMatch(keyword,cat,manufacturer,vendorId);
//		String keywordTemp = keywordMatch.getKeyword();
//		String catTemp = Optional.ofNullable(keywordMatch.getCat()).map(v->String.valueOf(v.getId())).orElse(StringUtils.EMPTY);
//		String manufacturerTemp = Optional.ofNullable(keywordMatch.getBrand()).map(v->String.valueOf(v.getStatus())).orElse(StringUtils.EMPTY);
//		String vendorIdTemp = Optional.ofNullable(keywordMatch.getSupplier()).map(SupplierVo::getSupplierId).orElse(StringUtils.EMPTY);
		
		Map<String, Object> resultMap = new HashMap<>();
		//获取所有的分类
//		List<ProductCategoryParent> cateList = categoryManager.getByCategroyId(null);
		//拼装查询参数
		JSONObject conJson = convergeParams(keyword, vendorId, manufacturer, cat, sort, page, pageSize, showQty);
		//查询商品信息
		JSONObject resultInfo = searchProduct(conJson,null);
		JSONArray hits = new JSONArray();
		if(resultInfo != null && !resultInfo.isEmpty()){
			hits = resultInfo.getJSONArray("hits");
		}
		//把_id、spu中的_id更改为id,并转成product对象
		List<ProductVo> productList = handleProductInfo(hits);
		
		//合并活动信息
		productManager.mergeActivity(productList, isPreview);
		
		//合并MOV信息
//		productManager.mergeMovInfo(productList);
		
		//获取交期
//		List<ProductLeadTimeVo> leadTimeInfo = getProductLeadTime(productList);
		
		//获取价格,并合并价格
		getProductPrice(productList);
		
		//处理商品图片
		inventoryImage(productList);
		
		//合并交期
//		mergeLeadTime(leadTimeInfo,productList);
		
		//查询商品是否在做活动
		searchManager.getVendorParams(productList);
		
		//拼装返回到页面上的值
//		Map<String, Object> returnParams = convergeReturnParams(conJson,resultInfo,cateList,keywordMatch.getSupplier());
		Map<String, Object> returnParams = convergeReturnParams(conJson,resultInfo,null);
		resultMap.putAll(returnParams);
		resultMap.put("productInfo", productList);
		//keyword不为空时，记录搜索结果日志
//		insertSearchResult(keywordTemp,hits);
		return resultMap;
	}
	
	
	/**
	 * 获取推广位信息，并根据相应的参数查询符合条件的商品
	 * @param page     当前页
	 * @param size     每页条数
	 * @param conJson  前台传入的查询参数
	 * @return
	 * @since 2017年5月5日
	 * @author zr.wenjiao@yikuyi.com
	 * @throws InterruptedException 
	 */
	public List<JSONObject> findProductByParams(JSONObject conJson,List<ProductCategoryParent> cateList){
		List<JSONObject> conJsonList = new ArrayList<>();
		int page = conJson.getIntValue("page");
		//获取推广位信息
		/*RecommendVo recommendation=	new RecommendVo();
		recommendation.setStatus(Recommendation.Status.PUBLISHED.toString());
		List<RecommendVo> recList = recommendationManager.findBySearchPromotion(recommendation);*/
		List<RecommendVo> recList = infoClientBuilder.builderRecommendationClient().findBySearchPromotion(Recommendation.Status.PUBLISHED);
		if(CollectionUtils.isEmpty(recList)){
			return conJsonList;
		}
	    //推广位只在第一页，并且没有排序的情况下展示
		if(page == 1 && StringUtils.isEmpty(conJson.getString("sort"))){
			for(RecommendVo rec : recList){
				JSONObject recConJson = new JSONObject();
				JSONObject mapInfo = JSONObject.parseObject(rec.getContent());
				//判断推广位中设置的查询条件和页面传递过来的查询条件是否相等
				JSONObject returnParams = checkEquals(mapInfo,conJson,recConJson,cateList);
				if(returnParams != null && !returnParams.isEmpty()){
					//推广位查询条数
					int numRe = mapInfo.getIntValue("goodsQuantity");
					recConJson.put(KEYWORD, conJson.getString(KEYWORD));
					recConJson.put(SHOWQTY, conJson.getString(SHOWQTY));
					recConJson.put("size", numRe);
					recConJson.put("page", 1);
					recConJson.put("includes", PRODUCT_RETURN_FIELD);
					conJsonList.add(recConJson);
				}
			}
		}
		return conJsonList;
	}
	
	/**
	 * 判断推广位中的条件是否包含在前台传入的条件中，如不包含则不显示推广位
	 * @param mapInfo  推广位中的配置的查询条件
	 * @param conJson  前台传入的参数
	 * @return
	 * @since 2017年5月5日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public JSONObject checkEquals(JSONObject mapInfo,JSONObject conJson,JSONObject recConJson,List<ProductCategoryParent> cateList){
		String vendorId = conJson.getString(VENDORID);
		String manufacturer = conJson.getString(MANUFACTURER);
		String cat = conJson.getString("cat");
		boolean flag = false;
		if(StringUtils.isEmpty(vendorId) && StringUtils.isEmpty(manufacturer) && StringUtils.isEmpty(cat)){
			flag = true;
		}
		String distributor = mapInfo.getString("distributor");
		String sourceName = mapInfo.getString("source");
		String categoryType = mapInfo.getString(CATEGORY_TYPE);
		//当前台传入的参数为空时，则不需要校验和推广位中的参数是否相等
		if(flag){
			recConJson.put(VENDORID, distributor);
			recConJson.put(MANUFACTURER, sourceName);
			recConJson.put("cat", categoryType);
			return recConJson;
		}
		//判断供应商、分类、制造商参数值是否相等
		JSONObject value = assemblyParam(mapInfo,conJson,recConJson,cateList);
		if(value == null || value.isEmpty()){
			return new JSONObject();
		}
        return recConJson;
	}
	
	/***
	 * 判断供应商、分类、制造商参数值是否相等
	 * @param mapInfo
	 * @param conJson
	 * @param recConJson
	 * @return
	 * @since 2017年6月28日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public JSONObject assemblyParam(JSONObject mapInfo,JSONObject conJson,JSONObject recConJson,List<ProductCategoryParent> cateList){
		//判断供应商是否相等
		String vendorValue = paramValue(conJson.getString(VENDORID),mapInfo.getString("distributor"));
		if(StringUtils.isNotEmpty(vendorValue) && (RETURN).equals(vendorValue)){
			return new JSONObject();
		}
		recConJson.put(VENDORID, vendorValue);
        //判断分类是否相等
        String catValue = assemblyCategory(mapInfo,conJson.getString("cat"),cateList);
		if(StringUtils.isNotEmpty(catValue) && (RETURN).equals(catValue)){
			return new JSONObject();
		}
		recConJson.put("cat", catValue);
		//判断制造商参数值是否相等
		JSONObject manufacturerValue = assemblyManufacturer(conJson.getString(MANUFACTURER),mapInfo.getString("source"),recConJson);
		if(manufacturerValue == null || manufacturerValue.isEmpty()){
			return new JSONObject();
		}
		return recConJson;
	}
	
	/**
	 * 判断制造商参数值是否相等
	 * @param manufacturer
	 * @param sourceName
	 * @param recConJson
	 * @return
	 * @since 2017年6月28日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public JSONObject assemblyManufacturer(String manufacturer,String sourceName,JSONObject recConJson){
		String[] manufacturerArr = null;
		if(StringUtils.isNotEmpty(manufacturer)){
			manufacturerArr = manufacturer.split(",");
		}
		if(StringUtils.isEmpty(sourceName)){
			recConJson.put(MANUFACTURER, manufacturer);
		}else{
			if(StringUtils.isEmpty(manufacturer) && StringUtils.isNotEmpty(sourceName)){
	        	recConJson.put(MANUFACTURER, sourceName);
			}
	        if(StringUtils.isNotEmpty(manufacturer) && StringUtils.isNotEmpty(sourceName) && Arrays.asList(manufacturerArr).contains(sourceName)){
	        	recConJson.put(MANUFACTURER, sourceName);
			}
	        if(StringUtils.isNotEmpty(manufacturer) && StringUtils.isNotEmpty(sourceName) && !Arrays.asList(manufacturerArr).contains(sourceName)){
	        	return new JSONObject();
			}
		}
        return recConJson;
	}
	
	/**
	 * 判断两个参数是否相等
	 * @param conValue  页面传递到后台的查询条件
	 * @param recValue  推广位中设置的查询条件
	 * @return
	 * @since 2017年6月28日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public String paramValue(String conValue,String recValue){
		String resultValue = "";
		if(StringUtils.isEmpty(recValue)){
			resultValue = conValue;
		}else{
			if(StringUtils.isNotEmpty(recValue) && StringUtils.isEmpty(conValue)){
	        	resultValue = recValue;
			}
	        if(StringUtils.isNotEmpty(conValue) && StringUtils.isNotEmpty(recValue) && conValue.equalsIgnoreCase(recValue)){
	        	resultValue = recValue;
			}
	        if(StringUtils.isNotEmpty(conValue) && StringUtils.isNotEmpty(recValue) && !conValue.equalsIgnoreCase(recValue)){
	        	resultValue = RETURN;
			}
		}
        return resultValue;
	}
	
	/**
	 * 判断分类参数值是否相等
	 * @param mapInfo
	 * @param cat
	 * @param cateList
	 * @return
	 * @since 2017年7月5日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public String assemblyCategory(JSONObject mapInfo,String cat,List<ProductCategoryParent> cateList){
		String resultValue;
		String catId = mapInfo.getString(CATEGORY_TYPE);
		//页面传过来的分类ID为空时，直接返回搜索推广中设置的分类Id,否则需要判断两个分类是否相等
		if(StringUtils.isEmpty(cat)){
			resultValue = catId;
		}else{
			//搜索推广设置的分类Id不为空，且判断是否和页面传过来的分类Id是否相等
			if(StringUtils.isNotEmpty(catId)){
				resultValue = isCategory(cateList,mapInfo,cat);
			}else{
				resultValue = cat;
			}
		}
		return resultValue;
	}
	
	/**
	 * 判断页面传过来的分类Id和搜索推广中设置的分类Id是否相等
	 * @param cateList
	 * @param mapInfo
	 * @param cat
	 * @return
	 * @since 2017年7月5日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public String isCategory(List<ProductCategoryParent> cateList,JSONObject mapInfo,String cat){
		String value = "";
		for(ProductCategoryParent cateInfo : cateList){
			if(String.valueOf(cateInfo.getId()).equals(cat)){
				int catLevel = cateInfo.getLevel();
				String recCatId = mapInfo.getString(CATEGORY_TYPE + catLevel);
				if(StringUtils.isEmpty(recCatId) || !cat.equals(recCatId)){
					value = RETURN;
				}
				if(StringUtils.isNotEmpty(recCatId) && cat.equals(recCatId)){
					value = mapInfo.getString(CATEGORY_TYPE);
				}
			}
		}
		return value;
	}
	
	/**
	 * 从搜索引擎中查询商品信息
	 * @param conJson
	 * @return
	 * @since 2017年5月22日
	 * @author zr.wenjiao@yikuyi.com
	 * @throws InterruptedException 
	 */
	@SuppressWarnings("rawtypes")
	public JSONObject searchProduct(JSONObject conJson,List<ProductCategoryParent> cateList) throws InterruptedException{
		int size = conJson.getIntValue("size");
		//获取推广位信息的查询参数
//		List<JSONObject> conJsonList = findProductByParams(conJson,cateList);
		List<JSONObject> conJsonList = Lists.newArrayList();
		conJsonList.add(conJson);
		JSONObject result = new JSONObject();
		List<Future<JSONObject>> resultList = new ArrayList<>();
		JSONArray resultHit = new JSONArray();
		//异步查询es中的商品
		for(JSONObject param : conJsonList){
			Future<JSONObject> future = searchAsyncManager.searchProductInfo(param);
			resultList.add(future);
		}
		//合并查询后的结果集
		try {
			for(Future futureInfo : resultList){
				result = (JSONObject)futureInfo.get();
				JSONArray re = result.getJSONArray("hits");
				if(CollectionUtils.isNotEmpty(re)){
					resultHit.addAll(re);
				}
	    	}
		} catch (InterruptedException e) {
			logger.error("getProduct InterruptedException:",e);
			throw e;
		} catch (ExecutionException e) {
			logger.error("getProduct ExecutionException:",e);
		}
		//去除重复元素
//		handleHits(resultHit,result,size);
		return result;	
	}
	
	/**
	 * 去除重复元素
	 * @param resultHit
	 * @param result
	 * @param size
	 * @since 2017年6月28日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public void handleHits(JSONArray resultHit,JSONObject result,int size){
		if(CollectionUtils.isEmpty(resultHit)){
			return;
		}
		JSONArray resultArray = new JSONArray();
		//去除重复元素
		Map<String,Object> map = new HashMap<>();
		for(Object o : resultHit){
			JSONObject jsonObject = (JSONObject)o;
			String id = jsonObject.getString("_ID");
			if(!map.containsKey(id)){
				map.put(id, id);
				resultArray.add(o);
			}
		}
		result.put("hits", resultArray);
		//当resultArray的总条数大于size时，只取和size相等的条数
		if(resultArray.size() > size){
			resultHit.clear();
			for(int i = 0; i < size; i++){
				resultHit.add(resultArray.get(i));
			}
			result.put("hits", resultHit);
		}
	}
	
	/**
	 * 把_id、spu中的_id更改为id,并转成product对象
	 * @param hits
	 * @since 2017年5月18日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public List<ProductVo> handleProductInfo(JSONArray hits){
		List<ProductVo> productList = new ArrayList<>();
		if(CollectionUtils.isEmpty(hits)){
			return productList;
		}
		for(int i =0; i< hits.size();i++){
			JSONObject info = (JSONObject)hits.get(i);
			String productId = StringUtils.isEmpty(info.getString("_ID")) ? info.getString("_id") : info.getString("_ID");
			if(StringUtils.isNotEmpty(productId)){
				info.put("id", productId);
				info.remove("_ID");
				info.remove("_id");
			}
			JSONObject spuInfo = info.getJSONObject("spu");
			String spuId = StringUtils.isEmpty(spuInfo.getString("_id")) ? spuInfo.getString("_ID") : spuInfo.getString("_id");
			if(StringUtils.isNotEmpty(spuId)){
				spuInfo.put("id", spuId);
				spuInfo.remove("_ID");
				spuInfo.remove("_id");
				info.put("spu", spuInfo);
			}
			//转成product对象
			try {
				ObjectMapper om = new ObjectMapper();
				om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				ProductVo productVo = om.readValue(info.toJSONString(), ProductVo.class);
				productList.add(productVo);
			} catch (Exception e) {
				logger.error("handleProductInfo error",e);
			}
		}
		return productList;
	}
	
	/**
	 * 获取商品的交期
	 * @param hits
	 * @since 2017年5月18日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public List<ProductLeadTimeVo> getProductLeadTime(List<ProductVo> productList){
		List<ProductLeadTimeVo> leadTimeList = new ArrayList<>();
		List<ProductLeadTimeVo> idList = new ArrayList<>();
		if(CollectionUtils.isEmpty(productList)){
			return leadTimeList;
		}
		try{
			productList.stream().forEach(productVo -> {
				ObjectMapper om = new ObjectMapper();
				om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				//当供应商Id包含在verdonIdExist中时，则需要查询交期策略,不包含则根据上传的交期字段显示
				/*if(Arrays.asList(leadTimeVendor).contains(productVo.getVendorId())){*/
					ProductLeadTimeVo leadTimeVo = new ProductLeadTimeVo();
					BeanUtils.copyProperties(productVo, leadTimeVo);
					idList.add(leadTimeVo);
				/*}else{
					//拼装交期
					ProductInfo leadInfo = assemblyLeadTime(productVo);
					productVo.setRealLeadTime(leadInfo);
				}*/
			});
			//查询交期
			if(CollectionUtils.isNotEmpty(idList)){
				leadTimeList = leadTimeManager.getLeadTimeByProdcutList(idList);
			}
		}catch (Exception e) {
			logger.error("getProductLeadTime error",e);
	    }
		return leadTimeList;
	}
	
	/**
	 * 当商品的供应商ID不是digikey,future,mouser时，根据上传的交期字段来拼装
	 * @param productVo
	 * @return
	 * @since 2017年6月28日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public ProductInfo assemblyLeadTime(ProductVo productVo){
		ProductInfo leadInfo = new ProductInfo();
		leadInfo.setLeadTimeCH("0");
		leadInfo.setLeadTimeHK("0");
		leadInfo.setFactoryLeadTimeCH("0");
		leadInfo.setFactoryLeadTimeHk("0");
		//国内现货交期,判断最小、最大值是否等于null，或者都等于0
		Integer minML = productVo.getMinLeadTimeML();
		Integer maxMl = productVo.getMaxLeadTimeML();
		if(isValueNull(minML,maxMl) && isValueEqualZero(minML,maxMl)){
			//判断最大、最小值是否相等，如果相等则显示最小交期，不相等则显示交期范围
			leadInfo.setLeadTimeCH(isValueEqual(minML,maxMl));
		}
		//香港现货交期，逻辑同上
		Integer minHk = productVo.getMinLeadTimeHK();
		Integer maxHK = productVo.getMaxLeadTimeHK();
		if(isValueNull(minHk,maxHK) && isValueEqualZero(minHk,maxHK)){
			leadInfo.setLeadTimeHK(isValueEqual(minHk,maxHK));
		}
		//国内工厂交期，逻辑同上
		Integer minFactoryML = productVo.getMinFactoryLeadTimeML();
		Integer maxFactoryML = productVo.getMaxFactoryLeadTimeML();
		if(isValueNull(minFactoryML,maxFactoryML) && isValueEqualZero(minFactoryML,maxFactoryML)){
			leadInfo.setFactoryLeadTimeCH(isValueEqual(minFactoryML, maxFactoryML));
		}
		//香港工厂交期，逻辑同上
		Integer minFactoryHK = productVo.getMinFactoryLeadTimeHK();
		Integer maxFactoryHK = productVo.getMaxFactoryLeadTimeHK();
		if(isValueNull(minFactoryHK,maxFactoryHK) && isValueEqualZero(minFactoryHK,maxFactoryHK)){
			leadInfo.setFactoryLeadTimeHk(isValueEqual(minFactoryHK,maxFactoryHK));
		}
		return leadInfo;
	}
	
	/**
	 * 判断是否等于null
	 * @param minValue
	 * @param maxValue
	 * @return
	 * @since 2017年6月28日
	 * @author zr.wenjiao@yikuyi.com
	 */
	private boolean isValueNull(Integer minValue,Integer maxValue){
		boolean result = true;
		if(minValue == null || maxValue == null){
			result = false;
		}
		return result;
	}
	
	/**
	 * 判断是否等于0
	 * @param minValue
	 * @param maxValue
	 * @return
	 * @since 2017年6月28日
	 * @author zr.wenjiao@yikuyi.com
	 */
	private boolean isValueEqualZero(Integer minValue,Integer maxValue){
		boolean result = true;
		if(minValue == 0 && maxValue == 0){
			result = false;
		}
		return result;
	}
	
	/**
	 * 判断最大、最小值是否相等，如果相等则显示最小值，不相等则显示范围
	 * @param leadInfo
	 * @param minValue
	 * @param maxValue
	 * @return
	 * @since 2017年6月28日
	 * @author zr.wenjiao@yikuyi.com
	 */
	private String isValueEqual(Integer minValue,Integer maxValue){
		String resultValue;
		if(minValue.equals(maxValue)){
			resultValue = String.valueOf(minValue);
		}else{
			resultValue = minValue + "-" + maxValue;
		}
		return resultValue;
	}
	
	/**
	 * 合并交期
	 * @param leadTimeList
	 * @param hits
	 * @since 2017年5月18日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public void mergeLeadTime(List<ProductLeadTimeVo> leadTimeList,List<ProductVo> productList){
		if(CollectionUtils.isEmpty(leadTimeList) || CollectionUtils.isEmpty(productList)){
			return;
		}
		for(ProductVo vo : productList){
			leadTimeList.stream().forEach(pi -> {
				if(vo.getId().equals(pi.getId())){
					ProductInfo leadInfo = new ProductInfo();
					//如果库存大于0，交期为现货交期,否则为排单交期
					if(vo.getQty() > 0){
						leadInfo = handleLeadTime(vo, pi);
					}else{
						leadInfo = handleFactoryLeadTime(vo, pi);
					}
					vo.setRealLeadTime(leadInfo);
				}
			});
		}
	}
	
	/**
	 * 获取商品的价格
	 * @param hits
	 * @since 2017年5月18日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public void getProductPrice(List<ProductVo> productList){
		if(CollectionUtils.isEmpty(productList)){
			return;
		}
		//查询价格
		List<PriceInfo> priceList = priceQueryManager.queryPriceWithActivity(productList);
		//合并价格
		if(CollectionUtils.isNotEmpty(priceList)){
			for(ProductVo info : productList){
				//设置商品的币种
				info.setCurrencyCode(getCurrencyCode(info.getPrices()));
				
				priceList.stream().forEach(oj -> {
					if(info.getId().equals(oj.getProductId())){						
						//取销售价格
						List<ProductPrice> priceArray = oj.getResalePrices();						
						//补全梯度价格
						mergePriceLevel(priceArray);
						info.setPrices(priceArray);
						info.setOriginalResalePrices(oj.getOriginalResalePrices());
					}
				});
			}
		}
	}
	
	/**
	 * 获得商品的币种
	 * @param esPrice   从es中获得的价格数据
	 * @return
	 * @since 2017年5月12日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public String getCurrencyCode(List<ProductPrice> esPrice){
		String currencyCode = "CNY";
		if(CollectionUtils.isEmpty(esPrice)){			
			return "";
		}
		for(ProductPrice price : esPrice){
			String code = price.getCurrencyCode();
			if("USD".equalsIgnoreCase(code)){
				currencyCode = "USD";
			}
		}
		return currencyCode;
	}
	
	/**
	 * 补全梯度价格
	 * @param priceArray
	 * @since 2017年5月18日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public void mergePriceLevel(List<ProductPrice> priceArray){
		if(CollectionUtils.isEmpty(priceArray)){
			return;
		}
		//求最大梯度
		int maxSize = 0;
		for(ProductPrice oj :priceArray){
			List<ProductPriceLevel> levels = oj.getPriceLevels();
			int currentSize = levels !=null ? levels.size():0;	
			maxSize = Math.max(maxSize, currentSize);
		}
		//求梯度列表
		List<Long> breakQtyList = breakQty(priceArray,maxSize);
		//开始补全梯度
		breakPrice(priceArray,breakQtyList,maxSize);
	}
	
	/**
	 * 求梯度列表
	 * @param priceArray
	 * @param maxSize
	 * @return
	 * @since 2017年6月28日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public List<Long> breakQty(List<ProductPrice> priceArray,int maxSize){
		List<Long> breakQtyList = new ArrayList<>();
		for(ProductPrice oj :priceArray){
			List<ProductPriceLevel> levels = oj.getPriceLevels();
			if(CollectionUtils.isEmpty(levels)){
				continue;
			}
			int currentSize = levels !=null ? levels.size():0;	
			if(currentSize == maxSize){
				for(ProductPriceLevel oItem :levels){
					Long quantity = oItem.getBreakQuantity() != null ? oItem.getBreakQuantity() : 0L;
					breakQtyList.add(quantity);					
				}		
			}
		}	
		return breakQtyList;
	}
	
	/**
	 * 拼装梯度价格
	 * @param priceArray
	 * @param breakQtyList
	 * @param maxSize
	 * @since 2017年6月28日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public void breakPrice(List<ProductPrice> priceArray,List<Long> breakQtyList,int maxSize){
		//只有一种梯度的时候
		if(priceArray.size() ==1){
			ProductPrice jsonObject = priceArray.get(0);
			ProductPrice newPrice = new ProductPrice();
			String current = jsonObject.getCurrencyCode();
			if(StringUtils.isBlank(current)){
				current = "CNY";
			}
			jsonObject.setCurrencyCode(current);
			String newCurrent = "CNY".equals(current)?"USD":"CNY";
			newPrice.setCurrencyCode(newCurrent);
			newPrice.setPriceLevels(new ArrayList<ProductPriceLevel>());
			priceArray.add(newPrice);			
		}
		
		//已经补全2个梯度之后,将梯度的数量统一
		for(ProductPrice oj :priceArray){
			List<ProductPriceLevel> levels = oj.getPriceLevels();
			if(levels == null){
				levels  = new ArrayList<>();
			}
			for(int i= 0;i<maxSize;i++ ){
				ProductPriceLevel breaklevel = new ProductPriceLevel();
				if(levels.size()<=i){
					breaklevel.setBreakQuantity(breakQtyList.get(i));
					breaklevel.setPrice("0");
					levels.add(breaklevel);	
				}
				//如果价格是空，则价格该为0
				ProductPriceLevel level = levels.get(i);
				String priceStr = level.getPrice();
				if(StringUtils.isBlank(priceStr)){
					level.setPrice("0");
				}
			}					
		}
	}
	
	/**
	 * 获取所有的供应商
	 * 
	 * @since 2017年5月19日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public List<PartyVo> getVendorInfo(JSONArray verdonAgg){
		if(CollectionUtils.isEmpty(verdonAgg)){
			return Collections.emptyList();
		}
		List<PartyVo> vendorList = new ArrayList<>();
		try{
			Set<String> supplierIds = verdonAgg.stream().map(v->((JSONObject)v).getString("key")).collect(Collectors.toSet());
			Map<String,SupplierVo> suppliers = partyClientBuilder.supplierClient().getSupplierSimple(supplierIds);
			//只返回前端需要的字段
			for(Object o : verdonAgg){
				JSONObject jsonObject = (JSONObject)o;
				String verdonId = jsonObject.getString("key");
				SupplierVo tempVo = suppliers.get(verdonId);
				PartyVo vo = new PartyVo();
				vo.setId(verdonId);
				vo.setDisplayName(tempVo.getDisplayName());
				PartyAttributes partyAttributes = new PartyAttributes();
				partyAttributes.setIsVendorDetail(tempVo.isVendorDetail()?"Y":"N");
				PartyAttribute partyAttribute = new PartyAttribute();
				partyAttribute.setKey("IS_SHOW_NAME");
				partyAttribute.setValue(tempVo.isShowName()?"Y":"N");
				partyAttributes.setIsShowName(partyAttribute);
				vo.setPartyAttributes(partyAttributes);
				vo.setLeadtimeAccuracyrate(tempVo.getLeadtimeAccuracyrate());
				vendorList.add(vo);
			}
		}catch(Exception e){
		    logger.error("getVendorInfo error",e);
		}
		return vendorList;
	}
	
	/**
	 * 汇聚制造商
	 * @param manufacturerAgg
	 * @return
	 * @since 2017年5月19日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public Map<String, List<ProductBrand>> convergeManufacturer(List<ProductBrand> brandInfo,JSONArray manufacturerAgg){
		/* 获取所有制造商 */
		Map<String, List<ProductBrand>> allManufacturerMap = new LinkedHashMap<>();
		List<String> allManufacturerId = new ArrayList<>();
		if(CollectionUtils.isEmpty(brandInfo) || CollectionUtils.isEmpty(manufacturerAgg)){
			return allManufacturerMap;
		}
		for(Object o : manufacturerAgg){
			JSONObject jsonObject = (JSONObject)o;
			allManufacturerId.add(jsonObject.getString("key"));
		}
		
		//根据制造商Id匹配制造商名称
		List<ProductBrand> allManufacturerList = new ArrayList<>();
		for(String manufacturerId : allManufacturerId){
			brandInfo.stream().forEach(brand -> {
				if(manufacturerId.equals(String.valueOf(brand.getId()))) {
					ProductBrand brandNew = new ProductBrand();
					brandNew.setId(brand.getId());
					brandNew.setBrandName(brand.getBrandName());
					allManufacturerList.add(brandNew);
					return;
				}
			});	
		}
		manufacturerAgg.clear();
		
		//品牌首字母,从A到Z循环,如果有的使用传过来的json，如果没有则新建
		for(int i = 0;i< 26;i++){
			String nowBI = String.valueOf((char) ('A'+i));
			allManufacturerMap.put(nowBI, null);
		}
		allManufacturerList.stream().forEach(binfo -> {
			String nowBI  = binfo.getBrandName().toUpperCase().substring(0, 1);
			if(allManufacturerMap.containsKey(nowBI)){
				addBrandToMap(nowBI,binfo,allManufacturerMap);
			}else{
				addBrandToMap("other",binfo,allManufacturerMap);
			}
		});
		allManufacturerList.clear();		
		return allManufacturerMap;
	}
	
	public void addBrandToMap(String key , ProductBrand value , Map<String,List<ProductBrand>> allBrandMap){
		if(null == allBrandMap.get(key)){
			List<ProductBrand> classifyBrandList = new ArrayList<>();
			classifyBrandList.add(value);
			allBrandMap.put(key, classifyBrandList);
		}else{
			allBrandMap.get(key).add(value);
		}
	}
	
	/**
	 * 汇聚分类
	 * @param catAgg
	 * @return
	 * @since 2017年5月19日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public SortedMap<String, SortedMap<String,List<String>>> convergeCat(JSONArray catAgg,List<ProductCategoryParent> cateList){
		SortedMap<String, SortedMap<String,List<String>>> rstMap =  new TreeMap<>((String a , String b) -> b.compareTo(a));
		//分类信息为空，则直接返回
		if(CollectionUtils.isEmpty(cateList) || CollectionUtils.isEmpty(catAgg)){
			return rstMap;
		}
		//处理查询后的结果并拼装层级关系
		for(Object o : catAgg){
			JSONObject jsonObject = (JSONObject)o;
			Integer catId = jsonObject.getInteger("key");
			cateList.stream().forEach(cate -> {
				if(catId.equals(cate.getId())){
					handleCategory(rstMap,cate);
				}
			});
		}
		return rstMap;
	}
	
	/**
	 * 分类拼装层级关系
	 * @param rstMap
	 * @param cate
	 * @since 2017年5月19日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public void handleCategory(SortedMap<String, SortedMap<String,List<String>>> rstMap,ProductCategoryParent cate){
		String mapKey = cate.getName()+STR_SEPARATE+cate.getId();
		if(cate.getLevel().equals(1)){
			if(!rstMap.containsKey(mapKey)){
				rstMap.put(mapKey, new TreeMap<>((String a , String b) -> b.compareTo(a)));
			}
		}
		if(cate.getLevel().equals(2)){
			String parentMapKey =  cate.getParent().getName()+STR_SEPARATE+cate.getParent().getId();
			SortedMap<String,List<String>> selfMap;
			if(rstMap.containsKey(parentMapKey)){
				selfMap = rstMap.get(parentMapKey);
			}else{
				selfMap = new TreeMap<>((String a , String b) -> b.compareTo(a));
			}
			if(!selfMap.containsKey(mapKey)){
				selfMap.put(mapKey, new ArrayList<>());
			}
			rstMap.put(parentMapKey, selfMap);
		}
		if(cate.getLevel().equals(3)){
			String parentMapKey =  cate.getParent().getName()+STR_SEPARATE+cate.getParent().getId();
			String grandpaKey =  cate.getParent().getParent().getName()+STR_SEPARATE+cate.getParent().getParent().getId();
			SortedMap<String,List<String>> parentMap;
			if(rstMap.containsKey(grandpaKey)){
				parentMap = rstMap.get(grandpaKey);
			}else{
				parentMap = new TreeMap<>((String a , String b) -> b.compareTo(a));
			}
			List<String> selfList;
			if(parentMap.containsKey(parentMapKey)){
				selfList = parentMap.get(parentMapKey);
			}else{
				selfList = new ArrayList<>();
			}
			selfList.add(mapKey);
			selfList.sort((String a , String b) -> a.compareTo(b));
			parentMap.put(parentMapKey, selfList);
			rstMap.put(grandpaKey, parentMap);
		}
	}
	
	/**
	 * 汇聚前台传入的参数，存放在Json中
	 * @param keyword       关键字
	 * @param vendorId      供应商Id
	 * @param manufacturer  制造商
	 * @param spuId         spuId
	 * @param cate          分类
	 * @param sort          排序
	 * @param page          当前页
	 * @param size          每页条数
	 * @param showQty       是否只显示有库存的
	 * @return
	 * @since 2017年5月22日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public JSONObject convergeParams(String keyword, String vendorId, String manufacturer,String cate,String sort, int page, Integer size, String showQty){
		JSONObject conJson = new JSONObject();
		//当keyword不为空时转换为小写
		String keywordNew = keyword;
//        if(StringUtils.isNotEmpty(keyword)){
//        	keywordNew = keyword.toLowerCase();
//        }
		conJson.put(KEYWORD, keywordNew);
		conJson.put("reKeyWord",keyword);
		conJson.put(VENDORID, vendorId);
		conJson.put(MANUFACTURER, manufacturer);
		conJson.put("cat", cate);
		conJson.put("sort", sort);
		conJson.put("page", page);
		conJson.put("size", size);
		conJson.put(SHOWQTY, showQty);
		conJson.put("includes", PRODUCT_RETURN_FIELD);
		return conJson;
	}
	
	/**
	 * 拼装返回到界面的参数
	 * @param conJson
	 * @param resultInfo
	 * @return
	 * @since 2017年5月22日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public Map<String, Object> convergeReturnParams(JSONObject conJson,JSONObject resultInfo,List<ProductCategoryParent> cateList){
		Map<String, Object> rstMap = new HashMap<>();
		String cat = conJson.getString("cat");
		int size = conJson.getIntValue("size");
		int page = conJson.getIntValue("page");
		String keyWord = conJson.getString("reKeyWord");
		String manufacturer= conJson.getString(MANUFACTURER);
		rstMap.put(KEYWORD, keyWord);//查询条件
		rstMap.put(VENDORID, conJson.getString(VENDORID));//供应商
		rstMap.put(MANUFACTURER, manufacturer);
		rstMap.put(SPUID, conJson.getString(SPUID));
		rstMap.put("cat",cat);
		rstMap.put("sort", conJson.getString("sort"));			
		rstMap.put("pageSize", size);
		rstMap.put(SHOWQTY, conJson.getString(SHOWQTY));
		//获取所有的制造商
		//List<ProductBrand> brandList = brandManager.findAll();
		//处理面包屑中的分类展示
		//rstMap.put("categoryData", crumbsCategory(cateList,cat));
		//处理面包屑中的制造商展示
		//rstMap.put("manufacturerName", crumbsBrand(manufacturer,brandList));
		//处理面包屑中的制造商展示
		//processVendorName(conJson, rstMap , partyVo);
		
		if(null == resultInfo || resultInfo.isEmpty()){//查询失败
			rstMap.put(SEARCHRESULT, 0);
			return rstMap;
		}
		long total = resultInfo.getLongValue("total");
		if(total == 0){//查询结果为0
			rstMap.put(SEARCHRESULT, 0);
			return rstMap;
		}
		int totalPageNum =(int) (total  +  size - 1) / size;
		if(totalPageNum > 10){
			totalPageNum = 10;
		}
		rstMap.put("totalPageNum", totalPageNum);
		int pageNew = page;
		if(page > totalPageNum){
			pageNew = totalPageNum;
		}
		rstMap.put("page", pageNew);
		//JSONArray aggs = resultInfo.getJSONArray("aggs");
		//rstMap.put("verdonIdMap", convergeVerdonId(aggs.getJSONArray(0)));//汇聚供应商
		//rstMap.put("manufacturerMap", convergeManufacturer(brandList,aggs.getJSONArray(1)));//汇聚制造商
		//rstMap.put("verdonAllList", getVendorInfo(aggs.getJSONArray(0)));//全部供应商
		//rstMap.put("catMap", convergeCat(aggs.getJSONArray(2),cateList));//汇聚分类
		rstMap.put("total", total);//数量
		rstMap.put(SEARCHRESULT, 2);
		//富昌仓库定义
		Map<String,String> futureMap = new HashMap<>();
		futureMap.put("future-FUTC", "北美仓");
		futureMap.put("future-FUTA", "亚太仓");
		futureMap.put("future-FUTE", "欧洲仓");
		rstMap.put("futureSource", futureMap);
		//rstMap.put("manufacturerDetail", getBrandInfo(brandList,resultInfo.getJSONArray("hits")));
		return rstMap;
	}
	
	/**
	 * 处理面包屑中的制造商展示
	 * @param conJson
	 * @param rstMap
	 * @param partyVo
	 */
	private void processVendorName(JSONObject conJson , Map<String,Object> rstMap , PartyVo partyVo){
		try {
			//处理面包屑中的供应商展示
			if(null != partyVo && null != partyVo.getPartyAttributes().getIsShowName()){
				String vendorName = null != partyVo.getPartyGroup() ? partyVo.getPartyGroup().getGroupCodeNum() : StringUtils.EMPTY;
				rstMap.put("vendorName", "Y".equalsIgnoreCase(partyVo.getPartyAttributes().getIsShowName().getValue())
						? partyVo.getPartyGroup().getGroupName() : vendorName);
				rstMap.put("showVendorName", "Y".equalsIgnoreCase(partyVo.getPartyAttributes().getIsShowName().getValue()));
				return;
			}
			if(StringUtils.isEmpty(conJson.getString("vendorId"))){
				return;
			}
			Party vendorVo = partyClientBuilder.vendorsClient().getVendorDetail(conJson.getString("vendorId"), authorizationUtil.getMockAuthorization());
			if(null == vendorVo){
				return;
			}
			if(null != vendorVo.getPartyGroup() && null != vendorVo.getPartyAttributes() && null != vendorVo.getPartyAttributes().getIsShowName()){
				String vendorName = vendorVo.getPartyGroup().getGroupCodeNum();
				rstMap.put("vendorName", "Y".equalsIgnoreCase(vendorVo.getPartyAttributes().getIsShowName().getValue())
						? vendorVo.getPartyGroup().getGroupName() : vendorName);
				rstMap.put("showVendorName", "Y".equalsIgnoreCase(vendorVo.getPartyAttributes().getIsShowName().getValue()));
			}
		} catch (Exception e) {
			logger.error("processVendorName error:{}",e.getMessage(),e);
		}
	}
	
	/**
	 * 处理面包屑中的分类展示
	 * @param cateList
	 * @param cat
	 * @return
	 * @since 2017年5月22日
	 * @author zr.wenjiao@yikuyi.com
	 */
	private ProductCategoryParent crumbsCategory(List<ProductCategoryParent> cateList,String cat){
		if(StringUtils.isEmpty(cat) || CollectionUtils.isEmpty(cateList)){
			return null;
		}
		for(ProductCategoryParent cate : cateList){
			if(cate.getId().equals(Integer.valueOf(cat))){
				return cate;
			}
		}
		return null;
	}
	
	/**
	 * 处理库存信息中图片的地址（/product开头的url需加上前缀）
	 * @param result
	 * @since 2017年3月29日
	 * @author zr.wenjiao@yikuyi.com
	 */
	private void inventoryImage(List<ProductVo> productList){
		if(CollectionUtils.isEmpty(productList)){
			return;
		}
		productList.stream().forEach(info -> {
			ProductStand spu = info.getSpu();
			List<ProductImage> spuImage = spu.getImages();
			List<ProductImage> spuImageNew = new ArrayList<>();
			//图片地址加上前缀后返回
			handleImageUrl(spuImage,spuImageNew);
			spu.setImages(spuImageNew);
		});
	}

	/**
	 * 处理库存信息中图片的地址（/product开头的url需加上前缀）
	 * @param result
	 * @since 2017年5月22日
	 * @author zr.wenjiao@yikuyi.com
	 */
	private void handleImageUrl(List<ProductImage> spuImage,List<ProductImage> spuImageNew){
		if(CollectionUtils.isEmpty(spuImage)){
			return;
		}
		spuImage.stream().forEach(imageInfo -> {
			String image = imageInfo.getUrl();
			String url = calYkyImgDomain(image);
			imageInfo.setUrl(url);
			spuImageNew.add(imageInfo);
		});
	}
	
	/**
	 * 由于图片存放的地址是/product/xxxxx这样的地址，但实际的地址是img0.xxx.com/product等。<br/>
	 * 需要读取配置文件将其拼接起来
	 * @param url
	 * @return
	 */
	public String calYkyImgDomain(String url){
		if(StringUtils.isEmpty(url)){
			return "";
		}
		String[] imageUrl = dnsPrefetchs.split(",");
		int max = 3;//可选url数量
		if(imageUrl.length < max){
			max = imageUrl.length;
		}
		if(url.indexOf("/product") == 0){
			int num = 0;
			for(int k = 0;k < url.length(); k++){
				num += url.charAt(k);
			}
			num = num % max;
			return imageUrl[num] + url;
		}
		return url;
	}
	
	/**
	 * 获取制造商
	 * @param result
	 * @return
	 * @since 2017年5月26日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public List<ProductBrand> getBrandInfo(List<ProductBrand> brandInfo,JSONArray result){
		List<ProductBrand> brandList = new ArrayList<>();
		Map<Integer,Object> map = new HashMap<>();
		if(CollectionUtils.isEmpty(result) || CollectionUtils.isEmpty(brandInfo)){
			return brandList;
		}
		for(int i = 0; i< result.size(); i++){
			JSONObject info = (JSONObject)result.get(i);
			Integer manufacturerId = info.getJSONObject("spu").getInteger("manufacturerId");
			brandInfo.stream().forEach(brand -> {
				if(manufacturerId.equals(brand.getId()) && !map.containsKey(manufacturerId)){
					ProductBrand brandNew = new ProductBrand();
					brandNew.setId(brand.getId());
					brandNew.setBrandName(brand.getBrandName());
					brandNew.setLogo(brand.getLogo());
					brandNew.setDesc(brand.getDesc());
					brandList.add(brandNew);
					map.put(brand.getId(), brand);
				}
			});
		}
		return brandList;
	}
	
	/**
	 * 处理面包屑中的制造商
	 * @param brands
	 * @return
	 * @since 2017年6月15日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public String crumbsBrand(String brands,List<ProductBrand> brandList){
		if(StringUtils.isEmpty(brands) || CollectionUtils.isEmpty(brandList)){
			return "";
		}
		List<String> brandName = new ArrayList<>();
		String[] brandInfo = brands.split(",");
		for(int i = 0; i < brandInfo.length; i++){
			String brandId = brandInfo[i];
			brandList.stream().forEach(brand -> {
				if(brandId.equals(brand.getId().toString())) {
					brandName.add(brand.getBrandName());
					return;
				}
			});	
		}
		if(CollectionUtils.isNotEmpty(brandName)){
			return StringUtils.join(brandName.toArray(),"、");
		}
		return null;
	}
	
	/**
	 * 汇聚供应商
	 * @param result
	 * @return
	 * @since 2016年11月22日
	 * @author zr.wenjiao@yikuyi.com
	 */
	private List<String> convergeVerdonId(JSONArray verdonAgg){
		List<String> allVerdonNameList = new ArrayList<>();
		if(CollectionUtils.isEmpty(verdonAgg)){
			return allVerdonNameList;
		}
		for(Object o : verdonAgg){
			JSONObject jsonObject = (JSONObject)o;
			allVerdonNameList.add(jsonObject.getString("key"));
		}
		return allVerdonNameList;
	}
	
	/**
	 * 记录搜索结果日志
	 * @param keyword
	 * @param hits
	 * @since 2017年6月27日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public void insertSearchResult(String keyword,JSONArray hits){
		if(StringUtils.isEmpty(keyword)){
			return;
		}
		String hasResult = "N";
		if(CollectionUtils.isNotEmpty(hits)){
			hasResult = "Y";
		}
		ProductSearchResult searchResult = new ProductSearchResult();
		String searchResultId = String.valueOf(IdGen.getInstance().nextId());
		searchResult.setSearchResultId(searchResultId);
		searchResult.setKeyWord(keyword);
		searchResult.setHasResult(hasResult);
		LoginUser user = RequestHelper.getLoginUser();
		if(user != null){
			searchResult.setPartyId(user.getId());
			searchResult.setPartyName(user.getUsername());
		}
		productSearchResultManager.insert(searchResult);
	}
	
	/**
	 * 合并现货交期
	 * @param vo
	 * @param pi
	 * @return
	 * @since 2017年8月28日
	 * @author tb.lijing@yikuyi.com
	 */
	public ProductInfo handleLeadTime(ProductVo vo,ProductLeadTimeVo pi){
		ProductInfo leadInfo = new ProductInfo();
		String[] leadTimeCHArr = pi.getRealityList().get(1).getRealityLeadTime().split("-");
		String[] leadTimeHKArr = pi.getRealityList().get(0).getRealityLeadTime().split("-");
		int leadTimeMinCH = 0;//国内最小现货交期
		int leadTimeMaxCH = 0;//国内最大现货交期
		int leadTimeMinHK = 0;//香港最小现货交期
		int leadTimeMaxHK = 0;//香港最大现货交期
		int minLeadTimeML = null == vo.getMinLeadTimeML()?0:vo.getMinLeadTimeML().intValue();//国内最小现货交期（来自上传）
		int maxLeadTimeML = null == vo.getMaxLeadTimeML()?0:vo.getMaxLeadTimeML().intValue();//国内最大现货交期（来自上传）
		int minLeadTimeHK = null == vo.getMinLeadTimeHK()?0:vo.getMinLeadTimeHK().intValue();//香港最小现货交期（来自上传）
		int maxLeadTimeHK = null == vo.getMaxLeadTimeHK()?0:vo.getMaxLeadTimeHK().intValue();//香港最大现货交期（来自上传）
		int leadTimeMinTotalCH = 0;
		int leadTimeMaxTotalCH = 0;
		int leadTimeMinTotalHK = 0;
		int leadTimeMaxTotalHK = 0;
		 if(leadTimeCHArr.length>1){
			 leadTimeMinCH = StringUtils.isBlank(leadTimeCHArr[0])?0:Integer.valueOf(leadTimeCHArr[0]);//国内最小现货交期（来自交期策略）
			 leadTimeMaxCH = StringUtils.isBlank(leadTimeCHArr[1])?0:Integer.valueOf(leadTimeCHArr[1]);//国内最大现货交期（来自交期策略）
		 }
		 if(leadTimeHKArr.length>1){
			 leadTimeMinHK = StringUtils.isBlank(leadTimeHKArr[0])?0:Integer.valueOf(leadTimeHKArr[0]);//香港最小现货交期（来自交期策略）
			 leadTimeMaxHK = StringUtils.isBlank(leadTimeHKArr[1])?0:Integer.valueOf(leadTimeHKArr[1]);//香港最大现货交期（来自交期策略)
		 }
		 leadTimeMinTotalCH = leadTimeMinCH+minLeadTimeML;//国内最小现货交期之和（上传+策略之和）
		 leadTimeMaxTotalCH = leadTimeMaxCH+maxLeadTimeML;//国内最大现货交期之和（上传+策略之和）
		 leadTimeMinTotalHK = leadTimeMinHK+minLeadTimeHK;//香港最小现货交期之和（上传+策略之和）
		 leadTimeMaxTotalHK = leadTimeMaxHK+maxLeadTimeHK;//香港最大现货交期之和（上传+策略之和）
		 //当IsShowLeadTime标识为"Y"时，表示上传交期为空时页面不显示交期
		 if("Y".equalsIgnoreCase(pi.getIsShowLeadTime())){
			 //当上传的交期为空或者0的时候，页面不显示交期
			 if(minLeadTimeML==0 || maxLeadTimeML==0){
				 leadInfo.setLeadTimeCH("0");
			 }
			 //当上传的交期为空或者0的时候，页面不显示交期
			 if(minLeadTimeHK==0 || maxLeadTimeHK==0){
				 leadInfo.setLeadTimeHK("0");
			 }
			if (minLeadTimeML>0 && maxLeadTimeML>0 && leadTimeMaxTotalCH >= leadTimeMinTotalCH) {
				if (leadTimeMinTotalCH != leadTimeMaxTotalCH) {
					leadInfo.setLeadTimeCH(leadTimeMinTotalCH + "-" + leadTimeMaxTotalCH);
				} else {
					leadInfo.setLeadTimeCH(String.valueOf(leadTimeMinTotalCH));
				}
			}
				 if(minLeadTimeHK>0 && maxLeadTimeHK>0 && leadTimeMaxTotalHK>=leadTimeMinTotalHK){
					 if(leadTimeMinTotalHK!=leadTimeMaxTotalHK){
						 leadInfo.setLeadTimeHK(leadTimeMinTotalHK+"-"+leadTimeMaxTotalHK); 
					 }else{
						 leadInfo.setLeadTimeHK(String.valueOf(leadTimeMinTotalHK)); 
					 }
				 }
		 }else{
			 if(leadTimeMaxTotalCH>0 && leadTimeMinTotalCH>0 && leadTimeMaxTotalCH>=leadTimeMinTotalCH){
				 if(leadTimeMinTotalCH!=leadTimeMaxTotalCH){
					 leadInfo.setLeadTimeCH(leadTimeMinTotalCH+"-"+leadTimeMaxTotalCH); 
				 }else{
					 leadInfo.setLeadTimeCH(String.valueOf(leadTimeMinTotalCH)); 
				 }
			 }
			 if(leadTimeMaxTotalHK>0 && leadTimeMinTotalHK>0 && leadTimeMaxTotalHK>=leadTimeMinTotalHK){
				 if(leadTimeMinTotalHK!=leadTimeMaxTotalHK){
					 leadInfo.setLeadTimeHK(leadTimeMinTotalHK+"-"+leadTimeMaxTotalHK); 
				 }else{
					 leadInfo.setLeadTimeHK(String.valueOf(leadTimeMinTotalHK)); 
				 }
			 }
		 }
		return leadInfo;
	}
	
	/**
	 * 合并排单交期
	 * @param vo
	 * @param pi
	 * @return
	 * @since 2017年9月1日
	 * @author tb.lijing@yikuyi.com
	 */
	public ProductInfo handleFactoryLeadTime(ProductVo vo,ProductLeadTimeVo pi){
		ProductInfo leadInfo = new ProductInfo();
		String[] factoryLeadTimeCHArr = pi.getRealityList().get(1).getRealityLeadTime().split("-");
		String[] factoryLeadTimeHKArr = pi.getRealityList().get(0).getRealityLeadTime().split("-");
		int schedulingLeadTimeMinCH = 0;
		int schedulingLeadTimeMaxCH = 0;
		int schedulingLeadTimeMinHK = 0;
		int schedulingLeadTimeMaxHK = 0;
		int minFactoryLeadTimeML = null == vo.getMinFactoryLeadTimeML()?0:vo.getMinFactoryLeadTimeML().intValue();//国内最小排单交期（来自上传）
		int maxFactoryLeadTimeML = null == vo.getMaxFactoryLeadTimeML()?0:vo.getMaxFactoryLeadTimeML().intValue();//国内最大排单交期（来自上传）
		int minFactoryLeadTimeHK = null == vo.getMinFactoryLeadTimeHK()?0:vo.getMinFactoryLeadTimeHK().intValue();//香港最小排单交期（来自上传）
		int maxFactoryLeadTimeHK = null == vo.getMaxFactoryLeadTimeHK()?0:vo.getMaxFactoryLeadTimeHK().intValue();//香港最大排单交期（来自上传）
		int factoryLeadTimeMinTotalCH = 0;
		int factoryLeadTimeMaxTotalCH = 0;
		int factoryLeadTimeMinTotalHK = 0;
		int factoryLeadTimeMaxTotalHK = 0;
		 if(factoryLeadTimeCHArr.length>1){
			 schedulingLeadTimeMinCH = StringUtils.isBlank(factoryLeadTimeCHArr[0])?0:Integer.valueOf(factoryLeadTimeCHArr[0]);//国内最小排单交期（来自交期策略）
			 schedulingLeadTimeMaxCH = StringUtils.isBlank(factoryLeadTimeCHArr[1])?0:Integer.valueOf(factoryLeadTimeCHArr[1]);//国内最大排单交期（来自交期策略）
		 }
		 if(factoryLeadTimeHKArr.length>1){
			 schedulingLeadTimeMinHK = StringUtils.isBlank(factoryLeadTimeHKArr[0])?0:Integer.valueOf(factoryLeadTimeHKArr[0]);//香港最小排单交期（来自交期策略）
			 schedulingLeadTimeMaxHK = StringUtils.isBlank(factoryLeadTimeHKArr[1])?0:Integer.valueOf(factoryLeadTimeHKArr[1]);//香港最大排单交期（来自交期策略）
		 }
		 factoryLeadTimeMinTotalCH = schedulingLeadTimeMinCH+minFactoryLeadTimeML;//国内最小排单交期之和（上传+策略之和）
		 factoryLeadTimeMaxTotalCH = schedulingLeadTimeMaxCH+maxFactoryLeadTimeML;//国内最大排单交期之和（上传+策略之和）
		 factoryLeadTimeMinTotalHK = schedulingLeadTimeMinHK+minFactoryLeadTimeHK;//香港最小排单交期之和（上传+策略之和）
		 factoryLeadTimeMaxTotalHK = schedulingLeadTimeMaxHK+maxFactoryLeadTimeHK;//香港最大排单交期之和（上传+策略之和）
		if ("Y".equalsIgnoreCase(pi.getIsShowLeadTime())) {
			if (minFactoryLeadTimeML == 0 || maxFactoryLeadTimeML == 0) {
				leadInfo.setFactoryLeadTimeCH("0");
			}
			if (minFactoryLeadTimeHK == 0 || maxFactoryLeadTimeHK == 0) {
				leadInfo.setFactoryLeadTimeHk("0");
			}
			if (minFactoryLeadTimeML>0 && maxFactoryLeadTimeML>0 && factoryLeadTimeMaxTotalCH >= factoryLeadTimeMinTotalCH) {
				if (factoryLeadTimeMinTotalCH != factoryLeadTimeMaxTotalCH) {
					leadInfo.setFactoryLeadTimeCH(factoryLeadTimeMinTotalCH + "-" + factoryLeadTimeMaxTotalCH);
				} else {
					leadInfo.setFactoryLeadTimeCH(String.valueOf(factoryLeadTimeMinTotalCH));
				}
			}
			if (minFactoryLeadTimeHK>0 && maxFactoryLeadTimeHK>0 && factoryLeadTimeMaxTotalHK >= factoryLeadTimeMinTotalHK) {
				if (factoryLeadTimeMinTotalHK != factoryLeadTimeMaxTotalHK) {
					leadInfo.setFactoryLeadTimeHk(factoryLeadTimeMinTotalHK + "-" + factoryLeadTimeMaxTotalHK);
				} else {
					leadInfo.setFactoryLeadTimeHk(String.valueOf(factoryLeadTimeMinTotalHK));
				}
			}
		} else {
			if (factoryLeadTimeMaxTotalCH>0 && factoryLeadTimeMinTotalCH>0 && factoryLeadTimeMaxTotalCH >= factoryLeadTimeMinTotalCH) {
				if (factoryLeadTimeMinTotalCH != factoryLeadTimeMaxTotalCH) {
					leadInfo.setFactoryLeadTimeCH(factoryLeadTimeMinTotalCH + "-" + factoryLeadTimeMaxTotalCH);
				} else {
					leadInfo.setFactoryLeadTimeCH(String.valueOf(factoryLeadTimeMinTotalCH));
				}
			}
			if (factoryLeadTimeMaxTotalHK>0 && factoryLeadTimeMinTotalHK>0 && factoryLeadTimeMaxTotalHK >= factoryLeadTimeMinTotalHK) {
				if (factoryLeadTimeMinTotalHK != factoryLeadTimeMaxTotalHK) {
					leadInfo.setFactoryLeadTimeHk(factoryLeadTimeMinTotalHK + "-" + factoryLeadTimeMaxTotalHK);
				} else {
					leadInfo.setFactoryLeadTimeHk(String.valueOf(factoryLeadTimeMinTotalHK));
				}
			}
		}
		return leadInfo;
	}
}

