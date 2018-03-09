package com.ictrade.web.shiro.realm;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasAuthenticationException;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.cas.CasToken;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.util.StringUtils;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.TicketValidationException;
import org.jasig.cas.client.validation.TicketValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ictrade.common.utils.UserInfoUtils;
import com.yikuyi.party.PartyClientBuilder;
import com.yikuyi.party.contact.vo.User;

/** */

public class AuthorityRealm extends CasRealm  {

	private static final Logger logger = LoggerFactory.getLogger(AuthorityRealm.class);
	
	@Autowired
	private PartyClientBuilder partyClientBuilder;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		User user = (User)principals.getPrimaryPrincipal();
		//初始化用户角色和权限列表
		//to-do: 缓存配置
		logger.info("start to get authorizationInfo,user id:"+user.getId());
        SimpleAuthorizationInfo authorizationInfo =  doGetAuthorizationInfo(user.getId());
        logger.info("end to get authorizationInfo, authorizationInfo:" + authorizationInfo.toString());
        return authorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
	    CasToken casToken = (CasToken) token;
	    if (token == null) {
	        return null;
	    }
	    String ticket = (String)casToken.getCredentials();
	    if (!StringUtils.hasText(ticket)) {
	        return null;
	    }
	    TicketValidator ticketValidator = ensureTicketValidator();
	    try {
	        // contact CAS server to validate service ticket
	        Assertion casAssertion = ticketValidator.validate(ticket, getCasService());
	        // get principal, user id and attributes
	        AttributePrincipal casPrincipal = casAssertion.getPrincipal();
	        String userId = casPrincipal.getName();
	        Map<String, Object> attributes = casPrincipal.getAttributes();
	        // refresh authentication token (user id + remember me)
	        casToken.setUserId(userId);
	        String rememberMeAttributeName = getRememberMeAttributeName();
	        String rememberMeStringValue = (String)attributes.get(rememberMeAttributeName);
	        boolean isRemembered = rememberMeStringValue != null && Boolean.parseBoolean(rememberMeStringValue);
	        if (isRemembered) {
	            casToken.setRememberMe(true);
	        }
	        // create simple authentication info
	        User userInfo = new User();
	        org.apache.commons.beanutils.BeanUtils.populate(userInfo, attributes);  
	        String mail = userInfo.getMail();
	        if(org.apache.commons.lang.StringUtils.isNotBlank(mail)){
	        	userInfo.setName(mail);
	        }
	        if(org.apache.commons.lang.StringUtils.isBlank(mail) && org.apache.commons.lang.StringUtils.isBlank(userInfo.getName()) ){
	          	userInfo.setName(userInfo.getMobile());
	        }
	        String loginName = org.apache.commons.lang.StringUtils.isEmpty( userInfo.getLoginAccount()) ? (org.apache.commons.lang.StringUtils.isEmpty (userInfo.getMobile())? userInfo.getMail():userInfo.getMobile()) : userInfo.getLoginAccount();
            userInfo.setName(loginName);

	        String base64LoginName = Base64.encodeToString((loginName+":"+userInfo.getId()).getBytes());
	        userInfo.setLoginName(base64LoginName);
	        List<Serializable> principals = CollectionUtils.asList(userInfo,userId);
	        PrincipalCollection principalCollection = new SimplePrincipalCollection(principals, getName());
	        return new SimpleAuthenticationInfo(principalCollection, ticket); //shiro-redis 只支持这个构造函数。
	    } catch (TicketValidationException | IllegalAccessException | InvocationTargetException e) { 
	        throw new CasAuthenticationException("Unable to validate ticket [" + ticket + "]", e);
	    }
	}
	
	 /**
     * 根据用户ID获取用户的权限数据
     * @param userId
     * @return
     * @since 2016年1月12日
     * @author tangrong@yikuyi.com
     */
    public SimpleAuthorizationInfo doGetAuthorizationInfo(String userId) {
    	User user = new User();
    	user.setId(userId);
    	SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();    	   	
    	info.setRoles(getUserRoleList(user));
    	info.setStringPermissions(getUserPermissionList(user));
        return info;
    }
	
    /**
     * 获取当前用户所有的角色数据： 1.用户本身被授予的角色   2.企业的共享角色数据
     * @param user
     * @return
     * @since 2016年3月29日
     * @author tangrong@yikuyi.com
     */
    private Set<String> getUserRoleList(User user){
    	logger.info("do get rolelist: user id:"+user.getId());
    	Set<String> roleSet = partyClientBuilder.aclClient().getUserRoles(user.getId(), UserInfoUtils.getLoginAuthorization());
    	logger.info("do get rolelist result: size " + (roleSet!=null ? roleSet.size() : 0 ));
    	return roleSet;
    }
    
    
	/**
	  * 获取当前用户所有权限数据
	  * @param user
	  * @return
	  * @since 2016年3月29日
	  * @author tangrong@yikuyi.com
	  */
	 public Set<String> getUserPermissionList(User user){
		logger.info("do get permissionlist: user id:"+user.getId());
		Set<String> permissionSet = partyClientBuilder.aclClient().getUserPermissions(user.getId(),  UserInfoUtils.getLoginAuthorization());
		logger.info("do get permissionlist result: size " + (permissionSet!=null ? permissionSet.size() : 0 ));
	    return permissionSet;
	 }
	 
}