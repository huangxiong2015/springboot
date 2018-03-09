/*
 * Created: 2017年6月27日
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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import redis.clients.jedis.JedisPool;

/**
 * @author liudian@yikuyi.com
 * @version 1.0.0
 */
@Configuration
public class JedisConfiguration {
	
	@Bean(destroyMethod="destroy")
	public JedisPool getJedis(JedisConnectionFactory jedisConnectinFactory){
		return new JedisPool(jedisConnectinFactory.getPoolConfig(),jedisConnectinFactory.getHostName(),jedisConnectinFactory.getPort(),0,jedisConnectinFactory.getPassword(),jedisConnectinFactory.getDatabase());
	} 

}
