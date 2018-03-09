package com.yikuyi.product.vo;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.annotation.Id;

import com.yikuyi.category.model.ProductCategory;
import com.yikuyi.category.model.ProductCategoryAlias;
import com.yikuyi.document.model.DocumentLog;
import com.yikuyi.material.vo.MaterialVo.MaterialVoType;
import com.yikuyi.product.model.Product;
import com.yikuyi.product.model.ProductDocument;
import com.yikuyi.product.model.ProductImage;
import com.yikuyi.product.model.ProductParameter;
import com.yikuyi.product.model.ProductPrice;
import com.yikuyi.product.model.ProductStand;
import com.yikuyi.product.model.ProductUtils;
import com.yikuyi.product.model.Stock;
import com.yikuyi.product.model.VendorSeries;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("RawData")
public class RawData implements Serializable{
	
	private static final long serialVersionUID = 262340049738885245L;

	/**
	 * 仓库类型
	 * @author tongkun@yikuyi.com
	 * @version 1.0.0
	 */
	public enum ProductSourceType {
		/** 现货**/
		STOCK(100l), 
    	/** 原厂**/
		SOURCE(400l);
		
		Long value;
		
		ProductSourceType(Long value){
			this.value = value;
		}
		
		public Long getValue(){
			return this.value;
		}
    }
	
	/**
	 * 更新类型
	 * @author tongkun@yikuyi.com
	 * @version 1.0.0
	 */
	public enum UpdateType {
		/** 美元价格**/
		USD_PRICE(1), 
    	/** 人民币价格**/
		CNY_PRICE(2),
    	/** 基础数据**/
		BASE_DATA(3),
    	/** 库存**/
		STOCK(4),
    	/** 全部**/
		ALL(5);
		
		Integer value;
		
		UpdateType(Integer value){
			this.value = value;
		}
		
		public Integer getValue(){
			return this.value;
		}
    }
	
	public RawData(){
		
	}
	
	public RawData(String manufacturerPartNumber, String manufacturer,String manufacturerOld){
		this.manufacturerPartNumber = manufacturerPartNumber;
		this.manufacturer = manufacturer;
		this.manufacturerOld = manufacturerOld;
	}
	
	@Id
	@ApiModelProperty(value="raw data的id")
	private String id;

	@ApiModelProperty(value="消息来源，crawler：爬虫，sku_file：sku文件上传，spu_file：spu文件上传")
	private String source;

	@ApiModelProperty(value="国家code如US")
	private String countryCode;

	@ApiModelProperty(value="商品的标准描述")
	private String description;

	@ApiModelProperty(value="同步类型")
	private UpdateType type;

	@ApiModelProperty(value="商品的附件")
	private List<ProductDocument> documents;

	@ApiModelProperty(value="商品的图片")
	private List<ProductImage> images;

	@ApiModelProperty(value="品牌")
	private String manufacturer;
	
	@ApiModelProperty(value="原始品牌")
	private String manufacturerOld;

	@ApiModelProperty(value="型号")
	private String manufacturerPartNumber;

	@ApiModelProperty(value="最小起定量")
	private Integer minimumQuantity;
	
	@ApiModelProperty(value="标准包装数量")
	private Integer spq;
	
	@ApiModelProperty(value="封装单位")
	private String packagingUnit;
	
	@ApiModelProperty(value="最小订单金额")
	private String mov;
	
	@ApiModelProperty(value="spu的唯一标识，值为型号+品牌")
	private String spuId;

	@ApiModelProperty(value="封装")
	private String packaging;

	@ApiModelProperty(value="商品参数")
	private List<ProductParameter> parameters;

	@ApiModelProperty(value="商品状态")
	private String partStatus;

	@ApiModelProperty(value="价格")
	private List<ProductPrice> prices;

	@ApiModelProperty(value = "成本价")
	private List<ProductPrice> costPrices; // real cost
	@ApiModelProperty(value = "销售价")
	private List<ProductPrice> resalePrices; // original resale  price
	@ApiModelProperty(value = "特价")
	private List<ProductPrice> specialResaleprices; // special resale price
	
