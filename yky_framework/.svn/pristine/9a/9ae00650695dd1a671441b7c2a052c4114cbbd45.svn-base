package com.framework.springboot.config;

import java.io.IOException;
import java.net.URI;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import com.framework.springboot.model.LoginUser;
import com.framewrok.springboot.web.RequestHelper;



public class  MyClientHttpRequestFactory extends SimpleClientHttpRequestFactory{

	private final Logger logger = LoggerFactory.getLogger(MyClientHttpRequestFactory.class);
	
        	@Override
        	public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
        		ClientHttpRequest request = super.createRequest(uri, httpMethod);        		  

                LoginUser user = RequestHelper.getLoginUser();
                logger.debug(" begin to get userInfo to prepare resttemplate http basic auth  from RequestHelper context.");
                if (null != user) {
	                String userId = user.getId();
	                String username = user.getUsername();
	                String base64LoginName = Base64.getEncoder().encodeToString((username+":"+userId).getBytes());
	                String auth = "Basic " + base64LoginName;
	                request.getHeaders().add("Authorization", auth);
	                logger.info("user info [ userId:"+userId+", userName:"+username+" ]");
	                logger.info("Authorization [" + auth + "]");
                }else{
                	logger.debug("user is null , if remote service request userInfo , call will failed.");
                }
        		return request;
        	}
}