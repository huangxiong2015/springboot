package com.yikuyi.party.partygroup.api;

import java.util.List;

import com.yikuyi.party.contact.vo.MsgResultVo;
import com.yikuyi.party.group.vo.PartyGroupVo;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.vo.PartyVo;
import com.ykyframework.exception.BusinessException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 定义地址的相关接口
 * @author guowenyao
 *
 */
public interface IPartyGroupResource {
	

	/**
	 * 根据partId获取供应商列表
	 * @param addressType
	 * @return
	 * @since 2016年12月12日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@ApiOperation(value = "根据条件查找会员相关信息列表", notes = "根据条件查找会员相关信息列表", response = Party.class, responseContainer="List")
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})                           
	public  List<PartyVo> getPartyGroupList(@ApiParam(value = "vo", required = false)PartyGroupVo vo);
	
	/**
	 * 根据组织名称查找组织信息
	 * @author 张伟
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "根据组织名称找记录", notes = "根据组织名称找记录", response = Party.class, responseContainer="Party")
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void  findPartyGroupByName(@ApiParam(value = "groupName", required = false)String groupName)throws BusinessException;
	
	
	/**
	 * 跟人不相关的信息
	 * @param vo
	 * @return
	 * @since 2017年8月15日
	 * @author 
	 */
	@ApiOperation(value = "跟人不相关的信息", notes = "跟人不相关的信息", response = Party.class, responseContainer="Party")
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public List<PartyVo>  getAllPartyGroupList(@ApiParam(value = "vo", required = false)PartyGroupVo vo);
	
	
	/**
	 * 
	 * @param vo
	 * @return
	 * @since 2017年8月15日
	 * @author 
	 */
	@ApiOperation(value = "供应商信息仓库只查状态为Y的", notes = "供应商信息仓库只查状态为Y的", response = Party.class, responseContainer="Party")
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public List<PartyVo>  partyGroups(@ApiParam(value = "vo", required = false)PartyGroupVo vo);
	
	
	
	
	/**
	 * 购物车下订单权限控制
	 * @param partyId
	 * @return
	 * @since 2017年8月15日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "购物车下订单权限控制", notes = "购物车下订单权限控制", response = Party.class, responseContainer="Party")
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public MsgResultVo  orderPermissions(@ApiParam(value = "partyId", required = true)String partyId);
	
	/**
	 * 新增物流公司信息
	 * @param vo
	 * @since 2017年5月3日
	 * @author tb.yumu@yikuyi.com
	 */
	@ApiOperation(value = "新增物流公司信息", notes = "新增物流公司信息", response = Void.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void insertLogisticsCompany(@ApiParam(value = "vo", required = false)PartyGroupVo vo);
	
	/**
	 * 修改物流公司信息
	 * @param vo
	 * @since 2017年5月3日
	 * @author tb.yumu@yikuyi.com
	 */
	@ApiOperation(value = "修改物流公司信息", notes = "修改物流公司信息", response = Void.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void updateLogisticsCompany(@ApiParam(value = "vo", required = false)PartyGroupVo vo);
	
	/**
	 * 启用/停用物流公司
	 * @param vo
	 * @since 2017年5月3日
	 * @author tb.yumu@yikuyi.com
	 */
	@ApiOperation(value = "启用/停用物流公司", notes = "启用/停用物流公司", response = Void.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void changeLogisticsCompanyStatus(@ApiParam(value = "vo", required = false)PartyGroupVo vo);
	
}