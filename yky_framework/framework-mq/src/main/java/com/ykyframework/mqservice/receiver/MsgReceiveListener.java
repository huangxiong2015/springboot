/*
 * Created: 2016年3月22日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2015 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ykyframework.mqservice.receiver;

import java.io.Serializable;
import java.util.Map;

import com.ykyframework.context.ContextAttributes;
import com.ykyframework.context.RuntimeContextHolder;

/**
 * @author liudian@yikuyi.com
 *
 */
@FunctionalInterface
public interface MsgReceiveListener {

	public void onMessage(Serializable msg);

	default void onMessage(Serializable msg, Map<String, String> properties) {
		this.preProcess(msg, properties);
		this.onMessage(msg);
		this.postProcess(msg, properties);
	}
	
	default void preProcess(Serializable msg, Map<String, String> properties) {
		RuntimeContextHolder.setContextAttributes(new ContextAttributes());
		if (properties != null && !properties.isEmpty())  {
			RuntimeContextHolder.currentContextAttributes().putAttribute(properties);
		}
	}
	
	default void postProcess(Serializable msg, Map<String, String> properties) {
		RuntimeContextHolder.currentContextAttributes().contextClosed();
		RuntimeContextHolder.resetContextAttributes();
	}

}
