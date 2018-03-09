/*
 * Created: 2017年1月11日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.customerssync.api;

import java.util.List;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 客户数据同步
 * @author zr.helinmei@yikuyi.com
 */
public interface ICustomersSyncResource {
	/**
	 * 全量查询企业信息全量同步数据方法
	 * @param rowBounds
	 * @return List<CustomersSyncVo>
	 * @since 2017年12月13日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "全量同步企业", notes = "全量同步企业", response = List.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public long getAllEntList(
			@ApiParam(value="page",required=false,defaultValue="1") int page,
			@ApiParam(value="size",required=false,defaultValue="100") int size);

	
	/**
	 * 查询企业数据增量同步数据方法
	 * @param selectStart
	 * @param selectEnd
	 * @return List<CustomersSyncVo>
	 * @since 2017年12月13日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "查询企业数据增量同步数据方法", notes = "查询企业数据增量同步数据方法", response = List.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public long getIncrementEntList(
			@ApiParam(value = "同步开始时间YYYY-MM-dd", required = false)String createStart,
			@ApiParam(value = "同步结束时间YYYY-MM-dd", required = false)String createEnd,
			@ApiParam(value="page",required=false,defaultValue="1") int page,
			@ApiParam(value="size",required=false,defaultValue="100") int size);
	
	
	
	/**
	 * 全量查询个人信息全量同步数据方法
	 * @param rowBounds
	 * @return List<CustomersSyncVo>
	 * @since 2017年12月13日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "全量同步个人用户", notes = "全量同步个人用户", response = List.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public long getAllPersonalList(
			@ApiParam(value="page",required=false,defaultValue="1") int page,
			@ApiParam(value="size",required=false,defaultValue="100") int size);
	
	
	/**
	 * 全量查询个人信息增量同步数据方法
	 * @param rowBounds
	 * @return List<CustomersSyncVo>
	 * @since 2017年12月13日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "查询个人用户数据增量同步数据方法", notes = "查询个人用户数据增量同步数据方法", response = List.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public long getIncrementPersonalList(
			@ApiParam(value = "同步开始时间YYYY-MM-dd", required = false)String createStart,
			@ApiParam(value = "同步结束时间YYYY-MM-dd", required = false)String createEnd,
			@ApiParam(value="page",required=false,defaultValue="1") int page,
			@ApiParam(value="size",required=false,defaultValue="100") int size);
	

}