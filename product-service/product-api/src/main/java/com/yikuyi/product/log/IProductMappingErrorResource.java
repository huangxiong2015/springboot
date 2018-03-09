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
package com.yikuyi.product.log;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.yikuyi.log.vo.ProductMappingError;
import com.ykyframework.exception.BusinessException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;

/**
 * 分类，制造商映射失败日志
 * @author injor.huang@yikuyi.com
 * @version 1.0.0
 */
public interface IProductMappingErrorResource {
	/**
	 * 列表查询
	 * @param vendorId
	 * @param startDate
	 * @param endDate
	 * @param status
	 * @param auditUserName
	 * @param page
	 * @param pageSize
	 * @return
	 * @since 2017年11月2日
	 * @author injor.huang@yikuyi.com
	 */
	@ApiOperation(value = "列表查询", notes ="列表查询", response = ProductMappingError.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	public PageInfo<ProductMappingError> search( 
			@ApiParam(value = "品牌")String brandName,
			@ApiParam(value = "数据来源")String vendorId,
			@ApiParam(value = "更新开始时间")String startDate,
			@ApiParam(value = "更新结束时间")String endDate,
			@ApiParam(value = "状态")Integer status,
			@ApiParam(value = "数据类型")String dataType,
			@ApiParam(value = "操作人名")String oprUserName,
			@ApiParam(value = "当前页") int page,
			@ApiParam(value = "当前页数量") int pageSize);
	
	/**
	 * 根据Ids批量更新
	 * @param ids
	 * @return
	 * @throws BusinessException
	 * @since 2017年11月2日
	 * @author injor.huang@yikuyi.com
	 */
	@ApiOperation(value = "更新", notes = "根据Ids批量审核", response = Void.class)
	public Integer batchUpdate(List<String> ids)throws BusinessException;

}
