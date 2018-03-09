package com.yikuyi.party.carrier;

import org.junit.Assert;
import org.junit.Test;

import com.github.pagehelper.PageInfo;
import com.yikuyi.party.config.BaseTest;
import com.yikuyi.party.vo.PartyCarrierVo;

public class CarrierResourceTest extends BaseTest {

	
	/**
	 * 
	 * 查询物流信息列表
	 * @since 2017年8月2日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void getPartyCarrierListTest() {
		PageInfo<PartyCarrierVo> pageInfo = super.partyClient.carrierClient().getPartyCarrierList(null, null, null, null, null, null, 1, 10, "YWRtaW46OTk5OTk5OTkwMQ==");
		Assert.assertTrue(pageInfo.getList().size() > 0);
	}
	
	
	/**
	 * 根据partyId查询相应的物流信息
	 * 
	 * @since 2017年8月2日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void getPartyCarrierVoInfoTest() {
		PartyCarrierVo  carrierVo = super.partyClient.carrierClient().getPartyCarrierVoInfo("889673708757581824", "YWRtaW46OTk5OTk5OTkwMQ==");
		if(carrierVo!=null){
			Assert.assertEquals(carrierVo.getPartyId(), "889673708757581824");
		}
	}
	
	/**
	 * 
	 *  物流公司信息-启用/停用
	 * @since 2017年8月2日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void updateStateIdTest() {
		PartyCarrierVo partyCarrierVo = new PartyCarrierVo();
		partyCarrierVo.setPartyId("889673708757581824");
		partyCarrierVo.setGroupName("顺丰");
		super.partyClient.carrierClient().updateStateId("889673708757581824", partyCarrierVo, "YWRtaW46OTk5OTk5OTkwMQ==");
	}
	
	
	
}