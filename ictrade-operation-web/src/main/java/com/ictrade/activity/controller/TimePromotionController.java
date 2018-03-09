package com.ictrade.activity.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ykyframework.exception.SystemException;

@Controller
@RequestMapping("/timepromotion")

public class TimePromotionController {
	private static final Logger logger = LoggerFactory.getLogger(TimePromotionController.class);
	@Value("#{appProps['api.product.serverUrlPrefix']}")
	private String dataPort;
	
	/**
	 * 限时促销-管理列表
	 * @return
	 * @since 2017年7月20日
	 * @author tb.liushuisheng@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:258")
	@RequestMapping(method = RequestMethod.GET)
	public String list(){
		return "timePromotion/timePromotionList";
	}
	
	
	/**
	 * 限时促销-活动信息新增
	 * @return
	 * @since 2017年6月8日
	 * @author tb.zhoulixia@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:258")
	@RequestMapping(value="/information", method = RequestMethod.GET)
	public String information(){
		return "timePromotion/timePromotionInformation";
	}
	
	/**
	 * 限时促销-活动信息编辑
	 * @return
	 * @since 2017年6月8日
	 * @author tb.zhoulixia@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:258")
	@RequestMapping(value="/information/{activityId}", method = RequestMethod.GET)
	public String information(@PathVariable(value="activityId") String activityId,ModelMap model){
		model.addAttribute("activityId",activityId);
		return "timePromotion/timePromotionInformation";
	}

	
	/**
	 * 限时促销-活动上传
	 * @return
	 * @since 2017年7月20日
	 * @author tb.liushuisheng@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:258")
	@RequestMapping(value="/upload/{activityId}", method = RequestMethod.GET)
	public String upload(@PathVariable(value="activityId") String activityId,ModelMap model){
		model.addAttribute("activityId",activityId);
		return "timePromotion/timePromotionUpload";
	}
	/**
	 * 导出活动草稿商品
	 * @param ids
	 * @param activityId
	 * @param periodsId
	 * @param status
	 * @param authorization
	 * @param response
	 * @throws IOException
	 * @since 2017年6月13日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@RequestMapping(value="/export",method = RequestMethod.POST)
	@ResponseBody
	public void exportExcel(@RequestParam(required=false) String ids
            ,@RequestParam(required=true) String activityId
            ,@RequestParam(required=true) String periodsId
            ,@RequestParam(required=false) String status
			,@RequestParam("Authorization") String authorization,
			HttpServletResponse response) throws IOException {
		String url = dataPort+"/v1/activities/"+activityId+ "/export?a=1";
		if(StringUtils.isNotEmpty(ids))
			url = url+"&ids="+ids;
		if(StringUtils.isNotEmpty(activityId))
			url = url+"&activityId="+activityId;
		if(StringUtils.isNotEmpty(periodsId))
			url = url+"&periodsId="+periodsId;
		if(StringUtils.isNotEmpty(status))
			url = url+"&status="+status;
//		logger.info("导出活动草稿商品url："+url);
//		logger.info("导出活动草稿商品Authorization："+authorization);
		response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");  
    	response.addHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", "Products.xls"));  
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
	/**
	 * 限时促销-维护商品
	 * @return
	 * @since 2017年6月8日
	 * @author tb.zhoulixia@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:258")
	@RequestMapping(value="/maintain/{activityId}", method = RequestMethod.GET)
	public String maintain(@PathVariable(value="activityId") String activityId,ModelMap model){
		model.addAttribute("activityId",activityId);
		return "timePromotion/timePromotionMaintain";
	}

	/**
	 * 商品促销-详情页
	 * @return
	 * @since 2017年7月20日
	 * @author tb.liushuisheng@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:258")
	@RequestMapping(value="/detail", method = RequestMethod.GET)
	public String detail(){
		return "timePromotion/timePromotionDetail";
	}

}
