package com.yikuyi.product.activity.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yikuyi.activity.model.ActivityProductDraft;
import com.yikuyi.activity.vo.ActivityProducOrderVo;
import com.yikuyi.product.activity.IActivityProductResource;
import com.yikuyi.product.activity.bll.ActivityProductManager;
import com.ykyframework.exception.BusinessException;
import com.ykyframework.mqservice.sender.MsgSender;

/**
 * 活动相关服务
 * @author zr.wenjiao@yikuyi.com
 * @version 1.0.0
 */
@RestController
@RequestMapping("v1/activityproducts")
public class ActivityProductResource implements IActivityProductResource{
	
	private static final Logger logger = LoggerFactory.getLogger("activity");

	@Autowired
	private ActivityProductManager activityProductManager;
	
	@Autowired
	private MsgSender msgSender;

	@Value("${mqConsumeConfig.updateActivityProductQty.topicName}")
	private String updateActivityProductQtyTopicName;
	
	/**
	 * 由于下订单，根据订单数量和参加的活动，增减活动库存<br>
	 * 必须指定要参加的活动，根据指定的活动扣减redis中的库存，然后发送mq通知修改数据库中的库存。如果redis中不存在这个活动则抛出异常
	 * @author tongkun@yikuyi.com
	 * @param productOrders 下订单的活动商品列表
	 * @throws BusinessException 
	 */
	@Override
	@RequestMapping(value="/order",method=RequestMethod.PUT)
	public List<ActivityProducOrderVo> updateActivityProductQty(@RequestBody List<ActivityProducOrderVo> productOrders){
		try{
			String[] succerssKey = activityProductManager.updateActivityProductCacheQty(productOrders);
			if(null != succerssKey && succerssKey.length>0){
				logger.info("updateActivityProductQty msgSender:{}",succerssKey.length);
				msgSender.sendMsg(updateActivityProductQtyTopicName, succerssKey, null);//发送同步库存的mq
			}
		}catch(Exception e){
			logger.error("未预料到的异常：",e);
		}
		return productOrders;
	}


	/**
	 * 根据活动商品id查询活动商品信息
	 * @author zr.aoxianbing@yikuyi.com
	 * @param productId 活动商品id
	 */
	@Override
	@RequestMapping(value="/{activityProductId}",method=RequestMethod.GET)
	public ActivityProductDraft getProductById(@PathVariable(required=true) String activityProductId) {
		return activityProductManager.getProductById(activityProductId);
	}

	/**
	 * 编辑商品信息
	 * @author zr.aoxianbing@yikuyi.com
	 * @param ActivityProduct 活动商品
	 */
	@Override
	@RequestMapping(value="/{activityProductId}",method=RequestMethod.PUT)
	public void editProductInfo(@RequestBody ActivityProductDraft activityProduct) {
		activityProductManager.editProductInfo(activityProduct);
	}

	/**
	 * 保存商品历史记录
	 * @since 2017年6月19日
	 * @author tongkun@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/history",method=RequestMethod.PUT)
	public String saveActivitiesProductsHistory() {
		int result = activityProductManager.saveActivitiesProductsHistory();
		return "{result:'success',rowcount:'"+result+"'}";
	}
}