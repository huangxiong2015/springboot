package com.yikuyi.product.goods.manager;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.ictrade.enterprisematerial.InventorySearchManager;
import com.mongodb.client.MongoCursor;
import com.yikuyi.brand.model.FuzzySearchType;
import com.yikuyi.brand.model.ProductBrand;
import com.yikuyi.category.model.ProductCategory;
import com.yikuyi.product.brand.manager.BrandManager;
import com.yikuyi.product.category.manager.CategoryManager;
import com.yikuyi.product.externalclient.PartyClientUtils;
import com.yikuyi.product.goods.dao.ProductClient;
import com.yikuyi.product.goods.dao.ProductRepository;
import com.yikuyi.product.model.Product;
import com.yikuyi.product.vo.ProductVo;
/**
 * 商品
 * 
 * @author zr.wujiajun
 * @2016年12月7日
 */
@Service
@Transactional
public class ProductAsyncManager {
	private static final Logger logger = LoggerFactory.getLogger(ProductAsyncManager.class);
	
	/**
	 * 更新时间的属性名
	 */
	public static final String UPDATED_TIME_MILLIS_FIELD_NAME = "updatedTimeMillis";
	
	
	 @Autowired
    private MongoTemplate mongoTemplate;
	 
	@Autowired
	private PartyClientUtils partyClientUtils;
	 
	 @Autowired
	private ProductRepository productRepository;
	 
	 @Autowired
	private ProductClient pc;
	 
	 /**
	 * 特殊供应商商品id缓存名
	 */
	private static final String SPECIAL_OFFER_PRODUCT_ID_CACHE_NAME = "specialOfferProductIdCacheName";

	/**
	 * 特殊供应商产品规则缓存名
	 */
	private static final String SPECIAL_OFFER_PRODUCT_RULE_CACHE_NAME = "specialOfferProductRuleCacheName";

	/**
	 * 特殊供应商文本名缓存
	 */

	private static final String SPECIAL_OFFER_TEXT_CACHE_NAME = "specialOfferTextCacheName";

	@Resource(name = "redisTemplate")
	private HashOperations<String,String,List<String>>  specialOfferProductIdCacheOps;

	@Resource(name = "redisTemplate")
	private HashOperations<String,String,List<String>> specialOfferProductRuleCacheOps;
	
	@Resource(name = "redisTemplate")
	private HashOperations<String,String,Map<String, String>> specialOfferTextCacheOps;
	
	@Autowired
	private PriceQueryManager priceQueryManager;
	
	@Autowired
	private InventorySearchManager inventorySearchManager;
	
	@Autowired
	private BrandManager brandManager;
	
	@Autowired
	private CategoryManager categoryManager;
	
	/**
	 * 一个月以外的非标准库存数据从正式表物理清除，写入到备份表里面
	 * 此服务jekins每天定时调度一次
	 * @since 2017年6月5日
	 * @author zr.wanghong
	 */
	public void cleanNonStandardData(){
		logger.info("开始非标准和垃圾数据清理!");
		//DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		//String collectionName = "product_"+LocalDateTime.now().format(dateFormatter);
		String collectionName = "product_prices_expired";
		LocalDateTime time = LocalDateTime.now().withNano(0).minusDays(6);
		
		//1.查询出需要数据清理的供应商
		Set<String> vendors = partyClientUtils.getAutoIntegrateQtyVendorIds(true);
		
		Sort sort = new Sort(Direction.ASC,"_id");
		PageRequest pageRequest = new PageRequest(0,1000,sort);
		vendors.stream().forEach(v->{
			int processedNum = 0;
			int totalCount = 0;
			//lastId用于循环分批处理
			String lastId = StringUtils.EMPTY;
			processPriceExpiredDatas(v, time, lastId, totalCount, processedNum, collectionName,pageRequest);
		});
	}
	
