
package com.yikuyi.party.common.utils;

import com.google.common.base.Objects;

public enum PartyActiveStatus {
	
		PARTY_NOT_VERIFIED("未申请"),
		WAIT_APPROVE("待审核"),
		REJECTED("驳回"),
		PARTY_VERIFIED("已认证"),
		INVALID("失效"),
		MAIN("主账号"),
		SON("子账号"),
		COMMON("平行账号"),
		PARTY_ENABLED("有效"),
		PARTY_DISABLED("冻结"),
		ACCOUNT_WAIT_APPROVE("待审核"),
		ACCOUNT_VERIFIED("审核通过"),
		ACCOUNT_NOT_VERIFIED("未审核"),
		ACCOUNT_REJECTED("驳回"),
		VALID("有效 "),
		NOT_VALID("无效"),
		INEFFECTIVE("未生效");
	
	
	
		private String activeStatusName;
		
		public String getActiveStatusName() {
			return activeStatusName;
		}
		private PartyActiveStatus(String activeStatusName) {
			this.activeStatusName = activeStatusName;
		}
		
		
		public static String enum2Desc(String enumName){
			for(PartyActiveStatus da : PartyActiveStatus.values())
			{
				if(Objects.equal(enumName,da.name())){
					return da.getActiveStatusName();
				}
			}
			return enumName;
		}
		
		
		
	

}