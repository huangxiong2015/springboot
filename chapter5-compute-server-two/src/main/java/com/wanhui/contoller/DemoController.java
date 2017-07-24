package com.wanhui.contoller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
	@Autowired
	private DiscoveryClient client;
	private static final Logger logger = LoggerFactory.getLogger(DemoController.class);
	
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public Integer add(@RequestParam Integer a,Integer b){
		Integer result  = a + b;
		ServiceInstance instance = client.getLocalServiceInstance();
		logger.info("/add host:"+instance.getHost()+",port:"+instance.getPort()+",instance_id:"+instance.getServiceId()+"--==result:"+result);
		return result;
	}

}
