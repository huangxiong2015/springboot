/*
 * Created: 2017年3月16日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ictrade.elasticsearch.ElasticsearchClient;
import com.ictrade.enterprisematerial.EsProductManager;
import com.ictrade.enterprisematerial.InventorySearchManager;
import com.ictrade.index.EsIndexManager;

@Configuration
public class SearchConfiguration {

	@Value("${elasticsearch.cluster_name}")
	private String clusterName;
	@Value("${elasticsearch.search_server}")
	private String searchServer;
	@Value("${elasticsearch.search_port}")
	private Integer searchPort;

	@Value("${elasticsearch.inventory.index}")
	private String index;
	@Value("${elasticsearch.inventory.type}")
	private String type;

	@Bean(initMethod="init",destroyMethod="destroy")
	public ElasticsearchClient initElasticsearchClient() {
		return new ElasticsearchClient(clusterName, searchServer, searchPort);
	}

	@Bean
	public EsIndexManager initEsIndexManager() {
		return new EsIndexManager(initElasticsearchClient());
	}

	@Bean
	public InventorySearchManager initInventorySearchManager() {
		return new InventorySearchManager(initElasticsearchClient(), index, type);
	}
	@Bean
	public EsProductManager initEsProductManager(){
		return new EsProductManager(clusterName, searchServer, searchPort);
	}
}