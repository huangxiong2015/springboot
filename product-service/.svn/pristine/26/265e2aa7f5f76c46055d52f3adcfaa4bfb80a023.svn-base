/*
 * Created: 2017年11月8日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.listener;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yikuyi.product.material.bll.MaterialManager;
import com.ykyframework.mqservice.receiver.MsgReceiveListener;
@Service
public class UploadFileProductReceiveListener implements MsgReceiveListener{
	private static final Logger logger = LoggerFactory.getLogger(UploadFileProductReceiveListener.class);

	@Autowired
	private MaterialManager materialManager;
	
	@Override
	public void onMessage(Serializable arg) {
		logger.info("UploadFileProductReceiveListener start");
		if (null == arg) {
			logger.error("UploadFileProductReceiveListener.java arg is null!");
			return;
		}
		materialManager.uploadFile((String)arg);
		logger.info("UploadFileProductReceiveListener end");
	}

}
