/*
 * Created: 2016年3月21日
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
 * 向队列发送消息的接口
 * @author liudian@yikuyi.com
 *
 */
public interface MsgSender {
	
	/**
	 * 发送消息到消息服务器
	 * @param queueName 拟发送到的队列名
	 * @param msg 待发送的消息对象
	 * @param props 拟在消息中加入的可跟踪元素，只在消息传送过程中使用，不会传送给消费者使用
	 * @return ture 表示发送成功，false表示失败
	 */
	public boolean sendMsgNoTransaction(String queueName, Serializable msg, Map<String, String> props);
	
	
	/**
	 * 发送顺序消息
	 * @param queueName 拟发送到的队列名
	 * @param msg 待发送的消息对象
	 * @param props 拟在消息中加入的可跟踪元素，只在消息传送过程中使用，不会传送给消费者使用
	 * @param shardBy 一个排序的分组依据（绑定相同对象的消息是顺序的）
	 * @return
	 */
	public boolean sendOrderedMsgNoTransaction(String queueName, Serializable msg, Map<String, String> props, Serializable shardBy);
	
	/**
	 * 发送消息到消息服务器
	 * @param queueName 拟发送到的队列名
	 * @param msg 待发送的消息对象
	 * @param props 拟在消息中加入的可跟踪元素，只在消息传送过程中使用，不会传送给消费者使用
	 * @throws Exception 发送过程中异常
	 */
	public void sendMsg(String queueName, Serializable msg, Map<String, String> props);
	
	/**
	 * 发送消息到消息服务器
	 * @param queueName 拟发送到的队列名
	 * @param msg 待发送的消息对象
	 * @param props 拟在消息中加入的可跟踪元素，只在消息传送过程中使用，不会传送给消费者使用
	 * @param shardBy 一个排序的分组依据（绑定相同对象的消息是顺序的）
	 * @throws Exception 发送过程中异常
	 */
	public void sendOrderedMsg(String queueName, Serializable msg, Map<String, String> props, Serializable shardBy);

}
