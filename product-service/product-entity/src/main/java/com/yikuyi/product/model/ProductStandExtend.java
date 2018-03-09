package com.yikuyi.product.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModelProperty;

@Document(collection = "product_stand_extend")
public class ProductStandExtend extends AbstractEntity {

	private static final long serialVersionUID = -5635592869503794873L;
	
	@ApiModelProperty(value = "ID")
	@Id
	@JsonProperty("id")
	private String id;
	
	@ApiModelProperty(value = "细节图片信息")
	private List<ProductImage> detailImages;
	
	@ApiModelProperty(value = "物料名称")
	private String materialName;
	
	@ApiModelProperty(value = "促销词")
	private String promotionWord;
	
	@ApiModelProperty(value = "物料详情")
	private String materialDetail;
	
	@ApiModelProperty(value = "是否限制物料")
	private String isControlMaterial;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<ProductImage> getDetailImages() {
		return detailImages;
	}

	public void setDetailImages(List<ProductImage> detailImages) {
		this.detailImages = detailImages;
	}

	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	public String getPromotionWord() {
		return promotionWord;
	}

	public void setPromotionWord(String promotionWord) {
		this.promotionWord = promotionWord;
	}

	public String getMaterialDetail() {
		return materialDetail;
	}

	public void setMaterialDetail(String materialDetail) {
		this.materialDetail = materialDetail;
	}

	public String getIsControlMaterial() {
		return isControlMaterial;
	}

	public void setIsControlMaterial(String isControlMaterial) {
		this.isControlMaterial = isControlMaterial;
	}
	
}
