package com.yikuyi.activity.model;

import java.util.Date;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModelProperty;

public class ActivitySaleHistory extends AbstractEntity{

	private static final long serialVersionUID = 6899094035646894720L;

	@ApiModelProperty(value="活动商品id")
	private String activityProductId;

	@ApiModelProperty(value="活动id")
	private String activityId;

	@ApiModelProperty(value="活动区间id")
	private String periodsId;

	@ApiModelProperty(value="商品id")
	private String productId;

	@ApiModelProperty(value="活动日期")
	private Date activityDate;

	@ApiModelProperty(value="总数量")
	private Long totalQty;

	@ApiModelProperty(value="剩余数量")
	private Long qty;

	public String getActivityProductId() {
		return activityProductId;
	}

	public void setActivityProductId(String activityProductId) {
		this.activityProductId = activityProductId;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getPeriodsId() {
		return periodsId;
	}

	public void setPeriodsId(String periodsId) {
		this.periodsId = periodsId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Date getActivityDate() {
		return activityDate;
	}

	public void setActivityDate(Date activityDate) {
		this.activityDate = activityDate;
	}

	public Long getTotalQty() {
		return totalQty;
	}

	public void setTotalQty(Long totalQty) {
		this.totalQty = totalQty;
	}

	public Long getQty() {
		return qty;
	}

	public void setQty(Long qty) {
		this.qty = qty;
	}
	
	
}
