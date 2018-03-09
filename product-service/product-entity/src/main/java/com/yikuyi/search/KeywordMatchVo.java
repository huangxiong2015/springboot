/*
 * Created: 2018年2月7日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2018 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.search;

import com.yikuyi.brand.model.ProductBrand;
import com.yikuyi.category.model.ProductCategory;
import com.yikuyi.party.supplier.SupplierVo;

public class KeywordMatchVo {

	// 关键字
	private String keyword;

	// 品牌ID
	private ProductBrand brand;

	// 分类ID
	private ProductCategory cat;

	// 分销商ID
	private SupplierVo supplier;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public ProductBrand getBrand() {
		return brand;
	}

	public void setBrand(ProductBrand brand) {
		this.brand = brand;
	}

	public ProductCategory getCat() {
		return cat;
	}

	public void setCat(ProductCategory cat) {
		this.cat = cat;
	}

	public SupplierVo getSupplier() {
		return supplier;
	}

	public void setSupplier(SupplierVo supplier) {
		this.supplier = supplier;
	}


}