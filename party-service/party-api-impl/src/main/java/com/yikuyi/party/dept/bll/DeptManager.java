package com.yikuyi.party.dept.bll;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framework.springboot.model.LoginUser;
import com.framework.springboot.utils.AuthorizationUtil;
import com.framewrok.springboot.web.RequestHelper;
import com.github.pagehelper.PageInfo;
import com.yikuyi.basedata.ShipmentClientBuilder;
import com.yikuyi.party.contact.vo.UserManage;
import com.yikuyi.party.dept.dao.RoleDao;
import com.yikuyi.party.group.model.PartyGroup;
import com.yikuyi.party.group.model.PartyGroup.ActiveStatus;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.model.Party.PartyStatus;
import com.yikuyi.party.model.Party.PartyType;
import com.yikuyi.party.model.PartyRelationship;
import com.yikuyi.party.model.PartyRelationship.PartyRelationshipStatus;
import com.yikuyi.party.model.PartyRelationship.PartyRelationshipTypeEnum;
import com.yikuyi.party.party.dao.PartyDao;
import com.yikuyi.party.partygroup.dao.PartyGroupDao;
import com.yikuyi.party.partygroup.dao.PartyRelationshipDao;
import com.yikuyi.party.person.model.Person;
import com.yikuyi.party.role.model.RoleTypeEnum;
import com.yikuyi.party.vo.DeptVo;
import com.yikuyi.party.vo.RoleVo;
import com.ykyframework.api.persist.IdGen;

@Service
@Transactional
public class DeptManager {

	private static final Logger logger = LoggerFactory.getLogger(DeptManager.class);

	@Autowired
	private PartyDao partyDao;

	@Autowired
	private PartyGroupDao partyGroupDao;

	@Autowired
	private PartyRelationshipDao partyRelationshipDao;

	@Autowired
	private RoleDao roleDao;

	private static final String PARENT_ID = "99999999";

	@Value("${api.basedata.serverUrlPrefix}")
	private String basedataServerUrl;

	@Autowired
	private ShipmentClientBuilder shipmentClientBuilder;

	@Autowired
	private AuthorizationUtil authorizationUtil;

	/**
	 * 查询树形部门结构
	 * 
	 * @return
	 * @since 2017年5月5日
	 * @author tb.yumu@yikuyi.com
	 */
	public List<DeptVo> deptList(String partyId) {
		return partyGroupDao.deptList(partyId);
	}

	/**
	 * 获取子部门
	 * 
	 * @param id
	 * @return
	 * @since 2017年5月5日
	 * @author tb.yumu@yikuyi.com
	 */
	public List<DeptVo> findSonDeptList(String id) {
		PartyRelationshipTypeEnum relactionName;
		if (id.equals(PARENT_ID)) {
			relactionName = PartyRelationshipTypeEnum.DEPT_CORPORATION_REL;
		} else {
			relactionName = PartyRelationshipTypeEnum.DEPT_DEPT_REL;
		}
		return partyGroupDao.findSonDeptList(id, relactionName);
	}

	/**
	 * 获取部门详情
	 * 
	 * @param partyId
	 * @return
	 * @since 2017年5月5日
	 * @author tb.yumu@yikuyi.com
	 */
	public DeptVo deptDetail(String partyId) {
		return partyGroupDao.deptDetail(partyId);
	}

