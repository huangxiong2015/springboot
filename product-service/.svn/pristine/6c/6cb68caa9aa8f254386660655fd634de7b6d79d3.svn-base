/*
 * Created: 2017年11月2日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.log.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.framewrok.springboot.web.RequestHelper;
import com.github.pagehelper.PageInfo;
import com.yikuyi.log.vo.ProductMappingError;
import com.yikuyi.product.log.IProductMappingErrorResource;
import com.yikuyi.product.log.manager.ProductMappingErrorManager;
import com.ykyframework.exception.BusinessException;

@RestController
@RequestMapping("v1/product/mapping/errors")
public class ProductMappingErrorResource implements IProductMappingErrorResource {
	
	@Autowired
	private ProductMappingErrorManager productMappingErrorManager;

	/**
	 * 列表查询
	 */
	@Override
	@RequestMapping(method = RequestMethod.GET)
	public PageInfo<ProductMappingError> search(
			@RequestParam(value="brandName", required=false)String brandName,
			@RequestParam(value="vendorId", required=false)String vendorId,
			@RequestParam(value="startDate", required=false)String startDate,
			@RequestParam(value="endDate", required=false)String endDate,
			@RequestParam(value="status", required=false)Integer status,
			@RequestParam(value="dataType", required=false)String dataType,
			@RequestParam(value="oprUserName", required=false)String oprUserName,
			@RequestParam(value="page", required=true, defaultValue="1") int page,
			@RequestParam(value="pageSize",required=true, defaultValue="20") int pageSize) {
		return productMappingErrorManager.findMappingErrorLogByPage(brandName,vendorId,startDate,endDate,status,dataType,oprUserName,page,pageSize);
	}
	
	/**
	 * 根据Ids批量更新
	 */
	@Override
	@RequestMapping(method = RequestMethod.PUT)
	public Integer batchUpdate(@RequestBody List<String> ids) throws BusinessException {
		String userId = RequestHelper.getLoginUserId();
		return productMappingErrorManager.batchUpdate(ids,userId);
	}

}
