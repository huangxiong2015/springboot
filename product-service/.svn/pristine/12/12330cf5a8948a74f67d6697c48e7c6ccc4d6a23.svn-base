package com.yikuyi.product.listener;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yikuyi.product.activity.bll.ActivityProductManager;
import com.ykyframework.mqservice.receiver.MsgReceiveListener;

/**
 * 更新活动商品库存
 * @author tongkun@yikuyi.com
 * @version 1.0.0
 */
@Service
public class UpdateActivityProductQtyListener implements MsgReceiveListener {

	private static final Logger logger = LoggerFactory.getLogger("activity");

	@Autowired
	private ActivityProductManager activityProductManager;

	@Override
	public void onMessage(Serializable arg) {
		logger.info("更新活动商品库存 update_qty Listener start");
		if (null == arg) {
			logger.error("更新活动商品库存  arg is null");
			return;
		}
		//转换key
		String[] keys = (String[])arg;
		for(String key:keys){
			//执行同步更新
			activityProductManager.syncActivityProductQty(key);
		}
		logger.info("更新活动商品库存 update_qty Listener end");
	}
}