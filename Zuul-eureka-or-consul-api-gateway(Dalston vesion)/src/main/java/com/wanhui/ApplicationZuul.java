package com.wanhui;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 *  Zuul
 * @author hx  例如访问  http://localhost:1101/consumer-ribbon/consumer or http://localhost:1101/huangxiong-client/add
 *首先启动服务  chapter6-eureka-consumer-ribbon(Dalston) 和  chapter6-eureka-or-consul-client(Dalston vesion)
 */ 
@EnableZuulProxy
@SpringCloudApplication
public class ApplicationZuul {

	public static void main(String[] args) {
		new SpringApplicationBuilder(ApplicationZuul.class).web(true).run(args);
	}
}
