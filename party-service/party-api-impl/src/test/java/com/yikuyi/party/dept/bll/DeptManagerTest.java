package com.yikuyi.party.dept.bll;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.framework.springboot.model.LoginUser;
import com.framework.springboot.utils.AuthorizationUtil;
import com.framewrok.springboot.web.LoginUserInjectionInterceptor;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.yikuyi.basedata.ShipmentClientBuilder;
import com.yikuyi.party.person.model.Person;
import com.yikuyi.party.vo.DeptVo;
import com.yikuyi.party.vo.RoleVo;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class })
@Transactional
@Rollback
public class DeptManagerTest {

	@Autowired
	private DeptManager deptManager;
	@SpyBean
	private ShipmentClientBuilder shipmentClientBuilder;

	@SpyBean
	private AuthorizationUtil authorizationUtil;

	public DeptManagerTest() {
		MockitoAnnotations.initMocks(this);
	}

	@Before
	public void init() {
		LoginUser loginUser = new LoginUser("9999999901", "admin", "9999999901",
				Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
		;
		RequestContextHolder.currentRequestAttributes().setAttribute(LoginUserInjectionInterceptor.LOGIN_USER_KEY,
				loginUser, RequestAttributes.SCOPE_REQUEST);
	}

	/**
	 * 获取部门列表，单元测试
	 * 
	 * @since 2017年5月23日
	 * @author tb.yumu@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "dept_sampledata.xml")
	public void deptListTest() {
		deptManager.deptList("1001");
	}

	/**
	 * 新增部门，单元测试
	 * 
	 * @since 2017年5月23日
	 * @author tb.yumu@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "dept_sampledata.xml")
	public void deptSaveTest() {
		DeptVo vo = new DeptVo();
		vo.setName("认证部");
		vo.setParentId("99999999");
		deptManager.deptSave(vo);
	}

	/**
	 * 新增部门，单元测试
	 * 
	 * @since 2017年5月23日
	 * @author tb.yumu@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "dept_sampledata.xml")
	public void deptSaveTest1() {
		DeptVo vo = new DeptVo();
		vo.setName("测试组");
		vo.setParentId("9999999902");
		deptManager.deptSave(vo);
	}

	/**
	 * 根据部门ID查询子部门,单元测试
	 * 
	 * @since 2017年5月23日
	 * @author tb.yumu@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "dept_sampledata.xml")
	public void findSonDeptListTest() {
		deptManager.findSonDeptList("9999999901");
	}

	/**
	 * 根据部门查询子部门单元测试
	 * 
	 * @since 2017年5月23日
	 * @author tb.yumu@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "dept_sampledata.xml")
	public void findSonDeptListTest1() {
		deptManager.findSonDeptList("99999999");
	}

	/**
	 * 部门详情，单元测试
	 * 
	 * @since 2017年5月23日
	 * @author tb.yumu@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "dept_sampledata.xml")
	public void deptDetailTest() {
		deptManager.deptDetail("9999999901");
	}

	/**
	 * 编辑部门，单元测试
	 * 
	 * @since 2017年5月23日
	 * @author tb.yumu@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "dept_sampledata.xml")
	public void deptUpdateTest() {
		DeptVo vo = new DeptVo();
		vo.setId("9999999901");
		vo.setName("行政部");
		deptManager.deptUpdate(vo);
	}

	/**
	 * 新增角色，单元测试
	 * 
	 * @since 2017年5月23日
	 * @author tb.yumu@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "dept_sampledata.xml")
	public void roleSaveTest() {
		RoleVo vo = new RoleVo();
		vo.setName("CMO");
		vo.setDeptId("9999999901");
		DeptVo deptVo = new DeptVo();
		deptVo.setId("99999999901");
		DeptVo deptVo1 = new DeptVo();
		deptVo1.setId("99999999902");
		List<DeptVo> list = new ArrayList<>();
		list.add(deptVo);
		list.add(deptVo1);
		vo.setDeptVoList(list);
		deptManager.roleSave(vo);
	}

	/**
	 * 编辑角色，单元测试
	 * 
	 * @since 2017年5月23日
	 * @author tb.yumu@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "dept_sampledata.xml")
	public void roleUpdateTest() {
		RoleVo vo = new RoleVo();
		vo.setId("8888888801");
		vo.setName("CMO");
		DeptVo deptVo = new DeptVo();
		deptVo.setId("99999999901");
		DeptVo deptVo1 = new DeptVo();
		deptVo1.setId("99999999902");
		List<DeptVo> list = new ArrayList<>();
		list.add(deptVo);
		list.add(deptVo1);
		vo.setDeptVoList(list);
		deptManager.roleUpdate(vo);
	}

	/**
	 * 角色列表，单元测试
	 * 
	 * @since 2017年5月23日
	 * @author tb.yumu@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "dept_sampledata.xml")
	public void roleListTest() {
		deptManager.roleList(null, null, null, 1, 10);
	}

	/**
	 * 角色详情，，单元测试
	 * 
	 * @since 2017年5月23日
	 * @author tb.yumu@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "dept_sampledata.xml")
	public void roleDetailTest() {
		deptManager.roleDetail("8888888801");
	}

	/**
	 * 角色详情，，单元测试
	 * 
	 * @since 2017年5月23日
	 * @author tb.yumu@yikuyi.com
	 */
	@Test
	public void findRoleVoByDeptIdTest() {
		deptManager.findRoleVoByDeptId("66");
	}

	/**
	 * 角色列表，单元测试
	 * 
	 * @since 2017年11月13日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "dept_sampledata.xml")
	public void findCustomerByDeptIdTest() {
		deptManager.findCustomerByDeptId("66", RowBounds.DEFAULT);
	}

	/**
	 * 角色列表，单元测试
	 * 
	 * @since 2017年11月13日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "dept_sampledata.xml")
	public void getPartyNameTest() {
		List<String> list = new ArrayList<>();
		list.add("1");
		deptManager.getPartyName(Arrays.asList("1"));
	}

	/**
	 * 角色列表，单元测试
	 * 
	 * @since 2017年11月13日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = "dept_sampledata.xml")
	public void findPersonByRoleNameTest() {
		List<String> list = new ArrayList<>();
		list.add("1");
		List<Person> pList = deptManager.findPersonByRoleName("COO");
		Assert.assertEquals(pList.get(0).getTel(), "15888889999");
	}

}