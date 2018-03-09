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

import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ykyframework.exception.BusinessException;
import com.ykyframework.exception.DataNotFoundException;
import com.ykyframework.exception.ExceptionResponse;
import com.ykyframework.exception.InvalidDataException;
import com.ykyframework.exception.PermissionException;
import com.ykyframework.exception.SecurityException;
import com.ykyframework.exception.SystemException;

/**
 * REST服务异常处理的Aop adivce。如果遇到异常，统一返回ExceptionResponse的JSON结构体
 * 
 * @author liaoke@yikuyi.com
 * @version 1.0.0
 */
@ControllerAdvice(annotations = { RestController.class })
@Order(2)
public class RestControllerAdvice {
	
	private static final Logger logger = LoggerFactory.getLogger(RestControllerAdvice.class);
	
	@Autowired
	private MessageSource messageSource;

	/**
	 * REST服务处理@Valid验证错的方法，将其消息以国际化的模式提供。
	 * @param ex
	 * @return
	 * @since 2016年1月20日
	 * @author liaoke@yikuyi.com
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ExceptionResponse processValidationError(MethodArgumentNotValidException ex) {
		BindingResult result = ex.getBindingResult();
//		FieldError error = result.getFieldError();
		if (result.getFieldErrors().size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (FieldError err : result.getFieldErrors()) {
				sb.append(err.getField() + err.getDefaultMessage() + ";");
			}
			return new ExceptionResponse("ex.validate", sb.toString());
		}
		return null;
	}

	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	@ExceptionHandler({UnauthorizedException.class})
	@ResponseBody
	public ExceptionResponse handlePermissionException(HttpServletRequest request, HttpServletResponse response,
			PermissionException ex) {
		return handleBaseException(request, response, ex);
	}

	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(BusinessException.class)
	@ResponseBody
	public ExceptionResponse handleBusinessException(HttpServletRequest request, HttpServletResponse response,
			BusinessException ex) {
		return ExceptionResponse.create(ex.getCode(), messageSource.getMessage(ex.getCode(), null, ex.getMessage(), LocaleContextHolder.getLocale()));
		//return ExceptionResponse.create(ex.getCode(), getLocaleErrorMessage(ex.getCode()));
	}
	
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(SystemException.class)
	@ResponseBody
	public ExceptionResponse handleSystemException(HttpServletRequest request, HttpServletResponse response,
			SystemException ex) {
		return handleBaseException(request, response, ex);
	}

	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	@ExceptionHandler(SecurityException.class)
	@ResponseBody
	public ExceptionResponse handleSecurityException(HttpServletRequest request, HttpServletResponse response,
			SecurityException ex) {
		return handleBaseException(request, response, ex);
	}

	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ExceptionHandler(DataNotFoundException.class)
	@ResponseBody
	public ExceptionResponse handleDataNotFoundException(HttpServletRequest request, HttpServletResponse response,
			DataNotFoundException ex) {
		return handleBaseException(request, response, ex);
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidDataException.class)
	@ResponseBody
	public ExceptionResponse handleInvalidDataException(HttpServletRequest request, HttpServletResponse response,
			InvalidDataException ex) {
		return handleBaseException(request, response, ex);
	}

	private ExceptionResponse handleBaseException(HttpServletRequest request, HttpServletResponse response,
			Exception ex) {
		logger.error("A exception throws from service, caused by:", ex);
		return ExceptionResponse.create(ex.getClass().getSimpleName(), null);
	}

	@ResponseStatus(value = HttpStatus.PRECONDITION_REQUIRED)
	@ExceptionHandler(org.apache.shiro.authz.UnauthenticatedException.class)
	@ResponseBody
	public ExceptionResponse handleShiroUserPermissionException(HttpServletRequest request, HttpServletResponse response,
			UnauthenticatedException ex) {
		return ExceptionResponse.create(ex.getClass().getSimpleName(), null);
	}
	
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ExceptionResponse handleException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
		//未处理异常在这里需要写异常日志
		logger.error("A exception throws from service, caused by:", ex);
		// TODO 考虑是否要屏蔽原始出错信息
		return ExceptionResponse.create("ex.unknown", ex.getMessage());
	}
//	private static String getLocaleErrorMessage(String key) {
//		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
//				.getRequest();
//		RequestContext requestContext = new RequestContext(request);
//		return requestContext.getMessage(key);
//	}
}
