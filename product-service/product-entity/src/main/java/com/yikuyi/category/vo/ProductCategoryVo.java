package com.yikuyi.category.vo;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.yikuyi.category.model.ProductCategory;

import io.swagger.annotations.ApiModelProperty;

public class ProductCategoryVo extends ProductCategory{
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="子节点")
	@DBRef(lazy=false)
	private List<ProductCategoryChild> children;
	
	@ApiModelProperty(value="当前类别的父类")
	private ProductCategoryParent parent;

	public ProductCategoryParent getParent() {
		return parent;
	}

	public void setParent(ProductCategoryParent parent) {
		this.parent = parent;
	}
	
	public List<ProductCategoryChild> getChildren() {
		return children;
	}

	public void setChildren(List<ProductCategoryChild> children) {
		this.children = children;
	}

	
	

}
