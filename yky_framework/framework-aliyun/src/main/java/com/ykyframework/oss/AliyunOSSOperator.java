/*
 * Created: 2017年4月6日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
/**
 * 
 */
package com.ykyframework.oss;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.io.FileUtils;

/**
 * 主要针对内部业务功能使用，考虑到部署环境可能在vpc内部时可以使用内部网络带宽而减少外网的网络吞吐。
 * @author liudian@yikuyi.com
 * @version 1.0.0
 */
public class AliyunOSSOperator {	
			
	private AliyunOSSAccount account;
	
	public enum AliyunOSSAccessType {		
		OSS_ACCESS_VPC,
		OSS_ACCESS_INTERNAL,
		OSS_ACCESS_PUBLIC		
	}
	
	private AliyunOSSAccessType clientAccessType = AliyunOSSAccessType.OSS_ACCESS_PUBLIC;
	
	public AliyunOSSOperator(AliyunOSSAccount account, AliyunOSSAccessType accessType) {
		this.setAccount(account);
		this.clientAccessType = accessType;
	}

	/**
	 * @return the clientAccessType
	 */
	public final AliyunOSSAccessType getClientAccessType() {
		return clientAccessType;
	}

	/**
	 * @return the account
	 */
	public final AliyunOSSAccount getAccount() {
		return account;
	}

	/**
	 * @param account the account to set
	 */
	public final void setAccount(AliyunOSSAccount account) {
		this.account = account;
	}

	public Map<String, String> getPostSignature(AliyunOSSEndpoint endpoint, String bucket, String key) {
		long expireSeconds = AliyunOSSHelper.EXPIRE_SECONDS;
		return getPostSignature(endpoint, bucket, key, expireSeconds, false);
	}

	/**
	 * Generate the policy & signature for uploading file to aliOSS bucket.
	 * @param bucket
	 *            The bucket to upload
	 * @param dir
	 *            the directory that the file to upload
	 * @param name
	 *            the key to upload/access the file
	 * @param expireSeconds
	 *            expire time in seconds for the post request
	 * @param charsetName
	 * 
	 * @return A map to return the necessary informations, contains { accessid:
	 *         the accesskeyid for the OSS access account, policy: the policy of
	 *         the post request, signature: signature of the post reqeust, host:
	 *         the host post to, expire: expire time in TimeMillis type, key:
	 *         the full access key to the object, expiration: the expire time in
	 *         Rfc822 type }
	 * @since 2016年6月1日
	 * @author liudian@yikuyi.com
	 */
	public Map<String, String> getPostSignature(AliyunOSSEndpoint endpoint,String bucket, String key, long expireSeconds) {
		return getPostSignature(endpoint, bucket, key, expireSeconds, false);
	}

	/**
	 * Generate the policy & signature for uploading file to aliOSS bucket.
	 * @param bucket
	 *            The bucket to upload
	 * @param expireSeconds
	 *            expire time in seconds for the post request
	 * @param charsetName
	 * @param isImage whether a image or not
	 * @param dir
	 *            the directory that the file to upload
	 * @param name
	 *            the key to upload/access the file
	 * @return A map to return the necessary informations, contains { accessid:
	 *         the accesskeyid for the OSS access account, policy: the policy of
	 *         the post request, signature: signature of the post reqeust, host:
	 *         the host post to, expire: expire time in TimeMillis type, key:
	 *         the full access key to the object, expiration: the expire time in
	 *         Rfc822 type }
	 * @since 2016年6月1日
	 * @author liudian@yikuyi.com
	 */
	public Map<String, String> getPostSignature(AliyunOSSEndpoint endpoint, String bucket, String key, long expireSeconds, boolean isImage) {
		return AliyunOSSHelper.getPostSignature(account, endpoint, bucket, key, expireSeconds, isImage);
	}

	public String getAccessUrl(AliyunOSSEndpoint endpoint, String bucket, String key) {
		return getAccessUrl(endpoint, bucket, key, false);
	}

	public String getAccessUrl(AliyunOSSEndpoint endpoint, String bucket, String key, boolean isImage) {
		long expireSeconds = 300;
		return getAccessUrl(endpoint, bucket, key, isImage, expireSeconds);
	}

	/**
	 * Generate the access url for a aliOSS Object, Not for purpose of external use.
	 * @param bucket
	 *            bucket to access
	 * @param dir
	 *            the directory that the file to access
	 * @param name
	 *            access key to the file
	 * @param expireSeconds
	 *            expire time in seconds
	 * 
	 * @return
	 * @since 2016年6月1日
	 * @author liudian@yikuyi.com
	 */
	public String getAccessUrl(AliyunOSSEndpoint endpoint, final String bucket, String key, boolean isImage, long expireSeconds) {
		return AliyunOSSHelper.getAccessUrl(account, endpoint, "https", bucket, key, isImage, expireSeconds, clientAccessType);
	}
	
