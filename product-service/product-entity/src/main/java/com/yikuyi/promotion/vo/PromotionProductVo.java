package com.yikuyi.promotion.vo;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import com.yikuyi.product.model.ProductPrice;
import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel("PromotionProductVo")
public class PromotionProductVo  extends AbstractEntity{
	
	private static final DecimalFormat decimalFormat = new DecimalFormat("###################.#####");
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4816330141886344760L;

	@ApiModelProperty(value="淇冮攢妯″潡鍟嗗搧id")
	private String promoModuleProductId;
	
	@ApiModelProperty(value="淇冮攢妯″潡id")
	private String promoModuleId;
	
	@ApiModelProperty(value="淇冮攢娲诲姩id")
	private String promotionId;
	
	@ApiModelProperty(value="鍟嗗搧id")
	private String productId;
	
	@ApiModelProperty(value="鍒堕�犲晢鍨嬪彿")
	private String manufacturerPartNumber;
	
	@ApiModelProperty(value="鍒堕�犲晢")
	private String manufacturer;
	
	@ApiModelProperty(value="浠撳簱Id")
	private String sourceId;
	
	@ApiModelProperty(value="浠撳簱鍚嶇О")
	private String sourceName;
	
	@ApiModelProperty(value="渚涘簲鍟咺d")
	private String vendorId;
	
	@ApiModelProperty(value="渚涘簲鍟嗗悕绉�")
	private String vendorName;
	
	@ApiModelProperty(value="鎶樻墸")
	private float discount;
	
	@ApiModelProperty(value="甯佺")
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
	
	@ApiModelProperty(value="鍘熶环")
	private String originalUnitPrice;
	
	@ApiModelProperty(value="褰撳墠閿�鍞崟浠�")
	private String resaleUnitPrice;
	
	@ApiModelProperty(value="鏍囬")
	private String title;
	
	@ApiModelProperty(value="sub鏍囬")
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
	
	@ApiModelProperty(value="鎬诲簱瀛樻暟")
	private long totalQty;
	
	@ApiModelProperty(value="褰撳墠搴撳瓨鏁�")
	private long qty;
	
	@ApiModelProperty(value="娆″皬绫诲悕绉�")
	private String category3Name;
	
	@ApiModelProperty(value="娆″皬绫籌d")
	private String category3Id;
		
	@ApiModelProperty(value="鏂囦欢涓婁紶鎵规鍙�")
	private String processId;
	
	@ApiModelProperty(value="閿欒鎻忚堪")
	private String errorDescription;

	private ModuleProductStatus status;
	@ApiModelProperty(value = "原价价格")
	private List<ProductPrice> originalResalePrices;
	
	@ApiModelProperty(value = "销售价格")
	private List<ProductPrice> prices;
	
	@ApiModelProperty(value = "闃舵")
	private List<String> qtyBreak;
	
	@ApiModelProperty(value = "闃舵浠锋牸")
	private List<String> priceBreak;
	@ApiModelProperty(value = "spu")
	private Map<String,String> spu; // special resale price
	
	@ApiModelProperty(value="新增id")
	private String id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, String> getSpu() {
		return spu;
	}

	public void setSpu(Map<String, String> spu) {
		this.spu = spu;
	}

	public List<ProductPrice> getPrices() {
		return prices;
	}

	public void setPrices(List<ProductPrice> prices) {
		this.prices = prices;
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
		this.priceBreak1 = decimalFormat.format(Double.parseDouble(priceBreak1));
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
		this.priceBreak2 = decimalFormat.format(Double.parseDouble(priceBreak2));
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
		this.priceBreak3 = decimalFormat.format(Double.parseDouble(priceBreak3));
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
		this.priceBreak4 = decimalFormat.format(Double.parseDouble(priceBreak4));
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
		this.priceBreak5 = decimalFormat.format(Double.parseDouble(priceBreak5));
	}
	public List<String> getQtyBreak() {
		return qtyBreak;
	}

	public void setQtyBreak(List<String> qtyBreak) {
		this.qtyBreak = qtyBreak;
	}

	public List<String> getPriceBreak() {
		return priceBreak;
	}

	public void setPriceBreak(List<String> priceBreak) {
		this.priceBreak = priceBreak;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public List<ProductPrice> getOriginalResalePrices() {
		return originalResalePrices;
	}

	public void setOriginalResalePrices(List<ProductPrice> originalResalePrices) {
		this.originalResalePrices = originalResalePrices;
	}



	public enum ModuleProductStatus {
		// 鏃犳晥
		UNABLE,
		// 鏈夋晥
		ENABLE
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

	public long getTotalQty() {
		return totalQty;
	}

	public void setTotalQty(long totalQty) {
		this.totalQty = totalQty;
	}

	public long getQty() {
		return qty;
	}

	public void setQty(long qty) {
		this.qty = qty;
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

}
