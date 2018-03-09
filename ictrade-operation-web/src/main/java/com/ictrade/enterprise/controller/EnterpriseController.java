/*
 * Created: 2016年12月5日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ictrade.enterprise.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ictrade.common.utils.UserInfoUtils;
import com.ykyframework.exception.SystemException;

@Controller
@RequestMapping("/enterprise")
public class EnterpriseController {
	private static final Logger logger = LoggerFactory.getLogger(EnterpriseController.class);
	@Value("#{appProps['api.party.serverUrlPrefix']}")
	private String dataPort;
	/**
	 * 到企业列表
	 * @return
	 * @since 2017年2月15日
	 * @author gongtianyu@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:226")
	@RequestMapping(method = RequestMethod.GET)
	public String list(){
		return "enterprise/firmAccountList";
	}
	
	/**
	 * 到企业详情
	 * @return
	 * @since 2017年2月15日
	 * @author gongtianyu@yikuyi.com
	 */
	@RequestMapping(params="action=detail",method = RequestMethod.GET)
	public String detail2(@RequestParam(value ="id" , required = true)String id ,ModelMap model){
		model.put("id", id);
		String userId = UserInfoUtils.getUserInfo().getId();
		model.put("userId", userId);
		return "enterprise/firmAccountDetail";
	}
	
	@RequestMapping(value="/excel",method = RequestMethod.POST)
//	@RequiresPermissions("BUTTON:CLICK:10001")
	@ResponseBody
	public void exportExcel(
			@RequestParam(value = "mail" , required = false)String mail, 
			@RequestParam(value = "name" , required = false)String name, 
			@RequestParam(value = "status" , required = false)String status,
			@RequestParam(value = "verifyStatus" , required = false)String verifyStatus, 
			@RequestParam(value = "registerStart" , required = false)String registerStart, 
			@RequestParam(value = "registerEnd" , required = false)String registerEnd, 
			@RequestParam(value = "lastLoginStart" , required = false)String lastLoginStart, 
			@RequestParam(value = "lastLoginEnd" , required = false)String lastLoginEnd,
			@RequestParam("Authorization") String authorization,
			HttpServletResponse response) throws IOException {
		String url = dataPort+"/v1/enterprises/excel?a=1";
//		String url = "http://localhost:27082/v1/enterprises/excel?a=1";
		if(mail!=null)
			url = url+"&mail="+mail;
		if(name!=null)
			url = url+"&name="+name;
		if(status!=null)
			url = url+"&status="+status;
		if(verifyStatus!=null)
			url = url+"&verifyStatus="+verifyStatus;
		if(registerStart!=null)
			url = url+"&registerStart="+registerStart;
		if(registerEnd!=null)
			url = url+"&registerEnd="+registerEnd;
		if(lastLoginStart!=null)
			url = url+"&lastLoginStart="+lastLoginStart;
		if(lastLoginEnd!=null)
			url = url+"&lastLoginEnd="+lastLoginEnd;

		response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");  
    	response.addHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", "EnterpriseList.xls"));  
    	response.addHeader("Pragma", "no-cache");  
    	response.addHeader("Expires", "0"); 
		try {
			exportFile(url,authorization,response.getOutputStream());
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
			throw new SystemException(e.getMessage(),e);
		}   
	}
	
	private void exportFile(String urlString,String authorization,OutputStream outputStream) throws IOException{
		urlString = urlString.replaceAll(" ", "%20");
		URL url = new URL(urlString);    
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();    
                //设置超时间为3秒  
        conn.setConnectTimeout(6*1000);  
        //防止屏蔽程序抓取而返回403错误  
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");  
        conn.setRequestProperty("Authorization", authorization);
  
        //得到输入流  
        InputStream inputStream = conn.getInputStream();    
        //转流
        readInputStream(inputStream,outputStream);  
	}
	
    private void readInputStream(InputStream inputStream,OutputStream outputStream) throws IOException {    
        byte[] buffer = new byte[1024];    
        int len ;    
        while((len = inputStream.read(buffer)) != -1) {    
        	outputStream.write(buffer, 0, len);    
        }
        inputStream.close();  
        outputStream.flush();  
        outputStream.close();
    } 
    
    
	/**
	 * 到企业管理列表
	 * @return
	 * @since 2017年5月18日
	 * @author zr.helinmei@yikuyi.com
	 */
    @RequestMapping(params="action=certificationEnt",method = RequestMethod.GET)
	public String cerEnterprise(){
		return "enterprise/cerEnterpriseList";
	}
    
    /**
	 * 到认证企业详情
	 * @return
	 * @since 2017年2月15日
	 * @author gongtianyu@yikuyi.com
	 */
	@RequestMapping(params="action=cerEntDetail",method = RequestMethod.GET)
	public String cerEntDetail(@RequestParam(value ="id" , required = true)String id ,ModelMap model){
		model.put("id", id);
		String userId = UserInfoUtils.getUserInfo().getId();
		model.put("userId", userId);
		return "enterprise/cerEnterpriseDetail";
	}
	
	/**
	 * 到企业编辑
	 * @return
	 * @since 2017年5月8日
	 * @author zr.helinmei@yikuyi.com
	 */
	@RequestMapping(params="action=entEdit",method = RequestMethod.GET)
	public String entEdit(@RequestParam(value ="id" , required = true)String id ,ModelMap model){
		model.put("id", id);
		String userId = UserInfoUtils.getUserInfo().getId();
		model.put("userId", userId);
		model.put("userName", UserInfoUtils.getUserInfo().getName());
		return "enterprise/enterpriseEdit";
	}

	@RequestMapping(params="action=test",method = RequestMethod.GET)
	public String test(){
		return "enterprise/test";
	}
	
	
	/**
	 * 到认证企业编辑
	 * @return
	 * @since 2017年2月15日
	 * @author gongtianyu@yikuyi.com
	 */
	@RequestMapping(params="action=cerEntEdit",method = RequestMethod.GET)
	public String cerEntEdit(@RequestParam(value ="id" , required = true)String id ,ModelMap model){
		model.put("id", id);
		String userId = UserInfoUtils.getUserInfo().getId();
		model.put("userId", userId);
		return "enterprise/cerEnterpriseEdit";
	}
	
	/**
	 * 到新增页面
	 * @return
	 * @since 2017年5月8日
	 * @author zr.helinmei@yikuyi.com
	 */
	@RequestMapping(params="action=entAdd",method = RequestMethod.GET)
	public String entAdd(ModelMap model){
		String userId = UserInfoUtils.getUserInfo().getId();
		model.put("userId", userId);
		return "enterprise/enterpriseEdit";
	}
}
