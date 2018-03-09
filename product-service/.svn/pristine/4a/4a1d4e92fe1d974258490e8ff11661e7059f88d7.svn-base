package com.yikuyi.activity.vo;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author tongkun
 * 活动商品下订单的vo
 */
public class ActivityProducOrderVo implements Serializable{
	private static final long serialVersionUID = 1L;

	public enum Operation{
		/**
		 * 增加
		 */
		INCREASE,
		/**
		 * 减少
		 */
		DECREASE
	}

	public enum ErrorCode{
		/**
		 * 活动未生效
		 */
		NOT_EFFECT_ACTIVITY,
		/**
		 * 数量不足
		 */
		QTY_NOT_ENOUGH,
		/**
		 * 操作失败 
		 */
		OPERATE_FAIL
	}
	
	/**
	 * 该商品参加的活动id
	 */
	@ApiModelProperty(value="该商品参加的活动id")
	private String activityId;
	
	/**
	 * 该商品参加的时段ID
	 */
	@ApiModelProperty(value="时间区间id")
	private String periodsId;
	
	/**
	 * 商品id
	 */
	@ApiModelProperty(value="商品id")
	private String productId;
	
	/**
	 * 操作
	 */
	@ApiModelProperty(value="操作，增加是：INCREASE，减少是：DECREASE")
	private Operation operation;
	
	/**
	 * 数量
	 */
	@ApiModelProperty(value="数量")
	private int number;
	
	/**
	 * 总数
	 */
	@ApiModelProperty(value="库存总数")
	private int totalQty;

	/**
	 * 当前数量
	 */
	@ApiModelProperty(value="当前数量")
	private Long qty;

	/**
	 * 错误码
	 */
	@ApiModelProperty(value="错误码")
	private ErrorCode errorCode;

	/**
	 * 错误说明
	 */
	@ApiModelProperty(value="错误说明")
	private String errorMessage;

	/**
	 * 下订单的时间，如果为空表示当天
	 */
	@ApiModelProperty(value="下订单的时间，如果为空表示当天")
	private Date orderDate;

	public int getTotalQty() {
		return totalQty;
	}

	public void setTotalQty(int totalQty) {
		this.totalQty = totalQty;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Long getQty() {
		return qty;
	}

	public void setQty(Long qty) {
		this.qty = qty;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getPeriodsId() {
		return periodsId;
	}

	public void setPeriodsId(String periodsId) {
		this.periodsId = periodsId;
	}
	
	public String getActivityQtyKey() {
		if(StringUtils.isNotBlank(activityId) && StringUtils.isNotBlank(periodsId) && StringUtils.isNotBlank(productId)){
			return new StringBuffer(ActivityVo.ACTIVITY_PRODUCT_QTY_CACHE).append(activityId).append("-").append(periodsId).append("-").append(productId).toString();
		}else{
			return StringUtils.EMPTY;
		}
	}
}