package com.yikuyi.rule.logistics.vo;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("LogisticsCondFee")
@JsonInclude(Include.NON_NULL)  //实体中为null的字段不进行序列化
public class LogisticsCondFee {
	
	@ApiModelProperty(value="运送地点")
	private String shipSite;
	
	@ApiModelProperty(value="运费")
	private BigDecimal faltAmount;

	public String getShipSite() {
		return shipSite;
	}

	public void setShipSite(String shipSite) {
		this.shipSite = shipSite;
	}

	public BigDecimal getFaltAmount() {
		return faltAmount;
	}

	public void setFaltAmount(BigDecimal faltAmount) {
		this.faltAmount = faltAmount;
	}

}
