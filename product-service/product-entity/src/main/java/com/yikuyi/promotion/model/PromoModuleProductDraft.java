package com.yikuyi.promotion.model;

import com.yikuyi.promotion.model.PromoModuleProduct.ModuleProductStatus;
import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModelProperty;

public class PromoModuleProductDraft extends AbstractEntity{
	
	private static final long serialVersionUID = 3956717154513089160L;
	
	@ApiModelProperty(value="促销模块商品id")
	private String promoModuleProductId;
	
	@ApiModelProperty(value="促销模块id")
	private String promoModuleId;
	
	@ApiModelProperty(value="促销活动id")
	private String promotionId;
	
	@ApiModelProperty(value="商品id")
	private String productId;
	
	@ApiModelProperty(value="制造商型号")
	private String manufacturerPartNumber;
	
	@ApiModelProperty(value="制造商")
	private String manufacturer;
	
	@ApiModelProperty(value="仓库Id")
	private String sourceId;
	
	@ApiModelProperty(value="仓库名称")
	private String sourceName;
	
	@ApiModelProperty(value="供应商Id")
	private String vendorId;
	
	@ApiModelProperty(value="供应商名称")
	private String vendorName;
	
	@ApiModelProperty(value="折扣")
	private float discount;
	
	@ApiModelProperty(value="币种")
	private String currencyUomId;
	
	@ApiModelProperty(value="")
	private String qtyBreak1;
	
	@ApiModelProperty(value="")
	private String priceBreak1;
	
	@ApiModelProperty(value="")
	private String qtyBreak2;
	
	@ApiModelProperty(value="")
	private String priceBreak2;
	
	@ApiModelProperty(value="")
	private String qtyBreak3;
	
	@ApiModelProperty(value="")
	private String priceBreak3;
	
	@ApiModelProperty(value="")
	private String qtyBreak4;
	
	@ApiModelProperty(value="")
	private String priceBreak4;
	
	@ApiModelProperty(value="")
	private String qtyBreak5;
	
	@ApiModelProperty(value="")
	private String priceBreak5;
	
	@ApiModelProperty(value="原价")
	private String originalUnitPrice;
	
	@ApiModelProperty(value="当前销售单价")
	private String resaleUnitPrice;
	
	@ApiModelProperty(value="标题")
	private String title;
	
	@ApiModelProperty(value="sub标题")
	private String subTitle;
	
	@ApiModelProperty(value="image1")
	private String image1;
	
	@ApiModelProperty(value="image2")
	private String image2;
	
	@ApiModelProperty(value="image3")
	private String image3;
	
	@ApiModelProperty(value="image4")
	private String image4;
	
	@ApiModelProperty(value="image5")
	private String image5;
	
	@ApiModelProperty(value="")
	private String description;
	
	@ApiModelProperty(value="总库存数")
	private String totalQty;
	
	@ApiModelProperty(value="当前库存数")
	private String qty;
	
	@ApiModelProperty(value="次小类名称")
	private String category3Name;
	
	@ApiModelProperty(value="次小类Id")
	private String category3Id;
		
	@ApiModelProperty(value="文件上传批次号")
	private String processId;
	
	@ApiModelProperty(value="错误描述")
	private String errorDescription;
	
	@ApiModelProperty(value="Excel行号")
	private int rowNum;//Excel行号
	
	private ModuleProductStatus status;
	
	@ApiModelProperty(value="描述")
	private String discription;
	
	
	public String getDiscription() {
		return discription;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

	public ModuleProductStatus getStatus() {
		return status;
	}

	public void setStatus(ModuleProductStatus status) {
		this.status = status;
	}

	public String getPromoModuleProductId() {
		return promoModuleProductId;
	}

	public void setPromoModuleProductId(String promoModuleProductId) {
		this.promoModuleProductId = promoModuleProductId;
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

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getManufacturerPartNumber() {
		return manufacturerPartNumber;
	}

	public void setManufacturerPartNumber(String manufacturerPartNumber) {
		this.manufacturerPartNumber = manufacturerPartNumber;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	}

	public String getCurrencyUomId() {
		return currencyUomId;
	}

	public void setCurrencyUomId(String currencyUomId) {
		this.currencyUomId = currencyUomId;
	}


	public String getOriginalUnitPrice() {
		return originalUnitPrice;
	}

	public void setOriginalUnitPrice(String originalUnitPrice) {
		this.originalUnitPrice = originalUnitPrice;
	}

	public String getResaleUnitPrice() {
		return resaleUnitPrice;
	}

	public void setResaleUnitPrice(String resaleUnitPrice) {
		this.resaleUnitPrice = resaleUnitPrice;
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

	public String getImage1() {
		return image1;
	}

	public void setImage1(String image1) {
		this.image1 = image1;
	}

	public String getImage2() {
		return image2;
	}

	public void setImage2(String image2) {
		this.image2 = image2;
	}

	public String getImage3() {
		return image3;
	}

	public void setImage3(String image3) {
		this.image3 = image3;
	}

	public String getImage4() {
		return image4;
	}

	public void setImage4(String image4) {
		this.image4 = image4;
	}

	public String getImage5() {
		return image5;
	}

	public void setImage5(String image5) {
		this.image5 = image5;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public String getCategory3Name() {
		return category3Name;
	}

	public void setCategory3Name(String category3Name) {
		this.category3Name = category3Name;
	}

	public String getCategory3Id() {
		return category3Id;
	}

	public void setCategory3Id(String category3Id) {
		this.category3Id = category3Id;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getQtyBreak1() {
		return qtyBreak1;
	}

	public void setQtyBreak1(String qtyBreak1) {
		this.qtyBreak1 = qtyBreak1;
	}

	public String getPriceBreak1() {
		return priceBreak1;
	}

	public void setPriceBreak1(String priceBreak1) {
		this.priceBreak1 = priceBreak1;
	}

	public String getQtyBreak2() {
		return qtyBreak2;
	}

	public void setQtyBreak2(String qtyBreak2) {
		this.qtyBreak2 = qtyBreak2;
	}

	public String getPriceBreak2() {
		return priceBreak2;
	}

	public void setPriceBreak2(String priceBreak2) {
		this.priceBreak2 = priceBreak2;
	}

	public String getQtyBreak3() {
		return qtyBreak3;
	}

	public void setQtyBreak3(String qtyBreak3) {
		this.qtyBreak3 = qtyBreak3;
	}

	public String getPriceBreak3() {
		return priceBreak3;
	}

	public void setPriceBreak3(String priceBreak3) {
		this.priceBreak3 = priceBreak3;
	}

	public String getQtyBreak4() {
		return qtyBreak4;
	}

	public void setQtyBreak4(String qtyBreak4) {
		this.qtyBreak4 = qtyBreak4;
	}

	public String getPriceBreak4() {
		return priceBreak4;
	}

	public void setPriceBreak4(String priceBreak4) {
		this.priceBreak4 = priceBreak4;
	}

	public String getQtyBreak5() {
		return qtyBreak5;
	}

	public void setQtyBreak5(String qtyBreak5) {
		this.qtyBreak5 = qtyBreak5;
	}

	public String getPriceBreak5() {
		return priceBreak5;
	}

	public void setPriceBreak5(String priceBreak5) {
		this.priceBreak5 = priceBreak5;
	}

	public String getTotalQty() {
		return totalQty;
	}

	public void setTotalQty(String totalQty) {
		this.totalQty = totalQty;
	}

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}


}