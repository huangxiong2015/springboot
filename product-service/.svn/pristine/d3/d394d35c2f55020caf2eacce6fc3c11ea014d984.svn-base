package com.yikuyi.product.document.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yikuyi.product.document.IDocumentResource;
import com.yikuyi.product.document.bll.DocumentLogManager;

@RestController
@RequestMapping("v1/document")
public class DocumentResource implements IDocumentResource {

	@Autowired
	private DocumentLogManager documentLogManager;
	
	
	/**
	 * 按固定规则删除，5天之外的所有数据
	 */
	@Override
	@RequestMapping(value = "/history", method = RequestMethod.DELETE, produces = "application/json; charset=utf-8")
	public void deleteHistoryLog() {
		documentLogManager.deleteHistoryLog();
	}
	
}