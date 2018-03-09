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
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageQueue;
import com.ykyframework.mqservice.MQConstants;
import com.ykyframework.mqservice.MQSystemException;
import com.ykyframework.mqservice.MQTopicMapping.TopicMapper;

/**
 * @author liudian@yikuyi.com
 *
 */
public class RocketMQSender extends MappedMQSender {

	private static final Logger logger = LoggerFactory.getLogger(RocketMQSender.class);

	private DefaultMQProducer defaultMQProducer;

	private String producerGroup;

	private String namesrvAddr;

	public static final String MSG_BODY_CLAZZ = "msg_body_clazz";

	private static final int MSG_MAX_SIZE = 1024 * 1024;
	
	public RocketMQSender() {
		/*
		 * constructor for spring injection with properties setter
		 */
	}

	public RocketMQSender(String producerGroup, String namesrvAddr) {
		this.producerGroup = producerGroup;
		this.namesrvAddr = namesrvAddr;
	}

	/**
	 * This is the init method for spring framework
	 * 
	 * @throws MQClientException
	 */
	public synchronized void init() throws MQClientException {

		if (null == this.defaultMQProducer) {
			this.defaultMQProducer = new DefaultMQProducer(this.producerGroup);
			this.defaultMQProducer.setNamesrvAddr(namesrvAddr);
			this.defaultMQProducer.setMaxMessageSize(MSG_MAX_SIZE);
			this.defaultMQProducer.start();
			logger.info("RocketMQSender init completed!");
		}
	}

	public void destroy() {
		if (null != this.defaultMQProducer) {
			synchronized (this) {
				if (null != this.defaultMQProducer) {
					this.defaultMQProducer.shutdown();
					this.defaultMQProducer = null;
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yikuyi.mqservice.sender.MsgSender#sendMsg(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public void sendMsg(String queueName, Serializable msg, Map<String, String> props) {
		Message message = packageMessage(queueName, msg, props);
		SendResult sendResult;
		try {
			// 消息发送没有异常则表示成功。但非SendStatus.SEND_OK情况下服务器宕机可能会丢失消息
			sendResult = this.defaultMQProducer.send(message);
			logger.debug("{}, Result :{}", message, sendResult);
			return;
		} catch (Exception e) {
			logger.error(message + "消息发送失败.", e);
			throw new MQSystemException(e);
		}
	}

	/**
	 * @return the defaultMQProducer
	 */
	public DefaultMQProducer getDefaultMQProducer() {
		return defaultMQProducer;
	}

	/**
	 * @param producerGroup
	 *            the producerGroup to set
	 */
	public void setProducerGroup(String producerGroup) {
		this.producerGroup = producerGroup;
	}

	/**
	 * @param namesrvAddr
	 *            the namesrvAddr to set
	 */
	public void setNamesrvAddr(String namesrvAddr) {
		this.namesrvAddr = namesrvAddr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yikuyi.mqservice.sender.MsgSender#sendOrderedMsg(java.lang.String,
	 * java.lang.Object, java.lang.Object)
	 */
	@Override
	public void sendOrderedMsg(String queueName, Serializable msg, Map<String, String> props, Serializable shardBy) {
		Message message = packageMessage(queueName, msg, props);
		SendResult sendResult;
		try {
			sendResult = this.defaultMQProducer.send(message, (List<MessageQueue> mqs, Message mes, Object arg) -> {
				int hash = ((Serializable) arg).hashCode();
				int index = Math.abs(hash) % mqs.size();
				return mqs.get(index);
			}, shardBy);
			logger.debug("{}, Result :{}", message, sendResult);
			return;
		} catch (Exception e) {
			logger.error(message.toString() + "发送失败.", e);
			throw new MQSystemException(e);
		}
	}

	private Message packageMessage(String topicName, Serializable obj, Map<String, String> props) {
		// 序列化对象
		byte[] buffer = SerializationUtils.serialize(obj);

		TopicMapper tMapper = this.getTopicMapping().findTopicMapper(topicName);
		
		Message message = new Message(tMapper.getOriginalTopic(), tMapper.getFilter(), buffer);
		if (null != props) {
			for (Map.Entry<String, String> entry : props.entrySet()) {
				message.putUserProperty(entry.getKey(), entry.getValue());
				if (MQConstants.MSG_PROPERTY_KEY.equals(entry.getKey())){
					message.setKeys(entry.getValue());
				}
				else if (MQConstants.MSG_PROPERTY_TAG.equals(entry.getKey()) && StringUtils.isBlank(message.getTags())){
					message.setTags(entry.getValue());
				}
			}
		}
		return message;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ykyframework.mqservice.sender.MsgSender#sendMsgNoTransaction(java.
	 * lang.String, java.io.Serializable, java.util.Map)
	 */
	@Override
	public boolean sendMsgNoTransaction(String queueName, Serializable msg, Map<String, String> props) {
		try {
			this.sendMsg(queueName, msg, props);
		} catch (MQSystemException e) {
			logger.error("sendMsgNoTransaction error", e);
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ykyframework.mqservice.sender.MsgSender#sendOrderedMsgNoTransaction(
	 * java.lang.String, java.io.Serializable, java.util.Map,
	 * java.io.Serializable)
	 */
	@Override
	public boolean sendOrderedMsgNoTransaction(String queueName, Serializable msg, Map<String, String> props,
			Serializable shardBy) {
		try {
			this.sendOrderedMsg(queueName, msg, props, shardBy);
		} catch (MQSystemException e) {
			logger.error("sendOrderedMsgNoTransaction error", e);
			return false;
		}
		return true;
	}
}
