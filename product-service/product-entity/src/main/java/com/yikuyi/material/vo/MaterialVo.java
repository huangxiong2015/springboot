/*
 * Created: 2017年1月17日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.material.vo;

import java.io.Serializable;
import java.util.List;

import org.bson.Document;

import com.yikuyi.product.vo.ProductVo;

import io.swagger.annotations.ApiModelProperty;

public class MaterialVo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String FUTURE_NAME = "future";
	
	/**
	 * WPG的FTP上的文件名
	 */
	public static final String WPG_FILE_NAME = "wpg_stock.csv";
	
	public enum MaterialVoType{
		/**
		 * 文件上传
		 */
		BASIS_FILE_UPLOAD,
		/**
		 * 文件上传
		 */
		FILE_UPLOAD,
		/**
		 * 更新数据
		 */
		UPDATE_DATA,
		/**
		 * 删除数据
		 */
		DELETE_DATA,
		/**
		 * 标准库型号同步
		 */
		PRODUCT_STAND_NO,
		/**
		 * 定时任务的文件上传
		 */
		FILE_UPLOAD_JOB
	}
	
	@ApiModelProperty(value="阿里云文件路径")
	private String fileUrl;
	
	@ApiModelProperty(value="文件原始名称")
	private String oriFileName;
	
	@ApiModelProperty(value="供应商ID")
	private String vendorId;
	
	@ApiModelProperty(value="供应商名称")
	private String vendorName;
	
	@ApiModelProperty(value="仓库ID")
	private String regionId;
	
	/**
	 * 文档ID=批次号
	 */
	private String docId;
	
	/**
	 * 操作类型
	 */
	private MaterialVoType type;
	
	/**
	 * 要操作的数据(最后转换List<ProductVo))
	 */
	private List<ProductVo> msg;
	
	/**
	 * 要操作的数据(String必须是一个完整的json字符串)
	 */
	private List<String> dataList;
	
	/**
	 * 发送给search的数据接口
	 */
	private List<Document> voList;
	
	private int size;
	
	/**
	 * FTP自动下载指定文件名
	 */
	private String ftpFileName;
	
	private String userId;
	
	private String userName;
	/**
	 * 商品失效时长天数
	 */
	private String productInvalidays;
	
	
	public String getProductInvalidays() {
		return productInvalidays;
	}

	public void setProductInvalidays(String productInvalidays) {
		this.productInvalidays = productInvalidays;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public List<ProductVo> getMsg() {
		return msg;
	}

	public void setMsg(List<ProductVo> msg) {
		this.msg = msg;
	}

	public MaterialVoType getType() {
		return type;
	}

	public void setType(MaterialVoType type) {
		this.type = type;
	}

	public List<String> getDataList() {
		return dataList;
	}

	public void setDataList(List<String> dataList) {
		this.dataList = dataList;
	}

	public List<Document> getVoList() {
		return voList;
	}

	public void setVoList(List<Document> voList) {
		this.voList = voList;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getOriFileName() {
		return oriFileName;
	}

	public void setOriFileName(String oriFileName) {
		this.oriFileName = oriFileName;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	
	public String getFtpFileName() {
		return ftpFileName;
	}

	public void setFtpFileName(String ftpFileName) {
		this.ftpFileName = ftpFileName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}