package com.yikuyi.promotion.vo;

import java.util.Date;
import java.util.List;

import com.yikuyi.promotion.model.Promotion.CreateType;

public class PromotionAndModuleVo{
	
	private String promotionId;
	
	private String promotionName;
	
	private String promotionStatus;
	
	private CreateType createType;
	
	private String displaySidebar;
	
	private String previewDisplaySidebar;
	
	private String promotionUrl;
	
	private String faceImgUrl;
	
	private String couponIds;
	
	private String isUseCoupon;
	
	private Date startDate;
	
	private Date endDate;
	
	private List<PromotionModuleVo> list;

	public String getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(String promotionId) {
		this.promotionId = promotionId;
	}

	public String getPromotionName() {
		return promotionName;
	}

	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
	}

	public String getPromotionStatus() {
		return promotionStatus;
	}

	public void setPromotionStatus(String promotionStatus) {
		this.promotionStatus = promotionStatus;
	}

	public CreateType getCreateType() {
		return createType;
	}

	public void setCreateType(CreateType createType) {
		this.createType = createType;
	}

	public String getDisplaySidebar() {
		return displaySidebar;
	}

	public void setDisplaySidebar(String displaySidebar) {
		this.displaySidebar = displaySidebar;
	}
	
	public String getPreviewDisplaySidebar() {
		return previewDisplaySidebar;
	}

	public void setPreviewDisplaySidebar(String previewDisplaySidebar) {
		this.previewDisplaySidebar = previewDisplaySidebar;
	}

	public String getPromotionUrl() {
		return promotionUrl;
	}

	public void setPromotionUrl(String promotionUrl) {
		this.promotionUrl = promotionUrl;
	}

	public String getFaceImgUrl() {
		return faceImgUrl;
	}

	public void setFaceImgUrl(String faceImgUrl) {
		this.faceImgUrl = faceImgUrl;
	}

	public String getCouponIds() {
		return couponIds;
	}

	public void setCouponIds(String couponIds) {
		this.couponIds = couponIds;
	}
	
	public String getIsUseCoupon() {
		return isUseCoupon;
	}

	public void setIsUseCoupon(String isUseCoupon) {
		this.isUseCoupon = isUseCoupon;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<PromotionModuleVo> getList() {
		return list;
	}

	public void setList(List<PromotionModuleVo> list) {
		this.list = list;
	}

	

	

	
	
	
	
	
	
	

	
	
}