package com.yikuyi.promotion.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 *模块生效信息 
 *
 */
public class PromotionModuleEffectiveVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty("折扣显示生效")
	private String isLimit;
	
	@ApiModelProperty("生效开始时间")
	private String startTime;
	
	@ApiModelProperty("生效结束时间")
	private String endTime;
	
	@ApiModelProperty("非生效时间是否显示")
	private String ineffectiveIsShow;
	
	@ApiModelProperty("推广词")
	private String extensionWords;

	public String getIsLimit() {
		return isLimit;
	}

	public void setIsLimit(String isLimit) {
		this.isLimit = isLimit;
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

	public String getIneffectiveIsShow() {
		return ineffectiveIsShow;
	}

	public void setIneffectiveIsShow(String ineffectiveIsShow) {
		this.ineffectiveIsShow = ineffectiveIsShow;
	}

	public String getExtensionWords() {
		return extensionWords;
	}

	public void setExtensionWords(String extensionWords) {
		this.extensionWords = extensionWords;
	}
	
}
