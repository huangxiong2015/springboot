package com.yikuyi.party.register;

import org.junit.Assert;
import org.junit.Test;

import com.yikuyi.party.config.BaseTest;
import com.ykyframework.exception.BusinessException;

public class RegisterResourceTest extends BaseTest {

	/**
	 * 加入主账号
	 * @param entId
	 * @param account
	 * @return
	 * @since 2017年5月5日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	public void joinMainAccount() {
		String falg = super.partyClient.registerClient(String.class).joinMainAccount("99999999", "admin", "1");
		Assert.assertEquals("mainAccount",falg);
	}
	/**
	 * 生成登陆账号（根据账号）
	 * @param partyId
	 * @param account
	 * @return
	 * @since 2017年7月28日
	 * @author zr.aoxianbing@yikuyi.com
	 * @throws BusinessException 
	 */
	@Test
	public void upgrade() {
		super.partyClient.registerClient().upgrade("9999999901", "test9999@126.com");
	}
}