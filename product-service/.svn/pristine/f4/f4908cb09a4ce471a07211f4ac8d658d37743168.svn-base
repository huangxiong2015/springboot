package com.yikuyi.promotion.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.alibaba.fastjson.JSONObject;
import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModelProperty;

@Document(collection = "promotion_module_content_draft")
public class PromotionModuleContentDraft extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1259378939263869629L;
	@ApiModelProperty(value="促销模块ID")
	@Id
	private String promoModuleId;
	
	@ApiModelProperty(value="促销模块内容")
	private JSONObject promotionContent;
	
	@ApiModelProperty(value="活动id")
	private String promotionId;
	
	@ApiModelProperty(value="模块类型")
	private String promoModuleType;
	
	@ApiModelProperty(value="状态")
	private String status;

	public String getPromoModuleType() {
		return promoModuleType;
	}

	public void setPromoModuleType(String promoModuleType) {
		this.promoModuleType = promoModuleType;
	}

	public String getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(String promotionId) {
		this.promotionId = promotionId;
	}


	public JSONObject getPromotionContent() {
		return promotionContent;
	}

	public void setPromotionContent(JSONObject promotionContent) {
		this.promotionContent = promotionContent;
	}

	public String getPromoModuleId() {
		return promoModuleId;
	}

	public void setPromoModuleId(String promoModuleId) {
		this.promoModuleId = promoModuleId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
