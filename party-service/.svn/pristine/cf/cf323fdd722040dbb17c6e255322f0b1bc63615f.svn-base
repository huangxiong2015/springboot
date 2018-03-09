package com.yikuyi.party.resource;

import com.ykyframework.exception.BusinessException;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * 权限相关的接口调用定义
 * 
 * @author jik.shu@yikuyi.com
 */
@Headers({ "Content-Type: application/json;charset=UTF-8" })
public interface RegisterClient {
	
	/**
	 * 加入主账号
	 * @param entId
	 * @param account
	 * @return
	 * @since 2017年5月5日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@RequestLine("POST /v1/customer/join?entId={entId}&account={account}&applyId={applyId}")
	public String joinMainAccount(@Param("entId") String entId, 
			@Param("account") String account,
			@Param("applyId") String applyId);
	
	/**
	 * 生成登陆账号（根据账号）
	 * @param partyId
	 * @param account
	 * @return
	 * @since 2017年7月28日
	 * @author zr.aoxianbing@yikuyi.com
	 * @throws BusinessException 
	 */
	@RequestLine("POST /v1/customer/upgrade?partyId={partyId}&account={account}")
	public void upgrade(@Param("partyId") String partyId, @Param("account") String account);
}