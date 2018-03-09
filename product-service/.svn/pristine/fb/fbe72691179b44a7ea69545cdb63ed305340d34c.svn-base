package com.yikuyi.product.goods;

import com.alibaba.fastjson.JSONObject;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@FunctionalInterface
public interface IProductDetailCrawlerResource {

	@ApiOperation(value = "根据skuId或商品ID爬取单条商品最新数据", notes = "根据skuId或商品ID爬取单条商品最新数据" ,response = JSONObject.class)
	@ApiResponses(value ={@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	JSONObject run(String param);

}