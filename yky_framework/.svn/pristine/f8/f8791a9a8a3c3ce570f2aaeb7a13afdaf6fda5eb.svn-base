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

/**
 * 本地模拟的短信发送器，使用固定的验证码，但是并不发送。测试环境使用
 * @author liaoke@yikuyi.com
 * @version 1.0.0
 */
public class MockSmsSender implements SmsSender {

	@Override
	public String genVerifyCode() {
		return "123456";
	}

	@Override
	public boolean send(String mobile, String content) {
		// do nothing
		return true;
	}

}
