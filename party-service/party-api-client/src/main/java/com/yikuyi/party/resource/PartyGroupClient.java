package com.yikuyi.party.resource;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import com.yikuyi.party.contact.vo.MsgResultVo;
import com.yikuyi.party.group.vo.PartyGroupVo;
import com.yikuyi.party.vo.PartyVo;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * 权限相关的接口调用定义
 * 
 * @author zr.helinmei@yikuyi.com
 */

@Headers({ "Content-Type: application/json;charset=UTF-8" })
public interface PartyGroupClient {
	/**
	 * 购物车下定的权限控制
	 * 
	 * @param role
	 * @param partyId
	 * @return
	 * @since 2017年6月30日
	 * @author zr.helinmei@yikuyi.com
	 */
	@RequestLine("GET /v1/party/permissions?partyId={partyId}")
	@Headers({ "Authorization: Basic {authToken}" })
	public MsgResultVo orderPermissions(@Param("partyId") String partyId, @Param("authToken") String authToken);

	/**
	 * 物流公司
	 * <p>
	 * 请使用SupplierClient类
	 * </p>
	 * 
	 * @param role
	 * @param partyId
	 * @return
	 * @since 2017年6月30日
	 * @author zr.helinmei@yikuyi.com
	 */
	@RequestLine("PUT /v1/party/allparty")
	@Deprecated()
	public List<PartyVo> getAllPartyGroupList(@RequestBody PartyGroupVo vo);

	/**
	 * 物流公司仓库只查询状态为Y的记录
	 * 
	 * <p>
	 * 请使用SupplierClient类
	 * </p>
	 * 
	 * @param role
	 * @param partyId
	 * @return
	 * @since 2017年6月30日
	 * @author zr.helinmei@yikuyi.com
	 */
	@RequestLine("PUT /v1/party/partygroups")
	@Deprecated
	public List<PartyVo> partyGroups(@RequestBody PartyGroupVo vo);

}
