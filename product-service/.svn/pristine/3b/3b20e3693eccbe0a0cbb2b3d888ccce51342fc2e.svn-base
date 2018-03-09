/*
 * Created: 2017年2月22日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.uploadutils;

public final class UploadUtils {
	
	private UploadUtils(){}
	
	/**
	 * 过滤所有null和null字符串
	 * @param i
	 * @param params
	 * @return
	 * @since 2017年1月12日
	 * @author zr.shuzuo@yikuyi.com
	 */
	public static final String getParam(int i, String[] params) {
		String param;
		// 非空判断
		if (i < params.length) {
			//当内容是小数时去除小数点后面多余的0，否则不处理
			if(com.ictrade.tools.StringUtils.isPositiveDecimal(params[i].trim())){
				param = com.ictrade.tools.StringUtils.subZeroAndDot(params[i].trim());//去除小数点后面多余的0
			}else{
				param = params[i].trim();
			}
			// json中表示空的字符串
			if ("null".equals(param)) {
				param = "";
			}
		} else {
			param = "";
		}
		return param;
	}
}
