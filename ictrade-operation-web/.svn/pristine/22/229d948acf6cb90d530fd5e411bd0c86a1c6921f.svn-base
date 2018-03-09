package com.ictrade.activitytemplet.controller;

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
@RequestMapping("/activitytemplet")

public class ActivityTempletController {

	private static final Logger logger = LoggerFactory.getLogger(ActivityTempletController.class);
	@Value("#{appProps['api.product.serverUrlPrefix']}")
	private String dataPort;
	/**
	 * 新蕾活动模板
	 * @return
	 * @since 2017年8月14日
	 * @author tb.liushuisheng@yikuyi.com
	 */
	@RequestMapping(value="/sunrayActive", method = RequestMethod.GET)
	public String list(){
		return "activityTemplet/sunrayActive";
	}

}
