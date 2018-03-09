package com.yikuyi.category.vo;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.yikuyi.category.model.ProductCategory;
import io.swagger.annotations.ApiModelProperty;

public class ProductCategoryChild extends ProductCategory{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1543691007549258380L;
	
	@ApiModelProperty(value="子节点")
	@DBRef(lazy=false)
	private List<ProductCategoryChild> children;
	
	@ApiModelProperty(value="portal首页广告大类对应的图片")
	private List<ClassifyIndexImage> imageBigList;
	
	@ApiModelProperty(value="portal首页广告大类对应的图片")
	private List<ClassifyIndexImage> imageSmallList;
	

	public List<ProductCategoryChild> getChildren() {
		return children;
	}

	public void setChildren(List<ProductCategoryChild> children) {
		this.children = children;
	}

	public List<ClassifyIndexImage> getImageBigList() {
		return imageBigList;
	}

	public void setImageBigList(List<ClassifyIndexImage> imageBigList) {
		this.imageBigList = imageBigList;
	}

	public List<ClassifyIndexImage> getImageSmallList() {
		return imageSmallList;
	}

	public void setImageSmallList(List<ClassifyIndexImage> imageSmallList) {
		this.imageSmallList = imageSmallList;
	}

	
}
