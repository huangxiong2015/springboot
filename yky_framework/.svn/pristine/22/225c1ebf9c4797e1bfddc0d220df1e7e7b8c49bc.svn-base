/*
 * Created: 2016年1月15日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ykyframework.springframework.web.servlet.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.support.RequestContext;

import com.ykyframework.exception.BusinessException;

/**
 * 支持ajax 异常处理的HandlerExceptionResolver，将异常返回成文本暴露给前端
 * 
 * @see http://blog.csdn.net/mr__fang/article/details/9092511
 * @author liaoke@yikuyi.com
 * @version 1.0.0
 */
public class CustomMappingExceptionResolver extends SimpleMappingExceptionResolver {
	/**
	 * AJAX错误消息模板
	 */
	private static final String ERR_MSG_TEMPLATE = "{\"code\": \"%s\", \"message\":\"%s\"}";

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		super.doResolveException(request, response, handler, ex);
		// Expose ModelAndView for chosen error view.
		String viewName = determineViewName(ex, request);
		if (viewName != null) {// JSP格式返回
//			if (!(request.getHeader("accept").indexOf("application/json") > -1
//					|| (request.getHeader("X-Requested-With") != null
//							&& request.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1))) {
			
			//如果不是XMLHttpRequest的异步请求，则维持原来的逻辑
			if (!(request.getHeader("X-Requested-With") != null
							&& request.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1)) {
				// 如果不是异步请求
				// Apply HTTP status code for error views, if specified.
				// Only apply it if we're processing a top-level request.
				Integer statusCode = determineStatusCode(request, viewName);
				if (statusCode != null) {
					applyStatusCodeIfPossible(request, response, statusCode);
				}
				return getModelAndView(viewName, ex, request);
			} else {// 否则将异常的消息以文本形式返回
				try {
					PrintWriter writer = response.getWriter();
					String errMsg = null;
					if (ex instanceof BusinessException) {
						BusinessException bex = (BusinessException) ex; 
						errMsg = String.format(ERR_MSG_TEMPLATE, bex.getCode(), bex.getMessage());
					} else {
						//TODO 此处看后期是否要屏蔽原始的 ex stacktrace
						errMsg = String.format(ERR_MSG_TEMPLATE, "undefined", ex.getMessage());
					}
					writer.write(errMsg);
					writer.flush();
				} catch (IOException e) {
					//do nothing
					logger.info(e);
				}
				return null;
			}
		} else {
			return null;
		}
	}
	
	
	public static String getLocaleErrorMessage(String key){
        HttpServletRequest request =  ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        RequestContext requestContext = new RequestContext(request);
        return requestContext.getMessage(key);
    }
}
