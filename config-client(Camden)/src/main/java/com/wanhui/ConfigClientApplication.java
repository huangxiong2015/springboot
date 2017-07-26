package com.wanhui;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * 
 * 
 * @author hx
 *
 */
@SpringBootApplication
public class ConfigClientApplication {

	public static void main(String[] args) {
		 new SpringApplicationBuilder(ConfigClientApplication.class).web(true).run(args);
	}
}
