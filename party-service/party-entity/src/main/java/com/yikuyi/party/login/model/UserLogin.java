package com.yikuyi.party.login.model;



import java.util.Date;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCrypt;

import com.yikuyi.party.model.Party;
import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author zr.aoxianbing@yikuyi.com
 * @version 1.0.0
 */
@ApiModel("UserLogin")
public class UserLogin extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4506372251333550370L;
	private static final java.util.regex.Pattern BCRYPT_PATTERN = java.util.regex.Pattern.compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}");
	
	@ApiModelProperty(value = "登录ID")
	private String id;
	@ApiModelProperty(value = "当前密码")
	private String currentPassword;
	@ApiModelProperty(value = "用于获取数据库加密后的密码")
	private String bcryptPassword;
	@ApiModelProperty(value = "密码提示")
	private String passwordHint;
	@ApiModelProperty(value = "是否为系统后台账号")
	private String isSystem;
	@ApiModelProperty(value = "是否启用，Y:启用")
	private String enabled;
	@ApiModelProperty(value = "是否强制修改密码，Ｙ：需要")
	private String requirePasswordChange;
	@ApiModelProperty(value = "上一次的语言")
	private String lastLocale;
	@ApiModelProperty(value = "上一次时区")
	private String lastTimeZone;
	@ApiModelProperty(value = "账号禁用日期", example="2016-01-01 08:00:00")
	private Date disabledDateTime;
	@ApiModelProperty(value = "连续登录失败次数")
	private Integer successiveFailedLogins;
	@ApiModelProperty(value = "成功登录次数")
	private Integer succeedLogins;
	@ApiModelProperty(value = "外部授权ＩＤ")
	private String externalAuthId;
	@ApiModelProperty(value = "域认证ＤＮ")
	private String userLdapDn;
	@ApiModelProperty(value = "会员ID")
	private Party party;
	@ApiModelProperty(value = "登录方式枚举：MOBILE，EMAIL，LOGIN_NAME")
	private String userLoginMethod;
	@ApiModelProperty(value = "会员登录目录枚举，PORTAL，OPERATION , PRODUCT_STORE")
	private String userLoginCatalog;
	@ApiModelProperty(value="用户登录历史")
	private List<UserLoginHistory> userLoginHistories;
	@ApiModelProperty(value="密码强度")
	private PwdStrength pwdStrength;
	
	public enum PwdStrength{
		WEAK,MDAIAM,HIGH
	}
	public enum UserLoginMethod{
		MOBILE,EMAIL,LOGIN_NAME
	}
	public enum UserLoginCatalog{
		PORTAL,OPERATION,PRODUCT_STORE
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCurrentPassword() {
		return currentPassword;
	}
	public void setCurrentPassword(String currentPassword) {
		 if (currentPassword == null) {
	        	return;
	        }
	        //如果密码非空，且为原文状态，则使用Bcrypt方式加密
	        if (!BCRYPT_PATTERN.matcher(currentPassword).matches()) {
	        	this.currentPassword = BCrypt.hashpw(currentPassword, BCrypt.gensalt());
	        } else {
	        	this.currentPassword = currentPassword;
	        }
	}
	
	public String getPasswordHint() {
		return passwordHint;
	}
	public void setPasswordHint(String passwordHint) {
		this.passwordHint = passwordHint;
	}
	public String getIsSystem() {
		return isSystem;
	}
	public void setIsSystem(String isSystem) {
		this.isSystem = isSystem;
	}
	public String getEnabled() {
		return enabled;
	}
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	public String getRequirePasswordChange() {
		return requirePasswordChange;
	}
	public void setRequirePasswordChange(String requirePasswordChange) {
		this.requirePasswordChange = requirePasswordChange;
	}
	public String getLastLocale() {
		return lastLocale;
	}
	public void setLastLocale(String lastLocale) {
		this.lastLocale = lastLocale;
	}
	public String getLastTimeZone() {
		return lastTimeZone;
	}
	public void setLastTimeZone(String lastTimeZone) {
		this.lastTimeZone = lastTimeZone;
	}
	public Integer getSuccessiveFailedLogins() {
		return successiveFailedLogins;
	}
	public void setSuccessiveFailedLogins(Integer successiveFailedLogins) {
		this.successiveFailedLogins = successiveFailedLogins;
	}
	public Integer getSucceedLogins() {
		return succeedLogins;
	}
	public void setSucceedLogins(Integer succeedLogins) {
		this.succeedLogins = succeedLogins;
	}
	public String getExternalAuthId() {
		return externalAuthId;
	}
	public void setExternalAuthId(String externalAuthId) {
		this.externalAuthId = externalAuthId;
	}
	public String getUserLdapDn() {
		return userLdapDn;
	}
	public void setUserLdapDn(String userLdapDn) {
		this.userLdapDn = userLdapDn;
	}

	public Date getDisabledDateTime() {
		return disabledDateTime;
	}
	public void setDisabledDateTime(Date disabledDateTime) {
		this.disabledDateTime = disabledDateTime;
	}
	public List<UserLoginHistory> getUserLoginHistories() {
		return userLoginHistories;
	}
	public void setUserLoginHistories(List<UserLoginHistory> userLoginHistories) {
		this.userLoginHistories = userLoginHistories;
	}
	public Party getParty() {
		return party;
	}
	public void setParty(Party party) {
		this.party = party;
	}
	public PwdStrength getPwdStrength() {
		return pwdStrength;
	}
	public void setPwdStrength(PwdStrength pwdStrength) {
		this.pwdStrength = pwdStrength;
	}
	public String getBcryptPassword() {
		return bcryptPassword;
	}
	public void setBcryptPassword(String bcryptPassword) {
		this.bcryptPassword = bcryptPassword;
	}
	public String getUserLoginMethod() {
		return userLoginMethod;
	}
	public void setUserLoginMethod(String userLoginMethod) {
		this.userLoginMethod = userLoginMethod;
	}
	public String getUserLoginCatalog() {
		return userLoginCatalog;
	}
	public void setUserLoginCatalog(String userLoginCatalog) {
		this.userLoginCatalog = userLoginCatalog;
	}
	
	
}
