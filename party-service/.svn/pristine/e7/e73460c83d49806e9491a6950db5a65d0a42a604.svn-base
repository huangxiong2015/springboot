package com.yikuyi.party.resource;

import org.springframework.web.bind.annotation.RequestBody;

import com.yikuyi.party.contact.vo.User;
import com.yikuyi.party.login.model.UserLogin;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * 权限相关的接口调用定义
 * 
 * @author jik.shu@yikuyi.com
 */
@Headers({ "Content-Type: application/json;charset=UTF-8" })
public interface UserLoginClient {


	/**
	 * 根据用户账户查找用户信息
	 * @param account
	 * @param authToken
	 * @return
	 * @since 2017年6月29日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@RequestLine("GET /v1/account/id?account={account}")
	@Headers({ "Authorization: Basic {authToken}" })
	public UserLogin getAccountById(@Param("account") String account,@Param("authToken") String authToken);
	
	/**
	 * 根据账号判断是否存在（手机、邮箱）
	 * 
	 * @param account
	 * @return
	 * @since 2017年1月12日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@RequestLine("GET /v1/account/mail?account={account}")
	@Headers({ "Authorization: Basic {authToken}" })
	public Boolean getAccount(@Param("account") String account,@Param("authToken") String authToken);

	/**
	 * 根据用户账户查找用户信息
	 * @param account
	 * @param authToken
	 * @return
	 * @since 2017年6月29日
	 * @author zr.helinmei@yikuyi.com
	 */
	@RequestLine("POST /v1/account/userLoginListener")
	@Headers({ "Authorization: Basic {authToken}" })
	public void userLoginListener(@RequestBody User user,@Param("authToken") String authToken);

	/**
	 * 获取认证会员当天数或者总数
	 * @param flag 不为空则查询当天认证会员数
	 * @return
	 * @since 2017年11月29日
	 * @author zr.helinmei@yikuyi.com
	 */
	@RequestLine("GET /v1/account/getAuthenticationData?flag={flag}")
	public Long getAuthenticationData(@Param("flag") String flag);
	
	
	/**
	 * 获取个人当天注册数或者总数
	 * @param flag 不为空则查询当天个人会员注册数
	 * @return
	 * @since 2017年11月29日
	 * @author zr.helinmei@yikuyi.com
	 */
	@RequestLine("GET /v1/account/getPersonalData?flag={flag}")
	public Long getPersonalData(@Param("flag") String flag);
	
	/**
	 * 获取个人当天注册数或者总数
	 * @param flag 不为空则查询当天个人会员注册数
	 * @return
	 * @since 2017年11月29日
	 * @author zr.helinmei@yikuyi.com
	 */
	@RequestLine("GET /v1/account/getLoginNumToday")
	public Long getLoginNumToday();

}