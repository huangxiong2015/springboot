package com.yikuyi.product.strategy;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.yikuyi.strategy.model.StrategyProduct;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 包邮/限购商品
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
public interface IStrategyProductResource {
	
	
	/**
	 * 正式商品列表
	 * @param strategyId
	 * @param page
	 * @param pageSize
	 * @return
	 * @since 2018年1月8日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "包邮/限购商品列表信息", notes = "包邮/限购商品列表信息", response = List.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public PageInfo<StrategyProduct> list(@ApiParam(value = "包邮/限购id", required = true) String strategyId,
			@ApiParam(value = "当前页", required = false) int page,
			@ApiParam(value = "当前页数量", required = false) int pageSize);

	
	
}
