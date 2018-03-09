/*
 * Created: 2017年12月19日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.specialoffer.dao;

import org.bson.Document;
import org.springframework.stereotype.Service;

import com.mongodb.client.MongoCollection;
import com.yikuyi.product.common.dao.BaseMongoClient;

@Service
public class SpecialOfferProductDraftClient extends BaseMongoClient{
	public static final String COLLECTION_NAME = "special_offer_product_draft";

	@Override
	public MongoCollection<Document> getCollection() {
		this.collectionName = COLLECTION_NAME;
		return super.getCollection();
	}
}
