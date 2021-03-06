package com.wanhui;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 *  提供服务
 * @author hx
 *
 */
@EnableDiscoveryClient
@SpringBootApplication
public class ApplicationCompute {

	public static void main(String[] args) {
		new SpringApplicationBuilder(ApplicationCompute.class).web(true).run(args);
	}
}
