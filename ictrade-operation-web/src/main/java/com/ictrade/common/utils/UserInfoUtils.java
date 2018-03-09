/*
 * Created: 2016年2月3日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
/**
 * 
 */
package com.ictrade.common.utils;

import java.util.Base64;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.yikuyi.party.contact.vo.User;

/**
 * 获取用户信息的临时帮助类，后续再优化
 * @author tangrong@yikuyi.com
 * @version 1.0.0
 */
public class UserInfoUtils {

	public static User getUserInfo(){	
		Subject subject = getSubject();
		if(subject == null){
			return null;
		}			
		User principal = (User)subject.getPrincipal();
		return principal;
	}
	
	public static boolean isUserLogin(){	
		Subject subject = getSubject();
		if(subject==null){
			return false;
		}
		return subject.isAuthenticated();
	}
		
	
	public static Subject getSubject(){
		return SecurityUtils.getSubject();
	}
	
	
	/**
	 * 获取当前登录用户真实authToken
	 * 
	 * @return
	 */
	public static String getLoginAuthorization() {
		if (null == getUserInfo()) {
			return getMockAuthorization();
		}
		return getUserInfo().getLoginName();
	}

	/**
	 * 获取mock的authToken
	 * 
	 * @return
	 */
	public static String getMockAuthorization() {
		return Base64.getEncoder().encodeToString(("restTemplateUser:9999999988").getBytes());
	}
}
