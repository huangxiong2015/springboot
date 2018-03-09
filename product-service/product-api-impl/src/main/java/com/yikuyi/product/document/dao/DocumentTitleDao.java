package com.yikuyi.product.document.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.yikuyi.document.model.DocumentTitle;

@Mapper
public interface DocumentTitleDao {
	
	public void insertDocTitles(List<DocumentTitle> titles);
	
	public void insertDocTitle(DocumentTitle titles);
	
	public List<DocumentTitle> getById(String docId);
	
}