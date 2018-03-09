/*
 * Created: 2016年7月6日
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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.ykyframework.mqservice.MQConstants;
import com.ykyframework.mqservice.MQTopicMapping;

/**
 * @author liudian@yikuyi.com
 * @version 1.0.0
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class AliyunMQSenderTest {
	

	private static AliyunMQSender sender;
	
	private static String TOPIC_NAME = "TOPIC_ICTRADE_TEST"; 

	/**
	 * @throws java.lang.Exception
	 * @since 2016年7月6日
	 * @author liudian@yikuyi.com
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		sender = new AliyunMQSender();
		sender.setAccessKey("wiPMJzbY8uCw5f5K");
		sender.setSecretKey("l0DLG6da2sTyv4K86EYKPWD3cZCK38");
		sender.setProducerId("PID_ICTRADE_TEST");
		sender.init();
	}

	/**
	 * @throws java.lang.Exception
	 * @since 2016年7月6日
	 * @author liudian@yikuyi.com
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		sender.destroy();
	}

	/**
	 * Test method for {@link com.ykyframework.mqservice.sender.AliyunMQSender#sendMsgNoTransaction(java.lang.String, java.io.Serializable, java.util.Map)}.
	 */
	@Test
	public void testSendMsgNoTransaction() {
		assertTrue(sender.sendMsgNoTransaction(TOPIC_NAME, "Junit测试消息1", null));
	}

	/**
	 * Test method for {@link com.ykyframework.mqservice.sender.AliyunMQSender#sendOrderedMsgNoTransaction(java.lang.String, java.io.Serializable, java.util.Map, java.io.Serializable)}.
	 */
	@Test
	public void testSendOrderedMsgNoTransaction() {
		assertTrue(sender.sendOrderedMsgNoTransaction(TOPIC_NAME, "Junit测试消息2", null, "122"));
	}

	/**
	 * Test method for {@link com.ykyframework.mqservice.sender.AliyunMQSender#sendMsg(java.lang.String, java.io.Serializable, java.util.Map)}.
	 */
	@Test
	public void testSendMsg() {
		try {
			sender.sendMsg(TOPIC_NAME, "Junit测试消息3", null);		
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link com.ykyframework.mqservice.sender.AliyunMQSender#sendOrderedMsg(java.lang.String, java.io.Serializable, java.util.Map, java.io.Serializable)}.
	 */
	@Test
	public void testSendOrderedMsg() {
		try {
			sender.sendOrderedMsg(TOPIC_NAME, "Junit测试消息4", null, (Serializable)"122");		
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test send a message over the topic that mapping to a other topic.
	 * 
	 * @since 2017年3月7日
	 * @author liudian@yikuyi.com
	 */
	@Test
	public void testSendMappedMsg() {
		String mapedTopic = "TOPIC_ICTRADE_TEST_MAPPED";
		AliyunMQSender asender = new AliyunMQSender();
		asender.setAccessKey("wiPMJzbY8uCw5f5K");
		asender.setSecretKey("l0DLG6da2sTyv4K86EYKPWD3cZCK38");
		asender.setProducerId("PID_ICTRADE_TEST");
		Map<String,String> mapper = new HashMap<>();
		mapper.put(mapedTopic, TOPIC_NAME);
		MQTopicMapping tm = new MQTopicMapping(mapper);
		asender.setTopicMapping(tm);		
		asender.init();
		Map<String,String> props = new HashMap<>();
		props.put(MQConstants.MSG_PROPERTY_KEY, "Atest4liudian");
		props.put(MQConstants.MSG_PROPERTY_TAG, "att");
		asender.sendMsg(mapedTopic, "message send by mapped topic!" + System.currentTimeMillis() , props);
	}

}
