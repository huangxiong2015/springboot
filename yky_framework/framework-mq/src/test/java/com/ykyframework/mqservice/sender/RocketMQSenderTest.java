/*
 * Created: 2016年3月28日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2015 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ykyframework.mqservice.sender;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.ykyframework.mqservice.MQConstants;

/**
 * @author liudian@yikuyi.com
 *
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class RocketMQSenderTest {
	
	private final static String NAME_ADDR = "192.168.1.106:9876";
	
	private final static String PROD_GROUP = "TestSendGroup";
	
	private final static String TOPIC_NAME = "a_demo_topic_simple";
	
	private final static RocketMQSender sender = new RocketMQSender();
	
	@BeforeClass
    public static void setUpBeforeClass() throws Exception {
		sender.setNamesrvAddr(NAME_ADDR);
		sender.setProducerGroup(PROD_GROUP);
		sender.init();
    }
	
	@AfterClass
	public static void tearAfterClass() throws Exception {
		sender.destroy();
	}
	
	/**
	 * Test method for {@link com.ykyframework.mqservice.sender.RocketMQSender#sendMsg(java.lang.String, java.io.Serializable, java.util.Map)}.
	 */
	@Test
	public void testSendMsg() {
		try {
			Map<String,String> props = new HashMap<>();
			props.put(MQConstants.MSG_PROPERTY_KEY, "Atest4liudian");
			props.put(MQConstants.MSG_PROPERTY_TAG, "att");
			sender.sendMsg(TOPIC_NAME, "Junit测试消息", props);		
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link com.ykyframework.mqservice.sender.RocketMQSender#sendOrderedMsg(java.lang.String, java.io.Serializable, java.util.Map, java.io.Serializable)}.
	 */
	@Test
	public void testSendOrderedMsg() {
		try {
			sender.sendOrderedMsg(TOPIC_NAME, "Junit测试消息", null, "1");		
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link com.ykyframework.mqservice.sender.RocketMQSender#sendMsgNoTransaction(java.lang.String, java.io.Serializable, java.util.Map)}.
	 */
	@Test
	public void testSendMsgNoTransaction() {
		assertTrue(sender.sendMsgNoTransaction(TOPIC_NAME, "Junit测试消息", null));
	}

	/**
	 * Test method for {@link com.ykyframework.mqservice.sender.RocketMQSender#sendOrderedMsgNoTransaction(java.lang.String, java.io.Serializable, java.util.Map, java.io.Serializable)}.
	 */
	@Test
	public void testSendOrderedMsgNoTransaction() {
		assertTrue(sender.sendOrderedMsgNoTransaction(TOPIC_NAME, "Junit测试消息", null, "1"));
	}

}