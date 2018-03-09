/*
 * Created: 2016年9月26日
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
package com.ykyframework.mqservice.receiver;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ykyframework.mqservice.MQException;

/**
 * @author liudian@yikuyi.com
 * @version 1.0.0
 */
public abstract class AbstractMsgReceiver implements MsgReceiver {

	private static final Logger logger = LoggerFactory.getLogger(AbstractMsgReceiver.class);

	private List<SimpleMQSubscription> subscriptions = new ArrayList<>();

	/**
	 * @return the subscriptions
	 */
	public List<SimpleMQSubscription> getSubscriptions() {
		return subscriptions;
	}

	/**
	 * @param subscriptions the subscriptions to set
	 */
	public void setSubscriptions(List<SimpleMQSubscription> subscriptions) {
		this.subscriptions = subscriptions;
	}
	
	/**
	 * 初始化的方法
	 * @throws MQException
	 */
	public void init() throws MQException {
		
		for ( final SimpleMQSubscription subscription: this.subscriptions){
			this.registerSubscription(subscription);
		}
		logger.info("MQReceiver {} init completed!", this);
	}
	
	/**
	 * 销毁的方法
	 * 
	 * @since 2016年9月26日
	 * @author liudian@yikuyi.com
	 */
	public abstract void destroy();
	
	/**
	 * 注册订阅关系
	 * @param subs
	 * @throws MQException
	 * @since 2017年4月13日
	 * @author liudian@yikuyi.com
	 */
	public abstract void registerSubscription(SimpleMQSubscription subs) throws MQException;

}
