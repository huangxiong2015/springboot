package com.yikuyi.party.userLogin.api;



import com.yikuyi.party.contact.vo.AccountVo;
import com.yikuyi.party.contact.vo.User;
import com.yikuyi.party.contact.vo.UserExtendVo;
import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.party.login.model.UserLogin;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 定义地址的相关接口
 * @author zr.aoxianbing@yikuyi.com
 *
 */
public interface IUserLoginResource {
	
	/**
	 * 根据手机或者邮箱判断用户是否存在(账号base64加密)
	 * @param account
	 * @return
	 * @since 2017年8月29日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "根据手机或者邮箱判断用户是否存在(账号base64加密)", notes = "根据手机或者邮箱判断用户是否存在(账号base64加密)", response = Boolean.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Boolean.class) 
			})
	public Boolean isExist(@ApiParam(value = "account", required = true) String account);
	
	/**
	 * 更改账号
	 * @param accountVo
	 * @return
	 * @since 2017年8月29日
	 */
	@ApiOperation(value = "更改账号", notes = "更改账号", response = AccountVo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = AccountVo.class) 
			})
	public String updateAccount(@ApiParam(value = "accountVo", required = true) AccountVo accountVo);
	
	/**
	 * 更改用户
	 * @param userInfoVo
	 * @since 2017年8月29日
	 */
	@ApiOperation(value = "更改用户", notes = "更改用户", response = Void.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void updatePerson(@ApiParam(value = "userInfoVo", required = true) UserExtendVo userInfoVo);
	
	/**
	 * 根据手机或者邮箱判断用户是否存在
	 * @param account
	 * @return
	 * @since 2017年8月29日
	 */
	@ApiOperation(value = "根据手机或者邮箱判断用户是否存在", notes = "根据手机或者邮箱判断用户是否存在", response = Boolean.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Boolean.class) 
			})
	public Boolean getAccount(@ApiParam(value = "account", required = true) String account);
	
	/**
	 * 初始化密码
	 * @param userVo
	 * @return
	 * @since 2017年8月29日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "初始化密码", notes = "初始化密码", response = UserVo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = UserVo.class) 
			})
	public UserVo initPassWord(@ApiParam(value = "userVo", required = true) UserVo userVo);
	
	/**
	 * 根据账号id查询用户
	 * @param account
	 * @return
	 * @since 2017年8月29日
	 */
	@ApiOperation(value = "根据账号id查询用户", notes = "根据账号id查询用户", response = UserLogin.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = void.class) 
			})	
	public UserLogin getAccountById(@ApiParam(value = "account", required = true) String account);
	
	/**
	 * 根据partyid和类型查询用户
	 * @param id
	 * @param type
	 * @return
	 * @since 2017年8月29日
	 */
	@ApiOperation(value = "根据partyid和类型查询用户", notes = "根据partyid和类型查询用户", response = String.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = void.class) 
			})	
	public String getAccountByIdAndType(@ApiParam(value = "id", required = true) String id ,@ApiParam(value = "id", required = true) String type );
	
	/**
	 * 发送邮件（不登陆验证账号）
	 * @param serVo
	 * @return
	 * @since 2017年8月29日
	 */
	@ApiOperation(value = "发送邮件（不登陆验证账号）", notes = "发送邮件（不登陆验证账号）", response = String.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "请求参数不正确", response = String.class),
			@ApiResponse(code = 404, message = "访问的页面不存在", response = String.class) })
	public String sendMail(@ApiParam(value = "用户", required = true) UserVo serVo);
	
	/**
	 * 发送创建账号邮件（不登陆验证账号）
	 * @param serVo
	 * @return
	 * @since 2017年8月29日
	 */
	@ApiOperation(value = "发送创建账号邮件（不登陆验证账号）", notes = "发送创建账号邮件（不登陆验证账号）", response = String.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "请求参数不正确", response = String.class),
			@ApiResponse(code = 404, message = "访问的页面不存在", response = String.class) })
	public String sendCreateMail(@ApiParam(value = "用户", required = true) UserVo serVo);
	
	
	/**
	 * 用户首次登陆监控
	 * @param user
	 * @since 2017年9月12日
	 */
	@ApiOperation(value = "用户首次登陆监控", notes = "用户首次登陆监控", response = Void.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void userLoginListener(@ApiParam(value = "userVo", required = true) User user);
	
	
	/**
	 * 获取个人当天注册数或者总数
	 * @param flag 不为空则查询当天个人会员注册数
	 * @return
	 * @since 2017年11月29日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "获取个人当天注册数或者总数", notes = "获取个人当天注册数或者总数", response = Long.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Long.class) 
			})
	public Long getPersonalData(@ApiParam(value = "标志") String flag);
	
	/**
	 * 获取认证会员当天数或者总数
	 * @param flag 不为空则查询当天认证会员数
	 * @return
	 * @since 2017年11月29日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "获取认证会员当天数或者总数", notes = "获取认证会员当天数或者总数", response = Long.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Long.class) 
			})
	public Long getAuthenticationData(@ApiParam(value = "标志") String flag);

	/**
	 *  获取今天登录数量
	 * @param
	 * @return
	 * @since 2017年11月29日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "获取今天登录数量", notes = "获取今天登录数量", response = Long.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Long.class) 
			})
	public Long getLoginNumToday();

}
