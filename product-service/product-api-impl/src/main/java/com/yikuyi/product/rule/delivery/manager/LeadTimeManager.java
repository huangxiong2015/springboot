package com.yikuyi.product.rule.delivery.manager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yikuyi.activity.vo.ActivityProductVo;
import com.yikuyi.product.model.Product;
import com.yikuyi.rule.delivery.vo.DeliveryInfo;
import com.yikuyi.rule.delivery.vo.LeadTimeVo;
import com.yikuyi.rule.delivery.vo.ProductInfo;
import com.yikuyi.rule.delivery.vo.ProductLeadTimeVo;

@Service
@Transactional
public class LeadTimeManager {
	
	private static final Logger logger = LoggerFactory.getLogger(LeadTimeManager.class);
	private static final String LEADTIMERULE = "leadTimeRule.";
	
	@Autowired
	private MongoRepository<Product, String> productRepository;
	
	@Autowired
	private CacheManager cacheManager;
	

	//注入HashOperations对象
	@Resource(name = "redisTemplate")
	private HashOperations<String,String,List<ActivityProductVo>> productCacheOps;
	
	
	/**
	 * 所有商品的Map结构Key
	 */
	public static final String ACTIVITY_PRODUCT_CACHE_MAP = "activityProductCache";
	
	/**
	 * 时间格式
	 */
	private SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm");
	
