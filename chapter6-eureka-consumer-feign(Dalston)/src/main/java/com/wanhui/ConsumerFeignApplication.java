package com.wanhui;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * 通过@EnableDiscoveryClient注解来添加发现服务能力。
 * 由于Feign是基于Ribbon实现的，所以它自带了客户端负载均衡功能
 * @author hx
 *
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class ConsumerFeignApplication {

	public static void main(String[] args) {
		 new SpringApplicationBuilder(ConsumerFeignApplication.class).web(true).run(args);
	}
}