	@Async
	public Future<Void> processPriceExpiredDatas(String supplierId,LocalDateTime time,String lastId ,int totalCount,int processedNum,String collectionName,PageRequest pageRequest ){
		JSONObject paramJson = new JSONObject();
		Document greatThan = new Document();
		greatThan.put("$gt", lastId);
		
		paramJson.put("_id", greatThan);
		paramJson.put("vendorId", supplierId);
		
		JSONObject lessUpdateDate = new JSONObject();
		lessUpdateDate.put("$lt", String.valueOf(Timestamp.valueOf(time).getTime()));
		paramJson.put(UPDATED_TIME_MILLIS_FIELD_NAME, lessUpdateDate);
		/*paramJson.put("priceStatus", "expired");
		Document gtTime = new Document();
		gtTime.put("$lt", String.valueOf(Timestamp.valueOf(time).getTime()));
		paramJson.put("expiryDate",gtTime);*/
		
		Document paramDoc = new Document();
		paramDoc.put("_id", greatThan);
		paramDoc.put("vendorId", supplierId);
		paramDoc.put(UPDATED_TIME_MILLIS_FIELD_NAME, lessUpdateDate);
		MongoCursor<Document>  mongoCursor = pc.getCollection().find(paramDoc).limit(1000).iterator();
		
		//List<Product> products = productRepository.findListByPage(paramJson,pageRequest);
		List<Product> products = new ArrayList<>();
		if(!mongoCursor.hasNext()){
			logger.info("供应商：{}无需要清理的数据!",supplierId);
			return new AsyncResult<>(null);
		}else{
		 while(mongoCursor.hasNext()){
            	try {
            		Document doc = mongoCursor.next();
        			Product product = new Product();
        			product.setId(doc.getString("_id"));
        			product.setExpiryDate(doc.getString("expiryDate"));
        			product.setPriceStatus(doc.getString("priceStatus"));
        			products.add(product);
				} catch (Exception e) {
					logger.info("error is e:{}",e);
				}
            }
		}
		totalCount += products.size();
		//标记最后处理的id
		Product lastProduct = products.get(products.size()-1);
		lastId = lastProduct.getId();
		
		List<Product> waitForDeletes = new ArrayList<>();
		for (Product product : products) {
			String expiryDateStr = product.getExpiryDate();
			//过期时间超过一个月的
			if(StringUtils.isNotEmpty(expiryDateStr) && Long.valueOf(expiryDateStr) < Timestamp.valueOf(time).getTime() && "expired".equals(product.getPriceStatus())){
				waitForDeletes.add(product);
			}
		}
		
		//垃圾数据存入临时表
		waitForDeletes.stream().forEach( tempProduct -> {
			mongoTemplate.save(tempProduct,collectionName);
		});
		productRepository.delete(waitForDeletes);
		processedNum += waitForDeletes.size();
		logger.info("已清理供应商：{} 价格失效的数据：{}/{}条!",supplierId,processedNum,totalCount);
		paramJson.clear();
		paramJson = null;
		waitForDeletes.clear();
		waitForDeletes = null;
		
		processPriceExpiredDatas(supplierId, time, lastId, totalCount, processedNum, collectionName,pageRequest);
		return new AsyncResult<>(null);
	}
	
