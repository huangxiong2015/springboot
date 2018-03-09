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
@RequestMapping("/shoppromotion")

public class ShopPromotionController {
	private static final Logger logger = LoggerFactory.getLogger(ShopPromotionController.class);
	@Value("#{appProps['api.product.serverUrlPrefix']}")
	private String dataPort;
	
	/**
	 * 商品促销-管理列表
	 * @return
	 * @since 2017年7月20日
	 * @author tb.liushuisheng@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:257")
	@RequestMapping(method = RequestMethod.GET)
	public String list(){
		return "shopPromotion/shopPromotionList";
	}
	
	/**
	 * 商品促销-活动信息新增
	 * @return
	 * @since 2017年7月20日
	 * @author tb.liushuisheng@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:257")
	@RequestMapping(value="/information", method = RequestMethod.GET)
	public String information(){
		return "shopPromotion/shopPromotionInformation";
	}
	
	/**
	 * 商品促销-活动信息编辑
	 * @return
	 * @since 2017年7月20日
	 * @author tb.liushuisheng@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:257")
	@RequestMapping(value="/information/{activityId}", method = RequestMethod.GET)
	public String information(@PathVariable(value="activityId") String activityId,ModelMap model){
		model.addAttribute("activityId",activityId);
		return "shopPromotion/shopPromotionInformation";
	}
	
	/**
	 * 商品促销-活动上传
	 * @return
	 * @since 2017年7月20日
	 * @author tb.liushuisheng@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:257")
	@RequestMapping(value="/upload/{activityId}", method = RequestMethod.GET)
	public String upload(@PathVariable(value="activityId") String activityId,ModelMap model){
		model.addAttribute("activityId",activityId);
		return "shopPromotion/shopPromotionUpload";
	}
	
	/**
	 * 商品促销-维护商品
	 * @return
	 * @since 2017年7月20日
	 * @author tb.liushuisheng@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:257")
	@RequestMapping(value="/maintain/{activityId}", method = RequestMethod.GET)
	public String maintain(@PathVariable(value="activityId") String activityId,ModelMap model){
		model.addAttribute("activityId",activityId);
		return "shopPromotion/shopPromotionMaintain";
	}
	
	/**
	 * 商品促销-详情页
	 * @return
	 * @since 2017年7月20日
	 * @author tb.liushuisheng@yikuyi.com
	 */
	@RequiresPermissions("MENU:VIEW:257")
	@RequestMapping(value="/detail", method = RequestMethod.GET)
	public String detail(){
		return "shopPromotion/shopPromotionDetail";
	}

}
