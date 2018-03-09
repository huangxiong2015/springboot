package com.ictrade.common.utils;

import java.io.IOException;
import java.net.URI;
import java.util.Base64;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import com.yikuyi.party.contact.vo.User;


public class MyClientHttpRequestFactory extends SimpleClientHttpRequestFactory{
	
	private static final Logger logger = LoggerFactory.getLogger(MyClientHttpRequestFactory.class);
	
	@Override
	public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
		ClientHttpRequest request = super.createRequest(uri, httpMethod);        		  
        User user = UserInfoUtils.getUserInfo();
        logger.info(" begin to get userInfo to prepare resttemplate http basic auth .");
        if (null != user) {
            String userId = user.getId();
            String username = StringUtils.isEmpty(user.getLoginAccount()) ? (StringUtils.isEmpty (user.getMobile())? user.getMail():user.getMobile()) : user.getLoginAccount();
            String base64LoginName = Base64.getEncoder().encodeToString((username+":"+userId).getBytes());
            String auth = "Basic " + base64LoginName ;     
            logger.info("user info [ userId:"+userId+", userName:"+username+" ]");
            logger.info("Authorization [" + auth + "]");
            request.getHeaders().add("Authorization", auth);
        }else{
        	logger.info("user is null , if service request userInfo , call will failed.");
        }
		return request;
	}
	

}
