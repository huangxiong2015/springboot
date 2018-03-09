/*
 * Created: 2016年10月10日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.config;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import com.framework.springboot.model.LoginUser;

/**
 * 默认的安全策略<br>
 * user token 默认使用 http basic认证策略，token = base64(登录用户名:用户ID:)
 * @author liaoke@yikuyi.com
 * @version 1.0.0
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Value("${spring.profiles.active}")
    private String env;
	
	@Value("${api.party.serverUrlPrefix}")
	private String partyServerUrlPrefix;
	
	@Autowired
	@Qualifier(value="restTemplate")
	private RestTemplate restTemplate;
	
	@Override
	@Bean
    public UserDetailsService userDetailsServiceBean() {
		return (String username) -> {
				try {
					String usernameStr = Base64Utils.encodeToString(username.getBytes());//加密
					logger.info("begin to get userInfo from remote service :" + partyServerUrlPrefix + "/v1/users/validated/" + usernameStr);
					String userId = restTemplate.getForEntity(partyServerUrlPrefix+"/v1/users/validated/"+usernameStr, String.class).getBody();
					return new LoginUser(userId, username, userId, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
				} catch (EmptyResultDataAccessException e) {
					logger.error("Data is empty", e);
					throw new UsernameNotFoundException("User [" + username + "] does not exist.");
				} catch (DataAccessException e) {
					logger.error("Failed to find user [" + username + "].", e);
					throw new UsernameNotFoundException("User [" + username + "] does not exist.");
				}
            };
    }
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		if (!"mock".equals(env)) { //mock环境下不进行权限校验，保证前端测试正常
			//允许所有用户访问swagger-ui相关的网页
			http.authorizeRequests().antMatchers("/favicon.ico", "/swagger-ui.html", "/webjars/**", "/swagger-resources/**", "/v2/api-docs",
					"/v1/products/batch/full","/v2/products/batch/full","/v1/products/batch/price","/v1/products/batch/seckill/price","/v1/products/batch/leadtime","/v1/products/categories/batch","/v1/imports/notification/syncall",
					"/v1/products/fullInfoBrands","/v1/products/brands","/v1/products/brands/*","/v1/products/categories/list","/v1/products/recommendOthers","/v2/products/batch/price","/v2/products/batch/leadtime",
					"/v1/imports/notification/uploadcomplete","/v1/imports/notification/job","/v1/inventory/record/searchresult","/v1/products/properties/sync/*","/v1/inventory","/v2/inventory","/v1/products/nonstandard/clean","/v1/essync/**","/v1/products/keyword/association","/v1/activities/*/products/standard","/v1/activities/*/products/draft","/v1/activities/todayactivity",
					"/v1/activityproducts/effectproduct","/v1/activities/refresh/task","/v1/activityproducts/history","/v1/imports/manufacture/transfer","/v1/activities/todayactivity/**","/v1/activities/listGift","/v1/activities/generateMockData","/v1/document/**","/v1/promotions","/v1/promotions/**/module/draft",
					"/v1/promotions/**/module","/v1/promotionModule/getPromotionDetail/**","/v1/promotions/cache","/v1/promotions/*/module/*/product/**","/v1/promoModuleProductDraft/products/export","/v1/products/brands/cache","/v1/products/categories/cache","/v1/products/categories/children","/v1/products/adviceInvalidProduct","/v1/crawler/detail",
					"/v1/promotions/*","/v1/products/searchRecommond","/v2/products/categories/getParentsByNames/**").permitAll()
			//OPTION方法都不需要认证，解决跨域问题
			.and().authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll()
			//其他的地址均需要认证身份
			.anyRequest().authenticated()//.hasRole("USER")
			.and().httpBasic();
		}
	}
	
}
