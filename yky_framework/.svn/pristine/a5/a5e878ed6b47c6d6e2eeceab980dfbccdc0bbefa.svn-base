/*
 * Created: 2016年3月12日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ykyframework.sender;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;

import com.ykyframework.exception.ExceptionResponse;

/**
 * 广州百信短信发送接口
 * 
 * @author liaoke@yikuyi.com
 * @version 1.0.0
 */
public class GzbxSmsSender implements SmsSender {
	private final static Logger logger = LoggerFactory.getLogger(GzbxSmsSender.class);
	
	/**
	 * 短信发送URL地址
	 */
	private String url;
	/**
	 * 短信网关发送ID
	 */
	private String userId;
	/**
	 * 短信网关发送账号
	 */
	private String account;
	/**
	 * 短信网关发送密码
	 */
	private String password;
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String genVerifyCode() {
		Random r = new Random();
		int i = r.nextInt(1000000);
		return String.format("%06d", i);
	}

	@Override
	public boolean send(String mobile, String content) {
		if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(content)) {
			logger.warn("the mobile or content is empty");
			return false;
		}
		
		InputStream in = null;
		String targetUrl = null;
		try {
			StringBuilder buff = new StringBuilder();
			buff.append(url);
			buff.append("mobile=" + mobile);
			buff.append("&content=" + URLEncoder.encode(content, "utf-8"));
			buff.append("&userid=" + userId);
			buff.append("&account=" + account);
			buff.append("&password=" + password);
			buff.append("&action=send&checkcontent=1");
			targetUrl = buff.toString();
		
			URL realUrl = new URL(targetUrl);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.setConnectTimeout(5000);  
			connection.setReadTimeout(5000);
			logger.debug("the sms send url is: {}", targetUrl);
			// 建立实际的连接
			connection.connect();
			
			in = connection.getInputStream();
			String respXml = IOUtils.toString(in);
			logger.debug("the sms send url is: {}, the response is\n {}", targetUrl, respXml);
			//解析
			ExceptionResponse resp = this.parse(respXml);
			return resp.isSuccess();
		} catch (UnsupportedEncodingException e) {
			logger.error("fail to send sms, the url is {}", targetUrl, e);
			return false;
		} catch (IOException e) {
			logger.error("fail to send sms, the url is {}", targetUrl, e);
			return false;
		} finally {
			IOUtils.closeQuietly(in);
		}
		

	}
	
	/**
	 * 解析短信网关返回的XML，判断是否发送成功
	 * @param respXml
	 * @return
	 * @since 2016年3月12日
	 * @author liaoke@yikuyi.com
	 */
	private ExceptionResponse parse(final String respXml) {
		//返回码形如
//		<?xml version="1.0" encoding="utf-8" ?>
//		<returnsms>
//		<returnstatus>status</returnstatus>-------返回状态值：成功返回Success失败返回：Faild
//		<message>message</message>--------------返回信息提示：见下表
//		< checkCounts > </ checkCounts >--------------预留字段
//		</returnsms>
		
		
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
			DocumentBuilder builder = dbf.newDocumentBuilder();  
			Document doc = builder.parse(new ByteArrayInputStream(respXml.getBytes("utf-8")));  
			
			// 生成XPath对象
			XPath xpath = XPathFactory.newInstance().newXPath();
			
			//xpath教程参看http://w3school.com.cn/xpath/
			String returnstatus = (String) xpath.evaluate("/returnsms/returnstatus[1]", doc, XPathConstants.STRING);
			
			if ("Success".equals(returnstatus)) {
				return ExceptionResponse.OK;
			} else {
				String errorMsg = (String) xpath.evaluate("/returnsms/message[1]", doc, XPathConstants.STRING);
				return ExceptionResponse.create(returnstatus, errorMsg);
			}
		} catch (XPathExpressionException e) {
			logger.error("failed to parse xml with xpath, the xml is {}", respXml, e);
		} catch (Exception e) {
			logger.error("failed to parse xml {}", respXml, e);
		}
		return ExceptionResponse.ERROR;
        

	}
}
