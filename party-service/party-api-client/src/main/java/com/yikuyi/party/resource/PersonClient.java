package com.yikuyi.party.resource;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import com.yikuyi.party.contact.vo.UserExtendVo;
import com.yikuyi.party.contact.vo.UserParamVo;
import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.party.credit.model.PartyCredit;
import com.yikuyi.party.credit.vo.PartyCreditVo;
import com.yikuyi.party.login.model.UserLogin;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.person.model.Person;
import com.yikuyi.party.vendor.vo.Vendor.Currency;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * 个人信息的接口调用定义
 * 
 * @author jik.shu@yikuyi.com
 */
@Headers({ "Content-Type: application/json;charset=UTF-8" })
public interface PersonClient {

	
	/**
	 * 根据用户Id获取person信息
	 * @param id
	 * @param authToken
	 * @return
	 * @since 2017年6月29日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@RequestLine("GET /v1/customers/{id}/username")
	@Headers({ "Authorization: Basic {authToken}" })
	public Person findPersonInfoByPartyId(@Param("id") String id,@Param("authToken") String authToken);
	
	
	
	/**
	 * 根据用户账户查找用户信息
	 * @param account
	 * @param authToken
	 * @return
	 * @since 2017年6月29日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@RequestLine("GET /v1/account/id?account={account}")
	@Headers({ "Authorization: Basic {authToken}" })
	public UserLogin getAccountById(@Param("account") String account,@Param("authToken") String authToken);
	
	/**
	 * 根据partyid和类型查询用户
	 * @param id
	 * @param type
	 * @param authToken
	 * @return
	 * @since 2017年6月29日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@RequestLine("GET /v1/account/user?id={id}&type={type}")
	public String getAccountByIdAndType(@Param("id") String id, @Param("type") String type);
	
	/**
	 * 批量查询用户
	 * @param ids
	 * @param authToken
	 * @return
	 * @since 2017年6月29日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@RequestLine("POST /v1/person/batch")
	@Headers({ "Authorization: Basic {authToken}" })	
	public List<UserVo> getPartyByIds(@RequestBody List<String> ids,@Param("authToken") String authToken);
	
	/**
	 * 获取用户详情
	 * @param id   用户Id
	 * @param loginId  登录Id
	 * @param authToken
	 * @return
	 * @since 2017年6月29日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@RequestLine("GET /v1/customers/{id}?loginId={loginId}")
	@Headers({ "Authorization: Basic {authToken}" })
	public UserExtendVo getBaseInfoDetail(@Param("id") String id,@Param("loginId") String loginId,@Param("authToken") String authToken);
	
	/**
	 * 根据用户名称查询用户列表（模糊查询）
	 * @param username
	 * @param authToken
	 * @return
	 * @since 2017年6月29日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@RequestLine("GET /v1/customers/username?username={username}")
	@Headers({ "Authorization: Basic {authToken}" })
	public List<UserVo> getUserByName(@Param("username") String username,@Param("authToken") String authToken);
	
	/**
	 * 根据用户名称查询用户列表（精确查询）
	 * @param name
	 * @param authToken
	 * @return
	 * @since 2017年6月29日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@RequestLine("GET /v1/customers/name?name={name}")
	@Headers({ "Authorization: Basic {authToken}" })
	public UserVo getUsersByName(@Param("name") String name,@Param("authToken") String authToken);
	
	/**
	 * 根据用户ID获取登录密码，登录ID
	 * @param id  用户Id
	 * @param authToken
	 * @return
	 * @since 2017年6月29日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@RequestLine("GET /v1/customers/{id}/login")
	@Headers({ "Authorization: Basic {authToken}" })
	public UserLogin findUserLogin(@Param("id") String id,@Param("authToken") String authToken);
	
	/**
	 * 根据角色类型获取所有个人用户的邮箱
	 * @param roleType
	 * @return
	 * @since 2017年3月23日
	 * @author gongtianyu@yikuyi.com
	 */
	@RequestLine("GET /v1/person/all?roleType={roleType}")
	@Headers({ "Authorization: Basic {authToken}" })
	public List<Person> getEmailListByRoleType(@Param("roleType") String roleType,@Param("authToken") String authToken);
	
	/**
	 * 根据partyId查询账期信息
	 * @param partyId
	 * @param currency
	 * @param authToken
	 * @return
	 * @since 2017年8月17日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@RequestLine("GET /v1/person/{partyId}/credit?currency={currency}")
	@Headers({ "Authorization: Basic {authToken}" })
	public PartyCreditVo getPartyCreditInfo(@Param("partyId") String partyId,
			@Param("currency") Currency currency,
			@Param("authToken") String authToken);
	
	
	/**
	 * 批量查询用户的账期信息
	 * @param partyId
	 * @param currency
	 * @param authToken
	 * @return
	 * @since 2017年8月17日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@RequestLine("POST /v1/person/credit/list")
	@Headers({ "Authorization: Basic {authToken}" })
	public List<PartyCreditVo> getPartyCreditInfoList(@RequestBody String partyList,@Param("authToken") String authToken);
	
	/**
	 * 根据partyId修改用户的账期信息
	 * @param partyCredit
	 * @since 2017年8月17日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@RequestLine("PUT /v1/person/credit")
	@Headers({ "Authorization: Basic {authToken}" })
	public void updatePartyCredit(@RequestBody PartyCredit partyCredit,@Param("authToken") String authToken);
	
	
	/**
	 * 根据角色获取相关用户信息
	 * @param roleList
	 * @param authToken
	 * @return
	 * @since 2017年9月4日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@RequestLine("POST /v1/person/role")
	@Headers({ "Authorization: Basic {authToken}" })
	public List<Party> findDataByRole(@RequestBody List<String> roleList,@Param("authToken") String authToken);
	
	
	@RequestLine("POST /v1/customers/getCustomersByIds")
	@Headers({ "Authorization: Basic {authToken}" })	
	public List<UserParamVo> getCustomersByIds(@RequestBody List<String> ids,@Param("authToken") String authToken);

	
}