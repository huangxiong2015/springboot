/*
 * Created: 2018年3月1日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2018 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.ykyframework.api.persist.IdGen;

@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(InterceptorConfig.class);
	
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor (){

			@Override
			public boolean preHandle(HttpServletRequest req, HttpServletResponse arg1, Object arg2) throws Exception {
				long startTime = System.currentTimeMillis();
				long traceId = IdGen.nextId(); 
				req.setAttribute("interceptor-startTime", startTime);
				req.setAttribute("interceptor-traceId", traceId);
				logger.info(new StringBuilder("mvc全局拦截器-pre：traceId:["+traceId+"]请求ip[").append(getRemoteHost(req)).append("]请求地址[").append(req.getRequestURL()).append("]").toString());
				return true;
			}
			
			@Override
			public void afterCompletion(HttpServletRequest req, HttpServletResponse arg1, Object arg2, Exception arg3)
					throws Exception {
				logger.info(new StringBuilder("mvc全局拦截器-after：traceId:["+req.getAttribute("interceptor-traceId")+"]请求ip[").append(getRemoteHost(req))
						.append("]请求地址[").append(req.getRequestURL()).append("]花费时间[").append(System.currentTimeMillis()-(Long)req.getAttribute("interceptor-startTime")).append("]").toString());
			}

			@Override
			public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
					throws Exception {
			}
        	
        }).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
    
    /**
     * 处理经过ngix转发后的客户端ip（作废）
     * 只返回客户端ip
     * @param request
     * @return
     * @since 2018年3月1日
     * @author tongkun@yikuyi.com
     */
    private String getRemoteHost(javax.servlet.http.HttpServletRequest request){
//        String ip = request.getHeader("x-forwarded-for");
//        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
//            ip = request.getHeader("Proxy-Client-IP");
//        }
//        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
//            ip = request.getHeader("WL-Proxy-Client-IP");
//        }
//        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
           String ip = request.getRemoteAddr();
//        }
        return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
    }
}
