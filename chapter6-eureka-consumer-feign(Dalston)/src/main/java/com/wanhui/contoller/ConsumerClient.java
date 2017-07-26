package com.wanhui.contoller;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("huangxiong-client")//调用服务名
public interface ConsumerClient {
	
	@GetMapping("add")
	public String add();

}
