/*
 * Created: 2016年1月16日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ykyframework.exception;

/**
 * 调用外部接口返回的通用结构体。如果错误码不是ok则认为是错误的。也可作为AJAX异常返回的标准结构体
 * @author liaoke@yikuyi.com
 * @version 1.0.0
 */
public class ExceptionResponse {
	public static final String OK_CODE = "ok";
	
	public static final ExceptionResponse OK = ExceptionResponse.create(OK_CODE, null);
	public static final ExceptionResponse ERROR = ExceptionResponse.create("common error", null);
	
	private String errMsg;
	private String errCode;

	/**
	 * Construction Method
	 * 
	 * @param code
	 * @param message
	 */
	public ExceptionResponse(String code, String message) {
		this.errMsg = message;
		this.errCode = code;
	}

	public static ExceptionResponse create(String code, String message) {
		return new ExceptionResponse(code, message);
	}

	public String getErrMsg() {
		return errMsg;
	}

	public String getErrCode() {
		return errCode;
	}
	
	public boolean isSuccess() {
		return OK_CODE.equals(this.errCode);
	}

	

}
