package com.yikuyi.party.carrier;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.yikuyi.party.model.Party.PartyStatus;
import com.yikuyi.party.vo.PartyCarrierVo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 物流信息
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
public interface ICarrierResource {
	

	
	/**
	 * 物流信息查询
	 * @param vo
	 * @return
	 * @since 2017年7月25日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "物流信息查询", notes = "物流信息查询", response = List.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public PageInfo<PartyCarrierVo>  getPartyCarrierList(@ApiParam(value = "物流公司名称")String groupName,
			@ApiParam(value = "状态")PartyStatus partyStatus,
			@ApiParam(value = "创建时间开始")String createDateStart,
			@ApiParam(value = "创建时间开始")String createDateEnd,
			@ApiParam(value = "最后更改时间开始")String lastUpdateDateStart,
			@ApiParam(value = "最后更改时间开始")String lastUpdateDateEnd,
			@ApiParam(value="page",required=false,defaultValue="1") int page,
			@ApiParam(value="size",required=false,defaultValue="10") int size);
	
	
	
	/**
	 * 根据partyId查询相应的物流信息
	 * @param partyId
	 * @return
	 * @since 2017年7月26日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "物流信息查询", notes = "物流信息查询", response = PartyCarrierVo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public PartyCarrierVo getPartyCarrierVoInfo(@ApiParam(value = "用户partyId", required = true) String partyId);
	
	
	/**
	 * 物流公司信息-启用/停用
	 * @param partyId
	 * @param statusId
	 * @return
	 * @since 2017年7月26日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "物流公司信息-启用/停用", notes = "物流公司信息-启用/停用", response = String.class)
	public void updateStateId(@ApiParam(value = "用户partyId", required = true) String partyId,
			@ApiParam(value = "vo", required = false) PartyCarrierVo partyCarrierVo);
}
