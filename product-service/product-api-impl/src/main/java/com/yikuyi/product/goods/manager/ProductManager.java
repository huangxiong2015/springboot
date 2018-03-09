package com.yikuyi.product.goods.manager;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.jasig.inspektr.audit.annotation.Audit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.springboot.config.ObjectMapperHelper;
import com.framework.springboot.utils.AuthorizationUtil;
import com.framewrok.springboot.web.RequestHelper;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.ictrade.enterprisematerial.InventorySearchManager;
import com.ictrade.tools.export.CsvFilePrinter;
import com.ictrade.vo.DocumentVo;
import com.ictrade.vo.EsResult;
import com.ictrade.vo.SkuDeleteVo;
import com.ictrade.vo.SkuSearchCondition;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.yikuyi.activity.model.Activity;
import com.yikuyi.activity.model.ActivityPeriods;
import com.yikuyi.activity.model.ActivityProduct;
import com.yikuyi.activity.vo.ActivityProductVo;
import com.yikuyi.activity.vo.ActivityVo;
import com.yikuyi.basedata.ShipmentClientBuilder;
import com.yikuyi.basedata.common.model.Currency;
import com.yikuyi.basedata.resource.UomConversionClient;
import com.yikuyi.brand.model.ProductBrand;
import com.yikuyi.category.vo.ProductCategoryParent;
import com.yikuyi.material.vo.MaterialVo;
import com.yikuyi.material.vo.MaterialVo.MaterialVoType;
import com.yikuyi.message.MessageClientBuilder;
import com.yikuyi.message.mail.vo.MailInfoVo;
import com.yikuyi.message.task.AsyncTaskInfo;
import com.yikuyi.party.PartyClientBuilder;
import com.yikuyi.party.facility.model.Facility;
import com.yikuyi.party.supplier.SupplierMailVo;
import com.yikuyi.party.supplier.SupplierVo;
import com.yikuyi.party.vendor.vo.PartyProductLineVo;
import com.yikuyi.product.activity.bll.ActivityManager;
import com.yikuyi.product.brand.manager.BrandManager;
import com.yikuyi.product.category.manager.CategoryManager;
import com.yikuyi.product.document.bll.DocumentLogManager;
import com.yikuyi.product.externalclient.PartyClientUtils;
import com.yikuyi.product.goods.dao.ProductClient;
import com.yikuyi.product.goods.dao.ProductRepository;
import com.yikuyi.product.goods.dao.ProductStandExtendRepository;
import com.yikuyi.product.goods.dao.ProductStandRepository;
import com.yikuyi.product.material.bll.MaterialManager;
import com.yikuyi.product.model.Product;
import com.yikuyi.product.model.ProductImage;
import com.yikuyi.product.model.ProductPrice;
import com.yikuyi.product.model.ProductPriceLevel;
import com.yikuyi.product.model.ProductStand;
import com.yikuyi.product.model.ProductStandExtend;
import com.yikuyi.product.model.ProductUtils;
import com.yikuyi.product.model.Stock;
import com.yikuyi.product.rule.delivery.manager.LeadTimeManager;
import com.yikuyi.product.strategy.cache.PackageMailCacheManager;
import com.yikuyi.product.vo.ExcelTemplate;
import com.yikuyi.product.vo.ProductPageable;
import com.yikuyi.product.vo.ProductRequest;
import com.yikuyi.product.vo.ProductVo;
import com.yikuyi.product.vo.RawData;
import com.yikuyi.rule.delivery.vo.ProductInfo;
import com.yikuyi.rule.mov.vo.MovInfo;
import com.yikuyi.rule.price.PriceInfo;
import com.yikuyi.strategy.vo.LimitedPurchaseVo;
import com.yikuyi.transaction.TransactionClient;
import com.yikuyi.transaction.order.model.OrderItem;
import com.ykyframework.exception.BusinessException;
import com.ykyframework.exception.SystemException;
import com.ykyframework.model.IdGen;
import com.ykyframework.mqservice.sender.MsgSender;
import com.ykyframework.oss.AliyunOSSFileUploadSetting;
/**
 * 商品
 *
 * @author zr.wujiajun
 * @2016年12月7日
 */
@Service
public class ProductManager {
	private static final Logger logger = LoggerFactory.getLogger(ProductManager.class);

	/**
	 * 更新时间的属性名
	 */
	public static final String UPDATED_TIME_MILLIS_FIELD_NAME = "updatedTimeMillis";

	/**
	 * 状态属性名
	 */
	public static final String STATUS_FIELD_NAME = "status";

	@Value("${mqProduceConfig.createProductSub.topicName}")
	private String createProductTopicName;

	@Autowired
	private LeadTimeManager leadTimeManager;
	@Autowired
	private PriceQueryManager priceQueryManager;
	@Autowired
	private MovQueryManagerV2 movQueryManager;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ProductStandManager productStandManager;
	@Autowired
	private ProductStandRepository productStandRepository;

	@Autowired
	private ProductStandExtendRepository standExtendRepository;

	@Autowired
	private MongoOperations mongoOptions;
	@Value("${spring.data.mongodb.uri}")
	private String mongoUri;
	@Value("${api.party.serverUrlPrefix}")
	private String partyServerUrlPrefix;
	@Value("${api.basedata.serverUrlPrefix}")
	private String basedataServerUrlPrefix;
	@Value("${dns-prefetchs}")
	private String dnsPrefetchs;
	@Value("${config.leadTime.vendorId}")
	private String leadTimeVendorId;
	@Value("${mqConsumeConfig.crawlerSyncProduct.topicName}")
	private String crawlerSyncProductTopicName;
	@Value("${mqConsumeConfig.invalidProduct.topicName}")
	private String invalidProductTopicName;

	@Value("${crawler.support.vendorId}")
	private String crawlerSupportVendorId;

	@Autowired
	private MsgSender producer;
	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private MsgSender msgSender;

	@Autowired
	private MaterialManager materialManager;

	@Autowired
	private BrandManager brandManager;

	@Autowired
	private DocumentLogManager documentLogManager;

	@Autowired
	private CategoryManager categoryManager;

	@Value("${mqProduceConfig.createProductSub.topicName}")
	private String createProductTopic;

	@Value("${mqProduceConfig.syncElasticsearchProduct.topicName}")
	private String syncElasticsearchProductTopicName;

	@Value("${mqProduceConfig.parseImportFileSub.topicName}")
	private String parseImportFileTopicName;

	@Autowired
	private PartyClientUtils partyClientUtils;

	@Autowired
	@Qualifier(value="restTemplate")
	private RestTemplate restTemplate;

	@Autowired
	private PartyClientBuilder partyClientBuilder;

	@Autowired
	private AuthorizationUtil authorizationUtil;

	@Autowired
	private InventorySearchManager inventorySearchManager;

	@Autowired
	private SearchAsyncManager searchAsyncManager;

	@Autowired
	private SearchManager searchManager;

	//注入HashOperations对象
	@Resource(name = "redisTemplate")
	private HashOperations<String,String,List<ActivityProductVo>> productCacheOps;

	//注入HashOperations对象
	@Resource(name = "redisTemplate")
	private HashOperations<String,String,List<ActivityVo>> activitSupplierOps;

	private com.google.common.cache.Cache<String, Double> exchangeRateCache = CacheBuilder.newBuilder().initialCapacity(128).expireAfterWrite(5, TimeUnit.MINUTES).build();

	@Autowired
	private ExcelExportManager excelExportManager;

	@Autowired
	private MessageClientBuilder messageClientBuilder;


	@Autowired
	private TransactionClient transactionClientBuilder;

	@Autowired
	private PriceQueryAsyncManager priceQueryAsyncManager;

	@Autowired
	private ProductClient pc;

	/**
	 * 所有商品的Map结构Key
	 */
	public static final String ACTIVITY_PRODUCT_CACHE_MAP = "activityProductCache";

	/**
	 * 所有活动供应商策略Key
	 */
	public static final String ACTIVITY_SUPPLIER_CACHE_MAP = "activitySupplierCache";


	// 活动预览的商品缓存
	public static final String ACTIVITY_PREVIEW_PRODUCT_CACHE = "activityPreviewProductCache";

	// 活动预览供应商规则缓存
	public static final String ACTIVITY_PREVIEW_SUPPLIER_CACHE = "activityPreviewSupplierCache";


	/**
	 * 时间格式
	 */
	private SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm");

	/**
	 * 日期格式
	 */
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 最多查询20W条数据
	 */
	private static final Integer LIMIT_QUERY_CNT = 200000;

	@Autowired
	@Qualifier(value = "aliyun.OSSFileUploadSetting")
	private AliyunOSSFileUploadSetting ossFileUploadSetting;

	@Value("${downloadFtp.vendorId.rs}")
	private String rsVendorId;

	@Value("${mqConsumeConfig.sendMsgAndEmail.topicName}")
	private String sendMsgAndEmailTopicName;
	/**
	 * 可信度高的供应商
	 */
	@Value("${credit.high.vendorId}")
	private String creditHighVendorIds;

	@Autowired
	private  VendorWeightManager vendorWeightManager;

	private static final ObjectMapper mapper = ObjectMapperHelper.configeObjectMapper(new ObjectMapper());

	@Autowired
	private ProductAsyncManager productAsyncManager;

	@Autowired
	private ShipmentClientBuilder shipmentClientBuilder;
	@Autowired
	private PackageMailCacheManager packageMailCacheManager;

	/**
	 * 统计产品更新数
	 */
	public long getCount(String time){
		Query query = new Query();
		Criteria criteria = new Criteria();
		criteria.and("updatedTimeMillis").gte(time);
		query.addCriteria(criteria);
		return  mongoOptions.count(query, Product.class);
	}

