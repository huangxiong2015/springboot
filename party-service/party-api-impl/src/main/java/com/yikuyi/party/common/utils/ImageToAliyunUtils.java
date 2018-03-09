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
package com.yikuyi.party.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.openservices.shade.com.alibaba.rocketmq.shade.io.netty.util.internal.StringUtil;
import com.ykyframework.oss.AliyunOSSAccount;
import com.ykyframework.oss.AliyunOSSHelper;

/**
 * 图片处理，http转化为https
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
public class ImageToAliyunUtils {
	
	
	private static final Logger logger = LoggerFactory.getLogger(ImageToAliyunUtils.class);
	
	
	/**
	 * 处理图片
	 * @param attachUrl
	 * @return
	 * @since 2017年9月29日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public static String imageToAliyun(String attachUrl,AliyunOSSAccount aliyunOSSAccount) {
		String tempAttachUrl = attachUrl;
		try {
			if (StringUtil.isNullOrEmpty(attachUrl)) {
				return "";
			}
			String newAttachUrl;
			if (!(attachUrl.indexOf("http") == 0 || attachUrl.indexOf("https") == 0)) {
				attachUrl = "https:" + attachUrl;
			}else if(attachUrl.indexOf("http") == 0 && attachUrl.indexOf("https") != 0){//如果包含http则将其替换为https
				attachUrl = attachUrl.replace("http", "https");
			}
			newAttachUrl = AliyunOSSHelper.getAccessUrl(aliyunOSSAccount, attachUrl, true, 1000L);
			return newAttachUrl;
		} catch (Exception e) {
			logger.debug("该图片在阿里云中不存在：{}", attachUrl);
		}
		//将不存在于阿里云中的图片进行https替换处理
		if (!(tempAttachUrl.indexOf("http") == 0 || tempAttachUrl.indexOf("https") == 0)) {
			tempAttachUrl = "https:" + tempAttachUrl;
		}else if(tempAttachUrl.indexOf("http") == 0 && tempAttachUrl.indexOf("https") != 0){
			tempAttachUrl = tempAttachUrl.replace("http", "https");
		}
		return tempAttachUrl;
	}
}