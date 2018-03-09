package com.yikuyi.product.essync;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ykyframework.exception.SystemException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

public interface IEssyncResource {

	@ApiOperation(value = "搜索引擎同步", notes = "作者：舒作<br>搜索引擎同步" ,response = List.class)
	@ApiResponses(value ={@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	@RequestMapping(method=RequestMethod.POST)
	public void esSync(@ApiParam("同步的索引")String type) throws SystemException;
}