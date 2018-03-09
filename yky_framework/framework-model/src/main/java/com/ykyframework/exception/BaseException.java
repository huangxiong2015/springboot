/*
 * Created: 2015年12月27日
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
 * 易库易框架定义的基础Exception
 * @author liaoke@yikuyi.com
 * @version 1.0.0
 */
public class BaseException extends Exception {
	
	/** 
	 * 
	 */
	private static final long serialVersionUID = -7634619690750740128L;
	
	public BaseException() {
	}

	
	public BaseException(String message) {
		super(message);
	}
	
	
	public BaseException(String message, Throwable cause) {
		super(message, cause);
	}
	

}
