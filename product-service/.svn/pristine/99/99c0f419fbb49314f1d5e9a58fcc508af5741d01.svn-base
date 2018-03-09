package com.yikuyi.strategy.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
/**
 * 包邮草稿商品信息
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@Document(collection = "strategy_product_draft")
public class StrategyProductDraft implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3255180624665816373L;

	@Id
	@ApiModelProperty(value = "主键id")
	@JsonProperty("id")
	private String id;
	
	@ApiModelProperty(value = "包邮Id")
	private String strategyId;
	
	@ApiModelProperty(value = "商品id")
	private String productId;
	
	@ApiModelProperty(value = "原来商品id")
	private String oldProductId;
	
	@ApiModelProperty(value = "型号")
	private String mpn;
	
	@ApiModelProperty(value = "型号链接")
	private String mpnUrl;
	
	@ApiModelProperty(value = "制造商id")
	private String brandId;
	
	@ApiModelProperty(value = "制造商")
	private String brandName;
	
	@ApiModelProperty(value = "仓库id")
	private String sourceId;
	
	@ApiModelProperty(value = "仓库名称")
	private String sourceName;
	
	@ApiModelProperty(value = "供应商(即分销商)Id")
	private String vendorId;
	
	@ApiModelProperty(value = "供应商名称(即分销商名称)")
	private String vendorName;
		
	@ApiModelProperty(value = "备注")
	private String remark;
	
	@ApiModelProperty(value = "上传批次号")
	private String documentId;
	
	@ApiModelProperty(value = "状态")
	private ProductStatus productStatus;
	
	@ApiModelProperty(value = "商品限购数量")
	private Integer limitNum;
	
	@ApiModelProperty(value = "商品库存")
	private Integer qty;
	
	@ApiModelProperty(value = "商品起订量")
	private Integer moq;
	
	@ApiModelProperty(value="数量区间1")
	private Integer qtyBreak1;
	
	@ApiModelProperty(value="限时价1")
	private Double priceBreak1;
	
	@ApiModelProperty(value="数量区间2")
    private Integer qtyBreak2;
	
	@ApiModelProperty(value="限时价2")
	private Double priceBreak2;
	
	@ApiModelProperty(value="数量区间3")
    private Integer qtyBreak3;
	
	@ApiModelProperty(value="限时价3")
	private Double priceBreak3;
	
	@ApiModelProperty(value="数量区间4")
    private Integer qtyBreak4;
	
	@ApiModelProperty(value="限时价4")
	private Double priceBreak4;
	
	@ApiModelProperty(value="数量区间5")
    private Integer qtyBreak5;
	
	@ApiModelProperty(value="限时价5")
	private Double priceBreak5;
	
	@ApiModelProperty(value="图片1")
	private String image1;
	
	@ApiModelProperty(value="图片2")
	private String image2;
	
	@ApiModelProperty(value="图片3")
	private String image3;
	
	@ApiModelProperty(value="图片4")
	private String image4;
	
	@ApiModelProperty(value="图片5")
	private String image5;
	
	@ApiModelProperty(value = "创建者")
	private String creator;
	
	@ApiModelProperty(value = "创建者名称")
	private String creatorName;
	
	@ApiModelProperty(value="创建时间戳")
	private String createdTimeMillis;
	
	@ApiModelProperty(value="更新时间戳")
	private String updatedTimeMillis;
	
	@ApiModelProperty(value = "更新者")
	private String lastUpdateUser;
	
	@ApiModelProperty(value = "更新者名称")
	private String lastUpdateUserName;
	
	public enum ProductStatus {
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
		DETETED
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStrategyId() {
		return strategyId;
	}

	public void setStrategyId(String strategyId) {
		this.strategyId = strategyId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public String getOldProductId() {
		return oldProductId;
	}

	public void setOldProductId(String oldProductId) {
		this.oldProductId = oldProductId;
	}

	public String getMpn() {
		return mpn;
	}

	public void setMpn(String mpn) {
		this.mpn = mpn;
	}

	public String getMpnUrl() {
		return mpnUrl;
	}

	public void setMpnUrl(String mpnUrl) {
		this.mpnUrl = mpnUrl;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public ProductStatus getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(ProductStatus productStatus) {
		this.productStatus = productStatus;
	}
	
	public Integer getLimitNum() {
		return limitNum;
	}

	public void setLimitNum(Integer limitNum) {
		this.limitNum = limitNum;
	}
	
	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}
	
	public Integer getMoq() {
		return moq;
	}

	public void setMoq(Integer moq) {
		this.moq = moq;
	}

	public Integer getQtyBreak1() {
		return qtyBreak1;
	}

	public void setQtyBreak1(Integer qtyBreak1) {
		this.qtyBreak1 = qtyBreak1;
	}

	public Double getPriceBreak1() {
		return priceBreak1;
	}

	public void setPriceBreak1(Double priceBreak1) {
		this.priceBreak1 = priceBreak1;
	}

	public Integer getQtyBreak2() {
		return qtyBreak2;
	}

	public void setQtyBreak2(Integer qtyBreak2) {
		this.qtyBreak2 = qtyBreak2;
	}

	public Double getPriceBreak2() {
		return priceBreak2;
	}

	public void setPriceBreak2(Double priceBreak2) {
		this.priceBreak2 = priceBreak2;
	}

	public Integer getQtyBreak3() {
		return qtyBreak3;
	}

	public void setQtyBreak3(Integer qtyBreak3) {
		this.qtyBreak3 = qtyBreak3;
	}

	public Double getPriceBreak3() {
		return priceBreak3;
	}

	public void setPriceBreak3(Double priceBreak3) {
		this.priceBreak3 = priceBreak3;
	}

	public Integer getQtyBreak4() {
		return qtyBreak4;
	}

	public void setQtyBreak4(Integer qtyBreak4) {
		this.qtyBreak4 = qtyBreak4;
	}

	public Double getPriceBreak4() {
		return priceBreak4;
	}

	public void setPriceBreak4(Double priceBreak4) {
		this.priceBreak4 = priceBreak4;
	}

	public Integer getQtyBreak5() {
		return qtyBreak5;
	}

	public void setQtyBreak5(Integer qtyBreak5) {
		this.qtyBreak5 = qtyBreak5;
	}

	public Double getPriceBreak5() {
		return priceBreak5;
	}

	public void setPriceBreak5(Double priceBreak5) {
		this.priceBreak5 = priceBreak5;
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

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getCreatedTimeMillis() {
		return createdTimeMillis;
	}

	public void setCreatedTimeMillis(String createdTimeMillis) {
		this.createdTimeMillis = createdTimeMillis;
	}

	public String getUpdatedTimeMillis() {
		return updatedTimeMillis;
	}

	public void setUpdatedTimeMillis(String updatedTimeMillis) {
		this.updatedTimeMillis = updatedTimeMillis;
	}

	public String getLastUpdateUser() {
		return lastUpdateUser;
	}

	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}

	public String getLastUpdateUserName() {
		return lastUpdateUserName;
	}

	public void setLastUpdateUserName(String lastUpdateUserName) {
		this.lastUpdateUserName = lastUpdateUserName;
	}

}