package com.yikuyi.party.findpassword;

import org.junit.Test;

import com.yikuyi.party.config.BaseTest;
import com.yikuyi.party.contact.vo.UserVo;

public class FindPasswordResourceTest extends BaseTest {

	/**
	 * 找回密码
	 * @param loginId
	 * @param vo
	 * @return
	 * @since 2017年1月11日
	 * @author tb.yumu@yikuyi.com
	 */
	@Test
	public void reSetPassword() {
		UserVo vo = new UserVo();
		vo.setNewPassword("123456");
		super.partyClient.findPasswordClient().reSetPassword("bWFsYW4=", vo, "YWRtaW46OTk5OTk5OTkwMQ==");
	}
}