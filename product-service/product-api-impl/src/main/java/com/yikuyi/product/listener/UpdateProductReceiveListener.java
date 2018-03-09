/*
 * Created: 2017年11月14日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.listener;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yikuyi.product.goods.manager.ProductManager;
import com.yikuyi.product.vo.RawData;
import com.ykyframework.mqservice.receiver.MsgReceiveListener;

@Service
public class UpdateProductReceiveListener implements MsgReceiveListener {
	private static final Logger logger = LoggerFactory.getLogger(UpdateProductReceiveListener.class);
	
	@Autowired
	private ProductManager productManager;
	@SuppressWarnings("unchecked")
	@Override
	public void onMessage(Serializable arg) {
		long s = System.currentTimeMillis();
		String message = null;
		try {
			logger.info("UpdateProductReceiveListener start");
			if (arg instanceof ArrayList) {
				ArrayList<RawData> rawData = (ArrayList<RawData>) arg;
				if (CollectionUtils.isEmpty(rawData)) {
					message = "传入参数为空";
					return;
				}
				productManager.updateProductInfoBatch(rawData);//批量更新商品数据
			} else {
				logger.error("未知的参数：" + arg.getClass());
			}
		} catch (Exception e) {
			logger.error("UpdateProductReceiveListener异常");
		} finally {
			logger.info("根据productId更新对应的商品完毕：消息:{},耗时:{}", message, System.currentTimeMillis() - s);
		}
	}
}
