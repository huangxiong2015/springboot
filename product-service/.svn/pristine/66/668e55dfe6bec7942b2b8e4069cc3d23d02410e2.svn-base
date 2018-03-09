/*
 * Created: 2017年7月3日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.framework.springboot.utils.AuthorizationUtil;
import com.yikuyi.basedata.ShipmentClientBuilder;
import com.yikuyi.info.InfoClientBuilder;
import com.yikuyi.message.MessageClientBuilder;
import com.yikuyi.party.PartyClientBuilder;
import com.yikuyi.pay.PayClientBuilder;
import com.yikuyi.product.ProductClientBuilder;
import com.yikuyi.transaction.TransactionClient;

@Configuration
public class ClientConfiguration {
	@Value("${api.basedata.serverUrlPrefix}")
	private String basedataServerUrlPrefix;	
	
	@Value("${api.party.serverUrlPrefix}")
	private String partyServerUrlPrefix;	
	
	@Value("${api.product.serverUrlPrefix}")
	private String productServerUrlPrefix;	
	
	@Value("${api.pay.serverUrlPrefix}")
	private String payServerUrlPrefix;	
	
	@Value("${api.message.serverUrlPrefix}")
	private String messageServerUrlPrefix;	
	
	@Value("${api.transaction.serverUrlPrefix}")
	private String transactionServerUrlPrefix;	
	
	@Value("${api.info.serverUrlPrefix}")
	private String infoServerUrlPrefix;	
	
	@Bean 
	public AuthorizationUtil initAuthorizationUtil(){
		return new AuthorizationUtil();
	}
	
	@Bean
	public ShipmentClientBuilder shipmentClientBuilderBean(){
		return new ShipmentClientBuilder(basedataServerUrlPrefix);
	}
	
	@Bean
	public PartyClientBuilder partyClientBean(){
		return new PartyClientBuilder(partyServerUrlPrefix);
	}
	
	@Bean
	public ProductClientBuilder productClientBuilderBean(){
		return new ProductClientBuilder(productServerUrlPrefix);
	}
	@Bean
	public PayClientBuilder payClientBuilderBean(){
		return new PayClientBuilder(payServerUrlPrefix);
	}
	@Bean
	public MessageClientBuilder messageClientBuilderBean(){
		return new MessageClientBuilder(messageServerUrlPrefix);
	}
	@Bean
	public TransactionClient transactionClientBean(){
		return new TransactionClient(transactionServerUrlPrefix);
	}
	@Bean
	public InfoClientBuilder infoClientBean(){
		return new InfoClientBuilder(infoServerUrlPrefix);
	}
	
	
}
