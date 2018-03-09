package com.yikuyi.com.party.notice;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.yikuyi.party.config.BaseTest;
import com.yikuyi.party.partyExpand.model.PartyExpand;

public class NoticeResourceTest extends BaseTest{
	/**
	 * 根据用户ID,查询对应所有角色
	 * @since 2017年9月1日
	 * jik.shu@yikuyi.com
	 */
	@Test
	public void getPartyExpandList() {
		List<PartyExpand> list = super.partyClient.noticeClient().getPartyExpandList("9999999901","YWRtaW46OTk5OTk5OTkwMQ==");
		Assert.assertTrue(list.size() > 0);
	}
}
