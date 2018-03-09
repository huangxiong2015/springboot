/*
 * Created: 2016年4月1日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2015 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ykyframework.mqservice.sender;

import java.io.Serializable;
import java.util.Map;

/**
 * @author liudian@yikuyi.com
 * 本地模拟发送MQ消息
 */
public class MockMsgSender implements MsgSender {

	/* (non-Javadoc)
	 * @see com.ykyframework.mqservice.sender.MsgSender#sendMsgNoTransaction(java.lang.String, java.io.Serializable, java.util.Map)
	 */
	@Override
	public boolean sendMsgNoTransaction(String queueName, Serializable msg, Map<String, String> props) {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.ykyframework.mqservice.sender.MsgSender#sendOrderedMsgNoTransaction(java.lang.String, java.io.Serializable, java.util.Map, java.io.Serializable)
	 */
	@Override
	public boolean sendOrderedMsgNoTransaction(String queueName, Serializable msg, Map<String, String> props,
			Serializable shardBy) {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.ykyframework.mqservice.sender.MsgSender#sendMsg(java.lang.String, java.io.Serializable, java.util.Map)
	 */
	@Override
	public void sendMsg(String queueName, Serializable msg, Map<String, String> props) {
		return;

	}

	/* (non-Javadoc)
	 * @see com.ykyframework.mqservice.sender.MsgSender#sendOrderedMsg(java.lang.String, java.io.Serializable, java.util.Map, java.io.Serializable)
	 */
	@Override
	public void sendOrderedMsg(String queueName, Serializable msg, Map<String, String> props, Serializable shardBy) {
		return;
	}
	
	public void init(){
		return;
	}
	
	public void destroy() {
		return;
	}

}
