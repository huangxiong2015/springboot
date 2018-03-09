/*
 * Created: 2016年6月12日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
/**
 * 
 */
package com.ykyframework.oss;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 阿里云目录文件的访问类。
 * @author liudian@yikuyi.com
 * @version 1.0.0
 */
public class AliyunOSSDirectory {
	
	private AliyunOSSBucket bucket;
	
	private String directory;
	
	public AliyunOSSDirectory(AliyunOSSBucket bucket, String dir) {
		this.bucket = bucket;
		this.directory = AliyunOSSHelper.trimDir(dir);
	}
	
	/**
	 * @return the bucket
	 */
	final AliyunOSSBucket getBucket() {
		return bucket;
	}

	/**
	 * @return the directory
	 */
	final String getDirectory() {
		return directory;
	}

	public Map<String, String> getPostSignature(String dir, String name, AliyunOSSHashType hashType, long expireSeconds, boolean isImage) {
		String key = this.getKey(dir, name, hashType);
		return bucket.getPostSignature(key, expireSeconds, isImage);
	}
	
	public Map<String, String> getPostSignature(String dir, String name, AliyunOSSHashType hashType, long expireSeconds) {
		return this.getPostSignature(dir, name, hashType, expireSeconds, false);
	}
	
	public String putObject(String dir, String name, AliyunOSSHashType hashType, InputStream input){
		String key = this.getKey(dir, name, hashType);
		return bucket.putObject(key, input);
	}
	
	public boolean deleteObject(String dir, String name){
		String key = this.getKey(dir, name, AliyunOSSHashType.FRONT_DISCRETE);
		return bucket.deleteObject(key);
	}
	
	private String getKey(String dir, String name, AliyunOSSHashType hashType) {
		String rDir = AliyunOSSHelper.trimDir(this.directory + dir);
		return rDir + this.generateHash(hashType, name);
	}
	
	private String generateHash(AliyunOSSHashType hashType, String name) {
		//前端已经进行了离散化处理
		if (AliyunOSSHashType.FRONT_DISCRETE.equals(hashType)){
			return name;
		}
		String md5 = DigestUtils.md5Hex(UUID.randomUUID().toString());
		String dir = null;
		if (AliyunOSSHashType.DATE_DISCRETE.equals(hashType)){
			Date date = new Date();
			dir = AliyunOSSHelper.trimDir(new SimpleDateFormat("yyyyMM").format(date) + "/" + new SimpleDateFormat("dd").format(date));
		}
		else if (AliyunOSSHashType.MD5_DISCRETE.equals(hashType)) {			
			dir = AliyunOSSHelper.trimDir(md5.substring(0, 2) + "/" + md5.substring(2, 5));
		}
		String ext = FilenameUtils.getExtension(name);
		return dir + md5 + (StringUtils.isEmpty(ext) ? "" : "." + ext); 
	}
}
