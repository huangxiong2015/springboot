package com.yikuyi.party.resource;

import java.util.List;

import com.yikuyi.party.partyExpand.model.PartyExpand;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
/**
 * 订单通知
 * 
 * @author zr.helinmei@yikuyi.com
 */
@Headers({ "Content-Type: application/json;charset=UTF-8" })
public interface NoticeClient {
	
	/**
	 * 查询订单通知
	 * @param partyId
	 * @return
	 * @since 2017年11月30日
	 * @author zr.helinmei@yikuyi.com
	 */
	@RequestLine("GET /v1/notice?partyId={partyId}")
	@Headers({ "Authorization: Basic {authToken}" })
	public List<PartyExpand> getPartyExpandList(@Param("partyId") String partyId,@Param("authToken") String authToken);
	
}
