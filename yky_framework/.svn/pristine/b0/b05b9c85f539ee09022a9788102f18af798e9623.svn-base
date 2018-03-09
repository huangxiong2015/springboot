/*
 * Created: 2016年3月31日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ykyframework.utils.file.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import com.ykyframework.utils.file.FileUtils;
import com.ykyframework.utils.file.inter.FileOperationInter;

public class FileOperationForLocal implements FileOperationInter {

	/* (non-Javadoc)
	 * @see com.ykyframework.utils.file.inter.FileOperationInter#write(java.lang.String, java.lang.String)
	 */
	@Override
	public String write(final InputStream in, final String targetPath) throws IOException {
		// TODO Auto-generated method stub
		final String fileName = UUID.randomUUID().toString()+".file";
		String targetFileName = FileUtils.create().getFilePath(targetPath, fileName);
		FileUtils.create().writeInputStreamToFile(in, targetFileName);
		return fileName;
	}

	/* (non-Javadoc)
	 * @see com.ykyframework.utils.file.inter.FileOperationInter#read(java.lang.String)
	 */
	@Override
	public InputStream read(final String filePath) throws IOException {
		// TODO Auto-generated method stub
		return new FileInputStream(filePath);
	}

	@Override
	public String write(String in,final String targetPath) throws IOException {
		// TODO Auto-generated method stub
		final String fileName = UUID.randomUUID().toString()+".file";
		String targetFileName = FileUtils.create().getFilePath(targetPath, fileName);
		FileUtils.create().writeStringToFile(in, targetFileName);
		return fileName;
	}

	@Override
	public String write(byte[] in, String targetPath) throws IOException {
		// TODO Auto-generated method stub
		final String fileName = UUID.randomUUID().toString()+".file";
		String targetFileName = FileUtils.create().getFilePath(targetPath, fileName);
		FileUtils.create().writeBytesToFile(in, targetFileName);
		return fileName;
	}

}
