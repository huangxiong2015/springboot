package com.yikuyi.party.vo;

import java.util.Date;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("FacilityVo")
public class FacilityVo  extends AbstractEntity{
	/**
	 * 供应商仓库
	 */
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "仓库ID")
	private String id;
	@ApiModelProperty(value = "仓库名")
	private String name;
	
	@ApiModelProperty(value = "Spirit ID ,用于描述人、组织、团体的ID。")
	private String ownerPartyId;
	
	private Double defaultDaysToShip;
	
	@ApiModelProperty(value = "有效期", example="2016-01-01 08:00:00")
	private Date fromDate;
	@ApiModelProperty(value = "失效日期", example="2016-01-01 08:00:00")
	private Date thruDate;
	
	
	

	public String getOwnerPartyId() {
		return ownerPartyId;
	}

	public void setOwnerPartyId(String ownerPartyId) {
		this.ownerPartyId = ownerPartyId;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	public Double getDefaultDaysToShip() {
		return defaultDaysToShip;
	}

	public void setDefaultDaysToShip(Double defaultDaysToShip) {
		this.defaultDaysToShip = defaultDaysToShip;
	}

	@Override
	public String toString() {
		return "FacilityVo [id=" + id + ", name=" + name + "]";
	}

}
