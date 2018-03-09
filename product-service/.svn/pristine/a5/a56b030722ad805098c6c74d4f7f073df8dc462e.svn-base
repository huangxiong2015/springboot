package com.yikuyi.activity.model;

import java.util.Date;
import java.util.List;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModelProperty;

public class Activity extends AbstractEntity{
	
	private static final long serialVersionUID = 3956717154513089160L;
	
	public enum Status{
		/**
		 * 无效
		 */
		UNABLE,
		/**
		 * 有效
		 */
		ENABLE,
		/**
		 * 删除
		 */
		DELETE
	}
	
	@ApiModelProperty(value="活动id")
	private String activityId;
	
	@ApiModelProperty(value="活动名称")
	private String name;
	
	@ApiModelProperty(value="活动类型:10000:商品促销  ，10001：显示促销")
	private String type;
	
	@ApiModelProperty(value="状态 UNABLE:无效 ENABLE:有效 DELETE:删除")
	private Status status;
	
	@ApiModelProperty(value="活动开始时间")
	private Date startDate;
	
	@ApiModelProperty(value="活动结束时间")
	private Date endDate;
	
	@ApiModelProperty(value="商品列表标识")
	private String productListCss;
	
	@ApiModelProperty(value="商品详情标识")
	private String productDetailCss;
	
	@ApiModelProperty(value="品牌标识")
	private String brandCss;
	
	@ApiModelProperty(value="供应商标识")
	private String vendorCss;
	
	@ApiModelProperty(value="创建人名称")
	private String creatorName;
	
	@ApiModelProperty(value="最后修改人名称")
	private String lastUpdateUserName;
	
	@ApiModelProperty(value="活动区间")
	private List<ActivityPeriods> periodsList;
	
	@ApiModelProperty(value="列表活动开始时间")
	private String startDates;
	
	@ApiModelProperty(value="列表活动结束时间")
	private String endDates;
	
	@ApiModelProperty(value="折扣状态(Y/N)")
	private char promoDiscountStatus;
	
	@ApiModelProperty(value="折扣值")
	private double promoDiscount;
	
	@ApiModelProperty(value="是否使用系统库存(Y/N)")
	private char isSystemQty;
	
	@ApiModelProperty(value="是否启用icon状态(Y/N)")
	private char iconStatus;
	
	@ApiModelProperty(value="是否指定供应商(true/false)")
	private Boolean specifySupplier;
	
	@ApiModelProperty(value="指定供应商规则[{SUPPLIER_ID:XX,SUPPLIER_NAME:XX}]")
	private String supplierRule;
	
	@ApiModelProperty(value="icon使用场景和具体图片{LIST:url,DETAIL:url,CART:url}")
	private String iconScenes;
	
	public Boolean getSpecifySupplier() {
		return specifySupplier;
	}

	public void setSpecifySupplier(Boolean specifySupplier) {
		this.specifySupplier = specifySupplier;
	}

	public String getSupplierRule() {
		return supplierRule;
	}

	public void setSupplierRule(String supplierRule) {
		this.supplierRule = supplierRule;
	}

	public char getIconStatus() {
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
	}

	public char getIsSystemQty() {
		return isSystemQty;
	}

	public void setIsSystemQty(char isSystemQty) {
		this.isSystemQty = isSystemQty;
	}

	public String getStartDates() {
		return startDates;
	}

	public void setStartDates(String startDates) {
		this.startDates = startDates;
	}

	public String getEndDates() {
		return endDates;
	}

	public void setEndDates(String endDates) {
		this.endDates = endDates;
	}

	public List<ActivityPeriods> getPeriodsList() {
		return periodsList;
	}

	public void setPeriodsList(List<ActivityPeriods> periodsList) {
		this.periodsList = periodsList;
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

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
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

	public String getProductListCss() {
		return productListCss;
	}

	public void setProductListCss(String productListCss) {
		this.productListCss = productListCss;
	}

	public String getProductDetailCss() {
		return productDetailCss;
	}

	public void setProductDetailCss(String productDetailCss) {
		this.productDetailCss = productDetailCss;
	}

	public String getBrandCss() {
		return brandCss;
	}

	public void setBrandCss(String brandCss) {
		this.brandCss = brandCss;
	}

	public String getVendorCss() {
		return vendorCss;
	}

	public void setVendorCss(String vendorCss) {
		this.vendorCss = vendorCss;
	}

	public char getPromoDiscountStatus() {
		return promoDiscountStatus;
	}

	public void setPromoDiscountStatus(char promoDiscountStatus) {
		this.promoDiscountStatus = promoDiscountStatus;
	}

	public double getPromoDiscount() {
		return promoDiscount;
	}

	public void setPromoDiscount(double promoDiscount) {
		this.promoDiscount = promoDiscount;
	}
}