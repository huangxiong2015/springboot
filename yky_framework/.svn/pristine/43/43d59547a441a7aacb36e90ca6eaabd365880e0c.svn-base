/*
 * Created: 2017年4月20日
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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.oss.internal.OSSUtils;

/**
 * @author liudian@yikuyi.com
 * @version 1.0.0
 */
public class AliyunOSSDirectoryTest {
	
	private static final Logger logger = LoggerFactory.getLogger(AliyunOSSDirectoryTest.class);
	
	private static AliyunOSSEndpoint endpoint = AliyunOSSEndpoint.OSS_CN_HANGZHOU;

	private static String accessId = "OTRxkGCUJ8Rh8xFu";
	private static String accessKey = "ljykU9VjebI9DrTUoVK9mjTDO8UFwb";
	private static String role = "acs:ram::1058949072375933:role/uat-ossaccess-role";	
	
	private static AliyunOSSAccount account = new AliyunOSSAccount(accessId, accessKey, role);
	
	private static AliyunOSSBucket publicBucket;
	
	private static AliyunOSSDirectory publicDirectory;
	
	private static AliyunOSSBucket privateBucket;
	
	private static AliyunOSSDirectory privateDirectory;
	
	private static String dir = "test";

	/**
	 * @throws java.lang.Exception
	 * @since 2017年4月20日
	 * @author liudian@yikuyi.com
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		publicBucket = new AliyunOSSBucket(account, endpoint, "ictrade-public-hz-uat");
		publicDirectory = new AliyunOSSDirectory(publicBucket, "dev");

		privateBucket = new AliyunOSSBucket(account, endpoint, "ictrade-private-hz-uat");
		privateDirectory = new AliyunOSSDirectory(privateBucket, "dev");
	}

	/**
	 * @throws java.lang.Exception
	 * @since 2017年4月20日
	 * @author liudian@yikuyi.com
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * Test method for {@link com.ykyframework.oss.AliyunOSSDirectory#getPostSignature(java.lang.String, java.lang.String, com.ykyframework.oss.AliyunOSSHashType, long, boolean)}.
	 */
	@Test
	public void testGetPostSignatureStringStringAliyunOSSHashTypeLongBoolean() {
		long random =  RandomUtils.nextLong();
		String name = "这是测试文件-publicDirectory" + random;
		String expectedUrl = "http://" + publicDirectory.getBucket().getBucket() + "." + endpoint.getUrl() + "/" + publicDirectory.getDirectory() + dir + "/" + name;
		Map<String, String> retMap = publicDirectory.getPostSignature(dir, name, AliyunOSSHashType.FRONT_DISCRETE, 100L, true);
		String url = retMap.get("url");
		logger.info("getUrl:{}", url);	
		Assert.assertEquals("", expectedUrl, url);
		String accessUrl = retMap.get("accessUrl");
		logger.info("accessUrl:{}", accessUrl);		

		Assert.assertTrue(Pattern.matches("http(s{0,1})://img(\\d{0,1})\\-uat\\.ykystatic\\.com" + "/" + publicDirectory.getDirectory() + OSSUtils.makeResourcePath(dir + "/" + name), accessUrl));	
		retMap = publicDirectory.getPostSignature(dir, name, AliyunOSSHashType.FRONT_DISCRETE, 100L, false);
		url = retMap.get("url");
		logger.info("getUrl:{}", url);	
		Assert.assertEquals("", expectedUrl, url);
		accessUrl = retMap.get("accessUrl");
		logger.info("accessUrl:{}", accessUrl);		

		Assert.assertTrue(Pattern.matches("http(s{0,1})://file(\\d{0,1})\\-uat\\.ykystatic\\.com" + "/" + publicDirectory.getDirectory() + OSSUtils.makeResourcePath(dir + "/" + name), accessUrl));
		name = "这是测试文件-publicDirectory" + random + ".bak";
		expectedUrl = "http://" + privateDirectory.getBucket().getBucket() + "." + endpoint.getUrl() + "/" + privateDirectory.getDirectory() + dir + "/" + name;
		retMap = privateDirectory.getPostSignature(dir, name, AliyunOSSHashType.FRONT_DISCRETE, 100L, true);
		url = retMap.get("url");
		logger.info("getUrl:{}", url);	
		Assert.assertEquals("", expectedUrl, url);
		
		expectedUrl = "http://" + privateDirectory.getBucket().getBucket() + "." + endpoint.getUrl() + "/" + privateDirectory.getDirectory() + dir + "/";
		retMap = privateDirectory.getPostSignature(dir, name, AliyunOSSHashType.MD5_DISCRETE, 100L, true);
		url = retMap.get("url");
		logger.info("getUrl:{}", url);	
		Assert.assertTrue(StringUtils.startsWith(url, expectedUrl));
		expectedUrl = "https://" + privateDirectory.getBucket().getBucket() + "." + endpoint.getUrl() + "/" + privateDirectory.getDirectory() + dir + "/";
		accessUrl = retMap.get("accessUrl");
		logger.info("accessUrl:{}", accessUrl);		
		Assert.assertTrue(StringUtils.startsWith(accessUrl, expectedUrl));
	}

