package com.yikuyi.rule.logistics.vo;

import java.math.BigDecimal;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("LogisticsCondInfo")
@JsonInclude(Include.NON_NULL)  //实体中为null的字段不进行序列化
public class LogisticsCondInfo {
	
	@ApiModelProperty(value="交货地(CH:国内，HK：香港)")
	private String deliveryPlace;
	
	@ApiModelProperty(value="包邮运费金额")
	private BigDecimal pinkageAmount;
	
	@ApiModelProperty(value="不包邮运费")
	private List<LogisticsCondFee> faltPinkageAmount;

	public String getDeliveryPlace() {
		return deliveryPlace;
	}

	public void setDeliveryPlace(String deliveryPlace) {
		this.deliveryPlace = deliveryPlace;
	}

	public BigDecimal getPinkageAmount() {
		return pinkageAmount;
	}

	public void setPinkageAmount(BigDecimal pinkageAmount) {
		this.pinkageAmount = pinkageAmount;
	}

	public List<LogisticsCondFee> getFaltPinkageAmount() {
		return faltPinkageAmount;
	}

	public void setFaltPinkageAmount(List<LogisticsCondFee> faltPinkageAmount) {
		this.faltPinkageAmount = faltPinkageAmount;
	}

}
