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
package com.ictrade.material.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ykyframework.exception.SystemException;

@Controller
@RequestMapping("/material")
public class MaterailController {
	
	@Value("#{appProps['api.product.serverUrlPrefix']}")
	private String productPort;
	
	/**
	 * 物料检测,文件上传页面
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String detail(){
		return "material/materialDetection";
	}
	
	
	@RequestMapping(value="/excel/down",method = RequestMethod.POST)
	@ResponseBody
	public void exportQuotationExcel(@RequestParam("fileUrl") String fileUrl,@RequestParam("Authorization") String authorization,HttpServletResponse response){
		response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");  
    	response.addHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", "materialDetection.xls"));  
    	response.addHeader("Pragma", "no-cache");  
    	response.addHeader("Expires", "0"); 
		try {
			exportFile(productPort+"/v1/imports/materialDetection?fileUrl="+fileUrl,authorization,response.getOutputStream());
		} catch (IOException e) {
			throw new SystemException(e.getMessage(),e);
		}    
	}
	
	/**
	 * 导出文件
	 * @param urlString
	 * @param authorization
	 * @param outputStream
	 * @throws IOException
	 * @since 2016年11月19日
	 * @author tongkun@yikuyi.com
	 */
	private void exportFile(String urlString,String authorization,OutputStream outputStream) throws IOException{
		URL url = new URL(urlString);    
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();    
                //设置超时间为1200秒  
        conn.setConnectTimeout(1200*1000);  
        //防止屏蔽程序抓取而返回403错误  
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");  
        conn.setRequestProperty("Authorization", authorization);
  
        //得到输入流  
        InputStream inputStream = conn.getInputStream();    
        //转流
        readInputStream(inputStream,outputStream);  
	}
	

    /** 
     * 将输入流的东西输出到输出流中
     * @param inputStream 
     * @return 
     * @throws IOException 
     */  
    private void readInputStream(InputStream inputStream,OutputStream outputStream) throws IOException {    
        byte[] buffer = new byte[1024];    
        int len;    
        while((len = inputStream.read(buffer)) != -1) {    
        	outputStream.write(buffer, 0, len);    
        }    
    }  
	
}
