package com.yikuyi.party.contact.api;

import java.util.List;

import com.yikuyi.party.contact.model.ContactMech;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 地址相关的服务
 * 
 * @author guowenyao
 *
 */
@FunctionalInterface
public interface IContactMechResource {

	/**
	 * 1.根据地址ID查找地址信息
	 * 
	 * @param id
	 * @return ContactMech集合
	 * @since 2016年11月29日
	 * @author guowenyao@yikuyi.com
	 */
	@ApiOperation(value = "根据地址ID查找地址信息", notes = "根据地址ID查找地址信息", response = ContactMech.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "请求参数不正确", response = Void.class),
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	public List<ContactMech> getContactMechList(@ApiParam(value = "ids查找地址信息", required = true) String ids);
}
