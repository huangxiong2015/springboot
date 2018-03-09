/*
 * Created: 2017年3月28日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.common.utils;

/**
 * 
 * @author zr.wujiajun@yikuyi.com
 * @version 1.0.0
 */
public class BusiErrorCode {
	/**
	 * 会员中心待审核状态下不能重复申请
	 */
	public static final String EXIST_ENT_AUTHENTICATION = "EXIST_ENT_AUTHENTICATION";
	
	/**
	 * 会员中心待审核状态下不能重复申请
	 */
	public static final String EXIST_SUPPLIERALIAS_NAME = "EXIST_SUPPLIERALIAS_NAME";
	
	/**
	 * 没有符合条件的数据
	 */
	public static final String AUDIT_LOG_NULL = "AUDIT_LOG_NULL";
	
	private BusiErrorCode(){}

}