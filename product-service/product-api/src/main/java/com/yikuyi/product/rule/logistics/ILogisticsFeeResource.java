package com.yikuyi.product.rule.logistics;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yikuyi.rule.logistics.vo.LogisticsFee;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 实时查询运费信息相关的服务
 * @author zr.wenjiao@yikuyi.com
 * @version 1.0.0
 */
@FunctionalInterface
public interface ILogisticsFeeResource {
	
	/**
	 * 实时查询商品的运费信息
	 * @param fee
	 * @return
	 * @since 2016年12月6日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@ApiOperation(value = "实时查询运费", notes = "实时查询运费", response = Double.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)})
	@RequestMapping(method=RequestMethod.POST,produces = "application/json; charset=utf-8")
	public double getLogisticsFee(@ApiParam("运费信息") LogisticsFee fee);

}
