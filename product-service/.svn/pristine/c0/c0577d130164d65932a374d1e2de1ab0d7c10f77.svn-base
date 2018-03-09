/*
 * Created: 2017年6月9日
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.yikuyi.log.vo.ActivityStatisticsLog;
import com.yikuyi.product.log.ActivityStatisticsLogUtils;
import com.ykyframework.mqservice.receiver.MsgReceiveListener;

/**
 * 活动统计日志消息监听类
 * @author zr.wanghong
 * @version 1.0.0
 * @since 2017年6月9日
 */
@Service
public class StatisticsActivityLogListener implements MsgReceiveListener {
	private static final Logger logger = LoggerFactory.getLogger(StatisticsActivityLogListener.class);

	@Override
	public void onMessage(Serializable arg) {
		if (null == arg) {
			logger.error("StatisticsActivityLogListener.java arg is null!");
			return;
		}
		logger.debug("StatisticsActivityLogListener.java start");
		ActivityStatisticsLog log = (ActivityStatisticsLog)arg;
		ActivityStatisticsLogUtils.contactLog(log);
		
		logger.debug("StatisticsActivityLogListener.java end");
	}

}
