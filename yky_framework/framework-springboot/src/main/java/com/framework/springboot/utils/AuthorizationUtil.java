package com.framework.springboot.utils;

import java.util.Base64;

import com.framewrok.springboot.web.RequestHelper;

public class AuthorizationUtil {

	/**
	 * 获取当前登录用户真实authToken
	 * 
	 * @return
	 */
	public String getLoginAuthorization() {
		return Base64.getEncoder().encodeToString((RequestHelper.getLoginUser().getUsername() + ":" + RequestHelper.getLoginUser().getId()).getBytes());
	}

	/**
	 * 获取mock的authToken
	 * 
	 * @return
	 */
	public String getMockAuthorization() {
		return Base64.getEncoder().encodeToString(("restTemplateUser:9999999988").getBytes());
	}

	/**
	 * 获取mock的真实authToken
	 * 
	 * @return
	 */
	public String mockRealAuthorization(String loginName, String partyId) {
		return Base64.getEncoder().encodeToString((loginName + ":" + partyId).getBytes());
	}
}