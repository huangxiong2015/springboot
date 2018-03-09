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
package com.yikuyi.product.log.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.yikuyi.log.vo.ProductMappingError;

@Repository
public interface ProductMappingErrorRepository extends MongoRepository<ProductMappingError, String> {

	/**
	 * 根据ids查询信息
	 * @param ids
	 * @return
	 * @since 2017年11月2日
	 * @author injor.huang@yikuyi.com
	 */
	@Query("{'_id':{'$in':?0}}")
	public List<ProductMappingError> findMappingErrorLogByIds(List<String> ids);
	
	/**
	 * 根据状态和类型查询
	 * @param ids
	 * @return
	 * @since 2017年11月2日
	 * @author injor.huang@yikuyi.com
	 */
	@Query("{'status':?0}")
	public List<ProductMappingError> getMappingErrorLogsByStatus(Integer status);
	/**
	 * 根据状态和品牌查询
	 * @param status
	 * @param branchName
	 * @return
	 */
	@Query("{'status':?0,'vendorId':?1,'brandName':?2}")
	public List<ProductMappingError> getMappingErrorLogsByStatusAndBrandName(Integer status,String vendorId,String brandName);
	/**
	 * 根据状态和分类(level1和level2)查询
	 * @param status
	 * @param cate1Name
	 * @param cate2Name
	 * @return
	 */
	@Query("{'status':?0,'vendorId':?1,'categoriesHashCode':?2}")
	public List<ProductMappingError> getMappingErrorLogsByStatusAndCategory(Integer status,String vendorId,int categoriesHashCode);
}