	/**
	 * 实时查询商品的交期
	 * @param ids
	 * @return
	 * @since 2016年12月6日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public List<ProductInfo> getLeadTimeList(List<String> ids){
		logger.info("LeadTimeManager---method:getLeadTimeList,input param {}", "ids:"+ids);
		Cache cache = cacheManager.getCache("leadTimeRule");
		List<ProductInfo> listInfo = new ArrayList<>();
		//查询商品详情
		Iterator<Product> productInfo = productRepository.findAll(ids).iterator();
		while (productInfo.hasNext()) {
			ProductInfo pi = new ProductInfo();
			Product product = productInfo.next();
			pi.setProductId(product.getId());
			//库存
			//Integer qty = product.getQty().intValue();
			//检验商品是否在做活动，如果是则返回活动库存
			//qty = activityInfo(qty,product.getId());
			String key1 = LEADTIMERULE +  product.getVendorId();
			String key2 = LEADTIMERULE +  product.getVendorId();
			String facilityId = product.getSourceId();
			if(StringUtils.isBlank(facilityId)){
				key1 += "-none";
				key2 += "-none";
			}else{
				key1 += "-" + facilityId;
				key2 += "-" + facilityId;
			}
			//如库存不为0则为现货
			//if(qty > 0){
				key1 += "-0";
				DeliveryInfo val = cache.get(key1, DeliveryInfo.class);
				if(null == val){
					String newKey = LEADTIMERULE +  product.getVendorId() + "-none-0";
					val = cache.get(newKey, DeliveryInfo.class);
				}
				if(null!=val){
					ProductInfo piResult1 = handleLeadTimeInfo(pi,val);
					listInfo.add(piResult1);
				} 
			//}else{   //如库存为0则为排单
				key2 += "-1";
				DeliveryInfo val2 = cache.get(key2, DeliveryInfo.class);
				if(null == val2){
					String newKey = LEADTIMERULE +  product.getVendorId() + "-none-1";
					val2 = cache.get(newKey, DeliveryInfo.class);
				}
				if(null!=val2){
					ProductInfo piResult2 = handleLeadTimeInfo(pi,val2);
					listInfo.add(piResult2);
				}
				if(null==val && null==val2){
					listInfo.add(pi);
				}
			//}
			
	    }
		return listInfo;
	}
	
	/**
	 * 处理从Redis中取出来的交期模板数据
	 * @param pi
	 * @param val
	 * @return
	 * @since 2016年12月7日
	 * @author zr.wenjiao@yikuyi.com
	 */
	private ProductInfo handleLeadTimeInfo(ProductInfo pi,DeliveryInfo val){
		ProductInfo productInfo = new ProductInfo();
		BeanUtils.copyProperties(pi, productInfo);
		if(null == val){
			productInfo.setLeadTimeCH("0");
			productInfo.setLeadTimeHK("0");
			return productInfo;
		}
		//if(val.getProductType() == 0){//现货
		productInfo.setIsShowLeadTime(val.getIsShowLeadTime());
		productInfo.setProductType(val.getProductType());
		if(null!=val.getLeadTimeMinCH()){
			productInfo.setLeadTimeMinCH(val.getLeadTimeMinCH().toString());
		}
		if(null!=val.getLeadTimeMaxCH()){
			productInfo.setLeadTimeMaxCH(val.getLeadTimeMaxCH().toString());
		}
		if(null!=val.getLeadTimeMinHK()){
			productInfo.setLeadTimeMinHK(val.getLeadTimeMinHK().toString());
		}
		if(null!=val.getLeadTimeMaxHK()){
			productInfo.setLeadTimeMaxHK(val.getLeadTimeMaxHK().toString());
		}
		if(null!=val.getFactoryLeadTimeMinCH()){
			productInfo.setSchedulingLeadTimeMinCH(val.getFactoryLeadTimeMinCH().toString());
		}
		if(null!=val.getFactoryLeadTimeMaxCH()){
			productInfo.setSchedulingLeadTimeMaxCH(val.getFactoryLeadTimeMaxCH().toString());
		}
		if(null!=val.getFactoryLeadTimeMinHK()){
			productInfo.setSchedulingLeadTimeMinHK(val.getFactoryLeadTimeMinHK().toString());
		}
		if(null!=val.getFactoryLeadTimeMaxHK()){
			productInfo.setSchedulingLeadTimeMaxHK(val.getFactoryLeadTimeMaxHK().toString());
		}
			
			
			//pi.setLeadTimeCH(val.getLeadTimeMinCH() + "-" + val.getLeadTimeMaxCH());
			
			
			//pi.setLeadTimeHK(val.getLeadTimeMinHK() + "-" + val.getLeadTimeMaxHK());
		//}else{//排单
			//pi.setLeadTimeCH(String.valueOf(val.getLeadTimeCH()));
			//pi.setLeadTimeHK(String.valueOf(val.getLeadTimeHK()));
			// update by wanghong 2017/8/2
			
			
			//pi.setSchedulingLeadTimeCH(val.getFactoryLeadTimeMinCH() + "-" + val.getFactoryLeadTimeMaxCH());
			
			
			//pi.setSchedulingLeadTimeHK(val.getFactoryLeadTimeMinHK() + "-" + val.getFactoryLeadTimeMaxHK());
			
		//}
		return productInfo;
	}
	
