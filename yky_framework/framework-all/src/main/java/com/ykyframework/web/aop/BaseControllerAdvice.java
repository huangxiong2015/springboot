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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ykyframework.exception.BusinessException;
import com.ykyframework.exception.DataNotFoundException;
import com.ykyframework.exception.PermissionException;

/**
 * Controller服务异常处理的Aop adivce。如果遇到异常则进入对应的报错页面，并根据逻辑记录日志
 * 
 * @author liaoke@yikuyi.com
 * @version 1.0.0
 */
@ControllerAdvice(annotations = { Controller.class })
@Order(1)
public class BaseControllerAdvice {
	private static final Logger logger = LoggerFactory.getLogger(BaseControllerAdvice.class);

	@ExceptionHandler(BusinessException.class)
	public String handleBusinessException(HttpServletRequest request, HttpServletResponse response,
			BusinessException ex) {
		request.setAttribute("ex", ex);
		return "errors/biz";
	}

	@ExceptionHandler(DataNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleDataNotFoundException(HttpServletRequest request, HttpServletResponse response,
			DataNotFoundException ex) {
		logger.warn(ex.getMessage());
		return "errors/404";
	}

	// @ExceptionHandler(SystemException.class)
	// public String handleSystemException(HttpServletRequest request,
	// HttpServletResponse response,
	// SystemException ex) {
	// logger.error("sys err", ex);
	// request.setAttribute("ex", ex);
	// return "errors/error";
	// }

	@ExceptionHandler({ PermissionException.class, UnauthorizedException.class })
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public String handlePermissionException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
		logger.warn(ex.getMessage());
		return "errors/401";
	}

	// @ExceptionHandler({UnauthorizedException.class})
	// @ResponseStatus(HttpStatus.UNAUTHORIZED)
	// public String handleUnauthorizedException(HttpServletRequest request,
	// HttpServletResponse response,
	// UnauthorizedException ex) {
	// logger.warn(ex.getMessage());
	// return "errors/401";
	// }

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleException(HttpServletRequest request, HttpServletResponse response, Exception ex) throws IOException {
		logger.error("System error occurred.", ex);
		request.setAttribute("ex", ex);
		return "errors/error";
	}
}
