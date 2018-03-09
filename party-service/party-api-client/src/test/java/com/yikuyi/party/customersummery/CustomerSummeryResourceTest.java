package com.yikuyi.party.customersummery;

import org.junit.Assert;
import org.junit.Test;

import com.yikuyi.party.config.BaseTest;
import com.yikuyi.party.contact.vo.UserExtendVo;
import com.yikuyi.party.contact.vo.UserParamVo;

public class CustomerSummeryResourceTest extends BaseTest {

	/**
	 * 根据用户id查找用户信息
	 * @param partyId
	 * @param authToken
	 * @return
	 * @since 2017年6月29日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	public void getAccountById() {
		UserParamVo vo = super.partyClient.customerSummeryClient().getAccountByPartyId("9999999901");
		Assert.assertEquals(vo.getPartyId(),"9999999901");
	}
	
	
	/**
	 * 
	 * 账户基本信息
	 * @since 2017年8月17日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void accountSummaryTest(){
		UserExtendVo userExtendVo = super.partyClient.customerSummeryClient().accountSummary("YWRtaW46OTk5OTk5OTkwMQ==");
		Assert.assertEquals(userExtendVo.getPartyId(),"9999999901");
	}
}