/*
 * Created: 2016年1月4日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ictrade.tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;


public class CommonUtils {
	
	/**
	 * 工具类不需要构造函数
	 * @author tongkun@yikuyi.com
	 */
	private CommonUtils(){}

	/**
	 * 转义HTML特殊字符
	 * @param sqlStr
	 * @return
	 * @since 2016年5月16日
	 * @author zr.shuzuo@yikuyi.com
	 */
	public static String htmlFilter(String sqlStr) {
		if (StringUtils.isBlank(sqlStr)) {
			return sqlStr;
		}
		String resultSql = sqlStr;
		resultSql = resultSql.replaceAll("'", "\\\\'");
		resultSql = resultSql.replaceAll("\"", "\\\\\"");
		return resultSql;
	}

	/*
	 * 判断是否为浮点数，包括double和float
	 * 
	 * @param str 传入的字符串
	 * 
	 * @return 是浮点数返回true,否则返回false
	 */
	public static boolean isDouble(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
		return pattern.matcher(str).matches();
	}

	/*
	 * 判断是否是大于零的数字
	 * 
	 * @param str 传入的字符串
	 * 
	 * @return 大于零返回true,否则返回false
	 */
	public static boolean moreThanZero(String str) {
		Pattern pattern = Pattern.compile("^[1-9]\\d*$");
		return pattern.matcher(str).matches();
	}
	/**
	 * 转义MySql特殊字符
	 * 
	 * @param sqlStr
	 * @return
	 * @since 2016年3月23日
	 * @author zr.xuheng@yikuyi.com
	 */
	public static String sqlFilter(String sqlStr) {
		if (StringUtils.isBlank(sqlStr)) {
			return sqlStr;
		}
		String resultSql = sqlStr; 
		resultSql = resultSql.replaceAll("\\\\", "\\\\\\\\");
		resultSql = resultSql.replaceAll("'", "\\\\'");
		resultSql = resultSql.replaceAll("_", "\\\\_");
		resultSql = resultSql.replaceAll("%", "\\\\%");
		resultSql = resultSql.replaceAll("\"", "\\\\\"");
		return resultSql;
	}
	
	/**
	 * 去掉首尾空格
	 */
	public static String trim(String str){
		if(str == null){
			return "";
		}
		return str.trim();
	}
	/**
	 * 获取指定时间时分秒
	 * 
	 * @return
	 * @since 2016年1月4日
	 * @author zr.gongtianyu@yikuyi.com
	 */
	public static String getTimeStr(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		return df.format(date);
	}
	
	/**
	 * 获取指定日期字符串
	 * @param date
	 * @return
	 * @since 2016年9月1日
	 * @author zr.wujiajun@yikuyi.com
	 */
	public static String getDateStr(Date date){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);
	}
}
