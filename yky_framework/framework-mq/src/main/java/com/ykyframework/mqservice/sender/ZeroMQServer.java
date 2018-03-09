/*
 * Created: 2016年9月7日
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

import java.io.Closeable;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.awaitility.Awaitility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

/**
 * @author liudian@yikuyi.com
 * @version 1.0.0
 */
public class ZeroMQServer implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(ZeroMQServer.class);
	
	public static final int ZEROMP_SOCKET_PORT_PUB = 5035;
	
	public static final int ZEROMP_SOCKET_PORT_SUB = 5036;
	
	public static final int ZEROMP_SOCKET_PORT_PUSH = 5037;
	
	public static final String ZEROMP_SOCKET_HOST = "127.0.0.1";
	
	public static final String ZEROMP_SOCKET_PROTOCOL = "tcp";
	
	public static final String ZEROMP_TOPIC_SUBFIX = "/";
	
	public static final String ZEROMP_SUBSCRIBE_URL = ZeroMQServer.ZEROMP_SOCKET_PROTOCOL + "://" + ZeroMQServer.ZEROMP_SOCKET_HOST  + ":" + ZeroMQServer.ZEROMP_SOCKET_PORT_SUB;
	
	public static final String ZEROMP_PUBLISH_URL = ZeroMQServer.ZEROMP_SOCKET_PROTOCOL + "://" + ZeroMQServer.ZEROMP_SOCKET_HOST + ":" + ZeroMQServer.ZEROMP_SOCKET_PORT_PUB;
	
	public static final String ZEROMP_PUSH_URL = ZeroMQServer.ZEROMP_SOCKET_PROTOCOL + "://" + ZeroMQServer.ZEROMP_SOCKET_HOST + ":" + ZeroMQServer.ZEROMP_SOCKET_PORT_PUSH;
	
	private Socket publisher = null;
	
	private Socket subscriber = null;
	
	private Socket pusher = null;
	
	private Context context = null; 
	
	private boolean destoried = false;
	
	@Override
	public void run() {
		logger.info("ZeroMQServer start..............");
		this.context = ZMQ.context(1);
		this.publisher = context.socket(ZMQ.XPUB);
		this.subscriber = context.socket(ZMQ.XSUB);
//		this.pusher = context.socket(ZMQ.DEALER);
		//Bind a tcp port for local network interface
		//接收publisher发送的消息
		logger.info("ZeroMQServer bound to {} for publisher", ZEROMP_PUBLISH_URL);
		this.publisher.bind(ZEROMP_PUBLISH_URL);
		//向subscriber推送消息
		logger.info("ZeroMQServer bound to {} for subscriber", ZEROMP_SUBSCRIBE_URL);
		this.subscriber.bind(ZEROMP_SUBSCRIBE_URL);
		logger.info("ZeroMQServer bound to {} for pusher", ZEROMP_PUSH_URL);
//		this.pusher.connect(ZEROMP_PUSH_URL);
		try{
			ZMQ.proxy(subscriber, publisher, pusher);
		}
		catch (Exception exp) {
			logger.trace("ZeroMQServer 消息循环退出.", exp);
		}
		finally{
			logger.info("ZeroMQServer terminate.");
		}
	}
	
	public void doDestroy() {
		synchronized (this) {
			if (this.destoried) {
				return;
			}
			this.destoried = true;
		}
		logger.info("ZeroMQServer prepare to stop..............");
		try{
			Awaitility.await().timeout(2, TimeUnit.SECONDS).until(new Callable<Boolean>() {
				
				@Override
				public Boolean call() throws Exception {
					doClose();
					return true;
				}
			});
		}
		catch(Exception exp) {
			logger.trace("ZeroMQServer({}) stop with error:", this, exp);
		}
		finally{
			publisher.unbind(ZEROMP_PUBLISH_URL);
			subscriber.unbind(ZEROMP_SUBSCRIBE_URL);
	//		pusher.unbind(ZEROMP_PUSH_URL);
		}
		logger.info("ZeroMQServer stop..............");
	}
	
	private void doClose() {
		try{
			close(context);
		}
		finally{
			close(publisher);
			close(subscriber);
			//		close(pusher);
		}
	}
	
	private void close(Closeable obj) {
		try{
			IOUtils.closeQuietly(obj);
		}
		catch (Exception exp) {
			logger.trace("Closeable object {}, closed by error.", obj, exp);
		}
	}
	
	public static final byte[] getMessageFilter(String queueName, String filter) {
		return (queueName + ZEROMP_TOPIC_SUBFIX + filter).getBytes();
	}
}
