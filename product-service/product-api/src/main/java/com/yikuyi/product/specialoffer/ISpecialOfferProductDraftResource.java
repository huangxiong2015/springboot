/*
 * Created: 2017年12月20日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.specialoffer;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.github.pagehelper.PageInfo;
import com.yikuyi.specialoffer.model.SpecialOfferProduct.Status;
import com.yikuyi.specialoffer.model.SpecialOfferProductDraft;
import com.yikuyi.specialoffer.vo.SpecialOfferProductDraftVo;
import com.ykyframework.exception.BusinessException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 专属特价商品草稿
 * @author tb.lijing@yikuyi.com
 * @version 1.0.0
 */
public interface ISpecialOfferProductDraftResource {
	/**
	 * 上传的文件解析
	 * @param spDraftVo
	 * @throws BusinessException
	 * @since 2017年12月20日
	 * @author tb.lijing@yikuyi.com
	 */
	@ApiOperation(value = "专属特价规则的文件解析", notes = "作者：李京<br>专属特价规则的文件解析", response = Void.class)
	public void parseFile(SpecialOfferProductDraftVo spDraftVo) throws BusinessException;
	
	/**
	 * 分页查询专属特价商品草稿数据
	 * @param ruleId
	 * @param page
	 * @param pageSize
	 * @return
	 * @since 2017年12月20日
	 * @author tb.lijing@yikuyi.com
	 */
	@ApiOperation(value = "查询专属特价商品草稿数据", notes = "作者：李京<br>查询专属特价商品草稿数据", response = SpecialOfferProductDraft.class)
	public PageInfo<SpecialOfferProductDraft> findSpecialOfferProductDraftByPage(@ApiParam(value = "规则id", required=true)String ruleId, 
			@ApiParam(value="页码", required=false) int page,
			@ApiParam(value="每页记录条数", required=false) int pageSize);
	
	/**
	 * 删除专属特价商品草稿数据
	 * 
	 * @since 2017年12月20日
	 * @author tb.lijing@yikuyi.com
	 */
	@ApiOperation(value = "删除专属特价商品草稿数据", notes = "作者：李京<br>删除专属特价商品草稿数据", response = Void.class)
	public void deleteSpecialOfferProductDraft(@ApiParam(value = "ids", required = true) List<String> ids);
	
	/**
	 * 导出专属特价商品草稿数据
	 * @param ids
	 * @param ruleId
	 * @param status
	 * @param response
	 * @throws IOException
	 * @since 2017年12月20日
	 * @author tb.lijing@yikuyi.com
	 */
	@ApiOperation(value = "导出专属特价商品草稿数据", notes = "作者：李京<br>导出专属特价商品草稿数据", response = Void.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	public void exportSpecialOfferProductDrafts(@ApiParam(value = "ids", required = false)String ids
			                   ,@ApiParam(value = "ruleId", required = true) String ruleId
			                   ,@ApiParam(value = "status", required = false) Status status
			                   ,HttpServletResponse response
			                   ) throws IOException;
	
	/**
	 * 添加草稿商品信息
	 * @param productDraft
	 * @since 2017年12月21日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "添加草稿商品信息", notes = "添加草稿商品信息", response = Void.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void addSpecialOfferProductDraft(SpecialOfferProductDraft productDraft); 
}
