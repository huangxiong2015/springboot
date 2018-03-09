/*
 * Created: 2017年3月17日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.listener;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yikuyi.party.audit.bll.AuditManager;
import com.yikuyi.party.audit.model.Audit;
import com.ykyframework.mqservice.receiver.MsgReceiveListener;

@Service
public class AuditLogReceiveListener implements MsgReceiveListener {
	private static final Logger logger = LoggerFactory.getLogger(AuditLogReceiveListener.class);

	@Autowired
	private AuditManager auditManager;

	@Override
	public void onMessage(Serializable msg) {
		logger.info("审计日志监听器：{}", msg);
		if (null == msg) {
			logger.error("auditLogReceiveListener msg is null!");
			return;
		}
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			Audit audit = objectMapper.readValue(msg.toString(), Audit.class);
			logger.info("AuditLogReceiveListener receive audit logger#######userId:{},applicationCode:{}",
					audit.getUserId(), audit.getApplicationCode());
			auditManager.insertAduit(audit);
		} catch (Exception e) {
			logger.error("auditLogReceiveListener msg is error!{}" , msg , e);
		}
	}
}