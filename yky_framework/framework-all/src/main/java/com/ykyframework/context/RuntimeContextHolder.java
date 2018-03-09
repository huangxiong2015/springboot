/*
 * Created: 2017年4月12日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
/**
 * 
 */
package com.ykyframework.context;

/**
 * @author liudian@yikuyi.com
 * @version 1.0.0
 */
public class RuntimeContextHolder {

	private static final ThreadLocal<ContextAttributes> contextAttributesHolder = ThreadLocal.withInitial(() -> {
		ContextAttributes attributes = new ContextAttributes();
		attributes.setContextActive(false);
		return attributes;
	});	

	private RuntimeContextHolder() {
		throw new IllegalAccessError("Utility class");
	}

	public static ContextAttributes currentContextAttributes() {
		return getContextAttributes();
	}

	public static void resetContextAttributes() {
		contextAttributesHolder.remove();
	}

	public static ContextAttributes getContextAttributes() {
		return contextAttributesHolder.get();
	}

	public static void setContextAttributes(ContextAttributes attributes) {
		if (attributes == null) {
			resetContextAttributes();
		} else {
			contextAttributesHolder.set(attributes);
		}
	}

}
