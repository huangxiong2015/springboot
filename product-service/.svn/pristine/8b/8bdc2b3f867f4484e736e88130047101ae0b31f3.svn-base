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
package com.yikuyi.log.vo;

import com.alibaba.fastjson.JSONObject;

/**
 * 活动统计日志VO
 * @author zr.wanghong
 * @version 1.0.0
 * @since 2017年6月9日
 */
public class ActivityStatisticsLog{
	
	public enum ActivityStatisticsLogType{
		/** pv数**/
		PV,
		/** uv数**/
		UV,
		/** 商品点击**/
		PRODUCT_CLICK,
		/** 商品流量 **/
		PRODUCT_FLOW,
		/** 购物车 **/
		SHOPPING_CART,
		/** 生成订单 **/
		ORDER,
		/** 支付订单**/
		PAYMENTORDER,
		
	}
	
	/** 操作人用户ID **/
	private String operateUserId;
	/** 客户端IP**/
	private String clientIp;
	/** 日志类型**/
	private ActivityStatisticsLogType logType;
	/** 日志内容**/
	private JSONObject logContent;
	public String getOperateUserId() {
		return operateUserId;
	}
	public void setOperateUserId(String operateUserId) {
		this.operateUserId = operateUserId;
	}
	public String getClientIp() {
		return clientIp;
	}
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	public ActivityStatisticsLogType getLogType() {
		return logType;
	}
	public void setLogType(ActivityStatisticsLogType logType) {
		this.logType = logType;
	}
	public JSONObject getLogContent() {
		return logContent;
	}
	public void setLogContent(JSONObject logContent) {
		this.logContent = logContent;
	}
	
}
