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
package com.yikuyi.product.material.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.yikuyi.document.model.Document.DocumentType;
import com.yikuyi.document.vo.DocumentVo;
import com.yikuyi.material.vo.MaterialVo;
import com.yikuyi.material.vo.MaterialVo.MaterialVoType;
import com.yikuyi.product.document.bll.DocumentManager;
import com.yikuyi.product.document.bll.SyncElasticsearchProductManager;
import com.yikuyi.product.goods.manager.ProductManager;
import com.yikuyi.product.material.IMaterialResource;
import com.yikuyi.product.material.bll.MaterialManager;
import com.ykyframework.exception.SystemException;
import com.ykyframework.mqservice.sender.MsgSender;

@RestController
@RequestMapping("v1/imports")
public class MaterialResource implements IMaterialResource {

	@Autowired
	private MaterialManager materialManager;

	@Autowired
	private DocumentManager documentManager;

	@Autowired(required = true)
	private MsgSender msgSender;

	@Autowired
	private ProductManager productManager;
	
	@Autowired
	private SyncElasticsearchProductManager syncElasticsearchProductManager;

	@Value("${mqProduceConfig.parseImportFileSub.topicName}")
	private String parseImportFileTopicName;
	
	/**
	 * ftp下载中电港vendorId
	 */
	@Value("${downloadFtp.vendorId.cecport}")
	private String cecport;

	@Override
	@RequestMapping(value = "/notification/material", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public void fileUploadNotification(@RequestBody MaterialVo materialVo) {
		materialManager.fileUpload(materialVo, DocumentType.VENDOR_SKU_DOCUMENT);
		// 如果上传的future的模版,仓库不填写,从EXCEL获取  ,增加中电港数据同逻辑处理,TODO 后续修改为配置
		if (MaterialVo.FUTURE_NAME.equals(materialVo.getVendorId().trim()) || cecport.equals(materialVo.getVendorId().trim())) {
			materialVo.setRegionId(null);
		} else if (StringUtils.isBlank(materialVo.getRegionId())) {// 如果其他供应商的仓库ID不填,默认100
			materialVo.setRegionId("100");
		}
		// 传递url连接给MQ,在回调MaterialResource.parseImportFile,目的实现负载均衡和MQ异常重启机制(同一个供应商按顺序)
		msgSender.sendOrderedMsg(parseImportFileTopicName, materialVo, null, materialVo.getVendorId());
	}
	
	@Override
	@RequestMapping(value = "/notification/job", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public void futureFileImportJob(@RequestParam(value = "vendorId", required = true) String vendorId,@RequestParam(value = "regionId", required = false) String regionId,@RequestParam(value = "ftpFileName", required = false) String ftpFileName) {
		MaterialVo materialVo = new MaterialVo();
		materialVo.setVendorId(vendorId);
		materialVo.setRegionId(regionId);
		materialVo.setType(MaterialVoType.FILE_UPLOAD_JOB);
		materialVo.setFtpFileName(ftpFileName);
		
		materialManager.fileUpload(materialVo, DocumentType.VENDOR_SKU_DOCUMENT);
		
		msgSender.sendOrderedMsg(parseImportFileTopicName, materialVo, null, materialVo.getVendorId());
	}

	@Override
	@RequestMapping(value = "/notification/syncupdate", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public void syncElasticsearchProductUpdate(@RequestBody MaterialVo materialVo) {
		syncElasticsearchProductManager.syncElasticsearchProductUpdate(materialVo);
	}

	@Override
	@RequestMapping(value = "/notification/syncall", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public List<String> syncElasticsearchProductAll(@RequestParam(value = "vendorId", required = false) String vendorId) {
		return materialManager.syncElasticsearchProduct(vendorId);
	}

	@Override
	@RequestMapping(value = "/notification/cancel", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public void cancelImport(@RequestBody String docId) {
		materialManager.cancelImport(docId);
	}

	@Override
	@RequestMapping(value = "/materialDetection", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public void materialDetection(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "fileUrl", required = true) String fileUrl) {
		try {
			response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.addHeader("Content-Disposition",
					String.format("attachment; filename=\"%s\"", "Errorlogging.xls"));
			response.addHeader("Pragma", "no-cache");
			response.addHeader("Expires", "0");
			materialManager.materialDetectionNew(fileUrl, response.getOutputStream());
		} catch (Exception e) {
			throw new SystemException(e.getMessage(), e);
		}
	}

	@Override
	@RequestMapping(value = "/history/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public void logHistoryDownload(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "id") String id) {
		try {
			response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.addHeader("Content-Disposition",
					String.format("attachment; filename=\"%s\"", "Errorlogging.xls"));
			response.addHeader("Pragma", "no-cache");
			response.addHeader("Expires", "0");
			documentManager.logHistoryDownload(id, response.getOutputStream());
		} catch (Exception e) {
			throw new SystemException(e.getMessage(), e);
		}
	}
	
	/**
	 * 查询上传记录
	 * @author zr.wujiajun
	 * @param docTypeId 上传类型：VENDOR_SKU_DOCUMENT，VENDOR_SPU_DOCUMENT
	 */
	@Override
	@RequestMapping(value = "/history", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public PageInfo<DocumentVo> logHistory(
			@RequestParam(value = "docTypeId", required = true,defaultValue="VENDOR_SKU_DOCUMENT") DocumentType docTypeId,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize) {
		return documentManager.findByType(docTypeId,page, pageSize);
	}
	
	/* 
	 * 1. 根据供应商或者仓库，找出最近最新自动上传的文件
	 * 2. 如果文件的状态是 部分成功或者全部成功则进行清理工作，如果没有则放弃本次清理。
	 * 3. 发送异步消息进行数据清理
	 */
	@Override
	@RequestMapping(value = "/notification/uploadcomplete", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public void clearOutOfDateProducts4AutoUpload(
			@RequestParam(value = "vendorId", required = true) String vendorId,
			@RequestParam(value = "storeId", required = false) String storeId) {
		//TODO Auto-generated method stub	
		DocumentVo documentVo = documentManager.findLatestDocumentByVendorAndType(vendorId,storeId,DocumentType.VENDOR_SKU_DOCUMENT,"Admin");
		if(documentVo != null){
			productManager.deleteOtherExceptProcessId( vendorId, storeId,documentVo.getId());
		}
	}
}