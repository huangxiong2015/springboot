package com.yikuyi.party;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.framework.springboot.utils.AuthorizationUtil;
import com.yikuyi.basedata.ShipmentClientBuilder;
import com.yikuyi.info.InfoClientBuilder;
import com.yikuyi.message.MessageClientBuilder;
import com.yikuyi.party.integration.PartyIntegrationClientBuilder;
import com.yikuyi.pay.PayClientBuilder;
import com.yikuyi.product.ProductClientBuilder;
import com.yikuyi.transaction.TransactionClient;
import com.yikuyi.workflow.WorkflowClientBuilder;
import com.ykyframework.web.aop.RestControllerAdvice;

@SpringBootApplication
public class PartyApplication {

	public static void main(String[] args) {
		SpringApplication.run(PartyApplication.class, args);
	}

	@Bean
	public RestControllerAdvice initRestControllerAdvice() {
		return new RestControllerAdvice();
	}

	@Bean
	public AuthorizationUtil initAuthorizationUtil() {
		return new AuthorizationUtil();
	}

	@Bean
	public WorkflowClientBuilder initWorkflowClient(@Value("${api.workflow.serverUrlPrefix}") String workflowUrlPrefix) {
		return new WorkflowClientBuilder(workflowUrlPrefix);
	}

	@Bean
	public ProductClientBuilder initProductClient(@Value("${api.product.serverUrlPrefix}") String productUrlPrefix) {
		return new ProductClientBuilder(productUrlPrefix);
	}

	@Bean
	public ShipmentClientBuilder initBasedataClient(@Value("${api.basedata.serverUrlPrefix}") String basedataUrlPrefix) {
		return new ShipmentClientBuilder(basedataUrlPrefix);
	}

	@Bean
	public TransactionClient initTransactionClient(@Value("${api.transaction.serverUrlPrefix}") String transactionUrlPrefix) {
		return new TransactionClient(transactionUrlPrefix);
	}

	@Bean
	public PayClientBuilder initPayClientBuilder(@Value("${api.pay.serverUrlPrefix}") String payUrlPrefix) {
		return new PayClientBuilder(payUrlPrefix);
	}

	@Bean
	public MessageClientBuilder initMessageClientBuilder(@Value("${api.message.serverUrlPrefix}") String messageUrlPrefix) {
		return new MessageClientBuilder(messageUrlPrefix);
	}

	@Bean
	public InfoClientBuilder initInfoClientBuilder(@Value("${api.info.serverUrlPrefix}") String infoUrlPrefix) {
		return new InfoClientBuilder(infoUrlPrefix);
	}
	
	@Bean
	public PartyIntegrationClientBuilder partyIntegrationClientBuilder(@Value("${api.integration.serverUrlPrefix}") String integrationUrlPrefix) {
		return new PartyIntegrationClientBuilder(integrationUrlPrefix);
	}
	
}