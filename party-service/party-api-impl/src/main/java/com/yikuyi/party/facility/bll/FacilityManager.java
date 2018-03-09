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
package com.yikuyi.party.facility.bll;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yikuyi.party.facility.dao.FacilityDao;
import com.yikuyi.party.facility.model.Facility;
import com.ykyframework.model.IdGen;

@Service
@Transactional
public class FacilityManager {
	@Autowired
	private FacilityDao facilityDao;
	@Resource(name = "redisTemplate")
	private HashOperations<String, String, String> aliasFacilityOps;


	/**
	 * 根据partId获取供应商列表
	 * @param addressType
	 * @return
	 * @since 2016年12月12日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public List<Facility> getFacilityList(String id) {
		return facilityDao.getFacilityList(id);
	}
	
	/**
	 * 获取仓库为Y的信息
	 * @param addressType
	 * @return
	 * @since 2016年12月12日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public List<Facility> getFacilityFlagYList(String id) {
		return facilityDao.getFacilityFlagYList(id);
	}
	
	
	
	/**
	 * 根据Id获取仓库列表
	 * @param ids
	 * @return
	 * @since 2016年12月17日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public List<Facility> getFacility(List<String> ids) {
		return facilityDao.getFacility(ids);
	}
	
	
	/**
	 * 新增仓库
	 * @param facility
	 * @return
	 * @since 2017年2月7日
	 * @author tb.chenxuemin@yikuyi.com
	 */
	public Facility addFacility(Facility facility) {
		facility.setId(String.valueOf(IdGen.getInstance().nextId()));
		facility.setFacilityNameAlia(facility.getFacilityName());
		facility.setFromDate(new Date());
		facilityDao.add(facility);
		aliasFacilityOps.getOperations().delete("materialFacilityCache");
		return facility;
	}
	
            

	/**
	 * 删除仓库
	 * @param menu
	 * @since 2017年2月7日
	 * @author tb.chenxuemin@yikuyi.com
	 */
	public void delete(Facility facility) {
		facilityDao.delete(facility);
	}
	
	/**
	 * 修改仓库信息
	 * @param menu
	 * @since 2017年2月7日
	 * @author tb.chenxuemin@yikuyi.com
	 */
	public void update(Facility facility) {
		facilityDao.update(facility);
	}

}
