package com.yikuyi.party.partyGroup;


import org.junit.Assert;
import org.junit.Test;

import com.yikuyi.party.config.BaseTest;
import com.yikuyi.party.contact.vo.MsgResultVo;

public class PartyGroupResourceTest extends BaseTest{
	
	/**
	 * 购物车下定的权限控制 
	 * @since 2017年9月1日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	public void orderPermissionsTest() {
		MsgResultVo msg = super.partyClient.partyGroupClient().orderPermissions("9999999901", "YWRtaW46OTk5OTk5OTkwMQ==");
		boolean msgResult= true;
		if(msg!=null){
			msgResult = true;
		}else{
			msgResult=false;
		}
		Assert.assertTrue(msgResult);
	}
	
}
