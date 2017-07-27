package com.wanhui.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
public class ConsumerController {
	
	@Autowired
	private ConsumerService consumerService;
	
	@GetMapping("/consumer")
	public String add(){
		return consumerService.consumer();
	}
	
	@Service
	public class ConsumerService {
		@Autowired
		private RestTemplate restTemplate;
		
		@HystrixCommand(fallbackMethod="fallBack")
		public String consumer(){
			return restTemplate.getForEntity("http://huangxiong-client/add", String.class).getBody();
		}
		
		public String fallBack(){
			return "fallback";
		}
		
	}
	
}
