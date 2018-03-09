/*
 * Created: 2017年1月12日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.uploadutils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yikuyi.template.model.ProductTemplate.TemplaterValieKey;
import com.ykyframework.exception.SystemException;

/**
 * 校验类
 * 
 * @author zr.shuzuo@yikuyi.com
 * @version 1.0.0
 */
public final class ValidateClass {

	private static final Logger logger = LoggerFactory.getLogger(ValidateClass.class);
	
	public static final String IS_NUMBER_STR = "%s必须是数字";
	public static final String NOT_EMPTY_STR = "%s不能为空";
	public static final String MORE_THANZERO_STR = "%s大于0才为有效数据";
	public static final String MORE_THANZEROINT_STR = "%s大于0的正整数才为有效数据";
	public static final String MORE_THANZEROINTANDZERO_STR = "%s大于等于0的正整数才为有效数据";
	public static final String MORE_THANMINUSONEANDMINUSONE_STR = "%s大于等于-1的整数才为有效数据";
	public static final String MIN_STR = "%s的长度不能小于%s位";
	public static final String MAX_STR = "%s的长度不能大于%s位";
	public static final String MINNUM_STR = "%s不能小于%s";
	public static final String MAXNUM_STR = "%s不能大于%s";
	public static final String QUALIFY_STR = "%s只能选择%s";
	public static final String DEFAULTRULES_STR = "%s的长度不能大于%s";
	public static final String REXGEX_STR = "%s被过滤规则舍弃";
	public static final String VALIDAT_EEXPIRY_DATE_STR = "%s必须大于今天";
	public static final String VALIDAT_DATE_FORMAT_STR = "%s格式不正确";

	private String errorMsg;

	private Predicate<String[]> condition;
	
	/**
	 * 实例化校验类
	 * 
	 * @param errorMsg(错误信息)
	 * @param condition(具体校验规则)
	 */
	public ValidateClass(String errorMsg, Predicate<String[]> condition) {
		this.errorMsg = errorMsg;
		this.condition = condition;
	}

	/**
	 * 如果getRst返回false,调用getMsg获取错误信息 arg[0] EXCEL要显示的行 arg[1]
	 * 校验的具体规则,例如:NotEmpty,Min=1,Max=40,Qualify=USD-CNY-RMB"
	 * 
	 * @param arg(支持多参数传入,可扩展)
	 * @return
	 * @since 2017年1月18日
	 * @author zr.shuzuo@yikuyi.com
	 */
	public final String getMsg(Object... arg) {
		String val = String.valueOf(arg[1]).indexOf('=') >= 0 ? String.valueOf(arg[1]).split("=")[1]
				: String.valueOf(arg[1]);
		arg[1] = val;
		return String.format(errorMsg, arg);
	}

	/**
	 * getRst方法会调用具体的校验规则 args[0] EXCEL对应数据的KEY args[1]
	 * 校验的具体内容,例如:NotEmpty,Min=1,Max=40,Qualify=USD-CNY-RMB" args
	 * 
	 * @param args(支持多参数传入,可扩展)
	 * @return true代表校验成功,数据符合标准,false代表校验失败,数据不符合标准
	 * @since 2017年1月18日
	 * @author zr.shuzuo@yikuyi.com
	 */
	public final boolean getRst(String... args) {
		return condition.test(args);
	}

	/**
	 * 方法返回true代表符合条件,是正确数据 args[0]excel传入参数 args[1]product_template.validate的配置
	 * args[2]可扩展
	 * 
	 * @author zr.shuzuo@yikuyi.com
	 * @version 1.0.0
	 */
	public static final class Rules {

		private Rules() {
		}
		
		private static boolean isMatch(String regex, String orginal) {
			if (orginal == null || orginal.trim().equals("")) {
				return false;
			}
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher  = pattern.matcher(orginal);
			return matcher.matches();
		}

		/*
		 * 是否是数字
		 */
		public static final boolean isNumber(String[] args) {
			if (null == args || StringUtils.isBlank(args[0])) {
				return true;
			}
			try {
			//	return StringUtils.isNumeric(args[0]);
				//new BigDecimal(args[0]);
				return com.ictrade.tools.StringUtils.isRealNumber(args[0]);
			} catch (Exception e) {
				logger.info(e.getMessage(),e);
				return false;
			}	
		}
		
		/*
		 * 非空校验
		 */
		public static final boolean notEmpty(String[] args) {
			return StringUtils.isNotBlank(args[0]);
		}

		/*
		 * 大于0 的正整数数字校验
		 */
		public static final boolean moreThanZeroInt(String[] args) {
			if (null == args || StringUtils.isBlank(args[0])) {
				return true;
			}
			if(!com.ictrade.tools.StringUtils.isRealNumber(args[0])){
				return false;
			}
			if(new BigDecimal(args[0]).compareTo(BigDecimal.ZERO) > 0 && new BigDecimal(args[0]).divideAndRemainder(BigDecimal.ONE)[1].compareTo(BigDecimal.ZERO) == 0){
				return true;
			}
			return false;
		}
		
