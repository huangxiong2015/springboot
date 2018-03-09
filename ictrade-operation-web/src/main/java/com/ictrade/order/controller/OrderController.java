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
package com.ictrade.order.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/order")
public class OrderController {
	/**
	 * 到订单列表页面
	 * @return
	 * @since 2016年12月5日
	 * @author zr.helinmei@yikuyi.com
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list(){
		return "order/orderList";
	}
	
	/**
	 * 到订单详情页面
	 * @return
	 * @since 2016年12月5日
	 * @author zr.helinmei@yikuyi.com
	 */
	@RequestMapping(params="action=detail",method = RequestMethod.GET)
	public String detail(){
		return "order/orderDetails";
	}
}
