/*
 * Created: 2017年1月25日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

@Component
public class FtpAccountConfig {

	private static final Logger logger = LoggerFactory.getLogger(FtpAccountConfig.class);
	
	/**
	 * 导入库存上传文件路径
	 */
	@Value("${leadMaterialFilePath}")
	private String leadMaterialFilePath;
	
	/**
	 * ftp下载富昌vendorId
	 */
	@Value("${downloadFtp.vendorId.future}")
	private String future;
	
	/**
	 * ftp下载中电港vendorId
	 */
	@Value("${downloadFtp.vendorId.cecport}")
	private String cecport;
	
	/**
	 * ftp下载WPGvendorId
	 */
	@Value("${downloadFtp.vendorId.wpg}")
	private String wpg;
	
	/**
	 * ftp下载willasvendorId
	 */
	@Value("${downloadFtp.vendorId.willas}")
	private String willas;
	
	/**
	 * ftp下载rs的vendorId
	 */
	@Value("${downloadFtp.vendorId.rs}")
	private String rs;
	
	
	private static Properties props;

	private FtpAccountConfig() {
		try {
			Resource resource = new ClassPathResource("/config/ftp-account.properties");
			props = PropertiesLoaderUtils.loadProperties(resource);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public String downloadFtpFile(String vendorId, String doc,String ftpFileName){
		String[] wpgArr = wpg.split(",");
		if(future.equals(vendorId)){
			return downloadFutureFtpFile(doc);
		}
		if(cecport.equals(vendorId)){
			return downloadCecportFtpFile(doc);
		}
		if(Arrays.asList(wpgArr).contains(vendorId)){
			return downloadWpgFtpFile(doc,ftpFileName);
		}
		if(willas.equals(vendorId)){
			return downloadWillasFtpFile(doc);
		}
		if(rs.equals(vendorId)){
			return downloadRsFtpFile(doc);
		} 
		return null;
	}
	
	/**
	 * 下载future存储在FTP的ZIP文件(步骤1)
	 * 
	 * @since 2017年1月25日
	 * @author zr.shuzuo@yikuyi.com
	 */
	private String downloadFutureFtpFile(String doc) {
		String fileName = leadMaterialFilePath + File.separator + doc + ".zip";
		FTPClient ftpClient = new FTPClient();
		try (FileOutputStream fos = new FileOutputStream(fileName)) {
			ftpClient.connect(props.getProperty("future.ip"));
			ftpClient.login(props.getProperty("future.name"), props.getProperty("future.pswd"));
			ftpClient.setBufferSize(1024);
			ftpClient.enterLocalPassiveMode();//设置本地模式下载
			// 设置文件类型（二进制）
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			String remoteFileName = "future.zip";
			ftpClient.retrieveFile(remoteFileName, fos);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return fileName;
	}
	
	/**
	 * 下载future存储在FTP的csv文件(步骤1)
	 * 
	 * @since 2017年1月25日
	 * @author zr.shuzuo@yikuyi.com
	 */
	@SuppressWarnings("unused")
	private String downloadFutureFtpCsv(String doc) {
		String fileName = leadMaterialFilePath + File.separator + doc + ".csv";
		FTPClient ftpClient = new FTPClient();
		try (FileOutputStream fos = new FileOutputStream(fileName)) {
			ftpClient.connect(props.getProperty("future.newIp"));
			ftpClient.login(props.getProperty("future.newName"), props.getProperty("future.newPswd"));
			ftpClient.setBufferSize(1024);
			ftpClient.enterLocalPassiveMode();//设置本地模式下载
			// 设置文件类型（二进制）
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			String remoteFileName = "future.csv";
			ftpClient.retrieveFile(remoteFileName, fos);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return fileName;
	}
	
	/**
	 * 下载中电港存储在FTP的csv文件(步骤1)
	 * 
	 * @since 2017年1月25日
	 * @author zr.shuzuo@yikuyi.com
	 */
	private String downloadCecportFtpFile(String doc) {
		String fileName = leadMaterialFilePath + File.separator + doc + ".csv";
		FTPClient ftpClient = new FTPClient();
		try (FileOutputStream fos = new FileOutputStream(fileName)) {
			ftpClient.connect(props.getProperty("cecport.ip"), Integer.parseInt(props.getProperty("cecport.port")));
			ftpClient.login(props.getProperty("cecport.name"), props.getProperty("cecport.pswd"));
			ftpClient.setBufferSize(1024);
			ftpClient.enterLocalPassiveMode();//设置本地模式下载
			// 设置文件类型（二进制）
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			String remoteFileName = "cecport"+new SimpleDateFormat("yyyyMMdd").format(new Date())+".csv";
			ftpClient.retrieveFile(remoteFileName, fos);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return fileName;
	}
	
	
	private String downloadWpgFtpFile(String doc,String ftpFileName) {
		String fileName = leadMaterialFilePath + File.separator + doc + ".csv";
		FTPClient ftpClient = new FTPClient();
		try (FileOutputStream fos = new FileOutputStream(fileName)) {
			ftpClient.connect(props.getProperty("wpg.ip"), Integer.parseInt(props.getProperty("wpg.port")));
			ftpClient.login(props.getProperty("wpg.name"), props.getProperty("wpg.pswd"));
			ftpClient.setBufferSize(1024);
			ftpClient.enterLocalPassiveMode();//设置本地模式下载
			// 设置文件类型（二进制）
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			String remoteFileName = ftpFileName;
			ftpClient.retrieveFile(remoteFileName, fos);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return fileName;
	}
	
	private String downloadWillasFtpFile(String doc) {
		String fileName = leadMaterialFilePath + File.separator + doc + ".csv";
		FTPClient ftpClient = new FTPClient();
		try (FileOutputStream fos = new FileOutputStream(fileName)) {
			ftpClient.connect(props.getProperty("willas.ip"));
			ftpClient.login(props.getProperty("willas.name"), props.getProperty("willas.pswd"));
			ftpClient.setBufferSize(1024);
			ftpClient.enterLocalPassiveMode();//设置本地模式下载
			// 设置文件类型（二进制）
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			FTPFile[] files = ftpClient.listFiles();
			String remoteFileName = files[0].getName();;
			ftpClient.retrieveFile(remoteFileName, fos);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return fileName;
	}
	
	private String downloadRsFtpFile(String doc) {
		String fileName = leadMaterialFilePath + File.separator + doc + ".csv";
		FTPClient ftpClient = new FTPClient();
		try (FileOutputStream fos = new FileOutputStream(fileName)) {
			ftpClient.connect(props.getProperty("rs.ip"), Integer.parseInt(props.getProperty("rs.port")));
			ftpClient.login(props.getProperty("rs.name"), props.getProperty("rs.pswd"));
			ftpClient.changeWorkingDirectory(props.getProperty("rs.pathName"));
			ftpClient.setBufferSize(1024);
			ftpClient.enterLocalPassiveMode();//设置本地模式下载
			// 设置文件类型（二进制）
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			String remoteFileName = "RS_YIKUYI Catalog_OnStock.csv";
			ftpClient.retrieveFile(remoteFileName, fos);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return fileName;
	}
}