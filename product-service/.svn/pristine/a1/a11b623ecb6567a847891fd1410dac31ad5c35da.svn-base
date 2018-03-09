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

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.pagehelper.PageInfo;
import com.yikuyi.product.model.ProductStand;
import com.yikuyi.product.model.SpuWhiteList;
import com.yikuyi.product.vo.RawData;
import com.ykyframework.exception.BusinessException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
/**
 * 物料列表
 * 
 * @author zr.wujiajun@yikuyi.com
 * @version 1.0.0
 */
public interface IProductStandResource {

	/**
	 * 物料列表
	 * @param manufacturerPartNumber型号
	 */
	@ApiOperation(value = "物料管理", notes = "物料管理", response = ProductStand.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	@RequestMapping(method = RequestMethod.GET)
	public PageInfo<ProductStand> list(@ApiParam(value = "型号右模糊查询")String manufacturerPartNumber, 
			@ApiParam(value = "型号（精确）")String manufacturerPartNumberExact,
			@ApiParam(value = "原厂")String manufacturer,
			@ApiParam(value = "开始时间")String startDate,
			@ApiParam(value = "结束时间")String endDate,
			@ApiParam(value = "状态")Integer status,
			@ApiParam(value = "审核状态")Integer auditStatus,
			@ApiParam(value = "大类")Integer cate1Name,
			@ApiParam(value = "小类")Integer cate2Name,
			@ApiParam(value = "次小类")Integer cate3Name,
			@ApiParam(value = "审核人名")String auditUserName,
			@ApiParam(value = "当前页", required = false) int page,
			@ApiParam(value = "当前页数量", required = false) int pageSize);
	
	/**
	 * 查询单个物料数据
	 * @param id
	 * @return
	 * @since 2017年2月23日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@ApiOperation(value = "查询单个物料数据", notes = "查询单个物料数据", response = ProductStand.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	@RequestMapping(method = RequestMethod.GET)
	public ProductStand getProductStand(@ApiParam(value = "物料ID", required = true) String id);

	/**
	 * 更新物料的数据
	 * @param productStand
	 * @param updateType
	 * @return
	 * @since 2017年3月21日
	 * @author tongkun@yikuyi.com
	 */
	@ApiOperation(value = "更新物料", notes = "根据不同的类型更新物料。当前只支持用品牌id更新品牌信息，此条件会更新所有这个品牌的数据", response = ProductStand.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	@RequestMapping(method = RequestMethod.PUT)
	public void updateProductStand(ProductStand productStand);

	/**
	 * 创建物料
	 * @param rawdata
	 * @since 2017年2月23日
	 * @author tongkun@yikuyi.com
	 * @return 
	 */
	@RequestMapping(method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String createProductStand(List<RawData> rawdata);
	
	/**
	 * 查询单个物料数据
	 * @param id
	 * @return
	 * @since 2017年2月28日
	 * @author zr.wujiajun@yikuyi.com
	 */
	@ApiOperation(value = "根据品牌manufacturer，型号manufacturerPartNumber批量查询物料", notes = "根据品牌manufacturer，型号manufacturerPartNumber批量查询物料", response = ProductStand.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	@RequestMapping(method = RequestMethod.POST)
	public List<ProductStand> batchQuery(@ApiParam(value = "物料ID", required = true) List<RawData> datas);
	
	/**
	 * 根据查询条件或者Ids导出物料数据
	 * @return
	 * @since 2017年8月18日
	 * @author injor.huang@yikuyi.com
	 */
	@ApiOperation(value = "根据查询条件或者Ids导出物料数据", response = String.class)
	public void exportExcel(@ApiParam(value = "型号精确查询")String manufacturerPartNumber, 
			@ApiParam(value = "原厂")String manufacturer,
			@ApiParam(value = "开始时间")String startDate,
			@ApiParam(value = "结束时间")String endDate,
			@ApiParam(value = "状态")Integer status,
			@ApiParam(value = "审核状态")Integer auditStatus,
			@ApiParam(value = "大类")Integer cate1Name,
			@ApiParam(value = "小类")Integer cate2Name,
			@ApiParam(value = "次小类")Integer cate3Name,
			@ApiParam(value = "物料IDs")String ids)throws BusinessException;
	
	/**
	 * 批量验证型号是否存在
	 * @param manufacturerPartNumberList
	 * @since 2017年11月27日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "批量验证型号是否存在", notes = "批量验证型号是否存在", response = String.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	public List<String> existManufacturerPartNumberList(@ApiParam(value = "型号集合")List<String> manufacturerPartNumberList);
	
	/**
	 * 新增特殊SPU
	 * @param manufacturerPartNumber
	 * @param manufacturerId
	 * @since 2017年12月8日
	 * @author tb.lijing@yikuyi.com
	 */
	@ApiOperation(value = "新增特殊SPU", notes = "作者：李京<br>新增特殊SPU", response = Void.class)
	public void saveWhiteList(SpuWhiteList spuWhiteList) throws BusinessException;
	
	/**
	 * 查询特殊SPU
	 * @return
	 * @since 2017年12月8日
	 * @author tb.lijing@yikuyi.com
	 */
	@ApiOperation(value = "查询特殊SPU", notes = "作者：李京<br>查询特殊SPU", response = SpuWhiteList.class)
	public PageInfo<SpuWhiteList> findWhiteListInfo(@ApiParam(value = "型号")String manufacturerPartNumber, 
			@ApiParam(value = "制造商")String manufacturer,
			@ApiParam(value="页码") int page,
			@ApiParam(value="每页记录条数") int pageSize);
	
	
}
