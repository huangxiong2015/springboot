/*
 * Created: 2016年11月28日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.statistics.bll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yikuyi.party.statistics.vo.StatisticsVo.StatisticsType;
import com.yikuyi.party.userLogin.dao.UserLoginDao;

@Service
@Transactional
public class StatisticsManager {

	@Autowired
	private UserLoginDao userLoginDao;

	public Long getStatisticsNumByType(StatisticsType type) {
		Long count;
		if (StatisticsType.REG_NUM.equals(type)) {
			count = userLoginDao.getRegNumToday();
		} else {
			count = userLoginDao.getLoginNumToday();
		}
		return count;
	}
}
