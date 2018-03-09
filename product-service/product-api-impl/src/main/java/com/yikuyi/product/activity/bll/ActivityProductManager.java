package com.yikuyi.product.activity.bll;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import com.yikuyi.activity.model.ActivityProduct;
import com.yikuyi.activity.model.ActivityProductDraft;
import com.yikuyi.activity.vo.ActivityProducOrderVo;
import com.yikuyi.activity.vo.ActivityProductVo;
import com.yikuyi.activity.vo.ActivityVo;
import com.yikuyi.product.activity.dao.ActivityProductDao;
import com.yikuyi.product.activity.dao.ActivityProductDraftDao;
import com.yikuyi.product.activity.dao.ActivitySaleHistoryDao;
import com.ykyframework.exception.BusinessException;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

@Service
public class ActivityProductManager {
	
	private static final Logger logger = LoggerFactory.getLogger("activity");
	
	/**
	 * 最大尝试次数
	 */
	private static final int MAX_TRY_TIMES = 10;

	@Autowired
	private ActivityProductDraftDao activityProductDraftDao;
	
	@Autowired
	private ActivityProductDao activityProductdao;
	
	@Autowired
	private ActivitySaleHistoryDao activitySaleHistoryDao;
	
	@Autowired
	private JedisPool jedisPool;
	
	//注入HashOperations对象
	@Resource(name = "redisTemplate")
	private HashOperations<String,String,List<ActivityProductVo>> activitProductOps;
	
	/**
	 * 根据活动商品id查询活动商品信息
	 * @param productId
	 * @return
	 * @since 2017年6月9日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public ActivityProductDraft getProductById(String productId) {
		return activityProductDraftDao.getProductById(productId);
	}

	/**
	 * 编辑商品信息
	 * @param activityProduct
	 * @since 2017年6月9日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public void editProductInfo(ActivityProductDraft activityProduct) {
		activityProductDraftDao.editProductInfo(activityProduct);
	}
	
	/**
	 * 由于下订单，根据订单数量和参加的活动，增减活动库存<br>
	 * 必须指定要参加的活动，根据指定的活动扣减redis中的库存，然后发送mq通知修改数据库中的库存。如果redis中不存在这个活动则抛出异常
	 * @param productOrders 下订单的活动商品列表
	 * @throws InterruptedException 
	 * @throws BusinessException 
	 */
	public String[] updateActivityProductCacheQty(List<ActivityProducOrderVo> productOrders) throws InterruptedException{
		//获取当前生效的活动对象KEY
		List<String> keysList = productOrders.stream().map(ActivityProducOrderVo::getActivityQtyKey).filter(StringUtils::isNotEmpty).collect(Collectors.toList());
		//执行redis扣减或增加操作
		if(updateActivityProductQtySync(productOrders,keysList)){
			return keysList.toArray(new String[keysList.size()]);//返回key
		}else{
			setProductOrderUpdateErrorMessage(productOrders);
			return ArrayUtils.EMPTY_STRING_ARRAY;
		}
	}
	
	
	/**
	 * 设置活动商品订单更新失败后的失败信息
	 * @param productOrders
	 * @since 2017年6月27日
	 * @author tongkun@yikuyi.com
	 */
	private void setProductOrderUpdateErrorMessage(List<ActivityProducOrderVo> productOrders){
		productOrders.stream().forEach(apo->{
			if(apo.getErrorCode()==null){
				apo.setErrorCode(ActivityProducOrderVo.ErrorCode.OPERATE_FAIL);
				apo.setErrorMessage("由于事务其它数据更新失败，或缓存被锁定，活动信息更新失败");
			}
			if(apo.getErrorMessage()!=null){
				logger.info(apo.getErrorMessage());
			}
		});
	}
	
	/**
	 * 批量更新活动缓存（同步）<br>
	 * 同步更新如果被打断，则会自动重试。如果重试次数超过最大值则会判定失败。<br>
	 * 如果同步更新失败，则会返回false;
	 * @param opMap
	 * @param jedis
	 * @return
	 * @throws InterruptedException
	 * @since 2017年6月27日
	 * @author tongkun@yikuyi.com
	 */
	public boolean updateActivityProductQtySync(List<ActivityProducOrderVo> orderVoList,List<String> keysList) throws InterruptedException{
		boolean success = false;
		//执行更新，成功或异常都不再重试。如果因为线程阻塞则需要再次重试。重试达到最大次数则不再重试
		for(int tryNumber = 0;tryNumber<MAX_TRY_TIMES;tryNumber++){
			boolean processResult = false;//本次执行是否成功
			boolean breakFlag = false;//本次执行是否需要跳出循环
			//确定执行异常则立刻跳出循环
			try {
				processResult = updateActivityProductQty(orderVoList,keysList);
			} catch (BusinessException e) {
				logger.error(e.getMessage(),e);
				breakFlag = true;//不需要再继续执行了，跳出循环
			}
			//执行成功随即跳出循环
			if(processResult){
				success = true;//执行成功，则不需要继续执行
				breakFlag = true;//需要跳出循环
			}
			//如果需要跳出，则break
			if(breakFlag){
				break;
			}
			//如果是线程阻塞，则不会异常，也不会成功，此时需要等待15毫秒后再重试
			try {
				logger.debug("其它线程正在更新，等待重新执行扣减");
				Thread.sleep(15);
			} catch (InterruptedException e) {
				logger.error("扣减等待时间被打断",e);
				throw e;
			}
		}
		return success;
	}
	
