package com.yikuyi.party.user.bll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.yikuyi.party.contact.vo.User;
import com.yikuyi.party.login.model.UserLogin;
import com.yikuyi.party.userLogin.dao.UserLoginDao;
import com.ykyframework.exception.SystemException;

@Service
@Transactional
public class UserManager {
	private static final Logger logger = LoggerFactory.getLogger(UserManager.class);
	
	@Autowired
	private CacheManager cacheManager;
	
	@Autowired
	private UserLoginDao userLoginDao;
	
	/**
	 * 根据用户名称获取用户id
	 * @param username
	 * @return
	 */
	public String getUserByAccount(String username){ 
		Cache cache = null;
		String userId = null;
		try {
			cache = cacheManager.getCache("usernameCache");
			userId = cache.get("username_"+username,String.class);
		} catch (Exception e) {
			logger.error("根据用户名称获取用户Id:{}",e);
			cache = null;
			userId = null;
		}
        if(!StringUtils.isEmpty(userId)){
        	return userId;
        }
		logger.info("用户登录检验开始..........begin..........,username:{}",username);
		 try {
			UserLogin userLogin = userLoginDao.findEntityById(username);
			userId = null == userLogin ? null : userLogin.getParty().getId();
			if(StringUtils.isEmpty(userId)){
				logger.error("Failed to find user username: {}",username);
				throw new SystemException("User [" + username + "] does not exist.");
			}
			if(null != cache){
				cache.put("username_"+username, userId);
			}
		} catch (Exception e) {
			throw new UsernameNotFoundException(e.getMessage(),e);
		}
		logger.info("用户登录检验开始..........ned..........,username:{},userid:{}",username,userId);
		return userId;
	}
	
	/**
	 * 根据用户id获取企业信息
	 * @param username
	 * @return
	 */
	public User getUserInfo(String userId){
		User user = userLoginDao.findUserInfo(userId);
		if(user == null){
			logger.error("Failed to find enterpriseInfo userId:{}",userId);
		}
		return user;
	}
}