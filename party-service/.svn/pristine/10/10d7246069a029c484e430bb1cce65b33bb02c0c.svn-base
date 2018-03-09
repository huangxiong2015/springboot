package com.yikuyi.party.vendors;

import org.junit.Assert;
import org.junit.Test;

import com.yikuyi.party.config.BaseTest;
import com.yikuyi.party.model.PartyRelationship;
import com.yikuyi.party.model.PartyRelationship.PartyRelationshipTypeEnum;

public class VendorsResourceTest extends BaseTest {
	/**
	 * 查询分销商的父子关系
	 * @param id
	 * @param relationshipType
	 * @return
	 * @since 2017年8月11日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	public void getParentRelationInfo() {
		PartyRelationship relationship = super.partyClient.vendorsClient().getParentRelationInfo("188888888", PartyRelationshipTypeEnum.AFFILIATED_REL);
		Assert.assertEquals(relationship.getPartyIdTo(),"166666666");
	}
}
