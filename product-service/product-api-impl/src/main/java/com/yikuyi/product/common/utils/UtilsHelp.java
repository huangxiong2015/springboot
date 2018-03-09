/*
 * Created: 2017年3月28日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 帮助类
 * 
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
public class UtilsHelp {

	private UtilsHelp() {
		super();
	}

	/**
	 * 某个时间的当天凌晨时刻
	 * 
	 * @param date
	 * @return
	 * @since 2017年10月13日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public static Date getDayBegin(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		return new Date(cal.getTimeInMillis());
	}

	/**
	 * 某个时间顺延一周
	 * 
	 * @param date
	 * @return
	 * @since 2017年10月13日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public static Date getDayWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 7);
		return new Date(cal.getTimeInMillis());
	}

	/**
	 * 时间戳转化为时间格式
	 * 
	 * @param seconds
	 * @return
	 * @since 2017年12月25日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public static String timeStamp2Date(String seconds) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String res;
		long time = Long.parseLong(seconds);
		res = format.format(time);
		return res;
	}
	
	
	/**
	 * 获取当前时间格式
	 * @return
	 * @since 2018年1月10日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public static String currentTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		return format.format(date);
	}

}
