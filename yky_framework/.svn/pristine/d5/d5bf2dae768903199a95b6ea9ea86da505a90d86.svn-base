/*
 * Created: 2016年9月15日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ykyframework.cache;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountDAOImpl implements AccountDAO {
	private static final Logger logger = LoggerFactory.getLogger(AccountDAO.class);
	
	private Map<Integer, Account> accountMap = new HashMap<Integer, Account>();
	
	public AccountDAOImpl() {
		//初始化
		Account acc = new Account();
		acc.setId(1);
		acc.setName("mock name");
		accountMap.put(acc.getId(), acc);
	}
	
	@Override
	public Account getAccountById(Integer id) {
		logger.info("get account from DB, the query ID is [{}]", id);
		return accountMap.get(id);
	}


	@Override
	public Account saveAccount(Account account) {
		logger.info("save account to DB, account info is [{}]", account);
		accountMap.put(account.getId(), account);
		return account;
	}


	@Override
	public void deleteAccountById(Integer id) {
		logger.info("delete account to DB, the id is [{}]", id);
		accountMap.remove(id);
	}
	
	
}
