/*
 * Created: 2016年9月26日
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.awaitility.Awaitility;
import org.hamcrest.core.IsEqual;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ykyframework.context.RuntimeContextHolder;
import com.ykyframework.mqservice.MQException;
import com.ykyframework.mqservice.MQTopicMapping;
import com.ykyframework.mqservice.receiver.MsgReceiveListener;
import com.ykyframework.mqservice.receiver.SimpleMQSubscription;
import com.ykyframework.mqservice.receiver.ZeroMQReceiver;

/**
 * @author liudian@yikuyi.com
 * @version 1.0.0
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class SimpleMQSenderTest {
	
	private static final Logger logger = LoggerFactory.getLogger(SimpleMQSenderTest.class);

	private String topic = "EH_CACHE_TOPIC_DEV_test";

	private ZeroMQSender sender;

	private ZeroMQReceiver rmr;

	private String message = null;

	/**
	 * @throws java.lang.Exception
	 * @since 2016年9月26日
	 * @author liudian@yikuyi.com
	 * @throws InterruptedException
	 */
	@Before
	public void setUp() throws InterruptedException {
		Map<String, String> mapper = new HashMap<>();
		mapper.put(topic, "TOPIC_ORIGINAL_DEV_test");
		MQTopicMapping tm = new MQTopicMapping(mapper);
		sender = new ZeroMQSender();
		sender.setTopicMapping(tm);
		sender.init();
		MsgReceiveListener listener = new MsgReceiveListener() {

			@Override
			public void onMessage(Serializable msg) {
				if (msg instanceof String) {
					message = (String) msg;
				}
				logger.warn("attributes in context: {}", RuntimeContextHolder.currentContextAttributes().isContextActive());
				return;
			}

		};

		rmr = new ZeroMQReceiver();
		SimpleMQSubscription sub1 = new SimpleMQSubscription("YKY_EhCacheMsg_Recv_Group_DEV_Test", topic, false,
				listener);
		try {
			List<SimpleMQSubscription> subscriptions = new ArrayList<SimpleMQSubscription>();
			subscriptions.add(sub1);
			rmr.setSubscriptions(subscriptions);
			rmr.setTopicMapping(tm);
			rmr.init();
		} catch (MQException e) {
			// Donothing here 
		}
	}

	/**
	 * @throws java.lang.Exception
	 * @since 2016年9月26日
	 * @author liudian@yikuyi.com
	 */
	@After
	public void tearDownAfterClass() throws Exception {
		Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(new Runnable() {

			@Override
			public void run() {
				try {
					sender.destroy();
				} catch (Exception exception) {
					// 解决单元测试runtimeException
				} finally {
					try {
						rmr.destroy();
					} catch (Exception exception) {
						// 解决单元测试runtimeException
					}
				}
			}
		});
	}

	/**
	 * Test method for
	 * {@link com.ykyframework.mqservice.sender.ZeroMQSender#sendMsg(java.lang.String, java.io.Serializable, java.util.Map)}
	 * .
	 * 
	 * @throws TimeoutException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@Test
	public void testSendMsg() throws InterruptedException {
		String msg = "aaaaaaaaaaaaaaaaaaaaaaaaaa";
		sender.sendMsg(topic, msg, null);
		String received = Awaitility.await().timeout(1, TimeUnit.SECONDS).until(new Callable<String>() {

			@Override
			public String call() {
				while (message == null && !Thread.currentThread().isInterrupted()) {
					Awaitility.await().timeout(5, TimeUnit.MILLISECONDS);
				}
				return message;
			}
		}, IsEqual.equalTo(msg));
		Assert.assertEquals(msg, received);
		return;
	}

}
