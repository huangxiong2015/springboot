/*
 * Created: 2016年9月6日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package org.jasig.cas.client.session;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Repository;

/**
 * CAS 客户端的 ST TICKET和session关系的持久化类，同时处理spring session对应的session的注销。
 * @author liaoke@yikuyi.com
 * @version 1.0.0
 */
@Repository
public class RedisBackedSessionMappingStorage implements SessionMappingStorage {
	private static final Logger logger = LoggerFactory.getLogger(RedisBackedSessionMappingStorage.class);
	
    /**
     * cas mapping ID和session ID的映射key前缀
     */
    private static final String CAS_MAPPING_SESSION_PREFIX = "session:cas_mapping:";
	
    @Autowired
	private RedisTemplate<String, Object> redisTemplate;
    
    @Autowired(required=false)
    private SessionRepository<?> redisOperationsSessionRepository;
	
	/**
	 * session映射关系缓存超期时间。设置为1小时，超过正常的session-config的最大值30分钟
	 */
	private int expireInSeconds = 3600;
	
	

	@Override
	public HttpSession removeSessionByMappingId(String mappingId) {
		// 从mappingId得到sessionId
		// 根据sessionId得到redis session，并进行删除
		
        final String sessionId = (String) redisTemplate.opsForValue().get(CAS_MAPPING_SESSION_PREFIX + mappingId);
        if (sessionId != null) {
        	removeBySessionById(sessionId);
        	if (redisOperationsSessionRepository != null) {
        		redisOperationsSessionRepository.delete(sessionId); // [IMPORTANT! 注销session]
        	}
        }
        redisTemplate.delete(CAS_MAPPING_SESSION_PREFIX + mappingId);
        return null;
	}

	@Override
	public void removeBySessionById(String sessionId) {
		if (logger.isDebugEnabled()) {
			logger.debug("Attempting to remove Session=[" + sessionId + "]");
		}
		
		//DO NOTHING 主要靠removeSessionByMappingId来注销session。因为在创建session的时候，会先删除session，导致spring session生成的东西被删除
        
	}

	@Override
	public void addSessionById(String mappingId, HttpSession session) {
		logger.debug("Attempting to add Session, session ID =[{}], mappingId = [{}].", session.getId(), mappingId);
		redisTemplate.opsForValue().set(CAS_MAPPING_SESSION_PREFIX + mappingId, session.getId(), getExpireInSeconds(), TimeUnit.SECONDS);
		
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public int getExpireInSeconds() {
		return expireInSeconds;
	}

	public void setExpireInSeconds(int expireInSeconds) {
		this.expireInSeconds = expireInSeconds;
	}

	public void setRedisOperationsSessionRepository(SessionRepository<?> redisOperationsSessionRepository) {
		this.redisOperationsSessionRepository = redisOperationsSessionRepository;
	}



}
