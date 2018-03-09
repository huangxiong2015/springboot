/*
 * Created: 2017年3月27日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
/**
 * 
 */
package com.framework.springboot.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

/**
 * @author liudian@yikuyi.com
 * @version 1.0.0
 */
@Configuration
@EnableConfigurationProperties(DataSourceProperties.class)
@ConditionalOnClass(DruidDataSource.class)
@ConditionalOnProperty(name = "spring.datasource.type", havingValue = "com.alibaba.druid.pool.DruidDataSource")
public class DruidConfig {

	private static final Logger log = LoggerFactory.getLogger(DruidConfig.class);

	@SuppressWarnings("unchecked")
	protected <T> T createDataSource(DataSourceProperties properties, Class<? extends DataSource> type) {
		return (T) properties.initializeDataSourceBuilder().type(type).build();
	}

	/**
	 * A druid DataSource injected
	 * 
	 * @param properties
	 * @return
	 * @since 2017年3月27日
	 * @author liudian@yikuyi.com
	 */
	@Bean(initMethod="init", destroyMethod="close")
	@ConfigurationProperties("spring.datasource")
	public DataSource dataSource(DataSourceProperties properties) {
		return createDataSource(properties, DruidDataSource.class);
	}

	@Bean
	@ConditionalOnProperty(name = "spring.datasource.druid.statwatch", havingValue = "true")
	public ServletRegistrationBean druidServlet() {
		log.info("init Druid Servlet Configuration ");
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
		servletRegistrationBean.setServlet(new StatViewServlet());
		servletRegistrationBean.addUrlMappings("/druid/*");
		Map<String, String> initParameters = new HashMap<>();
		initParameters.put(StatViewServlet.PARAM_NAME_RESET_ENABLE, "false");// 禁用HTML页面上的“Reset All”功能
		initParameters.put(StatViewServlet.PARAM_NAME_ALLOW, ""); // IP白名单 (没有配置或者为空，则允许所有访问)
		initParameters.put(StatViewServlet.PARAM_NAME_DENY, "");// IP黑名单
		// (存在共同时，deny优先于allow)
		servletRegistrationBean.setInitParameters(initParameters);
		return servletRegistrationBean;
	}

	@Bean
	@ConditionalOnProperty(name = "spring.datasource.druid.statwatch", havingValue = "true")
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new WebStatFilter());
		filterRegistrationBean.addUrlPatterns("/*");
		filterRegistrationBean.addInitParameter(WebStatFilter.PARAM_NAME_EXCLUSIONS, "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*,/swagger-ui.html#*");
		return filterRegistrationBean;
	}

}
