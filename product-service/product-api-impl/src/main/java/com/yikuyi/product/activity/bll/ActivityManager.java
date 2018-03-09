package com.yikuyi.product.activity.bll;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.framework.springboot.config.ObjectMapperHelper;
import com.framework.springboot.model.LoginUser;
import com.framewrok.springboot.web.RequestHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.ictrade.tools.JedisUtils;
import com.ictrade.tools.export.ExportFactory;
import com.ictrade.tools.export.ExportProcesser;
import com.ictrade.tools.leadin.LeadInFactorySax;
import com.yikuyi.activity.model.Activity;
import com.yikuyi.activity.model.Activity.Status;
import com.yikuyi.activity.model.ActivityPeriods;
import com.yikuyi.activity.model.ActivityProduct;
import com.yikuyi.activity.model.ActivityProductDraft;
import com.yikuyi.activity.vo.ActivityProductDraftVo;
import com.yikuyi.activity.vo.ActivityProductVo;
import com.yikuyi.activity.vo.ActivityVo;
import com.yikuyi.activity.vo.SupplierRuleVo;
import com.yikuyi.brand.model.ProductBrand;
import com.yikuyi.category.model.ProductCategory;
import com.yikuyi.document.model.Document;
import com.yikuyi.document.model.Document.DocumentStatus;
import com.yikuyi.document.model.Document.DocumentType;
import com.yikuyi.party.PartyClientBuilder;
import com.yikuyi.party.facility.model.Facility;
import com.yikuyi.party.supplier.SupplierVo;
import com.yikuyi.product.activity.dao.ActivityDao;
import com.yikuyi.product.activity.dao.ActivityDraftDao;
import com.yikuyi.product.activity.dao.ActivityPeriodsDao;
import com.yikuyi.product.activity.dao.ActivityPeriodsDraftDao;
import com.yikuyi.product.activity.dao.ActivityProductDao;
import com.yikuyi.product.activity.dao.ActivityProductDraftDao;
import com.yikuyi.product.brand.manager.BrandManager;
import com.yikuyi.product.common.utils.BusiErrorCode;
import com.yikuyi.product.document.bll.DocumentManager;
import com.yikuyi.product.externalclient.PartyClientUtils;
import com.yikuyi.product.goods.dao.ProductRepository;
import com.yikuyi.product.goods.manager.PriceQueryManager;
import com.yikuyi.product.goods.manager.ProductManager;
import com.yikuyi.product.material.bll.MaterialManager;
import com.yikuyi.product.model.Product;
import com.yikuyi.product.model.ProductImage;
import com.yikuyi.product.promotion.bll.PromotionCacheManager;
import com.yikuyi.product.template.dao.ProductTemplateRepository;
import com.yikuyi.product.vo.ProductVo;
import com.yikuyi.promotion.vo.PromotionFlagVo;
import com.yikuyi.rule.price.PriceInfo;
import com.yikuyi.template.model.ProductTemplate;
import com.ykyframework.exception.BusinessException;
import com.ykyframework.exception.SystemException;
import com.ykyframework.model.IdGen;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class ActivityManager {

	private static final Logger logger = LoggerFactory.getLogger(ActivityManager.class);

	private static final ObjectMapper mapper = ObjectMapperHelper.configeObjectMapper(new ObjectMapper());

	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	/** 商品促销 */
	public static final String ACTIVITY_PROMOTION_TYPE = "10000";

	/** 商品秒杀 */
	public static final String ACTIVITY_SPIKE_TYPE = "10001";

	/**
	 * 活动CACHE房间名
	 */
	public static final String ACTIVITY_CACHE_ROOM_NAME = "activity";

	/**
	 * 所有商品的Map结构Key
	 */
	public static final String ACTIVITY_PRODUCT_CACHE = "activityProductCache";
	/**
	 * 所有商品的Map结构Key
	 */
	public static final String ACTIVITY_PRODUCT_QTY_CACHE = "activityProductQtyCache:";
	/**
	 * 所有活动供应商策略Key
	 */
	public static final String ACTIVITY_SUPPLIER_CACHE = "activitySupplierCache";

	/**
	 * 活动集合CACHE的KEY
	 */
	public static final String ACTIVITY_INFO_CACHE_KEY = "activitiesInfo";

	/**
	 * mongodb中存的活动商品上传文件用的，模板id
	 */
	private static final String TEMPLATE_ID = "activityProduct";

	/**
	 * 所有商品的Map结构Key
	 */
	public static final String ACTIVITY_PRODUCT_CACHE_MAP = "activityProductCache";

	@Autowired
	private ActivityDao activityDao;
	
	@Autowired
	private PromotionCacheManager promotionCacheManager;

	@Autowired
	private ActivityProductDraftDao activityProductDraftDao;

	@Autowired
	private ActivityProductDao activityProductDao;
	@Autowired
	private ActivityPeriodsDraftDao activityPeriodsDraftDao;
	@Autowired
	private ActivityPeriodsDao activityPeriodsDao;
	@Autowired
	private ActivityDraftDao activityDraftDao;

	@Autowired
	private ProductTemplateRepository productTemplateRepository;

	@Autowired
	private PartyClientUtils partyClientUtils;
	
	@Autowired
	private PartyClientBuilder partyClientBuilder;
	
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private MaterialManager materialManager;

	@Autowired
	private DocumentManager documentManager;

	@Autowired
	private PriceQueryManager priceQueryManager;

	@Autowired
	private ProductManager productManager;

	@Autowired
	private BrandManager brandManager;

	// 阿里云文件路径
	@Value("${leadMaterialFilePath}")
	private String leadMaterialFilePath;

	@Value("${api.basedata.serverUrlPrefix}")
	private String basedataServerUrlPrefix;

	// 注入HashOperations对象
	@Resource(name = "redisTemplate")
	private HashOperations<String, String, List<ActivityProductVo>> activitProductOps;

	// 注入HashOperations对象
	@Resource(name = "redisTemplate")
	private HashOperations<String, String, List<ActivityVo>> activitSupplierOps;

	@Autowired
	private JedisPool jedisPool;

	// 注入HashOperations对象
	@Resource(name = "redisTemplate")
	private HashOperations<String, String, List<ActivityProductVo>> hashOps;

	private static final String EXPORT_TEMPLATE = "ManufacturerName（制造商）,MPN（型号）,*Distribution Name（分销商）,*STOCK QTY（库存）,Storehouse 仓库," + "*Currency（币种）,*QtyBreak1（数量区间1）,*PriceBreak1（价格区间1）,QtyBreak2（数量区间2）,PriceBreak2（价格区间2）,"
			+ "QtyBreak3（数量区间3）,PriceBreak3（价格区间3）,QtyBreak4（数量区间4）,PriceBreak4（价格区间4）,QtyBreak5（数量区间5）," + "PriceBreak5（价格区间5）";

	/**
	 * 活动管理启用、停用 当传入的活动状态为启用时，先判断活动结束时间是否小于当前时间
	 * @param activityId
	 * @param status
	 * @throws BusinessException
	 * @throws ParseException
	 * @since 2017年11月24日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public void updateActivityStatus(String activityId, Status status) throws BusinessException, ParseException{
		// 查询活动详情
		Activity activity = this.getActivityInfo(activityId);
		if (null == activity) {
			throw new BusinessException(BusiErrorCode.ACTIVITY_DOES_NOT_EXIST, "活动不存在！");
		}
		// 启用时判断活动结束时间是否小于当前时间
		if (status.equals(Activity.Status.ENABLE) && format.parse(format.format(activity.getEndDate())).before(format.parse(format.format(new Date())))) {
			throw new BusinessException(BusiErrorCode.EXPIRYDATE_OUT, "活动结束时间已过，无法启用，请修改活动时间后再启用！");
		}
		// 修改活动状态
		Activity record = new Activity();
		record.setStatus(status);
		record.setPromoDiscountStatus(activity.getPromoDiscountStatus());
		record.setPromoDiscount(activity.getPromoDiscount());
		record.setIsSystemQty(activity.getIsSystemQty());
		record.setIconStatus(activity.getIconStatus());
		record.setActivityId(activityId);
		record.setLastUpdateDate(new Date());
		record.setLastUpdateUser(null == RequestHelper.getLoginUser() ? null : RequestHelper.getLoginUser().getId());
		record.setLastUpdateUserName(null == RequestHelper.getLoginUser() ? null : RequestHelper.getLoginUser().getUsername());
		this.updateStatus(record, activity);				
		
	}
	
	/**
	 * 修改活动信息的状态
	 * 
	 * @param record
	 * @param activityInfo
	 * @throws ParseException
	 * @throws BusinessException
	 * @since 2017年8月29日
	 * @author jik.shu@yikuyi.com
	 */
	public void updateStatus(Activity record, Activity activityInfo) throws BusinessException {
		try {
			// 修改活动信息的状态
			activityDao.update(record);
			// 当活动状态为有效时，判断活动开始时间是否在当天，如果是则把活动放入redis中
			if (record.getStatus().equals(Activity.Status.ENABLE)) {
				String startDate = format.format(activityInfo.getStartDate());// 活动开始日期
				String nowDate = format.format(Calendar.getInstance().getTime());// 当前日期
				if (startDate.equals(nowDate) || format.parse(startDate).before(format.parse(nowDate))) {// 把活动信息和活动商品分别存放到redis中
					addRedis(Arrays.asList(activityInfo));
				}
			} else if (record.getStatus().equals(Activity.Status.UNABLE)) { // 当活动状态为无效时，需要把活动信息从redis缓存中清除
				//还原库存
				activityProductDao.updateQtyToTotalById(record.getActivityId());
				deleteRedis(Arrays.asList(activityInfo));
			}
		} catch (ParseException e) {
			logger.error("updateStatus,{}", e.getMessage(), e);
			throw new BusinessException(BusiErrorCode.ACTIVITY_CACHE_MODIFY_ERROR, "活动修改失败！");
		}
	}

	/**
	 * 启用活动时把活动信息和活动商品信息放入缓存中
	 * 
	 * @param activityInfos
	 * @throws BusinessException
	 * @since 2017年8月29日
	 * @author jik.shu@yikuyi.com
	 */
	public synchronized void addRedis(List<Activity> activityInfos) {
		if (CollectionUtils.isEmpty(activityInfos)) {
			return;
		}
		Map<String, List<ActivityVo>> activityCacheMap = new HashMap<>();
		Map<String, List<ActivityProductVo>> productCacheMap = activitProductOps.entries(ACTIVITY_PRODUCT_CACHE);
		Map<String, Integer> productQtyCacheMap = new HashMap<>();
		for (Activity activityTemp : activityInfos) {
			processCache(activityTemp, activityCacheMap, productCacheMap, productQtyCacheMap);
		}
		if (!productQtyCacheMap.isEmpty()) {
			productQtyCacheMap.forEach((k, v) -> {try(Jedis jedis = jedisPool.getResource()) {jedis.set(k, String.valueOf(v));}});
		}
		if (!activityCacheMap.isEmpty()) {
			activitSupplierOps.putAll(ACTIVITY_SUPPLIER_CACHE, activityCacheMap);
		}
		if (!productCacheMap.isEmpty()) {
			activitProductOps.putAll(ACTIVITY_PRODUCT_CACHE, productCacheMap);
		}
	}

	/**
	 * 处理活动缓存
	 * 
	 * @param activity
	 * @param activityCacheMap
	 * @param cacheMap
	 * @throws BusinessException
	 * @since 2017年8月29日
	 * @author jik.shu@yikuyi.com
	 */
	private void processCache(Activity activity, Map<String, List<ActivityVo>> activityCacheMap, Map<String, List<ActivityProductVo>> cacheMap, Map<String, Integer> productQtyCacheMap)  {
		try {
			// 指定供应商规则策略。不含有商品
			if (activity.getSpecifySupplier()) {
				processSupplierCache(activity, activityCacheMap);
			} else {
				processPrductCache(activity, cacheMap, productQtyCacheMap);
			}
		} catch (Exception e) {
			throw new SystemException(e.getMessage(), e);
		}
	}

	/**
	 * 处理所有的供应商缓存结构
	 * 
	 * @param activity
	 * @param activityCacheMap
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @since 2017年8月29日
	 * @author jik.shu@yikuyi.com
	 */
	private void processSupplierCache(Activity activity, Map<String,  List<ActivityVo>> activityCacheMap) throws IOException, IllegalAccessException, InvocationTargetException {
		JavaType valueType = TypeFactory.defaultInstance().constructParametricType(List.class, SupplierRuleVo.class);
		List<SupplierRuleVo> listRule = mapper.readValue(activity.getSupplierRule(), valueType);
		ActivityVo activityVo = new ActivityVo();
		BeanUtils.copyProperties(activity, activityVo);
		activityVo.setStartTime(activity.getPeriodsList().get(0).getStartTime());
		activityVo.setEndTime(activity.getPeriodsList().get(0).getEndTime());
		activityVo.setPromotionFlag(getPromotionFlagVoByString(String.valueOf(activity.getIconStatus()) , activity.getIconScenes()));
		activityVo.setSupplierRuleKey(listRule.get(0).toString());
		listRule.stream().forEach(v -> {
			if(null == activityCacheMap.get(v)){
				List<ActivityVo> tempList = new ArrayList<>();
				tempList.add(activityVo);
				activityCacheMap.put(v.toString(), tempList);
			}else{
				activityCacheMap.get(v.toString()).add(activityVo);
			}
		});
	}
	
	public PromotionFlagVo getPromotionFlagVoByString(String iconStatus,String iconScenes){
		PromotionFlagVo promotionFlag = new PromotionFlagVo();
		promotionFlag.setType(PromotionFlagVo.CUSTOMIZE);
		promotionFlag.setShow("Y".equals(iconStatus));
		if(StringUtils.isNotEmpty(iconScenes)){
			JSONObject jsonObject = JSONObject.parseObject(iconScenes);
			Map<String,String> tempMap = null;
			if(jsonObject.containsKey("DETAIL")){
				tempMap = new HashMap<>();
				tempMap.put("img", jsonObject.getString("DETAIL"));
				promotionFlag.setDetail(tempMap);
			}
			if(jsonObject.containsKey("CART")){
				tempMap = new HashMap<>();
				tempMap.put("img", jsonObject.getString("CART"));
				promotionFlag.setPopwindow(tempMap);
			}
			if(jsonObject.containsKey("LIST")){
				tempMap = new HashMap<>();
				tempMap.put("img", jsonObject.getString("LIST"));
				promotionFlag.setList(tempMap);
			}
		}
		return promotionFlag;
	}
	
	/**
	 * 处理所有的商品缓存结构
	 * 
	 * @param activity
	 * @param activityCacheMap
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @since 2017年8月29日
	 * @author jik.shu@yikuyi.com
	 */
	private void processPrductCache(Activity activity, Map<String, List<ActivityProductVo>> activityCacheMap, Map<String, Integer> productQtyCacheMap) throws IllegalAccessException, InvocationTargetException {
		for (ActivityPeriods period : activity.getPeriodsList()) {
			for (ActivityProduct apModel : period.getProductList()) {
				List<ActivityProductVo> tempList = new ArrayList<>();
				ActivityProductVo apVo = conversionVo(activity, apModel, period);
				if (activityCacheMap.containsKey(apModel.getProductId())) {
					tempList = activityCacheMap.get(apModel.getProductId());
					tempList.add(activity.getType().equals(ACTIVITY_SPIKE_TYPE) ? 0 : tempList.size(), apVo);
				} else {
					tempList.add(apVo);
				}
				if ("N".equals(String.valueOf(activity.getIsSystemQty()))) {
					productQtyCacheMap.put(new StringBuffer(ACTIVITY_PRODUCT_QTY_CACHE).append(activity.getActivityId()).append("-").append(period.getPeriodsId()).append("-").append(apVo.getProductId()).toString(), apVo.getTotalQty());
				}
				activityCacheMap.put(apVo.getProductId(), tempList);
			}
		}
	}

	/**
	 * 把ActionProduct和Activity数据合并到ActivityProductVo中
	 * 
	 * @param activityInfo
	 * @param apModel
	 * @param period
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @since 2017年8月29日
	 * @author jik.shu@yikuyi.com
	 */
	private ActivityProductVo conversionVo(Activity activityInfo, ActivityProduct apModel, ActivityPeriods period) throws IllegalAccessException, InvocationTargetException {
		ActivityProductVo apVo = new ActivityProductVo();
		BeanUtils.copyProperties(apModel, apVo);
		apVo.setActivityId(activityInfo.getActivityId());
		apVo.setActivityType(activityInfo.getType());
		apVo.setPromoDiscountStatus(activityInfo.getPromoDiscountStatus());
		apVo.setPromoDiscount(activityInfo.getPromoDiscount());
		apVo.setSpecifySupplier(activityInfo.getSpecifySupplier());
		apVo.setPromotionFlag(getPromotionFlagVoByString(String.valueOf(activityInfo.getIconStatus()), activityInfo.getIconScenes()));
		apVo.setIsSystemQty(activityInfo.getIsSystemQty());
		apVo.setPeriodsId(period.getPeriodsId());
		apVo.setStartTime(period.getStartTime());
		apVo.setEndTime(period.getEndTime());
		apVo.setQty(apVo.getTotalQty());
		return apVo;
	}

	/**
	 * 定时刷新缓存
	 * @throws BusinessException
	 * @since 2017年10月14日
	 * @author jik.shu@yikuyi.com
	 */
	public void activityTask() {
		List<Activity> startList = activityDao.getStartActivity();
		List<Activity> endList = activityDao.getEndActivity();
		addRedis(startList);
		deleteRedis(endList);
	}
	
	/**
	 * 活动停用时刷新缓存或者每天定时凌晨刷新缓存
	 * 
	 * @throws BusinessException
	 * @since 2017年8月29日
	 * @author jik.shu@yikuyi.com
	 */
	public void deleteRedis(List<Activity> delActivity) {
		try(Jedis jedis = jedisPool.getResource()) {
			if(CollectionUtils.isEmpty(delActivity)){
				return;
			}
			//删除库存
			for(int i = 0;i<delActivity.size();i++){
				Activity activity = delActivity.get(i);
				if (Objects.isNull(activity)) {
					continue;
				}
				Set<String> set = jedis.keys(new StringBuffer(ACTIVITY_PRODUCT_QTY_CACHE).append(activity.getActivityId()).append("-*").toString());
				if (CollectionUtils.isNotEmpty(set)) {
					jedis.del(set.toArray(new String[set.size()]));
				}
				List<String> moduleIds = new ArrayList<>();
				if (activity.getSpecifySupplier()) {
					JavaType valueType = TypeFactory.defaultInstance().constructParametricType(List.class, SupplierRuleVo.class);
					List<SupplierRuleVo> listRule = mapper.readValue(activity.getSupplierRule(), valueType);
					List<String> keys = listRule.stream().map(SupplierRuleVo::toString).collect(Collectors.toList());
					promotionCacheManager.deleteRuleCache(activity.getActivityId(), moduleIds , keys, ACTIVITY_SUPPLIER_CACHE);
				} else {
					List<String> keys = new ArrayList<>();
					for (ActivityPeriods period : activity.getPeriodsList()) {
						keys.addAll(period.getProductList().stream().map(ActivityProduct::getProductId).collect(Collectors.toList()));
					}
					promotionCacheManager.deleteProductCache(activity.getActivityId(), moduleIds , keys , ACTIVITY_PRODUCT_CACHE);
				}
			}
		} catch (Exception e) {
			throw new SystemException(e.getMessage(),e);
		}
	}

	/**
	 * 根据活动ID查询，时区是启用的活动
	 * 
	 * @param activityId
	 * @return
	 * @since 2017年9月4日
	 * @author jik.shu@yikuyi.com
	 */
	public Activity getActivityInfo(String activityId) {
		return activityDao.getActivityInfo(activityId);
	}

	/**
	 * 将草稿活动信息转化成正式活动信息
	 * 
	 * @param activityId
	 * @since 2017年6月9日
	 * @author zr.aoxianbing@yikuyi.com
	 * @throws BusinessException
	 */
	public String draftToFormal(String activityId, String userId, String userName) throws BusinessException {
		String falg = "success";
		Activity oriActivity = activityDao.getById(activityId);

		// 将草稿中有效的商品转到正式的表中，之后将草稿中的商品删除
		Activity activityDraft = activityDraftDao.getActivityById(activityId);
		List<ActivityProduct> alist = activityProductDraftDao.getProductByActivityId(activityId);

		List<ActivityPeriods> plist = activityPeriodsDraftDao.getPeriodsByActivityId(activityId);
		if (CollectionUtils.isEmpty(alist)) {
			falg = "DataIsNull";
			throw new BusinessException(falg, "活动商品为空！");
		}
		// 删除
		activityDao.deleteById(activityId);
		activityProductDao.deleteByActivityId(activityId);
		activityPeriodsDao.deleteByActivityId(activityId);

		if (activityDraft != null) {
			// 看下原来的数据的创建人
			if (oriActivity != null) {
				activityDraft.setCreator(oriActivity.getCreator());
				activityDraft.setCreatorName(oriActivity.getCreatorName());
				activityDraft.setCreatedDate(oriActivity.getCreatedDate());
			}
			// 如果就是新创建的，则使用当前登录人作为创建人
			else {
				activityDraft.setCreator(userId);
				activityDraft.setCreatorName(userName);
				activityDraft.setCreatedDate(new Date());
			}
			activityDraft.setLastUpdateUser(userId);
			activityDraft.setLastUpdateUserName(userName);
			activityDraft.setLastUpdateDate(new Date());
			activityDao.save(activityDraft);
		}
		for (ActivityProduct activityProduct : alist) {
			activityProduct.setQty(activityProduct.getTotalQty());
			activityProduct.setCreator(userId);
			activityProduct.setCreatedDate(new Date());
			activityProduct.setLastUpdateUser(userId);
			activityProduct.setLastUpdateDate(new Date());
			activityProductDao.save(activityProduct);
		}
		if (CollectionUtils.isNotEmpty(plist)) {
			for (ActivityPeriods activityPeriods : plist) {
				// 判断时间段是否存在
				ActivityPeriods activityP = new ActivityPeriods();
				activityP.setActivityId(activityId);
				activityP.setEndTime(activityPeriods.getEndTime());
				activityP.setStartTime(activityPeriods.getStartTime());
				int count = activityPeriodsDao.getPeriodsByInterval(activityP);
				if (count > 0) {
					falg = "PeriodsExsit";
					throw new BusinessException(falg, "时间区间已经存在！");
				}
			}
			for (ActivityPeriods activityPeriods : plist) {
				activityPeriods.setCreator(userId);
				activityPeriods.setCreatedDate(new Date());
				activityPeriods.setLastUpdateUser(userId);
				activityPeriods.setLastUpdateDate(new Date());
				activityPeriodsDao.save(activityPeriods);
			}
		}
		// 删除
		activityDraftDao.deleteById(activityId);
		activityProductDraftDao.deleteByActivityId(activityId);
		activityPeriodsDraftDao.deleteByActivityId(activityId);
		return falg;
	}

	/**
	 * 判断时区是否有效，并更新无活动商品时区的状态为无效
	 * 
	 * @param activityId
	 * @param periods
	 * @throws BusinessException
	 * @since 2017年7月26日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public void updatePeriodsStatus(String activityId) throws BusinessException {

		String falg = "DATA_IS_NULL";
		List<ActivityProduct> alist = activityProductDraftDao.getProductByActivityId(activityId);
		List<ActivityPeriods> plist = activityPeriodsDraftDao.getPeriodsByActivityId(activityId);
		if (CollectionUtils.isEmpty(alist)) {
			// 更新时区
			if (CollectionUtils.isNotEmpty(plist)) {
				for (ActivityPeriods activityPeriods : plist) {
					// 当前区间商品数据不存在，修改活动区间表的状态为无效
					activityPeriodsDraftDao.updateActivityPeriodsDraftByCondition(activityId, activityPeriods.getPeriodsId(), ActivityProductDraft.Status.UNABLE);
				}
			}
			throw new BusinessException(falg, "活动商品不存在！");
		} else {
			if (CollectionUtils.isEmpty(plist)) {
				return;
			}
			for (ActivityPeriods activityPeriods : plist) {
				ActivityProductDraft activityProductDraft = new ActivityProductDraft();
				activityProductDraft.setActivityId(activityId);
				activityProductDraft.setPeriodsId(activityPeriods.getPeriodsId());
				List<ActivityProductDraft> list = activityProductDraftDao.getProductByCondition(activityProductDraft);
				if (CollectionUtils.isEmpty(list)) {
					// 当前区间商品数据不存在，修改活动区间表的状态为无效
					activityPeriodsDraftDao.updateActivityPeriodsDraftByCondition(activityId, activityPeriods.getPeriodsId(), ActivityProductDraft.Status.UNABLE);
				}
			}
		}
	}

	/**
	 * 查询正式获得详情
	 * 
	 * @param activityId
	 * @return
	 * @since 2017年6月12日
	 * @author zr.zhanghua@yikuyi.com
	 */
	public ActivityVo findActivityStandardById(String activityId) {
		return activityDao.findActivityById(activityId);

	}

	/**
	 * 查询活动草稿详情
	 * 
	 * @param activityId
	 * @return
	 * @since 2017年6月12日
	 * @author zr.zhanghua@yikuyi.com
	 */
	public ActivityVo findActivityDraftById(String activityId) {
		return activityDraftDao.findActivityById(activityId);
	}

	/**
	 * 查询当天正式获得详情（秒杀页面）
	 * 
	 * @param activityId
	 * @return
	 * @since 2017年6月13日
	 * @author zr.zhanghua@yikuyi.com
	 */
	public ActivityVo getTodayActivity() {
		return activityDao.getTodayActivity();
	}

	/**
	 * 查询当天正式获得详情（秒杀页面）
	 * 
	 * @param activityId
	 * @return
	 * @since 2017年6月13日
	 * @author zr.zhanghua@yikuyi.com
	 */
	public ActivityVo getActivityStandardById(String activityId) {
		return activityDao.getActivityStandardById(activityId);
	}

	/**
	 * 保存活动草稿
	 * 
	 * @param activityVo
	 * @return
	 * @throws Exception
	 * @since 2017年6月12日
	 * @author zr.zhanghua@yikuyi.com
	 * @throws BusinessException
	 */
	public ActivityVo saveActivityDraft(ActivityVo activityVo) throws BusinessException {
		String err = "DATE_ERROR";
		if (!ACTIVITY_PROMOTION_TYPE.equals(activityVo.getType())) {// 促销不判断
			// 校验活动时间
			int index = activityDao.findActivityDate(activityVo);
			if (index > 0) {
				throw new BusinessException(err, "活动时间有重叠");
			}
		}
		// 校验活动名称
		if (!ACTIVITY_PROMOTION_TYPE.equals(activityVo.getType())) {// 促销不判断
			int indexName = activityDao.findActivityName(activityVo);
			if (indexName > 0) {
				throw new BusinessException("NAME_ERROR", "活动名称有重叠");
			}
		}

		String activityId = String.valueOf(IdGen.getInstance().nextId());
		activityVo.setActivityId(activityId);
		Activity record = new Activity();
		BeanUtils.copyProperties(activityVo, record);
		record.setCreatedDate(new Date());
		record.setStatus(Activity.Status.UNABLE);
		activityDraftDao.save(record);
		List<ActivityPeriods> list = activityVo.getPeriodsList();
		if (null != list) {
			// 校验活动时间段
			if (!checkActivityPeriods(list)) {
				throw new BusinessException(err, "活动时间结构异常");
			}
			for (ActivityPeriods activityPeriods : list) {
				activityPeriods.setPeriodsId(String.valueOf(IdGen.getInstance().nextId()));
				activityPeriods.setActivityId(activityId);
				activityPeriods.setStatus(ActivityPeriods.Status.UNABLE);
				activityPeriodsDraftDao.save(activityPeriods);
			}
		}
		return activityVo;
	}

	/**
	 * 指定了具体供应商，不需要创建草稿直接进入正式表
	 * 
	 * @param activityVo
	 * @return
	 * @throws BusinessException
	 * @since 2017年8月28日
	 * @author jik.shu@yikuyi.com
	 */
	public Activity saveActivity(ActivityVo activityVo) throws BusinessException {
		Activity record = new Activity();
		List<ActivityPeriods> list = activityVo.getPeriodsList();
		// 校验活动时间段
		if (CollectionUtils.isEmpty(list) || !checkActivityPeriods(list)) {
			throw new BusinessException(BusiErrorCode.DATE_ERROR, "活动时间结构异常");
		}
		BeanUtils.copyProperties(activityVo, record);
		// 补充创建者信息
		LoginUser userInfo = RequestHelper.getLoginUser();
		Date currentDate = new Date();
		String activityId = String.valueOf(IdGen.getInstance().nextId());
		record.setActivityId(activityId);
		record.setCreator(userInfo.getId());
		record.setCreatorName(userInfo.getUsername());
		record.setCreatedDate(currentDate);
		record.setLastUpdateUserName(userInfo.getUsername());
		record.setLastUpdateDate(currentDate);
		record.setStatus(Activity.Status.UNABLE);
		activityDao.save(record);
		for (ActivityPeriods activityPeriods : list) {
			activityPeriods.setPeriodsId(String.valueOf(IdGen.getInstance().nextId()));
			activityPeriods.setActivityId(activityId);
			activityPeriods.setStatus(ActivityPeriods.Status.ENABLE);
			activityPeriodsDao.save(activityPeriods);
		}
		return record;
	}

	public Activity updatActivity(ActivityVo activityVo) throws BusinessException {
		Activity record = new Activity();
		List<ActivityPeriods> list = activityVo.getPeriodsList();
		// 校验活动时间段
		if (CollectionUtils.isEmpty(list) || !checkActivityPeriods(list)) {
			throw new BusinessException(BusiErrorCode.DATE_ERROR, "活动时间结构异常");
		}
		String activityId = activityVo.getActivityId();
		// 删除所有草稿数据,防止非指定供应商数据转供应商数据，导致垃圾数据
		activityDraftDao.deleteById(activityId);
		activityPeriodsDraftDao.deleteByActivityId(activityId);
		activityProductDraftDao.deleteByActivityId(activityId);
		activityPeriodsDao.deleteByActivityId(activityId);// 删除时区
		activityProductDao.deleteByActivityId(activityId);// 删除商品

		BeanUtils.copyProperties(activityVo, record);
		// 补充更新人信息
		LoginUser userInfo = RequestHelper.getLoginUser();
		Date currentDate = new Date();
		record.setLastUpdateUserName(userInfo.getUsername());
		record.setLastUpdateDate(currentDate);
		record.setStatus(Activity.Status.UNABLE);
		activityDao.update(record);

		for (ActivityPeriods activityPeriods : list) {
			activityPeriods.setPeriodsId(String.valueOf(IdGen.getInstance().nextId()));
			activityPeriods.setActivityId(activityId);
			activityPeriods.setStatus(ActivityPeriods.Status.ENABLE);
			activityPeriodsDao.save(activityPeriods);
		}
		return record;
	}

	/**
	 * 修改活动草稿
	 * 
	 * @param activityVo
	 * @return
	 * @throws Exception
	 * @since 2017年6月12日
	 * @author zr.zhanghua@yikuyi.com
	 * @throws BusinessException
	 */
	public ActivityVo updatActivityDraft(ActivityVo activityVo) throws BusinessException {
		String activityId = activityVo.getActivityId();
		// 校验活动时间
		if (!ACTIVITY_PROMOTION_TYPE.equals(activityVo.getType())) {// 促销不判断
			int index = activityDao.findActivityDate(activityVo);
			if (index > 0) {
				throw new BusinessException("DATE_ERROR", "活动时间有重叠");
			}
		}
		// 校验活动名称
		if (!ACTIVITY_PROMOTION_TYPE.equals(activityVo.getType())) {// 促销不判断
			int indexName = activityDao.findActivityName(activityVo);
			if (indexName > 0) {
				throw new BusinessException("NAME_ERROR", "活动名称有重叠");
			}
		}
		// 删除原先记录
		activityDraftDao.deleteById(activityId);
		Activity record = new Activity();
		BeanUtils.copyProperties(activityVo, record);
		record.setLastUpdateDate(new Date());
		record.setStatus(Activity.Status.UNABLE);
		// 保存更新记录
		activityDraftDao.save(record);

		// 修改活动区间
		List<ActivityPeriods> periods = activityVo.getPeriodsList();
		if (periods == null)
			periods = new ArrayList<>();

		// 处理区间
		activityPeriodsDraftDao.deleteByActivityId(activityId);// 删除原区间
		// 重新增加区间信息
		List<String> apIds = new ArrayList<>();
		for (ActivityPeriods ap : periods) {
			// 如果是新的数据则生成一个id
			if (ap.getPeriodsId() == null)
				ap.setPeriodsId(String.valueOf(IdGen.getInstance().nextId()));
			// 保存
			activityPeriodsDraftDao.save(ap);
			apIds.add(ap.getPeriodsId());
		}
		// 如果直接进入页面开始编辑，则要将正式表的数据刷到草稿表。
		// 如果是从下一步点上一步而来(from不为空)，则不需要做此处理
		if (activityVo.getFrom() == null) {
			// 处理商品信息
			activityProductDraftDao.deleteByActivityId(activityId);// 先删除草稿表的所有草稿商品
			activityProductDraftDao.transProductsToDraft(apIds);// 将正式商品保存到草稿表中
		}
		return activityVo;
	}

	public boolean checkActivityPeriods(List<ActivityPeriods> list) {
		DateFormat df = new SimpleDateFormat("HH:mm");
		Date start;
		Date end;
		try {
			Date index = df.parse("00:00");
			for (ActivityPeriods activityPeriods : list) {
				start = df.parse(activityPeriods.getStartTime());// 将字符串转换为date类型
				end = df.parse(activityPeriods.getEndTime());
				if (end.getTime() > start.getTime() && start.getTime() >= index.getTime()) {
					index = end;
				} else {
					return false;
				}
			}
		} catch (ParseException e) {
			logger.error("时间转换异常", e);
			throw new SystemException(e);
		}
		return true;
	}

	/**
	 * 查询活动管理列表
	 * 
	 * @param activity
	 * @param rowBouds
	 * @return
	 * @since 2017年6月12日
	 * @author zr.zhanghua@yikuyi.com
	 * @throws Exception
	 */
	public PageInfo<ActivityVo> findActivityByEntity(String name, String type, String prStatus, String startTime, String endTime, int page, int pageSize) {

		ActivityVo activityVo = new ActivityVo();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (StringUtils.isNotBlank(startTime)) {
			Date startTimed;
			try {
				startTimed = sdf.parse(startTime);
			} catch (ParseException e) {
				logger.error("时间转换异常", e);
				throw new SystemException(e);
			}
			activityVo.setStartDate(startTimed);
		}
		if (StringUtils.isNotBlank(endTime)) {
			Date endTimed;
			try {
				endTimed = sdf.parse(endTime);
			} catch (ParseException e) {
				logger.error("时间转换异常", e);
				throw new SystemException(e);
			}
			activityVo.setEndDate(endTimed);
		}

		if (StringUtils.isNotBlank(name)) {
			activityVo.setName(name);
		}
		if (StringUtils.isNotBlank(type)) {

			activityVo.setType(type);
		}
		activityVo.setPrStatus(prStatus);

		RowBounds rowBouds = new RowBounds((page - 1) * pageSize, pageSize);
		return new PageInfo<>(activityDao.findActivityByEntity(activityVo, rowBouds));
	}

	/**
	 * 导出活动草稿商品
	 * 
	 * @param ids
	 * @param activityId
	 * @param periodsId
	 * @param status
	 * @param response
	 * @since 2017年6月12日
	 * @author zr.aoxianbing@yikuyi.com
	 * @throws IOException
	 */
	public void exportProducts(String ids, String activityId, String periodsId, ActivityProductDraft.Status status, HttpServletResponse response) throws IOException {
		List<ActivityProductDraft> list = getProductList(ids, activityId, periodsId, status);
		response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.addHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", "Products.xls"));
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Expires", "0");
		this.exportProductExcelXls(list, response.getOutputStream(), status);

	}

	/**
	 * 获取活动商品信息列表
	 * 
	 * @param ids
	 * @param activityId
	 * @param periodsId
	 * @param status
	 * @return
	 * @since 2017年6月12日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	private List<ActivityProductDraft> getProductList(String ids, String activityId, String periodsId, ActivityProductDraft.Status status) {
		// 判断是否为导出异常商品
		List<ActivityProductDraft> list = null;
		if (status != null && status == ActivityProductDraft.Status.UNABLE) {
			// 获取异常商品
			ActivityProductDraft activityProductDraft = new ActivityProductDraft();
			activityProductDraft.setActivityId(activityId);
			activityProductDraft.setPeriodsId(periodsId);
			activityProductDraft.setStatus(status);

			list = activityProductDraftDao.getProductByCondition(activityProductDraft);
		} else {
			// 获取正常商品
			if (StringUtils.isNotEmpty(ids)) {
				// 根据ids 查询商品
				String[] strs = ids.split(",");
				list = activityProductDraftDao.getProductByIds(strs);
			} else {
				ActivityProductDraft activityProductDraft = new ActivityProductDraft();
				activityProductDraft.setActivityId(activityId);
				activityProductDraft.setPeriodsId(periodsId);

				list = activityProductDraftDao.getProductByCondition(activityProductDraft);
			}
		}
		return list;
	}

	/**
	 * 导出商品信息列表
	 * 
	 * @param list
	 * @param os
	 * @since 2017年6月12日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	private void exportProductExcelXls(List<ActivityProductDraft> list, OutputStream os, ActivityProductDraft.Status status) {

		ExportProcesser processer = null;
		try {
			processer = ExportFactory.getProcesser(ExportFactory.TYPE_XLS, os);
			// 标题
			processer.writeLine("Sheet1", EXPORT_TEMPLATE.split(","));

			List<List<String>> rowDataList;
			rowDataList = this.productDataList(list, status);
			for (List<String> rowData : rowDataList) {
				processer.writeLine("Sheet1", rowData);
			}
			processer.output();
		} catch (Exception e) {
			logger.error("Exception", e);
			throw new SystemException("exportProductExcelXls Exception", e);
		} finally {
			if (null != processer) {
				processer.close();
			}
		}
	}

	/**
	 * 重构数据
	 * 
	 * @param list
	 * @return
	 * @since 2017年6月12日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	private List<List<String>> productDataList(List<ActivityProductDraft> list, ActivityProductDraft.Status status) {
		List<List<String>> rowDataList = Lists.newArrayList();
		if (!CollectionUtils.isNotEmpty(list)) {
			return rowDataList;
		}
		List<String> rowData;
		for (ActivityProductDraft activityProductDraft : list) {
			rowData = Lists.newArrayList();
			// 制造商
			rowData.add(activityProductDraft.getManufacturer());
			// 型号
			rowData.add(activityProductDraft.getManufacturerPartNumber());
			// 分销商
			rowData.add(activityProductDraft.getVendorName());
			// 库存
			rowData.add(activityProductDraft.getTotalQty());
			// 仓库
			rowData.add(activityProductDraft.getSourceName());
			// 币种
			rowData.add(activityProductDraft.getCurrencyUomId());
			// 数量区间一
			rowData.add(activityProductDraft.getQtyBreak1());
			// 限时价一
			rowData.add(activityProductDraft.getPriceBreak1());
			// 数量区间二
			rowData.add(activityProductDraft.getQtyBreak2());
			// 限时价二
			rowData.add(activityProductDraft.getPriceBreak2());
			// 数量区间三
			rowData.add(activityProductDraft.getQtyBreak3());
			// 限时价三
			rowData.add(activityProductDraft.getPriceBreak3());
			// 数量区间四
			rowData.add(activityProductDraft.getQtyBreak4());
			// 限时价四
			rowData.add(activityProductDraft.getPriceBreak4());
			// 数量区间五
			rowData.add(activityProductDraft.getQtyBreak5());
			// 限时价五
			rowData.add(activityProductDraft.getPriceBreak5());
			rowDataList.add(rowData);
		}
		return rowDataList;
	}

	/**
	 * 删除活动管理
	 * 
	 * @param activityId
	 * @throws Exception
	 * @since 2017年6月13日
	 * @author zr.zhanghua@yikuyi.com
	 */
	public void deleteActivity(String activityId) {
		Activity record = new Activity();
		record.setActivityId(activityId);
		record.setStatus(Activity.Status.DELETE);
		activityDao.update(record);
	}

	/**
	 * 活动商品上传的文件解析
	 * 
	 * @param activityId
	 * @param periodsId
	 * @param fileUrl
	 * @param oriFileName
	 * @since 2017年6月9日
	 * @author tb.lijing@yikuyi.com
	 * @throws BusinessException
	 */
	public String parseFile(String activityId, String periodsId, String fileUrl, String oriFileName) throws BusinessException {
		String processId = String.valueOf(IdGen.getInstance().nextId());
		String fileName = "";
		int total = 0;
		int count = 0;
		int successfulCount = 0;
		List<ActivityProductDraft> listResultTemp = new ArrayList<>();
		ActivityProductDraft tempI = new ActivityProductDraft();
		ActivityProductDraft tempJ = new ActivityProductDraft();
		String docId = String.valueOf(IdGen.getInstance().nextId());// 生成id用于下载的文件名称
		fileName = materialManager.fileDownload(fileUrl, oriFileName, docId);// 从阿里云下载文件
		File attFile = new File(leadMaterialFilePath + File.separator + fileName);// 取得文件
		ProductTemplate template = productTemplateRepository.findOne(TEMPLATE_ID);// 获取模板
		List<ActivityProductDraft> list = activityDraftDao.findActivityDraftByActivityIdAndPeriodsId(activityId, periodsId);// 查询当前活动和区间已上传的数据
		total = list.size();// 记录已上传的数据总数

		ActivityVo ac = activityDraftDao.findActivityById(activityId);
		int index = null != ac && ACTIVITY_PROMOTION_TYPE.equals(ac.getType()) ? 0 : 100;// 每次只读取100条数据,如果秒杀限制上传数量100，促销放开限制

		ActivityProductReader reader = new ActivityProductReader(template, index);
		LeadInFactorySax.createProcess(reader, attFile, null);
		String error = reader.getErrorMsg();
		if (error != null) {
			throw new BusinessException(BusiErrorCode.TITLE_ERROR, error);// 当标题错误时，抛出异常
		}
		List<String[]> data2 = reader.getDatas();// 获取已经读取的数据
		StringBuilder repeatError = new StringBuilder();
		int rowNum = 1;

		Map<String, ProductBrand> brandMap = brandManager.getAliasBrandMap();// 品牌
		
		//结合实际业务场景,一个文件很少出现多个供应商，避免使用全量供应商查询
		//定义一个全局变量，没有查询查询到了，本次文件解析直接再次使用
		//Key用供应商名称，Value存基本信息
		Map<String,SupplierVo> suppliers = new HashMap<>();
		Map<String,String> supplierNames = new CaseInsensitiveMap<>(partyClientBuilder.supplierClient().getSupplierNames());
		
		/**
		 * 处理读取到的数据
		 */
		for (String[] temp : data2) {
			String sourceName = "";
			Integer inputBrandId = null;
			rowNum++;
			ActivityProductDraft activityProductDraft = new ActivityProductDraft();
			activityProductDraft.setProcessId(processId);
			String activityProductId = String.valueOf(IdGen.getInstance().nextId());
			StringBuilder errorDesc = new StringBuilder();
			String inputBrand = StringUtils.isNotBlank(temp[0]) && brandMap.containsKey(temp[0].trim().toUpperCase()) ? brandMap.get(temp[0].trim().toUpperCase()).getBrandName() : temp[0];
			if(StringUtils.isNotBlank(temp[0]) && brandMap.containsKey(temp[0].trim().toUpperCase())){
				inputBrandId = brandMap.get(temp[0].trim().toUpperCase()).getId();//品牌id
			}
			if (temp.length > 0 && StringUtils.isBlank(temp[0])) {
				errorDesc.append("未填ManufacturerName（制造商）；");
			}
			if ((temp.length > 1 && StringUtils.isBlank(temp[1])) || temp.length == 1) {
				errorDesc.append("未填MPN（型号）；");
			}
			if ((temp.length > 2 && StringUtils.isBlank(temp[2])) || (temp.length > 0 && temp.length < 3)) {
				errorDesc.append("未填Distribution Name（分销商）；");
			}
			if ((temp.length > 3 && StringUtils.isBlank(temp[3])) || (temp.length > 0 && temp.length < 4)) {
				errorDesc.append("未填STOCK QTY（库存）；");
			}
			if ((temp.length > 5 && StringUtils.isBlank(temp[5])) || (temp.length > 0 && temp.length < 6)) {
				errorDesc.append("未填Currency（币种）；");
			}
			if ((temp.length > 6 && StringUtils.isBlank(temp[6])) || (temp.length > 0 && temp.length < 7)) {
				errorDesc.append("未填QtyBreak1（数量区间1）；");
			}
			if ((temp.length == 7 && StringUtils.isNotBlank(temp[6])) || (temp.length > 7 && StringUtils.isNotBlank(temp[6]) && StringUtils.isBlank(temp[7])) || (temp.length > 0 && temp.length < 8)) {
				errorDesc.append("未填PriceBreak1（价格区间1）；");
			}
			if ((temp.length == 9 && StringUtils.isNotBlank(temp[8])) || (temp.length > 9 && StringUtils.isNotBlank(temp[8]) && StringUtils.isBlank(temp[9]))) {
				errorDesc.append("未填PriceBreak2（价格区间2）；");
			}
			if ((temp.length > 9 && StringUtils.isNotBlank(temp[9]) && StringUtils.isBlank(temp[8]))) {
				errorDesc.append("未填QtyBreak2（数量区间2）；");
			}
			if ((temp.length == 11 && StringUtils.isNotBlank(temp[10])) || (temp.length > 11 && StringUtils.isNotBlank(temp[10]) && StringUtils.isBlank(temp[11]))) {
				errorDesc.append("未填PriceBreak3（价格区间3）；");
			}
			if (temp.length > 11 && StringUtils.isNotBlank(temp[11]) && StringUtils.isBlank(temp[10])) {
				errorDesc.append("未填QtyBreak3（数量区间3）；");
			}
			if ((temp.length == 13 && StringUtils.isNotBlank(temp[12])) || (temp.length > 13 && StringUtils.isNotBlank(temp[12]) && StringUtils.isBlank(temp[13]))) {
				errorDesc.append("未填PriceBreak4（价格区间4）；");
			}
			if (temp.length > 13 && StringUtils.isNotBlank(temp[13]) && StringUtils.isBlank(temp[12])) {
				errorDesc.append("未填QtyBreak4（数量区间4）；");
			}
			if ((temp.length == 15 && StringUtils.isNotBlank(temp[14])) || (temp.length > 15 && StringUtils.isNotBlank(temp[14]) && StringUtils.isBlank(temp[15]))) {
				errorDesc.append("未填PriceBreak5（价格区间5）；");
			}
			if (temp.length > 15 && StringUtils.isNotBlank(temp[15]) && StringUtils.isBlank(temp[14])) {
				errorDesc.append("未填QtyBreak5（数量区间5）；");
			}
			if (temp.length > 2 && StringUtils.isNotBlank(temp[2]) && !supplierNames.containsKey(temp[2])) {
					errorDesc.append("分销商不存在；");
			} else {
				String sourceId = StringUtils.EMPTY;
				String supplierName = temp[2];// 分销商名称
				String facilityValue = temp[4];// 仓库名称
				String supplierId = supplierNames.get(supplierName);
				boolean sourceExist = true;
				if (!suppliers.containsKey(supplierId)) {
					suppliers.putAll(partyClientBuilder.supplierClient().getSupplierSimple(Arrays.asList(supplierId)));
				}
				// 当仓库不为空时，并且匹配到仓库时，给仓库id赋值
				if (temp.length > 4 && StringUtils.isNotBlank(facilityValue)) {
					SupplierVo tempVo = suppliers.get(supplierId);
					// 查询仓库
					Map<String, Facility> facilitys = new CaseInsensitiveMap<>(tempVo.getFacilityNameMap());
					if (facilitys.containsKey(facilityValue)) {
						sourceId = facilitys.get(facilityValue).getId();
					} else {
						sourceExist = false;
						errorDesc.append("Storehouse(仓库)仓库不存在；");
					}
				}
				if(sourceExist){
					List<ProductVo> productVoList = new ArrayList<>();
					// 当制造商、型号、分销商id、仓库id都有值的时候，查询商品数据
					if (null != inputBrandId && StringUtils.isNotBlank(temp[1]) && StringUtils.isNotBlank(supplierId) && StringUtils.isNotBlank(sourceId)) {
						// 根据型号、制造商、供应商ID、仓库ID、状态查询有效的商品数据
						productVoList = productRepository.findProductsByMPNandManufacturer(inputBrandId, temp[1].trim(), supplierId.trim(), sourceId.trim(), 1);
					} else if (null != inputBrandId && StringUtils.isNotBlank(temp[1]) && StringUtils.isNotBlank(supplierId)) {
						// 根据型号、制造商、供应商ID、状态查询有效的商品数据
						productVoList = productRepository.findProductsByCondition(inputBrandId, temp[1].trim(), supplierId.trim(), 1);
					}
					if (productVoList.isEmpty()) {
						errorDesc.append("商品数据不存在；");
					} else if (productVoList.size() > 1) {
						errorDesc.append("商品数据为多条；");
					} else if (1 == productVoList.size()) {
						ProductVo productVo = productVoList.get(0);
						if (null != productVo.getSpu() && StringUtils.isBlank(productVo.getSpu().getId())) {
							errorDesc.append("商品数据非标准；");
						}
						// 当仓库未填写，并且查询到的仓库数据只有一条时，给仓库id赋值
						if ((temp.length > 4 && StringUtils.isBlank(temp[4])) || temp.length < 5) {
							if (null != productVo && StringUtils.isNotBlank(productVo.getSourceId())) {
								sourceName = Optional.ofNullable(suppliers.get(supplierId).getFacilityIdMap().get(productVo.getSourceId())).map(Facility::getFacilityName).orElse(StringUtils.EMPTY);
							}
						}
						activityProductDraft.setVendorId(productVo.getVendorId());
						activityProductDraft.setSourceId(productVo.getSourceId());
						if (StringUtils.isNotBlank(sourceName)) {
							activityProductDraft.setSourceName(sourceName);
						}
						activityProductDraft.setProductId(productVo.getId());
						if (null != productVo.getSpu() && StringUtils.isNotBlank(productVo.getSpu().getDescription())) {
							activityProductDraft.setDescription(productVo.getSpu().getDescription());
						}
						// 处理分类 add by zr.aoxianbing@yikuyi.com
						if (null != productVo.getSpu() && CollectionUtils.isNotEmpty(productVo.getSpu().getCategories())) {
							List<ProductCategory> categoryList = productVo.getSpu().getCategories();
							for (ProductCategory productCategory : categoryList) {
								if (Integer.valueOf(3).equals(productCategory.getLevel()) && null != productCategory.getId()) {
									activityProductDraft.setCategory3Id(productCategory.getId().toString());
									activityProductDraft.setCategory3Name(productCategory.getName());
								}
							}
						}
						// 处理图片
						List<ProductImage> spuImage = productVo.getSpu().getImages();
						// 图片地址加上前缀后返回
						List<ProductImage> spuImageNew = productManager.handleImageUrl(spuImage);
						for (ProductImage image : spuImageNew) {
							if ("large".equalsIgnoreCase(image.getType())) {
								activityProductDraft.setImage1(image.getUrl());
							}
						}
					}
				}
			}
			if (temp.length > 3 && StringUtils.isNotBlank(temp[3]) && !com.ictrade.tools.StringUtils.isPositiveInteger(temp[3])) {
				errorDesc.append("STOCK QTY（库存）只能填写正整数；");
			}
			if (temp.length > 5 && StringUtils.isNotBlank(temp[5]) && !"RMB".equalsIgnoreCase(temp[5]) && !"CNY".equalsIgnoreCase(temp[5]) && !"USD".equalsIgnoreCase(temp[5])) {
				errorDesc.append("Currency（币种）只能填写RMB、CNY、USD；");
			}
			if (temp.length > 6 && StringUtils.isNotBlank(temp[6]) && !com.ictrade.tools.StringUtils.isPositiveInteger(temp[6])) {
				errorDesc.append("QtyBreak1（数量区间1）只能填写正整数；");
			}
			if (temp.length > 7 && StringUtils.isNotBlank(temp[7]) && (!com.ictrade.tools.StringUtils.isPositiveSevenDecimal(temp[7]) || "0".equals(temp[7]))) {
				errorDesc.append("PriceBreak1（价格区间1）只能填写正数，且应不小于0.00001，最大值整数位不能超过7位，小数位不能超过5位；");
			}

			if (temp.length > 8 && StringUtils.isNotBlank(temp[8]) && !com.ictrade.tools.StringUtils.isPositiveInteger(temp[8])) {
				errorDesc.append("QtyBreak2（数量区间2）只能填写正整数；");
			}
			if (temp.length > 9 && StringUtils.isNotBlank(temp[9]) && (!com.ictrade.tools.StringUtils.isPositiveSevenDecimal(temp[9]) || "0".equals(temp[9]))) {
				errorDesc.append("PriceBreak2（价格区间2）只能填写正数，且应不小于0.00001，最大值整数位不能超过7位，小数位不能超过5位；");
			}
			if (temp.length > 10 && StringUtils.isNotBlank(temp[10]) && !com.ictrade.tools.StringUtils.isPositiveInteger(temp[10])) {
				errorDesc.append("QtyBreak3（数量区间3）只能填写正整数；");
			}
			if (temp.length > 11 && StringUtils.isNotBlank(temp[11]) && (!com.ictrade.tools.StringUtils.isPositiveSevenDecimal(temp[11]) || "0".equals(temp[11]))) {
				errorDesc.append("PriceBreak3（价格区间3）只能填写正数，且应不小于0.00001，最大值整数位不能超过7位，小数位不能超过5位；");
			}
			if (temp.length > 12 && StringUtils.isNotBlank(temp[12]) && !com.ictrade.tools.StringUtils.isPositiveInteger(temp[12])) {
				errorDesc.append("QtyBreak4（数量区间4）只能填写正整数；");

			}
			if (temp.length > 13 && StringUtils.isNotBlank(temp[13]) && (!com.ictrade.tools.StringUtils.isPositiveSevenDecimal(temp[13]) || "0".equals(temp[13]))) {
				errorDesc.append("PriceBreak4（价格区间4）只能填写正数，且应不小于0.00001，最大值整数位不能超过7位，小数位不能超过5位；");
			}
			if (temp.length > 14 && StringUtils.isNotBlank(temp[14]) && !com.ictrade.tools.StringUtils.isPositiveInteger(temp[14])) {
				errorDesc.append("QtyBreak5（数量区间5）只能填写正整数；");
			}
			if (temp.length > 15 && StringUtils.isNotBlank(temp[15]) && (!com.ictrade.tools.StringUtils.isPositiveSevenDecimal(temp[15]) || "0".equals(temp[15]))) {
				errorDesc.append("PriceBreak5（价格区间5）只能填写正数，且应不小于0.00001，最大值整数位不能超过7位，小数位不能超过5位；");
			}
			activityProductDraft.setActivityProductId(activityProductId);
			if (temp.length > 3 && StringUtils.isNotBlank(temp[3])) {
				activityProductDraft.setTotalQty(temp[3]);
			}
			if (temp.length > 1 && StringUtils.isNotBlank(temp[1])) {
				activityProductDraft.setManufacturerPartNumber(temp[1].trim());
			}
			if (temp.length > 0 && StringUtils.isNotBlank(inputBrand)) {
				activityProductDraft.setManufacturer(inputBrand.trim());
			}
			if (StringUtils.isNotBlank(activityId)) {
				activityProductDraft.setActivityId(activityId);
			}
			if (StringUtils.isNotBlank(periodsId)) {
				activityProductDraft.setPeriodsId(periodsId);
			}
			if (temp.length > 2 && StringUtils.isNotBlank(temp[2])) {
				activityProductDraft.setVendorName(temp[2]);
			}
			if (temp.length > 4 && StringUtils.isNotBlank(temp[4])) {
				activityProductDraft.setSourceName(temp[4]);
			}
			if (errorDesc.length() > 0) {
				errorDesc = errorDesc.deleteCharAt(errorDesc.length() - 1);
				errorDesc.append("。");
				activityProductDraft.setDesc(errorDesc.toString());
				activityProductDraft.setStatus(ActivityProductDraft.Status.UNABLE);
			} else {
				activityProductDraft.setStatus(ActivityProductDraft.Status.ENABLE);
				successfulCount++;
			}
			if (temp.length > 5 && StringUtils.isNotBlank(temp[5])) {
				if ("RMB".equalsIgnoreCase(temp[5])) {
					activityProductDraft.setCurrencyUomId("CNY");
				} else {
					activityProductDraft.setCurrencyUomId(temp[5].toUpperCase());
				}
			}
			if (temp.length > 6 && StringUtils.isNotBlank(temp[6])) {
				activityProductDraft.setQtyBreak1(temp[6]);
			}
			if (temp.length > 7 && StringUtils.isNotBlank(temp[7])) {
				activityProductDraft.setPriceBreak1(temp[7]);
			}
			if (temp.length > 8 && StringUtils.isNotBlank(temp[8])) {
				activityProductDraft.setQtyBreak2(temp[8]);
			}
			if (temp.length > 9 && StringUtils.isNotBlank(temp[9])) {
				activityProductDraft.setPriceBreak2(temp[9]);
			}
			if (temp.length > 10 && StringUtils.isNotBlank(temp[10])) {
				activityProductDraft.setQtyBreak3(temp[10]);
			}
			if (temp.length > 11 && StringUtils.isNotBlank(temp[11])) {
				activityProductDraft.setPriceBreak3(temp[11]);
			}
			if (temp.length > 12 && StringUtils.isNotBlank(temp[12])) {
				activityProductDraft.setQtyBreak4(temp[12]);
			}
			if (temp.length > 13 && StringUtils.isNotBlank(temp[13])) {
				activityProductDraft.setPriceBreak4(temp[13]);
			}
			if (temp.length > 14 && StringUtils.isNotBlank(temp[14])) {
				activityProductDraft.setQtyBreak5(temp[14]);
			}
			if (temp.length > 15 && StringUtils.isNotBlank(temp[15])) {
				activityProductDraft.setPriceBreak5(temp[15]);
			}

			if (null != activityProductDraft) {
				activityProductDraft.setRowNum(rowNum);
				listResultTemp.add(activityProductDraft);
			}
		}
		/**
		 * 判断上传的文件中是否有重复数据
		 */
		String tempIStr = "";
		String tempJStr = "";
		if (!listResultTemp.isEmpty()) {
			for (int i = 0; i < listResultTemp.size(); i++) {
				tempI = listResultTemp.get(i);
				tempIStr = tempI.getManufacturer() + tempI.getManufacturerPartNumber() + tempI.getVendorName() + tempI.getSourceName();
				for (int j = i + 1; j < listResultTemp.size(); j++) {
					tempJ = listResultTemp.get(j);
					tempJStr = tempJ.getManufacturer() + tempJ.getManufacturerPartNumber() + tempJ.getVendorName() + tempJ.getSourceName();
					if (StringUtils.isNotBlank(tempIStr) && StringUtils.isNotBlank(tempJStr) && tempIStr.equalsIgnoreCase(tempJStr)) {
						repeatError.append("第" + tempI.getRowNum() + "行" + "与第" + tempJ.getRowNum() + "行数据重复。\n");
					}
				}
			}
		}
		if (repeatError.length() > 0) {
			throw new BusinessException(BusiErrorCode.REPEAT_ERROR, repeatError.toString());// 当上传的数据有重复时，抛出异常
		}
		for (ActivityProductDraft draft : listResultTemp) {
			int deleteCount = 0;
			int updateCount = 0;
			// 根据活动id、区间id、manufacturer、manufacturerPartNumber、sourceName、vendorName、productId查询数据
			List<ActivityProductDraft> listResult = new ArrayList<>();
			if (null != draft) {
				listResult = activityProductDraftDao.findProductDraftByCondition(draft);
			}
			// 查到已有的数据，覆盖数据
			if (!listResult.isEmpty()) {
				for (ActivityProductDraft obj : listResult) {
					deleteCount += activityProductDraftDao.deleteActivityProductDraft(obj.getActivityProductId());// 将重复数据删除
				}
				if (deleteCount > 0) {
					updateCount = activityProductDraftDao.save(draft);
				}
			}
			if (null != draft && updateCount == 0) {
				if (total == 100 && ACTIVITY_SPIKE_TYPE.equalsIgnoreCase(ac.getType())) {
					break;
				}
				count = activityProductDraftDao.save(draft);
				if (count > 0 && deleteCount == 0) {
					total++;
				}
			}
		}
		if (total > 0 && successfulCount > 0) {
			activityPeriodsDraftDao.updateActivityPeriodsDraftByCondition(activityId, periodsId, ActivityProductDraft.Status.ENABLE);// 修改活动区间表的状态为有效
		}
		if (total > 0) {
			/**
			 * 将文档的数据插入到Document表中
			 */
			Document doc = new Document();
			String documentId = String.valueOf(IdGen.getInstance().nextId());
			doc.setId(documentId);
			doc.setTypeId(DocumentType.ACTIVITY_DOCUMENT);
			doc.setDocumentLocation("http:" + fileUrl);
			doc.setDocumentName(oriFileName);
			doc.setStatusId(DocumentStatus.DOC_PRO_SUCCESS);
			doc.setCreator(RequestHelper.getLoginUserId());
			doc.setLastUpdateUser(RequestHelper.getLoginUserId());
			documentManager.insertDoc(doc);
		}
		return processId;
	}

	/**
	 * 根据activityProductId删除商品草稿数据
	 * 
	 * @param activityProductId
	 * @since 2017年6月14日
	 * @author tb.lijing@yikuyi.com
	 */
	public void deleteActivityProductDraft(String activityProductId, String activityId, String periodsId) {
		activityProductDraftDao.deleteActivityProductDraft(activityProductId);
	}

	/**
	 * 根据activityProductId删除商品草稿数据
	 * 
	 * @param activityProductId
	 * @since 2017年6月14日
	 * @author tb.lijing@yikuyi.com
	 */
	public void deleteActivityProductDraft(List<String> activityProductIds, String activityId, String periodsId, String activityType) {
		activityProductDraftDao.deleteActivityProductDraftBatch(activityProductIds);
	}

	/**
	 * 查询活动商品草稿数据
	 * 
	 * @param activityId
	 * @param periodsId
	 * @param status
	 * @return
	 * @since 2017年6月14日
	 * @author tb.lijing@yikuyi.com
	 */
	public PageInfo<ActivityProductDraftVo> findActivityProductDraftByCondition(ActivityProductDraft vo, RowBounds rowBounds) {

		List<ActivityProductDraftVo> list2 = activityProductDraftDao.findActivityProductDraftByCondition(vo, rowBounds);
		for (ActivityProductDraftVo temp : list2) {
			List<String> qtyArr = new ArrayList<>();
			List<String> priceArr = new ArrayList<>();
			if (StringUtils.isNotBlank(temp.getQtyBreak1()) || StringUtils.isNotBlank(temp.getPriceBreak1())) {
				qtyArr.add(temp.getQtyBreak1());
				priceArr.add(temp.getPriceBreak1());
			}
			if (StringUtils.isNotBlank(temp.getQtyBreak2()) || StringUtils.isNotBlank(temp.getPriceBreak2())) {
				qtyArr.add(temp.getQtyBreak2());
				priceArr.add(temp.getPriceBreak2());
			}
			if (StringUtils.isNotBlank(temp.getQtyBreak3()) || StringUtils.isNotBlank(temp.getPriceBreak3())) {
				qtyArr.add(temp.getQtyBreak3());
				priceArr.add(temp.getPriceBreak3());
			}
			if (StringUtils.isNotBlank(temp.getQtyBreak4()) || StringUtils.isNotBlank(temp.getPriceBreak4())) {
				qtyArr.add(temp.getQtyBreak4());
				priceArr.add(temp.getPriceBreak4());
			}
			if (StringUtils.isNotBlank(temp.getQtyBreak5()) || StringUtils.isNotBlank(temp.getPriceBreak5())) {
				qtyArr.add(temp.getQtyBreak5());
				priceArr.add(temp.getPriceBreak5());
			}
			temp.setQtyBreak(qtyArr);
			temp.setPriceBreak(priceArr);
			// list.add(temp);
		}
		return new PageInfo<>(list2);
	}

	/**
	 * 从缓存中查询活动中的商品集合
	 * 
	 * @param keyPattern
	 *            商品缓存key的正则
	 * @return 活动中的商品集合
	 * @since 2017年7月3日
	 * @author zr.wanghong
	 */
	private List<ActivityProductVo> getActProductsByCache(String keyPattern) {
		try (Jedis jedis = jedisPool.getResource()) {
			JedisUtils<ActivityProduct> jedisUtils = new JedisUtils<>();
			// 模糊查询redis
			List<ActivityProduct> activityProducts = jedisUtils.getObjectByPattern(jedis, keyPattern);

			// 复制集合
			List<ActivityProductVo> activityProductVos = new ArrayList<>();
			activityProducts.stream().forEach(activityProduct -> {
				ActivityProductVo activityProductVo = new ActivityProductVo();
				org.springframework.beans.BeanUtils.copyProperties(activityProduct, activityProductVo);
				activityProductVos.add(activityProductVo);
			});
			return activityProductVos;
		}
	}

	/**
	 * 查询活动商品数据
	 * 
	 * @param activityId
	 * @param periodsId
	 * @param priceFlag
	 * @return
	 * @since 2017年6月14日
	 * @author tb.lijing@yikuyi.com
	 */
	public List<ActivityProductVo> findActivityProductByCondition(String activityId, String periodsId, String priceFlag) {
		String[] periodsIds = StringUtils.isNotBlank(periodsId) ? periodsId.split(",") : null;

		List<ActivityProductVo> totalResult = new ArrayList<>();
		if (null != periodsIds) {
			// 1.查询缓存中当天活动中的商品集合
			for (int i = 0; i < periodsIds.length; i++) {
				totalResult.addAll(getActProductsByCache(new StringBuilder().append("activity:" + activityId + "-").append(periodsIds[i] + "-").append("product-*").toString()));
			}
		}

		if (CollectionUtils.isEmpty(totalResult)) {
			// 2.缓存中没有则查询DB
			totalResult = activityProductDao.findActivityProductByArray(activityId, periodsIds);
		}
		List<ActivityProductVo> activityProductVoList;
		// Y表示调用价格服务，活动缓存中有价格，则不调用价格服务
		if ("Y".equalsIgnoreCase(priceFlag)) {
			activityProductVoList = isPriceMethod(totalResult);
		} else {
			activityProductVoList = isNotPriceMethod(totalResult);
		}
		if (CollectionUtils.isEmpty(activityProductVoList)) {
			return Collections.emptyList();
		}

		// 获取供应商
		Map<String,String> vendorDisplaynames = partyClientUtils.getSupplierDisplayName(null);
		if (null != vendorDisplaynames && !vendorDisplaynames.isEmpty() && CollectionUtils.isNotEmpty(activityProductVoList)) {
			activityProductVoList.stream().forEach(v->{
				if(vendorDisplaynames.containsKey(v.getVendorId())){
					v.setVendorName(vendorDisplaynames.get(v.getVendorId()));
				}
			});
		}

		List<String> ids = new ArrayList<>();
		activityProductVoList.stream().forEach(activityProduct -> {
			ids.add(activityProduct.getProductId());
		});
		Iterable<Product> cursor = productRepository.findAll(ids);
		cursor.forEach(product -> {
			for (ActivityProductVo activityProduct : activityProductVoList) {
				if (activityProduct.getProductId().equals(product.getId())) {
					// 是否使用系统库存(Y/N),Y表示使用数据库中的
					if ("Y".equals(String.valueOf(activityProduct.getIsSystemQty()))) {
						activityProduct.setQty(product.getQty().intValue());
					}
					break;
				}
			}
		});
		return activityProductVoList;

	}

	/**
	 * 提供价格接口
	 * 
	 * @param result
	 * @return
	 * @since 2017年8月10日
	 * @author jik.shu@yikuyi.com
	 */
	private List<ActivityProductVo> isPriceMethod(List<ActivityProductVo> result) {
		if (CollectionUtils.isEmpty(result)) {
			return Collections.emptyList();
		}
		// copy活动基础属性到ProductVo
		List<ProductVo> productVoList = new ArrayList<>();
		result.stream().filter(Objects::nonNull).forEach(temp -> {
			ProductVo productVo = new ProductVo();
			productVo.setId(temp.getProductId());
			productVo.setActivityId(temp.getActivityId());
			productVo.setPeriodsId(temp.getPeriodsId());
			productVo.setActivityType(temp.getActivityType());
			productVo.setPromoDiscount(temp.getPromoDiscount());
			productVo.setPromoDiscountStatus(temp.getPromoDiscountStatus());
			productVoList.add(productVo);
		});

		List<PriceInfo> priceInfoList = priceQueryManager.queryPriceForSeckill(productVoList);

		List<ActivityProductVo> activityProductVoList = new ArrayList<>();
		Map<String, PriceInfo> map = new HashMap<>();

		for (PriceInfo temp2 : priceInfoList) {
			if (temp2 == null)
				continue;
			map.put(temp2.getProductId(), temp2);
		}
		for (ActivityProductVo temp3 : result) {
			if (temp3 == null)
				continue;
			List<Integer> qtyArr = new ArrayList<>();
			List<Double> priceArr = new ArrayList<>();
			if (map.containsKey(temp3.getProductId())) {
				temp3.setPriceInfo(map.get(temp3.getProductId()));
			}
			// 下面是计算阶梯数量和阶梯价格用来前台快速使用（活动打折情况下QtyBreak和PriceBreak字段，价格下面获取的是活动商品实际上传的阶梯和价格）
			// 鉴于目前页面还没有使用这2个字段，下面逻辑暂时不做修改
			if (null != temp3.getQtyBreak1() || null != temp3.getPriceBreak1()) {
				qtyArr.add(temp3.getQtyBreak1());
				priceArr.add(temp3.getPriceBreak1());
			}
			if (null != temp3.getQtyBreak2() || null != temp3.getPriceBreak2()) {
				qtyArr.add(temp3.getQtyBreak2());
				priceArr.add(temp3.getPriceBreak2());
			}
			if (null != temp3.getQtyBreak3() || null != temp3.getPriceBreak3()) {
				qtyArr.add(temp3.getQtyBreak3());
				priceArr.add(temp3.getPriceBreak3());
			}
			if (null != temp3.getQtyBreak4() || null != temp3.getPriceBreak4()) {
				qtyArr.add(temp3.getQtyBreak4());
				priceArr.add(temp3.getPriceBreak4());
			}
			if (null != temp3.getQtyBreak5() || null != temp3.getPriceBreak5()) {
				qtyArr.add(temp3.getQtyBreak5());
				priceArr.add(temp3.getPriceBreak5());
			}

			temp3.setQtyBreak(qtyArr);
			temp3.setPriceBreak(priceArr);

			activityProductVoList.add(temp3);
		}
		return activityProductVoList;
	}

	private List<ActivityProductVo> isNotPriceMethod(List<ActivityProductVo> result) {
		List<ActivityProductVo> activityProductVoList = new ArrayList<>();
		for (ActivityProductVo temp4 : result) {
			List<Integer> qtyArr2 = new ArrayList<>();
			List<Double> priceArr2 = new ArrayList<>();
			if (null != temp4.getQtyBreak1() || null != temp4.getPriceBreak1()) {
				qtyArr2.add(temp4.getQtyBreak1());
				priceArr2.add(temp4.getPriceBreak1());
			}
			if (null != temp4.getQtyBreak2() || null != temp4.getPriceBreak2()) {
				qtyArr2.add(temp4.getQtyBreak2());
				priceArr2.add(temp4.getPriceBreak2());
			}
			if (null != temp4.getQtyBreak3() || null != temp4.getPriceBreak3()) {
				qtyArr2.add(temp4.getQtyBreak3());
				priceArr2.add(temp4.getPriceBreak3());
			}
			if (null != temp4.getQtyBreak4() || null != temp4.getPriceBreak4()) {
				qtyArr2.add(temp4.getQtyBreak4());
				priceArr2.add(temp4.getPriceBreak4());
			}
			if (null != temp4.getQtyBreak5() || null != temp4.getPriceBreak5()) {
				qtyArr2.add(temp4.getQtyBreak5());
				priceArr2.add(temp4.getPriceBreak5());
			}
			temp4.setQtyBreak(qtyArr2);
			temp4.setPriceBreak(priceArr2);
			activityProductVoList.add(temp4);
		}
		return activityProductVoList;
	}
	
	/**
	 * 查询秒杀和促销的数据
	 * 
	 * @param activityId
	 * @param periodsId
	 * @param priceFlag
	 * @param activityType
	 * @return
	 * @since 2017年8月30日
	 * @author tb.lijing@yikuyi.com
	 */
	public PageInfo<ActivityProductVo> findActivityProductList(String activityId, String periodsId, String priceFlag, String activityType) {
		try {
		List<ActivityProductVo> activityProductVoList;
		List<ProductVo> productVos = new ArrayList<>();
		String[] periodsIds = StringUtils.isNotBlank(periodsId) ? periodsId.split(",") : null;
		List<ActivityProductVo> dbActivityProductVos = activityProductDao.findActivityProductByCondition(activityId, periodsIds);
		Map<String, List<ActivityProductVo>> map = hashOps.entries(ACTIVITY_PRODUCT_CACHE_MAP);
		// activityType "10001"表示是秒杀,"10000"表示是促销
		if (ACTIVITY_SPIKE_TYPE.equalsIgnoreCase(activityType)) {
			activityProductVoList = findSecKillActivityByCache(priceFlag, productVos, periodsIds, dbActivityProductVos, map);
		} else {
			activityProductVoList = findPromoActivityByCache(priceFlag, productVos, periodsIds, dbActivityProductVos, map);
		}

		List<String> ids = new ArrayList<>();
		activityProductVoList.stream().forEach(activityProduct -> {
			ids.add(activityProduct.getProductId());
		});
		Iterable<Product> cursor = productRepository.findAll(ids);
		cursor.forEach(product -> {
			for (ActivityProductVo activityProduct : activityProductVoList) {
				if (activityProduct.getProductId().equals(product.getId())) {
					// 是否使用系统库存(Y/N),Y表示使用数据库中的
					if ("Y".equals(String.valueOf(activityProduct.getIsSystemQty()))) {
						activityProduct.setQty(product.getQty().intValue());
					}
					break;
				}
			}
		});
		return new PageInfo<>(activityProductVoList);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}

	/**
	 * 缓存查询促销活动集合
	 * 
	 * @param priceFlag
	 * @param productVos
	 * @param map
	 * @return
	 * @since 2017年8月30日
	 * @author tb.lijing@yikuyi.com
	 */
	private List<ActivityProductVo> findPromoActivityByCache(String priceFlag, List<ProductVo> productVos, String[] periodsIds, List<ActivityProductVo> dbActivityProductVos, Map<String, List<ActivityProductVo>> map) {

		List<ActivityProductVo> listResult = new ArrayList<>();
		for (Entry<String, List<ActivityProductVo>> entry : map.entrySet()) {
			listResult.addAll(entry.getValue());
		}
		// 筛选出促销的商品集合
		for (ActivityProductVo dbAcProductVo : dbActivityProductVos) {
			ProductVo productVo = new ProductVo();
			productVo.setId(dbAcProductVo.getProductId());
			productVo.setVendorId(dbAcProductVo.getVendorId());
			productVos.add(productVo);
		}

		productManager.mergeActivity(productVos,"N");
		for(int i=0;i<productVos.size();i++){
			for(int j=0;j<dbActivityProductVos.size();j++){
				if(dbActivityProductVos.get(j).getProductId().equals(productVos.get(i).getId())){
					if(null != productVos.get(i).getQty()){
						dbActivityProductVos.get(j).setQty(Integer.parseInt(String.valueOf(productVos.get(i).getQty())));
					}
					continue;
				}
			}
		}
		// Y表示调用价格服务，活动缓存中有价格，则不调用价格服务
		if ("Y".equalsIgnoreCase(priceFlag)) {
			needHandlePrice(dbActivityProductVos, productVos);
		} else {
			notHandlePrice(dbActivityProductVos);
		}
		return dbActivityProductVos;
	}

	/**
	 * 缓存查询秒杀活动集合
	 * 
	 * @param priceFlag
	 * @param productVos
	 * @param periodsIds
	 * @param dbActivityProductVos
	 * @param map
	 * @return
	 * @since 2017年8月30日
	 * @author tb.lijing@yikuyi.com
	 */
	private List<ActivityProductVo> findSecKillActivityByCache(String priceFlag, List<ProductVo> productVos, String[] periodsIds, List<ActivityProductVo> dbActivityProductVos, Map<String, List<ActivityProductVo>> map) {
		List<ActivityProductVo> listResult = new ArrayList<>();
		for (Entry<String, List<ActivityProductVo>> entry : map.entrySet()) {
			listResult.addAll(entry.getValue());
		}

		// 筛选出秒杀的商品集合
		for (ActivityProductVo dbAcProductVo : dbActivityProductVos) {
			List<ActivityProductVo> cacheProductList = map.get(dbAcProductVo.getProductId());
			if (CollectionUtils.isEmpty(cacheProductList)) {
				continue;
			}
			for (ActivityProductVo tempVo : cacheProductList) {
				if (tempVo.getActivityId().equals(dbAcProductVo.getActivityId()) && tempVo.getPeriodsId().equals(dbAcProductVo.getPeriodsId())) {
					ProductVo vo = new ProductVo();
					vo.setId(tempVo.getProductId());
					vo.setActivityProductVo(tempVo);
					dbAcProductVo.setQty(tempVo.getQty());// 获取最新缓存库存
					productVos.add(vo);
					break;
				}
			}
		}

		// Y表示调用价格服务，活动缓存中有价格，则不调用价格服务
		if ("Y".equalsIgnoreCase(priceFlag)) {
			needHandlePrice(dbActivityProductVos, productVos);
		} else {
			notHandlePrice(dbActivityProductVos);
		}
		if (CollectionUtils.isEmpty(dbActivityProductVos)) {
			return Collections.emptyList();
		}
		// 获取供应商
		Map<String,String> vendorDisplaynames = partyClientUtils.getSupplierDisplayName(null);
		if (null != vendorDisplaynames && !vendorDisplaynames.isEmpty() && CollectionUtils.isNotEmpty(dbActivityProductVos)) {
			dbActivityProductVos.stream().forEach(v->{
				if(vendorDisplaynames.containsKey(v.getVendorId())){
					v.setVendorName(vendorDisplaynames.get(v.getVendorId()));
				}
			});
		}
		return dbActivityProductVos;
	}

	private List<ActivityProductVo> needHandlePrice(List<ActivityProductVo> resultActivityProductVos, List<ProductVo> productVoList) {
		Map<String, PriceInfo> map = new HashMap<>();
		List<PriceInfo> priceInfoList = priceQueryManager.queryPriceForSeckill(productVoList);
		for (PriceInfo temp2 : priceInfoList) {
			if (temp2 == null)
				continue;
			map.put(temp2.getProductId(), temp2);
		}
		for (ActivityProductVo temp3 : resultActivityProductVos) {
			if (temp3 == null)
				continue;
			List<Integer> qtyArr = new ArrayList<>();
			List<Double> priceArr = new ArrayList<>();
			if (map.containsKey(temp3.getProductId())) {
				temp3.setPriceInfo(map.get(temp3.getProductId()));
			}
			// 下面是计算阶梯数量和阶梯价格用来前台快速使用（活动打折情况下QtyBreak和PriceBreak字段，价格下面获取的是活动商品实际上传的阶梯和价格）
			// 鉴于目前页面还没有使用这2个字段，下面逻辑暂时不做修改
			if (null != temp3.getQtyBreak1() || null != temp3.getPriceBreak1()) {
				qtyArr.add(temp3.getQtyBreak1());
				priceArr.add(temp3.getPriceBreak1());
			}
			if (null != temp3.getQtyBreak2() || null != temp3.getPriceBreak2()) {
				qtyArr.add(temp3.getQtyBreak2());
				priceArr.add(temp3.getPriceBreak2());
			}
			if (null != temp3.getQtyBreak3() || null != temp3.getPriceBreak3()) {
				qtyArr.add(temp3.getQtyBreak3());
				priceArr.add(temp3.getPriceBreak3());
			}
			if (null != temp3.getQtyBreak4() || null != temp3.getPriceBreak4()) {
				qtyArr.add(temp3.getQtyBreak4());
				priceArr.add(temp3.getPriceBreak4());
			}
			if (null != temp3.getQtyBreak5() || null != temp3.getPriceBreak5()) {
				qtyArr.add(temp3.getQtyBreak5());
				priceArr.add(temp3.getPriceBreak5());
			}

			temp3.setQtyBreak(qtyArr);
			temp3.setPriceBreak(priceArr);

		}
		return resultActivityProductVos;
	}

	private List<ActivityProductVo> notHandlePrice(List<ActivityProductVo> result) {
		for (ActivityProductVo temp4 : result) {
			List<Integer> qtyArr2 = new ArrayList<>();
			List<Double> priceArr2 = new ArrayList<>();
			if (null != temp4.getQtyBreak1() || null != temp4.getPriceBreak1()) {
				qtyArr2.add(temp4.getQtyBreak1());
				priceArr2.add(temp4.getPriceBreak1());
			}
			if (null != temp4.getQtyBreak2() || null != temp4.getPriceBreak2()) {
				qtyArr2.add(temp4.getQtyBreak2());
				priceArr2.add(temp4.getPriceBreak2());
			}
			if (null != temp4.getQtyBreak3() || null != temp4.getPriceBreak3()) {
				qtyArr2.add(temp4.getQtyBreak3());
				priceArr2.add(temp4.getPriceBreak3());
			}
			if (null != temp4.getQtyBreak4() || null != temp4.getPriceBreak4()) {
				qtyArr2.add(temp4.getQtyBreak4());
				priceArr2.add(temp4.getPriceBreak4());
			}
			if (null != temp4.getQtyBreak5() || null != temp4.getPriceBreak5()) {
				qtyArr2.add(temp4.getQtyBreak5());
				priceArr2.add(temp4.getPriceBreak5());
			}
			temp4.setQtyBreak(qtyArr2);
			temp4.setPriceBreak(priceArr2);
		}
		return result;
	}

	

}