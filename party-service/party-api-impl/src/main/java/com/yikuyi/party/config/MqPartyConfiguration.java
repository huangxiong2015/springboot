/*
 * Created: 2016年12月5日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.framework.springboot.config.MQConfigurator;
import com.ykyframework.mqservice.MQException;
import com.ykyframework.mqservice.sender.MsgSender;


@Configuration
public class MqPartyConfiguration extends MQConfigurator {
	
	Logger logger = LoggerFactory.getLogger(MqPartyConfiguration.class);
		
	@Profile({"mock"})
	@Bean
	public MsgSender mockMsgSender(){
		return super.getMockMsgSender();
	}
	
	@Profile({"dev"})
	@Bean(initMethod="init", destroyMethod="destroy")
	public MsgSender zeroMQSender(){
		return super.getZeroMQSender();
	}
	
	@Profile({"sit", "sit1"})
	@Bean(initMethod="init", destroyMethod="destroy")
	public MsgSender rocketMQSender(@Value("${mq.producer.groupid}") String producerId, @Value("${mq.namesrvAddr}") String namesrvAddr) throws MQException{
		return super.getRocketMQSender(producerId, namesrvAddr);
	}
	
	@Profile({"hz-uat","prod"})
	@Bean(initMethod="init", destroyMethod="destroy")
	public MsgSender aliyunMQSender(@Value("${mq.producer.groupid}") String producerId, @Value("${mq.accessKeyID}") String accessKeyID, @Value("${mq.secretKey}")String secretKey) throws MQException{
		return super.getAliyunMQSender(producerId, accessKeyID, secretKey);
	}
}
