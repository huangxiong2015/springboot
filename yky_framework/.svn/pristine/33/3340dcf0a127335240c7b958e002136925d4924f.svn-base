/*
 * Created: 2017年3月6日
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
package com.ykyframework.mqservice;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 定义一个系统配置topic和物理使用topic的映射关系。系统中定义可根据系统业务需要定制topic名，但实际上使用的物理topic可以共用一个。
 * 物理topic的配置和使用需要在平台和中间件配置打开。
 * 业务定义的topic相互之间共用物理topic时，需要考虑业务的实际用途最好具有相关性或想相似性。考虑相关性主要是为了相关业务对消息顺序的有要求是可以发送顺序消息。
 * 而相似性，则要求同等递送等级要求和处理时效性的消息共享一个topic（例如消息处理优先级相似，同时消息的处理程序时效等级相当等等）。
 * @author liudian@yikuyi.com
 * @version 1.0.0
 */
public class MQTopicMapping {
	
	private Map<String, String> topicMap = null;
	
	private MQTopicMapping() {
		this.topicMap = Collections.synchronizedMap(new HashMap<String, String>());
	}
	
	public MQTopicMapping(Map<String, String> mapping) {
		this();
		this.topicMap.putAll(mapping);
	}
	
	/**
	 * 通过配置的topic名查找实际负责消息的物理topic名
	 * @param source 配置的映射topic名
	 * @return 实际的物理topic名
	 * @since 2017年3月7日
	 * @author liudian@yikuyi.com
	 */
	public String findTopicMapping(String source) {
		String target = this.topicMap.get(source);
		return (target == null) ? source : target;
	}
	
	/**
	 * 通过配置的topic名查找其与实际负责消息的物理topic名的配对结构。
	 * @param source
	 * @return 如果有相应的配对，返回的物理队列为相应的配对队列，而相应的配置队列名则作为消息过滤条件(作为消息的tag）；
	 * 		否则返回物理队列名为其本身，消息过滤条件为其本身。
	 * @since 2017年3月7日
	 * @author liudian@yikuyi.com
	 */
	public TopicMapper findTopicMapper(String source) {
		String target = this.topicMap.get(source);
		return new TopicMapper(target == null ? source : target, source); 
	}
	
	public static class TopicMapper {
		
		private String originalTopic;
		
		private String filter;
		
		public TopicMapper(String topicName, String filterStr) {
			this.originalTopic = topicName;
			this.filter = filterStr;
		}

		/**
		 * @return the originalTopic
		 */
		public final String getOriginalTopic() {
			return originalTopic;
		}

		/**
		 * @param originalTopic the originalTopic to set
		 */
		public final void setOriginalTopic(String originalTopic) {
			this.originalTopic = originalTopic;
		}

		/**
		 * @return the filter
		 */
		public final String getFilter() {
			return filter;
		}

		/**
		 * @param filter the filter to set
		 */
		public final void setFilter(String filter) {
			this.filter = filter;
		}
	}

}
