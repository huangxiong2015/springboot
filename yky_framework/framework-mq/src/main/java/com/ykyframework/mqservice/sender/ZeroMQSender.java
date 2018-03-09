/*
 * Created: 2016年9月6日
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
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import com.ykyframework.mqservice.MQConstants;
import com.ykyframework.mqservice.MQTopicMapping.TopicMapper;

/**
 * A simple mq sender implements by ZeroMQ. It's only for development environment.
 * @author liudian@yikuyi.com
 * @version 1.0.0
 */
public class ZeroMQSender extends MappedMQSender {	
	
	private static final Logger logger = LoggerFactory.getLogger(ZeroMQSender.class);
	
	private static ZeroMQServer mqServer;
	
	private static final AtomicInteger senderCount = new AtomicInteger();
		
	private Socket publiser = null;
	
	private Context context = null;
	
	private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MICROSECONDS, new ArrayBlockingQueue<Runnable>(1, true));
		
	private static Future<Boolean> mqServerFuture = null;
	
	public ZeroMQSender() {				
		/*
		 * constructor for spring injection with properties setter 
		 */
	}
	
	public void init() {		
		if (senderCount.getAndIncrement() == 0) {
			initMQServer();
		}
		this.context = ZMQ.context(1);
		this.publiser = context.socket(ZMQ.PUB);
		//connect to local network interface
		//连接到端口订阅端口，发布消息
		publiser.connect(ZeroMQServer.ZEROMP_SUBSCRIBE_URL);
		logger.info("Message sender({}) for zeromq({}) started.", this, ZeroMQServer.ZEROMP_SUBSCRIBE_URL);
	}

	/**
	 * 
	 * @since 2017年5月9日
	 * @author liudian@yikuyi.com
	 */
	private static void initMQServer() {
		mqServer = new ZeroMQServer();
		logger.info("Message sender for zeromq({}) start.", ZeroMQServer.ZEROMP_SUBSCRIBE_URL);			
		mqServerFuture = executor.submit(mqServer, Boolean.TRUE);
	}
	
	public void destroy() {
		logger.info("Message sender for zeromq:{} prepare to stop.", this);
		try{
			if (senderCount.decrementAndGet() == 0) {
				destoryMQServer();
			}
		}
		finally {
			close(this.publiser);
			close(context);
			logger.info("Message sender for zeromq:{} stoped", this);
		}
	}

	/**
	 * 
	 * @since 2017年5月9日
	 * @author liudian@yikuyi.com
	 */
	private static void destoryMQServer() {
		try{
			mqServer.doDestroy();
		}
		finally{
			if (!mqServerFuture.isDone()) {
				mqServerFuture.cancel(true);
				mqServerFuture = null;
			}
			executor.shutdown();
		}
	}	

	
	private void close(Closeable obj) {
		try{
			IOUtils.closeQuietly(obj);
		}
		catch (Exception exp) {
			logger.error("Exception on close, cause: ", exp);			
		}
	}

	/* (non-Javadoc)
	 * @see com.ykyframework.mqservice.sender.MsgSender#sendMsgNoTransaction(java.lang.String, java.io.Serializable, java.util.Map)
	 */
	@Override
	public boolean sendMsgNoTransaction(String queueName, Serializable msg, Map<String, String> props) {
		if (msg == null || StringUtils.isBlank(queueName)) {
			return false;
		}
		byte[] buffer = this.packageMessage(queueName, msg, props);
		return this.publiser.send(buffer);
	}

	/* (non-Javadoc)
	 * @see com.ykyframework.mqservice.sender.MsgSender#sendOrderedMsgNoTransaction(java.lang.String, java.io.Serializable, java.util.Map, java.io.Serializable)
	 */
	@Override
	public boolean sendOrderedMsgNoTransaction(String queueName, Serializable msg, Map<String, String> props,
			Serializable shardBy) {
		this.sendOrderedMsg(queueName, msg, props, shardBy);
		return true;
	}

	/* (non-Javadoc)
	 * @see com.ykyframework.mqservice.sender.MsgSender#sendMsg(java.lang.String, java.io.Serializable, java.util.Map)
	 */
	@Override
	public void sendMsg(String queueName, Serializable msg, Map<String, String> props) {
		this.sendMsgNoTransaction(queueName, msg, props);
	}
	
	private byte[] packageMessage(String queueName, Serializable msg, Map<String, String> props) {
		TopicMapper topicMapper = this.getTopicMapping().findTopicMapper(queueName);
		
		Message message = new Message(topicMapper.getOriginalTopic(), topicMapper.getFilter(), msg);

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
		byte[] messageBuffer = SerializationUtils.serialize(message);
		return ArrayUtils.addAll(ZeroMQServer.getMessageFilter(topicMapper.getOriginalTopic(), topicMapper.getFilter()), messageBuffer);
	}

	/* (non-Javadoc)
	 * @see com.ykyframework.mqservice.sender.MsgSender#sendOrderedMsg(java.lang.String, java.io.Serializable, java.util.Map, java.io.Serializable)
	 */
	@Override
	public void sendOrderedMsg(String queueName, Serializable msg, Map<String, String> props, Serializable shardBy) {
		this.sendMsg(queueName, msg, props);
	}
	
	static final class MessageConst {
		 /**
	     * 消息关键词，多个Key用KEY_SEPARATOR隔开（查询消息使用）
	     */
	    public static final String PROPERTY_KEYS = "KEYS";
	    /**
	     * 消息标签，只支持设置一个Tag（服务端消息过滤使用）
	     */
	    public static final String PROPERTY_TAGS = "TAGS";
	    
	    public static final String KEY_SEPARATOR = " ";
	    
	    protected static final HashSet<String> systemKeySet = new HashSet<>();


	    static {
	        systemKeySet.add(PROPERTY_KEYS);
	        systemKeySet.add(PROPERTY_TAGS);
	    }
	    
	    private MessageConst() {
	    	/*
	    	 * A private constructor for hidden the public one 
	    	 */
	    }
	}
	
	public static class ContainsSysPropertyException extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = -5444286974921479502L;
		
		/**
		 * 
		 */
		public ContainsSysPropertyException() {
			super();
		}

		/**
		 * @param message
		 * @param cause
		 * @param enableSuppression
		 * @param writableStackTrace
		 */
		public ContainsSysPropertyException(String message, Throwable cause, boolean enableSuppression,
				boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
		}

		/**
		 * @param message
		 * @param cause
		 */
		public ContainsSysPropertyException(String message, Throwable cause) {
			super(message, cause);
		}

		/**
		 * @param message
		 */
		public ContainsSysPropertyException(String message) {
			super(message);
		}

		/**
		 * @param cause
		 */
		public ContainsSysPropertyException(Throwable cause) {
			super(cause);
		}
		
	}
	
	public static class Message implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -4868260533202498350L;

		/**
		 * 消息主题
		 */
		private String topic;
		/**
		 * 消息标志，系统不做干预，完全由应用决定如何使用
		 */
		private int flag;
		/**
		 * 消息属性，都是系统属性，禁止应用设置
		 */
		private Map<String, String> properties;
		/**
		 * 消息体
		 */
		private Serializable body;

		public Message() {
			/*
			 * a constructor default
			 */
		}

		public Message(String topic, Serializable body) {
			this(topic, "", "", 0, body);
		}

		public Message(String topic, String tags, String keys, int flag, Serializable body) {
			this.topic = topic;
			this.flag = flag;
			this.body = body;

			if (tags != null && tags.length() > 0)
				this.setTags(tags);

			if (keys != null && keys.length() > 0)
				this.setKeys(keys);
		}

		public Message(String topic, String tags, Serializable body) {
			this(topic, tags, "", 0, body);
		}

		public Message(String topic, String tags, String keys, Serializable body) {
			this(topic, tags, keys, 0, body);
		}

		public void setKeys(String keys) {
			this.putProperty(MessageConst.PROPERTY_KEYS, keys);
		}

		void putProperty(final String name, final String value) {
			if (null == this.properties) {
				this.properties = new HashMap<>();
			}

			this.properties.put(name, value);
		}

		void clearProperty(final String name) {
			if (null != this.properties) {
				this.properties.remove(name);
			}
		}

		public void putUserProperty(final String name, final String value) {
			if (MessageConst.systemKeySet.contains(name)) {
				throw new ContainsSysPropertyException(
						String.format("The Property<%s> is used by system, input another please", name));
			}
			this.putProperty(name, value);
		}

		public String getUserProperty(final String name) {
			return this.getProperty(name);
		}

		public String getProperty(final String name) {
			if (null == this.properties) {
				this.properties = new HashMap<>();
			}

			return this.properties.get(name);
		}

		public String getTopic() {
			return topic;
		}

		public void setTopic(String topic) {
			this.topic = topic;
		}

		public String getTags() {
			return this.getProperty(MessageConst.PROPERTY_TAGS);
		}

		public void setTags(String tags) {
			this.putProperty(MessageConst.PROPERTY_TAGS, tags);
		}

		public String getKeys() {
			return this.getProperty(MessageConst.PROPERTY_KEYS);
		}

		public void setKeys(Collection<String> keys) {
			StringBuilder sb = new StringBuilder();
			for (String k : keys) {
				sb.append(k);
				sb.append(MessageConst.KEY_SEPARATOR);
			}

			this.setKeys(sb.toString().trim());
		}

		public int getFlag() {
			return flag;
		}

		public void setFlag(int flag) {
			this.flag = flag;
		}

		public Serializable getBody() {
			return body;
		}

		public void setBody(Serializable body) {
			this.body = body;
		}

		public Map<String, String> getProperties() {
			return properties;
		}

		void setProperties(Map<String, String> properties) {
			this.properties = properties;
		}

		@Override
		public String toString() {
			return "Message [topic=" + topic + ", flag=" + flag + ", properties=" + properties + ", body=" + body + "]";
		}
	}

}