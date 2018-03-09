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
package com.yikuyi.party.model;

import java.util.List;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 企业属性表
 * 
 * @author zr.helinmei@yikuyi.com
 *
 */
@ApiModel("PartyAttribute")
public class PartyAttributes extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8562230060084519811L;
	
	@ApiModelProperty(value = "所属行业")
	private PartyAttribute  industryCategory;

	@ApiModelProperty(value = "公司官网")
	private PartyAttribute  webSite;
	
	@ApiModelProperty(value = "简介")
	private PartyAttribute  loa;
	

	@ApiModelProperty(value = "公司类型")
	private PartyAttribute corporationCategory;
	
	@ApiModelProperty(value = "公司资质")
	private List<PartyAttribute> corporationQualData;
	
	@ApiModelProperty(value = "是否显示名称 Y(显示),N(不显示)")
	private PartyAttribute isShowName;
	
	@ApiModelProperty(value = "是否支持价格策略 Y(显示),N(不显示)")
	private PartyAttribute isSupPrice;
	
	@ApiModelProperty(value = "邓氏编码")
	private PartyAttribute dCode;
	
	@ApiModelProperty(value = "资料完善度COMPLETE(完善)/NOT-COMPLETE(不完善)")
	private PartyAttribute infoLevel;
	
	@ApiModelProperty(value = "地址币种 CNY(国内),USD(海外)")
	private PartyAttribute usedCurruncy;
	
	@ApiModelProperty(value = "注册地")
	private PartyAttribute  registeAddr;
	
	@ApiModelProperty(value = "资质类型")
	private PartyAttribute  busiLisType;
	
	@ApiModelProperty(value = "公司资质营业执照维护信息")
	private List<PartyAttribute> corporationBusLicDataInfo;
	
	@ApiModelProperty(value = "公司资质税务登记维护信息")
	private List<PartyAttribute> corporationFaxDataInfo;
	
	@ApiModelProperty(value = "公司资质组织机构维护信息")
	private List<PartyAttribute> corporationOrgDataInfo;
	@ApiModelProperty(value = "其它属性拓展信息")
	private PartyAttribute otherAttrs;
	@ApiModelProperty(value = "供应商是否有分销商详情")
	private String isVendorDetail;
	
	@ApiModelProperty(value = "资质信息集合")
	private PartyAttribute certificateAttrs;
	
	@ApiModelProperty(value = "是否支持供应商mov校验")
	private PartyAttribute vendorMovStatus;
	
	@ApiModelProperty(value = "是否支持供应商sku的mov校验")
	private PartyAttribute skuMovStatus;
	
	@ApiModelProperty(value = "key集合")
	private List<String> keyList;
	
	public List<String> getKeyList() {
		return keyList;
	}

	public void setKeyList(List<String> keyList) {
		this.keyList = keyList;
	}

	public PartyAttribute getLoa() {
		return loa;
	}

	public void setLoa(PartyAttribute loa) {
		this.loa = loa;
	}
	
	
	public PartyAttribute getCertificateAttrs() {
		return certificateAttrs;
	}

	public void setCertificateAttrs(PartyAttribute certificateAttrs) {
		this.certificateAttrs = certificateAttrs;
	}

	public List<PartyAttribute> getCorporationBusLicDataInfo() {
		return corporationBusLicDataInfo;
	}

	public void setCorporationBusLicDataInfo(List<PartyAttribute> corporationBusLicDataInfo) {
		this.corporationBusLicDataInfo = corporationBusLicDataInfo;
	}

	public List<PartyAttribute> getCorporationFaxDataInfo() {
		return corporationFaxDataInfo;
	}

	public void setCorporationFaxDataInfo(List<PartyAttribute> corporationFaxDataInfo) {
		this.corporationFaxDataInfo = corporationFaxDataInfo;
	}

	public List<PartyAttribute> getCorporationOrgDataInfo() {
		return corporationOrgDataInfo;
	}

	public void setCorporationOrgDataInfo(List<PartyAttribute> corporationOrgDataInfo) {
		this.corporationOrgDataInfo = corporationOrgDataInfo;
	}

	public PartyAttribute getBusiLisType() {
		return busiLisType;
	}

	public void setBusiLisType(PartyAttribute busiLisType) {
		this.busiLisType = busiLisType;
	}

	public PartyAttribute getRegisteAddr() {
		return registeAddr;
	}

	public void setRegisteAddr(PartyAttribute registeAddr) {
		this.registeAddr = registeAddr;
	}

	public PartyAttribute getdCode() {
		return dCode;
	}

	public void setdCode(PartyAttribute dCode) {
		this.dCode = dCode;
	}

	public PartyAttribute getWebSite() {
		return webSite;
	}

	public void setWebSite(PartyAttribute webSite) {
		this.webSite = webSite;
	}

	
	
	public PartyAttribute getCorporationCategory() {
		return corporationCategory;
	}

	public void setCorporationCategory(PartyAttribute corporationCategory) {
		this.corporationCategory = corporationCategory;
	}

	public List<PartyAttribute> getCorporationQualData() {
		return corporationQualData;
	}

	public void setCorporationQualData(List<PartyAttribute> corporationQualData) {
		this.corporationQualData = corporationQualData;
	}
	
	public PartyAttribute getIsShowName() {
		return isShowName;
	}

	public void setIsShowName(PartyAttribute isShowName) {
		this.isShowName = isShowName;
	}

	
	public PartyAttribute getIndustryCategory() {
		return industryCategory;
	}

	public void setIndustryCategory(PartyAttribute industryCategory) {
		this.industryCategory = industryCategory;
	}
	@Override
	public String toString() {
		return "PartyAttributes [industryCategoryList=" + industryCategory + ", webSite="
				+ webSite + ", loa="+loa+ ", corporationCategory=" + corporationCategory + ", corporationQualData="
				+ corporationQualData + ", isShowName=" + isShowName + "]";
	}

	public PartyAttribute getInfoLevel() {
		return infoLevel;
	}

	public void setInfoLevel(PartyAttribute infoLevel) {
		this.infoLevel = infoLevel;
	}

	public PartyAttribute getOtherAttrs() {
		return otherAttrs;
	}

	public void setOtherAttrs(PartyAttribute otherAttrs) {
		this.otherAttrs = otherAttrs;
	}

	public String getIsVendorDetail() {
		return isVendorDetail;
	}

	public void setIsVendorDetail(String isVendorDetail) {
		this.isVendorDetail = isVendorDetail;
	}

	public PartyAttribute getIsSupPrice() {
		return isSupPrice;
	}

	public void setIsSupPrice(PartyAttribute isSupPrice) {
		this.isSupPrice = isSupPrice;
	}

	public PartyAttribute getUsedCurruncy() {
		return usedCurruncy;
	}

	public void setUsedCurruncy(PartyAttribute usedCurruncy) {
		this.usedCurruncy = usedCurruncy;
	}

	public PartyAttribute getVendorMovStatus() {
		return vendorMovStatus;
	}

	public void setVendorMovStatus(PartyAttribute vendorMovStatus) {
		this.vendorMovStatus = vendorMovStatus;
	}

	public PartyAttribute getSkuMovStatus() {
		return skuMovStatus;
	}

	public void setSkuMovStatus(PartyAttribute skuMovStatus) {
		this.skuMovStatus = skuMovStatus;
	}
	

}