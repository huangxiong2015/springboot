package com.yikuyi.product.model;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yikuyi.category.model.ProductCategory;
import com.yikuyi.product.vo.RawData;
import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModelProperty;

@Document(collection = "product_stand")
public class ProductStand extends AbstractEntity {

	private static final long serialVersionUID = -5635592869503794873L;
	
	@ApiModelProperty(value = "ID")
	@Id
	@JsonProperty("id")
	private String id;
	@ApiModelProperty(value = "spu的唯一标识")
	private String spuId;
	@ApiModelProperty(value = "制造商名称")
	private String manufacturer;
	@ApiModelProperty(value = "制造商ID")
	private Integer manufacturerId;
	@ApiModelProperty(value = "型号")
	private String manufacturerPartNumber;
	@ApiModelProperty(value = "品牌简称")
	private String manufacturerShort;
	@ApiModelProperty(value = "描述信息")
	private String description;
	@ApiModelProperty(value = "汇聚用品牌名称")
	private String manufacturerAgg;
	@ApiModelProperty(value = "是否无铅符合rohs")
	private Boolean rohs;
	@ApiModelProperty(value = "类别信息")
	private List<ProductCategory> categories;

	// 1.2 文档信息
	@ApiModelProperty(value = "附件信息")
	private List<ProductDocument> documents;

	// 1.3 图片信息
	@ApiModelProperty(value = "图片信息")
	private List<ProductImage> images;

	// 1.4 物料参数
	@ApiModelProperty(value = "物料参数")
	private List<ProductParameter> parameters;

	@ApiModelProperty(value="来源")
	private String from;
	
	@ApiModelProperty(value="创建时间戳")
	private String createdTimeMillis;
	
	@ApiModelProperty(value="更新时间戳")
	private String updatedTimeMillis;
	
	@ApiModelProperty(value="状态  0:有效    1:失效")
	private Integer status;
	
	@ApiModelProperty(value="限制物料类型,例如：F-T-I,F:受控；T：关税；I：商检")
	private String restrictMaterialType;
	
	@ApiModelProperty(value="创建人名称")
	private String creatorName;
	
	@ApiModelProperty(value="修改人名称")
	private String updateName;
	
	@ApiModelProperty(value="审核状态 0:待审核 1:未通过	2:通过")
	private Integer audit;
	
	@ApiModelProperty(value="物料扩展信息")
	private ProductStandExtend extendInfo;
	
	@ApiModelProperty(value="对象hashCode")
	private int objHashCode;
	
	@JsonProperty("_id")
	private String idBack;
	
	private String manufacturerPartNumberNoIndex;
	
	public String getIdBack() {
		return idBack;
	}

	public void setIdBack(String idBack) {
		this.idBack = idBack;
	}

	public String getManufacturerPartNumberNoIndex() {
		return manufacturerPartNumberNoIndex;
	}

	public void setManufacturerPartNumberNoIndex(String manufacturerPartNumberNoIndex) {
		this.manufacturerPartNumberNoIndex = manufacturerPartNumberNoIndex;
	}

	public int getObjHashCode() {
		return objHashCode;
	}

	public void setObjHashCode(int objHashCode) {
		this.objHashCode = objHashCode;
	}

	public ProductStandExtend getExtendInfo() {
		return extendInfo;
	}

	public void setExtendInfo(ProductStandExtend extendInfo) {
		this.extendInfo = extendInfo;
	}

	public enum RestrictMaterialType{
		/**
		 * 否
		 */
		NO("否","N"),
		/**
		 * 需特殊包装
		 */
		PACKAGE("需特殊包装","P"),
		/**
		 * 需特殊文件
		 */
		FILE("需特殊文件","F"),
		
		CONTROLLED("受控","F"),
		
		CUSTOMS("关税","T"),
		
		COMMODITY_INSPECTION("商检","I");
		
		private String name;
		private String value;
		private RestrictMaterialType(String name, String value) {
			this.name = name;
			this.value = value;
		}
		// 普通方法
        public static String getName(String value) {
        	if(value == null){
        		return null;
        	}
            for (RestrictMaterialType type : RestrictMaterialType.values()) {
                if (StringUtils.equals(type.getValue(), value)) {
                    return type.name;
                }
            }
            return null;
        }

		public String getName() {
			return name;
		}

		public String getValue() {
			return value;
		}
		
	}
	
	public enum Status{
		/**
		 * 有效
		 */
		VALID("有效",0),
		/**
		 * 无效
		 */
		INVALID("失效",1);
		private String name;
		private int value;
		
		private Status(String name, int value) {
			this.name = name;
			this.value = value;
		}

		// 普通方法
        public static String getName(Integer value) {
        	if(value == null){
        		return null;
        	}
            for (Status c : Status.values()) {
                if (c.getValue() == value) {
                    return c.name;
                }
            }
            return null;
        }

		public String getName() {
			return name;
		}

		public int getValue() {
			return value;
		}
	}
	
	public enum Rohs{
		/**
		 * 否
		 */
		NO("N",false),
		/**
		 * 符合
		 */
		YES("Y",true);
		
		private String name;
		private Boolean value;
		private Rohs(String name, Boolean value) {
			this.name = name;
			this.value = value;
		}
		// 普通方法
        public static String getName(Boolean value) {
        	if(value == null){
        		return null;
        	}
            for (Rohs type : Rohs.values()) {
                if (type.getValue() == value) {
                    return type.name;
                }
            }
            return null;
        }
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Boolean getValue() {
			return value;
		}
		public void setValue(Boolean value) {
			this.value = value;
		}
	}
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public Integer getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(Integer manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public String getManufacturerPartNumber() {
		return manufacturerPartNumber;
	}

	public void setManufacturerPartNumber(String manufacturerPartNumber) {
		this.manufacturerPartNumber = manufacturerPartNumber;
	}

	public String getDescription() {
		return description;
	}

	public Boolean getRohs() {
		return rohs;
	}

	public void setRohs(Boolean rohs) {
		this.rohs = rohs;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ProductCategory> getCategories() {
		return categories;
	}

	public void setCategories(List<ProductCategory> categories) {
		this.categories = categories;
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

	public List<ProductParameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<ProductParameter> parameters) {
		this.parameters = parameters;
	}

	public String getManufacturerAgg() {
		return manufacturerAgg;
	}

	public void setManufacturerAgg(String manufacturerAgg) {
		this.manufacturerAgg = manufacturerAgg;
	}
	
	public String getSpuId() {
		return spuId;
	}

	public void setSpuId(String spuId) {
		this.spuId = spuId;
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
	
	public String getManufacturerShort() {
		return manufacturerShort;
	}

	public void setManufacturerShort(String manufacturerShort) {
		this.manufacturerShort = manufacturerShort;
	}
	
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRestrictMaterialType() {
		return restrictMaterialType;
	}

	public void setRestrictMaterialType(String restrictMaterialType) {
		this.restrictMaterialType = restrictMaterialType;
	}

	
	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getUpdateName() {
		return updateName;
	}

	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}

	public Integer getAudit() {
		return audit;
	}

	public void setAudit(Integer audit) {
		this.audit = audit;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj instanceof RawData){
			RawData prod = (RawData)obj;
			return null != this.getManufacturer() && this.getManufacturer().equals(prod.getManufacturer()) && 
					null != this.getManufacturerPartNumber() && this.getManufacturerPartNumber().equals(prod.getManufacturerPartNumber());
		}else if(obj instanceof ProductStand){
			return ((ProductStand)obj).getSpuId()!=null && ((ProductStand)obj).getSpuId().equals(this.getSpuId());
		}
		return super.equals(obj);
	}
}
