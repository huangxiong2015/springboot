package com.ictrade.bill.controller;

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

import com.ictrade.enterprise.controller.EnterpriseController;
import com.ykyframework.exception.SystemException;

/**
 * 运营账期管理
 *
 */
@Controller
@RequestMapping(value = "/bill")
public class BillController {
	
	private static final Logger logger = LoggerFactory.getLogger(EnterpriseController.class);
	
	@Value("#{appProps['api.party.serverUrlPrefix']}")
	private String dataPort;
	
	/*账期订单列表*/
	//@RequiresPermissions("MENU:VIEW:250")
	@RequestMapping(method = RequestMethod.GET)
	public String customerList(ModelMap model){
		return "bill/paymentdaysList";
	}
	
	/**
	 * 账期订单详情
	 * 2017年8月14日
	 * @author 张伟
	 * 1044867128@qq.com
	 * @param enterpriseId 企业id
	 * @param model
	 * @return
	 */
//	@RequiresPermissions("MENU:VIEW:250")
	@RequestMapping(params="action=detail",method = RequestMethod.GET)
	public String detail(@RequestParam(value = "enterpriseId" , required = true)String enterpriseId,ModelMap model){
		model.addAttribute("enterpriseId", enterpriseId);
		return "bill/paymentDaysDetail";
	}
	
	@RequestMapping(value="/excel",method = RequestMethod.POST)
//	@RequiresPermissions("BUTTON:CLICK:10001")
	@ResponseBody
	public void exportExcel(
			@RequestParam(value = "mail" , required = false)String mail, 
			@RequestParam(value = "name" , required = false)String name, 
			@RequestParam(value = "status" , required = false)String status,	
			@RequestParam("Authorization") String authorization,
			HttpServletResponse response) throws IOException {
//		String url = dataPort+"/v1/enterprises/excel?a=1";
		String url = "http://192.168.1.110:27085/v1/accountperiod/excel";
//		if(mail!=null)
//			url = url+"&mail="+mail;
//		if(name!=null)
//			url = url+"&name="+name;
//		if(status!=null)
//			url = url+"&status="+status;
		response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");  
    	response.addHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", "EnterpriseList.xls"));  
    	response.addHeader("Pragma", "no-cache");  
    	response.addHeader("Expires", "0"); 
		try {
			exportFile(url,response.getOutputStream());
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
			throw new SystemException(e.getMessage(),e);
		}   
	}
	
	private void exportFile(String urlString,OutputStream outputStream) throws IOException{
		urlString = urlString.replaceAll(" ", "%20");
		URL url = new URL(urlString);    
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();    
                //设置超时间为3秒  
        conn.setConnectTimeout(6*1000);  
        //防止屏蔽程序抓取而返回403错误  
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");  
        //conn.setRequestProperty("Authorization", authorization);
  
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
