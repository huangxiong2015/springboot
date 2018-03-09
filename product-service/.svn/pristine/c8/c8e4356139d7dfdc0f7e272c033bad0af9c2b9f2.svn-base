package com.yikuyi.product.goods;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yikuyi.product.vo.ProductVo;
import com.yikuyi.rule.price.PriceInfo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

public interface IPriceQueryResouceV2 {

	@ApiOperation(value = "实时查询商品价格", notes = "实时查询商品价格" ,response = PriceInfo.class,responseContainer = "List")
	@ApiResponses(value ={@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	@RequestMapping(method=RequestMethod.POST)
	public List<PriceInfo> queryPrice(@ApiParam("商品的对象数组")List<ProductVo> productVos) throws Exception;
	
	@ApiOperation(value = "实时查询秒杀专场商品价格", notes = "作者：王洪<br/>实时查询秒杀专场商品价格,productVo中必填字段：商品ID，活动ID，时段ID" ,response = PriceInfo.class,responseContainer = "List")
	@ApiResponses(value ={@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	@RequestMapping(method=RequestMethod.POST)
	public List<PriceInfo> queryPriceForSeckill(@RequestBody List<ProductVo> productVos);
	
}
