/*
 * Created: 2017年3月29日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.document.bll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.yikuyi.material.vo.MaterialVo;
import com.yikuyi.product.material.bll.MaterialManager;

@Service
@Transactional
public class SyncElasticsearchProductManager {
	
	@Autowired
	private MaterialManager materialManager;
	
	public void syncElasticsearchProduct(@RequestBody MaterialVo materialVo) {
		if (null == materialVo.getMsg() || materialVo.getMsg().isEmpty()) {
			materialManager.syncResultProcessing(materialVo.getDocId(), materialVo.getSize());
		} else {
			materialManager.syncElasticsearchProductUpdate(materialVo);
		}
	}
	
	public void syncElasticsearchProductUpdate(@RequestBody MaterialVo materialVo) {
		materialManager.syncElasticsearchProductUpdate(materialVo);
	}
}
