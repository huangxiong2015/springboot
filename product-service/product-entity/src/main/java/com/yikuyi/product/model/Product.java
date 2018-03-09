package com.yikuyi.product.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yikuyi.category.model.ProductCategory;
import com.yikuyi.product.vo.RawData;
import com.yikuyi.strategy.vo.LimitedPurchaseVo;
import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModelProperty;
/**
 * 商品信息
 * 
 * @author zr.wujiajun
 * @since 2016-12-07
 */
@Document(collection = "product")
public class Product extends AbstractEntity {

	private static final long serialVersionUID = -5960246695330219578L;


	/**
	 * 0:删除
	 * 1:有效
	 * 2:停用
	 */
	@ApiModelProperty(value="商品状态")
	private Integer status; 
	
	// 1. 产品信息
	// 1.1 基本信息
	@Id
	@ApiModelProperty(value = "商品ID")
	@JsonProperty("id")
	private String id;
	
	@ApiModelProperty(value = "商品spu实体信息")
	private ProductStand spu;

	// 2 SKU信息
	// 2.1 vendor信息
	@ApiModelProperty(value = "供应商物料编码")
	private String skuId;
	
	// private List<Map<String, ProductCategory>> vendorCategories;

	@ApiModelProperty(value = "供应商名称（digikey，mouser等）")
	private String vendorName;
	
	@ApiModelProperty(value = "供应商名称,显示供应商名称而不是编码")
	private String oldVendorName;
	
	@ApiModelProperty(value = "供应商id")
	private String vendorId;
	@ApiModelProperty(value = "供应商详情页链接")
	private String vendorDetailsLink;
	
	@ApiModelProperty(value = "是否显示供应商详情页(Y/N)")
	private String isVendorDetail;
	
	@ApiModelProperty(value = "是否显示真实供应商")
	private boolean showVendor;
	@ApiModelProperty(value = "series的链接和值")
	private VendorSeries vendorSeries;
	@ApiModelProperty(value = "包装")
	private String packaging;
	@ApiModelProperty(value = "包装单位")
	private String packagingUnit;
	@ApiModelProperty(value = "国家码")
	private String countryCode;

	// 2.2 库存信息
	@ApiModelProperty(value = "最小交易量  起定量")
	private Integer minimumQuantity;
	@ApiModelProperty(value = "库存")
	private Long qty;
	
	@ApiModelProperty(value="标准包装数量")
	private Integer spq;
	
	@ApiModelProperty(value="最小订单金额")
	private String mov;
	
	// 2.3 价格信息
	@ApiModelProperty(value = "币种")
	private String currencyCode;
	@ApiModelProperty(value = "单价，和梯度价格中的1个是冗余的")
	private String unitPrice;
	@ApiModelProperty(value = "单位")
	private String unit;
	@ApiModelProperty(value = "仓库id")	
	private String sourceId;
	
	@ApiModelProperty(value = "仓库名称")
	private String sourceName;

	@ApiModelProperty(value = "交期")	
	private String leadTime;
	
	@ApiModelProperty(value = "最小交期")	
	private Integer minLeadTime;
	@ApiModelProperty(value = "最大交期")
	private Integer maxLeadTime;
	
	@ApiModelProperty(value = "工厂交期")	
	private Integer factoryLeadTime;
	
	@ApiModelProperty(value = "国内最小交期")	
	private Integer minLeadTimeML;
	@ApiModelProperty(value = "国内最大交期")
	private Integer maxLeadTimeML;
	
	@ApiModelProperty(value = "国内工厂交期")	
	private Integer minFactoryLeadTimeML;
	private Integer maxFactoryLeadTimeML;
	
	@ApiModelProperty(value = "香港最小交期")	
	private Integer minLeadTimeHK;
	@ApiModelProperty(value = "香港最大交期")
	private Integer maxLeadTimeHK;
	
	@ApiModelProperty(value = "香港工厂交期")	
	private Integer minFactoryLeadTimeHK;
	private Integer maxFactoryLeadTimeHK;
	
	
	@ApiModelProperty(value = "快速查找id")	
	private String quickFindKey;
	@ApiModelProperty(value = "商店id")
	private Long storeId;
	@ApiModelProperty(value = "梯度价格")
	private List<ProductPrice> prices; // orginal real Cost
	@ApiModelProperty(value = "成本价")
	private List<ProductPrice> costPrices; // real cost
	@ApiModelProperty(value = "销售价")
	private List<ProductPrice> resalePrices; // original resale  price
	@ApiModelProperty(value = "特价")
	private List<ProductPrice> specialResaleprices; // special resale price
	@ApiModelProperty(value = "批量处理的批次id")
	private String processId;
	@ApiModelProperty(value = "状态")
	private String partStatus;
	