	/**
	 * Test method for {@link com.ykyframework.oss.AliyunOSSDirectory#getPostSignature(java.lang.String, java.lang.String, com.ykyframework.oss.AliyunOSSHashType, long)}.
	 */
	@Test
	public void testGetPostSignatureStringStringAliyunOSSHashTypeLong() {
		long random =  RandomUtils.nextLong();
		String name = "这是测试文件-publicDirectory" + random;
		String expectedUrl = "http://" + publicDirectory.getBucket().getBucket() + "." + endpoint.getUrl() + "/" + publicDirectory.getDirectory() + dir + "/" + name;
		Map<String, String> retMap = publicDirectory.getPostSignature(dir, name, AliyunOSSHashType.FRONT_DISCRETE, 100L);
		String url = retMap.get("url");
		logger.info("getUrl:{}", url);	
		Assert.assertEquals("", expectedUrl, url);
	}
	
	private String doPutObject(String bucket, String dir, String name, String inputStr) {
		String url = null;
		InputStream bi = null;
		try {
			bi = IOUtils.toBufferedInputStream(new ByteArrayInputStream(inputStr.getBytes()));
			url = publicDirectory.putObject(dir, name, AliyunOSSHashType.FRONT_DISCRETE, bi);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		finally{
			IOUtils.closeQuietly(bi);
		}
		return url;
	}

	/**
	 * Test method for {@link com.ykyframework.oss.AliyunOSSDirectory#putObject(java.lang.String, java.lang.String, com.ykyframework.oss.AliyunOSSHashType, java.io.InputStream)}.
	 */
	@Test
	public void testPutObject() {
		long random =  RandomUtils.nextLong();		
		String bucket = "ictrade-private-hz-uat";
		String name = "这是测试文件-publicDirectory" + " &%" + random;
		String inputStr = "TEXT_FOR_TEST" + random;
		File tempFile = null;
		InputStream inStream = null;
		try {
			String getUrl = this.doPutObject(bucket, dir, name, inputStr);
			logger.info("getUrl:{}", getUrl);
			String expectedUrl = "http://" + publicDirectory.getBucket().getBucket() + "." + endpoint.getUrl() + "/" + publicDirectory.getDirectory() + AliyunOSSHelper.trimDir(dir) + name;
			Assert.assertEquals("", expectedUrl, getUrl);
			inStream = publicDirectory.getBucket().getOperator().getObject(getUrl);
			String readed = IOUtils.toString(inStream, "UTF-8");
			Assert.assertEquals("", inputStr, readed);
			this.doDeleteObject(bucket, dir, name);
		} catch (IOException e) {
		}
		finally {
			IOUtils.closeQuietly(inStream);
			FileUtils.deleteQuietly(tempFile);
		}		
	}
	
	private boolean doDeleteObject(String bucket, String dir, String name) {
		return publicDirectory.deleteObject(dir, name);
	}
}
