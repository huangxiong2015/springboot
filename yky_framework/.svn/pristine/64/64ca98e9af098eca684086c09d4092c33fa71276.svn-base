/*
 * Created: 2017年7月10日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.framework.springboot.audit;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.jasig.inspektr.common.spi.ClientInfoResolver;
import org.jasig.inspektr.common.web.ClientInfo;
import org.jasig.inspektr.common.web.ClientInfoHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class YkyClientInfoResolver implements ClientInfoResolver{

	 private final Logger log = LoggerFactory.getLogger(getClass());

	 	@Override
	    public ClientInfo resolveFrom(final JoinPoint joinPoint, final Object retVal) {
	 		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
	 		if(requestAttributes != null){
	 			HttpServletRequest request =  ((ServletRequestAttributes)requestAttributes).getRequest();
		    	String clientIpAddress =  request.getHeader("X-Forwarded-For") != null ? request.getHeader("X-Forwarded-For") : request.getRemoteAddr();
		    	int pos = clientIpAddress.contains("\\,")?clientIpAddress.indexOf("\\,"):clientIpAddress.length();
		    	ClientInfo ykyClientInfo = new ClientInfo(request.getLocalAddr(),clientIpAddress.substring(0,pos));
		    	ykyClientInfo.getClientIpAddress();
		    	ClientInfoHolder.setClientInfo(ykyClientInfo);
	 		}
	    	final ClientInfo clientInfo = ClientInfoHolder.getClientInfo();

	        if (clientInfo != null) {
	            return clientInfo;
	        }

	        log.warn("No ClientInfo could be found.  Returning empty ClientInfo object.");

	        return ClientInfo.EMPTY_CLIENT_INFO;
	    }
	 	
	 	  
}