	/**
	 * 查询商品基本信息,不含交期,实时价格等
	 *
	 * @author zr.wujiajun
	 */
	public List<Product> findBasicInfo(List<String> ids) {
		List<Product> result = (List<Product>) productRepository.findAll(ids);
		//去掉状态不为1的,1为有效，2为限购失效后状态
		List<Product> newResult = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(result)){
			for(int i=result.size()-1;i>=0;i--){
				Product p= result.get(i);
				if(p.getStatus() != null && (p.getStatus().intValue() ==1 || p.getStatus().intValue() ==2 )){
					newResult.add(p);
				}				
			}			
		}		
		if(CollectionUtils.isEmpty(newResult)){
			return Collections.emptyList();
		}
		Set<String> supplierIds = newResult.stream().map(Product::getVendorId).collect(Collectors.toSet());
		Map<String,SupplierVo> suppliers = partyClientBuilder.supplierClient().getSupplierSimple(supplierIds);
		//查询供应商
		for (Product tempPo : newResult) {
			//合并供应商名称
			SupplierVo tempVo = suppliers.get(tempPo.getVendorId());
			if(null != tempVo){
				//是不显示是否显示真实供应商标识
				tempPo.setShowVendor(tempVo.isShowName());
				tempPo.setVendorName(tempVo.getDisplayName());
				tempPo.setIsVendorDetail(tempVo.isVendorDetail()?"Y":"N");
				//设置是否校验供应商mov和商品mov标识
				tempPo.setVendorMovStatus(tempVo.getVendorMovStatus());
				tempPo.setSkuMovStatus(tempVo.getSkuMovStatus());
				Map<String,Facility> facilities = tempVo.getFacilityIdMap();
				if(facilities.containsKey(tempPo.getSourceId()) && "Y".equalsIgnoreCase(facilities.get(tempPo.getSourceId()).getIsShowaFacility())){
					tempPo.setSourceName(facilities.get(tempPo.getSourceId()).getFacilityName());
				}
				//设置供应商的名称，而不是编码
				tempPo.setOldVendorName(tempVo.getSupplierName());
			}else{
				//没有供应商名称则设置为空
				tempPo.setVendorName("");
				tempPo.setVendorDetailsLink("");
			}
			
			//处理最小起订量
			this.handleMinimumQuantity(tempPo);
			
			//设置交货地币种类型
			tempPo.setCurrencyCode(movQueryManager.getCurrencyType(tempPo));
			
			//处理图片
			List<ProductImage> spuImage = tempPo.getSpu().getImages();
			
			//图片地址加上前缀后返回
			List<ProductImage> spuImageNew = handleImageUrl(spuImage);
			tempPo.getSpu().setImages(spuImageNew);
			
			if(StringUtils.isNotEmpty(tempPo.getExpiryDate())){
				Date expiryDate = new Date(Long.valueOf(tempPo.getExpiryDate()));
				tempPo.setExpiryDate(dateFormat.format(expiryDate));
			}
		}
		return newResult;
	}

	/**
	 * 处理商品最小起订量
	 * @param product 商品实体
	 */
	public void handleMinimumQuantity(Product product){
		int minQty = Integer.MAX_VALUE;//默认取最大阶梯数量
		int defaultQty = minQty;
		int qty;
		//如果存在阶梯价格数量 就取阶梯价数量中数量最少的。如果没有阶梯数量 就取本身的
		// 是否存在梯度
		boolean hasBreakLevel = false;
		for(ProductPrice price : product.getPrices()){
			List<ProductPriceLevel> levels = price.getPriceLevels();
			if(CollectionUtils.isEmpty(levels)){
				continue;
			}
			for(ProductPriceLevel level:levels){
				if(level.getBreakQuantity() !=null){
					qty = level.getBreakQuantity().intValue();
					hasBreakLevel = true;
				}else{
					qty = 0;
				}
				//比较数量，如果不是最小，则将当前数量赋予最小数量
				if(qty!=0&&minQty>qty){
					minQty = qty;
				}
			}
		}
		//如果没有循环，则最小数量为1
		if(minQty == defaultQty){
			minQty = 1;
		}
		if(CollectionUtils.isNotEmpty(product.getPrices()) && hasBreakLevel){
			product.setMinimumQuantity(minQty);
		}
	}
	
	/**
	 * 字符串转换为double
	 * @param str
	 * @return
	 * @since 2017年1月9日
	 * @author zr.wujiajun@yikuyi.com
	 */
	public int parseInt(String str){
		int qty = Integer.MAX_VALUE;
		try{
			qty = (int)Double.parseDouble(str);
		}catch(Exception e){
			logger.error("parse Exception",e);
		}
		return qty;
	}

	/**
	 * 合并活动信息
	 * @param productVos 商品集合
	 * @param isPreview 是否预览（Y/N）
	 */
	public void mergeActivity(List<ProductVo> productVos,String isPreview){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if(CollectionUtils.isEmpty(productVos)){
			return;
		}
		Map<String,List<ActivityProductVo>> activityProductVoMap = null;
		if("Y".equals(isPreview)){
			activityProductVoMap = productCacheOps.entries(ACTIVITY_PREVIEW_PRODUCT_CACHE);
		}else{
			activityProductVoMap = productCacheOps.entries(ACTIVITY_PRODUCT_CACHE_MAP);
		}
		for (ProductVo productVo : productVos) {
			//从缓存中取商品的活动信息
			List<ActivityProductVo> activityProductVos = activityProductVoMap.get(productVo.getId());
			ActivityProductVo curentAcProductVo = null;
			if(CollectionUtils.isNotEmpty(activityProductVos)){
				//迭代依次获取活动，秒杀和促销的活动依次按顺序存取
				for (ActivityProductVo acProductVo : activityProductVos) {
					if(ActivityManager.ACTIVITY_SPIKE_TYPE.equals(acProductVo.getActivityType())){
						Date nowDate = new Date();
						String nowTimeString = timeFormat.format(nowDate);
						int nowTimeInt = Integer.parseInt(nowTimeString);
						String startTime = acProductVo.getStartTime();
						String endTime = acProductVo.getEndTime();
						int startTimed = Integer.MAX_VALUE;
						int endTimed = 0;
						try {
							startTimed = Integer.parseInt(startTime.replaceAll(":", ""));
							endTimed = Integer.parseInt(endTime.replaceAll(":", ""));
						} catch (NumberFormatException e) {
							logger.error("活动时间区间的开始时间和结束时间格式不正确："+startTime+" "+endTime,e);
						}
						//是否在时间区段内的活动
						if(nowTimeInt>=startTimed && nowTimeInt<endTimed){
							curentAcProductVo = acProductVo;
							break;
						}
					}else{
						//不是秒杀，即为促销，取最后一个最新的活动,判断活动如果不在有效区间内，则从后往前依次获取
						//curentAcProductVo = activityProductVos.get(activityProductVos.size()-1);
						curentAcProductVo = findPromotionActivity(activityProductVos, curentAcProductVo);
						break;
					}
				}
			}
			if(null != curentAcProductVo){
				setProductVoActivityInfo(format, productVo, curentAcProductVo);
			}else{

				//没有商品的活动信息，获取供应商活动信息,供应商活动只有打折没有上传价
				ActivityVo activityVo = null;
				//isPreview Y:启用预览 N:不启用预览
				if("Y".equals(isPreview)){
					activityVo = findSupplierActityCache(productVo,ACTIVITY_PREVIEW_SUPPLIER_CACHE);
				}else{
					activityVo = findSupplierActityCache(productVo,ACTIVITY_SUPPLIER_CACHE_MAP);
				}
				if(null != activityVo){
					curentAcProductVo = new ActivityProductVo();
					curentAcProductVo.setStartTime(activityVo.getStartTime());
					curentAcProductVo.setEndTime(activityVo.getEndTime());
					curentAcProductVo.setActivityId(activityVo.getActivityId());
					curentAcProductVo.setIconStatus(activityVo.getIconStatus());
					curentAcProductVo.setIconScenes(activityVo.getIconScenes());
					curentAcProductVo.setActivityType(activityVo.getType());
					curentAcProductVo.setPromoDiscount(activityVo.getPromoDiscount());
					curentAcProductVo.setPromoDiscountStatus(activityVo.getPromoDiscountStatus());
					curentAcProductVo.setIsSystemQty(activityVo.getIsSystemQty());
					//促销标志
					curentAcProductVo.setPromotionFlag(activityVo.getPromotionFlag());
					setProductVoActivityInfo(format, productVo, curentAcProductVo);
					productVo.setActivityProductVo(curentAcProductVo);
				}
			}

		}
	}

	/**
	 * 从缓存集合中查找模块生效的促销活动
	 * 1.依次从最后的往前找，并判断模块生效时间是否生效
	 * 2.迭代中当前活动如果是秒杀类型则结束查找
	 * @param activityProductVos
	 * @param curentAcProductVo
	 * @return
	 */
	private ActivityProductVo findPromotionActivity(List<ActivityProductVo> activityProductVos,
			ActivityProductVo curentAcProductVo) {
		for (int index = activityProductVos.size()-1; index >= 0; index--) {
			ActivityProductVo lastActProductVo = activityProductVos.get(index);
			if(ActivityManager.ACTIVITY_SPIKE_TYPE.equals(lastActProductVo.getActivityType())){
				break;
			}
			String moduelStartTime = lastActProductVo.getModuelStartTime();
			String moduelEndTime = lastActProductVo.getModuelEndTime();
			if(StringUtils.isEmpty(moduelStartTime) || StringUtils.isEmpty(moduelEndTime)){
				curentAcProductVo = lastActProductVo;
				break;
			}
			long currentTime = System.currentTimeMillis();
			if(currentTime>=Long.valueOf(moduelStartTime) && currentTime < Long.valueOf(moduelEndTime)){
				curentAcProductVo = lastActProductVo;
				break;
			}

		}
		return curentAcProductVo;
	}

	/**
	 * 查询供应商的活动缓存信息
	 * @param productVo
	 * @param cacheRoomName
	 * @return
	 */
	public ActivityVo findSupplierActityCache(ProductVo productVo,String cacheRoomName){
		//穷举出所有的供应商活动缓存的key
		/*
		 	供应商ID_*
			供应商ID_制造商ID_*
			供应商ID_制造商ID_大类_小类_*
			供应商ID_大类_小类_*
			制造商ID_*
			制造商ID_大类_小类_*
			大类_小类_*
		 */
		ActivityVo result = null;
		List<String> hashKeys = new ArrayList<>();
		String vendorId = productVo.getVendorId();
		String manufactureId = String.valueOf(productVo.getSpu().getManufacturerId());
		Map<String, String> cateMap = priceQueryManager.getCategoriesMap(productVo.getSpu().getCategories());
		String cate1 = cateMap.get("cate1");
		String cate2 = cateMap.get("cate2");

		StringBuilder cacheKey = new StringBuilder();
		cacheKey.append(vendorId).append("_*");
		hashKeys.add(cacheKey.toString());
		cacheKey.delete(0, cacheKey.length());

		cacheKey.append(vendorId).append("_"+manufactureId+"_*");
		hashKeys.add(cacheKey.toString());
		cacheKey.delete(0, cacheKey.length());

		cacheKey.append(vendorId).append("_"+manufactureId+"_"+cate1+"_"+cate2+"_*");
		hashKeys.add(cacheKey.toString());
		cacheKey.delete(0, cacheKey.length());

		cacheKey.append(vendorId).append("_"+cate1+"_"+cate2+"_*");
		hashKeys.add(cacheKey.toString());
		cacheKey.delete(0, cacheKey.length());

		cacheKey.append(manufactureId).append("_*");
		hashKeys.add(cacheKey.toString());
		cacheKey.delete(0, cacheKey.length());

		cacheKey.append(manufactureId).append("_"+cate1+"_"+cate2+"_*");
		hashKeys.add(cacheKey.toString());
		cacheKey.delete(0, cacheKey.length());

		cacheKey.append(cate1+"_"+cate2+"_*");
		hashKeys.add(cacheKey.toString());
		cacheKey.delete(0, cacheKey.length());

		List<ActivityVo> acVos = new ArrayList<>();
		for (String hashKey : hashKeys) {
			List<ActivityVo> activityVos = activitSupplierOps.get(cacheRoomName,hashKey);
			ActivityVo activityVo = null;
			if(CollectionUtils.isNotEmpty(activityVos)){
				activityVo = activityVos.get(activityVos.size()-1);
			}
			if(activityVo!=null){
				acVos.add(activityVo);
			}
		}

		//按时间排序供应商活动策略，最新的排前面
		Collections.sort(acVos,(ActivityVo ac1, ActivityVo ac2) -> {
		  Long date1 = ac1.getLastUpdateDate().getTime();
		  Long date2 = ac2.getLastUpdateDate().getTime();
		  Long date3 = date1 - date2;
		  return date3.intValue();
        });
		//有活动策略，则取一个最新的
		if(CollectionUtils.isNotEmpty(acVos)){
			result = acVos.get(0);
		}
		//判断模块是否在生效时间内
		if(result!=null){
			long currentTime = System.currentTimeMillis();
			String moduelStartTime = result.getModuelStartTime();
			String moduelEndTime = result.getModuelEndTime();
			if(StringUtils.isEmpty(moduelStartTime) || StringUtils.isEmpty(moduelEndTime)){
				return result;
			}
			//不在时间范围类，返回空
			if(currentTime < Long.valueOf(moduelStartTime) || currentTime > Long.valueOf(moduelEndTime)){
				return null;
			}
		}
		return result;
	}

	/**
	 * 设置活动信息
	 * @param format
	 * @param productVo
	 * @param curentAcProductVo
	 */
	private void setProductVoActivityInfo(SimpleDateFormat format, ProductVo productVo,
			ActivityProductVo curentAcProductVo) {
		String systemDate = format.format(new Date());
		if(StringUtils.isNotEmpty(curentAcProductVo.getStartTime())){
			productVo.setStartTime(systemDate + " " + curentAcProductVo.getStartTime() + ":00");
		}
		if(StringUtils.isNotEmpty(curentAcProductVo.getEndTime())){
			productVo.setEndTime(systemDate + " " + curentAcProductVo.getEndTime() + ":00");
		}
		if(StringUtils.isNotEmpty(curentAcProductVo.getTitle())){
			productVo.setTitle(curentAcProductVo.getTitle());
		}

		//是否使用系统库存(Y/N),为N时使用活动的库存
		if(StringUtils.isNotEmpty(String.valueOf(curentAcProductVo.getIsSystemQty()))
				&& "N".equals(String.valueOf(curentAcProductVo.getIsSystemQty()))){
			Integer qty = curentAcProductVo.getQty() == null ? curentAcProductVo.getTotalQty() : curentAcProductVo.getQty();
			if(qty!=null){
				productVo.setQty(Long.valueOf(qty));
			}
		}
		if(StringUtils.isNotEmpty(curentAcProductVo.getSubTitle())){
			productVo.setSubTitle(curentAcProductVo.getSubTitle());
		}
		productVo.setActivityId(curentAcProductVo.getActivityId());
		if(StringUtils.isNotEmpty(String.valueOf(curentAcProductVo.getIsSystemQty()))){
			productVo.setIsSystemQty(String.valueOf(curentAcProductVo.getIsSystemQty()));
		}
		if(StringUtils.isNotEmpty(String.valueOf(curentAcProductVo.getIconStatus()))){
			productVo.setIconStatus(String.valueOf(curentAcProductVo.getIconStatus()));
		}
		productVo.setIconScenes(curentAcProductVo.getIconScenes());
		productVo.setPeriodsId(curentAcProductVo.getPeriodsId());
		List<String> imageList = mergeImage(curentAcProductVo);
		if(CollectionUtils.isNotEmpty(imageList)){
			productVo.setImageList(imageList);
		}

		//增加 商品描述  add by zr.aoxianbing@yikuyi.com
		if(StringUtils.isNotEmpty(curentAcProductVo.getDescription())){
			productVo.setDescription(curentAcProductVo.getDescription());
		}
		productVo.setActivityType(curentAcProductVo.getActivityType());
		productVo.setActivityProductVo(curentAcProductVo);

		productVo.setPromotionFlag(curentAcProductVo.getPromotionFlag());
	}

	/**
	 * 是否需要合并价格交期
	 * @param productVos
	 * @return false不需要，true需要
	 */
	public boolean isNeedMergepriceAndLeadTime(ProductVo productVo){
		if(productVo != null){
			Long currentTime = System.currentTimeMillis();
			Long expirationTime = productVo.getExpirationTime();
			if(expirationTime != null && expirationTime != 0 && expirationTime < currentTime){
				return false;
			}
		}
		return true;
	}
	/**
	 * 查询商品全部信息,包含交期,实时价格等
	 */
	public List<ProductVo> findFullInfo(List<String> ids) {
		logger.info("ProductManager---method:findFullInfo");
		List<String> idList = new ArrayList<>();
		List<Product> productList = findBasicInfo(ids);
		List<ProductVo> result = new ArrayList<>();
		if (CollectionUtils.isEmpty(productList)) {
			return Collections.emptyList();
		}
		for (Product p : productList) {
			ProductVo vo = new ProductVo();
			BeanUtils.copyProperties(p,vo );
			idList.add(p.getId());

			//查询标题和副标题
			setSpuExtendInfo(p, vo);
			//判断限购是否有效
			setIsLimitedPurchaseFlag(vo);
			result.add(vo);
		}

		//合并专属特价文案字段
		this.mergeSpecialOfferText(result);
		//合并包邮信息
		this.mergeFreeShipping(result);

		//判断商品是否在做活动，如果是则合并活动信息
		mergeActivity(result, "N");
		//查询交期、价格后并合并交期与价格
		mergePriceAndLeadTime(idList,ids,result);
		//合并MOV信息
		mergeMovInfo(result);

		ProductUtils.setVendorPackaging(result,rsVendorId);
		//发送mq
		sendSyncProductMQ(productList);
		return result;
	}

	/**
	 * 设置spu相关的字段
	 * @param p
	 * @param vo
	 */
	private void setSpuExtendInfo(Product p, ProductVo vo) {
			if(p.getSpu() != null && StringUtils.isNotEmpty(p.getSpu().getId())){
				ProductStandExtend pse = standExtendRepository.findOne(p.getSpu().getId());
				if(pse!=null){
					vo.setTitle(pse.getMaterialName());
					vo.setSubTitle(pse.getPromotionWord());
					//细节图
					List<ProductImage> iamges = pse.getDetailImages();
					if(CollectionUtils.isNotEmpty(iamges)){
						List<String> imageList = iamges.stream().map(ProductImage::getUrl).collect(Collectors.toList());
						//获取大图
						if(CollectionUtils.isNotEmpty(vo.getSpu().getImages())){
							vo.getSpu().getImages().stream().forEach(image -> {
								if("large".equals(image.getType())){
									imageList.add(0,image.getUrl());
								}
							});
						}
						vo.setImageList(imageList);
					}
					//物料描述
					vo.setDescription(pse.getMaterialDetail());
				}
			}
		}

	/**
	 * 设置限购是否过期的标识
	 * @param vo
	 */
	private void setIsLimitedPurchaseFlag(ProductVo vo) {
		LimitedPurchaseVo limitedPurchaseVo = vo.getLimitedPurchaseInfo();
		if(limitedPurchaseVo!=null){
			long nowTime = System.currentTimeMillis();
			long startTime = limitedPurchaseVo.getStartTime()!=null ?Long.valueOf(limitedPurchaseVo.getStartTime()):0;
			long endTime = limitedPurchaseVo.getEndTime()!=null ? Long.valueOf(limitedPurchaseVo.getEndTime()):0;
			//是否在时间区段内的活动
			if(nowTime>=startTime && nowTime<endTime){
				limitedPurchaseVo.setIsLimitedPurchase("Y");
			}else{
				limitedPurchaseVo.setIsLimitedPurchase("N");
			}
		}
	}

	/**
	 * 发送爬虫同步更新单条数据的MQ
	 * @param productList
	 */
	private void sendSyncProductMQ(List<Product> productList) {
		ArrayList<String> crawlerSupportVendorIds = new ArrayList<String>(Arrays.asList(crawlerSupportVendorId.split(",")));
		for (Product p : productList) {
			if(!crawlerSupportVendorIds.contains(p.getVendorId())){
				continue;
			}
			Cache cache = cacheManager.getCache("crawlerSyncProduct");
			ValueWrapper valueWrapper = cache.get(p.getId());
			if(valueWrapper == null){
				cache.put(p.getId(), p);
				StringBuilder stringBuilder = new StringBuilder();
				if(StringUtils.contains(p.getVendorDetailsLink(), "?")){
					stringBuilder.append(p.getVendorDetailsLink()).append("&yds=m");
				}else{
					stringBuilder.append(p.getVendorDetailsLink()).append("?yds=m");
				}
				producer.sendMsg(crawlerSyncProductTopicName, stringBuilder.toString(),null);
			}
		}
	}

	/**
	 * 合并商品的MOV信息
	 * @param productVos
	 * @since 2017年8月14日
	 * @author zr.wanghong
	 */
	public void mergeMovInfo(List<ProductVo> productVos){
		if(CollectionUtils.isNotEmpty(productVos)){
			List<MovInfo> movInfos = movQueryManager.queryByEntities(productVos);
			productVos.stream().forEach( productVo -> {
				LimitedPurchaseVo limitedPurchaseVo = productVo.getLimitedPurchaseInfo();
				//如果限购商品，不合并mov信息
				if(limitedPurchaseVo!=null && "Y".equals(limitedPurchaseVo.getLimitedPurchaseFlag())){
					return;
				}
				movInfos.stream().forEach(movInfo -> {
					if(productVo.getId().equals(movInfo.getProductId())){
						productVo.setMovInfo(movInfo);
					}
				});
			});
		}
	}

	public void mergeSpecialOfferText(List<ProductVo> productVos){
		try {
			productAsyncManager.mergeSpecialOfferTextAsync(productVos).get();
		} catch (Exception e) {
			logger.error("合并专属特价文案异常,error is {}",e);
		}
	}

	/**
	 * 合并包邮信息
	 * @param productVos
	 */
	public void mergeFreeShipping(List<ProductVo> productVos){
		try {
			packageMailCacheManager.mergePackageMailInfo(productVos).get();
		} catch (Exception e) {
			logger.error("合并专属特价文案异常,error is {}",e);
		}
	}


	/**
	 * 查询交期、价格后并合并交期与价格
	 * @param idList
	 * @param ids
	 * @param result
	 * @since 2017年7月3日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public void mergePriceAndLeadTime(List<String> idList,List<String> ids,List<ProductVo> result){
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			// 查询交期
			List<ProductInfo> leadTimeList = new ArrayList<>();

			if(CollectionUtils.isNotEmpty(idList)){
				leadTimeList = leadTimeManager.getLeadTimeList(idList);
				logger.info("查询交期,参数{}"+idList);
			}
			// 合并实时价格
			List<PriceInfo> priceList = priceQueryManager.queryPriceWithActivity(result);
			logger.info("查询价格,参数{}"+ids);
			if (priceList == null) {
				priceList = new ArrayList<>();
			}

			for (ProductVo vo : result) {

				//限购商品限购过期后不合并价格，并修改状态为2
				if(!isNeedMergepriceAndLeadTime(vo)){
					//发送删除商品MQ
					vo.setStatus(2);
					vo.setPrices(Collections.emptyList());
					vo.setResalePrices(Collections.emptyList());
					updateProductInfo(vo);
					continue;
				}



				List<ProductInfo> leadTimeResult = new ArrayList<>();
				// 合并交期
				leadTimeList.stream().forEach(pi -> {
					if (vo.getId().equals(pi.getProductId())) {
						ProductInfo productInfoLead = handleLeadTime(vo, pi);//合并现货交期
						if(null!=productInfoLead){
							leadTimeResult.add(productInfoLead);
						}
						ProductInfo productInfoFactory = handleFactoryLeadTime(vo, pi);//合并排单交期
						if(null!=productInfoFactory){
							leadTimeResult.add(productInfoFactory);
						}
						ProductInfo productInfo = handleUploadLeadTime(vo, pi);//合并上传的交期(当交期策略没有设置的时候，处理上传的交期)
						if(null!=productInfo){
							leadTimeResult.add(productInfo);
						}
					}
				});
				ProductInfo productInfoResult = new ProductInfo();
				if(CollectionUtils.isNotEmpty(leadTimeResult)){
					for(ProductInfo temp : leadTimeResult){
						if(null!=temp.getProductType() && 0==temp.getProductType()){
							productInfoResult.setLeadTimeCH(temp.getLeadTimeCH());
							productInfoResult.setLeadTimeHK(temp.getLeadTimeHK());
						}
						if(null!=temp.getProductType() && 1==temp.getProductType()){
							productInfoResult.setSchedulingLeadTimeCH(temp.getSchedulingLeadTimeCH());
							productInfoResult.setSchedulingLeadTimeHK(temp.getSchedulingLeadTimeHK());
						}
						if(null==temp.getProductType()){
							if(null==productInfoResult.getLeadTimeCH()){
								productInfoResult.setLeadTimeCH(temp.getLeadTimeCH());
							}
							if(null==productInfoResult.getLeadTimeHK()){
								productInfoResult.setLeadTimeHK(temp.getLeadTimeHK());
							}
							if(null==productInfoResult.getSchedulingLeadTimeCH()){
								productInfoResult.setSchedulingLeadTimeCH(temp.getSchedulingLeadTimeCH());
							}
							if(null==productInfoResult.getSchedulingLeadTimeHK()){
								productInfoResult.setSchedulingLeadTimeHK(temp.getSchedulingLeadTimeHK());
							}
						}
					}
					vo.setRealLeadTime(productInfoResult);
				}

				// }

				// 合并价格
				priceList.stream().forEach(price -> {
					if (vo.getId().equals(price.getProductId())) {
						vo.setCostPrices(price.getCostPrices());
						vo.setPrices(price.getResalePrices());
						if (StringUtils.isNotEmpty(vo.getActivityId())) {
							vo.setOriginalResalePrices(price.getOriginalResalePrices());
						}
						//设置活动中的商品最小起订量
						setActivityMiniQty(vo, price);
						return;
					}
				});
				vo.setNowDate(format2.format(new Date()));
			}
		} catch (Exception e) {
			logger.error("批量获取商品详细信息" + e.getMessage(), e);
		}
	}

	/**
	 * 设置活动中的商品最小起订量
	 * @param vo
	 * @param price
	 */
	private void setActivityMiniQty(ProductVo vo, PriceInfo price) {
		if(StringUtils.isNotEmpty(vo.getActivityId())){
			if(!CollectionUtils.isEmpty(price.getResalePrices())){
				price.getResalePrices().forEach( resalePrice -> {
					if("CNY".equals(resalePrice.getCurrencyCode())){
						int minimumQuantity = !CollectionUtils.isEmpty(resalePrice.getPriceLevels()) ? resalePrice.getPriceLevels().get(0).getBreakQuantity().intValue():1;
						vo.setMinimumQuantity(minimumQuantity);
					}
				});
			}else{
				vo.setMinimumQuantity(1);
			}
		}
	}

	/**
	 * 按照优先规则获取最新的活动对象
	 * @param product
	 * @param effectActivities
	 * @return
	 * @since 2017年7月28日
	 * @author jik.shu@yikuyi.com
	 */
	public ActivityProduct getActivityProductVoOrdery(ProductVo product, List<Activity> effectActivities){
		Map<String,ActivityProduct> activityProdctMap = new HashMap<>();
		//从缓存中取活动信息
		Cache cache = cacheManager.getCache("activity");
		effectActivities.stream().forEach(activity ->{
			ActivityProduct pInfo = getActivityProduct(product,activity,cache);
			if(null != pInfo && !activityProdctMap.containsKey(product.getId())){
				activityProdctMap.put(product.getId(), pInfo);
			}else if(null != pInfo && activityProdctMap.containsKey(product.getId())){
				String oldType = activityProdctMap.get(product.getId()).getActivityType();
				String newType = pInfo.getActivityType();

				//先处理活动类型相同的情况，按时间优先级放入Map
				if(oldType.equals(newType)){
					Date oldLastUpdateDate = activityProdctMap.get(product.getId()).getLastUpdateDate();
					Date newLastUpdateDate = pInfo.getLastUpdateDate();
					if(newLastUpdateDate != null && oldLastUpdateDate != null && newLastUpdateDate.after(oldLastUpdateDate)){
						activityProdctMap.put(product.getId(), pInfo);
					}
				}

				//在处理活动类型不相同的情况，秒杀优先于促销
				if(ActivityManager.ACTIVITY_SPIKE_TYPE.equals(oldType)){
					//旧的是秒杀则不处理
					return;
				}else{
					//旧的是促销，新的是秒杀则覆盖
					if(ActivityManager.ACTIVITY_SPIKE_TYPE.equals(newType)){
						activityProdctMap.put(product.getId(), pInfo);
					}
				}
			}
		});
		return activityProdctMap.getOrDefault(product.getId(), null);
	}


	/**
	 * 从缓存中查询活动商品信息
	 * @param product
	 * @param activity
	 * @param cache
	 * @return
	 * @since 2017年6月27日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public ActivityProduct getActivityProduct(ProductVo product,Activity activity,Cache cache){
		//活动商品key
		StringBuilder strKey = new StringBuilder();
		strKey.append(activity.getActivityId() + "-");
		List<ActivityPeriods> periodsList = activity.getPeriodsList();
		if(CollectionUtils.isEmpty(periodsList)){
			return null;
		}
		strKey.append(activity.getPeriodsList().get(0).getPeriodsId() + "-product-");
		strKey.append(product.getId());
		ValueWrapper productActivity = cache.get(strKey.toString());
		if(productActivity != null){
			ActivityProduct temp = (ActivityProduct)productActivity.get();
			temp.setActivityType(activity.getType());

			temp.setPromoDiscountStatus(activity.getPromoDiscountStatus());
			temp.setPromoDiscount(activity.getPromoDiscount());
			temp.setIsSystemQty(activity.getIsSystemQty());
			temp.setIconStatus(activity.getIconStatus());
			temp.setIconScenes(activity.getIconScenes());

			return temp;
		}
		return null;
	}

	/**
	 * 合并活动商品中的图片
	 * @param pInfo
	 * @return
	 * @since 2017年6月27日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public List<String> mergeImage(ActivityProduct pInfo){
		List<String> imageList = new ArrayList<>();
		if(StringUtils.isNotEmpty(pInfo.getImage1())){
			imageList.add(pInfo.getImage1());
		}
		if(StringUtils.isNotEmpty(pInfo.getImage2())){
			imageList.add(pInfo.getImage2());
		}
		if(StringUtils.isNotEmpty(pInfo.getImage3())){
			imageList.add(pInfo.getImage3());
		}
		if(StringUtils.isNotEmpty(pInfo.getImage4())){
			imageList.add(pInfo.getImage4());
		}
		if(StringUtils.isNotEmpty(pInfo.getImage5())){
			imageList.add(pInfo.getImage5());
		}
		return imageList;
	}


	/**
	 * 查询商品全部信息,包含交期,实时价格等
	 */
	public List<ProductVo> findFullInfoV2(List<String> ids) {
		logger.info("ProductManager---method:findFullInfo");
		List<Product> productList = findBasicInfo(ids);
		List<ProductVo> result = new ArrayList<>();

		if (productList == null) {
			return new ArrayList<>();
		}
		for (Product p : productList) {
			ProductVo vo = new ProductVo();
			BeanUtils.copyProperties(p ,vo);
			result.add(vo);
		}

		try {
			// 查询交期
			List<ProductInfo> leadTimeList = leadTimeManager.getLeadTimeList(ids);
			logger.info("查询交期,参数{}"+ids);
			if (leadTimeList == null) {
				leadTimeList = new ArrayList<>();
			}
			// 合并实时价格
			List<PriceInfo> priceList = priceQueryManager.queryPrice(ids);
			if (priceList == null) {
				priceList = new ArrayList<>();
			}
			logger.info("查询价格,参数{}"+ids);
			for (ProductVo vo : result) {
				List<ProductVo> leadTimeResult = new ArrayList<>();
				//合并交期
				leadTimeList.stream().forEach(pi -> {
					if (vo.getId().equals(pi.getProductId())) {
						ProductVo productVoLead = handleLeadTimeShow(vo, pi);//合并现货交期
						if(null!=productVoLead){
							leadTimeResult.add(productVoLead);
						}
						ProductVo productVoFactory = handleFactoryLeadTimeShow(vo, pi);//合并排单交期
						if(null!=productVoFactory){
							leadTimeResult.add(productVoFactory);
						}
						ProductVo productVo = handleUploadLeadTimeShow(vo, pi);//合并上传的交期
						if(null!=productVo){
							leadTimeResult.add(productVo);
						}
					}
				});

				if(CollectionUtils.isNotEmpty(leadTimeResult)){
					for(ProductVo temp : leadTimeResult){
						if(null!=temp.getProductType() && 0==temp.getProductType()){
							vo.setMinLeadTimeMLShow(temp.getMinLeadTimeMLShow());
							vo.setMaxLeadTimeMLShow(temp.getMaxLeadTimeMLShow());
							vo.setMinLeadTimeHKShow(temp.getMinLeadTimeHKShow());
							vo.setMaxLeadTimeHKShow(temp.getMaxLeadTimeHKShow());
						}
						if(null!=temp.getProductType() && 1==temp.getProductType()){
							vo.setMinFactoryLeadTimeMLShow(temp.getMinFactoryLeadTimeMLShow());
							vo.setMaxFactoryLeadTimeMLShow(temp.getMaxFactoryLeadTimeMLShow());
							vo.setMinFactoryLeadTimeHKShow(temp.getMinFactoryLeadTimeHKShow());
							vo.setMaxFactoryLeadTimeHKShow(temp.getMaxFactoryLeadTimeHKShow());
						}
						if(null==temp.getProductType()){
							if(null==vo.getMinLeadTimeMLShow()){
								vo.setMinLeadTimeMLShow(temp.getMinLeadTimeMLShow());
							}
							if(null==vo.getMaxLeadTimeMLShow()){
								vo.setMaxLeadTimeMLShow(temp.getMaxLeadTimeMLShow());
							}
							if(null==vo.getMinLeadTimeHKShow()){
								vo.setMinLeadTimeHKShow(temp.getMinLeadTimeHKShow());
							}
							if(null==vo.getMaxLeadTimeHKShow()){
								vo.setMaxLeadTimeHKShow(temp.getMaxLeadTimeHKShow());
							}
							if(null==vo.getMinFactoryLeadTimeMLShow()){
								vo.setMinFactoryLeadTimeMLShow(temp.getMinFactoryLeadTimeMLShow());
							}
							if(null==vo.getMaxFactoryLeadTimeMLShow()){
								vo.setMaxFactoryLeadTimeMLShow(temp.getMaxFactoryLeadTimeMLShow());
							}
							if(null==vo.getMinFactoryLeadTimeHKShow()){
								vo.setMinFactoryLeadTimeHKShow(temp.getMinFactoryLeadTimeHKShow());
							}
							if(null==vo.getMaxFactoryLeadTimeHKShow()){
								vo.setMaxFactoryLeadTimeHKShow(temp.getMaxFactoryLeadTimeHKShow());
							}
						}
					}
				}
				//合并价格
				priceList.stream().forEach(price ->{
					if(vo.getId().equals(price.getProductId())) {
						vo.setPrices(price.getPrices());
						vo.setCostPrices(price.getCostPrices());
						vo.setResalePrices(price.getResalePrices());
						vo.setSpecialResaleprices(price.getSpecialResaleprices());
						return;
					}
				});
				//根据仓库id查询仓库名称
				if(StringUtils.isNotBlank(vo.getSourceId())){
					List<String> facilityIds = new ArrayList<>();
					facilityIds.add(vo.getSourceId());
					List<Facility> facilityList = partyClientBuilder.facilityResource().getFacility(facilityIds);
					if(CollectionUtils.isNotEmpty(facilityList)){
						vo.setSourceName(facilityList.get(0).getFacilityName());
					}
				}

			}
		} catch (Exception e) {
			logger.error("批量获取商品详细信息" + e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 合并物料数据
	 * @param data
	 * @param type
	 * @param ps
	 * @return
	 */
	private ProductStand mergeProductStandData(RawData data,MaterialVoType type,ProductStand ps){
		//爬虫过的数据，并且是可信度高的供应商  保存product_stand 表中  1.4.1版本后的新逻辑
		 if(type != null && StringUtils.equalsIgnoreCase(type.name(), MaterialVoType.UPDATE_DATA.name()) &&
				 Arrays.asList(creditHighVendorIds.split(",")).contains(data.getVendorId())){
			 if(StringUtils.isNotBlank(data.getManufacturer())&& CollectionUtils.isNotEmpty(data.getVendorCategories()) &&
					 (data.getCantCreateStand() == null || !data.getCantCreateStand())){
				//合并物料数据
				 productStandManager.mergeProductStandData(ps);

				 return ps;
			 }else{
				 logger.info("爬虫过来的数据，品牌或分类数据不完整，(data)={}",JSON.toJSONString(data));
			 }
		 }
		 return null;
	}

	/**
	 * 验证能否更新物料数据
	 * @param data
	 * @param brandAliasMap
	 * @param categoryAliasMap
	 * @param oriProductStand
	 * @param saveStandList
	 * @param type
	 * @param vendorWeightMap
	 */
	private void validateIsCanUpdateProductStand(RawData data, Map<String, ProductBrand> brandAliasMap,
			Map<String, ProductCategoryParent> categoryAliasMap,ProductStand oriProductStand,List<ProductStand> saveStandList,
			MaterialVoType type,Map<String,Integer> vendorWeightMap,Set<String> autoIntegrateQtyVendorIds){
		try {
			//判断供应商的权重是否可以修改spu
			if(!vendorWeightManager.isCanSaveProductStand(vendorWeightMap,data.getVendorId(), oriProductStand.getFrom()))
				return;
			ProductStand productStand = productStandManager.createProductStand(data, brandAliasMap, categoryAliasMap,
					oriProductStand,type,autoIntegrateQtyVendorIds);
			//是否能修改物料 如果属性没有任何变化，就不更新数据
			if(!productStandManager.isCanUpdate(productStand, oriProductStand))
				return;
			productStand = this.mergeProductStandData(data, type, productStand);
			if(Optional.ofNullable(productStand).isPresent())
				oriProductStand = productStand;
			//合并物料数据
			if(Optional.ofNullable(productStand).isPresent() && !saveStandList.contains(productStand))
				saveStandList.add(productStand);
		} catch (BusinessException e) {
			logger.error("验证能否更新物料数据异常：{}",e);
		}
	}


	/**
	 * 根据raw创建商品sku
	 *
	 * @param data
	 *            rawData
	 * @param brandAliasMap
	 *            品牌别名映射
	 * @param categoryAliasMap
	 *            类别别名映射
	 * @param oriProduct
	 *            原sku
	 * @param oriProductStand
	 *            原spu
	 * @since 2016年12月9日
	 * @author tongkun@yikuyi.com
	 * @throws BusinessException
	 */
	public List<Product> createProduct(RawData data, Map<String, ProductBrand> brandAliasMap,
			Map<String, ProductCategoryParent> categoryAliasMap, Product oriProduct,
			ProductStand oriProductStand,List<ProductStand> saveStandList,
			MaterialVoType type,Map<String,Integer> vendorWeightMap,Set<String> autoIntegrateQtyVendorIds) throws BusinessException {
		List<Product> results = new ArrayList<>();
		ProductStand ps = null;
		if(null != oriProductStand){
			ps = oriProductStand;
			//验证能否更新物料数据
			this.validateIsCanUpdateProductStand(data, brandAliasMap, categoryAliasMap, ps, saveStandList, type, vendorWeightMap,autoIntegrateQtyVendorIds);
		}else{
			//不满足上面的情况，则需要程序重新计算一个spu对象。这个对象需要：1、灌入到product对象的spu属性，
			try{
			 ps = productStandManager.createProductStand(data, brandAliasMap, categoryAliasMap, oriProductStand,type,autoIntegrateQtyVendorIds);
				//合并物料数据
                ProductStand stand = this.mergeProductStandData(data, type, ps);
				//特殊spu
				if(Optional.ofNullable(stand).isPresent() && Optional.ofNullable(data.getSpecialSpu()).isPresent() && data.getSpecialSpu())
					stand.setSpuId((stand.getSpuId().concat("-").concat(data.getSkuId())).toUpperCase());
				if(Optional.ofNullable(stand).isPresent() && !saveStandList.contains(stand)){
					ps = stand;
					saveStandList.add(stand);
				}

			}catch(BusinessException e){
				logger.error("物料数据验证拼装出现异常:{}",e);
				ps = new ProductStand();
				ps.setManufacturer(data.getManufacturer());
				ps.setManufacturerPartNumber(data.getManufacturerPartNumber());
			}
		}

		// 创建spu
		//如果无法创建spu，但是原先已经存在了spu则不改变这个spu
		if(ps==null&&oriProduct!=null&&oriProduct.getSpu()!=null){
			ps = oriProduct.getSpu();
		}

		// 判空库存
		List<Stock> stocks = data.getStocks();
		if (stocks == null || stocks.isEmpty()) {
			stocks = new ArrayList<>();
			Stock stock = new Stock();
			stock.setSource(String.valueOf(RawData.ProductSourceType.STOCK.getValue()));
			stock.setQuantity(0);
			stock.setLeadTime("0");
			stocks.add(stock);
		}
		// 循环仓库，非原厂即可入库
		for (Stock stock : stocks) {
			if (StringUtils.isNotBlank(stock.getSourceId()) || !String.valueOf(RawData.ProductSourceType.SOURCE.getValue()).equals(stock.getSource())) {
				Product product = createProduct(data, ps, oriProduct, stock);// 根据当前仓库创建库存
				results.add(product);
			}
		}
		return results;
	}

	/**
	 * 批量保存商品到数据库
	 * @param list
	 * @since 2017年2月28日
	 * @author tongkun@yikuyi.com
	 */
	public void saveProducts(List<Product> list){
		productRepository.save(list);
		logger.info("保存商品数据成功,id="+list.get(0).getId());
	}

	/**
	 * 创建一个确定仓库的库存数据
	 *
	 * @param data
	 *            rawdata
	 * @param ps
	 *            spu对象
	 * @param oriProduct
	 *            原sku对象
	 * @param stock
	 *            库存对象
	 * @since 2016年12月11日
	 * @author tongkun@yikuyi.com
	 */
	public Product createProduct(RawData data, ProductStand ps, Product oriProduct, Stock stock) {
		logger.debug("RawData(createProduct)中，最小交期为："+data.getMinLeadTimeML());
		logger.debug("RawData(createProduct)中，最大交期为："+data.getMaxLeadTimeML());
		logger.debug("RawData(createProduct)中，工厂交期为："+data.getMinFactoryLeadTimeML());
		Product result = oriProduct;
		if (result == null) {
			result = new Product();
			result.setId(String.valueOf(IdGen.getInstance().nextId()));
			result.setCreatedTimeMillis(Long.toString(new Date().getTime()));//创建时间
		}
		result.setUpdatedTimeMillis(Long.toString(new Date().getTime()));//更新时间
		result.setPriceStatus(null);//清空价格状态
		// 重新设置
		if (RawData.ProductSourceType.STOCK.getValue().toString().equals(stock.getSource())) {
			stock.setSourceId(data.getVendorId() + "-" + stock.getSource());
		}
		// spu
		result.setSpu(ps);
		// 标准包装数量
		//if(data.getSpq()!=null){
			result.setSpq(data.getSpq());
		//}
		//封装单位
		if(StringUtils.isNotBlank(data.getPackagingUnit())){
			result.setPackagingUnit(data.getPackagingUnit());
		}
		// 最小订单金额
		//if(data.getMov()!=null){
			result.setMov(data.getMov());
		//}
		// 国家码
		if (data.getCountryCode() != null) {
			result.setCountryCode(data.getCountryCode());
		}
		// 币种
		if (data.getCurrencyCode() != null) {
			result.setCurrencyCode(data.getCurrencyCode());
		}
		// 供应商id
		result.setVendorId(data.getVendorId());
		// 封装
		if (data.getPackaging() != null) {
			result.setPackaging(data.getPackaging());
		}
		Integer mini = 1;
		Long breakQuantityMini = null;
		/*if (data.getMinimumQuantity() != null) {

			mini = data.getMinimumQuantity();
		}*/
		List<ProductPrice> listOld = data.getPrices();//原始的价格阶梯
		List<ProductPrice> listNew = new ArrayList<>();//转换币种后的价格阶梯
		if(CollectionUtils.isNotEmpty(listOld)){
			boolean haveUsd = false;
			//对原始的价格阶梯进行小数位保留5位，四舍五入处理
			for(ProductPrice productPrice : listOld){
				if (null != productPrice) {
				if(StringUtils.isNotBlank(productPrice.getUnitPrice())){
						Double unitPrice = new Double(productPrice.getUnitPrice());
						productPrice.setUnitPrice(BigDecimal.valueOf(unitPrice).setScale(5, BigDecimal.ROUND_HALF_UP).toString());
					}
					if(CollectionUtils.isNotEmpty(productPrice.getPriceLevels())){
						for(ProductPriceLevel productPriceLevel : productPrice.getPriceLevels()){
							if(null != productPriceLevel && StringUtils.isNotBlank(productPriceLevel.getPrice())){
								Double price = new Double(productPriceLevel.getPrice());
								productPriceLevel.setPrice(BigDecimal.valueOf(price).setScale(5, BigDecimal.ROUND_HALF_UP).toString());
							}
						}
					}
				}
			}
			//获取最小起订量
			for(ProductPrice temp : listOld){
				if(CollectionUtils.isNotEmpty(temp.getPriceLevels())){
					breakQuantityMini = temp.getPriceLevels().get(0).getBreakQuantity();
					if(breakQuantityMini != null){
						mini = Integer.valueOf(breakQuantityMini.toString());
					}
					break;
				}
			}
			//判断价格是否包含美元
			for(ProductPrice temp2 : listOld){
				if(Currency.USD.name().equalsIgnoreCase(temp2.getCurrencyCode())){
					haveUsd = true;
					break;
		}
			}
			if(!haveUsd){
				//港币转美元汇率
				for(ProductPrice temp3 : listOld){
					//没有美元，当前这条数据的币种不是人民币，则转换一个美元
					ProductPrice productPrice = null;
					if(!Currency.CNY.name().equalsIgnoreCase(temp3.getCurrencyCode())){
						productPrice = new ProductPrice();
						List<ProductPriceLevel> productPriceLevelList = new ArrayList<>();
						//拿汇率
						Double rate = this.getExchangeRate(temp3.getCurrencyCode(), Currency.USD.name());
						if(rate>0){
							//转换单价
							if(null!=temp3 && StringUtils.isNotBlank(temp3.getUnitPrice())){
								Double unitPrice = new Double(temp3.getUnitPrice());
								productPrice.setUnitPrice(BigDecimal.valueOf(unitPrice*rate).setScale(5, BigDecimal.ROUND_HALF_UP).toString());
								productPrice.setCurrencyCode(Currency.USD.name());
							}
							//转换阶梯价
							if(null!=temp3 && CollectionUtils.isNotEmpty(temp3.getPriceLevels())){
								for(ProductPriceLevel productPriceLevelTemp : temp3.getPriceLevels()){
									ProductPriceLevel productPriceLevel = new ProductPriceLevel();
									Double price = new Double(productPriceLevelTemp.getPrice());
									productPriceLevel.setBreakQuantity(productPriceLevelTemp.getBreakQuantity());
									productPriceLevel.setPrice(BigDecimal.valueOf(price*rate).setScale(5, BigDecimal.ROUND_HALF_UP).toString());
									productPriceLevelList.add(productPriceLevel);
								}
							}
							//转换了币种放转换后的对象
							productPrice.setPriceLevels(productPriceLevelList);
							listNew.add(productPrice);
						}
					}
					listNew.add(temp3);//放原来的对象
				}
			}
		}
		if(data.getMinimumQuantity() != null && breakQuantityMini == null){
			mini = data.getMinimumQuantity();
		}
		// 价格
		if(CollectionUtils.isNotEmpty(listNew)){
			result.setPrices(listNew);
		}else if(CollectionUtils.isNotEmpty(listOld)){
			result.setPrices(listOld);
		} else {
			result.setPrices(new ArrayList<ProductPrice>());
		}
		// 原价
		if (data.getCostPrices() != null && !data.getCostPrices().isEmpty()) {
			result.setCostPrices(data.getCostPrices());
		}
		// 销售价
		if (data.getResalePrices() != null && data.getResalePrices().isEmpty()) {
			result.setResalePrices(data.getResalePrices());
		}
		// 特价
		if (data.getSpecialResaleprices() != null && data.getSpecialResaleprices().isEmpty()) {
			result.setSpecialResaleprices(data.getSpecialResaleprices());
		}
		result.setMinimumQuantity(mini);
		// 数量
		result.setQty(stock.getQuantity());
		// 快速查找码
		if(data.getQuickFindKey()==null){
			result.setQuickFindKey(ProductUtils.getProductQuickFindKey(data, ps, stock.getSourceId()));
		}else{
			result.setQuickFindKey(stock.getQuickFindKey());
		}
		// skuId
		result.setSkuId(data.getSkuId());
		// 店铺id
		result.setStoreId(99999999L);
		// 状态
		result.setPartStatus(data.getPartStatus());
		// 仓库
		result.setSourceId(stock.getSourceId());
		//单位
		if(null == data.getUnit()){
			result.setUnit(null);
		}else if(null != data.getUnit()){
			result.setUnit(data.getUnit());
		}
		// 交期
		result.setLeadTime(stock.getLeadTime());
		//最小交期
		if(null == data.getMinLeadTimeML()){
			result.setMinLeadTimeML(null);
		}else if(null != data.getMinLeadTimeML()){
			result.setMinLeadTimeML(Integer.parseInt(data.getMinLeadTimeML()));
		}

		//最大交期
		if(null == data.getMaxLeadTimeML()){
			result.setMaxLeadTimeML(null);
		}else if(null != data.getMaxLeadTimeML()){
			result.setMaxLeadTimeML(Integer.parseInt(data.getMaxLeadTimeML()));
		}
		logger.debug("data.getFactoryLeadTime()（工厂交期为）"+data.getMinFactoryLeadTimeML());
		//工厂交期
		if(null == data.getMinFactoryLeadTimeML()){
			result.setMinFactoryLeadTimeML(null);
		}else if(null != data.getMinFactoryLeadTimeML()){
			result.setMinFactoryLeadTimeML(Integer.parseInt(data.getMinFactoryLeadTimeML()));
		}
		//工厂交期
		if(null == data.getMaxFactoryLeadTimeML()){
			result.setMaxFactoryLeadTimeML(null);
		}else if(null != data.getMaxFactoryLeadTimeML()){
			result.setMaxFactoryLeadTimeML(Integer.parseInt(data.getMaxFactoryLeadTimeML()));
		}

		//最小交期
		if(null == data.getMinLeadTimeHK()){
			result.setMinLeadTimeHK(null);
		}else if(null != data.getMinLeadTimeHK()){
			result.setMinLeadTimeHK(Integer.parseInt(data.getMinLeadTimeHK()));
		}

		//最大交期
		if(null == data.getMaxLeadTimeHK()){
			result.setMaxLeadTimeHK(null);
		}else if(null != data.getMaxLeadTimeHK()){
			result.setMaxLeadTimeHK(Integer.parseInt(data.getMaxLeadTimeHK()));
		}
		logger.debug("data.getFactoryLeadTime()（工厂交期为）"+data.getMinFactoryLeadTimeHK());
		//工厂交期
		if(null == data.getMinFactoryLeadTimeHK()){
			result.setMinFactoryLeadTimeHK(null);
		}else if(null != data.getMinFactoryLeadTimeHK()){
			result.setMinFactoryLeadTimeHK(Integer.parseInt(data.getMinFactoryLeadTimeHK()));
		}
		//工厂交期
		if(null == data.getMaxFactoryLeadTimeHK()){
			result.setMaxFactoryLeadTimeHK(null);
		}else if(null != data.getMaxFactoryLeadTimeHK()){
			result.setMaxFactoryLeadTimeHK(Integer.parseInt(data.getMaxFactoryLeadTimeHK()));
		}

		// 供应商id
		if (data.getVendorId() != null) {
			result.setVendorId(data.getVendorId());
		}
		// 供应商名称
		if (data.getVendorName() != null) {
			result.setVendorName(data.getVendorName());
		}
		// 供应商连接
		if (data.getVendorDetailsLink() != null) {
			result.setVendorDetailsLink(data.getVendorDetailsLink());
		}
		// 供应商系列
		if (data.getVendorSeries() != null) {
			result.setVendorSeries(data.getVendorSeries());
		}
		// 供应商分类
		if(data.getVendorCategories()!=null){
			result.setVendorCategories(data.getVendorCategories());
		}
		// processId
		if (data.getProcessId() != null) {
			result.setProcessId(data.getProcessId());
		}
		//当qty为-1时，将status改为0
		if(stock.getQuantity() == -1){
			result.setStatus(0);
		}else{
			result.setStatus(1);
		}

		if(data.getRemark() != null){
			result.setRemark(data.getRemark());
		}
		//批号
		result.setDateCode(data.getDateCode());
		//数据类型
		result.setCostType(data.getCostType());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Date date = null; //初始化date
		//失效日期
		if(null!=data.getExpiryDate()){
			try {
				date = sdf.parse(data.getExpiryDate());
				result.setExpiryDate(String.valueOf(date.getTime()));
			} catch (ParseException e) {
				logger.error("parse date error ,messge is {}",e.getMessage());
				throw new SystemException(e);
			}
		}else{
			result.setExpiryDate(null);
		}

		logger.debug("创建商品时(createProduct)，最小交期为："+data.getMinLeadTimeML());
		logger.debug("创建商品时(createProduct)，最大交期为："+data.getMaxLeadTimeML());
		return result;
	}

	/**
	 * 更新一个product数据，只能更新销售信息
	 * @param oriProduct 要更新
	 * @return
	 * @throws BusinessException
	 * @since 2017年3月21日
	 * @author tongkun@yikuyi.com
	 */
	public Product updateProduct(Product oriProduct,Boolean canQuery) throws BusinessException{
		Update up = getUpdateProduct(oriProduct);
		//更新商品
		mongoOptions.updateMulti(query(where("_id").is(oriProduct.getId())), up, Product.class);
		//根据ID查询产品信息并且刷新原始缓存数据
		return this.findProductByIdAndCleanOrigin(oriProduct.getId(),canQuery);
	}

	/**
	 * 根据ID查询产品信息并且刷新原始缓存数据
	 * @param id
	 * @return
	 * @since 2017年9月4日
	 * @author injor.huang@yikuyi.com
	 * @throws BusinessException
	 */
	public Product findProductByIdAndCleanOrigin(String id,Boolean canQuery) throws BusinessException{
		Product result = null;
		if(canQuery){
			//反查更新后的数据
			result = productRepository.findOne(id);
			if(result==null){
				throw new BusinessException("NO_DATA_DELETE","传入的id:"+id+"不正确，因此没有数据可以删除");
			}
		}
		//清除原来的价格缓存
		Cache productPriceCache = cacheManager.getCache("productPriceCache");
		productPriceCache.evict("ProductPriceCache-"+id);
		return result;
	}

	/**
	 * 根据传入的参数获取需要更新的对象
	 * 因为product只能更新销售信息，因此此方法主要用于验证是否传入id，以及保留可以更新的属性
	 * @param oriProduct
	 * @return
	 * @since 2017年3月21日
	 * @author tongkun@yikuyi.com
	 * @throws BusinessException
	 */
	public Update getUpdateProduct(Product oriProduct) throws BusinessException{
		//没有输入id则报错
		if(oriProduct.getId()==null){
			throw new BusinessException("NO_ID","没有传入必须的参数id");
		}
		Integer mini = 1;
		Long breakQuantityMini = null;
		//更新时间
		Update up = update(UPDATED_TIME_MILLIS_FIELD_NAME,Long.toString(new Date().getTime()));
		//价格

		//价格保存
		if(oriProduct.getPrices()!=null&&!oriProduct.getPrices().isEmpty()){
			up.set("prices", sortPriceLevel(oriProduct.getPrices()));
		}
		//产地
		if(oriProduct.getCountryCode()!=null){
			up.set("countryCode",oriProduct.getCountryCode());
		}
		//更新者
		if(oriProduct.getLastUpdateUser()!=null){
			up.set("lastUpdateUser",oriProduct.getLastUpdateUser());
		}
		//币种
		if(oriProduct.getCurrencyCode()!=null){
			up.set("currencyCode",oriProduct.getCurrencyCode());
		}
		//交期
		if(oriProduct.getLeadTime()!=null){
			up.set("leadTime",oriProduct.getLeadTime());
		}
		if(oriProduct.getPrices()!=null&&!oriProduct.getPrices().isEmpty()){
			for(ProductPrice temp : oriProduct.getPrices()){
				if(CollectionUtils.isNotEmpty(temp.getPriceLevels())){
					breakQuantityMini = temp.getPriceLevels().get(0).getBreakQuantity();
					if(breakQuantityMini != null){
						mini = Integer.valueOf(breakQuantityMini.toString());
					}
					break;
				}
			}
		}
		if(oriProduct.getMinimumQuantity()!=null && breakQuantityMini == null){
			mini = oriProduct.getMinimumQuantity();
		}

		//最小起订量
		up.set("minimumQuantity",mini);
		//最小包装数
		if(oriProduct.getMov()!=null){
			up.set("Mov",oriProduct.getMov());
		}
		//spq
		//if(oriProduct.getSpq()!=null){
			up.set("spq",oriProduct.getSpq());
		//数量
		if(oriProduct.getQty()!=null){
			up.set("qty",oriProduct.getQty());
		}
		//单位
		if(oriProduct.getUnit()!=null){
			up.set("unit",oriProduct.getUnit());
		}
		//状态
		if(oriProduct.getStatus()!=null){
			up.set(STATUS_FIELD_NAME, oriProduct.getStatus());
		}

		//国内最小交期
		up.set("minLeadTimeML", oriProduct.getMinLeadTimeML());

		//国内最大交期
		up.set("maxLeadTimeML", oriProduct.getMaxLeadTimeML());

		//国内工厂交期
		up.set("minFactoryLeadTimeML", oriProduct.getMinFactoryLeadTimeML());
		up.set("maxFactoryLeadTimeML", oriProduct.getMaxFactoryLeadTimeML());

		//国内最小交期
		up.set("minLeadTimeHK", oriProduct.getMinLeadTimeHK());

		//国内最大交期
		up.set("maxLeadTimeHK", oriProduct.getMaxLeadTimeHK());

		//国内工厂交期
		up.set("minFactoryLeadTimeHK", oriProduct.getMinFactoryLeadTimeHK());
		up.set("maxFactoryLeadTimeHK", oriProduct.getMaxFactoryLeadTimeHK());

		//批号
		up.set("dateCode", oriProduct.getDateCode());
		//包装
		up.set("packaging", oriProduct.getPackaging());
		if(oriProduct.getSpu() != null){
			up.set("spu", oriProduct.getSpu());
			/*up.set("spu.manufacturer", oriProduct.getSpu().getManufacturer());
			up.set("spu.manufacturerId", oriProduct.getSpu().getManufacturerId());
			up.set("spu.manufacturerPartNumber", oriProduct.getSpu().getManufacturerPartNumber());
			up.set("spu.description", oriProduct.getSpu().getDescription());
			up.set("spu.manufacturerAgg", oriProduct.getSpu().getManufacturerAgg());
			up.set("spu.rohs", oriProduct.getSpu().getRohs());
			up.set("spu.categories", oriProduct.getSpu().getCategories());
			up.set("spu.documents", oriProduct.getSpu().getDocuments());
			up.set("spu.images", oriProduct.getSpu().getImages());
			up.set("spu.parameters", oriProduct.getSpu().getParameters());
			up.set("spu.updatedTimeMillis", Long.toString(new Date().getTime()));*/
		}
		//过期时间
		if(StringUtils.isNotEmpty(oriProduct.getExpiryDate())){
			try {
				Date expiryDate = dateFormat.parse(oriProduct.getExpiryDate());
				up.set("expiryDate", String.valueOf(expiryDate.getTime()));
				Date nowDate = dateFormat.parse(dateFormat.format(new Date()));

				//大于当天
				if(expiryDate.after(nowDate)){
					up.set("priceStatus", null);
				}
			} catch (ParseException e) {
				logger.error("ProductManager.getUpdateProduct parse field expiryDate to date error!");
			}
		}else{
			up.set("expiryDate", null);
			up.set("priceStatus", null);
		}
		return up;
	}

	/**
	 * 价格梯度排序
	 * 如果有为0，负数的价格，则要去除掉。
	 * 大于0的价格要进行排序
	 * @param oriLevels
	 * @return
	 * @since 2017年4月1日
	 * @author tongkun@yikuyi.com
	 */
	public List<ProductPrice> sortPriceLevel(List<ProductPrice> oriPrices){
		List<ProductPrice> result = new ArrayList<>();
		for(ProductPrice price:oriPrices){
			List<ProductPriceLevel> newLevels = new ArrayList<>();
			List<ProductPriceLevel> levels = price.getPriceLevels();
			//价格梯度排序
			if(levels!=null){
				sortPriceLevel(levels,newLevels);
			}
			//单价
			if(CollectionUtils.isNotEmpty(newLevels)){
				price.setUnitPrice(newLevels.get(0).getPrice());
			}else{
				price.setUnitPrice(null);
			}
			price.setPriceLevels(newLevels);
			result.add(price);
		}
		return result;
	}

	/**
	 * 排序价格梯度
	 * @param levels
	 * @param newLevels
	 * @since 2017年6月28日
	 * @author tongkun@yikuyi.com
	 */
	private void sortPriceLevel(List<ProductPriceLevel> levels,List<ProductPriceLevel> newLevels){
		for(ProductPriceLevel level:levels){
			boolean canSort = canSortPriceLevel(level);
			if(!canSort)
				continue;
			int insertIndex = 0;
			//选择法排序方式插入新list
			for(int i = 0;i < newLevels.size();i++){
				//循环至末尾则将梯度加到末尾
				ProductPriceLevel newLevel = newLevels.get(i);
				//如果循环至刚好大于当前元素的时候，插入到这个元素后面
				if(level.getBreakQuantity()>newLevel.getBreakQuantity()){
					insertIndex = getInsertPosition(i,newLevels.size());
				}
				//如果有相同的，则跳过
				else if(level.getBreakQuantity().equals(newLevel.getBreakQuantity())){
					insertIndex = -1;
					break;
				}
			}
			if(insertIndex>=0)
				newLevels.add(insertIndex,level);
		}
	}

	/**
	 * 判断这个价格对象是否需要排序
	 * @param level
	 * @return
	 * @since 2017年6月28日
	 * @author tongkun@yikuyi.com
	 */
	private boolean canSortPriceLevel(ProductPriceLevel level){
		boolean canSort = true;
		//数量为空，小于等于0则不进行排序
		if(level.getBreakQuantity()==null||
				level.getBreakQuantity()<=0)
			canSort = false;
		//价格为空，小于等于0则不进行排序
		if(level.getPrice()==null||
				!isPositiveNumber(level.getPrice()))
			canSort = false;
		return canSort;
	}

	/**
	 * 判断字符串是否正数
	 * @param numString 数字字符串
	 * @return 判定结果
	 * @since 2017年6月28日
	 * @author tongkun@yikuyi.com
	 */
	private boolean isPositiveNumber(String numString){
		return com.ictrade.tools.StringUtils.isPositiveDecimal(numString)||com.ictrade.tools.StringUtils.isPositiveInteger(numString);
	}

	/**
	 * 获取数组插入位置
	 * @param nowIndex 当前位置
	 * @param maxIndex
	 * @return
	 * @since 2017年6月28日
	 * @author tongkun@yikuyi.com
	 */
	private int getInsertPosition(int nowIndex,int maxIndex){
		if(nowIndex<maxIndex)
			return nowIndex+1;
		else
			return nowIndex;
	}
	/**
	 * 查询商品信息
	 *
	 * @param datas
	 * @param brandAliasMap
	 * @return
	 * @since 2016年12月9日
	 * @author tongkun@yikuyi.com
	 */
	public List<Product> findProductByRawDatas(List<RawData> datas, Map<String, ProductBrand> brandAliasMap) {
		List<String> keys = new ArrayList<>();
		List<RawData> newRawData = new ArrayList<>();
		for (RawData data : datas) {
			if (data != null) {
				// 默认库存
				if (CollectionUtils.isEmpty(data.getStocks())) {
					List<Stock> stocks = new ArrayList<>();
					Stock stock = new Stock();
					stock.setSource(String.valueOf(RawData.ProductSourceType.STOCK.getValue()));
					stocks.add(stock);
					data.setStocks(stocks);
				} // 循环库存
				//如果stock的size大于0，则需要按照stock分解。比如2个stock要分解成2条rawdata
				boolean needCopy = data.getStocks().size()>1;
				for (Stock stock : data.getStocks()) {
					// 跳过原厂
					if (RawData.ProductSourceType.SOURCE.getValue().toString().equals(stock.getSource())) {
						continue;
					} else if (RawData.ProductSourceType.STOCK.getValue().toString().equals(stock.getSource())) {
						stock.setSourceId(data.getVendorId() + "-" + stock.getSource());
					}else if(StringUtils.isBlank(stock.getSourceId())){
						stock.setSourceId(materialManager.findFacilityId(data.getVendorId(), stock.getSource(),data.getManufacturerPartNumber()));
					}
					// 找到品牌
					ProductBrand brand = brandAliasMap.get(brandManager.getAliasKey(data.getVendorId(), data.getManufacturer()));
					if(brand==null){
						brand = brandAliasMap.get(brandManager.getAliasKey(null, data.getManufacturer()));
					}
					String brandNameOrId = (brand == null ? data.getManufacturer() : brand.getId().toString());// 标准品牌或原品牌
					// 默认skuId
					if (data.getSkuId() == null) {
						data.setSkuId(data.getManufacturerPartNumber());
					}
					// 获取key值
					String key = ProductUtils.getProductQuickFindKey(data, brandNameOrId, stock.getSourceId());
					stock.setQuickFindKey(key);
					//如果需要分解复制rawdata
					if(needCopy){
						RawData newData = new RawData();
						BeanUtils.copyProperties(data ,newData);
						newData.setStocks(new ArrayList<>());
						newData.getStocks().add(stock);
						newData.setQuickFindKey(key);
						newRawData.add(newData);
					}
					//如果不需要分解复制rawdata
					else{
						newRawData.add(data);
						data.setQuickFindKey(key);
					}
					keys.add(key);// 增加标准查找
					keys.add(ProductUtils.getProductQuickFindKey(data, data.getManufacturer(), stock.getSourceId()));// 增加非标准查找
				}
			}
		}
		if(null != datas){
			//重设需要创建的数据
			datas.clear();
			datas.addAll(newRawData);
		}
		return productRepository.findProductByQuickFindKey(keys);
	}

	/**
	 * 查询根据spuId来查询商品数据
	 * @param spuIds
	 * @return
	 * @since 2017年2月28日
	 * @author tongkun@yikuyi.com
	 */
	public List<Product> findProductBySpuIds(List<String> spuIds){
		return productRepository.findProductBySpuIds(spuIds);
	}

	/**
	 * 获取品牌名
	 * @param brandName
	 * @param brandAliasMap
	 * @return
	 */
	public ProductBrand getBrandName(String brandName,Map<String,ProductBrand> brandAliasMap){
		if(brandName==null){
			return null;
		}
		ProductBrand brand = brandAliasMap.get(brandName.toUpperCase());
		if(brand==null){
			brand = new ProductBrand();
			brand.setBrandName(brandName);
		}
		return brand;
	}

	public Product matchingProduct(String partNum){
		Product p = productRepository.matchingProduct(partNum);
		//处理图片
		List<ProductImage> spuImage = p.getSpu().getImages();
		//图片地址加上前缀后返回
		List<ProductImage> spuImageNew = handleImageUrl(spuImage);
		p.getSpu().setImages(spuImageNew);
		return p;
	}

	/**
	 * 删除商品
	 * @param product
	 * @since 2016年12月21日
	 * @author tongkun@yikuyi.com
	 */
	public void deleteProduct(Product product){
		productRepository.delete(product);
	}

	/**
	 * 删除spu
	 * @param stand
	 * @since 2016年12月21日
	 * @author tongkun@yikuyi.com
	 */
	public void deleteProductStand(ProductStand stand){
		productStandRepository.delete(stand);
	}

	/**
	 * 查询销售中的商品  ---新
	 * @since 2017年2月22日
	 * @author zr.wujiajun
	 * @throws IOException
	 * @throws JsonProcessingException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public Map<String, Object> getSaleProductList(String id, String manufacturerPartNumber, String manufacturer,
			String vendorId, String sourceId, Integer cate1Id, Integer cate2Id, Integer cate3Id, String keyword,
			String startDate,String endDate,Boolean standard,int page,int pageSize,String hasQty,String isInvalid) {
		Map<String,Object> result = new HashMap<>();
		//拼装查询条件
		JSONObject paramJson = this.assembleQueryCondition(id, manufacturerPartNumber, manufacturer, vendorId, sourceId, cate1Id, cate2Id, cate3Id, keyword, startDate, endDate,standard,hasQty,isInvalid);
		//查询
		int queryPage = 0;
		if(page>0)
			queryPage = page-1;
		PageRequest pageable = new PageRequest(queryPage,pageSize);
		Page<Product> datas = productRepository.findByPage(paramJson,pageable);
		long total = datas.getTotalElements();
		//合并供应商，仓库，价格等信息
		List<ProductVo> voList = new ArrayList<>();
		if( CollectionUtils.isNotEmpty(datas.getContent())){
			for(Product p : datas.getContent()){
				//处理图片
				List<ProductImage> spuImage = p.getSpu().getImages();
				//图片地址加上前缀后返回
				List<ProductImage> spuImageNew = handleImageUrl(spuImage);
				p.getSpu().setImages(spuImageNew);
				ProductVo vo = new ProductVo();
				BeanUtils.copyProperties(p ,vo);
				voList.add(vo);
			}
		}

		mergeCurrentData(voList);
		result.put("productList", voList);
		result.put("page", page);
		result.put("pageSize", pageSize);
		result.put("total", total);
		return result;
	}

	/**
	 * 拼装查询条件
	 * @param id
	 * @param manufacturerPartNumber
	 * @param manufacturer
	 * @param vendorId
	 * @param sourceId
	 * @param cate1Id
	 * @param cate2Id
	 * @param cate3Id
	 * @param keyword
	 * @param startDate
	 * @param endDate
	 * @param hasQty 是否有库存
	 * @return
	 * @since 2017年8月30日
	 * @author injor.huang@yikuyi.com
	 */
	public JSONObject assembleQueryCondition(String id, String manufacturerPartNumber, String manufacturer,
			String vendorId, String sourceId, Integer cate1Id, Integer cate2Id, Integer cate3Id, String keyword,
			String startDate,String endDate,Boolean standard,String hasQty,String isInvalid){
		JSONObject paramJson = new JSONObject();
		if(StringUtils.isNotBlank(hasQty)){
			if("Y".equalsIgnoreCase(hasQty.trim())){
				JSONObject gtQty = new JSONObject();
				gtQty.put("$gt", 0);
				paramJson.put("qty", gtQty);
			}
			if("N".equalsIgnoreCase(hasQty.trim())){
				JSONObject eqQty = new JSONObject();
				eqQty.put("$eq", 0);
				paramJson.put("qty", eqQty);
			}
		}
		if(StringUtils.isNotBlank(vendorId)){
			paramJson.put("vendorId", vendorId);
		}
		//查询有效的
		paramJson.put(STATUS_FIELD_NAME, 1);

		//按照关键字查询
		if(StringUtils.isNotBlank(keyword)){
			BasicDBList keywordList = new  BasicDBList();
			keywordList.add(new  BasicDBObject("spu.manufacturer",  keyword.trim()));
			keywordList.add(new  BasicDBObject("spu.manufacturerPartNumber",  keyword.trim()));
			paramJson.put("$or", keywordList);
		}
		//高级查询和只查询是否存在商品
			if(StringUtils.isNotBlank(id)){
				paramJson.put("_id", id);
			}
			if(StringUtils.isNotBlank(manufacturer)){
				paramJson.put("spu.manufacturer", manufacturer.trim());
			}
			if(StringUtils.isNotBlank(manufacturerPartNumber)){
				paramJson.put("spu.manufacturerPartNumber",manufacturerPartNumber.trim());
			}
			if(StringUtils.isNotBlank(sourceId)){
				paramJson.put("sourceId", sourceId);
			}
			if(standard != null && standard){
				Document great= new Document();
				great.put("$ne", null);
				paramJson.put("spu._id", great);
			}else if(standard != null && !standard){
				paramJson.put("spu._id", null);
			}

			if(cate1Id !=null){
				paramJson.put("spu.categories.0._id", cate1Id);
			}
			if(cate2Id != null){
				paramJson.put("spu.categories.1._id", cate2Id);
			}
			if(cate3Id != null){
				paramJson.put("spu.categories.2._id", cate3Id);
			}
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Document greatAndLessThan = new Document();
			if(StringUtils.isNotBlank(startDate) && !startDate.equals(endDate)){
				try{
					Long startL = df.parse(startDate).getTime();
					greatAndLessThan.put("$gte", startL.toString());
				}catch(ParseException e){
					logger.error("日期格式不正确", e);
				}
			}
			if(StringUtils.isNotBlank(endDate) && !endDate.equals(startDate)){
				try {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(df.parse(endDate));
					calendar.add(Calendar.DAY_OF_MONTH, 1);
					greatAndLessThan.put("$lt", String.valueOf(calendar.getTime().getTime()));
				} catch (ParseException e) {
					logger.error("日期格式不正确，", e);
				}
			}
			//日期相等
			if(StringUtils.isNotBlank(endDate) && endDate.equals(startDate)){
				try {
					Long startL = df.parse(startDate).getTime();
					greatAndLessThan.put("$gte", startL.toString());

					Calendar cal = Calendar.getInstance();
					cal.setTime(df.parse(startDate));
					cal.add(Calendar.DATE, 1);
					greatAndLessThan.put("$lt", String.valueOf(cal.getTime().getTime()));
				} catch (ParseException e) {
					logger.error("日期格式不正确，", e);
				}
			}
			if(!greatAndLessThan.isEmpty()){
				paramJson.put(UPDATED_TIME_MILLIS_FIELD_NAME, greatAndLessThan);
			}
			//是否失效
			if(StringUtils.isNotBlank(isInvalid)){
				//priceStatus expired失效
				JSONObject priceStatusNe = new JSONObject();
				if(isInvalid.equalsIgnoreCase("Y") ){
					paramJson.put("priceStatus", "expired");
				}else{
					priceStatusNe.put("$ne", "expired");
					paramJson.put("priceStatus", priceStatusNe);
				}

			}
			return paramJson;
	}


	/**
	 * 销售中的商品查询条件拼接
	 * @param id
	 * @param manufacturerPartNumber
	 * @param manufacturer
	 * @param vendorId
	 * @param sourceId
	 * @param cate1Id
	 * @param cate2Id
	 * @param cate3Id
	 * @param keyword
	 * @param startDate
	 * @param endDate
	 * @param standard
	 * @return
	 * @since 2017年9月12日
	 * @author injor.huang@yikuyi.com
	 */
	public Query mergeCondition(String id, String manufacturerPartNumber, String manufacturer,
			String vendorId, String sourceId, Integer cate1Id, Integer cate2Id, Integer cate3Id, String keyword,
			String startDate,String endDate,Boolean standard,String hasQty,String isInvalid){
		Query query = new Query();
		Criteria criteria = new Criteria();

		//是否有库存
		if(StringUtils.isNotEmpty(hasQty)){
			if("Y".equalsIgnoreCase(hasQty.trim())){
				criteria.and("qty").gt(0);
			}
			if("N".equalsIgnoreCase(hasQty.trim())){
				criteria.and("qty").is(0);
			}
		}

		if(StringUtils.isNotBlank(vendorId)){
			criteria.and("vendorId").is(vendorId);
		}
		//按照关键字查询
		if(StringUtils.isNotBlank(keyword)){
			criteria.orOperator(new Criteria[]{new Criteria().and("spu.manufacturer").is(keyword.trim()),
					new Criteria().and("spu.manufacturerPartNumber").is(keyword.trim())});
		}
		//高级查询和只查询是否存在商品
			if(StringUtils.isNotBlank(id)){
				criteria.and("_id").is(id);
			}
			if(StringUtils.isNotBlank(manufacturer)){
				criteria.and("spu.manufacturer").is( manufacturer.trim());
			}
			if(StringUtils.isNotBlank(manufacturerPartNumber)){
				criteria.and("spu.manufacturerPartNumber").is( manufacturerPartNumber.trim());
			}
			if(StringUtils.isNotBlank(sourceId)){
				criteria.and("sourceId").is(sourceId);
			}
			if(standard != null && standard){
				criteria.and("spu._id").ne(null);
			}else if(standard != null && !standard){
				criteria.and("spu._id").is(null);
			}

			if(cate1Id !=null){
				criteria.and("spu.categories.0._id").is(cate1Id);
			}
			if(cate2Id != null){
				criteria.and("spu.categories.1._id").is(cate1Id);
			}
			if(cate3Id != null){
				criteria.and("spu.categories.2._id").is(cate1Id);
			}

			JSONObject gtAndlet = new JSONObject();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			if(StringUtils.isNotBlank(startDate) && !startDate.equals(endDate)){
				try{
					Long startL = df.parse(startDate).getTime();
					gtAndlet.put("$gte", String.valueOf(startL));
				}catch(ParseException e){
					logger.error("日期格式不正确", e);
				}
			}

			if(StringUtils.isNotBlank(endDate) && !endDate.equals(startDate)){
				try {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(df.parse(endDate));
					calendar.add(Calendar.DAY_OF_MONTH, 1);
					gtAndlet.put("$lt", String.valueOf(calendar.getTime().getTime()));
				} catch (ParseException e) {
					logger.error("日期格式不正确，", e);
				}
			}

			//开始时间和结束时间相等
			if(StringUtils.isNotBlank(endDate) && endDate.equals(startDate)){
				try {
					Long startL = df.parse(startDate).getTime();
					gtAndlet.put("$gte", startL.toString());

					Calendar cal = Calendar.getInstance();
					cal.setTime(df.parse(startDate));
					cal.add(Calendar.DATE, 1);
					gtAndlet.put("$lt", String.valueOf(cal.getTime().getTime()));
				} catch (ParseException e) {
					logger.error("日期格式不正确，", e);
				}
			}

			if(StringUtils.isNotBlank(startDate) || StringUtils.isNotBlank(endDate) ){
				criteria.and(UPDATED_TIME_MILLIS_FIELD_NAME).is(gtAndlet);
			}

			//是否失效
			if(StringUtils.isNotBlank(isInvalid)){
				//priceStatus expired失效
				JSONObject priceStatusNe = new JSONObject();
				if(isInvalid.equalsIgnoreCase("Y") ){
					criteria.and("priceStatus").is("expired");
				}else{
					priceStatusNe.put("$ne", "expired");
					criteria.and("priceStatus").is(priceStatusNe);
				}
			}
			query.addCriteria(criteria);
			return query;
	}

	/**
	 * 合并实时价格，仓库，供应商信息
	 * @param datas 商品列表
	 * @param vendorId 供应商
	 * @param vendorId
	 * @since 2017年1月22日
	 * @author zr.wujiajun@yikuyi.com
	 */
	private void mergeCurrentData(List<ProductVo> datas){
		if(CollectionUtils.isEmpty(datas)){
			return;
		}
		List<String> ids = new ArrayList<>();
		datas.stream().forEach(p ->	ids.add(p.getId()) );
		// 合并实时价格
		List<PriceInfo> priceList  = null;
		try{
			priceList = priceQueryManager.queryPrice(ids);
		}catch(Exception e){
			logger.error("查询实时价格: ",e);
		}
		if (priceList == null) {
			priceList = new ArrayList<>();
		}	
						
		Set<String> supplierIds = datas.stream().map(ProductVo::getVendorId).collect(Collectors.toSet());
		Map<String,SupplierVo> suppliers = partyClientBuilder.supplierClient().getSupplierSimple(supplierIds);
		for (ProductVo vo : datas) {
			//设置创建时间
			if( StringUtils.isNotBlank(vo.getCreatedTimeMillis())){
				vo.setCreatedDate(new Date(Long.parseLong(vo.getCreatedTimeMillis())));
			}
			//设置更新时间
			if(StringUtils.isNotBlank(vo.getUpdatedTimeMillis())){
				vo.setLastUpdateDate(new Date(Long.parseLong(vo.getUpdatedTimeMillis())));
			}
			//合并价格
			priceList.stream().forEach(price ->{
				if(vo.getId().equals(price.getProductId())) {
					vo.setResalePrices(price.getResalePrices());
					return;
				}
			});
			if(suppliers.containsKey(vo.getVendorId())){
				vo.setVendorName(suppliers.get(vo.getVendorId()).getSupplierName());
			}
			if(suppliers.containsKey(vo.getVendorId()) && suppliers.get(vo.getVendorId()).getFacilityIdMap().containsKey(vo.getSourceId())){
				vo.setSourceName(suppliers.get(vo.getVendorId()).getFacilityIdMap().get(vo.getSourceId()).getFacilityName());	
			}
		}		
	}
	
	/**
	 * 查询该供应商是否有商品
	 * @author zr.wujiajun
	 */
	public boolean isHasProduct(String vendorId){
		Product p = productRepository.findByVendorId(vendorId);
		return p !=null;
	}

	/**
	 * 推荐相关其他商品
	 * 描述:相同次小类下的商品，找出小于本页显示商品的SKUID的其他5个商品，如果没有就显示本次小类的5个商品
	 * @param id 当前商品id
	 * @since 2017年2月25日
	 * @author zr.wujiajun@yikuyi.com
	 */
	@SuppressWarnings("unchecked")
	public List<ProductVo> recommendOthers(String id,String catId){
		cacheManager.getCache("recommendOthersCache");
		Cache cache = cacheManager.getCache("recommendOthersCache");
		ValueWrapper valueWrapper = cache.get(catId);
		if(valueWrapper != null){
			List<ProductVo> list = (List<ProductVo>)valueWrapper.get();
			return list.stream().filter(v->!v.getId().equals(id)).collect(Collectors.toList());
		}
		Integer cate3Id = Integer.parseInt(catId);//次小类
		List<ProductVo> mongoResult = new ArrayList<>();
		if(cate3Id !=null && cate3Id >0){
			Query query = query(where(STATUS_FIELD_NAME).is(1))
					.addCriteria(where("_id").lt(id)).addCriteria(where("spu.categories.2._id").is(cate3Id)).limit(6);
			mongoResult = mongoOptions.find(query, ProductVo.class);

			if(mongoResult.size()<3){
				query = query(where(STATUS_FIELD_NAME).is(1))
						.addCriteria(where("spu.categories.2._id").is(cate3Id)).limit(6);
				mongoResult = mongoOptions.find(query, ProductVo.class);
			}
			//searchManager.getProductPrice(mongoResult.stream().filter(v->!v.getId().equals(id)).collect(Collectors.toList()));
		}
		cache.put(catId, mongoResult);
		return mongoResult;
	}

	/**
	 * 据商品ID查询单个商品详情
	 * @param id 商品ID
	 * @return 商品信息
	 * @since 2017年3月17日
	 * @author zr.wanghong
	 */
	public ProductVo findProductById(String id){
		Product product = productRepository.findOne(id);
		ProductVo productVo = new ProductVo();
		try {
			if(product != null)
			BeanUtils.copyProperties(product ,productVo );
		} catch (Exception e) {
			logger.error("ProductManager.findProductById copy properties error ,exception is:{}",e);
		}
		return productVo;
	}

	/**
	 * 删除销售中的商品
	 * @param productVo
	 * @return
	 * @since 2017年3月21日
	 * @author zr.wanghong
	 */
	public void deleteSaleProduct(String id){
		Product product = new Product();
		product.setId(id);
		product.setStatus(0);
		producer.sendMsg(this.createProductTopic,product,null);
	}

	/**
	 * 更新产品
	 * @param product
	 * @return
	 * @since 2017年3月30日
	 * @author tb.lijing@yikuyi.com
	 */
	public String updateProductInfo(@RequestBody Product product) {
		try {
			// 更新数据
			Product opProduct = this.updateProduct(product,true);
			// 搜索引擎同步
			List<ProductVo> list = new ArrayList<>();
			ProductVo opProductVo = new ProductVo();
			BeanUtils.copyProperties(opProduct ,opProductVo);
			list.add(opProductVo);
			logger.info("商品同步搜索引擎 id:" + opProduct.getId());
			this.adviceSyncElasticsearch(list);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}

	/**
	 * 根据productId批量更新商品库存和价格
	 *
	 * @since 2017年11月14日
	 * @author tb.lijing@yikuyi.com
	 */
	public void updateProductInfoBatch(List<RawData> rawDatas) {
		for (RawData data : rawDatas) {
			//当id不为空时更新商品数据
			if (StringUtils.isNotBlank(data.getId())) {
				Product product = new Product();
				product.setId(data.getId());
				List<Stock> stocks = data.getStocks();
				// 当库存不为空时更新库存
				if (CollectionUtils.isNotEmpty(stocks)) {
					// 循环仓库，非原厂即可入库
					for (Stock stock : stocks) {
						if (!stock.getSource().equals(String.valueOf(RawData.ProductSourceType.SOURCE.getValue()))) {
							product.setQty(stock.getQuantity());
						}
					}
				}
				// 当价格不为空时更新价格
				if (CollectionUtils.isNotEmpty(data.getPrices())) {
					product.setPrices(data.getPrices());
				}
				this.updateProductInfo(product);//更新商品数据
			}
		}
	}

	/**
	 * 异步通知搜索引擎更新
	 *
	 * @since 2017年8月30日
	 * @author injor.huang@yikuyi.com
	 */
	public void adviceSyncElasticsearch(List<ProductVo> list){
		MaterialVo materialVo = new MaterialVo();
		materialVo.setType(MaterialVo.MaterialVoType.UPDATE_DATA);
		materialVo.setMsg(list);
		materialVo.setSize(list.size());
		msgSender.sendMsg(syncElasticsearchProductTopicName, materialVo, null);
	}

	/**
	 * 批量创建sku信息
	 * @param rawDatas
	 * @return
	 * @since 2017年3月30日
	 * @author tb.lijing@yikuyi.com
	 */
	public String createProduct(List<RawData> rawDatas , MaterialVoType type) {
		if (MaterialVoType.FILE_UPLOAD == type && materialManager.createProductCancelTheJudgment(rawDatas)) {
			return StringUtils.EMPTY;
		}
		rawDatas = Lists.newArrayList(rawDatas);
		MaterialVo materialVo = new MaterialVo();
		materialVo.setType(type);
		materialVo.setDocId(rawDatas.get(0).getProcessId());
		materialVo.setSize(rawDatas.size());

		// 获得品牌映射
		Map<String, ProductBrand> brandAliasMap = null;
		// 获得分类映射
		Map<String, ProductCategoryParent> categoryAliasMap = null;
		// 原sku信息
		List<Product> oriProducts = null;
		// 原spu
		List<ProductStand> oriProductStands = null;
		Set<String> supplierIds = null;
		//获取所有权重供应商
		Map<String,Integer> vendorWeightMap = null;
		//获取自动集成库的供应商
		Set<String> autoIntegrateQtyVendorIds = null;
		try {
			List<String> keys = new ArrayList<>();
			for (int i = 0;i < rawDatas.size();i++){
				RawData v = rawDatas.get(i);
				if(v==null)
					continue;
				if(StringUtils.isBlank(v.getManufacturer())){
					logger.info("爬虫、上传过来的数据，缺少品牌 rawDatas="+JSON.toJSONString(v));
					rawDatas.remove(v);
					i--;
					continue;
				}
				keys.add(brandManager.getAliasKey(v.getVendorId(), v.getManufacturer()));
				keys.add(brandManager.getAliasKey(null, v.getManufacturer()));
			}
			brandAliasMap = brandManager.getBrandByAliasName(keys);
			Set<String> cahceKey = new HashSet<>();
			rawDatas.stream().forEach(v -> cahceKey.addAll(RawData.getVendorCategoryRedisKey(v.getVendorCategories())));
			categoryAliasMap = categoryManager.getCategoryByAliasName(cahceKey);// 获得分类映射
			cahceKey.clear();
			oriProducts = findProductByRawDatas(rawDatas, brandAliasMap);
			oriProductStands = productStandManager.findProductStandByRawDatas(rawDatas, brandAliasMap,type);
			supplierIds = partyClientUtils.getAllSupplierIds();
			vendorWeightMap = vendorWeightManager.getVendorWeightMap();
			autoIntegrateQtyVendorIds = partyClientUtils.getAutoIntegrateQtyVendorIds(true);
		} catch (Exception e) {
			// 品牌加载失败的异常
			if (MaterialVoType.FILE_UPLOAD.equals(materialVo.getType())) {
				documentLogManager.updateDocLogsStatusByRaw(rawDatas,e.getMessage());
			}
			logger.error(e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
		try {
			List<RawData> errorList = new ArrayList<>();//处理失败的list
			List<ProductVo> sendList = new ArrayList<>();//处理结束发给下一个处理过程的list
			List<Product> saveList = new ArrayList<>();//要保存到数据库的商品list
			List<ProductStand> saveStandList = new ArrayList<>();//要保存到数据库的产品list
			//循环原始数据
			for(RawData data:rawDatas){
				if(!supplierIds.contains(data.getVendorId())){
					//供应商不存在，数据取消，主要针对爬虫数据做处理，文件上传的时候，供应商一定存在
					continue;
				}
				//查找用对象
				RawData rawData = new RawData();
				rawData.setQuickFindKey(data.getQuickFindKey());
				rawData.setSkuId(data.getSkuId());
				rawData.setManufacturerPartNumber(data.getManufacturerPartNumber());
				String tempManufacturer = null == data.getManufacturer() ? null : data.getManufacturer().toUpperCase();
				if(brandAliasMap.containsKey(tempManufacturer)){
					rawData.setManufacturer(brandAliasMap.get(tempManufacturer).getBrandName());
				}else{
					rawData.setManufacturer(tempManufacturer);
				}
				rawData.setSpuId(data.getSpuId());
				rawData.setStocks(data.getStocks());
				//查找原来的sku
				int index = -1;
				Product oriProduct = null;
				if(CollectionUtils.isNotEmpty(oriProducts)){
					index = oriProducts.indexOf(rawData);
					if(index>=0){
						oriProduct = oriProducts.get(index);
					}
				}
				//查找原spu
				ProductStand oriProductStand = null;
				if(CollectionUtils.isNotEmpty(oriProductStands)){
					index = oriProductStands.indexOf(rawData);
					if(index>=0){
						oriProductStand = oriProductStands.get(index);
					}
				}
				//验证并且创建SPU
				String errorMsg = this.validateAndCreateProduct(data , oriProduct , oriProductStand , brandAliasMap , categoryAliasMap ,
						sendList , saveList , saveStandList,type,vendorWeightMap,autoIntegrateQtyVendorIds);
				if(StringUtils.isNotEmpty(errorMsg)){//失败
					data.setErrorMsg(errorMsg);
					errorList.add(data);
				}
			}
			//价格缓存清除改为批量
			priceQueryAsyncManager.evictProductPrice(saveList);

			brandAliasMap.clear();
			categoryAliasMap.clear();
			oriProducts.clear();
			oriProductStands.clear();
			supplierIds.clear();
			//保存到数据库
			try{
				productRepository.save(saveList);
				productStandManager.saveProductStands(saveStandList);
			}catch(Exception e){
				logger.error(e.getMessage(),e);
				for(Product p:saveList){
					errorList.add(rawDatas.get(rawDatas.indexOf(p)));
				}
				sendList.clear();//失敗，不再同步搜索引擎
			}
			//更新错误日志
			if(!errorList.isEmpty()&&MaterialVoType.FILE_UPLOAD.equals(materialVo.getType())){
				documentLogManager.updateDocLogsStatusByRaw(errorList);
				errorList.clear();
			}
			if(!sendList.isEmpty()){//mq同步
				try {
					//大于200拆分
					int batchSize = 200;
					if(sendList.size() > batchSize){
						List<ProductVo> newList = new ArrayList<>();
						for(int i = 0;i < sendList.size();i++){
							newList.add(sendList.get(i));
							//拆分后发送消息
							if(i == sendList.size()-1|| newList.size() >= batchSize){
								materialVo.setMsg(newList);
								logger.debug("发送到es:"+JSON.toJSONString(materialVo));
								msgSender.sendMsg(syncElasticsearchProductTopicName, materialVo, null);
								newList.clear();
							}
						}
					}
					//如果没有大于0.8m则直接发送消息
					else{
						materialVo.setMsg(sendList);
						logger.debug("发送到es:"+JSON.toJSONString(materialVo));
						msgSender.sendMsg(syncElasticsearchProductTopicName, materialVo, null);
					}
				} catch (SystemException e) {
					logger.error("JsonProcessingException",e);
					throw new SystemException("JsonProcessingException",e);
				}//传递给mq进行同步
			}
			return "";
		} catch (Exception e) {
			// 异常，还是回调统计次数
			if (MaterialVoType.FILE_UPLOAD.equals(materialVo.getType())) {
				documentLogManager.updateDocLogsStatusByRaw(rawDatas,e.getMessage());
			}
			logger.error(e.getMessage(),e);
		}
		return "";
	}

	public String validateAndCreateProduct(RawData data , Product oriProduct, ProductStand oriProductStand,
			Map<String, ProductBrand> brandAliasMap, Map<String, ProductCategoryParent> categoryAliasMap,
			List<ProductVo> sendList, List<Product> saveList , List<ProductStand> saveStandList,MaterialVoType type,
			Map<String,Integer> vendorWeightMap,Set<String> autoIntegrateQtyVendorIds) {
		
		String errorMsg = StringUtils.EMPTY;
		try{
			List<Product> results = createProduct(data,brandAliasMap,categoryAliasMap,oriProduct,
					oriProductStand,saveStandList,type,vendorWeightMap,autoIntegrateQtyVendorIds);
			if(CollectionUtils.isEmpty(results)){
				return "系统错误创建商品异常!";
			}
			for(Product p:results){
				//增加供应商产品线校验,
				errorMsg = validateProductLine(p);
				if(StringUtils.isEmpty(errorMsg)){
					saveList.add(p);//保存sku
					ProductVo result = new ProductVo(p);
					result.setLineNo(null == data.getLineNo() ? null : data.getLineNo());//加入行号
					sendList.add(result);
				}else{//同SPU,不同SKU，只要其中一条校验失败，直接break
					results.clear();//兼容finally逻辑处理，置空返回结果
					break;
				}
			}
		}catch(BusinessException e){
			if ("NO_BRAND".equals(e.getCode())) {
				errorMsg = "品牌非标准";
			} else if ("NO_CATEGORY".equals(e.getCode())) {
				errorMsg = "分类非标准";
			} else if ("SALE_CONTROL".equals(e.getCode())) {
				errorMsg = "此商品已被管制";
			}
		}catch(Exception e){
			errorMsg = e.getMessage();
		}
		return errorMsg;
	}

	/**
	 * 处理库存信息中图片的地址（/product开头的url需加上前缀）
	 * @param result
	 * @since 2017年3月29日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public List<ProductImage> handleImageUrl(List<ProductImage> spuImage){
		List<ProductImage> spuImageNew = new ArrayList<>();
		if(null == spuImage || spuImage.isEmpty()){
			return spuImageNew;
		}
		for(int j = 0; j < spuImage.size(); j++){
			ProductImage imageInfo = spuImage.get(j);
			String image = imageInfo.getUrl();
			imageInfo.setUrl(calYkyImgDomain(image));
			spuImageNew.add(imageInfo);
		}
		return spuImageNew;
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
		if(imageUrl.length<max)
			max = imageUrl.length;
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
	 * 提供给询报价的接口
	 * 根据  品牌 和 型号  查询商品信息集合
	 * @params manufacturer 品牌名称
	 * @params manufacturerPartNumber 型号
	 * @author tb.huangqingfeng
	 */
	public List<ProductVo> findFacturerAndPartNumber(String manufacturer,String manufacturerPartNumber){
		Map<String,ProductBrand> brandAliasMap = null;
		try{
			brandAliasMap = brandManager.getAliasBrandMap();
			if(brandAliasMap == null){
				return new ArrayList<>();
			}
		}catch(Exception e){
			logger.error("Exception",e);
			throw new SystemException("Exception",e);
		}
		ProductBrand productBrand = brandAliasMap.get(manufacturer.toUpperCase());
		String manufacturerId = null;
		if(null != productBrand){
			manufacturerId = productBrand.getId().toString();
		}
		if(StringUtils.isEmpty(manufacturerId) || StringUtils.isEmpty(manufacturerPartNumber)){
			return new ArrayList<>();
		}
		//查询数据库改为调用搜索引擎
		List<ProductVo> productVos = searchByFacturerAndPartNumber(manufacturerId, manufacturerPartNumber);
		List<ProductVo> result = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(productVos)){
			List<String> ids = productVos.stream().map(ProductVo::getId).collect(Collectors.toList());
			result = this.findFullInfo(ids);
		}
		return result;
	}

	/**
	 * 根据品牌名称和型号查询搜索引擎
	 * @param manufacturer 品牌名称
	 * @param manufacturerPartNumber 型号
	 * @return 商品集合
	 */
	private List<ProductVo> searchByFacturerAndPartNumber(String manufacturer, String manufacturerPartNumber) {
		JSONObject conJson = new JSONObject();
		int size = 50;
		conJson.put("manufacturer", manufacturer);
		conJson.put("manufacturerPartNumber",manufacturerPartNumber);
		conJson.put("size", size);
		Future<JSONObject> future = searchAsyncManager.searchProductInfo(conJson);
		//合并查询后的结果集
		JSONArray resultHit = new JSONArray();
		JSONObject result = null;
		try {
			result = future.get();
			JSONArray re = result.getJSONArray("hits");
			if(CollectionUtils.isNotEmpty(re)){
				resultHit.addAll(re);
			}
		} catch (InterruptedException e) {
			logger.error("getProduct InterruptedException:",e);
			Thread.currentThread().interrupt();
			throw new SystemException(e.getMessage(),e.getCause());
		} catch (ExecutionException e) {
			logger.error("getProduct ExecutionException:",e);
		}
		//去除重复元素
		this.handleHits(resultHit,result,size);

		JSONArray hits = new JSONArray();
		if(result != null && !result.isEmpty()){
			hits = result.getJSONArray("hits");
		}
		//把_id、spu中的_id更改为id,并转成product对象
		return handleProductInfo(hits);
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
	
	 @Autowired
    private MongoTemplate mongoTemplate;


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
	 * 管制商品
	 * @param productId 商品id
	 * @since 2017年6月16日
	 * @author tongkun@yikuyi.com
	 */
	public void saleControl(String productId){
		mongoOptions.updateFirst(query(where("_id").is(productId)), update("saleControl", "1"), Product.class);
	}

	/**
	 * 更新商品中的spu数据
	 *
	 * @since 2017年7月19日
	 * @author tb.lijing@yikuyi.com
	 */
	public void updateProductSpu(RawData data){
		Update up = getProductSpu(data);
		mongoOptions.updateMulti(query(where("spu._id").is(data.getId())), up, "product");
	}

	/**
	 * 拼装更新的对象
	 * @param data
	 * @return
	 * @since 2017年7月19日
	 * @author tb.lijing@yikuyi.com
	 */
	public Update getProductSpu(RawData data){
		Update up = new Update();
		up.set("spu.updatedTimeMillis", Long.toString(new Date().getTime()));
		if(StringUtils.isNotBlank(data.getDescription())){
			up.set("spu.description", data.getDescription());
		}
		if(StringUtils.isNotBlank(data.getRohs())){
			up.set("spu.rohs", data.getRohs());
		}
		if(CollectionUtils.isNotEmpty(data.getVendorCategories())){
			up.set("spu.categories", data.getVendorCategories());
		}
		if(CollectionUtils.isNotEmpty(data.getDocuments())){
			up.set("spu.documents", data.getDocuments());
		}
		if(CollectionUtils.isNotEmpty(data.getImages())){
			up.set("spu.images", data.getImages());
		}
		if(CollectionUtils.isNotEmpty(data.getParameters())){
			up.set("spu.parameters", data.getParameters());
		}
		return up;
	}

	/**
	 *  删除所有数据
	 * @param id
	 * @param manufacturerPartNumber
	 * @param manufacturer
	 * @param vendorId
	 * @param sourceId
	 * @param cate1Id
	 * @param cate2Id
	 * @param cate3Id
	 * @param keyword
	 * @param startDate
	 * @param endDate
	 * @param ids
	 * @param hasQty 是否有库存
	 * @return
	 * @since 2017年8月30日
	 * @author injor.huang@yikuyi.com
	 */
	public Boolean deleteAll(String id, String manufacturerPartNumber,String manufacturer, String vendorId, String sourceId,
		Integer cate1Id, Integer cate2Id, Integer cate3Id, String keyword,
		String startDate, String endDate, Boolean standard, String ids,String hasQty,String isInvalid) {

		//实体里面也没有状态的说明，0是删除状态，
		mongoOptions.updateMulti(this.mergeCondition(id, manufacturerPartNumber, manufacturer, vendorId,
				sourceId, cate1Id, cate2Id, cate3Id, keyword, startDate, endDate, standard,hasQty,isInvalid), update("status", 0), Product.class);

		//通过查询条件异步通知搜索引擎更新
		this.adviceSyncElasticsearchByCondition(id, manufacturerPartNumber, manufacturer, vendorId, sourceId,
				cate1Id, cate2Id, cate3Id, keyword, standard,null,hasQty,isInvalid,startDate,endDate);

		return true;
	}

	public Boolean deleteOtherExceptProcessId(String vendorId,String sourceId, String processId) {
		if(StringUtils.isBlank(processId) || StringUtils.isBlank(vendorId)){
			return false;
		}
		logger.info("delete condition ,vendorId:"+vendorId+" ,sourceId:"+sourceId+" ,processId:"+processId);

		//通过查询条件异步通知搜索引擎更新
		this.adviceSyncElasticsearchByCondition(null, null, null, vendorId, sourceId,null, null, null, null, null,processId,null,null,null,null);

		//删除mongodb 里面的数据
		Criteria criteria = new Criteria();
		criteria.and("status").is(1).
		and("vendorId").is(vendorId).
		and("processId").is(processId);
		if(StringUtils.isNotBlank(sourceId)){
			criteria.and("sourceId").is(sourceId);
		}
		mongoOptions.updateMulti(new Query().addCriteria(criteria), update("status", 0), Product.class);
		return true;
		}

	/**
	 * 通过查询条件异步通知搜索引擎更新
	 * @param id
	 * @param manufacturerPartNumber
	 * @param manufacturer
	 * @param vendorId
	 * @param sourceId
	 * @param cate1Id
	 * @param cate2Id
	 * @param cate3Id
	 * @param keyword
	 * @param standard
	 * @since 2017年9月12日
	 * @author injor.huang@yikuyi.com
	 */
	public void adviceSyncElasticsearchByCondition(String id, String manufacturerPartNumber,String manufacturer, String vendorId, String sourceId,
			Integer cate1Id, Integer cate2Id, Integer cate3Id, String keyword, Boolean standard,String processId,String hasQty,String isInvalid,
			String startDate, String endDate){
		//原厂Id
		if(StringUtils.isNotBlank(manufacturer) || StringUtils.isNotBlank(keyword)){
			Map<String,ProductBrand> brandAliasMap = brandManager.getAliasBrandMap();
			Optional<ProductBrand> optional = null;
			if(StringUtils.isNotBlank(manufacturer)){
				optional = Optional.ofNullable(brandAliasMap.get(manufacturer));
				if(optional.isPresent()){
					manufacturer = optional.get().getId()+"";
				}
			}else if(StringUtils.isNotBlank(keyword)){
				optional = Optional.ofNullable(brandAliasMap.get(keyword));
				if(optional.isPresent()){//说明keyword匹配的是原厂
					manufacturer = optional.get().getId()+"";
				}else{//否则就是匹配型号
					manufacturerPartNumber = keyword;
				}
			}
		}

		SkuDeleteVo deleteVo = new SkuDeleteVo();
		DocumentVo mustCondition = new DocumentVo();
		mustCondition.setId(id);
		mustCondition.setManufacturerPartNumber(manufacturerPartNumber);
		mustCondition.setManufacturer(manufacturer);
		mustCondition.setVendorId(vendorId);
		mustCondition.setSourceId(sourceId);
		mustCondition.setCate1Id(cate1Id);
		mustCondition.setCate2Id(cate2Id);
		mustCondition.setCate3Id(cate3Id);
		mustCondition.setStandard(standard);
		mustCondition.setProcessId(processId);
		mustCondition.setPriceStatus(isInvalid);
		mustCondition.setHasQty(hasQty);

		if(StringUtils.isNotBlank(startDate) && !startDate.equals(endDate)){
			try{

				Long startL = dateFormat.parse(startDate).getTime();
				mustCondition.setMinUpdatedTimeMillis(String.valueOf(startL));
			}catch(ParseException e){
				logger.error("日期格式不正确", e);
			}
		}

		if(StringUtils.isNotBlank(endDate) && !endDate.equals(startDate)){
			try {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(dateFormat.parse(endDate));
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				mustCondition.setMaxUpdatedTimeMillis(String.valueOf(calendar.getTime().getTime()));
			} catch (ParseException e) {
				logger.error("日期格式不正确，", e);
			}
		}

		//开始时间和结束时间相等
		if(StringUtils.isNotBlank(endDate) && endDate.equals(startDate)){
			try {
				Long startL = dateFormat.parse(startDate).getTime();
				mustCondition.setMinUpdatedTimeMillis(String.valueOf(startL));

				Calendar cal = Calendar.getInstance();
				cal.setTime(dateFormat.parse(startDate));
				cal.add(Calendar.DATE, 1);
				mustCondition.setMaxUpdatedTimeMillis(String.valueOf(cal.getTime().getTime()));
			} catch (ParseException e) {
				logger.error("日期格式不正确，", e);
			}
		}


		deleteVo.setMustCondition(mustCondition);

		//清空搜索引擎
		msgSender.sendMsg(syncElasticsearchProductTopicName, deleteVo, null);
	}

	/**
	 * 根据id异步通知同步搜索引擎
	 * @param ids 要同步的数据的id集合
	 * @since 2017年11月2日
	 * @author tongkun@yikuyi.com
	 */
	public void adviceSyncElasticsearchByIds(List<Product> ps){
		List<ProductVo> syncList = new ArrayList<>();
		for(Product p:ps){
			ProductVo pv = new ProductVo();
			BeanUtils.copyProperties(p ,pv);
			pv.setPriceStatus("expired");
			pv.setPrices(new ArrayList<>());
			syncList.add(pv);
		}
		adviceSyncElasticsearch(syncList);
	}

	/**
	 * 根据条件查询产品列表信息
	 * @param id
	 * @param manufacturerPartNumber
	 * @param manufacturer
	 * @param vendorId
	 * @param sourceId
	 * @param cate1Id
	 * @param cate2Id
	 * @param cate3Id
	 * @param keyword
	 * @param startDate
	 * @param endDate
	 * @param ids
	 * @return
	 * @since 2017年8月30日
	 * @author injor.huang@yikuyi.com
	 */
	public File getProductsByCondition(String id, String manufacturerPartNumber,String manufacturer, String vendorId, String sourceId,
			Integer cate1Id, Integer cate2Id, Integer cate3Id, String keyword, String startDate, String endDate,
			Boolean standard,Integer limitQueryCnt,String fileName,String hasQty,String isInvalid)throws BusinessException {
		long time = System.currentTimeMillis();
		File file = FileUtils.getFile(fileName);
		CsvFilePrinter filePrinter;
		try {
			filePrinter = new CsvFilePrinter(file,fileName, ExcelTemplate.Template.EXPORT_SALE_PRODUCT_TEMPLATE.getValue().split(","));
		} catch (IOException e) {
			logger.error("生成模板失败:{}",e);
			return null;
		}
		//拼装查询条件
		JSONObject paramJson = this.assembleQueryCondition(id, manufacturerPartNumber, manufacturer, vendorId, sourceId, cate1Id,
				cate2Id, cate3Id, keyword, startDate, endDate,standard,hasQty,isInvalid);
		int size = 1000;//最大可以查询1百万条数据
		int pageSize = 1000;
		for (int i = 0; i < size; i++) {
			PageRequest pageable = new PageRequest(i,pageSize);
			Page<Product> page = productRepository.findByPage(paramJson,pageable);//限制最大20W条数据
			if(CollectionUtils.isEmpty(page.getContent())){
				break;
			}
			excelExportManager.writeProductData(page.getContent(), filePrinter);
			long total = page.getTotalElements();
			if((i+1)*pageSize >= total){
				break;
			}
			if(limitQueryCnt != null && (i+1)*pageSize >= limitQueryCnt){
				break;
			}
		}
		logger.info("下载产品csv文件耗时:"+(System.currentTimeMillis()-time));
		return file;
	}
	/**
	 * 执行导出操作
	 * @param productRequest
	 * @since 2017年9月21日
	 * @author injor.huang@yikuyi.com
	 * @throws BusinessException
	 */
	public void doExport(ProductRequest productRequest){
		AsyncTaskInfo asyncTaskInfo = new AsyncTaskInfo();
		asyncTaskInfo.setId(productRequest.getTaskId());
		try{
			asyncTaskInfo.setStatus(AsyncTaskInfo.Status.PROCESSING.name());//处理中
			messageClientBuilder.asyncTaskResource().update(asyncTaskInfo,authorizationUtil.getMockAuthorization());
			//根据查询条件生成本地文件
			File excelFile = this.exportExcelByCondition(productRequest, "product"+System.currentTimeMillis()+".csv");
			if(excelFile == null){
				throw new BusinessException("export file error");
			}
			asyncTaskInfo.setStatus(AsyncTaskInfo.Status.SUCCESS.name());//默认成功
	    	//上传本地文件到阿里云并获取文件路径
			asyncTaskInfo.setUrl(productStandManager.uploadFileAliyun(excelFile,"product.export"));
			Date date =new Date();
			asyncTaskInfo.setEndTime(date);
			asyncTaskInfo.setLastUpdateDate(date);
		}catch(BusinessException e){
			logger.error("material task {}update fail：{}",productRequest.getTaskId(),e);
			asyncTaskInfo.setStatus(AsyncTaskInfo.Status.FAIL.name());
			asyncTaskInfo.setMessage(e.getCode());
			asyncTaskInfo.setLastUpdateDate(new Date());
		}
		//更新任务状态
		messageClientBuilder.asyncTaskResource().update(asyncTaskInfo,authorizationUtil.getMockAuthorization());
	}

	/**
	 * 销售中的商品 导出excel
	 * @param id
	 * @param manufacturerPartNumber
	 * @param manufacturer
	 * @param vendorId
	 * @param sourceId
	 * @param cate1Id
	 * @param cate2Id
	 * @param cate3Id
	 * @param keyword
	 * @param startDate
	 * @param endDate
	 * @param ids
	 * @return
	 * @since 2017年8月30日
	 * @author injor.huang@yikuyi.com
	 */
	public File exportExcelByCondition(ProductRequest productRequest,String fileName)throws BusinessException {
		return this.getProductsByCondition(productRequest.getId(), productRequest.getManufacturerPartNumber(),
				productRequest.getManufacturer(), productRequest.getVendorId(), productRequest.getSourceId(),
				productRequest.getCate1Id(),productRequest.getCate2Id(), productRequest.getCate3Id(),
				productRequest.getKeyword(),productRequest.getStartDate(),productRequest.getEndDate(),
				productRequest.getStandard(),LIMIT_QUERY_CNT,fileName,
				productRequest.getHasQty(),
				productRequest.getIsInvalid());//最大获取20W
//		if(CollectionUtils.isEmpty(products)){
//			return null;
//		}
//		return excelExportManager.exportProductDataFile(products,fileName);
	}


	/**
	 * 合并现货交期
	 * @param vo
	 * @param pi
	 * @return
	 * @since 2017年9月1日
	 * @author tb.lijing@yikuyi.com
	 */
	public ProductInfo handleLeadTime(ProductVo vo,ProductInfo pi){
		int minLeadTimeML = null == vo.getMinLeadTimeML() ? 0 : vo.getMinLeadTimeML().intValue();//国内最小现货交期（来自上传）
		int maxLeadTimeML = null == vo.getMaxLeadTimeML() ? 0 : vo.getMaxLeadTimeML().intValue();//国内最大现货交期（来自上传）
		int leadTimeMinCH = StringUtils.isBlank(pi.getLeadTimeMinCH()) ? 0 : Integer.valueOf(pi.getLeadTimeMinCH());//国内最小现货交期（来自交期策略）
		int leadTimeMaxCH = StringUtils.isBlank(pi.getLeadTimeMaxCH()) ? 0 : Integer.valueOf(pi.getLeadTimeMaxCH());//国内最大现货交期（来自交期策略）
		int minLeadTimeHK = null == vo.getMinLeadTimeHK() ? 0 : vo.getMinLeadTimeHK().intValue();// 香港最小现货交期（来自上传）
		int maxLeadTimeHK = null == vo.getMaxLeadTimeHK() ? 0 : vo.getMaxLeadTimeHK().intValue();// 香港最大现货交期（来自上传）
		int leadTimeMinHK = StringUtils.isBlank(pi.getLeadTimeMinHK()) ? 0 : Integer.valueOf(pi.getLeadTimeMinHK());// 香港最小现货交期（来自交期策略）
		int leadTimeMaxHK = StringUtils.isBlank(pi.getLeadTimeMaxHK()) ? 0 : Integer.valueOf(pi.getLeadTimeMaxHK());// 香港最大现货交期（来自交期策略）
		int leadTimeMinTotalCH = leadTimeMinCH+minLeadTimeML;//国内最小现货交期之和（上传+策略之和）
		int leadTimeMaxTotalCH = leadTimeMaxCH+maxLeadTimeML;//国内最大现货交期之和（上传+策略之和）
		int leadTimeMinTotalHK = leadTimeMinHK+minLeadTimeHK;//香港最小现货交期之和（上传+策略之和）
		int leadTimeMaxTotalHK = leadTimeMaxHK+maxLeadTimeHK;//香港最大现货交期之和（上传+策略之和）
		ProductInfo productInfoLead = new ProductInfo();
		if (null!=pi.getProductType() && 0 == pi.getProductType()) {
			productInfoLead.setProductType(pi.getProductType());
			if ("Y".equalsIgnoreCase(pi.getIsShowLeadTime())) {
				if (minLeadTimeML == 0 || maxLeadTimeML == 0) {
					productInfoLead.setLeadTimeCH("0");
				}
				if (minLeadTimeHK == 0 || maxLeadTimeHK == 0) {
					productInfoLead.setLeadTimeHK("0");
				}
				if (minLeadTimeML > 0 && maxLeadTimeML > 0 && leadTimeMaxTotalCH >= leadTimeMinTotalCH) {
					if (leadTimeMinTotalCH != leadTimeMaxTotalCH) {
						productInfoLead.setLeadTimeCH(leadTimeMinTotalCH + "-" + leadTimeMaxTotalCH);
					} else {
						productInfoLead.setLeadTimeCH(String.valueOf(leadTimeMinTotalCH));
					}
				}
				if (minLeadTimeHK > 0 && maxLeadTimeHK > 0 && leadTimeMaxTotalHK >= leadTimeMinTotalHK) {
					if (leadTimeMinTotalHK != leadTimeMaxTotalHK) {
						productInfoLead.setLeadTimeHK(leadTimeMinTotalHK + "-" + leadTimeMaxTotalHK);
					} else {
						productInfoLead.setLeadTimeHK(String.valueOf(leadTimeMinTotalHK));
					}
				}
			} else {
				if (leadTimeMaxTotalCH>0 && leadTimeMinTotalCH>0 && leadTimeMaxTotalCH >= leadTimeMinTotalCH) {
					if (leadTimeMinTotalCH != leadTimeMaxTotalCH) {
						productInfoLead.setLeadTimeCH(leadTimeMinTotalCH + "-" + leadTimeMaxTotalCH);
					} else {
						productInfoLead.setLeadTimeCH(String.valueOf(leadTimeMinTotalCH));
					}
				}
				if (leadTimeMaxTotalHK>0 && leadTimeMinTotalHK>0 && leadTimeMaxTotalHK >= leadTimeMinTotalHK) {
					if (leadTimeMinTotalHK != leadTimeMaxTotalHK) {
						productInfoLead.setLeadTimeHK(leadTimeMinTotalHK + "-" + leadTimeMaxTotalHK);
					} else {
						productInfoLead.setLeadTimeHK(String.valueOf(leadTimeMinTotalHK));
					}
				}
			}
		}else if((minLeadTimeML>0&&maxLeadTimeML>0)||(minLeadTimeHK>0&&maxLeadTimeHK>0)){
			if (leadTimeMaxTotalCH>0 && leadTimeMinTotalCH>0 && leadTimeMaxTotalCH >= leadTimeMinTotalCH) {
				if (leadTimeMinTotalCH != leadTimeMaxTotalCH) {
					productInfoLead.setLeadTimeCH(leadTimeMinTotalCH + "-" + leadTimeMaxTotalCH);
				} else {
					productInfoLead.setLeadTimeCH(String.valueOf(leadTimeMinTotalCH));
				}
			}
			if (leadTimeMaxTotalHK>0 && leadTimeMinTotalHK>0 && leadTimeMaxTotalHK >= leadTimeMinTotalHK) {
				if (leadTimeMinTotalHK != leadTimeMaxTotalHK) {
					productInfoLead.setLeadTimeHK(leadTimeMinTotalHK + "-" + leadTimeMaxTotalHK);
				} else {
					productInfoLead.setLeadTimeHK(String.valueOf(leadTimeMinTotalHK));
				}
			}
		}
		return productInfoLead;
	}

	public ProductInfo handleFactoryLeadTime(ProductVo vo,ProductInfo pi){
		int minFactoryLeadTimeML = null == vo.getMinFactoryLeadTimeML() ? 0 : vo.getMinFactoryLeadTimeML().intValue();// 来自上传
		int maxFactoryLeadTimeML = null == vo.getMaxFactoryLeadTimeML() ? 0 : vo.getMaxFactoryLeadTimeML().intValue();// 来自上传
		int schedulingLeadTimeMinCH = StringUtils.isBlank(pi.getSchedulingLeadTimeMinCH()) ? 0 : Integer.valueOf(pi.getSchedulingLeadTimeMinCH());// 来自交期策略
		int schedulingLeadTimeMaxCH = StringUtils.isBlank(pi.getSchedulingLeadTimeMaxCH()) ? 0 : Integer.valueOf(pi.getSchedulingLeadTimeMaxCH());// 来自交期策略
		int minFactoryLeadTimeHK = null == vo.getMinFactoryLeadTimeHK() ? 0 : vo.getMinFactoryLeadTimeHK().intValue();// 来自上传
		int maxFactoryLeadTimeHK = null == vo.getMaxFactoryLeadTimeHK() ? 0 : vo.getMaxFactoryLeadTimeHK().intValue();// 来自上传
		int schedulingLeadTimeMinHK = StringUtils.isBlank(pi.getSchedulingLeadTimeMinHK()) ? 0 : Integer.valueOf(pi.getSchedulingLeadTimeMinHK());// 来自交期策略
		int schedulingLeadTimeMaxHK = StringUtils.isBlank(pi.getSchedulingLeadTimeMaxHK()) ? 0 : Integer.valueOf(pi.getSchedulingLeadTimeMaxHK());// 来自交期策略
		int factoryLeadTimeMinTotalCH = schedulingLeadTimeMinCH+minFactoryLeadTimeML;//国内最小排单交期之和（上传+策略之和）
		int factoryLeadTimeMaxTotalCH = schedulingLeadTimeMaxCH+maxFactoryLeadTimeML;//国内最大排单交期之和（上传+策略之和）
		int factoryLeadTimeMinTotalHK = schedulingLeadTimeMinHK+minFactoryLeadTimeHK;//香港最小排单交期之和（上传+策略之和）
		int factoryLeadTimeMaxTotalHK = schedulingLeadTimeMaxHK+maxFactoryLeadTimeHK;//香港最大排单交期之和（上传+策略之和）
		ProductInfo productInfoFactory = new ProductInfo();
		if (null!=pi.getProductType() && 1 == pi.getProductType()){
			productInfoFactory.setProductType(pi.getProductType());
			if ("Y".equalsIgnoreCase(pi.getIsShowLeadTime())) {
				if (minFactoryLeadTimeML == 0 || maxFactoryLeadTimeML == 0) {
					productInfoFactory.setSchedulingLeadTimeCH("0");
				}
				if (minFactoryLeadTimeHK == 0 || maxFactoryLeadTimeHK == 0) {
					productInfoFactory.setSchedulingLeadTimeHK("0");
				}
					if (minFactoryLeadTimeML > 0 && maxFactoryLeadTimeML > 0 && factoryLeadTimeMaxTotalCH >= factoryLeadTimeMinTotalCH) {
						if (factoryLeadTimeMinTotalCH != factoryLeadTimeMaxTotalCH) {
							productInfoFactory.setSchedulingLeadTimeCH(factoryLeadTimeMinTotalCH + "-" + factoryLeadTimeMaxTotalCH);
						} else {
							productInfoFactory.setSchedulingLeadTimeCH(String.valueOf(factoryLeadTimeMinTotalCH));
						}
					}
					if (minFactoryLeadTimeHK > 0 && maxFactoryLeadTimeHK > 0 && factoryLeadTimeMaxTotalHK >= factoryLeadTimeMinTotalHK) {
						if (factoryLeadTimeMinTotalHK != factoryLeadTimeMaxTotalHK) {
							productInfoFactory.setSchedulingLeadTimeHK(factoryLeadTimeMinTotalHK + "-" + factoryLeadTimeMaxTotalHK);
						} else {
							productInfoFactory.setSchedulingLeadTimeHK(String.valueOf(factoryLeadTimeMinTotalHK));
						}
					}
			} else {
				if (factoryLeadTimeMaxTotalCH>0 && factoryLeadTimeMinTotalCH>0 && factoryLeadTimeMaxTotalCH >= factoryLeadTimeMinTotalCH) {
					if (factoryLeadTimeMinTotalCH != factoryLeadTimeMaxTotalCH) {
						productInfoFactory.setSchedulingLeadTimeCH(factoryLeadTimeMinTotalCH + "-" + factoryLeadTimeMaxTotalCH);
					} else {
						productInfoFactory.setSchedulingLeadTimeCH(String.valueOf(factoryLeadTimeMinTotalCH));
					}
				}
				if (factoryLeadTimeMaxTotalHK>0 && factoryLeadTimeMinTotalHK>0 && factoryLeadTimeMaxTotalHK >= factoryLeadTimeMinTotalHK) {
					if (factoryLeadTimeMinTotalHK != factoryLeadTimeMaxTotalHK) {
						productInfoFactory.setSchedulingLeadTimeHK(factoryLeadTimeMinTotalHK + "-" + factoryLeadTimeMaxTotalHK);
					} else {
						productInfoFactory.setSchedulingLeadTimeHK(String.valueOf(factoryLeadTimeMinTotalHK));
					}
				}
			}
		}else if((minFactoryLeadTimeML>0&&maxFactoryLeadTimeML>0)||(minFactoryLeadTimeHK>0&&maxFactoryLeadTimeHK>0)){
			if (factoryLeadTimeMaxTotalCH>0 && factoryLeadTimeMinTotalCH>0 && factoryLeadTimeMaxTotalCH >= factoryLeadTimeMinTotalCH) {
				if (factoryLeadTimeMinTotalCH != factoryLeadTimeMaxTotalCH) {
					productInfoFactory.setSchedulingLeadTimeCH(factoryLeadTimeMinTotalCH + "-" + factoryLeadTimeMaxTotalCH);
				} else {
					productInfoFactory.setSchedulingLeadTimeCH(String.valueOf(factoryLeadTimeMinTotalCH));
				}
			}
			if (factoryLeadTimeMaxTotalHK>0 && factoryLeadTimeMinTotalHK>0 && factoryLeadTimeMaxTotalHK >= factoryLeadTimeMinTotalHK) {
				if (factoryLeadTimeMinTotalHK != factoryLeadTimeMaxTotalHK) {
					productInfoFactory.setSchedulingLeadTimeHK(factoryLeadTimeMinTotalHK + "-" + factoryLeadTimeMaxTotalHK);
				} else {
					productInfoFactory.setSchedulingLeadTimeHK(String.valueOf(factoryLeadTimeMinTotalHK));
				}
			}
		}
		return productInfoFactory;
	}

	/**
	 * 合并上传的交期
	 * @param vo
	 * @param pi
	 * @return
	 * @since 2017年9月1日
	 * @author tb.lijing@yikuyi.com
	 */
	public ProductInfo handleUploadLeadTime(ProductVo vo,ProductInfo pi){
		ProductInfo productInfo= new ProductInfo();
		int minLeadTimeML = null == vo.getMinLeadTimeML() ? 0 : vo.getMinLeadTimeML().intValue();// 来自上传
		int maxLeadTimeML = null == vo.getMaxLeadTimeML() ? 0 : vo.getMaxLeadTimeML().intValue();// 来自上传
		int minLeadTimeHK = null == vo.getMinLeadTimeHK() ? 0 : vo.getMinLeadTimeHK().intValue();// 来自上传
		int maxLeadTimeHK = null == vo.getMaxLeadTimeHK() ? 0 : vo.getMaxLeadTimeHK().intValue();// 来自上传
		int minFactoryLeadTimeML = null == vo.getMinFactoryLeadTimeML() ? 0 : vo.getMinFactoryLeadTimeML().intValue();// 来自上传
		int maxFactoryLeadTimeML = null == vo.getMaxFactoryLeadTimeML() ? 0 : vo.getMaxFactoryLeadTimeML().intValue();// 来自上传
		int minFactoryLeadTimeHK = null == vo.getMinFactoryLeadTimeHK() ? 0 : vo.getMinFactoryLeadTimeHK().intValue();// 来自上传
		int maxFactoryLeadTimeHK = null == vo.getMaxFactoryLeadTimeHK() ? 0 : vo.getMaxFactoryLeadTimeHK().intValue();// 来自上传
		if(null == pi.getProductType()){
			productInfo.setProductType(pi.getProductType());
			 if(maxLeadTimeML>0 && minLeadTimeML>0 && maxLeadTimeML>=minLeadTimeML){
				 if(minLeadTimeML!=maxLeadTimeML){
					 productInfo.setLeadTimeCH(minLeadTimeML+"-"+maxLeadTimeML);
				 }else{
					 productInfo.setLeadTimeCH(String.valueOf(minLeadTimeML));
				 }
			 }
			 if(maxLeadTimeHK>0 && minLeadTimeHK>0 && maxLeadTimeHK>=minLeadTimeHK){
				 if(minLeadTimeHK!=maxLeadTimeHK){
					 productInfo.setLeadTimeHK(minLeadTimeHK+"-"+maxLeadTimeHK);
				 }else{
					 productInfo.setLeadTimeHK(String.valueOf(minLeadTimeHK));
				 }
			 }

			  if(maxFactoryLeadTimeML>0 && minFactoryLeadTimeML>0 && maxFactoryLeadTimeML>=minFactoryLeadTimeML){
				 if(minFactoryLeadTimeML!=maxFactoryLeadTimeML){
					 productInfo.setSchedulingLeadTimeCH(minFactoryLeadTimeML+"-"+maxFactoryLeadTimeML);
				 }else{
					 productInfo.setSchedulingLeadTimeCH(String.valueOf(minFactoryLeadTimeML));
				 }
			 }
			 if(minFactoryLeadTimeHK>0 && maxFactoryLeadTimeHK>0 && maxFactoryLeadTimeHK>=minFactoryLeadTimeHK){
				 if(minFactoryLeadTimeHK!=maxFactoryLeadTimeHK){
					 productInfo.setSchedulingLeadTimeHK(minFactoryLeadTimeHK+"-"+maxFactoryLeadTimeHK);
				 }else{
					 productInfo.setSchedulingLeadTimeHK(String.valueOf(minFactoryLeadTimeHK));
				 }
			 }
		}
		return productInfo;
	}

	public ProductVo handleLeadTimeShow(ProductVo vo,ProductInfo pi){
		int minLeadTimeML = null == vo.getMinLeadTimeML() ? 0 : vo.getMinLeadTimeML().intValue();// 来自上传
		int maxLeadTimeML = null == vo.getMaxLeadTimeML() ? 0 : vo.getMaxLeadTimeML().intValue();// 来自上传
		int leadTimeMinCH = StringUtils.isBlank(pi.getLeadTimeMinCH()) ? 0 : Integer.valueOf(pi.getLeadTimeMinCH());// 来自交期策略
		int leadTimeMaxCH = StringUtils.isBlank(pi.getLeadTimeMaxCH()) ? 0 : Integer.valueOf(pi.getLeadTimeMaxCH());// 来自交期策略
		int minLeadTimeHK = null == vo.getMinLeadTimeHK() ? 0 : vo.getMinLeadTimeHK().intValue();// 来自上传
		int maxLeadTimeHK = null == vo.getMaxLeadTimeHK() ? 0 : vo.getMaxLeadTimeHK().intValue();// 来自上传
		int leadTimeMinHK = StringUtils.isBlank(pi.getLeadTimeMinHK()) ? 0 : Integer.valueOf(pi.getLeadTimeMinHK());// 来自交期策略
		int leadTimeMaxHK = StringUtils.isBlank(pi.getLeadTimeMaxHK()) ? 0 : Integer.valueOf(pi.getLeadTimeMaxHK());// 来自交期策略
		int leadTimeMinTotalCH = leadTimeMinCH+minLeadTimeML;//国内最小现货交期之和（上传+策略之和）
		int leadTimeMaxTotalCH = leadTimeMaxCH+maxLeadTimeML;//国内最大现货交期之和（上传+策略之和）
		int leadTimeMinTotalHK = leadTimeMinHK+minLeadTimeHK;//香港最小现货交期之和（上传+策略之和）
		int leadTimeMaxTotalHK = leadTimeMaxHK+maxLeadTimeHK;//香港最大现货交期之和（上传+策略之和）
		ProductVo productVoLead = new ProductVo();
		if (null!=pi.getProductType() && 0 == pi.getProductType()) {
			productVoLead.setProductType(pi.getProductType());
			if ("Y".equalsIgnoreCase(pi.getIsShowLeadTime())) {
				if(minLeadTimeML==0 || maxLeadTimeML==0){
					productVoLead.setMinLeadTimeMLShow(0);
					productVoLead.setMaxLeadTimeMLShow(0);
				}
				if(minLeadTimeHK==0 || maxLeadTimeHK==0){
					productVoLead.setMinLeadTimeHKShow(0);
					productVoLead.setMaxLeadTimeHKShow(0);
				}
				if(minLeadTimeML>0 && maxLeadTimeML>0){
					productVoLead.setMinLeadTimeMLShow(leadTimeMinTotalCH);
					productVoLead.setMaxLeadTimeMLShow(leadTimeMaxTotalCH);
				}
				if(minLeadTimeHK>0 && maxLeadTimeHK>0){
					productVoLead.setMinLeadTimeHKShow(leadTimeMinTotalHK);
					productVoLead.setMaxLeadTimeHKShow(leadTimeMaxTotalHK);
				}
			} else {
				if (leadTimeMinTotalCH > 0 && leadTimeMaxTotalCH > 0) {
					productVoLead.setMinLeadTimeMLShow(leadTimeMinTotalCH);
					productVoLead.setMaxLeadTimeMLShow(leadTimeMaxTotalCH);
				}
				if (leadTimeMinTotalHK > 0 && leadTimeMaxTotalHK > 0) {
					productVoLead.setMinLeadTimeHKShow(leadTimeMinTotalHK);
					productVoLead.setMaxLeadTimeHKShow(leadTimeMaxTotalHK);
				}
			}
		}else if((minLeadTimeML>0&&maxLeadTimeML>0)||(minLeadTimeHK>0&&maxLeadTimeHK>0)){
			if(leadTimeMinTotalCH>0 && leadTimeMaxTotalCH>0){
				productVoLead.setMinLeadTimeMLShow(leadTimeMinTotalCH);
				productVoLead.setMaxLeadTimeMLShow(leadTimeMaxTotalCH);
			}
			if(leadTimeMinTotalHK>0 && leadTimeMaxTotalHK>0){
				productVoLead.setMinLeadTimeHKShow(leadTimeMinTotalHK);
				productVoLead.setMaxLeadTimeHKShow(leadTimeMaxTotalHK);
			}
		}
		return productVoLead;
	}

	public ProductVo handleFactoryLeadTimeShow(ProductVo vo,ProductInfo pi){
		int minFactoryLeadTimeML = null == vo.getMinFactoryLeadTimeML() ? 0 : vo.getMinFactoryLeadTimeML().intValue();// 来自上传
		int maxFactoryLeadTimeML = null == vo.getMaxFactoryLeadTimeML() ? 0 : vo.getMaxFactoryLeadTimeML().intValue();// 来自上传
		int schedulingLeadTimeMinCH = StringUtils.isBlank(pi.getSchedulingLeadTimeMinCH()) ? 0 : Integer.valueOf(pi.getSchedulingLeadTimeMinCH());// 来自交期策略
		int schedulingLeadTimeMaxCH = StringUtils.isBlank(pi.getSchedulingLeadTimeMaxCH()) ? 0 : Integer.valueOf(pi.getSchedulingLeadTimeMaxCH());// 来自交期策略
		int minFactoryLeadTimeHK = null == vo.getMinFactoryLeadTimeHK() ? 0 : vo.getMinFactoryLeadTimeHK().intValue();// 来自上传
		int maxFactoryLeadTimeHK = null == vo.getMaxFactoryLeadTimeHK() ? 0 : vo.getMaxFactoryLeadTimeHK().intValue();// 来自上传
		int schedulingLeadTimeMinHK = StringUtils.isBlank(pi.getSchedulingLeadTimeMinHK()) ? 0 : Integer.valueOf(pi.getSchedulingLeadTimeMinHK());// 来自交期策略
		int schedulingLeadTimeMaxHK = StringUtils.isBlank(pi.getSchedulingLeadTimeMaxHK()) ? 0 : Integer.valueOf(pi.getSchedulingLeadTimeMaxHK());// 来自交期策略
		int factoryLeadTimeMinTotalCH = schedulingLeadTimeMinCH+minFactoryLeadTimeML;//国内最小排单交期之和（上传+策略之和）
		int factoryLeadTimeMaxTotalCH = schedulingLeadTimeMaxCH+maxFactoryLeadTimeML;//国内最大排单交期之和（上传+策略之和）
		int factoryLeadTimeMinTotalHK = schedulingLeadTimeMinHK+minFactoryLeadTimeHK;//香港最小排单交期之和（上传+策略之和）
		int factoryLeadTimeMaxTotalHK = schedulingLeadTimeMaxHK+maxFactoryLeadTimeHK;//香港最大排单交期之和（上传+策略之和）
		ProductVo productVoFactory = new ProductVo();
		if (null!=pi.getProductType() && 1 == pi.getProductType()){
			productVoFactory.setProductType(pi.getProductType());
			if ("Y".equalsIgnoreCase(pi.getIsShowLeadTime())) {
				if(minFactoryLeadTimeML==0 || maxFactoryLeadTimeML==0){
					productVoFactory.setMinFactoryLeadTimeMLShow(0);
					productVoFactory.setMaxFactoryLeadTimeMLShow(0);
				}
				if(minFactoryLeadTimeHK==0 || maxFactoryLeadTimeHK==0){
					productVoFactory.setMinFactoryLeadTimeHKShow(0);
					productVoFactory.setMaxFactoryLeadTimeHKShow(0);
				}
				if(minFactoryLeadTimeML>0 && maxFactoryLeadTimeML>0){
					productVoFactory.setMinFactoryLeadTimeMLShow(factoryLeadTimeMinTotalCH);
					productVoFactory.setMaxFactoryLeadTimeMLShow(factoryLeadTimeMaxTotalCH);
				}
				if(minFactoryLeadTimeHK>0 && maxFactoryLeadTimeHK>0){
					productVoFactory.setMinFactoryLeadTimeHKShow(factoryLeadTimeMinTotalHK);
					productVoFactory.setMaxFactoryLeadTimeHKShow(factoryLeadTimeMaxTotalHK);
				}
			} else {
				if(factoryLeadTimeMinTotalCH>0 && factoryLeadTimeMaxTotalCH>0){
					productVoFactory.setMinFactoryLeadTimeMLShow(factoryLeadTimeMinTotalCH);
					productVoFactory.setMaxFactoryLeadTimeMLShow(factoryLeadTimeMaxTotalCH);
				}
				if(factoryLeadTimeMinTotalHK>0 && factoryLeadTimeMaxTotalHK>0){
					productVoFactory.setMinFactoryLeadTimeHKShow(factoryLeadTimeMinTotalHK);
					productVoFactory.setMaxFactoryLeadTimeHKShow(factoryLeadTimeMaxTotalHK);
				}
			}
		}else if((minFactoryLeadTimeML>0&&maxFactoryLeadTimeML>0)||(minFactoryLeadTimeHK>0&&maxFactoryLeadTimeHK>0)){
			if(factoryLeadTimeMinTotalCH>0 && factoryLeadTimeMaxTotalCH>0){
				productVoFactory.setMinFactoryLeadTimeMLShow(factoryLeadTimeMinTotalCH);
				productVoFactory.setMaxFactoryLeadTimeMLShow(factoryLeadTimeMaxTotalCH);
			}
			if(factoryLeadTimeMinTotalHK>0 && factoryLeadTimeMaxTotalHK>0){
				productVoFactory.setMinFactoryLeadTimeHKShow(factoryLeadTimeMinTotalHK);
				productVoFactory.setMaxFactoryLeadTimeHKShow(factoryLeadTimeMaxTotalHK);
			}
		}
		return productVoFactory;
	}

	public ProductVo handleUploadLeadTimeShow(ProductVo vo,ProductInfo pi){
		int minLeadTimeML = null == vo.getMinLeadTimeML() ? 0 : vo.getMinLeadTimeML().intValue();// 来自上传
		int maxLeadTimeML = null == vo.getMaxLeadTimeML() ? 0 : vo.getMaxLeadTimeML().intValue();// 来自上传
		int minLeadTimeHK = null == vo.getMinLeadTimeHK() ? 0 : vo.getMinLeadTimeHK().intValue();// 来自上传
		int maxLeadTimeHK = null == vo.getMaxLeadTimeHK() ? 0 : vo.getMaxLeadTimeHK().intValue();// 来自上传
		int minFactoryLeadTimeML = null == vo.getMinFactoryLeadTimeML() ? 0 : vo.getMinFactoryLeadTimeML().intValue();// 来自上传
		int maxFactoryLeadTimeML = null == vo.getMaxFactoryLeadTimeML() ? 0 : vo.getMaxFactoryLeadTimeML().intValue();// 来自上传
		int minFactoryLeadTimeHK = null == vo.getMinFactoryLeadTimeHK() ? 0 : vo.getMinFactoryLeadTimeHK().intValue();// 来自上传
		int maxFactoryLeadTimeHK = null == vo.getMaxFactoryLeadTimeHK() ? 0 : vo.getMaxFactoryLeadTimeHK().intValue();// 来自上传
		ProductVo productVo = new ProductVo();
		if (null == pi.getProductType()) {
			if(minLeadTimeML>0 && maxLeadTimeML>0){
				productVo.setMinLeadTimeMLShow(minLeadTimeML);
				productVo.setMaxLeadTimeMLShow(maxLeadTimeML);
			}
			if(minLeadTimeHK>0 && maxLeadTimeHK>0){
				productVo.setMinLeadTimeHKShow(minLeadTimeHK);
				productVo.setMaxLeadTimeHKShow(maxLeadTimeHK);
			}
			if(minFactoryLeadTimeML>0 && maxFactoryLeadTimeML>0){
				productVo.setMinFactoryLeadTimeMLShow(minFactoryLeadTimeML);
				productVo.setMaxFactoryLeadTimeMLShow(maxFactoryLeadTimeML);
			}
			if(minFactoryLeadTimeHK>0 && maxFactoryLeadTimeHK>0){
				productVo.setMinFactoryLeadTimeHKShow(minFactoryLeadTimeHK);
				productVo.setMaxFactoryLeadTimeHKShow(maxFactoryLeadTimeHK);
			}
		}
		return productVo;
	}


	/**
	 * 根据物料更新产品信息
	 * @param standAudits
	 * @since 2017年9月4日
	 * @author injor.huang@yikuyi.com
	 * @throws BusinessException
	 */
	public void updateProductBySpus(List<ProductStand> stands){
		List<String> skuIds = Lists.newArrayList();
		stands.stream().forEach(stand -> {
			mongoTemplate.updateMulti(query(Criteria.where("spu._id").is(stand.getId())), update("spu", stand), Product.class);
			skuIds.add(stand.getId());
		});
		List<Product> products = productRepository.findProductBySkuIds(skuIds);
		if(CollectionUtils.isEmpty(products)){
			logger.error("There is no data according to skuIds query products");
			return;
		}
		logger.info("根据"+stands.size()+"条的skuIds查询到商品"+products.size()+"条");
		//清除原来的价格缓存
		Cache productPriceCache = cacheManager.getCache("productPriceCache");
		// 搜索引擎同步
		List<ProductVo> productVos = Lists.newArrayList();
		products.stream().forEach(product -> {
			try {
				productPriceCache.evict("ProductPriceCache-"+product.getId());
				ProductVo opProductVo = new ProductVo();
				BeanUtils.copyProperties(product ,opProductVo);
				productVos.add(opProductVo);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		});
		//异步通知搜索引擎更新
		this.adviceSyncElasticsearch(productVos);
	}

	/**
	 * 校验SKU下的SPU是否符合对应供应商下的产品线
	 * @param sku
	 * @return 失败返回原因,成功无返回
	 * @since 2017年9月7日
	 * @author jik.shu@yikuyi.com
	 */
	public String validateProductLine(Product sku){
		//sku为null或者sku下的spu为null，直接通过校验.
		if(null == sku || null == sku.getSpu()){
			return StringUtils.EMPTY;
		}
		//sku下供应商ID没有直接通过校验
		if(StringUtils.isEmpty(sku.getVendorId())){
			return StringUtils.EMPTY;
		}
		if(null == sku.getSpu().getManufacturerId()){
			return StringUtils.EMPTY;
		}
		String vendorId = sku.getVendorId();
		Map<String,Boolean> map = partyClientUtils.getProductLIneBySupllierId(vendorId);
		if(map.isEmpty()){
			return StringUtils.EMPTY;
		}

		String manufacturerId = sku.getSpu().getManufacturerId().toString();
		String manufacturerName = sku.getSpu().getManufacturer();
		String cat1Id = StringUtils.EMPTY;
		String cat2Id = StringUtils.EMPTY;
		String cat3Id = StringUtils.EMPTY;
		String cat1Name = StringUtils.EMPTY;
		String cat2Name = StringUtils.EMPTY;
		String cat3Name = StringUtils.EMPTY;
		StringBuilder cateSb =  new StringBuilder("");
		if(CollectionUtils.isNotEmpty(sku.getSpu().getCategories()) && sku.getSpu().getCategories().size() >= 3){
			 cat1Id = sku.getSpu().getCategories().get(0).getId().toString();
			 cat2Id = sku.getSpu().getCategories().get(1).getId().toString();
			 cat3Id = sku.getSpu().getCategories().get(2).getId().toString();
			 cat1Name = sku.getSpu().getCategories().get(0).getName();
			 cat2Name = sku.getSpu().getCategories().get(1).getName();
			 cat3Name = sku.getSpu().getCategories().get(2).getName();
			cateSb.append(",分类").append(cat1Name).append(",").append(cat2Name).append(",").append(cat3Name);
		}
		String[] keysArray = new String[]{manufacturerId,cat1Id,cat2Id,cat3Id};
		String key = PartyClientUtils.getSupplierProductLineCacheKey(manufacturerId,cat1Id,cat2Id,cat3Id);
		int count = 4;
		//默认先判断不代理的产品线
		while(count>0){
			if(map.containsKey(key) && !MapUtils.getBooleanValue(map, key)){
				return new StringBuilder().append("原厂").append(manufacturerName).append(cateSb).append("不符合供应商下的产品线规则.").toString();
			}else{
				count--;
				keysArray[count] = "*";
				key = PartyClientUtils.getSupplierProductLineCacheKey(keysArray[0], keysArray[1], keysArray[2] ,keysArray[3]);
			}
		}
		//再判断代理有的产品线
		if(map.containsKey(PartyProductLineVo.Type.PROXY.name())){
			String[] keysArrayTwo = new String[]{manufacturerId,cat1Id,cat2Id,cat3Id};
			String keyTwo = PartyClientUtils.getSupplierProductLineCacheKey(manufacturerId,cat1Id,cat2Id,cat3Id);
			int countTwo = 4;
			while(countTwo>0){
				if(map.containsKey(keyTwo) && MapUtils.getBooleanValue(map, keyTwo)){
					return StringUtils.EMPTY;
				}else{
					countTwo--;
					keysArrayTwo[countTwo] = "*";
					keyTwo = PartyClientUtils.getSupplierProductLineCacheKey(keysArrayTwo[0], keysArrayTwo[1], keysArrayTwo[2] ,keysArrayTwo[3]);
				}
			}
		}else{
			return StringUtils.EMPTY;
		}
		return new StringBuilder().append("原厂").append(manufacturerName).append(cateSb).append("不符合供应商下的产品线规则.").toString();
	}

	/**
	 * 导出
	 * @param productRequest
	 * @since 2017年9月21日
	 * @author injor.huang@yikuyi.com
	 * @throws BusinessException
	 */
	@Audit(action = "SaleProduct Exportqqq;;;'#id'qqq;;;'#userName'导出", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public void export(ProductRequest productRequest,
			@com.framework.springboot.audit.Param("userName")String userName) throws BusinessException {
		//检验能否下载  同用户同查询条件缓存15分钟
		String key = JSON.toJSONString(productRequest) + RequestHelper.getLoginUserId();
		Cache cache = cacheManager.getCache("saleProductExport");
		ValueWrapper valueWrapper = cache.get(key);
		if(valueWrapper != null){
			throw new BusinessException("请勿重复导出");
		}
		cache.put(key, key);
		//创建下载任务
		AsyncTaskInfo asyncTaskInfo = productStandManager.createTask(AsyncTaskInfo.BizType.PRODUCT.name(),AsyncTaskInfo.Action.DOWNLOAD.name());
		productRequest.setTaskId(asyncTaskInfo.getId());
		//异步调用下载
		msgSender.sendMsg(parseImportFileTopicName,productRequest, null);
	}

	/**
	 * 根据供应商id或者是品牌id查询热销型号数据
	 * @param arg
	 * @param flag
	 * @return
	 * @since 2017年10月26日
	 * @author tb.lijing@yikuyi.com
	 */
	public List<ProductVo> findProductInfobyCondition(String arg, String flag){
		List<ProductVo> productVoList = new ArrayList<>();
		List<OrderItem> orderItemResult = null;
		int index = 0;
		//v表示根据供应商id维度查询热销型号数据
		if("v".equalsIgnoreCase(flag)){
			orderItemResult = transactionClientBuilder.orderStatisticsClient().searchOrderProductRank(arg, null, 1, 50, authorizationUtil.getMockAuthorization());
			if(CollectionUtils.isNotEmpty(orderItemResult)){
				rmark:
				for(OrderItem order : orderItemResult){
					ProductVo productVo = productRepository.findProductByVendIdAndId(order.getProductId(), arg, 1);
					if(null!=productVo && null!=productVo.getSpu() && CollectionUtils.isNotEmpty(productVo.getPrices()) && productVo.getQty()>0 && productVo.getSpu().getImages().size()>0){
						for(ProductPrice price : productVo.getPrices()){
							if(CollectionUtils.isNotEmpty(price.getPriceLevels())){
								productVoList.add(productVo);
								index++;
								if(index==10){
									break rmark;
								}
							}
						}
					}
				}
			}
		//b表示根据品牌id维度查询热销型号数据
		}else if("b".equalsIgnoreCase(flag)){
			orderItemResult = transactionClientBuilder.orderStatisticsClient().searchOrderProductRank(null, arg, 1, 50, authorizationUtil.getMockAuthorization());
			if(CollectionUtils.isNotEmpty(orderItemResult)){
				rmark:
				for(OrderItem order : orderItemResult){
					ProductVo productVo = productRepository.findProductByManufacturerIdAndId(order.getProductId(),Integer.valueOf(arg), 1);
					if(null!=productVo && null!=productVo.getSpu() && CollectionUtils.isNotEmpty(productVo.getPrices()) && productVo.getQty()>0 && productVo.getSpu().getImages().size()>0){
						for(ProductPrice price : productVo.getPrices()){
							if(CollectionUtils.isNotEmpty(price.getPriceLevels())){
								productVoList.add(productVo);
								index++;
								if(index==10){
									break rmark;
								}
							}
						}
					}
				}
			}
		}
		return productVoList;
	}

	 /**
	  * 按时间下架商品(总）
	  * 1、查询总数
	  * 2、从末页开始向前面查询id
	  * 3、将id以mq的形式发出
	  * @param vendorId 供应商id
	  * @param valiTime 过期时间
	  * @return 下架的数量
	  */
	 public long downShelfStock(String vendorId,long validTime){
		 FindIterable<Document> cur = pc.getCollection().find(new Document("vendorId",vendorId)
				 .append("updatedTimeMillis", new Document("$lt",String.valueOf(validTime)))
				 .append("priceStatus", new Document("$in",Arrays.asList(new String[]{null}))));
		 ArrayList<Product> sendList = new ArrayList<>();
		 Consumer<Document> c = new Consumer<Document>(){
			 int count = 0;
				@Override
				public void accept(Document arg0) {
					count++;
					try {
						arg0.put("id",arg0.get("_id"));
						Document spu = (Document)arg0.get("spu");
						spu.put("id",spu.get("_id"));
						sendList.add(mapper.readValue(mapper.writeValueAsString(arg0), Product.class));
					} catch (IOException e) {
						logger.error(e.getMessage(),e);
					}
					if(sendList.size()==ProductPageable.PAGESIZE){
						logger.info("自动失效digikey,mouser数量："+count);
						msgSender.sendMsg(invalidProductTopicName, sendList, null);
						sendList.clear();
					}
				}
			 };
		 cur.forEach(c);
		 if(!sendList.isEmpty()){
			msgSender.sendMsg(invalidProductTopicName, sendList, null);
			sendList.clear();
		 }
		 logger.info("自动失效digikey,mouser完成");
		 return 0;
	 }

	 /**
	  * 将一批库存更新为失效
	 * @param list 库存数据
	 */
	public void updateInvalidProduct(List<Product> list){
		//1、拼接更新信息
		List<String> blankList = new ArrayList<>();
		Update up = update(UPDATED_TIME_MILLIS_FIELD_NAME,Long.toString(new Date().getTime()));
		up.set("prices", blankList);
		up.set("priceStatus", "expired");

		//2、拼接要更新的id
		List<String> idList = new ArrayList<>();
		for(Product p:list){
			if(p.getId()!=null)
				idList.add(p.getId());
		}

		//3、执行更新
		mongoOptions.updateMulti(query(where("_id").in(idList)), up, Product.class);

		//4、同步搜索引擎
		adviceSyncElasticsearchByIds(list);
	 }

	/**
	 * 发送商品失效通知邮件
	 */
	public void adviceInvalidProduct(){
		Set<String> vendors = partyClientUtils.getAutoIntegrateQtyVendorIds(false);
		logger.info("开始启动失效通知定时任务！");
		// 1.筛选出需要发送失效通知的供应商
		vendors.stream().forEach(v -> msgSender.sendMsg(invalidProductTopicName, v, null));
	}

	public void processVendorProduct( String supplierId,int page,int pageSize,int invalidCount,int waitForAdviceCount,String lastId) {
		//2.分批查询供应商有效商品数据
		JSONObject condition = new JSONObject();
		//condition.put("status", 1);
		condition.put("vendorId", supplierId);
		
		Document greatThan = new Document();
		greatThan.put("$gt", lastId);
		condition.put("_id", greatThan);

		/*JSONObject expiryDateJson = new JSONObject();
		expiryDateJson.put("$exists", true);
		condition.put("expiryDate", expiryDateJson);*/

		//排除已过期的
		/*JSONObject priceStatusNe = new JSONObject();
		priceStatusNe.put("$ne", "expired");
		condition.put("priceStatus", priceStatusNe);*/
		
		logger.info("开始分批查询供应商商品数据！vendorName:{},invalidCount:{},waitForAdviceCount:{},page:{}",supplierId,invalidCount,waitForAdviceCount,page);
		Sort sort = new Sort(Direction.ASC,"_id");
		PageRequest pageRequest = new PageRequest(page,pageSize,sort);
		List<Product> result = productRepository.findListByPage(condition, pageRequest);
		logger.info("查询成功！vendorName:{},product num:{}",supplierId,result.size());
		
		ArrayList<Product> invalidDatas = new ArrayList<>();
		//List<Product> waitForAdviceDatas = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(result)){
			//标记最后处理的id
			Product lastProduct = result.get(result.size()-1);
			lastId = lastProduct.getId();

			List<Product> products = result.stream().filter(product -> {
				if(StringUtils.isNotEmpty(product.getExpiryDate()) && !"expired".equals(product.getPriceStatus()) && product.getStatus() != null && product.getStatus().equals(1)){
					return true;
				}
				return false;
			}).collect(Collectors.toList());

			if(CollectionUtils.isNotEmpty(products)){
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

				for (Product product : products) {
					long expiryDate = StringUtils.isNotEmpty(product.getExpiryDate()) ? Long.valueOf(product.getExpiryDate()):0;
					//计算失效时间
					long nowTimeMillis = 0;
					try {
						nowTimeMillis = format.parse(format.format(new Date())).getTime();
					} catch (Exception e) {
						logger.error("method processVendorProduct parse date error!");
					}
					int invalidDays = (int) Math.ceil(Float.valueOf((expiryDate - nowTimeMillis)/1000/60/60/24));
					logger.info("过期天数:{},商品ID:{}",invalidDays,product.getId());

					//如果更新时间在失效前第7天当天内，发送失效MQ和失效邮件
					if(invalidDays <= 7  && invalidDays >0 && expiryDate !=0){
						waitForAdviceCount ++ ;
						//waitForAdviceDatas.add(product);
					}

					//筛选出已失效的数据
					if(nowTimeMillis >= expiryDate && expiryDate !=0){
						invalidCount ++;
						invalidDatas.add(product);
					}
				}
			}

			//发送已失效数据的MQ
			//大于200拆分
			int batchSize = 100;
			if(invalidDatas.size() > batchSize){
				ArrayList<Product> newList = new ArrayList<>();
				for(int i = 0;i < invalidDatas.size();i++){
					newList.add(invalidDatas.get(i));
					//拆分后发送消息
					if(i == invalidDatas.size()-1|| newList.size() >= batchSize){
						logger.info("开始发送商品失效MQ");
						msgSender.sendMsg(invalidProductTopicName, newList, null);
						newList.clear();
					}
				}
			}else{
				//如果没有大于0.8m则直接发送消息
				logger.info("开始发送商品失效MQ");
				msgSender.sendMsg(invalidProductTopicName, invalidDatas, null);
			}
			
			//page++;
			this.processVendorProduct(supplierId,page,pageSize,invalidCount,waitForAdviceCount,lastId);
		}else{
			//发送即将失效通知
			if(waitForAdviceCount > 0){
				sendInvalidMail(waitForAdviceCount,"N",supplierId);
			}
			//将已到期的商品改为失效
			if(invalidCount > 0){
				sendInvalidMail(invalidCount,"Y",supplierId);
			}
		}
	}

	private void sendInvalidMail( int productCount,String isHasInValid,String supplierId) {
		//按供应商进行分组
		//final Map<String, List<Product>>  productsByVendor = waitForAdviceDatas.stream().collect(Collectors.groupingBy(Product::getVendorId));
		SupplierMailVo mailVo = partyClientBuilder.supplierClient().getSuplierRelationShipMail(supplierId);
		
		//按负责人分组
		if(mailVo != null && CollectionUtils.isNotEmpty(mailVo.getPrincipalMails())){
			String principalMail = StringUtils.join(mailVo.getPrincipalMails(),";");
			//for (String principalMail : vendorsGroupByPrincipal.keySet()) {
				
				//List<PartyVo> partyVos = vendorsGroupByPrincipal.get(principalMail);
				StringBuilder contentStr = new StringBuilder();
				//if(CollectionUtils.isNotEmpty(partyVos)) {
					//for (PartyVo party : partyVos) {
						//List<Product> vendorProducts = productsByVendor.get(party.getId());
						//int productCount = CollectionUtils.isNotEmpty(vendorProducts)? vendorProducts.size():0;
						if(productCount==0){
							return;
						}

						String expiryDateStr = "";
						if("Y".equals(isHasInValid)){
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
							expiryDateStr = format.format(new Date());
						}else{
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(new Date());
							calendar.add(Calendar.DAY_OF_MONTH, 7);
							expiryDateStr = format.format(calendar.getTime());
						}
						
						contentStr.append("<tr style=\"height:47px;\"> <td style=\"height:30px;padding-left:20px;padding-right: 15px; word-break:break-all;text-overflow:ellipsis; white-space:nowrap; overflow:hidden;text-align: center;\" ><span style=\"max-width:100%;\">"+mailVo.getSupplierName()+"</span></td><td style=\"height:30px;text-align: center;\">"+productCount+"条</td> <td style=\"height:30px;text-align: center;\">"+expiryDateStr+"</td></tr>");
						
					//}
				//}
				if(StringUtils.isEmpty(contentStr.toString())){
					return;
				}
				MailInfoVo mailInfoVo = new MailInfoVo();
				//主送负责人
				mailInfoVo.setTo(principalMail);
				//抄送询价人
				if(CollectionUtils.isNotEmpty(mailVo.getInquiryMails())){
					String cc = StringUtils.join(mailVo.getInquiryMails(), ";");
					mailInfoVo.setCc(cc);
				}
				mailInfoVo.setTemplateId("VENDOR_PRODUCT_INVALID_ADVICE");
				mailInfoVo.setType("EMAIL");
				JSONObject content = new JSONObject();
				content.put("toAddress", mailInfoVo.getTo());
				content.put("trData", contentStr.toString());
				if("Y".equals(isHasInValid)){
					content.put("isHasInValidTitile", "已失效数据");
				}else{
					content.put("isHasInValidTitile", "将失效数据");
				}

				mailInfoVo.setContent(content);
				logger.info("开始发送邮件,toAddress：{}",mailInfoVo.getTo());
				msgSender.sendMsg(sendMsgAndEmailTopicName, mailInfoVo, null);
			//}

		}
	}

	/**
	 * 查询分销商、制造商最新商品
	 * @param arg
	 * @param flag
	 * @return
	 * @since 2017年11月29日
	 * @author tb.lijing@yikuyi.com
	 */
	public List<ProductVo> findNewProducts(String arg, String flag){
		JSONObject conJson = new JSONObject();
		conJson.put("showQty", 1);
		conJson.put("size", 10);
		if("v".equalsIgnoreCase(flag)){
			conJson.put("vendorId", arg);
		}else{
			conJson.put("manufacturer", arg);
		}
		//查询商品信息
		JSONObject resultInfo = null;
		try {
			resultInfo = searchManager.searchProduct(conJson,null);
		} catch (Exception e) {
			logger.error("从搜索引擎中查询商品信息失败!错误信息{}", e);
		}
		JSONArray hits = new JSONArray();
		if(resultInfo != null && !resultInfo.isEmpty()){
			hits = resultInfo.getJSONArray("hits");
		}
		//把_id、spu中的_id更改为id,并转成product对象
		List<ProductVo> productList = handleProductInfo(hits);
		return productList;
	}

	/**
	 * 拼装查询条件
	 * @param product
	 * @return
	 * @since 2017年11月29日
	 * @author tb.lijing@yikuyi.com
	 */
	public Query mergeCondition(Product product){
		Query query = new Query();
		Criteria criteria = new Criteria();
		Criteria criteria2 = new Criteria();
		criteria.and("status").is(1);
		if(StringUtils.isNotBlank(product.getVendorId())){
			criteria.and("vendorId").is(product.getVendorId());
		}else{
			criteria.and("spu.manufacturerId").is(product.getSpu().getManufacturerId());
		}
		criteria2.ne(null);
		criteria.and("prices.priceLevels").elemMatch(criteria2);
		criteria.and("qty").gt(0);
		criteria.and("spu.images").elemMatch(criteria2);
		Sort sort = new Sort(Direction.DESC,"updatedTimeMillis");
		query.with(sort);
		query.limit(10);
		query.addCriteria(criteria);
		return query;

	}
	
	/**
	 * 获取汇率
	 * 先从guava里取，没有取到从redis里取，否则从接口取
	 * @param src
	 * @param target
	 * @return
	 * @since 2018年1月11日
	 * @author tb.lijing@yikuyi.com
	 */
	public Double getExchangeRate(String src,String target){
		if(StringUtils.isBlank(src)||StringUtils.isBlank(target)){
			return 0D;
		}
		Double retExchangeRate = 0D;
		try {
			retExchangeRate = exchangeRateCache.get(src+"-"+target, ()->{
				Cache exchangeRateCache = cacheManager.getCache("exchangeRateCache");
				Map<String,Object> returnRates = exchangeRateCache.get("exchangeRateCache-"+src.toUpperCase()+"-"+target.toUpperCase(),Map.class);
				UomConversionClient client = shipmentClientBuilder.uomConversionResource();
				returnRates = returnRates == null ? client.getById(Currency.HKD, Currency.USD) : returnRates;
				Double exchangeRate = 0D;
				if(!returnRates.isEmpty() && null!=returnRates.get("exchangeRate")){
					exchangeRate = new Double(returnRates.get("exchangeRate").toString());
				}
				return exchangeRate;
			});
		} catch (ExecutionException e) {
			logger.error("无法加载汇率 src:"+src+" target:"+target,e);
		}
		return retExchangeRate;
	}
	
	/**
	 * 搜索页你是不是想找的接口
	 * @return 联想结果
	 */
	public List<String> searchRecommond(String keyword){
		try {
			return productAsyncManager.searchRecommondAsync(keyword).get();
		} catch (Exception e) {
			logger.warn("是不是想找联想接口错误,error is :{}",e);
		}
		return Collections.emptyList();
	}
	
	/**
	 * 批量根据  品牌Id 和 型号  查商品信息
	 * @author injor.huang
	 * @date 2018年1月31日
	 * @param productRequests
	 * @return
	 */
	public List<ProductVo> findBatchMfrIdAndMpm(List<ProductRequest> productRequests) {
		List<ProductVo> productList = new ArrayList<>();
		List<Document> documents = Lists.newArrayList();
		productRequests.stream().forEach(a -> {
			SkuSearchCondition condition = new SkuSearchCondition();
			condition.setMfrId(Integer.valueOf(a.getManufacturerId()));
			condition.setMpn(a.getManufacturerPartNumber());
			Future<EsResult<Document>> future = searchAsyncManager.searchProductInfo(condition, 0, 100);
			EsResult<Document> result = null;
			try {
				result = future.get();
				if(CollectionUtils.isNotEmpty(result.getHits())){
					documents.addAll(result.getHits());
				}
			} catch (InterruptedException e) {
				logger.error("getProduct InterruptedException:",e);
				Thread.currentThread().interrupt();
				throw new SystemException(e.getMessage(),e.getCause());
			} catch (ExecutionException e) {
				logger.error("getProduct ExecutionException:",e);
			}
		});
		if(CollectionUtils.isEmpty(documents)){
			return productList;
		}
		//解析数据
		this.parserData(documents,productList);
		
		if(CollectionUtils.isNotEmpty(productList)){
			List<String> ids = productList.stream().map(ProductVo::getId).collect(Collectors.toList());
			productList = this.findFullInfo(ids);
		}
		return productList;
	}
	
	/**
	 * 解析数据
	 * @author injor.huang
	 * @date 2018年1月31日
	 * @param documents
	 * @param productList
	 */
	public void parserData(List<Document> documents,List<ProductVo> productList){
		documents.stream().forEach(info -> {
			ProductVo productVo = new ProductVo(); 
			String productId = StringUtils.isEmpty(ObjectUtils.toString(info.get("_id"))) ? ObjectUtils.toString(info.get("_ID")) : ObjectUtils.toString(info.get("_id"));
			String sourceId = StringUtils.isEmpty(ObjectUtils.toString(info.get("sourceId"))) ? ObjectUtils.toString(info.get("SOURCEID")) : ObjectUtils.toString(info.get("sourceId"));
			String skuId = StringUtils.isEmpty(ObjectUtils.toString(info.get("skuId"))) ? ObjectUtils.toString(info.get("SKUID")) : ObjectUtils.toString(info.get("skuId"));
			String vendorId = StringUtils.isEmpty(ObjectUtils.toString(info.get("vendorId"))) ? ObjectUtils.toString(info.get("VENDORID")) : ObjectUtils.toString(info.get("vendorId"));
			productVo.setId(productId);
			productVo.setSkuId(skuId);
			productVo.setVendorId(vendorId);
			productVo.setSourceId(sourceId);
			//转成product对象
			productList.add(productVo);
			
		});
	}
}