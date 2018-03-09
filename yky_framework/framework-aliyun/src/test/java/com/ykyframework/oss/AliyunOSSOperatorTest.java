/*
 * Created: 2017年4月11日
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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ykyframework.oss.AliyunOSSOperator.AliyunOSSAccessType;

/**
 * @author liudian@yikuyi.com
 * @version 1.0.0
 */
public class AliyunOSSOperatorTest {
	
	private static final Logger logger = LoggerFactory.getLogger(AliyunOSSOperatorTest.class);

	private static AliyunOSSEndpoint endpoint = AliyunOSSEndpoint.OSS_CN_HANGZHOU;
	
	private static String accessId = "OTRxkGCUJ8Rh8xFu";
	private static String accessKey = "ljykU9VjebI9DrTUoVK9mjTDO8UFwb";
	private static String role = "acs:ram::1058949072375933:role/uat-ossaccess-role";
	
	private static String TEXT_FOR_TEST = "This is a test text with random:";
	
	private static AliyunOSSOperator operator = null;

	/**
	 * @throws java.lang.Exception
	 * @since 2017年4月11日
	 * @author liudian@yikuyi.com
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		operator = new AliyunOSSOperator(new AliyunOSSAccount(accessId, accessKey, role), AliyunOSSAccessType.OSS_ACCESS_PUBLIC);
	}

	/**
	 * @throws java.lang.Exception
	 * @since 2017年4月11日
	 * @author liudian@yikuyi.com
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * Test method for {@link com.ykyframework.oss.AliyunOSSOperator#getPostSignature(com.ykyframework.oss.AliyunOSSEndpoint, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetPostSignatureAliyunOSSEndpointStringString() {
		long random =  RandomUtils.nextLong();		
		String bucket = "ictrade-private-hz-uat";
		String key = "这是测试文件" + " &%" + random;
		Map<String, String> map = operator.getPostSignature(endpoint, bucket, key);
		Assert.assertNotNull(map.get("authorization"));
	}

	/**
	 * Test method for {@link com.ykyframework.oss.AliyunOSSOperator#getPostSignature(com.ykyframework.oss.AliyunOSSEndpoint, java.lang.String, java.lang.String, long)}.
	 */
	@Test
	public void testGetPostSignatureAliyunOSSEndpointStringStringLong() {
		long random =  RandomUtils.nextLong();
		String bucket = "ictrade-private-hz-uat";
		String key = "这是测试文件" + " &%" + random;
		Map<String, String> map = operator.getPostSignature(endpoint, bucket, key, 10);
		Assert.assertNotNull(map.get("authorization"));
	}

	/**
	 * Test method for {@link com.ykyframework.oss.AliyunOSSOperator#getPostSignature(com.ykyframework.oss.AliyunOSSEndpoint, java.lang.String, java.lang.String, long, boolean)}.
	 */
	@Test
	public void testGetPostSignatureAliyunOSSEndpointStringStringLongBoolean() {
		long random =  RandomUtils.nextLong();
		String bucket = "ictrade-private-hz-uat";
		String key = "这是测试文件" + " &%" + random;
		Map<String, String> map = operator.getPostSignature(endpoint, bucket, key, 10, true);
		Assert.assertNotNull(map.get("authorization"));
	}

	/**
	 * Test method for {@link com.ykyframework.oss.AliyunOSSOperator#getPostSignature(com.ykyframework.oss.AliyunOSSEndpoint, java.lang.String, java.lang.String, long, boolean, java.util.List)}.
	 */
	@Test
	public void testGetPostSignatureAliyunOSSEndpointStringStringLongBooleanListOfString() {
		long random =  RandomUtils.nextLong();
		String bucket = "ictrade-private-hz-uat";
		String key = "这是测试文件" + " &%" + random;
		Map<String, String> map = operator.getPostSignature(endpoint, bucket, key, 10, false);
		Assert.assertNotNull(map.get("authorization"));
	}

