package com.ictrade.category.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ykyframework.exception.SystemException;

@Controller
@RequestMapping("/category")
public class CategoryController {

	@Value("#{appProps['api.product.serverUrlPrefix']}")
	private String productPort;
	/**
	 * category列表入口
	 * @return
	 * @since 2017年06月06日
	 * @author zr.xieyuanpeng@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:987655444434")
	@RequestMapping(method = RequestMethod.GET)
	public String list(){
		return "systemMaintain/categoryList";
	}
	/**
	 * 新增/编辑category入口
	 * @return
	 * @since 2017年06月06日
	 * @author zr.xieyuanpeng@yikuyi.com
	 */
	@RequestMapping(value="/edit", method = RequestMethod.GET)
	public String editCategory(){
		return "systemMaintain/editCategory";
	}
	
	/**
	 * 分类导出url
	 * @param fileUrl
	 * @param authorization
	 * @param response
	 * @since 2017年11月14日
	 * @author tongkun@yikuyi.com
	 */
	@RequestMapping(value="/excel/down",method = RequestMethod.GET)
	@ResponseBody
	public void exportCategoryExcel(@RequestParam("Authorization") String authorization,HttpServletResponse response){
		response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");  
    	response.addHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", "all-category.xls"));  
    	response.addHeader("Pragma", "no-cache");  
    	response.addHeader("Expires", "0"); 
		try {
			URL url = new URL(productPort+"/v1/products/categories/export");    
	        HttpURLConnection conn = (HttpURLConnection)url.openConnection();    
	                //设置超时间为1200秒  
	        conn.setConnectTimeout(1200*1000);  
	        //防止屏蔽程序抓取而返回403错误  
	        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");  
	        conn.setRequestProperty("Authorization", authorization);
	  
	        //得到输入流  
	        InputStream inputStream = conn.getInputStream();    
	        //转流
	        readInputStream(inputStream,response.getOutputStream());  
		} catch (IOException e) {
			throw new SystemException(e.getMessage(),e);
		}    
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
