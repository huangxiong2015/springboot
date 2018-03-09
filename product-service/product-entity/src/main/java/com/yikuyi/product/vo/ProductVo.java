/*
 * Created: 2016年12月14日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.vo;

import java.util.List;

import org.springframework.beans.BeanUtils;

import com.yikuyi.activity.vo.ActivityProductVo;
import com.yikuyi.document.model.DocumentLog;
import com.yikuyi.packagemail.vo.PackageMailVO;
import com.yikuyi.product.model.Product;
import com.yikuyi.product.model.ProductPrice;
import com.yikuyi.promotion.vo.PromotionFlagVo;
import com.yikuyi.rule.delivery.vo.ProductInfo;
import com.yikuyi.rule.mov.vo.MovInfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 
 * @author zr.wujiajun@yikuyi.com
 * @version 1.0.0
 */
@ApiModel("productVo")
public class ProductVo extends Product {

	private static final long serialVersionUID = -6332580036970800728L;
	
	@ApiModelProperty(value = "供应商包装")
	private String vendorPackaging;

	@ApiModelProperty(value = "交期")
	private ProductInfo realLeadTime;
	
	@ApiModelProperty(value = "活动类型")
	private String activityType;
	
	@ApiModelProperty(value="折扣状态(Y/N)")
	private char promoDiscountStatus;
	
	@ApiModelProperty(value="折扣值")
	private double promoDiscount;
	
	/**
	 * 内地价格范围
	 * @return
	 */
	private String priceRangCH;	
	/**
	 * 美元价格范围
	 */
	private String priceRangHK;
	

	@ApiModelProperty(value = "处理行号")
	private Long lineNo;
	
	@ApiModelProperty(value = "数据处理过程中异常信息,主要用户保存进DocumentLog的,备注")
	private String errorMsg;

	@ApiModelProperty(value = "活动开始时间")
	private String startTime;
	
	@ApiModelProperty(value = "活动结束时间")
	private String endTime;
	
	@ApiModelProperty(value = "商品标题")
	private String title;
	
	@ApiModelProperty(value = "商品副标题")
	private String subTitle;
	
	@ApiModelProperty(value = "商品图片")
	private List<String> imageList;
	
	@ApiModelProperty(value = "活动Id")
	private String activityId;
	
	@ApiModelProperty(value="时间区间id")
	private String periodsId;
	
	@ApiModelProperty(value="是否使用系统库存(Y/N)")
	private String isSystemQty;
	
	@ApiModelProperty(value = "服务器当前时间",example="2016-01-01 00:00:00")
	private String nowDate;
	
	@ApiModelProperty(value = "原价")
	private List<ProductPrice> originalResalePrices;
	
	@ApiModelProperty(value = "原价内地范围价格")
	private String originalPriceRangCH;	

	@ApiModelProperty(value = "原价香港范围价格")
	private String originalPriceRangHK;
	
	@ApiModelProperty(value = "商品描述")
	private String description;
	
	@ApiModelProperty(value="是否启用icon状态(Y/N)")
	private String iconStatus;
	
	@ApiModelProperty(value="icon使用场景和具体图片{LIST:url,DETAIL:url,CART:url}")
	private String iconScenes;
	/**
	 * 详情页使用交期字段
	 */
	@ApiModelProperty(value = "国内最小交期")	
	private Integer minLeadTimeMLShow;
	@ApiModelProperty(value = "国内最大交期")
	private Integer maxLeadTimeMLShow;
	

	@ApiModelProperty(value = "国内工厂最小交期")	
	private Integer minFactoryLeadTimeMLShow;
	@ApiModelProperty(value = "国内工厂最大交期")	
	private Integer maxFactoryLeadTimeMLShow;
	
	@ApiModelProperty(value = "香港最小交期")	
	private Integer minLeadTimeHKShow;
	@ApiModelProperty(value = "香港最大交期")
	private Integer maxLeadTimeHKShow;
	
	@ApiModelProperty(value = "香港工厂交期")	
	private Integer minFactoryLeadTimeHKShow;
	private Integer maxFactoryLeadTimeHKShow;

	@ApiModelProperty(value="最小订单金额信息")
	private MovInfo movInfo;
	
	@ApiModelProperty(value="商品类型：0-现货，1-排单")
	private Integer productType;
	
	
	@ApiModelProperty(value="商品活动信息")
	private ActivityProductVo activityProductVo;
	
	@ApiModelProperty(value="促销标识前端json对应的Vo")
	private PromotionFlagVo promotionFlag;
	
	@ApiModelProperty(value="专属特价显示的文案")
	private String specialOfferText;
	
	@ApiModelProperty(value="包邮信息")
	private PackageMailVO packageMailInfo;
	
	@ApiModelProperty(value="是否推广商品")
	private Boolean isRecommend;
	
	public Boolean getIsRecommend() {
		return isRecommend;
	}


	public void setIsRecommend(Boolean isRecommend) {
		this.isRecommend = isRecommend;
	}


	public PackageMailVO getPackageMailInfo() {
		return packageMailInfo;
	}


	public void setPackageMailInfo(PackageMailVO packageMailInfo) {
		this.packageMailInfo = packageMailInfo;
	}


	public String getSpecialOfferText() {
		return specialOfferText;
	}


	public void setSpecialOfferText(String specialOfferText) {
		this.specialOfferText = specialOfferText;
	}


	public String getVendorPackaging() {
		return vendorPackaging;
	}


	public void setVendorPackaging(String vendorPackaging) {
		this.vendorPackaging = vendorPackaging;
	}


	public PromotionFlagVo getPromotionFlag() {
		return promotionFlag;
	}


