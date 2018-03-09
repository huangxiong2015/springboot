package com.yikuyi.party.resource;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import com.yikuyi.party.facility.model.Facility;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * @author huangqingfeng
 * 物流快递鸟接口调用定义
 */
@Headers({"Content-Type: application/json;charset=UTF-8"})
public interface FacilityClient {


	
	/**
	 * 保存供应商的仓库信息
	 * @param facility
	 * @param DataSign
	 * @return
	 * @since 2017年6月29日
	 * @author zr.zhanghua@yikuyi.com
	 */
	@RequestLine("POST /v1/facility")
	@Headers({"Authorization: Basic {authToken}"})
	public Facility addFacility(@RequestBody Facility facility,@Param("authToken") String authToken);
		
	/**
	 * 根据仓库ID修改仓库信息
	 * @param id
	 * @param facility
	 * @param DataSign
	 * @return
	 * @since 2017年6月29日
	 * @author zr.zhanghua@yikuyi.com
	 */
	@RequestLine("PUT /v1/facility/{id}")
	@Headers({"Authorization: Basic {authToken}"})
	public boolean upFacilityById(@Param("id") String id,@RequestBody Facility facility,@Param("authToken") String authToken);
	
	/**
	 * 根据供应商ID查找对应的仓库信息
	 * @param id
	 * @param DataSign
	 * @return
	 * @since 2017年6月29日
	 * @author zr.zhanghua@yikuyi.com
	 */
	@RequestLine("GET /v1/facility?id={id}")
	public List<Facility> getFacilityList(@Param("id") String id);
		
	/**
	 * 根据partyID删除供应商的仓库
	 * @param ownerPartyId
	 * @param DataSign
	 * @return
	 * @since 2017年6月29日
	 * @author zr.zhanghua@yikuyi.com
	 */
	@RequestLine("DELETE /v1/facility/ownerParty/{ownerPartyId}?ownerPartyId={ownerPartyId}")
	@Headers({"Authorization: Basic {authToken}"})
	public boolean delFacilityByPartyId(@Param("ownerPartyId") String ownerPartyId,@Param("authToken") String authToken);
		
	
	/**
	 * 根据仓库id删除仓库
	 * @param id
	 * @param facility
	 * @param DataSign
	 * @return
	 * @since 2017年6月29日
	 * @author zr.zhanghua@yikuyi.com
	 */
	@RequestLine("DELETE /v1/facility/{id}?id={id}")
	@Headers({"Authorization: Basic {authToken}"})
	public boolean delFacilityById(@Param("id") String id,@Param("authToken") String authToken);
	
	/**
	 * 根据Id获取仓库列表
	 * @param ids
	 * @return
	 * @since 2017年6月29日
	 * @author zr.zhanghua@yikuyi.com
	 */
	@RequestLine("POST /v1/facility/batch")
	public List<Facility> getFacility(@RequestBody List<String> ids);
	
	/**
	 * 添加仓库信息
	 */
	@RequestLine("POST /v1/facility/leadMaterial")	
	public Facility addFacilityFromLeadMaterial(@RequestBody Facility facility); 
	
	
}
