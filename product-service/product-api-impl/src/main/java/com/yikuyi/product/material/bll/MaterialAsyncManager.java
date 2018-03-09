/*
 * Created: 2016年12月7日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.material.bll;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import org.bson.Document;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.ictrade.enterprisematerial.InventorySearchManager;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * 物料处理业务类
 * 
 * @author zr.shuzuo@yikuyi.com
 * @version 1.0.0
 */
@Service
public class MaterialAsyncManager {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MaterialAsyncManager.class);

	@Value("${spring.data.mongodb.uri}")
	private String mongoUri;
	
	@Autowired
	private InventorySearchManager inventorySearchManager;


	private static final String PRODUCT_COL_NAME = "product";

	private static final int BATCH_SIZE = 100;

	
	/**
	 * 同步一个分类下的所有数据到搜索引擎
	 * @param cateId 分类（大类）
	 * @return 
	 * @since 2017年12月6日
	 * @author tongkun@yikuyi.com
	 */
	@Async
	public Future<String> syncElasticsearchByCategory(MongoClient mongoClient,String databaseName,String indexName,Integer cateId,String vendorId){
		MongoDatabase mongoDatabase = mongoClient.getDatabase(databaseName);
		MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(PRODUCT_COL_NAME);
		//存放一批同步的数据
		List<Document> batchList = new ArrayList<>();
		//查询全部有效数据
				Document condition = new Document("status",1).append("spu.categories.0._id", cateId);
		if(vendorId!=null)
			condition.append("vendorId", vendorId);
		//查询全部有效数据
		FindIterable<Document> findIterable = mongoCollection.find(condition).projection(new Document("vendorCategories",0).append("spu.parameters", 0)).batchSize(BATCH_SIZE * 10);
		//循环全部数据
		findIterable.forEach(new Consumer<Document>() {
			@Override
			public void accept(Document doc) {
				//spu._id存在的才可以进行同步
				Document spu = doc.get("spu",Document.class);
				if(spu!=null&&spu.get("_id")!=null){
					batchList.add(doc);//可以同步的情况下加入到一批中
					//如果达到了一批次的数量，则发送到搜索引擎中
					if(batchList.size()==BATCH_SIZE){
						syncElasticsearchProduct(indexName, batchList);
						batchList.clear();
						//防止占满cpu，每批数据休息10毫秒
						try {
							Thread.sleep(5);
						} catch (Exception e) {
							logger.error(e.getMessage(),e);
						}
					}
				}
			}
		});
		//如果集合中还存在数据。则将剩下的数据同步入搜索引擎
		if(!batchList.isEmpty()){
			syncElasticsearchProduct(indexName, batchList);
		}
		return new AsyncResult<>("success");
	}

	private void syncElasticsearchProduct(String newIndex, List<Document> batchList) {
		try {
			List<String> errorDocList = inventorySearchManager.updateBatchAll(newIndex , "product" , batchList);
			if (null != errorDocList && !errorDocList.isEmpty()) {
				logger.error("syncElasticsearchProduct all error:" + errorDocList.get(0));
				errorDocList.clear();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			batchList.clear();
		}
	}

}