	public void setPromotionFlag(PromotionFlagVo promotionFlag) {
		this.promotionFlag = promotionFlag;
	}


	public ActivityProductVo getActivityProductVo() {
		return activityProductVo;
	}


	public void setActivityProductVo(ActivityProductVo activityProductVo) {
		this.activityProductVo = activityProductVo;
	}


	public MovInfo getMovInfo() {
		return movInfo;
	}


	public void setMovInfo(MovInfo movInfo) {
		this.movInfo = movInfo;
	}

	public String getIconStatus() {
		return iconStatus;
	}

	public void setIconStatus(String iconStatus) {
		this.iconStatus = iconStatus;
	}

	public String getIconScenes() {
		return iconScenes;
	}

	public void setIconScenes(String iconScenes) {
		this.iconScenes = iconScenes;
	}

	public String getIsSystemQty() {
		return isSystemQty;
	}

	public void setIsSystemQty(String isSystemQty) {
		this.isSystemQty = isSystemQty;
	}

	public List<ProductPrice> getOriginalResalePrices() {
		return originalResalePrices;
	}

	public void setOriginalResalePrices(List<ProductPrice> originalResalePrices) {
		this.originalResalePrices = originalResalePrices;
	}

	public String getOriginalPriceRangCH() {
		return originalPriceRangCH;
	}

	public void setOriginalPriceRangCH(String originalPriceRangCH) {
		this.originalPriceRangCH = originalPriceRangCH;
	}

	public String getOriginalPriceRangHK() {
		return originalPriceRangHK;
	}

	public void setOriginalPriceRangHK(String originalPriceRangHK) {
		this.originalPriceRangHK = originalPriceRangHK;
	}

	public String getNowDate() {
		return nowDate;
	}

	public void setNowDate(String nowDate) {
		this.nowDate = nowDate;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public List<String> getImageList() {
		return imageList;
	}

	public void setImageList(List<String> imageList) {
		this.imageList = imageList;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public ProductVo(){
		super();
	}
	
	/**
	 * 构造函数，将entity里的属性复制过来
	 * @param product
	 */
	public ProductVo(Product product){
		BeanUtils.copyProperties(product, this);
	}
	
	public ProductInfo getRealLeadTime() {
		return realLeadTime;
	}
	public void setRealLeadTime(ProductInfo realLeadTime) {
		this.realLeadTime = realLeadTime;
	}

	public String getPriceRangCH() {
		return priceRangCH;
	}

	public void setPriceRangCH(String priceRangCH) {
		this.priceRangCH = priceRangCH;
	}

	public String getPriceRangHK() {
		return priceRangHK;
	}

	public void setPriceRangHK(String priceRangHK) {
		this.priceRangHK = priceRangHK;
	}

	public Long getLineNo() {
		return lineNo;
	}

	public void setLineNo(Long lineNo) {
		this.lineNo = lineNo;
	}

	public String getErrorMsg() {
		return errorMsg;
	}
	
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = DocumentLog.getSizeLengthMsg(errorMsg);
	}

	public String getPeriodsId() {
		return periodsId;
	}

	public void setPeriodsId(String periodsId) {
		this.periodsId = periodsId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
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

	public Integer getMinLeadTimeMLShow() {
		return minLeadTimeMLShow;
	}

	public void setMinLeadTimeMLShow(Integer minLeadTimeMLShow) {
		this.minLeadTimeMLShow = minLeadTimeMLShow;
	}

	public Integer getMaxLeadTimeMLShow() {
		return maxLeadTimeMLShow;
	}

	public void setMaxLeadTimeMLShow(Integer maxLeadTimeMLShow) {
		this.maxLeadTimeMLShow = maxLeadTimeMLShow;
	}

	public Integer getMinFactoryLeadTimeMLShow() {
		return minFactoryLeadTimeMLShow;
	}

	public void setMinFactoryLeadTimeMLShow(Integer minFactoryLeadTimeMLShow) {
		this.minFactoryLeadTimeMLShow = minFactoryLeadTimeMLShow;
	}

	public Integer getMaxFactoryLeadTimeMLShow() {
		return maxFactoryLeadTimeMLShow;
	}

	public void setMaxFactoryLeadTimeMLShow(Integer maxFactoryLeadTimeMLShow) {
		this.maxFactoryLeadTimeMLShow = maxFactoryLeadTimeMLShow;
	}

	public Integer getMinLeadTimeHKShow() {
		return minLeadTimeHKShow;
	}

	public void setMinLeadTimeHKShow(Integer minLeadTimeHKShow) {
		this.minLeadTimeHKShow = minLeadTimeHKShow;
	}

	public Integer getMaxLeadTimeHKShow() {
		return maxLeadTimeHKShow;
	}

	public void setMaxLeadTimeHKShow(Integer maxLeadTimeHKShow) {
		this.maxLeadTimeHKShow = maxLeadTimeHKShow;
	}

	public Integer getMinFactoryLeadTimeHKShow() {
		return minFactoryLeadTimeHKShow;
	}

	public void setMinFactoryLeadTimeHKShow(Integer minFactoryLeadTimeHKShow) {
		this.minFactoryLeadTimeHKShow = minFactoryLeadTimeHKShow;
	}

	public Integer getMaxFactoryLeadTimeHKShow() {
		return maxFactoryLeadTimeHKShow;
	}

	public void setMaxFactoryLeadTimeHKShow(Integer maxFactoryLeadTimeHKShow) {
		this.maxFactoryLeadTimeHKShow = maxFactoryLeadTimeHKShow;
	}


	public Integer getProductType() {
		return productType;
	}


	public void setProductType(Integer productType) {
		this.productType = productType;
	}
}