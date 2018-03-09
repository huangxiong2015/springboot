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

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.bson.Document;
import org.bson.assertions.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yikuyi.document.model.Document.DocumentStatus;
import com.yikuyi.document.model.DocumentLog;
import com.yikuyi.document.model.DocumentLog.DocumentLogStatus;
import com.yikuyi.product.document.dao.DocumentLogDao;
import com.yikuyi.product.vo.ProductVo;
import com.yikuyi.product.vo.RawData;

/**
 * DocumentLog业务处理类
 * 
 * @author zr.shuzuo@yikuyi.com
 * @version 1.0.0
 */
@Service
@Transactional
public class DocumentLogManager {

	private static final Logger logger = LoggerFactory.getLogger(DocumentLogManager.class);
	
	@Autowired
	private DocumentLogDao documentLogDao;
	
	/**
	 * sql工厂
	 */
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	/**
	 * 插入文档LOG
	 * @param doc
	 * @since 2016年12月14日
	 * @author zr.shuzuo@yikuyi.com
	 */
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public void insertDocs(List<DocumentLog> docLogs){
		Assertions.notNull("docLogs", docLogs);
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
			session.insert(DocumentLogDao.class.getName()+".insertDocLogs", docLogs);
			session.commit(true);
			session.clearCache();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		} finally{
			if(session!=null){
				session.close();
			}
		}
	}
	
	/**
	 * 整批次失败,集体更新状态
	 * @param list
	 * @param errorMsg
	 * @since 2016年12月15日
	 * @author zr.shuzuo@yikuyi.com
	 */
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public void updateDocLogsStatusByVo(List<ProductVo> list , String errorMsg){
		Assertions.notNull("list", list);
		try {
			documentLogDao.updateFailDocLogsByVoMsg(list.get(0).getProcessId() , list, DocumentLog.getSizeLengthMsg(errorMsg));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 整批次失败,集体更新状态
	 * @param list
	 * @param errorMsg
	 * @since 2016年12月15日
	 * @author zr.shuzuo@yikuyi.com
	 */
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public void updateDocLogsStatusByRaw(List<RawData> list , String errorMsg){
		Assertions.notNull("list", list);
		try {
			documentLogDao.updateFailDocLogsByRawMsg(list.get(0).getProcessId() , list, DocumentLog.getSizeLengthMsg(errorMsg));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 整批次失败,根据ID和行号修改状态和备注
	 * @param list
	 * @param errorMsg
	 * @since 2016年12月15日
	 * @author zr.shuzuo@yikuyi.com
	 */
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public void updateDocLogsStatusByRaw(List<RawData> list){
		Assertions.notNull("list", list);
		try {
			documentLogDao.updateDocLogsStatusByRaw(list);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 整批次失败,根据ID和行号修改状态和备注
	 * @param list
	 * @param errorMsg
	 * @since 2016年12月15日
	 * @author zr.shuzuo@yikuyi.com
	 */
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public void updateDocLogsStatusByRawAndSheet(List<RawData> list){
		Assertions.notNull("list", list);
		try {
			documentLogDao.updateDocLogsStatusByRawAndSheet(list);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 整批次失败,根据ID和行号修改状态和备注
	 * @param list
	 * @param errorMsg
	 * @since 2016年12月15日
	 * @author zr.shuzuo@yikuyi.com
	 */
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public void updateDocLogsStatusByDoc(List<Document> list){
		Assertions.notNull("list", list);
		try {
			documentLogDao.updateDocLogsStatusByDoc(list);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 循环删除5天之外的历史记录信息（每次删除50W）
	 */
	public void deleteHistoryLog(){
		Integer i = documentLogDao.deleteHistoryLog();
		while (i == 500000) {
			i = documentLogDao.deleteHistoryLog();
		}
	}
	
	/**
	 * 根据文档ID查询,文件上传,文件解析,数据保存,数据清理,数据同步的结果()
	 * @param docId
	 * @return
	 * @since 2016年12月14日
	 * @author zr.shuzuo@yikuyi.com
	 */
	public DocumentStatus findDocStatusByDocId(String docId){
		Assertions.notNull("docId", docId);
		List<Map<String, Integer>> listMap = documentLogDao.findCountGroupStatus(docId);
		if( listMap == null || listMap.isEmpty()){
			return DocumentStatus.DOC_PRO_FAILED;
		}
		if(listMap.size()==1){
			return listMap.get(0).get("STATUS").equals(DocumentLogStatus.SUCCESS.getValue()) ? DocumentStatus.DOC_PRO_SUCCESS : DocumentStatus.DOC_PRO_FAILED;
		}
		if(listMap.size()==2){
			return DocumentStatus.DOC_PRO_PART_SUCCESS;
		}
		return DocumentStatus.DOC_PRO_FAILED;
	}
}