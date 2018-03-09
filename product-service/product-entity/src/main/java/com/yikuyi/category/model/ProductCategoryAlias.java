package com.yikuyi.category.model;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModelProperty;

/**
 * 分类信息
 * @author zr.wenjiao@yikuyi.com
 */
public class ProductCategoryAlias extends AbstractEntity{
	
	private static final long serialVersionUID = 3889756628104343207L;
	
	@ApiModelProperty(value="所属供应商名称")
	private String vendorName;
	
	@ApiModelProperty(value="所属供应商id")
	private String vendorId;

	@ApiModelProperty(value="别名level1")
	private String level1;

	@ApiModelProperty(value="别名level2")
	private String level2;

	public String getLevel1() {
		return level1;
	}

	public void setLevel1(String level1) {
		this.level1 = level1;
	}

	public String getLevel2() {
		return level2;
	}

	public void setLevel2(String level2) {
		this.level2 = level2;
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

	@Override
	public int hashCode() {
		return (""+this.level1+this.level2).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ProductCategoryAlias)){
			return false;
		}
		ProductCategoryAlias ali = (ProductCategoryAlias)obj;
		boolean level1Equal = false;
		boolean level2Equal = false;
		if(this.getLevel1()==ali.getLevel1()||
				(this.getLevel1()!=null&&ali.getLevel1().equals(this.getLevel1()))){
			level1Equal = true;
		}
		if(this.getLevel2()==ali.getLevel2()||
				(this.getLevel2()!=null&&this.getLevel2().equals(ali.getLevel2()))){
			level2Equal = true;
		}
		
		return level1Equal&&level2Equal;
	}
}
