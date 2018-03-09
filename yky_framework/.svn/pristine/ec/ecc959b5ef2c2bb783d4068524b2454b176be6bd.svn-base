/*
 * Created: 2016年3月12日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ykyframework.sender;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.ykyframework.exception.ExceptionResponse;

public class GzbxSmsSenderTest {
	private final static String URL = "http://112.74.128.141:7888/sms.aspx?"; // 地址
	private final static String SMS_USER_ID = "119"; // 用户ID
	private final static String SMS_ACCOUNT = "易库易"; // 用户名
	private final static String SMS_PWD = "yky123";// 密码
	
	@Test
	public void testSend() {
		GzbxSmsSender s = new GzbxSmsSender();
		s.setUrl(URL);
		s.setUserId(SMS_USER_ID);
		s.setAccount(SMS_ACCOUNT+1);
		s.setPassword(SMS_PWD);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//先用错误的密码发
		assertTrue(!s.send("13802701185", "【易库易】" + sdf.format(new Date()) + "junit 测试短信 验证码"  + s.genVerifyCode()));
		//再用正确的密码发
		s.setAccount(SMS_ACCOUNT);
		//assertTrue(s.send("13802701185", "【易库易】" + sdf.format(new Date()) + "junit 测试短信 验证码"  + s.genVerifyCode()));
	}


}
