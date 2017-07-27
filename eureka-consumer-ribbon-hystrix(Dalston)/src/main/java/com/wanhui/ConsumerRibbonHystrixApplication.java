package com.wanhui;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * 通过@EnableDiscoveryClient注解来添加发现服务能力。创建RestTemplate实例，并通过@LoadBalanced注解开启均衡负载能力。
 * @author hx
 *
 */
@SpringCloudApplication
public class ConsumerRibbonHystrixApplication {

	public static void main(String[] args) {
		 new SpringApplicationBuilder(ConsumerRibbonHystrixApplication.class).web(true).run(args);
	}
	
	@LoadBalanced
	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
}
