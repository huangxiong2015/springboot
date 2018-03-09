/*
 * Created: 2017年3月17日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.audit.bll;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.yikuyi.party.audit.dao.AuditDao;
import com.yikuyi.party.audit.model.Audit;
import com.yikuyi.party.audit.model.AuditLog;
import com.yikuyi.party.audit.repository.AuditRepository;
import com.yikuyi.party.audit.vo.AuditVo;
import com.ykyframework.model.IdGen;



@Service
@Transactional
public class AuditManager {
	
	private static final Logger logger = LoggerFactory.getLogger(AuditManager.class);
	
	@Autowired
	@Qualifier(value="restTemplate")
	private RestTemplate restTemplate;
	
	@Autowired
	private AuditDao auditDao;
	
	@Autowired
	private AuditRepository  auditRepository;
	
	@Value("${api.audit.ipInterface}")
	private String ipInterface;
	

	
	/**
	 * 插入审计日志
	 * @param audit
	 * @since 2017年3月17日
	 * @author guowenyao@yikuyi.com
	 */
	public void insertAduit(Audit audit){
		auditDao.insert(audit);
	}

	/**
	 * 根据审计条件查询审计日志
	 * @param audit
	 * @param rowBounds
	 * @return
	 * @since 2017年3月17日
	 * @author guowenyao@yikuyi.com
	 */
	public PageInfo<AuditVo> getAduitListByEntity(AuditVo audit,RowBounds rowBounds){
		return new PageInfo<>(auditDao.getAuditListByEntity(audit, rowBounds));
	}
	
	
	
	/**
	 * 定时器：将客户信息的IP地址录入到mongodb
	 * @param auditTime
	 * @since 2017年12月11日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public void handleAuditLog(String auditTime,int page,int size){
			RowBounds rowBounds = new RowBounds((page-1)*size, size);
			List<Audit> auditList = null;
			if(StringUtils.isEmpty(auditTime)){//如果时间点为空，则查询系统时间的前一天
				 auditList = auditDao.getLastDayAuditList(rowBounds);
			}else{
				auditList = auditDao.getAuditListByAudDate(auditTime,rowBounds);
			}
			PageInfo<Audit> pageInfo = new PageInfo<>(auditList);
			List<AuditLog> auditLogList = new ArrayList<>();
			if(CollectionUtils.isNotEmpty(auditList)){
				for (Audit audit : auditList) {
					if(audit.getClientIp()!=null){
						try {
							//淘宝的ip查询接口每秒不能超过10次
							//此方法有时间延迟，
							String result = restTemplate.exchange(ipInterface+"?ip="+audit.getClientIp(), HttpMethod.POST, null, new ParameterizedTypeReference<String>(){}).getBody();
			        		if(result!=null){
			        			JSONObject jsonObject = JSONObject.parseObject(result);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
			        			JSONObject data = jsonObject.getJSONObject("data");
			        			AuditLog auditLog = new AuditLog();
			        			auditLog.setId(String.valueOf(IdGen.getInstance().nextId()));
			        			auditLog.setCode(jsonObject.getString("code"));
			        			auditLog.setCountry(data.getString("country"));
			        			auditLog.setCountryId(data.getString("country_id"));
			        			auditLog.setArea(data.getString("area"));
			        			auditLog.setAreaId(data.getString("arae_id"));
			        			auditLog.setRegion(data.getString("region"));
			        			auditLog.setRegionId(data.getString("region_id"));
			        			auditLog.setCity(data.getString("city"));
			        			auditLog.setCityId(data.getString("city_id"));
			        			auditLog.setCounty(data.getString("county"));
			        			auditLog.setCountyId(data.getString("county_id"));
			        			auditLog.setIsp(data.getString("isp"));
			        			auditLog.setIspId(data.getString("isp_id"));
			        			auditLog.setIp(data.getString("ip"));
			        			auditLog.setCreateTime(String.valueOf(System.currentTimeMillis()));
			        			
			        			auditLogList.add(auditLog);
			        		}		
						} catch (Exception e) {
							logger.error("定时器:{},ip:{}", e,audit.getClientIp());
						}
					}
				}
			}
			//批量添加
			auditRepository.save(auditLogList);
			if(pageInfo.getPageNum() != pageInfo.getPages() && pageInfo.getPages()>0){
				handleAuditLog(auditTime,pageInfo.getPageNum()+1,size);
			}
	}
	
	

}
