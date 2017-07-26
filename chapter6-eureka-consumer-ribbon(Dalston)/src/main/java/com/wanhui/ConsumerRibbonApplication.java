package com.wanhui;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * 通过@EnableDiscoveryClient注解来添加发现服务能力。创建RestTemplate实例，并通过@LoadBalanced注解开启均衡负载能力。
 * @author hx
 *
 */
@EnableDiscoveryClient
@SpringBootApplication
public class ConsumerRibbonApplication {

	public static void main(String[] args) {
		 new SpringApplicationBuilder(ConsumerRibbonApplication.class).web(true).run(args);
	}
	
	@LoadBalanced
	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
	
}
