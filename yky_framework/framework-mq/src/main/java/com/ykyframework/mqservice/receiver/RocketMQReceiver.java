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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.rocketmq.common.protocol.heartbeat.MessageModel;
import com.ykyframework.mqservice.MQException;
import com.ykyframework.mqservice.MQTopicMapping.TopicMapper;

/**
 * RocketQ 接收消息的实现类
 * @author liudian@yikuyi.com
 *
 */
public class RocketMQReceiver extends MappedMQReceiver{
	
	private static final Logger logger = LoggerFactory.getLogger(RocketMQReceiver.class);
	
	private List<DefaultMQPushConsumer> consumers;
	
	private String namesrvAddr;
	
	public RocketMQReceiver(){
		this.consumers = new ArrayList<>();		
	}
	
	/**
	 * @param namesrvAddr
	 *            the namesrvAddr to set
	 */
	public void setNamesrvAddr(String namesrvAddr) {
		this.namesrvAddr = namesrvAddr;
	}	
	
	/**
	 * @return the namesrvAddr
	 */
	public String getNamesrvAddr() {
		return namesrvAddr;
	}

	@Override
	public void destroy(){
		for (DefaultMQPushConsumer consumer : this.consumers){
			consumer.shutdown();
		}
		this.consumers.clear();
		logger.info("MQReceiver {} destory completed!", this);
	}
	
	/**
	 * 反序列化的方法
	 * @param msg
	 * @return
	 * @throws Exception
	 * @since 2016年7月5日
	 * @author liudian@yikuyi.com
	 */
	private Serializable parseObject(MessageExt msg) {
		return (Serializable)SerializationUtils.deserialize(msg.getBody());
	}
	
	@Override
	public void registerSubscription(SimpleMQSubscription subs) throws MQException {
		final MsgReceiveListener listener = subs.getMsgReceiveListener();
		
		DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer(subs.getConsumerGroup());
		//设置并发处理线程数（最大）
		defaultMQPushConsumer.setConsumeThreadMin(subs.getConcurrency());
		defaultMQPushConsumer.setConsumeThreadMax(subs.getConcurrency());

		defaultMQPushConsumer.setNamesrvAddr(this.getNamesrvAddr());
		//从队列的最后位置开始消费后续再启动接着上次消费的进度开始消费
		if (subs.isClustering()) {
			defaultMQPushConsumer.setMessageModel(MessageModel.CLUSTERING);
		} else {
			defaultMQPushConsumer.setMessageModel(MessageModel.BROADCASTING);
		}
		TopicMapper tMapper = this.getTopicMapping().findTopicMapper(subs.getTopicName());
		if (subs.isOrderly()) {
			registerListenerOrderly(listener, defaultMQPushConsumer);
		} else {
			registerListenerConcurrently(listener, defaultMQPushConsumer);
		}
		try {
			defaultMQPushConsumer.subscribe(tMapper.getOriginalTopic(), tMapper.getFilter());
			defaultMQPushConsumer.setConsumeMessageBatchMaxSize(1);
			this.consumers.add(defaultMQPushConsumer);
			defaultMQPushConsumer.start();
			logger.info("Listener {} is registered and started on {}:{}@{} with group {}, is in isClustering mode: {}.", subs.getMsgReceiveListener(), tMapper.getOriginalTopic(), tMapper.getFilter(), this.getNamesrvAddr(), subs.getConsumerGroup(), subs.isClustering());
		} catch (MQClientException e) {
			logger.error("error", e);
			throw new MQException(e);
		}
	}

	/**
	 * @param listener
	 * @param defaultMQPushConsumer
	 * @since 2017年3月7日
	 * @author liudian@yikuyi.com
	 */
	private void registerListenerConcurrently(final MsgReceiveListener listener,
			DefaultMQPushConsumer defaultMQPushConsumer) {
		defaultMQPushConsumer.registerMessageListener((List<MessageExt> msgs, ConsumeConcurrentlyContext context) -> {
			MessageExt msg = msgs.get(0);
			Serializable msgBody;
			try {
				msgBody = parseObject(msg);
			} catch (Exception e) {
				logger.error("解包消息：" + msg + "出错，",e);
				// 信息有异常，丢弃消息
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
			try {
				listener.onMessage(msgBody, msg.getProperties());
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			} catch (Exception e) {
				logger.error("消费消息：" + msg + "出错，",e);
				return ConsumeConcurrentlyStatus.RECONSUME_LATER;
			}
		});
	}

	/**
	 * @param listener
	 * @param defaultMQPushConsumer
	 * @since 2017年3月7日
	 * @author liudian@yikuyi.com
	 */
	private void registerListenerOrderly(final MsgReceiveListener listener,
			DefaultMQPushConsumer defaultMQPushConsumer) {
		defaultMQPushConsumer.registerMessageListener((List<MessageExt> msgs, ConsumeOrderlyContext context) -> {
			context.setAutoCommit(true);
			MessageExt msg = msgs.get(0);
			Serializable msgBody;
			try {
				msgBody = parseObject(msg);
			} catch (Exception e) {
				logger.error("解包消息：" + msg + "出错，", e);
				// 信息有异常，丢弃消息
				return ConsumeOrderlyStatus.SUCCESS;
			}
			try {
				listener.onMessage(msgBody, msg.getProperties());
				return ConsumeOrderlyStatus.SUCCESS;
			} catch (Exception e) {
				logger.error("消费消息：" + msg + "出错，", e);
				context.setSuspendCurrentQueueTimeMillis(500);
				return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
			}
		});
	}
}
