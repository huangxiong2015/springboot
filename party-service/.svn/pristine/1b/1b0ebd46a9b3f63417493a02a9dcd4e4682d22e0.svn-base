package com.yikuyi.party.profiles.api;

import com.yikuyi.party.model.PartyProfileDefault;
import com.yikuyi.party.model.PartyProfileDefault.DefaultAddressType;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 设置默认地址
 * @author guowenyao
 *
 */
public interface IProfilesResource {

	/**
	 * 设置默认地址
	 * @param id
	 * @return 默认地址
	 */
	@ApiOperation(value = "设置默认地址", notes = "设置默认地址", response = PartyProfileDefault.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public PartyProfileDefault defaultAdress(@ApiParam(value = "id", required = true)String id,@ApiParam(value = "defaultAddress", required = true)DefaultAddressType defaultAddress);

	
	/**
	 * 获取默认地址
	 * @author 张伟
	 * @return
	 */
	@ApiOperation(value = "获取默认地址", notes = "获取默认地址", response = PartyProfileDefault.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public PartyProfileDefault getDefaultAdress();
}
