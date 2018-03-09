package com.yikuyi.promotion.model;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModelProperty;

public class PromotionModule extends AbstractEntity{
	
	private static final long serialVersionUID = 3956717154513089160L;
	
	@ApiModelProperty(value="促销模块id")
	private String promoModuleId;
	
	@ApiModelProperty(value="促销id")
	private String promotionId;
		
	@ApiModelProperty(value="模块类型")
	private PromoModuleType promoModuleType;
	
	@ApiModelProperty(value="状态")
	private PromoModuleStatus promoModuleStatus;
	
	@ApiModelProperty(value="模块排序")
	private int orderSeq;
	
	@ApiModelProperty(value="创建者名称")
	private String creatorName;
	
	@ApiModelProperty(value="最后更改使用者名称")
	private String lastUpdateUserName;
	
	public enum PromoModuleType{
		/**
		 * 优惠券
		 */
		COUPON,
		/**
		 * banner
		 */
		BANNER,
		/**
		 * 商品列表
		 */
		PRODUCT_LIST,
		/**
		 * 自定义
		 */
		CUSTOM,
		/**
		 * 搜索
		 */
		SEARCH
	}
	
	public enum PromoModuleStatus{
		/**
		 * 无效
		 */
		DISABLE,
		/**
		 * 有效
		 */
		ENABLE,
		/**
		 * 删除
		 */
		DELETE
	}

	public String getPromoModuleId() {
		return promoModuleId;
	}

	public void setPromoModuleId(String promoModuleId) {
		this.promoModuleId = promoModuleId;
	}

	public String getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(String promotionId) {
		this.promotionId = promotionId;
	}

	public PromoModuleType getPromoModuleType() {
		return promoModuleType;
	}

	public void setPromoModuleType(PromoModuleType promoModuleType) {
		this.promoModuleType = promoModuleType;
	}

	public PromoModuleStatus getPromoModuleStatus() {
		return promoModuleStatus;
	}

	public void setPromoModuleStatus(PromoModuleStatus promoModuleStatus) {
		this.promoModuleStatus = promoModuleStatus;
	}

	public int getOrderSeq() {
		return orderSeq;
	}

	public void setOrderSeq(int orderSeq) {
		this.orderSeq = orderSeq;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getLastUpdateUserName() {
		return lastUpdateUserName;
	}

	public void setLastUpdateUserName(String lastUpdateUserName) {
		this.lastUpdateUserName = lastUpdateUserName;
	}


	
}