	@ApiModelProperty(value="rohs")
	private String rohs;

	@ApiModelProperty(value="sku的id")
	private String skuId;

	@ApiModelProperty(value="库存")
	private List<Stock> stocks;

	@ApiModelProperty(value="单位")
	private String unit;

	@ApiModelProperty(value="供应商分类")
	private List<ProductCategory> vendorCategories;

	@ApiModelProperty(value="供应商url")
	private String vendorDetailsLink;

	@ApiModelProperty(value="供应商id")
	private String vendorId;

	@ApiModelProperty(value="供应商name")
	private String vendorName;

	@ApiModelProperty(value="可以用于创建标准数据")
	private Boolean cantCreateStand;

	@ApiModelProperty(value="快速查找key")
	private String quickFindKey;

	@ApiModelProperty(value="供应商簇")
	private VendorSeries vendorSeries;
	
	@ApiModelProperty(value="批次号")
	private String processId;
	
	@ApiModelProperty(value="仓库")
	private String region;

	@ApiModelProperty(value="币种")
	private String currencyCode;

	@ApiModelProperty(value="行号")
	private Long lineNo;
	
	@ApiModelProperty(value="excel,sheet下标")
	private Integer sheetIndex;
	
	@ApiModelProperty(value="同步数据过程错误信息")
	private String errorMsg;
	
	@ApiModelProperty(value="同步数据过程错误信息")
	private String from;
	
	@ApiModelProperty(value = "香港最小交期")	
	private String minLeadTimeHK;
	
	@ApiModelProperty(value = "香港最大交期")
	private String maxLeadTimeHK;
	
	@ApiModelProperty(value = "香港工厂交期")
	private String minFactoryLeadTimeHK;
	private String maxFactoryLeadTimeHK;
	
	@ApiModelProperty(value = "国内最小交期")	
	private String minLeadTimeML;
	
	@ApiModelProperty(value = "国内最大交期")
	private String maxLeadTimeML;
	
	@ApiModelProperty(value = "国内工厂交期")
	private String minFactoryLeadTimeML;
	private String maxFactoryLeadTimeML;
	
	@ApiModelProperty(value = "操作类型")
	private MaterialVoType operateType;
	
	@ApiModelProperty(value="商品备注")	
	private String remark;
	
	@ApiModelProperty(value="批号")	
	private String dateCode;
	
	@ApiModelProperty(value="限制物料  N表示否、P表示需特殊包装、F表示需特殊文件")
	private String restrictMaterialType;
	
	@ApiModelProperty(value="数据类型")	
	private String costType;
	
	@ApiModelProperty(value="失效日期")	
	private String expiryDate;
	
	@ApiModelProperty(value="创建人")
	private String userId;
	@ApiModelProperty(value="创建人名")
	private String userName;

	@ApiModelProperty(value="是否是特殊spu")
	private Boolean specialSpu;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ProductDocument> getDocuments() {
		return documents;
	}

	public void setDocuments(List<ProductDocument> documents) {
		this.documents = documents;
	}

	public List<ProductImage> getImages() {
		return images;
	}

	public void setImages(List<ProductImage> images) {
		this.images = images;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	 

	public String getManufacturerOld() {
		return manufacturerOld;
	}

	public void setManufacturerOld(String manufacturerOld) {
		this.manufacturerOld = manufacturerOld;
	}

	public String getManufacturerPartNumber() {
		return manufacturerPartNumber;
	}

	public void setManufacturerPartNumber(String manufacturerPartNumber) {
		this.manufacturerPartNumber = manufacturerPartNumber;
	}

	public Integer getMinimumQuantity() {
		return minimumQuantity;
	}

	public void setMinimumQuantity(Integer minimumQuantity) {
		this.minimumQuantity = minimumQuantity;
	}

	public List<ProductParameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<ProductParameter> parameters) {
		this.parameters = parameters;
	}

