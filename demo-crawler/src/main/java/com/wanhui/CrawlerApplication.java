package com.wanhui;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
@EnableScheduling
@SpringBootApplication
public class CrawlerApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(CrawlerApplication.class).web(true).run(args);
	}
}
