package com.yikuyi.party.dept.api.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.framewrok.springboot.web.RequestHelper;
import com.github.pagehelper.PageInfo;
import com.yikuyi.party.contact.vo.UserManage;
import com.yikuyi.party.dept.api.IDeptResource;
import com.yikuyi.party.dept.bll.DeptManager;
import com.yikuyi.party.person.model.Person;
import com.yikuyi.party.vo.DeptVo;
import com.yikuyi.party.vo.RoleVo;

@RestController
@RequestMapping("v1/dept")
public class DeptResource implements IDeptResource {
	
	@Autowired
	private DeptManager deptManager;

	/**
	 * 新增部门信息
	 */
	@Override
	@RequestMapping(method = RequestMethod.POST)
	public void deptSave(@RequestBody DeptVo vo) {
		deptManager.deptSave(vo);
		
	}

	/**
	 * 编辑部门信息
	 */
	@Override
	@RequestMapping(method = RequestMethod.PUT)
	public void deptUpdate(@RequestBody DeptVo vo) {
		deptManager.deptUpdate(vo);
	}


	/**
	 * 查询部门列表
	 */
	@Override
	@RequestMapping(value = "/list" ,method = RequestMethod.GET)
	public List<DeptVo> deptList() {
		
		return deptManager.deptList(RequestHelper.getLoginUserId());
	}

	/**
	 * 查询部门下的子部门
	 */
	@Override
	@RequestMapping(value = "/sonlist" ,method = RequestMethod.GET)
	public List<DeptVo> findSonDeptList(@RequestParam(value="parentId",required = true)String parentId) {
		return deptManager.findSonDeptList(parentId);
	}

	/**
	 * 查询部门详情
	 */
	@Override
	@RequestMapping(value = "/detail" ,method = RequestMethod.GET)
	public DeptVo deptDetail(@RequestParam(value="partyId",required = true)String partyId) {
		return deptManager.deptDetail(partyId);
	}

	/**
	 * 新增角色信息
	 */
	@Override
	@RequestMapping(value="/role" , method = RequestMethod.POST)
	public void roleSave(@RequestBody RoleVo roleVo) {
		deptManager.roleSave(roleVo);
	}

	/**
	 * 编辑角色信息
	 */
	@Override
	@RequestMapping(value="/role" , method = RequestMethod.PUT)
	public void roleUpdate(@RequestBody RoleVo roleVo) {
		deptManager.roleUpdate(roleVo);
		
	}

	/**
	 * 查询角色列表
	 */
	@Override
	@RequestMapping(value="/role/list" , method = RequestMethod.GET)
	public PageInfo<RoleVo> roleList(@RequestParam(value = "name" ,required = false) String name, 
									@RequestParam(value = "deptId" ,required = false)String deptId, 
									@RequestParam(value = "status" ,required = false)String status, 
									@RequestParam(value = "page" ,defaultValue = "1")int page, 
									@RequestParam(value = "size" ,defaultValue = "10")int size) {
		return deptManager.roleList(name, deptId, status, page, size);
	}

	
	/**
	 * 根据id查询相应的角色详情
	 */
	@Override
	@RequestMapping(value="/role/detail" , method = RequestMethod.GET)
	public RoleVo roleDetail(@RequestParam(value="id" , required = true) String id) {
		return deptManager.roleDetail(id);
	}
	
	/**
	 * 根据部门ID查询部门下用户接口
	 */
	@Override
	@RequestMapping(value="/findCustomerByDeptId" , method = RequestMethod.GET)
	public PageInfo<UserManage> findCustomerByDeptId(@RequestParam(value="deptId" , required = true) String deptId,
					@RequestParam(value = "page" ,defaultValue = "1")int page, 
					@RequestParam(value = "size" ,defaultValue = "10")int size) {
		RowBounds rowBounds = new RowBounds((page-1)*size, size);		
		return deptManager.findCustomerByDeptId(deptId,rowBounds);
	}
	
	/**
	 *[后台]根据部门ID,查询对应部门下面的所有角色,并且查询出对应角色的菜单权限
	 */
	@Override
	@RequestMapping(value="/findRoleVoByDeptId" , method = RequestMethod.GET)
	public List<RoleVo> findRoleVoByDeptId(@RequestParam(value="deptId" , required = true) String deptId) {
		return deptManager.findRoleVoByDeptId(deptId);
	}
	
	/**
	 * 根据角色名称找用户信息list   redis监控用
	 * @param roleName
	 * @return
	 * @since 2017年6月15日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/findPersonByRoleName" , method = RequestMethod.GET)
	public List<Person> findPersonByRoleName(@RequestParam(value="roleName" , required = true) String roleName) {
		return deptManager.findPersonByRoleName(roleName);
	}
	

}
