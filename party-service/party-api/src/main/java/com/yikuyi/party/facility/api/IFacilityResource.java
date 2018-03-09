package com.yikuyi.party.facility.api;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yikuyi.party.facility.model.Facility;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 定义地址的相关接口
 * @author guowenyao
 *
 */
public interface IFacilityResource {
	

	/**
	 * 根据partId获取仓库列表
	 * @param addressType
	 * @return
	 * @since 2016年12月12日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@ApiOperation(value = "根据partId获取仓库列表", notes = "根据partId获取仓库列表", response = Facility.class, responseContainer="List")
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public  List<Facility> getFacilityList(@ApiParam(value = "id", required = true)String id);
	/**
	 * 根据Id获取仓库列表
	 * @param ids
	 * @return
	 * @since 2016年12月17日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@ApiOperation(value = "根据仓库Id获取仓库", notes = "根据仓库Id获取仓库", response = Facility.class, responseContainer="List")
	@RequestMapping(method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public  List<Facility> getFacility(@ApiParam(value = "ids", required = false) List<String> ids);
	
	
	
	/**
	 * 添加仓库信息
	 * @param Facility
	 * @return
	 * @since 2017年2月7日
	 * @author zr.chenxuemin@yikuyi.com
	 */
	@ApiOperation(value = "新增仓库", notes = "新增仓库", response = Facility.class)
	public  Facility addFacility(@ApiParam(value = "Facility实体对象", required = true) Facility facility);
	
	/**
	 * 添加仓库信息
	 * @param Facility
	 * @return
	 * @since 2017年2月7日
	 * @author zr.chenxuemin@yikuyi.com
	 */
	@ApiOperation(value = "新增仓库", notes = "新增仓库", response = Facility.class)
	public  Facility addFacilityFromLeadMaterial(@ApiParam(value = "Facility实体对象", required = true) Facility facility);
	
	/**
	 * 删除仓库信息
	 * @param id
	 * @return
	 * @since 2017年2月7日
	 * @author zr.chenxuemin@yikuyi.com
	 */
	@ApiOperation(value = "根据仓库id删除仓库", notes = "根据仓库Id删除仓库")
	public  boolean  delFacilityById(@ApiParam(value = "仓库id", required = true) String id);
	
	/**
	 * 删除仓库信息
	 * @param id
	 * @return
	 * @since 2017年2月7日
	 * @author zr.chenxuemin@yikuyi.com
	 */
	@ApiOperation(value = "根据ownerPartyId删除仓库", notes = "根据ownerPartyId删除仓库")
	public  boolean  delFacilityByPartyId(@ApiParam(value = "ownerPartyId", required = true) String ownerPartyId);

	/**
	 * 修改仓库信息
	 * @param id
	 * @return
	 * @since 2017年2月7日
	 * @author zr.chenxuemin@yikuyi.com
	 */
	public boolean upFacilityById(@ApiParam(value = "仓库id", required = true) String id,@ApiParam(value = "Facility实体对象", required = true) Facility facility);
}
