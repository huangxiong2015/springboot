package com.yikuyi.product.strategy;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import com.github.pagehelper.PageInfo;
import com.yikuyi.strategy.model.Strategy.StrategyType;
import com.yikuyi.strategy.model.StrategyProductDraft;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 包邮/限购草稿商品
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
public interface IStrategyProductDraftResource {
	
	
	/**
	 * 草稿商品列表
	 * @param strategyId
	 * @param page
	 * @param pageSize
	 * @return
	 * @since 2018年1月8日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "草稿商品列表信息", notes = "草稿商品列表信息", response = List.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public PageInfo<StrategyProductDraft> list(@ApiParam(value = "包邮id", required = true) String strategyId,
			@ApiParam(value = "当前页", required = false) int page,
			@ApiParam(value = "当前页数量", required = false) int pageSize);

	
	
	/**
	 * 删除包邮/限购商品草稿数据
	 * @param ids
	 * @since 2018年1月8日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "删除包邮/限购商品草稿数据", notes = "删除包邮/限购商品草稿数据", response = Void.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void deleteStrategyProductDraft(@ApiParam(value = "ids", required = true) List<String> ids,@ApiParam(value="类型", required=false) StrategyType strategyType);
	
	
	/**
	 * 添加草稿商品信息
	 * @param productDraft
	 * @since 2018年1月8日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "添加草稿商品信息", notes = "添加草稿商品信息", response = Void.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void addStrategyProductDraft(@RequestBody(required=true) StrategyProductDraft productDraft); 
	
}
