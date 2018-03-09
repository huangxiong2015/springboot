package com.yikuyi.party.facility.model;


import java.util.Date;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author zr.aoxianbing@yikuyi.com
 * @version 1.0.0
 */
@ApiModel("Facility")
public class Facility extends AbstractEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4389941009015673340L;
	@ApiModelProperty(value = "仓库ID")
	private String id;
	@ApiModelProperty(value = "Spirit ID ,用于描述人、组织、团体的ID。")
	private String ownerPartyId;
	@ApiModelProperty(value = "仓库名")
	private String facilityName;
	private Double defaultDaysToShip;
	@ApiModelProperty(value = "有效期", example="2016-01-01 08:00:00")
	private Date fromDate;
	@ApiModelProperty(value = "失效日期", example="2016-01-01 08:00:00")
	private Date thruDate;
	
	@ApiModelProperty(value = "仓库别名")
	private String facilityNameAlia;
	
	@ApiModelProperty(value = "是否显示状态Y显示，N不显示")
	private String displayFlag;
	
	
	@ApiModelProperty(value = "是否显示仓库Y显示，N不显示")
	private String isShowaFacility;
	
	public String getIsShowaFacility() {
		return isShowaFacility;
	}
	public void setIsShowaFacility(String isShowaFacility) {
		this.isShowaFacility = isShowaFacility;
	}
	public String getFacilityNameAlia() {
		return facilityNameAlia;
	}
	public void setFacilityNameAlia(String facilityNameAlia) {
		this.facilityNameAlia = facilityNameAlia;
	}
	public String getDisplayFlag() {
		return displayFlag;
	}
	public void setDisplayFlag(String displayFlag) {
		this.displayFlag = displayFlag;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOwnerPartyId() {
		return ownerPartyId;
	}
	public void setOwnerPartyId(String ownerPartyId) {
		this.ownerPartyId = ownerPartyId;
	}
	public String getFacilityName() {
		return facilityName;
	}
	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}
	public Double getDefaultDaysToShip() {
		return defaultDaysToShip;
	}
	public void setDefaultDaysToShip(Double defaultDaysToShip) {
		this.defaultDaysToShip = defaultDaysToShip;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getThruDate() {
		return thruDate;
	}
	public void setThruDate(Date thruDate) {
		this.thruDate = thruDate;
	}
}