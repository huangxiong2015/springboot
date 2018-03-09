/*
 * Created: 2016年4月4日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.framework.springboot.audit;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.jasig.inspektr.audit.AuditActionContext;
import org.jasig.inspektr.audit.support.AbstractStringAuditTrailManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSONObject;
import com.ykyframework.mqservice.sender.MsgSender;

public final class YkyAuditTrailManager extends AbstractStringAuditTrailManager {

	private static final int DEFAULT_COLUMN_LENGTH = 100;

	private static final String ACTION = "action";

	@Min(50)
	private int columnLength = DEFAULT_COLUMN_LENGTH;

	@Autowired
	private MsgSender msgSender;

	@Value("${mqConsumeConfig.sendAudit.topicName}")
	private String sendAuditMsgTopicName;

	/**
	 * ExecutorService that has one thread to asynchronously save requests.
	 *
	 * You can configure one with an
	 * {@link org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean}
	 * .
	 */
	@NotNull
	private ExecutorService executorService = Executors.newSingleThreadExecutor();

	@Override
	public void record(final AuditActionContext auditActionContext) {
		this.executorService.execute(new LoggingTask(auditActionContext, this.msgSender, this.columnLength));
	}

	protected class LoggingTask implements Runnable {

		private final AuditActionContext auditActionContext;

		private final MsgSender msgSender;

		private final int columnLength;

		public LoggingTask(final AuditActionContext auditActionContext, final MsgSender msgSender, final int columnLength) {
			this.auditActionContext = auditActionContext;
			this.msgSender = msgSender;
			this.columnLength = columnLength;
		}

		@Override
		public void run() {
			String userId = auditActionContext.getPrincipal().length() <= columnLength ? auditActionContext.getPrincipal() : auditActionContext.getPrincipal().substring(0, columnLength);
			String resource = auditActionContext.getResourceOperatedUpon();
			String action = auditActionContext.getActionPerformed().length() <= columnLength ? auditActionContext.getActionPerformed() : auditActionContext.getActionPerformed().substring(0, columnLength);
			JSONObject jsonObject = new JSONObject();
			String actionRst = action.substring(action.lastIndexOf('_') + 1, action.length());
			action = action.substring(0, action.lastIndexOf('_'));
			String[] actionArrays = action.split("qqq;;;");
			if (actionArrays.length == 1) {
				jsonObject.put(ACTION, action);
			}
			if (actionArrays.length == 2) {
				jsonObject.put(ACTION, actionArrays[0]);
				jsonObject.put("actionDesc", actionArrays[1]);
			}
			if (actionArrays.length >= 3) {
				jsonObject.put(ACTION, actionArrays[0]);
				jsonObject.put("actionId", actionArrays[1]);
				jsonObject.put("actionDesc", actionArrays[2]);
			}
			jsonObject.put("userId", userId);
			jsonObject.put("resource", resource);
			jsonObject.put("actionRst", actionRst);
			jsonObject.put("clientIp", auditActionContext.getClientIpAddress());
			jsonObject.put("serverIp", auditActionContext.getServerIpAddress());
			jsonObject.put("applicationCode", auditActionContext.getApplicationCode());
			jsonObject.put("date", auditActionContext.getWhenActionWasPerformed());
			this.msgSender.sendMsg(sendAuditMsgTopicName, jsonObject, null);
		}
	}
}