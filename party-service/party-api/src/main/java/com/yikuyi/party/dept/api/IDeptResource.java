package com.yikuyi.party.dept.api;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.yikuyi.party.contact.vo.UserManage;
import com.yikuyi.party.person.model.Person;
import com.yikuyi.party.vo.DeptVo;
import com.yikuyi.party.vo.RoleVo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 运营后台部门管理
 * @author tb.yumu@yikuyi.com
 * @version 1.0.0
 */
public interface IDeptResource {
	
	/**
	 * 部门树形结构查询
	 * @return
	 * @since 2017年8月23日
	 */
	@ApiOperation(value = "部门树形结构查询", notes = "部门树形结构查询", response = List.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
			})
	public List<DeptVo> deptList();
	
	
	/**
	 * 根据部门ID查询子部门
	 * @param parentId
	 * @return
	 * @since 2017年8月23日
	 */
	@ApiOperation(value = "根据部门ID查询子部门", notes = "根据部门ID查询子部门", response = List.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
			})
	public List<DeptVo> findSonDeptList(@ApiParam(value = "父级部门对应的ID", required = true) String parentId);
	
	
	/**
	 * 查询部门详情
	 * @param partyId
	 * @return
	 * @since 2017年8月23日
	 */
	@ApiOperation(value = "查询部门详情", notes = "查询部门详情", response = DeptVo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
			})
	public DeptVo deptDetail(@ApiParam(value = "当前部门ID", required = true) String partyId);
	
	
	/**
	 * 新增部门信息
	 * @param vo
	 * @since 2017年8月23日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "新增部门信息", notes = "新增部门信息", response = Void.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
			})
	public void deptSave(@ApiParam(value = "新增部门的vo对象", required = true) DeptVo vo);
	
	
	/**
	 * 编辑部门信息
	 * @param vo
	 * @since 2017年8月23日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "编辑部门信息", notes = "编辑部门信息", response = Void.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
			})
	public void deptUpdate(@ApiParam(value = "编辑部门的vo对象", required = true) DeptVo vo);
	
	
	/**
	 * 新增角色信息
	 * @param roleVo
	 * @since 2017年8月23日
	 */
	@ApiOperation(value = "新增角色信息", notes = "新增角色信息", response = Void.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
			})
	public void roleSave(@ApiParam(value = "新增角色信息vo对象", required = true) RoleVo roleVo);
	
	
	/**
	 * 编辑角色信息
	 * @param roleVo
	 * @since 2017年8月23日
	 */
	@ApiOperation(value = "编辑角色信息", notes = "编辑角色信息", response = Void.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
			})
	public void roleUpdate(@ApiParam(value = "编辑角色信息vo对象", required = true) RoleVo roleVo);
	
	
	/**
	 * 查询角色列表接口
	 * @param name
	 * @param deptId
	 * @param status
	 * @param page
	 * @param size
	 * @return
	 * @since 2017年8月23日
	 */
	@ApiOperation(value = "查询角色列表接口", notes = "查询角色列表接口", response = PageInfo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
			})
	public PageInfo<RoleVo> roleList(@ApiParam(value = "角色名称", required = false)String name,
			                     @ApiParam(value = "所属部门", required = false)String deptId,
			                     @ApiParam(value = "状态", required = false)String status,
			                     @ApiParam(value = "当前页数", required = false)int page,
			                     @ApiParam(value = "每页显示条数", required = false)int size
			                     );
	
	/**
	 * 查询角色详情接口
	 * @param id
	 * @return
	 * @since 2017年8月23日
	 */
	@ApiOperation(value = "查询角色详情接口", notes = "查询角色详情接口", response = RoleVo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
			})
	public RoleVo roleDetail(@ApiParam(value = "角色ID", required = false)String id);
	
	/**
	 * 根据部门ID查询部门下用户接口
	 * @param deptId
	 * @param page
	 * @param size
	 * @return
	 * @since 2017年8月23日
	 */
	@ApiOperation(value = "根据部门ID查询部门下用户接口", notes = "根据部门ID查询部门下用户接口", response = UserManage.class, responseContainer="List")
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
			})
	public PageInfo<UserManage> findCustomerByDeptId(@ApiParam(value = "部门ID", required = true)String deptId,
									@ApiParam(value="page",required=false,defaultValue="1") int page,
									@ApiParam(value="size",required=false,defaultValue="10") int size);
	
	
	/**
	 * [后台]根据部门ID,查询对应部门下面的所有角色,并且查询出对应角色的菜单权限
	 * @param deptId
	 * @return
	 * @since 2017年8月23日
	 */
	@ApiOperation(value = "[后台]根据部门ID,查询对应部门下面的所有角色,并且查询出对应角色的菜单权限", notes = "[后台]根据部门ID,查询对应部门下面的所有角色,并且查询出对应角色的菜单权限", response = RoleVo.class, responseContainer="List")
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
			})
	public List<RoleVo> findRoleVoByDeptId(@ApiParam(value = "部门ID", required = true)String deptId);
	
	
	/**
	 * 据角色名称找用户信息list   redis监控用
	 * @param roleName
	 * @return
	 * @since 2017年8月23日
	 */
	@ApiOperation(value = "据角色名称找用户信息list   redis监控用", notes = "据角色名称找用户信息list   redis监控用", response = Person.class, responseContainer="List")
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
			})
	public List<Person> findPersonByRoleName(@ApiParam(value = "角色名称", required = true)String roleName);

}