	public String getPartStatus() {
		return partStatus;
	}

	public void setPartStatus(String partStatus) {
		this.partStatus = partStatus;
	}

	public List<ProductPrice> getPrices() {
		return prices;
	}

	public void setPrices(List<ProductPrice> prices) {
		this.prices = prices;
	}
	
	public String getRohs() {
		return rohs;
	}

	public void setRohs(String rohs) {
		this.rohs = rohs;
	}

	public String getSpuId() {
		return spuId;
	}

	public void setSpuId(String spuId) {
		this.spuId = spuId;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public List<Stock> getStocks() {
		return stocks;
	}

	public void setStocks(List<Stock> stocks) {
		this.stocks = stocks;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public List<ProductCategory> getVendorCategories() {
		return vendorCategories;
	}

	public void setVendorCategories(List<ProductCategory> vendorCategories) {
		this.vendorCategories = vendorCategories;
	}

	public String getVendorDetailsLink() {
		return vendorDetailsLink;
	}

	public void setVendorDetailsLink(String vendorDetailsLink) {
		this.vendorDetailsLink = vendorDetailsLink;
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

	public VendorSeries getVendorSeries() {
		return vendorSeries;
	}

	public void setVendorSeries(VendorSeries vendorSeries) {
		this.vendorSeries = vendorSeries;
	}

	public String getQuickFindKey() {
		return quickFindKey;
	}

	public void setQuickFindKey(String quickFindKey) {
		this.quickFindKey = quickFindKey;
	}
	
	public Boolean getCantCreateStand() {
		return cantCreateStand;
	}

	public void setCantCreateStand(Boolean cantCreateStand) {
		this.cantCreateStand = cantCreateStand;
	}
	

	public String getPackaging() {
		return packaging;
	}

	public void setPackaging(String packaging) {
		this.packaging = packaging;
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
	
	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}
	
	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public UpdateType getType() {
		return type;
	}

	public void setType(UpdateType type) {
		this.type = type;
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

	public Boolean getSpecialSpu() {
		return specialSpu;
	}

	public void setSpecialSpu(Boolean specialSpu) {
		this.specialSpu = specialSpu;
	}

	public void setErrorMsg(Exception e){
		if(null != e){
			StringBuilder builder = new StringBuilder();
			builder.append(e.toString()+",");
			for(StackTraceElement el : e.getStackTrace()){
				if((builder.length() + el.toString().length()) > 250){
					break;
				}
				builder.append(el.toString()+",");
			}
			this.errorMsg = DocumentLog.getSizeLengthMsg(builder.toString());
		}
	}
	
	public RawData setErrorMsgRst(String errorMsg) {
		this.errorMsg = DocumentLog.getSizeLengthMsg(errorMsg);
		return this;
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
	
	public Integer getSheetIndex() {
		return sheetIndex;
	}

	public void setSheetIndex(Integer sheetIndex) {
		this.sheetIndex = sheetIndex;
	}
	
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}
	
	public String getMinLeadTimeHK() {
		return minLeadTimeHK;
	}

	public void setMinLeadTimeHK(String minLeadTimeHK) {
		this.minLeadTimeHK = minLeadTimeHK;
	}

	public String getMaxLeadTimeHK() {
		return maxLeadTimeHK;
	}

	public void setMaxLeadTimeHK(String maxLeadTimeHK) {
		this.maxLeadTimeHK = maxLeadTimeHK;
	}

	public String getMinLeadTimeML() {
		return minLeadTimeML;
	}

	public void setMinLeadTimeML(String minLeadTimeML) {
		this.minLeadTimeML = minLeadTimeML;
	}

	public String getMaxLeadTimeML() {
		return maxLeadTimeML;
	}

	public void setMaxLeadTimeML(String maxLeadTimeML) {
		this.maxLeadTimeML = maxLeadTimeML;
	}

	public String getMinFactoryLeadTimeHK() {
		return minFactoryLeadTimeHK;
	}

	public void setMinFactoryLeadTimeHK(String minFactoryLeadTimeHK) {
		this.minFactoryLeadTimeHK = minFactoryLeadTimeHK;
	}

	public String getMaxFactoryLeadTimeHK() {
		return maxFactoryLeadTimeHK;
	}

	public void setMaxFactoryLeadTimeHK(String maxFactoryLeadTimeHK) {
		this.maxFactoryLeadTimeHK = maxFactoryLeadTimeHK;
	}

	public String getMinFactoryLeadTimeML() {
		return minFactoryLeadTimeML;
	}

	public void setMinFactoryLeadTimeML(String minFactoryLeadTimeML) {
		this.minFactoryLeadTimeML = minFactoryLeadTimeML;
	}

	public String getMaxFactoryLeadTimeML() {
		return maxFactoryLeadTimeML;
	}

	public void setMaxFactoryLeadTimeML(String maxFactoryLeadTimeML) {
		this.maxFactoryLeadTimeML = maxFactoryLeadTimeML;
	}

	public MaterialVoType getOperateType() {
		return operateType;
	}

	public void setOperateType(MaterialVoType operateType) {
		this.operateType = operateType;
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

	public String getRestrictMaterialType() {
		return restrictMaterialType;
	}

	public void setRestrictMaterialType(String restrictMaterialType) {
		this.restrictMaterialType = restrictMaterialType;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPackagingUnit() {
		return packagingUnit;
	}

	public void setPackagingUnit(String packagingUnit) {
		this.packagingUnit = packagingUnit;
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
		if(obj instanceof Product){
			Product prod = (Product)obj;
			if(this.getQuickFindKey()!=null&&prod.getQuickFindKey()!=null){
				//标准查找
				if(this.getQuickFindKey().equals(prod.getQuickFindKey())){
					return true;
				}
				//非标准查找
				String sourceId = (this.getVendorId()==null?"100":(this.getVendorId()+"-100"));
				for(int i = 0;i < this.getStocks().size();i++){
					String source = this.getStocks().get(i).getSource();
					if(!"100".equals(source)){
						sourceId = source;
						break;
					}
				}
				if(ProductUtils.getProductQuickFindKey(this, this.getManufacturer(), sourceId).equals(prod.getQuickFindKey())){
					return true;
				}
			}
			return false;
		}else if(obj instanceof ProductStand){
			ProductStand stand = (ProductStand)obj;
			boolean eqBrand = false;
			boolean eqPartNo = false;
			if(this.getSpuId()!=null&&this.getSpuId().equals(stand.getSpuId())){
				eqBrand = true;
				eqPartNo = true;
			}
			return eqBrand&&eqPartNo;
		}
		return super.equals(obj);
	}

	/**
	 * 获取raw下供应商分类对应redis缓存key
	 * @return
	 */
	public static Set<String>  getVendorCategoryRedisKey(List<ProductCategory> vendorCategories){
		if(org.springframework.util.CollectionUtils.isEmpty(vendorCategories)){
			return Collections.emptySet();
		}
		Set<String> cahceKey = new HashSet<>();
		// 分类
		ProductCategoryAlias alias = new ProductCategoryAlias();
		ProductCategoryAlias aliasLast = new ProductCategoryAlias();
		Integer minLevel = -1;
		for (ProductCategory cate : vendorCategories) {
			Integer level = cate.getLevel();
			if(level==null)
				continue;
			if (minLevel<level) {
				alias.setLevel1(StringUtils.isEmpty(alias.getLevel2()) ? alias.getLevel2() : alias.getLevel2().toUpperCase());
				alias.setLevel2(cate.getName().toUpperCase());
				aliasLast.setLevel2(cate.getName().toUpperCase());
				minLevel = level;
			}
		}
		cahceKey.add(String.valueOf(alias.hashCode()));
		cahceKey.add(String.valueOf(aliasLast.hashCode()));
		return cahceKey;
	}
}
