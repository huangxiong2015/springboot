package com.wanhui.contoller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
	@Autowired
	private DiscoveryClient client;
	private static final Logger logger = LoggerFactory.getLogger(DemoController.class);
	
//	@RequestMapping(value="/add",method=RequestMethod.GET)
	@GetMapping(value="/add")
	public String add(HttpServletRequest request){
		String  service = "Services:"+client.getServices();
		logger.info(service);
		return service;
	}

}
