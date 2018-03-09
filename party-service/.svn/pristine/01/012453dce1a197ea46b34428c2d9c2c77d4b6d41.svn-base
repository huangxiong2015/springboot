package com.yikuyi.party.resource;

import org.springframework.web.bind.annotation.RequestBody;

import com.yikuyi.party.contact.vo.UserVo;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * 权限相关的接口调用定义
 * 
 * @author jik.shu@yikuyi.com
 */
@Headers({ "Content-Type: application/json;charset=UTF-8" })
public interface FindPasswordClient {
	
	/**
	 * 找回密码
	 * @param loginId
	 * @param vo
	 * @return
	 * @since 2017年1月11日
	 * @author tb.yumu@yikuyi.com
	 */
	@RequestLine("PUT v1/findpassword/{account}/password")
	@Headers({ "Authorization: Basic {authToken}" })
	public void reSetPassword(@Param("account") String account,@RequestBody UserVo vo,@Param("authToken") String authToken);
}