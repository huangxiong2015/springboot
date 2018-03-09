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
package com.yikuyi.product.basicmaterial.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.framewrok.springboot.web.RequestHelper;
import com.yikuyi.document.model.Document.DocumentType;
import com.yikuyi.material.vo.MaterialVo;
import com.yikuyi.material.vo.MaterialVo.MaterialVoType;
import com.yikuyi.product.basicmaterial.bll.BasicMaterialManager;
import com.yikuyi.product.material.IBasicMaterialResource;
import com.yikuyi.product.material.bll.MaterialManager;
import com.yikuyi.product.template.bll.ProductTemplateManager;
import com.yikuyi.template.model.ProductTemplate;
import com.ykyframework.mqservice.sender.MsgSender;

@RestController
@RequestMapping("v1/basicmaterial")
public class BasicMaterialResource implements IBasicMaterialResource {
	
	private static final Logger logger = LoggerFactory.getLogger(BasicMaterialResource.class);
	
	@Autowired
	private MaterialManager materialManager;
	
	@Autowired
	private BasicMaterialManager basicMaterialManager;

	@Autowired
	private ProductTemplateManager productTemplateManager;

	@Autowired(required = true)
	private MsgSender msgSender;

	@Value("${mqProduceConfig.parseImportFileSub.topicName}")
	private String parseImportFileTopicName;
	
	@Override
	@RequestMapping(value = "/notification/upload", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public void fileUploadNotification(@RequestBody MaterialVo materialVo) {
		String docId = materialManager.fileUpload(materialVo , DocumentType.VENDOR_SPU_DOCUMENT);
		materialVo.setDocId(docId);
		materialVo.setType(MaterialVoType.BASIS_FILE_UPLOAD);
		materialVo.setUserId(RequestHelper.getLoginUserId());
		materialVo.setUserName(RequestHelper.getLoginUser().getUsername());
		//传递url连接给MQ,在回调MaterialResource.parseImportFile,目的实现负载均衡和MQ异常重启机制
		msgSender.sendOrderedMsg(parseImportFileTopicName, materialVo, null, MaterialVoType.BASIS_FILE_UPLOAD.toString());
	}

	@Override
	@RequestMapping(value = "/notification/parse", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public void parseImportFile(@RequestBody MaterialVo materialVo) {
		/*documentManager.updateDocStatus(materialVo.getDocId(), DocumentStatus.DOC_IN_PROCESS, null);
		try {
			basicMaterialManager.parseImportFile(materialVo);
		} catch (Exception e) {
			documentManager.updateDocStatus(materialVo.getDocId(), DocumentStatus.DOC_PRO_FAILED,
					DocumentLog.getSizeLengthMsg(e));
		}*/
		logger.info("从前台调用的物料解析方法"+materialVo.getDocId());
		basicMaterialManager.basicMaterialParseImportFile(materialVo);
	}

	@Override
	@RequestMapping(value = "/template/{vendorId}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ProductTemplate findTemplatebyId(@PathVariable(value = "vendorId") String vendorId,@RequestParam(value="sourceId", required=false)String sourceId) {
		return productTemplateManager.geTemplate(vendorId,sourceId);
	}
}