	/**
	 * Test method for {@link com.ykyframework.oss.AliyunOSSOperator#getAccessUrl(com.ykyframework.oss.AliyunOSSEndpoint, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetAccessUrlAliyunOSSEndpointStringString() {
		long random =  RandomUtils.nextLong();		
		String bucket = "ictrade-public-hz-uat";
		String key = "thisisatestfile" + random;
		String inputStr = TEXT_FOR_TEST + random;
		try {
			String getUrl = this.doPutObject(bucket, key, inputStr);
			logger.info("GetUrl:{}", getUrl);
			String objectUrl = operator.getAccessUrl(endpoint, bucket, key);
			logger.info("ObjectUrl:{}", objectUrl);
			Map<String, String> getMap = AliyunOSSHelper.parseOSSObjectUrl(getUrl);
			Map<String, String> objectMap = AliyunOSSHelper.parseOSSObjectUrl(objectUrl);
			Assert.assertEquals(getMap.get(key), objectMap.get(key));
			Assert.assertEquals(getMap.get("endpoint"), objectMap.get("endpoint"));
			Assert.assertEquals(getMap.get("bucket"), objectMap.get("bucket"));
			String readed = new String(IOUtils.toByteArray(operator.getObject(getUrl)));
			Assert.assertTrue(inputStr.equals(readed));			
			this.doDeleteObject(bucket, key);
		} catch (IOException e) {			
		}		
	}

	/**
	 * Test method for {@link com.ykyframework.oss.AliyunOSSOperator#getAccessUrl(com.ykyframework.oss.AliyunOSSEndpoint, java.lang.String, java.lang.String, boolean)}.
	 */
	@Test
	public void testGetAccessUrlAliyunOSSEndpointStringStringBoolean() {
		long random =  RandomUtils.nextLong();		
		String bucket = "ictrade-public-hz-uat";
		String key = "thisisatestfile" + random;
		String inputStr = TEXT_FOR_TEST + random;
		try {
			String getUrl = this.doPutObject(bucket, key, inputStr);
			logger.info("GetUrl:{}", getUrl);
			String objectUrl = operator.getAccessUrl(endpoint, bucket, key, true);
			logger.info("ObjectUrl:{}", objectUrl);
			Map<String, String> getMap = AliyunOSSHelper.parseOSSObjectUrl(getUrl);
			Map<String, String> objectMap = AliyunOSSHelper.parseOSSObjectUrl(objectUrl);
			Assert.assertEquals(getMap.get(key), objectMap.get(key));
			Assert.assertEquals(getMap.get("endpoint"), objectMap.get("endpoint"));
			Assert.assertEquals(getMap.get("bucket"), objectMap.get("bucket"));
			String readed = new String(IOUtils.toByteArray(operator.getObject(getUrl)));
			Assert.assertTrue(inputStr.equals(readed));			
			this.doDeleteObject(bucket, key);
		} catch (IOException e) {			
		}		
	}

	/**
	 * Test method for {@link com.ykyframework.oss.AliyunOSSOperator#getAccessUrl(com.ykyframework.oss.AliyunOSSEndpoint, java.lang.String, java.lang.String, boolean, long)}.
	 */
	@Test
	public void testGetAccessUrlAliyunOSSEndpointStringStringBooleanLong() {
		long random =  RandomUtils.nextLong();		
		String bucket = "ictrade-public-hz-uat";
		String key = "thisisatestfile" + random;
		String inputStr = TEXT_FOR_TEST + random;
		try {
			String getUrl = this.doPutObject(bucket, key, inputStr);
			logger.info("GetUrl:{}", getUrl);
			String objectUrl = operator.getAccessUrl(endpoint, bucket, key, true, 100);
			logger.info("ObjectUrl:{}", objectUrl);
			Map<String, String> getMap = AliyunOSSHelper.parseOSSObjectUrl(getUrl);
			Map<String, String> objectMap = AliyunOSSHelper.parseOSSObjectUrl(objectUrl);
			Assert.assertEquals(getMap.get(key), objectMap.get(key));
			Assert.assertEquals(getMap.get("endpoint"), objectMap.get("endpoint"));
			Assert.assertEquals(getMap.get("bucket"), objectMap.get("bucket"));
			String readed = new String(IOUtils.toByteArray(operator.getObject(getUrl)));
			Assert.assertTrue(inputStr.equals(readed));			
			this.doDeleteObject(bucket, key);
		} catch (IOException e) {			
		}		
	}

