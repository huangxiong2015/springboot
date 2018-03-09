package com.yikuyi.strategy.vo;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("LimitedPurchaseVo")
public class LimitedPurchaseVo extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty("策略ID")
	private String strategyId;
	
	@ApiModelProperty("限购标识(Y/N)")
	private String limitedPurchaseFlag;
	
	@ApiModelProperty("限购是否有效(Y/N)")
	private String isLimitedPurchase;
	
	@ApiModelProperty("限购数量")
	private int num;
	
	@ApiModelProperty(value = "限购开始时间")
	private String startTime;
	
	@ApiModelProperty(value = "限购结束时间")
	private String endTime;
	
	public String getLimitedPurchaseFlag() {
		return limitedPurchaseFlag;
	}

	public void setLimitedPurchaseFlag(String limitedPurchaseFlag) {
		this.limitedPurchaseFlag = limitedPurchaseFlag;
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

	public String getStrategyId() {
		return strategyId;
	}

	public void setStrategyId(String strategyId) {
		this.strategyId = strategyId;
	}

	public String getIsLimitedPurchase() {
		return isLimitedPurchase;
	}

	public void setIsLimitedPurchase(String isLimitedPurchase) {
		this.isLimitedPurchase = isLimitedPurchase;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
	
	

}
