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

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ContextAttributes implements Serializable {	
	
	private final HashMap<String, Object> attributes = new LinkedHashMap<>();

	/**
	 * 
	 */
	private static final String CONTEXT_IS_NOT_ACTIVE = "Cannot ask for attributes - context is not active anymore!";

	/**
	 * 
	 */
	private static final long serialVersionUID = -3534576868520061811L;

	private volatile boolean contextActive = true;

	/**
	 * @param contextActive the contextActive to set
	 */
	final void setContextActive(boolean contextActive) {
		this.contextActive = contextActive;
	}

	/**
	 * @return the contextActive
	 */
	public final boolean isContextActive() {
		return contextActive;
	}

	public void contextClosed() {
		this.contextActive = false;
	}

	public Object get(String attrID) {
		if (!isContextActive()) {
			throw new IllegalStateException(CONTEXT_IS_NOT_ACTIVE);
		}
		return this.attributes.get(attrID);
	}
	
	public Object getAttribute(String attrID) {
		return this.get(attrID);
	}

	public void setAttribute(String attrID, Object val) {
		if (!isContextActive()) {
			throw new IllegalStateException(CONTEXT_IS_NOT_ACTIVE);
		}
		this.attributes.put(attrID, val);
	}
	
	public void putAttribute(Map<String, ?> attrs) {
		attrs.forEach(attributes::put);
	}

	public Object remove(String attrID) {
		if (!isContextActive()) {
			throw new IllegalStateException(CONTEXT_IS_NOT_ACTIVE);
		}
		return this.attributes.remove(attrID);
	}
	
	public void removeAttribute(String attrID) {
		if (!isContextActive()) {
			throw new IllegalStateException(CONTEXT_IS_NOT_ACTIVE);
		}
		this.attributes.remove(attrID);
	}
	

	
	public void removeAll() {
		if (!isContextActive()) {
			throw new IllegalStateException(CONTEXT_IS_NOT_ACTIVE);
		}
		this.attributes.clear();
	}
}