	/**
	 * 根据url获取aliyunOSS的访问url，包括签名等。不作为外部使用的目的，即在系统运行环境之外（如：把url暴露给web访问的用户等情况）可能无法进行正常访问。
	 * 因为可能因为vpc环境下为了节省外部网络接口带宽而使用了内部域名访问。
	 * 如果需要将url暴露给web环境中用户通过浏览器访问，请使用AliyunOSSHelper相关方法。
	 * @param getUrl
	 * @param isImage
	 * @param expireSeconds
	 * @return
	 * @since 2017年10月10日
	 * @author liudian@yikuyi.com
	 */
	public String getAccessUrl(String getUrl, boolean isImage, long expireSeconds) {
		Map<String, String> hostMap = AliyunOSSHelper.parseOSSObjectUrl(getUrl);
		String key = hostMap.get(AliyunOSSHelper.KEY_OBJECT);
		String bucket = hostMap.get(AliyunOSSHelper.KEY_BUCKET);
		AliyunOSSEndpoint endpoint = AliyunOSSEndpoint.valueOf(hostMap.get(AliyunOSSHelper.KEY_ENDPOINT));
		return this.getAccessUrl(endpoint, bucket, key, isImage, expireSeconds);
	}
	
	public String putObject(AliyunOSSEndpoint endpoint, final String bucket, String key, InputStream input) {
		return AliyunOSSHelper.putObject(account, endpoint, bucket, key, input);
	}
	
	/**
	 * Fetch a object from aliyun oss to a file
	 * @param account the account to access aliyun oss
	 * @param objectUrl the object access url to aliyun
	 * @param file2Store the file to store the object
	 * @throws IOException any ioexception to get object
	 * @since 2016年11月11日
	 * @author liudian@yikuyi.com
	 */
	public void fetchObject2File(String objectUrl, File file2Store) throws IOException {
		Map<String, String> hostMap = AliyunOSSHelper.parseOSSObjectUrl(objectUrl);
		String key = hostMap.get(AliyunOSSHelper.KEY_OBJECT);
		String bucket = hostMap.get(AliyunOSSHelper.KEY_BUCKET);
		AliyunOSSEndpoint endpoint = AliyunOSSEndpoint.valueOf(hostMap.get(AliyunOSSHelper.KEY_ENDPOINT));
		fetchObject2File(endpoint, bucket, key, file2Store);
	}
	
	/**
	 * Fetch a object from aliyun oss to a file
	 * 
	 * @param account
	 *            the account to access aliyun oss
	 * @param endpoint
	 *            the endpoint that bucket store on
	 * @param bucket
	 *            the bucket name of aliyun oss
	 * @param key
	 *            the object key of object(includes the directory and object
	 *            name)
	 * @param file2Store
	 *            the file to store the object
	 * @throws IOException
	 *             any ioexception to get object
	 * @since 2016年11月11日
	 * @author liudian@yikuyi.com
	 */
	public void fetchObject2File(AliyunOSSEndpoint endpoint, final String bucket, String key, File file2Store) throws IOException {
		InputStream inputStream = getObject(endpoint, bucket, key);
		FileUtils.copyInputStreamToFile(inputStream, file2Store);
	}
	
	/**
	 * Get a object as inputstream from aliyun oss
	 * @param account the account to access aliyun oss
	 * @param objectUrl the object access url to aliyun
	 * @return the inputstream fetch from aliyun oss
	 * @since 2016年11月10日
	 * @author liudian@yikuyi.com
	 */
	public InputStream getObject(String objectUrl) {
		Map<String, String> hostMap = AliyunOSSHelper.parseOSSObjectUrl(objectUrl);
		String key = hostMap.get(AliyunOSSHelper.KEY_OBJECT);
		String bucket = hostMap.get(AliyunOSSHelper.KEY_BUCKET);
		AliyunOSSEndpoint endpoint = AliyunOSSEndpoint.valueOf(hostMap.get(AliyunOSSHelper.KEY_ENDPOINT));
		return getObject(endpoint, bucket, key);
	}
	
	/**
	 * Get a object as inputstream from aliyun oss
	 * @param account the account to access aliyun oss
	 * @param endpoint the endpoint that bucket store on
	 * @param bucket the bucket name of aliyun oss
	 * @param objectKey the object key of object(includes the directory and object name)
	 * @return the inputstream fetch from aliyun oss
	 * @since 2016年11月10日
	 * @author liudian@yikuyi.com
	 */
	public InputStream getObject(AliyunOSSEndpoint endpoint, final String bucket, String objectKey) {
		return AliyunOSSHelper.getObject(account, clientAccessType, endpoint, bucket, objectKey);
	}
	
	/**
	 * 从aliyunOSS的bucket中删除指定key对应的对象
	 * @param account 访问aliyun的账户信息
	 * @param endpoint aliyunOSS的服务节点
	 * @param bucket 对象所在的bucket名
	 * @param objectKey 要删除的对象的key
	 * @return true表示成功删除，false表示出现异常情况
	 * @since 2016年9月22日
	 * @author liudian@yikuyi.com
	 */
	public boolean deleteObject(AliyunOSSEndpoint endpoint, final String bucket, String objectKey){
		return AliyunOSSHelper.deleteObject(account, clientAccessType, endpoint, bucket, objectKey);
	}
	
	/**
	 * 
	 * @param objectUrl 对象的访问路径，不带签名，未进行urlencode
	 * @return
	 * @since 2017年4月18日
	 * @author liudian@yikuyi.com
	 */
	public boolean deleteObject(final String objectUrl){
		Map<String, String> hostMap = AliyunOSSHelper.parseOSSObjectUrl(objectUrl);
		String key = hostMap.get(AliyunOSSHelper.KEY_OBJECT);
		String bucket = hostMap.get(AliyunOSSHelper.KEY_BUCKET);
		AliyunOSSEndpoint endpoint = AliyunOSSEndpoint.valueOf(hostMap.get(AliyunOSSHelper.KEY_ENDPOINT));
		return AliyunOSSHelper.deleteObject(account, clientAccessType, endpoint, bucket, key);
	}
}
