package com.ictrade.tools;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * 
 * @author tb.lijing@yikuyi.com
 * @version 1.0.0
 */
public class StringUtils {
	private static boolean isMatch(String regex, String orginal) {
		if (orginal == null || orginal.trim().equals("")) {
			return false;
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher isNum = pattern.matcher(orginal);
		return isNum.matches();
	}

	/**
	 * 校验正整数
	 * 
	 * @param orginal
	 * @return
	 * @since 2017年3月31日
	 * @author tb.lijing@yikuyi.com
	 */
	public static boolean isPositiveInteger(String orginal) {
		return isMatch("^\\+{0,1}[1-9]\\d*", orginal);
	}

	/**
	 * 校验负整数
	 * 
	 * @param orginal
	 * @return
	 * @since 2017年3月31日
	 * @author tb.lijing@yikuyi.com
	 */
	public static boolean isNegativeInteger(String orginal) {
		return isMatch("^-[1-9]\\d*", orginal);
	}

	/**
	 * 校验是否为0、正整数、负整数
	 * 
	 * @param orginal
	 * @return
	 * @since 2017年3月31日
	 * @author tb.lijing@yikuyi.com
	 */
	public static boolean isWholeNumber(String orginal) {
		return isMatch("[+-]{0,1}0", orginal) || isPositiveInteger(orginal) || isNegativeInteger(orginal);
	}

	/**
	 * 校验正小数
	 * 
	 * @param orginal
	 * @return
	 * @since 2017年3月31日
	 * @author tb.lijing@yikuyi.com
	 */
	public static boolean isPositiveDecimal(String orginal) {
		return isMatch("\\+{0,1}[0]\\.[0-9]*|\\+{0,1}[1-9]\\d*\\.\\d*", orginal);
	}

	/**
	 * 校验负小数
	 * 
	 * @param orginal
	 * @return
	 * @since 2017年3月31日
	 * @author tb.lijing@yikuyi.com
	 */
	public static boolean isNegativeDecimal(String orginal) {
		return isMatch("^-[0]\\.[0-9]*|^-[1-9]\\d*\\.\\d*", orginal);
	}

	/**
	 * 校验正小数、负小数
	 * 
	 * @param orginal
	 * @return
	 * @since 2017年3月31日
	 * @author tb.lijing@yikuyi.com
	 */
	public static boolean isDecimal(String orginal) {
		return isMatch("[-+]{0,1}\\d+\\.\\d*|[-+]{0,1}\\d*\\.\\d+", orginal);
	}

	/**
	 * 校验正、负科学计数
	 * 
	 * @param orginal
	 * @return
	 * @since 2017年3月31日
	 * @author tb.lijing@yikuyi.com
	 */
	public static boolean isENumber(String orginal) {
		return isMatch("^((-?\\d+.?\\d*)[Ee]{1}(-?\\+?\\d+))$", orginal);
	}

	/**
	 * 校验所有数字和科学计数
	 * 
	 * @param orginal
	 * @return
	 * @since 2017年3月31日
	 * @author tb.lijing@yikuyi.com
	 */
	public static boolean isRealNumber(String orginal) {
		return isWholeNumber(orginal) || isDecimal(orginal) || isENumber(orginal);
	}

	/**
	 * 整数最多7位，小数最多5位的小数
	 * 
	 * @param orginal
	 * @return
	 * @since 2017年6月16日
	 * @author tb.lijing@yikuyi.com
	 */
	public static boolean isPositiveSevenDecimal(String orginal) {
		return isMatch("^[0-9]{1,7}+(.[0-9]{1,5})?$", orginal);
	}

	/**
	 * 去除小数点后面多余的0
	 * @param s
	 * @return
	 * @since 2017年8月24日
	 * @author tb.lijing@yikuyi.com
	 */
	public static String subZeroAndDot(String s) {
		if (s.indexOf(".") > 0) {
			s = s.replaceAll("0+?$", "");// 去掉多余的0
			s = s.replaceAll("[.]$", "");// 如最后一位是.则去掉
		}
		return s;
	}
	
	/**
	 * 校验日期(支持校验平年和闰年)
	 * @param orginal
	 * @return
	 * @since 2017年11月14日
	 * @author tb.lijing@yikuyi.com
	 */
	public static boolean validateDate(String orginal) {
		return isMatch("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))", orginal);
	}
}
