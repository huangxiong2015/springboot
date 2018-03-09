/*
 * Created: 2017年2月23日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.goods;

import com.github.pagehelper.PageInfo;
import com.yikuyi.product.model.ProductStandAudit;
import com.yikuyi.product.vo.ProductStandAuditRequest;
import com.ykyframework.exception.BusinessException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
/**
 * 物料审核
 * @author injor.huang@yikuyi.com
 * @version 1.0.0
 */
public interface IProductStandAuditResource {

	/**
	 * 列表查询
	 * @param manufacturerPartNumber
	 * @param keyword
	 * @param page
	 * @param pageSize
	 * @return
	 * @since 2017年8月15日
	 * @author injor.huang@yikuyi.com
	 */
	@ApiOperation(value = "列表查询", notes ="列表查询", response = ProductStandAudit.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	public PageInfo<ProductStandAudit> search(@ApiParam(value = "型号精确查询")String manufacturerPartNumber, 
			@ApiParam(value = "原厂")String manufacturer,
			@ApiParam(value = "开始时间")String startDate,
			@ApiParam(value = "结束时间")String endDate,
			@ApiParam(value = "状态")Integer status,
			@ApiParam(value = "审核状态")Integer auditStatus,
			@ApiParam(value = "大类")Integer cate1Name,
			@ApiParam(value = "小类")Integer cate2Name,
			@ApiParam(value = "次小类")Integer cate3Name,
			@ApiParam(value = "审核人名")String auditUserName,
			@ApiParam(value = "当前页") int page,
			@ApiParam(value = "当前页数量") int pageSize);
	
	/**
	 * 查询单个物料数据
	 * @param id
	 * @return
	 * @since 2017年2月23日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@ApiOperation(value = "查询单个物料数据", notes = "查询单个物料数据", response = ProductStandAudit.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	public ProductStandAudit getProductStandById(@ApiParam(value = "物料ID", required = true) String id)throws BusinessException;

	/**
	 * 更新物料的数据
	 * @param productStand
	 * @param updateType
	 * @return
	 * @since 2017年3月21日
	 * @author tongkun@yikuyi.com
	 */
	@ApiOperation(value = "更新物料", notes = "根据ID更新物料信息", response = ProductStandAudit.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	public ProductStandAudit updateProductStandAudit(@ApiParam(value = "物料ID", required = true)String id,ProductStandAudit productStandAudit)throws BusinessException;

	/**
	 * 根据Ids批量审核
	 * @param ids
	 * @throws BusinessException
	 * @since 2017年8月16日
	 * @author injor.huang@yikuyi.com
	 */
	@ApiOperation(value = "物料批量审核", notes = "根据Ids批量审核", response = Void.class)
	public void audit(ProductStandAuditRequest auditRequest)throws BusinessException;
}
