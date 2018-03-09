package com.yikuyi.promotion.model;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("Promotion")
public class Promotion extends AbstractEntity{
	
	private static final long serialVersionUID = 3956717154513089160L;
	
	@ApiModelProperty(value="促销id")
	private String promotionId;
	
	@ApiModelProperty(value="促销名称")
	private String promotionName;
	
	@ApiModelProperty(value="状态")
	private PromotionStatus promotionStatus;
	
	@ApiModelProperty(value="活动类型")
	private PromotionType  promotionType;
	
	@ApiModelProperty(value="创建方式")
	private CreateType  createType;
	
	@ApiModelProperty(value="控制是否显示导航的侧边栏,Y:显示,N:不显示")
	private String displaySidebar;
	
	@ApiModelProperty(value="预览是否显示导航的侧边栏,Y:显示,N:不显示")
	private String previewDisplaySidebar;
	
	@ApiModelProperty(value="活动链接URL")
	private String promotionUrl;
	
	@ApiModelProperty(value="活动封面图片地址URL")
	private String faceImgUrl;
	
	@ApiModelProperty(value="优惠券id的集合")
	private String couponIds;
	
	@ApiModelProperty(value="是否使用优惠券,Y:使用,N:不使用")
	private String isUseCoupon;
	
	@ApiModelProperty(value="促销开始时间")
	private Date startDate;
	
	@ApiModelProperty(value="促销结束时间")
	private Date endDate;
	
	@ApiModelProperty(value="创建者名称")
	private String creatorName;
	
	@ApiModelProperty(value="最后更改使用者名称")
	private String lastUpdateUserName;
	
	@ApiModelProperty(value="SEO标题")
	private String seoTitle;
	
	@ApiModelProperty(value="SEO关键字")
	private String seoKeyword;
	
	@ApiModelProperty(value="SEO描述")
	private String seoDescription;

	@ApiModelProperty(value="活动内容")
	private JSONObject promotionContent;
	
	public JSONObject getPromotionContent() {
		return promotionContent;
	}

	public void setPromotionContent(JSONObject promotionContent) {
		this.promotionContent = promotionContent;
	}

	public String getSeoTitle() {
		return seoTitle;
	}

	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}

	public String getSeoKeyword() {
		return seoKeyword;
	}

	public void setSeoKeyword(String seoKeyword) {
		this.seoKeyword = seoKeyword;
	}

	public String getSeoDescription() {
		return seoDescription;
	}

	public void setSeoDescription(String seoDescription) {
		this.seoDescription = seoDescription;
	}
	

	public PromotionType getPromotionType() {
		return promotionType;
	}

	public void setPromotionType(PromotionType promotionType) {
		this.promotionType = promotionType;
	}


	public enum PromotionStatus{
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
	
	public enum CreateType{
		/**
		 * 定制开发
		 */
		CUSTOM,
		/**
		 * 模板创建
		 */
		TEMPLATE
	}
	
	public enum PromotionType{
		/**
		 * 普通活动
		 */
		ORDINARY,
		/**
		 * 购物车推荐活动
		 */
		RECOMMEND
	}

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

	public PromotionStatus getPromotionStatus() {
		return promotionStatus;
	}

	public void setPromotionStatus(PromotionStatus promotionStatus) {
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