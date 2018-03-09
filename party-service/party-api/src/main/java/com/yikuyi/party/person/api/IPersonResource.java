package com.yikuyi.party.person.api;



import java.util.List;

import com.github.pagehelper.PageInfo;
import com.yikuyi.party.contact.vo.UserExtendVo;
import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.party.credit.model.PartyCredit;
import com.yikuyi.party.credit.vo.PartyCreditVo;
import com.yikuyi.party.person.model.Person;
import com.yikuyi.party.vendor.vo.Vendor.Currency;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 定义地址的相关接口
 * @author zr.aoxianbing@yikuyi.com
 *
 */
public interface IPersonResource {
	
	/**
	 * 查询账号列表
	 * @param name
	 * @param page
	 * @param size
	 * @return
	 * @since 2017年7月27日
	 */
	@ApiOperation(value = "查询账号列表", notes = "查询账号列表", response = PageInfo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = void.class) 
			})	
	public PageInfo<UserVo> search(@ApiParam(value = "name", required = false) String name,
			@ApiParam(value="page",required=false,defaultValue="1") int page,
			@ApiParam(value="size",required=false,defaultValue="10") int size);
	
	
	/**
	 * 新增账号
	 * @param userVo
	 * @return
	 * @since 2017年7月27日
	 */
	@ApiOperation(value = "新增账号", notes = "新增账号", response = UserVo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = UserVo.class) 
			})
	public String save(@ApiParam(value = "userVo", required = true) UserVo userVo);
	
	
	/**
	 * 根据id查询账号
	 * @param id
	 * @return
	 * @since 2017年7月27日
	 */
	@ApiOperation(value = "根据id查询账号", notes = "根据id查询账号", response = UserVo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = void.class) 
			})
	public UserVo getPerson(@ApiParam(value = "id", required = true) String id);
	
	
	/**
	 * 修改账号
	 * @param userVo
	 * @return
	 * @since 2017年7月27日
	 */
	@ApiOperation(value = "修改账号", notes = "修改账号", response = String.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = String.class) 
			})
	public String update(@ApiParam(value = "userVo", required = true) UserVo userVo);
	
	
	/**
	 * 重置密码
	 * @param id
	 * @param passWord
	 * @return
	 * @since 2017年7月27日
	 */
	@ApiOperation(value = "重置密码", notes = "重置密码", response = String.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = String.class) 
			})
	public String updatePwd(@ApiParam(value = "id", required = true) String id,
			@ApiParam(value = "passWord", required = true) String passWord
			);
	
	
	/**
	 * 查询供应商可授权人
	 * @param partyId
	 * @return
	 * @since 2017年7月27日
	 */
	@ApiOperation(value = "查询供应商可授权人", notes = "查询供应商可授权人", response = List.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = String.class) 
			})
	public List<UserExtendVo> getReportsTo(@ApiParam(value = "partyId", required = true) String partyId);

	
	/**
	 * 查询角色下所有邮件集合
	 * @param roleType
	 * @return
	 * @since 2017年7月27日
	 */
	@ApiOperation(value = "查询角色下所有邮件集合", notes = "查询角色下所有邮件集合", response = List.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = List.class) 
			})
	List<Person> getEmailListByRoleType(String roleType);
	
	
	/**
	 * 根据partId获取用户信息
	 * @param ids
	 * @return
	 * @since 2017年7月27日
	 */
	@ApiOperation(value = "根据partId获取用户信息", notes = "根据partId获取用户信息", response = UserVo.class, responseContainer="List")
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public  List<UserVo> getPartyByIds(@ApiParam(value = "ids", required = false) List<String> ids);
	
	
	/**
	 * 新增部门下相关用户
	 * @param vo
	 * @since 2017年7月27日
	 */
	@ApiOperation(value = "新增部门下相关用户", notes = "新增部门下相关用户", response = UserVo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = void.class) 
			})
	public void addUser(@ApiParam(value = "新增用户UserVo", required = true) UserVo vo);
	
	
	/**
	 * 根据ID查询用户详情
	 * @param partyId
	 * @return
	 * @since 2017年7月27日
	 */
	@ApiOperation(value = "根据ID查询用户详情", notes = "根据ID查询用户详情", response = UserVo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = void.class) 
			})
	public UserVo getUserDetail(@ApiParam(value = "用户partyId", required = true)String partyId);
	
	
	/**
	 * 编辑用户信息
	 * @param vo
	 * @since 2017年7月27日
	 */
	@ApiOperation(value = "编辑用户信息", notes = "编辑用户信息", response = UserVo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = void.class) 
			})
	public void updateUser(@ApiParam(value = "编辑用户UserVo", required = true) UserVo vo);
	
	
	/**
	 * 根据partyId查询用户的账期信息(包括账期状态，如果账期开通，还包括账期金额)
	 * @param partyId  用户id
	 * @param currency 币种
	 * @return
	 * @since 2017年8月3日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "查询用户的账期信息", notes = "查询用户的账期信息", response = PartyCreditVo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public PartyCreditVo getPartyCreditInfo(@ApiParam(value = "用户id", required = true) String partyId,
			@ApiParam(value = "币种", required = false) Currency currency);
	
	
	/**
	 * 批量查询用户的账期信息
	 * @param partyMaps
	 * @return
	 * @since 2017年10月30日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "批量查询用户的账期信息", notes = "批量查询用户的账期信息", response = PartyCreditVo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public List<PartyCreditVo> getPartyCreditInfoList(@ApiParam(value = "list", required = true) String partyList);
	
	/**
	 * 根据partyId修改用户的账期信息
	 * @param partyId
	 * @since 2017年8月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "修改用户的账期信息", notes = "修改用户的账期信息", response = PartyCreditVo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void updatePartyCredit(@ApiParam(value = "用户id", required = true) PartyCredit partyCredit);
	
	
}
