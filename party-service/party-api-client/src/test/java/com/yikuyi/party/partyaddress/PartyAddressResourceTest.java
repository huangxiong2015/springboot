package com.yikuyi.party.partyaddress;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.yikuyi.party.config.BaseTest;
import com.yikuyi.party.contact.model.ContactMech;
import com.yikuyi.party.model.PartyContactMech;
import com.yikuyi.party.model.PartyContactMech.PurposeType;

public class PartyAddressResourceTest extends BaseTest {

	/**
	 * 查询地址信息
	 * @param purposeType
	 * @param userId
	 * @param currency
	 * @param authToken
	 * @return
	 * @since 2017年6月29日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	public void getShipAddressList() {
		List<PartyContactMech> list = super.partyClient.partyAddressClient().getShipAddressList(PurposeType.REGISTER_LOCATION, "9999999901", null, "YWRtaW46OTk5OTk5OTkwMQ==");
		Assert.assertTrue(list.size() > 0);
	}
	
	/**
	 * 批量查询收货地址
	 * @since 2017年7月25日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void getBatchddressList(){
		List<String> mechIds = new ArrayList<String>();
		mechIds.add("855272640573603840");
		mechIds.add("832430906533740544");
		List<ContactMech> mechlist = super.partyClient.partyAddressClient().getBatchAddressList(mechIds,"YWRtaW46OTk5OTk5OTkwMQ==");
		Assert.assertTrue(mechlist.size() > 0);
	}
}