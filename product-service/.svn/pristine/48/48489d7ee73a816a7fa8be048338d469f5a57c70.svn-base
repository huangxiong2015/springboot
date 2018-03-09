package com.yikuyi.product.goods;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yikuyi.product.vo.ProductVo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
/**
 * 批量查询商品信息相关的服务
 * 
 * @author zr.wujiajun@yikuyi.com
 * @version 1.0.0
 */
public interface IProductResourceV2 {
	/**
	 * 批量查询商品详细信息,包含价格,交期等
	 * 
	 * @params ids
	 * @author zr.wujiajun
	 */
	@ApiOperation(value = "批量查询商品信息,包含实时价格,交期等", notes = "实时查询商品信息,包含实时价格,交期等" ,response = ProductVo.class)
	@ApiResponses(value ={@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	@RequestMapping(method=RequestMethod.POST)
	public List<ProductVo> getFullInfoByIds(List<String> ids);
	
	/**
	 * 根据  品牌 和 型号  查商品ID，价格阶梯
	 * @params manufacturer
	 * @params manufacturerPartNumber
	 * @author tb.huangqingfeng
	 */
	@ApiOperation(value = "根据  品牌 和 型号  查商品ID，价格阶梯", notes = "根据  品牌 和 型号  查商品ID，价格阶梯" ,response = ProductVo.class)
	@ApiResponses(value ={@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	@RequestMapping(method=RequestMethod.GET)
	public List<ProductVo> findFacturerAndPartNumber(@ApiParam("制造商名称")String manufacturer,@ApiParam("型号")String manufacturerPartNumber);
}