	/**
	 * Test method for {@link com.ykyframework.oss.AliyunOSSOperator#getAccessUrl(com.ykyframework.oss.AliyunOSSEndpoint, java.lang.String, java.lang.String, boolean, long, AliyunOSSDomainMapper)}.
	 */
	@Test
	public void testGetAccessUrlAliyunOSSEndpointStringStringBooleanLongListOfString() {
		long random =  RandomUtils.nextLong();		
		String bucket = "ictrade-public-hz-uat";
		String key = "thisisatestfile" + random;
		String inputStr = TEXT_FOR_TEST + random;
		try {
			String getUrl = this.doPutObject(bucket, key, inputStr);
			logger.info("GetUrl:{}", getUrl);
			String objectUrl = operator.getAccessUrl(endpoint, bucket, key, false, 100);
			logger.info("ObjectUrl:{}", objectUrl);
			String readed = new String(IOUtils.toByteArray(operator.getObject(getUrl)));
			Assert.assertTrue(inputStr.equals(readed));
			this.doDeleteObject(bucket, key);
		} catch (IOException e) {			
		}		
	}

	/**
	 * Test method for {@link com.ykyframework.oss.AliyunOSSOperator#putObject(com.ykyframework.oss.AliyunOSSEndpoint, java.lang.String, java.lang.String, java.io.InputStream)}.
	 */
	@Test
	public void testPutObject() {
		long random =  RandomUtils.nextLong();		
		String bucket = "ictrade-private-hz-uat";
		String key = "这是测试文件" + " &%" + random;
		String inputStr = TEXT_FOR_TEST + random;
		File tempFile = null;
		try {
			String getUrl = this.doPutObject(bucket, key, inputStr);
			logger.info("GetUrl:{}", getUrl);
			String readed = this.doGetObject(bucket, key);
			Assert.assertTrue(inputStr.equals(readed));
			this.doDeleteObject(bucket, key);
		}
		finally {
			FileUtils.deleteQuietly(tempFile);
		}		
	}
	
	private String doGetObject(String bucket, String key) {
		String ret = null;
		InputStream input = null;
		try {
			input = operator.getObject(endpoint, bucket, key);
			ret = new String(IOUtils.toByteArray(input));
		}
		catch(Exception exp) {
		}
		finally {
			IOUtils.closeQuietly(input);
		}
		return ret;
	}
	
