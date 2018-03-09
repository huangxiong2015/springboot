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
package com.ykyframework.web.aop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ykyframework.exception.DataNotFoundException;
import com.ykyframework.exception.InvalidDataException;
import com.ykyframework.exception.SystemException;

/**
 * REST服务异常处理的Aop adivce。如果遇到异常，统一返回ExceptionResponse的JSON结构体
 * 
 * @author liaoke@yikuyi.com
 * @version 1.0.0
 */
@ControllerAdvice(annotations = { RestController.class })
public class RestExceptionHandleAdvice {
	private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandleAdvice.class);
	// @Autowired
	// private MessageSource messageSource;

	// /**
	// * REST服务处理@Valid验证错的方法，将其消息以国际化的模式提供。
	// * @param ex
	// * @return
	// * @since 2016年1月20日
	// * @author liaoke@yikuyi.com
	// */
	// @ExceptionHandler(MethodArgumentNotValidException.class)
	// @ResponseStatus(HttpStatus.BAD_REQUEST)
	// @ResponseBody
	// public ExceptionResponse
	// processValidationError(MethodArgumentNotValidException ex) {
	// BindingResult result = ex.getBindingResult();
	//// FieldError error = result.getFieldError();
	// if (result.getFieldErrors().size() > 0) {
	// StringBuilder sb = new StringBuilder();
	// for (FieldError err : result.getFieldErrors()) {
	// sb.append(err.getField() + err.getDefaultMessage() + ";");
	// }
	// return new ExceptionResponse("ex.validate", sb.toString());
	// }
	// return null;
	// }

	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({ SystemException.class, Exception.class })
	@ResponseBody
	public ExceptionResponse handleSystemException(HttpServletRequest request, HttpServletResponse response,
			Exception ex) {
		logger.error("system error occured.", ex);
		return ExceptionResponse.create(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
	}

	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	@ExceptionHandler(SecurityException.class)
	@ResponseBody
	public ExceptionResponse handleSecurityException(HttpServletRequest request, HttpServletResponse response,
			SecurityException ex) {
		return ExceptionResponse.create(HttpStatus.FORBIDDEN.value(), ex.getMessage());
	}

	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ExceptionHandler(DataNotFoundException.class)
	@ResponseBody
	public ExceptionResponse handleDataNotFoundException(HttpServletRequest request, HttpServletResponse response,
			DataNotFoundException ex) {
		return ExceptionResponse.create(HttpStatus.NOT_FOUND.value(), ex.getMessage());
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidDataException.class)
	@ResponseBody
	public ExceptionResponse handleInvalidDataException(HttpServletRequest request, HttpServletResponse response,
			InvalidDataException ex) {
		return ExceptionResponse.create(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
	}

	/**
	 * rest服务异常标准的返回结构体。
	 * 
	 * <pre>
	 * {
	 * "timestamp": 1476527450606,
	 * "status": 401,
	 * "error": "Unauthorized",
	 * "message": "Full authentication is required to access this resource",
	 * "path": "/v1/users"
	 * }
	 * </pre>
	 * 
	 * @author liaoke@yikuyi.com
	 * @version 1.0.0
	 */
	public static class ExceptionResponse {
		/**
		 * 时间戳
		 */
		private long timestamp;
		/**
		 * 错误状态码
		 */
		private int status;
		/**
		 * http status code对应的信息
		 */
		private String error;
		/**
		 * 具体错误消息
		 */
		private String message;
		private String path;

		public static ExceptionResponse create(int status, String errorMsg) {
			ExceptionResponse res = new ExceptionResponse();
			res.timestamp = System.currentTimeMillis();
			res.status = status;
			res.error = HttpStatus.valueOf(status).getReasonPhrase();
			res.message = errorMsg;
			return res;
		}

		public long getTimestamp() {
			return timestamp;
		}

		public int getStatus() {
			return status;
		}

		public String getError() {
			return error;
		}

		public String getMessage() {
			return message;
		}

		public String getPath() {
			return path;
		}
	}

}
