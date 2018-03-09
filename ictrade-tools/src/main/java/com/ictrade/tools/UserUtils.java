/*
 * Created: 2017年6月30日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ictrade.tools;

import java.util.Base64;
import org.apache.commons.lang.StringUtils;

/**
 * 用户工具类
 * @author tongkun@yikuyi.com
 * @version 1.0.0
 */
public class UserUtils {
	
	/**
	 * 获取验证token
	 * @param userId 用户id
	 * @param loginAccount 登录名
	 * @param mobile 手机
	 * @param mail 邮箱
	 * @return
	 * @since 2017年6月30日
	 * @author tongkun@yikuyi.com
	 */
	public static String getAuthToken(String userId,String loginAccount,String mobile,String mail){
        String username = StringUtils.isEmpty(loginAccount) ? (StringUtils.isEmpty (mobile)? mail:mobile) : loginAccount;
        String base64LoginName = Base64.getEncoder().encodeToString((username+":"+userId).getBytes());
        return "Basic " + base64LoginName ;                
	}
}