	/**
	 * 根据商品信息查询交期策略
	 * @param info
	 * @return
	 * @since 2017年3月17日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public List<ProductLeadTimeVo> getLeadTimeByProdcut(List<ProductLeadTimeVo> info){
		logger.info("LeadTimeManager---method:getLeadTimeByProdcut,input param {}", info);
		Cache cache = cacheManager.getCache("leadTimeRule");
		
		List<ProductLeadTimeVo> leadTimeInfo = new ArrayList<>();
		for(ProductLeadTimeVo leadTimeVo : info){
			//库存
			//Integer qty = leadTimeVo.getQty().intValue();
			//检验商品是否在做活动，如果是则返回活动库存
			//qty = activityInfo(qty,leadTimeVo.getId());
			String key1 = LEADTIMERULE +  leadTimeVo.getVendorId();
			String key2 = LEADTIMERULE +  leadTimeVo.getVendorId();
			//仓库
			String facilityId = leadTimeVo.getSourceId();
			//如仓库不为空则根据仓库查询
			if(StringUtils.isNotEmpty(facilityId)){
				key1 += "-" + facilityId;
				key2 += "-" + facilityId;
			}else{
				key1 += "-none";
				key2 += "-none";
			}
			//如库存不为0则为现货
			//if(qty > 0){
				key1 += "-0";
				DeliveryInfo val1 = cache.get(key1, DeliveryInfo.class);
				if(null == val1){
					String newKey = LEADTIMERULE +  leadTimeVo.getVendorId() + "-none-0";
					val1 = cache.get(newKey, DeliveryInfo.class);
				}
				ProductLeadTimeVo leadTimeVo1 = handleRealityLeadTime(leadTimeVo,val1);
				leadTimeInfo.add(leadTimeVo1);
			//}else{   //如库存为0则为排单
				ProductLeadTimeVo leadTimeVoTemp = leadTimeVo;
				key2 += "-1";
				DeliveryInfo val2 = cache.get(key2, DeliveryInfo.class);
				if(null == val2){
					String newKey = LEADTIMERULE +  leadTimeVoTemp.getVendorId() + "-none-1";
					val2 = cache.get(newKey, DeliveryInfo.class);
				}
				ProductLeadTimeVo leadTimeVo2 = handleRealityLeadTime(leadTimeVoTemp,val2);
			//}
			leadTimeInfo.add(leadTimeVo2);
		}
		return leadTimeInfo;
	}
	
	/**
	 * 搜索列表页交期处理
	 * @param info
	 * @return
	 * @since 2017年8月15日
	 * @author tb.lijing@yikuyi.com
	 */
	public List<ProductLeadTimeVo> getLeadTimeByProdcutList(List<ProductLeadTimeVo> info){
		logger.info("LeadTimeManager---method:getLeadTimeByProdcut,input param {}", info);
		Cache cache = cacheManager.getCache("leadTimeRule");
		
		List<ProductLeadTimeVo> leadTimeInfo = new ArrayList<>();
		for(ProductLeadTimeVo leadTimeVo : info){
			ProductLeadTimeVo leadTimeVoNew = new ProductLeadTimeVo();
			//库存
			Integer qty = leadTimeVo.getQty().intValue();
			//检验商品是否在做活动，如果是则返回活动库存
			//qty = activityInfo(qty,leadTimeVo.getId());
			String key = LEADTIMERULE +  leadTimeVo.getVendorId();
			//仓库
			String facilityId = leadTimeVo.getSourceId();
			//如仓库不为空则根据仓库查询
			if(StringUtils.isNotEmpty(facilityId)){
				key += "-" + facilityId;
			}else{
				key += "-none";
			}
			//如库存不为0则为现货
			if(qty > 0){
				key += "-0";
				DeliveryInfo val = cache.get(key, DeliveryInfo.class);
				if(null == val){
					String newKey = LEADTIMERULE +  leadTimeVo.getVendorId() + "-none-0";
					val = cache.get(newKey, DeliveryInfo.class);
				}
				leadTimeVoNew = handleRealityLeadTime(leadTimeVo,val);
			}else{   //如库存为0则为排单
				key += "-1";
				DeliveryInfo val = cache.get(key, DeliveryInfo.class);
				if(null == val){
					String newKey = LEADTIMERULE +  leadTimeVo.getVendorId() + "-none-1";
					val = cache.get(newKey, DeliveryInfo.class);
				}
				leadTimeVoNew = handleRealityLeadTime(leadTimeVo,val);
			}
			leadTimeInfo.add(leadTimeVoNew);
		}
		
		return leadTimeInfo;
	}
	