	/**
	 * 保存 部门信息
	 * 
	 * @param vo
	 * @since 2017年5月5日
	 * @author tb.yumu@yikuyi.com
	 */
	public void deptSave(DeptVo vo) {
		String id = String.valueOf(IdGen.getInstance().nextId());
		String userId = RequestHelper.getLoginUserId();
		String account = RequestHelper.getLoginUser().getUsername();
		Date date = new Date();
		Party party = new Party();
		party.setId(id);
		party.setPartyType(PartyType.DEPARTMENT);
		party.setPartyStatus(PartyStatus.PARTY_ENABLED);
		party.setIsSystem("N");
		party.setCreator(userId);
		party.setCreatedDate(date);
		party.setLastUpdateUser(userId);
		party.setLastUpdateDate(date);
		partyDao.insert(party);

		PartyGroup group = new PartyGroup();
		group.setGroupName(vo.getName());
		group.setActiveStatus(ActiveStatus.PARTY_VERIFIED);
		group.setDescription(account);
		group.setCreator(userId);
		group.setCreatedDate(date);
		group.setLastUpdateUser(userId);
		group.setLastUpdateDate(date);
		party.setPartyGroup(group);
		partyGroupDao.insert(party);

		PartyRelationship relationship = null;
		
		// 如果父级id是99999999，则代表是顶级部门，否则为下级部门
		if (vo.getParentId().equals(PARENT_ID)) {
			relationship = PartyRelationship.build(PartyRelationshipTypeEnum.DEPT_CORPORATION_REL);
		} else {
			relationship = PartyRelationship.build(PartyRelationshipTypeEnum.DEPT_DEPT_REL);
		}
		relationship.setPartyIdFrom(id);
		relationship.setPartyIdTo(vo.getParentId());
		relationship.setFromDate(date);
		relationship.setCreator(userId);
		relationship.setCreatedDate(date);
		relationship.setLastUpdateUser(userId);
		relationship.setLastUpdateDate(date);

		partyRelationshipDao.insert(relationship);
	}

	/**
	 * 编辑部门信息
	 * 
	 * @param vo
	 * @since 2017年5月5日
	 * @author tb.yumu@yikuyi.com
	 */
	public void deptUpdate(DeptVo vo) {
		Party party = new Party();
		party.setId(vo.getId());
		PartyGroup partyGroup = new PartyGroup();
		partyGroup.setGroupName(vo.getName());
		LoginUser user = RequestHelper.getLoginUser();
		partyGroup.setDescription(user.getUsername());
		partyGroup.setLastUpdateUser(user.getId());
		partyGroup.setLastUpdateDate(new Date());
		party.setPartyGroup(partyGroup);
		partyGroupDao.updatePartyGroup(party);
	}

	/**
	 * 新增角色信息
	 * 
	 * @param vo
	 * @since 2017年5月8日
	 * @author tb.yumu@yikuyi.com
	 */
	public void roleSave(RoleVo vo) {

		String id = String.valueOf(IdGen.getInstance().nextId());
		String userId = RequestHelper.getLoginUserId();
		String account = RequestHelper.getLoginUser().getUsername();
		Date date = new Date();
		vo.setId(id);
		vo.setAccount(account);
		vo.setCreator(userId);
		vo.setCreatedDate(date);
		vo.setLastUpdateUser(userId);
		vo.setLastUpdateDate(date);
		vo.setParentTypeId(RoleTypeEnum.OPERATION_FUNCTION.name());
		roleDao.saveRoleType(vo);
		PartyRelationship relationship;
		for (DeptVo deptVo : vo.getDeptVoList()) {
			relationship = PartyRelationship.build(PartyRelationshipTypeEnum.ROLE_DEPT_REL);
			relationship.setPartyIdFrom(id);
			relationship.setPartyIdTo(deptVo.getId());
			relationship.setFromDate(date);
			relationship.setCreator(userId);
			relationship.setCreatedDate(date);
			relationship.setLastUpdateUser(userId);
			relationship.setLastUpdateDate(date);
			partyRelationshipDao.insert(relationship);
		}

	}

