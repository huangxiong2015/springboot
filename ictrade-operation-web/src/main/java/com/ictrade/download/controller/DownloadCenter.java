/*
 * Created: 2017年9月21日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ictrade.download.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/download")
public class DownloadCenter {
	/**
	 * 我的导出下载列表
	 * @return
	 * @since 2017年9月21日
	 * @author    chenhong@yikuyi.com
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list(){
		return "downloadCenter/mydownload";
	}
	

}
