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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

@Configuration
public class MongoConfiguration {

	@Value("${spring.data.mongodb.uri}")
	private String mongoUriString;
	
	@Autowired
	private MongoClientURI mongoUri;

	@Bean
	public MongoClientURI getMongoUri() {
		return new MongoClientURI(mongoUriString);
	}

	@Bean
	public MongoClient initMongoClient() {
		MongoClient mongoClient = new MongoClient(mongoUri);;
		return mongoClient;
	}
	
}