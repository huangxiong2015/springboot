/*
 * Created: 2017年11月2日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.log.vo;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yikuyi.category.model.ProductCategory;
import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModelProperty;

/**
 * 产品映射失败日志
 * @author injor.huang@yikuyi.com
 * @version 1.0.0
 */
@SuppressWarnings("serial")
@Document(collection="product_mapping_error")
public class ProductMappingError  extends AbstractEntity{
	
	@ApiModelProperty(value="ID")
	@Id
	@JsonProperty("_id")
	private String id;
	
	@ApiModelProperty(value="供应商ID")
	private String vendorId;
	
	@ApiModelProperty(value="供应商名")
	private String vendorName;
	
	@ApiModelProperty(value="数据类型  BRAND-品牌  CATEGORY-分类")
	private String dataType;
	
	@ApiModelProperty(value="状态： 0-标准化  1-未标准化")
	private Integer status;
	
	@ApiModelProperty(value="小类名称")
	private String cate1Name;
	@ApiModelProperty(value="次小类名称")
	private String cate2Name;
	
	@ApiModelProperty(value="制造商名")
	private String brandName;
	
	@ApiModelProperty(value="描述")
	private String description;
	
	@ApiModelProperty(value="创建时间戳")
	private String createdTimeMillis;
	
	@ApiModelProperty(value="更新时间戳")
	private String updatedTimeMillis;
	
	@ApiModelProperty(value="操作人")
	private String oprName;
	/**
	 * 分类数据
	 */
	private List<ProductCategory> productCategories;
	
	public enum DataType{
		/**
		 * 品牌
		 */
		BRAND,
		/**
		 * 分类
		 */
		CATEGORY;
		
	}
	
	public enum Status{
		/**
		 * 标准化
		 */
		STANDARD(0),
		/**
		 * 未标准化
		 */
		UNSTANDARD(1);
		private Integer value;
		private Status( Integer value) { 
	        this.value = value;  
	    }  
		public Integer getValue() {
			return value;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCate1Name() {
		return cate1Name;
	}

	public void setCate1Name(String cate1Name) {
		this.cate1Name = cate1Name;
	}

	public String getCate2Name() {
		return cate2Name;
	}

	public void setCate2Name(String cate2Name) {
		this.cate2Name = cate2Name;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreatedTimeMillis() {
		return createdTimeMillis;
	}

	public void setCreatedTimeMillis(String createdTimeMillis) {
		this.createdTimeMillis = createdTimeMillis;
	}

	public String getUpdatedTimeMillis() {
		return updatedTimeMillis;
	}

	public void setUpdatedTimeMillis(String updatedTimeMillis) {
		this.updatedTimeMillis = updatedTimeMillis;
	}

	public String getOprName() {
		return oprName;
	}

	public void setOprName(String oprName) {
		this.oprName = oprName;
	}

	public List<ProductCategory> getProductCategories() {
		return productCategories;
	}

	public void setProductCategories(List<ProductCategory> productCategories) {
		this.productCategories = productCategories;
	}
}
