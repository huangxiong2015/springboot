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
package com.yikuyi.party.config;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.framework.springboot.model.LoginUser;
import com.yikuyi.party.user.bll.UserManager;

/**
 * 默认的安全策略<br>
 * user token 默认使用 http basic认证策略，token = base64(登录用户名:用户ID:)
 * 
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

	@Autowired
	private UserManager userManager;

	@Override
	@Bean
	public UserDetailsService userDetailsServiceBean() {
		return (String username) -> {
			try {
				String userId = userManager.getUserByAccount(username);
				return new LoginUser(userId, username, userId, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
			} catch (EmptyResultDataAccessException e) {
				logger.error("Data is empty:{}", e);
				throw new UsernameNotFoundException("User [" + username + "] does not exist.");
			} catch (DataAccessException e) {
				logger.error("Failed to find user :{}, username :{}", e, username);
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
		if (!"mock".equals(env)) { // mock环境下不进行权限校验，保证前端测试正常
			// 允许所有用户访问swagger-ui相关的网页
			http.authorizeRequests()
					.antMatchers("/v1/facility/**", "/favicon.ico", "/swagger-ui.html", "/webjars/**", "/swagger-resources/**", "/v2/api-docs", "/v1/users/validated/**", "/v1/partyGroup", "/v1/account/**", "/v1/customer/**",
							"/v1/findpassword/**", "/v1/party/allparty", "/v1/enterprises/expired", "/v1/customers/**/username", "/v1/customers/**/login", "/v1/enterprises/baseInfo", "/v1/customersummery/**/info",
							"/v1/enterprises/accountPeriodAudit", "/v1/vendors/getParentRelationInfo/**", "/v1/vendorManage/apply/**", "/v1/party/partygroups", "/v1/vendorManage/listVendorManage", 
							"/v1/vendors/ids","/v1/vendors/supplierLeadTimejob","/v1/audit/ip/annal","/v1/customeSync/allEntList","/v1/customeSync/allPersonalList","/v1/customeSync/getIncrementEntList",
							"/v1/customeSync/getIncrementPersonalList","/v1/customersummery/**/customersInfo","/v1/supplier/**","/v1/dept/findPersonByRoleName")
					.permitAll()
					// OPTION方法都不需要认证，解决跨域问题
					.and().authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll()
					// 其他的地址均需要认证身份
					.anyRequest().authenticated()// .hasRole("USER")
					.and().httpBasic();
		}
	}
}