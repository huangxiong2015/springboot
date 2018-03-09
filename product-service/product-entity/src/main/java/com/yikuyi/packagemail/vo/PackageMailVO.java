package com.yikuyi.packagemail.vo;

import java.util.Date;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("packageMailVO")
public class PackageMailVO extends AbstractEntity{

	private static final long serialVersionUID = 9011959983876929737L;
	
	@ApiModelProperty("包邮策略ID")
	private String strategyId;
	
	@ApiModelProperty(value = "是否包邮（Y/N）")
	private String isPackageMail;
	
	@ApiModelProperty(value = "包邮开始时间")
	private Date startTime;
	
	@ApiModelProperty(value = "包邮结束时间")
	private Date endTime;

	public String getStrategyId() {
		return strategyId;
	}

	public void setStrategyId(String strategyId) {
		this.strategyId = strategyId;
	}

	public String getIsPackageMail() {
		return isPackageMail;
	}

	public void setIsPackageMail(String isPackageMail) {
		this.isPackageMail = isPackageMail;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

}
