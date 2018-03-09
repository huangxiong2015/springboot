/*
 * Created: 2017年12月7日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.common.dao;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * mongo连接的基础类
 * @author tongkun@yikuyi.com
 * @version 1.0.0
 */
@Service
public class BaseMongoClient {
	
	/**
	 * uri的解析对象
	 */
	@Autowired
	private MongoClientURI uri;
	
	/**
	 * 表名，获取表对象的时候需要使用
	 */
	protected String collectionName;
	
	/**
	 * 注入的连接对象
	 */
	@Autowired
	private MongoClient client;
	
	/**
	 * 获取表对象，需要子类继承，并设置了collection才可以使用
	 * @return
	 * @since 2017年12月7日
	 * @author tongkun@yikuyi.com
	 */
	public MongoCollection<Document> getCollection(){
		return client.getDatabase(uri.getDatabase()).getCollection(collectionName);
	}
	
	/**
	 * 获取数据库对象，   用于执行批处理使用
	 * @return
	 * @since 2017年12月7日
	 * @author tongkun@yikuyi.com
	 */
	public MongoDatabase getDatabase(){
		return client.getDatabase(uri.getDatabase());
	}
}
