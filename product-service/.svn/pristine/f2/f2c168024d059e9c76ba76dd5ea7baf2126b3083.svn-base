package com.yikuyi.product.document.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.bson.Document;

import com.yikuyi.document.model.DocumentLog;
import com.yikuyi.document.model.DocumentLog.DocumentLogStatus;
import com.yikuyi.product.vo.ProductVo;
import com.yikuyi.product.vo.RawData;

@Mapper
public interface DocumentLogDao {

	public void insertDocLogs(List<DocumentLog> docLogs);

	public void updateFailDocLogsByVoMsg(@Param("docId") String docId , @Param("list") List<ProductVo> list ,@Param("comments") String comments);
	
	public void updateFailDocLogsByRawMsg(@Param("docId") String docId , @Param("list") List<RawData> list ,@Param("comments") String comments);
	
	public void updateDocLogsStatusByRaw(@Param("list") List<RawData> list);
	
	public void updateDocLogsStatusByRawAndSheet(@Param("list") List<RawData> list);
	
	public void updateDocLogsStatusByDoc(@Param("list") List<Document> list);
	
	public List<Map<String, Integer>> findCountGroupStatus(@Param("docId") String docId);
	
	public List<DocumentLog> findLogsByStatus(@Param("docId") String docId, @Param("sheetIndex") Integer sheetIndex, @Param("status") DocumentLogStatus status);

	@Delete("DELETE FROM `DOCUMENT_LOG` WHERE `CREATED_DATE` < date_add( NOW(),INTERVAL -2 DAY ) LIMIT 500000")
	public Integer deleteHistoryLog();
	
	public void deleteHistoryLogById(@Param("docId") String docId);
	
	public List<DocumentLog> findLogsByPage(@Param("docId") String docId, @Param("sheetIndex") Integer sheetIndex, @Param("status") DocumentLogStatus status,RowBounds rowBouds);
}