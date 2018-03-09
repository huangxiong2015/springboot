package com.yikuyi.activity.vo;

import java.util.Date;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModelProperty;

public class MockActivityDataVo extends AbstractEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8775325850452424605L;

	@ApiModelProperty(value="partyId")
	private String partyId;
	
	@ApiModelProperty(value="名称")
	private String partyName;
	
	@ApiModelProperty(value="活动类型")
	private String activityType;
	
	@ApiModelProperty(value="礼品")
	private String gift;
	
	@ApiModelProperty(value="创建日期")
	private Date createDate;
	
	@ApiModelProperty(value="数据类型")
	private String mockType;
	
	@ApiModelProperty(value="当前时间")
	private Date lastUpdateDate;

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public String getPartyName() {
		return partyName;
	}

	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getGift() {
		return gift;
	}

	public void setGift(String gift) {
		this.gift = gift;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getMockType() {
		return mockType;
	}

	public void setMockType(String mockType) {
		this.mockType = mockType;
	}
	
}
