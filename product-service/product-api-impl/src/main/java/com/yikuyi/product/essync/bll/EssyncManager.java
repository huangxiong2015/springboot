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
package com.yikuyi.product.essync.bll;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ictrade.enterprisematerial.InventorySearchManager;
import com.ictrade.index.EsIndexManager;
import com.ictrade.index.IndexProcesserFactory;
import com.ictrade.index.IndexProcesserFactory.IndexFactoryType;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.yikuyi.document.model.Document;
import com.yikuyi.document.model.Document.DocumentStatus;
import com.yikuyi.material.vo.MaterialVo;
import com.yikuyi.product.document.bll.DocumentManager;

/**
 * DocumentLog业务处理类
 * 
 * @author zr.shuzuo@yikuyi.com
 * @version 1.0.0
 */
@Service
@Transactional
public class EssyncManager {

	private static final Logger logger = LoggerFactory.getLogger(EssyncManager.class);
	
	@Autowired
	private EsIndexManager esIndexManager;
	
	@Autowired
	private InventorySearchManager inventorySearchManager;
	
	@Autowired
	private DocumentManager documentManager;
	
	@Value("${spring.data.mongodb.uri}")
	private String mongoUri;
	
	private static final int DEFAULT_PAGE_SIZE = 1000;
	
	private static final String PRODUCT_STAND_COL_NAME = "product_stand";
	
	/**
	 * 标准型号同步实现类
	 * @param materialVo
	 * @since 2017年6月14日
	 * @author zr.shuzuo@yikuyi.com
	 */
	public void syncElasticsearchNo(MaterialVo materialVo){
		Document doc = new Document();
		doc.setId(materialVo.getDocId());
		MongoClientURI mongoClientURI = new MongoClientURI(mongoUri);
		try (MongoClient mongoClient = new MongoClient(mongoClientURI);) {

			String newIndex = esIndexManager.createIndex(IndexProcesserFactory.createIndexProcesser(IndexFactoryType.COMPLETION));

			MongoDatabase mongoDatabase = mongoClient.getDatabase(mongoClientURI.getDatabase());
			MongoCollection<org.bson.Document> mongoCollection = mongoDatabase.getCollection(PRODUCT_STAND_COL_NAME);
			
			BasicDBObject keys = new BasicDBObject();
		    keys.put("manufacturerPartNumber", 1);
			
			FindIterable<org.bson.Document> findIterable = mongoCollection.find().projection(keys).batchSize(DEFAULT_PAGE_SIZE);
			MongoCursor<org.bson.Document> mongoCursor = findIterable.iterator();
			List<org.bson.Document> batchList = new ArrayList<>();
			long no = 0L;
			long count = mongoCollection.count();
			while (mongoCursor.hasNext()) {
				org.bson.Document docTemp = mongoCursor.next();
				Object idObj = docTemp.get("_id");
				if(idObj instanceof String){
					docTemp.append("id", docTemp.getString("_id"));
				}else if(idObj instanceof ObjectId){
					docTemp.append("id", docTemp.getObjectId("_id").toString());
				}
				no++;
				docTemp.remove("_id");
				docTemp.append("type", "1");
				org.bson.Document inputTemp = new org.bson.Document();
				inputTemp.append("input", docTemp.getString("manufacturerPartNumber"));
				docTemp.append("suggest", inputTemp);
				
				batchList.add(docTemp);
				if (no % DEFAULT_PAGE_SIZE == 0 || count == no) {
					List<String> errorDocList = inventorySearchManager.updateProductStandBatchAll(newIndex, "completion", batchList);
					if (null != errorDocList && !errorDocList.isEmpty()) {
						logger.error("syncElasticsearchProductStand all error:" + errorDocList.get(0));
						errorDocList.clear();
					}
					batchList.clear();
					Thread.sleep(10);
				}
			}
			//查询别名为completion的老的index
			String oldIndex = StringUtils.join(esIndexManager.getIndexByAliases("completion"),",");
			
			//为新索引创建别名
			esIndexManager.addAlias(newIndex, "completion");
			
			Thread.sleep(1000);
			
			if(StringUtils.isNotBlank(oldIndex)){// 老的索引删除别名
				esIndexManager.removeAlias(oldIndex.split(","), new String[]{"completion"});
				Thread.sleep(1000);
			}
			
			//计算总数
			doc.setStatusId(DocumentStatus.DOC_PRO_SUCCESS);
			doc.setComments(newIndex+"</br>"+oldIndex+"</br>"+esIndexManager.getIndexTypeCount(newIndex, "completion"));
			documentManager.updateDoc(doc);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			doc.setStatusId(DocumentStatus.DOC_PRO_FAILED);
			doc.setComments(e.getMessage());
			documentManager.updateDoc(doc);
		}
	}
}