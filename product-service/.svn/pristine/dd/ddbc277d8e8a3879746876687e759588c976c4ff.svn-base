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
package com.yikuyi.product.document.bll;

import org.bson.assertions.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yikuyi.document.model.ProductDocument;
import com.yikuyi.product.document.dao.ProductDocumentDao;

/**
 * ProductDocument业务处理类
 * 
 * @author zr.shuzuo@yikuyi.com
 * @version 1.0.0
 */
@Service
@Transactional
public class ProductDocumentManager {

	@Autowired
	private ProductDocumentDao productDocumentDao;
	
	/**
	 * 插入文档
	 * @param doc
	 * @since 2016年12月14日
	 * @author zr.shuzuo@yikuyi.com
	 */
	public void insertProductDoc(ProductDocument doc){
		Assertions.notNull("doc", doc);
		productDocumentDao.insertProductDoc(doc);
	}
	
}