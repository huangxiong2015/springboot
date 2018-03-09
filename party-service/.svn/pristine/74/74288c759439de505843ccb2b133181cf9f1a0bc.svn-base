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
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yikuyi.party.supplier.bll.SupplierManager;
import com.ykyframework.mqservice.receiver.MsgReceiveListener;

@Service
public class SupplierCacheReceiveListener implements MsgReceiveListener {

	private static final Logger logger = LoggerFactory.getLogger(SupplierCacheReceiveListener.class);

	@Autowired
	private SupplierManager supplierManager;

	@SuppressWarnings("unchecked")
	@Override
	public void onMessage(Serializable msg) {
		logger.info("Supplier cache refresh complite start!");
		if (null == msg) {
			return;
		}
		try {
			supplierManager.initSupplierCache((Collection<String>) msg);
		} catch (Exception e) {
			logger.error("供应商缓存异步刷新失败:{},{}", e.getMessage(), e);
		}
		logger.info("Supplier cache refresh complite end!");
	}
}