package com.yikuyi.party.userlogin;

import org.junit.Assert;
import org.junit.Test;

import com.yikuyi.party.config.BaseTest;
import com.yikuyi.party.contact.vo.User;
import com.yikuyi.party.login.model.UserLogin;

public class UserLoginResourceTest extends BaseTest {

	/**
	 * 根据用户账户查找用户信息
	 * @param account
	 * @param authToken
	 * @return
	 * @since 2017年6月29日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	public void getAccountById() {
		UserLogin userLogin = super.partyClient.userLoginClient().getAccountById("admin","YWRtaW46OTk5OTk5OTkwMQ==");
		Assert.assertEquals(userLogin.getId(),"admin");
	}
	/**
	 * 根据账号判断是否存在（手机、邮箱）
	 * 
	 * @param account
	 * @return
	 * @since 2017年1月12日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	public void getAccount() {
		Boolean flag = super.partyClient.userLoginClient().getAccount("admin","YWRtaW46OTk5OTk5OTkwMQ==");
		Assert.assertEquals(true,flag);
	}
	
	/**
	 * 用户首次登陆监控
	 * 
	 * @param account
	 * @return
	 * @since 2017年8月30日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	public void getUserLoginListener() {
		User user = new User();
		super.partyClient.userLoginClient().userLoginListener(user, "YWRtaW46OTk5OTk5OTkwMQ==");
		
	}
}