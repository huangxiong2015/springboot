package com.yikuyi.party.user;

import com.ykyframework.model.AbstractEntity;

/**
 * 用户操作vo
 * 
 * @author jik.shu@yikuyi.com
 * @version 1.0.0
 */
public class UserActionVo extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userId;

	private String userName;

	private String loginName;

	private boolean enterpriseCustomer;

	private String enterpriseId;

	private String enterpriseName;

	private ActionType actionType;

	private Action action;

	public boolean isEnterpriseCustomer() {
		return enterpriseCustomer;
	}

	public void setEnterpriseCustomer(boolean enterpriseCustomer) {
		this.enterpriseCustomer = enterpriseCustomer;
	}

	public enum ActionType {
		/**
		 * 商品推荐
		 */
		PRODUCT_RECOMMEND,
		/**
		 * 优惠价推荐
		 */
		COUPON_RECOMMEND;
	}

	public enum Action {
		/**
		 * 注册
		 */
		REGISTER,
		/**
		 * 登录
		 */
		LOGIN;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public ActionType getActionType() {
		return actionType;
	}

	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}

}