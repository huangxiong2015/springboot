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
package com.yikuyi.product.essync.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.framewrok.springboot.web.RequestHelper;
import com.yikuyi.document.model.Document;
import com.yikuyi.document.model.Document.DocumentStatus;
import com.yikuyi.document.model.Document.DocumentType;
import com.yikuyi.material.vo.MaterialVo;
import com.yikuyi.material.vo.MaterialVo.MaterialVoType;
import com.yikuyi.product.document.bll.DocumentManager;
import com.yikuyi.product.essync.IEssyncResource;
import com.ykyframework.model.IdGen;
import com.ykyframework.mqservice.sender.MsgSender;

@RestController
@RequestMapping("v1/essync")
public class EssyncResource implements IEssyncResource {
	
	@Autowired(required = true)
	private MsgSender msgSender;

	@Value("${mqProduceConfig.syncElasticsearchProduct.topicName}")
	private String syncElasticsearchProductTopicName;
	
	@Autowired
	private DocumentManager documentManager;
	
	/**
	 *从mongodb数据库同步数据到elasticsearch,目前这个接口只有 联想词的同步，后续可以把product的同步也迁移过来 
	 */
	@Override
	@RequestMapping(value = "/{type}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public void esSync(@PathVariable("type") String type) {
		String docId  = String.valueOf(IdGen.getInstance().nextId());
		
		Document document = new Document();
		document.setId(docId);
		document.setTypeId(DocumentType.PRODUCT_STAND_NO);
		document.setStatusId(DocumentStatus.DOC_CREATED);
		document.setCreator(null ==  RequestHelper.getLoginUser() ? "Admin" : RequestHelper.getLoginUser().getUsername());
		documentManager.insertDoc(document);
		
		MaterialVo materialVo = new MaterialVo();
		materialVo.setDocId(docId);
		materialVo.setType(MaterialVoType.PRODUCT_STAND_NO);
		msgSender.sendOrderedMsg(syncElasticsearchProductTopicName, materialVo, null, materialVo.getType().toString());
	}
}