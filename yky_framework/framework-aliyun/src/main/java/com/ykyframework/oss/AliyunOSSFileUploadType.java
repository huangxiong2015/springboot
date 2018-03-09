/*
 * Created: 2016年4月6日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ykyframework.oss;

/**
 * 上传类型的endpoint和目标上传路径targetPath配置
 * 
 * @author longyou@yikuyi.com
 * @version 1.0.0
 */
public class AliyunOSSFileUploadType {

	private AliyunOSSDirectory directory; // OSS endpoint

	private AliyunOSSHashType hashType = AliyunOSSHashType.FRONT_DISCRETE;

	private String targetPath; // 上传的目标路径
	
	public AliyunOSSFileUploadType(){	
		//默认构造函数,为兼容spring试用properties初始化
	}
	
	public AliyunOSSFileUploadType(AliyunOSSDirectory directory, AliyunOSSHashType hashType, String targetPath) {
		this.directory = directory;
		this.hashType = hashType;
		this.targetPath = targetPath;
	}

	/**
	 * @return the hashType
	 */
	public final AliyunOSSHashType getHashType() {
		return hashType;
	}

	/**
	 * @param hashType
	 *            the hashType to set
	 */
	public final void setHashType(AliyunOSSHashType hashType) {
		this.hashType = hashType;
	}

	/**
	 * @return the directory
	 */
	public final AliyunOSSDirectory getDirectory() {
		return directory;
	}

	/**
	 * @param directory
	 *            the directory to set
	 */
	public final void setDirectory(AliyunOSSDirectory directory) {
		this.directory = directory;
	}

	public String getTargetPath() {
		return targetPath;
	}

	public void setTargetPath(String targetPath) {
		this.targetPath = targetPath;
	}
}
