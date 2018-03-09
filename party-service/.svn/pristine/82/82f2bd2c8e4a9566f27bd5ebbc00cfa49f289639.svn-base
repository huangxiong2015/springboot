package com.yikuyi.party.shipAddress.api;

import java.util.List;

import com.yikuyi.basedata.common.model.Currency;
import com.yikuyi.party.contact.model.ContactMech;
import com.yikuyi.party.model.PartyContactMech;
import com.yikuyi.party.model.PartyContactMech.PurposeType;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 定义地址的相关接口
 * @author guowenyao
 *
 */
public interface IPartyAddressResource {
	
	/**
	 * 1.查询地址（类型）列表
	 * @return 地址列表
	 */
	@ApiOperation(value = "获取用户的地址列表", notes = "获取用户的地址列表", response = PartyContactMech.class, responseContainer="List")
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public  List<PartyContactMech> getShipAddressList(@ApiParam(value = "地址使用类型枚举")PurposeType addressType,@ApiParam(value = "用户id")String userId,@ApiParam(value = "币种")Currency currency);
	
	
	/**
	 * 2.根据id地址详情
	 * @param id
	 * @return 地址
	 */
	@ApiOperation(value = "根据id获取单条地址详情", notes = "根据id获取单条地址详情", response = PartyContactMech.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public PartyContactMech getShipAddressById(@ApiParam(value = "收货的地址id", required = true)String id,@ApiParam(value = "地址使用类型枚举")PurposeType addressType,@ApiParam(value = "币种")Currency currency);
	
	
	/**
	 * 3.新增地址
	 * @param 地址
	 * @return 地址
	 */
	@ApiOperation(value = "保存地址", notes = "保存地址", response = PartyContactMech.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public PartyContactMech save(@ApiParam(value = "地址信息的json结构", required = true)PartyContactMech partyContactMech);
	
	
	/**
	 * 4.编辑地址
	 * @param id,地址json
	 * @return 地址
	 */
	@ApiOperation(value = "修改地址", notes = "修改地址", response = PartyContactMech.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public PartyContactMech update(@ApiParam(value = "根据id修改地址信息", required = true)String id,@ApiParam(value = "根据id修改地址信息", required = true)PartyContactMech partyContactMech);
	
	
	/**
	 * 5.删除地址
	 * @param id
	 * @return 地址
	 */
	@ApiOperation(value = "删除地址", notes = "删除地址", response = PartyContactMech.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void delete(@ApiParam(value = "根据id修改地址信息", required = true)String id);
	
	
	
	/**
	 * SDK获取用户地址列表
	 * @return 地址列表
	 */
	@ApiOperation(value = "获取用户的地址列表", notes = "获取用户的地址列表", response = PartyContactMech.class, responseContainer="List")
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public  List<ContactMech> getBatchAddressList(@ApiParam(value = "contactMechIds") List<String> contactMechIds);
	
}