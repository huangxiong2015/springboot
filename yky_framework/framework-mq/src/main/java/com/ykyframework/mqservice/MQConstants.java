/*
 * Created: 2017年4月13日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
/**
 * 
 */
package com.ykyframework.mqservice;

/**
 * @author liudian@yikuyi.com
 * @version 1.0.0
 */
public class MQConstants {

	private MQConstants() {
		throw new IllegalAccessError("Utility class");
	}

	//hader for username
	public static final String MSG_HEADER_USER_NAME = "msg.header.username";

	//header for password
	public static final String MSG_HEADER_PASS_WORD = "msg.header.password";
	
	public static final String MSG_PROPERTY_KEY = "key";
	
	public static final String MSG_PROPERTY_TAG = "tag";

}
