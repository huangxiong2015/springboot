package com.yikuyi.product.goods;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONArray;
import com.yikuyi.product.model.Product;
import com.yikuyi.rule.price.PriceInfo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

public interface IPriceQueryResouce {

	@ApiOperation(value = "实时查询商品价格", notes = "实时查询商品价格" ,response = List.class)
	@ApiResponses(value ={@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	@RequestMapping(method=RequestMethod.POST)
	public List<PriceInfo> queryPrice(@ApiParam("商品的id数组")List<String> id) throws Exception;
	
	
	@ApiOperation(value = "查询商品价格模板", notes = "查询商品价格模板" ,response = JSONArray.class)
	@ApiResponses(value ={@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	public JSONArray queryPriceTemplate(@ApiParam("商品的id数组")List<String> id);
	
	@ApiOperation(value = "查询商品价格模板", notes = "查询商品价格模板" ,response = JSONArray.class)
	@ApiResponses(value ={@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	public JSONArray queryPriceTemplate(@ApiParam("商品对象，必填字段有：vendorId,sourceId,manufacturerId,cate1,cate2,cate3,价格信息") Product product);
}
