/*
 * Created: 2016年7月5日
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

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.PropertyValueConst;
import com.aliyun.openservices.ons.api.order.ConsumeOrderContext;
import com.aliyun.openservices.ons.api.order.OrderAction;
import com.aliyun.openservices.ons.api.order.OrderConsumer;
import com.ykyframework.mqservice.MQException;
import com.ykyframework.mqservice.MQTopicMapping.TopicMapper;

/**
 * @author liudian@yikuyi.com
 * @version 1.0.0
 */
public class AliyunMQReceiver extends MappedMQReceiver {

	private static final Logger logger = LoggerFactory.getLogger(AliyunMQReceiver.class);

	private String accessKey;

	private String secretKey;

	List<Consumer> consumers = new LinkedList<>();

	List<OrderConsumer> orderConsumers = new LinkedList<>();

	@Override
	public void destroy() {
		for (Consumer consumer : this.consumers) {
			consumer.shutdown();
		}
		this.consumers.clear();
		for (OrderConsumer orderConsumer : this.orderConsumers) {
			orderConsumer.shutdown();
		}
		this.orderConsumers.clear();
	}

	/**
	 * @return the accessKey
	 */
	public final String getAccessKey() {
		return accessKey;
	}

	/**
	 * @param accessKey
	 *            the accessKey to set
	 */
	public final void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	/**
	 * @return the secretKey
	 */
	public final String getSecretKey() {
		return secretKey;
	}

	/**
	 * @param secretKey
	 *            the secretKey to set
	 */
	public final void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	@Override
	public void registerSubscription(SimpleMQSubscription subs) throws MQException {
		final MsgReceiveListener listener = subs.getMsgReceiveListener();
		Properties properties = new Properties();
		// 您在控制台创建的 Producer ID
		properties.put(PropertyKeyConst.ConsumerId, subs.getConsumerGroup());
		// AccessKey 阿里云身份验证，在阿里云服务器管理控制台创建
		properties.put(PropertyKeyConst.AccessKey, this.accessKey);
		// SecretKey 阿里云身份验证，在阿里云服务器管理控制台创建
		properties.put(PropertyKeyConst.SecretKey, this.secretKey);
		// 设置消息消费模式：集群或广播
		properties.put(PropertyKeyConst.MessageModel,
				subs.isClustering() ? PropertyValueConst.CLUSTERING : PropertyValueConst.BROADCASTING);

		properties.put(PropertyKeyConst.ConsumeThreadNums, subs.getConcurrency());
		// 设置最大超时时间
		properties.put(PropertyKeyConst.ConsumeTimeout, subs.getConsumeTimeout());
		TopicMapper tMapper = this.getTopicMapping().findTopicMapper(subs.getTopicName());
		if (subs.isOrderly()) {
			properties.put(PropertyKeyConst.SuspendTimeMillis, "500");
			OrderConsumer orderConsumer = registerListenerOrderly(listener, properties, tMapper);
			this.orderConsumers.add(orderConsumer);
			orderConsumer.start();
		} else {
			Consumer consumer = registerListenerNormal(listener, properties, tMapper);
			this.consumers.add(consumer);
			consumer.start();
		}
		logger.info("Listener {} is registered and started on {}:{}@{} with group {}", subs.getMsgReceiveListener(),
				tMapper.getOriginalTopic(), tMapper.getFilter(), "aliyunONS", subs.getConsumerGroup());
	}

	/**
	 * @param listener
	 * @param properties
	 * @param tMapper
	 * @return
	 * @since 2017年4月13日
	 * @author liudian@yikuyi.com
	 */
	private Consumer registerListenerNormal(final MsgReceiveListener listener, Properties properties,
			TopicMapper tMapper) {
		Consumer consumer = ONSFactory.createConsumer(properties);

		// 订阅消息
		consumer.subscribe(tMapper.getOriginalTopic(), tMapper.getFilter(),
				(Message message, ConsumeContext context) -> {
					Serializable msgBody;
					try {
						msgBody = (Serializable) SerializationUtils.deserialize(message.getBody());
					} catch (Exception e) {
						logger.error("解包消息：{}，出错:", message, e);
						// 信息有异常，丢弃消息
						return Action.CommitMessage;
					}

					try {
						Map<String, String> propsMap = new HashMap<>();
						Properties props = message.getUserProperties();
						if (props != null) {
							props.forEach((key, value) -> propsMap.put((String)key, (String)value));
						}			
						listener.onMessage(msgBody, propsMap);
						return Action.CommitMessage;
					} catch (Exception e) {
						logger.error("消费消息：{}，出错:", message, e);
						return Action.ReconsumeLater;
					}
				});
		return consumer;
	}

	/**
	 * @param listener
	 * @param properties
	 * @param tMapper
	 * @return
	 * @since 2017年4月13日
	 * @author liudian@yikuyi.com
	 */
	private OrderConsumer registerListenerOrderly(final MsgReceiveListener listener, Properties properties,
			TopicMapper tMapper) {
		OrderConsumer orderConsumer = ONSFactory.createOrderedConsumer(properties);
		orderConsumer.subscribe(tMapper.getOriginalTopic(), tMapper.getFilter(),
				(Message message, ConsumeOrderContext context) -> {
					Serializable msgBody;
					try {
						msgBody = (Serializable) SerializationUtils.deserialize(message.getBody());
					} catch (Exception e) {
						logger.error("解包消息：{}，请检查是否使用了非兼容的客户端发送消息。出错:", message, e);
						// 信息有异常，丢弃消息
						return OrderAction.Success;
					}

					try {
						Map<String, String> propsMap = new HashMap<>();
						Properties props = message.getUserProperties();
						if (props != null) {
							props.forEach((key, value) -> propsMap.put((String)key, (String)value));
						}							
						listener.onMessage(msgBody, propsMap);
						return OrderAction.Success;
					} catch (Exception e) {
						logger.error("消费消息：{}，出错:", message, e);
						return OrderAction.Suspend;
					}
				});
		return orderConsumer;
	}
}
