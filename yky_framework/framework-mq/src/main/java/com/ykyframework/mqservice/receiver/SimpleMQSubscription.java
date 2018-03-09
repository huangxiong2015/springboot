/*
 * Created: 2016年3月22日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2015 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ykyframework.mqservice.receiver;

/**
 * @author liudian@yikuyi.com
 *
 */
public class SimpleMQSubscription {
	
	private String consumerGroup;

	private boolean clustering = true;

	private boolean orderly = false;

	private String topicName;

	//默认并发度为20, 设置时只接收>=5的数值
	private int concurrency = 20;

	// 消费超时时间（分钟），默认10分钟
	private long consumeTimeout = 10;

	private MsgReceiveListener msgReceiveListener;

	public SimpleMQSubscription() {
		/**
		 * keep a empty constuctor 
		 */
	}

	public SimpleMQSubscription(String consumerGroup, String topicName, boolean isOrder,
			MsgReceiveListener listener) {
		this.consumerGroup = consumerGroup;
		this.topicName = topicName;
		this.orderly = isOrder;
		this.msgReceiveListener = listener;
	}
	
	/**
	 * @param consumerGroup
	 *            the consumerGroup to set
	 */
	public void setConsumerGroup(String consumerGroup) {
		this.consumerGroup = consumerGroup;
	}

	/**
	 * @param isClustering
	 *            the isClustering to set
	 */
	public void setClustering(boolean isClustering) {
		this.clustering = isClustering;
	}

	/**
	 * @param isOrderly
	 *            the isOrderly to set
	 */
	public void setOrderly(boolean isOrderly) {
		this.orderly = isOrderly;
	}

	/**
	 * @param topicName
	 *            the topicName to set
	 */
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	/**
	 * @return the msgReceiveListener
	 */
	public MsgReceiveListener getMsgReceiveListener() {
		return msgReceiveListener;
	}

	/**
	 * @param msgReceiveListener
	 *            the msgReceiveListener to set
	 */
	public void setMsgReceiveListener(MsgReceiveListener msgReceiveListener) {
		this.msgReceiveListener = msgReceiveListener;
	}

	/**
	 * @return the concurrency
	 */
	public int getConcurrency() {
		return concurrency;
	}

	/**
	 * @param concurrency
	 *            the concurrency to set, must >= 5
	 */
	public void setConcurrency(int concurrency) {
		if (concurrency >= 5) {
			this.concurrency = concurrency;
		}
		else {
			this.concurrency = 5;
		}
	}

	/**
	 * @return the topicName
	 */
	public String getTopicName() {
		return topicName;
	}

	/**
	 * @return the consumerGroup
	 */
	public String getConsumerGroup() {
		return consumerGroup;
	}

	/**
	 * @return the isClustering
	 */
	public boolean isClustering() {
		return clustering;
	}

	/**
	 * @return the isOrderly
	 */
	public boolean isOrderly() {
		return orderly;
	}

	/**
	 * @return the consumeTimeout
	 */
	public long getConsumeTimeout() {
		return consumeTimeout;
	}

	/**
	 * @param consumeTimeout
	 *            the consumeTimeout to set
	 */
	public void setConsumeTimeout(final long consumeTimeout) {
		this.consumeTimeout = consumeTimeout;
	}

}
