/*
 * Created: 2017年3月6日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
/**
 * 
 */
package com.ykyframework.mqservice.receiver;

import java.util.HashMap;

import com.ykyframework.mqservice.MQTopicMapping;

/**
 * @author liudian@yikuyi.com
 * @version 1.0.0
 */
public abstract class MappedMQReceiver extends AbstractMsgReceiver {

	private MQTopicMapping topicMapping = new MQTopicMapping(new HashMap<String, String>());
		
	/**
	 * @return the topicMapping
	 */
	public final MQTopicMapping getTopicMapping() {
		return topicMapping;
	}	

	/**
	 * @param topicMapping the topicMapping to set
	 */
	public final void setTopicMapping(MQTopicMapping topicMapping) {
		this.topicMapping = topicMapping;
	}

}
