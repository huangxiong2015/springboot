package com.yikuyi.party.user;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.Base64Utils;

import com.yikuyi.party.config.BaseTest;
import com.yikuyi.party.contact.vo.User;
import com.yikuyi.party.model.PartyAttribute;

public class UserResourceTest extends BaseTest{
	
	
	/**
	 * 根据userId,获取用户和企业信息
	 * 
	 * @since 2017年6月30日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void getUserInfoTest() {
		User user = super.partyClient.userClient().getUserInfo("9999999901", "YWRtaW46OTk5OTk5OTkwMQ==");
		Assert.assertEquals("9999999901", user.getId());
	}
	
	
	/**
	 * 根据用户Id获取person信息
	 * 
	 * @since 2017年6月29日
	 * @author tb.yumu@yikuyi.com
	 */
	@Test
	public void getUserAttributesTest() {
		PartyAttribute attr = super.partyClient.userClient().getUserAttributes("111", "test", "YWRtaW46OTk5OTk5OTkwMQ==");
		boolean isExistsAttr = true;
		if(attr!=null){
			isExistsAttr = true;
		}else{
			isExistsAttr=false;
		}
		Assert.assertTrue(isExistsAttr);
	}
	
	/**
	 * 购物车下定的权限控制 
	 * @param role
	 * @param partyId
	 * @return
	 * @since 2017年6月30日
	 * @author zr.helinmei@yikuyi.com
	 */

	@Test
	public void getUserByAccountTest() {
		String userId = super.partyClient.userClient(String.class).getUserByAccount(Base64Utils.encodeToString("admin".getBytes()));
		boolean isExists = true;
		if(userId!=null){
			isExists = true;
		}else{
			isExists=false;
		}
		Assert.assertTrue(isExists);
	}
}
