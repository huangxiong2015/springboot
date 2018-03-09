package com.yikuyi.party.userLogin.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.framewrok.springboot.web.RequestHelper;
import com.yikuyi.party.contact.vo.AccountVo;
import com.yikuyi.party.contact.vo.User;
import com.yikuyi.party.contact.vo.UserExtendVo;
import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.party.login.model.UserLogin;
import com.yikuyi.party.userLogin.api.IUserLoginResource;
import com.yikuyi.party.userLogin.bll.UserLoginManager;

/**
 * 定义收货地址的相关接口
 * 
 * @author zr.aoxianbing@yikuyi.com
 *
 */

@RestController
@RequestMapping("v1/account")
public class UserLoginResource implements IUserLoginResource {

	@Autowired
	private UserLoginManager userLoginManager;
	
	/**
	 * 根据账号判断是否存在（手机、邮箱）
	 * 
	 * @param account
	 * @return
	 * @since 2017年1月12日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/{account}", method = RequestMethod.GET)
	public Boolean isExist(@PathVariable("account") String account) {
		return userLoginManager.isExist(account);
	}
	/**
	 * 根据账号判断是否存在（手机、邮箱）
	 * 
	 * @param account
	 * @return
	 * @since 2017年1月12日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/mail", method = RequestMethod.GET)
	public Boolean getAccount(@RequestParam(value = "account", required = true) String account) {
		return userLoginManager.getAccount(account);
	}
	/**
	 * 修改账号
	 * 
	 * @param account
	 * @return
	 * @since 2017年1月12日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/account", method = RequestMethod.PUT)
	public String updateAccount(@RequestBody AccountVo accountVo) {
		
		String partyId = RequestHelper.getLoginUserId();
		if(StringUtils.isEmpty(accountVo.getPartyId())){
			accountVo.setPartyId(partyId);
		}
		return userLoginManager.updateAccount(accountVo);
	}
	/**
	 * 发送邮件（不登陆验证账号）
	 * @param userVo
	 * @since 2017年2月9日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/sendMail", method = RequestMethod.POST)
	public String sendMail(@RequestBody UserVo userVo) {
		return userLoginManager.sendMail(userVo);
	}
	/**
	 * 发送邮件（不登陆验证账号）
	 * @param userVo
	 * @since 2017年2月9日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/sendCreateMail", method = RequestMethod.POST)
	public String sendCreateMail(@RequestBody UserVo userVo) {
		return userLoginManager.sendCreateMail(userVo);
	}
	/**
	 * 更新用户信息
	 * 
	 * @param userInfoVo
	 * @since 2017年2月14日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/updatePerson",method = RequestMethod.PUT)
	public void updatePerson(@RequestBody UserExtendVo userInfoVo) {
		userLoginManager.updatePerson(userInfoVo);
	}
	
	
	/**
	 * 初始化密码
	 * 
	 * @param account
	 * @return
	 * @since 2017年1月12日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/{account}", method = RequestMethod.PUT)
	public UserVo initPassWord(@RequestBody UserVo userVo) {
		return userLoginManager.initPassWord(userVo);
	}

	/**
	 * 根据账号id查询用户
	 * 
	 * @param account
	 * @return
	 * @since 2017年1月12日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/id", method = RequestMethod.GET)
	public UserLogin getAccountById(@RequestParam("account") String account) {
		return userLoginManager.getAccountById(account);
	}

	/**
	 * 根据partyid和类型查询用户
	 * 
	 * @param account
	 * @return
	 * @since 2017年1月12日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public String getAccountByIdAndType(@RequestParam("id") String id, @RequestParam("type") String type) {
		return userLoginManager.getAccountByIdAndType(id, type);
	}
	
	/**
	 * 监控用户是否为首次登陆
	 * 
	 * @param account
	 * @return
	 * @since 2017年8月29日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/userLoginListener", method = RequestMethod.POST)
	public void userLoginListener(@RequestBody User user) {
		userLoginManager.userLoginListener(user);
	}
	
	/**
	 * 获取个人当天注册数或者总数
	 * @param flag 不为空则查询当天个人会员注册数
	 * @return
	 * @since 2017年11月29日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/getPersonalData", method = RequestMethod.GET)
	public Long getPersonalData(@RequestParam(value = "flag" , required = false)String flag) {
		return userLoginManager.getPersonalData(flag);
	}

	/**
	 * 获取认证会员当天数或者总数
	 * @param flag 不为空则查询当天认证会员数
	 * @return
	 * @since 2017年11月29日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/getAuthenticationData", method = RequestMethod.GET)
	public Long getAuthenticationData(@RequestParam(value = "flag" , required = false)String flag) {
		return userLoginManager.getAuthenticationData(flag);
	}
	/**
	 * 获取当天登陆数
	 * @return
	 * @since 2017年11月29日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/getLoginNumToday", method = RequestMethod.GET)
	public Long getLoginNumToday() {
		return userLoginManager.getLoginNumToday();
	}

	
}
