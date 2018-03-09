/*
 * Created: 2017年2月27日
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
public class MQSystemException extends RuntimeException {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9125976255993459376L;


	public MQSystemException(Throwable cause) {
		super(cause.getMessage(), cause);
	}
	
	
	public MQSystemException(String message, Throwable cause) {
		super(message, cause);
	}
	
}