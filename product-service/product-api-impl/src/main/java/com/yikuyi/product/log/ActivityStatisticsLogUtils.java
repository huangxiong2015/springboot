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
package com.yikuyi.product.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yikuyi.log.vo.ActivityStatisticsLog;

public class ActivityStatisticsLogUtils {
	private static final Logger logger = LoggerFactory.getLogger(ActivityStatisticsLogUtils.class);

	ActivityStatisticsLogUtils(){}
	
	public static void contactLog(ActivityStatisticsLog log) {
		logger.info("operateUserId:{},clientIp:{},logType:{},logContent:{}",
				log.getOperateUserId(),log.getClientIp(),log.getLogType(),log.getLogContent());
	}
}
