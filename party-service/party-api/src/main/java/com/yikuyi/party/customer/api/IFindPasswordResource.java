package com.yikuyi.party.customer.api;

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
public interface IFindPasswordResource {
	
	/**
	 * 用户找回密码
	 * @param vo
	 * @return
	 * @since 2017年1月11日
	 * @author tb.yumu@yikuyi.com
	 */
	@ApiOperation(value = "用户找回密码", notes = "用户找回密码", response = String.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
			})
	public void reSetPassword(@ApiParam(value = "要找回密码的账号") String account,@ApiParam(value = "找回密码的vo对象", required = true) UserVo vo) throws BusinessException;
	
	/**
	 * 发送邮件
	 * @param mail
	 * @return
	 * @since 2017年2月9日
	 * @author tb.yumu@yikuyi.com
	 */
	@ApiOperation(value = "发送邮箱找回密码的验证邮件", notes = "发送邮箱找回密码的验证邮件", response = String.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
			})
	public void sendMail(@ApiParam(value = "邮箱账号")String mail) throws BusinessException;
}
