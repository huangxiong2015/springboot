package com.yikuyi.party.vo;

import com.yikuyi.party.model.Party.PartyStatus;


public class PartyCarrierParamVo{

	
	
	private String partyId;  
	
	private String groupName; 
	
	private PartyStatus partyStatus; 
	
	private String createDateStart;

	public String getCreateDateEnd() {
		return createDateEnd;
	}

	public void setCreateDateEnd(String createDateEnd) {
		this.createDateEnd = createDateEnd;
	}

	private String createDateEnd;
	
	private String lastUpdateDateStart;
	
	private String lastUpdateDateEnd;
	
	
	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public PartyStatus getPartyStatus() {
		return partyStatus;
	}

	public void setPartyStatus(PartyStatus partyStatus) {
		this.partyStatus = partyStatus;
	}

	public String getCreateDateStart() {
		return createDateStart;
	}

	public void setCreateDateStart(String createDateStart) {
		this.createDateStart = createDateStart;
	}

	public String getLastUpdateDateStart() {
		return lastUpdateDateStart;
	}

	public void setLastUpdateDateStart(String lastUpdateDateStart) {
		this.lastUpdateDateStart = lastUpdateDateStart;
	}

	public String getLastUpdateDateEnd() {
		return lastUpdateDateEnd;
	}

	public void setLastUpdateDateEnd(String lastUpdateDateEnd) {
		this.lastUpdateDateEnd = lastUpdateDateEnd;
	}

	

	

	

	



}