	/**
	 * 合并专属特价文案字段
	 * @param productVos
	 */
	@Async
	public Future<Void> mergeSpecialOfferTextAsync(List<ProductVo> productVos){
		Map<String, List<String>> specialOfferProductIdMap = specialOfferProductIdCacheOps.entries(SPECIAL_OFFER_PRODUCT_ID_CACHE_NAME);
		Map<String, List<String>> specialOfferProductRuleMap = specialOfferProductRuleCacheOps.entries(SPECIAL_OFFER_PRODUCT_RULE_CACHE_NAME);
		Map<String, Map<String,String>> specialOfferTextMap = specialOfferTextCacheOps.entries(SPECIAL_OFFER_TEXT_CACHE_NAME);
		productVos.stream().forEach(productVo -> {
			//策略设置为All不进行后续查找
			String vendorId = productVo.getVendorId();
			Map<String,String> map = specialOfferTextMap.get(vendorId);
			if(map != null && "ALL".equals(map.get("type"))){
				productVo.setSpecialOfferText(map.get("ruleText"));
				return;
			}
			//策略不生效
			if(map != null && "INOPERATIVE".equals(map.get("type"))){
				return;
			}
			
			List<String> specialOfferProductId = specialOfferProductIdMap.get(productVo.getId());
			String ruleCacheKey = null;
			
			if(CollectionUtils.isNotEmpty(specialOfferProductId)){
				ruleCacheKey = vendorId;
			}else{
				
				
				String warehouse = productVo.getSourceId() == null ? "*" : productVo.getSourceId();
				Integer brand = productVo.getSpu().getManufacturerId();
				Map<String, String> cateMap = priceQueryManager.getCategoriesMap(productVo.getSpu().getCategories());
				String cate1 = cateMap.get("cate1");
				String cate2 = cateMap.get("cate2");
				String cate3 = cateMap.get("cate3");
				
				List<String> canNoneParams = new ArrayList<>();
				canNoneParams.add(vendorId);
				canNoneParams.add(brand != null?brand.toString():"*");
				canNoneParams.add(warehouse);
				canNoneParams.add(cate1);
				canNoneParams.add(cate2);
				canNoneParams.add(cate3);//可以为NONE
				
				List<String> canNotNoneParams = new ArrayList<>();
				List<String> keys = getKeyList(canNoneParams,canNotNoneParams);
				if(CollectionUtils.isNotEmpty(keys)){
					for (String key : keys) {
						List<String> values = specialOfferProductRuleMap.get(key);
						if(CollectionUtils.isNotEmpty(values)){
							ruleCacheKey = vendorId;
							break;
						}
					}
				}
			}
			if(map != null && StringUtils.isNotEmpty(ruleCacheKey)){
				productVo.setSpecialOfferText(map.get("ruleText"));
			}
		});
		return new AsyncResult<>(null);
	}
	
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
				firstCantNoneParams.set(noneIndex, "*");
				
			if(j == canNoneParams.size()){
				firstCantNoneParams.set(noneIndex+1, "*");
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
				key.append("/");
			key.append(fc);
		});
		noneParams.stream().forEach( np -> {
			if(key.length()>0)
				key.append("/");
			key.append("*");
		});
		
		lastCantNoneParams.stream().forEach(lcnp -> {
			if(key.length()>0)
				key.append("/");
			key.append(lcnp);
		});
		return key.toString();
	}
	
	/**
	 * 搜索根据关键词获取联想结果
	 * @param keyword
	 * @return
	 * @since 2017年6月14日
	 * @author zr.wanghong
	 */
	public List<String> associaWord(String keyword){
		return inventorySearchManager.serachCompletion(keyword,10);
	}
	
	/**
	 * 搜索页你是不是想找的接口
	 * @return 联想结果
	 */
	@Async
	public Future<List<String>> searchRecommondAsync(String keyword){
		if(StringUtils.isEmpty(keyword)){
			return new AsyncResult<>(Collections.emptyList());
		}
		//1.根据型号联想
		List<String> result = this.associaWord(keyword);
		if(CollectionUtils.isNotEmpty(result) && result.size()>=10){
			return new AsyncResult<>(result.subList(0, 10));
		}
		//2.根据品牌联想
		List<ProductBrand> brands = brandManager.findByAilas(keyword, "N", FuzzySearchType.RIGHT_FUZZY);
		int count = CollectionUtils.isEmpty(result)?0:result.size();
		int needMergeNum =  10-count;
		int brandsSize =  CollectionUtils.isEmpty(brands)?0:brands.size();
		needMergeNum = needMergeNum>brandsSize?brandsSize:needMergeNum;
		
		for (int i = 0;CollectionUtils.isNotEmpty(brands) && needMergeNum>0 && i < needMergeNum; i++) {
			String brandName = brands.get(i).getBrandName();
			result.add(brandName);
		}
		
		//3.品牌补不足10个，根据分类补足
		List<ProductCategory> categories = categoryManager.fuzzyMatch(keyword, FuzzySearchType.RIGHT_FUZZY);
		count = CollectionUtils.isEmpty(result)?0:result.size();
		needMergeNum = 10-count;
		int categoriesSize =  CollectionUtils.isEmpty(categories)?0:categories.size();
		needMergeNum = needMergeNum>categoriesSize?categoriesSize:needMergeNum;
		
		for (int i = 0; CollectionUtils.isNotEmpty(categories) && needMergeNum>0 &&i < needMergeNum; i++) {
			String cateName = categories.get(i).getName();
			result.add(cateName);
		}
		return new AsyncResult<>(result);
	}
	
}