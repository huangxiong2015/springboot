package com.yikuyi.party.resource;
import com.yikuyi.party.contact.vo.User;
import com.yikuyi.party.model.PartyAttribute;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * 权限相关的接口调用定义
 * 
 * @author zr.helinmei@yikuyi.com
 */

@Headers({ "Content-Type: application/json;charset=UTF-8" })
public interface UserClient {
	
	
	/**
	 * 根据userId,获取用户和企业信息
	 * @param userId
	 * @param authToken
	 * @return
	 * @since 2017年6月29日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@RequestLine("GET /v1/users/{userId}")
	@Headers({ "Authorization: Basic {authToken}" })
	public User getUserInfo(@Param("userId") String userId,@Param("authToken") String authToken);
	
	/**
	 * 购物车下定的权限控制 
	 * @param role
	 * @param partyId
	 * @return
	 * @since 2017年6月30日
	 * @author zr.helinmei@yikuyi.com
	 */
	@RequestLine("GET /v1/users/validated/{username}")
	public String getUserByAccount(@Param("username") String username);
	
	/**
	 * 根据用户Id,获取用户属性信息
	 * 
	 * @param username
	 * @return
	 */
	@RequestLine("GET /v1/users/{userId}/attributes/{attrName}")
	@Headers({ "Authorization: Basic {authToken}" })
	public PartyAttribute getUserAttributes(@Param("userId") String userId, @Param("attrName") String attrName,@Param("authToken") String authToken);
	

}
