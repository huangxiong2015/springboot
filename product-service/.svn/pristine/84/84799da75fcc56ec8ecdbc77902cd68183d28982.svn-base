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

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.bson.assertions.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.ictrade.tools.export.ExportFactory;
import com.ictrade.tools.export.ExportProcesser;
import com.yikuyi.document.model.Document;
import com.yikuyi.document.model.Document.DocumentStatus;
import com.yikuyi.document.model.Document.DocumentType;
import com.yikuyi.document.model.DocumentLog;
import com.yikuyi.document.model.DocumentLog.DocumentLogStatus;
import com.yikuyi.document.model.DocumentTitle;
import com.yikuyi.document.vo.DocumentVo;
import com.yikuyi.party.PartyClientBuilder;
import com.yikuyi.party.facility.model.Facility;
import com.yikuyi.party.supplier.SupplierVo;
import com.yikuyi.product.document.dao.DocumentDao;
import com.yikuyi.product.document.dao.DocumentLogDao;
import com.yikuyi.product.document.dao.DocumentTitleDao;
import com.yikuyi.product.material.bll.LeadMaterialParser;
import com.ykyframework.oss.AliyunOSSAccount;
import com.ykyframework.oss.AliyunOSSHelper;

/**
 * Document业务处理类
 * 
 * @author zr.shuzuo@yikuyi.com
 * @version 1.0.0
 */
@Service
@Transactional
public class DocumentManager {

	private static final Logger logger = LoggerFactory.getLogger(DocumentManager.class);

	public static final String ERRORDES = "异常说明";
	private static final String SHEET = "sheetName";
	private static final String SHEET_NAME = "失败记录";
	
	@Autowired
	private DocumentDao documentDao;
	
	@Autowired
	private DocumentTitleDao documentTitleDao;
	
	@Value("${api.party.serverUrlPrefix}")
	private String partyServerUrlPrefix;
	
	@Value("${api.basedata.serverUrlPrefix}")
	private String basedataServerUrlPrefix;
	
	@Autowired
	@Qualifier(value = "aliyun.oss.account")
	private AliyunOSSAccount aliyunOSSAccount;
	
	@Autowired
	private PartyClientBuilder partyClientBuilder;
	
	@Autowired
	private DocumentLogDao documentLogDao;
	
