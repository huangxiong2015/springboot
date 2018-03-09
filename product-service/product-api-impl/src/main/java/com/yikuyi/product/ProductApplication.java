package com.yikuyi.product;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import com.ykyframework.web.aop.RestControllerAdvice;

import io.prometheus.client.spring.boot.EnablePrometheusEndpoint;
import io.prometheus.client.spring.boot.EnableSpringBootMetricsCollector;

@SpringBootApplication
@EnablePrometheusEndpoint
@EnableSpringBootMetricsCollector
@EnableAsync
public class ProductApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(ProductApplication.class).web(true).run(args);
	}
	
	@Bean
	public RestControllerAdvice initRestControllerAdvice(){
		return new RestControllerAdvice();
	}
}
