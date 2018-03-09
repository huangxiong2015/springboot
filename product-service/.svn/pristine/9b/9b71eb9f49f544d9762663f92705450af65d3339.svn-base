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
package com.yikuyi.product.document.bll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.yikuyi.document.model.DocumentLog;
import com.yikuyi.document.model.Document.DocumentStatus;
import com.yikuyi.material.vo.MaterialVo;
import com.yikuyi.product.basicmaterial.bll.BasicMaterialManager;
import com.yikuyi.product.material.bll.MaterialManager;

@Service
@Transactional
public class ParseImportFileManager {
	
	@Autowired
	private DocumentManager documentManager;
	
	@Autowired
	private BasicMaterialManager basicMaterialManager;
	
	@Autowired
	private MaterialManager materialManager;
	
	public void basicMaterialParseImportFile(@RequestBody MaterialVo materialVo) {
		documentManager.updateDocStatus(materialVo.getDocId(), DocumentStatus.DOC_IN_PROCESS, null);
		try {
			basicMaterialManager.parseImportFile(materialVo);
		} catch (Exception e) {
			documentManager.updateDocStatus(materialVo.getDocId(), DocumentStatus.DOC_PRO_FAILED,
					DocumentLog.getSizeLengthMsg(e));
		}
	}
	
	/**
	 * 解析导入文件
	 * @param materialVo
	 * @since 2017年3月30日
	 * @author tb.lijing@yikuyi.com
	 */
	public void importsParseImportFile(@RequestBody MaterialVo materialVo) {
		// 判断文件是否取消上传
		if (materialManager.isCancelImport(materialVo.getDocId())) {
			documentManager.updateDocStatus(materialVo.getDocId(), DocumentStatus.DOC_CANCEL, null);
		} else {
			documentManager.updateDocStatus(materialVo.getDocId(), DocumentStatus.DOC_IN_PROCESS, null);
			try {
				materialManager.parseImportFile(materialVo);
			} catch (Exception e) {
				documentManager.updateDocStatus(materialVo.getDocId(), DocumentStatus.DOC_PRO_FAILED,
						DocumentLog.getSizeLengthMsg(e));
			}
		}
		// materialManager.deleteFile(fileName);//TODO SZ文件解析完成,删除
	}
}
