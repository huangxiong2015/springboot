package com.yikuyi.party.credit.vo;

import com.yikuyi.party.group.model.PartyGroup.AccountPeriodStatus;
import com.yikuyi.party.model.PartyRelationship;

/**
 * 
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
public class PartyCreditParamVo {
	
	
	//YKY客户编码
	private String partyCode;
	
	//公司名称
	private String groupName;
	
	//账期状态
	private AccountPeriodStatus accountPeriodStatus;
	
	
	private PartyRelationship partyRelationship;
	
	


	public String getPartyCode() {
		return partyCode;
	}

	public void setPartyCode(String partyCode) {
		this.partyCode = partyCode;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public AccountPeriodStatus getAccountPeriodStatus() {
		return accountPeriodStatus;
	}

	public void setAccountPeriodStatus(AccountPeriodStatus accountPeriodStatus) {
		this.accountPeriodStatus = accountPeriodStatus;
	}

	public PartyRelationship getPartyRelationship() {
		return partyRelationship;
	}

	public void setPartyRelationship(PartyRelationship partyRelationship) {
		this.partyRelationship = partyRelationship;
	}
	
	

	
}
