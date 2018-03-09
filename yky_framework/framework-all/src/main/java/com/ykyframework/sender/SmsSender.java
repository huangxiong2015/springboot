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
 * 短信发送器
 * @author liaoke@yikuyi.com
 * @version 1.0.0
 */
public interface SmsSender {
	/**
	 * 生成验证码
	 * @return
	 * @since 2016年3月12日
	 * @author liaoke@yikuyi.com
	 */
	public String genVerifyCode();
	
	/**
	 * 发送短信
	 * @param mobile
	 * @param content
	 * @since 2016年3月12日
	 * @author liaoke@yikuyi.com
	 */
	public boolean send(String mobile, String content);

}
