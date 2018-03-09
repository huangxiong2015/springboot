/*
 * Created: 2017年2月21日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.vendorManage.api.impl;

import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ykyframework.exception.SystemException;
import com.ykyframework.oss.AliyunOSSOperator;

/**
 * 文件上传
 * 
 * @author tb.huangqingfeng@yikuyi.com
 * @version 1.0.0
 */
@Service
public class UpFileManager {

	private static Logger logger = LoggerFactory.getLogger(UpFileManager.class);

	@Autowired
	private AliyunOSSOperator aliyunOSSOperator;

	/**
	 * 阿里云文件下载(步骤1) {@link DocumentManager.parseImportFile}
	 * 
	 * @param fileUrl
	 * @param docId
	 * @return
	 * @throws SystemException
	 * @since 2016年12月14日
	 * @author zr.shuzuo@yikuyi.com
	 */
	public List<List<String>> fileDownload(String fileUrl) {
		// 判断后缀
		String fileName = fileUrl.substring(fileUrl.lastIndexOf('.'));
		ExcelUtils ex = new ExcelUtils();

		List<List<String>> list = null;
		if (".xls".equals(fileName) || ".xlsx".equals(fileName)) {
			try {
				InputStream is = this.aliyunOSSOperator.getObject(fileUrl);
				if (".xls".equals(fileName)) {
					list = ex.readXlsIn(is);
				}
				if (".xlsx".equals(fileName)) {
					list = ex.readXlsxIn(is);
				}
			} catch (Exception e) {
				logger.error("文件下载失败：url是：" + fileUrl + " 错误原因是：" + e.getMessage(), e);
				throw new SystemException(e.getMessage(), e);
			}
		} else {
			throw new SystemException("目前只支持.xls,.xlsx文件上传");
		}
		return list;
	}
}
