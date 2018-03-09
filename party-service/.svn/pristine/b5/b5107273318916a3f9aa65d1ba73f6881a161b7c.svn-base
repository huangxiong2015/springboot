package com.yikuyi.party.resource;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import com.yikuyi.party.contact.model.ContactMech;
import com.yikuyi.party.model.PartyContactMech;
import com.yikuyi.party.model.PartyContactMech.PurposeType;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * 权限相关的接口调用定义
 * 
 * @author jik.shu@yikuyi.com
 */
@Headers({ "Content-Type: application/json;charset=UTF-8" })
public interface PartyAddressClient {

	/**
	 * 查询地址信息
	 * 
	 * @param purposeType
	 * @param userId
	 * @param currency
	 * @param authToken
	 * @return
	 * @since 2017年6月29日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@RequestLine("GET /v1/address?purposeType={purposeType}&userId={userId}&currency={currency}")
	@Headers({ "Authorization: Basic {authToken}" })
	public List<PartyContactMech> getShipAddressList(@Param("purposeType") PurposeType purposeType, @Param("userId") String userId, @Param("currency") String currency, @Param("authToken") String authToken);

	/**
	 * 批量查询收货地址
	 * 
	 * @param contactMechIds
	 * @param authToken
	 * @return
	 * @since 2017年7月25日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@RequestLine("POST /v1/address/batch")
	@Headers({ "Authorization: Basic {authToken}" })
	public List<ContactMech> getBatchAddressList(@RequestBody List<String> contactMechIds, @Param("authToken") String authToken);

}