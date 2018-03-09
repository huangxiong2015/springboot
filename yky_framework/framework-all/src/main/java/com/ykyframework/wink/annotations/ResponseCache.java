/*
 * Created: 2015年12月21日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2015 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ykyframework.wink.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Wink Rest响应缓存的annotation类，用于在http response里增加对应的浏览器缓存
 * 
 * @see com.ykyframework.wink.handlers.SetCacheHandler
 * @author liaoke@yikuyi.com
 * @version 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ResponseCache {
	
	/**
	 * 缓存的天数
	 */
	int day() default 0;
	/**
	 * 缓存的小时数
	 */
	int hour() default 0;
	/**
	 * 缓存的分钟数
	 */
	int minute() default 0;
	/**
	 * 缓存的秒数
	 */
	int second() default 0;
	/**
	 * 是否支持缓存
	 */
	boolean eTagEnable() default false;
	
//	/**
//	 * 缓存的到期时间，格式:yyyy-MM-dd HH:mm:ss
//	 */
//	String expireAt() default null;
	

}
