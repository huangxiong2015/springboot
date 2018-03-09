package com.yikuyi.party.contactMech;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.yikuyi.party.config.BaseTest;
import com.yikuyi.party.contact.model.ContactMech;

public class ContactMechResourceTest extends BaseTest {

	/**
	 * 根据地址ID查询地址详情
	 * @since 2017年9月1日
	 * @author 
	 */
	@Test
	public void getContactMechListTest() {
		List<ContactMech> contactMechList = super.partyClient.contactMechClient().getContactMechList("10002","YWRtaW46OTk5OTk5OTkwMQ==");
		Assert.assertTrue(contactMechList.size() > 0);
	}
}