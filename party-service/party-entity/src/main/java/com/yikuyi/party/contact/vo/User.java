package com.yikuyi.party.contact.vo;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModelProperty;

public class User extends AbstractEntity{
	

	private static final long serialVersionUID = -77398925248759250L;

	@ApiModelProperty(value = "ID")
	private String id;

	@ApiModelProperty(value = "用户名称")
    private String name;
    
 	@ApiModelProperty(value = "手机号码")
    private String mobile;

 	@ApiModelProperty(value = "邮箱")
    private String mail;
    
 	@ApiModelProperty(value = "登录名")
    private String loginName;

 	@ApiModelProperty(value = "企业ID")
    private String enterpriseId;

	@ApiModelProperty(value = "企业名字")
    private String enterpriseName;
	
	@ApiModelProperty(value = "密码")
	private String password;
	
	@ApiModelProperty(value = "登录账号")
	private String loginAccount;
	
	
	@ApiModelProperty(value = "用户类型,内部用户还是外部用户")
	private String userType;
	
	
	@ApiModelProperty(value = "是否为企业账号")
	private String isEnterprise;
	



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}


	public String getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(String enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLoginAccount() {
		return loginAccount;
	}

	public void setLoginAccount(String loginAccount) {
		this.loginAccount = loginAccount;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getIsEnterprise() {
		return isEnterprise;
	}

	public void setIsEnterprise(String isEnterprise) {
		this.isEnterprise = isEnterprise;
	}
	
	
	
}