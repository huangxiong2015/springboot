/*
 * Created: 2016年6月1日
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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.oss.common.utils.HttpHeaders;
import com.ykyframework.oss.AliyunOSSHelper.ImageUrls;
import com.ykyframework.oss.AliyunOSSOperator.AliyunOSSAccessType;

/**
 * @author liudian@yikuyi.com
 * @version 1.0.0
 */
public class AliyunOSSHelperTest {
	
	private static final Logger logger = LoggerFactory.getLogger(AliyunOSSHelperTest.class);

	private static AliyunOSSEndpoint endpoint = AliyunOSSEndpoint.OSS_CN_HANGZHOU;
	
	private static String accessId = "OTRxkGCUJ8Rh8xFu";
	private static String accessKey = "ljykU9VjebI9DrTUoVK9mjTDO8UFwb";
	private static String role = "acs:ram::1058949072375933:role/uat-ossaccess-role";	

	private static AliyunOSSAccount account = null;

	/**
	 * @throws java.lang.Exception
	 * @since 2016年6月1日
	 * @author liudian@yikuyi.com
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		account = new AliyunOSSAccount(accessId, accessKey, role);
	}

	/**
	 * @throws java.lang.Exception
	 * @since 2016年6月1日
	 * @author liudian@yikuyi.com
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * Test method for {@link com.ykyframework.oss.AliyunOSSHelper#getPostSignature(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetPostSignature() {
		Map<String, String> result = AliyunOSSHelper.getPostSignature(account, endpoint, "ictrade-private-hz-uat", "a/b/c/asfasd测试啦", 300, false);
		logger.warn("testGetPostSignature:{}", result);
		assertNotNull(result.get("Signature"));
	}
	
	@Test
	public void testGetImgPostSignature() {
		Map<String, String> result = AliyunOSSHelper.getPostSignature(account, endpoint, "ictrade-private-hz-uat", "a/b/c/asfasd.png", 300, true);
		logger.warn("testGetImgPostSignature:{}", result);
		assertNotNull(result.get("Signature"));
	}
	
	@Test
	public void testGetAccessUrl() {
		String result = AliyunOSSHelper.getAccessUrl(account, endpoint, "http", "ictrade-private-hz-uat", "a/b/c/asfasd", false, 120, AliyunOSSAccessType.OSS_ACCESS_PUBLIC);
		logger.warn("testGetAccessUrl:{}", result);
		assertNotNull(result);
	}
	
	@Test
	public void testGetAccessUrl1() {
		String result = AliyunOSSHelper.getAccessUrl(account, endpoint, "http", "ictrade-private-hz-uat", "测试消息.png@100h_100w_0e", true, 120, AliyunOSSAccessType.OSS_ACCESS_PUBLIC);
		logger.warn("testGetAccessUrl1:{}", result);
		assertNotNull(result);
	}
	
	@Test
	public void testGetAccessUrl2() {
		String result = AliyunOSSHelper.getAccessUrl(account, "http://ictrade-private-hz-uat.oss-cn-hangzhou.aliyuncs.com/测试消息.png@100h_100w_0e", true, 30);
		logger.warn("testGetAccessUrl2:{}", result);
		assertNotNull(result);
	}
	
	@Test
	public void testGetPutUrl() {
		String result = AliyunOSSHelper.getPutUrl(account, endpoint, "ictrade-public-hz-uat", "asdfafwaerwer.txt", 1200);
		logger.warn("testGetPutUrl:{}", result);
		assertNotNull(result);
	}
	
	@Test
	public void testGetDeleteUrl() {
		String result = AliyunOSSHelper.getDeleteUrl(account, endpoint, "ictrade-private-hz-uat", "a/b/c/asfasd", new Date(), 20);
		logger.warn("testGetDeleteUrl:{}", result);
		assertNotNull(result);
	}
	
	@Test
	public void testGetDeleteSignature() {
		Map<String, String> result = AliyunOSSHelper.getDeleteSignature(account, endpoint, "ictrade-public-hz-uat", "index.php");
		logger.warn("testGetDeleteSignature:{}", result);
		assertNotNull(result.get(HttpHeaders.AUTHORIZATION));
	}
	
	@Test
	public void testGetImageUrls() {
		List<String> urls = new ArrayList<String>();
		urls.add("http://ictrade-private-hz-uat.oss-cn-hangzhou.aliyuncs.com/a/b/c/asfasd.png");
		urls.add("http://ictrade-private-hz-uat.oss-cn-hangzhou.aliyuncs.com/dev/register/approval/1d/5d4/1d5d4eae493966ce52f823a2256d0533.jpg");
		urls.add("http://ictrade-public-hz-uat.oss-cn-hangzhou.aliyuncs.com/dev/ent/certificates/0c/983/0c983e769bdb9e8d7b903b7f014a65f2.jpg");
		Map<String, ImageUrls> result = AliyunOSSHelper.getImageUrls(account, urls, 300L, AliyunOSSAccessType.OSS_ACCESS_PUBLIC);
		logger.warn("testGetImageUrls:{}", result);
		assertTrue(result.size() > 0);
	}
	
	@Test
	public void testGetImageUrls2() {
		
		ImageUrls url = AliyunOSSHelper.getImageUrl(account, "http://ictrade-private-hz-uat.oss-cn-hangzhou.aliyuncs.com/dev/register/approval/1d/5d4/1d5d4eae493966ce52f823a2256d0533.jpg", 1000, ThumbnailsSuffix.THUMBNAIL_SUFFIX_128);
		
		logger.warn("testGetImageUrls:{}", url.getThumbnail());
		assertNotNull(url.getThumbnail());
	}
	
	private String doPutObject(String bucket, String key, String inputStr) {
		String url = null;
		InputStream bi = null;
		try {
			bi = IOUtils.toBufferedInputStream(new ByteArrayInputStream(inputStr.getBytes()));
			url = AliyunOSSHelper.putObject(account, endpoint, bucket, key, bi);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		finally{
			IOUtils.closeQuietly(bi);
		}
		return url;
	}
	
	private void doDeleteObject(String bucket, String key) {
		AliyunOSSHelper.deleteObject(account, AliyunOSSAccessType.OSS_ACCESS_PUBLIC , endpoint, bucket, key);
	}
	
	@Test
	public void testPutObject() {
		String bucket = "ictrade-public-hz-uat";
		String key = "a/b/c/我是谁 %123" + RandomUtils.nextLong();
		String inputStr = "afj92874052387405723452345234525";
		String url = this.doPutObject(bucket, key, inputStr);
		logger.warn("putObjectUrl:{}", url);
		assertNotNull(url);
		this.doDeleteObject(bucket, key);
	}
	
	@Test
	public void testDeleteObject() {
		String bucket = "ictrade-private-hz-uat";
		String key = String.valueOf(RandomUtils.nextLong()) + System.currentTimeMillis();
		String inputStr = "afj92874052387405723452345234525";
		this.doPutObject(bucket, key, inputStr);
		assertTrue(AliyunOSSHelper.deleteObject(account, AliyunOSSAccessType.OSS_ACCESS_PUBLIC , endpoint, bucket, key));
	}
	
	@Test
	public void testGetObject() throws IOException {
		String inputStr = "afj92874052387405723452345234525";
		ByteArrayInputStream bi = new ByteArrayInputStream(inputStr.getBytes());
		String key = "a/b/c/dfaofuwyerwerwrwr" + RandomUtils.nextLong();
		AliyunOSSHelper.putObject(account, endpoint, "ictrade-public-hz-uat", key, bi);
		InputStream iStream = AliyunOSSHelper.getObject(account, AliyunOSSAccessType.OSS_ACCESS_PUBLIC, endpoint, "ictrade-public-hz-uat", key);
		try{
			assertTrue(inputStr.equals(new String(IOUtils.toByteArray(iStream))));
		}
		finally{
			AliyunOSSHelper.deleteObject(account, AliyunOSSAccessType.OSS_ACCESS_PUBLIC, endpoint, "ictrade-public-hz-uat", key);
		}
		
	}
	
	@Test
	public void testgetAccessUrlAliyunOSSAccountStringLong() {
		String getUrl = "http://ictrade-public-hz-uat.oss-cn-hangzhou.aliyuncs.com/thisisatestfile5039138738327985589";
		String url = AliyunOSSHelper.getAccessUrl(account, getUrl,true, 100);
		logger.info("ImageURL:{}", url);
		Assert.assertTrue(Pattern.matches("http(s{0,1})://img([0|1]{0,1})\\-uat\\.ykystatic\\.com/thisisatestfile5039138738327985589", url));		
		url = AliyunOSSHelper.getAccessUrl(account, getUrl,false, 100);
		logger.info("FileURL:{}", url);
		Assert.assertTrue(Pattern.matches("http(s{0,1})://file([0|1]{0,1})\\-uat\\.ykystatic\\.com/thisisatestfile5039138738327985589", url));
	}
}
