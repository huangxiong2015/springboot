package com.yikuyi.party.facility.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yikuyi.party.facility.model.Facility;



@Mapper
public interface FacilityDao {
	
	/**
	 * 根据(供应商ID)partId获取仓库列表
	 * @param id
	 * @return
	 * @since 2016年12月12日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public List<Facility> getFacilityList(String id);
	
	
	/**
	 * 获取仓库为Y的信息
	 * @param id
	 * @return
	 * @since 2017年10月17日
	 * @author zr.chenxuemin@yikuyi.com
	 */
	public List<Facility> getFacilityFlagYList(String id);
	
	/**
	 * 根据仓库Id获取仓库列表
	 * @param ids
	 * @return
	 * @since 2016年12月17日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public List<Facility> getFacility(@Param("ids") List<String> ids);
	
	/**
	 * 添加仓库
	 * @param facility
	 * @return
	 * @since 2017年2月7日
	 * @author tb.chenxuemin@yikuyi.com
	 */
	public int add(Facility facility);
	
	/**
	 * 根据条件删除仓库信息
	 * @param facility
	 * @return
	 * @since 2017年2月7日
	 * @author tb.chenxuemin@yikuyi.com
	 */
	public int delete(Facility facility);
	
	/**
	 * 根据条件修改仓库信息
	 * @param facility
	 * @return
	 * @since 2017年2月7日
	 * @author tb.chenxuemin@yikuyi.com
	 */
	public int update(Facility facility);
	
	/**
	 * 根据仓库是否存在来判断插入或者更新
	 * @param facility
	 * @return
	 * @since 2017年10月18日
	 * @author tb.zouyuankai@yikuyi.com
	 */

	public void insertOrUpdate(Facility facility);
	
	

}