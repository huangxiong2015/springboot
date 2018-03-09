/*
 * Created: 2016年3月22日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2015 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ykyframework.mqservice.receiver;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.awaitility.Awaitility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

import com.ykyframework.mqservice.MQException;
import com.ykyframework.mqservice.MQTopicMapping.TopicMapper;
import com.ykyframework.mqservice.sender.ZeroMQSender.Message;
import com.ykyframework.mqservice.sender.ZeroMQServer;

/**
 * RocketQ 接收消息的实现类
 * @author liudian@yikuyi.com
 *
 */
public class ZeroMQReceiver extends MappedMQReceiver {
	
	private static final Logger logger = LoggerFactory.getLogger(ZeroMQReceiver.class);	
		
	private List<Consumer> consumers;
	
	public ZeroMQReceiver(){
		this.consumers = new ArrayList<>();		
	}
	
	class Consumer extends Thread {
		
		private ZMQ.Context context = ZMQ.context(1);
		final Socket subscriber = context.socket(ZMQ.SUB);
		
		private MsgReceiveListener listener;
		
		private byte[] filter = null;
		
		private String address = null;
		
		public Consumer(MsgReceiveListener msgListener, TopicMapper topicMapper, String addr) {
			if (msgListener == null || topicMapper == null || addr == null || StringUtils.isBlank(addr)) {
				throw new IllegalArgumentException();
			}			
			this.listener = msgListener;
			this.filter = ZeroMQServer.getMessageFilter(topicMapper.getOriginalTopic(), topicMapper.getFilter());
			this.address = addr;
		}
		
		@Override
		public void run() {
			subscriber.connect(address);
			subscriber.subscribe(filter);
			
			ZMQ.Poller poller = new ZMQ.Poller(1);
			poller.register(subscriber, ZMQ.Poller.POLLIN);

			try{
				doPoll(listener, poller, subscriber);
			}
			catch(Exception exp) {
				logger.trace("消息处理循环退出：", exp);
			}
			finally{
				poller.unregister(subscriber);
				subscriber.unsubscribe(this.filter);
				subscriber.disconnect(this.address);
//				this.doDestroy();
			}
		}

		/**
		 * @param listener
		 * @param poller
		 * @since 2016年10月18日
		 * @author liudian@yikuyi.com
		 */
		private void doPoll(final MsgReceiveListener listener, ZMQ.Poller poller, Socket sub) {
			while (!this.isInterrupted()) {
				poller.poll();
				
				if (poller.pollin(0)) {
					doListening(listener, sub);
				}
			}
		}

		/**
		 * @param listener
		 * @param sub
		 * @since 2017年2月28日
		 * @author liudian@yikuyi.com
		 */
		private void doListening(final MsgReceiveListener listener, Socket sub) {
			byte[] messageAll = new byte[0];
			byte[] recvs;
			while ((recvs = sub.recv()) != null) {
				messageAll = ArrayUtils.addAll(messageAll, recvs);
				boolean hasMore = sub.hasReceiveMore();
				
				if (!hasMore) {
					Message msg = null;
					try {
						msg = parseObject(ArrayUtils.subarray(messageAll, filter.length, messageAll.length));
						listener.onMessage(msg.getBody(), msg.getProperties());
					} catch (Exception e) {
						logger.error("解包消息：{}，出错：", msg, e);
						// 信息有异常，丢弃消息
					}
					break;
				}
			}
		}
		
		public void doDestroy() {
			logger.info("ZeroMQReceiver(Consumer {} with listener {} on {}) prepare to stop.", this, this.listener, this.address);
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
			finally {
				this.interrupt();
				logger.info("ZeroMQReceiver(Consumer {} with listener {} on {}) stoped.", this, this.listener ,this.address);
			}
		}
		
		private void doClose() {
			try {
				close(context);
			}
			finally {
				close(subscriber);
			}
		}
		
		private void close(Closeable obj) {
			try{
				IOUtils.closeQuietly(obj);
			}
			catch (Exception exp) {
				logger.trace("Closeable object {} closeed by error.", obj, exp);
			}
		}
		
		/**
		 * 反序列化的方法
		 * @param msg
		 * @return
		 * @throws Exception
		 * @since 2016年7月5日
		 * @author liudian@yikuyi.com
		 */
		private Message parseObject(byte[] msg) {
			return (Message)SerializationUtils.deserialize(msg);

		}
	}

	@Override
	public void destroy(){
		logger.info("ZeroMQReceiver({}) prepare to stop.", this);
		for (Consumer consumer : this.consumers){
			consumer.doDestroy();
		}
		this.consumers.clear();
		logger.info("ZeroMQReceiver({}) stoped.", this);
	}
	
	@Override
	public void registerSubscription(final SimpleMQSubscription subs) throws MQException {
		//从队列的最后位置开始消费后续再启动接着上次消费的进度开始消费
		if (subs.isClustering()) {
			//不做其他处理
		} else {
			//不做其他处理
		}
		if (subs.isOrderly()) {
			//没有实现顺序发送
			
		}
		try {
			TopicMapper tMapper = this.getTopicMapping().findTopicMapper(subs.getTopicName());
			Consumer consumer = new Consumer(subs.getMsgReceiveListener(), tMapper, ZeroMQServer.ZEROMP_PUBLISH_URL);
			this.consumers.add(consumer);
			consumer.start();
			logger.info("Listener {} is registered and started on {}:{}@{}  with group {}", subs.getMsgReceiveListener(), tMapper.getOriginalTopic(), tMapper.getFilter(), ZeroMQServer.ZEROMP_PUBLISH_URL, subs.getConsumerGroup() );
		} catch (Exception e) {
			logger.error("error", e);
			throw new MQException(e);
		}
	}
}
