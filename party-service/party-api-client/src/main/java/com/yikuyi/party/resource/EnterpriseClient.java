package com.yikuyi.party.resource;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.yikuyi.party.contact.vo.EnterpriseVo;
import com.yikuyi.party.credit.vo.PartyCreditVo;
import com.yikuyi.party.group.model.PartyGroup;
import com.yikuyi.party.group.model.PartyGroup.AccountPeriodStatus;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * 权限相关的接口调用定义
 * 
 * @author jik.shu@yikuyi.com
 */
@Headers({ "Content-Type: application/json;charset=UTF-8" })
public interface EnterpriseClient {
	
	/**
	 * 根据用户的id判断是否为管理员
	 * @param id
	 * @return
	 * @since 2017年1月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@RequestLine("GET /v1/enterprises/accounts/admin")
	@Headers({ "Authorization: Basic {authToken}" })
	public Boolean isAdmin(@Param("authToken") String authToken);
	/**
	 * 根据用户的id判断是否为首次去激活或者关联
	 * 
	 * @param id
	 * @return
	 * @since 2017年1月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@RequestLine("GET /v1/enterprises/accounts/isFristActive")
	@Headers({ "Authorization: Basic {authToken}" })
	public Boolean isFristActive(@Param("authToken") String authToken);
	
	/**
	 * 根据用户id查找公司
	 * @param role
	 * @param partyId
	 * @return
	 * @since 2017年7月17日
	 * @author zr.helinmei@yikuyi.com
	 */
	@RequestLine("GET /v1/enterprises/baseInfo?partyId={partyId}")
	public PartyGroup getEntBaseInfo(@Param("partyId") String partyId);
	
	@RequestLine("GET /v1/enterprises/{id}")
	@Headers({ "Authorization: Basic {authToken}" })
	public EnterpriseVo getEnterprise(@Param("id") String id,@Param("authToken") String authToken);
	
	/**
	 * 账期订单列表查询(带分页)
	 * @param partyCode
	 * @param groupName
	 * @param accountPeriodStatus
	 * @param page
	 * @param size
	 * @param authToken
	 * @return
	 * @since 2017年8月9日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@RequestLine("GET /v1/enterprises/credit?partyCode={partyCode}&groupName={groupName}"
			+ "&accountPeriodStatus={accountPeriodStatus}&page={page}&size={size}")
	@Headers({ "Authorization: Basic {authToken}" })
	public PageInfo<PartyCreditVo> getPartyCreditVoList(@Param("partyCode") String partyCode,
			@Param("groupName") String groupName,
			@Param("accountPeriodStatus") AccountPeriodStatus accountPeriodStatus,
			@Param("page") int page,
			@Param("size") int size,
			@Param("authToken") String authToken);
	
	
	/**
	 * 根据企业id(corporationId查询用户的账期信息)
	 * @param corporationId
	 * @param authToken
	 * @return
	 * @since 2017年8月17日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@RequestLine("GET /v1/enterprises/{corporationId}/credit")
	@Headers({ "Authorization: Basic {authToken}" })
	public PartyCreditVo getPartyCreditVoByCorporationId(@Param("corporationId") String corporationId,
			@Param("authToken") String authToken);
	
	
	/**
	 * 根据企业id查询partyIds
	 * @param corporationId
	 * @param authToken
	 * @return
	 * @since 2017年8月21日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@RequestLine("GET /v1/enterprises/{vipCorporationId}/enterprises")
	@Headers({ "Authorization: Basic {authToken}" })
	public List<String> getPartyIdList(@Param("vipCorporationId") String vipCorporationId,
			@Param("authToken") String authToken);
}