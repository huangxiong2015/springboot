package com.yikuyi.party.resource;

import java.util.List;

import com.yikuyi.party.person.model.Person;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * 权限相关的接口调用定义
 * 
 * @author jik.shu@yikuyi.com
 */
@Headers({ "Content-Type: application/json;charset=UTF-8" })
public interface DeptClient {
	
	/**
	 * 根据角色名称找用户信息list   redis监控用
	 * @param roleName
	 * @return
	 * @since 2017年6月15日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	@RequestLine("GET /v1/dept/findPersonByRoleName/{roleName}")
	public List<Person> findPersonByRoleName(@Param("roleName") String roleName);
}