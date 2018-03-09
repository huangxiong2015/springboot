/*
 * Created: 2017年6月30日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.resource;

import java.util.List;
import java.util.Set;

import org.springframework.web.bind.annotation.RequestBody;

import com.yikuyi.category.model.ProductCategory;
import com.yikuyi.category.model.ProductCategory.ProductCategoryLevel;
import com.yikuyi.category.vo.ProductCategoryChild;
import com.yikuyi.category.vo.ProductCategoryParent;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import io.swagger.annotations.ApiParam;

@Headers({"Content-Type: application/json;charset=UTF-8"})
public interface CategoryClient {
	@RequestLine("GET /v1/products/categories/list?status={status}")
	public List<ProductCategoryChild> getAllCategory(@Param(value="status")List<Integer> status);
	
	@RequestLine("GET /v1/products/categories/{cateId}")
	@Headers({ "Authorization: Basic {authToken}" })
	public ProductCategory findById(@Param("cateId") Integer cateId,@Param("authToken") String authToken);
	
	@RequestLine("POST /v1/products/categories/batch")
	public List<ProductCategoryParent> getListByIds(@ApiParam(value = "类别Ids") List<Long> ids);
	
	@RequestLine("GET /v1/products/categories/children?parentCateId={parentCateId}")
	public List<ProductCategory> getChildrenById(@Param("parentCateId")Integer parentCateId);
	
	@RequestLine("GET /v2/products/categories/getParentsByNames?cateLevel={cateLevel}")
	public List<ProductCategoryParent> getParentsByNames(@RequestBody Set<String> cateNames, @Param("cateLevel")ProductCategoryLevel cateLevel);
}