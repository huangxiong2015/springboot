package com.yikuyi.activity.model;

import java.util.List;
import com.ykyframework.model.AbstractEntity;
import io.swagger.annotations.ApiModelProperty;

public class ActivityPeriods extends AbstractEntity{
	
	private static final long serialVersionUID = 3956717154513089160L;
	
	public enum Status{
		/**
		 * 无效
		 */
		UNABLE,
		/**
		 * 有效
		 */
		ENABLE
	}
	
	@ApiModelProperty(value="时间区间id")
	private String periodsId;
	
	@ApiModelProperty(value="活动id")
	private String activityId;
	
	@ApiModelProperty(value="活动开始时间")
	private String startTime;
	
	@ApiModelProperty(value="活动结束时间")
	private String endTime;
	
	@ApiModelProperty(value="状态 0:无效 1:有效")
	private Status status;
	
	@ApiModelProperty(value="活动商品")
	private List<ActivityProduct> productList;

	public List<ActivityProduct> getProductList() {
		return productList;
	}

	public void setProductList(List<ActivityProduct> productList) {
		this.productList = productList;
	}

	public String getPeriodsId() {
		return periodsId;
	}

	public void setPeriodsId(String periodsId) {
		this.periodsId = periodsId;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
