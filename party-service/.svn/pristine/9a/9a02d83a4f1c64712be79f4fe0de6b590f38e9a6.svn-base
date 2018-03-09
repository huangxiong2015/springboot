package com.yikuyi.party.login.model;

import java.util.Date;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户登录历史记录
 * @author tb.yumu@yikuyi.com
 * @version 1.0.0
 */
@ApiModel("UserLoginHistory")
public class UserLoginHistory extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1119021842876904231L;
	
	@ApiModelProperty(value="访客ID")
	private String visitId;
	
	@ApiModelProperty(value="有效日期")
	private Date fromDate;
	
	@ApiModelProperty(value="失效日期")
	private Date thruDate;
	
	@ApiModelProperty(value="使用的密码")
	private String passwordUsed;
	
	@ApiModelProperty(value="是否成功登录，Y:成功")
	private String successfulLogin;

	public String getVisitId() {
		return visitId;
	}

	public void setVisitId(String visitId) {
		this.visitId = visitId;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getThruDate() {
		return thruDate;
	}

	public void setThruDate(Date thruDate) {
		this.thruDate = thruDate;
	}

	public String getPasswordUsed() {
		return passwordUsed;
	}

	public void setPasswordUsed(String passwordUsed) {
		this.passwordUsed = passwordUsed;
	}

	public String getSuccessfulLogin() {
		return successfulLogin;
	}

	public void setSuccessfulLogin(String successfulLogin) {
		this.successfulLogin = successfulLogin;
	}	
	

}
