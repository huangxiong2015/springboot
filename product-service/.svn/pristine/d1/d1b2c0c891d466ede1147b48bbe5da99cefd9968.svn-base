package com.yikuyi.category.vo;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.yikuyi.category.model.ProductCategory;

import io.swagger.annotations.ApiModelProperty;

public class ProductCategoryParent extends ProductCategory{
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="当前类别的父类")
	@DBRef
	private ProductCategoryParent parent;

	public ProductCategoryParent getParent() {
		return parent;
	}

	public void setParent(ProductCategoryParent parent) {
		this.parent = parent;
	}
	
	

}
