/*
 * Created: 2017年3月16日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.audit.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModelProperty;

/**
 * mongodb ip查询
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@Document(collection = "audit_log")
public class AuditLog extends AbstractEntity{

   private static final long serialVersionUID = -8071630422943674647L;

	@Id
	@ApiModelProperty(value = "id")
	@JsonProperty("id")
	private String id;
	
	@ApiModelProperty(value = "请求返回码")
	private String code;
	
	@ApiModelProperty(value = "国家")
	private String  country;
	
	@ApiModelProperty(value = "国家id")
	private String  countryId;
	
	@ApiModelProperty(value = "地区")
	private String  area;
	
	@ApiModelProperty(value = "地区id")
	private String  areaId;
	
	@ApiModelProperty(value = "")
	private String  region;
	
	@ApiModelProperty(value = "")
	private String  regionId;
	
	@ApiModelProperty(value = "城市")
	private String  city;
	
	@ApiModelProperty(value = "城市id")
	private String  cityId;
	
	@ApiModelProperty(value = "")
	private String  county;
	
	@ApiModelProperty(value = "id")
	private String  countyId;
	
	@ApiModelProperty(value = "")
	private String  isp;
	
	@ApiModelProperty(value = "id")
	private String  ispId;
	
	@ApiModelProperty(value = "ip")
	private String  ip;
	
	@ApiModelProperty(value = "创建时间")
	private String createTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getCountyId() {
		return countyId;
	}

	public void setCountyId(String countyId) {
		this.countyId = countyId;
	}

	public String getIsp() {
		return isp;
	}

	public void setIsp(String isp) {
		this.isp = isp;
	}

	public String getIspId() {
		return ispId;
	}

	public void setIspId(String ispId) {
		this.ispId = ispId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
}
