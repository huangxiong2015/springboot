/*
 * Created: 2016年12月12日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.config;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import com.yikuyi.party.supplier.bll.SupplierManager;

@Configuration
public class SupplierCacheConfiguration implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(SupplierCacheConfiguration.class);

	@Autowired
	private SupplierManager supplierManager;

	@Override
	public void run(String... args) throws Exception {
		supplierManager.initSupplierCache(Collections.emptyList());
		logger.info("Supplier cache init complite!");
	}

}