	/**
	 * 调用reids的watch命令，启动事物处理扣减和增加库存逻辑
	 * @param orderVoList
	 * @param keysList
	 * @return
	 * @throws BusinessException
	 * @since 2017年9月4日
	 * @author jik.shu@yikuyi.com
	 */
	private boolean updateActivityProductQty(List<ActivityProducOrderVo> orderVoList,List<String> keysList) throws BusinessException{
		try (Jedis jedis = jedisPool.getResource()) {
			jedis.watch(keysList.toArray(new String[keysList.size()]));
			//获取缓存，如果不存在置为NULL，后续有用到NULL做逻辑处理
			orderVoList.stream().forEach(v->{
				if(jedis.exists(v.getActivityQtyKey())){
					v.setQty(Long.valueOf(jedis.get(v.getActivityQtyKey())));
				}else{
					v.setQty(null);
				}
			});
			Transaction trans = jedis.multi();
			for (ActivityProducOrderVo tempVo : orderVoList) {
				if(null == tempVo.getQty()){//如果没有缓存，默认不执行下面操作
					continue;
				}
				if(tempVo.getOperation().equals(ActivityProducOrderVo.Operation.INCREASE)){//增加库存
					trans.incrBy(tempVo.getActivityQtyKey(), tempVo.getNumber());
				}else if(tempVo.getOperation().equals(ActivityProducOrderVo.Operation.DECREASE)){//扣减库存
					if(tempVo.getQty() >= tempVo.getNumber()){//剩余库存>=购买库存
						trans.decrBy(tempVo.getActivityQtyKey(), tempVo.getNumber());
					}else{
						tempVo.setErrorCode(ActivityProducOrderVo.ErrorCode.QTY_NOT_ENOUGH);
						tempVo.setErrorMessage("数量不足！商品:"+tempVo.getProductId()+" 参加的活动:"+tempVo.getActivityId()+" 参与的时区"+tempVo.getPeriodsId()+" 剩余的库存数:"+tempVo.getQty()+" 不足以扣减数量:"+tempVo.getNumber());
						throw new BusinessException("QTY_NOT_ENOUGH","QTY_NOT_ENOUGH");
					}
					logger.info("数量足够！商品:{} 参加的活动:{} 参与的时区{} 剩余的库存数:{} 足以扣减数量:{}",tempVo.getProductId(),tempVo.getActivityId(),tempVo.getPeriodsId(),tempVo.getQty(),tempVo.getNumber());
				}
			}
			List<Object> result = trans.exec();
			return result != null && !result.isEmpty();// 返回执行是否成功
		}
	}
	
	
	/**
	 * 在活动商品实时库存更新之后，由此方法进行同步更新数据库
	 * @param activityId 活动id
	 * @param periodsId 区间id
	 * @param productId 商品id
	 * @since 2017年6月13日
	 * @author tongkun@yikuyi.com
	 */
	public void syncActivityProductQty(String key){
		try(Jedis jedis = jedisPool.getResource()){
			logger.info("start_update_cache_qty 开始更新库存:{}",key);
			if(!jedis.exists(key)){
				logger.info("start_update_cache_qty 缓存不存在:{}",key);
				return;
			}
			int qty = Integer.parseInt(jedis.get(key));
			logger.info("start_update_cache_qty 开始更新库存:{},{}",key,qty);
			String[] keys = key.split(":")[1].split("-");
			String activityId = keys[0];
			String periodsId = keys[1];
			String productId = keys[2];
			List<ActivityProductVo> apVolist = activitProductOps.get(ActivityVo.ACTIVITY_PRODUCT_CACHE, productId);
			if(CollectionUtils.isEmpty(apVolist)){
				logger.info("start_update_cache_qty 缓存结果为NULL:{}",key);
				return;
			}
			//更新缓存和数据库
			apVolist.stream().forEach(v->{
				if(v.getActivityId().equals(activityId) && v.getPeriodsId().equals(periodsId)){
					v.setQty(qty);
					activitProductOps.put(ActivityVo.ACTIVITY_PRODUCT_CACHE, productId, apVolist);
					//更新数据库
					ActivityProduct saveProd = new ActivityProduct();
					saveProd.setActivityProductId(v.getActivityProductId());
					saveProd.setQty(qty);
					activityProductdao.updateQty(saveProd);
					return;
				}
			});
		}
	}
	
	/**
	 * 保存商品历史记录
	 * @since 2017年6月19日
	 * @author tongkun@yikuyi.com
	 */
	public int saveActivitiesProductsHistory(){
		int result = activitySaleHistoryDao.saveTodayProductsToHistory();//先保存到历史中
		activityProductdao.updateQtyToTotal();//恢复当前库存到最大值
		return result;
	}
}