	/**
	 * 更新文档状态
	 * 
	 * @param docId
	 * @param documentStatus
	 * @param comments(用来插入失败时候的备注)
	 * @since 2016年12月14日
	 * @author zr.shuzuo@yikuyi.com
	 */
	public void updateDocStatus(String docId, DocumentStatus documentStatus, String comments) {
		try {
			Document doc = new Document();
			doc.setId(docId);
			doc.setStatusId(documentStatus);
			doc.setComments(comments);
			doc.setLastUpdateDate(new Date());
			documentDao.updateDoc(doc);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 更新文档
	 * 
	 * @param doc
	 * @since 2016年12月14日
	 * @author zr.shuzuo@yikuyi.com
	 */
	public void updateDoc(Document doc) {
		Assertions.notNull("doc", doc);
		documentDao.updateDoc(doc);
	}

	/**
	 * 插入文档
	 * 
	 * @param doc
	 * @since 2016年12月14日
	 * @author zr.shuzuo@yikuyi.com
	 */
	public void insertDoc(Document doc) {
		Assertions.notNull("doc", doc);
		documentDao.insertDoc(doc);
	}

	/**
	 * 查询历史记录
	 * @param ducumentTypeId 文档类型 ，比如：VENDOR_SPU_DOCUMENT spu附件数据,VENDOR_REQ_DOCUMENT 客户需求数据文档,VENDOR_SKU_DOCUMENT 供应商SKU附件数据
	 * @param page第几页
	 * @param pageSize
	 *            每页多少条记录
	 * @author zr.wujiajun
	 */
	public PageInfo<DocumentVo> findByType(DocumentType ducumentTypeId,int page, int pageSize) {
		RowBounds rowBouds = new RowBounds((page - 1) * pageSize, pageSize);		
		PageInfo<DocumentVo> pageInfo = new PageInfo<>(documentDao.findByType(ducumentTypeId.toString(),rowBouds));	
		List<DocumentVo> documentList = pageInfo.getList();
		//如果上传sku，需要查询仓库，供应商信息
		if(ducumentTypeId.equals(DocumentType.VENDOR_SKU_DOCUMENT)){
			if (CollectionUtils.isNotEmpty(documentList)) {
				Set<String> supplierIds = documentList.stream().map(DocumentVo::getPartyId).collect(Collectors.toSet());
				Map<String, SupplierVo> suppliers = partyClientBuilder.supplierClient().getSupplierSimple(supplierIds);
				for (DocumentVo vo : documentList) {
					if(suppliers.containsKey(vo.getPartyId())){
						vo.setPartyName(suppliers.get(vo.getPartyId()).getSupplierName());
						vo.setFacilityName(Optional.ofNullable(suppliers.get(vo.getPartyId()).getFacilityIdMap().get(vo.getFacilityId())).map(Facility::getFacilityName).orElse(StringUtils.EMPTY));
					}
					if(StringUtils.isNotBlank(vo.getLogLocation())){
						vo.setLogLocation(AliyunOSSHelper.getAccessUrl(aliyunOSSAccount, vo.getLogLocation(), false,300));//将阿里云路径解密
					}
				}
			}			
		}else{
			if (CollectionUtils.isNotEmpty(documentList)){
				for (DocumentVo vo : documentList) {
					if(StringUtils.isNotBlank(vo.getLogLocation())){
						vo.setLogLocation(AliyunOSSHelper.getAccessUrl(aliyunOSSAccount, vo.getLogLocation(), false,300));//将阿里云路径解密
					}
				}
			}
		}
		return pageInfo;
	}
	
	public void logHistoryDownload(String id, OutputStream outputStream) {
		Document doc = documentDao.findById(id);
		if(null == doc){
			return;
		}
		Map<String, String> tempMap = new HashMap<>();
		tempMap.put(SHEET, SHEET_NAME);
		ExportProcesser processer = ExportFactory.getProcesser(ExportFactory.TYPE_XLS, outputStream);
		List<DocumentTitle> sheetTitle = documentTitleDao.getById(id);
		for(DocumentTitle docTitle : sheetTitle){
			List<String> titleList = new ArrayList<>();
			titleList.addAll(Arrays.asList(formtaStr(docTitle.getOriginalTitle())));
			titleList.add(ERRORDES);
			
			if(!StringUtils.isBlank(docTitle.getSheetName())){
				tempMap.put(SHEET, docTitle.getSheetName());
			}
			processer.writeLine(tempMap.get("sheetName"), titleList);
			List<DocumentLog> list = documentLogDao.findLogsByStatus(id, docTitle.getSheetIndex(), DocumentLogStatus.FAIL);
			list.stream().map(l -> formatMsg(l, titleList.size())).forEach(l -> processer.writeLine(tempMap.get(SHEET), l));
		}
		processer.output();
		processer.close();
	}
	
	List<String> formatMsg(DocumentLog doc, int size){
		List<String> dataList = new ArrayList<>();
		dataList.addAll(Arrays.asList(formtaStr(doc.getOriginalData())));
		while (dataList.size()<size-1) {
			dataList.add("");
		}
		dataList.add(doc.getComments());
		return dataList;
	}
	
	static String[] formtaStr(String srt){
		if(StringUtils.isBlank(srt)){
			return new String[0];
		}
		return srt.split(LeadMaterialParser.VALUE_SEPARATE);
	}	
	
	/**
	 * 找出当前供应商最次新上传的文件
	 * 保留最新上传的，所以只能删除第二新的文件
	 * @return
	 */
	public DocumentVo findLatestDocumentByVendorAndType(String vendorId,String storeId,DocumentType uploadType, String creator){
		List<DocumentVo> result = documentDao.findLatestAutoUploadDoc(vendorId,storeId, uploadType.name(), creator);
		if(result != null && result.size() == 2 ){
			return result.get(1);
		}
		return null;
	}
}