		/*
		 * 大于等于0 的正整数数字校验
		 */
		public static final boolean moreThanZeroAndZero(String[] args) {
			if (null == args || StringUtils.isBlank(args[0])) {
				return true;
			}
			if(!com.ictrade.tools.StringUtils.isRealNumber(args[0])){
				return false;
			}
			if (new BigDecimal(args[0]).compareTo(BigDecimal.ZERO) == 0) {
				return true;
			}
			if(new BigDecimal(args[0]).compareTo(BigDecimal.ZERO) >= 0 && new BigDecimal(args[0]).divideAndRemainder(BigDecimal.ONE)[1].compareTo(BigDecimal.ZERO) == 0){
				return true;
			}
			return false;
		}
		
		/*
		 * 大于等于-1 的整数数字校验
		 */
		public static final boolean moreThanMinusOneAndMinusOne(String[] args) {
			if (null == args || StringUtils.isBlank(args[0])) {
				return true;
			}
			if(!com.ictrade.tools.StringUtils.isRealNumber(args[0])){
				return false;
			}
			if(com.ictrade.tools.StringUtils.isDecimal(args[0])){
				return false;
			}
			if(Long.valueOf(args[0])>=-1){
				return true;
			}
			return false;
		}
		
		/*
		 * 大于0 的数字校验
		 */
		public static final boolean moreThanZero(String[] args) {
			if (null == args || StringUtils.isBlank(args[0])) {
				return true;
			}
			if(!com.ictrade.tools.StringUtils.isRealNumber(args[0])){
				return false;
			}
			if(new BigDecimal(args[0]).compareTo(BigDecimal.ZERO) > 0){
				return true;
			}
			return false;
		}
		
		/*
		 * 不能低于XX最小长度
		 */
		public static final boolean min(String[] args) {
			if (null == args || StringUtils.isBlank(args[0])) {
				return true;
			}
			return args[0].length() >= Integer
					.parseInt(args[1].substring(TemplaterValieKey.MIN.getValue().length() + 1));
		}

		/*
		 * 不能大于XX最大长度
		 */
		public static final boolean max(String[] args) {
			if (null == args || StringUtils.isBlank(args[0])) {
				return true;
			}
			return args[0].length() <= Integer
					.parseInt(args[1].substring(TemplaterValieKey.MAX.getValue().length() + 1));
		}

		/*
		 * 不能低于XX最小数字
		 */
		public static final boolean minNum(String[] args) {
			if (null == args || StringUtils.isBlank(args[0])) {
				return true;
			}
			if(!com.ictrade.tools.StringUtils.isRealNumber(args[0])){
				return false;
			}
			return new BigDecimal(args[0]).compareTo(new BigDecimal(args[1].substring(TemplaterValieKey.MIN_NUM.getValue().length() + 1))) >= 0;
		}

		/*
		 * 不能大于XX最大数字
		 */
		public static final boolean maxNum(String[] args) {
			if (null == args || StringUtils.isBlank(args[0])) {
				return true;
			}
			return new BigDecimal(args[0]).compareTo(new BigDecimal(args[1].substring(TemplaterValieKey.MIN_NUM.getValue().length() + 1))) <= 0;
		}

		/*
		 * 限制内容(忽略大小写)
		 */
		public static final boolean qualify(String[] args) {
			if (null == args || StringUtils.isBlank(args[0])) {
				return true;
			}
			String param = args[0];
			String[] qualifyVal = args[1].substring(TemplaterValieKey.QUALIFY.getValue().length() + 1).split("-");
			for (int i = 0; i < qualifyVal.length; i++) {
				if (param.equalsIgnoreCase(qualifyVal[i])) {
					return true;
				}
			}
			return false;
		}

		/*
		 * 不能大于2000最大长度
		 */
		public static final boolean defaultRules(String[] args) {
			return args[0].length() <= 2000;
		}
		
		public static final boolean regex(String[] args){
			return isMatch(args[1].substring(TemplaterValieKey.REGEX.getValue().length() + 1), args[0]);
		}
		/**
		 * 校验日期格式
		 * @param args
		 * @return
		 * @since 2017年11月14日
		 * @author tb.lijing@yikuyi.com
		 */
		public static final boolean validateDateFormat(String[] args){
			return isMatch(args[1].substring(TemplaterValieKey.VALIDATE_DATE_FORMAT.getValue().length() + 1), args[0]);
		}
		
		/**
		 * 校验失效日期必须大于今天
		 * @param args
		 * @return
		 * @since 2017年11月13日
		 * @author tb.lijing@yikuyi.com
		 */
		public static final boolean validateExpiryDate(String[] args){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
			String currentTime = sdf.format(new Date());//当前时间
		    Date currentDate;
		    Date expiryDate;
		    long days = 0;
		    try {
		    	currentDate = sdf.parse(currentTime);
				expiryDate=sdf.parse(args[0]);
				days=(expiryDate.getTime()-currentDate.getTime())/(1000*3600*24);//失效日期减去当前时间
			} catch (ParseException e) {
				logger.error("parse date error ,messge is {}",e.getMessage());
				throw new SystemException(e);
			}
				return days>0;
		}
		
	}
}