package com.yikuyi.product.document.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.yikuyi.document.model.Document;
import com.yikuyi.document.vo.DocumentVo;

@Mapper
public interface DocumentDao {

	public void insertDoc(Document doc);

	public Document findById(@Param(value = "id") String id);

	public void updateDoc(Document doc);
	/**
	 * 上传历史记录
	 * @param partyId
	 * @param rowBouds
	 * @return
	 * @since 2017年1月18日
	 * @author zr.wujiajun@yikuyi.com
	 */
	public List<DocumentVo> findByType(String type,RowBounds rowBouds);
	
	public List<DocumentVo> findLatestAutoUploadDoc(@Param("vendorId") String vendorId,@Param("storeId") String storeId,@Param("typeId") String typeId, @Param("creator") String creator);
}