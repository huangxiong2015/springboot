package com.ictrade.customer.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import com.ictrade.enterprise.controller.EnterpriseController;
import com.ykyframework.exception.SystemException;

@Controller
@RequestMapping(value = "/enterprise/customers")
public class CustomerController {
	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
	@Value("#{appProps['api.party.serverUrlPrefix']}")
	private String dataPort;
	@RequestMapping(method = RequestMethod.GET)
	public String customerList(ModelMap model){
		return "customer/customerlist";
	}
	
	@RequestMapping(params="action=detail",method = RequestMethod.GET)
	public String customerDetail(@RequestParam(value ="id" , required = true)String id ,ModelMap model){
		model.put("id", id);
		return "customer/customerdetail";
	}
	@RequestMapping(params="action=personUpEnt",method = RequestMethod.GET)
	public String personUpEnterprise(@RequestParam(value ="id" , required = true)String id ,ModelMap model){
		model.put("id", id);
		String userId = UserInfoUtils.getUserInfo().getId();
		model.put("userId", userId);
		return "customer/personUpEnterprise";
	}
	
	
	@RequestMapping(value="/excel",method = RequestMethod.POST)
//	@RequiresPermissions("BUTTON:CLICK:10001")
	@ResponseBody
	public void exportExcel(@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "mail" , required = false)String mail, 
			@RequestParam(value = "name" , required = false)String name, 
			@RequestParam(value = "status" , required = false)String status,
			@RequestParam(value = "registerStart" , required = false)String registerStart, 
			@RequestParam(value = "registerEnd" , required = false)String registerEnd, 
			@RequestParam(value = "lastLoginStart" , required = false)String lastLoginStart, 
			@RequestParam(value = "lastLoginEnd" , required = false)String lastLoginEnd,
			@RequestParam("Authorization") String authorization,
			HttpServletResponse response) throws IOException {
		String url = dataPort+"/v1/customers/excel?a=1";
//		String url = "http://localhost:27082/v1/customers/excel?a=1";
		if(StringUtils.isNotEmpty(phone))
			url = url+"&phone="+phone;
		if(StringUtils.isNotEmpty(mail))
			url = url+"&mail="+mail;
		if(StringUtils.isNotEmpty(name))
			url = url+"&name="+name;
		if(StringUtils.isNotEmpty(status))
			url = url+"&status="+status;
		if(StringUtils.isNotEmpty(registerStart))
			url = url+"&registerStart="+registerStart;
		if(StringUtils.isNotEmpty(registerEnd))
			url = url+"&registerEnd="+registerEnd;
		if(StringUtils.isNotEmpty(lastLoginStart))
			url = url+"&lastLoginStart="+lastLoginStart;
		if(StringUtils.isNotEmpty(lastLoginEnd))
			url = url+"&lastLoginEnd="+lastLoginEnd;

		response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");  
    	response.addHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", "UserList.xls"));  
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
        conn.setConnectTimeout(3*1000);  
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
}
