/*
 * Created: 2017年8月10日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.supplier;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yikuyi.party.facility.model.Facility;
import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModelProperty;

/**
 * 供应商基本信息vo
 * 
 * @author jik.shu@yikuyi.com
 * @version 1.0.0
 */
public class SupplierVo extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "供应商ID")
	private String supplierId;

	@ApiModelProperty(value = "供应商简称")
	private String supplierName;

	@ApiModelProperty(value = "公司全称")
	private String supplierFullName;

	@ApiModelProperty(value = "供应商编码")
	private String supplierCode;

	@ApiModelProperty(value = "供应商显示名称")
	private String displayName;

	@ApiModelProperty(value = "是否显示名称")
	private boolean showName;

	@ApiModelProperty(value = "供应商Mov状态")
	private String vendorMovStatus;

	@ApiModelProperty(value = "供应商下商品Mov状态")
	private String skuMovStatus;

	@ApiModelProperty(value = "是否配置供应商详情页面")
	private boolean vendorDetail;

	@ApiModelProperty(value = "全部仓库")
	private Set<Facility> facilitys;

	@ApiModelProperty(value = "是否系统自动集成库存（Y/N）")
	private String isAutoIntegrateQty;

	@ApiModelProperty(value = "供应商交期准确率,默认")
	private String leadtimeAccuracyrate;

	@ApiModelProperty(value = "仓库ID加仓库对象集合")
	private Map<String, Facility> idFacilitys;

	@ApiModelProperty(value = "仓库名称加仓库对象集合")
	private Map<String, Facility> nameFacilitys;

	public String getLeadtimeAccuracyrate() {
		return this.leadtimeAccuracyrate;
	}

	public void setLeadtimeAccuracyrate(String leadtimeAccuracyrate) {
		this.leadtimeAccuracyrate = leadtimeAccuracyrate;
	}

	public boolean isVendorDetail() {
		return vendorDetail;
	}

	public void setVendorDetail(boolean vendorDetail) {
		this.vendorDetail = vendorDetail;
	}

	public String getDisplayName() {
		return this.showName ? this.supplierName : this.supplierCode;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
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

	public String getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getSupplierFullName() {
		return supplierFullName;
	}

	public void setSupplierFullName(String supplierFullName) {
		this.supplierFullName = supplierFullName;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public boolean isShowName() {
		return showName;
	}

	public void setShowName(boolean showName) {
		this.showName = showName;
	}

	public Set<Facility> getFacilitys() {
		return facilitys;
	}

	@JsonIgnore
	public Map<String, Facility> getFacilityNameMap() {
		if (null == this.nameFacilitys) {
			this.nameFacilitys = CollectionUtils.isEmpty(facilitys) ? Collections.emptyMap() : facilitys.stream().collect(Collectors.toMap(Facility::getFacilityName, Function.identity() ,(key1 , key2) -> key2 , HashMap::new));
		}
		return this.nameFacilitys;
	}

	@JsonIgnore
	public Map<String, Facility> getFacilityIdMap() {
		if (null == this.idFacilitys) {
			this.idFacilitys = CollectionUtils.isEmpty(facilitys) ? Collections.emptyMap() : facilitys.stream().collect(Collectors.toMap(Facility::getId, Function.identity()));
		}
		return this.idFacilitys;
	}

	public void setFacilitys(Set<Facility> facilitys) {
		this.facilitys = facilitys;
	}

	public String getIsAutoIntegrateQty() {
		return isAutoIntegrateQty;
	}

	public void setIsAutoIntegrateQty(String isAutoIntegrateQty) {
		this.isAutoIntegrateQty = isAutoIntegrateQty;
	}

}