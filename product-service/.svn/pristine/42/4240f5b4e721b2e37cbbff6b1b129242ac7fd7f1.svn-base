/*
 * Created: 2017年6月13日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.activity.vo;

import java.util.List;

import com.yikuyi.activity.model.ActivityProduct;
import com.yikuyi.promotion.vo.PromotionFlagVo;

import io.swagger.annotations.ApiModelProperty;

public class ActivityProductVo extends ActivityProduct{
	
	private static final long serialVersionUID = -7347954834371189909L;
	
	private List<Integer> qtyBreak;
	
	private List<Double> priceBreak;
	
	@ApiModelProperty(value="活动开始时间(18:00)")
	private String startTime;
	
	@ApiModelProperty(value="活动结束时间(19:00)")
	private String endTime;
	
	@ApiModelProperty(value="是否指定供应商(true/false)")
	private boolean specifySupplier;
	
	//模块ID
	private String moduelId;
	
	private PromotionFlagVo promotionFlag;
	
	@ApiModelProperty("模块开始时间")
	private String moduelStartTime;
	
	@ApiModelProperty("模块结束时间")
	private String moduelEndTime;
	
	/*
	@ApiModelProperty(value="是否启用icon状态(Y/N)")
	private char iconStatus;
	
	@ApiModelProperty(value="icon使用场景和具体图片{LIST:url,DETAIL:url,CART:url}")
	private String iconScenes;*/
	
	/*public char getIconStatus() {
		return iconStatus;
	}

	public void setIconStatus(char iconStatus) {
		this.iconStatus = iconStatus;
	}

	public String getIconScenes() {
		return iconScenes;
	}

	public void setIconScenes(String iconScenes) {
		this.iconScenes = iconScenes;
	}*/

	public String getModuelStartTime() {
		return moduelStartTime;
	}

	public void setModuelStartTime(String moduelStartTime) {
		this.moduelStartTime = moduelStartTime;
	}

	public String getModuelEndTime() {
		return moduelEndTime;
	}

	public void setModuelEndTime(String moduelEndTime) {
		this.moduelEndTime = moduelEndTime;
	}

	public boolean isSpecifySupplier() {
		return specifySupplier;
	}

	public PromotionFlagVo getPromotionFlag() {
		return promotionFlag;
	}

	public void setPromotionFlag(PromotionFlagVo promotionFlag) {
		this.promotionFlag = promotionFlag;
	}

	public String getModuelId() {
		return moduelId;
	}

	public void setModuelId(String moduelId) {
		this.moduelId = moduelId;
	}

	public void setSpecifySupplier(boolean specifySupplier) {
		this.specifySupplier = specifySupplier;
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

	public List<Integer> getQtyBreak() {
		return qtyBreak;
	}

	public void setQtyBreak(List<Integer> qtyBreak) {
		this.qtyBreak = qtyBreak;
	}

	public List<Double> getPriceBreak() {
		return priceBreak;
	}

	public void setPriceBreak(List<Double> priceBreak) {
		this.priceBreak = priceBreak;
	}
}