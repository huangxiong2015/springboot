/*
 * Created: 2015年10月25日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2015 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ykyframework.exception;

/**
 * 数据不存在异常。
 * @author liaoke@yikuyi.com
 * @since 1.0.0
 */
public class DataNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5166740141872520534L;
	
	
	public DataNotFoundException(String message) {
		super(message);
	}
	
	
	public DataNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
