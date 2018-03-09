package com.yikuyi.product.rule.delivery;

import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.yikuyi.rule.delivery.vo.ProductInfo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 实时查询交期信息相关的服务
 * @author zr.wenjiao@yikuyi.com
 * @version 1.0.0
 */
@FunctionalInterface
public interface ILeadTimeResource {
	
	/**
	 * 根据商品Id实时查询交期信息
	 * @param ids
	 * @return
	 * @since 2016年12月5日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@ApiOperation(value = "实时查询交期", notes = "实时查询交期", response = ProductInfo.class,responseContainer="List")
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "不存在指定id的商品信息", response = Void.class) })
	@RequestMapping(method=RequestMethod.POST,produces = "application/json; charset=utf-8")
	public List<ProductInfo> getLeadTimeList(@ApiParam(value = "商品Id", required = true) List<String> ids);
	
}
