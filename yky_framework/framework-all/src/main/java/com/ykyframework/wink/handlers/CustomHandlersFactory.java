/*
 * Created: 2015年12月21日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2015 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ykyframework.wink.handlers;

import java.util.Arrays;
import java.util.List;

import org.apache.wink.server.handlers.HandlersFactory;
import org.apache.wink.server.handlers.RequestHandler;
import org.apache.wink.server.handlers.ResponseHandler;

/**
 * 平台框架自定义的wink的handler工厂，内置了一些自定义的handler
 * @author liaoke@yikuyi.com
 * @version 1.0.0
 */
public class CustomHandlersFactory extends HandlersFactory {

	@Override
	public List<? extends ResponseHandler> getErrorHandlers() {
		// TODO Auto-generated method stub
		return super.getErrorHandlers();
	}

	@Override
	public List<? extends RequestHandler> getRequestHandlers() {
		// TODO Auto-generated method stub
		return super.getRequestHandlers();
	}

	@Override
	public List<? extends ResponseHandler> getResponseHandlers() {
		return Arrays.asList(new SetHttpRespCacheHandler());
	}

}