	private String doPutObject(String bucket, String key, String inputStr) {
		String url = null;
		InputStream bi = null;
		try {
			bi = IOUtils.toBufferedInputStream(new ByteArrayInputStream(inputStr.getBytes()));
			url = operator.putObject(endpoint, bucket, key, bi);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		finally{
			IOUtils.closeQuietly(bi);
		}
		return url;
	}
	
	private boolean doDeleteObject(String bucket, String key) {
		return operator.deleteObject(endpoint, bucket, key);
	}

	/**
	 * Test method for {@link com.ykyframework.oss.AliyunOSSOperator#fetchObject2File(java.lang.String, java.io.File)}.
	 * @throws IOException 
	 */
	@Test
	public void testFetchObject2FileStringFile() throws IOException {
		long random =  RandomUtils.nextLong();
		
		String bucket = "ictrade-private-hz-uat";
		String key = "这是测试文件" + " &%" + random;
		String inputStr = TEXT_FOR_TEST + random;
		File tempFile = null;
		try {
			String getUrl = this.doPutObject(bucket, key, inputStr);
			logger.info("GetUrl:{}", getUrl);
			String fileName = String.valueOf(RandomUtils.nextLong());
			tempFile = FileUtils.getFile(FileUtils.getTempDirectory(), fileName);
			operator.fetchObject2File(getUrl, tempFile);
			String readed = FileUtils.readFileToString(tempFile, "UTF-8");
			Assert.assertTrue(inputStr.equals(readed));
			this.doDeleteObject(bucket, key);
		}
		finally {
			FileUtils.deleteQuietly(tempFile);
		}
	}

	/**
	 * Test method for {@link com.ykyframework.oss.AliyunOSSOperator#fetchObject2File(com.ykyframework.oss.AliyunOSSEndpoint, java.lang.String, java.lang.String, java.io.File)}.
	 * @throws IOException 
	 */
	@Test
	public void testFetchObject2FileAliyunOSSEndpointStringStringFile() throws IOException {

		long random =  RandomUtils.nextLong();
		
		String bucket = "ictrade-private-hz-uat";
		String key = "这是测试文件" + " &%" + random;
		String inputStr = TEXT_FOR_TEST + random;
		File tempFile = null;
		try {
			String getUrl = this.doPutObject(bucket, key, inputStr);
			logger.info("GetUrl:{}", getUrl);
			String fileName = String.valueOf(RandomUtils.nextLong());
			tempFile = FileUtils.getFile(FileUtils.getTempDirectory(), fileName);
			operator.fetchObject2File(endpoint, bucket, key, tempFile);
			String readed = FileUtils.readFileToString(tempFile, "UTF-8");
			Assert.assertTrue(inputStr.equals(readed));
			this.doDeleteObject(bucket, key);
		}
		finally {
			FileUtils.deleteQuietly(tempFile);
		}
	
	}

	/**
	 * Test method for {@link com.ykyframework.oss.AliyunOSSOperator#getObject(java.lang.String)}.
	 * @throws IOException 
	 */
	@Test
	public void testGetObjectString() throws IOException {

		long random =  RandomUtils.nextLong();
		
		String bucket = "ictrade-private-hz-uat";
		String key = "这是测试文件" + " &%" + random;
		String inputStr = TEXT_FOR_TEST + random;
		InputStream is = null;
		File tempFile = null;
		try {
			String getUrl = this.doPutObject(bucket, key, inputStr);
			logger.info("GetUrl:{}", getUrl);
			is = operator.getObject(getUrl);
			String fileName = String.valueOf(RandomUtils.nextLong());
			tempFile = FileUtils.getFile(FileUtils.getTempDirectory(), fileName);
			FileUtils.copyToFile(is, tempFile);
			String readed = FileUtils.readFileToString(tempFile, "UTF-8");
			Assert.assertTrue(inputStr.equals(readed));
			this.doDeleteObject(bucket, key);
		}
		finally{
			IOUtils.closeQuietly(is);
			FileUtils.deleteQuietly(tempFile);
		}
	}

	/**
	 * Test method for {@link com.ykyframework.oss.AliyunOSSOperator#getObject(com.ykyframework.oss.AliyunOSSEndpoint, java.lang.String, java.lang.String)}.
	 * @throws IOException 
	 */
	@Test
	public void testGetObjectAliyunOSSEndpointStringString() throws IOException {
		long random =  RandomUtils.nextLong();
		
		String bucket = "ictrade-private-hz-uat";
		String key = "这是测试文件" + " &%" + random;
		String inputStr = TEXT_FOR_TEST + random;
		File tempFile = null;
		InputStream input = null;
		try {
			String getUrl = this.doPutObject(bucket, key, inputStr);
			logger.info("GetUrl:{}", getUrl);
			String fileName = String.valueOf(RandomUtils.nextLong());
			tempFile = FileUtils.getFile(FileUtils.getTempDirectory(), fileName);
			input = operator.getObject(endpoint, bucket, key);
			FileUtils.copyInputStreamToFile(input, tempFile);
			String readed = FileUtils.readFileToString(tempFile, "UTF-8");
			Assert.assertTrue(inputStr.equals(readed));
			this.doDeleteObject(bucket, key);
		}
		finally {
			IOUtils.closeQuietly(input);
			FileUtils.deleteQuietly(tempFile);
		}

	}

	/**
	 * Test method for {@link com.ykyframework.oss.AliyunOSSOperator#deleteObject(com.ykyframework.oss.AliyunOSSEndpoint, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testDeleteObject() {

		long random =  RandomUtils.nextLong();
		
		String bucket = "ictrade-private-hz-uat";
		String key = "这是测试文件" + " &%" + random;
		String inputStr = TEXT_FOR_TEST + random;
		File tempFile = null;
		InputStream input = null;
		try {
			String getUrl = this.doPutObject(bucket, key, inputStr);
			logger.info("GetUrl:{}", getUrl);
			boolean deleted = this.doDeleteObject(bucket, key);
			Assert.assertTrue(deleted);
		}
		finally {
			IOUtils.closeQuietly(input);
			FileUtils.deleteQuietly(tempFile);
		}
	}
	


	/**
	 * Test method for {@link com.ykyframework.oss.AliyunOSSOperator#deleteObject(java.lang.String)}.
	 */
	@Test
	public void testDeleteObjectString() {

		long random =  RandomUtils.nextLong();
		
		String bucket = "ictrade-private-hz-uat";
		String key = "thisisatestfile" + random;
		String inputStr = TEXT_FOR_TEST + random;
		File tempFile = null;
		InputStream input = null;
		try {
			String getUrl = this.doPutObject(bucket, key, inputStr);
			logger.info("GetUrl:{}", getUrl);
			boolean deleted = operator.deleteObject(getUrl);
			Assert.assertTrue(deleted);
			String readed = this.doGetObject(bucket, key);
			Assert.assertNull(readed);			
		}
		finally {
			IOUtils.closeQuietly(input);
			FileUtils.deleteQuietly(tempFile);
		}
	}
}
