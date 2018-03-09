/*
 * Created: 2016年7月4日
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
package com.ykyframework.mqservice.sender;

import java.io.Serializable;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.order.OrderProducer;
import com.ykyframework.mqservice.MQConstants;
import com.ykyframework.mqservice.MQSystemException;
import com.ykyframework.mqservice.MQTopicMapping.TopicMapper;

/**
 * @author liudian@yikuyi.com
 * @version 1.0.0
 */
public class AliyunMQSender extends MappedMQSender {	

	private static final Logger logger = LoggerFactory.getLogger(AliyunMQSender.class);
	
	private String producerId;
	
	private String accessKey;
	
	private String secretKey;
	
	private static class ProducerGroup {

		private Producer producer;
		
		private OrderProducer orderProducer;
				
		private ProducerGroup(Producer producer, OrderProducer orderProducer) {
			this.producer = producer;
			this.orderProducer = orderProducer;
		}
		
		private void shutdown(){
			if (null != this.producer){
				this.producer.shutdown();
			}
			if (null != this.orderProducer){
				this.orderProducer.shutdown();
			}
		}
		
	}
	
	private ProducerGroup producers = null;
	
	public AliyunMQSender(){
		/*
		 * for spring injection
		 */
	}
	
	public AliyunMQSender(String accessKey, String secretkkey, String producerId){
		this.accessKey = accessKey;
		this.secretKey = secretkkey;
		this.producerId = producerId;
	}
	
	public synchronized void init() {
		if (null == this.producers) {
			logger.info("RocketMQSender init started!");
			Properties properties = new Properties();
			//您在控制台创建的Producer ID
	        properties.put(PropertyKeyConst.ProducerId, this.producerId);
	        //AccessKey 阿里云身份验证，在阿里云服务器管理控制台创建
	        properties.put(PropertyKeyConst.AccessKey, this.accessKey);
	        //SecretKey 阿里云身份验证，在阿里云服务器管理控制台创建
	        properties.put(PropertyKeyConst.SecretKey, this.secretKey);
			Producer producer = ONSFactory.createProducer(properties);
			producer.start();
			OrderProducer orderProducer = ONSFactory.createOrderProducer(properties);
			orderProducer.start();
			this.producers = new ProducerGroup(producer, orderProducer);
		}
	}

	public void destroy(){
		if (null != this.producers) {
			synchronized (this){
				if (null != this.producers) {
					this.producers.shutdown();
					this.producers = null;
				}
			}
		}
	}

	/**
	 * @return the producerId
	 */
	public final String getProducerId() {
		return producerId;
	}

	/**
	 * @param producerId the producerId to set
	 */
	public final void setProducerId(String producerId) {
		this.producerId = producerId;
	}

	/**
	 * @return the accessKey
	 */
	public final String getAccessKey() {
		return accessKey;
	}

	/**
	 * @param accessKey the accessKey to set
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
	 * @param secretKey the secretKey to set
	 */
	public final void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	/* (non-Javadoc)
	 * @see com.ykyframework.mqservice.sender.MsgSender#sendMsgNoTransaction(java.lang.String, java.io.Serializable, java.util.Map)
	 */
	@Override
	public boolean sendMsgNoTransaction(String queueName, Serializable msg, Map<String, String> props) {
		try {
			this.sendMsg(queueName, msg, props);
		} catch (Exception e) {
			logger.error("failed to sendMsgNoTransaction.", e);
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.ykyframework.mqservice.sender.MsgSender#sendOrderedMsgNoTransaction(java.lang.String, java.io.Serializable, java.util.Map, java.io.Serializable)
	 */
	@Override
	public boolean sendOrderedMsgNoTransaction(String queueName, Serializable msg, Map<String, String> props,
			Serializable shardBy) {
		try {
			this.sendOrderedMsg(queueName, msg, props, shardBy);
		} catch (Exception e) {
			logger.error("failed to sendOrderedMsgNoTransaction.", e);
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.ykyframework.mqservice.sender.MsgSender#sendMsg(java.lang.String, java.io.Serializable, java.util.Map)
	 */
	@Override
	public void sendMsg(String queueName, Serializable msg, Map<String, String> props) {
		Message message = this.packageMessage(queueName, msg, props);
		SendResult sendResult;
		try {
			//消息发送没有异常则表示成功。但非SendStatus.SEND_OK情况下服务器宕机可能会丢失消息
			sendResult = this.producers.producer.send(message);
			logger.debug("{}, Result :{}", message, sendResult);
			return;
		} catch (Exception e) {
			logger.error(message + "消息发送失败" , e);
			throw new MQSystemException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.ykyframework.mqservice.sender.MsgSender#sendOrderedMsg(java.lang.String, java.io.Serializable, java.util.Map, java.io.Serializable)
	 */
	@Override
	public void sendOrderedMsg(String queueName, Serializable msg, Map<String, String> props, Serializable shardBy) {
		Message message = packageMessage(queueName, msg, props);
		SendResult sendResult;
		try {
			String shardingKey = (shardBy == null) ? null:String.valueOf(shardBy.hashCode());
			sendResult = this.producers.orderProducer.send(message, shardingKey);
			logger.debug("{}, Result :{}", message, sendResult);
			return;
		} catch (Exception e) {
			logger.error(message.toString() + "发送失败." , e);
			throw new MQSystemException(e);
		}
	}
	
	private Message packageMessage(String topicName, Serializable obj, Map<String, String> props){
		//序列化对象		
		byte[] buffer = SerializationUtils.serialize(obj);
		
		TopicMapper tMapper = this.getTopicMapping().findTopicMapper(topicName);
		Message message = new Message(tMapper.getOriginalTopic(), tMapper.getFilter(), buffer);
		if (null != props) {
			for (Map.Entry<String, String> entry : props.entrySet()) {
				message.putUserProperties(entry.getKey(), entry.getValue());
				if (MQConstants.MSG_PROPERTY_KEY.equals(entry.getKey())){
					message.setKey(entry.getValue());
				}
				else if (MQConstants.MSG_PROPERTY_TAG.equals(entry.getKey()) && StringUtils.isBlank(message.getTag())){
					message.setTag(entry.getValue());
				}
			}
		}
		return message;
		
	} 

}