	/**
	 * 编辑角色信息
	 * 
	 * @param vo
	 * @since 2017年5月8日
	 * @author tb.yumu@yikuyi.com
	 */
	public void roleUpdate(RoleVo vo) {
		String userId = RequestHelper.getLoginUserId();
		String account = RequestHelper.getLoginUser().getUsername();
		Date date = new Date();
		vo.setAccount(account);
		vo.setLastUpdateUser(userId);
		vo.setLastUpdateDate(date);
		roleDao.updateRoleType(vo);
		PartyRelationship relationship = PartyRelationship.build(PartyRelationshipTypeEnum.ROLE_DEPT_REL);
		// 将之前的部门关系过期掉，然后创建新的部门关系
		relationship.setPartyIdFrom(vo.getId());
		relationship.setThruDate(new Date());
		relationship.setStatusId(PartyRelationshipStatus.DISABLED);
		partyRelationshipDao.updateRelationShip(relationship);

		// 重新创建角色与部门的关系
		String roleId = vo.getId();
		PartyRelationship ship;
		for (DeptVo deptVo : vo.getDeptVoList()) {
			ship = PartyRelationship.build(PartyRelationshipTypeEnum.ROLE_DEPT_REL);
			ship.setPartyIdFrom(roleId);
			ship.setPartyIdTo(deptVo.getId());
			ship.setFromDate(date);
			ship.setCreator(userId);
			ship.setCreatedDate(date);
			ship.setLastUpdateUser(userId);
			ship.setLastUpdateDate(date);
			partyRelationshipDao.insert(ship);
		}
	}

	public PageInfo<RoleVo> roleList(String name, String deptId, String status, int page, int size) {
		RowBounds rowBounds = new RowBounds((page - 1) * size, size);
		List<RoleVo> list = roleDao.roleList(name, deptId, status, rowBounds);
		PageInfo<RoleVo> info = new PageInfo<>(list);
		info.setPageSize(size);
		info.setPageNum(page);
		return info;
	}

	public RoleVo roleDetail(String id) {
		return roleDao.roleDetail(id);
	}

	public PageInfo<UserManage> findCustomerByDeptId(String deptId, RowBounds rowBounds) {

		String subDeptId = partyGroupDao.findSubDeptId(deptId);
		String[] arr = subDeptId.split(",");

		PageInfo<UserManage> pageInfo = new PageInfo<>(partyGroupDao.findCustomerByDeptId(arr, rowBounds));

		if (CollectionUtils.isEmpty(pageInfo.getList())) {
			return pageInfo;
		}
		for (UserManage ma : pageInfo.getList()) {
			UserManage userManage = partyGroupDao.findRoleNameByPartyId(ma.getPartyId());
			if (null != userManage) {
				ma.setRoleTypeNameList(userManage.getRoleTypeNameList());
			}
			if (CollectionUtils.isNotEmpty(ma.getRoleTypeNameList())) {
				StringBuilder stringB = new StringBuilder();
				for (String name : ma.getRoleTypeNameList()) {
					stringB.append(name).append(",");
				}
				ma.setRoleTypeName(stringB.toString().substring(0, stringB.toString().length() - 1));
				ma.setRoleTypeNameList(null);
			} else {
				ma.setRoleTypeName(null);
			}

		}

		return pageInfo;
	}

	public List<RoleVo> findRoleVoByDeptId(String deptId) {
		List<RoleVo> list = roleDao.findRoleVoByDeptId(deptId);
		if (CollectionUtils.isNotEmpty(list)) {
			for (RoleVo vo : list) {
				if (CollectionUtils.isNotEmpty(vo.getMenuList())) {
					List<String> name = getPartyName(vo.getMenuList());
					vo.setMenuNameList(name);
				}
			}

		}
		return list;
	}

	public List<String> getPartyName(List<String> list) {
		StringBuilder men = new StringBuilder();
		if (list != null) {
			for (String s : list) {
				men.append(s + ",");
			}
		}
		try {
			return shipmentClientBuilder.menuResource().findNameById(men.toString(),
					authorizationUtil.getLoginAuthorization());
		} catch (Exception e) {
			logger.error("菜单接口异常：{},菜单接口:{}", e, men.toString());
			return Collections.emptyList();
		}
	}

	public List<Person> findPersonByRoleName(String roleName) {
		if (StringUtils.isBlank(roleName)) {
			return Collections.emptyList();
		}
		return partyGroupDao.findPersonByRoleName(roleName);
	}
}
