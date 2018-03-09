/*
 * Created: 2018年2月11日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2018 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.user;

import com.yikuyi.transaction.carts.vo.CartsVo;

public class RecommendCartsVo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userId;

	private String loginName;

	private CartsVo cartsVo;

	public CartsVo getCartsVo() {
		return cartsVo;
	}

	public void setCartsVo(CartsVo cartsVo) {
		this.cartsVo = cartsVo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

}