	@ApiModelProperty(value="创建时间戳")
	private String createdTimeMillis;
	
	@ApiModelProperty(value="更新时间戳")
	private String updatedTimeMillis;
	
	@ApiModelProperty(value="供应商分类")	
	private List<ProductCategory> vendorCategories;

	@ApiModelProperty(value="管制")	
	private String saleControl;
	
	@ApiModelProperty(value="商品备注")	
	private String remark;
	
	@ApiModelProperty(value="批号")	
	private String dateCode;
	
	@ApiModelProperty(value="是否校验供应商MOV(Y/N)")
	private String vendorMovStatus;
	
	@ApiModelProperty(value="是否校验商品MOV(Y/N)")
	private String skuMovStatus;
	
	@ApiModelProperty(value="数据类型（Inventory/Bookcost/SPR）")	
	private String costType;
	
	@ApiModelProperty(value="失效日期")	
	private String expiryDate;
	
	@ApiModelProperty(value="限购信息")
	private LimitedPurchaseVo limitedPurchaseInfo;
	
	@ApiModelProperty(value="限购活动结束时间")
	private Long expirationTime;
	
	@ApiModelProperty(value="原始商品id")
	private String originalId;
	
	public String getIsVendorDetail() {
		return isVendorDetail;
	}

	public void setIsVendorDetail(String isVendorDetail) {
		this.isVendorDetail = isVendorDetail;
	}

	/**
	 * expired：价格失效
	 * null:有效
	 */
	@ApiModelProperty(value="价格状态")	
	private String priceStatus;
	
	public String getOldVendorName() {
		return oldVendorName;
	}

	public void setOldVendorName(String oldVendorName) {
		this.oldVendorName = oldVendorName;
	}

	public String getVendorMovStatus() {
		return vendorMovStatus;
	}

	public void setVendorMovStatus(String vendorMovStatus) {
		this.vendorMovStatus = vendorMovStatus;
	}

	public String getSkuMovStatus() {
		return skuMovStatus;
	}

	public void setSkuMovStatus(String skuMovStatus) {
		this.skuMovStatus = skuMovStatus;
	}

	public String getCostType() {
		return costType;
	}

	public void setCostType(String costType) {
		this.costType = costType;
	}


	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDateCode() {
		return dateCode;
	}

