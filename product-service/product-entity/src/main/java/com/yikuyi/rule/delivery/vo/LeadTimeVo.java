package com.yikuyi.rule.delivery.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("LeadTimeVo")
@JsonInclude(Include.NON_NULL)  //实体中为null的字段不进行序列化
public class LeadTimeVo {
	
	@ApiModelProperty(value="交货地(CH:国内，HK：香港)")
	private String deliveryPlace;
	
	@ApiModelProperty(value="实际交期")
	private String realityLeadTime;

	public String getDeliveryPlace() {
		return deliveryPlace;
	}

	public void setDeliveryPlace(String deliveryPlace) {
		this.deliveryPlace = deliveryPlace;
	}

	public String getRealityLeadTime() {
		return realityLeadTime;
	}

	public void setRealityLeadTime(String realityLeadTime) {
		this.realityLeadTime = realityLeadTime;
	}
	
}
