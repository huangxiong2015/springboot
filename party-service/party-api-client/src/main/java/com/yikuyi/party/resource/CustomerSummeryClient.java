package com.yikuyi.party.resource;

import com.yikuyi.party.contact.vo.CustomersInfoVo;
import com.yikuyi.party.contact.vo.UserExtendVo;
import com.yikuyi.party.contact.vo.UserParamVo;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * 权限相关的接口调用定义
 * 
 * @author jik.shu@yikuyi.com
 */
@Headers({ "Content-Type: application/json;charset=UTF-8" })
public interface CustomerSummeryClient {


	/**
	 * 根据用户id查找用户信息
	 * @param partyId
	 * @param authToken
	 * @return
	 * @since 2017年6月29日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@RequestLine("GET /v1/customersummery/{partyId}/info")
	public UserParamVo getAccountByPartyId(@Param("partyId") String partyId);
	
	
	/**
	 * 账户基本信息
	 * @return
	 * @since 2017年8月17日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@RequestLine("GET /v1/customersummery/summary")
	@Headers({ "Authorization: Basic {authToken}" })
	public UserExtendVo accountSummary(@Param("authToken") String authToken);
	
	

	/**
	 * 根据用户id查找用户信息
	 * @param partyId
	 * @param authToken
	 * @return
	 * @since 2018年1月9日
	 * @author zr.helinmei@yikuyi.com
	 */
	@RequestLine("GET /v1/customersummery/{partyId}/customersInfo")
	public CustomersInfoVo getCustomersInfoById(@Param("partyId") String partyId);

}