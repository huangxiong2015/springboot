/*
 * Created: 2016年6月8日
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
import java.util.List;
import java.util.Map;

import com.ykyframework.oss.AliyunOSSOperator.AliyunOSSAccessType;

/**
 * 阿里云bucket数据的访问类
 * @author liudian@yikuyi.com
 * @version 1.0.0
 */
public class AliyunOSSBucket {

	private AliyunOSSEndpoint endpoint = null;
	
	private AliyunOSSOperator operator = null;

	private String bucket = null;
	
	public AliyunOSSBucket(AliyunOSSAccount account, AliyunOSSEndpoint endpoint, String bucketName, AliyunOSSAccessType accessType){
		AliyunOSSDomainMapper.putBucketEndpointMapping(bucketName, endpoint);
		this.endpoint = endpoint;
		this.bucket = bucketName;
		this.operator = new AliyunOSSOperator(account, accessType);
	}
	
	public AliyunOSSBucket(String role, String accessId, String accessKey, AliyunOSSEndpoint endpoint, String bucketName, AliyunOSSAccessType accessType){
		this(new AliyunOSSAccount(accessId, accessKey, role) , endpoint, bucketName, accessType);
	}
	
	public AliyunOSSBucket(String role, String accessId, String accessKey, String bucketName, AliyunOSSAccessType accessType){		
		this(role, accessId, accessKey, AliyunOSSDomainMapper.findEndpointbyBucket(bucketName), bucketName, accessType);
	}
	
	public AliyunOSSBucket(AliyunOSSAccount account, String endpoint, String bucketName, String accessType){
		this(account, AliyunOSSEndpoint.valueOf(endpoint), bucketName, AliyunOSSAccessType.valueOf(accessType));
	}
	
	public AliyunOSSBucket(AliyunOSSAccount account, String endpoint, String bucketName){
		this(account, AliyunOSSEndpoint.valueOf(endpoint), bucketName);
	}
	
	public AliyunOSSBucket(AliyunOSSAccount account, AliyunOSSEndpoint endpoint, String bucketName){
		this(account, endpoint, bucketName, AliyunOSSAccessType.OSS_ACCESS_PUBLIC);
	}

	/**
	 * @return the operator
	 */
	protected final AliyunOSSOperator getOperator() {
		return operator;
	}

	/**
	 * @return the endpoint
	 */
	public final AliyunOSSEndpoint getEndpoint() {
		return endpoint;
	}

	public final List<String> getDomains() {
		return AliyunOSSDomainMapper.getDomains(this.bucket);
	}

	public final void setDomains(List<String> domains) {
		AliyunOSSDomainMapper.putBucketDomainMapping(bucket, domains);
	}

	/**
	 * @return the bucket
	 */
	public final String getBucket() {
		return bucket;
	}
	
	public Map<String, String> getPostSignature(String key, long expireSeconds) {
		return this.getPostSignature(key, expireSeconds, false);		
	}
	
	public Map<String, String> getPostSignature(String key, long expireSeconds, boolean isImage) {
		return this.operator.getPostSignature(this.endpoint, this.bucket, key, expireSeconds, isImage);		
	}
	
	public String putObject(String key, InputStream input){
		return this.operator.putObject(this.endpoint, this.bucket, key, input);
	}
	
	public boolean deleteObject(String key){
		return this.operator.deleteObject(this.endpoint, this.bucket, key);
	}
}
