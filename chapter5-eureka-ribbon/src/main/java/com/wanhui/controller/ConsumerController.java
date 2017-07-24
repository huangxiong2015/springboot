package com.wanhui.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ConsumerController {
	@Autowired
	private RestTemplate restTemplate;
	
	private static final Logger logger = LoggerFactory.getLogger(ConsumerController.class);
	
	@RequestMapping(value="/add",method= RequestMethod.GET)
	public String add(){
		logger.info("http://compute-server/add?a=1&b=99");
		return restTemplate.getForEntity("http://compute-server/add?a=1&b=99", String.class).getBody();
	}

}
