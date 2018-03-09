/*
 * Created: 2016年9月23日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package org.jasig.cas.client.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * SessionMappingStorage初始化，通过spring bean的初始化，将入RedisBackedSessionMappingStorage注入到SingleSignOutFilter中
 * @author liaoke@yikuyi.com
 * @version 1.0.0
 */
@Configuration
public class SessionMappingStorageConfig {
	
	@Autowired
	private SessionMappingStorage sessionMappingStorage;
	
	public SessionMappingStorage getSessionMappingStorage() {
		return sessionMappingStorage;
	}

	public void setSessionMappingStorage(SessionMappingStorage sessionMappingStorage) {
		this.sessionMappingStorage = sessionMappingStorage;
		SingleSignOutFilter.getSingleSignOutHandler().setSessionMappingStorage(sessionMappingStorage);
	}

}
