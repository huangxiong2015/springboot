/*
 * Created: 2017年3月16日
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
package com.framework.springboot.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.alibaba.fastjson.JSONObject;
import com.ykyframework.mqservice.MQException;
import com.ykyframework.mqservice.MQTopicMapping;
import com.ykyframework.mqservice.receiver.AliyunMQReceiver;
import com.ykyframework.mqservice.receiver.MappedMQReceiver;
import com.ykyframework.mqservice.receiver.MsgReceiveListener;
import com.ykyframework.mqservice.receiver.RocketMQReceiver;
import com.ykyframework.mqservice.receiver.SimpleMQSubscription;
import com.ykyframework.mqservice.receiver.ZeroMQReceiver;
import com.ykyframework.mqservice.sender.AliyunMQSender;
import com.ykyframework.mqservice.sender.MappedMQSender;
import com.ykyframework.mqservice.sender.MockMsgSender;
import com.ykyframework.mqservice.sender.MsgSender;
import com.ykyframework.mqservice.sender.RocketMQSender;
import com.ykyframework.mqservice.sender.ZeroMQSender;

/**
 * @author liudian@yikuyi.com
 * @version 1.0.0
 */
@ConfigurationProperties(prefix="mq.config")
public class MQConfigurator implements ApplicationContextAware {
	
	private ApplicationContext applicationContext; // Spring应用上下文环境
		
	private Map<String, String> mqConsumeConfig;
	
	private String mqTopicMappingConfig;
			
	private MQTopicMapping topicMapping;
	
	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

    public Map<String, String> getMqConsumeConfig() {
		return mqConsumeConfig;
	}
    
	public void setMqConsumeConfig(Map<String, String> mqConsumeConfig) {
		this.mqConsumeConfig = mqConsumeConfig;
	}
	
	private List<SimpleMQSubscription> getMqSubs() throws MQException{
		if(null == mqConsumeConfig || mqConsumeConfig.size()<=0){
			return Collections.emptyList();
		}
		List<SimpleMQSubscription> list = new ArrayList<>();
		for(Entry<String, String> entry : mqConsumeConfig.entrySet()){
			try {
				String configs = entry.getValue();
				JSONObject object = JSONObject.parseObject(configs);
				SimpleMQSubscription simpleMQSubscription = extractSubscription(object);
				list.add(simpleMQSubscription);
			} catch (Exception e) {
				throw new MQException(e);
			} 
		}
		return list;
	}

	/**
	 * @param object
	 * @return
	 * @throws BeansException
	 * @throws ClassNotFoundException
	 * @since 2017年3月16日
	 * @author liudian@yikuyi.com
	 */
	@SuppressWarnings("unchecked")
	private SimpleMQSubscription extractSubscription(JSONObject object) throws ClassNotFoundException {
		SimpleMQSubscription simpleMQSubscription = new SimpleMQSubscription();
		simpleMQSubscription.setConsumerGroup(object.getString("consumerGroup"));
		simpleMQSubscription.setTopicName(object.getString("topicName"));
		simpleMQSubscription.setConcurrency(object.getIntValue("concurrency"));
		simpleMQSubscription.setMsgReceiveListener((MsgReceiveListener)applicationContext.getBean((Class<MsgReceiveListener>)Class.forName(object.getString("msgReceiveListener"))));
		if(object.containsKey("clustering")){
			simpleMQSubscription.setClustering(object.getBooleanValue("clustering"));
		}
		if(object.containsKey("orderly")){
			simpleMQSubscription.setOrderly(object.getBooleanValue("orderly"));
		}
		return simpleMQSubscription;
	}
	
	/**
	 * @return the mqTopicMappingConfig
	 */
	public String getMqTopicMappingConfig() {
		return mqTopicMappingConfig;
	}

	/**
	 * @param mqTopicMappingConfig the mqTopicMappingConfig to set
	 */
	public void setMqTopicMappingConfig(String mqTopicMappingConfig) {
		this.mqTopicMappingConfig = mqTopicMappingConfig;
	}

	protected MappedMQReceiver getZeroMQReceiver() throws MQException {
		ZeroMQReceiver zeroMQReceiver = new ZeroMQReceiver();
		zeroMQReceiver.setSubscriptions(getMqSubs());
		zeroMQReceiver.setTopicMapping(topicMapping);
		return zeroMQReceiver;
	}
	
	protected MappedMQReceiver getRocketMQReceiver(@Value("${mq.namesrvAddr}") String namesrvAddr) throws MQException{
		RocketMQReceiver rocketMQReceiver = new RocketMQReceiver();
		rocketMQReceiver.setNamesrvAddr(namesrvAddr);
		rocketMQReceiver.setSubscriptions(getMqSubs());
		rocketMQReceiver.setTopicMapping(topicMapping);
		return rocketMQReceiver;
	}
	
	protected MappedMQReceiver getAliyunMQReceiver(String accessKeyID, String secretKey) throws MQException{
		AliyunMQReceiver aliyunMQReceiver = new AliyunMQReceiver();
		aliyunMQReceiver.setAccessKey(accessKeyID);
		aliyunMQReceiver.setSecretKey(secretKey);
		aliyunMQReceiver.setSubscriptions(getMqSubs());
		aliyunMQReceiver.setTopicMapping(topicMapping);
		return aliyunMQReceiver;
	}
	
	@PostConstruct
	public void configMQTopicMapping() {
		JSONObject jMap = JSONObject.parseObject(this.mqTopicMappingConfig);
		final Map<String, String> mapper = new HashMap<>();
		jMap.entrySet().stream().filter(entry -> entry.getValue() instanceof String).forEach(p -> mapper.put(p.getKey(), (String)p.getValue()));
		this.topicMapping = new MQTopicMapping(mapper);
	}
	
	protected MsgSender getMockMsgSender(){
		return new MockMsgSender();
	}
	
	protected MappedMQSender getZeroMQSender(){
		ZeroMQSender zeroMQSender = new ZeroMQSender();
		zeroMQSender.setTopicMapping(this.topicMapping);
		zeroMQSender.init();
		return zeroMQSender;
	}
	
	protected MappedMQSender getRocketMQSender(String producerGroup, String namesrvAddr) throws MQException{
		RocketMQSender rocketMQSender = new RocketMQSender();
		rocketMQSender.setNamesrvAddr(namesrvAddr);
		rocketMQSender.setProducerGroup(producerGroup);
		rocketMQSender.setTopicMapping(this.topicMapping);
		try{
			rocketMQSender.init();
		} 
		catch (Exception e) {
			throw new MQException(e);
		}
		return rocketMQSender;
	}
	
	protected MappedMQSender getAliyunMQSender(String producerId, String accessKeyID, String secretKey) throws MQException{
		AliyunMQSender aliyunMQSender = new AliyunMQSender();
		aliyunMQSender.setProducerId(producerId);
		aliyunMQSender.setAccessKey(accessKeyID);
		aliyunMQSender.setSecretKey(secretKey);
		aliyunMQSender.setTopicMapping(this.topicMapping);
		try {
			aliyunMQSender.init();
		} catch (Exception e) {
			throw new MQException(e);
		}
		return aliyunMQSender;
	}

}
