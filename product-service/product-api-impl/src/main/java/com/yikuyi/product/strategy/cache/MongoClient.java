package com.yikuyi.product.strategy.cache;

import org.bson.Document;
import org.springframework.stereotype.Service;

import com.mongodb.client.MongoCollection;
import com.yikuyi.product.common.dao.BaseMongoClient;

@Service
public class MongoClient extends BaseMongoClient {

	public MongoCollection<Document> getCollection(String collectionName) {
		this.collectionName = collectionName;
		return super.getCollection();
	}

}