	public void setDateCode(String dateCode) {
		this.dateCode = dateCode;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public ProductStand getSpu() {
		return spu;
	}

	public void setSpu(ProductStand spu) {
		this.spu = spu;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorDetailsLink() {
		return vendorDetailsLink;
	}

	public void setVendorDetailsLink(String vendorDetailsLink) {
		this.vendorDetailsLink = vendorDetailsLink;
	}


	public String getPackaging() {
		return packaging;
	}

	public void setPackaging(String packaging) {
		this.packaging = packaging;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public Long getQty() {
		return qty;
	}

	public void setQty(Long qty) {
		this.qty = qty;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public List<ProductPrice> getPrices() {
		return prices;
	}

	public void setPrices(List<ProductPrice> prices) {
		this.prices = prices;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getQuickFindKey() {
		return quickFindKey;
	}

	public void setQuickFindKey(String quickFindKey) {
		this.quickFindKey = quickFindKey;
	}

	public Integer getMinimumQuantity() {
		return minimumQuantity;
	}

	public void setMinimumQuantity(Integer minimumQuantity) {
		this.minimumQuantity = minimumQuantity;
	}	
	
	public VendorSeries getVendorSeries() {
		return vendorSeries;
	}

	public void setVendorSeries(VendorSeries vendorSeries) {
		this.vendorSeries = vendorSeries;
	}

	public List<ProductPrice> getCostPrices() {
		return costPrices;
	}

	public void setCostPrices(List<ProductPrice> costPrices) {
		this.costPrices = costPrices;
	}

	public List<ProductPrice> getResalePrices() {
		return resalePrices;
	}

	public void setResalePrices(List<ProductPrice> resalePrices) {
		this.resalePrices = resalePrices;
	}

	public List<ProductPrice> getSpecialResaleprices() {
		return specialResaleprices;
	}

	public void setSpecialResaleprices(List<ProductPrice> specialResaleprices) {
		this.specialResaleprices = specialResaleprices;
	}
	
	public String getLeadTime() {
		return leadTime;
	}

	public void setLeadTime(String leadTime) {
		this.leadTime = leadTime;
	}

	public Integer getMinLeadTimeML() {
		return minLeadTimeML;
	}

	public void setMinLeadTimeML(Integer minLeadTimeML) {
		this.minLeadTimeML = minLeadTimeML;
	}

	public Integer getMaxLeadTimeML() {
		return maxLeadTimeML;
	}

	public void setMaxLeadTimeML(Integer maxLeadTimeML) {
		this.maxLeadTimeML = maxLeadTimeML;
	}

	public Integer getMinLeadTimeHK() {
		return minLeadTimeHK;
	}

	public void setMinLeadTimeHK(Integer minLeadTimeHK) {
		this.minLeadTimeHK = minLeadTimeHK;
	}

	public Integer getMaxLeadTimeHK() {
		return maxLeadTimeHK;
	}

	public void setMaxLeadTimeHK(Integer maxLeadTimeHK) {
		this.maxLeadTimeHK = maxLeadTimeHK;
	}
	
	public Integer getMinFactoryLeadTimeML() {
		return minFactoryLeadTimeML;
	}

	public void setMinFactoryLeadTimeML(Integer minFactoryLeadTimeML) {
		this.minFactoryLeadTimeML = minFactoryLeadTimeML;
	}

	public Integer getMaxFactoryLeadTimeML() {
		return maxFactoryLeadTimeML;
	}

	public void setMaxFactoryLeadTimeML(Integer maxFactoryLeadTimeML) {
		this.maxFactoryLeadTimeML = maxFactoryLeadTimeML;
	}

	public Integer getMinFactoryLeadTimeHK() {
		return minFactoryLeadTimeHK;
	}

	public void setMinFactoryLeadTimeHK(Integer minFactoryLeadTimeHK) {
		this.minFactoryLeadTimeHK = minFactoryLeadTimeHK;
	}

	public Integer getMaxFactoryLeadTimeHK() {
		return maxFactoryLeadTimeHK;
	}

	public void setMaxFactoryLeadTimeHK(Integer maxFactoryLeadTimeHK) {
		this.maxFactoryLeadTimeHK = maxFactoryLeadTimeHK;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getPartStatus() {
		return partStatus;
	}

	public void setPartStatus(String partStatus) {
		this.partStatus = partStatus;
	}
	
	public List<ProductCategory> getVendorCategories() {
		return vendorCategories;
	}

	public void setVendorCategories(List<ProductCategory> vendorCategories) {
		this.vendorCategories = vendorCategories;
	}
	
	public Integer getSpq() {
		return spq;
	}

	public void setSpq(Integer spq) {
		this.spq = spq;
	}

	public String getMov() {
		return mov;
	}

	public void setMov(String mov) {
		this.mov = mov;
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
	
	public Integer getMinLeadTime() {
		return minLeadTime;
	}

	public void setMinLeadTime(Integer minLeadTime) {
		this.minLeadTime = minLeadTime;
	}

	public Integer getMaxLeadTime() {
		return maxLeadTime;
	}

	public void setMaxLeadTime(Integer maxLeadTime) {
		this.maxLeadTime = maxLeadTime;
	}

	public Integer getFactoryLeadTime() {
		return factoryLeadTime;
	}

	public void setFactoryLeadTime(Integer factoryLeadTime) {
		this.factoryLeadTime = factoryLeadTime;
	}
	
	public String getSaleControl() {
		return saleControl;
	}

	public void setSaleControl(String saleControl) {
		this.saleControl = saleControl;
	}

	public boolean isShowVendor() {
		return showVendor;
	}

	public void setShowVendor(boolean showVendor) {
		this.showVendor = showVendor;
	}
	
	public String getPackagingUnit() {
		return packagingUnit;
	}

	public void setPackagingUnit(String packagingUnit) {
		this.packagingUnit = packagingUnit;
	}
	
	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	
	public String getPriceStatus() {
		return priceStatus;
	}

	public void setPriceStatus(String priceStatus) {
		this.priceStatus = priceStatus;
	}

	public LimitedPurchaseVo getLimitedPurchaseInfo() {
		return limitedPurchaseInfo;
	}


	public void setLimitedPurchaseInfo(LimitedPurchaseVo limitedPurchaseInfo) {
		this.limitedPurchaseInfo = limitedPurchaseInfo;
	}

	public Long getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(Long expirationTime) {
		this.expirationTime = expirationTime;
	}

	public String getOriginalId() {
		return originalId;
	}

	public void setOriginalId(String originalId) {
		this.originalId = originalId;
	}

	@Override
	public int hashCode() {
		if(this.getQuickFindKey()!=null)
			return this.getQuickFindKey().hashCode();
		else
			return super.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj instanceof RawData){
			RawData data = (RawData)obj;
			if(this.getQuickFindKey()!=null&&data.getQuickFindKey()!=null){
				//标准查找
				if(this.getQuickFindKey().equals(data.getQuickFindKey())){
					return true;
				}
				//非标准查找
				if(ProductUtils.getProductQuickFindKey(this.getSkuId(), this.getSpu().getManufacturer(), this.getSourceId()).equals(data.getQuickFindKey())){
					return true;
				}
			}
		}
		return super.equals(obj);
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	 
}