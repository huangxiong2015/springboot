package com.yikuyi.party.customer.api;

import com.yikuyi.party.contact.vo.CustomersInfoVo;
import com.yikuyi.party.contact.vo.MsgResultVo;
import com.yikuyi.party.contact.vo.UserExtendVo;
import com.yikuyi.party.contact.vo.UserParamVo;
import com.yikuyi.party.contact.vo.UserVo;
import com.ykyframework.exception.BusinessException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
/**
 * 账户信息及密码相关服务
 * @author tb.yumu@yikuyi.com
 * @version 1.0.0
 */
public interface ICustomerSummeryResource {
	
	/**
	 * 获取个人账户基本信息
	 * @return
	 * @since 2017年1月11日
	 * @author tb.yumu@yikuyi.com
	 */
	@ApiOperation(value = "获取个人账户基本信息", notes = "获取个人账户基本信息", response = UserExtendVo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
			})
	public UserExtendVo accountSummary();
	
	/**
	 * 根据id获取账户信息
	 * @param partyId
	 * @return
	 * @since 2017年8月17日
	 */
	@ApiOperation(value = "根据id获取账户信息", notes = "根据id获取账户信息", response = UserParamVo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
			})
	public UserParamVo getAccountByPartyId(@ApiParam(value = "用户id", required = true) String partyId);
	
	/**
	 * 用户修改密码
	 * @param vo
	 * @return
	 * @since 2017年1月11日
	 * @author tb.yumu@yikuyi.com
	 */
	@ApiOperation(value = "用户修改密码", notes = "用户修改密码", response  = String.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
			})
	public void changePassword(@ApiParam(value = "修改密码的vo对象", required = true) UserVo vo) throws BusinessException;
	

	/**
	 * 验证用户原密码是否正确
	 * @param password
	 * @return
	 * @since 2017年1月12日
	 * @author tb.yumu@yikuyi.com
	 */
	@ApiOperation(value = "验证用户原密码是否正确", notes = "验证用户原密码是否正确", response = String.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
			})
	public void checkedOldPwd(@ApiParam(value = "用户原密码") String password) throws BusinessException;
	
	/**
	 * 查询用户登录信息
	 * @return
	 * @since 2017年2月27日
	 * @author tb.yumu@yikuyi.com
	 */
	@ApiOperation(value = "查询用户登录信息", notes = "查询用户登录信息", response = MsgResultVo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
			})
	public UserExtendVo getUserLoginInfo();
	
	
	
	/**
	 * 根据id获取用户信息
	 * @param id
	 * @return
	 * @since 2018年1月9日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "根据id获取用户信息", notes = "根据id获取用户信息", response = UserParamVo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
			})
	public CustomersInfoVo getCustomersInfoById(@ApiParam(value = "用户id", required = true) String partyId);
	
}
