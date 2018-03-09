package com.yikuyi.party.dept.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.yikuyi.party.vo.RoleVo;

@Mapper
public interface RoleDao {
	
	/**
	 * 新增角色信息
	 * @param vo
	 * @since 2017年5月8日
	 * @author tb.yumu@yikuyi.com
	 */
	public void saveRoleType(RoleVo vo);
	
	/**
	 * 编辑角色信息
	 * @param vo
	 * @since 2017年5月8日
	 * @author tb.yumu@yikuyi.com
	 */
	public void updateRoleType(RoleVo vo);
	
	/**
	 * 保存角色权限关系,角色对菜单：一对多
	 * @param vo
	 * @since 2017年5月8日
	 * @author tb.yumu@yikuyi.com
	 */
	public void saveSecurityRolePermission(RoleVo vo);
	
	/**
	 * 保存角色权限关系,角色对菜单：多对一
	 * @param vo
	 * @since 2017年5月8日
	 * @author tb.yumu@yikuyi.com
	 */
	public void securityRolePermissionSave(RoleVo vo);
	
	/**
	 * 删除角色权限关系
	 * @param id
	 * @since 2017年5月8日
	 * @author tb.yumu@yikuyi.com
	 */
	public void roleDelete(@Param("id")String id);
	
	/**
	 * 根据菜单ID删除角色菜单关系
	 * @param id
	 * @since 2017年5月15日
	 * @author tb.yumu@yikuyi.com
	 */
	public void deleteRoleByMenuId(@Param("id")String id);
	
	/**
	 * 分页查询角色列表
	 * @param name
	 * @param deptId
	 * @param status
	 * @param rowBounds
	 * @return
	 * @since 2017年5月9日
	 * @author tb.yumu@yikuyi.com
	 */
	public List<RoleVo> roleList(@Param("name")String name,@Param("deptId")String deptId,@Param("status")String status , RowBounds rowBounds);
	
	public RoleVo roleDetail(@Param("id") String id);
	
	/**
	 * [后台]根据部门ID,查询对应部门下面的所有角色,并且查询出对应角色的菜单权限
	 * @param deptId
	 * @return
	 * @since 2017年5月11日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	public List<RoleVo> findRoleVoByDeptId(@Param("deptId") String deptId);

}
