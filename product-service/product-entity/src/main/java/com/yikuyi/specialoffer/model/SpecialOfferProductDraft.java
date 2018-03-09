package com.yikuyi.specialoffer.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yikuyi.specialoffer.model.SpecialOfferProduct.Status;

import io.swagger.annotations.ApiModelProperty;
/**
 * 供应商信息
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@Document(collection = "special_offer_product_draft")
public class SpecialOfferProductDraft implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3348126914131814018L;

	@Id
	@ApiModelProperty(value = "主键id")
	@JsonProperty("id")
	private String id;
	
	@ApiModelProperty(value = "供应商(即分销商)Id")
	private String vendorId;
	
	@ApiModelProperty(value = "供应商名称(即分销商名称)")
	private String vendorName;
	
	@ApiModelProperty(value = "规则Id")
	private String ruleId;
	
	@ApiModelProperty(value = "型号")
	private String mpn;
	
	@ApiModelProperty(value = "型号链接")
	private String mpnUrl;
	
	@ApiModelProperty(value = "制造商id")
	private String brandId;
	
	@ApiModelProperty(value = "制造商")
	private String brandName;
	
	@ApiModelProperty(value = "仓库id")
	private String sourceId;
	
	@ApiModelProperty(value = "仓库名称")
	private String sourceName;
	
	@ApiModelProperty(value = "备注")
	private String remark;
	
	@ApiModelProperty(value = "商品id(取自mpnUrl)")
	private String productId;
	
	@ApiModelProperty(value = "上传批次号")
	private String documentId;
	
	@ApiModelProperty(value = "状态")
	private Status status;
	
	@ApiModelProperty(value = "创建者")
	private String creator;
	
	@ApiModelProperty(value = "创建者名称")
	private String creatorName;
	
	@ApiModelProperty(value="创建时间戳")
	private String createdTimeMillis;
	
	@ApiModelProperty(value="更新时间戳")
	private String updatedTimeMillis;
	
	
	@ApiModelProperty(value = "更新者")
	private String lastUpdateUser;
	
	@ApiModelProperty(value = "更新者名称")
	private String lastUpdateUserName;

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

	public String getMpn() {
		return mpn;
	}

	public void setMpn(String mpn) {
		this.mpn = mpn;
	}
	
	public String getMpnUrl() {
		return mpnUrl;
	}

	public void setMpnUrl(String mpnUrl) {
		this.mpnUrl = mpnUrl;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
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

	public String getLastUpdateUser() {
		return lastUpdateUser;
	}

	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}

	public String getLastUpdateUserName() {
		return lastUpdateUserName;
	}

	public void setLastUpdateUserName(String lastUpdateUserName) {
		this.lastUpdateUserName = lastUpdateUserName;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	

	
}