/*
 * Created: 2017年3月23日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.vendor.vo;

import java.math.BigDecimal;

import com.yikuyi.party.vendor.vo.Vendor.SupplierStatus;
import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel("PartySupplier")
public class PartySupplier extends AbstractEntity {
	
	/**
	 * 供应商属性表
	 */
	private static final long serialVersionUID = 1L;
	
	
	@ApiModelProperty(value = "主键ID")
	private String partyId;
	
	@ApiModelProperty(value = "地区")
	private String region;
	
	@ApiModelProperty(value = "分类")
	private String category;
		
	@ApiModelProperty(value = "总部（总公司名称）")
	private String generalHeadquarters;
	
	@ApiModelProperty(value = "类型")
	private String genre;
	
	@ApiModelProperty(value = "上市状态(Y/N)")
	private String listed;
	
	@ApiModelProperty(value = "上市的股票代码")
	private String stockCode;
	
	@ApiModelProperty(value = "是否核心(Y/N)")
	private String isCore;
	
	@ApiModelProperty(value = "是否启用供应商MOV策略(Y/N)")
	private String vendorMovStatus;
	
	@ApiModelProperty(value = "是否启用供应商SKU的MOV策略(Y/N)")
	private String skuMovStatus;
	
	
	@ApiModelProperty(value = "订单是否审核(Y/N)")
	private String orderVerify;
	
	@ApiModelProperty(value = "是否显示名称(Y/N)")
	private String isShowName;

	@ApiModelProperty(value = "是否显示销售价格(Y/N)")
	private String isSupPrice;

	@ApiModelProperty(value = "支持币种(RMB,USD,XXX)")
	private String supportCurrency;
	
	@ApiModelProperty(value = "装运条款")
	private String description;
	
	@ApiModelProperty(value = "备注说明")
	private String comments;
	
	@ApiModelProperty(value = "最小订单金额人民币")
	private BigDecimal minOrderPriceCNY;
	
	@ApiModelProperty(value = "最小订单金额美元")
	private BigDecimal minOrderPriceUSD;
	
	@ApiModelProperty(value = "状态(ENABLE/UNABLE/DELETE)")
	private SupplierStatus supplierStatus;
	
	@ApiModelProperty(value = "商品失效时长天数")
	private String productInvalidDays;
	
	@ApiModelProperty(value = "是否系统自动集成库存（Y/N）")
	private String isAutoIntegrateQty;

	public String getProductInvalidDays() {
		return productInvalidDays;
	}

	public void setProductInvalidDays(String productInvalidDays) {
		this.productInvalidDays = productInvalidDays;
	}

	public String getIsAutoIntegrateQty() {
		return isAutoIntegrateQty;
	}

	public void setIsAutoIntegrateQty(String isAutoIntegrateQty) {
		this.isAutoIntegrateQty = isAutoIntegrateQty;
	}

	public String getVendorMovStatus() {
		return vendorMovStatus;
	}

	public void setVendorMovStatus(String vendorMovStatus) {
		this.vendorMovStatus = vendorMovStatus;
	}

	public String getSkuMovStatus() {
		return skuMovStatus;
	}

	public void setSkuMovStatus(String skuMovStatus) {
		this.skuMovStatus = skuMovStatus;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getGeneralHeadquarters() {
		return generalHeadquarters;
	}

	public void setGeneralHeadquarters(String generalHeadquarters) {
		this.generalHeadquarters = generalHeadquarters;
	}

	

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getListed() {
		return listed;
	}

	public void setListed(String listed) {
		this.listed = listed;
	}

	public String getStockCode() {
		return stockCode;
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	public String getIsCore() {
		return isCore;
	}

	public void setIsCore(String isCore) {
		this.isCore = isCore;
	}

	public String getOrderVerify() {
		return orderVerify;
	}

	public void setOrderVerify(String orderVerify) {
		this.orderVerify = orderVerify;
	}

	public String getIsShowName() {
		return isShowName;
	}

	public void setIsShowName(String isShowName) {
		this.isShowName = isShowName;
	}

	public String getIsSupPrice() {
		return isSupPrice;
	}

	public void setIsSupPrice(String isSupPrice) {
		this.isSupPrice = isSupPrice;
	}

	


	public String getSupportCurrency() {
		return supportCurrency;
	}

	public void setSupportCurrency(String supportCurrency) {
		this.supportCurrency = supportCurrency;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}


	public BigDecimal getMinOrderPriceCNY() {
		return minOrderPriceCNY;
	}

	public void setMinOrderPriceCNY(BigDecimal minOrderPriceCNY) {
		this.minOrderPriceCNY = minOrderPriceCNY;
	}

	public BigDecimal getMinOrderPriceUSD() {
		return minOrderPriceUSD;
	}

	public void setMinOrderPriceUSD(BigDecimal minOrderPriceUSD) {
		this.minOrderPriceUSD = minOrderPriceUSD;
	}

	public SupplierStatus getSupplierStatus() {
		return supplierStatus;
	}

	public void setSupplierStatus(SupplierStatus supplierStatus) {
		this.supplierStatus = supplierStatus;
	}


	
}