	private ProductLeadTimeVo handleRealityLeadTime(ProductLeadTimeVo info,DeliveryInfo val){
		ProductLeadTimeVo productLeadTimeVo = new ProductLeadTimeVo();
		BeanUtils.copyProperties(info, productLeadTimeVo);
		List<LeadTimeVo> realityList = new ArrayList<>();
		LeadTimeVo realityCH = new LeadTimeVo();
		LeadTimeVo realityHK = new LeadTimeVo();
		//当缓存中没有交期策略时，返回0
		if(val == null){
			//香港交期
			realityHK.setDeliveryPlace("HK");
			realityHK.setRealityLeadTime("0");
			realityList.add(realityHK);
			//国内交期
			realityCH.setDeliveryPlace("CH");
			realityCH.setRealityLeadTime("0");
			realityList.add(realityCH);
			productLeadTimeVo.setRealityList(realityList);
			return productLeadTimeVo;
		}
		productLeadTimeVo.setTemplateName(val.getDeliveryRuleName());
		productLeadTimeVo.setIsShowLeadTime(val.getIsShowLeadTime());
		if(val.getProductType() == 0){//现货
			//香港交期
			realityHK.setDeliveryPlace("HK");
			realityHK.setRealityLeadTime(val.getLeadTimeMinHK() + "-" + val.getLeadTimeMaxHK());
			realityList.add(realityHK);
			//国内交期
			realityCH.setDeliveryPlace("CH");
			realityCH.setRealityLeadTime(val.getLeadTimeMinCH() + "-" + val.getLeadTimeMaxCH());
			realityList.add(realityCH);
			productLeadTimeVo.setRealityList(realityList);
		}else{//排单
			//香港交期
			realityHK.setDeliveryPlace("HK");
			//update by wanghong 
			//realityHK.setRealityLeadTime(String.valueOf(val.getLeadTimeHK()));
			realityHK.setRealityLeadTime(val.getFactoryLeadTimeMinHK() + "-" + val.getFactoryLeadTimeMaxHK());
			realityList.add(realityHK);
			//国内交期
			realityCH.setDeliveryPlace("CH");
			//update by wanghong 
			//realityCH.setRealityLeadTime(String.valueOf(val.getLeadTimeCH()));
			realityCH.setRealityLeadTime(val.getFactoryLeadTimeMinCH() + "-" + val.getFactoryLeadTimeMaxCH());
			realityList.add(realityCH);
			productLeadTimeVo.setRealityList(realityList);
		}
		return productLeadTimeVo;
	}
	
	/**
	 * 检验商品是否在做活动，如果是则返回活动库存
	 * @return
	 * @since 2017年7月11日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public Integer activityInfo(Integer qty,String productId){
		/*Cache activityCache = redisCacheManagerNoTransaction.getCache("activity");
		//获取正在生效的活动
		List<Activity> effectActivities = activityProductManager.getEffectActivity(activityCache);*/
		//从缓存中取商品的活动信息
		Map<String,List<ActivityProductVo>> activityProductVoMap = productCacheOps.entries(ACTIVITY_PRODUCT_CACHE_MAP);
		List<ActivityProductVo> activityProductVos = activityProductVoMap.get(productId);
		ActivityProductVo curentAcProductVo = null;
		if(CollectionUtils.isNotEmpty(activityProductVos)){
			//迭代依次获取活动，秒杀和促销的活动依次按顺序存取
			for (ActivityProductVo acProductVo : activityProductVos) {
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
				
			}
		}
		if(null!=curentAcProductVo && null!=curentAcProductVo.getQty()){
			qty = curentAcProductVo.getQty();
		}
		/*if(CollectionUtils.isEmpty(effectActivities)){
			return qty;
		}
		for(Activity info : effectActivities){
			List<ActivityPeriods> period = info.getPeriodsList();
			if(CollectionUtils.isNotEmpty(period)){
				//拼装活动商品的key
				StringBuilder str = new StringBuilder();
				str.append(info.getActivityId() + "-");
				str.append(period.get(0).getPeriodsId() + "-");
				str.append("product-" + productId);
				//获取活动商品信息
				ValueWrapper productActivity = activityCache.get(str.toString());
				if(productActivity != null){
					ActivityProduct pInfo = (ActivityProduct)productActivity.get();
					Integer activityQty = pInfo.getQty() == null ? pInfo.getTotalQty() : pInfo.getQty();
					qty = activityQty == null ? 0 : activityQty;
				}
			}
		}*/
		return qty;
	}

}