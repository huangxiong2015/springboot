package com.yikuyi.rule.logistics.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("LogisticsFee")
@JsonInclude(Include.NON_NULL)  //实体中为null的字段不进行序列化
public class LogisticsFee {
	
	@ApiModelProperty(value="寄件省份Id")
	private String fromGeoId;
	
	@ApiModelProperty(value="收件省份Id")
	private String toGeoId;
	
	@ApiModelProperty(value="币种类型")
	private String currency;
	
	@ApiModelProperty(value="总额")
	private double amount;

	public String getFromGeoId() {
		return fromGeoId;
	}

	public void setFromGeoId(String fromGeoId) {
		this.fromGeoId = fromGeoId;
	}

	public String getToGeoId() {
		return toGeoId;
	}

	public void setToGeoId(String toGeoId) {
		this.toGeoId = toGeoId;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
}
