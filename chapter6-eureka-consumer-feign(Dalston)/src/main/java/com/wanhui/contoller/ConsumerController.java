package com.wanhui.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {
	@Autowired
	private ConsumerClient consumerClient;
	
	@GetMapping("/consumer")
	public String add(){
		return consumerClient.add();
	}
	
}
